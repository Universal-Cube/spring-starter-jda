package org.universalcube.spring_starter_discord.discord.commands.interfaces;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.lang.NonNull;

public interface BotCommandExecutor {
	void onExecute(@NonNull Member sender, @NonNull SlashCommandInteractionEvent event);
}
