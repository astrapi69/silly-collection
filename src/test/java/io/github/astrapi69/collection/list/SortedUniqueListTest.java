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

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.comparator.object.StringComparator;

/**
 * The unit test class for {@link SortedUniqueList}
 */
public class SortedUniqueListTest
{

	/**
	 * The message of the UnsupportedOperationException of set, addFirst, addLast and the add and
	 * set of the list iterator with a comparator, as TreeSet.addFirst says it, and the start of the
	 * one of replaceAll
	 */
	private static final String POSITIONING = "The comparator of a SortedUniqueList determines the"
		+ " position of its elements, so explicit positioning is not supported";

	/**
	 * The message of the UnsupportedOperationException of sort
	 */
	private static final String SORT = "A SortedUniqueList keeps the order of its comparator;"
		+ " setComparator(Comparator) changes it";

	/**
	 * The message of the UnsupportedOperationException of the view of subList
	 */
	private static final String SUB_LIST = "A subList view of a SortedUniqueList reads and removes"
		+ " only; set and add through the list itself";

	/**
	 * The message of the UnsupportedOperationException of the view of reversed
	 */
	private static final String REVERSED = "The reversed view of a SortedUniqueList reads and"
		+ " removes only; add through the list itself";

	/**
	 * A new {@link SortedUniqueList} of the given elements in the order of the given comparator
	 *
	 * @param comparator
	 *            the comparator, null for none
	 * @param elements
	 *            the elements
	 * @return the list
	 */
	private static SortedUniqueList<String> sorted(final Comparator<String> comparator,
		final String... elements)
	{
		return new SortedUniqueList<>(Arrays.asList(elements), comparator);
	}

	/**
	 * Adds the given elements one after the other with {@link List#add(Object)}
	 *
	 * @param list
	 *            the list
	 * @param elements
	 *            the elements
	 * @return what each add returned
	 */
	private static List<Boolean> addEach(final List<String> list, final String... elements)
	{
		final List<Boolean> returned = new ArrayList<>();
		for (final String element : elements)
		{
			returned.add(list.add(element));
		}
		return returned;
	}

	/**
	 * Calls the given call and returns null, for a void method
	 *
	 * @param list
	 *            the list
	 * @param call
	 *            the call
	 * @return null
	 */
	private static Object none(final List<String> list, final Consumer<List<String>> call)
	{
		call.accept(list);
		return null;
	}

	/**
	 * A call that changes a {@link SortedUniqueList}, what it returns and the list afterwards
	 *
	 * @param description
	 *            the name of the case
	 * @param list
	 *            makes the list
	 * @param call
	 *            the call, which returns what the method returns, null for a void method
	 * @param returned
	 *            what the call returns
	 * @param expected
	 *            the elements of the list afterwards, in their order
	 */
	record ChangeCase(String description, Supplier<List<String>> list,
		Function<List<String>, Object> call, Object returned, List<String> expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The calls that change a {@link SortedUniqueList}: the add methods put an element the list
	 * does not hold at its place in the order of the comparator, whatever index they are given, and
	 * append it without a comparator, and leave a held one where it is; without a comparator set
	 * swaps an element held at another index, replaceAll and the algorithms of {@link Collections}
	 * work as on an ArrayList, and with one replaceAll replaces in place where the values keep the
	 * order. "The names" are "", Emil, Anton, Anton, Anton, Emil, ""
	 *
	 * @return the cases
	 */
	static Stream<ChangeCase> changes()
	{
		final List<String> names = Arrays.asList("", "Emil", "Anton", "Anton", "Anton", "Emil", "");
		final Supplier<List<String>> aToF = () -> sorted(null, "a", "b", "c", "d", "e", "f");
		return Stream.of(new ChangeCase(
			"add of Leonidas, Berta, Caesar, Dora, Emil, Anton, each twice, initial capacity 6, no comparator: each once",
			() -> new SortedUniqueList<>(6),
			list -> addEach(list, "Leonidas", "Leonidas", "Berta", "Berta", "Caesar", "Caesar",
				"Dora", "Dora", "Emil", "Emil", "Anton", "Anton"),
			List.of(true, false, true, false, true, false, true, false, true, false, true, false),
			List.of("Leonidas", "Berta", "Caesar", "Dora", "Emil", "Anton")),
			new ChangeCase(
				"add(0, e) of Leonidas, Berta, Caesar, Dora, Emil, Anton, no comparator: appended, the index ignored",
				SortedUniqueList::new, list -> none(list, each -> {
					for (final String name : List.of("Leonidas", "Berta", "Caesar", "Dora", "Emil",
						"Anton"))
					{
						each.add(0, name);
					}
				}), null, List.of("Leonidas", "Berta", "Caesar", "Dora", "Emil", "Anton")),
			new ChangeCase(
				"add of Leonidas, Berta, Caesar, Dora, Emil, Anton, natural order: sorted in",
				() -> sorted(Comparator.naturalOrder()),
				list -> addEach(list, "Leonidas", "Berta", "Caesar", "Dora", "Emil", "Anton"),
				List.of(true, true, true, true, true, true),
				List.of("Anton", "Berta", "Caesar", "Dora", "Emil", "Leonidas")),
			new ChangeCase("add of 3, 1, 2, 3, natural order: 1, 2, 3, the second 3 false",
				() -> sorted(Comparator.naturalOrder()), list -> addEach(list, "3", "1", "2", "3"),
				List.of(true, true, true, false), List.of("1", "2", "3")),
			new ChangeCase(
				"add of A, B, a, B to a, b, case insensitive: A and B next to the one held equal, a and B false (#40)",
				() -> sorted(String.CASE_INSENSITIVE_ORDER, "a", "b"),
				list -> addEach(list, "A", "B", "a", "B"), List.of(true, true, false, false),
				List.of("A", "a", "B", "b")),
			new ChangeCase(
				"add of Leonidas, Berta, Leonidas, no comparator: appended, the second Leonidas false",
				SortedUniqueList::new, list -> addEach(list, "Leonidas", "Berta", "Leonidas"),
				List.of(true, true, false), List.of("Leonidas", "Berta")),
			new ChangeCase(
				"add(-1, Caesar), add(5, Anton) and add(-1, Caesar), no comparator: appended once (#40)",
				SortedUniqueList::new, list -> none(list, each -> {
					each.add(-1, "Caesar");
					each.add(5, "Anton");
					each.add(-1, "Caesar");
				}), null, List.of("Caesar", "Anton")),
			new ChangeCase(
				"add(-1, Caesar), add(5, Anton) and add(5, Caesar), natural order: sorted in once (#40)",
				() -> sorted(Comparator.naturalOrder()), list -> none(list, each -> {
					each.add(-1, "Caesar");
					each.add(5, "Anton");
					each.add(5, "Caesar");
				}), null, List.of("Anton", "Caesar")),
			new ChangeCase(
				"addAll of the names to Emil, Caesar, StringComparator: the new ones sorted in once",
				() -> {
					final List<String> list = new SortedUniqueList<>(new StringComparator());
					list.add("Emil");
					list.add("Caesar");
					return list;
				}, list -> list.addAll(names), true, List.of("", "Anton", "Caesar", "Emil")),
			new ChangeCase("addAll of nothing to Emil, Caesar, no comparator: unchanged, false",
				() -> sorted(null, "Emil", "Caesar"), list -> list.addAll(List.of()), false,
				List.of("Emil", "Caesar")),
			new ChangeCase(
				"addAll(1, the names) to Emil, Caesar, no comparator: the new ones appended once",
				() -> sorted(null, "Emil", "Caesar"), list -> list.addAll(1, names), true,
				List.of("Emil", "Caesar", "", "Anton")),
			new ChangeCase("addAll(1, nothing) to Emil, Caesar, no comparator: unchanged, false",
				() -> sorted(null, "Emil", "Caesar"), list -> list.addAll(1, List.of()), false,
				List.of("Emil", "Caesar")),
			new ChangeCase(
				"addAll of Emil, Caesar, Anton, Emil to Emil, natural order: the new ones sorted in",
				() -> sorted(Comparator.naturalOrder(), "Emil"),
				list -> list.addAll(Arrays.asList("Emil", "Caesar", "Anton", "Emil")), true,
				List.of("Anton", "Caesar", "Emil")),
			new ChangeCase("addAll of Emil, Anton to Anton, Emil, natural order: all held, false",
				() -> sorted(Comparator.naturalOrder(), "Anton", "Emil"),
				list -> list.addAll(Arrays.asList("Emil", "Anton")), false,
				List.of("Anton", "Emil")),
			new ChangeCase("addAll of nothing to Emil, natural order: unchanged, false",
				() -> sorted(Comparator.naturalOrder(), "Emil"),
				list -> list.addAll(Collections.emptyList()), false, List.of("Emil")),
			new ChangeCase(
				"addAll(0, Emil, Caesar, Anton) to Emil, natural order: sorted in, not at the index",
				() -> sorted(Comparator.naturalOrder(), "Emil"),
				list -> list.addAll(0, Arrays.asList("Emil", "Caesar", "Anton")), true,
				List.of("Anton", "Caesar", "Emil")),
			new ChangeCase("addAll(0, Emil, Anton) to Anton, Emil, natural order: all held, false",
				() -> sorted(Comparator.naturalOrder(), "Anton", "Emil"),
				list -> list.addAll(0, Arrays.asList("Emil", "Anton")), false,
				List.of("Anton", "Emil")),
			new ChangeCase(
				"addAll(-1, Caesar, Anton) and addAll(5, Anton, Berta), no comparator: appended once (#40)",
				SortedUniqueList::new,
				list -> List.of(list.addAll(-1, Arrays.asList("Caesar", "Anton")),
					list.addAll(5, Arrays.asList("Anton", "Berta"))),
				List.of(true, true), List.of("Caesar", "Anton", "Berta")),
			new ChangeCase(
				"addAll(-1, Caesar, Anton) and addAll(5, Anton, Berta), natural order: sorted in once (#40)",
				() -> sorted(Comparator.naturalOrder()),
				list -> List.of(list.addAll(-1, Arrays.asList("Caesar", "Anton")),
					list.addAll(5, Arrays.asList("Anton", "Berta"))),
				List.of(true, true), List.of("Anton", "Berta", "Caesar")),
			new ChangeCase(
				"addAll of Caesar, Emil, Anton, Caesar to Emil, no comparator: the new ones appended once",
				() -> sorted(null, "Emil"),
				list -> list.addAll(Arrays.asList("Caesar", "Emil", "Anton", "Caesar")), true,
				List.of("Emil", "Caesar", "Anton")),
			new ChangeCase("addAll of a, b itself, natural order: nothing added, false (#77)",
				() -> sorted(Comparator.naturalOrder(), "a", "b"), list -> list.addAll(list), false,
				List.of("a", "b")),
			new ChangeCase(
				"set(0, c) of a, b, c, no comparator: c held at 2 swaps with a, returns a, as in UniqueList (#77)",
				() -> sorted(null, "a", "b", "c"), list -> list.set(0, "c"), "a",
				List.of("c", "b", "a")),
			new ChangeCase(
				"set(2, a) of a, b, c, no comparator: a held at 0 swaps with c, returns c, as in UniqueList (#77)",
				() -> sorted(null, "a", "b", "c"), list -> list.set(2, "a"), "c",
				List.of("c", "b", "a")),
			new ChangeCase("set(0, b) of c, a, no comparator: replaced in place, returns c",
				() -> sorted(null, "c", "a"), list -> list.set(0, "b"), "c", List.of("b", "a")),
			new ChangeCase("replaceAll with upper case of c, a, no comparator: C, A",
				() -> sorted(null, "c", "a"),
				list -> none(list, each -> each.replaceAll(String::toUpperCase)), null,
				List.of("C", "A")),
			new ChangeCase(
				"replaceAll doubling a, c, e, natural order: aa, cc, ee, the order kept (#78)",
				() -> sorted(Comparator.naturalOrder(), "a", "c", "e"),
				list -> none(list, each -> each.replaceAll(element -> element + element)), null,
				List.of("aa", "cc", "ee")),
			new ChangeCase(
				"replaceAll with the next letter of a, b, c, natural order: b, c, d, each a held value (#78)",
				() -> sorted(Comparator.naturalOrder(), "a", "b", "c"),
				list -> none(list,
					each -> each
						.replaceAll(element -> String.valueOf((char)(element.charAt(0) + 1)))),
				null, List.of("b", "c", "d")),
			new ChangeCase(
				"replaceAll giving B for a of a, b, c, case insensitive: B, b, c, held equal (#78)",
				() -> sorted(String.CASE_INSENSITIVE_ORDER, "a", "b", "c"),
				list -> none(list,
					each -> each.replaceAll(element -> "a".equals(element) ? "B" : element)),
				null, List.of("B", "b", "c")),
			new ChangeCase("Collections.swap(0, 5) of a to f, no comparator: f, b, c, d, e, a",
				aToF, list -> none(list, each -> Collections.swap(each, 0, 5)), null,
				List.of("f", "b", "c", "d", "e", "a")),
			new ChangeCase("Collections.swap(1, 1) of a to f, no comparator: unchanged", aToF,
				list -> none(list, each -> Collections.swap(each, 1, 1)), null,
				List.of("a", "b", "c", "d", "e", "f")),
			new ChangeCase("Collections.reverse of a to f, an even size, no comparator: f to a",
				aToF, list -> none(list, Collections::reverse), null,
				List.of("f", "e", "d", "c", "b", "a")),
			new ChangeCase("Collections.reverse of a, b, c, an odd size, no comparator: c, b, a",
				() -> sorted(null, "a", "b", "c"), list -> none(list, Collections::reverse), null,
				List.of("c", "b", "a")),
			new ChangeCase(
				"Collections.shuffle with the seed 77 of a to f, no comparator: a, c, f, b, e, d, as an ArrayList",
				aToF, list -> none(list, each -> Collections.shuffle(each, new Random(77))), null,
				List.of("a", "c", "f", "b", "e", "d")),
			new ChangeCase("Collections.rotate by 1 of a to f, no comparator: f, a, b, c, d, e",
				aToF, list -> none(list, each -> Collections.rotate(each, 1)), null,
				List.of("f", "a", "b", "c", "d", "e")),
			new ChangeCase("Collections.rotate by -1 of a to f, no comparator: b, c, d, e, f, a",
				aToF, list -> none(list, each -> Collections.rotate(each, -1)), null,
				List.of("b", "c", "d", "e", "f", "a")),
			new ChangeCase("Collections.rotate by 2 of a to f, no comparator: e, f, a, b, c, d",
				aToF, list -> none(list, each -> Collections.rotate(each, 2)), null,
				List.of("e", "f", "a", "b", "c", "d")),
			new ChangeCase("Collections.rotate by 3 of a to f, no comparator: d, e, f, a, b, c",
				aToF, list -> none(list, each -> Collections.rotate(each, 3)), null,
				List.of("d", "e", "f", "a", "b", "c")));
	}

	/**
	 * Test the calls that change a {@link SortedUniqueList}: what each returns and the list
	 * afterwards
	 *
	 * @param change
	 *            the list, the call and its result
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("changes")
	public void testChanges(final ChangeCase change)
	{
		final List<String> list = change.list().get();
		assertEquals(change.returned(), change.call().apply(list));
		assertEquals(change.expected(), list);
	}

	/**
	 * A call on a {@link SortedUniqueList} that throws, the exception and the list afterwards
	 *
	 * @param description
	 *            the name of the case
	 * @param list
	 *            makes the list
	 * @param call
	 *            the call
	 * @param exception
	 *            the class of the exception
	 * @param message
	 *            the message of the exception, null if a case does not check it
	 * @param expected
	 *            the elements of the list afterwards, null if a case does not check them
	 */
	record RefusalCase(String description, Supplier<List<String>> list, Consumer<List<String>> call,
		Class<? extends RuntimeException> exception, String message, List<String> expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * A call that throws an UnsupportedOperationException and leaves the list unchanged
	 *
	 * @param description
	 *            the name of the case
	 * @param list
	 *            makes the list
	 * @param call
	 *            the call
	 * @param message
	 *            the message of the exception, null if the case does not check it
	 * @param expected
	 *            the elements of the list, before and after the call
	 * @return the case
	 */
	private static RefusalCase refused(final String description, final Supplier<List<String>> list,
		final Consumer<List<String>> call, final String message, final String... expected)
	{
		return new RefusalCase(description, list, call, UnsupportedOperationException.class,
			message, List.of(expected));
	}

	/**
	 * The calls on a {@link SortedUniqueList} that throw: with a comparator what would put an
	 * element at a position the caller chooses or reorder the list against its comparator, as in a
	 * SortedSet (#77, #78, #87, decided on #91), the writes through the views of subList and
	 * reversed (#78, #87), sort, addFirst and addLast also without a comparator, replaceAll with
	 * two equal values, and replaceAll with an operator that fails or a value the comparator cannot
	 * compare; a refused call leaves the list as it is
	 *
	 * @return the cases
	 */
	static Stream<RefusalCase> refusals()
	{
		final Supplier<List<String>> abc = () -> sorted(Comparator.naturalOrder(), "a", "b", "c");
		final Supplier<List<String>> ace = () -> sorted(Comparator.naturalOrder(), "a", "c", "e");
		final Supplier<List<String>> ac = () -> sorted(Comparator.naturalOrder(), "a", "c");
		final Supplier<List<String>> bdf = () -> sorted(Comparator.naturalOrder(), "b", "d", "f");
		final Supplier<List<String>> abcd = () -> sorted(Comparator.naturalOrder(), "a", "b", "c",
			"d");
		final UnaryOperator<String> failsForB = element -> {
			if ("b".equals(element))
			{
				throw new IllegalStateException("no value for b");
			}
			return element.toUpperCase();
		};
		return Stream.of(
			refused("set(0, c) of a, b, c, natural order, c held behind it (#78)", abc,
				list -> list.set(0, "c"), POSITIONING, "a", "b", "c"),
			refused("set(2, a) of a, b, c, natural order, a held in front of it (#78)", abc,
				list -> list.set(2, "a"), null, "a", "b", "c"),
			refused("set(1, a) of a, b, c, natural order, held, would keep the order (#91)", abc,
				list -> list.set(1, "a"), null, "a", "b", "c"),
			refused("set(1, c) of a, b, c, natural order, held, would keep the order (#91)", abc,
				list -> list.set(1, "c"), null, "a", "b", "c"),
			refused("set(0, a) of b, d, f, natural order, keeps the order at the first index (#91)",
				bdf, list -> list.set(0, "a"), null, "b", "d", "f"),
			refused("set(0, c) of b, d, f, natural order, keeps the order (#91)", bdf,
				list -> list.set(0, "c"), null, "b", "d", "f"),
			refused("set(1, c) of b, d, f, natural order, keeps the order in the middle (#91)", bdf,
				list -> list.set(1, "c"), null, "b", "d", "f"),
			refused("set(1, e) of b, d, f, natural order, keeps the order in the middle (#91)", bdf,
				list -> list.set(1, "e"), null, "b", "d", "f"),
			refused("set(2, e) of b, d, f, natural order, keeps the order at the last index (#91)",
				bdf, list -> list.set(2, "e"), null, "b", "d", "f"),
			refused("set(2, z) of b, d, f, natural order, keeps the order at the last index (#91)",
				bdf, list -> list.set(2, "z"), null, "b", "d", "f"),
			refused("set(0, z) of m, natural order, the only element (#91)",
				() -> sorted(Comparator.naturalOrder(), "m"), list -> list.set(0, "z"), null, "m"),
			refused(
				"set(1, B) of b, d, f, case insensitive, held equal to the neighbour in front (#91)",
				() -> sorted(String.CASE_INSENSITIVE_ORDER, "b", "d", "f"),
				list -> list.set(1, "B"), null, "b", "d", "f"),
			refused(
				"set(1, F) of b, d, f, case insensitive, held equal to the next neighbour (#91)",
				() -> sorted(String.CASE_INSENSITIVE_ORDER, "b", "d", "f"),
				list -> list.set(1, "F"), null, "b", "d", "f"),
			refused("set(0, d) of a, c, e, natural order, against the order (#78)", ace,
				list -> list.set(0, "d"), POSITIONING, "a", "c", "e"),
			refused("set(2, b) of a, c, e, natural order, against the order (#78)", ace,
				list -> list.set(2, "b"), null, "a", "c", "e"),
			refused("set(1, d) of a, c, e, natural order, an element that keeps the order (#91)",
				ace, list -> list.set(1, "d"), null, "a", "c", "e"),
			refused("set(2, a) of a, b, natural order, an index out of range (#78)",
				() -> sorted(Comparator.naturalOrder(), "a", "b"), list -> list.set(2, "a"), null,
				"a", "b"),
			refused("set(-1, b) of a, b, natural order, a negative index (#78)",
				() -> sorted(Comparator.naturalOrder(), "a", "b"), list -> list.set(-1, "b"), null,
				"a", "b"),
			new RefusalCase("set(2, a) of b, a, no comparator: Index 2 out of bounds (#77)",
				() -> sorted(null, "b", "a"), list -> list.set(2, "a"),
				IndexOutOfBoundsException.class, "Index 2 out of bounds for length 2",
				List.of("b", "a")),
			new RefusalCase("set(-1, b) of b, a, no comparator: Index -1 out of bounds (#77)",
				() -> sorted(null, "b", "a"), list -> list.set(-1, "b"),
				IndexOutOfBoundsException.class, "Index -1 out of bounds for length 2",
				List.of("b", "a")),
			refused("set(0, null) of a, c, natural order, a value it cannot compare (#77, #78)", ac,
				list -> list.set(0, null), null, "a", "c"),
			new RefusalCase(
				"replaceAll giving x for each of a, b, c: IllegalArgumentException, x twice (#77)",
				abc, list -> list.replaceAll(element -> "x"), IllegalArgumentException.class,
				"The operator gives the value x for two elements of this list",
				List.of("a", "b", "c")),
			new RefusalCase(
				"replaceAll giving A for a and B for the others of a, b, c: IllegalArgumentException (#77)",
				abc, list -> list.replaceAll(element -> element.equals("a") ? "A" : "B"),
				IllegalArgumentException.class, null, List.of("a", "b", "c")),
			new RefusalCase(
				"replaceAll giving b for b and c for the others of a, b, c: c twice, out of order (#78)",
				abc, list -> list.replaceAll(element -> "b".equals(element) ? "b" : "c"),
				IllegalArgumentException.class,
				"The operator gives the value c for two elements of this list",
				List.of("a", "b", "c")),
			refused("replaceAll giving f for a of a, c, e, natural order: c at 1 breaks it (#78)",
				ace, list -> list.replaceAll(element -> "a".equals(element) ? "f" : element),
				POSITIONING + ": the value c at the index 1 would break its order", "a", "c", "e"),
			refused("replaceAll giving b, c, a for a, b, c, natural order, distinct values (#78)",
				abc,
				list -> list.replaceAll(
					element -> element.equals("a") ? "b" : element.equals("b") ? "c" : "a"),
				null, "a", "b", "c"),
			new RefusalCase(
				"replaceAll giving null for a of a, c, natural order: NullPointerException (#77)",
				ac, list -> list.replaceAll(element -> "a".equals(element) ? null : element),
				NullPointerException.class, null, List.of("a", "c")),
			new RefusalCase("replaceAll(null) of an empty list: NullPointerException",
				() -> sorted(Comparator.naturalOrder()), list -> list.replaceAll(null),
				NullPointerException.class, null, List.of()),
			new RefusalCase(
				"replaceAll with an operator that removes b of a, b: ConcurrentModificationException",
				() -> sorted(Comparator.naturalOrder(), "a", "b"),
				list -> list.replaceAll(element -> {
					list.remove("b");
					return element.toUpperCase();
				}), ConcurrentModificationException.class, null, null),
			new RefusalCase(
				"replaceAll with an operator that throws for b of a, b, c, natural order: none replaced",
				abc, list -> list.replaceAll(failsForB), IllegalStateException.class,
				"no value for b", List.of("a", "b", "c")),
			new RefusalCase(
				"replaceAll with an operator that throws for b of a, b, c, no comparator: none replaced",
				() -> sorted(null, "a", "b", "c"), list -> list.replaceAll(failsForB),
				IllegalStateException.class, "no value for b", List.of("a", "b", "c")),
			refused("sort(reverse order) of a, b, c, natural order (#77)", abc,
				list -> list.sort(Comparator.reverseOrder()), SORT, "a", "b", "c"),
			refused("sort(null) of a, b, c, natural order (#77)", abc, list -> list.sort(null),
				null, "a", "b", "c"),
			refused("sort(natural order) of a, b, c, the comparator of the list (#77)", abc,
				list -> list.sort(Comparator.naturalOrder()), null, "a", "b", "c"),
			refused("sort(null) of b, a, no comparator (#77)", () -> sorted(null, "b", "a"),
				list -> list.sort(null), null, "b", "a"),
			refused("Collections.sort of c, b, a, reverse order (#77)",
				() -> sorted(Comparator.reverseOrder(), "a", "b", "c"),
				list -> Collections.sort(list), null, "c", "b", "a"),
			refused("Collections.swap(0, 3) of a, b, c, d, natural order (#91)", abcd,
				list -> Collections.swap(list, 0, 3), null, "a", "b", "c", "d"),
			refused("Collections.reverse of a, b, c, d, natural order (#91)", abcd,
				Collections::reverse, null, "a", "b", "c", "d"),
			refused("Collections.shuffle with the seed 77 of a, b, c, d, natural order (#91)", abcd,
				list -> Collections.shuffle(list, new Random(77)), null, "a", "b", "c", "d"),
			refused("Collections.rotate by 1 of a, b, c, d, natural order (#91)", abcd,
				list -> Collections.rotate(list, 1), null, "a", "b", "c", "d"),
			refused("Collections.swap(0, 1) of a, b, c, d, natural order, two neighbours (#91)",
				abcd, list -> Collections.swap(list, 0, 1), null, "a", "b", "c", "d"),
			refused("Collections.rotate by -1 of a, b, c, d, natural order (#91)", abcd,
				list -> Collections.rotate(list, -1), null, "a", "b", "c", "d"),
			refused(
				"Collections.swap(1, 1) of a, b, c, d, natural order, an index with itself (#91)",
				abcd, list -> Collections.swap(list, 1, 1), null, "a", "b", "c", "d"),
			refused("Collections.fill with a of b, c, d, natural order (#91)",
				() -> sorted(Comparator.naturalOrder(), "b", "c", "d"),
				list -> Collections.fill(list, "a"), null, "b", "c", "d"),
			refused("Collections.copy of a, c into b, d, f, natural order (#91)", bdf,
				list -> Collections.copy(list, Arrays.asList("a", "c")), null, "b", "d", "f"),
			refused("subList(1, 3).set(0, z) of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).set(0, "z"), SUB_LIST, "a", "b", "c", "d"),
			refused("subList(1, 3).set(0, a) of a, b, c, d, an element the list holds (#78)", abcd,
				list -> list.subList(1, 3).set(0, "a"), null, "a", "b", "c", "d"),
			refused("subList(1, 3).replaceAll of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).replaceAll(String::toUpperCase), null, "a", "b", "c",
				"d"),
			refused("subList(1, 3).subList(0, 1).set(0, z) of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).subList(0, 1).set(0, "z"), null, "a", "b", "c", "d"),
			refused("subList(1, 3).subList(0, 1).replaceAll of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).subList(0, 1).replaceAll(String::toUpperCase), null, "a",
				"b", "c", "d"),
			refused("subList(0, 2).add(a) of a, c, the whole list, an element it holds (#78)", ac,
				list -> list.subList(0, 2).add("a"), null, "a", "c"),
			refused("subList(1, 3).add(e) of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).add("e"), null, "a", "b", "c", "d"),
			refused("subList(1, 3).add(0, e) of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).add(0, "e"), null, "a", "b", "c", "d"),
			refused("subList(1, 3).addAll(e) of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).addAll(Arrays.asList("e")), null, "a", "b", "c", "d"),
			refused("subList(1, 3).addAll(0, e) of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).addAll(0, Arrays.asList("e")), null, "a", "b", "c", "d"),
			refused("subList(1, 3).addAll of nothing of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).addAll(Collections.emptyList()), null, "a", "b", "c",
				"d"),
			refused("subList(1, 3).addAll(0, nothing) of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).addAll(0, Collections.emptyList()), null, "a", "b", "c",
				"d"),
			refused("subList(1, 3).subList(0, 1).add(e) of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).subList(0, 1).add("e"), null, "a", "b", "c", "d"),
			refused("subList(1, 3).subList(0, 1).addAll(e) of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).subList(0, 1).addAll(Arrays.asList("e")), null, "a", "b",
				"c", "d"),
			refused("subList(1, 3).sort(reverse order) of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).sort(Comparator.reverseOrder()), null, "a", "b", "c",
				"d"),
			refused("subList(1, 3).sort(null) of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).sort(null), null, "a", "b", "c", "d"),
			refused("subList(1, 3).sort(natural order) of a, b, c, d, its comparator (#78)", abcd,
				list -> list.subList(1, 3).sort(Comparator.naturalOrder()), null, "a", "b", "c",
				"d"),
			refused("subList(2, 2).sort(null) of a, b, c, d, an empty view (#78)", abcd,
				list -> list.subList(2, 2).sort(null), null, "a", "b", "c", "d"),
			refused("subList(1, 3).subList(0, 2).sort(null) of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).subList(0, 2).sort(null), null, "a", "b", "c", "d"),
			refused("Collections.reverse of subList(1, 3) of a, b, c, d (#78)", abcd,
				list -> Collections.reverse(list.subList(1, 3)), null, "a", "b", "c", "d"),
			refused("Collections.swap(0, 1) of subList(1, 3) of a, b, c, d (#78)", abcd,
				list -> Collections.swap(list.subList(1, 3), 0, 1), null, "a", "b", "c", "d"),
			refused("addFirst(z) of a, b, natural order (#87)",
				() -> sorted(Comparator.naturalOrder(), "a", "b"), list -> list.addFirst("z"),
				POSITIONING, "a", "b"),
			refused("addLast(0) of a, b, natural order (#87)",
				() -> sorted(Comparator.naturalOrder(), "a", "b"), list -> list.addLast("0"), null,
				"a", "b"),
			refused("addFirst(b) of a, b, natural order, an element the list holds (#87)",
				() -> sorted(Comparator.naturalOrder(), "a", "b"), list -> list.addFirst("b"), null,
				"a", "b"),
			refused("addFirst(z) of b, a, no comparator (#87)", () -> sorted(null, "b", "a"),
				list -> list.addFirst("z"), null, "b", "a"),
			refused("addLast(z) of b, a, no comparator (#87)", () -> sorted(null, "b", "a"),
				list -> list.addLast("z"), null, "b", "a"),
			refused("reversed().addFirst(0) of a, b, c (#87)", abc,
				list -> list.reversed().addFirst("0"), REVERSED, "a", "b", "c"),
			refused("reversed().addLast(z) of a, b, c (#87)", abc,
				list -> list.reversed().addLast("z"), null, "a", "b", "c"),
			refused("reversed().add(z) of a, b, c (#87)", abc, list -> list.reversed().add("z"),
				null, "a", "b", "c"),
			refused("reversed().add(0, z) of a, b, c (#87)", abc,
				list -> list.reversed().add(0, "z"), null, "a", "b", "c"),
			refused("reversed().addAll(z) of a, b, c (#87)", abc,
				list -> list.reversed().addAll(Arrays.asList("z")), null, "a", "b", "c"),
			refused("reversed().set(0, y) of a, b, c (#87)", abc,
				list -> list.reversed().set(0, "y"), null, "a", "b", "c"),
			refused("reversed().replaceAll of a, b, c (#87)", abc,
				list -> list.reversed().replaceAll(String::toUpperCase), null, "a", "b", "c"),
			refused("reversed().sort(null) of a, b, c (#87)", abc,
				list -> list.reversed().sort(null), null, "a", "b", "c"),
			refused("set(y) of the list iterator of reversed() after next() of a, b, c (#87)", abc,
				list -> {
					final ListIterator<String> iterator = list.reversed().listIterator();
					assertEquals("c", iterator.next());
					iterator.set("y");
				}, null, "a", "b", "c"),
			refused("add(y) of the list iterator of reversed() after next() of a, b, c (#87)", abc,
				list -> {
					final ListIterator<String> iterator = list.reversed().listIterator();
					assertEquals("c", iterator.next());
					iterator.add("y");
				}, null, "a", "b", "c"),
			refused("reversed().subList(0, 2).addFirst(0) of a, b, c (#87)", abc,
				list -> list.reversed().subList(0, 2).addFirst("0"), null, "a", "b", "c"));
	}

	/**
	 * Test the calls on a {@link SortedUniqueList} that throw: the exception, its message where a
	 * case names it, and the list afterwards, unchanged where the call is refused
	 *
	 * @param refusal
	 *            the list, the call and the exception
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("refusals")
	public void testRefusals(final RefusalCase refusal)
	{
		final List<String> list = refusal.list().get();
		final RuntimeException exception = assertThrows(refusal.exception(),
			() -> refusal.call().accept(list));
		if (refusal.message() != null)
		{
			assertEquals(refusal.message(), exception.getMessage());
		}
		if (refusal.expected() != null)
		{
			assertEquals(refusal.expected(), list);
		}
	}

	/**
	 * Test method for {@link StringComparator#compare(String, String)} with null smaller.
	 */
	@Test
	public void testCompare()
	{
		Comparator<String> comparator;
		Collection<String> collection;
		int actual;
		boolean expected;
		List<String> list;

		String alex = "Alex";
		String bill = "Billy";
		String leo = "Leon";

		// Now lets see a demo on a list...
		list = new SortedUniqueList<>();
		list.add(leo);
		list.add(alex);
		list.add(bill);
		list.add(null);
		list.add(null);
		collection = ListFactory.newArrayList(list);

		expected = list.indexOf(leo) == 0;
		assertTrue(expected);
		expected = list.indexOf(alex) == 1;
		assertTrue(expected);
		expected = list.indexOf(bill) == 2;
		assertTrue(expected);

		comparator = StringComparator.of(true);

		// Now lets see a demo on a list...
		list = new SortedUniqueList<>(comparator);
		list.add(leo);
		list.add(alex);
		list.add(bill);
		list.add(null);
		list.add(null);

		expected = list.indexOf(leo) == 2;
		assertTrue(expected);
		expected = list.indexOf(alex) == 0;
		assertTrue(expected);
		expected = list.indexOf(bill) == 1;
		assertTrue(expected);

		comparator = StringComparator.of(false);
		((SortedUniqueList<String>)list).setComparator(comparator);

		expected = list.indexOf(alex) == 1;
		assertTrue(expected);
		expected = list.indexOf(bill) == 2;
		assertTrue(expected);
		expected = list.indexOf(leo) == 3;
		assertTrue(expected);

		// test with the other constructor
		SortedUniqueList<String> sortedArrayList = new SortedUniqueList<>(collection, comparator);

		expected = sortedArrayList.indexOf(alex) == 1;
		assertTrue(expected);
		expected = sortedArrayList.indexOf(bill) == 2;
		assertTrue(expected);
		expected = sortedArrayList.indexOf(leo) == 3;
		assertTrue(expected);
		assertEquals(comparator, sortedArrayList.getComparator());
		comparator = StringComparator.of(true);
		sortedArrayList.setComparator(comparator);
		assertEquals(comparator, sortedArrayList.getComparator());

	}

	/**
	 * Test method for {@link SortedUniqueList#setComparator(Comparator)}.
	 */
	@Test
	@DisplayName("Test setting comparator")
	public void testSetComparator()
	{
		SortedUniqueList<String> list = new SortedUniqueList<>();
		list.add("banana");
		list.add("apple");
		list.add("cherry");
		list.setComparator(Comparator.reverseOrder());
		assertEquals(Arrays.asList("cherry", "banana", "apple"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#add(Object)} with several elements the comparator
	 * holds equal to the given one, which equals does not: it goes in front of one of them, so not
	 * after the last of them, and the list stays in the order of the comparator. An element equal
	 * to one of them by equals is not added (#66)
	 */
	@Test
	public void testAddWithSeveralElementsTheComparatorHoldsEqual()
	{
		List<String> list = new SortedUniqueList<>(Comparator.comparingInt(String::length));
		list.addAll(Arrays.asList("x", "aa", "bb", "cc", "zzz"));

		assertTrue(list.add("dd"));
		assertFalse(list.add("bb"));
		assertEquals(6, list.size());
		assertEquals("x", list.get(0));
		assertEquals("zzz", list.get(5));
		int index = list.indexOf("dd");
		assertTrue(1 <= index && index <= 3, "dd is not in front of one of aa, bb and cc: " + list);
	}

	/**
	 * Test the method {@link SortedUniqueList#set(int, Object)} with a comparator while an iterator
	 * runs over the list: set(1, d) of [a, c, e] after the first next() throws the
	 * UnsupportedOperationException, the list stays [a, c, e], and the iterator goes on with c and
	 * e (#78, decided on #91)
	 */
	@Test
	public void testSetWithComparatorWhileAnIteratorRuns()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "c", "e"),
			Comparator.naturalOrder());
		Iterator<String> iterator = list.iterator();
		assertEquals("a", iterator.next());

		assertThrows(UnsupportedOperationException.class, () -> list.set(1, "d"));
		assertEquals(Arrays.asList("a", "c", "e"), list);
		assertEquals("c", iterator.next());
		assertEquals("e", iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 * Test the method {@link SortedUniqueList#set(int, Object)} without a comparator while an
	 * iterator runs over the list: the swap is no structural modification, set(0, c) of [a, b, c]
	 * after the first next() gives [c, b, a], and the iterator goes on with b and a without a
	 * ConcurrentModificationException (#77)
	 */
	@Test
	public void testSetWithoutComparatorWhileAnIteratorRuns()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"));
		Iterator<String> iterator = list.iterator();
		assertEquals("a", iterator.next());

		list.set(0, "c");
		assertEquals("b", iterator.next());
		assertEquals("a", iterator.next());
		assertFalse(iterator.hasNext());
		assertEquals(Arrays.asList("c", "b", "a"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#set(int, Object)} with an element equal to the one at
	 * the index: with a comparator it throws the UnsupportedOperationException, and the list keeps
	 * the instance it holds; without a comparator the given instance replaces the one at the index,
	 * and the method returns the previous one (#77, #78)
	 */
	@Test
	public void testSetWithElementHeldAtTheIndex()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"),
			Comparator.naturalOrder());
		String previous = list.get(1);
		String equal = new String("b");
		assertThrows(UnsupportedOperationException.class, () -> list.set(1, equal));
		assertSame(previous, list.get(1));
		assertEquals(Arrays.asList("a", "b", "c"), list);

		List<String> withoutComparator = new SortedUniqueList<>(Arrays.asList("b", "a"));
		previous = withoutComparator.get(0);
		String other = new String("b");
		assertSame(previous, withoutComparator.set(0, other));
		assertSame(other, withoutComparator.get(0));
		assertEquals(Arrays.asList("b", "a"), withoutComparator);
	}

	/**
	 * Test the method set of the list iterator, of listIterator() and of listIterator(int), with a
	 * comparator: it calls set(int, E) of the list at the index of the element last returned, which
	 * throws the UnsupportedOperationException whatever its arguments, c, which the list holds, and
	 * z after next() returned a, and b after previous() returned c; the list stays [a, c], and the
	 * iterators go on with c and with a (#78, decided on #91)
	 */
	@Test
	public void testListIteratorSet()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "c"),
			Comparator.naturalOrder());
		ListIterator<String> iterator = list.listIterator();
		assertEquals("a", iterator.next());
		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> iterator.set("c"));
		assertEquals(
			"The comparator of a SortedUniqueList determines the position of its elements, so"
				+ " explicit positioning is not supported",
			exception.getMessage());
		assertThrows(UnsupportedOperationException.class, () -> iterator.set("z"));
		assertEquals(Arrays.asList("a", "c"), list);
		assertEquals("c", iterator.next());

		ListIterator<String> fromIndex = list.listIterator(2);
		assertEquals("c", fromIndex.previous());
		assertThrows(UnsupportedOperationException.class, () -> fromIndex.set("b"));
		assertEquals(Arrays.asList("a", "c"), list);
		assertEquals("a", fromIndex.previous());
	}

	/**
	 * Test the method add of the list iterator, of listIterator() and of listIterator(int): it
	 * throws an UnsupportedOperationException with the message of addFirst, as TreeSet.addFirst
	 * does, for an element the list holds too, the list stays [a, c], the cursor does not move, and
	 * next() returns c (#77, #78)
	 */
	@Test
	public void testListIteratorAdd()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "c"),
			Comparator.naturalOrder());
		ListIterator<String> iterator = list.listIterator();
		assertEquals("a", iterator.next());
		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> iterator.add("a"));
		assertEquals(
			"The comparator of a SortedUniqueList determines the position of its elements, so"
				+ " explicit positioning is not supported",
			exception.getMessage());
		assertEquals(Arrays.asList("a", "c"), list);
		assertEquals(1, iterator.nextIndex());
		assertTrue(iterator.hasNext());

		ListIterator<String> fromIndex = list.listIterator(1);
		assertThrows(UnsupportedOperationException.class, () -> fromIndex.add("b"));
		assertEquals(Arrays.asList("a", "c"), list);
		assertEquals("c", fromIndex.next());
	}

	/**
	 * Test the method set of the list iterator of a {@link SortedUniqueList} without a comparator:
	 * it sets through set(int, E) of the list, which swaps an element the list holds at another
	 * index without a comparator, set(c) after the first next() gives [c, b, a] for [a, b, c], and
	 * the iterator goes on with b and a, the element it returned first (#78)
	 */
	@Test
	public void testListIteratorSetWithoutComparator()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"));
		ListIterator<String> iterator = list.listIterator();
		assertEquals("a", iterator.next());

		iterator.set("c");
		assertEquals(Arrays.asList("c", "b", "a"), list);
		assertEquals("b", iterator.next());
		assertEquals("a", iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 * Test that the set of the list iterator of a {@link SortedUniqueList} without a comparator
	 * fails fast: after an add to the list that the iterator did not make it throws a
	 * ConcurrentModificationException, and the list stays [a, b, c, d]; before and after the
	 * decision of 2026-11-04 on #91
	 */
	@Test
	public void testListIteratorSetWithoutComparatorFailsFast()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"));
		ListIterator<String> iterator = list.listIterator();
		assertEquals("a", iterator.next());

		list.add("d");
		assertThrows(ConcurrentModificationException.class, () -> iterator.set("x"));
		assertEquals(Arrays.asList("a", "b", "c", "d"), list);
	}

	/**
	 * Test set and add of the list iterators of the view of
	 * {@link SortedUniqueList#subList(int, int)}, of listIterator() and of listIterator(int): they
	 * throw an UnsupportedOperationException, for an element the list holds and for a new one, and
	 * neither the list nor the view changes
	 */
	@Test
	public void testSubListListIteratorSetAndAdd()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c", "d"),
			Comparator.naturalOrder());
		List<String> view = list.subList(1, 3);

		ListIterator<String> iterator = view.listIterator();
		assertEquals("b", iterator.next());
		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> iterator.set("z"));
		assertEquals(
			"A subList view of a SortedUniqueList reads and removes only; set and add through"
				+ " the list itself",
			exception.getMessage());
		assertThrows(UnsupportedOperationException.class, () -> iterator.set("a"));
		assertThrows(UnsupportedOperationException.class, () -> iterator.add("e"));

		ListIterator<String> fromIndex = view.listIterator(1);
		assertThrows(UnsupportedOperationException.class, () -> fromIndex.add("e"));
		assertEquals("c", fromIndex.next());
		assertEquals(Arrays.asList("a", "b", "c", "d"), list);
		assertEquals(Arrays.asList("b", "c"), view);
	}

	/**
	 * Test that the view of {@link SortedUniqueList#subList(int, int)} reads as the view of
	 * ArrayList.subList reads; before and after the fix of #78
	 */
	@Test
	public void testSubListReads()
	{
		SubListViewAssertions
			.assertReads(elements -> new SortedUniqueList<>(elements, Comparator.naturalOrder()));
	}

	/**
	 * Test that the view of {@link SortedUniqueList#subList(int, int)} removes as the view of
	 * ArrayList.subList removes, so that the list stays in the order of its comparator and holds
	 * each element once; before and after the fix of #78
	 */
	@Test
	public void testSubListRemoves()
	{
		SubListViewAssertions
			.assertRemoves(elements -> new SortedUniqueList<>(elements, Comparator.naturalOrder()));
	}

	/**
	 * Test that subList(from, to).clear() removes the range from a {@link SortedUniqueList}; before
	 * and after the fix of #78
	 */
	@Test
	public void testSubListClearRemovesTheRange()
	{
		SubListViewAssertions.assertClearRemovesTheRange(
			elements -> new SortedUniqueList<>(elements, Comparator.naturalOrder()));
	}

	/**
	 * Test that the view of a view of {@link SortedUniqueList#subList(int, int)} reads and removes
	 * as the view does; before and after the fix of #78
	 */
	@Test
	public void testSubListOfSubListRemoves()
	{
		SubListViewAssertions.assertSubListOfSubListRemoves(
			elements -> new SortedUniqueList<>(elements, Comparator.naturalOrder()));
	}

	/**
	 * Test the method {@link SortedUniqueList#setComparator(Comparator)} with a comparator whose
	 * sort throws: it throws that exception, and the list keeps its comparator and its order; null,
	 * the natural order, throws a ClassCastException for two Objects, and the natural order throws
	 * a NullPointerException for [b, a, c, null] without a comparator, after which the list is
	 * still [b, a, c, null] without one, where a sort in place would have left [a, b, c, null]
	 * (#87)
	 */
	@Test
	public void testSetComparatorWhenTheSortThrows()
	{
		Comparator<Object> equal = (first, second) -> 0;
		SortedUniqueList<Object> objects = new SortedUniqueList<>(
			Arrays.asList(new Object(), new Object()), equal);
		List<Object> order = List.copyOf(objects);
		assertThrows(ClassCastException.class, () -> objects.setComparator(null));
		assertSame(equal, objects.getComparator());
		assertEquals(order, objects);

		SortedUniqueList<String> list = new SortedUniqueList<>(Arrays.asList("b", "a", "c", null));
		assertThrows(NullPointerException.class,
			() -> list.setComparator(Comparator.naturalOrder()));
		assertNull(list.getComparator());
		assertEquals(Arrays.asList("b", "a", "c", null), list);
	}

	/**
	 * Test that {@link SortedUniqueList#reversed()} reads the list backwards and removes from it:
	 * get, size, contains, indexOf, iterator, equals, removeFirst, remove(Object) and removeIf of
	 * the view, and that reversed() of the view gives the order of the list again; before and after
	 * the fix of #87
	 */
	@Test
	public void testReversedReadsAndRemoves()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c", "d"),
			Comparator.naturalOrder());
		List<String> reversed = list.reversed();

		assertEquals(Arrays.asList("d", "c", "b", "a"), reversed);
		assertEquals("d", reversed.get(0));
		assertEquals("a", reversed.getLast());
		assertEquals(4, reversed.size());
		assertTrue(reversed.contains("b"));
		assertEquals(2, reversed.indexOf("b"));
		assertEquals("d", reversed.iterator().next());
		assertEquals(list, reversed.reversed());

		assertEquals("d", reversed.removeFirst());
		assertTrue(reversed.remove("b"));
		assertTrue(reversed.removeIf("a"::equals));
		assertEquals(Arrays.asList("c"), list);
		assertEquals(Arrays.asList("c"), reversed);
	}
}
