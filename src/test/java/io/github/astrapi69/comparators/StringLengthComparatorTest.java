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
package io.github.astrapi69.comparators;

import static org.testng.AssertJUnit.assertTrue;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.testng.annotations.Test;

import io.github.astrapi69.collections.list.ListFactory;

/**
 * The unit test class for the class {@link StringLengthComparator}.
 */
public class StringLengthComparatorTest
{

	/** For use of the result of the comparator. */
	int actual;

	/** The comparator. */
	Comparator<String> comparator;

	/** For use of the expected result. */
	boolean expected;

	/**
	 * Test method for {@link StringLengthComparator#compare(String, String)}
	 */
	@Test
	public void testCompare()
	{
		final String alex = "Alex";
		final String billy = "Billy";
		final String leo = "Leon";

		comparator = StringLengthComparator.of();

		// Leon length is equal with Alex length so the natural order will be taken
		// so the alphabetic order is that Leon is smaller ...
		actual = comparator.compare(alex, leo);
		expected = actual < 0;
		assertTrue(expected);
		// Leon length is equal with Alex length so the natural order will be taken
		// so the alphabetic order is that Alex is greater ...
		actual = comparator.compare(leo, alex);
		expected = 0 < actual;
		assertTrue(expected);
		// Billy is longer then Alex so it is greater...
		actual = comparator.compare(alex, billy);
		expected = 0 < actual;
		assertTrue(expected);
		// Now lets see a demo on a list...
		final List<String> list = ListFactory.newArrayList();
		list.add(leo);
		list.add(alex);
		list.add(billy);

		expected = list.indexOf(leo) == 0;
		assertTrue(expected);
		expected = list.indexOf(alex) == 1;
		assertTrue(expected);
		expected = list.indexOf(billy) == 2;
		assertTrue(expected);

		Collections.sort(list, comparator);

		expected = list.indexOf(billy) == 0;
		assertTrue(expected);
		expected = list.indexOf(alex) == 1;
		assertTrue(expected);
		expected = list.indexOf(leo) == 2;
		assertTrue(expected);

	}

	/**
	 * Test method for {@link StringLengthComparator#compare(String, String)} created with different
	 * factory methods.
	 */
	@Test
	public void testCompareFactoryMethods()
	{
		final String alex = "Alex";
		final String bill = "Billy";

		comparator = StringLengthComparator.of(SortOrder.DESCENDING);

		actual = comparator.compare(alex, alex);
		expected = 0 == actual;
		assertTrue(expected);

		actual = comparator.compare(alex, bill);
		expected = 0 < actual;
		assertTrue(expected);

		actual = comparator.compare(bill, alex);
		expected = 0 > actual;
		assertTrue(expected);
		// null is greater...
		actual = comparator.compare(null, bill);
		expected = actual > 0;
		assertTrue(expected);

		comparator = StringLengthComparator.of(SortOrder.DESCENDING, false);

		actual = comparator.compare(alex, alex);
		expected = 0 == actual;
		assertTrue(expected);

		actual = comparator.compare(alex, bill);
		expected = 0 < actual;
		assertTrue(expected);

		actual = comparator.compare(bill, alex);
		expected = 0 > actual;
		assertTrue(expected);
		// null is smaller...
		actual = comparator.compare(null, bill);
		expected = actual < 0;
		assertTrue(expected);
	}

}
