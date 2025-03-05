package org.universalcube.spring_starter_discord.properties;

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
public final class Settings {
	private Set<GatewayIntent> gatewayIntents = new HashSet<>();
	private Set<CacheFlag> cacheFlags = new HashSet<>();
	private OnlineStatus onlineStatus = OnlineStatus.ONLINE;
	private boolean enabledChunking = false;
	private int chunkSize = 1024;

	/**
	 * Constructs a new Settings instance with default configuration.
	 * By default, all fields within this Settings instance are set to their
	 * respective default values.
	 * <p>
	 * This constructor initializes a Settings instance without any initial
	 * customization. To customize the configuration fields such as gateway
	 * intents, cache flags, online status, chunking, or chunk size, use the
	 * parameterized constructor or setter methods provided (if applicable).
	 */
	public Settings() {
	}

	/**
	 * Constructs a new Settings instance with the specified configuration parameters.
	 * This constructor allows customization of gateway intents, cache flags, online status,
	 * chunking, and chunk size for the Settings instance.
	 *
	 * @param gatewayIntents  a set of gateway intents that define specific actions or events
	 *                        the bot can subscribe to. Passing an empty or null set will
	 *                        result in no intents being configured.
	 * @param cacheFlags      a set of cache flags that specify which aspects of the bot's cache
	 *                        functionalities are enabled. A null or empty set disables caching
	 *                        for all components.
	 * @param onlineStatus    the online status to be used for the bot such as online, offline,
	 *                        or idle. This determines how the bot appears to other users.
	 * @param enabledChunking a boolean value indicating whether chunking (loading user
	 *                        data in parts) is enabled or not. If true, chunking operates
	 *                        based on the specified chunk size.
	 * @param chunkSize       an integer defining the size of each chunk when chunking is enabled.
	 *                        This controls the number of items loaded per chunk. Ignored if
	 *                        chunking is disabled.
	 */
	public Settings(Set<GatewayIntent> gatewayIntents, Set<CacheFlag> cacheFlags, OnlineStatus onlineStatus, boolean enabledChunking, int chunkSize) {
		this.gatewayIntents = gatewayIntents;
		this.cacheFlags = cacheFlags;
		this.onlineStatus = onlineStatus;
		this.enabledChunking = enabledChunking;
		this.chunkSize = chunkSize;
	}

	/**
	 * Retrieves the set of gateway intents associated with the current configuration.
	 * Gateway intents define specific actions or events that the bot can subscribe to.
	 *
	 * @return a set of {@code GatewayIntent} objects representing the configured gateway intents.
	 */
	public Set<GatewayIntent> getGatewayIntents() {
		return gatewayIntents;
	}

	/**
	 * Sets the gateway intents for the configuration.
	 * Gateway intents define specific actions or events that the bot can subscribe to.
	 *
	 * @param gatewayIntents a set of {@code GatewayIntent} objects representing the
	 *                       desired intents for the bot. Passing a null or empty set
	 *                       will result in no gateway intents being configured.
	 */
	public void setGatewayIntents(Set<GatewayIntent> gatewayIntents) {
		this.gatewayIntents = gatewayIntents;
	}

	/**
	 * Retrieves the set of cache flags configured for this instance.
	 * Cache flags specify which components or aspects of caching functionality
	 * are enabled within the application.
	 *
	 * @return a set of {@code CacheFlag} objects representing the current cache flags.
	 * Returns an empty set if no cache flags are configured.
	 */
	public Set<CacheFlag> getCacheFlags() {
		return cacheFlags;
	}

	/**
	 * Configures the cache flags for the current Settings instance.
	 * Cache flags determine which aspects of the bot's caching functionality
	 * are enabled. Updating the cache flags modifies the components or data
	 * that are retained in memory during runtime.
	 *
	 * @param cacheFlags a set of {@code CacheFlag} objects representing
	 *                   the cache functionalities to enable. Passing a
	 *                   null value disables all caching components.
	 */
	public void setCacheFlags(Set<CacheFlag> cacheFlags) {
		this.cacheFlags = cacheFlags;
	}

	/**
	 * Retrieves the online status configured for this Settings instance.
	 * The online status determines how the bot appears to other users (e.g., online, offline, idle).
	 *
	 * @return the current online status of type {@code OnlineStatus} for this instance.
	 */
	public OnlineStatus getOnlineStatus() {
		return onlineStatus;
	}

	/**
	 * Sets the online status for the current Settings instance.
	 * The online status determines how the bot appears to other users, such as online, offline, or idle.
	 *
	 * @param onlineStatus the desired online status of type {@code OnlineStatus} for this instance.
	 */
	public void setOnlineStatus(OnlineStatus onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	/**
	 * Checks whether chunking is enabled in the current configuration.
	 * Chunking refers to the process of loading data in parts rather than all at once.
	 *
	 * @return {@code true} if chunking is enabled; {@code false} otherwise.
	 */
	public boolean isEnabledChunking() {
		return enabledChunking;
	}

	/**
	 * Sets the chunking status for the current configuration.
	 * Chunking determines whether data is loaded in parts rather than all at once.
	 *
	 * @param enabledChunking a boolean indicating whether chunking is enabled
	 *                        ({@code true}) or disabled ({@code false}).
	 */
	public void setEnabledChunking(boolean enabledChunking) {
		this.enabledChunking = enabledChunking;
	}

	/**
	 * Retrieves the current chunk size configured for this Settings instance.
	 * The chunk size indicates the number of items processed per batch when chunking is enabled.
	 *
	 * @return the chunk size as an integer. If chunking is disabled, this value may be ignored.
	 */
	public int getChunkSize() {
		return chunkSize;
	}

	/**
	 * Sets the size of chunks used for processing data when chunking is enabled.
	 * The chunk size determines the number of items processed per chunk.
	 *
	 * @param chunkSize the size of each chunk as an integer. A higher value may
	 *                  result in larger batches being processed, while a lower
	 *                  value may lead to smaller and more frequent batches. The
	 *                  value is ignored if chunking is disabled.
	 */
	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}

	/**
	 * Compares this Settings instance with another object for equality.
	 * Two Settings instances are considered equal if all their fields
	 * (gateway intents, cache flags, online status, enabled chunking, and chunk size)
	 * are identical. Otherwise, the method delegates the comparison to the superclass.
	 *
	 * @param obj the object to be compared for equality with this Settings instance.
	 * @return {@code true} if the specified object is an instance of Settings and
	 * all fields are equal; {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Settings settings) {
			return this.gatewayIntents.equals(settings.gatewayIntents)
					&& this.cacheFlags.equals(settings.cacheFlags)
					&& this.onlineStatus.equals(settings.onlineStatus)
					&& this.enabledChunking == settings.enabledChunking
					&& this.chunkSize == settings.chunkSize;
		}

		return super.equals(obj);
	}

	/**
	 * Provides a string representation of the Settings instance.
	 * The returned string includes the values of gateway intents, cache flags,
	 * online status, chunking status, and chunk size in a formatted manner.
	 *
	 * @return a formatted string representing the current state of the Settings instance.
	 */
	@Override
	public String toString() {
		return "Settings{gatewayIntents=%s, cacheFlags=%s, onlineStatus=%s, enabledChunking=%s, chunkSize=%s}"
				.formatted(gatewayIntents, cacheFlags, onlineStatus, enabledChunking, chunkSize);
	}
}
