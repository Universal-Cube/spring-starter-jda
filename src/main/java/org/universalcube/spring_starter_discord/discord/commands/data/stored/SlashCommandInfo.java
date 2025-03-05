package org.universalcube.spring_starter_discord.discord.commands.data.stored;

import org.springframework.lang.NonNull;

/**
 * Represents a slash command with associated metadata.
 * Provides methods to access the unique identifier and name of the command.
 */
public interface SlashCommandInfo {
	/**
	 * Retrieves the unique identifier associated with the slash command.
	 *
	 * @return the unique identifier of the slash command as a long value
	 */
	long getId();

	/**
	 * Retrieves the name of the slash command.
	 *
	 * @return the name of the slash command as a non-null string
	 */
	@NonNull
	String getName();
}
