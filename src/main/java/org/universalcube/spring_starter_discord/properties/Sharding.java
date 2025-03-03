package org.universalcube.spring_starter_discord.properties;

import lombok.Data;

@Data
public final class Sharding {
	private boolean enabled = false;
	private int minShards = 1;
	private int maxShards = 8;
}
