package de.alpharogroup.collections.array;

import lombok.experimental.UtilityClass;

/**
 * The factory class {@link ArrayFactory} provides factory methods for create new {@code Array} objects
 */
@UtilityClass
public final class ArrayFactory
{

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
	
}
