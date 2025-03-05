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

public class ModulesManagerBackend {
	private final static Logger log = LoggerFactory.getLogger(ModulesManagerBackend.class);
	private final JDA jda;
	private final ApplicationContext applicationContext;
	private final Map<String, BotModule> modules = new ConcurrentHashMap<>();

	public ModulesManagerBackend(JDA jda, ApplicationContext applicationContext) {
		this.jda = jda;
		this.applicationContext = applicationContext;
	}

	public Map<String, BotModule> getModules() {
		return Collections.unmodifiableMap(modules);
	}

	public Map<String, BotModule> getEnabledModules() {
		return modules.entrySet()
				.stream()
				.filter(entry -> entry.getValue().isEnabled())
				.collect(ConcurrentHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), Map::putAll);
	}

	public Map<String, BotModule> getSortedModules() {
		return modules.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByKey())
				.collect(ConcurrentHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), Map::putAll);
	}

	public BotModule getModule(String name) {
		return modules.get(name);
	}

	public synchronized void addModule(BotModule module) {
		modules.putIfAbsent(module.getName(), module);
		jda.addEventListener(module);

		if (module.isEnabled()) {
			module.onEnable();
		}
	}

	public void reloadModules() {
		log.info("Reloading Discord modules...");
		getEnabledModules().forEach(((s, module) -> module.onDisable()));
		modules.clear();
		init();
		log.info("Discord modules reloaded");
	}

	public synchronized void reloadModule(String name) {
		BotModule module = modules.get(name);

		if (module != null) {
			module.onDisable();
			modules.remove(name);
			init();
			log.info("Discord module {} reloaded", name);
		}
	}

	@PostConstruct
	public void init() {
		Map<String, Object> beans = applicationContext.getBeansWithAnnotation(BotModuleComponent.class);

		beans.forEach((name, bean) -> {
			if (bean instanceof BotModule module) {
				BotModuleComponent annotation = module.getClass().getAnnotation(BotModuleComponent.class);
				if (annotation == null) {
					return;
				}

				try {
					BotModule newModule = module.getClass()
							.getConstructor(String.class, String.class, boolean.class, boolean.class, boolean.class)
							.newInstance(annotation.name(), annotation.description(), annotation.enabled(), annotation.devOnly(), annotation.botOwnerOnly());

					modules.putIfAbsent(annotation.name(), newModule);
					jda.addEventListener(newModule);

					if (newModule.isEnabled()) {
						newModule.onEnable();
					}
				} catch (Exception e) {
					log.error("Error initializing module {}", annotation.name(), e);
					throw new RuntimeException("Error initializing module " + annotation.name(), e);
				}
			}
		});
	}
}
