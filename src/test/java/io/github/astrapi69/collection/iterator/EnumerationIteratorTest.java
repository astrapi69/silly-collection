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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.astrapi69.collection.list.ListFactory;

/**
 * The unit test class for the class {@link EnumerationIterator}.
 *
 * @author Asterios Raptis
 */
public class EnumerationIteratorTest
{


	/** The List for the test. */
	private final List<String> list = new ArrayList<String>()
	{
		/**
		 * The serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		{
			add("one");
			add("two");
			add("three");
			add("four");
		}
	};
	/** The EnumerationIterator. */
	private EnumerationIterator<String> iterator;

	/**
	 * Sets up method will be invoked before every unit test method in this class.
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@BeforeEach
	protected void setUp() throws Exception
	{
		final Enumeration<String> elem = Collections.enumeration(list);
		this.iterator = new EnumerationIterator<>(elem);
	}

	/**
	 * Tear down method will be invoked after every unit test method in this class.
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@AfterEach
	protected void tearDown() throws Exception
	{
	}

	/**
	 * Test method for {@link EnumerationIterator#EnumerationIterator(java.util.Enumeration)} .
	 */
	@Test
	public void testEnumerationIterator()
	{
		int iteratorLength = 0;
		while (this.iterator.hasNext())
		{
			final String stringNumber = this.iterator.next();
			System.out.println(stringNumber);
			iteratorLength++;
		}
		assertTrue(list.size() == iteratorLength, "The length from the array should be equal with "
			+ "the length from the enumerationIterator.");
	}

	/**
	 * Run the EnumerationIterator(E[]) constructor test.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testEnumerationIteratorConstructor() throws Exception
	{
		final Enumeration<String> elem = Collections.enumeration(list);
		final EnumerationIterator<String> result = new EnumerationIterator<>(elem);

		assertNotNull(result);
	}

	/**
	 * Run the boolean hasNext() method test.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testHasNextWithContainingOneObject() throws Exception
	{
		final Enumeration<String> elemements = Collections.enumeration(new ArrayList<String>()
		{
			/**
			 * The serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			{
				add("one");
			}
		});
		final EnumerationIterator<String> enumerationIterator = new EnumerationIterator<>(
			elemements);

		final boolean result = enumerationIterator.hasNext();
		assertEquals(true, result);
	}

	/**
	 * Run the boolean hasNext() method test.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testHasNextWithEmptyEnumeration() throws Exception
	{
		final Enumeration<String> elemements = Collections.enumeration(new ArrayList<String>());
		final EnumerationIterator<String> enumerationIterator = new EnumerationIterator<>(
			elemements);

		final boolean result = enumerationIterator.hasNext();

		assertEquals(false, result);
	}

	/**
	 * Run the boolean next() method test.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testNextContainingOneObject() throws Exception
	{
		final String expected = "foo";
		final Enumeration<String> elemements = Collections.enumeration(new ArrayList<String>()
		{
			/**
			 * The serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			{
				add(expected);
			}
		});
		final EnumerationIterator<String> enumerationIterator = new EnumerationIterator<>(
			elemements);

		final String actual = enumerationIterator.next();

		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	/**
	 * Run the boolean next() method test.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testNextContainingThreeObject() throws Exception
	{
		final String expected = "foo";
		final String expected2 = "bar";
		final String expected3 = "chu";
		final Enumeration<String> elemements = Collections.enumeration(new ArrayList<String>()
		{
			/**
			 * The serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			{
				add(expected);
				add(expected2);
				add(expected3);
			}
		});
		final EnumerationIterator<String> enumerationIterator = new EnumerationIterator<>(
			elemements);

		String actual = enumerationIterator.next();

		assertNotNull(actual);
		assertEquals(expected, actual);

		actual = enumerationIterator.next();

		assertNotNull(actual);
		assertEquals(expected2, actual);

		actual = enumerationIterator.next();

		assertNotNull(actual);
		assertEquals(expected3, actual);
	}

	/**
	 * Run the boolean next() method test.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testNextContainingTwoObject() throws Exception
	{
		final String expected = "foo";
		final String expected2 = "bar";
		final Enumeration<String> elemements = Collections.enumeration(new ArrayList<String>()
		{
			/**
			 * The serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			{
				add(expected);
				add(expected2);
			}
		});
		final EnumerationIterator<String> enumerationIterator = new EnumerationIterator<>(
			elemements);

		String actual = enumerationIterator.next();

		assertNotNull(actual);
		assertEquals(expected, actual);

		actual = enumerationIterator.next();

		assertNotNull(actual);
		assertEquals(expected2, actual);
	}

	/**
	 * Run the boolean next() method test.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testNextWithEmptyEnumeration() throws Exception
	{
		final Enumeration<String> elemements = Collections.enumeration(new ArrayList<String>());
		final EnumerationIterator<String> enumerationIterator = new EnumerationIterator<>(
			elemements);
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			enumerationIterator.next();
		});
	}

	/**
	 * Test for method {@link EnumerationIterator#remove()}
	 */
	@Test
	public void testRemove()
	{
		final Enumeration<String> elemements = Collections
			.enumeration(ListFactory.newArrayList("1", "2"));
		final EnumerationIterator<String> enumerationIterator = new EnumerationIterator<>(
			elemements);
		Assertions.assertThrows(UnsupportedOperationException.class, () -> {
			enumerationIterator.remove();
		});
	}

}
