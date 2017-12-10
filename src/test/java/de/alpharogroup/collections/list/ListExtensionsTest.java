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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.collections.modifications.ModifiedCollections;
import de.alpharogroup.test.objects.Gender;
import de.alpharogroup.test.objects.Person;
import lombok.experimental.ExtensionMethod;

/**
 * Tests for the class {@link ListExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
@ExtensionMethod(ListExtensions.class)
public class ListExtensionsTest
{

	/**
	 * Sets up method will be invoked before every unit test method in this class.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@BeforeMethod
	protected void setUp() throws Exception
	{
	}

	/**
	 * Tear down method will be invoked after every unit test method in this class.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@AfterMethod
	protected void tearDown() throws Exception
	{
	}

	/**
	 * Test the method ListExtensions.containAtleastOneObject(List,List).
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
	 * Test method for
	 * {@link de.alpharogroup.collections.list.ListExtensions#getFirst(java.util.List)} .
	 */
	@Test
	public void testGetFirst()
	{
		final String expected = "Leonidas";
		final List<String> search = new ArrayList<>();
		search.add(expected);
		search.add("Berta");
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add("Anton");
		final String compare = ListExtensions.getFirst(search);
		assertTrue("", expected.equals(compare));
	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.collections.list.ListExtensions#getLast(java.util.List)} .
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
	 * Test the method ListExtensions.getSameElementsFromLists(List,List).
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
	 * Test method for
	 * {@link de.alpharogroup.collections.list.ListExtensions#isFirst(java.util.List)} .
	 */
	@Test
	public void testIsFirst()
	{
		final String expected = "Leonidas";
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
	 * Test method for
	 * {@link de.alpharogroup.collections.list.ListExtensions#isLast(java.util.List)} .
	 */
	@Test
	public void testIsLast()
	{
		final String expected = "Leonidas";
		final List<String> search = new ArrayList<>();
		search.add("Anton");
		search.add("Berta");
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add(expected);
		final boolean actual = search.isLast(expected);
		assertTrue("", actual);
	}

	/**
	 * Test the method ListExtensions.isNullOrEmpty(List).
	 */
	@Test
	public void testIsNullOrEmpty()
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
	 * Test the method {@link ListExtensions#newArrayList(java.util.Collection, Object...)}.
	 */
	@Test
	public void testNewArrayListCollectionObjects()
	{
		List<String> strings = ListExtensions.newArrayList(null, "foo");
		assertNotNull(strings);
		assertTrue(strings.size() == 1);
		assertTrue(strings.get(0).equals("foo"));
	}

	/**
	 * Test the method {@link ListExtensions#newArrayList(int)}.
	 */
	@Test
	public void testNewArrayListInt()
	{
		List<String> strings = ListExtensions.newArrayList(2);
		assertNotNull(strings);
		assertTrue(strings.size() == 0);
	}

	/**
	 * Test the method {@link ListExtensions#newArrayList(Object...)}.
	 */
	@Test
	public void testNewArrayListObjects()
	{
		List<String> strings = ListExtensions.newArrayList("foo", "bar");
		assertNotNull(strings);
		assertTrue(strings.size() == 2);
		assertTrue(strings.get(0).equals("foo"));
		assertTrue(strings.get(1).equals("bar"));
	}

	@Test
	public void testNewRangeArray()
	{
		final Integer[] actual = ListExtensions.newRangeArray(5, 9);
		final Integer[] expected = { 5, 6, 7, 8, 9 };

		assertTrue(Arrays.deepEquals(actual, expected));
	}

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
	 * Test method for
	 * {@link de.alpharogroup.collections.list.ListExtensions#removeFirst(java.util.List)} .
	 */
	@Test
	public void testRemoveFirst()
	{
		final String expected = "Leonidas";
		final String removed = "Berta";
		final List<String> search = new ArrayList<>();
		search.add(removed);
		search.add(expected);
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add("Anton");
		String compare = ListExtensions.removeFirst(search);
		assertTrue("", removed.equals(compare));

		compare = ListExtensions.getFirst(search);
		assertTrue("", expected.equals(compare));
	}

	/**
	 * Test method for
	 * {@link de.alpharogroup.collections.list.ListExtensions#removeLast(java.util.List)} .
	 */
	@Test
	public void testRemoveLast()
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
		String compare = ListExtensions.removeLast(search);
		assertTrue("", compare.equals(removed));

		compare = ListExtensions.getLast(search);
		assertTrue("", expected.equals(compare));
	}

	/**
	 * Test the method {@link ListExtensions#shuffle(List, List, int[])}.
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

	@Test
	public void testSortWithProperty()
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
	 * Test the method ListExtensions.splitSetToParts(Set, int).
	 */
	@Test
	@SuppressWarnings("rawtypes")
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
	 * Test the method ListExtensions.splitListToParts(List,int).
	 */
	@Test
	@SuppressWarnings("rawtypes")
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
	 * Test the method {@link ListExtensions#toObjectArray(Object...)}.
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

}