package org.universalcube.spring_starter_discord.collections.tuples;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents an immutable or mutable collection of strongly-typed elements
 * that can be accessed by their relative positional index. It provides utility
 * methods for commonly needed operations like size determination, containment
 * checks, conversion to other collections, and creation of sub-tuples.
 *
 * @param <T> the type of the elements contained in the tuple
 */
public interface Tuple<T> extends Serializable {
	/**
	 * Retrieves the element at the specified position in the tuple.
	 *
	 * @param index the index of the element to return, must be non-negative and
	 *              less than the size of the tuple.
	 * @return the element at the specified position.
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
	T get(int index);

	/**
	 * Returns the number of elements in the tuple.
	 *
	 * @return the total count of elements in this tuple.
	 */
	int size();

	/**
	 * Checks whether the specified element is present in the tuple.
	 *
	 * @param element the element to check for containment in the tuple
	 * @return {@code true} if the element is present in the tuple, otherwise {@code false}
	 */
	boolean contains(T element);

	/**
	 * Returns the index of the first occurrence of the specified element in the tuple,
	 * or -1 if the element is not found.
	 *
	 * @param element the element to locate in the tuple
	 * @return the index of the first occurrence of the specified element
	 *         in the tuple, or -1 if the element is not present
	 */
	int indexOf(T element);

	/**
	 * Converts the tuple into an array containing all elements in their respective order.
	 *
	 * @return an array of elements of type {@code T} contained within the tuple,
	 *         or {@code null} if the tuple is empty or contains {@code null} as the
	 *         first element.
	 */
	T[] toArray();

	/**
	 * Creates a new {@code Tuple} that is a sub-sequence of this tuple. The sub-tuple
	 * includes elements from the specified start index (inclusive) to the end index
	 * (exclusive). The indices must specify a valid range within this tuple.
	 *
	 * @param start the starting index (inclusive) of the sub-tuple; must be non-negative
	 *              and less than or equal to {@code end}.
	 * @param end   the ending index (exclusive) of the sub-tuple; must be greater than
	 *              {@code start} and less than or equal to the size of the tuple.
	 * @return a new {@code Tuple} containing the elements in the specified range.
	 * @throws IllegalArgumentException if {@code start} is negative, {@code end} exceeds
	 *                                  the tuple size, or {@code start} is greater than or
	 *                                  equal to {@code end}.
	 */
	Tuple<T> subTuple(int start, int end);

	/**
	 * Checks if the tuple is empty, meaning it contains no elements.
	 *
	 * @return {@code true} if the tuple contains no elements, otherwise {@code false}.
	 */
	boolean isEmpty();

	/**
	 * Converts the tuple into a list containing all elements in their respective order.
	 *
	 * @return a list of elements of type {@code T} contained within the tuple.
	 *         The resulting list will mirror the order of elements in the tuple.
	 */
	List<T> toList();

	/**
	 * Converts the tuple into a {@code Set} containing all elements.
	 * The resulting set will contain only unique elements from the tuple,
	 * as duplicates will be eliminated during the conversion.
	 *
	 * @return a {@link Set<T>} of elements of type {@code T} contained within the tuple.
	 */
	Set<T> toSet();

	/**
	 * Converts the contents of the tuple into a map, where each key corresponds
	 * to the index of an element in the tuple and the value is the element itself.
	 *
	 * @return a {@code Map<Integer, T>} where keys are the indices of the tuple
	 *         elements, and values are the corresponding elements of type {@code T}.
	 * @throws IllegalStateException if the tuple is not in a valid state for conversion.
	 */
	Map<Integer, T> toMap();
}
