package org.universalcube.spring_starter_discord.discord.commands.interfaces;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import org.springframework.lang.NonNull;

public interface AutoCompletable {
	void onAutoComplete(@NonNull CommandAutoCompleteInteractionEvent event);
}
