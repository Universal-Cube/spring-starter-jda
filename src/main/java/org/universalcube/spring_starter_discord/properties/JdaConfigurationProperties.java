package org.universalcube.spring_starter_discord.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
@ConfigurationProperties("spring.jda")
public class JdaConfigurationProperties {
	private String token;
	@NestedConfigurationProperty
	private Settings settings;
	@NestedConfigurationProperty
	private Activity activity;
	@NestedConfigurationProperty
	private Owners owners;
}
