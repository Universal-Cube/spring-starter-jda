package org.universalcube.spring_starter_discord.discord.commands.annotations;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a class implements a slash command component for Discord bots. This annotation is
 * used to identify and register bean classes that define specific slash commands.
 * <p>
 * Classes annotated with {@code SlashCommandComponent} must implement the {@code BotCommand}
 * interface and provide the necessary methods for handling command data creation and execution.
 * <p>
 * The annotation is typically processed during application initialization to register and map commands
 * to their respective names, which are defined through the {@code name} parameter. These commands
 * can then be dynamically added to the Discord bot instance.
 * <p>
 * Annotated classes must define the {@code name} attribute, which specifies the unique name of the
 * slash command as registered with Discord.
 * <p>
 * This annotation requires Spring's {@code @Component} for automatic bean detection and registration.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface SlashCommandComponent {
	@NonNull String name();
}
