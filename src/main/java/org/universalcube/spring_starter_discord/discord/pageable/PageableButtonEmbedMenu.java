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

/**
 * An abstract class that extends {@code PageableMenu} to provide functionality for
 * creating pageable menus that utilize interactive buttons within an embedded message.
 * This class manages forward and backward navigation controls, ensuring smooth interaction
 * with users in a pageable embed context. Subclasses are expected to provide the
 * implementation for content rendering and page creation.
 */
@Component
public abstract class PageableButtonEmbedMenu extends PageableMenu {
	private final ScheduledExecutorService scheduler;
	private final ButtonRegistry buttonRegistry;
	private final SecureRandom random = new SecureRandom();
	private ScheduledFuture<?> scheduledFuture;
	private Button forwardButton;
	private Button backwardButton;
	private InteractionHook hook;

	/**
	 * Constructs a new PageableButtonEmbedMenu with the specific scheduler and button registry.
	 * This constructor initializes the scheduler for managing asynchronous tasks and the
	 * button registry to handle interactions with buttons used for menu navigation.
	 *
	 * @param scheduler      the {@link ScheduledExecutorService} to manage scheduled tasks such as message deletion.
	 * @param buttonRegistry the {@link ButtonRegistry} to register and manage button interactions.
	 */
	public PageableButtonEmbedMenu(ScheduledExecutorService scheduler, ButtonRegistry buttonRegistry) {
		this.scheduler = scheduler;
		this.buttonRegistry = buttonRegistry;
	}

	/**
	 * Initializes navigation buttons for a pageable menu system.
	 * This method creates a backward and forward button for navigating through pages,
	 * assigns unique IDs to each button using a random value, associates corresponding
	 * emoji representations specific to the action, and registers button interactions
	 * with appropriate handler methods (`previousPage` and `nextPage`).
	 * <p>
	 * The backward button is initialized as disabled by default, while
	 * the forward button is enabled. The buttons are then registered
	 * with the button registry to listen for user interactions during
	 * the menu operation.
	 */
	private void initializeButtons() {
		this.backwardButton = Button.primary(random.nextLong() + "-previous", BotConstants.BACK_ARROW_EMOJI).asDisabled();
		this.forwardButton = Button.primary(random.nextLong() + "-forward", BotConstants.FORWARD_ARROW_EMOJI);
		this.buttonRegistry.registerButton(this.backwardButton, this::previousPage)
				.registerButton(this.forwardButton, this::nextPage);
	}

	/**
	 * Edits the original interaction message to display the initial page and buttons for navigation.
	 * This method updates the content of the original interaction response by creating an embedded
	 * message for the current page, initializing navigation buttons, and scheduling their deletion
	 * if applicable. It utilizes the provided {@link InteractionHook} to edit the message.
	 *
	 * @param hook the {@link InteractionHook} used to interact with and modify the original message
	 *             sent by the bot. This is required to perform the message update operation.
	 */
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

	/**
	 * Sends the initial message for a pageable embed menu using the provided interaction event.
	 * This method creates an embed representation of the current page, initializes navigation buttons,
	 * and sends the embed along with the buttons as an interaction response. If applicable, a deletion
	 * schedule for the buttons will also be initiated.
	 *
	 * @param event     the {@link GenericCommandInteractionEvent} used to reply with the embed and buttons.
	 *                  It represents the interaction event initiated by the user.
	 * @param ephemeral a boolean flag indicating if the response should be ephemeral.
	 *                  If true, the response will only be visible to the user who initiated the interaction.
	 */
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

	/**
	 * Creates an embedded message representing the content of a specific page.
	 * This method is abstract and should be implemented by subclasses to define
	 * the content and structure of the embed for each page.
	 *
	 * @param page the page number to create an embed for. Must be a positive integer.
	 * @return a {@link MessageEmbed} representing the content of the specified page.
	 */
	public abstract MessageEmbed createPage(int page);

	public void nextPage(ButtonInteractionEvent event) {
		int initialPage = this.currentPage.get();
		int newPage = super.nextPage();
		if (newPage != initialPage)
			this.render(newPage, event);
	}

	/**
	 * Handles the interaction for navigating to the previous page in a pageable menu system.
	 * If the current page is not the first page, this method updates the current page state
	 * to the previous page and renders its content. The provided {@code event} is used to
	 * edit the message displaying the menu.
	 *
	 * @param event the {@link ButtonInteractionEvent} triggered by the user interaction with
	 *              the navigation button. This event is used to edit the message to display
	 *              the updated page content and navigation buttons.
	 */
	public void previousPage(ButtonInteractionEvent event) {
		int initialPage = this.currentPage.get();
		int newPage = super.previousPage();
		if (newPage != initialPage)
			this.render(newPage, event);
	}

	/**
	 * Renders the content of a specific page for the pageable embed menu system.
	 * Updates the displayed message with an embedded representation of the page
	 * and manages the navigation buttons' states accordingly.
	 *
	 * @param page the page number to render. If the page number exceeds the maximum allowed,
	 *             the method will return without performing any operation.
	 */
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

	/**
	 * Renders the content of a specified page along with the interactive buttons,
	 * updating the message tied to the {@code event}. If the provided page number
	 * exceeds the maximum allowable page number, the method will exit without
	 * performing any further actions.
	 *
	 * @param page  the page number to render. If the page number is greater than the
	 *              maximum page allowed, the method will terminate early.
	 * @param event the {@link ButtonInteractionEvent} representing the interaction
	 *              triggered by a button press. This is used to update the message
	 *              with the embedded page content and navigation controls.
	 */
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

	/**
	 * Updates the states of navigation buttons based on the given page number.
	 * This method modifies the forward and backward buttons' enabled or disabled
	 * states to reflect the navigability of the current page.
	 *
	 * @param page the current page number to determine the buttons' states.
	 *             If the page is the first, the backward button is disabled, and the forward button is enabled.
	 *             If the page is the last, the forward button is disabled, and the backward button is enabled.
	 *             For all other pages, both buttons are enabled.
	 */
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

	/**
	 * Schedules the deletion of registered navigation buttons in the pageable menu system.
	 * <p>
	 * This method utilizes a scheduler to manage a delayed task that unregisters the backward
	 * and forward navigation buttons associated with the pageable menu. The task is set to run
	 * after 1 hour. If a previously scheduled deletion task exists, it is canceled before scheduling
	 * the new one. The buttons are unregistered from the {@link ButtonRegistry}, which effectively
	 * disables their functionality and removes them from tracking.
	 * <p>
	 * The method ensures that only one deletion task is active at any given time, allowing the menu
	 * system's lifecycle to be efficiently managed.
	 */
	public void scheduleDeletion() {
		if (this.scheduledFuture != null) {
			this.scheduledFuture.cancel(false);
		}
		this.scheduledFuture = this.scheduler.schedule(() -> this.buttonRegistry.unregisterButtons(this.backwardButton, this.forwardButton), 1, TimeUnit.HOURS);
	}
}
