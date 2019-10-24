/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.alpharogroup.collections.array;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.alpharogroup.collections.list.ListFactory;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * The class {@link ArrayExtensions} is an extensions class for use with array objects.
 */
@UtilityClass
public final class ArrayExtensions
{

	/**
	 * Removes the given prefix array from the first given array
	 *
	 * @param <T>
	 *            the generic type of the objects in the arrays
	 *
	 * @param array
	 *            the array
	 * @param prefix
	 *            the prefix
	 * @return the resulted array
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] removeFromStart(final @NonNull T[] array, final @NonNull T[] prefix)
	{
		if (0 < array.length)
		{
			T[] result = (T[])ArrayFactory.newArray(array[0].getClass(),
				array.length - prefix.length);
			System.arraycopy(array, prefix.length, result, 0, result.length);
			return result;
		}
		return array;

	}

	/**
	 * Removes the given suffix array from the first given array
	 *
	 * @param <T>
	 *            the generic type of the objects in the arrays
	 *
	 * @param array
	 *            the array
	 * @param suffix
	 *            the suffix
	 * @return the resulted array
	 */
	public static <T> T[] removeFromEnd(final T[] array, T[] suffix)
	{
		return Arrays.copyOf(array, array.length - suffix.length);
	}

	/**
	 * Removes the first element of the array.
	 *
	 * @param <T>
	 *            the generic type of the objects in the array
	 * @param array
	 *            the origin array
	 * @param arrayToRemove
	 *            the array to remove
	 * @return the new created array with the elements
	 */
	public static <T> T[] removeAll(@NonNull final T[] array, @NonNull final T[] arrayToRemove)
	{
		List<T> list = ListFactory.newArrayList(array);
		List<T> listToRemove = ListFactory.newArrayList(arrayToRemove);
		list.removeAll(listToRemove);
		return list.toArray(Arrays.copyOf(array, list.size()));
	}

	/**
	 * Returns <tt>true</tt> if and only if the given element is in the given array
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @return <tt>true</tt> if and only if the given element is in the given array otherwise
	 *         <tt>false</tt>
	 */
	public static <T> boolean contains(final T[] array, final T element)
	{
		return indexOf(array, element) >= 0;
	}

	/**
	 * Removes the first element of the array.
	 *
	 * @param <T>
	 *            the generic type of the objects in the array
	 * @param array
	 *            the origin array
	 * @param indexes
	 *            the indexes to remove
	 * @return the new created array with the elements from the given indexes
	 */
	public static <T> T[] remove(@NonNull final T[] array, int... indexes)
	{
		List<T> list = ListFactory.newArrayList(array);
		final int lastIndex = indexes.length - 1;
		Arrays.sort(indexes);
		for (int i = lastIndex; -1 < i; i--)
		{
			int index = indexes[i];
			list.remove(index);
		}
		return list.toArray(Arrays.copyOf(array, list.size()));
	}

	/**
	 * Creates a new array cloned from the given array with the difference that the first element is
	 * removed
	 *
	 * @param <T>
	 *            the generic type of the objects in the array
	 * @param original
	 *            the origin array
	 * @return the new created array with out the first element
	 */
	public static <T> T[] removeFirst(@NonNull final T[] original)
	{
		return remove(original, 0);
	}

	/**
	 * Removes the first element of the array
	 *
	 * @param <T>
	 *            the generic type of the objects in the array
	 * @param original
	 *            the origin array
	 * @return the new created array with out the first element
	 */
	public static <T> T[] removeLast(@NonNull final T[] original)
	{
		return Arrays.copyOf(original, original.length - 1);
	}

	/**
	 * Copy the given source array to the given destination array.<br>
	 * <br>
	 * Note: the given destination array have to be already initialized with the size of the
	 * source.<br>
	 * <br>
	 * Example: <br>
	 * <br>
	 * <code>
	 * Integer[] source = {1,2,3,4,5,6,7,8,9};<br>
	 * Integer[] destination = new Integer[source.length];<br>
	 * destination = ArrayExtensions.arraycopyWithSystem(source, destination);<br>
	 * </code>
	 *
	 *
	 * @param <T>
	 *            the generic type of the objects in the array
	 * @param source
	 *            the source
	 * @param destination
	 *            the destination
	 * @return the t[]
	 */
	public static <T> T[] arraycopyWithSystem(T[] source, T[] destination)
	{
		if (source == null)
		{
			return null;
		}
		System.arraycopy(source, 0, destination, 0, source.length);
		return destination;
	}

	/**
	 * Creates a new {@link Set} from the given array. <br>
	 * <br>
	 *
	 * @param <T>
	 *            the generic type of the objects in the array.
	 * @param array
	 *            the array
	 * @return the new {@link Set} created from the given array.
	 */
	@SafeVarargs
	public static <T> Set<T> asSet(T... array)
	{
		return Stream.of(array).collect(Collectors.toSet());
	}

	/**
	 * Creates a new {@link List} from the given array. <br>
	 * <br>
	 * Note: This wraps only the method asList from {@link Arrays#asList(Object...)}.
	 *
	 * @param <T>
	 *            the generic type of the objects in the array.
	 * @param array
	 *            the array
	 * @return the new {@link List} created from the given array.
	 */
	public static <T> List<T> asList(final T[] array)
	{
		return ListFactory.newArrayList(array);
	}

	/**
	 * Gets the first object from the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array.
	 * @return Returns the first object from the given array or null if the array is empty or null.
	 */
	public static <T> T getFirst(final T[] array)
	{
		if (array != null && array.length != 0)
		{
			return array[0];
		}
		return null;
	}

	/**
	 * Gets the index of the given element in the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @return the int
	 */
	public static <T> int getIndex(final T[] array, final T element)
	{
		return ArrayExtensions.indexOf(array, element);
	}

	/**
	 * Gets the last object from the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array.
	 * @return Returns the last object from the given array or null if the array is empty or null.
	 */
	public static <T> T getLast(final T[] array)
	{
		if (array != null && array.length != 0)
		{
			final int lastIndex = array.length - 1;
			return array[lastIndex];
		}
		return null;
	}

	/**
	 * Gets the next index of the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @return the next index
	 */
	public static <T> int getNextIndex(final T[] array, final T element)
	{
		final int indexOfElement = ArrayExtensions.indexOf(array, element);
		if (indexOfElement == -1)
		{
			return indexOfElement;
		}
		final int lastIndex = array.length - 1;
		if (indexOfElement == lastIndex)
		{
			return 0;
		}
		final int nextIndex = indexOfElement + 1;
		return nextIndex;
	}

	/**
	 * Gets the next indexes from the given count of the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @param count
	 *            the count
	 * @return the next indexes or null if there is no next indexes.
	 */
	public static <T> int[] getNextIndexes(final T[] array, final T element, final int count)
	{
		if (element != null)
		{
			final int[] nextIndexes = new int[count];
			T currentElement = element;
			for (int i = 0; i < count; i++)
			{
				final int nextIndex = getNextIndex(array, currentElement);
				if (nextIndex == -1)
				{
					return null;
				}
				nextIndexes[i] = nextIndex;
				currentElement = array[nextIndex];
			}
			return nextIndexes;
		}
		return null;
	}

	/**
	 * Gets the previous index of the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @return the previous index
	 */
	public static <T> int getPreviousIndex(final T[] array, final T element)
	{
		final int lastIndex = array.length - 1;
		final int indexOfElement = ArrayExtensions.indexOf(array, element);
		if (indexOfElement == -1)
		{
			return indexOfElement;
		}
		if (indexOfElement == 0)
		{
			return lastIndex;
		}
		final int previousIndex = indexOfElement - 1;
		return previousIndex;
	}

	/**
	 * Gets the previous indexes from the given count of the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @param count
	 *            the count
	 * @return the previous indexes or null if there is no previous indexes.
	 */
	public static <T> int[] getPreviousIndexes(final T[] array, final T element, final int count)
	{
		if (element != null)
		{
			final int[] previousIndexes = new int[count];
			T currentElement = element;
			for (int i = 0; i < count; i++)
			{
				final int previousIndex = getPreviousIndex(array, currentElement);
				if (previousIndex == -1)
				{
					return null;
				}
				previousIndexes[i] = previousIndex;
				currentElement = array[previousIndex];
			}
			return previousIndexes;
		}
		return null;
	}

	/**
	 * Intersection of the given two arrays.
	 *
	 * @param <T>
	 *            the generic type
	 * @param one
	 *            the first array
	 * @param other
	 *            the other array
	 * @return the result of the intersection
	 */
	public static <T> T[] intersection(final @NonNull T[] one, final @NonNull T[] other)
	{
		T[] intersection = ArrayFactory.newEmptyArray(one);
		int j = 0;
		for (int i = 0; i < one.length; i++)
		{
			if (contains(other, one[i]))
			{
				intersection[j++] = one[i];
			}
		}
		return ArrayFactory.newSubArray(intersection, 0, j);
	}

	/**
	 * Gets the index of the given element in the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @return the int
	 */
	public static <T> int indexOf(final T[] array, final T element)
	{
		final int indexOfElement = Arrays.asList(array).indexOf(element);
		return indexOfElement;
	}

	/**
	 * Checks if the given element is the first in the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @return true if the given element is the first otherwise false
	 */
	public static <T> boolean isFirst(final T[] array, final T element)
	{
		final int indexOfElement = Arrays.asList(array).indexOf(element);
		return indexOfElement == 0;
	}

	/**
	 * Checks if the given element is the last in the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @return true if the given element is the last otherwise false
	 */
	public static <T> boolean isLast(final T[] array, final T element)
	{
		final int lastIndex = array.length - 1;
		final int indexOfElement = Arrays.asList(array).indexOf(element);
		return indexOfElement == lastIndex;
	}

	/**
	 * Split the given byte array in to new arrays with the chunk size.
	 *
	 * @param bytes
	 *            the bytes
	 * @param chunkSize
	 *            the chunk size
	 * @return the byte[][]
	 */
	public static byte[][] splitInChunks(final byte[] bytes, final int chunkSize)
	{
		final int size = ((bytes.length - 1) / chunkSize) + 1;
		final byte[][] dataChunks = new byte[size][];
		int to = bytes.length;
		int count = size - 1;
		int from = count * chunkSize;
		while (-1 < count)
		{
			dataChunks[count] = Arrays.copyOfRange(bytes, from, to);
			to = from;
			from = to - chunkSize;
			count--;
		}
		return dataChunks;
	}

	/**
	 * Creates a new {@link List} from the given array. <br>
	 * <br>
	 * Note: This wraps only the method asList from {@link Arrays#asList(Object...)}.
	 *
	 * @param <T>
	 *            the generic type of the objects in the array.
	 * @param array
	 *            the array
	 * @return the new {@link List} created from the given array.
	 */
	public static <T> List<T> toList(final T[] array)
	{
		return asList(array);
	}

}
