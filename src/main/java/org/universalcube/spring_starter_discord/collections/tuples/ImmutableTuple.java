package org.universalcube.spring_starter_discord.collections.tuples;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents an immutable implementation of the {@link Tuple} interface.
 * This class provides a fixed set of elements, preventing modifications
 * after instantiation. It supports various operations like accessing
 * individual elements, checking size, searching for elements, and
 * converting the tuple to other data structures.
 *
 * @param <T> the type of elements stored in this tuple
 */
public record ImmutableTuple<T>(T[] elements) implements Tuple<T> {

	/**
	 * Retrieves the element at the specified position in the tuple.
	 *
	 * @param index the position of the element to return, starting from 0
	 * @return the element at the specified position in the tuple
	 * @throws IndexOutOfBoundsException if the index is negative or greater than or equal to the size of the tuple
	 */
	@Override
	public T get(int index) {
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException("Index out of bounds: " + index);

		return elements[index];
	}

	/**
	 * Returns the number of elements in the tuple.
	 *
	 * @return the number of elements in the tuple
	 */
	@Override
	public int size() {
		return elements.length;
	}

	/**
	 * Checks if the tuple contains the specified element.
	 *
	 * @param element the element to check for in the tuple
	 * @return {@code true} if the element is found in the tuple, otherwise {@code false}
	 */
	@Override
	public boolean contains(T element) {
		for (int i = 0; i < size(); i++) {
			if (elements[i].equals(element)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the index of the first occurrence of the specified element in the tuple.
	 * If the element is not found, returns -1.
	 *
	 * @param element the element to search for in the tuple
	 * @return the index of the first occurrence of the specified element, or -1 if the element is not found
	 */
	@Override
	public int indexOf(T element) {
		for (int i = 0; i < size(); i++) {
			if (elements[i].equals(element)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Converts the elements of the tuple into a new array.
	 * The returned array is a copy of the tuple's internal element array, ensuring immutability.
	 *
	 * @return an array containing all elements of the tuple in the same order
	 */
	@Override
	public T[] toArray() {
		return Arrays.copyOf(elements, size());
	}

	/**
	 * Creates a new sub-tuple containing elements from the specified range
	 * within the current tuple.
	 *
	 * @param start the starting index (inclusive) of the sub-tuple
	 * @param end the ending index (exclusive) of the sub-tuple
	 * @return a new {@code Tuple<T>} containing the elements in the specified range
	 * @throws IllegalArgumentException if the start index is negative, the end index
	 * is greater than the size of the tuple, or if the start index is greater than or
	 * equal to the end index
	 */
	@Override
	public Tuple<T> subTuple(int start, int end) {
		if (start < 0 || end > size() || start >= end) {
			throw new IllegalArgumentException("Invalid range for subTuple");
		}
		T[] subArray = Arrays.copyOfRange(elements, start, end);
		return new ImmutableTuple<>(subArray);
	}

	/**
	 * Checks whether the tuple contains no elements.
	 *
	 * @return {@code true} if the tuple is empty (contains no elements), otherwise {@code false}
	 */
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Converts the tuple's elements into an immutable list.
	 *
	 * @return a list containing all elements of the tuple in the same order
	 */
	@Override
	public List<T> toList() {
		return List.of(elements);
	}

	/**
	 * Converts the elements of the tuple into an immutable set.
	 *
	 * @return a set containing all elements of the tuple
	 */
	@Override
	public Set<T> toSet() {
		return Set.of(elements);
	}

	/**
	 * Converts the tuple into a map where the keys are the indices of the elements (starting from 0)
	 * and the values are the corresponding elements from the tuple.
	 *
	 * @return a map containing the elements of the tuple with their associated indices
	 * @throws IllegalStateException if the tuple contains an odd number of elements
	 */
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
