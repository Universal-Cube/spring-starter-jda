package org.universalcube.spring_starter_discord.discord.listener;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ButtonRegistry {
	private final Map<String, Consumer<ButtonInteractionEvent>> buttonMap = new ConcurrentHashMap<>();

	@EventListener(ButtonInteractionEvent.class)
	public void onButtonInteraction(@NonNull ButtonInteractionEvent event) {
		Consumer<ButtonInteractionEvent> consumer = this.buttonMap.get(event.getComponentId());
		if (consumer != null) {
			consumer.accept(event);
		}
	}

	public ButtonRegistry registerButton(Button button, Consumer<ButtonInteractionEvent> consumer) {
		return this.registerButton(button.getId(), consumer);
	}

	public ButtonRegistry registerButton(String id, Consumer<ButtonInteractionEvent> consumer) {
		this.buttonMap.put(id, consumer);
		return this;
	}

	public void unregisterButton(Button button) {
		this.buttonMap.remove(button.getId());
	}

	public void unregisterButtons(Button... buttons) {
		for (Button button : buttons) {
			this.unregisterButton(button);
		}
	}
}
