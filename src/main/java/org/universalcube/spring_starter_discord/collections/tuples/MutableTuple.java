package org.universalcube.spring_starter_discord.collections.tuples;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.lang.reflect.Array;
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

	@Nullable
	@Override
	public synchronized T[] toArray() {
		if (elements.length() == 0)
			return null;

		T firstElement = elements.get(0);
		if (firstElement == null)
			return null;

		@SuppressWarnings("unchecked")
		T[] array = (T[]) Array.newInstance(firstElement.getClass(), size());
		for (int i = 0; i < size(); i++)
			array[i] = elements.get(i);

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

	public synchronized void reset() {
		elements = new AtomicReferenceArray<>(0);
	}

	public synchronized void reset(T[] elements) {
		this.elements = new AtomicReferenceArray<>(elements);
	}

	public synchronized void set(int index, T element) {
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException("Index out of bounds: " + index);
		elements.set(index, element);
	}

	public synchronized void add(T element) {
		AtomicReferenceArray<T> newArray = new AtomicReferenceArray<>(size() + 1);
		for (int i = 0; i < size(); i++) {
			newArray.set(i, elements.get(i));
		}

		newArray.set(size(), element);
		elements = newArray;
	}

	public synchronized void remove(int index) {
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException("Index out of bounds: " + index);

		AtomicReferenceArray<T> newArray = new AtomicReferenceArray<>(size() - 1);
		for (int i = 0, j = 0; i < size(); i++) {
			if (i != index)
				newArray.set(j++, elements.get(i));
		}
		this.elements = newArray;
	}

	public synchronized void clear() {
		this.reset();
	}

	public synchronized void reverse() {
		int start = 0;
		int end = size() - 1;
		while (start < end) {
			T temp = elements.get(start);
			elements.set(start, elements.get(end));
			elements.set(end, temp);
			start++;
			end--;
		}
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
