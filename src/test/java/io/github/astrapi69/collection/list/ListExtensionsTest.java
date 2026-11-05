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
package io.github.astrapi69.collection.list;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.modification.ModifiedCollections;
import io.github.astrapi69.comparator.factory.ComparatorFactory;
import io.github.astrapi69.test.object.Person;
import io.github.astrapi69.test.object.enumeration.Gender;

/**
 * The unit test class for the class {@link ListExtensions}
 *
 * @author Asterios Raptis
 */
public class ListExtensionsTest
{

	/**
	 * The elements of a column of a table, separated by a space
	 *
	 * @param column
	 *            the column, empty for no elements
	 * @return the elements in a new {@link ArrayList}, which the test may change
	 */
	private static List<String> elements(final String column)
	{
		return column.isEmpty()
			? new ArrayList<>()
			: Arrays.stream(column.split(" ")).collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * The numbers of a column of a table, separated by a space
	 *
	 * @param column
	 *            the column, empty for no numbers
	 * @return the numbers in a new {@link ArrayList}, which the test may change
	 */
	private static List<Integer> numbers(final String column)
	{
		return elements(column).stream().map(Integer::valueOf)
			.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * Test method for {@link ListExtensions#subList(List, int, int)} on 1, 2, 3, 4, 5: a view of
	 * the range, with a negative start taken as 0 and an end beyond the size as the size, and a
	 * modifiable empty list of its own, not a view, where the range holds no element
	 *
	 * @param description
	 *            the name of the case
	 * @param fromIndex
	 *            the start index, inclusive
	 * @param toIndex
	 *            the end index, exclusive
	 * @param expected
	 *            the elements of the result, separated by a space
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "1 to 4: 2, 3, 4, a view | 1 | 4 | 2 3 4",
			"0 to 5, the full range: all of them, a view | 0 | 5 | 1 2 3 4 5",
			"-2 to 2, a negative start: 1, 2, from 0 on | -2 | 2 | 1 2",
			"3 to 9, an end beyond the size: 4, 5, to the size | 3 | 9 | 4 5",
			"5 to 7, the start at the size: an empty list of its own | 5 | 7 | ''",
			"-1 to 0, the end at 0: an empty list of its own | -1 | 0 | ''",
			"3 to 3, the start equal to the end: an empty list of its own | 3 | 3 | ''",
			"4 to 2, the start greater than the end: an empty list of its own | 4 | 2 | ''" }, delimiter = '|')
	public void testSubList(final String description, final int fromIndex, final int toIndex,
		final String expected)
	{
		final List<Integer> list = numbers("1 2 3 4 5");
		final List<Integer> actual = ListExtensions.subList(list, fromIndex, toIndex);
		assertEquals(numbers(expected), actual);
		final List<Integer> afterwards = numbers("1 2 3 4 5");
		if (actual.isEmpty())
		{
			actual.add(42);
			assertEquals(List.of(42), actual);
		}
		else
		{
			actual.clear();
			afterwards.removeAll(numbers(expected));
		}
		assertEquals(afterwards, list);
	}

	/**
	 * Test method for {@link ListExtensions#getFirst(List)}, {@link ListExtensions#getLast(List)},
	 * {@link ListExtensions#getFirstElement(List)} and {@link ListExtensions#getLastElement(List)}:
	 * the first and the last element, null or an empty {@link Optional} for an empty list, and the
	 * list does not change
	 *
	 * @param description
	 *            the name of the case
	 * @param elements
	 *            the elements of the list, separated by a space
	 * @param first
	 *            the first element, null if there is none
	 * @param last
	 *            the last element, null if there is none
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"Leonidas, Berta, Caesar, Dora, Emil, Anton: Leonidas first, Anton last | Leonidas Berta Caesar Dora Emil Anton | Leonidas | Anton",
			"Anton, Berta, Caesar, Dora, Emil, Leonidas: Anton first, Leonidas last | Anton Berta Caesar Dora Emil Leonidas | Anton | Leonidas",
			"a single element a: first and last | a | a | a",
			"an empty list: none | '' | none | none" }, delimiter = '|', nullValues = "none")
	public void testGetFirstAndGetLast(final String description, final String elements,
		final String first, final String last)
	{
		final List<String> list = elements(elements);
		assertEquals(first, ListExtensions.getFirst(list), "getFirst");
		assertEquals(last, ListExtensions.getLast(list), "getLast");
		assertEquals(Optional.ofNullable(first), ListExtensions.getFirstElement(list),
			"getFirstElement");
		assertEquals(Optional.ofNullable(last), ListExtensions.getLastElement(list),
			"getLastElement");
		assertEquals(elements(elements), list, "the list changed");
	}

	/**
	 * Test method for {@link ListExtensions#removeFirst(List)},
	 * {@link ListExtensions#removeFirstElement(List)}, {@link ListExtensions#removeLast(List)} and
	 * {@link ListExtensions#removeLastElement(List)}, each on a list of its own: the removed
	 * element, null or an empty {@link Optional} for an empty list, and the list without it
	 *
	 * @param description
	 *            the name of the case
	 * @param elements
	 *            the elements of the list, separated by a space
	 * @param first
	 *            the first element, null if there is none
	 * @param last
	 *            the last element, null if there is none
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"Berta, Leonidas, Caesar, Dora, Emil, Anton: Berta first, Anton last | Berta Leonidas Caesar Dora Emil Anton | Berta | Anton",
			"Anton, Caesar, Dora, Emil, Leonidas, Berta: Anton first, Berta last | Anton Caesar Dora Emil Leonidas Berta | Anton | Berta",
			"a single element a: first and last, the list is empty then | a | a | a",
			"an empty list: none, the list stays empty | '' | none | none" }, delimiter = '|', nullValues = "none")
	public void testRemoveFirstAndRemoveLast(final String description, final String elements,
		final String first, final String last)
	{
		final List<String> given = elements(elements);
		final List<String> withoutFirst = given.isEmpty()
			? List.of()
			: given.subList(1, given.size());
		final List<String> withoutLast = given.isEmpty()
			? List.of()
			: given.subList(0, given.size() - 1);
		List<String> list = elements(elements);
		assertEquals(first, ListExtensions.removeFirst(list), "removeFirst");
		assertEquals(withoutFirst, list, "the list after removeFirst");
		list = elements(elements);
		assertEquals(Optional.ofNullable(first), ListExtensions.removeFirstElement(list),
			"removeFirstElement");
		assertEquals(withoutFirst, list, "the list after removeFirstElement");
		list = elements(elements);
		assertEquals(last, ListExtensions.removeLast(list), "removeLast");
		assertEquals(withoutLast, list, "the list after removeLast");
		list = elements(elements);
		assertEquals(Optional.ofNullable(last), ListExtensions.removeLastElement(list),
			"removeLastElement");
		assertEquals(withoutLast, list, "the list after removeLastElement");
	}

	/**
	 * Test the method {@link ListExtensions#clone(List)}: a new list with the elements in their
	 * order, which changes without the given list
	 *
	 * @param description
	 *            the name of the case
	 * @param elements
	 *            the elements of the list, separated by a space
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "Dora, Emil, Franz: a new list of them | Dora Emil Franz",
			"no elements: a new empty list | ''" }, delimiter = '|')
	public void testClone(final String description, final String elements)
	{
		final List<String> list = elements(elements);
		final List<String> actual = ListExtensions.clone(list);
		assertEquals(list, actual);
		assertNotSame(list, actual);
		actual.add("Gustav");
		assertEquals(elements(elements), list, "the given list changed with its clone");
	}

	/**
	 * Test the method {@link ListExtensions#containAtLeastOneObject(List, List)}: true if one
	 * element of the first list is in the second one, otherwise false
	 *
	 * @param description
	 *            the name of the case
	 * @param toSearch
	 *            the elements of the first list, separated by a space
	 * @param search
	 *            the elements of the second list, separated by a space
	 * @param expected
	 *            the answer
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"Anton, Berta, Caesar and Dora, Emil, Franz, none in common: false | Anton Berta Caesar | Dora Emil Franz | false",
			"Anton, Berta, Caesar and Anton, Dora, the first in common: true | Anton Berta Caesar | Anton Dora | true",
			"Dora, Emil, Franz and Anton, Dora: true | Dora Emil Franz | Anton Dora | true",
			"Anton, Berta, Caesar and Caesar, the last in common: true | Anton Berta Caesar | Caesar | true",
			"an empty list and Anton: false | '' | Anton | false",
			"Anton and an empty list: false | Anton | '' | false" }, delimiter = '|')
	public void testContainAtLeastOneObject(final String description, final String toSearch,
		final String search, final boolean expected)
	{
		assertEquals(expected,
			ListExtensions.containAtLeastOneObject(elements(toSearch), elements(search)));
	}

	/**
	 * The numbers 1 to the given count and the combinations of the given size of them
	 *
	 * @param description
	 *            the name of the case
	 * @param count
	 *            the count of the numbers, 1 to count
	 * @param size
	 *            the size of the combinations
	 * @param expected
	 *            the combinations, each in the order of the numbers
	 */
	record CombinationCase(String description, int count, int size, List<List<Integer>> expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * All combinations of three of the numbers 1 to n
	 *
	 * @param n
	 *            the greatest number
	 * @return the combinations, each in ascending order
	 */
	private static List<List<Integer>> combinationsOfThree(final int n)
	{
		final List<List<Integer>> combinations = new ArrayList<>();
		for (int a = 1; a <= n; a++)
		{
			for (int b = a + 1; b <= n; b++)
			{
				for (int c = b + 1; c <= n; c++)
				{
					combinations.add(List.of(a, b, c));
				}
			}
		}
		return combinations;
	}

	/**
	 * The cases of both {@link ListExtensions#getAllCombinations(List, int)} and
	 * {@link ListExtensions#getCombinations(List, int)}
	 *
	 * @return the cases
	 */
	static Stream<CombinationCase> combinations()
	{
		return Stream.of(
			new CombinationCase("1 to 4 in combinations of 3: 4 of them", 4, 3,
				List.of(List.of(1, 2, 3), List.of(1, 2, 4), List.of(1, 3, 4), List.of(2, 3, 4))),
			new CombinationCase("1 to 5 in combinations of 3: 10 of them", 5, 3,
				List.of(List.of(1, 2, 3), List.of(1, 2, 4), List.of(1, 2, 5), List.of(1, 3, 4),
					List.of(1, 3, 5), List.of(1, 4, 5), List.of(2, 3, 4), List.of(2, 3, 5),
					List.of(2, 4, 5), List.of(3, 4, 5))),
			new CombinationCase("1 to 7 in combinations of 6: 7 of them", 7, 6,
				List.of(List.of(1, 2, 3, 4, 5, 6), List.of(1, 2, 3, 4, 5, 7),
					List.of(1, 2, 3, 4, 6, 7), List.of(1, 2, 3, 5, 6, 7), List.of(1, 2, 4, 5, 6, 7),
					List.of(1, 3, 4, 5, 6, 7), List.of(2, 3, 4, 5, 6, 7))),
			new CombinationCase("1 to 11 in combinations of 3: 165 of them", 11, 3,
				combinationsOfThree(11)),
			new CombinationCase("1 to 3 in combinations of 0: one, the empty combination", 3, 0,
				List.of(List.of())),
			new CombinationCase("1 to 3 in combinations of 4, more than the numbers: none", 3, 4,
				List.of()),
			new CombinationCase("no numbers in combinations of 1: none", 0, 1, List.of()));
	}

	/**
	 * The cases of {@link #combinations()} and the one of all 32 of 32 numbers: the search drops
	 * every branch that has too few values left to fill the combination, which leaves one path;
	 * without that it would run through 2^32 branches.
	 * {@link ListExtensions#getCombinations(List, int)} does not drop them and has no such case
	 *
	 * @return the cases
	 */
	static Stream<CombinationCase> allCombinations()
	{
		return Stream.concat(combinations(),
			Stream.of(new CombinationCase(
				"1 to 32 in combinations of 32: one, all of them in their order", 32, 32,
				List.of(IntStream.rangeClosed(1, 32).boxed().toList()))));
	}

	/**
	 * Test the method {@link ListExtensions#getAllCombinations(List, int)}: each combination once,
	 * in any order of the combinations; the values of a combination in the order of the given list
	 *
	 * @param combination
	 *            the numbers, the size and the combinations
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("allCombinations")
	public void testGetAllCombinations(final CombinationCase combination)
	{
		final List<Integer> values = IntStream.rangeClosed(1, combination.count()).boxed()
			.collect(Collectors.toCollection(ArrayList::new));
		final List<List<Integer>> actual = ListExtensions.getAllCombinations(values,
			combination.size());
		assertEquals(combination.expected().size(), actual.size());
		assertEquals(new HashSet<>(combination.expected()), new HashSet<>(actual));
	}

	/**
	 * Test the method {@link ListExtensions#getCombinations(List, int)}: each combination once, in
	 * any order of the combinations. Its Javadoc promises no order of the values within a
	 * combination, so each combination is sorted before the comparison
	 *
	 * @param combination
	 *            the numbers, the size and the combinations
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("combinations")
	public void testGetCombinations(final CombinationCase combination)
	{
		final List<Integer> values = IntStream.rangeClosed(1, combination.count()).boxed()
			.collect(Collectors.toCollection(ArrayList::new));
		final List<List<Integer>> actual = ListExtensions
			.getCombinations(values, combination.size()).stream()
			.map(found -> found.stream().sorted().toList()).toList();
		assertEquals(combination.expected().size(), actual.size());
		assertEquals(new HashSet<>(combination.expected()), new HashSet<>(actual));
	}

	/**
	 * Test method for
	 * {@link ListExtensions#getModifiedCollections(java.util.Collection, java.util.Collection)}:
	 * the elements of the next collection that the previous one does not hold are added, those of
	 * the previous one that the next one does not hold are removed, each in the order of its
	 * collection
	 *
	 * @param description
	 *            the name of the case
	 * @param previous
	 *            the elements of the previous collection, separated by a space
	 * @param next
	 *            the elements of the next collection, separated by a space
	 * @param added
	 *            the added elements, separated by a space
	 * @param removed
	 *            the removed elements, separated by a space
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"1 to 7, then 2, 5, 6, 7, 8, 9: 8, 9 added, 1, 3, 4 removed | 1 2 3 4 5 6 7 | 2 5 6 7 8 9 | 8 9 | 1 3 4",
			"1, 2, then 1, 2: none added, none removed | 1 2 | 1 2 | '' | ''",
			"nothing, then 1, 2: both added | '' | 1 2 | 1 2 | ''",
			"1, 2, then nothing: both removed | 1 2 | '' | '' | 1 2" }, delimiter = '|')
	public void testGetModifiedCollections(final String description, final String previous,
		final String next, final String added, final String removed)
	{
		final ModifiedCollections<String> actual = ListExtensions
			.getModifiedCollections(elements(previous), elements(next));
		assertEquals(elements(added), actual.getAddedElements());
		assertEquals(elements(removed), actual.getRemovedElements());
	}

	/**
	 * Test method for {@link ListExtensions#hasNext(List, Object)},
	 * {@link ListExtensions#hasPrevious(List, Object)},
	 * {@link ListExtensions#getNextElement(List, Object)} and
	 * {@link ListExtensions#getPreviousElement(List, Object)}: the neighbours of the given element,
	 * none for the last and for the first, and none for an element the list does not hold
	 *
	 * @param description
	 *            the name of the case
	 * @param elements
	 *            the elements of the list, separated by a space
	 * @param element
	 *            the given element, may be null
	 * @param next
	 *            the next element, null if there is none
	 * @param previous
	 *            the previous element, null if there is none
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"Caesar, the first: next Dora, no previous | Caesar Dora Emil Anton | Caesar | Dora | none",
			"Dora: next Emil, previous Caesar | Caesar Dora Emil Anton | Dora | Emil | Caesar",
			"Emil: next Anton, previous Dora | Caesar Dora Emil Anton | Emil | Anton | Dora",
			"Anton, the last: no next, previous Emil | Caesar Dora Emil Anton | Anton | none | Emil",
			"foo, not in the list: none | Caesar Dora Emil Anton | foo | none | none",
			"null, not in the list: none | Caesar Dora Emil Anton | null | none | none",
			"a, the only element: none | a | a | none | none",
			"a in an empty list: none | '' | a | none | none" }, delimiter = '|', nullValues = {
					"null", "none" })
	public void testNeighbours(final String description, final String elements,
		final String element, final String next, final String previous)
	{
		final List<String> list = elements(elements);
		assertEquals(next != null, ListExtensions.hasNext(list, element), "hasNext");
		assertEquals(previous != null, ListExtensions.hasPrevious(list, element), "hasPrevious");
		assertEquals(Optional.ofNullable(next), ListExtensions.getNextElement(list, element),
			"getNextElement");
		assertEquals(Optional.ofNullable(previous),
			ListExtensions.getPreviousElement(list, element), "getPreviousElement");
	}

	/**
	 * Test method for {@link ListExtensions#getSameElementsFromLists(List, List)}: a new list with
	 * the elements of the first list that the second one holds, in the order of the first; the
	 * method decorates retainAll, so the first list keeps only them, and the second does not change
	 *
	 * @param description
	 *            the name of the case
	 * @param toSearch
	 *            the elements of the first list, separated by a space
	 * @param search
	 *            the elements of the second list, separated by a space
	 * @param expected
	 *            the elements both hold, separated by a space
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"Dora, Emil, Franz and Anton, Dora, Emil: Dora, Emil | Dora Emil Franz | Anton Dora Emil | Dora Emil",
			"Anton, Berta, Caesar and Dora, Emil, Franz: none | Anton Berta Caesar | Dora Emil Franz | ''",
			"Anton, Berta and Berta, Anton: both, in the order of the first | Anton Berta | Berta Anton | Anton Berta" }, delimiter = '|')
	public void testGetSameElementsFromLists(final String description, final String toSearch,
		final String search, final String expected)
	{
		final List<String> first = elements(toSearch);
		final List<String> second = elements(search);
		final List<String> actual = ListExtensions.getSameElementsFromLists(first, second);
		assertEquals(elements(expected), actual);
		assertNotSame(first, actual);
		assertEquals(elements(expected), first, "the first list, after retainAll");
		assertEquals(elements(search), second, "the second list changed");
	}

	/**
	 * Two lists of arrays and whether {@link ListExtensions#isEqualListOfArrays(List, List)} calls
	 * them equal
	 *
	 * @param description
	 *            the name of the case
	 * @param one
	 *            makes the one list, with new arrays on each call, may make null
	 * @param other
	 *            makes the other list, with new arrays on each call, may make null
	 * @param equal
	 *            the answer
	 */
	record ListsOfArraysCase(String description, Supplier<List<String[]>> one,
		Supplier<List<String[]>> other, boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link ListExtensions#isEqualListOfArrays(List, List)}: equal if both are null,
	 * or if they hold arrays of the same content at the same indexes; each call of a supplier makes
	 * new arrays, so the arrays of two lists are equal in content only
	 *
	 * @return the cases
	 */
	static Stream<ListsOfArraysCase> listsOfArrays()
	{
		final Supplier<List<String[]>> threeLines = () -> new ArrayList<>(
			List.of(new String[] { "1", "23", "3" }, new String[] { "4", "25", "9" },
				new String[] { "6", "21", "8" }));
		return Stream.of(
			new ListsOfArraysCase("three lines and three other arrays of the same content: equal",
				threeLines, threeLines, true),
			new ListsOfArraysCase("null and null: equal", () -> null, () -> null, true),
			new ListsOfArraysCase("null and three lines: not equal", () -> null, threeLines, false),
			new ListsOfArraysCase("three lines and null: not equal", threeLines, () -> null, false),
			new ListsOfArraysCase("two lines and three lines, another size: not equal",
				() -> new ArrayList<>(
					List.of(new String[] { "1", "23", "3" }, new String[] { "4", "25", "9" })),
				threeLines, false),
			new ListsOfArraysCase("three lines, the last of other content: not equal",
				() -> new ArrayList<>(List.of(new String[] { "1", "23", "3" },
					new String[] { "4", "25", "9" }, new String[] { "6", "21", "777" })),
				threeLines, false),
			new ListsOfArraysCase("the three lines in another order: not equal, as in a list",
				() -> new ArrayList<>(List.of(new String[] { "6", "21", "8" },
					new String[] { "1", "23", "3" }, new String[] { "4", "25", "9" })),
				threeLines, false),
			new ListsOfArraysCase("two empty lists: equal", ArrayList::new, ArrayList::new, true));
	}

	/**
	 * Test method for {@link ListExtensions#isEqualListOfArrays(List, List)}: the answer, the same
	 * in both directions
	 *
	 * @param lists
	 *            the two lists and the answer
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("listsOfArrays")
	public void testIsEqualListOfArrays(final ListsOfArraysCase lists)
	{
		assertEquals(lists.equal(),
			ListExtensions.isEqualListOfArrays(lists.one().get(), lists.other().get()));
		assertEquals(lists.equal(),
			ListExtensions.isEqualListOfArrays(lists.other().get(), lists.one().get()),
			"the other way round");
	}

	/**
	 * Test method for {@link ListExtensions#isFirst(List, Object)} and
	 * {@link ListExtensions#isLast(List, Object)}
	 *
	 * @param description
	 *            the name of the case
	 * @param elements
	 *            the elements of the list, separated by a space
	 * @param element
	 *            the given element
	 * @param first
	 *            the answer of isFirst
	 * @param last
	 *            the answer of isLast
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"Leonidas, the first of Leonidas, Berta, Caesar, Dora, Emil, Anton: first | Leonidas Berta Caesar Dora Emil Anton | Leonidas | true | false",
			"Berta, the second of Leonidas, Berta, Caesar: neither | Leonidas Berta Caesar | Berta | false | false",
			"Foo, not in Leonidas, Berta, Caesar: neither | Leonidas Berta Caesar | Foo | false | false",
			"Leonidas, the last of Anton, Berta, Caesar, Dora, Emil, Leonidas: last | Anton Berta Caesar Dora Emil Leonidas | Leonidas | false | true",
			"Foo, not in Anton, Berta, Caesar, Dora, Emil, Leonidas: neither | Anton Berta Caesar Dora Emil Leonidas | Foo | false | false",
			"Foo in an empty list: neither | '' | Foo | false | false",
			"a, the only element: first and last | a | a | true | true",
			"a in a, b, a: first, and last too | a b a | a | true | true" }, delimiter = '|')
	public void testIsFirstAndIsLast(final String description, final String elements,
		final String element, final boolean first, final boolean last)
	{
		final List<String> list = elements(elements);
		assertEquals(first, ListExtensions.isFirst(list, element), "isFirst");
		assertEquals(last, ListExtensions.isLast(list, element), "isLast");
	}

	/**
	 * Test method for {@link ListExtensions#isAfter(List, Object, Object)} and
	 * {@link ListExtensions#isBefore(List, Object, Object)} in Content, Donate, Diagnostic,
	 * License, Info: whether the element to check stands after or before the element; neither if
	 * one of them is not in the list, and an element is neither after nor before itself
	 *
	 * @param description
	 *            the name of the case
	 * @param element
	 *            the element
	 * @param elementToCheck
	 *            the element to check
	 * @param after
	 *            the answer of isAfter
	 * @param before
	 *            the answer of isBefore
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"Diagnostic after Content: after, not before | Content | Diagnostic | true | false",
			"Content before Diagnostic: before, not after | Diagnostic | Content | false | true",
			"Info, the last, after Content, the first: after | Content | Info | true | false",
			"Diagnostic and foo, not in the list: neither | foo | Diagnostic | false | false",
			"foo, not in the list, and Diagnostic: neither | Diagnostic | foo | false | false",
			"Donate and Donate, the same element: neither | Donate | Donate | false | false" }, delimiter = '|')
	public void testIsAfterAndIsBefore(final String description, final String element,
		final String elementToCheck, final boolean after, final boolean before)
	{
		final List<String> list = elements("Content Donate Diagnostic License Info");
		assertEquals(after, ListExtensions.isAfter(list, element, elementToCheck), "isAfter");
		assertEquals(before, ListExtensions.isBefore(list, element, elementToCheck), "isBefore");
	}

	/**
	 * Test the methods {@link ListExtensions#isAfter(List, Object, Object)} and
	 * {@link ListExtensions#isBefore(List, Object, Object)} with null for the list, the element or
	 * the element to check: each throws a {@link NullPointerException}
	 *
	 * @param description
	 *            the name of the case
	 * @param elements
	 *            the elements of the list, separated by a space, or null for no list
	 * @param element
	 *            the element, may be null
	 * @param elementToCheck
	 *            the element to check, may be null
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "a null list | null | Donate | Content",
			"a null element | Content Donate Diagnostic | null | Content",
			"a null element to check | Content Donate Diagnostic | Donate | null" }, delimiter = '|', nullValues = "null")
	public void testIsAfterAndIsBeforeWithNull(final String description, final String elements,
		final String element, final String elementToCheck)
	{
		final List<String> list = elements == null ? null : elements(elements);
		assertThrows(NullPointerException.class,
			() -> ListExtensions.isAfter(list, element, elementToCheck), "isAfter");
		assertThrows(NullPointerException.class,
			() -> ListExtensions.isBefore(list, element, elementToCheck), "isBefore");
	}

	/**
	 * A list and the array {@link ListExtensions#toArray(List)} makes of it
	 *
	 * @param description
	 *            the name of the case
	 * @param list
	 *            makes the given list
	 * @param arrayClass
	 *            the class of the returned array
	 * @param expected
	 *            the elements of the returned array
	 */
	record ListToArrayCase(String description, Supplier<List<?>> list, Class<?> arrayClass,
		Object[] expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link ListExtensions#toArray(List)}, the examples of its Javadoc: an array of
	 * the most specific common superclass of the non-null elements (#64)
	 *
	 * @return the cases
	 */
	static Stream<ListToArrayCase> listsToArrays()
	{
		return Stream.of(
			new ListToArrayCase("1, 2, 3: an Integer[]", () -> new ArrayList<>(List.of(1, 2, 3)),
				Integer[].class, new Integer[] { 1, 2, 3 }),
			new ListToArrayCase("null, a: a String[] of null, a (#64)",
				() -> new ArrayList<>(Arrays.asList(null, "a")), String[].class,
				new String[] { null, "a" }),
			new ListToArrayCase("1, 2.5: a Number[] (#64)",
				() -> new ArrayList<Number>(Arrays.asList(1, 2.5)), Number[].class,
				new Number[] { 1, 2.5 }),
			new ListToArrayCase("2.5, 1: a Number[] in their order (#64)",
				() -> new ArrayList<Number>(Arrays.asList(2.5, 1)), Number[].class,
				new Number[] { 2.5, 1 }),
			new ListToArrayCase("a, 1: an Object[] (#64)",
				() -> new ArrayList<Object>(Arrays.asList("a", 1)), Object[].class,
				new Object[] { "a", 1 }),
			new ListToArrayCase("null, null: an Object[] of two nulls (#64)",
				() -> new ArrayList<String>(Arrays.asList(null, null)), Object[].class,
				new Object[] { null, null }));
	}

	/**
	 * Test the method {@link ListExtensions#toArray(List)}: the elements in the order of the list,
	 * in an array of the class its Javadoc names
	 *
	 * @param listToArray
	 *            the given list and the returned array
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("listsToArrays")
	public void testListToArray(final ListToArrayCase listToArray)
	{
		final Object[] actual = ListExtensions.toArray(listToArray.list().get());
		assertEquals(listToArray.arrayClass(), actual.getClass());
		assertArrayEquals(listToArray.expected(), actual);
	}

	/**
	 * Test the method {@link ListExtensions#toArray(List)} with null and with an empty list: an
	 * {@link IllegalArgumentException}, as its Javadoc says
	 *
	 * @param list
	 *            null or an empty list
	 */
	@ParameterizedTest(name = "{0}: an IllegalArgumentException")
	@NullAndEmptySource
	public void testListToArrayWithNullOrAnEmptyList(final List<String> list)
	{
		assertThrows(IllegalArgumentException.class, () -> ListExtensions.toArray(list));
	}

	/**
	 * Test the method {@link ListExtensions#toArray(Object...)}: a new array of the same class with
	 * the given elements, also when no elements are given, and null for null (#62)
	 *
	 * @param description
	 *            the name of the case
	 * @param elements
	 *            the given elements, separated by a space, or null for null
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "1, 2, 3: a new array of them | 1 2 3",
			"a, b: a new array, not the given one (#62) | a b",
			"no elements: a new empty array (#62) | ''",
			"null: null, as before #62 | null" }, delimiter = '|', nullValues = "null")
	public void testToArray(final String description, final String elements)
	{
		final String[] given = elements == null ? null : elements(elements).toArray(new String[0]);
		final String[] actual = ListExtensions.toArray(given);
		assertArrayEquals(given, actual);
		if (given != null)
		{
			assertNotSame(given, actual);
			assertEquals(String[].class, actual.getClass());
		}
	}

	/**
	 * Test the method {@link ListExtensions#toIndexMap(List)}: each element under its index, the
	 * keys in the order of the indexes
	 *
	 * @param description
	 *            the name of the case
	 * @param elements
	 *            the elements of the list, separated by a space
	 * @param numbers
	 *            true if the elements are numbers, {@link Integer} objects, false if they are
	 *            strings
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "the numbers 1, 2, 3: 0=1, 1=2, 2=3 | 1 2 3 | true",
			"the strings 1, 2, 3: 0=1, 1=2, 2=3 | 1 2 3 | false",
			"c, a, b: 0=c, 1=a, 2=b, in the order of the list | c a b | false",
			"a single element a: 0=a | a | false" }, delimiter = '|')
	public void testToIndexMap(final String description, final String elements,
		final boolean numbers)
	{
		final List<?> list = numbers ? numbers(elements) : elements(elements);
		final Map<Integer, ?> actual = ListExtensions.toIndexMap(list);
		assertEquals(IntStream.range(0, list.size()).boxed().toList(),
			new ArrayList<>(actual.keySet()));
		assertEquals(list, new ArrayList<>(actual.values()));
	}

	/**
	 * Test method for {@link ListExtensions#rearrange(List, Object, int)}: the list with the
	 * element at the given index; unchanged if the element is there already, if the index is the
	 * size of the list, or if the list does not hold the element. The given list does not change
	 *
	 * @param description
	 *            the name of the case
	 * @param elements
	 *            the elements of the list, separated by a space
	 * @param element
	 *            the element to rearrange
	 * @param index
	 *            the index to rearrange it to
	 * @param expected
	 *            the elements of the rearranged list, separated by a space
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"Anton, the last, to 0: Anton first | Leonidas Berta Caesar Dora Emil Anton | Anton | 0 | Anton Leonidas Berta Caesar Dora Emil",
			"Anton to 2 | Leonidas Berta Caesar Dora Emil Anton | Anton | 2 | Leonidas Berta Anton Caesar Dora Emil",
			"Anton to 5, its index: unchanged | Leonidas Berta Caesar Dora Emil Anton | Anton | 5 | Leonidas Berta Caesar Dora Emil Anton",
			"Anton to 6, the size: unchanged | Leonidas Berta Caesar Dora Emil Anton | Anton | 6 | Leonidas Berta Caesar Dora Emil Anton",
			"Anton, the first of Anton, Berta, Caesar, Dora, to 2 | Anton Berta Caesar Dora | Anton | 2 | Berta Caesar Anton Dora",
			"foo, not in the list, to 0: unchanged | Leonidas Berta Caesar Dora Emil Anton | foo | 0 | Leonidas Berta Caesar Dora Emil Anton" }, delimiter = '|')
	public void testRearrange(final String description, final String elements, final String element,
		final int index, final String expected)
	{
		final List<String> list = elements(elements);
		assertEquals(elements(expected), ListExtensions.rearrange(list, element, index));
		assertEquals(elements(elements), list, "the given list changed");
	}

	/**
	 * Test method for {@link ListExtensions#relocate(List, Object, int)}: the element moves in the
	 * list to the given index, or to the end of the list if the index is its size or greater; the
	 * list is not changed if the element is at the index already or if the list does not hold it,
	 * so that even an unmodifiable list is accepted then
	 *
	 * @param description
	 *            the name of the case
	 * @param elements
	 *            the elements of the list, separated by a space
	 * @param element
	 *            the element to relocate
	 * @param index
	 *            the new index
	 * @param expected
	 *            the elements of the list afterwards, separated by a space
	 * @param unmodifiable
	 *            true if the list is unmodifiable
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"Anton, the last, to 0: Anton first | Leonidas Berta Caesar Dora Emil Anton | Anton | 0 | Anton Leonidas Berta Caesar Dora Emil | false",
			"Anton to 2 | Leonidas Berta Caesar Dora Emil Anton | Anton | 2 | Leonidas Berta Anton Caesar Dora Emil | false",
			"Anton, the first, to 2 | Anton Leonidas Berta Caesar Dora Emil | Anton | 2 | Leonidas Berta Anton Caesar Dora Emil | false",
			"Berta to 5, the last index: Berta last | Leonidas Berta Caesar Dora Emil Anton | Berta | 5 | Leonidas Caesar Dora Emil Anton Berta | false",
			"Berta to 6, the size: Berta last | Leonidas Berta Caesar Dora Emil Anton | Berta | 6 | Leonidas Caesar Dora Emil Anton Berta | false",
			"Berta to 8, beyond the size: Berta last | Leonidas Berta Caesar Dora Emil Anton | Berta | 8 | Leonidas Caesar Dora Emil Anton Berta | false",
			"Anton, the last, to 6, the size: unchanged | Leonidas Berta Caesar Dora Emil Anton | Anton | 6 | Leonidas Berta Caesar Dora Emil Anton | false",
			"Anton, the last, to 8, beyond the size: unchanged | Leonidas Berta Caesar Dora Emil Anton | Anton | 8 | Leonidas Berta Caesar Dora Emil Anton | false",
			"Anton to 5, its index, in an unmodifiable list: unchanged | Leonidas Berta Caesar Dora Emil Anton | Anton | 5 | Leonidas Berta Caesar Dora Emil Anton | true",
			"Berta to 1, its index, in an unmodifiable list: unchanged | Anton Berta Caesar | Berta | 1 | Anton Berta Caesar | true",
			"foo, not in an unmodifiable list, to 1: unchanged | Leonidas Berta Caesar Dora Emil Anton | foo | 1 | Leonidas Berta Caesar Dora Emil Anton | true" }, delimiter = '|')
	public void testRelocate(final String description, final String elements, final String element,
		final int index, final String expected, final boolean unmodifiable)
	{
		final List<String> list = unmodifiable
			? Collections.unmodifiableList(elements(elements))
			: elements(elements);
		ListExtensions.relocate(list, element, index);
		assertEquals(elements(expected), list);
	}

	/**
	 * Test method for {@link ListExtensions#removeLastValues(List, int)}: the list without its
	 * given number of last values (#43); the given list does not change
	 *
	 * @param description
	 *            the name of the case
	 * @param elements
	 *            the elements of the list, separated by a space
	 * @param remove
	 *            the number of values to remove
	 * @param expected
	 *            the values of the result, separated by a space
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"Anton, Caesar, Dora, Emil, Leonidas, Berta without the last 2: the first 4 | Anton Caesar Dora Emil Leonidas Berta | 2 | Anton Caesar Dora Emil",
			"A to F without the last 2: A, B, C, D (#43) | A B C D E F | 2 | A B C D",
			"A to F without all 6: empty (#43) | A B C D E F | 6 | ''",
			"A to F without 0: all of them | A B C D E F | 0 | A B C D E F" }, delimiter = '|')
	public void testRemoveLastValues(final String description, final String elements,
		final int remove, final String expected)
	{
		final List<String> list = elements(elements);
		assertEquals(elements(expected), ListExtensions.removeLastValues(list, remove));
		assertEquals(elements(elements), list, "the given list changed");
	}

	/**
	 * Test method for {@link ListExtensions#removeLastValues(List, int)} with more values to remove
	 * than the list holds: an {@link IllegalArgumentException}, as its Javadoc says
	 *
	 * @param description
	 *            the name of the case
	 * @param elements
	 *            the elements of the list, separated by a space
	 * @param remove
	 *            the number of values to remove
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"7 of Anton, Caesar, Dora, Emil, Leonidas, Berta, one more than it holds | Anton Caesar Dora Emil Leonidas Berta | 7",
			"1 of an empty list | '' | 1" }, delimiter = '|')
	public void testRemoveLastValuesWithMoreThanTheListHolds(final String description,
		final String elements, final int remove)
	{
		final List<String> list = elements(elements);
		assertThrows(IllegalArgumentException.class,
			() -> ListExtensions.removeLastValues(list, remove));
	}

	/**
	 * Test the method {@link ListExtensions#revertOrder(List)}: a new list with the elements in
	 * reverse order; the given list does not change
	 *
	 * @param description
	 *            the name of the case
	 * @param elements
	 *            the elements of the list, separated by a space
	 * @param expected
	 *            the elements of the result, separated by a space
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"Anton, Caesar, Dora, Emil, Leonidas, Berta: in reverse order | Anton Caesar Dora Emil Leonidas Berta | Berta Leonidas Emil Dora Caesar Anton",
			"a single element a: a | a | a", "no elements: none | '' | ''" }, delimiter = '|')
	public void testRevertOrder(final String description, final String elements,
		final String expected)
	{
		final List<String> list = elements(elements);
		final List<String> actual = ListExtensions.revertOrder(list);
		assertEquals(elements(expected), actual);
		assertNotSame(list, actual);
		assertEquals(elements(elements), list, "the given list changed");
	}

	/**
	 * Test the method {@link ListExtensions#shuffle(List, List, int[])} with indexes in ascending
	 * order: the elements at the given indexes move from the source to the destination, the others
	 * stay in the source in their order
	 *
	 * @param description
	 *            the name of the case
	 * @param source
	 *            the elements of the source, separated by a space
	 * @param indexes
	 *            the indexes of the elements to move, separated by a space
	 * @param moved
	 *            the elements that move, separated by a space
	 * @param rest
	 *            the elements of the source afterwards, separated by a space
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"indexes 1, 2 of Anton to Franz: Berta and Caesar move | Anton Berta Caesar Dora Emil Franz | 1 2 | Berta Caesar | Anton Dora Emil Franz",
			"indexes 0, 1 of Anton, Dora, Emil, Franz: Anton and Dora move | Anton Dora Emil Franz | 0 1 | Anton Dora | Emil Franz",
			"index 5, the last of Anton to Franz: Franz moves | Anton Berta Caesar Dora Emil Franz | 5 | Franz | Anton Berta Caesar Dora Emil",
			"no indexes: nothing moves | Anton Berta Caesar | '' | '' | Anton Berta Caesar" }, delimiter = '|')
	public void testShuffle(final String description, final String source, final String indexes,
		final String moved, final String rest)
	{
		final List<String> from = elements(source);
		final List<String> destination = new ArrayList<>();
		ListExtensions.shuffle(from, destination,
			numbers(indexes).stream().mapToInt(Integer::intValue).toArray());
		assertEquals(elements(rest), from);
		assertEquals(elements(moved).size(), destination.size());
		assertEquals(new HashSet<>(elements(moved)), new HashSet<>(destination));
	}

	/**
	 * Persons with the given names, sorted by
	 * {@link ListExtensions#sortByProperty(List, java.util.function.Function, boolean)} by name,
	 * and the names in their order afterwards
	 *
	 * @param description
	 *            the name of the case
	 * @param names
	 *            the names of the persons, may hold null
	 * @param ascending
	 *            true for ascending order
	 * @param expected
	 *            the names in their order after the sort
	 */
	record SortCase(String description, List<String> names, boolean ascending,
		List<String> expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of
	 * {@link ListExtensions#sortByProperty(List, java.util.function.Function, boolean)}: a null
	 * name comes first in ascending order, and so last in descending order
	 *
	 * @return the cases
	 */
	static Stream<SortCase> sorts()
	{
		return Stream.of(
			new SortCase("obelix, asterix, miraculix ascending: asterix, miraculix, obelix (#43)",
				List.of("obelix", "asterix", "miraculix"), true,
				List.of("asterix", "miraculix", "obelix")),
			new SortCase("obelix, asterix, miraculix descending: obelix, miraculix, asterix (#43)",
				List.of("obelix", "asterix", "miraculix"), false,
				List.of("obelix", "miraculix", "asterix")),
			new SortCase("obelix, null, miraculix ascending: the null name first",
				Arrays.asList("obelix", null, "miraculix"), true,
				Arrays.asList(null, "miraculix", "obelix")),
			new SortCase("obelix, null, miraculix descending: the null name last",
				Arrays.asList("obelix", null, "miraculix"), false,
				Arrays.asList("obelix", "miraculix", null)));
	}

	/**
	 * Test the method
	 * {@link ListExtensions#sortByProperty(List, java.util.function.Function, boolean)} with the
	 * name of persons
	 *
	 * @param sort
	 *            the names, the order and the names afterwards
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("sorts")
	public void testSortByProperty(final SortCase sort)
	{
		final List<Person> persons = sort.names().stream().map(ListExtensionsTest::newPerson)
			.collect(Collectors.toCollection(ArrayList::new));
		ListExtensions.sortByProperty(persons, Person::getName, sort.ascending());
		assertEquals(sort.expected(), persons.stream().map(Person::getName).toList());
	}

	private static Person newPerson(final String name)
	{
		final Person person = new Person();
		person.setGender(Gender.MALE);
		person.setName(name);
		return person;
	}

	/**
	 * Test the method {@link ListExtensions#splitToParts(java.util.Collection, int)}: the parts,
	 * one after the other, give the elements in their order; each part but the last holds the given
	 * size of them, the last one at least one and at most the given size
	 *
	 * @param description
	 *            the name of the case
	 * @param count
	 *            the number of elements, 0 to count - 1
	 * @param size
	 *            the size of the parts
	 * @param parts
	 *            the number of the parts
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"999 elements in parts of 50: 19 full parts and the last of 49 | 999 | 50 | 20",
			"6 elements in parts of 4: one full part and the last of 2 | 6 | 4 | 2",
			"4 elements in parts of 2, a multiple: two full parts | 4 | 2 | 2",
			"3 elements in parts of 5, fewer than a part: one part of 3 | 3 | 5 | 1",
			"no elements in parts of 3: no part | 0 | 3 | 0" }, delimiter = '|')
	public void testSplitToParts(final String description, final int count, final int size,
		final int parts)
	{
		final List<Integer> list = IntStream.range(0, count).boxed()
			.collect(Collectors.toCollection(ArrayList::new));
		final List<List<Integer>> actual = ListExtensions.splitToParts(list, size);
		assertEquals(parts, actual.size());
		assertEquals(list, actual.stream().flatMap(List::stream).toList());
		for (int i = 0; i < actual.size() - 1; i++)
		{
			assertEquals(size, actual.get(i).size(), "part " + i);
		}
		if (!actual.isEmpty())
		{
			final int last = actual.get(actual.size() - 1).size();
			assertTrue(0 < last && last <= size, "the last part holds " + last);
		}
	}

	/**
	 * Test the methods {@link ListExtensions#toList(java.util.Enumeration)} and
	 * {@link ListExtensions#toList(Set)}: a list of all elements of an enumeration of the given
	 * elements in their order, and a list of all elements of a set of them in the iteration order
	 * of the set
	 *
	 * @param description
	 *            the name of the case
	 * @param elements
	 *            the elements, separated by a space
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "1, 2, 3: in their order | 1 2 3", "1, 2: in their order | 1 2",
			"foo, bar, baz: in their order | foo bar baz",
			"foo, bar, foo: the enumeration gives both foo, the set one | foo bar foo",
			"no elements: an empty list | ''" }, delimiter = '|')
	public void testToList(final String description, final String elements)
	{
		final List<String> given = elements(elements);
		assertEquals(given, ListExtensions.toList(Collections.enumeration(given)),
			"toList(Enumeration)");
		final Set<String> set = new LinkedHashSet<>(given);
		assertEquals(new ArrayList<>(set), ListExtensions.toList(set), "toList(Set)");
	}

	/**
	 * Test the method {@link ListExtensions#toObjectArray(Object...)}: a new {@link Object} array
	 * with the given elements
	 *
	 * @param description
	 *            the name of the case
	 * @param elements
	 *            the elements, separated by a space
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "2, 5, 6, 7, 8, 9: an Object[] of them | 2 5 6 7 8 9",
			"no elements: an empty Object[] | ''" }, delimiter = '|')
	public void testToObjectArray(final String description, final String elements)
	{
		final String[] given = elements(elements).toArray(new String[0]);
		final Object[] actual = ListExtensions.toObjectArray(given);
		assertEquals(Object[].class, actual.getClass());
		assertArrayEquals(given, actual);
		assertNotSame(given, actual);
	}

	/**
	 * Test method for {@link ListExtensions#getIndexToInsert(List, List, Object)}
	 */
	@Test
	public final void testGetIndexToInsert()
	{
		List<String> helpMenu;
		List<String> unsortedHelpMenu;

		String content = "Content";
		String donate = "Donate";
		String diagnostic = "Diagnostic";
		String license = "License";
		String info = "Info";
		helpMenu = ListFactory.newArrayList();
		helpMenu.add(content);
		helpMenu.add(donate);
		helpMenu.add(diagnostic);
		helpMenu.add(license);
		helpMenu.add(info);

		unsortedHelpMenu = ListFactory.newArrayList(helpMenu);

		int iterationCount = 1000;

		for (int i = 0; i < iterationCount; i++)
		{
			shuffleAndSort(helpMenu, unsortedHelpMenu);
		}
	}

	private static void shuffleAndSort(List<String> helpMenu, List<String> unsortedHelpMenu)
	{
		List<String> newSortedHelpMenu;
		Collections.shuffle(unsortedHelpMenu);
		newSortedHelpMenu = ListFactory.newArrayList();
		for (int i = 0; i < unsortedHelpMenu.size(); i++)
		{
			String currentUnsortedHelpMenuItem = unsortedHelpMenu.get(i);
			int indexToInsert = ListExtensions.getIndexToInsert(newSortedHelpMenu, helpMenu,
				currentUnsortedHelpMenuItem);

			int insertIndex = ListExtensions.getIndexToInsert(newSortedHelpMenu,
				currentUnsortedHelpMenuItem, ComparatorFactory.newDefinedOrderComparator(helpMenu));

			assertEquals(indexToInsert, insertIndex);
			newSortedHelpMenu.add(indexToInsert, currentUnsortedHelpMenuItem);
		}
		assertEquals(helpMenu, newSortedHelpMenu);
	}

	/**
	 * Test method for {@link ListExtensions#getIndexToInsert(List, Object, java.util.Comparator)}
	 * with {@link String#CASE_INSENSITIVE_ORDER}: the index of an element that the comparator holds
	 * equal to the given one, whatever equals says (#40), otherwise the index of the first greater
	 * element, or the size of the list if there is none
	 *
	 * @param description
	 *            the name of the case
	 * @param elements
	 *            the elements of the list, in the order of the comparator, separated by a space
	 * @param element
	 *            the element to insert
	 * @param expected
	 *            the index
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "A in a, b, equal to a by the comparator only: 0 (#40) | a b | A | 0",
			"B in a, b, equal to b by the comparator only: 1 (#40) | a b | B | 1",
			"b in a, c, between them: 1 | a c | b | 1",
			"c in a, b, greater than all: 2, the size | a b | c | 2",
			"0 in a, b, less than all: 0 | a b | 0 | 0",
			"a in an empty list: 0 | '' | a | 0" }, delimiter = '|')
	public void testGetIndexToInsertWithComparator(final String description, final String elements,
		final String element, final int expected)
	{
		assertEquals(expected, ListExtensions.getIndexToInsert(elements(elements), element,
			String.CASE_INSENSITIVE_ORDER));
	}

	/**
	 * Test method for the combination methods
	 */
	@Test
	@Disabled
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
	 * Test method for {@link ListExtensions} with {@link BeanTester}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ListExtensions.class);
	}

}
