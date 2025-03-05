package org.universalcube.spring_starter_discord.discord.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class EmojiUtils {

	@NonNull
	public static Emoji parseFromUnicode(String unicode) {
		return Emoji.fromUnicode(unicode);
	}

	@Nullable
	public static Emoji parseGuildEmoji(Guild guild, String emojiId) {
		return guild.retrieveEmojis().complete()
				.stream()
				.filter(e -> e.getId().equals(emojiId))
				.findFirst()
				.orElse(null);
	}

	public static EmbedBuilder createEmojiEmbed(Emoji emoji) {
		return new EmbedBuilder()
				.setTitle(emoji.getName())
				.setDescription(emoji.getFormatted());
	}

	public static String getEmojiName(Emoji emoji) {
		return emoji.getName();
	}

	public static String getEmojiFormatted(Emoji emoji) {
		return emoji.getFormatted();
	}
}
