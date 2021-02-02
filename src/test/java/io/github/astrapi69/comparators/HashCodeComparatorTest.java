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

import java.util.Comparator;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The unit test class for the class {@link HashCodeComparator}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class HashCodeComparatorTest
{

	/** For use of the result of the comparator. */
	int actual;

	/** The comparator. */
	Comparator<String> comparator;

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
		comparator = new HashCodeComparator<>();
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
	 * Test method for {@link HashCodeComparator#compare(java.lang.Object, java.lang.Object)} .
	 */
	@Test
	public void testCompare()
	{
		final String testString = "Albert";
		final String notExpected = "Asterios";
		final String expected = "Albert";

		assertTrue(
			"Hashcodes should be " + testString.hashCode() + ">" + notExpected.hashCode() + ".",
			comparator.compare(testString, notExpected) == 1);

		comparator.compare(notExpected, testString);
		assertTrue(
			"Hashcodes should be " + notExpected.hashCode() + "<" + testString.hashCode() + ".",
			comparator.compare(notExpected, testString) == -1);

		comparator.compare(testString, expected);
		assertTrue("Hashcodes should be " + testString.hashCode() + "=" + expected.hashCode() + ".",
			comparator.compare(testString, expected) == 0);
	}

}
