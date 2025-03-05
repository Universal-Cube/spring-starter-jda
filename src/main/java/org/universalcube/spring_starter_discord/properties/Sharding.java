package org.universalcube.spring_starter_discord.properties;

import lombok.Data;

/**
 * Represents the sharding configuration properties for a Discord bot using JDA (Java Discord API).
 * <p>
 * Sharding is a mechanism that divides the bot's connection into smaller segments (shards),
 * allowing it to handle large-scale bots or distribute workloads more efficiently.
 * This class allows enabling or disabling sharding and specifying the number of shards to be used.
 */
@Data
public final class Sharding {
	private boolean enabled = false;
	private int shardCount = 8;
}
