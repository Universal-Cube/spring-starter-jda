package org.universalcube.spring_starter_discord.discord.commands.interfaces;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.lang.NonNull;

/**
 * Represents a bot command component that integrates with Discord's slash commands. This interface
 * extends {@code BotCommandExecutor} to handle the execution logic of a command and provides
 * additional capabilities for defining the command structure.
 * <p>
 * Implementing classes are responsible for defining the command data that is used to register
 * the command with Discord via the {@code createCommandData()} method. This method must return
 * a {@code CommandData} object that encapsulates the metadata and structure of the command, including
 * its name, description, and parameters.
 * <p>
 * In addition to defining the command data, implementing classes must also provide logic for
 * handling the execution of the command through the {@code onExecute()} method inherited
 * from {@code BotCommandExecutor}.
 */
public interface BotCommand extends BotCommandExecutor {
	/**
	 * Creates and returns the command data required to register a slash command with Discord.
	 * The returned {@link CommandData} object encapsulates all necessary metadata and structure
	 * of the command, including details such as the command name, description, and parameters.
	 *
	 * @return a {@link CommandData} instance representing the structure and metadata of the slash command.
	 * Must not be null.
	 */
	@NonNull
	CommandData createCommandData();
}
