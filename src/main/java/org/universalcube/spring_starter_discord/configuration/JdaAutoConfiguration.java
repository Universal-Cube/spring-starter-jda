package org.universalcube.spring_starter_discord.configuration;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.universalcube.spring_starter_discord.properties.JdaConfigurationProperties;

import java.util.Objects;

@Getter
@AutoConfiguration
@EnableConfigurationProperties(JdaConfigurationProperties.class)
@ConditionalOnProperty(prefix = "spring.jda", name = "enabled", havingValue = "true", matchIfMissing = true)
public class JdaAutoConfiguration {
	private final JdaConfigurationProperties properties;

	public JdaAutoConfiguration(JdaConfigurationProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean(JDA.class)
	public JDA jda() {
		JDABuilder builder = JDABuilder.createDefault(properties.getToken());

		if (Objects.nonNull(properties.getSettings())) {
			builder.enableIntents(properties.getSettings().getGatewayIntents());
			builder.enableCache(properties.getSettings().getCacheFlags());
			builder.setMemberCachePolicy(MemberCachePolicy.ALL);
			builder.setStatus(properties.getSettings().getOnlineStatus());
			builder.setCompression(Compression.ZLIB);
		}

		if (Objects.nonNull(properties.getActivity())) {
			if (Objects.nonNull(properties.getActivity().getUrl())) {
				builder.setActivity(Activity.of(properties.getActivity().getType(), properties.getActivity().getName(), properties.getActivity().getUrl()));
			} else {
				builder.setActivity(Activity.of(properties.getActivity().getType(), properties.getActivity().getName()));
			}
		}

		return builder.build();
	}
}
