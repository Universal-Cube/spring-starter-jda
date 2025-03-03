package org.universalcube.spring_starter_discord.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.universalcube.spring_starter_discord.discord.permissions.JdaPermissionUtils;
import org.universalcube.spring_starter_discord.properties.JdaConfigurationProperties;

@AutoConfiguration
@AutoConfigureAfter(JdaAutoConfiguration.class)
public class JdaPermissionUtilsAutoConfiguration {

	@Bean
	@ConditionalOnBean(JdaConfigurationProperties.class)
	@ConditionalOnMissingBean
	public JdaPermissionUtils jdaPermissionUtils(JdaConfigurationProperties jdaConfigurationProperties) {
		return new JdaPermissionUtils(jdaConfigurationProperties);
	}
}
