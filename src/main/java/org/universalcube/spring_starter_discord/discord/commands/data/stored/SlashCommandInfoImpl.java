package org.universalcube.spring_starter_discord.discord.commands.data.stored;

import org.springframework.lang.NonNull;

/**
 * A record implementation of the {@link SlashCommandInfo} interface,
 * representing metadata associated with a slash command.
 * This implementation encapsulates the command's name and its unique identifier.
 */
public record SlashCommandInfoImpl(@NonNull String name, long id) implements SlashCommandInfo {
	/**
	 * Retrieves the unique identifier of the slash command.
	 *
	 * @return the unique identifier of the slash command as a long value
	 */
	@Override
	public long getId() {
		return this.id;
	}

	/**
	 * Retrieves the name of the slash command.
	 *
	 * @return the name of the slash command as a non-null string
	 */
	@Override
	@NonNull
	public String getName() {
		return this.name;
	}
}
