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
package de.alpharogroup.collections;

import java.util.LinkedHashSet;
import java.util.Set;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test class for the class InsertionOrderMap.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class InsertionOrderSetTest
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
	 * Test the InsertionOrderMap.
	 */
	@Test
	public void testInsertionOrderSet()
	{
		final String expected[] = { "value1", "value3", "value4", "value2" };
		int count = 0;
		final Set<String> set = new InsertionOrderSet<>();
		set.add("value1");
		set.add("value2");
		set.add("value3");
		set.add("value4");

		set.add("value2"); // Add the same object.

		for (final String element : set)
		{
			AssertJUnit.assertTrue(element.equals(expected[count]));
			count++;
		}

	}

	/**
	 * Test the InsertionOrderMap.
	 */
	@Test
	public void testLinkedHashSet()
	{
		final String expected[] = { "value1", "value2", "value3", "value4" };
		int count = 0;
		final Set<String> set = new LinkedHashSet<>();
		set.add("value1");
		set.add("value2");
		set.add("value3");
		set.add("value4");

		set.add("value2"); // Add the same object.

		for (final String element : set)
		{
			final String exp = expected[count];
			AssertJUnit.assertTrue(element.equals(exp));
			count++;
		}
	}

}
