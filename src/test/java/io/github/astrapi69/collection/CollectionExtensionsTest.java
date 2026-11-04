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
package io.github.astrapi69.collection;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.array.ArrayFactory;
import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.collection.set.SetFactory;

/**
 * The unit test class for the class {@link CollectionExtensions}.
 */
public class CollectionExtensionsTest
{

	/**
	 * Test method for {@link CollectionExtensions#toArray(Collection)}
	 */
	@Test
	public void testToArray()
	{
		Integer[] expected;
		Integer[] actual;
		Collection<Integer> data;

		// Test with LinkedHashSet
		data = SetFactory.newLinkedHashSet(1, 2, 3);
		expected = ArrayFactory.newArray(1, 2, 3);
		actual = CollectionExtensions.toArray(data);
		assertArrayEquals(expected, actual);

		// Test with ArrayList
		data = ListFactory.newArrayList(1, 2, 3);
		actual = CollectionExtensions.toArray(data);
		assertArrayEquals(expected, actual);

		// Test with null collection
		data = null;
		actual = CollectionExtensions.toArray(data);
		assertNull(actual);

		// Test with empty LinkedHashSet
		data = SetFactory.newLinkedHashSet();
		actual = CollectionExtensions.toArray(data);
		assertNull(actual);
	}

	/**
	 * Test method for {@link CollectionExtensions#toArray(Collection)} with null as first element.
	 * Pins what the method does today: it takes the class of the first element as component type
	 * and throws a {@link NullPointerException}. The fix of #64 changes the result to a
	 * {@code String[]} that holds null and "a"
	 */
	@Test
	public void testToArrayWithNullFirstElement()
	{
		final List<String> list = new ArrayList<>(Arrays.asList(null, "a"));

		assertThrows(NullPointerException.class, () -> CollectionExtensions.toArray(list));
	}

	/**
	 * Test method for {@link CollectionExtensions#toArray(Collection)} with elements of different
	 * classes. Pins what the method does today: the component type is the class of the first
	 * element, {@code Integer} for [1, 2.5] and {@code Double} for [2.5, 1], and storing the other
	 * element throws an {@link ArrayStoreException}. The fix of #64 changes the result to a
	 * {@code Number[]} in both orders
	 */
	@Test
	public void testToArrayWithElementsOfDifferentClasses()
	{
		final List<Number> integerFirst = new ArrayList<>(Arrays.asList(1, 2.5));
		final List<Number> doubleFirst = new ArrayList<>(Arrays.asList(2.5, 1));

		assertThrows(ArrayStoreException.class, () -> CollectionExtensions.toArray(integerFirst));
		assertThrows(ArrayStoreException.class, () -> CollectionExtensions.toArray(doubleFirst));
	}

	/**
	 * Test method for {@link CollectionExtensions#toArray(Collection)} with elements whose only
	 * common superclass is {@link Object}, 1 and "a" in a {@code List<Comparable<?>>}. Pins what
	 * the method does today: the component type is {@code Integer}, and storing "a" throws an
	 * {@link ArrayStoreException}. The fix of #64 changes the result to an {@code Object[]}, which
	 * a caller cannot assign to a {@code Comparable<?>[]}
	 */
	@Test
	public void testToArrayWithElementsWithoutCommonSuperclass()
	{
		final List<Comparable<?>> list = new ArrayList<>(Arrays.asList(1, "a"));

		assertThrows(ArrayStoreException.class, () -> CollectionExtensions.toArray(list));
	}

	/**
	 * Test method for {@link CollectionExtensions#toArray(Collection)} with null elements only.
	 * Pins what the method does today: it throws a {@link NullPointerException}. The fix of #64
	 * changes the result to an {@code Object[]} that holds two nulls, which a caller cannot assign
	 * to a {@code String[]}
	 */
	@Test
	public void testToArrayWithNullElementsOnly()
	{
		final List<String> list = new ArrayList<>(Arrays.asList(null, null));

		assertThrows(NullPointerException.class, () -> CollectionExtensions.toArray(list));
	}

	/**
	 * Test method for {@link CollectionExtensions#toList(Collection)}
	 */
	@Test
	public void testToList()
	{
		List<Integer> expected;
		List<Integer> actual;
		Collection<Integer> data;

		data = SetFactory.newLinkedHashSet(1, 2, 3);
		actual = CollectionExtensions.toList(data);
		expected = ListFactory.newArrayList(1, 2, 3);
		assertEquals(expected, actual);

		data = ListFactory.newArrayList(1, 2, 3);
		actual = CollectionExtensions.toList(data);
		expected = ListFactory.newArrayList(1, 2, 3);
		assertEquals(expected, actual);

		data = null;
		actual = CollectionExtensions.toList(data);
		expected = null;
		assertEquals(expected, actual);
	}

	/**
	 * Test the method {@link CollectionExtensions#toList(Collection)} with a {@link List}: the
	 * result is the given list itself, not a copy of it
	 */
	@Test
	public void testToListReturnsAGivenListItself()
	{
		final List<Integer> arrayList = ListFactory.newArrayList(1, 2, 3);
		final List<Integer> linkedList = ListFactory.newLinkedList(1, 2, 3);
		assertSame(arrayList, CollectionExtensions.toList(arrayList));
		assertSame(linkedList, CollectionExtensions.toList(linkedList));
	}

	/**
	 * Test method for {@link CollectionExtensions#containsAtLeastOne(Collection, Object[])}
	 */
	@Test
	public void testContainsAtLeastOne()
	{
		boolean expected;
		boolean actual;
		Integer[] objectsToCheckIfContains;
		Collection<Integer> someNumbers;
		// new scenario ...
		someNumbers = ListFactory.newArrayList(22, 33, 25, 45);
		objectsToCheckIfContains = ArrayFactory.newArray(3, 7);
		actual = CollectionExtensions.containsAtLeastOne(someNumbers, objectsToCheckIfContains);
		expected = false;
		assertEquals(expected, actual);
		// new scenario ...
		someNumbers = ListFactory.newArrayList(22, 33, 25, 45);
		objectsToCheckIfContains = ArrayFactory.newArray(3, 33);
		actual = CollectionExtensions.containsAtLeastOne(someNumbers, objectsToCheckIfContains);
		expected = true;
		assertEquals(expected, actual);
	}

	/**
	 * Test the method {@link CollectionExtensions#containsAtLeastOne(Collection, Object[])} without
	 * objects to look for: the result is false
	 */
	@Test
	public void testContainsAtLeastOneWithoutObjects()
	{
		final Collection<Integer> someNumbers = ListFactory.newArrayList(22, 33, 25, 45);
		assertFalse(CollectionExtensions.containsAtLeastOne(someNumbers));
	}

	/**
	 * Test method for {@link CollectionExtensions#difference(Collection, Collection)}
	 */
	@Test
	public void testDifference()
	{
		Collection<Integer> expected;
		Collection<Integer> actual;
		Collection<Integer> yourNumbers;
		yourNumbers = ListFactory.newArrayList(22, 33, 25, 45);
		actual = ListFactory.newArrayList(3, 7, 22, 23, 34, 45);
		expected = ListFactory.newArrayList(3, 7, 23, 34);
		CollectionExtensions.difference(actual, yourNumbers);

		assertEquals(expected.size(), actual.size());
		for (final Integer number : actual)
		{
			assertTrue(expected.contains(number));
		}
	}

	/**
	 * Test method for {@link CollectionExtensions#isEqualCollection(Collection, Collection)} with
	 * lists of lines, each line a list, which compares by its content; an array would compare by
	 * identity, see {@link #testIsEqualCollectionComparesArraysByIdentity()}
	 */
	@Test
	public void testEqualCollections()
	{
		boolean actual;
		boolean expected;
		List<String> lineOne;
		List<String> lineTwo;
		List<String> lineThree;
		List<String> lineFour;
		List<String> lineFive;
		List<String> lineSix;
		List<List<String>> one;
		List<List<String>> other;
		lineOne = ListFactory.newArrayList("1", "23", "3");
		lineTwo = ListFactory.newArrayList("4", "25", "9");
		lineThree = ListFactory.newArrayList("6", "21", "8");
		one = ListFactory.newArrayList(lineOne, lineTwo, lineThree);
		lineFour = ListFactory.newArrayList("1", "23", "3");
		lineFive = ListFactory.newArrayList("4", "25", "9");
		lineSix = ListFactory.newArrayList("6", "21", "8");
		other = ListFactory.newArrayList(lineFour, lineFive, lineSix);
		actual = CollectionExtensions.isEqualCollection(one, other);
		expected = true;
		assertEquals(expected, actual);
		//
		one = null;
		other = null;
		actual = CollectionExtensions.isEqualCollection(one, other);
		assertEquals(expected, actual);
		//
		other = ListFactory.newArrayList(lineFour, lineFive, lineSix);
		actual = CollectionExtensions.isEqualCollection(one, other);
		expected = false;
		assertEquals(expected, actual);
		//
		one = ListFactory.newArrayList(lineOne, lineTwo, lineThree);
		other = null;
		actual = CollectionExtensions.isEqualCollection(one, other);
		assertEquals(expected, actual);
		//
		one = ListFactory.newArrayList(lineOne, lineTwo);
		other = ListFactory.newArrayList(lineFour, lineFive, lineSix);
		actual = CollectionExtensions.isEqualCollection(one, other);
		assertEquals(expected, actual);

	}

	/**
	 * Test the method {@link CollectionExtensions#isEqualCollection(Collection, Collection)} with
	 * two collections of the same size that share some but not all of their elements
	 */
	@Test
	public void testIsEqualCollectionWithPartlyDifferentElements()
	{
		final List<Integer> one = ListFactory.newArrayList(1, 2, 3);
		final List<Integer> other = ListFactory.newArrayList(1, 2, 4);
		assertFalse(CollectionExtensions.isEqualCollection(one, other));
	}

	/**
	 * Test the method {@link CollectionExtensions#isEqualCollection(Collection, Collection)} with
	 * two collections of the same size without a common element: they are not equal (#42)
	 */
	@Test
	public void testIsEqualCollectionWithoutCommonElement()
	{
		boolean actual;
		boolean expected;
		final List<Integer> one = ListFactory.newArrayList(1, 2);
		final List<Integer> other = ListFactory.newArrayList(3, 4);
		actual = CollectionExtensions.isEqualCollection(one, other);
		expected = false;
		assertEquals(expected, actual);
	}

	/**
	 * Test the method {@link CollectionExtensions#isEqualCollection(Collection, Collection)} with
	 * two collections that hold the same elements with a different cardinality: they are not equal,
	 * as the cardinality counts (#42)
	 */
	@Test
	public void testIsEqualCollectionWithDifferentCardinality()
	{
		boolean actual;
		boolean expected;
		final List<Integer> one = ListFactory.newArrayList(1, 1, 2);
		final List<Integer> other = ListFactory.newArrayList(1, 2, 2);
		actual = CollectionExtensions.isEqualCollection(one, other);
		expected = false;
		assertEquals(expected, actual);
	}

	/**
	 * Test the method {@link CollectionExtensions#isEqualCollection(Collection, Collection)} with
	 * two lists of the same size whose arrays hold different values: they are not equal (#42)
	 */
	@Test
	public void testIsEqualCollectionWithArraysOfDifferentContent()
	{
		boolean actual;
		boolean expected;
		final List<String[]> one = ListFactory.newArrayList(ArrayFactory.newArray("1", "23", "3"),
			ArrayFactory.newArray("4", "25", "9"));
		final List<String[]> other = ListFactory.newArrayList(ArrayFactory.newArray("x"),
			ArrayFactory.newArray("y"));
		actual = CollectionExtensions.isEqualCollection(one, other);
		expected = false;
		assertEquals(expected, actual);
	}

	/**
	 * Test the method {@link CollectionExtensions#isEqualCollection(Collection, Collection)} with
	 * lists of arrays: the arrays compare by identity, so two lists that hold the same arrays are
	 * equal in any order, and two lists that hold other arrays with the same content are not (#42)
	 */
	@Test
	public void testIsEqualCollectionComparesArraysByIdentity()
	{
		boolean actual;
		boolean expected;
		final String[] lineOne = ArrayFactory.newArray("1", "23", "3");
		final String[] lineTwo = ArrayFactory.newArray("4", "25", "9");
		final List<String[]> one = ListFactory.newArrayList(lineOne, lineTwo);
		List<String[]> other;
		// the same arrays in the other order
		other = ListFactory.newArrayList(lineTwo, lineOne);
		actual = CollectionExtensions.isEqualCollection(one, other);
		expected = true;
		assertEquals(expected, actual);
		// other arrays with the same content
		other = ListFactory.newArrayList(ArrayFactory.newArray("1", "23", "3"),
			ArrayFactory.newArray("4", "25", "9"));
		actual = CollectionExtensions.isEqualCollection(one, other);
		expected = false;
		assertEquals(expected, actual);
	}

	/**
	 * Test the method {@link CollectionExtensions#isEqualCollection(Collection, Collection)} with
	 * two collections that hold the same elements with the same cardinality in another order: they
	 * are equal, as the order does not count (#42)
	 */
	@Test
	public void testIsEqualCollectionWithSameElementsInAnotherOrder()
	{
		boolean actual;
		boolean expected;
		final List<Integer> one = ListFactory.newArrayList(1, 1, 2);
		final List<Integer> other = ListFactory.newArrayList(2, 1, 1);
		actual = CollectionExtensions.isEqualCollection(one, other);
		expected = true;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link CollectionExtensions#hashCode(java.util.Collection)}.
	 */
	@Test
	public void testHashCodeCollectionWithArrayObjects()
	{
		int actual;
		int expected;
		String[] lineOne;
		String[] lineTwo;
		String[] lineThree;
		List<String[]> lines;
		lineOne = ArrayFactory.newArray("1", "23", "3");
		lineTwo = ArrayFactory.newArray("4", "25", "9");
		lineThree = ArrayFactory.newArray("6", "21", "8");
		lines = ListFactory.newArrayList(lineOne, lineTwo, lineThree);

		actual = CollectionExtensions.hashCode(lines);
		expected = -1269378556;
		assertEquals(expected, actual);

		lineOne = ArrayFactory.newArray("John", "23", "male");
		lineTwo = ArrayFactory.newArray("Jim", "25", "male");
		lineThree = ArrayFactory.newArray("Mary", "21", "female");
		lines = ListFactory.newArrayList(lineOne, lineTwo, lineThree);


		actual = CollectionExtensions.hashCode(lines);
		expected = -1124541430;
		assertEquals(expected, actual);

	}

	/**
	 * Test the method for {@link CollectionExtensions#intersection(Collection...)}
	 */
	@Test
	public void testIntersection()
	{
		Collection<String> expected;
		Collection<String> actual;
		final Collection<String> nameList = ListFactory.newArrayList();

		nameList.add("Anton");
		nameList.add("Alex");
		nameList.add("Berta");
		nameList.add("Brad");
		nameList.add("Caesar");
		nameList.add("Leonardo");

		final Collection<String> otherNames = ListFactory.newArrayList();

		otherNames.add("Alex");
		otherNames.add("Berta");
		otherNames.add("Brad");
		otherNames.add("Caesar");
		otherNames.add("Leonardo");

		final Collection<String> vipNames = ListFactory.newArrayList();
		vipNames.add("Alex");
		vipNames.add("Brad");
		vipNames.add("Caesar");
		vipNames.add("Leonardo");
		vipNames.add("Jesus");

		expected = ListFactory.newArrayList();
		expected.add("Alex");
		expected.add("Brad");
		expected.add("Caesar");
		expected.add("Leonardo");
		actual = CollectionExtensions.intersection(vipNames, otherNames, nameList);
		assertNotNull(actual);
		assertEquals(expected.size(), actual.size());
		for (final String name : actual)
		{
			assertTrue(expected.contains(name));
		}

		actual = CollectionExtensions.intersection(vipNames);
		assertNull(actual);

	}

	/**
	 * Test the method for {@link CollectionExtensions#intersection(Collection...)} with the
	 * scenario of lotto numbers
	 */
	@Test
	public void testIntersectionLottoNumbers()
	{
		Collection<Integer> expected;
		Collection<Integer> actual;
		Collection<Integer> yourNumbers;
		final Collection<Integer> lottoNumbers = ListFactory.newArrayList(3, 7, 22, 23, 34, 45);
		yourNumbers = ListFactory.newArrayList(3, 7, 23, 34, 22, 45);
		actual = CollectionExtensions.intersection(lottoNumbers, yourNumbers);
		expected = lottoNumbers;
		assertEquals(expected.size(), actual.size());
		for (final Integer number : actual)
		{
			assertTrue(expected.contains(number));
		}

		yourNumbers = ListFactory.newArrayList(3, 7, 24, 35, 25, 46);
		actual = CollectionExtensions.intersection(lottoNumbers, yourNumbers);
		expected = ListFactory.newArrayList(3, 7);
		assertEquals(expected.size(), actual.size());
		for (final Integer number : actual)
		{
			assertTrue(expected.contains(number));
		}

	}

	/**
	 * Test the method {@link CollectionExtensions#isEmpty(Collection)}
	 */
	@Test
	public void testIsEmpty()
	{
		boolean actual;
		boolean expected;

		Collection<String> nullCollection = null;

		actual = CollectionExtensions.isEmpty(nullCollection);
		expected = true;
		assertEquals(expected, actual);

		nullCollection = ListFactory.newArrayList();

		actual = CollectionExtensions.isEmpty(nullCollection);
		assertEquals(expected, actual);

		nullCollection.add("foo");

		actual = CollectionExtensions.isEmpty(nullCollection);
		expected = false;
		assertEquals(expected, actual);
	}

	/**
	 * Test the method {@link CollectionExtensions#isNotEmpty(Collection)}
	 */
	@Test
	public void testIsNotEmpty()
	{
		boolean actual;
		boolean expected;

		Collection<String> nullCollection = null;

		actual = CollectionExtensions.isNotEmpty(nullCollection);
		expected = false;
		assertEquals(expected, actual);

		nullCollection = ListFactory.newArrayList();

		actual = CollectionExtensions.isNotEmpty(nullCollection);
		assertEquals(expected, actual);

		nullCollection.add("foo");

		actual = CollectionExtensions.isNotEmpty(nullCollection);
		expected = true;
		assertEquals(expected, actual);
	}

	/**
	 * Test the method {@link CollectionExtensions#partition(Collection, int)}
	 */
	@Test
	public void testPartition()
	{
		final List<Integer> list = ListFactory.newArrayList();
		for (int i = 0; i < 999; i++)
		{
			list.add(i);
		}
		Collection<List<Integer>> all = CollectionExtensions.partition(list, 50);
		assertEquals(all.size(), 20);
		all.forEach(l -> assertTrue(l.size() == 50 || l.size() == 49));
	}

	/**
	 * Test method for {@link CollectionExtensions} with {@link BeanTester}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(CollectionExtensions.class);
	}

}
