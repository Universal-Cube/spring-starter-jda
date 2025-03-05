package org.universalcube.spring_starter_discord.configuration;

import net.dv8tion.jda.api.JDA;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.universalcube.spring_starter_discord.discord.modules.ModulesManagerBackend;

/**
 * Autoconfiguration class for automatically setting up JDA (Java Discord API) modules.
 * <p>
 * This autoconfiguration class ensures that JDA modules are properly initialized
 * and integrated with the Spring application context. It is dependent on
 * {@link JdaAutoConfiguration} to configure the JDA instance before setting up
 * the module management backend.
 * <p>
 * It exposes a bean of type {@link ModulesManagerBackend}, which manages the
 * lifecycle and integration of JDA modules with the application.
 */
@AutoConfiguration
@AutoConfigureAfter(JdaAutoConfiguration.class)
public class JdaModulesAutoConfiguration {
	private final ApplicationContext applicationContext;
	private final JDA jda;

	/**
	 * Constructs a new instance of JdaModulesAutoConfiguration.
	 *
	 * @param applicationContext the Spring ApplicationContext used to manage the application environment and components.
	 * @param jdaManager         the JDA manager object, expected to be an instance of JDA, that manages the Discord API interaction.
	 */
	public JdaModulesAutoConfiguration(ApplicationContext applicationContext, JDA jdaManager) {
		this.applicationContext = applicationContext;
		this.jda = jdaManager;
	}

	/**
	 * Creates and configures a {@link ModulesManagerBackend} bean to manage the lifecycle and
	 * interactions of bot modules within the JDA (Java Discord API) system.
	 * This method ensures that the ModulesManagerBackend is only created if no other
	 * bean of the same type is already defined in the Spring ApplicationContext.
	 *
	 * @return an instance of {@link ModulesManagerBackend} responsible for managing
	 * Discord bot modules and their integration with the Spring application context
	 */
	@Bean
	@ConditionalOnMissingBean
	public ModulesManagerBackend modulesManager() {
		return new ModulesManagerBackend(jda, applicationContext);
	}
}
