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

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import io.github.astrapi69.collection.list.ListFactory;
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
	 * Test the method {@link IndexSortedProperties#clear()} empties both the properties and the
	 * index, so that the first key put afterwards is the first one in the index
	 */
	@Test
	public void testClearEmptiesPropertiesAndIndex()
	{
		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.setProperty("B", "2");
		properties.setProperty("A", "1");

		properties.clear();
		assertTrue(properties.isEmpty());
		assertNull(properties.getProperty("A"));
		assertNull(properties.get(0));
		assertEquals(-1, properties.indexOf("1"));

		properties.setProperty("C", "3");
		assertEquals("3", properties.get(0));
		assertNull(properties.get(1));
		assertEquals(0, properties.indexOf("3"));
	}

	/**
	 * Test the method {@link IndexSortedProperties#clear()} on an empty object, whose index is
	 * empty, too
	 */
	@Test
	public void testClearWhenEmpty()
	{
		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.clear();
		assertTrue(properties.isEmpty());
		assertNull(properties.get(0));
	}

	/**
	 * Test for constructor of the class {@link IndexSortedProperties}: it reads the keys of the
	 * given defaults into its index, sorted with its comparator
	 */
	@Test
	public void testConstructor()
	{
		final Properties unsortedProperties = new Properties();
		unsortedProperties.put("B", "2");
		unsortedProperties.put("C", "3");
		unsortedProperties.put("A", "1");
		unsortedProperties.put("D", "4");

		IndexSortedProperties sortedProperties = new IndexSortedProperties(unsortedProperties);

		// the given properties are the defaults, which keys() does not list: the index holds their
		// keys A, B, C, D, and getProperty(int) returns the value of the key at an index
		int count = 0;
		while (sortedProperties.getProperty(count) != null)
		{
			assertTrue(count < 4, "the index holds more than the four keys of the defaults");
			final String value = sortedProperties.getProperty(count);
			if (count == 0)
			{
				assertEquals("1", value);
			}
			if (count == 1)
			{
				assertEquals("2", value);
			}
			if (count == 2)
			{
				assertEquals("3", value);
			}
			if (count == 3)
			{
				assertEquals("4", value);
			}
			count++;
		}
		assertEquals(4, count);

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
					(final Object o1, final Object o2) -> o2.toString().compareTo(o1.toString()),
					false);
			}
		};

		count = 0;
		while (sortedProperties.getProperty(count) != null)
		{
			assertTrue(count < 4, "the index holds more than the four keys of the defaults");
			final String value = sortedProperties.getProperty(count);
			if (count == 0)
			{
				assertEquals("4", value);
			}
			if (count == 1)
			{
				assertEquals("3", value);
			}
			if (count == 2)
			{
				assertEquals("2", value);
			}
			if (count == 3)
			{
				assertEquals("1", value);
			}
			count++;
		}
		assertEquals(4, count);
	}

	/**
	 * Test the constructor {@link IndexSortedProperties#IndexSortedProperties(Properties)} sorts
	 * the keys of the given defaults into the index, whatever order the defaults iterate them in
	 */
	@Test
	public void testConstructorSortsTheKeysOfTheDefaults()
	{
		final SortedProperties defaults = SortedProperties.of(new Properties(),
			(o1, o2) -> o2.toString().compareTo(o1.toString()));
		defaults.setProperty("A", "1");
		defaults.setProperty("B", "2");
		defaults.setProperty("C", "3");
		// the defaults iterate their keys in descending order
		assertEquals(ListFactory.newArrayList("C", "B", "A"), new ArrayList<>(defaults.keySet()));

		final IndexSortedProperties properties = new IndexSortedProperties(defaults);
		assertEquals("1", properties.getProperty(0));
		assertEquals("2", properties.getProperty(1));
		assertEquals("3", properties.getProperty(2));
		assertNull(properties.getProperty(3));
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
	 * Test the factory method {@link IndexSortedProperties#of(Properties, Comparator)} sorts the
	 * index with the given comparator, the keys of the defaults as well as a key put later
	 */
	@Test
	public void testOfWithComparatorSortsTheIndexWithIt()
	{
		final Properties defaults = new Properties();
		defaults.setProperty("A", "1");
		defaults.setProperty("C", "3");

		final IndexSortedProperties properties = IndexSortedProperties.of(defaults,
			(o1, o2) -> o2.toString().compareTo(o1.toString()));
		assertEquals("3", properties.get(0));
		assertEquals("1", properties.get(1));

		properties.setProperty("B", "2");
		assertEquals("3", properties.get(0));
		assertEquals("2", properties.get(1));
		assertEquals("1", properties.get(2));
	}

	/**
	 * Test the factory method {@link IndexSortedProperties#of(Properties, Comparator, boolean)}
	 * sorts the index with the given comparator, and that its comparator puts null after any other
	 * key when the flag nullIsGreaterThan is true
	 */
	@Test
	public void testOfWithComparatorAndNullIsGreaterThan()
	{
		final Properties defaults = new Properties();
		defaults.setProperty("A", "1");
		defaults.setProperty("C", "3");

		final IndexSortedProperties properties = IndexSortedProperties.of(defaults,
			(o1, o2) -> o2.toString().compareTo(o1.toString()), true);
		assertEquals("3", properties.get(0));
		assertEquals("1", properties.get(1));

		properties.setProperty("B", "2");
		assertEquals("3", properties.get(0));
		assertEquals("2", properties.get(1));
		assertEquals("1", properties.get(2));
		assertTrue(0 < properties.getComparator().compare(null, "A"));
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
	 * Test the method {@link IndexSortedProperties#put(Object, Object)} returns the previous value
	 * of the key, and null for a key that had none, as {@link java.util.Map#put(Object, Object)}
	 * specifies
	 */
	@Test
	public void testPutReturnsThePreviousValue()
	{
		final IndexSortedProperties properties = IndexSortedProperties.of();
		assertNull(properties.put("A", "1"));
		assertEquals("1", properties.put("A", "2"));
		assertEquals("2", properties.setProperty("A", "3"));
		assertEquals("3", properties.getProperty("A"));
		assertEquals(1, properties.size());
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
	 * Test the method {@link IndexSortedProperties#remove(Object, Object)} removes the key from the
	 * index together with its entry, and keeps both when the value does not match
	 */
	@Test
	public void testRemoveObjectObjectUpdatesTheIndex()
	{
		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.setProperty("A", "1");
		properties.setProperty("B", "2");
		properties.setProperty("C", "3");

		assertFalse(properties.remove("B", "3"));
		assertEquals("2", properties.getProperty("B"));
		assertEquals(1, properties.indexOf("2"));

		assertTrue(properties.remove("B", "2"));
		assertNull(properties.getProperty("B"));
		assertEquals("1", properties.get(0));
		assertEquals("3", properties.get(1));
		assertNull(properties.get(2));
		assertEquals(1, properties.indexOf("3"));
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
