package org.universalcube.spring_starter_discord.properties;

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

	/**
	 * Retrieves the bot token used for authenticating with Discord's API.
	 *
	 * @return the Discord bot token as a string.
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Sets the bot token used for authenticating with Discord's API.
	 *
	 * @param token the Discord bot token as a string
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Retrieves the settings configuration for the Discord bot.
	 *
	 * @return an instance of {@link Settings} containing the bot's gateway intents, cache settings, online status,
	 * and chunking configurations.
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * Updates the settings configuration for the Discord bot.
	 *
	 * @param settings an instance of {@link Settings} containing the bot's gateway intents,
	 *                 cache settings, online status, and chunking configurations.
	 */
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/**
	 * Retrieves the activity settings configured for the Discord bot.
	 *
	 * @return an instance of {@link Activity} representing the activity settings,
	 * such as the activity type, name, and URL.
	 */
	public Activity getActivity() {
		return activity;
	}

	/**
	 * Sets the activity settings for the Discord bot.
	 * The activity represents what the bot is doing, such as playing, watching, or streaming content.
	 *
	 * @param activity an instance of {@link Activity} containing the activity type, name,
	 *                 and optional URL to be displayed by the bot.
	 */
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	/**
	 * Retrieves the owner configuration settings for a Discord bot.
	 * The owners configuration defines the root owner and additional owner IDs,
	 * which are used to determine elevated permissions and access control.
	 *
	 * @return an {@link Owners} instance containing the configuration for the bot's owners,
	 * including the root owner and other owner IDs.
	 */
	public Owners getOwners() {
		return owners;
	}

	/**
	 * Sets the owner configuration for the Discord bot.
	 * The owner configuration includes details about the primary root owner
	 * and a collection of additional owner IDs with elevated permissions.
	 *
	 * @param owners an instance of {@link Owners} containing the root owner ID
	 *               and other owner identities used for access control.
	 */
	public void setOwners(Owners owners) {
		this.owners = owners;
	}

	/**
	 * Retrieves the sharding configuration for the Discord bot.
	 * Sharding allows the bot's connection to be divided into smaller segments
	 * to handle large-scale bots or distribute workloads efficiently.
	 *
	 * @return an instance of {@link Sharding} representing the sharding configuration,
	 * including whether sharding is enabled and the number of shards.
	 */
	public Sharding getSharding() {
		return sharding;
	}

	/**
	 * Updates the sharding configuration for the Discord bot.
	 * Sharding enables dividing the bot's connection into multiple shards,
	 * allowing the bot to handle larger scale setups efficiently.
	 *
	 * @param sharding an instance of {@link Sharding} containing the configuration
	 *                 for enabling or disabling sharding and the number of shards.
	 */
	public void setSharding(Sharding sharding) {
		this.sharding = sharding;
	}
}
