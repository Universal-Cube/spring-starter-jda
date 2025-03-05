package org.universalcube.spring_starter_discord.discord.commands.interfaces;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.lang.NonNull;

/**
 * Represents an executor for handling the logic of a Discord bot command. This interface defines
 * the method to process and handle commands received from Discord's slash command interaction system.
 * <p>
 * Implementations of this interface are responsible for executing the command logic when invoked,
 * utilizing the event data and sender details provided during execution.
 * <p>
 * Typically, implementations are registered with a command handling system, which maps command names
 * to executors and invokes them when corresponding slash commands are triggered by users.
 */
public interface BotCommandExecutor {
	/**
	 * Executes the logic associated with a Discord slash command. This method is invoked when a
	 * specific slash command is triggered by a user. It uses the sender's information and the
	 * interaction event details to perform the appropriate actions defined by the command.
	 *
	 * @param sender the {@link Member} who triggered the slash command. Must not be null.
	 * @param event  the {@link SlashCommandInteractionEvent} representing the interaction details
	 *               of the triggered command. Must not be null.
	 */
	void onExecute(@NonNull Member sender, @NonNull SlashCommandInteractionEvent event);
}
