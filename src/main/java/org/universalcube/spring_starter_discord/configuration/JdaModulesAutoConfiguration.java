package org.universalcube.spring_starter_discord.configuration;

import net.dv8tion.jda.api.JDA;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.universalcube.spring_starter_discord.discord.modules.ModulesManagerBackend;

@AutoConfiguration
@AutoConfigureAfter(JdaAutoConfiguration.class)
public class JdaModulesAutoConfiguration {
	private final ApplicationContext applicationContext;
	private final JDA jda;

	public JdaModulesAutoConfiguration(ApplicationContext applicationContext, JDA jda) {
		this.applicationContext = applicationContext;
		this.jda = jda;
	}

	@Bean
	@ConditionalOnMissingBean
	public ModulesManagerBackend modulesManager() {
		return new ModulesManagerBackend(jda, applicationContext);
	}
}
