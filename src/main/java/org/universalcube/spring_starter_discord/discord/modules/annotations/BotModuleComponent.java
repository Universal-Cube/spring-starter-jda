package org.universalcube.spring_starter_discord.discord.modules.annotations;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
