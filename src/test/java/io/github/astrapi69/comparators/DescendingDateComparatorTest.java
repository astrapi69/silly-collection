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
import java.util.Date;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.astrapi69.date.CreateDateExtensions;
import io.github.astrapi69.collections.list.ListFactory;

/**
 * The unit test class for the class {@link DescendingDateComparator}.
 */
public class DescendingDateComparatorTest
{

	/** For use of the result of the comparator. */
	int actual;

	/** The comparator. */
	Comparator<Date> comparator;

	/** For use of the expected result. */
	boolean expected;

	/**
	 * Sets up method will be invoked before every unit test method
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@BeforeMethod
	public void setUp() throws Exception
	{
		comparator = new DateComparator();
	}

	/**
	 * Tear down method will be invoked after every unit test method
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@AfterMethod
	public void tearDown() throws Exception
	{
		comparator = null;
	}

	/**
	 * Test method for {@link DescendingDateComparator#compare(Date, Date)}.
	 */
	@Test
	public void testCompare()
	{
		final Date past = CreateDateExtensions.newDate(2009, 3, 26, 10, 37, 04);
		final Date before = CreateDateExtensions.newDate(2010, 3, 26, 10, 37, 04);

		final Date after = CreateDateExtensions.newDate(2010, 3, 27, 10, 37, 04);
		final Date future = CreateDateExtensions.newDate(2011, 3, 27, 10, 37, 04);

		final List<Date> dates = ListFactory.newArrayList();

		dates.add(before);
		dates.add(future);
		dates.add(past);
		dates.add(after);
		dates.add(null);
		assertTrue("List object with index 0 should be 'before'-object.",
			dates.get(0).equals(before));
		assertTrue("List object with index 1 should be 'future'-object.",
			dates.get(1).equals(future));
		assertTrue("List object with index 2 should be 'past'-object.", dates.get(2).equals(past));
		assertTrue("List object with index 3 should be 'after'-object.",
			dates.get(3).equals(after));
		assertTrue("List object with index 4 should be 'null'", dates.get(4) == null);
		// Sort collection with our DescendingDateComparator...
		comparator = new DescendingDateComparator();
		Collections.sort(dates, comparator);
		assertTrue("List object with index 0 should be 'future'-object.",
			dates.get(0).equals(future));
		assertTrue("List object with index 1 should be 'after'-object.",
			dates.get(1).equals(after));
		assertTrue("List object with index 2 should be 'before'-object.",
			dates.get(2).equals(before));
		assertTrue("List object with index 3 should be 'past'-object.", dates.get(3).equals(past));
		assertTrue("List object with index 4 should be 'null'", dates.get(4) == null);
	}

}
