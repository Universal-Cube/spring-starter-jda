package org.universalcube.spring_starter_discord.collections.pairs;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A mutable implementation of the {@link Pair} interface, representing a pair of two associated objects.
 * This class allows modification of its key and value, making it flexible for mutable use cases.
 *
 * @param <K> the type of the first object, referred to as the key
 * @param <V> the type of the second object, referred to as the value
 *            <p>
 *            Functionalities:
 *            - Provides a constructor for creating a pair with non-null key and value.
 *            - Offers a static factory method {@code of} for convenient instance creation.
 *            - Supports modification of the key and value using setter methods.
 *            - Provides a method to reverse the key and value.
 *            - Allows updates of the key or value via functional transformations.
 *            - Offers a method for simultaneously transforming the key and value to a new pair.
 *            - Includes comparison methods for checking equality of keys or values.
 *            - Provides reset methods for setting the pair to null or new values.
 *            - Implements custom {@code equals}, {@code hashCode}, and {@code toString} methods for object comparison and representation.
 *            <p>
 *            Thread-Safety:
 *            - All methods are synchronized to ensure thread safety for concurrent access and modifications.
 *            <p>
 *            Use Cases:
 *            - Ideal for scenarios where the key and value need to be updated after initialization.
 *            - Suitable for operations requiring temporary value changes in a pair structure.
 */
public class MutablePair<K, V> implements Pair<K, V> {
	private volatile K key;
	private volatile V value;

	/**
	 * Constructs a {@code MutablePair} with the specified key and value.
	 * Both key and value must be non-null.
	 *
	 * @param key   the key of the pair, must not be null
	 * @param value the value of the pair, must not be null
	 * @throws NullPointerException if the key or value is null
	 */
	public MutablePair(K key, V value) {
		this.key = Objects.requireNonNull(key, "Key cannot be null");
		;
		this.value = Objects.requireNonNull(value, "Value cannot be null");
		;
	}

	/**
	 * Creates a new instance of {@code MutablePair} with the specified key and value.
	 * Both the key and the value are allowed to be null.
	 *
	 * @param <S> the type of the key of the pair
	 * @param <U> the type of the value of the pair
	 * @param key the key of the pair, can be null
	 * @param value the value of the pair, can be null
	 * @return a new {@code MutablePair} instance containing the provided key and value
	 */
	public static <S, U> MutablePair<S, U> of(S key, U value) {
		return new MutablePair<>(key, value);
	}

	/**
	 * Retrieves the key of this {@code MutablePair}.
	 * This method is synchronized to ensure thread safety when accessing the key.
	 *
	 * @return the key of the pair, which may be null depending on the state of the {@code MutablePair}.
	 */
	@Override
	public synchronized K key() {
		return key;
	}

	/**
	 * Retrieves the key of this {@code MutablePair}.
	 * This method is synchronized to ensure thread safety when accessing the key.
	 *
	 * @return the key of the pair, which may be null depending on the state of the {@code MutablePair}.
	 */
	public K getKey() {
		return key;
	}

	/**
	 * Sets the key of this {@code MutablePair}.
	 * This method is synchronized to ensure thread safety when modifying the key.
	 *
	 * @param key the new key to set, which may be null depending on the intended state of the {@code MutablePair}.
	 */
	public synchronized void setKey(K key) {
		this.key = key;
	}

	/**
	 * Retrieves the value of this {@code MutablePair}.
	 * This method is synchronized to ensure thread safety when accessing the value.
	 *
	 * @return the value of the pair, which may be null depending on the state of the {@code MutablePair}.
	 */
	@Override
	public synchronized V value() {
		return value;
	}

	/**
	 * Retrieves the value of this {@code MutablePair}.
	 * This method is synchronized to ensure thread safety when accessing the value.
	 *
	 * @return the value of the pair, which may be null depending on the state of the {@code MutablePair}.
	 */
	public V getValue() {
		return value;
	}

	/**
	 * Sets the value of this {@code MutablePair}.
	 * This method is synchronized to ensure thread safety when modifying the value.
	 *
	 * @param value the new value to set, which may be null depending on the intended state of the {@code MutablePair}.
	 */
	public synchronized void setValue(V value) {
		this.value = value;
	}

	/**
	 * Reverses the key and value of this {@code MutablePair}.
	 * The method creates a new {@code MutablePair} instance where the current value becomes the key
	 * and the current key becomes the value.
	 *
	 * @return a new {@code MutablePair} instance with the roles of key and value swapped.
	 */
	public synchronized MutablePair<V, K> reverse() {
		return new MutablePair<>(value, key);
	}

	/**
	 * Updates the key of this {@code MutablePair} by applying the provided updater function.
	 * The updated key must not be null, as the method enforces this through a validation check.
	 * This method is synchronized to ensure thread safety during the update operation.
	 *
	 * @param updater a {@code Function} that takes the current key as input
	 *                and returns the new key. The function must not produce a null result.
	 * @return the current {@code MutablePair} instance with the updated key.
	 * @throws NullPointerException if the updater function is null or if the updated key is null.
	 */
	public synchronized MutablePair<K, V> updateKey(Function<K, K> updater) {
		this.key = Objects.requireNonNull(updater.apply(this.key), "Updated key cannot be null");
		return this;
	}

	/**
	 * Updates the value of this {@code MutablePair} by applying the provided updater function.
	 * The updated value must not be {@code null}, as the method enforces this through a validation check.
	 * This method is synchronized to ensure thread safety during the update operation.
	 *
	 * @param updater a {@code Function} that takes the current value as input
	 *                and returns the new value. The function must not produce a {@code null} result.
	 * @return the current {@code MutablePair} instance with the updated value.
	 * @throws NullPointerException if the updater function is {@code null} or if the updated value is {@code null}.
	 */
	public synchronized MutablePair<K, V> updateValue(Function<V, V> updater) {
		this.value = Objects.requireNonNull(updater.apply(this.value), "Updated value cannot be null");
		return this;
	}

	/**
	 * Applies a transformation to the current key and value of this {@code MutablePair} and returns a new {@code MutablePair}.
	 * The transformation is defined by the provided {@code BiFunction}, which processes the key and value and returns
	 * a new {@code MutablePair}. The resulting pair cannot be null.
	 *
	 * @param transformer a {@code BiFunction} that takes the current key and value as input and generates a new {@code MutablePair}.
	 *                    The function must not return a null value.
	 * @return a new {@code MutablePair} instance created from the key and value as transformed by the {@code transformer}.
	 * @throws NullPointerException if the {@code transformer} function is null or if it returns a null result.
	 */
	public synchronized MutablePair<K, V> map(BiFunction<K, V, MutablePair<K, V>> transformer) {
		return Objects.requireNonNull(transformer.apply(key, value), "Transformed pair cannot be null");
	}

	/**
	 * Compares the specified key with the current key of this {@code MutablePair} for equality.
	 * The comparison uses {@link Objects#equals(Object, Object)} to determine equality.
	 * This method is thread-safe due to the synchronized modifier.
	 *
	 * @param otherKey the key to compare with the current key of the pair. It may be null.
	 * @return {@code true} if the specified key is equal to the current key; {@code false} otherwise.
	 */
	public synchronized boolean isKeyEqual(K otherKey) {
		return Objects.equals(this.key, otherKey);
	}

	/**
	 * Compares the specified value with the current value of this {@code MutablePair} for equality.
	 * The comparison uses {@link Objects#equals(Object, Object)} to determine equality.
	 * This method is thread-safe due to the synchronized modifier.
	 *
	 * @param otherValue the value to compare with the current value of the pair. It may be null.
	 * @return {@code true} if the specified value is equal to the current value; {@code false} otherwise.
	 */
	public synchronized boolean isValueEqual(V otherValue) {
		return Objects.equals(this.value, otherValue);
	}

	/**
	 * Resets the key and value of this {@code MutablePair} to {@code null}.
	 * After this method is invoked, both the key and value of the pair will be unset.
	 * This method is synchronized to ensure thread safety during the reset operation.
	 */
	public synchronized void reset() {
		this.key = null;
		this.value = null;
	}

	/**
	 * Resets the key and value of this {@code MutablePair} to the specified new values.
	 * Both the new key and the new value must not be null.
	 *
	 * @param newKey   the new key to set, must not be null
	 * @param newValue the new value to set, must not be null
	 * @throws NullPointerException if the new key or new value is null
	 */
	public synchronized void reset(K newKey, V newValue) {
		this.key = Objects.requireNonNull(newKey, "Key cannot be null");
		this.value = Objects.requireNonNull(newValue, "Value cannot be null");
	}

	/**
	 * Compares this {@code MutablePair} instance with another object for equality.
	 * The comparison checks whether the provided object is a {@code MutablePair}
	 * and if the key and value of both pairs are equal.
	 *
	 * @param obj the object to compare with this {@code MutablePair}. It may be
	 *            null or any object.
	 * @return {@code true} if the specified object is a {@code MutablePair} and
	 *         its key and value are equal to the key and value of this instance;
	 *         {@code false} otherwise.
	 */
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

	/**
	 * Computes and returns the hash code for this {@code MutablePair} instance.
	 * The hash code is calculated based on the key and value of the pair using
	 * {@link Objects#hash(Object...)} ensuring consistency with the {@link #equals(Object)} method.
	 *
	 * @return an integer hash code value for this {@code MutablePair}, taking both
	 *         the key and value into account.
	 */
	@Override
	public synchronized int hashCode() {
		return Objects.hash(this.key, this.value);
	}

	/**
	 * Returns a string representation of the {@code MutablePair} instance. The representation
	 * includes the key and value held by the pair in the format:
	 * "MutablePair{key=%s, value=%s}".
	 *
	 * @return a string representing the current state of the mutable pair,
	 *         showing its key and value.
	 */
	@Override
	public synchronized String toString() {
		return "MutablePair{key=%s, value=%s}".formatted(key, value);
	}
}
