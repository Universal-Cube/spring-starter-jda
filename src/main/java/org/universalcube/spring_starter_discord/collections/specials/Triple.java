package org.universalcube.spring_starter_discord.collections.specials;

import java.io.Serializable;

public record Triple<T, U, V>(T first, U second, V third) implements Serializable {
}