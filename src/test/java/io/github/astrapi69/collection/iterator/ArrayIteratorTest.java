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
package io.github.astrapi69.collection.iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;

/**
 * The unit test class for the class {@link ArrayIterator}.
 *
 * @author Asterios Raptis
 */
public class ArrayIteratorTest
{

	/**
	 * The instance from ArrayIterator for tests.
	 */
	private ArrayIterator<Integer> iterator;

	/**
	 * The instance from an object array for tests.
	 */
	private Integer[] testArray;

	/**
	 * Sets up method will be invoked before every unit test method
	 */
	@BeforeEach
	protected void setUp()
	{
		this.testArray = new Integer[4];
		this.testArray[0] = Integer.valueOf("0");
		this.testArray[1] = Integer.valueOf("1");
		this.testArray[2] = Integer.valueOf("2");
		this.testArray[3] = Integer.valueOf("3");
		this.iterator = new ArrayIterator<>(this.testArray);
	}

	/**
	 * Tear down method will be invoked after every unit test method
	 */
	@AfterEach
	protected void tearDown()
	{
	}

	/**
	 * Test method for iterate through the instance variable iterator and makes an addition of the
	 * numbers in the array.
	 */
	@Test
	public void testArrayIterator()
	{
		int expected;
		int actual;
		int iteratorLength = 0;
		int sum = 0;
		while (this.iterator.hasNext())
		{
			final Integer integer = this.iterator.next();
			sum += integer;
			iteratorLength++;
		}
		expected = 6;
		actual = sum;
		assertEquals(expected, actual, "The sum should be 6.");

		expected = this.testArray.length;
		actual = iteratorLength;
		assertEquals(expected, actual, "The length from the array should be equal with "
			+ "the length from the ArrayIterator.");
	}

	/**
	 * Test method for the ArrayIterator(E[]) constructor with an empty array and the method
	 * {@link ArrayIterator#hasNext()}.
	 */
	@Test
	public void testArrayIteratorConstructor()
	{
		boolean expected;
		boolean actual;
		Object[] array = new Object[] { };
		final ArrayIterator<Object> arrayIterator = new ArrayIterator<>(array);
		assertNotNull(arrayIterator);

		assertEquals(array, arrayIterator.getArray());

		expected = false;
		actual = arrayIterator.hasNext();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for the ArrayIterator(E[]) constructor with an array with one object and the
	 * method {@link ArrayIterator#hasNext()}.
	 */
	@Test
	public void testHasNextWithArrayContainingOneObject()
	{
		boolean expected;
		boolean actual;
		final ArrayIterator<Object> arrayIterator = new ArrayIterator<>(new Object[] { "foo" });
		assertNotNull(arrayIterator);

		expected = true;
		actual = arrayIterator.hasNext();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for the ArrayIterator(E[]) constructor with an empty array and the method
	 * {@link ArrayIterator#hasNext()}.
	 */
	@Test
	public void testHasNextWithEmptyArray()
	{
		boolean expected;
		boolean actual;

		final ArrayIterator<Object> arrayIterator = new ArrayIterator<>(new Object[] { });
		assertNotNull(arrayIterator);

		expected = false;
		actual = arrayIterator.hasNext();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for the ArrayIterator(E[]) constructor with an array with one object and the
	 * method {@link ArrayIterator#next()}.
	 */
	@Test
	public void testNextWithArrayContainingOneObject()
	{
		String expected;
		String actual;
		expected = "foo";
		final ArrayIterator<String> fixture = new ArrayIterator<>(new String[] { expected });

		actual = fixture.next();

		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for the ArrayIterator(E[]) constructor with an array with two objects and the
	 * method {@link ArrayIterator#next()}.
	 */
	@Test
	public void testNextWithArrayContainingTwoObject()
	{
		String expected;
		String actual;
		final String foo = "foo";
		final String bar = "bar";
		final ArrayIterator<String> fixture = new ArrayIterator<>(new String[] { foo, bar });

		actual = fixture.next();
		assertNotNull(actual);

		expected = foo;
		assertEquals(expected, actual);

		actual = fixture.next();
		assertNotNull(actual);

		expected = bar;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for the ArrayIterator(E[]) constructor with an empty array and the method
	 * {@link ArrayIterator#next()} with expected {@link NoSuchElementException}.
	 */
	@Test
	public void testNextWithEmptyArray()
	{
		final ArrayIterator<Object> fixture = new ArrayIterator<>(new Object[] { });

		Assertions.assertThrows(NoSuchElementException.class, () -> {
			fixture.next();
		});
	}

	/**
	 * Test method for the ArrayIterator(E[]) constructor with an array with one object and the
	 * method {@link ArrayIterator#remove()}and the method {@link ArrayIterator#next()}.
	 */
	@Test
	public void testRemoveWithContainingOneObject()
	{
		boolean expected;
		boolean actual;
		final String foo = "foo";

		final ArrayIterator<String> arrayIterator = new ArrayIterator<>(new String[] { foo });
		arrayIterator.next();
		arrayIterator.remove();

		expected = false;
		actual = arrayIterator.hasNext();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for the ArrayIterator(E[]) constructor with an array with three objects and the
	 * method {@link ArrayIterator#remove()}.
	 */
	@Test
	public void testRemoveWithContainingThreeObject()
	{
		boolean expected;
		boolean actual;
		final String foo = "foo";
		final String bar = "bar";
		final String jack = "jack";
		final ArrayIterator<String> arrayIterator = new ArrayIterator<>(
			new String[] { foo, bar, jack });
		arrayIterator.next();
		arrayIterator.remove();

		expected = true;
		actual = arrayIterator.hasNext();
		assertEquals(expected, actual);

		arrayIterator.remove();

		expected = false;
		actual = arrayIterator.hasNext();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for the ArrayIterator(E[]) constructor with an array with two objects and the
	 * method {@link ArrayIterator#remove()}
	 */
	@Test
	public void testRemoveWithContainingTwoObject()
	{
		final boolean expected;
		final boolean actual;
		final String foo = "foo";
		final String bar = "bar";
		final ArrayIterator<String> arrayIterator = new ArrayIterator<>(new String[] { foo, bar });
		arrayIterator.next();
		arrayIterator.remove();

		expected = false;
		actual = arrayIterator.hasNext();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for the ArrayIterator(E[]) constructor with an empty array and the method
	 * {@link ArrayIterator#remove()} with expected {@link IllegalStateException}.
	 */
	@Test
	public void testRemoveWithEmptyArray()
	{
		final ArrayIterator<Object> arrayIterator = new ArrayIterator<>(new Object[] { });

		Assertions.assertThrows(IllegalStateException.class, () -> {
			arrayIterator.remove();
		});
	}

	/**
	 * Test method for {@link ArrayIterator}
	 */
	@Test
	@Disabled("add factory method to config beanTester")
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ArrayIterator.class);
	}

}
