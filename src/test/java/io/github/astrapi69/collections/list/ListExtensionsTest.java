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
package io.github.astrapi69.collections.list;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.astrapi69.test.objects.Person;
import io.github.astrapi69.test.objects.enums.Gender;
import io.github.astrapi69.collections.CollectionExtensions;
import io.github.astrapi69.collections.array.ArrayFactory;
import io.github.astrapi69.collections.modifications.ModifiedCollections;
import io.github.astrapi69.collections.set.SetFactory;

/**
 * The unit test class for the class {@link ListExtensions}
 *
 * @author Asterios Raptis
 */
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
	 * Test method for the combination methods
	 */
	@Test(enabled = false)
	public void testBenchmarkCombinations()
	{

		long start;
		long end;
		List<Integer> possibleNumbers;
		int combinationSize;

		possibleNumbers = ListFactory.newRangeList(1, 49);
		combinationSize = 6;

		start = System.currentTimeMillis();
		ListExtensions.getCombinations(possibleNumbers, combinationSize);
		end = System.currentTimeMillis();
		System.err.println("getCombinations time:" + (end - start));

		ListExtensions.getAllCombinations(possibleNumbers, combinationSize);
		end = System.currentTimeMillis();
		System.err.println("getAllCombinations time:" + (end - start));
	}


	/**
	 * Test the method {@link ListExtensions#containAtleastOneObject(List, List)}
	 */
	@Test
	public void testContainAtleastOneObject()
	{
		final List<String> toSearch = ListFactory.newArrayList();
		final List<String> search = ListFactory.newArrayList();
		final List<String> atLeastOneSameObject = ListFactory.newArrayList();
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
	 * Test the method {@link ListExtensions#getAllCombinations(List, int)}
	 */
	@Test
	public void testGetAllCombinations()
	{
		List<Integer> values;
		List<List<Integer>> actual;
		List<List<Integer>> expected;
		// new scenario...
		expected = ListFactory.newArrayList();
		values = ListFactory.newArrayList(1, 2, 3, 4);
		actual = ListExtensions.getAllCombinations(values, 3);
		expected.add(ListFactory.newArrayList(1, 2, 3));
		expected.add(ListFactory.newArrayList(1, 2, 4));
		expected.add(ListFactory.newArrayList(1, 3, 4));
		expected.add(ListFactory.newArrayList(2, 3, 4));
		assertTrue(CollectionExtensions.isEqualCollection(actual, expected));
		// new scenario...
		expected = ListFactory.newArrayList();
		values = ListFactory.newArrayList(1, 2, 3, 4, 5);
		actual = ListExtensions.getAllCombinations(values, 3);
		expected.add(ListFactory.newArrayList(1, 2, 3));
		expected.add(ListFactory.newArrayList(1, 2, 4));
		expected.add(ListFactory.newArrayList(1, 2, 5));
		expected.add(ListFactory.newArrayList(1, 3, 4));
		expected.add(ListFactory.newArrayList(1, 3, 5));
		expected.add(ListFactory.newArrayList(1, 4, 5));
		expected.add(ListFactory.newArrayList(2, 3, 4));
		expected.add(ListFactory.newArrayList(2, 3, 5));
		expected.add(ListFactory.newArrayList(2, 4, 5));
		expected.add(ListFactory.newArrayList(3, 4, 5));
		assertTrue(CollectionExtensions.isEqualCollection(actual, expected));

		expected = ListFactory.newArrayList();
		values = ListFactory.newRangeList(1, 11);
		actual = ListExtensions.getAllCombinations(values, 3);
		System.err.println(actual.size());
		// new scenario...
		expected = ListFactory.newArrayList();
		values = ListFactory.newArrayList(1, 2, 3, 4, 5, 6, 7);
		actual = ListExtensions.getAllCombinations(values, 6);
		expected.add(ListFactory.newArrayList(1, 2, 3, 4, 5, 6));
		expected.add(ListFactory.newArrayList(1, 2, 3, 4, 5, 7));
		expected.add(ListFactory.newArrayList(1, 2, 3, 4, 6, 7));
		expected.add(ListFactory.newArrayList(1, 2, 3, 5, 6, 7));
		expected.add(ListFactory.newArrayList(1, 2, 4, 5, 6, 7));
		expected.add(ListFactory.newArrayList(1, 3, 4, 5, 6, 7));
		expected.add(ListFactory.newArrayList(2, 3, 4, 5, 6, 7));
		assertTrue(CollectionExtensions.isEqualCollection(actual, expected));
	}

	/**
	 * Test the method {@link ListExtensions#getCombinations(List, int)}
	 */
	@Test
	public void testGetCombinations()
	{
		List<String> values;
		List<List<String>> actual;
		List<List<String>> expected;
		// new scenario...
		expected = ListFactory.newArrayList();
		values = ListFactory.newArrayList("1", "2", "3", "4");
		actual = ListExtensions.getCombinations(values, 3);
		expected.add(ListFactory.newArrayList("1", "2", "3"));
		expected.add(ListFactory.newArrayList("1", "2", "4"));
		expected.add(ListFactory.newArrayList("1", "3", "4"));
		expected.add(ListFactory.newArrayList("2", "3", "4"));
		assertTrue(CollectionExtensions.isEqualCollection(actual, expected));
		// new scenario...
		expected = ListFactory.newArrayList();
		values = ListFactory.newArrayList("1", "2", "3", "4", "5", "6", "7");
		actual = ListExtensions.getCombinations(values, 6);
		expected.add(ListFactory.newArrayList("1", "2", "3", "4", "5", "6"));
		expected.add(ListFactory.newArrayList("1", "2", "3", "4", "5", "7"));
		expected.add(ListFactory.newArrayList("1", "2", "3", "4", "6", "7"));
		expected.add(ListFactory.newArrayList("1", "2", "3", "5", "6", "7"));
		expected.add(ListFactory.newArrayList("1", "2", "4", "5", "6", "7"));
		expected.add(ListFactory.newArrayList("1", "3", "4", "5", "6", "7"));
		expected.add(ListFactory.newArrayList("2", "3", "4", "5", "6", "7"));
		assertTrue(CollectionExtensions.isEqualCollection(actual, expected));
	}

	/**
	 * Test method for {@link ListExtensions#getFirst(List)}
	 */
	@Test
	public void testGetFirst()
	{
		String actual;
		String expected;
		List<String> search;

		expected = "Leonidas";
		search = ListFactory.newArrayList();
		search.add(expected);
		search.add("Berta");
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add("Anton");

		actual = ListExtensions.getFirst(search);
		assertEquals(expected, actual);

		search = ListFactory.newArrayList();
		actual = ListExtensions.getFirst(search);
		expected = null;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ListExtensions#getLast(List)}
	 */
	@Test
	public void testGetLast()
	{
		final String expected = "Leonidas";
		final List<String> search = ListFactory.newArrayList();
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
	 * Test method for {@link ListExtensions#getModifiedCollections(Collection, Collection)}
	 */
	@Test
	public void testGetModifiedLists()
	{

		final String previous[] = { "1", "2", "3", "4", "5", "6", "7" };
		final String next[] = { "2", "5", "6", "7", "8", "9" };

		final String expectedadded[] = { "8", "9" };
		final String expectedremoved[] = { "1", "3", "4" };

		final List<String> previousList = ListFactory.newArrayList(Arrays.asList(previous));
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
		final List<String> toSearch = ListFactory.newArrayList();
		final List<String> search = ListFactory.newArrayList();
		final List<String> atLeastOneSameObject = ListFactory.newArrayList();
		toSearch.add("Anton");
		toSearch.add("Berta");
		toSearch.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add("Franz");
		atLeastOneSameObject.add("Anton");
		atLeastOneSameObject.add("Dora");
		atLeastOneSameObject.add("Emil");
		final List<String> expectedElements = ListFactory.newArrayList();
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
		assertTrue("List should be empty.", empty.isEmpty());
	}

	/**
	 * Test method for {@link ListExtensions#hasNext(List, Object)}
	 */
	@Test(enabled = true)
	public void testHasNext()
	{
		boolean expected;
		boolean actual;
		List<String> search;
		String emil = "Emil";
		String anton = "Anton";
		search = ListFactory.newArrayList();
		search.add("Caesar");
		search.add("Dora");
		search.add(emil);
		search.add(anton);

		expected = false;
		actual = ListExtensions.hasNext(search, null);
		assertEquals(expected, actual);

		expected = true;
		actual = ListExtensions.hasNext(search, emil);
		assertEquals(expected, actual);

		expected = false;
		actual = ListExtensions.hasNext(search, anton);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ListExtensions#hasPrevious(List, Object)}
	 */
	@Test(enabled = true)
	public void testHasPrevious()
	{
		boolean expected;
		boolean actual;
		List<String> search;

		String caeser = "Caesar";
		String dora = "Dora";
		String emil = "Emil";
		String anton = "Anton";
		search = ListFactory.newArrayList();
		search.add(caeser);
		search.add(dora);
		search.add(emil);
		search.add(anton);

		expected = false;
		actual = ListExtensions.hasPrevious(search, null);
		assertEquals(expected, actual);

		expected = false;
		actual = ListExtensions.hasPrevious(search, "foo");
		assertEquals(expected, actual);

		expected = false;
		actual = ListExtensions.hasPrevious(search, caeser);
		assertEquals(expected, actual);

		expected = true;
		actual = ListExtensions.hasPrevious(search, dora);
		assertEquals(expected, actual);

		expected = true;
		actual = ListExtensions.hasPrevious(search, emil);
		assertEquals(expected, actual);

		expected = true;
		actual = ListExtensions.hasPrevious(search, anton);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ListExtensions#isEqualListOfArrays(List, List)}.
	 */
	@Test
	public void testIsEqualListOfArrays()
	{
		boolean actual;
		boolean expected;
		String[] lineOne;
		String[] lineTwo;
		String[] lineThree;
		String[] lineFour;
		String[] lineFive;
		String[] lineSix;
		List<String[]> one;
		List<String[]> other;
		lineOne = ArrayFactory.newArray("1", "23", "3");
		lineTwo = ArrayFactory.newArray("4", "25", "9");
		lineThree = ArrayFactory.newArray("6", "21", "8");
		lineFour = ArrayFactory.newArray("1", "23", "3");
		lineFive = ArrayFactory.newArray("4", "25", "9");
		lineSix = ArrayFactory.newArray("6", "21", "8");
		//
		one = ListFactory.newArrayList(lineOne, lineTwo, lineThree);
		other = ListFactory.newArrayList(lineFour, lineFive, lineSix);
		actual = ListExtensions.isEqualListOfArrays(one, other);
		expected = true;
		assertEquals(expected, actual);
		//
		one = null;
		other = null;
		actual = ListExtensions.isEqualListOfArrays(one, other);
		expected = true;
		assertEquals(expected, actual);
		//
		one = null;
		other = ListFactory.newArrayList(lineFour, lineFive, lineSix);
		actual = ListExtensions.isEqualListOfArrays(one, other);
		expected = false;
		assertEquals(expected, actual);
		//
		one = ListFactory.newArrayList(lineOne, lineTwo, lineThree);
		other = null;
		actual = ListExtensions.isEqualListOfArrays(one, other);
		expected = false;
		assertEquals(expected, actual);
		//
		one = ListFactory.newArrayList(lineOne, lineTwo);
		other = ListFactory.newArrayList(lineFour, lineFive, lineSix);
		actual = ListExtensions.isEqualListOfArrays(one, other);
		expected = false;
		assertEquals(expected, actual);
		//
		lineThree = ArrayFactory.newArray("6", "21", "777");
		one = ListFactory.newArrayList(lineOne, lineTwo, lineThree);
		other = ListFactory.newArrayList(lineFour, lineFive, lineSix);
		actual = ListExtensions.isEqualListOfArrays(one, other);
		expected = false;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ListExtensions#isFirst(List, Object)}
	 */
	@Test
	public void testIsFirst()
	{
		String expected;
		expected = "Leonidas";
		final List<String> search = ListFactory.newArrayList();
		search.add(expected);
		search.add("Berta");
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add("Anton");
		final boolean actual = ListExtensions.isFirst(search, expected);
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
		final List<String> search = ListFactory.newArrayList();
		search.add("Anton");
		search.add("Berta");
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add(name);
		actual = ListExtensions.isLast(search, name);
		expected = true;
		assertEquals(expected, actual);

		actual = ListExtensions.isLast(search, "Foo");
		expected = false;
		assertEquals(expected, actual);

		actual = ListExtensions.isLast(ListFactory.newArrayList(), "Foo");
		expected = false;
		assertEquals(expected, actual);
	}

	/**
	 * Test the method {@link ListExtensions#toArray(List)}
	 */
	@Test
	public void testListToArray()
	{
		Integer[] actual;
		Integer[] expected;
		List<Integer> numbers;
		// new scenario...
		numbers = ListFactory.newArrayList(1, 2, 3);
		actual = ListExtensions.toArray(numbers);
		expected = ArrayFactory.newArray(1, 2, 3);
		assertTrue(Arrays.equals(actual, expected));
	}

	/**
	 * Test the method {@link ListExtensions#toArray(List)} that throws an
	 * <code>IllegalArgumentException</code>
	 */
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testListToArrayWithException()
	{
		ListExtensions.toArray(ListFactory.newArrayList());
	}

	/**
	 * Test method for {@link ListExtensions#rearrange(List, Object, int)}
	 */
	@Test
	public void testRearrange()
	{
		List<String> search;
		List<String> rearrangedList;
		int index;

		search = ListFactory.newArrayList();
		search.add("Leonidas");
		search.add("Berta");
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add("Anton");

		rearrangedList = ListExtensions.rearrange("Anton", search, 0);
		index = rearrangedList.indexOf("Anton");
		assertEquals(index, 0);

		rearrangedList = ListExtensions.rearrange("Anton", search, 2);
		index = rearrangedList.indexOf("Anton");
		assertEquals(index, 2);

		rearrangedList = ListExtensions.rearrange("Anton", search, 5);
		index = rearrangedList.indexOf("Anton");
		assertEquals(index, 5);

		rearrangedList = ListExtensions.rearrange("Anton", search, 6);
		index = rearrangedList.indexOf("Anton");
		assertEquals(index, 5);
	}

	/**
	 * Test method for {@link ListExtensions#removeFirst(List)}
	 */
	@Test
	public void testRemoveFirst()
	{
		String expected;
		String actual;
		String removed;
		List<String> search;
		expected = "Leonidas";
		removed = "Berta";
		search = ListFactory.newArrayList();
		search.add(removed);
		search.add(expected);
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add("Anton");
		actual = ListExtensions.removeFirst(search);
		assertTrue("", removed.equals(actual));

		actual = ListExtensions.getFirst(search);
		assertEquals(expected, actual);

		search.clear();

		actual = ListExtensions.removeFirst(search);
		expected = null;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ListExtensions#removeLast(List)}
	 */
	@Test
	public void testRemoveLast()
	{
		String expected;
		String actual;
		String removed;
		List<String> search;
		expected = "Leonidas";
		removed = "Berta";
		search = ListFactory.newArrayList();

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

		actual = ListExtensions.removeLast(search);
		expected = null;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ListExtensions#removeLastValues(List, int)}
	 */
	@Test
	public void testRemoveLastValues()
	{
		String expected;
		String removed;
		List<String> search;
		List<String> removedLastValues;

		expected = "Leonidas";
		removed = "Berta";
		search = ListFactory.newArrayList();

		search.add("Anton");
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add(expected);
		search.add(removed);
		removedLastValues = ListExtensions.removeLastValues(search, 2);
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
		final List<String> search = ListFactory.newArrayList();
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
		final List<String> search = ListFactory.newArrayList();
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
		final List<String> source = ListFactory.newArrayList();
		final List<String> destination = ListFactory.newArrayList();
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
		ListExtensions.shuffle(source, destination, selectedElements);

		final boolean actual = destination.size() == 2;
		assertTrue(actual);
		assertTrue(destination.contains(berta));
		assertTrue(destination.contains(caesar));
		// shuffle the elements back
		final int[] newSelectedElements = { 0, 1 };
		ListExtensions.shuffle(source, destination, newSelectedElements);

	}

	/**
	 * Test the method {@link ListExtensions#sortByProperty(List, String, boolean)}
	 */
	@Test
	public void testSortByProperty()
	{
		final List<Person> persons = ListFactory.newArrayList();
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
	 * Test the method {@link ListExtensions#splitListToParts(List, int)}
	 */
	@Test
	@SuppressWarnings({ "rawtypes" })
	public void testsplitToPartsInList()
	{
		final List<Integer> list = ListFactory.newArrayList();
		for (int i = 0; i < 999; i++)
		{
			list.add(i);
		}
		final List al = ListExtensions.splitToParts(list, 50);
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
		final List<String> arrayList = ListFactory.newArrayList("1", "2");
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
		Set<String> set = SetFactory.newHashSet();
		assertTrue(set.size() == 0);
		set = SetFactory.newHashSet("foo", "bar", "foo");
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
	 * Test method for {@link ListExtensions} with {@link BeanTester}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ListExtensions.class);
	}

}
