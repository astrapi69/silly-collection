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
package io.github.astrapi69.collections.set;

import static org.testng.AssertJUnit.assertTrue;

import java.util.Arrays;
import java.util.Set;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The unit test class for the class {@link InsertionOrderSet}
 *
 * @author Asterios Raptis
 */
public class InsertionOrderSetTest
{

	/**
	 * Sets up method will be invoked before every unit test method
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@BeforeMethod
	protected void setUp() throws Exception
	{
	}

	/**
	 * Tear down method will be invoked after every unit test method
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@AfterMethod
	protected void tearDown() throws Exception
	{
	}

	/**
	 * Test method for {@link InsertionOrderSet#add(Object)}
	 */
	@Test
	public void testAddE()
	{
		final String[] expected = { "value1", "value3", "value4", "value2" };
		int count = 0;
		final Set<String> set = new InsertionOrderSet<>(4, 4f);
		set.add("value1");
		set.add("value2");
		set.add("value3");
		set.add("value4");

		set.add("value2"); // Add the same object.

		for (final String element : set)
		{
			assertTrue(element.equals(expected[count]));
			count++;
		}
	}

	/**
	 * Test the no argument constructor of {@link InsertionOrderSet}.
	 */
	@Test
	public void testInsertionOrderSet()
	{
		final String[] expected = { "value1", "value3", "value4", "value2" };
		int count = 0;
		final Set<String> set = new InsertionOrderSet<>();
		set.add("value1");
		set.add("value2");
		set.add("value3");
		set.add("value4");

		set.add("value2"); // Add the same object.

		for (final String element : set)
		{
			assertTrue(element.equals(expected[count]));
			count++;
		}

	}

	/**
	 * Test method for {@link InsertionOrderSet#add(Object)} initialized with the constructor that
	 * takes a collection
	 */
	@Test
	public void testInsertionOrderSetCollectionOfQextendsE()
	{

		final String[] expected = { "value1", "value3", "value4", "value2" };

		final String[] elements = { "value1", "value2", "value3", "value4" };

		final Set<String> set = new InsertionOrderSet<>(Arrays.asList(elements));

		set.add("value2"); // Add the same object.
		int count = 0;
		for (final String element : set)
		{
			final String exp = expected[count];
			assertTrue(element.equals(exp));
			count++;
		}
	}

	/**
	 * Test the constructor with the initial capacity of {@link InsertionOrderSet}.
	 */
	@Test
	public void testInsertionOrderSetInt()
	{
		final String[] expected = { "value1", "value3", "value4", "value2" };
		int count = 0;
		final Set<String> set = new InsertionOrderSet<>(4);
		set.add("value1");
		set.add("value2");
		set.add("value3");
		set.add("value4");

		set.add("value2"); // Add the same object.

		for (final String element : set)
		{
			assertTrue(element.equals(expected[count]));
			count++;
		}
	}

	/**
	 * Test the constructor with the initial capacity and the load factor of
	 * {@link InsertionOrderSet}.
	 */
	@Test
	public void testInsertionOrderSetIntFloat()
	{
		final String[] expected = { "value1", "value3", "value4", "value2" };
		int count = 0;
		final Set<String> set = new InsertionOrderSet<>(4, 4f);
		set.add("value1");
		set.add("value2");
		set.add("value3");
		set.add("value4");

		set.add("value2"); // Add the same object.

		for (final String element : set)
		{
			assertTrue(element.equals(expected[count]));
			count++;
		}
	}

	/**
	 * Test method for {@link InsertionOrderSet#setOf(Object...)}.
	 */
	@Test
	public void testInsertionOrderSetOf()
	{
		final String[] expected = { "value1", "value3", "value4", "value2" };

		final Set<String> set = InsertionOrderSet.setOf("value1", "value2", "value3", "value4");

		set.add("value2"); // Add the same object.
		int count = 0;
		for (final String element : set)
		{
			final String exp = expected[count];
			assertTrue(element.equals(exp));
			count++;
		}
	}

}
