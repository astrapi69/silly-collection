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
package de.alpharogroup.comparators;

import java.util.Comparator;

/**
 * The class {@link StringLengthComparator} compares {@link String} objects based on length. The
 * default order is greater length come first. If length are equal then the natural ordering will be
 * taken.
 *
 * @author Asterios Raptis
 */
public class StringLengthComparator extends SortOrderComparator<String>
{

	/**
	 * Factory method to create a new {@link StringLengthComparator} object.
	 *
	 * @return the new {@link StringLengthComparator} object
	 */
	@SuppressWarnings("unchecked")
	public static Comparator<String> of()
	{
		return StringLengthComparator.of(true);
	}

	/**
	 * Factory method to create a new {@link StringLengthComparator} object.
	 *
	 * @param nullIsGreaterThan
	 *            the flag that specifies if null objects is greater than non null objects.
	 * @return the new {@link StringLengthComparator} object
	 */
	public static Comparator<String> of(final boolean nullIsGreaterThan)
	{
		return NullCheckComparator.<String> of(new StringLengthComparator(), nullIsGreaterThan);
	}

	/**
	 * Factory method to create a new {@link StringLengthComparator} object.
	 *
	 * @param sortOrder
	 *            the sort order
	 * @return the new {@link StringLengthComparator} object
	 */
	@SuppressWarnings("unchecked")
	public static Comparator<String> of(final SortOrder sortOrder)
	{
		return StringLengthComparator.of(sortOrder, true);
	}

	/**
	 * Factory method to create a new {@link StringLengthComparator} object.
	 *
	 * @param nullIsGreaterThan
	 *            the flag that specifies if null objects is greater than non null objects
	 * @param sortOrder
	 *            the sort order
	 * @return the new {@link StringLengthComparator} object
	 */
	public static Comparator<String> of(final SortOrder sortOrder, final boolean nullIsGreaterThan)
	{
		return NullCheckComparator.<String> of(new StringLengthComparator(sortOrder),
			nullIsGreaterThan);
	}

	public StringLengthComparator()
	{
	}

	/**
	 * Instantiates a new {@link StringLengthComparator}.
	 *
	 * @param sortOrder
	 *            the sort order
	 */
	public StringLengthComparator(final SortOrder sortOrder)
	{
		super(sortOrder);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(final String object, final String compareWithObject)
	{
		final int compareWithObjectLength = compareWithObject.length();
		final int objectLength = object.length();
		if (objectLength == compareWithObjectLength)
		{
			return super.compare(object, compareWithObject);
		}
		return compareWithObjectLength - objectLength;
	}

}
