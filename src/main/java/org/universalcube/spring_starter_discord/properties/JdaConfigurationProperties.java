package org.universalcube.spring_starter_discord.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Data
@ConfigurationProperties(prefix = "spring.jda")
public class JdaConfigurationProperties {
	private String token;
	@NestedConfigurationProperty
	private Settings settings;
	@NestedConfigurationProperty
	private Activity activity;
	@NestedConfigurationProperty
	private Owners owners;
}
