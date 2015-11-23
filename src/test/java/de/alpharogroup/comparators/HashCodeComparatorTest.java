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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test class for the class HashCodeComparator.
 * 
 * @version 1.0
 * @author Asterios Raptis
 */
public class HashCodeComparatorTest
{

	/**
	 * Sets up method will be invoked before every unit test method in this class.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@BeforeMethod
	protected void setUp() throws Exception
	{
	}

	/**
	 * Tear down method will be invoked after every unit test method in this class.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@AfterMethod
	protected void tearDown() throws Exception
	{
	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.comparators.HashCodeComparator#compare(java.lang.Object, java.lang.Object)}
	 * .
	 */
	@Test
	public void testCompare()
	{
		final String testString = "Albert";
		final String notExpected = "Asterios";
		final String expected = "Albert";

		final HashCodeComparator<String> comparator = new HashCodeComparator<>();
		int i = comparator.compare(testString, notExpected);
		System.out.println("comparator.compare(testString, notExpected)=i:" + i);
		System.out.println("testString.hashCode():" + testString.hashCode());
		System.out.println("notExpected.hashCode():" + notExpected.hashCode());
		System.out.println(testString.hashCode() + ">" + notExpected.hashCode());
		AssertJUnit.assertTrue(
			"Hashcodes should be " + testString.hashCode() + ">" + notExpected.hashCode() + ".",
			comparator.compare(testString, notExpected) == 1);

		i = comparator.compare(notExpected, testString);
		System.out.println("comparator.compare(notExpected, testString)=i:" + i);
		System.out.println("notExpected.hashCode():" + notExpected.hashCode());
		System.out.println("testString.hashCode():" + testString.hashCode());
		System.out.println(notExpected.hashCode() + "<" + testString.hashCode());
		AssertJUnit.assertTrue(
			"Hashcodes should be " + notExpected.hashCode() + "<" + testString.hashCode() + ".",
			comparator.compare(notExpected, testString) == -1);

		i = comparator.compare(testString, expected);
		System.out.println("comparator.compare(testString, expected)=i:" + i);
		System.out.println("testString.hashCode():" + testString.hashCode());
		System.out.println("expected.hashCode():" + expected.hashCode());
		System.out.println(testString.hashCode() + "=" + expected.hashCode());
		AssertJUnit.assertTrue(
			"Hashcodes should be " + testString.hashCode() + "=" + expected.hashCode() + ".",
			comparator.compare(testString, expected) == 0);
	}

}
