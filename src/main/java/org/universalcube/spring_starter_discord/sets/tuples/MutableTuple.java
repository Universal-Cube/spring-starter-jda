package org.universalcube.spring_starter_discord.sets.tuples;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReferenceArray;

@Getter
@Setter
public class MutableTuple<T> implements Tuple<T> {
	private volatile AtomicReferenceArray<T> elements;

	@SafeVarargs
	public MutableTuple(T... elements) {
		this.elements = new AtomicReferenceArray<>(elements);
	}

	@Override
	public synchronized T get(int index) {
		T element;
		if ((element = elements.get(index)) != null && (index < 0 || index >= size()))
			throw new IndexOutOfBoundsException("Index out of bounds: " + index);
		return element;
	}

	@Override
	public synchronized int size() {
		return elements.length();
	}

	@Override
	public synchronized boolean contains(T element) {
		for (int i = 0; i < size(); i++) {
			if (elements.get(i).equals(element)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public synchronized int indexOf(T element) {
		for (int i = 0; i < size(); i++) {
			if (elements.get(i).equals(element)) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public synchronized T[] toArray() {
		T[] array = (T[]) new Object[size()];
		for (int i = 0; i < size(); i++) {
			array[i] = elements.get(i);
		}
		return array;
	}

	@Override
	public synchronized Tuple<T> subTuple(int start, int end) {
		if (start < 0 || end > size() || start >= end)
			throw new IllegalArgumentException("Invalid range for subTuple");

		T[] subArray = Arrays.copyOfRange(toArray(), start, end);
		return new MutableTuple<>(subArray);
	}

	@Override
	public synchronized boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public synchronized ArrayList<T> toList() {
		ArrayList<T> list = new ArrayList<>(size());
		for (int i = 0; i < size(); i++) {
			list.add(i, elements.get(i));
		}
		return list;
	}

	@Override
	public synchronized HashSet<T> toSet() {
		HashSet<T> set = new HashSet<>(size());
		for (int i = 0; i < size(); i++) {
			set.add(elements.get(i));
		}
		return set;
	}

	@Override
	public synchronized ConcurrentHashMap<Integer, T> toMap() {
		if (size() % 2 != 0) {
			throw new IllegalStateException("Tuple must contain an even number of elements to convert to a Map");
		}

		ConcurrentHashMap<Integer, T> map = new ConcurrentHashMap<>();
		for (int i = 0; i < size(); i++) {
			map.put(i, elements.get(i + 1));
		}
		return map;
	}

	@Override
	public synchronized boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (obj instanceof MutableTuple<?> tuple)
			return Objects.deepEquals(elements, tuple.elements);
		return false;
	}

	@Override
	public synchronized int hashCode() {
		return Objects.hash(elements);
	}

	@Override
	public synchronized String toString() {
		return "MutapleTuple{%s}".formatted(
				Arrays.toString(toArray())
		);
	}
}
