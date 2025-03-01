package org.universalcube.spring_starter_discord.discord.commands.interfaces;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.lang.NonNull;

public interface BotCommand extends BotCommandExecutor {
	@NonNull
	CommandData createCommandData();
}
