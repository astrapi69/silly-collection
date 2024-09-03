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
package io.github.astrapi69.collection.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Comparator;
import java.util.Enumeration;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import io.github.astrapi69.comparator.NullCheckComparator;

/**
 * The unit test class for the class {@link IndexSortedProperties}.
 *
 * @author Asterios Raptis
 */
public class IndexSortedPropertiesTest
{

	/**
	 * Test for method {@link IndexSortedProperties#clear()}
	 */
	@Test
	public void testClear()
	{
		final Properties unsortedProperties = new Properties();
		unsortedProperties.put("B", "2");
		unsortedProperties.put("C", "3");
		unsortedProperties.put("A", "1");
		unsortedProperties.put("D", "4");
		unsortedProperties.put("E", "5");

		final IndexSortedProperties properties = IndexSortedProperties.of(unsortedProperties);
		int expected;
		int actual;
		expected = 4;
		actual = properties.indexOf("5");
		assertEquals(expected, actual);
		expected = 3;
		actual = properties.indexOf("4");
		assertEquals(expected, actual);
		expected = 2;
		actual = properties.indexOf("3");
		assertEquals(expected, actual);
		expected = 1;
		actual = properties.indexOf("2");
		assertEquals(expected, actual);
		expected = 0;
		actual = properties.indexOf("1");
		assertEquals(expected, actual);

		properties.clear();
		actual = properties.size();
		expected = 0;
		assertEquals(expected, actual);
	}

	/**
	 * Test for constructor of the class {@link IndexSortedProperties}.
	 */
	@Test
	public void testConstructor()
	{
		final Properties unsortedProperties = new Properties();
		unsortedProperties.put("B", "2");
		unsortedProperties.put("C", "3");
		unsortedProperties.put("A", "1");
		unsortedProperties.put("D", "4");

		Properties sortedProperties = new IndexSortedProperties(unsortedProperties);

		Enumeration<?> keys = sortedProperties.keys();
		int count = 1;
		while (keys.hasMoreElements())
		{
			final Object key = keys.nextElement();
			if (count == 1)
			{
				assertEquals(key, "A");
			}
			if (count == 2)
			{
				assertEquals(key, "B");
			}
			if (count == 3)
			{
				assertEquals(key, "C");
			}
			if (count == 4)
			{
				assertEquals(key, "D");
			}
			count++;
		}

		// Add a reverse comparator.
		sortedProperties = new IndexSortedProperties(unsortedProperties)
		{
			/**
			 * The serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected Comparator<Object> newComparator()
			{

				return NullCheckComparator.of(
					(final Object o1, final Object o2) -> o1.toString().compareTo(o2.toString()),
					false);
			}
		};

		keys = sortedProperties.keys();
		count = 1;
		while (keys.hasMoreElements())
		{
			final Object key = keys.nextElement();
			if (count == 1)
			{
				assertEquals(key, "D");
			}
			if (count == 2)
			{
				assertEquals(key, "C");
			}
			if (count == 3)
			{
				assertEquals(key, "B");
			}
			if (count == 4)
			{
				assertEquals(key, "A");
			}
			count++;
		}
	}

	/**
	 * Test for method {@link IndexSortedProperties#of()} and
	 * {@link IndexSortedProperties#of(Properties)}
	 */
	@Test
	public void testFactoryMethods()
	{
		final IndexSortedProperties indexSortedProperties = IndexSortedProperties.of();
		assertNotNull(indexSortedProperties);


		final Properties unsortedProperties = new Properties();
		unsortedProperties.put("B", "2");
		unsortedProperties.put("C", "3");
		unsortedProperties.put("A", "1");
		unsortedProperties.put("D", "4");
		unsortedProperties.put("E", "5");

		final IndexSortedProperties properties = IndexSortedProperties.of(unsortedProperties);
		int expected;
		int actual;
		expected = 4;
		actual = properties.indexOf("5");
		assertEquals(expected, actual);
		expected = 3;
		actual = properties.indexOf("4");
		assertEquals(expected, actual);
		expected = 2;
		actual = properties.indexOf("3");
		assertEquals(expected, actual);
		expected = 1;
		actual = properties.indexOf("2");
		assertEquals(expected, actual);
		expected = 0;
		actual = properties.indexOf("1");
		assertEquals(expected, actual);

	}

	/**
	 * Test for method {@link IndexSortedProperties#of()} and
	 * {@link IndexSortedProperties#of(Properties)}
	 */
	@Test
	public void testFactoryMethodsWithComparator()
	{

		int expected;
		int actual;
		final Properties unsortedProperties = new Properties();
		unsortedProperties.put("B", "2");
		unsortedProperties.put("C", "3");
		unsortedProperties.put("A", "1");
		unsortedProperties.put("D", "4");
		unsortedProperties.put("E", "5");

		IndexSortedProperties properties = IndexSortedProperties.of(unsortedProperties,
			NullCheckComparator
				.of((final Object o1, final Object o2) -> o1.toString().compareTo(o2.toString())));
		expected = 4;
		actual = properties.indexOf("5");
		assertEquals(expected, actual);
		expected = 3;
		actual = properties.indexOf("4");
		assertEquals(expected, actual);
		expected = 2;
		actual = properties.indexOf("3");
		assertEquals(expected, actual);
		expected = 1;
		actual = properties.indexOf("2");
		assertEquals(expected, actual);
		expected = 0;
		actual = properties.indexOf("1");
		assertEquals(expected, actual);

		properties = IndexSortedProperties.of(unsortedProperties,
			NullCheckComparator
				.of((final Object o1, final Object o2) -> o1.toString().compareTo(o2.toString())),
			false);
		expected = 4;
		actual = properties.indexOf("5");
		assertEquals(expected, actual);
		expected = 3;
		actual = properties.indexOf("4");
		assertEquals(expected, actual);
		expected = 2;
		actual = properties.indexOf("3");
		assertEquals(expected, actual);
		expected = 1;
		actual = properties.indexOf("2");
		assertEquals(expected, actual);
		expected = 0;
		actual = properties.indexOf("1");
		assertEquals(expected, actual);

	}

	/**
	 * Test for method {@link IndexSortedProperties#get(int)}
	 */
	@Test
	public void testGetInt()
	{

		final Properties unsortedProperties = new Properties();
		unsortedProperties.put("B", "2");
		unsortedProperties.put("C", "3");
		unsortedProperties.put("A", "1");
		unsortedProperties.put("D", "4");
		unsortedProperties.put("E", "5");

		final IndexSortedProperties properties = IndexSortedProperties.of(unsortedProperties);
		Object expected;
		Object actual;
		actual = properties.get(0);
		expected = "1";
		assertEquals(expected, actual);

		actual = properties.get(5);
		expected = null;
		assertEquals(expected, actual);


	}

	/**
	 * Test for method {@link IndexSortedProperties#getProperty(int)}
	 */
	@Test
	public void testGetPropertyInt()
	{
		final Properties unsortedProperties = new Properties();
		unsortedProperties.put("B", "2");
		unsortedProperties.put("C", "3");
		unsortedProperties.put("A", "1");
		unsortedProperties.put("D", "4");
		unsortedProperties.put("E", "5");

		final IndexSortedProperties properties = IndexSortedProperties.of(unsortedProperties);
		Object expected;
		Object actual;
		actual = properties.getProperty(0);
		expected = "1";
		assertEquals(expected, actual);

		actual = properties.getProperty(5);
		expected = null;
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link IndexSortedProperties#indexOf(Object)}
	 */
	@Test
	public void testIndexOf()
	{

		final Properties unsortedProperties = new Properties();
		unsortedProperties.put("B", "2");
		unsortedProperties.put("C", "3");
		unsortedProperties.put("A", "1");
		unsortedProperties.put("D", "4");

		final IndexSortedProperties properties = new IndexSortedProperties(unsortedProperties);

		properties.setProperty("E", "5");
		int expected;
		int actual;
		expected = 4;
		actual = properties.indexOf("5");
		assertEquals(expected, actual);
		expected = -1;
		actual = properties.indexOf("6");
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link IndexSortedProperties#putAll(java.util.Map)}
	 */
	@Test
	public void testPutAllMapOfQextendsObjectQextendsObject()
	{
		final Properties unsortedProperties = new Properties();
		unsortedProperties.put("B", "2");
		unsortedProperties.put("C", "3");
		unsortedProperties.put("A", "1");
		unsortedProperties.put("D", "4");
		unsortedProperties.put("E", "5");

		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.putAll(unsortedProperties);
		Object expected;
		Object actual;
		actual = properties.getProperty(0);
		expected = "1";
		assertEquals(expected, actual);

		actual = properties.getProperty(5);
		expected = null;
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link IndexSortedProperties#remove(int)}
	 */
	@Test
	public void testRemoveInt()
	{
		final Properties unsortedProperties = new Properties();
		unsortedProperties.put("B", "2");
		unsortedProperties.put("C", "3");
		unsortedProperties.put("A", "1");
		unsortedProperties.put("D", "4");
		unsortedProperties.put("E", "5");

		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.putAll(unsortedProperties);
		Object expected;
		Object actual;
		Object removed;

		removed = properties.remove(0);
		actual = removed;
		expected = "1";
		assertEquals(expected, actual);

		removed = properties.remove(0);
		actual = removed;
		expected = "2";
		assertEquals(expected, actual);

		removed = properties.remove(0);
		actual = removed;
		expected = "3";
		assertEquals(expected, actual);

		removed = properties.remove(0);
		actual = removed;
		expected = "4";
		assertEquals(expected, actual);

		removed = properties.remove(0);
		actual = removed;
		expected = "5";
		assertEquals(expected, actual);

		removed = properties.remove(0);
		actual = removed;
		expected = null;
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link IndexSortedProperties#remove(Object)}
	 */
	@Test
	public void testRemoveObject()
	{
		final Properties unsortedProperties = new Properties();
		unsortedProperties.put("B", "2");
		unsortedProperties.put("C", "3");
		unsortedProperties.put("A", "1");
		unsortedProperties.put("D", "4");
		unsortedProperties.put("E", "5");

		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.putAll(unsortedProperties);
		Object expected;
		Object actual;
		Object removed;

		removed = properties.remove("A");
		actual = removed;
		expected = "1";
		assertEquals(expected, actual);

		removed = properties.remove("B");
		actual = removed;
		expected = "2";
		assertEquals(expected, actual);

		removed = properties.remove("C");
		actual = removed;
		expected = "3";
		assertEquals(expected, actual);

		removed = properties.remove("D");
		actual = removed;
		expected = "4";
		assertEquals(expected, actual);

		removed = properties.remove("E");
		actual = removed;
		expected = "5";
		assertEquals(expected, actual);

		removed = properties.remove("E");
		actual = removed;
		expected = null;
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link IndexSortedProperties#remove(Object, Object)}
	 */
	@Test
	public void testRemoveObjectObject()
	{
		final Properties unsortedProperties = new Properties();
		unsortedProperties.put("B", "2");
		unsortedProperties.put("C", "3");
		unsortedProperties.put("A", "1");
		unsortedProperties.put("D", "4");
		unsortedProperties.put("E", "5");

		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.putAll(unsortedProperties);
		Object expected;
		Object actual;
		Object removed;

		removed = properties.remove("A", "1");
		actual = removed;
		expected = Boolean.TRUE;
		assertEquals(expected, actual);

		removed = properties.remove("B", "2");
		actual = removed;
		expected = Boolean.TRUE;
		assertEquals(expected, actual);

		removed = properties.remove("C", "3");
		actual = removed;
		expected = Boolean.TRUE;
		assertEquals(expected, actual);

		removed = properties.remove("D", "4");
		actual = removed;
		expected = Boolean.TRUE;
		assertEquals(expected, actual);

		removed = properties.remove("E", "5");
		actual = removed;
		expected = Boolean.TRUE;
		assertEquals(expected, actual);

		removed = properties.remove("E", "5");
		actual = removed;
		expected = Boolean.FALSE;
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link IndexSortedProperties#setProperty(String, String)}
	 */
	@Test
	public void testSetPropertyStringString()
	{
		final IndexSortedProperties properties = new IndexSortedProperties();
		properties.setProperty("B", "2");
		properties.setProperty("C", "3");
		properties.setProperty("A", "1");
		properties.setProperty("D", "4");
		properties.setProperty("E", "5");

		int expected;
		int actual;
		expected = 4;
		actual = properties.indexOf("5");
		assertEquals(expected, actual);
		expected = 3;
		actual = properties.indexOf("4");
		assertEquals(expected, actual);
		expected = 2;
		actual = properties.indexOf("3");
		assertEquals(expected, actual);
		expected = 1;
		actual = properties.indexOf("2");
		assertEquals(expected, actual);
		expected = 0;
		actual = properties.indexOf("1");
		assertEquals(expected, actual);
	}

}
