package org.universalcube.spring_starter_discord.collections.specials;

import java.io.Serializable;

public record Quadruple<T, U, V, W>(T first, U second, V third, W fourth) implements Serializable {
}