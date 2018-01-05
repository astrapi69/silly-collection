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

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.testng.annotations.Test;

/**
 * The unit test class for the class {@link StringLengthComparator}.
 */
public class StringLengthComparatorTest
{

	boolean expected;
	int actual;

	/**
	 * Test method for {@link StringLengthComparator#compare(String, String)}
	 */
	@Test
	public void testCompare()
	{
		String alex = "Alex";
		String bill = "Billy";
		String leo = "Leon";

		Comparator<String> comparator = StringLengthComparator.of();

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
		actual = comparator.compare(alex, bill);
		expected = 0 < actual;
		assertTrue(expected);
		// Now lets see a demo on a list...
		List<String> list = new ArrayList<>();
		list.add(leo);
		list.add(alex);
		list.add(bill);

		expected = list.indexOf(leo) == 0;
		assertTrue(expected);
		expected = list.indexOf(alex) == 1;
		assertTrue(expected);
		expected = list.indexOf(bill) == 2;
		assertTrue(expected);

		Collections.sort(list, comparator);

		expected = list.indexOf(bill) == 0;
		assertTrue(expected);
		expected = list.indexOf(alex) == 1;
		assertTrue(expected);
		expected = list.indexOf(leo) == 2;
		assertTrue(expected);

	}

}
