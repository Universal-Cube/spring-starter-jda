package org.universalcube.spring_starter_discord.collections.pairs;

import java.util.Objects;

/**
 * An immutable implementation of the {@link Pair} interface that represents a pair of two associated objects.
 * This class ensures that the key and value are not null and cannot be modified after initialization.
 *
 * @param <K> the type of the first object, referred to as the key
 * @param <V> the type of the second object, referred to as the value
 *            <p>
 *            The {@code ImmutablePair} is a record class that provides a simple, immutable implementation
 *            for holding a pair of objects. Once created, neither the key nor the value can be changed.
 *            This is in contrast to {@code MutablePair}, which allows modification of its key and value.
 *            <p>
 *            Functionalities:
 *            - Provides access to the key and value through its accessor methods.
 *            - Offers a factory method {@code of} for convenient instance creation.
 *            <p>
 *            This implementation enforces immutability, ensuring that the pair is thread-safe and
 *            reliable in concurrent environments.
 */
public record ImmutablePair<K, V>(K key, V value) implements Pair<K, V> {
	public ImmutablePair {
		Objects.requireNonNull(key, "Key cannot be null");
		Objects.requireNonNull(value, "Value cannot be null");
	}

	public static <S, U> ImmutablePair<S, U> of(S key, U value) {
		return new ImmutablePair<>(key, value);
	}
}
