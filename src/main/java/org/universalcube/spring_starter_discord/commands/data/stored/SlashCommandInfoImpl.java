package org.universalcube.spring_starter_discord.commands.data.stored;

import org.springframework.lang.NonNull;

public record SlashCommandInfoImpl(@NonNull String name, long id) implements SlashCommandInfo {
	@Override
	public long getId() {
		return this.id;
	}

	@Override
	@NonNull
	public String getName() {
		return this.name;
	}
}
