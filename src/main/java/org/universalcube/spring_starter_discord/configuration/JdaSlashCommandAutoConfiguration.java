package org.universalcube.spring_starter_discord.configuration;

import net.dv8tion.jda.api.JDA;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.universalcube.spring_starter_discord.discord.commands.SlashCommandBackend;

/**
 * Autoconfiguration class for setting up JDA (Java Discord API) Slash Command support.
 * <p>
 * This class initializes and configures the necessary beans for handling
 * slash commands within a Spring Boot application. It depends on the JDA
 * instance and the Spring application context to properly register and process
 * the slash commands.
 * <p>
 * This configuration is applied only if a SlashCommandBackend bean is not
 * already defined in the application context.
 * <p>
 * An instance of <code>SlashCommandBackend</code> is created as a Bean, which is responsible
 * for integrating slash commands with Discord using JDA and also interacts with the application context.
 * <p>
 * This class should be used in conjunction with the {@code JdaAutoConfiguration} class
 * to ensure proper initialization of the JDA instance required for handling Discord commands.
 */
@AutoConfiguration
@AutoConfigureAfter(JdaAutoConfiguration.class)
public class JdaSlashCommandAutoConfiguration {
	private final ApplicationContext applicationContext;
	private final JDA jda;

	/**
	 * Constructs an instance of the JdaSlashCommandAutoConfiguration class.
	 *
	 * @param applicationContext the Spring application context used to manage application beans and dependencies
	 * @param jdaManager         an object representing the JDA (Java Discord API) instance; it is cast to a JDA type within the constructor
	 */
	public JdaSlashCommandAutoConfiguration(ApplicationContext applicationContext, JDA jdaManager) {
		this.applicationContext = applicationContext;
		this.jda = jdaManager;
	}

	/**
	 * Creates and provides a Bean of type {@link SlashCommandBackend} if no such Bean
	 * is already defined in the application context.
	 * <p>
	 * The SlashCommandBackend is responsible for handling the registration, execution,
	 * and management of Discord slash commands, leveraging the provided JDA instance
	 * for interacting with Discord and the Spring ApplicationContext for identifying
	 * and managing command-related components.
	 *
	 * @return a new instance of SlashCommandBackend initialized with the JDA and
	 *         ApplicationContext instances from the configuration class.
	 */
	@Bean
	@ConditionalOnMissingBean
	public SlashCommandBackend slashCommandBackend() {
		return new SlashCommandBackend(jda, applicationContext);
	}
}
