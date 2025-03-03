package org.universalcube.spring_starter_discord.properties;

import lombok.Data;

@Data
public final class Sharding {
	private boolean enabled = false;
	private int shardCount = 8;
}
