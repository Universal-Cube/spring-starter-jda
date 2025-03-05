package org.universalcube.spring_starter_discord.discord.modules;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
public abstract class BotModule extends ListenerAdapter {
	private final static Logger log = LoggerFactory.getLogger(BotModule.class);
	private String name;
	private String description;
	private boolean enabled;
	private boolean devOnly;
	private boolean botOwnerOnly;

	public BotModule() {
		this("Unnamed Module", "No description");
	}

	public BotModule(String name, String description) {
		this(name, description, false);
	}

	public BotModule(String name, String description, boolean enabled) {
		this(name, description, enabled, false, false);
	}

	public BotModule(String name, String description, boolean enabled, boolean devOnly) {
		this(name, description, enabled, devOnly, false);
	}

	public BotModule(String name, String description, boolean enabled, boolean devOnly, boolean botOwnerOnly) {
		this.name = name;
		this.description = description;
		this.enabled = enabled;
		this.devOnly = devOnly;
		this.botOwnerOnly = botOwnerOnly;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isDevOnly() {
		return devOnly;
	}

	public void setDevOnly(boolean devOnly) {
		this.devOnly = devOnly;
	}

	public boolean isBotOwnerOnly() {
		return botOwnerOnly;
	}

	public void setBotOwnerOnly(boolean botOwnerOnly) {
		this.botOwnerOnly = botOwnerOnly;
	}

	public void enable() {
		if (!enabled) {
			this.setEnabled(true);
			this.onEnable();
			log.info("Enabled module '{}'", this.getName());
		}
	}

	public void disable() {
		if (enabled) {
			this.setEnabled(false);
			this.onDisable();
			log.info("Disabled module '{}'", this.getName());
		}
	}

	public abstract void onEnable();

	public abstract void onDisable();

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (obj instanceof BotModule module) {
			return Objects.equals(name, module.name)
					&& Objects.equals(description, module.description)
					&& Objects.equals(enabled, module.enabled)
					&& Objects.equals(devOnly, module.devOnly)
					&& Objects.equals(botOwnerOnly, module.botOwnerOnly);
		}

		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, enabled, devOnly, botOwnerOnly);
	}

	@Override
	public String toString() {
		return "BotModule{name='%s', description='%s', enabled=%b, devOnly=%b, botOwnerOnly=%b}"
				.formatted(name, description, enabled, devOnly, botOwnerOnly);
	}
}
