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
package de.alpharogroup.collections.list;

import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.collections.array.ArrayExtensions;
import de.alpharogroup.collections.modifications.ModifiedCollections;
import de.alpharogroup.collections.set.SetExtensions;
import de.alpharogroup.test.objects.Person;
import de.alpharogroup.test.objects.enums.Gender;
import lombok.experimental.ExtensionMethod;

/**
 * The unit test class for the class {@link ListExtensions}.
 *
 * @author Asterios Raptis
 */
@ExtensionMethod(ListExtensions.class)
public class ListExtensionsTest
{

	/**
	 * Sets up method will be invoked before every unit test method
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@BeforeMethod
	protected void setUp() throws Exception
	{
	}

	/**
	 * Tear down method will be invoked after every unit test method
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@AfterMethod
	protected void tearDown() throws Exception
	{
	}

	/**
	 * Test the method {@link ListExtensions#containAtleastOneObject(List, List)}
	 */
	@Test
	public void testContainAtleastOneObject()
	{
		final List<String> toSearch = new ArrayList<>();
		final List<String> search = new ArrayList<>();
		final List<String> atLeastOneSameObject = new ArrayList<>();
		toSearch.add("Anton");
		toSearch.add("Berta");
		toSearch.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add("Franz");
		final boolean isFalse = ListExtensions.containAtleastOneObject(toSearch, search);
		assertFalse(isFalse);
		atLeastOneSameObject.add("Anton");
		atLeastOneSameObject.add("Dora");
		boolean isTrue = ListExtensions.containAtleastOneObject(toSearch, atLeastOneSameObject);
		assertTrue(isTrue);
		isTrue = ListExtensions.containAtleastOneObject(search, atLeastOneSameObject);
		assertTrue(isTrue);
	}

	/**
	 * Test method for {@link ListExtensions#getFirst(java.util.List)}
	 */
	@Test
	public void testGetFirst()
	{
		String actual;
		String expected;
		expected = "Leonidas";
		List<String> search = new ArrayList<>();
		search.add(expected);
		search.add("Berta");
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add("Anton");
		actual = ListExtensions.getFirst(search);
		assertEquals(expected, actual);

		search = new ArrayList<>();
		actual = ListExtensions.getFirst(search);
		expected = null;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ListExtensions#getLast(java.util.List)}
	 */
	@Test
	public void testGetLast()
	{
		final String expected = "Leonidas";
		final List<String> search = new ArrayList<>();
		String compare = ListExtensions.getLast(search);
		assertNull(compare);
		search.add("Anton");
		search.add("Berta");
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add(expected);
		compare = ListExtensions.getLast(search);
		assertTrue("", expected.equals(compare));
	}

	/**
	 * Test method for
	 * {@link ListExtensions#getModifiedCollections(java.util.Collection, java.util.Collection)}
	 */
	@Test
	public void testGetModifiedLists()
	{

		final String previous[] = { "1", "2", "3", "4", "5", "6", "7" };
		final String next[] = { "2", "5", "6", "7", "8", "9" };

		final String expectedadded[] = { "8", "9" };
		final String expectedremoved[] = { "1", "3", "4" };

		final List<String> previousList = new ArrayList<>(Arrays.asList(previous));
		final List<String> nextList = Arrays.asList(next);

		final List<String> expectedaddedList = Arrays.asList(expectedadded);
		final List<String> expectedremovedList = Arrays.asList(expectedremoved);

		final ModifiedCollections<String> result = ListExtensions
			.getModifiedCollections(previousList, nextList);

		assertTrue(result.getRemovedElements().equals(expectedremovedList));
		assertTrue(result.getAddedElements().equals(expectedaddedList));

	}

	/**
	 * Test method for {@link ListExtensions#getSameElementsFromLists(List, List)}
	 */
	@Test
	public void testGetSameElementsFromLists()
	{
		final List<String> toSearch = new ArrayList<>();
		final List<String> search = new ArrayList<>();
		final List<String> atLeastOneSameObject = new ArrayList<>();
		toSearch.add("Anton");
		toSearch.add("Berta");
		toSearch.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add("Franz");
		atLeastOneSameObject.add("Anton");
		atLeastOneSameObject.add("Dora");
		atLeastOneSameObject.add("Emil");
		final List<String> expectedElements = new ArrayList<>();
		expectedElements.add("Dora");
		expectedElements.add("Emil");
		final List<String> foundElements = ListExtensions.getSameElementsFromLists(search,
			atLeastOneSameObject);
		final boolean isTrue = expectedElements.equals(foundElements);
		assertTrue(isTrue);
		for (final String string : foundElements)
		{
			assertTrue(expectedElements.contains(string));

		}
		final List<String> empty = ListExtensions.getSameElementsFromLists(toSearch, search);
		assertNull("List should be empty.", empty);
	}

	/**
	 * Test the method {@link ListExtensions#isEmpty(List)}
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testIsEmpty()
	{

		List<String> nullList = null;

		boolean isNull = ListExtensions.isEmpty(nullList);

		assertTrue("List should be null.", isNull);

		nullList = new ArrayList<>();

		isNull = ListExtensions.isEmpty(nullList);

		assertTrue("List should be empty.", isNull);

		nullList.add("");

		isNull = ListExtensions.isEmpty(nullList);

		assertFalse("List should not be empty.", isNull);

	}

	/**
	 * Test method for {@link ListExtensions#isFirst(List, Object)}
	 */
	@Test
	public void testIsFirst()
	{
		String expected;
		expected = "Leonidas";
		final List<String> search = new ArrayList<>();
		search.add(expected);
		search.add("Berta");
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add("Anton");
		final boolean actual = search.isFirst(expected);
		assertTrue("", actual);
	}

	/**
	 * Test method for {@link ListExtensions#isLast(List, Object)}
	 */
	@Test
	public void testIsLast()
	{
		boolean actual;
		boolean expected;
		String name;
		name = "Leonidas";
		final List<String> search = ListExtensions.newArrayList();
		search.add("Anton");
		search.add("Berta");
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add(name);
		actual = search.isLast(name);
		expected = true;
		assertEquals(expected, actual);

		actual = ListExtensions.isLast(search, "Foo");
		expected = false;
		assertEquals(expected, actual);

		actual = ListExtensions.isLast(ListExtensions.newArrayList(), "Foo");
		expected = false;
		assertEquals(expected, actual);
	}

	/**
	 * Test the method {@link ListExtensions#newArrayList(java.util.Collection, Object...)}.
	 */
	@Test
	public void testNewArrayListCollectionObjects()
	{
		List<String> strings = ListExtensions.newArrayList(null, "foo");
		assertNotNull(strings);
		assertTrue(strings.size() == 1);
		assertTrue(strings.get(0).equals("foo"));

		strings = ListExtensions.newArrayList(strings, "foo");
		assertNotNull(strings);
		assertTrue(strings.size() == 2);
		assertTrue(strings.get(0).equals("foo"));
	}

	/**
	 * Test the method {@link ListExtensions#newArrayList(int)}.
	 */
	@Test
	public void testNewArrayListInt()
	{
		final List<String> strings = ListExtensions.newArrayList(2);
		assertNotNull(strings);
		assertTrue(strings.size() == 0);
	}

	/**
	 * Test the method {@link ListExtensions#newArrayList(Object...)}.
	 */
	@Test
	public void testNewArrayListObjects()
	{
		final List<String> strings = ListExtensions.newArrayList("foo", "bar");
		assertNotNull(strings);
		assertTrue(strings.size() == 2);
		assertTrue(strings.get(0).equals("foo"));
		assertTrue(strings.get(1).equals("bar"));
	}

	/**
	 * Test the method {@link ListExtensions#newRangeArray(int, int)}
	 */
	@Test
	public void testNewRangeArray()
	{
		Integer[] actual;
		Integer[] expected;
		actual = ListExtensions.newRangeArray(5, 9);
		expected = ArrayExtensions.newArray(5, 6, 7, 8, 9);
		assertTrue(Arrays.deepEquals(actual, expected));

		actual = ListExtensions.newRangeArray(1, 49);
		expected = ArrayExtensions.newArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
			17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38,
			39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49);

		assertTrue(Arrays.deepEquals(actual, expected));
	}

	/**
	 * Test the method {@link ListExtensions#newRangeArray(int, int)} where end is smaller then
	 * start
	 */
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testNewRangeArrayException()
	{
		ListExtensions.newRangeArray(9, 8);
	}

	/**
	 * Test the method {@link ListExtensions#newRangeList(int, int)}
	 */
	@SuppressWarnings("serial")
	@Test
	public void testNewRangeList()
	{
		final List<Integer> actual = ListExtensions.newRangeList(5, 9);
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
	 * Test the method {@link ListExtensions#printCollection(java.util.Collection)}
	 */
	@Test
	public void testPrintCollection()
	{
		final List<String> strings = ListExtensions.newArrayList("2", "3");
		assertNotNull(strings);
		ListExtensions.printCollection(strings);
	}

	/**
	 * Test method for {@link ListExtensions#removeFirst(java.util.List)}
	 */
	@Test
	public void testRemoveFirst()
	{
		String expected = "Leonidas";
		final String removed = "Berta";
		final List<String> search = new ArrayList<>();
		search.add(removed);
		search.add(expected);
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add("Anton");
		String actual = ListExtensions.removeFirst(search);
		assertTrue("", removed.equals(actual));

		actual = ListExtensions.getFirst(search);
		assertEquals(expected, actual);

		search.clear();

		actual = ListExtensions.removeFirst(search);
		expected = null;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ListExtensions#removeLast(java.util.List)}
	 */
	@Test
	public void testRemoveLast()
	{
		String expected = "Leonidas";
		final String removed = "Berta";
		final List<String> search = new ArrayList<>();
		search.add("Anton");
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add(expected);
		search.add(removed);
		String compare = ListExtensions.removeLast(search);
		assertTrue("", compare.equals(removed));

		compare = ListExtensions.getLast(search);
		assertTrue("", expected.equals(compare));

		search.clear();

		final String actual = ListExtensions.removeLast(search);
		expected = null;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ListExtensions#removeLastValues(List, int)}
	 */
	@Test
	public void testRemoveLastValues()
	{
		final String expected = "Leonidas";
		final String removed = "Berta";
		final List<String> search = new ArrayList<>();
		search.add("Anton");
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add(expected);
		search.add(removed);
		final List<String> removedLastValues = ListExtensions.removeLastValues(search, 2);
		assertTrue(removedLastValues.size() == 4);

	}

	/**
	 * Test method for {@link ListExtensions#removeLastValues(List, int)} where the remove value is
	 * greater then the size of the given list
	 */
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testRemoveLastValuesException()
	{
		final String expected = "Leonidas";
		final String removed = "Berta";
		final List<String> search = new ArrayList<>();
		search.add("Anton");
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add(expected);
		search.add(removed);
		final List<String> removedLastValues = ListExtensions.removeLastValues(search, 7);
		assertTrue(removedLastValues.size() == 4);
	}

	/**
	 * Test the method {@link ListExtensions#revertOrder(List)}
	 */
	@Test
	public void testRevertOrder()
	{
		final List<String> search = new ArrayList<>();
		search.add("Anton");
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add("Leonidas");
		search.add("Berta");
		final List<String> revertOrdered = ListExtensions.revertOrder(search);
		assertNotNull(revertOrdered);

		assertTrue(revertOrdered.get(0).equals("Berta"));
		assertTrue(revertOrdered.get(1).equals("Leonidas"));
		assertTrue(revertOrdered.get(2).equals("Emil"));
		assertTrue(revertOrdered.get(3).equals("Dora"));
		assertTrue(revertOrdered.get(4).equals("Caesar"));
		assertTrue(revertOrdered.get(5).equals("Anton"));
	}

	/**
	 * Test the method {@link ListExtensions#shuffle(List, List, int[])}
	 */
	@Test
	public void testShuffle()
	{
		final List<String> source = new ArrayList<>();
		final List<String> destination = new ArrayList<>();
		final String berta = "Berta";
		final String caesar = "Caesar";
		source.add("Anton");
		source.add(berta);
		source.add(caesar);
		source.add("Dora");
		source.add("Emil");
		source.add("Franz");
		// shuffle two elements to destination list with index 1 and 2.
		final int[] selectedElements = { 1, 2 };
		source.shuffle(destination, selectedElements);

		final boolean actual = destination.size() == 2;
		assertTrue(actual);
		assertTrue(destination.contains(berta));
		assertTrue(destination.contains(caesar));
		// shuffle the elements back
		final int[] newSelectedElements = { 0, 1 };
		destination.shuffle(source, newSelectedElements);

	}

	/**
	 * Test the method {@link ListExtensions#sortByProperty(List, String, boolean)}
	 */
	@Test
	public void testSortByProperty()
	{
		final List<Person> persons = new ArrayList<>();
		final Person obelix = new Person();
		obelix.setGender(Gender.MALE);
		obelix.setName("obelix");

		final Person asterix = new Person();
		asterix.setGender(Gender.MALE);
		asterix.setName("asterix");

		final Person miraculix = new Person();
		miraculix.setGender(Gender.MALE);
		miraculix.setName("miraculix");

		persons.add(obelix);
		persons.add(asterix);
		persons.add(miraculix);

		// Unsorted Persons...
		assertTrue(
			"Index of person 'obelix' should be <0> but was <" + persons.indexOf(obelix) + ">.",
			persons.indexOf(obelix) == 0);
		assertTrue(
			"Index of person 'asterix' should be <1> but was <" + persons.indexOf(asterix) + ">.",
			persons.indexOf(asterix) == 1);
		assertTrue("Index of person 'miraculix' should be <2> but was <"
			+ persons.indexOf(miraculix) + ">.", persons.indexOf(miraculix) == 2);

		ListExtensions.sortByProperty(persons, "name", true);
		// Sorted Persons by name in ascending order...
		assertTrue(
			"Index of person 'obelix' should be <0> but was <" + persons.indexOf(obelix) + ">.",
			persons.indexOf(obelix) == 0);
		assertTrue("Index of person 'miraculix' should be <1> but was <"
			+ persons.indexOf(miraculix) + ">.", persons.indexOf(miraculix) == 1);
		assertTrue(
			"Index of person 'asterix' should be <2> but was <" + persons.indexOf(asterix) + ">.",
			persons.indexOf(asterix) == 2);

		ListExtensions.sortByProperty(persons, "name", false);
		// Sorted Persons by name in descending order...
		assertTrue(
			"Index of person 'asterix' should be <0> but was <" + persons.indexOf(asterix) + ">.",
			persons.indexOf(asterix) == 0);
		assertTrue("Index of person 'miraculix' should be <1> but was <"
			+ persons.indexOf(miraculix) + ">.", persons.indexOf(miraculix) == 1);
		assertTrue(
			"Index of person 'obelix' should be <2> but was <" + persons.indexOf(obelix) + ">.",
			persons.indexOf(obelix) == 2);
		// set a null value...
		asterix.setName(null);

		ListExtensions.sortByProperty(persons, "name", true);
		// Sorted Persons by name in ascending order with a null value...
		assertTrue(
			"Index of person 'obelix' should be <0> but was <" + persons.indexOf(obelix) + ">.",
			persons.indexOf(obelix) == 0);
		assertTrue("Index of person 'miraculix' should be <1> but was <"
			+ persons.indexOf(miraculix) + ">.", persons.indexOf(miraculix) == 1);
		assertTrue(
			"Index of person 'asterix' should be <2> but was <" + persons.indexOf(asterix) + ">.",
			persons.indexOf(asterix) == 2);
	}

	/**
	 * Test the method {@link ListExtensions#splitSetToParts(Set, int)}
	 */
	@Test
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public void testSplitToParts()
	{
		final Set<Integer> set = new HashSet<>();
		for (int i = 0; i < 999; i++)
		{
			set.add(i);
		}
		final List al = ListExtensions.splitSetToParts(set, 50);
		assertTrue(al.size() == 20);
		for (int i = 0; i < al.size(); i++)
		{
			if (i == al.size() - 1)
			{
				final ArrayList lastPart = (ArrayList)al.get(i);
				assertTrue(lastPart.size() == 49);
			}
			else
			{
				final ArrayList aPart = (ArrayList)al.get(i);
				assertTrue(aPart.size() == 50);
			}
		}
	}

	/**
	 * Test the method {@link ListExtensions#splitListToParts(List, int)}
	 */
	@Test
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public void testsplitToPartsInList()
	{
		final List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 999; i++)
		{
			list.add(i);
		}
		final List al = ListExtensions.splitListToParts(list, 50);
		assertTrue(al.size() == 20);
		for (int i = 0; i < al.size(); i++)
		{
			if (i == al.size() - 1)
			{
				final List lastPart = (ArrayList)al.get(i);
				assertTrue(lastPart.size() == 49);
			}
			else
			{
				final List aPart = (ArrayList)al.get(i);
				assertTrue(aPart.size() == 50);
			}
		}
	}

	/**
	 * Test the method {@link ListExtensions#toArray(Object...)}
	 */
	@Test
	public void testToArray()
	{
		Integer actual;
		Integer expected;
		final Integer[] numbers = { 1, 2, 3 };
		final Integer[] array = ListExtensions.toArray(1, 2, 3);
		for (int i = 0; i < numbers.length; i++)
		{
			expected = numbers[i];
			actual = array[i];
			assertEquals(expected, actual);
		}
	}

	/**
	 * Test the method {@link ListExtensions#toList(Enumeration)}
	 */
	@Test
	public void testToListEnumerationOfT()
	{
		final List<String> arrayList = ListExtensions.newArrayList("1", "2");
		final Enumeration<String> elements = Collections.enumeration(arrayList);
		final List<String> list = ListExtensions.toList(elements);
		for (final String item : list)
		{
			assertTrue(arrayList.contains(item));
		}
	}

	/**
	 * Test the method {@link ListExtensions#toList(Set)}
	 */
	@Test
	public void testToListSetOfT()
	{
		Set<String> set = SetExtensions.newHashSet();
		assertTrue(set.size() == 0);
		set = SetExtensions.newHashSet("foo", "bar", "foo");
		assertTrue(set.size() == 2);
		final List<String> list = ListExtensions.toList(set);
		for (final String item : list)
		{
			assertTrue(set.contains(item));
		}
	}

	/**
	 * Test the method {@link ListExtensions#toObjectArray(Object...)}
	 */
	@Test
	public void testToObjectArray()
	{
		final Object expected[] = { "2", "5", "6", "7", "8", "9" };
		Object[] actual = ListExtensions.toObjectArray("2", "5", "6", "7", "8", "9");
		assertTrue(expected.length == actual.length);
		for (int i = 0; i < actual.length; i++)
		{
			assertEquals(expected[i], actual[i]);
		}
		actual = ListExtensions.toObjectArray();
		assertTrue(actual.length == 0);
	}

	/**
	 * Test the method {@link ListExtensions#toVector(Enumeration)}
	 */
	@Test
	public void testToVector()
	{
		final List<String> arrayList = ListExtensions.newArrayList("1", "2");
		final Enumeration<String> elements = Collections.enumeration(arrayList);
		@SuppressWarnings("deprecation")
		final Vector<String> vector = ListExtensions.toVector(elements);
		for (final String string : vector)
		{
			assertTrue(arrayList.contains(string));
		}
	}

	/**
	 * Test method for {@link ListExtensions} with {@link BeanTester}
	 */
	@Test(expectedExceptions = { BeanTestException.class, InvocationTargetException.class,
			UnsupportedOperationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ListExtensions.class);
	}

}
