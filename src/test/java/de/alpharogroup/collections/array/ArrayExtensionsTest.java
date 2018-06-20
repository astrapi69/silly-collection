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
package de.alpharogroup.collections.array;

import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import lombok.experimental.ExtensionMethod;

/**
 * The unit test class for the class {@link ArrayExtensions}.
 *
 * @venrsion 1.0
 * @author Asterios Raptis
 */
@ExtensionMethod(ArrayExtensions.class)
// @Slf4j
public class ArrayExtensionsTest
{

	/**
	 * Test method for {@link ArrayExtensions#arraycopyWithSystem(T[], T[])}.
	 */
	@Test
	public void testArraycopyWithSystem()
	{
		boolean actual;
		boolean expected;
		Integer[] source;
		Integer[] destination;

		source = ArrayFactory.newArray(1, 2, 3, 4, 5, 6, 7, 8, 9);
		destination = new Integer[source.length];
		destination = ArrayExtensions.arraycopyWithSystem(source, destination);
		actual = Arrays.deepEquals(source, destination);
		expected = true;
		assertEquals(expected, actual);

		source = null;
		destination = ArrayExtensions.arraycopyWithSystem(source, destination);
		assertNull(destination);
	}

	/**
	 * Test for method {@link ArrayExtensions#asList(Object[])}
	 */
	@Test
	public void testAsList()
	{
		String actual;
		String expected;
		final String numbers[] = { "1", "2", "3", "4", "5", "6", "7" };
		final List<String> numberList = ArrayExtensions.asList(numbers);// lombok magic extension
																		// method
		for (int i = 0; i < numbers.length; i++)
		{
			expected = numbers[i];
			actual = numberList.get(i);
			assertEquals(expected, actual);
		}
	}

	/**
	 * Test for method {@link ArrayExtensions#getFirst(Object[])}
	 */
	@Test
	public void testGetFirst()
	{
		String expected;
		String actual;
		expected = "1";
		final String numbers[] = { expected, "2", "3", "4", "5", "6", "7" };
		// Old vanilla java with static method...
		actual = ArrayExtensions.getFirst(numbers);
		assertEquals(expected, actual);
		// use lombok extensions method
		actual = numbers.getFirst();
		assertEquals(expected, actual);
		final String empty[] = { };
		expected = null;
		actual = ArrayExtensions.getFirst(empty);
		assertEquals(expected, actual);
		expected = null;
		actual = ArrayExtensions.getFirst(null);
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#getIndex(Object[], Object)}
	 */
	@Test
	public void testGetIndex()
	{
		int expected;
		int actual;
		expected = 6;
		final String last = "7";
		final String numbers[] = { "1", "2", "3", "4", "5", "6", last };
		// Old vanilla java with static method...
		actual = ArrayExtensions.getIndex(numbers, last);
		assertEquals(expected, actual);
		// use lombok extensions method
		actual = numbers.getIndex(last);
		assertEquals(expected, actual);
		final String empty[] = { };
		expected = -1;
		actual = ArrayExtensions.getIndex(empty, last);
		assertEquals(expected, actual);

		actual = ArrayExtensions.getIndex(empty, null);
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#getLast(Object[])}
	 */
	@Test
	public void testGetLast()
	{
		String expected;
		String actual;
		expected = "7";
		final String numbers[] = { "1", "2", "3", "4", "5", "6", expected };
		// Old vanilla java with static method...
		actual = ArrayExtensions.getLast(numbers);
		assertEquals(expected, actual);
		// use lombok extensions method
		actual = numbers.getLast();
		assertEquals(expected, actual);
		final String empty[] = { };
		expected = null;
		actual = ArrayExtensions.getLast(empty);
		assertEquals(expected, actual);
		expected = null;
		actual = ArrayExtensions.getLast(null);
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#getNextIndex(Object[], Object)}
	 */
	@Test
	public void testGetNextIndex()
	{
		int expected;
		int actual;
		expected = 0;
		final String last = "7";
		final String first = "1";
		final String numbers[] = { first, "2", "3", "4", "5", "6", last };
		// use case with last...
		// Old vanilla java with static method...
		actual = ArrayExtensions.getNextIndex(numbers, last);
		assertEquals(expected, actual);
		// use lombok extensions method
		actual = numbers.getNextIndex(last);
		assertEquals(expected, actual);
		// use case with first...
		expected = 1;
		actual = ArrayExtensions.getNextIndex(numbers, first);
		assertEquals(expected, actual);
		// use lombok extensions method
		actual = numbers.getNextIndex(first);
		assertEquals(expected, actual);
		// scenarios of empty or null value...
		final String empty[] = { };
		expected = -1;
		actual = ArrayExtensions.getNextIndex(empty, last);
		assertEquals(expected, actual);

		actual = ArrayExtensions.getNextIndex(empty, null);
		assertEquals(expected, actual);
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
		assertEquals(expected[0], actual[0]);
		assertEquals(expected[1], actual[1]);
		// use lombok extensions method
		actual = numbers.getNextIndexes(last, 2);
		assertEquals(expected[0], actual[0]);
		assertEquals(expected[1], actual[1]);
		// use case with first...
		expected[0] = 1;
		expected[1] = 2;
		actual = ArrayExtensions.getNextIndexes(numbers, first, 2);
		assertEquals(expected[0], actual[0]);
		assertEquals(expected[1], actual[1]);
		// use lombok extensions method
		actual = numbers.getNextIndexes(first, 2);
		assertEquals(expected[0], actual[0]);
		assertEquals(expected[1], actual[1]);
		// scenarios of empty or null value...
		final String empty[] = { };
		expected = null;
		actual = ArrayExtensions.getNextIndexes(empty, last, 2);
		assertEquals(expected, actual);

		actual = ArrayExtensions.getNextIndexes(empty, null, 2);
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#getPreviousIndex(Object[], Object)}
	 */
	@Test
	public void testGetPreviousIndex()
	{
		int expected;
		int actual;
		expected = 5;
		final String last = "7";
		final String first = "1";
		final String numbers[] = { first, "2", "3", "4", "5", "6", last };
		// use case with last...
		// Old vanilla java with static method...
		actual = ArrayExtensions.getPreviousIndex(numbers, last);
		assertEquals(expected, actual);
		// use lombok extensions method
		actual = numbers.getPreviousIndex(last);
		assertEquals(expected, actual);
		// use case with first...
		expected = 6;
		actual = ArrayExtensions.getPreviousIndex(numbers, first);
		assertEquals(expected, actual);
		// use lombok extensions method
		actual = numbers.getPreviousIndex(first);
		assertEquals(expected, actual);
		// scenarios of empty or null value...
		final String empty[] = { };
		expected = -1;
		actual = ArrayExtensions.getPreviousIndex(empty, last);
		assertEquals(expected, actual);
		expected = -1;
		actual = ArrayExtensions.getPreviousIndex(empty, null);
		assertEquals(expected, actual);
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
		assertEquals(expected[0], actual[0]);
		assertEquals(expected[1], actual[1]);
		// use lombok extensions method
		actual = numbers.getPreviousIndexes(last, 2);
		assertEquals(expected[0], actual[0]);
		assertEquals(expected[1], actual[1]);
		// use case with first...
		expected[0] = 6;
		expected[1] = 5;
		actual = ArrayExtensions.getPreviousIndexes(numbers, first, 2);
		assertEquals(expected[0], actual[0]);
		assertEquals(expected[1], actual[1]);
		// use lombok extensions method
		actual = numbers.getPreviousIndexes(first, 2);
		assertEquals(expected[0], actual[0]);
		assertEquals(expected[1], actual[1]);
		// scenarios of empty or null value...
		final String empty[] = { };
		expected = null;
		actual = ArrayExtensions.getPreviousIndexes(empty, last, 2);
		assertEquals(expected, actual);

		actual = empty.getPreviousIndexes(null, 2);
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#indexOf(Object[], Object)}
	 */
	@Test
	public void testIndexOf()
	{
		int expected;
		int actual;
		expected = 6;
		final String last = "7";
		final String numbers[] = { "1", "2", "3", "4", "5", "6", last };
		// Old vanilla java with static method...
		actual = ArrayExtensions.indexOf(numbers, last);
		assertEquals(expected, actual);
		// use lombok extensions method
		actual = numbers.indexOf(last);
		assertEquals(expected, actual);
		final String empty[] = { };
		expected = -1;
		actual = ArrayExtensions.indexOf(empty, last);
		assertEquals(expected, actual);

		actual = ArrayExtensions.indexOf(empty, null);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.collections.array.ArrayExtensions#isFirst(Object[], Object)} .
	 */
	@Test
	public void testIsFirst()
	{
		boolean expected;
		boolean actual;
		final String numbers[] = { "1", "2", "3", "4", "5", "6", "7" };

		actual = numbers.isFirst("1");

		expected = true;
		assertEquals(expected, actual);

		expected = false;
		actual = numbers.isFirst(null);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.collections.array.ArrayExtensions#isLast(Object[], Object)} .
	 */
	@Test
	public void testIsLast()
	{
		boolean expected;
		boolean actual;

		final String numbers[] = { "1", "2", "3", "4", "5", "6", "7" };

		expected = true;
		actual = numbers.isLast("7");
		assertEquals(expected, actual);

		expected = false;
		actual = numbers.isLast(null);
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#splitInChunks(byte[], int)}
	 */
	@Test
	public void testSplit()
	{
		int expected;
		int actual;
		final String byteString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

		final byte[] byteArray = byteString.getBytes();

		expected = 56;
		actual = byteArray.length;
		assertEquals(expected, actual);

		final byte[][] splitedBytes = ArrayExtensions.splitInChunks(byteArray, 5);

		expected = 12;
		actual = splitedBytes.length;
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#toList(Object[])}
	 */
	@Test
	public void testToList()
	{
		String actual;
		String expected;
		final String numbers[] = { "1", "2", "3", "4", "5", "6", "7" };
		final List<String> numberList = numbers.toList();// lombok magic extension method
		for (int i = 0; i < numbers.length; i++)
		{
			expected = numbers[i];
			actual = numberList.get(i);
			assertEquals(expected, actual);
		}
	}

	/**
	 * Test method for {@link ArrayExtensions} with {@link BeanTester}
	 */
	@Test(expectedExceptions = { BeanTestException.class, InvocationTargetException.class,
			UnsupportedOperationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ArrayExtensions.class);
	}

}
