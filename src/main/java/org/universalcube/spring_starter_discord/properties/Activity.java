package org.universalcube.spring_starter_discord.properties;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import org.springframework.util.StringUtils;

/**
 * Represents the activity settings for a Discord bot.
 * The activity indicates what the bot is doing, such as playing a game or watching content.
 * This class is part of the configuration properties and allows customization of the activity displayed.
 * <p>
 * The class automatically initializes and validates its properties upon construction.
 * If the activity is enabled, it ensures the name and other necessary parameters are properly set
 * and throws exceptions for invalid configurations.
 * </p>
 */
@Data
public final class Activity {
	private boolean enabled = false;
	private String name;
	private String url;
	private ActivityType type;

	@PostConstruct
	private void init() {
		this.enabled = shouldEnableActivity();
		if (enabled) {
			validateActivityParameters();
		}
	}

	private boolean shouldEnableActivity() {
		return StringUtils.hasText(name) && type != null;
	}

	private void validateActivityParameters() {
		if (!StringUtils.hasText(name))
			throw new IllegalArgumentException("Activity name cannot be null or empty");
		if (type == ActivityType.WATCHING && !StringUtils.hasText(url))
			throw new IllegalArgumentException("Activity url cannot be null or empty for type WATCHING");
	}
}