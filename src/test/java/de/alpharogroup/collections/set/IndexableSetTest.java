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
package de.alpharogroup.collections.set;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

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
	@Test(expectedExceptions = IndexOutOfBoundsException.class)
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
		set.get(5);
	}

	/**
	 * Test method for {@link IndexableSet#get(int)} with index over the minimum
	 */
	@Test(expectedExceptions = IndexOutOfBoundsException.class)
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
		// new scenario..
		set.get(-1);
	}

	/**
	 * Test method for {@link IndexableSet#get(int)} with index over the minimum
	 */
	@Test(expectedExceptions = IndexOutOfBoundsException.class)
	public void testGetThrowIndexOutOfBoundsExceptionWithEmptySet()
	{
		IndexableSet<String> set;
		// init test data
		set = new IndexableSet<>();
		// test
		// new scenario..
		set.get(0);
	}

}
