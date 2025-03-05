package org.universalcube.spring_starter_discord.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.universalcube.spring_starter_discord.discord.permissions.JdaPermissionUtils;
import org.universalcube.spring_starter_discord.properties.JdaConfigurationProperties;

/**
 * Autoconfiguration class for setting up JdaPermissionUtils.
 * <p>
 * This configuration is triggered after JdaAutoConfiguration has been processed.
 * It provides a bean of type JdaPermissionUtils if a JdaConfigurationProperties bean
 * is available in the application context and no JdaPermissionUtils bean is already defined.
 */
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
