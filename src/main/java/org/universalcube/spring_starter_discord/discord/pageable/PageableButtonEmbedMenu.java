package org.universalcube.spring_starter_discord.discord.pageable;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageEditAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.springframework.stereotype.Component;
import org.universalcube.spring_starter_discord.discord.listener.ButtonRegistry;
import org.universalcube.spring_starter_discord.discord.utils.BotConstants;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public abstract class PageableButtonEmbedMenu extends PageableMenu {
	private final ScheduledExecutorService scheduler;
	private final ButtonRegistry buttonRegistry;
	private final SecureRandom random = new SecureRandom();
	private ScheduledFuture<?> scheduledFuture;
	private Button forwardButton;
	private Button backwardButton;
	private InteractionHook hook;

	public PageableButtonEmbedMenu(ScheduledExecutorService scheduler, ButtonRegistry buttonRegistry) {
		this.scheduler = scheduler;
		this.buttonRegistry = buttonRegistry;
	}

	private void initializeButtons() {
		this.backwardButton = Button.primary(random.nextLong() + "-previous", BotConstants.BACK_ARROW_EMOJI).asDisabled();
		this.forwardButton = Button.primary(random.nextLong() + "-forward", BotConstants.FORWARD_ARROW_EMOJI);
		this.buttonRegistry.registerButton(this.backwardButton, this::previousPage)
				.registerButton(this.forwardButton, this::nextPage);
	}

	public void editInitialInteraction(InteractionHook hook) {
		MessageEmbed embed = this.createPage(super.currentPage.get());
		initializeButtons();
		WebhookMessageEditAction<Message> webhookMessageEditAction = hook.editOriginalEmbeds(embed);

		if (Objects.nonNull(this.forwardButton))
			webhookMessageEditAction.setActionRow(this.backwardButton, this.forwardButton);

		if (super.maxPage > 1)
			this.scheduleDeletion();

		webhookMessageEditAction.queue();
		this.hook = hook;
	}

	public void sendInitialMessage(GenericCommandInteractionEvent event, boolean ephemeral) {
		MessageEmbed embed = this.createPage(super.currentPage.get());
		initializeButtons();
		ReplyCallbackAction replyCallbackAction = event.replyEmbeds(embed)
				.setEphemeral(ephemeral);

		if (Objects.nonNull(this.forwardButton))
			replyCallbackAction.addActionRow(this.backwardButton, this.forwardButton);

		if (super.maxPage > 1)
			this.scheduleDeletion();

		replyCallbackAction.queue();
		this.hook = event.getHook();
	}

	public abstract MessageEmbed createPage(int page);

	public void nextPage(ButtonInteractionEvent event) {
		int initialPage = this.currentPage.get();
		int newPage = super.nextPage();
		if (newPage != initialPage)
			this.render(newPage, event);
	}

	public void previousPage(ButtonInteractionEvent event) {
		int initialPage = this.currentPage.get();
		int newPage = super.previousPage();
		if (newPage != initialPage)
			this.render(newPage, event);
	}

	@Override
	public void render(int page) {
		if (page > this.maxPage)
			return;
		MessageEmbed embed = this.createPage(page);
		if (this.maxPage > 1)
			this.updateButtonsStates(page);
		this.hook.editOriginalEmbeds(embed)
				.setActionRow(this.backwardButton, this.forwardButton)
				.queue();
	}

	public void render(int page, ButtonInteractionEvent event) {
		if (page > this.maxPage)
			return;
		MessageEmbed embed = this.createPage(page);

		if (this.maxPage > 1)
			this.updateButtonsStates(page);
		event.editMessageEmbeds(embed)
				.setActionRow(this.backwardButton, this.forwardButton)
				.queue();
	}

	private void updateButtonsStates(int page) {
		if (page == 1) {
			if (!this.backwardButton.isDisabled()) this.backwardButton = this.backwardButton.asDisabled();
			if (this.forwardButton.isDisabled()) this.forwardButton = this.forwardButton.asEnabled();
		} else if (page >= this.maxPage) {
			if (this.backwardButton.isDisabled()) this.backwardButton = this.backwardButton.asEnabled();
			if (!this.forwardButton.isDisabled()) this.forwardButton = this.forwardButton.asDisabled();
		} else {
			if (this.backwardButton.isDisabled()) this.backwardButton = this.backwardButton.asEnabled();
			if (this.forwardButton.isDisabled()) this.forwardButton = this.forwardButton.asEnabled();
		}
	}

	public void scheduleDeletion() {
		if (this.scheduledFuture != null) {
			this.scheduledFuture.cancel(false);
		}
		this.scheduledFuture = this.scheduler.schedule(() -> this.buttonRegistry.unregisterButtons(this.backwardButton, this.forwardButton), 1, TimeUnit.HOURS);
	}
}
