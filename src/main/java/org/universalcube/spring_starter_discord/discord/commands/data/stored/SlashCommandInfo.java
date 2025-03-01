package org.universalcube.spring_starter_discord.discord.commands.data.stored;

import org.springframework.lang.NonNull;

public interface SlashCommandInfo {
	long getId();

	@NonNull
	String getName();
}
