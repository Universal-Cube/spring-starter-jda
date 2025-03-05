package org.universalcube.spring_starter_discord.properties;


/**
 * Represents the sharding configuration properties for a Discord bot using JDA (Java Discord API).
 * <p>
 * Sharding is a mechanism that divides the bot's connection into smaller segments (shards),
 * allowing it to handle large-scale bots or distribute workloads more efficiently.
 * This class allows enabling or disabling sharding and specifying the number of shards to be used.
 */
public final class Sharding {
	private boolean enabled = false;
	private int shardCount = 8;

	/**
	 * Constructs a new instance of the Sharding class with default configuration.
	 * By default, sharding is disabled, and the number of shards is set to the default value.
	 */
	public Sharding() {
	}

	/**
	 * Constructs a new instance of the Sharding class with the specified sharding configuration.
	 *
	 * @param enabled    a boolean indicating whether sharding is enabled.
	 *                   {@code true} to enable sharding, {@code false} to disable it.
	 * @param shardCount an integer specifying the number of shards to be used.
	 *                   Must be greater than or equal to 1 when sharding is enabled.
	 */
	public Sharding(boolean enabled, int shardCount) {
		this.enabled = enabled;
		this.shardCount = shardCount;
	}

	/**
	 * Determines whether sharding is enabled for the bot configuration.
	 *
	 * @return {@code true} if sharding is enabled; {@code false} otherwise.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Enables or disables the sharding configuration for a Discord bot.
	 *
	 * @param enabled a boolean value indicating whether sharding should be enabled ({@code true})
	 *                or disabled ({@code false}).
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Retrieves the total number of shards configured for the Discord bot.
	 * Shards divide the bot's workload, enabling better scalability and efficiency
	 * when handling large-scale bots or distributing operations.
	 *
	 * @return the total number of shards as an integer. Defaults to 8 if not explicitly configured.
	 */
	public int getShardCount() {
		return shardCount;
	}

	/**
	 * Sets the number of shards to be used in the sharding configuration.
	 * Shards divide the bot's workload, allowing for better scalability
	 * and efficient handling of large-scale bots or distributed operations.
	 *
	 * @param shardCount the total number of shards to be configured.
	 *                   This value must be greater than or equal to 1.
	 */
	public void setShardCount(int shardCount) {
		this.shardCount = shardCount;
	}
}
