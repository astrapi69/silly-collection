package de.alpharogroup.collections.list;

import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

/**
 * The unit test class for the class {@link ListFactory}.
 */
public class ListFactoryTest
{

	/**
	 * Test the method {@link ListFactory#newArrayList(java.util.Collection, Object...)}.
	 */
	@Test
	public void testNewArrayListCollectionObjects()
	{
		List<String> strings = ListFactory.newArrayList(null, "foo");
		assertNotNull(strings);
		assertTrue(strings.size() == 1);
		assertTrue(strings.get(0).equals("foo"));

		strings = ListFactory.newArrayList(strings, "foo");
		assertNotNull(strings);
		assertTrue(strings.size() == 2);
		assertTrue(strings.get(0).equals("foo"));
	}

	/**
	 * Test the method {@link ListFactory#newArrayList(int)}.
	 */
	@Test
	public void testNewArrayListInt()
	{
		final List<String> strings = ListFactory.newArrayList(2);
		assertNotNull(strings);
		assertTrue(strings.size() == 0);
	}

	/**
	 * Test the method {@link ListFactory#newArrayList(Object...)}.
	 */
	@Test
	public void testNewArrayListObjects()
	{
		final List<String> strings = ListFactory.newArrayList("foo", "bar");
		assertNotNull(strings);
		assertTrue(strings.size() == 2);
		assertTrue(strings.get(0).equals("foo"));
		assertTrue(strings.get(1).equals("bar"));
	}

	/**
	 * Test the method {@link ListFactory#newRangeList(int, int)}
	 */
	@SuppressWarnings("serial")
	@Test
	public void testNewRangeList()
	{
		final List<Integer> actual = ListFactory.newRangeList(5, 9);
		final List<Integer> expected = new ArrayList<Integer>()
		{
			{
				add(5);
				add(6);
				add(7);
				add(8);
				add(9);
			}
		};
		assertTrue(actual.equals(expected));
	}

	/**
	 * Test method for {@link ListFactory} with {@link BeanTester}
	 */
	@Test(expectedExceptions = { BeanTestException.class, InvocationTargetException.class,
			UnsupportedOperationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ListFactory.class);
	}
	
}
