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

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

public class ComparableComparatorTest
{

	@Test
	public void testComparable()
	{
		ComparableComparator<Integer> comparator = new ComparableComparator<>();
		final Integer i1 = 42;

		final Integer lesser = i1 / 2;
		final Integer same = i1;
		final Integer greater = i1 * 2;

		AssertJUnit.assertTrue(comparator.compare(i1, lesser) > 0);
		AssertJUnit.assertTrue(comparator.compare(i1, same) == 0);
		AssertJUnit.assertTrue(comparator.compare(i1, greater) < 0);
		AssertJUnit.assertTrue(comparator.compare(i1, null) > 0);

		comparator = new ComparableComparator<>(SortOrder.DESCENDING);

		AssertJUnit.assertTrue(comparator.compare(i1, lesser) < 0);
		AssertJUnit.assertTrue(comparator.compare(i1, same) == 0);
		AssertJUnit.assertTrue(comparator.compare(i1, greater) > 0);
		AssertJUnit.assertTrue(comparator.compare(i1, null) < 0);
	}
}