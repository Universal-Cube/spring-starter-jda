package org.universalcube.spring_starter_discord.collections.specials;

import java.io.Serializable;

/**
 * A generic immutable data structure representing a tuple of three elements.
 * This class can store three values of potentially different types.
 * The order of the elements is maintained as provided during object creation.
 *
 * @param <T> the type of the first element
 * @param <U> the type of the second element
 * @param <V> the type of the third element
 */
public record Triple<T, U, V>(T first, U second, V third) implements Serializable {
}