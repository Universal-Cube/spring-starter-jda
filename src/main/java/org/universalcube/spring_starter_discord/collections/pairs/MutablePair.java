package org.universalcube.spring_starter_discord.collections.pairs;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
@Setter
public class MutablePair<K, V> implements Pair<K, V> {
	private volatile K key;
	private volatile V value;

	public MutablePair(K key, V value) {
		this.key = Objects.requireNonNull(key, "Key cannot be null");
		;
		this.value = Objects.requireNonNull(value, "Value cannot be null");
		;
	}

	public static <S, U> MutablePair<S, U> of(S key, U value) {
		return new MutablePair<>(key, value);
	}

	@Override
	public synchronized K key() {
		return key;
	}

	public synchronized void setKey(K key) {
		this.key = key;
	}

	@Override
	public synchronized V value() {
		return value;
	}

	public synchronized void setValue(V value) {
		this.value = value;
	}

	public synchronized MutablePair<V, K> reverse() {
		return new MutablePair<>(value, key);
	}

	public synchronized MutablePair<K, V> updateKey(Function<K, K> updater) {
		this.key = Objects.requireNonNull(updater.apply(this.key), "Updated key cannot be null");
		return this;
	}

	public synchronized MutablePair<K, V> updateValue(Function<V, V> updater) {
		this.value = Objects.requireNonNull(updater.apply(this.value), "Updated value cannot be null");
		return this;
	}

	public synchronized MutablePair<K, V> map(BiFunction<K, V, MutablePair<K, V>> transformer) {
		return Objects.requireNonNull(transformer.apply(key, value), "Transformed pair cannot be null");
	}

	public synchronized boolean isKeyEqual(K otherKey) {
		return Objects.equals(this.key, otherKey);
	}

	public synchronized boolean isValueEqual(V otherValue) {
		return Objects.equals(this.value, otherValue);
	}

	public synchronized void reset() {
		this.key = null;
		this.value = null;
	}

	public synchronized void reset(K newKey, V newValue) {
		this.key = Objects.requireNonNull(newKey, "Key cannot be null");
		this.value = Objects.requireNonNull(newValue, "Value cannot be null");
	}

	@Override
	public synchronized boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (obj instanceof MutablePair<?, ?> pair) {
			return Objects.equals(key, pair.key())
					&& Objects.equals(value, pair.value());
		}

		return super.equals(obj);
	}

	@Override
	public synchronized int hashCode() {
		return Objects.hash(key(), value());
	}

	@Override
	public synchronized String toString() {
		return "MutablePair{key=%s, value=%s}".formatted(key, value);
	}
}
