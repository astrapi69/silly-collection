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

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.comparator.NullCheckComparator;
import io.github.astrapi69.io.SerializedObjectExtensions;

/**
 * The unit test class for the class {@link IndexSortedProperties}.
 *
 * @author Asterios Raptis
 */
public class IndexSortedPropertiesTest
{

	/**
	 * Test for method {@link IndexSortedProperties#clear()}: it removes the entries, and their keys
	 * from the index
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

		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.putAll(unsortedProperties);
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
		assertNull(properties.get(0));
		assertEquals(-1, properties.indexOf("1"));
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
	 * Test for constructor of the class {@link IndexSortedProperties}: the given properties are its
	 * defaults, which {@link Properties#getProperty(String)} finds and the index does not hold, as
	 * {@link IndexSortedProperties#keySet()} does not; the index holds the keys of the entries put
	 * into the object, sorted with its comparator
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
		assertEquals("1", sortedProperties.getProperty("A"));
		assertNull(sortedProperties.getProperty(0));

		// the same entries put into the object: the index holds their keys A, B, C, D, and
		// getProperty(int) returns the value of the key at an index
		sortedProperties.putAll(unsortedProperties);
		int count = 0;
		while (sortedProperties.getProperty(count) != null)
		{
			assertTrue(count < 4, "the index holds more than the four keys of the entries");
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
		assertNull(sortedProperties.getProperty(0));

		sortedProperties.putAll(unsortedProperties);
		count = 0;
		while (sortedProperties.getProperty(count) != null)
		{
			assertTrue(count < 4, "the index holds more than the four keys of the entries");
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
	 * Test the constructor {@link IndexSortedProperties#IndexSortedProperties(Properties)} leaves
	 * the keys of the given defaults out of the index, and that
	 * {@link IndexSortedProperties#putAll(java.util.Map)} sorts the keys it puts into the index,
	 * whatever order the given map iterates them in
	 */
	@Test
	public void testConstructorLeavesTheKeysOfTheDefaultsOutOfTheIndex()
	{
		final SortedProperties defaults = SortedProperties.of(new Properties(),
			(o1, o2) -> o2.toString().compareTo(o1.toString()));
		defaults.setProperty("A", "1");
		defaults.setProperty("B", "2");
		defaults.setProperty("C", "3");
		// the defaults iterate their keys in descending order
		assertEquals(ListFactory.newArrayList("C", "B", "A"), new ArrayList<>(defaults.keySet()));

		final IndexSortedProperties properties = new IndexSortedProperties(defaults);
		assertEquals("1", properties.getProperty("A"));
		assertNull(properties.getProperty(0));

		properties.putAll(defaults);
		assertEquals("1", properties.getProperty(0));
		assertEquals("2", properties.getProperty(1));
		assertEquals("3", properties.getProperty(2));
		assertNull(properties.getProperty(3));
	}

	/**
	 * Test for method {@link IndexSortedProperties#of()} and
	 * {@link IndexSortedProperties#of(Properties)}: the given properties are the defaults, whose
	 * values the index does not find, and the index holds the entries put into the object
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
		assertEquals("5", properties.getProperty("E"));
		assertEquals(-1, properties.indexOf("5"));

		properties.putAll(unsortedProperties);
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
	 * Test for method {@link IndexSortedProperties#of(Properties, Comparator)} and
	 * {@link IndexSortedProperties#of(Properties, Comparator, boolean)}: the given properties are
	 * the defaults, whose values the index does not find, and the index holds the entries put into
	 * the object
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
		assertEquals(-1, properties.indexOf("5"));
		properties.putAll(unsortedProperties);
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
		assertEquals(-1, properties.indexOf("5"));
		properties.putAll(unsortedProperties);
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
	 * index with the given comparator, the keys put with putAll as well as a key put later; the
	 * keys of the defaults are not in the index
	 */
	@Test
	public void testOfWithComparatorSortsTheIndexWithIt()
	{
		final Properties defaults = new Properties();
		defaults.setProperty("A", "1");
		defaults.setProperty("C", "3");

		final IndexSortedProperties properties = IndexSortedProperties.of(defaults,
			(o1, o2) -> o2.toString().compareTo(o1.toString()));
		assertNull(properties.get(0));

		properties.putAll(defaults);
		assertEquals("3", properties.get(0));
		assertEquals("1", properties.get(1));

		properties.setProperty("B", "2");
		assertEquals("3", properties.get(0));
		assertEquals("2", properties.get(1));
		assertEquals("1", properties.get(2));
	}

	/**
	 * Test the factory method {@link IndexSortedProperties#of(Properties, Comparator, boolean)}
	 * sorts the index with the given comparator, the keys of the defaults left out, and that its
	 * comparator puts null after any other key when the flag nullIsGreaterThan is true
	 */
	@Test
	public void testOfWithComparatorAndNullIsGreaterThan()
	{
		final Properties defaults = new Properties();
		defaults.setProperty("A", "1");
		defaults.setProperty("C", "3");

		final IndexSortedProperties properties = IndexSortedProperties.of(defaults,
			(o1, o2) -> o2.toString().compareTo(o1.toString()), true);
		assertNull(properties.get(0));

		properties.putAll(defaults);
		assertEquals("3", properties.get(0));
		assertEquals("1", properties.get(1));

		properties.setProperty("B", "2");
		assertEquals("3", properties.get(0));
		assertEquals("2", properties.get(1));
		assertEquals("1", properties.get(2));
		assertTrue(0 < properties.getComparator().compare(null, "A"));
	}

	/**
	 * Test for method {@link IndexSortedProperties#get(int)}: the value of the key at the index,
	 * and null at the index equal to the size
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

		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.putAll(unsortedProperties);
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
	 * Test for method {@link IndexSortedProperties#getProperty(int)}: the value of the key at the
	 * index, and null at the index equal to the size
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
	 * Test for method {@link IndexSortedProperties#indexOf(Object)}: the index of the key whose
	 * value it is, and -1 for a value that no key of the index has, also a value of the defaults,
	 * whose keys are not in the index
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
		expected = 0;
		actual = properties.indexOf("5");
		assertEquals(expected, actual);
		expected = -1;
		actual = properties.indexOf("6");
		assertEquals(expected, actual);
		expected = -1;
		actual = properties.indexOf("1");
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

	/**
	 * Test the method {@link IndexSortedProperties#keySet()}: removing a key through the set or its
	 * iterator removes it from the index, too, as the set removes it with
	 * {@link IndexSortedProperties#remove(Object)}
	 */
	@Test
	public void testKeySetRemoveUpdatesTheIndex()
	{
		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.setProperty("A", "1");
		properties.setProperty("B", "2");
		properties.setProperty("C", "3");

		assertTrue(properties.keySet().remove("B"));
		assertEquals("1", properties.get(0));
		assertEquals("3", properties.get(1));
		assertNull(properties.get(2));

		final Iterator<Object> iterator = properties.keySet().iterator();
		assertEquals("A", iterator.next());
		iterator.remove();
		assertEquals("3", properties.get(0));
		assertNull(properties.get(1));
		assertEquals(0, properties.indexOf("3"));
	}

	/**
	 * Test the serialization of an {@link IndexSortedProperties}: the comparator, a lambda, is not
	 * part of its serialized form, so it serializes; the copy has the entries and the index of the
	 * original, and sorts a key put into it later into its own index with a comparator of its own
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 * @throws ClassNotFoundException
	 *             is thrown when a class of the serialized object is not found
	 */
	@Test
	public void testSerialization() throws IOException, ClassNotFoundException
	{
		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.setProperty("C", "3");
		properties.setProperty("B", "2");

		final IndexSortedProperties copy = (IndexSortedProperties)SerializedObjectExtensions
			.toObject(SerializedObjectExtensions.toByteArray(properties));
		assertEquals(properties, copy);
		assertEquals("2", copy.get(0));
		assertEquals("3", copy.get(1));
		assertNull(copy.get(2));

		copy.setProperty("A", "1");
		assertEquals("1", copy.get(0));
		assertEquals("2", copy.get(1));
		assertEquals("3", copy.get(2));
		assertEquals(0, copy.indexOf("1"));
		assertNull(properties.get(2));
	}

	/**
	 * Test the method {@link IndexSortedProperties#put(Object, Object)} with a key that is there
	 * already: the index holds the key once, as the properties do
	 */
	@Test
	public void testPutOfAKeyTwice()
	{
		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.put("A", "1");
		properties.put("A", "2");
		assertEquals(1, properties.size());
		assertEquals("2", properties.get(0));
		assertNull(properties.get(1));
		assertEquals(0, properties.indexOf("2"));
	}

	/**
	 * Test the method {@link IndexSortedProperties#remove(Object)} with a key that was put twice,
	 * and a put of another key afterwards: the key leaves the index, and the other key is the first
	 * one in it. A key that the properties do not hold leaves the index as it is
	 */
	@Test
	public void testRemoveOfAKeyPutTwice()
	{
		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.put("A", "1");
		properties.put("A", "2");
		properties.remove("A");
		properties.put("B", "3");
		assertEquals("3", properties.get(0));
		assertNull(properties.get(1));
		assertEquals(0, properties.indexOf("3"));

		assertNull(properties.remove("X"));
		assertEquals("3", properties.get(0));
		assertNull(properties.get(1));
	}

	/**
	 * Test the methods {@link IndexSortedProperties#put(Object, Object)} and
	 * {@link IndexSortedProperties#putAll(java.util.Map)} with defaults: after either, the index
	 * holds the keys of the entries of the object and not those of the defaults, which
	 * {@link Properties#getProperty(String)} still finds
	 */
	@Test
	public void testPutAndPutAllWithDefaults()
	{
		final Properties defaults = new Properties();
		defaults.setProperty("A", "1");
		defaults.setProperty("B", "2");

		final IndexSortedProperties put = new IndexSortedProperties(defaults);
		put.put("C", "3");
		assertEquals("3", put.getProperty(0));
		assertNull(put.getProperty(1));
		assertEquals("1", put.getProperty("A"));

		final Properties more = new Properties();
		more.setProperty("C", "3");
		final IndexSortedProperties putAll = new IndexSortedProperties(defaults);
		putAll.putAll(more);
		assertEquals("3", putAll.getProperty(0));
		assertNull(putAll.getProperty(1));
		assertEquals("1", putAll.getProperty("A"));
	}

	/**
	 * Test the methods {@link IndexSortedProperties#putIfAbsent(Object, Object)},
	 * {@link IndexSortedProperties#merge(Object, Object, java.util.function.BiFunction)} and
	 * {@link IndexSortedProperties#compute(Object, java.util.function.BiFunction)}: each returns
	 * what {@link java.util.Map} specifies, puts the key it adds into the index and takes the key
	 * it removes out of it
	 */
	@Test
	public void testPutIfAbsentMergeAndCompute()
	{
		final IndexSortedProperties properties = IndexSortedProperties.of();
		assertNull(properties.putIfAbsent("A", "1"));
		assertEquals("2", properties.merge("B", "2", (oldValue, value) -> value));
		assertEquals("3", properties.compute("C", (key, value) -> "3"));
		assertEquals(3, properties.size());
		assertEquals("1", properties.get(0));
		assertEquals("2", properties.get(1));
		assertEquals("3", properties.get(2));
		assertEquals(0, properties.indexOf("1"));

		assertEquals("1", properties.putIfAbsent("A", "10"));
		assertEquals("1", properties.get(0));
		assertNull(properties.get(3));

		// a remapping function that returns null removes the entry
		assertNull(properties.merge("B", "20", (oldValue, value) -> null));
		assertEquals("1", properties.get(0));
		assertEquals("3", properties.get(1));
		assertNull(properties.get(2));

		assertNull(properties.compute("A", (key, value) -> null));
		assertEquals("3", properties.get(0));
		assertNull(properties.get(1));
		assertEquals(1, properties.size());
	}

	/**
	 * Test the methods
	 * {@link IndexSortedProperties#computeIfAbsent(Object, java.util.function.Function)} and
	 * {@link IndexSortedProperties#computeIfPresent(Object, java.util.function.BiFunction)}: each
	 * returns what {@link java.util.Map} specifies, the key computeIfAbsent adds goes into the
	 * index, and the key computeIfPresent removes leaves it
	 */
	@Test
	public void testComputeIfAbsentAndComputeIfPresent()
	{
		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.setProperty("A", "1");
		assertEquals("2", properties.computeIfAbsent("B", key -> "2"));
		assertEquals("1", properties.get(0));
		assertEquals("2", properties.get(1));

		assertEquals("10", properties.computeIfPresent("A", (key, value) -> "10"));
		assertEquals("10", properties.get(0));

		assertNull(properties.computeIfPresent("A", (key, value) -> null));
		assertEquals(1, properties.size());
		assertEquals("2", properties.get(0));
		assertNull(properties.get(1));
		assertEquals(0, properties.indexOf("2"));
	}

	/**
	 * Test the methods {@link IndexSortedProperties#replace(Object, Object)},
	 * {@link IndexSortedProperties#replace(Object, Object, Object)} and
	 * {@link IndexSortedProperties#replaceAll(java.util.function.BiFunction)}: they change values,
	 * not keys, so the index holds the same keys, and finds the new values
	 */
	@Test
	public void testReplaceAndReplaceAll()
	{
		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.setProperty("B", "2");
		properties.setProperty("A", "1");
		assertEquals("1", properties.get(0));

		assertEquals("1", properties.replace("A", "10"));
		assertTrue(properties.replace("B", "2", "20"));
		assertNull(properties.replace("C", "3"));
		assertEquals("10", properties.get(0));
		assertEquals("20", properties.get(1));
		assertNull(properties.get(2));

		properties.replaceAll((key, value) -> value + "0");
		assertEquals("100", properties.get(0));
		assertEquals("200", properties.get(1));
		assertNull(properties.get(2));
		assertEquals(1, properties.indexOf("200"));
	}

	/**
	 * Test the views {@link IndexSortedProperties#entrySet()} and
	 * {@link IndexSortedProperties#values()}: they remove entries without a method of
	 * {@link IndexSortedProperties}, and the index follows them all the same
	 */
	@Test
	public void testRemoveThroughEntrySetAndValues()
	{
		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.setProperty("A", "1");
		properties.setProperty("B", "2");
		properties.setProperty("C", "3");
		assertEquals("1", properties.get(0));

		assertTrue(properties.entrySet().removeIf(entry -> "B".equals(entry.getKey())));
		assertEquals("1", properties.get(0));
		assertEquals("3", properties.get(1));
		assertNull(properties.get(2));

		assertTrue(properties.values().remove("1"));
		assertEquals("3", properties.get(0));
		assertNull(properties.get(1));
		assertEquals(0, properties.indexOf("3"));
	}

	/**
	 * Test the method {@link IndexSortedProperties#load(java.io.Reader)} with a key that is twice
	 * in the input: the index holds the key once, with the value that came last
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testLoadOfAKeyTwice() throws IOException
	{
		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.load(new StringReader("B=2\nA=1\nA=3\n"));
		assertEquals(2, properties.size());
		assertEquals("3", properties.get(0));
		assertEquals("2", properties.get(1));
		assertNull(properties.get(2));
	}

	/**
	 * Test the method {@link IndexSortedProperties#clone()}: the copy has an index of its own, so
	 * that a change of the copy leaves the index of the original as it is, and the other way round
	 */
	@Test
	public void testClone()
	{
		final IndexSortedProperties original = IndexSortedProperties.of();
		original.setProperty("A", "1");
		assertEquals("1", original.get(0));

		final IndexSortedProperties copy = (IndexSortedProperties)original.clone();
		copy.setProperty("B", "2");
		copy.remove("A");
		assertEquals("1", original.getProperty("A"));
		assertEquals("1", original.get(0));
		assertNull(original.get(1));
		assertEquals(0, original.indexOf("1"));
		assertEquals("2", copy.get(0));
		assertNull(copy.get(1));

		original.setProperty("C", "3");
		assertEquals("3", original.get(1));
		assertEquals("2", copy.get(0));
		assertNull(copy.get(1));
	}

	/**
	 * Test the methods {@link IndexSortedProperties#get(int)},
	 * {@link IndexSortedProperties#getProperty(int)} and {@link IndexSortedProperties#remove(int)}
	 * with a negative index: each returns null, as their Javadoc says for an index out of range,
	 * and removes nothing
	 */
	@Test
	public void testNegativeIndex()
	{
		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.setProperty("A", "1");
		assertNull(properties.get(-1));
		assertNull(properties.getProperty(-1));
		assertNull(properties.remove(-1));
		assertEquals(1, properties.size());
		assertEquals("1", properties.get(0));
	}

	/**
	 * Test the constructor with a subclass whose {@link IndexSortedProperties#newComparator()}
	 * returns a descending comparator from a field of its own: the constructor does not call
	 * newComparator(), the keys of the defaults are not in the index, and the first read of the
	 * index calls newComparator(), with the field set, and sorts with its comparator
	 */
	@Test
	public void testConstructorWithTheComparatorFromAFieldOfASubclass()
	{
		final Properties defaults = new Properties();
		defaults.setProperty("A", "1");
		defaults.setProperty("B", "2");
		defaults.setProperty("C", "3");

		// newComparator() records whether the field of the subclass was set when it was called
		final List<Boolean> calls = new ArrayList<>();
		final IndexSortedProperties properties = new IndexSortedProperties(defaults)
		{
			private static final long serialVersionUID = 1L;

			private final Comparator<Object> descending = (o1, o2) -> o2.toString()
				.compareTo(o1.toString());

			@Override
			protected Comparator<Object> newComparator()
			{
				calls.add(descending != null);
				return descending;
			}
		};
		assertTrue(calls.isEmpty());
		assertNull(properties.getProperty(0));
		assertEquals("1", properties.getProperty("A"));

		properties.putAll(defaults);
		assertEquals("3", properties.getProperty(0));
		assertEquals("2", properties.getProperty(1));
		assertEquals("1", properties.getProperty(2));
		assertEquals(ListFactory.newArrayList(true), calls);
	}

}
