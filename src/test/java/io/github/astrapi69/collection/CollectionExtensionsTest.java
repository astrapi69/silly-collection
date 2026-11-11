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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
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
	 * A collection and the array {@link CollectionExtensions#toArray(Collection)} makes of it
	 *
	 * @param description
	 *            the name of the case
	 * @param collection
	 *            makes the given collection, may make null
	 * @param arrayClass
	 *            the class of the returned array, or null if it returns null
	 * @param expected
	 *            the elements of the returned array, or null if it returns null
	 */
	record ToArrayCase(String description, Supplier<Collection<?>> collection, Class<?> arrayClass,
		Object[] expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link CollectionExtensions#toArray(Collection)}: the elements in the iteration
	 * order of the collection, in an array of the most specific common superclass of the non-null
	 * elements (#64), and null for null and for an empty collection
	 *
	 * @return the cases
	 */
	static Stream<ToArrayCase> arrays()
	{
		return Stream.of(new ToArrayCase("a LinkedHashSet of 1, 2, 3: an Integer[] in its order",
			() -> SetFactory.newLinkedHashSet(1, 2, 3), Integer[].class, new Integer[] { 1, 2, 3 }),
			new ToArrayCase("an ArrayList of 1, 2, 3: an Integer[] in its order",
				() -> ListFactory.newArrayList(1, 2, 3), Integer[].class,
				new Integer[] { 1, 2, 3 }),
			new ToArrayCase("a single element a: a String[] of a",
				() -> ListFactory.newArrayList("a"), String[].class, new String[] { "a" }),
			new ToArrayCase("null: null", () -> null, null, null),
			new ToArrayCase("an empty LinkedHashSet: null", () -> SetFactory.newLinkedHashSet(),
				null, null),
			new ToArrayCase("an empty ArrayList: null", () -> ListFactory.newArrayList(), null,
				null),
			new ToArrayCase("null, a, null first: a String[] of null, a (#64)",
				() -> new ArrayList<>(Arrays.asList(null, "a")), String[].class,
				new String[] { null, "a" }),
			new ToArrayCase("a, null, null last: a String[] of a, null",
				() -> new ArrayList<>(Arrays.asList("a", null)), String[].class,
				new String[] { "a", null }),
			new ToArrayCase("1, 2.5, an Integer first: a Number[] (#64)",
				() -> new ArrayList<Number>(Arrays.asList(1, 2.5)), Number[].class,
				new Number[] { 1, 2.5 }),
			new ToArrayCase("2.5, 1, a Double first: a Number[] in their order (#64)",
				() -> new ArrayList<Number>(Arrays.asList(2.5, 1)), Number[].class,
				new Number[] { 2.5, 1 }),
			new ToArrayCase("1, a, no common superclass but Object: an Object[] (#64)",
				() -> new ArrayList<Comparable<?>>(Arrays.asList(1, "a")), Object[].class,
				new Object[] { 1, "a" }),
			new ToArrayCase("null, null, nulls only: an Object[] of two nulls (#64)",
				() -> new ArrayList<String>(Arrays.asList(null, null)), Object[].class,
				new Object[] { null, null }));
	}

	/**
	 * Test for method {@link CollectionExtensions#toArray(Collection)}: the result holds the
	 * elements of the collection in its iteration order, and its component type is the most
	 * specific common superclass of the non-null elements, or {@link Object} if every element is
	 * null. Before the fix of #64 the component type was the class of the first element, so that a
	 * null first threw a {@link NullPointerException} and an element of another class an
	 * {@link ArrayStoreException}
	 *
	 * @param toArray
	 *            the given collection and the returned array
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("arrays")
	public void testToArray(final ToArrayCase toArray)
	{
		final Object[] actual = CollectionExtensions.toArray(toArray.collection().get());
		assertArrayEquals(toArray.expected(), actual);
		if (actual != null)
		{
			assertEquals(toArray.arrayClass(), actual.getClass());
		}
	}

	/**
	 * A caller that assigns the result of {@link CollectionExtensions#toArray(Collection)} to an
	 * array of the type of its elements, and whether that assignment throws a
	 * {@link ClassCastException}
	 *
	 * @param description
	 *            the name of the case
	 * @param assignment
	 *            the call and the assignment
	 * @param refused
	 *            true if the assignment throws a {@link ClassCastException}
	 */
	record AssignmentCase(String description, Executable assignment, boolean refused) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of a caller that assigns the result of
	 * {@link CollectionExtensions#toArray(Collection)} to a {@code T[]}: as the Javadoc says, the
	 * assignment throws a {@link ClassCastException} where the common superclass of the elements is
	 * neither {@code T} nor a subclass of it, so where {@code T} is an interface or the collection
	 * holds only null (#64)
	 *
	 * @return the cases
	 */
	static Stream<AssignmentCase> assignments()
	{
		return Stream.of(new AssignmentCase(
			"a List<Comparable<?>> of 1 and a to a Comparable<?>[]: refused, an Object[] (#64)",
			() -> {
				final Comparable<?>[] comparables = CollectionExtensions
					.toArray(new ArrayList<Comparable<?>>(Arrays.asList(1, "a")));
			}, true), new AssignmentCase(
				"a List<String> of null and null to a String[]: refused, an Object[] (#64)", () -> {
					final String[] strings = CollectionExtensions
						.toArray(new ArrayList<String>(Arrays.asList(null, null)));
				}, true),
			new AssignmentCase("a List<String> of null and a to a String[]: a String[]", () -> {
				final String[] strings = CollectionExtensions
					.toArray(new ArrayList<String>(Arrays.asList(null, "a")));
			}, false),
			new AssignmentCase("a List<Number> of 1 and 2.5 to a Number[]: a Number[]", () -> {
				final Number[] numbers = CollectionExtensions
					.toArray(new ArrayList<Number>(Arrays.asList(1, 2.5)));
			}, false),
			new AssignmentCase(
				"a List<Number> of 1 and 2 to a Number[]: an Integer[], a subclass of Number[]",
				() -> {
					final Number[] numbers = CollectionExtensions
						.toArray(new ArrayList<Number>(Arrays.asList(1, 2)));
				}, false));
	}

	/**
	 * Test for method {@link CollectionExtensions#toArray(Collection)} with a caller that assigns
	 * the result to an array of the type of the elements: a {@link ClassCastException} where the
	 * Javadoc says so, and none where the common superclass is that type or a subclass of it
	 *
	 * @param assignment
	 *            the call, the assignment and whether it is refused
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("assignments")
	public void testToArrayAssignedToAnArrayOfTheElementType(final AssignmentCase assignment)
	{
		if (assignment.refused())
		{
			assertThrows(ClassCastException.class, assignment.assignment());
		}
		else
		{
			assertDoesNotThrow(assignment.assignment());
		}
	}

	/**
	 * A collection and the list {@link CollectionExtensions#toList(Collection)} makes of it
	 *
	 * @param description
	 *            the name of the case
	 * @param collection
	 *            makes the given collection, may make null
	 * @param expected
	 *            the elements of the returned list, or null if it returns null
	 * @param itself
	 *            true if the returned list is the given collection itself
	 */
	record ToListCase(String description, Supplier<Collection<Integer>> collection,
		List<Integer> expected, boolean itself) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link CollectionExtensions#toList(Collection)}: a {@link List} itself, a new
	 * {@link ArrayList} in the iteration order of any other collection, and null for null
	 *
	 * @return the cases
	 */
	static Stream<ToListCase> lists()
	{
		return Stream.of(
			new ToListCase("a LinkedHashSet of 1, 2, 3: a new ArrayList in its order",
				() -> SetFactory.newLinkedHashSet(1, 2, 3), List.of(1, 2, 3), false),
			new ToListCase("a TreeSet of 3, 1, 2: a new ArrayList in its order, 1, 2, 3",
				() -> SetFactory.newTreeSet(3, 1, 2), List.of(1, 2, 3), false),
			new ToListCase("an empty LinkedHashSet: a new empty ArrayList",
				() -> SetFactory.newLinkedHashSet(), List.of(), false),
			new ToListCase("an ArrayList of 1, 2, 3: the list itself",
				() -> ListFactory.newArrayList(1, 2, 3), List.of(1, 2, 3), true),
			new ToListCase("a LinkedList of 1, 2, 3: the list itself",
				() -> ListFactory.newLinkedList(1, 2, 3), List.of(1, 2, 3), true),
			new ToListCase("null: null", () -> null, null, false));
	}

	/**
	 * Test for method {@link CollectionExtensions#toList(Collection)}: a {@link List} is returned
	 * itself, not copied, and any other collection gives a new {@link ArrayList} with its elements
	 * in its iteration order
	 *
	 * @param toList
	 *            the given collection and the returned list
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("lists")
	public void testToList(final ToListCase toList)
	{
		final Collection<Integer> given = toList.collection().get();
		final List<Integer> actual = CollectionExtensions.toList(given);
		assertEquals(toList.expected(), actual);
		if (actual != null && toList.itself())
		{
			assertSame(given, actual);
		}
		if (actual != null && !toList.itself())
		{
			assertNotSame(given, actual);
			assertEquals(ArrayList.class, actual.getClass());
		}
	}

	/**
	 * Test for method {@link CollectionExtensions#containsAtLeastOne(Collection, Object[])}: true
	 * if the collection holds one of the given objects, wherever they stand, and false if it holds
	 * none of them or if no objects are given
	 *
	 * @param description
	 *            the name of the case
	 * @param collection
	 *            the numbers of the collection, separated by a space
	 * @param objects
	 *            the numbers to look for, separated by a space
	 * @param expected
	 *            the answer
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"3 or 7 in 22, 33, 25, 45, neither in it: false | 22 33 25 45 | 3 7 | false",
			"3 or 33 in 22, 33, 25, 45, the second in it: true | 22 33 25 45 | 3 33 | true",
			"33 or 3 in 22, 33, 25, 45, the first in it: true | 22 33 25 45 | 33 3 | true",
			"45 in 22, 33, 25, 45, the last: true | 22 33 25 45 | 45 | true",
			"no objects in 22, 33, 25, 45: false | 22 33 25 45 | '' | false",
			"3 in an empty collection: false | '' | 3 | false" }, delimiter = '|')
	public void testContainsAtLeastOne(final String description, final String collection,
		final String objects, final boolean expected)
	{
		assertEquals(expected, CollectionExtensions.containsAtLeastOne(numbers(collection),
			numbers(objects).toArray(new Integer[0])));
	}

	/**
	 * Test for method {@link CollectionExtensions#difference(Collection, Collection)}: the first
	 * collection loses every element the second one holds, each occurrence of it, and keeps the
	 * others in their order; the second collection does not change
	 *
	 * @param description
	 *            the name of the case
	 * @param one
	 *            the numbers of the first collection, separated by a space
	 * @param other
	 *            the numbers of the second collection, separated by a space
	 * @param expected
	 *            the numbers the first collection holds afterwards
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"3, 7, 22, 23, 34, 45 without 22, 33, 25, 45: 3, 7, 23, 34 | 3 7 22 23 34 45 | 22 33 25 45 | 3 7 23 34",
			"1, 2, 2, 3 without 2: 1, 3, each 2 goes | 1 2 2 3 | 2 | 1 3",
			"1, 2 without 3, 4, nothing in common: 1, 2 | 1 2 | 3 4 | 1 2",
			"1, 2 without nothing: 1, 2 | 1 2 | '' | 1 2",
			"1, 2 without 2, 1, all of them: nothing | 1 2 | 2 1 | ''",
			"nothing without 1: nothing | '' | 1 | ''" }, delimiter = '|')
	public void testDifference(final String description, final String one, final String other,
		final String expected)
	{
		final List<Integer> first = numbers(one);
		final List<Integer> second = numbers(other);
		CollectionExtensions.difference(first, second);
		assertEquals(numbers(expected), first);
		assertEquals(numbers(other), second, "the second collection changed");
	}

	/**
	 * Two collections and whether
	 * {@link CollectionExtensions#isEqualCollection(Collection, Collection)} calls them equal
	 *
	 * @param description
	 *            the name of the case
	 * @param one
	 *            makes the one collection, may make null
	 * @param other
	 *            makes the other collection, may make null
	 * @param equal
	 *            the answer
	 */
	record EqualityCase(String description, Supplier<Collection<Object>> one,
		Supplier<Collection<Object>> other, boolean equal) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link CollectionExtensions#isEqualCollection(Collection, Collection)}: equal if
	 * both are null, or if they hold equal elements with the same cardinality, whatever their order
	 * (#42). Each call of a supplier makes new collections and lists, so that the lines of the
	 * first case are equal in content only; the arrays of the two cases with the same arrays are
	 * the same instances
	 *
	 * @return the cases
	 */
	static Stream<EqualityCase> equalities()
	{
		final Supplier<Collection<Object>> threeLines = () -> ListFactory.newArrayList(
			ListFactory.newArrayList("1", "23", "3"), ListFactory.newArrayList("4", "25", "9"),
			ListFactory.newArrayList("6", "21", "8"));
		final Supplier<Collection<Object>> twoLines = () -> ListFactory.newArrayList(
			ListFactory.newArrayList("1", "23", "3"), ListFactory.newArrayList("4", "25", "9"));
		final String[] lineOne = { "1", "23", "3" };
		final String[] lineTwo = { "4", "25", "9" };
		return Stream.of(
			new EqualityCase("three lines, each a list, equal in content: equal", threeLines,
				threeLines, true),
			new EqualityCase("null and null: equal", () -> null, () -> null, true),
			new EqualityCase("null and three lines: not equal", () -> null, threeLines, false),
			new EqualityCase("three lines and null: not equal", threeLines, () -> null, false),
			new EqualityCase("an empty list and null: not equal", () -> ListFactory.newArrayList(),
				() -> null, false),
			new EqualityCase("two empty lists: equal", () -> ListFactory.newArrayList(),
				() -> ListFactory.newArrayList(), true),
			new EqualityCase("two lines and three lines, another size: not equal", twoLines,
				threeLines, false),
			new EqualityCase("1, 2, 3 and 1, 2, 4, partly different: not equal",
				() -> ListFactory.newArrayList(1, 2, 3), () -> ListFactory.newArrayList(1, 2, 4),
				false),
			new EqualityCase("1, 2 and 3, 4, no common element: not equal (#42)",
				() -> ListFactory.newArrayList(1, 2), () -> ListFactory.newArrayList(3, 4), false),
			new EqualityCase("1, 1, 2 and 1, 2, 2, another cardinality: not equal (#42)",
				() -> ListFactory.newArrayList(1, 1, 2), () -> ListFactory.newArrayList(1, 2, 2),
				false),
			new EqualityCase("1, 1, 2 and 2, 1, 1, another order: equal (#42)",
				() -> ListFactory.newArrayList(1, 1, 2), () -> ListFactory.newArrayList(2, 1, 1),
				true),
			new EqualityCase("the list 1, 2 and the set 2, 1, another kind of collection: equal",
				() -> ListFactory.newArrayList(1, 2), () -> SetFactory.newLinkedHashSet(2, 1),
				true),
			new EqualityCase("two arrays and two arrays of other content: not equal (#42)",
				() -> ListFactory.newArrayList(ArrayFactory.newArray("1", "23", "3"),
					ArrayFactory.newArray("4", "25", "9")),
				() -> ListFactory.newArrayList(ArrayFactory.newArray("x"),
					ArrayFactory.newArray("y")),
				false),
			new EqualityCase("the same two arrays in the other order: equal (#42)",
				() -> ListFactory.newArrayList(lineOne, lineTwo),
				() -> ListFactory.newArrayList(lineTwo, lineOne), true),
			new EqualityCase(
				"two other arrays with the same content, arrays compare by identity: not equal (#42)",
				() -> ListFactory.newArrayList(lineOne, lineTwo),
				() -> ListFactory.newArrayList(ArrayFactory.newArray("1", "23", "3"),
					ArrayFactory.newArray("4", "25", "9")),
				false));
	}

	/**
	 * Test for method {@link CollectionExtensions#isEqualCollection(Collection, Collection)}: the
	 * answer of the Javadoc, the same in both directions
	 *
	 * @param equality
	 *            the two collections and the answer
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("equalities")
	public void testIsEqualCollection(final EqualityCase equality)
	{
		assertEquals(equality.equal(),
			CollectionExtensions.isEqualCollection(equality.one().get(), equality.other().get()));
		assertEquals(equality.equal(),
			CollectionExtensions.isEqualCollection(equality.other().get(), equality.one().get()),
			"the other way round");
	}

	/**
	 * A collection of arrays and its hash code by {@link CollectionExtensions#hashCode(Collection)}
	 *
	 * @param description
	 *            the name of the case
	 * @param lines
	 *            makes the collection, with new arrays on each call
	 * @param expected
	 *            the hash code
	 */
	record HashCodeCase(String description, Supplier<Collection<Object[]>> lines, int expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link CollectionExtensions#hashCode(Collection)}; each call of a supplier makes
	 * new arrays, so the hash code is the one of their content, not of their identity
	 *
	 * @return the cases
	 */
	static Stream<HashCodeCase> hashCodes()
	{
		return Stream.of(
			new HashCodeCase("three lines of numbers: -1269378556",
				() -> ListFactory.newArrayList(ArrayFactory.newArray("1", "23", "3"),
					ArrayFactory.newArray("4", "25", "9"), ArrayFactory.newArray("6", "21", "8")),
				-1269378556),
			new HashCodeCase("three lines of names, ages and genders: -1124541430",
				() -> ListFactory.newArrayList(ArrayFactory.newArray("John", "23", "male"),
					ArrayFactory.newArray("Jim", "25", "male"),
					ArrayFactory.newArray("Mary", "21", "female")),
				-1124541430),
			new HashCodeCase("no lines: 1, as the hash code of an empty List",
				() -> ListFactory.newArrayList(), 1),
			new HashCodeCase("the lines a and b: 15868032",
				() -> ListFactory.newArrayList(ArrayFactory.newArray("a"),
					ArrayFactory.newArray("b")),
				15868032),
			new HashCodeCase("the lines b and a, the same hash code as a and b: 15868032",
				() -> ListFactory.newArrayList(ArrayFactory.newArray("b"),
					ArrayFactory.newArray("a")),
				15868032),
			new HashCodeCase("a null line after two others: 0",
				() -> Arrays.asList(ArrayFactory.newArray("5"), ArrayFactory.newArray("7"), null),
				0),
			new HashCodeCase("a null line only: 0",
				() -> Collections.<Object[]> singletonList(null), 0),
			new HashCodeCase("a line whose Arrays.hashCode is 0: 0",
				() -> Collections.<Object[]> singletonList(new Integer[] { -31 }), 0));
	}

	/**
	 * Test for method {@link CollectionExtensions#hashCode(Collection)}: the hash code of the
	 * content of the arrays, equal for other arrays with the same content
	 *
	 * @param hashCode
	 *            the collection and its hash code
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("hashCodes")
	public void testHashCodeCollectionWithArrayObjects(final HashCodeCase hashCode)
	{
		assertEquals(hashCode.expected(), CollectionExtensions.hashCode(hashCode.lines().get()));
	}

	/**
	 * Test for method {@link CollectionExtensions#intersection(Collection...)}: the first
	 * collection itself keeps the elements that all the others hold too, in its order, and the
	 * others do not change; with fewer than two collections the result is null and no collection
	 * changes
	 *
	 * @param description
	 *            the name of the case
	 * @param collections
	 *            the collections, separated by a semicolon, their elements by a space
	 * @param expected
	 *            the elements of the result, separated by a space, or null if it is null
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"three lists of names: the four names all of them hold | Alex Brad Caesar Leonardo Jesus; Alex Berta Brad Caesar Leonardo; Anton Alex Berta Brad Caesar Leonardo | Alex Brad Caesar Leonardo",
			"lotto numbers and the same numbers in another order: all of them | 3 7 22 23 34 45; 3 7 23 34 22 45 | 3 7 22 23 34 45",
			"lotto numbers and numbers with 3 and 7 in common: 3, 7 | 3 7 22 23 34 45; 3 7 24 35 25 46 | 3 7",
			"two collections without a common element: nothing | 1 2; 3 4 | ''",
			"one collection alone: null | Alex Brad | null",
			"no collection: null | '' | null" }, delimiter = '|', nullValues = "null")
	@SuppressWarnings("unchecked")
	public void testIntersection(final String description, final String collections,
		final String expected)
	{
		final List<String> columns = collections.isEmpty()
			? List.of()
			: Arrays.asList(collections.split("; "));
		final List<List<String>> given = columns.stream().map(CollectionExtensionsTest::elements)
			.toList();
		final Collection<String> actual = CollectionExtensions
			.intersection(given.toArray(new Collection[0]));
		if (expected == null)
		{
			assertNull(actual);
		}
		else
		{
			assertSame(given.get(0), actual);
			assertEquals(elements(expected), actual);
		}
		for (int i = actual == null ? 0 : 1; i < given.size(); i++)
		{
			assertEquals(elements(columns.get(i)), given.get(i), "collection " + i + " changed");
		}
	}

	/**
	 * A collection and whether {@link CollectionExtensions#isEmpty(Collection)} calls it empty
	 *
	 * @param description
	 *            the name of the case
	 * @param collection
	 *            makes the collection, may make null
	 * @param empty
	 *            true if it is null or empty
	 */
	record EmptinessCase(String description, Supplier<Collection<String>> collection,
		boolean empty) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link CollectionExtensions#isEmpty(Collection)} and
	 * {@link CollectionExtensions#isNotEmpty(Collection)}: null and an empty collection are empty,
	 * a collection with an element is not, also if that element is null
	 *
	 * @return the cases
	 */
	static Stream<EmptinessCase> emptinesses()
	{
		return Stream.of(new EmptinessCase("null: empty", () -> null, true),
			new EmptinessCase("an empty list: empty", () -> ListFactory.newArrayList(), true),
			new EmptinessCase("an empty set: empty", () -> SetFactory.newLinkedHashSet(), true),
			new EmptinessCase("a list of foo: not empty", () -> ListFactory.newArrayList("foo"),
				false),
			new EmptinessCase("a list that holds null: not empty",
				() -> new ArrayList<>(Arrays.asList((String)null)), false));
	}

	/**
	 * Test for the methods {@link CollectionExtensions#isEmpty(Collection)} and
	 * {@link CollectionExtensions#isNotEmpty(Collection)}: each answers the opposite of the other
	 *
	 * @param emptiness
	 *            the collection and whether it is empty
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("emptinesses")
	public void testIsEmptyAndIsNotEmpty(final EmptinessCase emptiness)
	{
		assertEquals(emptiness.empty(), CollectionExtensions.isEmpty(emptiness.collection().get()),
			"isEmpty");
		assertEquals(!emptiness.empty(),
			CollectionExtensions.isNotEmpty(emptiness.collection().get()), "isNotEmpty");
	}

	/**
	 * Test for method {@link CollectionExtensions#partition(Collection, int)}: the parts, one after
	 * the other, give the elements in their order; each part but the last holds the given size of
	 * them, the last one at least one and at most the given size
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
			"5 elements in parts of 2: two full parts and the last of 1 | 5 | 2 | 3",
			"4 elements in parts of 2, a multiple: two full parts | 4 | 2 | 2",
			"3 elements in parts of 5, fewer than a part: one part of 3 | 3 | 5 | 1",
			"3 elements in parts of 1: three parts of 1 | 3 | 1 | 3",
			"no elements in parts of 3: no part | 0 | 3 | 0" }, delimiter = '|')
	public void testPartition(final String description, final int count, final int size,
		final int parts)
	{
		final List<Integer> list = IntStream.range(0, count).boxed()
			.collect(Collectors.toCollection(ArrayList::new));
		final List<List<Integer>> actual = new ArrayList<>(
			CollectionExtensions.partition(list, size));
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
	 * Test method for {@link CollectionExtensions} with {@link BeanTester}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(CollectionExtensions.class);
	}

}
