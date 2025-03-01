package org.universalcube.spring_starter_discord.commands.data;

import org.springframework.lang.NonNull;
import org.universalcube.spring_starter_discord.commands.interfaces.BotCommand;

public record BotCommandData(@NonNull BotCommand command, String name) {

}
