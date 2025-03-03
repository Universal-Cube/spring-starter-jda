package org.universalcube.spring_starter_discord.discord.permissions;

import org.universalcube.spring_starter_discord.properties.JdaConfigurationProperties;
import org.universalcube.spring_starter_discord.properties.Owners;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class JdaPermissionUtils {
	private final Owners ownersSettings;

	public JdaPermissionUtils(JdaConfigurationProperties properties) {
		if (Objects.isNull(properties) || Objects.isNull(properties.getOwners())) {
			throw new IllegalArgumentException("Properties cannot be null");
		}
		this.ownersSettings = properties.getOwners();
	}

	public boolean isEnabled() {
		return this.ownersSettings.isEnabled();
	}

	public boolean isRoot(String userId) {
		return ownersSettings.getRootId() != null && ownersSettings.getRootId().equals(userId);
	}

	public boolean isOwner(String userId) {
		return ownersSettings.getIds().contains(userId) || isRoot(userId);
	}

	public String getRoot() {
		return ownersSettings.getRootId();
	}

	public Set<String> getOwners() {
		return Collections.unmodifiableSet(ownersSettings.getIds());
	}
}
