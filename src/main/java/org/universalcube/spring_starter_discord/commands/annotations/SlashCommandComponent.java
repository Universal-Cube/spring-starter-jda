package org.universalcube.spring_starter_discord.commands.annotations;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface SlashCommandComponent {
	@NonNull String name();
}
