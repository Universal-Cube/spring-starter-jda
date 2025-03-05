package org.universalcube.spring_starter_discord.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.universalcube.spring_starter_discord.discord.listener.ButtonRegistry;

/**
 * ButtonRegistryAutoConfiguration is an Auto-Configuration class that sets up the necessary
 * configuration for managing button interactions within a Discord bot application. It ensures
 * that a {@link ButtonRegistry} bean is available in the application context, which is responsible
 * for registering, handling, and unregistering button interaction events.
 * <p>
 * This configuration class is automatically loaded only after the {@link JdaAutoConfiguration} class,
 * ensuring that the core Discord bot components are properly initialized before the button
 * interaction management features are configured.
 * <p>
 * Conditional Behavior:
 * - The {@link ButtonRegistry} bean is only registered if no other bean of the same type is already present
 * in the Spring application context.
 */
@AutoConfiguration
@AutoConfigureAfter(JdaAutoConfiguration.class)
public class ButtonRegistryAutoConfiguration {

	/**
	 * Registers a {@link ButtonRegistry} bean in the Spring application context if no other bean
	 * of the same type is already defined. The {@link ButtonRegistry} is responsible for managing
	 * button interactions within a Discord bot application by linking button IDs with event handlers.
	 *
	 * @return a new instance of {@link ButtonRegistry}, which allows for the registration, handling,
	 *         and unregistration of button interaction events.
	 */
	@Bean
	@ConditionalOnMissingBean
	public ButtonRegistry buttonRegistry() {
		return new ButtonRegistry();
	}
}
