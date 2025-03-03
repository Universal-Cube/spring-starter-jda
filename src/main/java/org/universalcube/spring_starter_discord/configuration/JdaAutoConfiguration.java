package org.universalcube.spring_starter_discord.configuration;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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

import java.util.Objects;

@Getter
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(JdaConfigurationProperties.class)
public class JdaAutoConfiguration {
	private final JdaConfigurationProperties properties;

	public JdaAutoConfiguration(JdaConfigurationProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty("spring.jda.token")
	public ShardManager shardManager() throws InterruptedException {
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(properties.getToken());

		if (Objects.nonNull(properties.getSettings())) {
			Settings settings = properties.getSettings();

			builder.enableIntents(settings.getGatewayIntents());
			builder.enableCache(settings.getCacheFlags());
			builder.setMemberCachePolicy(MemberCachePolicy.ALL);
			builder.setStatus(settings.getOnlineStatus());
			builder.setCompression(Compression.ZLIB);
			builder.setChunkingFilter(ChunkingFilter.ALL);

			if (settings.isEnabledChunking() && settings.getChunkSize() > 0) {
				builder.setLargeThreshold(settings.getChunkSize());
				builder.setAutoReconnect(true);
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

		if (Objects.nonNull(properties.getSharding())) {
			Sharding sharding = properties.getSharding();

			if (sharding.isEnabled())
				builder.setShardsTotal(sharding.getShardCount());
		}

		return builder.build();
	}
}
