package org.universalcube.spring_starter_discord.properties;

import lombok.Data;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the configuration properties for the settings of a Discord bot using JDA (Java Discord API).
 * <p>
 * This class contains customizable properties for controlling the bot's gateway intents, cache settings,
 * online status, and chunking behavior. These settings allow for fine-grained configuration of the bot's
 * behavior and performance based on its requirements and use cases.
 * <p>
 * Fields:
 * - `gatewayIntents`: A set of {@link GatewayIntent} specifying the types of events the bot should receive from Discord.
 * - `cacheFlags`: A set of {@link CacheFlag} indicating which caching options should be enabled for the bot.
 * - `onlineStatus`: Specifies the default {@link OnlineStatus} of the bot when it starts.
 * - `enabledChunking`: A boolean that determines whether chunking of member data is enabled.
 * - `chunkSize`: An integer that specifies the size of chunks when chunking is enabled.
 */
@Data
public final class Settings {
	private Set<GatewayIntent> gatewayIntents = new HashSet<>();
	private Set<CacheFlag> cacheFlags = new HashSet<>();
	private OnlineStatus onlineStatus = OnlineStatus.ONLINE;
	private boolean enabledChunking = false;
	private int chunkSize = 1024;
}
