package org.universalcube.spring_starter_discord.discord.modules.annotations;

import net.dv8tion.jda.api.events.Event;
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

	@NonNull Class<? extends Event> eventClass();
}
