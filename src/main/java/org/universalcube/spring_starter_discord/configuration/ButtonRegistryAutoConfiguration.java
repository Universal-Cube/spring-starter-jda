package org.universalcube.spring_starter_discord.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.universalcube.spring_starter_discord.discord.listener.ButtonRegistry;

@AutoConfiguration
@AutoConfigureAfter(JdaAutoConfiguration.class)
public class ButtonRegistryAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public ButtonRegistry buttonRegistry() {
		return new ButtonRegistry();
	}
}
