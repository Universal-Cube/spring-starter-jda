package org.universalcube.spring_starter_discord.discord.modules;

public interface BotModule {
	boolean isEnabled();

	void onEnable();

	void onDisable();
}
