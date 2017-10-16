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
package de.alpharogroup.collections.properties;

import java.util.Comparator;
import java.util.Enumeration;
import java.util.Properties;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import de.alpharogroup.comparators.NullCheckComparator;

/**
 * Test class for the class {@link IndexSortedProperties}.
 *
 * @author Asterios Raptis
 */
public class IndexSortedPropertiesTest
{

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
		AssertJUnit.assertEquals(expected, actual);
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
		sortedProperties = new IndexSortedProperties(unsortedProperties)
		{
			/**
			 * The serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected Comparator<Object> newComparator()
			{

				return NullCheckComparator.<Object> of(new Comparator<Object>()
				{
					@Override
					public int compare(final Object o1, final Object o2)
					{
						return ((o1.toString().compareTo(o2.toString())) * (-1));
					}
				}, false);
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
	 * Test for method {@link IndexSortedProperties#indexOf(Object)}
	 */
	@Test
	public void testSetPropertyStringString()
	{

		final Properties unsortedProperties = new Properties();
		unsortedProperties.put("B", "2");
		unsortedProperties.put("C", "3");
		unsortedProperties.put("A", "1");
		unsortedProperties.put("D", "4");
		unsortedProperties.put("E", "5");

		final IndexSortedProperties properties = new IndexSortedProperties(unsortedProperties);

		int expected;
		int actual;
		expected = 4;
		actual = properties.indexOf("5");
		AssertJUnit.assertEquals(expected, actual);
		expected = 3;
		actual = properties.indexOf("4");
		AssertJUnit.assertEquals(expected, actual);
		expected = 2;
		actual = properties.indexOf("3");
		AssertJUnit.assertEquals(expected, actual);
		expected = 1;
		actual = properties.indexOf("2");
		AssertJUnit.assertEquals(expected, actual);
		expected = 0;
		actual = properties.indexOf("1");
		AssertJUnit.assertEquals(expected, actual);
	}

}
