package org.universalcube.spring_starter_discord.collections.pairs;

import java.io.Serializable;

public interface Pair<K, V> extends Serializable {
	K key();

	V value();
}
