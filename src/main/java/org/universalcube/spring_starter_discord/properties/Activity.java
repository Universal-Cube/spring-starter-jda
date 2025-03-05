package org.universalcube.spring_starter_discord.properties;

import jakarta.annotation.PostConstruct;
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
public final class Activity {
	private boolean enabled = false;
	private String name;
	private String url;
	private ActivityType type;

	/**
	 * Determines whether the activity is enabled for the Discord bot.
	 *
	 * @return true if the activity is enabled; false otherwise.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets the enabled state of the activity.
	 *
	 * @param enabled true to enable the activity; false to disable it.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Retrieves the name associated with the activity configuration.
	 *
	 * @return the name of the activity, or null if no name has been set.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the activity for the Discord bot's configuration.
	 *
	 * @param name the name of the activity to be displayed. It must not be null or empty if activity is enabled.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Retrieves the URL associated with the activity configuration.
	 *
	 * @return the URL of the activity, or null if no URL has been set.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the URL associated with the activity for the Discord bot's configuration.
	 *
	 * @param url the URL of the activity to be displayed. It must not be null or empty
	 *            if the activity type requires a URL for proper configuration.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Retrieves the type of activity configured for the Discord bot.
	 *
	 * @return the activity type, or null if no type has been set.
	 */
	public ActivityType getType() {
		return type;
	}

	/**
	 * Sets the type of activity for the Discord bot's configuration.
	 *
	 * @param type the activity type to be set. It must not be null and should match
	 *             valid activity types for the desired configuration.
	 */
	public void setType(ActivityType type) {
		this.type = type;
	}

	/**
	 * Initializes the activity configuration for the Discord bot after dependency injection is completed.
	 * <p>
	 * This method ensures the activity is enabled or disabled based on the evaluation of its parameters.
	 * If the activity is deemed to be enabled, it further validates the required configuration parameters
	 * to confirm they meet the criteria for a valid activity setup.
	 * <p>
	 * - If the activity is not currently enabled, the method evaluates it using the {@code shouldEnableActivity()} logic.
	 * - When enabled, the parameters are validated using {@code validateActivityParameters()}.
	 */
	@PostConstruct
	private void init() {
		if (!this.enabled)
			this.enabled = shouldEnableActivity();

		if (enabled)
			validateActivityParameters();
	}

	/**
	 * Determines whether the activity should be enabled based on its name and type.
	 * The activity is considered enabled if it has a non-empty name and a non-null type.
	 *
	 * @return {@code true} if the activity should be enabled; {@code false} otherwise.
	 */
	private boolean shouldEnableActivity() {
		return StringUtils.hasText(name) && type != null;
	}

	/**
	 * Validates the parameters of the activity to ensure they meet the requirements for a valid configuration.
	 * <p>
	 * This method performs the following validations:
	 * - Checks if the activity name is neither null nor empty. If invalid, an {@link IllegalArgumentException} is thrown.
	 * - If the activity type is {@code WATCHING}, verifies that the activity URL is neither null nor empty.
	 *   If invalid, an {@link IllegalArgumentException} is thrown.
	 * <p>
	 * @throws IllegalArgumentException if any validation fails.
	 */
	private void validateActivityParameters() {
		if (!StringUtils.hasText(name))
			throw new IllegalArgumentException("Activity name cannot be null or empty");
		if (type == ActivityType.WATCHING && !StringUtils.hasText(url))
			throw new IllegalArgumentException("Activity url cannot be null or empty for type WATCHING");
	}


	/**
	 * Compares this activity with the specified object for equality.
	 * Returns true if the specified object is also an instance of Activity
	 * and all fields (enabled, name, url, and type) are equal. Otherwise,
	 * delegates the equality check to the superclass.
	 *
	 * @param obj the object to be compared for equality with this activity.
	 * @return {@code true} if the specified object is equal to this activity; {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Activity activity) {
			return this.enabled == activity.enabled &&
					this.name.equals(activity.name) &&
					this.url.equals(activity.url) &&
					this.type.equals(activity.type);
		}

		return super.equals(obj);
	}

	/**
	 * Returns a string representation of the `Activity` object.
	 * The string includes the values of the fields: `enabled`, `name`, `url`, and `type`.
	 *
	 * @return a formatted string representation of the `Activity` object.
	 */
	@Override
	public String toString() {
		return "ActivityConfiguration(enabled=%s, name='%s', url='%s', type=%s)"
				.formatted(enabled, name, url, type);
	}
}