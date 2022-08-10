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
package io.github.astrapi69.collection.set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.comparator.object.StringLengthComparator;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import io.github.astrapi69.comparator.SortOrder;

/**
 * The unit test class for the class {@link SetFactory}.
 */
public class SetFactoryTest
{

	/**
	 * Test for method {@link SetFactory#newHashSet(java.util.Collection, Object...)}
	 */
	@Test
	public void testNewHashSetCollectionObjects()
	{
		int actual;
		int expected;
		Set<String> set;

		set = SetFactory.newHashSet();
		actual = set.size();
		expected = 0;
		assertEquals(actual, expected);

		set.add("foo");
		actual = set.size();
		expected = 1;
		assertEquals(actual, expected);

		set = SetFactory.newHashSet(ListFactory.newArrayList("foo", "fasel"), "foo", "bar", "foo");
		actual = set.size();
		expected = 3;
		assertEquals(actual, expected);

		set = SetFactory.newHashSet(ListFactory.newArrayList(), "foo", "bar", "foo");
		actual = set.size();
		expected = 2;
		assertEquals(actual, expected);
	}

	/**
	 * Test for method {@link SetFactory#newHashSet(Object...)}
	 */
	@Test
	public void testNewHashSetObjects()
	{
		int actual;
		int expected;
		Set<String> set;

		set = SetFactory.newHashSet();
		actual = set.size();
		expected = 0;
		assertEquals(actual, expected);

		set = SetFactory.newHashSet("foo", "bar", "foo");
		actual = set.size();
		expected = 2;
		assertEquals(actual, expected);
	}

	/**
	 * Test for method {@link SetFactory#newInsertionOrderSet(java.util.Collection, Object...)}
	 */
	@Test
	public void testNewInsertionOrderSetCollectionObjects()
	{
		int actual;
		int expected;
		Set<String> set;

		set = SetFactory.newInsertionOrderSet();
		actual = set.size();
		expected = 0;
		assertEquals(actual, expected);

		set.add("foo");
		actual = set.size();
		expected = 1;
		assertEquals(actual, expected);

		set = SetFactory.newInsertionOrderSet(ListFactory.newArrayList("foo", "fasel"), "foo",
			"bar", "foo");
		actual = set.size();
		expected = 3;
		assertEquals(actual, expected);

		set = SetFactory.newInsertionOrderSet(ListFactory.newArrayList(), "foo", "bar", "foo");
		actual = set.size();
		expected = 2;
		assertEquals(actual, expected);
	}

	/**
	 * Test for method {@link SetFactory#newInsertionOrderSet(Object...)}
	 */
	@Test
	public void testNewInsertionOrderSetObjects()
	{
		int actual;
		int expected;
		Set<String> set;
		set = SetFactory.newInsertionOrderSet();
		actual = set.size();
		expected = 0;
		assertEquals(actual, expected);

		set = SetFactory.newInsertionOrderSet("foo", "bar", "foo");
		actual = set.size();
		expected = 2;
		assertEquals(actual, expected);
	}

	/**
	 * Test for method {@link SetFactory#newLinkedHashSet(java.util.Collection, Object...)}
	 */
	@Test
	public void testNewLinkedHashSetCollectionObjects()
	{
		int actual;
		int expected;
		Set<String> set;
		set = SetFactory.newLinkedHashSet();
		actual = set.size();
		expected = 0;
		assertEquals(actual, expected);

		set.add("foo");
		actual = set.size();
		expected = 1;
		assertEquals(actual, expected);

		set = SetFactory.newLinkedHashSet(ListFactory.newArrayList("foo", "fasel"), "foo", "bar",
			"foo");
		actual = set.size();
		expected = 3;
		assertEquals(actual, expected);

		set = SetFactory.newLinkedHashSet(ListFactory.newArrayList(), "foo", "bar", "foo");
		actual = set.size();
		expected = 2;
		assertEquals(actual, expected);
	}

	/**
	 * Test for method {@link SetFactory#newLinkedHashSet(Object...)}
	 */
	@Test
	public void testNewLinkedHashSetObjects()
	{
		int actual;
		int expected;
		Set<String> set;
		set = SetFactory.newLinkedHashSet();
		actual = set.size();
		expected = 0;
		assertEquals(actual, expected);

		set = SetFactory.newLinkedHashSet("foo", "bar", "foo");
		actual = set.size();
		expected = 2;
		assertEquals(actual, expected);
	}

	/**
	 * Test for method {@link SetFactory#newTreeSet(Collection, Object...)}
	 */
	@Test
	public void testNewTreeSetCollectionObjects()
	{
		int actual;
		int expected;
		SortedSet<String> set;
		set = SetFactory.newTreeSet();
		actual = set.size();
		expected = 0;
		assertEquals(actual, expected);

		set.add("foo");
		actual = set.size();
		expected = 1;
		assertEquals(actual, expected);

		set = SetFactory.newTreeSet(ListFactory.newArrayList("foo", "fasel"), "foo", "bar", "foo");
		actual = set.size();
		expected = 3;
		assertEquals(actual, expected);

		set = SetFactory.newTreeSet(ListFactory.newArrayList(), "foo", "bar", "foo");
		actual = set.size();
		expected = 2;
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link SetFactory#newTreeSet(Collection, Comparator, Object[])}
	 */
	@Test
	public void testNewTreeSetCollectionOfTComparatorOfTTArray()
	{
		int actual;
		int expected;
		SortedSet<String> set;

		set = SetFactory.newTreeSet();
		actual = set.size();
		expected = 0;
		assertEquals(actual, expected);

		set.add("foo");
		actual = set.size();
		expected = 1;
		assertEquals(actual, expected);

		final Comparator<String> comparator = StringLengthComparator.of(SortOrder.ASCENDING);
		final List<String> list = ListFactory.newArrayList("foo", "fasel");
		set = SetFactory.newTreeSet(list, comparator, "food", "barista", "fao");
		actual = set.size();
		expected = 5;
		assertEquals(actual, expected);

		set = SetFactory.newTreeSet(ListFactory.<String> newArrayList(), comparator, "foo", "bar",
			"foo");
		actual = set.size();
		expected = 2;
		assertEquals(actual, expected);
	}


	/**
	 * Test for method {@link SetFactory#newTreeSetSupplier}
	 */
	@Test
	public void testNewTreeSetSupplier()
	{
		SortedSet<String> expected;
		SortedSet<String> actual;
		List<String> list;
		List<String> list2;
		Comparator<String> comparator;

		list = ListFactory.newArrayList("foo", "fasel", "food", "barista", "fao");
		Comparator<String> stringComparator = Comparator.naturalOrder();
		Supplier<SortedSet<String>> supplier = SetFactory.newTreeSetSupplier(stringComparator);
		assertNotNull(supplier);
		actual = list.stream().collect(Collectors.toCollection(supplier));
		assertEquals(actual.size(), 5);
		comparator = StringLengthComparator.of(SortOrder.ASCENDING);
		list2 = ListFactory.newArrayList("foo", "fasel");
		expected = SetFactory.newTreeSet(list2, comparator, "food", "barista", "fao");
		assertEquals(actual, expected);
	}

	/**
	 * Test for method {@link SetFactory#newTreeSet(Comparator, Object[])}
	 */
	@Test
	public void testNewTreeSetWithComparator()
	{
		SortedSet<String> expected;
		SortedSet<String> actual;
		List<String> list;
		Comparator<String> comparator;

		comparator = Comparator.naturalOrder();
		actual = SetFactory.newTreeSet(comparator, "foo", "fasel", "food", "barista", "fao");
		assertEquals(actual.size(), 5);
		list = ListFactory.newArrayList("foo", "fasel", "food", "barista", "fao");
		expected = SetFactory.newTreeSet(list, comparator, "food", "barista", "fao");
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link SetFactory} with {@link BeanTester}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(SetFactory.class);
	}

}
