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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;


/**
 * The unit test class for the class {@link ListFactory}.
 */
public class ListFactoryTest
{

	/**
	 * A call of one of the factory methods, the collection it is given and the list it returns
	 *
	 * @param description
	 *            the name of the case
	 * @param collection
	 *            makes the given collection, may make null; the factory methods that take no
	 *            collection ignore it
	 * @param factory
	 *            the call of the factory method with the given collection
	 * @param listClass
	 *            the class of the returned list
	 * @param expected
	 *            the elements of the returned list, in their order
	 */
	record FactoryCase(String description, Supplier<Collection<String>> collection,
		Function<Collection<String>, List<String>> factory, Class<?> listClass,
		List<String> expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of the factory methods of {@link ListFactory} with a collection, an iterable or an
	 * iterator, with elements, and with both: the elements of the collection first, then the given
	 * elements, with a null or an empty collection the given elements alone. A unique list holds
	 * each element once where it comes first (#65); a sorted list without a comparator has no order
	 * to keep and appends, with a comparator it sorts. "The names" are "", Emil, Anton, Anton,
	 * Anton, Emil, ""
	 *
	 * @return the cases
	 */
	static Stream<FactoryCase> factories()
	{
		final Supplier<Collection<String>> none = () -> null;
		final Supplier<Collection<String>> names = () -> new ArrayList<>(
			Arrays.asList("", "Emil", "Anton", "Anton", "Anton", "Emil", ""));
		final Supplier<Collection<String>> empty = ArrayList::new;
		final Supplier<Collection<String>> foo = () -> new ArrayList<>(List.of("foo"));
		final Supplier<Collection<String>> ba = () -> new ArrayList<>(List.of("b", "a"));
		final Comparator<String> compareTo = String::compareTo;
		final Comparator<String> reverse = Collections.reverseOrder();
		final Comparator<String> noComparator = null;
		return Stream.of(
			new FactoryCase("newArrayList(null, foo): foo", none,
				given -> ListFactory.newArrayList(given, "foo"), ArrayList.class, List.of("foo")),
			new FactoryCase("newArrayList([foo], foo): foo, foo", foo,
				given -> ListFactory.newArrayList(given, "foo"), ArrayList.class,
				List.of("foo", "foo")),
			new FactoryCase("newArrayList(an empty collection, foo): foo", empty,
				given -> ListFactory.newArrayList(given, "foo"), ArrayList.class, List.of("foo")),
			new FactoryCase("newArrayList([b, a]) without elements: b, a", ba,
				given -> ListFactory.newArrayList(given), ArrayList.class, List.of("b", "a")),
			new FactoryCase("newArrayList(a null iterable, foo): foo", none,
				given -> ListFactory.newArrayList((Iterable<String>)given, "foo"), ArrayList.class,
				List.of("foo")),
			new FactoryCase("newArrayList(the iterable [foo], foo): foo, foo", foo,
				given -> ListFactory.newArrayList((Iterable<String>)given, "foo"), ArrayList.class,
				List.of("foo", "foo")),
			new FactoryCase("newArrayList(a null iterator, foo): foo", none,
				given -> ListFactory.newArrayList((Iterator<String>)null, "foo"), ArrayList.class,
				List.of("foo")),
			new FactoryCase("newArrayList(the iterator of [foo], bar): foo, bar", foo,
				given -> ListFactory.newArrayList(given.iterator(), "bar"), ArrayList.class,
				List.of("foo", "bar")),
			new FactoryCase("newArrayList(foo, bar): foo, bar", none,
				given -> ListFactory.newArrayList("foo", "bar"), ArrayList.class,
				List.of("foo", "bar")),
			new FactoryCase("newArrayList(): an empty ArrayList", none,
				given -> ListFactory.newArrayList(), ArrayList.class, List.of()),
			new FactoryCase("newLinkedList(null, foo): foo", none,
				given -> ListFactory.newLinkedList(given, "foo"), LinkedList.class, List.of("foo")),
			new FactoryCase("newLinkedList([foo], foo): foo, foo", foo,
				given -> ListFactory.newLinkedList(given, "foo"), LinkedList.class,
				List.of("foo", "foo")),
			new FactoryCase("newLinkedList(an empty collection, foo): foo", empty,
				given -> ListFactory.newLinkedList(given, "foo"), LinkedList.class, List.of("foo")),
			new FactoryCase("newLinkedList(foo, bar): foo, bar", none,
				given -> ListFactory.newLinkedList("foo", "bar"), LinkedList.class,
				List.of("foo", "bar")),
			new FactoryCase("newVector(the names, foo): the names, then foo", names,
				given -> ListFactory.newVector(given, "foo"), Vector.class,
				List.of("", "Emil", "Anton", "Anton", "Anton", "Emil", "", "foo")),
			new FactoryCase("newVector(null, foo): foo", none,
				given -> ListFactory.newVector(given, "foo"), Vector.class, List.of("foo")),
			new FactoryCase("newVector(an empty collection, foo): foo", empty,
				given -> ListFactory.newVector(given, "foo"), Vector.class, List.of("foo")),
			new FactoryCase("newVector(foo and the names): foo, then the names", none,
				given -> ListFactory.newVector("foo", "", "Emil", "Anton", "Anton", "Anton", "Emil",
					""),
				Vector.class, List.of("foo", "", "Emil", "Anton", "Anton", "Anton", "Emil", "")),
			new FactoryCase("newVector(foo): foo", none, given -> ListFactory.newVector("foo"),
				Vector.class, List.of("foo")),
			new FactoryCase("newVector(): an empty Vector", none, given -> ListFactory.newVector(),
				Vector.class, List.of()),
			new FactoryCase("newUniqueList(null, foo, bar, foo): foo, bar, foo once", none,
				given -> ListFactory.newUniqueList(given, "foo", "bar", "foo"), UniqueList.class,
				List.of("foo", "bar")),
			new FactoryCase("newUniqueList(the names, foo): '', Emil, Anton, foo, each once", names,
				given -> ListFactory.newUniqueList(given, "foo"), UniqueList.class,
				List.of("", "Emil", "Anton", "foo")),
			new FactoryCase("newUniqueList(null, foo): foo", none,
				given -> ListFactory.newUniqueList(given, "foo"), UniqueList.class, List.of("foo")),
			new FactoryCase("newUniqueList([b, a], c): b, a, c (#65)", ba,
				given -> ListFactory.newUniqueList(given, "c"), UniqueList.class,
				List.of("b", "a", "c")),
			new FactoryCase("newUniqueList(an empty collection, c): c (#65)", empty,
				given -> ListFactory.newUniqueList(given, "c"), UniqueList.class, List.of("c")),
			new FactoryCase(
				"newUniqueList([b, a, b], c, a, d, c): b, a, c, d, each where it comes first (#65)",
				() -> new ArrayList<>(List.of("b", "a", "b")),
				given -> ListFactory.newUniqueList(given, "c", "a", "d", "c"), UniqueList.class,
				List.of("b", "a", "c", "d")),
			new FactoryCase("newUniqueList(foo and the names): foo, '', Emil, Anton", none,
				given -> ListFactory.newUniqueList("foo", "", "Emil", "Anton", "Anton", "Anton",
					"Emil", ""),
				UniqueList.class, List.of("foo", "", "Emil", "Anton")),
			new FactoryCase("newUniqueList(foo): foo", none,
				given -> ListFactory.newUniqueList("foo"), UniqueList.class, List.of("foo")),
			new FactoryCase("newUniqueList(): an empty UniqueList", none,
				given -> ListFactory.newUniqueList(), UniqueList.class, List.of()),
			new FactoryCase("newUniqueList(b, a): b, a (#65)", none,
				given -> ListFactory.newUniqueList("b", "a"), UniqueList.class, List.of("b", "a")),
			new FactoryCase("newUniqueList(b, a, b, c): b, a, c (#65)", none,
				given -> ListFactory.newUniqueList("b", "a", "b", "c"), UniqueList.class,
				List.of("b", "a", "c")),
			new FactoryCase("newSortedList(null, String::compareTo, foo, bar, baz): bar, baz, foo",
				none, given -> ListFactory.newSortedList(given, compareTo, "foo", "bar", "baz"),
				SortedList.class, List.of("bar", "baz", "foo")),
			new FactoryCase("newSortedList(null, String::compareTo) without elements: empty", none,
				given -> ListFactory.newSortedList(given, compareTo, new String[0]),
				SortedList.class, List.of()),
			new FactoryCase(
				"newSortedList(the names, reverse order, foo): foo, Emil, Emil, Anton, Anton, Anton, '', ''",
				names, given -> ListFactory.newSortedList(given, reverse, "foo"), SortedList.class,
				List.of("foo", "Emil", "Emil", "Anton", "Anton", "Anton", "", "")),
			new FactoryCase(
				"newSortedList(an empty collection, reverse order, a, c, b): c, b, a, sorted",
				empty, given -> ListFactory.newSortedList(given, reverse, "a", "c", "b"),
				SortedList.class, List.of("c", "b", "a")),
			new FactoryCase(
				"newSortedList(the names, no comparator, foo): the names, then foo, appended",
				names, given -> ListFactory.newSortedList(given, noComparator, "foo"),
				SortedList.class,
				List.of("", "Emil", "Anton", "Anton", "Anton", "Emil", "", "foo")),
			new FactoryCase("newSortedList(null, no comparator, foo): foo", none,
				given -> ListFactory.newSortedList(given, noComparator, "foo"), SortedList.class,
				List.of("foo")),
			new FactoryCase("newSortedList(the names, foo): the names, then foo, appended", names,
				given -> ListFactory.newSortedList(given, "foo"), SortedList.class,
				List.of("", "Emil", "Anton", "Anton", "Anton", "Emil", "", "foo")),
			new FactoryCase("newSortedList(null, foo): foo", none,
				given -> ListFactory.newSortedList(given, "foo"), SortedList.class, List.of("foo")),
			new FactoryCase("newSortedList(foo and the names): in their order, appended", none,
				given -> ListFactory.newSortedList("foo", "", "Emil", "Anton", "Anton", "Anton",
					"Emil", ""),
				SortedList.class,
				List.of("foo", "", "Emil", "Anton", "Anton", "Anton", "Emil", "")),
			new FactoryCase("newSortedList(foo): foo", none,
				given -> ListFactory.newSortedList("foo"), SortedList.class, List.of("foo")),
			new FactoryCase("newSortedList(): an empty SortedList", none,
				given -> ListFactory.newSortedList(), SortedList.class, List.of()),
			new FactoryCase(
				"newSortedUniqueList(null, String::compareTo, foo, bar, baz, foo): bar, baz, foo",
				none,
				given -> ListFactory.newSortedUniqueList(given, compareTo, "foo", "bar", "baz",
					"foo"),
				SortedUniqueList.class, List.of("bar", "baz", "foo")),
			new FactoryCase(
				"newSortedUniqueList(the names, reverse order, foo): foo, Emil, Anton, ''", names,
				given -> ListFactory.newSortedUniqueList(given, reverse, "foo"),
				SortedUniqueList.class, List.of("foo", "Emil", "Anton", "")),
			new FactoryCase(
				"newSortedUniqueList(an empty collection, reverse order, a, c, b, c): c, b, a",
				empty, given -> ListFactory.newSortedUniqueList(given, reverse, "a", "c", "b", "c"),
				SortedUniqueList.class, List.of("c", "b", "a")),
			new FactoryCase(
				"newSortedUniqueList(the names, no comparator, foo): '', Emil, Anton, foo, appended",
				names, given -> ListFactory.newSortedUniqueList(given, noComparator, "foo"),
				SortedUniqueList.class, List.of("", "Emil", "Anton", "foo")),
			new FactoryCase("newSortedUniqueList(null, no comparator, foo): foo", none,
				given -> ListFactory.newSortedUniqueList(given, noComparator, "foo"),
				SortedUniqueList.class, List.of("foo")),
			new FactoryCase("newSortedUniqueList(the names, foo): '', Emil, Anton, foo, appended",
				names, given -> ListFactory.newSortedUniqueList(given, "foo"),
				SortedUniqueList.class, List.of("", "Emil", "Anton", "foo")),
			new FactoryCase("newSortedUniqueList(null, foo): foo", none,
				given -> ListFactory.newSortedUniqueList(given, "foo"), SortedUniqueList.class,
				List.of("foo")),
			new FactoryCase("newSortedUniqueList(foo and the names): foo, '', Emil, Anton", none,
				given -> ListFactory.newSortedUniqueList("foo", "", "Emil", "Anton", "Anton",
					"Anton", "Emil", ""),
				SortedUniqueList.class, List.of("foo", "", "Emil", "Anton")),
			new FactoryCase("newSortedUniqueList(foo): foo", none,
				given -> ListFactory.newSortedUniqueList("foo"), SortedUniqueList.class,
				List.of("foo")),
			new FactoryCase("newSortedUniqueList(): an empty SortedUniqueList", none,
				given -> ListFactory.newSortedUniqueList(), SortedUniqueList.class, List.of()));
	}

	/**
	 * Test for the factory methods newArrayList, newLinkedList, newVector, newUniqueList,
	 * newSortedList and newSortedUniqueList of {@link ListFactory}: a new list of the class the
	 * method names, with the elements in their order, and the given collection does not change
	 *
	 * @param factory
	 *            the call, the given collection and the returned list
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("factories")
	public void testFactoryMethods(final FactoryCase factory)
	{
		final Collection<String> given = factory.collection().get();
		final List<String> before = given == null ? null : new ArrayList<>(given);
		final List<String> actual = factory.factory().apply(given);
		assertEquals(factory.listClass(), actual.getClass());
		assertEquals(factory.expected(), actual);
		if (given != null)
		{
			assertNotSame(given, actual);
			assertEquals(before, new ArrayList<>(given), "the given collection changed");
		}
	}

	/**
	 * A call of {@link ListFactory#newCharacterArrayList(Collection, char...)} and the list it
	 * returns
	 *
	 * @param description
	 *            the name of the case
	 * @param collection
	 *            makes the given collection, may make null
	 * @param characters
	 *            the given characters, may be null
	 * @param expected
	 *            the characters of the returned list, in their order
	 */
	record CharacterCase(String description, Supplier<Collection<Character>> collection,
		char[] characters, List<Character> expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link ListFactory#newCharacterArrayList(Collection, char...)}: the characters
	 * of the collection first, then the given ones; null for either counts as none
	 *
	 * @return the cases
	 */
	static Stream<CharacterCase> characterLists()
	{
		final Supplier<Collection<Character>> ab = () -> new ArrayList<>(List.of('a', 'b'));
		return Stream.of(
			new CharacterCase("null and c, d: c, d", () -> null, new char[] { 'c', 'd' },
				List.of('c', 'd')),
			new CharacterCase("a, b and c, d: a, b, c, d", ab, new char[] { 'c', 'd' },
				List.of('a', 'b', 'c', 'd')),
			new CharacterCase("an empty collection and c: c", ArrayList::new, new char[] { 'c' },
				List.of('c')),
			new CharacterCase("a, b and no characters: a, b", ab, new char[0], List.of('a', 'b')),
			new CharacterCase("a, b and null instead of characters: a, b", ab, null,
				List.of('a', 'b')),
			new CharacterCase("null and null: an empty list", () -> null, null, List.of()));
	}

	/**
	 * Test for method {@link ListFactory#newCharacterArrayList(Collection, char...)}: a new
	 * {@link ArrayList} with the characters in their order, and the given collection does not
	 * change
	 *
	 * @param characterList
	 *            the call and the returned list
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("characterLists")
	public void testNewCharacterArrayList(final CharacterCase characterList)
	{
		final Collection<Character> given = characterList.collection().get();
		final List<Character> before = given == null ? null : new ArrayList<>(given);
		final List<Character> actual = ListFactory.newCharacterArrayList(given,
			characterList.characters());
		assertEquals(ArrayList.class, actual.getClass());
		assertEquals(characterList.expected(), actual);
		if (given != null)
		{
			assertNotSame(given, actual);
			assertEquals(before, new ArrayList<>(given), "the given collection changed");
		}
	}

	/**
	 * A list of newUniqueList, a call of its add with an index, and the list afterwards
	 *
	 * @param description
	 *            the name of the case
	 * @param list
	 *            makes the list of newUniqueList
	 * @param index
	 *            the index
	 * @param element
	 *            the element to add
	 * @param expected
	 *            the list afterwards
	 */
	record AddAtIndexCase(String description, Supplier<List<String>> list, int index,
		String element, List<String> expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of add with an index on the lists of newUniqueList (#65)
	 *
	 * @return the cases
	 */
	static Stream<AddAtIndexCase> addsAtIndex()
	{
		return Stream.of(
			new AddAtIndexCase("newUniqueList(b, a).add(0, c): c, b, a, inserted at 0",
				() -> ListFactory.newUniqueList("b", "a"), 0, "c", List.of("c", "b", "a")),
			new AddAtIndexCase("newUniqueList(c, b, a).add(0, a): a, c, b, a held one moves to 0",
				() -> ListFactory.newUniqueList("c", "b", "a"), 0, "a", List.of("a", "c", "b")),
			new AddAtIndexCase("newUniqueList([b, a], c).add(1, d): b, d, a, c, inserted at 1",
				() -> ListFactory.newUniqueList(Arrays.asList("b", "a"), "c"), 1, "d",
				List.of("b", "d", "a", "c")),
			new AddAtIndexCase(
				"newUniqueList([b, a], c).add(0, c): c, b, a, the held last one moves to 0",
				() -> ListFactory.newUniqueList(Arrays.asList("b", "a"), "c"), 0, "c",
				List.of("c", "b", "a")));
	}

	/**
	 * Test for the method add with an index on the lists of newUniqueList: it inserts at the index
	 * and moves an element the list holds there, as {@link UniqueList#add(int, Object)} does (#65)
	 *
	 * @param addAtIndex
	 *            the list, the call and the list afterwards
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("addsAtIndex")
	public void testNewUniqueListAddWithIndex(final AddAtIndexCase addAtIndex)
	{
		final List<String> list = addAtIndex.list().get();
		list.add(addAtIndex.index(), addAtIndex.element());
		assertEquals(addAtIndex.expected(), list);
	}

	/**
	 * Test for method {@link ListFactory#newRangeList(int, int)}: the numbers from the start to the
	 * end, both inclusive, as the example of its Javadoc says
	 *
	 * @param description
	 *            the name of the case
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param expected
	 *            the numbers, separated by a space
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "5 to 9: 5, 6, 7, 8, 9 | 5 | 9 | 5 6 7 8 9",
			"5 to 5, start equal to end: 5 alone | 5 | 5 | 5",
			"-2 to 1, across zero: -2, -1, 0, 1 | -2 | 1 | -2 -1 0 1" }, delimiter = '|')
	public void testNewRangeList(final String description, final int start, final int end,
		final String expected)
	{
		assertEquals(Arrays.stream(expected.split(" ")).map(Integer::valueOf).toList(),
			ListFactory.newRangeList(start, end));
	}

	/**
	 * Test for method {@link ListFactory#newRangeList(int, int)} with an end below the start: an
	 * {@link IllegalArgumentException}, as
	 * {@link io.github.astrapi69.collection.array.ArrayFactory#newRangeArray(int, int)} says (#145)
	 *
	 * @param description
	 *            the name of the case
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "5 to 4: the end is below the start | 5 | 4",
			"0 to -1: across zero | 0 | -1" }, delimiter = '|')
	public void testNewRangeListWithEndBelowStart(final String description, final int start,
		final int end)
	{
		assertEquals("Parameter end must not be less than parameter start.",
			assertThrows(IllegalArgumentException.class, () -> ListFactory.newRangeList(start, end))
				.getMessage());
	}

	/**
	 * Test method for {@link ListFactory} with {@link BeanTester}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ListFactory.class);
	}

}
