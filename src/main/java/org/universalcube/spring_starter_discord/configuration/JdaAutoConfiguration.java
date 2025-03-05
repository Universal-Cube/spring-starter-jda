package org.universalcube.spring_starter_discord.configuration;

import jakarta.annotation.PreDestroy;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.universalcube.spring_starter_discord.properties.JdaConfigurationProperties;
import org.universalcube.spring_starter_discord.properties.Settings;
import org.universalcube.spring_starter_discord.properties.Sharding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Autoconfiguration class for initializing and managing JDA (Java Discord API) instances.
 * <p>
 * This class is responsible for providing the necessary Spring beans to configure and create
 * either single-instance or sharded JDA setups, based on the application properties provided.
 * It supports customization through the {@link JdaConfigurationProperties} class.
 * <p>
 * The configuration enables both single and sharded Discord bot setups, with customization
 * options like gateway intents, cache policies, activities, and online statuses.
 * <p>
 * Beans Provided:
 * - {@link JdaConfigurationProperties}: Configuration properties for customizing JDA instances.
 * - A JDA or ShardManager instance (single or sharded, based on property configuration).
 * <p>
 * Shutdown:
 * - Implements a lifecycle shutdown hook to terminate all JDA instances gracefully.
 * <p>
 * Requirements:
 * - `spring.jda.token` property must be set in the application configuration to initialize.
 * - Sharding configuration can be enabled by setting appropriate properties in `spring.jda`.
 */
@AutoConfiguration
@EnableConfigurationProperties(JdaConfigurationProperties.class)
public class JdaAutoConfiguration {
	private final static Logger log = LoggerFactory.getLogger(JdaAutoConfiguration.class);
	private final JdaConfigurationProperties properties;
	private final List<ShardManager> shardManagers = new ArrayList<>();
	private final List<JDA> jdaInstances = new ArrayList<>();

	/**
	 * Constructs a new instance of {@code JdaAutoConfiguration} with the given configuration properties.
	 *
	 * @param properties an instance of {@link JdaConfigurationProperties} containing the necessary
	 *                   configuration for initializing the JDA (Java Discord API) instances, such as
	 *                   bot token, gateway intents, activity settings, and optional sharding configuration.
	 */
	public JdaAutoConfiguration(JdaConfigurationProperties properties) {
		this.properties = properties;
	}

	/**
	 * Creates and returns an instance of {@link JdaConfigurationProperties} for configuring
	 * JDA (Java Discord API) in a Spring Boot application.
	 * <p>
	 * This method is annotated as a Spring bean and is only executed if a bean of the same type
	 * is not already present and the property "spring.jda.token" is defined.
	 *
	 * @return an instance of {@link JdaConfigurationProperties} containing the configuration
	 *         for initializing the JDA, such as bot token, activity settings, and sharding configuration.
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty("spring.jda.token")
	public JdaConfigurationProperties jdaConfigurationProperties() {
		return this.properties;
	}

	/**
	 * Initializes and configures the JDA (Java Discord API) management instance.
	 * Depending on the sharding configuration, this method creates either a
	 * single instance or a sharded instance of JDA.
	 * <p>
	 * If sharding is enabled, a {@link ShardManager} is created to manage multiple shards.
	 * Otherwise, a single {@link JDA} instance is initialized.
	 * <p>
	 * This method is annotated as a Spring bean. It is executed only if a bean of
	 * the same type is not already registered and the property "spring.jda.token" is provided.
	 *
	 * @return an instance of {@link JDA} for a single bot setup or {@link ShardManager}
	 *         for a sharded setup, used to interact with the Discord API.
	 * @throws InterruptedException if the thread is interrupted while awaiting the
	 *                              readiness of the single JDA instance in non-sharded mode.
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty("spring.jda.token")
	public Object jdaManager() throws InterruptedException {
		if (Objects.nonNull(properties.getSharding()) && properties.getSharding().isEnabled()) {
			log.info("Starting bot in SHARDED instance mode.");
			log.info("JDA manager is sharded.");
			return createShardedInstance();
		} else {
			log.info("Starting bot in SINGLE instance mode.");
			log.info("JDA manager is not sharded.");
			return createSingleInstance();
		}
	}

	/**
	 * Creates and initializes a new instance of {@link ShardManager} for managing
	 * sharded connections to the Discord API. The method uses configuration
	 * properties to determine the total number of shards and sets up the
	 * {@link DefaultShardManagerBuilder} accordingly.
	 * The created {@link ShardManager} is added to the internal list of shard managers.
	 *
	 * @return a fully configured instance of {@link ShardManager} that manages
	 *         sharded connections to the Discord API.
	 */
	private ShardManager createShardedInstance() {
		Sharding sharding = properties.getSharding();
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(properties.getToken());

		configureBuilder(builder);
		builder.setShardsTotal(sharding.getShardCount());

		ShardManager shardManager = builder.build();
		shardManagers.add(shardManager);
		return shardManager;
	}

	/**
	 * Creates and initializes a single instance of {@link JDA} for interacting with the Discord API.
	 * This method configures a {@link JDABuilder} with settings retrieved from the {@link JdaConfigurationProperties},
	 * builds the JDA instance, awaits its readiness, and adds it to the list of managed JDA instances.
	 *
	 * @return a newly created instance of {@link JDA} that is ready to use for interacting with Discord's API.
	 * @throws InterruptedException if the thread is interrupted while awaiting readiness of the JDA instance.
	 */
	private JDA createSingleInstance() throws InterruptedException {
		JDABuilder builder = JDABuilder.createDefault(properties.getToken());

		configureBuilder(builder);

		JDA jda = builder.build().awaitReady();
		jdaInstances.add(jda);
		return jda;
	}

	/**
	 * Configures the provided {@link JDABuilder} instance with properties such as gateway intents,
	 * cache settings, online status, activity settings, and other related options based on the
	 * application's configuration.
	 *
	 * @param builder the {@link JDABuilder} instance to be configured. It is used to set up
	 *                various settings for the JDA (Java Discord API) instance, such as
	 *                enabling intents, configuring caching, setting member cache policies,
	 *                defining online status, and initializing activity configurations.
	 */
	private void configureBuilder(JDABuilder builder) {
		if (Objects.nonNull(properties.getSettings())) {
			Settings settings = properties.getSettings();

			builder.enableIntents(settings.getGatewayIntents());
			builder.enableCache(settings.getCacheFlags());
			builder.setMemberCachePolicy(MemberCachePolicy.ALL);
			builder.setStatus(settings.getOnlineStatus());
			builder.setCompression(Compression.ZLIB);
			builder.setChunkingFilter(ChunkingFilter.ALL);
			builder.setAutoReconnect(true);

			if (settings.isEnabledChunking() && settings.getChunkSize() > 0) {
				builder.setLargeThreshold(settings.getChunkSize());
			}
		}

		if (Objects.nonNull(properties.getActivity())) {
			org.universalcube.spring_starter_discord.properties.Activity activity = properties.getActivity();

			if (Objects.nonNull(activity.getUrl())) {
				builder.setActivity(Activity.of(activity.getType(), activity.getName(), activity.getUrl()));
			} else {
				builder.setActivity(Activity.of(activity.getType(), activity.getName()));
			}
		}
	}

	/**
	 * Configures the provided {@link DefaultShardManagerBuilder} instance with application-specific properties
	 * such as gateway intents, cache settings, member cache policy, online status, compression, chunking options,
	 * and activity settings.
	 *
	 * @param builder the {@link DefaultShardManagerBuilder} instance to be configured. This builder is used
	 *                to set up the ShardManager for the Discord bot with various configurations based on application properties.
	 */
	private void configureBuilder(DefaultShardManagerBuilder builder) {
		if (Objects.nonNull(properties.getSettings())) {
			Settings settings = properties.getSettings();

			builder.enableIntents(settings.getGatewayIntents());
			builder.enableCache(settings.getCacheFlags());
			builder.setMemberCachePolicy(MemberCachePolicy.ALL);
			builder.setStatus(settings.getOnlineStatus());
			builder.setCompression(Compression.ZLIB);
			builder.setChunkingFilter(ChunkingFilter.ALL);
			builder.setAutoReconnect(true);

			if (settings.isEnabledChunking() && settings.getChunkSize() > 0) {
				builder.setLargeThreshold(settings.getChunkSize());
			}
		}

		if (Objects.nonNull(properties.getActivity())) {
			org.universalcube.spring_starter_discord.properties.Activity activity = properties.getActivity();

			if (Objects.nonNull(activity.getUrl())) {
				builder.setActivity(Activity.of(activity.getType(), activity.getName(), activity.getUrl()));
			} else {
				builder.setActivity(Activity.of(activity.getType(), activity.getName()));
			}
		}
	}

	/**
	 * Gracefully shuts down all JDA-related components, ensuring proper cleanup of resources.
	 * <p>
	 * This method is annotated with {@code @PreDestroy}, indicating it is invoked during the
	 * application's shutdown process. It handles the termination of both {@link ShardManager}
	 * and {@link JDA} instances created for interacting with the Discord API.
	 * <p>
	 * The shutdown process includes:
	 * - Logging the shutdown operation for visibility.
	 * - Iterating over all available {@link ShardManager} instances and invoking their {@code shutdown()} method.
	 * - Iterating over all {@link JDA} instances directly and invoking their {@code shutdown()} method.
	 */
	@PreDestroy
	public void shutdown() {
		log.info("Shutting down JDA manager.");
		shardManagers.forEach(ShardManager::shutdown);
		jdaInstances.forEach(JDA::shutdown);
	}
}
