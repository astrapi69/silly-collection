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
package io.github.astrapi69.collection.array;

import java.lang.reflect.Array;

import io.github.astrapi69.check.Argument;

/**
 * The factory class {@link ArrayFactory} provides factory methods for create new {@code Array}
 * objects
 */
public final class ArrayFactory
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private ArrayFactory()
	{
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
	 * Factory method for create a new array from an existing array and the given optional elements.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param existingArray
	 *            the existing array
	 * @param elements
	 *            the optional elements that will be in the returned array.
	 * @return the new array
	 */
	@SafeVarargs
	public static <T> T[] newArray(T[] existingArray, final T... elements)
	{
		return ArrayExtensions.concatenate(existingArray, elements);
	}

	/**
	 * Factory method for create new array of primitive boolean values from the given optional
	 * elements.
	 * <p>
	 * The result is a new array, also when no elements are given: a copy of the given array, so
	 * that a write to the result does not change the array of the caller. Null gives null.
	 * </p>
	 *
	 * @param elements
	 *            the optional primitive boolean values that will be in the returned array, may be
	 *            null
	 * @return a new array with the given primitive boolean values, or null if the given elements
	 *         are null
	 */
	@SafeVarargs
	public static boolean[] newBooleanArray(final boolean... elements)
	{
		return elements == null ? null : elements.clone();
	}

	/**
	 * Factory method for create new array of primitive byte values from the given optional
	 * elements.
	 * <p>
	 * The result is a new array, also when no elements are given: a copy of the given array, so
	 * that a write to the result does not change the array of the caller. Null gives null.
	 * </p>
	 *
	 * @param elements
	 *            the optional primitive byte values that will be in the returned array, may be null
	 * @return a new array with the given primitive byte values, or null if the given elements are
	 *         null
	 */
	@SafeVarargs
	public static byte[] newByteArray(final byte... elements)
	{
		return elements == null ? null : elements.clone();
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
	public static byte[] newByteArray(final int... elements)
	{
		byte[] array = new byte[0];
		if (elements != null && 0 < elements.length)
		{
			array = new byte[elements.length];
			for (int i = 0; i < elements.length; ++i)
			{
				array[i] = (byte)elements[i];
			}
		}
		return array;
	}

	/**
	 * Factory method for create new array of primitive character values from the given optional
	 * elements.
	 * <p>
	 * The result is a new array, also when no elements are given: a copy of the given array, so
	 * that a write to the result does not change the array of the caller. Null gives null.
	 * </p>
	 *
	 * @param elements
	 *            the optional primitive character values that will be in the returned array, may be
	 *            null
	 * @return a new array with the given primitive character values, or null if the given elements
	 *         are null
	 */
	@SafeVarargs
	public static char[] newCharArray(final char... elements)
	{
		return elements == null ? null : elements.clone();
	}

	/**
	 * Factory method for create new array of primitive double values from the given optional
	 * elements.
	 * <p>
	 * The result is a new array, also when no elements are given: a copy of the given array, so
	 * that a write to the result does not change the array of the caller. Null gives null.
	 * </p>
	 *
	 * @param elements
	 *            the optional primitive double values that will be in the returned array, may be
	 *            null
	 * @return a new array with the given primitive double values, or null if the given elements are
	 *         null
	 */
	@SafeVarargs
	public static double[] newDoubleArray(final double... elements)
	{
		return elements == null ? null : elements.clone();
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
	public static <T> T[] newEmptyArray(final T[] array)
	{
		Argument.notNull(array, "array");
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
	public static <T> T[] newEmptyArray(final T[] array, int length)
	{
		Argument.notNull(array, "array");
		return (T[])Array.newInstance(array.getClass().getComponentType(), length);
	}

	/**
	 * Factory method for create new array of primitive float values from the given optional
	 * elements.
	 * <p>
	 * The result is a new array, also when no elements are given: a copy of the given array, so
	 * that a write to the result does not change the array of the caller. Null gives null.
	 * </p>
	 *
	 * @param elements
	 *            the optional primitive float values that will be in the returned array, may be
	 *            null
	 * @return a new array with the given primitive float values, or null if the given elements are
	 *         null
	 */
	@SafeVarargs
	public static float[] newFloatArray(final float... elements)
	{
		return elements == null ? null : elements.clone();
	}

	/**
	 * Factory method for create new array of primitive integer values from the given optional
	 * elements.
	 * <p>
	 * The result is a new array, also when no elements are given: a copy of the given array, so
	 * that a write to the result does not change the array of the caller. Null gives null.
	 * </p>
	 *
	 * @param elements
	 *            the optional primitive integer values that will be in the returned array, may be
	 *            null
	 * @return a new array with the given primitive integer values, or null if the given elements
	 *         are null
	 */
	@SafeVarargs
	public static int[] newIntArray(final int... elements)
	{
		return elements == null ? null : elements.clone();
	}

	/**
	 * Factory method for create new array of primitive long values from the given optional
	 * elements.
	 * <p>
	 * The result is a new array, also when no elements are given: a copy of the given array, so
	 * that a write to the result does not change the array of the caller. Null gives null.
	 * </p>
	 *
	 * @param elements
	 *            the optional primitive long values that will be in the returned array, may be null
	 * @return a new array with the given primitive long values, or null if the given elements are
	 *         null
	 */
	@SafeVarargs
	public static long[] newLongArray(final long... elements)
	{
		return elements == null ? null : elements.clone();
	}

	/**
	 * Creates a new {@link Integer} array with the given range that is defined through start and
	 * end, both inclusive. For instance if the start is 5 and the end is 9 the resulted array will
	 * be [5,6,7,8,9], and if the start and the end are 5 it will be [5]
	 *
	 * @param start
	 *            The number to start, inclusive
	 * @param end
	 *            The number to end, inclusive
	 * @return the generated {@link Integer} array
	 * @throws IllegalArgumentException
	 *             if end is less than start
	 */
	public static Integer[] newRangeArray(final int start, final int end)
	{
		if (end < start)
		{
			throw new IllegalArgumentException(
				"Parameter end must not be less than parameter start.");
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
	 * Factory method for create new array of primitive short values from the given optional
	 * elements.
	 * <p>
	 * The result is a new array, also when no elements are given: a copy of the given array, so
	 * that a write to the result does not change the array of the caller. Null gives null.
	 * </p>
	 *
	 * @param elements
	 *            the optional primitive short values that will be in the returned array, may be
	 *            null
	 * @return a new array with the given primitive short values, or null if the given elements are
	 *         null
	 */
	@SafeVarargs
	public static short[] newShortArray(final short... elements)
	{
		return elements == null ? null : elements.clone();
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
	public static <T> T[] newSubArray(final T[] source, int startIndex, int endIndex)
	{
		Argument.notNull(source, "source");
		int newSize = endIndex - startIndex;
		T[] subArray = newEmptyArray(source, newSize);
		for (int i = 0; i < newSize; i++)
		{
			subArray[i] = source[i + startIndex];
		}
		return subArray;
	}

}
