package de.alpharogroup.collections.set;

import static org.testng.AssertJUnit.assertTrue;

import java.lang.reflect.InvocationTargetException;
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
	 * Test method for {@link SetFactory} with {@link BeanTester}
	 */
	@Test(expectedExceptions = { BeanTestException.class, InvocationTargetException.class,
			UnsupportedOperationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(SetFactory.class);
	}
	
}
