package org.universalcube.spring_starter_discord.sets.pairs;

import java.util.Objects;

public record ImmutablePair<K, V>(K key, V value) implements Pair<K, V> {
	public ImmutablePair {
		Objects.requireNonNull(key, "Key cannot be null");
		Objects.requireNonNull(value, "Value cannot be null");
	}

	public static <S, U> ImmutablePair<S, U> of(S key, U value) {
		return new ImmutablePair<>(key, value);
	}
}
