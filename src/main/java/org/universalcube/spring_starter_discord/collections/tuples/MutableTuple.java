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

/**
 * A thread-safe, mutable implementation of a tuple that allows modification of its elements.
 * The tuple can store multiple elements of the same or different types, defined by the generic type parameter {@code T}.
 *
 * @param <T> the type of elements in the tuple
 */
@Getter
@Setter
public class MutableTuple<T> implements Tuple<T> {
	private volatile AtomicReferenceArray<T> elements;

	/**
	 * Constructs a new {@code MutableTuple} instance that is initialized with the given elements.
	 * This tuple allows mutable operations such as adding, removing, and replacing elements.
	 *
	 * @param elements the initial elements to populate the tuple with.
	 *                 The elements are stored in order and must be of the generic type {@code T}.
	 */
	@SafeVarargs
	public MutableTuple(T... elements) {
		this.elements = new AtomicReferenceArray<>(elements);
	}

	/**
	 * Retrieves the element at the specified index in the tuple.
	 * The index must be within the valid range of 0 (inclusive) to size (exclusive).
	 *
	 * @param index the index of the element to retrieve; must be non-negative and less than the size of the tuple
	 * @return the element at the specified index
	 * @throws IndexOutOfBoundsException if the index is out of bounds {@code (index < 0 || index >= size())}
	 */
	@Override
	public synchronized T get(int index) {
		T element;
		if ((element = elements.get(index)) != null && (index < 0 || index >= size()))
			throw new IndexOutOfBoundsException("Index out of bounds: " + index);
		return element;
	}

	/**
	 * Returns the number of elements contained in this {@code MutableTuple}.
	 * This method is thread-safe and ensures synchronized access.
	 *
	 * @return the total count of elements in the tuple.
	 */
	@Override
	public synchronized int size() {
		return elements.length();
	}

	/**
	 * Checks if the specified element is present in this {@code MutableTuple}.
	 *
	 * @param element the element to check for containment within the tuple; must be of type {@code T}.
	 * @return {@code true} if the element is found in the tuple; otherwise {@code false}.
	 */
	@Override
	public synchronized boolean contains(T element) {
		for (int i = 0; i < size(); i++) {
			if (elements.get(i).equals(element)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the index of the first occurrence of the specified element
	 * in this tuple, or -1 if the element is not found. The search is
	 * performed sequentially from the beginning of the tuple.
	 *
	 * @param element the element to locate in the tuple; must be of type {@code T}.
	 *                May be {@code null}, in which case the method checks for
	 *                the presence of a {@code null} element in the tuple.
	 * @return the index of the first occurrence of the specified element,
	 *         or -1 if the element is not present.
	 */
	@Override
	public synchronized int indexOf(T element) {
		for (int i = 0; i < size(); i++) {
			if (elements.get(i).equals(element)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Converts the elements of this {@code MutableTuple} into an array.
	 * The resulting array contains all elements in their respective order.
	 *
	 * @return an array of elements of type {@code T} contained within the tuple,
	 *         or {@code null} if the tuple is empty or the first element is {@code null}.
	 */
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

	/**
	 * Creates a new {@code Tuple} that is a sub-sequence of this {@code MutableTuple}.
	 * The sub-tuple includes elements from the specified start index (inclusive) to
	 * the end index (exclusive). The indices must specify a valid range within this tuple.
	 *
	 * @param start the starting index (inclusive) of the sub-tuple; must be non-negative
	 *              and less than {@code end}.
	 * @param end   the ending index (exclusive) of the sub-tuple; must be greater than
	 *              {@code start} and less than or equal to the size of the tuple.
	 * @return a new {@code Tuple} containing the elements in the specified range.
	 * @throws IllegalArgumentException if {@code start} is negative, {@code end}
	 *                                  exceeds the tuple size, or {@code start}
	 *                                  is greater than or equal to {@code end}.
	 */
	@Override
	public synchronized Tuple<T> subTuple(int start, int end) {
		if (start < 0 || end > size() || start >= end)
			throw new IllegalArgumentException("Invalid range for subTuple");

		T[] subArray = Arrays.copyOfRange(toArray(), start, end);
		return new MutableTuple<>(subArray);
	}

	/**
	 * Checks if this {@code MutableTuple} contains no elements.
	 * This method is thread-safe and ensures synchronized access.
	 *
	 * @return {@code true} if the tuple is empty (contains no elements);
	 *         {@code false} otherwise.
	 */
	@Override
	public synchronized boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Converts the elements of this {@code MutableTuple} into a new {@code ArrayList}.
	 * The resulting list contains all elements in their respective order.
	 *
	 * @return an {@code ArrayList} containing all elements of this {@code MutableTuple}
	 *         in order, or an empty list if the tuple has no elements.
	 */
	@Override
	public synchronized ArrayList<T> toList() {
		ArrayList<T> list = new ArrayList<>(size());
		for (int i = 0; i < size(); i++) {
			list.add(i, elements.get(i));
		}
		return list;
	}

	/**
	 * Converts the elements of this {@code MutableTuple} into a {@code HashSet}.
	 * The resulting set contains all unique elements of the tuple in no particular order.
	 *
	 * @return a {@code HashSet} containing all unique elements of this {@code MutableTuple}.
	 */
	@Override
	public synchronized HashSet<T> toSet() {
		HashSet<T> set = new HashSet<>(size());
		for (int i = 0; i < size(); i++) {
			set.add(elements.get(i));
		}
		return set;
	}

	/**
	 * Converts the elements of the {@code MutableTuple} into a {@code ConcurrentHashMap}.
	 * The keys are integers representing the element indices, starting from 0, and the
	 * values are the corresponding elements from the tuple.
	 *
	 * @return a {@code ConcurrentHashMap} containing the elements of the tuple, where the keys
	 *         are the index positions and the values are the respective tuple elements.
	 * @throws IllegalStateException if the tuple contains an odd number of elements.
	 */
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

	/**
	 * Resets the state of this {@code MutableTuple} by clearing all elements.
	 * This method replaces the current elements with an empty internal structure,
	 * effectively resetting the tuple to an empty state. It ensures thread-safe
	 * access and modification.
	 */
	public synchronized void reset() {
		elements = new AtomicReferenceArray<>(0);
	}

	/**
	 * Resets the state of this {@code MutableTuple} by replacing its elements with
	 * the provided array. This method ensures thread-safe modification of the tuple's content.
	 *
	 * @param elements the new elements to initialize the tuple with; must be an array
	 *                 of type {@code T}.
	 */
	public synchronized void reset(T[] elements) {
		this.elements = new AtomicReferenceArray<>(elements);
	}

	/**
	 * Replaces the element at the specified index in this {@code MutableTuple}
	 * with the provided element. This method ensures thread-safe modification
	 * of the tuple.
	 *
	 * @param index  the index of the element to replace; must be non-negative
	 *               and less than the size of the tuple.
	 * @param element the new element to store at the specified index; must be of type {@code T}.
	 * @throws IndexOutOfBoundsException if the specified index is out of bounds
	 *         {@code (index < 0 || index >= size())}.
	 */
	public synchronized void set(int index, T element) {
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException("Index out of bounds: " + index);
		elements.set(index, element);
	}

	/**
	 * Adds the specified element to the tuple. The element is appended as the last
	 * item in the tuple, increasing the size of the tuple by one. This method is
	 * thread-safe, ensuring synchronized access during the modification of the tuple.
	 *
	 * @param element the element to add to the tuple. The element must be of the
	 *                generic type {@code T}.
	 */
	public synchronized void add(T element) {
		AtomicReferenceArray<T> newArray = new AtomicReferenceArray<>(size() + 1);
		for (int i = 0; i < size(); i++) {
			newArray.set(i, elements.get(i));
		}

		newArray.set(size(), element);
		elements = newArray;
	}

	/**
	 * Removes the element at the specified index from this {@code MutableTuple}.
	 * The remaining elements are shifted to close the gap, and the size of the tuple
	 * is reduced by one. This method ensures thread-safe modification of the tuple.
	 *
	 * @param index the index of the element to be removed; must be non-negative
	 *              and less than the size of the tuple.
	 * @throws IndexOutOfBoundsException if the index is out of bounds
	 *         {@code (index < 0 || index >= size())}.
	 */
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

	/**
	 * Clears all elements from this {@code MutableTuple}, effectively resetting it to an empty state.
	 * This method is thread-safe and internally delegates to the {@code reset()} method
	 * to perform the operation.
	 */
	public synchronized void clear() {
		this.reset();
	}

	/**
	 * Reverses the order of elements in this {@code MutableTuple}.
	 * The first element becomes the last, the second becomes the second-to-last,
	 * and so on, until the entire sequence is reversed in-place.
	 * <p>
	 * This operation ensures thread-safe modification of the tuple by synchronizing access.
	 * The complexity of the operation is O(n), where n is the size of the tuple.
	 */
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

	/**
	 * Compares this {@code MutableTuple} to the specified object to determine equality.
	 * The method checks if the specified object is a {@code MutableTuple} and contains
	 * elements that are deeply equal to this tuple's elements in both value and order.
	 *
	 * @param obj the object to compare for equality; may be {@code null}.
	 * @return {@code true} if the specified object is a {@code MutableTuple} with
	 *         elements that are deeply equal to this tuple's elements; otherwise, {@code false}.
	 */
	@Override
	public synchronized boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (obj instanceof MutableTuple<?> tuple)
			return Objects.deepEquals(elements, tuple.elements);
		return false;
	}

	/**
	 * Computes the hash code of this {@code MutableTuple}.
	 * The hash code is computed based on the elements contained in the tuple.
	 * This implementation is synchronized to ensure thread safety.
	 *
	 * @return an integer hash code value representing this tuple.
	 */
	@Override
	public synchronized int hashCode() {
		return Objects.hash(elements);
	}

	/**
	 * Returns a string representation of the object.
	 * The string includes the class name and the array elements
	 * contained within the object, formatted for readability.
	 *
	 * @return A string representation of the object.
	 */
	@Override
	public synchronized String toString() {
		return "MutapleTuple{%s}".formatted(
				Arrays.toString(toArray())
		);
	}
}
