package org.universalcube.spring_starter_discord.configuration;

import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
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

@Getter
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(JdaConfigurationProperties.class)
public class JdaAutoConfiguration {
	private final JdaConfigurationProperties properties;
	private final List<ShardManager> shardManagers = new ArrayList<>();
	private final List<JDA> jdaInstances = new ArrayList<>();

	public JdaAutoConfiguration(JdaConfigurationProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty("spring.jda.token")
	public JdaConfigurationProperties jdaConfigurationProperties() {
		return this.properties;
	}

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

	private ShardManager createShardedInstance() {
		Sharding sharding = properties.getSharding();
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(properties.getToken());

		configureBuilder(builder);
		builder.setShardsTotal(sharding.getShardCount());

		ShardManager shardManager = builder.build();
		shardManagers.add(shardManager);
		return shardManager;
	}

	private JDA createSingleInstance() throws InterruptedException {
		JDABuilder builder = JDABuilder.createDefault(properties.getToken());

		configureBuilder(builder);

		JDA jda = builder.build().awaitReady();
		jdaInstances.add(jda);
		return jda;
	}

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

	@PreDestroy
	public void shutdown() {
		log.info("Shutting down JDA manager.");
		shardManagers.forEach(ShardManager::shutdown);
		jdaInstances.forEach(JDA::shutdown);
	}
}
