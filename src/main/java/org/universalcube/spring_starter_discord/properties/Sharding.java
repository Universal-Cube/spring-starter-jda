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

	/**
	 * Compares the specified object with this Sharding instance for equality.
	 * Determines whether the given object is an instance of Sharding and has
	 * the same "enabled" status and "shardCount" value as the current instance.
	 *
	 * @param obj the object to be compared for equality with this instance.
	 * @return {@code true} if the specified object is an instance of Sharding with the
	 * same "enabled" status and "shardCount"; {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Sharding sharding) {
			return this.enabled == sharding.enabled &&
					this.shardCount == sharding.shardCount;
		}
		return super.equals(obj);
	}

	/**
	 * Returns a string representation of the current state of the Sharding configuration.
	 * The string includes whether sharding is enabled and the number of shards configured.
	 *
	 * @return a string representation of the sharding configuration, including the enabled state
	 * and the shard count.
	 */
	@Override
	public String toString() {
		return "ShardingConfiguration(enabled=%s, shardCount=%d)".formatted(enabled, shardCount);
	}
}
