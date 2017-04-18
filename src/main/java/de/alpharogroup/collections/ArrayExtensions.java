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
package de.alpharogroup.collections;

import java.util.Arrays;

/**
 * The class {@link ArrayExtensions} is an extensions class for use with array objects.
 */
public final class ArrayExtensions
{
	
	/**
	 * Groups successive numbers from the given integer array. <br>
	 * Example: <br>
	 * 
	 * <pre>
	 * int[] integerArray = [11, 12, 13, 14, 16, 18];
	 * String actual = StringUtil.groupSuccessiveNumbers(integerArray, "-", ",", true);
	 * 
	 * assertTrue(actual.equals("11-14, 16, 18"));
	 * </pre>
	 * 
	 * <br>
	 * 
	 * <pre>
	 * int[] integerArray = [12, 13, 14, 16, 17, 18, 20, 22, 23, 24, 25];
	 * String actual = StringUtil.groupSuccessiveNumbers(integerArray, "-", ",", true);
	 * 
	 * assertTrue(actual.equals("12-14, 16-18, 20, 22-25"));
	 * </pre>
	 *
	 * @param integerArray
	 *            the integer array that will be processed.
	 * @param groupDelimiter
	 *            the group delimiter for successive numbers.
	 * @param defaultDelimiter
	 *            the default delimiter for not successive numbers.
	 * @param appendWithspace
	 *            flag if true a whitespace will be appended after the default delimiter
	 * @return the resulted {@link String} object.
	 */
	public static String groupSuccessiveNumbers(int[] integerArray, String groupDelimiter,
		String defaultDelimiter, boolean appendWithspace)
	{
		StringBuilder sb = new StringBuilder();
		int size = integerArray.length;
		int firstIndex = 0;
		boolean isLastIndex = false;
		boolean skipInsert = false;
		String currentDelimiter = defaultDelimiter;
		for (int i = 0; i < size; i++)
		{
			int currentValue = integerArray[i];
			if (i == firstIndex)
			{
				sb.append(currentValue);
			}
			int nextIndex = i + 1;
			if (nextIndex == size)
			{
				isLastIndex = true;
			}
			if (!isLastIndex)
			{
				int nextValue = integerArray[nextIndex];
				skipInsert = isNext(currentValue, nextValue);
				if (skipInsert)
				{
					if (currentDelimiter.equals(defaultDelimiter))
					{
						if (i != firstIndex)
						{
							sb.append(currentValue);
						}
						currentDelimiter = groupDelimiter;
						sb.append(currentDelimiter);
					}
				}
				else
				{
					sb.append(currentValue);
					currentDelimiter = defaultDelimiter;
					sb.append(currentDelimiter);
					if (appendWithspace)
					{
						sb.append(" ");
					}
				}
			}
			else
			{
				sb.append(currentValue);
			}
		}
		return sb.toString();
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

}
