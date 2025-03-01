package org.universalcube.spring_starter_discord.configuration;

import net.dv8tion.jda.api.JDA;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.universalcube.spring_starter_discord.discord.commands.SlashCommandBackend;

@AutoConfiguration
@AutoConfigureAfter(JdaAutoConfiguration.class)
public class JdaSlashCommandAutoConfiguration {
	private final ApplicationContext applicationContext;
	private final JDA jda;

	public JdaSlashCommandAutoConfiguration(ApplicationContext applicationContext, JDA jda) {
		this.applicationContext = applicationContext;
		this.jda = jda;
	}

	@Bean
	@ConditionalOnMissingBean
	public SlashCommandBackend slashCommandBackend() {
		return new SlashCommandBackend(jda, applicationContext);
	}
}
