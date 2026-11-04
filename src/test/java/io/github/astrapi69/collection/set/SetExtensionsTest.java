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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
	 * Test the method {@link SetExtensions#toArray(Set)} with an empty set: the result is an empty
	 * {@code Object[]}, whatever the type of the elements, so that a caller that assigns it to a
	 * {@code String[]} gets a {@link ClassCastException}. The fix of #64 keeps this result and
	 * documents it
	 */
	@Test
	public void testToArrayWithException()
	{
		Object[] array = SetExtensions.toArray(SetFactory.newTreeSet());
		assertNotNull(array);
		assertTrue(array.length == 0);
		assertEquals(Object[].class, array.getClass());
		assertThrows(ClassCastException.class, () -> {
			String[] strings = SetExtensions.toArray(new HashSet<String>());
		});
	}

	/**
	 * Test the method {@link SetExtensions#toArray(Set)} with null: it throws the
	 * {@link IllegalArgumentException} of {@code Argument.notNull}. The fix of #64 keeps this
	 * result and names the exception in the Javadoc
	 */
	@Test
	public void testToArrayWithNullSet()
	{
		assertThrows(IllegalArgumentException.class, () -> SetExtensions.toArray(null));
	}

	/**
	 * Test the method {@link SetExtensions#toArray(Set)} with null as first element. Pins what the
	 * method does today: it takes the class of the first element as component type and throws a
	 * {@link NullPointerException}. The fix of #64 changes the result to a {@code String[]} that
	 * holds null and "a"
	 */
	@Test
	public void testToArrayWithNullFirstElement()
	{
		final Set<String> set = new LinkedHashSet<>(Arrays.asList(null, "a"));

		assertThrows(NullPointerException.class, () -> SetExtensions.toArray(set));
	}

	/**
	 * Test the method {@link SetExtensions#toArray(Set)} with elements of different classes. Pins
	 * what the method does today: the component type is the class of the first element,
	 * {@code Double} for [2.5, 1], and storing 1 throws an {@link ArrayStoreException}. The fix of
	 * #64 changes the result to a {@code Number[]} in the iteration order of the set, [2.5, 1]
	 */
	@Test
	public void testToArrayWithElementsOfDifferentClasses()
	{
		final Set<Number> set = new LinkedHashSet<>(Arrays.asList(2.5, 1));

		assertThrows(ArrayStoreException.class, () -> SetExtensions.toArray(set));
	}

	/**
	 * Test the method {@link SetExtensions#toSet(Collection)}: the new set holds the elements of
	 * the collection
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
		assertEquals(SetFactory.newHashSet("Anton", "Alex", "Berta", "Brad", "Caesar", "Leonardo"),
			set);
	}

	/**
	 * Test the method {@link SetExtensions#toSet(Collection)} with a collection that holds an
	 * element twice, with an empty collection and with null: the set holds each element once, and
	 * an empty or a null collection gives an empty set, as the collection is optional for
	 * {@link SetFactory#newHashSet(Collection, Object...)} that it delegates to
	 */
	@Test
	public void testToSetWithDuplicatesEmptyAndNull()
	{
		assertEquals(SetFactory.newHashSet("Anton", "Alex"),
			SetExtensions.toSet(ListFactory.newArrayList("Anton", "Alex", "Anton")));
		assertTrue(SetExtensions.toSet(ListFactory.<String> newArrayList()).isEmpty());
		assertTrue(SetExtensions.toSet(null).isEmpty());
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
