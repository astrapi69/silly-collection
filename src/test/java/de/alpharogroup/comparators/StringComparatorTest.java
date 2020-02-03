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

import static org.testng.AssertJUnit.assertTrue;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.testng.annotations.Test;

import de.alpharogroup.collections.list.ListFactory;

/**
 * The unit test class for the class {@link StringComparator}
 */
public class StringComparatorTest
{

	/** For use of the result of the comparator. */
	private int actual;

	/** The comparator. */
	private Comparator<String> comparator;

	/** For use of the expected result. */
	private boolean expected;


	/**
	 * Test method for {@link StringComparator#compare(String, String)} with null smaller.
	 */
	@Test
	public void testCompare()
	{
		String alex = "Alex";
		String bill = "Billy";
		String leo = "Leon";

		comparator = StringComparator.of(false);

		actual = comparator.compare(alex, alex);
		expected = 0 == actual;
		assertTrue(expected);

		actual = comparator.compare(alex, leo);
		expected = 0 > actual;
		assertTrue(expected);

		actual = comparator.compare(leo, alex);
		expected = 0 < actual;
		assertTrue(expected);

		actual = comparator.compare(alex, bill);
		expected = actual < 0;
		assertTrue(expected);
		// null is smaller...
		actual = comparator.compare(null, bill);
		expected = actual < 0;
		assertTrue(expected);

		// Now lets see a demo on a list...
		List<String> list = ListFactory.newArrayList();
		list.add(leo);
		list.add(alex);
		list.add(bill);
		list.add(null);
		list.add(null);

		expected = list.indexOf(leo) == 0;
		assertTrue(expected);
		expected = list.indexOf(alex) == 1;
		assertTrue(expected);
		expected = list.indexOf(bill) == 2;
		assertTrue(expected);

		Collections.sort(list, comparator);

		expected = list.indexOf(alex) == 2;
		assertTrue(expected);
		expected = list.indexOf(bill) == 3;
		assertTrue(expected);
		expected = list.indexOf(leo) == 4;
		assertTrue(expected);

	}


	/**
	 * Test method for {@link StringComparator#compare(String, String)} created with different
	 * factory methods.
	 */
	@Test
	public void testCompareFactoryMethods()
	{
		String alex = "Alex";
		String bill = "Billy";
		String leo = "Leon";

		comparator = StringComparator.of();

		actual = comparator.compare(alex, alex);
		expected = 0 == actual;
		assertTrue(expected);

		actual = comparator.compare(alex, leo);
		expected = 0 > actual;
		assertTrue(expected);

		actual = comparator.compare(leo, alex);
		expected = 0 < actual;
		assertTrue(expected);
		// null is greater...
		actual = comparator.compare(null, bill);
		expected = actual > 0;
		assertTrue(expected);

		comparator = StringComparator.of(SortOrder.DESCENDING);

		actual = comparator.compare(alex, alex);
		expected = 0 == actual;
		assertTrue(expected);

		actual = comparator.compare(alex, leo);
		expected = 0 < actual;
		assertTrue(expected);

		actual = comparator.compare(leo, alex);
		expected = 0 > actual;
		assertTrue(expected);
		// null is greater...
		actual = comparator.compare(null, bill);
		expected = actual > 0;
		assertTrue(expected);

		comparator = StringComparator.of(SortOrder.DESCENDING, false);

		actual = comparator.compare(alex, alex);
		expected = 0 == actual;
		assertTrue(expected);

		actual = comparator.compare(alex, leo);
		expected = 0 < actual;
		assertTrue(expected);

		actual = comparator.compare(leo, alex);
		expected = 0 > actual;
		assertTrue(expected);
		// null is smaller...
		actual = comparator.compare(null, bill);
		expected = actual < 0;
		assertTrue(expected);
	}

	/**
	 * Test method for {@link StringComparator#compare(String, String)} with null greater.
	 */
	@Test
	public void testCompareNullGreater()
	{
		String alex = "Alex";
		String bill = "Billy";
		String leo = "Leon";

		comparator = StringComparator.of(true);

		actual = comparator.compare(alex, leo);
		expected = actual < 0;
		assertTrue(expected);

		actual = comparator.compare(leo, alex);
		expected = 0 < actual;
		assertTrue(expected);

		actual = comparator.compare(alex, bill);
		expected = actual < 0;
		assertTrue(expected);
		// null is greater...
		actual = comparator.compare(null, bill);
		expected = actual > 0;
		assertTrue(expected);

		// Now lets see a demo on a list...
		List<String> list = ListFactory.newArrayList();
		list.add(leo);
		list.add(alex);
		list.add(bill);
		list.add(null);
		list.add(null);

		expected = list.indexOf(leo) == 0;
		assertTrue(expected);
		expected = list.indexOf(alex) == 1;
		assertTrue(expected);
		expected = list.indexOf(bill) == 2;
		assertTrue(expected);

		Collections.sort(list, comparator);

		expected = list.indexOf(alex) == 0;
		assertTrue(expected);
		expected = list.indexOf(bill) == 1;
		assertTrue(expected);
		expected = list.indexOf(leo) == 2;
		assertTrue(expected);

	}

}
