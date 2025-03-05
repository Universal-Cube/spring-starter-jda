package org.universalcube.spring_starter_discord.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * A configuration properties class for setting up a Discord bot using the JDA (Java Discord API) in a Spring Boot application.
 * <p>
 * This class enables customization of various JDA-related configurations via the `spring.jda` property prefix in
 * application properties or YAML files. It acts as a central place to define token settings, bot behavior,
 * activity customization, owner specifications, and sharding details.
 * <p>
 * Fields:
 * - `token`: The bot token used for authenticating with Discord's API.
 * - `settings`: Defines gateway intents, cache flags, online status, and chunking configurations via {@link Settings}
 * - `activity`: Configures the activity settings for the bot, such as what it is "playing" or "watching." via {@link Activity}
 * - `owners`: Specifies owner-related settings, including primary root owner and additional owner IDs via {@link Owners}
 * - `sharding`: Determines whether sharding is enabled and specifies the shard count via {@link Sharding}
 * <p>
 * This class integrates with Spring Boot's configuration properties mechanism and uses nested configuration
 * properties features for complex configuration hierarchies.
 */
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
	@NestedConfigurationProperty
	private Sharding sharding;
}
