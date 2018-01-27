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

import java.util.Comparator;
import java.util.Locale;

import org.testng.annotations.Test;

/**
 * The unit test class for the class {@link LocaleComparator}.
 */
public class LocaleComparatorTest
{

	boolean expected;
	int actual;

	/**
	 * Test method for {@link LocaleComparator#compare(Locale, Locale)}
	 */
	@Test
	public void testCompare()
	{
		final Comparator<Locale> comparator = LocaleComparator.of();

		actual = comparator.compare(Locale.CANADA, Locale.CANADA);
		expected = actual == 0;
		assertTrue(expected);

		actual = comparator.compare(Locale.CANADA, Locale.GERMAN);
		expected = actual > 0;
		assertTrue(expected);
	}

}
