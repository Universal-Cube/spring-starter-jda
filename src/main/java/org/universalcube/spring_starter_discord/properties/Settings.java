package org.universalcube.spring_starter_discord.properties;

import lombok.Data;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.HashSet;
import java.util.Set;

@Data
public final class Settings {
	private Set<GatewayIntent> gatewayIntents = new HashSet<>();
	private Set<CacheFlag> cacheFlags = new HashSet<>();
	private OnlineStatus onlineStatus = OnlineStatus.ONLINE;
	private boolean enabledChunking = false;
	private int chunkSize = 1024;
}
