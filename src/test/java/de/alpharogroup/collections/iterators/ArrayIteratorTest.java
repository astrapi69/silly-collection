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
package de.alpharogroup.collections.iterators;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test class for the class ArrayIterator.
 *
 * @version 1.0
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
	 * Executes every time before a test method.
	 */
	@BeforeMethod
	protected void setUp()
	{
		this.testArray = new Integer[4];
		this.testArray[0] = new Integer("0");
		this.testArray[1] = new Integer("1");
		this.testArray[2] = new Integer("2");
		this.testArray[3] = new Integer("3");
		this.iterator = new ArrayIterator<>(this.testArray);
	}

	/**
	 * Executes every time after a test method.
	 */
	@AfterMethod
	protected void tearDown()
	{
	}

	/**
	 * Test method for iterate through the instance variable iterator and makes an addition of the
	 * numbers in the array.
	 */
	@Test(enabled = true)
	public void testArrayIterator()
	{
		int iteratorLength = 0;
		int sum = 0;
		while (this.iterator.hasNext())
		{
			final Integer integer = this.iterator.next();
			sum += integer;
			iteratorLength++;
		}
		AssertJUnit.assertTrue("The sum should be 6.", sum == 6);
		AssertJUnit.assertTrue("The length from the array should be equal with "
			+ "the length from the ArrayIterator.", this.testArray.length == iteratorLength);
	}

	/**
	 * Run the ArrayIterator(E[]) constructor test.
	 */
	@Test(enabled = true)
	public void testArrayIteratorConstructor()
	{

		final ArrayIterator<Object> arrayIterator = new ArrayIterator<>(new Object[] { });

		AssertJUnit.assertNotNull(arrayIterator);
		AssertJUnit.assertEquals(false, arrayIterator.hasNext());
	}

	/**
	 * Run the boolean hasNext() method test.
	 */
	@Test(enabled = true)
	public void testHasNextWithArrayContainingOneObject()
	{
		final ArrayIterator<Object> fixture = new ArrayIterator<>(new Object[] { "foo" });

		final boolean result = fixture.hasNext();

		AssertJUnit.assertEquals(true, result);
	}

	/**
	 * Run the boolean hasNext() method test.
	 */
	@Test(enabled = true)
	public void testHasNextWithEmptyArray()
	{
		final ArrayIterator<Object> arrayIterator = new ArrayIterator<>(new Object[] { });

		final boolean result = arrayIterator.hasNext();

		AssertJUnit.assertEquals(false, result);
	}

	/**
	 * Run the boolean next() method test.
	 */
	@Test(enabled = true)
	public void testNextWithArrayContainingOneObject()
	{
		final String expected = "foo";
		final ArrayIterator<String> fixture = new ArrayIterator<>(new String[] { expected });

		final String actual = fixture.next();

		AssertJUnit.assertNotNull(actual);
		AssertJUnit.assertEquals(expected, actual);
	}

	/**
	 * Run the boolean next() method test.
	 */
	@Test(enabled = true)
	public void testNextWithArrayContainingTwoObject()
	{
		final String foo = "foo";
		final String bar = "bar";
		final ArrayIterator<String> fixture = new ArrayIterator<>(new String[] { foo, bar });

		String actual = fixture.next();

		AssertJUnit.assertNotNull(actual);
		AssertJUnit.assertEquals(foo, actual);

		actual = fixture.next();

		AssertJUnit.assertNotNull(actual);
		AssertJUnit.assertEquals(bar, actual);
	}

	/**
	 * Run the Object next() method test.
	 */
	@Test(expectedExceptions = java.util.NoSuchElementException.class)
	public void testNextWithEmptyArray()
	{
		final ArrayIterator<Object> fixture = new ArrayIterator<>(new Object[] { });
		fixture.next();
	}

	/**
	 * Run the void remove() method test.
	 */
	@Test(enabled = true)
	public void testRemoveWithContainingOneObject()
	{
		final String expected = "foo";

		final ArrayIterator<String> fixture = new ArrayIterator<>(new String[] { expected });
		fixture.next();
		fixture.remove();

		final boolean result = fixture.hasNext();

		AssertJUnit.assertEquals(false, result);

	}

	/**
	 * Run the void remove() method test.
	 */
	@Test(enabled = true)
	public void testRemoveWithContainingThreeObject()
	{
		final String foo = "foo";
		final String bar = "bar";
		final String jack = "jack";
		final ArrayIterator<String> fixture = new ArrayIterator<>(new String[] { foo, bar, jack });
		fixture.next();
		fixture.remove();

		boolean result = fixture.hasNext();

		AssertJUnit.assertEquals(true, result);

		fixture.remove();
		result = fixture.hasNext();

		AssertJUnit.assertEquals(false, result);
	}

	/**
	 * Run the void remove() method test.
	 */
	@Test(enabled = true)
	public void testRemoveWithContainingTwoObject()
	{
		final String foo = "foo";
		final String bar = "bar";
		final ArrayIterator<String> fixture = new ArrayIterator<>(new String[] { foo, bar });
		fixture.next();
		fixture.remove();

		final boolean result = fixture.hasNext();

		AssertJUnit.assertEquals(false, result);
	}

	/**
	 * Run the void remove() method test.
	 */
	@Test(expectedExceptions = java.lang.IllegalStateException.class)
	public void testRemoveWithEmptyArray()
	{
		final ArrayIterator<Object> fixture = new ArrayIterator<>(new Object[] { });

		fixture.remove();
	}

}
