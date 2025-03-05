package org.universalcube.spring_starter_discord.discord.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Utility class for handling operations related to emojis within a Discord bot environment.
 * The class provides methods for parsing, retrieving, and formatting emojis, as well as creating embed builders
 * related to emoji properties.
 */
public final class EmojiUtils {

	/**
	 * Parses a Unicode string representation of an emoji and converts it into an Emoji instance.
	 *
	 * @param unicode the Unicode string representing the emoji to be parsed
	 * @return {@link Emoji}  the Emoji instance corresponding to the given Unicode string
	 */
	@NonNull
	public static Emoji parseFromUnicode(String unicode) {
		return Emoji.fromUnicode(unicode);
	}

	/**
	 * Retrieves a specific guild emoji by its unique identifier.
	 * If the specified emoji is not found within the guild, returns null.
	 *
	 * @param guild the Discord guild from which to retrieve the emoji
	 * @param emojiId the unique identifier of the emoji to retrieve
	 * @return {@link Emoji} the Emoji instance corresponding to the specified identifier, or null if not found
	 */
	@Nullable
	public static Emoji parseGuildEmoji(Guild guild, String emojiId) {
		return guild.retrieveEmojis().complete()
				.stream()
				.filter(e -> e.getId().equals(emojiId))
				.findFirst()
				.orElse(null);
	}

	/**
	 * Creates an {@link EmbedBuilder} configured with the properties of the given emoji.
	 * The embed will contain the emoji's name as the title and its formatted representation as the description.
	 *
	 * @param emoji the Emoji instance whose properties will be used to create the embed
	 * @return {@link EmbedBuilder}  an EmbedBuilder instance representing the provided emoji
	 */
	public static EmbedBuilder createEmojiEmbed(Emoji emoji) {
		return new EmbedBuilder()
				.setTitle(emoji.getName())
				.setDescription(emoji.getFormatted());
	}

	/**
	 * Retrieves the name of the given emoji.
	 *
	 * @param emoji the Emoji instance whose name is to be retrieved
	 * @return {@link String}  the name of the provided Emoji
	 */
	public static String getEmojiName(Emoji emoji) {
		return emoji.getName();
	}

	/**
	 * Retrieves the formatted representation of the given emoji.
	 *
	 * @param emoji the Emoji instance from which to retrieve the formatted representation
	 * @return {@link String} a String containing the formatted representation of the provided emoji
	 */
	public static String getEmojiFormatted(Emoji emoji) {
		return emoji.getFormatted();
	}
}
