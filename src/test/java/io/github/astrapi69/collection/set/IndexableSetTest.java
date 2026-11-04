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
package io.github.astrapi69.collection.set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The unit test class for the class {@link IndexableSet}
 *
 * @author Asterios Raptis
 */
public class IndexableSetTest
{

	/**
	 * Test method for {@link IndexableSet#get(int)}
	 */
	@Test
	public void testGet()
	{
		String actual;
		String expected;

		IndexableSet<String> set;
		// init test data
		set = new IndexableSet<>();
		set.add("value1");
		set.add("value2");
		set.add("value3");
		set.add("value4");
		// test
		// new scenario..
		actual = set.get(1);
		expected = "value2";
		assertEquals(actual, expected);
		// new scenario..
		set.add("value2");
		actual = set.get(3);
		expected = "value2";
		assertEquals(actual, expected);
		// new scenario..
		actual = set.get(0);
		expected = "value1";
		assertEquals(actual, expected);
		// new scenario..
		actual = set.get(1);
		expected = "value3";
		assertEquals(actual, expected);
		// new scenario..
		actual = set.get(2);
		expected = "value4";
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link IndexableSet#getIndex(Object)}
	 */
	@Test
	public void testGetIndex()
	{
		int actual;
		int expected;
		IndexableSet<String> set;
		// init test data
		set = new IndexableSet<>();
		set.add("value1");
		set.add("value2");
		set.add("value3");
		set.add("value4");
		// test
		// new scenario..
		actual = set.getIndex("value2");
		expected = 1;
		assertEquals(actual, expected);
		// new scenario..
		set.add("value2");
		actual = set.getIndex("value2");
		expected = 3;
		assertEquals(actual, expected);
		// new scenario..
		actual = set.getIndex("value5");
		expected = -1;
		assertEquals(actual, expected);

	}


	/**
	 * Test method for {@link IndexableSet#get(int)} with index over the maximum
	 */
	@Test
	public void testGetThrowIndexOutOfBoundsExceptionOverMaximun()
	{
		IndexableSet<String> set;
		// init test data
		set = new IndexableSet<>();
		set.add("value1");
		set.add("value2");
		set.add("value3");
		set.add("value4");
		// test
		// new scenario..
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
			set.get(5);
		});
	}

	/**
	 * Test method for {@link IndexableSet#get(int)} with index over the minimum
	 */
	@Test
	public void testGetThrowIndexOutOfBoundsExceptionOverMinimum()
	{
		IndexableSet<String> set;
		// init test data
		set = new IndexableSet<>();
		set.add("value1");
		set.add("value2");
		set.add("value3");
		set.add("value4");
		// test
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
			set.get(-1);
		});
	}

	/**
	 * Test the method {@link IndexableSet#get(int)} at the upper bound of the index, as its Javadoc
	 * names it (index &gt;= size()): size() - 1 gives the last element, size() throws an
	 * IndexOutOfBoundsException
	 */
	@Test
	public void testGetThrowIndexOutOfBoundsExceptionAtSize()
	{
		final IndexableSet<String> set = new IndexableSet<>();
		set.add("value1");
		set.add("value2");
		set.add("value3");
		assertEquals("value3", set.get(2));
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> set.get(3));
	}

	/**
	 * Test method for {@link IndexableSet#get(int)} with index over the minimum
	 */
	@Test
	public void testGetThrowIndexOutOfBoundsExceptionWithEmptySet()
	{
		IndexableSet<String> set;
		// init test data
		set = new IndexableSet<>();
		// test
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
			set.get(0);
		});
	}

	/**
	 * Test the method {@link IndexableSet#add(Object)}, inherited from {@link InsertionOrderSet},
	 * with an element the set holds already: the element moves to the last index, and today the
	 * method returns true (#41); the fix of #41 changes the result to false
	 */
	@Test
	public void testAddWithContainedElement()
	{
		final IndexableSet<String> set = new IndexableSet<>();
		set.add("a");
		set.add("b");

		assertTrue(set.add("a"));
		assertEquals(1, set.getIndex("a"));
		assertEquals("b", set.get(0));
	}

	/**
	 * Test the method {@link IndexableSet#addAll(Collection)}, inherited from
	 * {@link InsertionOrderSet}, with an element the set holds already: today it returns true
	 * (#41), although the set holds no new element; the fix of #41 changes the result to false
	 */
	@Test
	public void testAddAllWithContainedElement()
	{
		final IndexableSet<String> set = new IndexableSet<>();
		set.add("a");

		assertTrue(set.addAll(Arrays.asList("a")));
		assertEquals(1, set.size());
	}

	/**
	 * Test the method {@link IndexableSet#getIndex(Object)} with a set that holds null and "a":
	 * today it throws a NullPointerException as soon as its loop reaches null, for "a" and for null
	 * (#41); the fix of #41 changes the result to the indexes 1 and 0
	 */
	@Test
	public void testGetIndexWithNullElement()
	{
		final IndexableSet<String> set = new IndexableSet<>();
		set.add(null);
		set.add("a");

		Assertions.assertThrows(NullPointerException.class, () -> set.getIndex("a"));
		Assertions.assertThrows(NullPointerException.class, () -> set.getIndex(null));
	}

}
