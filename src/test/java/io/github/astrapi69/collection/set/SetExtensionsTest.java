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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.array.ArrayFactory;
import io.github.astrapi69.collection.list.ListFactory;

/**
 * The unit test class for the class {@link SetExtensionsTest}.
 */
public class SetExtensionsTest
{


	/**
	 * Test the method {@link SetExtensions#toArray(Set)}
	 */
	@Test
	public void testToArray()
	{
		Integer[] actual;
		Integer[] expected;
		Set<Integer> numbers;
		// new scenario...
		numbers = SetFactory.newTreeSet(1, 2, 3);
		actual = SetExtensions.toArray(numbers);
		expected = ArrayFactory.newArray(1, 2, 3);
		assertTrue(Arrays.equals(actual, expected));
	}

	/**
	 * Test the method {@link SetExtensions#toArray(Set)} that throws an
	 * <code>IllegalArgumentException</code>
	 */
	@Test
	public void testToArrayWithException()
	{
		Object[] array = SetExtensions.toArray(SetFactory.newTreeSet());
		assertNotNull(array);
		assertTrue(array.length == 0);
	}

	/**
	 * Test for method {@link SetExtensions#toSet(Collection)}
	 */
	@Test
	public void testToSet()
	{
		final Collection<String> nameList = ListFactory.newArrayList();

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
		final Collection<String> nameList = ListFactory.newArrayList();

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
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(SetExtensions.class);
	}

}
