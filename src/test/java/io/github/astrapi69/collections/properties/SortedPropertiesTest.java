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
package io.github.astrapi69.collections.properties;

import static org.testng.AssertJUnit.assertNotNull;

import java.util.Comparator;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import de.alpharogroup.comparators.NullCheckComparator;

/**
 * The unit test class for the class {@link SortedProperties}.
 *
 * @author Asterios Raptis
 */
public class SortedPropertiesTest
{

	/**
	 * Test for constructor of {@link SortedProperties}.
	 */
	@Test
	public void testConstructor()
	{
		final Properties unsortedProperties = new Properties();
		unsortedProperties.put("B", "2");
		unsortedProperties.put("C", "3");
		unsortedProperties.put("A", "1");
		unsortedProperties.put("D", "4");

		Properties sortedProperties = new SortedProperties(unsortedProperties);

		Enumeration<?> keys = sortedProperties.keys();
		int count = 1;
		while (keys.hasMoreElements())
		{
			final Object key = keys.nextElement();
			if (count == 1)
			{
				AssertJUnit.assertEquals(key, "A");
			}
			if (count == 2)
			{
				AssertJUnit.assertEquals(key, "B");
			}
			if (count == 3)
			{
				AssertJUnit.assertEquals(key, "C");
			}
			if (count == 4)
			{
				AssertJUnit.assertEquals(key, "D");
			}
			count++;
		}

		// Add a reverse comparator.
		sortedProperties = new SortedProperties(unsortedProperties)
		{
			/**
			 * The serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected Comparator<Object> newComparator()
			{

				return NullCheckComparator.<Object> of(
					(final Object o1,
						final Object o2) -> ((o1.toString().compareTo(o2.toString())) * (-1)),
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
				AssertJUnit.assertEquals(key, "D");
			}
			if (count == 2)
			{
				AssertJUnit.assertEquals(key, "C");
			}
			if (count == 3)
			{
				AssertJUnit.assertEquals(key, "B");
			}
			if (count == 4)
			{
				AssertJUnit.assertEquals(key, "A");
			}
			count++;
		}
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
				AssertJUnit.assertEquals(key, "A");
			}
			if (count == 2)
			{
				AssertJUnit.assertEquals(key, "B");
			}
			if (count == 3)
			{
				AssertJUnit.assertEquals(key, "C");
			}
			if (count == 4)
			{
				AssertJUnit.assertEquals(key, "D");
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

				return NullCheckComparator.<Object> of(
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
				AssertJUnit.assertEquals(key, "D");
			}
			if (count == 2)
			{
				AssertJUnit.assertEquals(key, "C");
			}
			if (count == 3)
			{
				AssertJUnit.assertEquals(key, "B");
			}
			if (count == 4)
			{
				AssertJUnit.assertEquals(key, "A");
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
				AssertJUnit.assertEquals(key, "A");
			}
			if (count == 2)
			{
				AssertJUnit.assertEquals(key, "B");
			}
			if (count == 3)
			{
				AssertJUnit.assertEquals(key, "C");
			}
			if (count == 4)
			{
				AssertJUnit.assertEquals(key, "D");
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

				return NullCheckComparator.<Object> of(
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
				AssertJUnit.assertEquals(key, "D");
			}
			if (count == 2)
			{
				AssertJUnit.assertEquals(key, "C");
			}
			if (count == 3)
			{
				AssertJUnit.assertEquals(key, "B");
			}
			if (count == 4)
			{
				AssertJUnit.assertEquals(key, "A");
			}
			count++;
		}
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
	 * Test for factory method {@link SortedProperties#of(Properties, Comparator)}
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
				AssertJUnit.assertEquals(key, "D");
			}
			if (count == 2)
			{
				AssertJUnit.assertEquals(key, "C");
			}
			if (count == 3)
			{
				AssertJUnit.assertEquals(key, "B");
			}
			if (count == 4)
			{
				AssertJUnit.assertEquals(key, "A");
			}
			count++;
		}
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
				AssertJUnit.assertEquals(key, "A");
			}
			if (count == 2)
			{
				AssertJUnit.assertEquals(key, "B");
			}
			if (count == 3)
			{
				AssertJUnit.assertEquals(key, "C");
			}
			if (count == 4)
			{
				AssertJUnit.assertEquals(key, "D");
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

				return NullCheckComparator.<Object> of(
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
				AssertJUnit.assertEquals(key, "D");
			}
			if (count == 2)
			{
				AssertJUnit.assertEquals(key, "C");
			}
			if (count == 3)
			{
				AssertJUnit.assertEquals(key, "B");
			}
			if (count == 4)
			{
				AssertJUnit.assertEquals(key, "A");
			}
			count++;
		}
	}

}
