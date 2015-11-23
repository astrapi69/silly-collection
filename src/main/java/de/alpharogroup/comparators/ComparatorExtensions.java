/**
 * The MIT License
 *
 * Copyright (C) 2007 Asterios Raptis
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
package de.alpharogroup.comparators;

/**
 * The Class ComparatorExtensions have helper methods for comparators.
 */
public class ComparatorExtensions
{

	/**
	 * Compares the given objects and returns the int value.
	 *
	 * @param <T>
	 *            the generic type
	 * @param object
	 *            the object
	 * @param compareWithObject
	 *            the object to compare with.
	 * @return the int
	 */
	public static <T extends Comparable<T>> int compare(final T object, final T compareWithObject)
	{
		final Integer nc = ComparatorExtensions.nullCheck(object, compareWithObject);
		if (nc != null)
		{
			return nc;
		}
		// Null check completed so we can compare the objects
		return object.compareTo(compareWithObject);

	}

	/**
	 * Compares the given objects and returns the int value.
	 *
	 * @param <T>
	 *            the generic type
	 * @param object
	 *            the object
	 * @param compareWithObject
	 *            the object to compare with.
	 * @param sortOrder
	 *            the sort order
	 * @return the int
	 */
	public static <T extends Comparable<T>> int compare(final T object, final T compareWithObject,
		final SortOrder sortOrder)
	{
		if (SortOrder.DESCENDING.equals(sortOrder))
		{
			return compare(object, compareWithObject) * (-1);
		}
		return compare(object, compareWithObject);

	}

	/**
	 * Checks if one of the given objects are null and returns the value for the Comparator or null
	 * if both are not null or if the given objects are not the same Object.
	 *
	 * @param <T>
	 *            the generic type
	 * @param object
	 *            the object
	 * @param compareWithObject
	 *            the compare with object
	 * @return the integer or null if both are not null or if the given objects are not the same
	 *         Object.
	 */
	public static <T> Integer nullCheck(final T object, final T compareWithObject)
	{
		// Check if one of the objects are null
		if (object != null && compareWithObject == null)
		{
			return 1;// compareWithObject is null so its bigger
		}
		if (object == null && compareWithObject != null)
		{
			return -1; // object is null so its smaller
		}
		if (object == compareWithObject)
		{
			return 0;// it is the same Object
		}
		return null;
	}

}
