package org.universalcube.spring_starter_discord.discord.modules;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.universalcube.spring_starter_discord.discord.modules.annotations.BotModuleComponent;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages the lifecycle and interactions of bot modules within a Discord bot system.
 * Provides functionality to add, retrieve, enable, disable, and sort bot modules.
 * Modules are managed in a concurrent map and can be dynamically added or reloaded.
 * This class is responsible for integrating bot modules with the JDA (Java Discord API)
 * and initializing them using the Spring ApplicationContext.
 */
public class ModulesManagerBackend {
	private final static Logger log = LoggerFactory.getLogger(ModulesManagerBackend.class);
	private final JDA jda;
	private final ApplicationContext applicationContext;
	private final Map<String, BotModule> modules = new ConcurrentHashMap<>();

	/**
	 * Creates an instance of the ModulesManagerBackend.
	 *
	 * @param jda                the JDA (Java Discord API) instance used to interact with Discord
	 * @param applicationContext the Spring ApplicationContext used for initializing and managing bot modules
	 */
	public ModulesManagerBackend(JDA jda, ApplicationContext applicationContext) {
		this.jda = jda;
		this.applicationContext = applicationContext;
	}

	/**
	 * Retrieves an unmodifiable view of all bot modules managed by the system.
	 * The returned map contains the module names as keys and their corresponding
	 * {@link BotModule} instances as values.
	 *
	 * @return an unmodifiable map of bot modules where keys are module names and values are {@link BotModule} objects
	 */
	public Map<String, BotModule> getModules() {
		return Collections.unmodifiableMap(modules);
	}

	/**
	 * Retrieves a map of all the enabled bot modules managed by the system.
	 * The returned map contains the module names as keys and their corresponding
	 * {@link BotModule} instances as values.
	 *
	 * @return a map of enabled bot modules where keys are module names and values are {@link BotModule} objects
	 */
	public Map<String, BotModule> getEnabledModules() {
		return modules.entrySet()
				.stream()
				.filter(entry -> entry.getValue().isEnabled())
				.collect(ConcurrentHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), Map::putAll);
	}

	/**
	 * Retrieves a map of all bot modules sorted by their names in natural order.
	 * The map's keys represent the module names, and the values represent their corresponding {@link BotModule} instances.
	 *
	 * @return a map of bot modules sorted by their names, where keys are module names and values are {@link BotModule} objects
	 */
	public Map<String, BotModule> getSortedModules() {
		return modules.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByKey())
				.collect(ConcurrentHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), Map::putAll);
	}

	/**
	 * Retrieves a specific bot module by its name.
	 *
	 * @param name the name of the bot module to retrieve
	 * @return the {@link BotModule} associated with the specified name, or null if no module is found with that name
	 */
	public BotModule getModule(String name) {
		return modules.get(name);
	}

	/**
	 * Adds a bot module to the system. If the module is not already present, it is added, and
	 * its event listener is registered with the JDA instance. If the added module is enabled,
	 * its {@code onEnable()} method is invoked.
	 *
	 * @param module the {@link BotModule} instance to add to the system
	 */
	public synchronized void addModule(BotModule module) {
		modules.putIfAbsent(module.getName(), module);
		jda.addEventListener(module);

		if (module.isEnabled()) {
			module.onEnable();
		}
	}

	/**
	 * Reloads all currently enabled bot modules managed by the system.
	 * <p>
	 * This method performs the following operations:
	 * - Logs the start of the module reloading process.
	 * - Iterates through all enabled modules and invokes their {@code onDisable()}
	 *   method to ensure proper cleanup and resource release for each module.
	 * - Clears the internal module storage, effectively removing all module references.
	 * - Reinitialize the module manager by invoking the {@code init()} method, which
	 *   registers modules anew through the Spring ApplicationContext and JDA event listeners.
	 * - Logs the completion of the module reloading process.
	 * <p>
	 * This method is intended to be used for dynamically refreshing the state of all
	 * bot modules without restarting the entire system.
	 */
	public void reloadModules() {
		log.info("Reloading Discord modules...");
		getEnabledModules().forEach(((s, module) -> module.onDisable()));
		modules.clear();
		init();
		log.info("Discord modules reloaded");
	}

	/**
	 * Reloads a specific bot module by its name.
	 * <p>
	 * This method performs the following operations:
	 * - Retrieves the bot module associated with the specified name.
	 * - If the module exists, it invokes the module's {@code onDisable()} method to properly clean up
	 *   and release resources.
	 * - Removes the module from the internal module storage.
	 * - Reinitialize the module manager by invoking the {@code init()} method, which ensures the
	 *   proper registration of remaining modules and their event listeners.
	 * - Logs a message indicating that the specified module has been reloaded.
	 *
	 * @param name the name of the bot module to reload
	 */
	public synchronized void reloadModule(String name) {
		BotModule module = modules.get(name);

		if (module != null) {
			module.onDisable();
			modules.remove(name);
			init();
			log.info("Discord module {} reloaded", name);
		}
	}

	/**
	 * Initializes the bot module management system by scanning the Spring
	 * ApplicationContext for all beans annotated with {@link BotModuleComponent}.
	 * Modules are registered, their event listeners are added to the JDA
	 * instance, and any enabled modules are activated.
	 * <p>
	 * The initialization process performs the following steps:
	 * <p>
	 * 1. Retrieves all beans annotated with {@code BotModuleComponent} from
	 *    the Spring ApplicationContext.
	 * 2. For each bean that extends the {@link BotModule} class:
	 *    - Retrieves the {@code BotModuleComponent} annotation to access
	 *      its metadata, such as the module's name.
	 *    - Adds the module to the system's internal module map if it is not
	 *      already registered.
	 *    - Registers the module as an event listener with the JDA instance.
	 *    - If the module is enabled (based on the annotation), invokes the
	 *      module's {@code onEnable()} method.
	 * <p>
	 * This method is automatically invoked after the {@link ModulesManagerBackend}
	 * instance is constructed, as it is annotated with {@code @PostConstruct}.
	 * It is also invoked during dynamic reinitialization via the {@code reloadModules()}
	 * or {@code reloadModule(String name)} methods.
	 */
	@PostConstruct
	public void init() {
		Map<String, Object> beans = applicationContext.getBeansWithAnnotation(BotModuleComponent.class);

		beans.forEach((name, bean) -> {
			if (bean instanceof BotModule module) {
				BotModuleComponent annotation = module.getClass().getAnnotation(BotModuleComponent.class);
				if (annotation == null) {
					return;
				}

				modules.putIfAbsent(annotation.name(), module);
				jda.addEventListener(module);

				if (module.isEnabled()) {
					module.onEnable();
				}
			}
		});
	}
}
