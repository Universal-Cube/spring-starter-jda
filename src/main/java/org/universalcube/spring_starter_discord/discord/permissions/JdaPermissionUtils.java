package org.universalcube.spring_starter_discord.discord.permissions;

import org.universalcube.spring_starter_discord.properties.JdaConfigurationProperties;
import org.universalcube.spring_starter_discord.properties.Owners;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * Utility class for handling permission-related checks for Discord bot owners and root administrators.
 * <p>
 * This class provides methods to check the permissions for users based on their roles (e.g., root owner, general owners)
 * as specified in the {@link Owners} configuration, which is retrieved via {@link JdaConfigurationProperties}.
 * It supports functionality for:
 * - Determining if the owner-related features are enabled.
 * - Checking if a specific user is a root owner.
 * - Checking if a specific user is an owner (root or general owner).
 * - Accessing the root owner's ID.
 * - Retrieving the set of all owners.
 */
public class JdaPermissionUtils {
	private final Owners ownersSettings;

	/**
	 * Constructs a new instance of {@code JdaPermissionUtils}.
	 *
	 * @param properties the configuration properties object used to set up owner-related settings
	 *                   for determining user permissions. This parameter must not be null, and its
	 *                   {@code owners} field must also not be null.
	 * @throws IllegalArgumentException if the {@code properties} parameter or its {@code owners} field is null
	 */
	public JdaPermissionUtils(JdaConfigurationProperties properties) {
		if (Objects.isNull(properties) || Objects.isNull(properties.getOwners())) {
			throw new IllegalArgumentException("Properties cannot be null");
		}
		this.ownersSettings = properties.getOwners();
	}

	/**
	 * Checks whether the owner-related features are enabled based on the current configuration.
	 *
	 * @return {@code true} if the owners configuration contains one or more owner IDs; {@code false} otherwise.
	 */
	public boolean isEnabled() {
		return this.ownersSettings.isEnabled();
	}

	/**
	 * Checks if the given user ID corresponds to the root owner.
	 *
	 * @param userId the unique identifier of the user to check
	 * @return {@code true} if the given user ID matches the root owner's ID; {@code false} otherwise
	 */
	public boolean isRoot(String userId) {
		return ownersSettings.getRootId() != null && ownersSettings.getRootId().equals(userId);
	}

	/**
	 * Determines whether the provided user ID corresponds to either a root owner
	 * or a general owner as defined in the configuration.
	 *
	 * @param userId the unique identifier of the user to check
	 * @return {@code true} if the user ID belongs to a root owner or is contained in the set of general owner IDs;
	 *         {@code false} otherwise
	 */
	public boolean isOwner(String userId) {
		return ownersSettings.getIds().contains(userId) || isRoot(userId);
	}

	/**
	 * Retrieves the root owner's identifier from the current configuration.
	 *
	 * @return the root owner's ID as a string, or {@code null} if no root ID is defined
	 */
	public String getRoot() {
		return ownersSettings.getRootId();
	}

	/**
	 * Retrieves the set of owner IDs defined in the configuration.
	 *
	 * @return an unmodifiable set of owner IDs. If no owner IDs are defined, an empty set is returned.
	 */
	public Set<String> getOwners() {
		return Collections.unmodifiableSet(ownersSettings.getIds());
	}
}
