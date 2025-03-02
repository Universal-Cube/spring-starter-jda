package org.universalcube.spring_starter_discord.discord.modules;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import org.springframework.context.ApplicationContext;
import org.universalcube.spring_starter_discord.discord.modules.annotations.BotModuleComponent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModulesManagerBackend {
	private final JDA jda;
	private final ApplicationContext applicationContext;
	private final Map<String, BotModule> modules = new ConcurrentHashMap<>();

	public ModulesManagerBackend(JDA jda, ApplicationContext applicationContext) {
		this.jda = jda;
		this.applicationContext = applicationContext;
	}

	@PostConstruct
	public void init() {
		Map<String, Object> beans = applicationContext.getBeansWithAnnotation(BotModuleComponent.class);
		beans.forEach((name, bean) -> {
			if (bean instanceof BotModule module) {
				BotModuleComponent annotation = module.getClass().getAnnotation(BotModuleComponent.class);
				String moduleName = annotation.name();
				modules.putIfAbsent(moduleName, module);
				jda.addEventListener(module);
			}
		});
	}
}
