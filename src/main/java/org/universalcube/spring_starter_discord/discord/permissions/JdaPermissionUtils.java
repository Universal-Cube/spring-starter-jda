package org.universalcube.spring_starter_discord.discord.permissions;

import org.universalcube.spring_starter_discord.properties.JdaConfigurationProperties;
import org.universalcube.spring_starter_discord.properties.Owners;

import java.util.Objects;

public class JdaPermissionUtils {
	private final Owners ownersSettings;

	public JdaPermissionUtils(JdaConfigurationProperties properties) {
		if (Objects.isNull(properties.getOwners())) {
			throw new IllegalArgumentException("Properties cannot be null");
		}
		this.ownersSettings = properties.getOwners();
	}

	public boolean isEnabled() {
		return this.ownersSettings.isEnabled();
	}
}
