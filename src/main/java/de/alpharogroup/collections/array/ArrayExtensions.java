/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.alpharogroup.collections.array;

import java.util.Arrays;
import java.util.List;

import lombok.experimental.UtilityClass;

/**
 * The class {@link ArrayExtensions} is an extensions class for use with array objects.
 */
@UtilityClass
public final class ArrayExtensions
{

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
		return ArrayExtensions.toList(array);
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
		final int lastIndex = array.length - 1;
		final int indexOfElement = ArrayExtensions.indexOf(array, element);
		if (indexOfElement == -1)
		{
			return indexOfElement;
		}
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
	 * Factory method for create new array from the given optional elements.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param elements
	 *            the optional elements that will be in the returned array.
	 * @return the new array.
	 */
	@SafeVarargs
	public static <T> T[] newArray(final T... elements)
	{
		return elements;
	}

	/**
	 * Creates a new {@link Integer} array with the given range that is defined through start and
	 * end. For instance if the start is 5 and the end is 9 the resulted array will be [5,6,7,8,9]
	 *
	 * @param start
	 *            The number to start
	 * @param end
	 *            The number to end minus one
	 * @return the generated {@link Integer} array
	 */
	public static Integer[] newRangeArray(final int start, final int end)
	{
		if (end < start)
		{
			throw new IllegalArgumentException(
				"Parameter end should be greater than parameter start.");
		}
		final int length = end - start + 1;
		final Integer[] array = new Integer[length];
		for (int i = start; i <= end; i++)
		{
			array[i - start] = i;
		}
		return array;
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
		return Arrays.asList(array);
	}

}
