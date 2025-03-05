package org.universalcube.spring_starter_discord.discord.commands;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.universalcube.spring_starter_discord.discord.commands.annotations.SlashCommandComponent;
import org.universalcube.spring_starter_discord.discord.commands.interfaces.BotCommand;
import org.universalcube.spring_starter_discord.discord.commands.interfaces.BotCommandExecutor;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * Handles the registration, execution, and management of Discord slash commands for a bot.
 * This class integrates with the JDA library and uses Spring's ApplicationContext for
 * dependency injection and component scanning.
 * <p>
 * Slash commands are dynamically registered based on Spring beans annotated with
 * {@code @SlashCommandComponent}. When a slash command is triggered, the corresponding
 * executor is invoked to process the command.
 * <p>
 * The execution logic is managed using a thread pool to ensure efficient handling of
 * multiple commands concurrently without blocking the main bot thread. The thread pool size
 * and behavior are based on the number of processors available and a queue for managing
 * pending tasks.
 * <p>
 * This class also implements the {@code DisposableBean} interface to ensure the thread pool
 * is properly shut down during the application's termination process.
 * <p>
 * Key Responsibilities:
 * - Registers slash commands with Discord based on annotated Spring beans.
 * - Executes the appropriate command logic when a slash command interaction event occurs.
 * - Manages a thread pool for handling command execution asynchronously.
 * - Cleans up resources during application shutdown.
 */
public class SlashCommandBackend implements DisposableBean {
	private final static Logger log = LoggerFactory.getLogger(SlashCommandBackend.class);
	private final JDA jda;
	private final ApplicationContext applicationContext;
	private final ExecutorService executorService;
	private final Map<String, BotCommandExecutor> commands = new ConcurrentHashMap<>();

	/**
	 * Constructs a new instance of SlashCommandBackend.
	 * This constructor initializes the internal executor service with a thread pool
	 * sized based on the available processors and containing a bounded work queue.
	 * It also assigns the provided JDA and ApplicationContext instances to internal fields.
	 *
	 * @param jda                the JDA instance used for Discord bot operations.
	 * @param applicationContext the application context provided by the Spring framework,
	 *                           used for managing application beans and events.
	 */
	public SlashCommandBackend(JDA jda, ApplicationContext applicationContext) {
		this.jda = jda;
		this.applicationContext = applicationContext;
		int corePoolSize = Runtime.getRuntime().availableProcessors();
		int maximumPoolSize = corePoolSize * 2;
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(1000);
		RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
		this.executorService = new ThreadPoolExecutor(
				corePoolSize,
				maximumPoolSize,
				30L,
				TimeUnit.SECONDS,
				workQueue,
				rejectedExecutionHandler
		);
	}

	/**
	 * Initializes and registers all beans annotated with {@link SlashCommandComponent} as slash commands
	 * in the JDA instance. This method is executed automatically after the Spring context has been
	 * initialized, leveraging the {@code @PostConstruct} annotation.
	 * <p>
	 * For each bean annotated with {@link SlashCommandComponent}, the following operations are performed:
	 * 1. The command's name is extracted from the annotation's {@code name} parameter.
	 * 2. The command is stored in the internal map of commands, associating the name with the command instance.
	 * 3. The command's metadata and structure created using {@code createCommandData()} and
	 *    registered with Discord through the JDA instance using the {@code upsertCommand} method.
	 * <p>
	 * This dynamic registration allows slash commands to be easily added or extended by creating
	 * classes annotated with {@link SlashCommandComponent} that implement the {@link BotCommand} interface.
	 */
	@PostConstruct
	public void init() {
		Map<String, Object> beans = applicationContext.getBeansWithAnnotation(SlashCommandComponent.class);
		beans.forEach((name, bean) -> {
			if (bean instanceof BotCommand command) {
				SlashCommandComponent annotation = command.getClass().getAnnotation(SlashCommandComponent.class);
				String commandName = annotation.name();
				commands.put(commandName, command);
				jda.upsertCommand(command.createCommandData()).queue();
			}
		});
	}

	/**
	 * Handles the slash command interaction event triggered by a Discord user. This method processes
	 * the event by retrieving the appropriate command executor based on the command name and delegating
	 * the execution logic to it. The command is executed asynchronously using an internal executor
	 * service to avoid blocking the main thread.
	 * <p>
	 * If an error occurs during command processing, an ephemeral reply is sent to the user, and the
	 * error is logged for debugging purposes.
	 *
	 * @param event the {@link SlashCommandInteractionEvent} representing the slash command interaction
	 *              initiated by the user. Must not be null.
	 */
	@EventListener(SlashCommandInteractionEvent.class)
	public void onSlashCommand(SlashCommandInteractionEvent event) {
		executorService.submit(() -> {
			try {
				BotCommandExecutor executor = commands.get(event.getName());
				if (Objects.requireNonNull(executor) instanceof BotCommand command) {
					command.onExecute(Objects.requireNonNull(event.getMember()), event);
				}
			} catch (Exception e) {
				event.reply("An error occurred while executing the command").setEphemeral(true).queue();
				log.error("Error executing slash command", e);
			}
		});
	}

	/**
	 * Cleans up resources associated with the class by shutting down the internal executor service.
	 * Attempts to gracefully terminate all tasks within the executor service's queue within a
	 * specified timeout period. If the timeout expires before the service can terminate, a
	 * forced shutdown is initiated, interrupting any remaining tasks in progress.
	 * <p>
	 * If the thread is interrupted during the wait for termination, the method immediately invokes
	 * a forced shutdown and restores the thread's interrupted status.
	 */
	@Override
	public void destroy() {
		executorService.shutdown();
		try {
			if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
				executorService.shutdownNow();
			}
		} catch (InterruptedException e) {
			executorService.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}
}