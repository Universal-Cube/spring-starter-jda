package org.universalcube.spring_starter_discord.discord.utils;

import net.dv8tion.jda.api.entities.emoji.Emoji;

/**
 * A utility class that contains constant values specifically used for Discord bot operations.
 * This class is designed to store emoji constants for common navigation controls.
 * <p>
 * The constants in this class are primarily focused on enhancing user interaction
 * by providing predefined emoji representations for specific actions.
 * <p>
 * This class is final and cannot be instantiated or subclassed.
 */
@SuppressWarnings("UnnecessaryUnicodeEscape")
public final class BotConstants {
	public static final Emoji BACK_ARROW_EMOJI = EmojiUtils.parseFromUnicode("\u2B05");
	public static final Emoji FORWARD_ARROW_EMOJI = Emoji.fromUnicode("\u27A1");
}
