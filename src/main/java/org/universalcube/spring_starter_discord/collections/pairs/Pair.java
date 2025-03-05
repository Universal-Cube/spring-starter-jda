package org.universalcube.spring_starter_discord.collections.pairs;

import java.io.Serializable;

/**
 * A generic interface extends {@link Serializable} and representing a pair of two associated objects.
 *
 * @param <K> the type of the first object, referred to as the key.
 * @param <V> the type of the second object, referred to as the value.
 *            <p>
 *            The Pair interface provides a contract for classes that encapsulate two related
 *            objects. It is commonly used to associate a key with a corresponding value. The
 *            implementation can support both immutable and mutable pairs.
 *            <p>
 *            Classes implementing this interface should override its methods to expose the
 *            functionality of accessing the key and value.
 */
public interface Pair<K, V> extends Serializable {
	K key();

	V value();
}
