package org.universalcube.spring_starter_discord.discord.modules.annotations;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to define metadata for a bot module in a Spring-based
 * application. This annotation is used to mark a class as a bot module
 * component, which can then be recognized and managed during runtime.
 * <p>
 * The annotation provides essential properties such as name, description,
 * and feature flags like whether the module is enabled, restricted to
 * developers, or restricted to the bot owner only.
 * <p>
 * Fields:
 * - name: The name of the bot module.
 * - description: A brief description of the bot module's purpose or functionality.
 * - enabled: Indicates whether the module is enabled by default.
 * - devOnly: Specifies whether the module is restricted to developers only.
 * - botOwnerOnly: Specifies whether the module is restricted to the bot owner only.
 * <p>
 * Usage:
 * The annotated class must extend the {@code BotModule} abstract class.
 * The {@code ModulesManagerBackend} will automatically scan and register
 * all classes annotated with {@code BotModuleComponent}, initialize them,
 * and add them to the bot's module system during runtime.
 * <p>
 * Default Values:
 * - enabled: false
 * - devOnly: false
 * - botOwnerOnly: false
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface BotModuleComponent {
	@NonNull String name();

	@NonNull String description();

	boolean enabled() default false;

	boolean devOnly() default false;

	boolean botOwnerOnly() default false;
}
