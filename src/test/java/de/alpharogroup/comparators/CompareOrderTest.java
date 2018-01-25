package de.alpharogroup.comparators;

import static org.testng.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;

import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

/**
 * The unit test class for the enum class {@link CompareOrder}.
 */
public class CompareOrderTest
{

	/**
	 * Test method for {@link CompareOrder}
	 */
	@Test
	public void test()
	{
		int expected;
		int actual;
		actual = CompareOrder.AFTER.getOrder();
		expected = 1;
		assertEquals(actual, expected);
		actual = CompareOrder.BEFORE.getOrder();
		expected = -1;
		assertEquals(actual, expected);
		actual = CompareOrder.EQUAL.getOrder();
		expected = 0;
		assertEquals(actual, expected);
	}

	/**
	 * Test method for {@link CompareOrder}
	 */
	@Test(expectedExceptions = { BeanTestException.class, InvocationTargetException.class,
			UnsupportedOperationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(CompareOrder.class);
	}

}
