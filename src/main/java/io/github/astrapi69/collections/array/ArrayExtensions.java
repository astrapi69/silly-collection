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
package io.github.astrapi69.collections.array;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.github.astrapi69.check.Argument;
import io.github.astrapi69.collections.list.ListFactory;

/**
 * The class {@link ArrayExtensions} is an extensions class for use with array objects.
 */
public final class ArrayExtensions
{
	private ArrayExtensions()
	{
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
	 * Creates a new {@link Stream} from the given array. <br>
	 * <br>
	 *
	 * @param <T>
	 *            the generic type of the objects in the array.
	 * @param array
	 *            the array
	 * @return the new {@link Stream} created from the given array.
	 */
	@SafeVarargs
	public static <T> Stream<T> asStream(T... array)
	{
		return Arrays.stream(array);
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
	 * Returns a concatenated array from given arrays
	 *
	 * @param <T>
	 *            the generic type
	 * @param existingArray
	 *            the existing array
	 * @param elements
	 *            the element that will be concatenated
	 * @return a new concatenated array from given arrays
	 */
	public static <T> T[] concatenate(T[] existingArray, T[] elements)
	{
		if (elements == null || elements.length == 0)
		{
			return existingArray;
		}
		if (existingArray == null)
		{
			return elements;
		}
		int existingArrayLength = existingArray.length;
		int elementsLength = elements.length;

		T[] concatenatedArray = (T[])ArrayFactory.newArray(
			existingArray.getClass().getComponentType(), existingArrayLength + elementsLength);
		System.arraycopy(existingArray, 0, concatenatedArray, 0, existingArrayLength);
		System.arraycopy(elements, 0, concatenatedArray, existingArrayLength, elementsLength);
		return concatenatedArray;
	}

	/**
	 * Returns <tt>true</tt> if at least one of given elements is in the given array
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param elements
	 *            the elements to check if is containing in the given array
	 * @return <tt>true</tt> if at least one of given elements is in the given array otherwise
	 *         <tt>false</tt>
	 */
	@SafeVarargs
	public static <T> boolean containsAtLeastOne(final T[] array, final T... elements)
	{
		boolean contains = false;
		for (T element : elements)
		{
			if (contains(array, element))
			{
				contains = true;
				break;
			}
		}
		return contains;
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
			final int lastIndex = getLastIndex(array);
			return array[lastIndex];
		}
		return null;
	}

	/**
	 * Gets the last index from the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array.
	 * @return Returns the last index from the given array or -1 if the array is null.
	 */
	public static <T> int getLastIndex(final T[] array)
	{
		if (array == null)
		{
			return -1;
		}
		return array.length - 1;
	}

	/**
	 * Gets the last index from the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array.
	 * @return Returns the last index from the given array or -1 if the array is null.
	 */
	public static <T> int getLastIndex(final boolean[] array)
	{
		if (array == null)
		{
			return -1;
		}
		return array.length - 1;
	}

	/**
	 * Gets the last index from the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array.
	 * @return Returns the last index from the given array or -1 if the array is null.
	 */
	public static <T> int getLastIndex(final byte[] array)
	{
		if (array == null)
		{
			return -1;
		}
		return array.length - 1;
	}

	/**
	 * Gets the last index from the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array.
	 * @return Returns the last index from the given array or -1 if the array is null.
	 */
	public static <T> int getLastIndex(final char[] array)
	{
		if (array == null)
		{
			return -1;
		}
		return array.length - 1;
	}

	/**
	 * Gets the last index from the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array.
	 * @return Returns the last index from the given array or -1 if the array is null.
	 */
	public static <T> int getLastIndex(final float[] array)
	{
		if (array == null)
		{
			return -1;
		}
		return array.length - 1;
	}

	/**
	 * Gets the last index from the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array.
	 * @return Returns the last index from the given array or -1 if the array is null.
	 */
	public static <T> int getLastIndex(final long[] array)
	{
		if (array == null)
		{
			return -1;
		}
		return array.length - 1;
	}

	/**
	 * Gets the last index from the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array.
	 * @return Returns the last index from the given array or -1 if the array is null.
	 */
	public static <T> int getLastIndex(final short[] array)
	{
		if (array == null)
		{
			return -1;
		}
		return array.length - 1;
	}

	/**
	 * Gets the last index from the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array.
	 * @return Returns the last index from the given array or -1 if the array is null.
	 */
	public static <T> int getLastIndex(final double[] array)
	{
		if (array == null)
		{
			return -1;
		}
		return array.length - 1;
	}

	/**
	 * Gets the last index from the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array.
	 * @return Returns the last index from the given array or -1 if the array is null.
	 */
	public static <T> int getLastIndex(final int[] array)
	{
		if (array == null)
		{
			return -1;
		}
		return array.length - 1;
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
		final int lastIndex = getLastIndex(array);
		if (indexOfElement == lastIndex)
		{
			return 0;
		}
		return indexOfElement + 1;
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
		final int lastIndex = getLastIndex(array);
		final int indexOfElement = ArrayExtensions.indexOf(array, element);
		if (indexOfElement == -1)
		{
			return indexOfElement;
		}
		if (indexOfElement == 0)
		{
			return lastIndex;
		}
		return indexOfElement - 1;
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
		return Arrays.asList(array).indexOf(element);
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
	public static <T> T[] intersection(final T[] one, final T[] other)
	{
		Argument.notNull(one, "one");
		Argument.notNull(other, "other");
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
		final int lastIndex = getLastIndex(array);
		final int indexOfElement = Arrays.asList(array).indexOf(element);
		return indexOfElement == lastIndex;
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
	public static <T> T[] remove(final T[] array, int... indexes)
	{
		Argument.notNull(array, "array");
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
	public static <T> T[] removeAll(final T[] array, final T[] arrayToRemove)
	{
		Argument.notNull(array, "array");
		Argument.notNull(arrayToRemove, "arrayToRemove");
		List<T> list = ListFactory.newArrayList(array);
		List<T> listToRemove = ListFactory.newArrayList(arrayToRemove);
		list.removeAll(listToRemove);
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
	public static <T> T[] removeFirst(final T[] original)
	{
		Argument.notNull(original, "original");
		return remove(original, 0);
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
	public static <T> T[] removeFromStart(final T[] array, final T[] prefix)
	{
		Argument.notNull(array, "array");
		Argument.notNull(prefix, "prefix");
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
	 * Removes the first element of the array
	 *
	 * @param <T>
	 *            the generic type of the objects in the array
	 * @param original
	 *            the origin array
	 * @return the new created array with out the first element
	 */
	public static <T> T[] removeLast(final T[] original)
	{
		Argument.notNull(original, "original");
		return Arrays.copyOf(original, original.length - 1);
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
