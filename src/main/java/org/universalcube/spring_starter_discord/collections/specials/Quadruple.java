package org.universalcube.spring_starter_discord.collections.specials;

import java.io.Serializable;

/**
 * A generic immutable data structure representing a tuple of four elements.
 * This class can store four values of potentially different types.
 * The order of the elements is maintained as provided during object creation.
 *
 * @param <T> the type of the first element
 * @param <U> the type of the second element
 * @param <V> the type of the third element
 * @param <W> the type of the fourth element
 */
public record Quadruple<T, U, V, W>(T first, U second, V third, W fourth) implements Serializable {
}