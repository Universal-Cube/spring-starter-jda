package org.universalcube.spring_starter_discord.discord.commands.interfaces;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import org.springframework.lang.NonNull;

/**
 * Represents an entity capable of handling autocomplete interactions for commands.
 * Implementations of this interface are responsible for providing relevant autocomplete
 * suggestions based on the context of the interaction event.
 */
public interface AutoCompletable {
	/**
	 * Handles the autocomplete interaction event for a command, providing context-specific suggestions
	 * based on the interaction details.
	 *
	 * @param event the {@link CommandAutoCompleteInteractionEvent} containing information
	 *              about the autocomplete request. Must not be null.
	 */
	void onAutoComplete(@NonNull CommandAutoCompleteInteractionEvent event);
}
