package org.universalcube.spring_starter_discord.properties;

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
public final class Owners {
	private String rootId;
	private Set<String> ids = new HashSet<>();

	/**
	 * Constructs a new instance of the Owners class with default values.
	 * By default, the root ID is set to null, and the set of owner IDs is initialized as an empty set.
	 */
	public Owners() {
	}

	/**
	 * Constructs a new Owners instance with the specified root ID and set of owner IDs.
	 *
	 * @param rootId the root ID representing the primary owner with the highest level of permissions.
	 *               This may be null or empty if no primary root owner is configured.
	 * @param ids    the set of owner IDs representing additional users with elevated permissions.
	 *               A null or empty set indicates that no additional owners are configured.
	 */
	public Owners(String rootId, Set<String> ids) {
		this.rootId = rootId;
		this.ids = ids;
	}

	/**
	 * Retrieves the root ID of the primary owner.
	 *
	 * @return the root ID as a string, or {@code null} if not set.
	 */
	public String getRootId() {
		return rootId;
	}

	/**
	 * Sets the root ID of the primary owner for this configuration.
	 *
	 * @param rootId the root ID to be set. This represents the identifier for the primary owner
	 *               with the highest level of permissions. A null or empty value indicates
	 *               the absence of a primary root owner configuration.
	 */
	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

	/**
	 * Retrieves the set of owner IDs configured for this instance.
	 * These IDs represent additional users with elevated permissions.
	 *
	 * @return a set of strings representing the owner IDs. Returns an empty set if no IDs are configured.
	 */
	public Set<String> getIds() {
		return ids;
	}

	/**
	 * Sets the collection of owner IDs for the configuration.
	 *
	 * @param ids a set of strings representing the IDs of owners with elevated permissions.
	 *            Passing a null value or an empty set will clear the existing owner IDs.
	 */
	public void setIds(Set<String> ids) {
		this.ids = ids;
	}

	/**
	 * Determines whether the root owner functionality is enabled based on the presence of a valid root ID.
	 *
	 * @return {@code true} if the root ID is not null and not empty; {@code false} otherwise.
	 */
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

	/**
	 * Compares this Owners instance with the specified object for equality.
	 * Two Owners instances are considered equal if their root IDs and sets of owner IDs
	 * are both equal. Otherwise, the method delegates the comparison to the superclass.
	 *
	 * @param obj the object to be compared for equality with this Owners instance.
	 * @return {@code true} if the specified object is an instance of Owners and both the
	 * root ID and the set of owner IDs are equal; {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Owners owners) {
			return this.rootId.equals(owners.rootId)
					&& this.ids.equals(owners.ids);
		}
		return super.equals(obj);
	}

	/**
	 * Returns a string representation of the OwnersConfiguration object.
	 * The string includes the root ID and the set of owner IDs.
	 *
	 * @return a string representation of the current state of the OwnersConfiguration object.
	 */
	@Override
	public String toString() {
		return "OwnersConfiguration(rootId='%s', ids=%s)".formatted(rootId, ids);
	}
}
