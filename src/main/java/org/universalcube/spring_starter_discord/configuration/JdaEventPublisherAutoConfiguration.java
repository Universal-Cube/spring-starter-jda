package org.universalcube.spring_starter_discord.configuration;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.lang.NonNull;

/**
 * Autoconfiguration class for setting up event publishing for JDA (Java Discord API) events.
 * <p>
 * This autoconfiguration ensures that JDA events are published as Spring application events,
 * allowing integration of JDA event handling with the Spring application context.
 * <p>
 * When a JDA instance or a ShardManager is provided, an event listener is added to forward
 * JDA events as {@link PayloadApplicationEvent} objects.
 * <p>
 * The class depends on the {@link JdaAutoConfiguration} class and is loaded after it.
 * <p>
 * Prerequisite:
 * - Ensure that {@link JdaAutoConfiguration} is correctly configured to provide
 * the required JDA or ShardManager instance.
 */
@AutoConfiguration
@AutoConfigureAfter(JdaAutoConfiguration.class)
public class JdaEventPublisherAutoConfiguration {

	/**
	 * Constructs an instance of the JdaEventPublisherAutoConfiguration class, which sets up
	 * forwarding of JDA (Java Discord API) events as Spring application events.
	 *
	 * @param jdaManager an instance of either {@code JDA} or {@code ShardManager} that represents
	 *                   the JDA manager responsible for handling Discord events. The implementation
	 *                   determines whether the event listener is added to a single JDA instance
	 *                   or a ShardManager.
	 * @param publisher  the Spring {@code ApplicationEventPublisher} used to publish JDA events
	 *                   (e.g., instances of {@code GenericEvent}) as {@link PayloadApplicationEvent}
	 *                   to the Spring application context.
	 */
	public JdaEventPublisherAutoConfiguration(JDA jdaManager, ApplicationEventPublisher publisher) {
		if (jdaManager instanceof JDA jda) {
			jda.addEventListener(new ListenerAdapter() {
				@Override
				public void onGenericEvent(@NonNull GenericEvent event) {
					publisher.publishEvent(new PayloadApplicationEvent<>(jda, event));
				}
			});
		} else if (jdaManager instanceof ShardManager shardManager) {
			shardManager.addEventListener(new ListenerAdapter() {
				@Override
				public void onGenericEvent(@NonNull GenericEvent event) {
					publisher.publishEvent(new PayloadApplicationEvent<>(shardManager, event));
				}
			});
		}
	}
}
