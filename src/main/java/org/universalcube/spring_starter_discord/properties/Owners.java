package org.universalcube.spring_starter_discord.properties;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the configuration properties for defining owner identities in a Discord bot setup.
 * <p>
 * This class includes the ability to manage a primary root owner along with a collection of other owner IDs.
 * A root owner (specified by `rootId`) holds special significance and optionally determines
 * if specific functionalities are enabled at a higher privilege.
 * <p>
 * The set of owner IDs (`ids`) represents additional users with elevated permissions, apart from the root owner.
 *
 */
@Data
public final class Owners {
	private String rootId;
	private Set<String> ids = new HashSet<>();

	private boolean isRootEnabled() {
		return rootId != null && !rootId.isEmpty();
	}

	/**
	 * Determines whether any owner IDs are defined in the configuration.
	 *
	 * @return {@code true} if the set of owner IDs is not empty; {@code false} otherwise.
	 */
	public boolean isEnabled() {
		return !ids.isEmpty();
	}
}
