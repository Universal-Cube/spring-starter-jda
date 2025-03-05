package org.universalcube.spring_starter_discord.discord.listener;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * The ButtonRegistry class is responsible for managing button interactions by associating
 * buttons (identified by their IDs or instances) with specific consumer functions that
 * handle {@link ButtonInteractionEvent}s. This class provides a framework for registering
 * and unregistering buttons, enabling dynamic event handling for user interactions.
 * <p>
 * It uses a thread-safe map for storing and retrieving button ID-to-consumer mappings.
 */
public class ButtonRegistry {
	private final Map<String, Consumer<ButtonInteractionEvent>> buttonMap = new ConcurrentHashMap<>();

	/**
	 * Handles a {@link ButtonInteractionEvent} by invoking the registered consumer function
	 * for the button associated with the event's component ID. If no consumer is registered
	 * for the given ID, the event is ignored.
	 *
	 * @param event the {@link ButtonInteractionEvent} that contains information about the button interaction,
	 *              including the component ID which is used to locate the appropriate consumer function.
	 */
	@EventListener(ButtonInteractionEvent.class)
	public void onButtonInteraction(@NonNull ButtonInteractionEvent event) {
		Consumer<ButtonInteractionEvent> consumer = this.buttonMap.get(event.getComponentId());
		if (consumer != null) {
			consumer.accept(event);
		}
	}

	/**
	 * Registers a button and its corresponding consumer function to handle interaction events.
	 * This method associates a {@link Button} instance with a {@link Consumer}, which
	 * will be invoked whenever a {@link ButtonInteractionEvent} for the specified button is triggered.
	 * Internally, the button ID is used to uniquely register the consumer.
	 *
	 * @param button   the {@link Button} to be registered. The button must have a unique ID.
	 * @param consumer the {@link Consumer} to handle the {@link ButtonInteractionEvent} for the specified button.
	 * @return the current {@link ButtonRegistry} instance for method chaining.
	 */
	public ButtonRegistry registerButton(Button button, Consumer<ButtonInteractionEvent> consumer) {
		return this.registerButton(button.getId(), consumer);
	}

	/**
	 * Registers a button identified by its unique ID and associates it with a consumer function
	 * that will handle corresponding {@link ButtonInteractionEvent}s. This method allows you to
	 * dynamically manage button interactions by linking an ID with a specified event handler.
	 *
	 * @param id       the unique identifier for the button to be registered.
	 * @param consumer the {@link Consumer} function that handles the {@link ButtonInteractionEvent}
	 *                 triggered for the specified button ID.
	 * @return the current {@link ButtonRegistry} instance to allow method chaining.
	 */
	public ButtonRegistry registerButton(String id, Consumer<ButtonInteractionEvent> consumer) {
		this.buttonMap.put(id, consumer);
		return this;
	}

	/**
	 * Unregisters a button from the registry, removing its association with any previously registered
	 * consumer function. The button is identified by its unique ID, and once unregistered, interaction
	 * events for the button will no longer be handled.
	 *
	 * @param button the {@link Button} to be unregistered. The button must have a unique ID associated
	 *               with it in the registry to be successfully removed.
	 */
	public void unregisterButton(Button button) {
		this.buttonMap.remove(button.getId());
	}

	/**
	 * Unregisters one or more buttons from the registry. Each button is removed
	 * by invoking the {@code unregisterButton} method internally for every provided
	 * {@link Button} instance. Once unregistered, the interaction events for
	 * the specified buttons will no longer be handled.
	 *
	 * @param buttons the buttons to be unregistered. Each {@link Button} must have
	 *                a unique ID associated with it in the registry to be successfully removed.
	 */
	public void unregisterButtons(Button... buttons) {
		for (Button button : buttons) {
			this.unregisterButton(button);
		}
	}
}
