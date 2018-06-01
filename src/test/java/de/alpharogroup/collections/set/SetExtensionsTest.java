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
package de.alpharogroup.collections.set;

import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import de.alpharogroup.collections.list.ListFactory;
import de.alpharogroup.comparators.SortOrder;
import de.alpharogroup.comparators.StringLengthComparator;

/**
 * The unit test class for the class {@link SetExtensionsTest}.
 */
public class SetExtensionsTest
{

	/**
	 * Test for method {@link SetFactory#newHashSet(java.util.Collection, Object...)}
	 */
	@Test
	public void testNewHashSetCollectionObjects()
	{
		Set<String> set = SetFactory.newHashSet();
		assertTrue(set.size() == 0);
		set.add("foo");
		assertTrue(set.size() == 1);
		set = SetFactory.newHashSet(ListFactory.newArrayList("foo", "fasel"), "foo", "bar",
			"foo");
		assertTrue(set.size() == 3);
		set = SetFactory.newHashSet(ListFactory.newArrayList(), "foo", "bar", "foo");
		assertTrue(set.size() == 2);

	}

	/**
	 * Test for method {@link SetFactory#newHashSet(Object...)}
	 */
	@Test
	public void testNewHashSetObjects()
	{
		Set<String> set = SetFactory.newHashSet();
		assertTrue(set.size() == 0);
		set = SetFactory.newHashSet("foo", "bar", "foo");
		assertTrue(set.size() == 2);
	}

	/**
	 * Test for method {@link SetFactory#newTreeSet(Collection, Object...)}
	 */
	@Test
	public void testNewTreeSetCollectionObjects()
	{
		SortedSet<String> set = SetFactory.newTreeSet();
		assertTrue(set.size() == 0);
		set.add("foo");
		assertTrue(set.size() == 1);
		set = SetFactory.newTreeSet(ListFactory.newArrayList("foo", "fasel"), "foo", "bar",
			"foo");
		assertTrue(set.size() == 3);
		set = SetFactory.newTreeSet(ListFactory.newArrayList(), "foo", "bar", "foo");
		assertTrue(set.size() == 2);

	}


	/**
	 * Test method for {@link SetFactory#newTreeSet(Collection, Comparator, T[])}.
	 */
	@Test
	public void testNewTreeSetCollectionOfTComparatorOfTTArray()
	{
		SortedSet<String> set = SetFactory.newTreeSet();
		assertTrue(set.size() == 0);
		set.add("foo");
		assertTrue(set.size() == 1);
		final Comparator<String> comparator = StringLengthComparator.of(SortOrder.ASCENDING);
		final List<String> list = ListFactory.newArrayList("foo", "fasel");
		set = SetFactory.newTreeSet(list, comparator, "food", "barista", "fao");
		assertTrue(set.size() == 5);
		set = SetFactory.newTreeSet(ListFactory.<String> newArrayList(), comparator, "foo",
			"bar", "foo");
		assertTrue(set.size() == 2);
	}

	/**
	 * Test for method {@link SetExtensions#toSet(Collection)}
	 */
	@Test
	public void testToSet()
	{
		final Collection<String> nameList = new ArrayList<>();

		nameList.add("Anton");
		nameList.add("Alex");
		nameList.add("Berta");
		nameList.add("Brad");
		nameList.add("Caesar");
		nameList.add("Leonardo");

		final Set<String> set = SetExtensions.toSet(nameList);
		assertNotNull(set);
	}

	/**
	 * Test for method {@link SetExtensions#toSortedSet(Collection)}
	 */
	@Test
	public void testToSortedSet()
	{
		final Collection<String> nameList = new ArrayList<>();

		nameList.add("Anton");
		nameList.add("Alex");
		nameList.add("Berta");
		nameList.add("Brad");
		nameList.add("Caesar");
		nameList.add("Leonardo");

		final Set<String> set = SetExtensions.toSortedSet(nameList);
		assertNotNull(set);
	}

	/**
	 * Test method for {@link SetExtensions} with {@link BeanTester}
	 */
	@Test(expectedExceptions = { BeanTestException.class, InvocationTargetException.class,
			UnsupportedOperationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(SetExtensions.class);
	}

}
