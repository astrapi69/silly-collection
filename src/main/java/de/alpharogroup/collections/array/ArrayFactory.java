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

import lombok.NonNull;

import java.lang.reflect.Array;

/**
 * The factory class {@link ArrayFactory} provides factory methods for create new {@code Array}
 * objects
 */
public final class ArrayFactory
{

	private ArrayFactory() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}
	/**
	 * Factory method for create new array from the given class type and the given length
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param cls
	 *            the class type
	 * @param length
	 *            the capacity
	 * @return the new array of the given class type and the given length
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newArray(final Class<T> cls, int length)
	{
		return (T[])Array.newInstance(cls, length);
	}

	/**
	 * Factory method for create new array from the given optional elements.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param elements
	 *            the optional elements that will be in the returned array.
	 * @return the new array
	 */
	@SafeVarargs
	public static <T> T[] newArray(final T... elements)
	{
		return elements;
	}

	/**
	 * Factory method for create new empty array with the length of the given array
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param array
	 *            the array that is used as a template
	 * @return the new empty array
	 */
	public static <T> T[] newEmptyArray(final @NonNull T[] array)
	{
		return newEmptyArray(array, array.length);
	}

	/**
	 * Factory method for create new empty array with the length of the given array.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param array
	 *            the array that is used as a template
	 * @param length
	 *            the length of the new empty array
	 * @return the new empty array
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newEmptyArray(final @NonNull T[] array, int length)
	{
		return (T[])Array.newInstance(array.getClass().getComponentType(), length);
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
	 * Factory method for create new array of primitive boolean values from the given optional
	 * elements.
	 *
	 * @param elements
	 *            the optional primitive boolean values that will be in the returned array of
	 *            primitive boolean values array.
	 * @return the new array of primitive boolean values
	 */
	@SafeVarargs
	public static boolean[] newBooleanArray(final boolean... elements)
	{
		return elements;
	}

	/**
	 * Factory method for create new array of primitive byte values from the given optional
	 * elements.
	 *
	 * @param elements
	 *            the optional primitive byte values that will be in the returned array of primitive
	 *            byte values array.
	 * @return the new array of primitive byte values
	 */
	@SafeVarargs
	public static byte[] newByteArray(final byte... elements)
	{
		return elements;
	}

	/**
	 * Factory method for create new array of primitive character values from the given optional
	 * elements.
	 *
	 * @param elements
	 *            the optional primitive character values that will be in the returned array of
	 *            primitive character values array.
	 * @return the new array of primitive character values
	 */
	@SafeVarargs
	public static char[] newCharArray(final char... elements)
	{
		return elements;
	}

	/**
	 * Factory method for create new array of primitive integer values from the given optional
	 * elements.
	 *
	 * @param elements
	 *            the optional primitive integer values that will be in the returned array of
	 *            primitive integer values array.
	 * @return the new array of primitive integer values
	 */
	@SafeVarargs
	public static int[] newIntArray(final int... elements)
	{
		return elements;
	}

	/**
	 * Factory method for create new array of primitive long values from the given optional
	 * elements.
	 *
	 * @param elements
	 *            the optional primitive long values that will be in the returned array of primitive
	 *            long values array.
	 * @return the new array of primitive long values
	 */
	@SafeVarargs
	public static long[] newLongArray(final long... elements)
	{
		return elements;
	}

	/**
	 * Factory method for create new array of primitive float values from the given optional
	 * elements.
	 *
	 * @param elements
	 *            the optional primitive float values that will be in the returned array of
	 *            primitive float values array.
	 * @return the new array of primitive float values
	 */
	@SafeVarargs
	public static float[] newFloatArray(final float... elements)
	{
		return elements;
	}

	/**
	 * Factory method for create new array of primitive double values from the given optional
	 * elements.
	 *
	 * @param elements
	 *            the optional primitive double values that will be in the returned array of
	 *            primitive double values array.
	 * @return the new array of primitive double values
	 */
	@SafeVarargs
	public static double[] newDoubleArray(final double... elements)
	{
		return elements;
	}

	/**
	 * Factory method for create new array of primitive short values from the given optional
	 * elements.
	 *
	 * @param elements
	 *            the optional primitive short values that will be in the returned array of
	 *            primitive short values array.
	 * @return the new array of primitive short values
	 */
	@SafeVarargs
	public static short[] newShortArray(final short... elements)
	{
		return elements;
	}

	/**
	 * Creates a new partial array from the given source array starting from the given start index
	 * that is inclusive and the end index that is exclusive
	 *
	 * @param <T>
	 *            the generic type
	 * @param source
	 *            the source array
	 * @param startIndex
	 *            the start index that is inclusive
	 * @param endIndex
	 *            the end index that is exclusive
	 * @return the new partial array
	 */
	public static <T> T[] newSubArray(final @NonNull T[] source, int startIndex, int endIndex)
	{
		int newSize = endIndex - startIndex;
		T[] subArray = newEmptyArray(source, newSize);
		for (int i = 0; i < newSize; i++)
		{
			subArray[i] = source[i + startIndex];
		}
		return subArray;
	}

}
