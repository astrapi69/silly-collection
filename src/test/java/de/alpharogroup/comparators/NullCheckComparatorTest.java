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

import static org.testng.AssertJUnit.*;

import java.util.Comparator;
import java.util.Locale;

import org.testng.annotations.Test;

public class NullCheckComparatorTest
{

	@Test
	public void testCompare()
	{
		int expected;
		int actual;
		Comparator<Locale> localeComparator = NullCheckComparator
			.<Locale> of(new LocaleComparator());
		actual = localeComparator.compare(Locale.CANADA, null);
		expected = 1;
		assertEquals(expected, actual);

		actual = localeComparator.compare(null, null);
		expected = 0;
		assertEquals(expected, actual);

		actual = localeComparator.compare(null, Locale.CANADA);
		expected = -1;
		assertEquals(expected, actual);

		// set null flag to true so null are greater...
		localeComparator = LocaleComparator.of(true);

		actual = localeComparator.compare(Locale.CANADA, null);
		expected = -1;
		assertEquals(expected, actual);

		actual = localeComparator.compare(null, null);
		expected = 0;
		assertEquals(expected, actual);

		actual = localeComparator.compare(null, Locale.CANADA);
		expected = 1;
		assertEquals(expected, actual);

	}

}
