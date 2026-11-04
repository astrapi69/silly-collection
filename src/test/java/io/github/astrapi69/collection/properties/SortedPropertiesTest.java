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
	 * Test the method {@link Set#remove(Object)} of {@link SortedProperties#keySet()}: the set is
	 * backed by the properties, as {@link java.util.Map#keySet()} specifies, so removing a key from
	 * it removes its entry, and a key that the properties do not hold is not removed
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
		assertFalse(properties.containsKey("A"));
		assertEquals(1, properties.size());
		assertEquals(1, keySet.size());

		assertFalse(keySet.remove("C"));
		assertEquals(1, properties.size());
	}

	/**
	 * Test the method {@link Iterator#remove()} of the iterator of
	 * {@link SortedProperties#keySet()}: it removes the entry of the key that next() returned last,
	 * and throws an {@link IllegalStateException} if next() was not called since the last remove(),
	 * as {@link Iterator#remove()} specifies
	 */
	@Test
	public void testKeySetIteratorRemove()
	{
		final SortedProperties properties = new SortedProperties();
		properties.setProperty("B", "2");
		properties.setProperty("A", "1");

		final Iterator<Object> iterator = properties.keySet().iterator();
		assertThrows(IllegalStateException.class, iterator::remove);
		assertEquals("A", iterator.next());
		iterator.remove();
		assertFalse(properties.containsKey("A"));
		assertEquals(1, properties.size());
		assertThrows(IllegalStateException.class, iterator::remove);

		assertEquals("B", iterator.next());
		assertFalse(iterator.hasNext());
		assertEquals("2", properties.getProperty("B"));
	}

	/**
	 * Test the method {@link SortedProperties#keySet()} after a change of the properties: the set
	 * is backed by them, so it shows the change, sorted
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
		assertEquals(ListFactory.newArrayList("B", "C"), new ArrayList<>(keySet));
		assertFalse(keySet.contains("A"));
		assertTrue(keySet.contains("C"));
		assertEquals(2, keySet.size());
	}

	/**
	 * Test the methods {@link Set#add(Object)} and {@link Set#addAll(java.util.Collection)} of
	 * {@link SortedProperties#keySet()}: the set does not support them, as
	 * {@link java.util.Map#keySet()} specifies, and the properties stay as they are
	 */
	@Test
	public void testKeySetAdd()
	{
		final SortedProperties properties = new SortedProperties();
		properties.setProperty("A", "1");

		final Set<Object> keySet = properties.keySet();
		assertThrows(UnsupportedOperationException.class, () -> keySet.add("B"));
		assertThrows(UnsupportedOperationException.class,
			() -> keySet.addAll(ListFactory.newArrayList("B")));
		assertFalse(properties.containsKey("B"));
		assertEquals(1, properties.size());
	}

	/**
	 * Test the methods {@link Set#removeAll(java.util.Collection)},
	 * {@link Set#retainAll(java.util.Collection)} and {@link Set#clear()} of
	 * {@link SortedProperties#keySet()}: each removes the entries of the keys it removes from the
	 * set
	 */
	@Test
	public void testKeySetRemoveAllRetainAllAndClear()
	{
		final SortedProperties properties = new SortedProperties();
		properties.setProperty("A", "1");
		properties.setProperty("B", "2");
		properties.setProperty("C", "3");
		properties.setProperty("D", "4");

		final Set<Object> keySet = properties.keySet();
		assertTrue(keySet.removeAll(ListFactory.newArrayList("A", "E")));
		assertEquals(ListFactory.newArrayList("B", "C", "D"), Collections.list(properties.keys()));

		assertTrue(keySet.retainAll(ListFactory.newArrayList("C", "D")));
		assertEquals(ListFactory.newArrayList("C", "D"), Collections.list(properties.keys()));

		keySet.clear();
		assertTrue(properties.isEmpty());
		assertTrue(keySet.isEmpty());
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
	 * Test for factory method {@link SortedProperties#of(Properties, Comparator)}: the given
	 * properties are the defaults of the new object, which {@link Properties#getProperty(String)}
	 * finds and {@link SortedProperties#propertyNames()} lists, sorted with the given comparator
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
		assertEquals("4", sortedProperties.getProperty("D"));
		final Enumeration<?> propertyNames = sortedProperties.propertyNames();
		int count = 1;
		while (propertyNames.hasMoreElements())
		{
			final Object key = propertyNames.nextElement();
			if (count == 1)
			{
				assertEquals("D", key);
			}
			if (count == 2)
			{
				assertEquals("C", key);
			}
			if (count == 3)
			{
				assertEquals("B", key);
			}
			if (count == 4)
			{
				assertEquals("A", key);
			}
			count++;
		}
		// count started at 1: the loop ran once for each of the four keys of the defaults
		assertEquals(5, count);
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
	 * also a key of the properties: it lists the keys of the properties and of the defaults, each
	 * once, as {@link Properties#propertyNames()} does, and also those of the defaults of the
	 * defaults, sorted with the comparator
	 */
	@Test
	public void testPropertyNamesWithDefaults()
	{
		final Properties defaults = new Properties();
		defaults.setProperty("A", "1");
		defaults.setProperty("B", "2");
		assertEquals(ListFactory.newArrayList("A", "B"),
			Collections.list(new SortedProperties(defaults).propertyNames()));

		final SortedProperties properties = new SortedProperties(defaults);
		properties.setProperty("C", "3");
		properties.setProperty("B", "20");
		assertEquals(ListFactory.newArrayList("A", "B", "C"),
			Collections.list(properties.propertyNames()));

		final SortedProperties reversed = SortedProperties.of(defaults,
			(o1, o2) -> o2.toString().compareTo(o1.toString()));
		reversed.setProperty("C", "3");
		reversed.setProperty("B", "20");
		assertEquals(ListFactory.newArrayList("C", "B", "A"),
			Collections.list(reversed.propertyNames()));

		final SortedProperties nested = new SortedProperties(properties);
		nested.setProperty("D", "4");
		assertEquals(ListFactory.newArrayList("A", "B", "C", "D"),
			Collections.list(nested.propertyNames()));
	}

	/**
	 * Test the method {@link SortedProperties#propertyNames()} with keys that are no strings: it
	 * throws a {@link ClassCastException}, as {@link Properties#propertyNames()} specifies
	 */
	@Test
	public void testPropertyNamesWithKeysThatAreNoStrings()
	{
		final SortedProperties properties = new SortedProperties();
		properties.put(10, "ten");
		properties.put(9, "nine");
		properties.put(100, "hundred");
		assertThrows(ClassCastException.class, properties::propertyNames);
	}

	/**
	 * Test the serialization of a {@link SortedProperties}: its comparator, a lambda, is not part
	 * of its serialized form, so it serializes also after it created its comparator, and the copy
	 * creates one of its own with {@link SortedProperties#newComparator()} when it needs one. The
	 * copy has the entries and the defaults of the original and sorts the keys by their string form
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 * @throws ClassNotFoundException
	 *             is thrown when a class of the serialized object is not found
	 */
	@Test
	public void testSerialization() throws IOException, ClassNotFoundException
	{
		final Properties defaults = new Properties();
		defaults.setProperty("C", "3");
		final SortedProperties properties = new SortedProperties(defaults);
		properties.setProperty("B", "2");
		properties.setProperty("A", "1");

		// keys() creates the comparator
		assertEquals(ListFactory.newArrayList("A", "B"), Collections.list(properties.keys()));
		final SortedProperties copy = (SortedProperties)SerializedObjectExtensions
			.toObject(SerializedObjectExtensions.toByteArray(properties));
		assertEquals(properties, copy);
		assertEquals("3", copy.getProperty("C"));
		assertEquals(ListFactory.newArrayList("A", "B"), Collections.list(copy.keys()));
		assertEquals(ListFactory.newArrayList("A", "B", "C"),
			Collections.list(copy.propertyNames()));

		// by their string form "10" < "9" < "A" < "B"; integers and strings have no common order
		copy.put(10, "ten");
		copy.put(9, "nine");
		assertEquals(ListFactory.<Object> newArrayList(10, 9, "A", "B"),
			new ArrayList<>(copy.keySet()));
	}

}
