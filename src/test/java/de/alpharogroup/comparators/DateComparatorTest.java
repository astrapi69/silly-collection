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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.date.CreateDateExtensions;

/**
 * The class DateComparatorTest.
 */
public class DateComparatorTest
{

	/** The comparator. */
	private Comparator<Date> comparator;

	/**
	 * Sets the up.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@BeforeMethod
	public void setUp() throws Exception
	{
		comparator = new DateComparator();
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@AfterMethod
	public void tearDown() throws Exception
	{
	}

	/**
	 * Test compare.
	 */
	@Test
	public void testCompare()
	{
		final Date past = CreateDateExtensions.newDate(2009, 3, 26, 10, 37, 04);
		final Date before = CreateDateExtensions.newDate(2010, 3, 26, 10, 37, 04);

		final Date after = CreateDateExtensions.newDate(2010, 3, 27, 10, 37, 04);
		final Date future = CreateDateExtensions.newDate(2011, 3, 27, 10, 37, 04);

		final List<Date> dates = new ArrayList<>();

		dates.add(before);
		dates.add(future);
		dates.add(past);
		dates.add(after);
		dates.add(null);
		int i = 1;
		for (final Date date : dates)
		{
			System.out.println(i + "." + date);
			i++;
		}
		assertTrue("List object with index 0 should be 'before'-object.",
			dates.get(0).equals(before));
		assertTrue("List object with index 1 should be 'future'-object.",
			dates.get(1).equals(future));
		assertTrue("List object with index 2 should be 'past'-object.", dates.get(2).equals(past));
		assertTrue("List object with index 3 should be 'after'-object.",
			dates.get(3).equals(after));
		assertTrue("List object with index 4 should be 'null'", dates.get(4) == null);
		// Sort collection with our DateComparator...
		Collections.sort(dates, comparator);
		System.out.println("===================================================");
		i = 1;
		for (final Date date : dates)
		{
			System.out.println(i + "." + date);
			i++;
		}

		assertTrue("List object with index 0 should be 'null'", dates.get(0) == null);
		assertTrue("List object with index 1 should be 'past'-object.", dates.get(1).equals(past));
		assertTrue("List object with index 2 should be 'before'-object.",
			dates.get(2).equals(before));
		assertTrue("List object with index 3 should be 'after'-object.",
			dates.get(3).equals(after));
		assertTrue("List object with index 4 should be 'future'-object.",
			dates.get(4).equals(future));
	}

	/**
	 * Test for the scenario when {@linkplain Date} objects are equal.
	 */
	@Test
	public void testEqual()
	{
	}

	/**
	 * Test for the scenario when an {@linkplain Date} object are greater than another
	 * {@linkplain Date} object.
	 */
	@Test
	public void testGreaterThan()
	{
	}

	/**
	 * Test for the scenario when an {@linkplain Date} object are less than another
	 * {@linkplain Date} object.
	 */
	@Test
	public void testLessThan()
	{
	}

}
