package org.universalcube.spring_starter_discord.discord.modules;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Represents an abstract module for a bot that can be enabled, disabled, and configured
 * with specific flags (e.g., developer-only, bot owner-only). It serves as a base class
 * for creating specific bot modules with associated behaviors and properties.
 * <p>
 * This class extends {@link ListenerAdapter} to allow the modules
 * to listen to and handle Discord events.
 * <p>
 * Each module has the following configurable properties:
 * - Name: a unique identifier for the module.
 * - Description: a brief description of the module's functionality.
 * - Enabled: indicates whether the module is active or not.
 * - DevOnly: specifies if the module is intended to be used only by developers.
 * - BotOwnerOnly: specifies if the module is restricted to bot owners.
 * <p>
 * Subclasses of this class must implement the abstract methods {@code onEnable()}
 * and {@code onDisable()} to define specific behavior upon enabling and disabling
 * the module, respectively.
 */
public abstract class BotModule extends ListenerAdapter {
	private final static Logger log = LoggerFactory.getLogger(BotModule.class);
	private String name;
	private String description;
	private boolean enabled;
	private boolean devOnly;
	private boolean botOwnerOnly;

	/**
	 * Default constructor for the BotModule class.
	 * <p>
	 * Initializes an instance of the BotModule with default values: the name set to
	 * "Unnamed Module" and the description set to "No description". Other internal
	 * properties such as enabled, devOnly, and botOwnerOnly are set to their default
	 * values via delegation to other constructors.
	 */
	public BotModule() {
		this("Unnamed Module", "No description");
	}

	/**
	 * Constructs a BotModule instance with the specified name and description.
	 * This constructor initializes the module's name and description fields and
	 * sets the enabled state to false by default.
	 *
	 * @param name The name of the BotModule.
	 * @param description A brief description of the BotModule's purpose or functionality.
	 */
	public BotModule(String name, String description) {
		this(name, description, false);
	}

	/**
	 * Constructs a BotModule instance with the specified name, description, and enabled state.
	 * This constructor initializes the module's name, description, and enabled state while
	 * delegating additional property initialization to another constructor.
	 *
	 * @param name The name of the BotModule.
	 * @param description A brief description of the BotModule's purpose or functionality.
	 * @param enabled Indicates whether the BotModule is enabled by default.
	 */
	public BotModule(String name, String description, boolean enabled) {
		this(name, description, enabled, false, false);
	}

	/**
	 * Constructs a BotModule instance with the specified name, description,
	 * enabled state, and developer restriction.
	 * <p>
	 * This constructor initializes the module's name, description, enabled state,
	 * and developer-only restriction while delegating additional property initialization
	 * to another constructor.
	 *
	 * @param name The name of the BotModule.
	 * @param description A brief description of the BotModule's purpose or functionality.
	 * @param enabled Indicates whether the BotModule is enabled by default.
	 * @param devOnly Specifies whether the BotModule is restricted to developers only.
	 */
	public BotModule(String name, String description, boolean enabled, boolean devOnly) {
		this(name, description, enabled, devOnly, false);
	}

	/**
	 * Constructs a BotModule instance with the specified name, description, enabled state,
	 * developer restriction, and bot owner restriction.
	 * This constructor initializes the module's fields to the provided values.
	 *
	 * @param name The name of the BotModule.
	 * @param description A brief description of the BotModule's purpose or functionality.
	 * @param enabled Indicates whether the BotModule is enabled by default.
	 * @param devOnly Specifies whether the BotModule is restricted to developers only.
	 * @param botOwnerOnly Specifies whether the BotModule is restricted to the bot owner only.
	 */
	public BotModule(String name, String description, boolean enabled, boolean devOnly, boolean botOwnerOnly) {
		this.name = name;
		this.description = description;
		this.enabled = enabled;
		this.devOnly = devOnly;
		this.botOwnerOnly = botOwnerOnly;
	}

	/**
	 * Retrieves the name of the BotModule.
	 *
	 * @return The name of the module as a String.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the BotModule.
	 *
	 * @param name The name to be assigned to the BotModule.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Retrieves the description of the BotModule.
	 *
	 * @return The description of the module as a String.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the BotModule.
	 *
	 * @param description A brief description of the BotModule's purpose or functionality.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Checks whether the BotModule is currently enabled.
	 *
	 * @return true if the BotModule is enabled; false otherwise.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets the enabled state of the BotModule.
	 *
	 * @param enabled A boolean indicating whether the BotModule should be enabled (true) or disabled (false).
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Checks if the BotModule is restricted to developers only.
	 *
	 * @return true if the BotModule is restricted to developers; false otherwise.
	 */
	public boolean isDevOnly() {
		return devOnly;
	}

	/**
	 * Sets the developer-only restriction for the BotModule.
	 * This property determines whether the module is accessible exclusively to developers.
	 *
	 * @param devOnly A boolean indicating whether the module should be restricted to developers only (true) or not (false).
	 */
	public void setDevOnly(boolean devOnly) {
		this.devOnly = devOnly;
	}

	/**
	 * Checks if the BotModule is restricted to the bot owner only.
	 *
	 * @return true if the BotModule is restricted to the bot owner; false otherwise.
	 */
	public boolean isBotOwnerOnly() {
		return botOwnerOnly;
	}

	/**
	 * Sets whether the BotModule is restricted to the bot owner only.
	 * This restriction determines if the module's functionality is exclusive to the bot owner.
	 *
	 * @param botOwnerOnly A boolean indicating whether the BotModule should be restricted to the bot owner only (true) or not (false).
	 */
	public void setBotOwnerOnly(boolean botOwnerOnly) {
		this.botOwnerOnly = botOwnerOnly;
	}

	/**
	 * Enables the module if it is currently disabled.
	 * <p>
	 * This method checks the enabled state of the BotModule and performs the following operations
	 * if the module is not already enabled:
	 * 1. Sets the module's enabled state to true via {@link #setEnabled(boolean)}.
	 * 2. Calls the {@link #onEnable()} method to perform any necessary initialization or startup actions.
	 * 3. Logs a message indicating that the module has been enabled, including the module's name.
	 * <p>
	 * This method ensures that the enable process is idempotent, meaning it does nothing if the
	 * module is already enabled.
	 * <p>
	 * Subclasses can override {@link #onEnable()} to define specific behavior that should occur
	 * when the module is enabled (e.g., registering listeners, initializing resources).
	 */
	public void enable() {
		if (!enabled) {
			this.setEnabled(true);
			this.onEnable();
			log.info("Enabled module '{}'", this.getName());
		}
	}

	/**
	 * Disables the BotModule if it is currently enabled.
	 * <p>
	 * This method checks if the module is enabled and, if so, performs the following operations:
	 * 1. Sets the module's enabled state to false via {@link #setEnabled(boolean)}.
	 * 2. Calls the {@link #onDisable()} method to perform any necessary cleanup or shutdown actions.
	 * 3. Logs a message indicating that the module has been disabled, including the module's name.
	 * <p>
	 * The specific cleanup behavior is defined in the implementation of the {@link #onDisable()} method
	 * within subclasses.
	 */
	public void disable() {
		if (enabled) {
			this.setEnabled(false);
			this.onDisable();
			log.info("Disabled module '{}'", this.getName());
		}
	}

	/**
	 * Performs initialization or startup operations when the module is enabled.
	 * <p>
	 * This method is called as part of the enabling process for the BotModule.
	 * Subclasses should implement this method to define the specific actions
	 * or behaviors to be executed when the module becomes active. Typical uses
	 * include registering event listeners, initializing resources, or setting up
	 * the internal state required for the module's functionality.
	 * <p>
	 * This method is invoked internally when the {@link #enable()} method is
	 * called or when the module is added to the module manager and the enabled
	 * state is set to true by default.
	 */
	public abstract void onEnable();

	/**
	 * Performs cleanup or shutdown operations when the module is disabled.
	 * <p>
	 * This method is called as part of the module disabling process, allowing
	 * implementations to release resources, unregister event listeners, or
	 * perform any other necessary de-initialization logic. The exact behavior
	 * should be defined in the subclass.
	 * <p>
	 * This method will be invoked internally when modules are explicitly disabled
	 * via {@link #disable()} or during a module reload by the module manager.
	 */
	public abstract void onDisable();

	/**
	 * Compares this BotModule instance with another object to determine equality.
	 * Two BotModule instances are considered equal if they have the same values
	 * for the name, description, enabled state, developer-only restriction, and
	 * bot owner-only restriction.
	 *
	 * @param obj The object to be compared for equality with this instance.
	 * @return true if the specified object is equal to this BotModule; false otherwise.
	 */
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

	/**
	 * Computes the hash code for the BotModule instance based on its name,
	 * description, enabled state, and access restrictions (developer-only and bot owner-only flags).
	 *
	 * @return The computed hash code as an integer value.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(name, description, enabled, devOnly, botOwnerOnly);
	}

	/**
	 * Returns a string representation of the BotModule instance.
	 * The string includes the name, description, enabled state, developer-only restriction,
	 * and bot owner-only restriction of the module.
	 *
	 * @return A formatted string representing the current state of the BotModule.
	 */
	@Override
	public String toString() {
		return "BotModule{name='%s', description='%s', enabled=%b, devOnly=%b, botOwnerOnly=%b}"
				.formatted(name, description, enabled, devOnly, botOwnerOnly);
	}
}
