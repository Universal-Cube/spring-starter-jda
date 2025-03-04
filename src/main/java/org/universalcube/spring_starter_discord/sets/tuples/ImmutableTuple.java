package org.universalcube.spring_starter_discord.sets.tuples;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public record ImmutableTuple<T>(T... elements) implements Tuple<T> {

	@SafeVarargs
	public ImmutableTuple {
	}

	@Override
	public T get(int index) {
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException("Index out of bounds: " + index);

		return elements[index];
	}

	@Override
	public int size() {
		return elements.length;
	}

	@Override
	public boolean contains(T element) {
		for (int i = 0; i < size(); i++) {
			if (elements[i].equals(element)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int indexOf(T element) {
		for (int i = 0; i < size(); i++) {
			if (elements[i].equals(element)) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public T[] toArray() {
		return Arrays.copyOf(elements, size());
	}

	@Override
	public Tuple<T> subTuple(int start, int end) {
		if (start < 0 || end > size() || start >= end) {
			throw new IllegalArgumentException("Invalid range for subTuple");
		}
		T[] subArray = Arrays.copyOfRange(elements, start, end);
		return new ImmutableTuple<>(subArray);
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public List<T> toList() {
		return List.of(elements);
	}

	@Override
	public Set<T> toSet() {
		return Set.of(elements);
	}

	@Override
	public Map<Integer, T> toMap() {
		if (size() % 2 != 0) {
			throw new IllegalStateException("Tuple must contain an even number of elements to convert to a Map");
		}

		Map<Integer, T> map = new ConcurrentHashMap<>();
		for (int i = 0; i < size(); i++) {
			map.put(i, elements[i + 1]);
		}
		return map;
	}
}
