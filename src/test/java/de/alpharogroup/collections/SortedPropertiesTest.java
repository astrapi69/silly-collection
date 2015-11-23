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
package de.alpharogroup.collections;

import java.util.Comparator;
import java.util.Enumeration;
import java.util.Set;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * The test class for SortedProperties.
 */
public class SortedPropertiesTest
{

	/**
	 * Test for method keys of class SortedProperties.
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
				return new Comparator<Object>()
				{
					@Override
					public int compare(final Object o1, final Object o2)
					{
						return ((o1.toString().compareTo(o2.toString())) * (-1));
					}
				};
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
	 * Test for method keySet of class SortedProperties.
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
				return new Comparator<Object>()
				{
					@Override
					public int compare(final Object o1, final Object o2)
					{
						return ((o1.toString().compareTo(o2.toString())) * (-1));
					}
				};
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
	 * Test for method propertyNames of class SortedProperties.
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
				return new Comparator<Object>()
				{
					@Override
					public int compare(final Object o1, final Object o2)
					{
						return ((o1.toString().compareTo(o2.toString())) * (-1));
					}
				};
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
