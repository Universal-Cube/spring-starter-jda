package org.universalcube.spring_starter_discord.configuration;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.lang.NonNull;

@AutoConfiguration
@AutoConfigureAfter(JdaAutoConfiguration.class)
public class JdaEventPublisherAutoConfiguration {
	public JdaEventPublisherAutoConfiguration(JDA jda, ApplicationEventPublisher publisher) {
		jda.addEventListener(new ListenerAdapter() {
			@Override
			public void onGenericEvent(@NonNull GenericEvent event) {
				publisher.publishEvent(new PayloadApplicationEvent<>(jda, event));
			}
		});
	}
}
