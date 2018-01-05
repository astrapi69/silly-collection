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

import lombok.NoArgsConstructor;

/**
 * The class {@link TestStringComparator} sorts String objects in ascending order as default.
 *
 * @author Asterios Raptis
 */
@NoArgsConstructor
public class TestStringComparator extends SortOrderComparator<String>
{

	/** The serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Factory method to create a new {@link TestStringComparator} object.
	 *
	 * @return the new {@link TestStringComparator} object
	 */
	public static Comparator<String> of()
	{
		return TestStringComparator.of(false);
	}

	/**
	 * Factory method to create a new {@link TestStringComparator} object.
	 *
	 * @param nullIsGreaterThan
	 *            the flag that specifies if null objects is greater than non null objects.
	 * @return the new {@link TestStringComparator} object
	 */
	public static Comparator<String> of(final boolean nullIsGreaterThan)
	{
		return NullCheckComparator.<String> of(new TestStringComparator(), nullIsGreaterThan);
	}

	/**
	 * Factory method to create a new {@link TestStringComparator} object.
	 *
	 * @param sortOrder
	 *            the sort order
	 * @return the new {@link TestStringComparator} object
	 */
	public static Comparator<String> of(final SortOrder sortOrder)
	{
		return TestStringComparator.of(sortOrder, false);
	}

	/**
	 * Factory method to create a new {@link TestStringComparator} object.
	 *
	 * @param nullIsGreaterThan
	 *            the flag that specifies if null objects is greater than non null objects
	 * @param sortOrder
	 *            the sort order
	 * @return the new {@link TestStringComparator} object
	 */
	public static Comparator<String> of(final SortOrder sortOrder, final boolean nullIsGreaterThan)
	{
		return NullCheckComparator.<String> of(new TestStringComparator(sortOrder),
			nullIsGreaterThan);
	}

	/**
	 * Instantiates a new {@link TestStringComparator}.
	 *
	 * @param sortOrder
	 *            the sort order
	 */
	public TestStringComparator(final SortOrder sortOrder)
	{
		super(sortOrder);
	}

}
