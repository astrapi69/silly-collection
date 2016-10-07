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
package de.alpharogroup.collections;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import lombok.experimental.ExtensionMethod;

/**
 * Test class for the class {@link ArrayExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
@ExtensionMethod(ArrayExtensions.class)
public class ArrayExtensionsTest
{

	/**
	 * Test for method {@link ArrayExtensions#getFirst(Object[])}
	 */
	@Test
	public void testGetFirst()
	{
		String expected = "1";
		final String numbers[] = { expected, "2", "3", "4", "5", "6", "7" };
		// Old vanilla java with static method...
		String actual = ArrayExtensions.getFirst(numbers);
		AssertJUnit.assertEquals(expected, actual);
		// use lombok extensions method
		actual = numbers.getFirst();
		AssertJUnit.assertEquals(expected, actual);
		final String empty[] = { };
		expected = null;
		actual = ArrayExtensions.getFirst(empty);
		AssertJUnit.assertEquals(expected, actual);
		expected = null;
		actual = ArrayExtensions.getFirst(null);
		AssertJUnit.assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link de.alpharogroup.collections.ArrayExtensions#isFirst(Object[], Object)}
	 * .
	 */
	@Test
	public void testIsFirst()
	{
		final String expected = "1";
		final String numbers[] = { expected, "2", "3", "4", "5", "6", "7" };

		boolean actual = numbers.isFirst(expected);
		AssertJUnit.assertTrue("", actual);

		actual = numbers.isFirst(null);
		AssertJUnit.assertFalse("", actual);


	}

	/**
	 * Test method for {@link de.alpharogroup.collections.ArrayExtensions#isLast(Object[], Object)}
	 * .
	 */
	@Test
	public void testIsLast()
	{
		final String expected = "7";
		final String numbers[] = { "1", "2", "3", "4", "5", "6", expected };

		boolean actual = numbers.isLast(expected);
		AssertJUnit.assertTrue("", actual);

		actual = numbers.isLast(null);
		AssertJUnit.assertFalse("", actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#getLast(Object[])}
	 */
	@Test
	public void testGetLast()
	{
		String expected = "7";
		final String numbers[] = { "1", "2", "3", "4", "5", "6", expected };
		// Old vanilla java with static method...
		String actual = ArrayExtensions.getLast(numbers);
		AssertJUnit.assertEquals(expected, actual);
		// use lombok extensions method
		actual = numbers.getLast();
		AssertJUnit.assertEquals(expected, actual);
		final String empty[] = { };
		expected = null;
		actual = ArrayExtensions.getLast(empty);
		AssertJUnit.assertEquals(expected, actual);
		expected = null;
		actual = ArrayExtensions.getLast(null);
		AssertJUnit.assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#getIndex(Object[], Object)}
	 */
	@Test
	public void testGetIndex()
	{
		int expected = 6;
		final String last = "7";
		final String numbers[] = { "1", "2", "3", "4", "5", "6", last };
		// Old vanilla java with static method...
		int actual = ArrayExtensions.getIndex(numbers, last);
		AssertJUnit.assertEquals(expected, actual);
		// use lombok extensions method
		actual = numbers.getIndex(last);
		AssertJUnit.assertEquals(expected, actual);
		final String empty[] = { };
		expected = -1;
		actual = ArrayExtensions.getIndex(empty, last);
		AssertJUnit.assertEquals(expected, actual);

		actual = ArrayExtensions.getIndex(empty, null);
		AssertJUnit.assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#indexOf(Object[], Object)}
	 */
	@Test
	public void testIndexOf()
	{
		int expected = 6;
		final String last = "7";
		final String numbers[] = { "1", "2", "3", "4", "5", "6", last };
		// Old vanilla java with static method...
		int actual = ArrayExtensions.indexOf(numbers, last);
		AssertJUnit.assertEquals(expected, actual);
		// use lombok extensions method
		actual = numbers.indexOf(last);
		AssertJUnit.assertEquals(expected, actual);
		final String empty[] = { };
		expected = -1;
		actual = ArrayExtensions.indexOf(empty, last);
		AssertJUnit.assertEquals(expected, actual);

		actual = ArrayExtensions.indexOf(empty, null);
		AssertJUnit.assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#getNextIndex(Object[], Object)}
	 */
	@Test
	public void testGetNextIndex()
	{
		int expected = 0;
		final String last = "7";
		final String first = "1";
		final String numbers[] = { first, "2", "3", "4", "5", "6", last };
		// use case with last...
		// Old vanilla java with static method...
		int actual = ArrayExtensions.getNextIndex(numbers, last);
		AssertJUnit.assertEquals(expected, actual);
		// use lombok extensions method
		actual = numbers.getNextIndex(last);
		AssertJUnit.assertEquals(expected, actual);
		// use case with first...
		expected = 1;
		actual = ArrayExtensions.getNextIndex(numbers, first);
		AssertJUnit.assertEquals(expected, actual);
		// use lombok extensions method
		actual = numbers.getNextIndex(first);
		AssertJUnit.assertEquals(expected, actual);
		// scenarios of empty or null value...
		final String empty[] = { };
		expected = -1;
		actual = ArrayExtensions.getNextIndex(empty, last);
		AssertJUnit.assertEquals(expected, actual);

		actual = ArrayExtensions.getNextIndex(empty, null);
		AssertJUnit.assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#getNextIndexes(Object[], Object, int)}
	 */
	@Test
	public void testGetNextIndexes()
	{
		int[] expected = { 0, 1 };
		final String last = "7";
		final String first = "1";
		final String numbers[] = { first, "2", "3", "4", "5", "6", last };
		// use case with last...
		// Old vanilla java with static method...
		int[] actual = ArrayExtensions.getNextIndexes(numbers, last, 2);
		AssertJUnit.assertEquals(expected[0], actual[0]);
		AssertJUnit.assertEquals(expected[1], actual[1]);
		// use lombok extensions method
		actual = numbers.getNextIndexes(last, 2);
		AssertJUnit.assertEquals(expected[0], actual[0]);
		AssertJUnit.assertEquals(expected[1], actual[1]);
		// use case with first...
		expected[0] = 1;
		expected[1] = 2;
		actual = ArrayExtensions.getNextIndexes(numbers, first, 2);
		AssertJUnit.assertEquals(expected[0], actual[0]);
		AssertJUnit.assertEquals(expected[1], actual[1]);
		// use lombok extensions method
		actual = numbers.getNextIndexes(first, 2);
		AssertJUnit.assertEquals(expected[0], actual[0]);
		AssertJUnit.assertEquals(expected[1], actual[1]);
		// scenarios of empty or null value...
		final String empty[] = { };
		expected = null;
		actual = ArrayExtensions.getNextIndexes(empty, last, 2);
		AssertJUnit.assertEquals(expected, actual);

		actual = ArrayExtensions.getNextIndexes(empty, null, 2);
		AssertJUnit.assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#getPreviousIndex(Object[], Object)}
	 */
	@Test
	public void testGetPreviousIndex()
	{
		int expected = 5;
		final String last = "7";
		final String first = "1";
		final String numbers[] = { first, "2", "3", "4", "5", "6", last };
		// use case with last...
		// Old vanilla java with static method...
		int actual = ArrayExtensions.getPreviousIndex(numbers, last);
		AssertJUnit.assertEquals(expected, actual);
		// use lombok extensions method
		actual = numbers.getPreviousIndex(last);
		AssertJUnit.assertEquals(expected, actual);
		// use case with first...
		expected = 6;
		actual = ArrayExtensions.getPreviousIndex(numbers, first);
		AssertJUnit.assertEquals(expected, actual);
		// use lombok extensions method
		actual = numbers.getPreviousIndex(first);
		AssertJUnit.assertEquals(expected, actual);
		// scenarios of empty or null value...
		final String empty[] = { };
		expected = -1;
		actual = ArrayExtensions.getPreviousIndex(empty, last);
		AssertJUnit.assertEquals(expected, actual);
		expected = -1;
		actual = ArrayExtensions.getPreviousIndex(empty, null);
		AssertJUnit.assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#getPreviousIndexes(Object[], Object, int)}
	 */
	@Test
	public void testGetPreviousIndexes()
	{
		int[] expected = { 5, 4 };
		final String last = "7";
		final String first = "1";
		final String numbers[] = { first, "2", "3", "4", "5", "6", last };
		// use case with last...
		// Old vanilla java with static method...
		int[] actual = ArrayExtensions.getPreviousIndexes(numbers, last, 2);
		AssertJUnit.assertEquals(expected[0], actual[0]);
		AssertJUnit.assertEquals(expected[1], actual[1]);
		// use lombok extensions method
		actual = numbers.getPreviousIndexes(last, 2);
		AssertJUnit.assertEquals(expected[0], actual[0]);
		AssertJUnit.assertEquals(expected[1], actual[1]);
		// use case with first...
		expected[0] = 6;
		expected[1] = 5;
		actual = ArrayExtensions.getPreviousIndexes(numbers, first, 2);
		AssertJUnit.assertEquals(expected[0], actual[0]);
		AssertJUnit.assertEquals(expected[1], actual[1]);
		// use lombok extensions method
		actual = numbers.getPreviousIndexes(first, 2);
		AssertJUnit.assertEquals(expected[0], actual[0]);
		AssertJUnit.assertEquals(expected[1], actual[1]);
		// scenarios of empty or null value...
		final String empty[] = { };
		expected = null;
		actual = ArrayExtensions.getPreviousIndexes(empty, last, 2);
		AssertJUnit.assertEquals(expected, actual);

		actual = empty.getPreviousIndexes(null, 2);
		AssertJUnit.assertEquals(expected, actual);
	}


}
