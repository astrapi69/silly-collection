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
import java.io.NotSerializableException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.comparator.NullCheckComparator;
import io.github.astrapi69.io.SerializedObjectExtensions;

/**
 * The unit test class for the class {@link SortedProperties}.
 *
 * @author Asterios Raptis
 */
public class SortedPropertiesTest
{

	/**
	 * Test for constructor of {@link SortedProperties}: the given properties are its defaults,
	 * which {@link Properties#getProperty(String)} finds, while {@link SortedProperties#keys()}
	 * lists the keys of its own entries, sorted, and not the keys of the defaults
	 */
	@Test
	public void testConstructor()
	{
		final Properties defaults = new Properties();
		defaults.put("E", "5");

		final Properties unsortedProperties = new Properties();
		unsortedProperties.put("B", "2");
		unsortedProperties.put("C", "3");
		unsortedProperties.put("A", "1");
		unsortedProperties.put("D", "4");

		Properties sortedProperties = new SortedProperties(defaults);
		sortedProperties.putAll(unsortedProperties);
		assertEquals("5", sortedProperties.getProperty("E"));

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
		// count started at 1: the loop ran once for each of the four keys of its own entries, and
		// not for the key E of the defaults
		assertEquals(5, count);

		// Add a reverse comparator.
		sortedProperties = new SortedProperties(defaults)
		{
			/**
			 * The serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected Comparator<Object> newComparator()
			{

				return NullCheckComparator.of(
					(final Object o1,
						final Object o2) -> ((o1.toString().compareTo(o2.toString())) * (-1)),
					false);
			}
		};
		sortedProperties.putAll(unsortedProperties);
		assertEquals("5", sortedProperties.getProperty("E"));

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
		assertEquals(5, count);
	}

	/**
	 * Test for method {@link SortedProperties#keys()}
	 */
	@Test
	public void testKeys()
	{
		SortedProperties sortedProperties = new SortedProperties();
		sortedProperties.put("B", "2");
		sortedProperties.put("C", "3");
		sortedProperties.put("A", "1");
		sortedProperties.put("D", "4");
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
		sortedProperties = new SortedProperties()
		{
			/**
			 * The serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected Comparator<Object> newComparator()
			{

				return NullCheckComparator.of(
					(final Object o1,
						final Object o2) -> ((o1.toString().compareTo(o2.toString())) * (-1)),
					false);
			}
		};
		sortedProperties.put("B", "2");
		sortedProperties.put("C", "3");
		sortedProperties.put("A", "1");
		sortedProperties.put("D", "4");

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
	 * Test for method {@link SortedProperties#keySet()}
	 */
	@Test
	public void testKeySet()
	{
		SortedProperties sortedProperties = new SortedProperties();
		sortedProperties.put("B", "2");
		sortedProperties.put("C", "3");
		sortedProperties.put("A", "1");
		sortedProperties.put("D", "4");
		Set<Object> set = sortedProperties.keySet();
		int count = 1;
		for (final Object key : set)
		{
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
		sortedProperties = new SortedProperties()
		{
			/**
			 * The serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected Comparator<Object> newComparator()
			{

				return NullCheckComparator.of(
					(final Object o1,
						final Object o2) -> ((o1.toString().compareTo(o2.toString())) * (-1)),
					false);
			}
		};
		sortedProperties.put("B", "2");
		sortedProperties.put("C", "3");
		sortedProperties.put("A", "1");
		sortedProperties.put("D", "4");

		set = sortedProperties.keySet();
		count = 1;
		for (final Object key : set)
		{
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
	 * Test the methods {@link SortedProperties#keys()} and {@link SortedProperties#keySet()} with
	 * keys that are no strings: the default comparator sorts them by their string form, not by
	 * their natural order
	 */
	@Test
	public void testKeysAreSortedByTheirStringForm()
	{
		final SortedProperties sortedProperties = new SortedProperties();
		sortedProperties.put(10, "ten");
		sortedProperties.put(9, "nine");
		sortedProperties.put(100, "hundred");

		// by their string form "10" < "100" < "9", by their natural order 9 < 10 < 100
		final List<Integer> expected = ListFactory.newArrayList(10, 100, 9);
		assertEquals(expected, Collections.list(sortedProperties.keys()));
		assertEquals(expected, new ArrayList<>(sortedProperties.keySet()));
	}

	/**
	 * Test the method {@link Set#remove(Object)} of {@link SortedProperties#keySet()}. #46: today
	 * the set is a copy, so the key goes from the copy and its entry stays in the properties; the
	 * fix of #46 changes the result
	 */
	@Test
	public void testKeySetRemove()
	{
		final SortedProperties properties = new SortedProperties();
		properties.setProperty("B", "2");
		properties.setProperty("A", "1");

		final Set<Object> keySet = properties.keySet();
		assertTrue(keySet.remove("A"));
		assertFalse(keySet.contains("A"));
		assertTrue(properties.containsKey("A"));
		assertEquals(2, properties.size());
	}

	/**
	 * Test the method {@link Iterator#remove()} of the iterator of
	 * {@link SortedProperties#keySet()}. #46: today the set is a copy, so the key goes from the
	 * copy and its entry stays in the properties; the fix of #46 changes the result
	 */
	@Test
	public void testKeySetIteratorRemove()
	{
		final SortedProperties properties = new SortedProperties();
		properties.setProperty("B", "2");
		properties.setProperty("A", "1");

		final Iterator<Object> iterator = properties.keySet().iterator();
		assertEquals("A", iterator.next());
		iterator.remove();
		assertTrue(properties.containsKey("A"));
		assertEquals(2, properties.size());
	}

	/**
	 * Test the method {@link SortedProperties#keySet()} after a change of the properties. #46:
	 * today the set is a copy, which keeps the keys of the moment it was made; the fix of #46
	 * changes the result
	 */
	@Test
	public void testKeySetAfterAChangeOfTheProperties()
	{
		final SortedProperties properties = new SortedProperties();
		properties.setProperty("B", "2");
		properties.setProperty("A", "1");

		final Set<Object> keySet = properties.keySet();
		properties.setProperty("C", "3");
		properties.remove("A");
		assertEquals(ListFactory.newArrayList("A", "B"), new ArrayList<>(keySet));
		assertTrue(keySet.contains("A"));
		assertFalse(keySet.contains("C"));
	}

	/**
	 * Test the method {@link Set#add(Object)} of {@link SortedProperties#keySet()}. #46: today the
	 * set is a copy that takes the key and leaves the properties as they are; the fix of #46
	 * changes the result
	 */
	@Test
	public void testKeySetAdd()
	{
		final SortedProperties properties = new SortedProperties();
		properties.setProperty("A", "1");

		final Set<Object> keySet = properties.keySet();
		assertTrue(keySet.add("B"));
		assertTrue(keySet.contains("B"));
		assertFalse(properties.containsKey("B"));
	}

	/**
	 * Test for factory method {@link SortedProperties#of()}
	 */
	@Test
	public void testOf()
	{

		final Properties sortedProperties = SortedProperties.of();
		assertNotNull(sortedProperties);

	}

	/**
	 * Test for factory method {@link SortedProperties#of(Properties)}
	 */
	@Test
	public void testOfProperties()
	{
		final Properties unsortedProperties = new Properties();
		unsortedProperties.put("B", "2");
		unsortedProperties.put("C", "3");
		unsortedProperties.put("A", "1");
		unsortedProperties.put("D", "4");
		unsortedProperties.put("E", "5");

		final Properties properties = SortedProperties.of(unsortedProperties);
		assertNotNull(properties);

	}

	/**
	 * Test for factory method {@link SortedProperties#of(Properties, Comparator)}. #46: today
	 * {@link SortedProperties#propertyNames()} leaves out the keys of the defaults, so the loop
	 * over them never runs; the fix of #46 changes the result
	 */
	@Test
	public void testOfPropertiesComparatorOfObject()
	{
		final Properties unsortedProperties = new Properties();
		unsortedProperties.put("B", "2");
		unsortedProperties.put("C", "3");
		unsortedProperties.put("A", "1");
		unsortedProperties.put("D", "4");

		final Properties sortedProperties = SortedProperties.of(unsortedProperties,
			(final Object o1,
				final Object o2) -> ((o1.toString().compareTo(o2.toString())) * (-1)));
		assertNotNull(sortedProperties);
		Enumeration<?> propertyNames = sortedProperties.propertyNames();
		int count = 1;
		propertyNames = sortedProperties.propertyNames();
		count = 1;
		while (propertyNames.hasMoreElements())
		{
			final Object key = propertyNames.nextElement();
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
		// count started at 1: the loop did not run
		assertEquals(1, count);
	}

	/**
	 * Test the factory method {@link SortedProperties#of(Properties, Comparator)} sorts the keys
	 * with the given comparator in {@link SortedProperties#keys()},
	 * {@link SortedProperties#keySet()} and {@link SortedProperties#propertyNames()}
	 */
	@Test
	public void testOfPropertiesComparatorSortsTheKeysWithIt()
	{
		final SortedProperties sortedProperties = SortedProperties.of(new Properties(),
			(o1, o2) -> o2.toString().compareTo(o1.toString()));
		sortedProperties.setProperty("B", "2");
		sortedProperties.setProperty("C", "3");
		sortedProperties.setProperty("A", "1");

		final List<String> expected = ListFactory.newArrayList("C", "B", "A");
		assertEquals(expected, Collections.list(sortedProperties.keys()));
		assertEquals(expected, new ArrayList<>(sortedProperties.keySet()));
		assertEquals(expected, Collections.list(sortedProperties.propertyNames()));
	}

	/**
	 * Test for method {@link SortedProperties#propertyNames()}
	 */
	@Test
	public void testPropertyNames()
	{
		SortedProperties sortedProperties = new SortedProperties();
		sortedProperties.put("B", "2");
		sortedProperties.put("C", "3");
		sortedProperties.put("A", "1");
		sortedProperties.put("D", "4");
		Enumeration<?> propertyNames = sortedProperties.propertyNames();
		int count = 1;
		while (propertyNames.hasMoreElements())
		{
			final Object key = propertyNames.nextElement();
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
		sortedProperties = new SortedProperties()
		{
			/**
			 * The serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected Comparator<Object> newComparator()
			{

				return NullCheckComparator.of(
					(final Object o1,
						final Object o2) -> ((o1.toString().compareTo(o2.toString())) * (-1)),
					false);
			}
		};
		sortedProperties.put("B", "2");
		sortedProperties.put("C", "3");
		sortedProperties.put("A", "1");
		sortedProperties.put("D", "4");

		propertyNames = sortedProperties.propertyNames();
		count = 1;
		while (propertyNames.hasMoreElements())
		{
			final Object key = propertyNames.nextElement();
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
	 * Test the method {@link SortedProperties#propertyNames()} with defaults, one of their keys
	 * also a key of the properties. #46: today it lists only the keys of the properties, where
	 * {@link Properties#propertyNames()} includes those of the defaults; the fix of #46 changes the
	 * result
	 */
	@Test
	public void testPropertyNamesWithDefaults()
	{
		final Properties defaults = new Properties();
		defaults.setProperty("A", "1");
		defaults.setProperty("B", "2");
		assertEquals(Collections.emptyList(),
			Collections.list(new SortedProperties(defaults).propertyNames()));

		final SortedProperties properties = new SortedProperties(defaults);
		properties.setProperty("C", "3");
		properties.setProperty("B", "20");
		assertEquals(ListFactory.newArrayList("B", "C"),
			Collections.list(properties.propertyNames()));

		final SortedProperties reversed = SortedProperties.of(defaults,
			(o1, o2) -> o2.toString().compareTo(o1.toString()));
		reversed.setProperty("C", "3");
		reversed.setProperty("B", "20");
		assertEquals(ListFactory.newArrayList("C", "B"),
			Collections.list(reversed.propertyNames()));
	}

	/**
	 * Test the method {@link SortedProperties#propertyNames()} with keys that are no strings. #46:
	 * today it lists them, sorted by their string form, where {@link Properties#propertyNames()}
	 * throws a {@link ClassCastException}; the fix of #46 changes the result
	 */
	@Test
	public void testPropertyNamesWithKeysThatAreNoStrings()
	{
		final SortedProperties properties = new SortedProperties();
		properties.put(10, "ten");
		properties.put(9, "nine");
		properties.put(100, "hundred");
		assertEquals(ListFactory.newArrayList(10, 100, 9),
			Collections.list(properties.propertyNames()));
	}

	/**
	 * Test the serialization of a {@link SortedProperties}. #46: today it serializes only until it
	 * creates its comparator, a lambda, which is part of its serialized form; the fix of #46
	 * changes the result
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testSerialization() throws IOException
	{
		final SortedProperties properties = new SortedProperties();
		properties.setProperty("B", "2");
		properties.setProperty("A", "1");
		assertNotNull(SerializedObjectExtensions.toByteArray(properties));

		// keys() creates the comparator
		assertEquals(ListFactory.newArrayList("A", "B"), Collections.list(properties.keys()));
		assertThrows(NotSerializableException.class,
			() -> SerializedObjectExtensions.toByteArray(properties));
	}

}
