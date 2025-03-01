package org.universalcube.spring_starter_discord.commands;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.universalcube.spring_starter_discord.commands.annotations.SlashCommandComponent;
import org.universalcube.spring_starter_discord.commands.interfaces.BotCommand;
import org.universalcube.spring_starter_discord.commands.interfaces.BotCommandExecutor;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

@Slf4j
public class SlashCommandBackend implements DisposableBean {
	private final JDA jda;
	private final ApplicationContext applicationContext;
	private final ExecutorService executorService;
	private final Map<String, BotCommandExecutor> commands = new ConcurrentHashMap<>();

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