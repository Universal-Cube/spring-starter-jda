package org.universalcube.spring_starter_discord.sets.tuples;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Tuple<T> extends Serializable {
	T get(int index);

	int size();

	boolean contains(T element);

	int indexOf(T element);

	T[] toArray();

	Tuple<T> subTuple(int start, int end);

	boolean isEmpty();

	List<T> toList();

	Set<T> toSet();

	Map<Integer, T> toMap();
}
