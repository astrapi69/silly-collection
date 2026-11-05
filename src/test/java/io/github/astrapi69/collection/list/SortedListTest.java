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
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.comparator.object.StringComparator;

/**
 * The unit test class for the class {@link SortedList}
 */
public class SortedListTest
{

	/**
	 * The message of the UnsupportedOperationException of set, addFirst, addLast and the add and
	 * set of the list iterator with a comparator, as TreeSet.addFirst says it, and the start of the
	 * one of replaceAll
	 */
	private static final String POSITIONING = "The comparator of a SortedList determines the"
		+ " position of its elements, so explicit positioning is not supported";

	/**
	 * The message of the UnsupportedOperationException of sort
	 */
	private static final String SORT = "A SortedList keeps the order of its comparator;"
		+ " setComparator(Comparator) changes it";

	/**
	 * The message of the UnsupportedOperationException of the view of subList
	 */
	private static final String SUB_LIST = "A subList view of a SortedList reads and removes only;"
		+ " set and add through the list itself";

	/**
	 * The message of the UnsupportedOperationException of the view of reversed
	 */
	private static final String REVERSED = "The reversed view of a SortedList reads and removes"
		+ " only; add through the list itself";

	/**
	 * A new {@link SortedList} of the given elements in the order of the given comparator
	 *
	 * @param comparator
	 *            the comparator, null for none
	 * @param elements
	 *            the elements
	 * @return the list
	 */
	private static SortedList<String> sorted(final Comparator<String> comparator,
		final String... elements)
	{
		return new SortedList<>(Arrays.asList(elements), comparator);
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
	 * A call that changes a {@link SortedList}, what it returns and the list afterwards
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
	 * The calls that change a {@link SortedList}: the add methods put an element at its place in
	 * the order of the comparator, whatever index they are given, and append it without a
	 * comparator; without a comparator set, replaceAll and the algorithms of {@link Collections}
	 * work as on an ArrayList, and with one replaceAll replaces in place where the values keep the
	 * order. "The names" are "", Emil, Anton, Anton, Anton, Emil, ""
	 *
	 * @return the cases
	 */
	static Stream<ChangeCase> changes()
	{
		final List<String> names = Arrays.asList("", "Emil", "Anton", "Anton", "Anton", "Emil", "");
		final List<String> shuffled = new ArrayList<>(List.of("a", "b", "c", "d"));
		Collections.shuffle(shuffled, new Random(77));
		return Stream.of(new ChangeCase(
			"add of Leonidas, Berta, Caesar, Dora, Emil, Anton, initial capacity 6, no comparator: appended",
			() -> new SortedList<>(6),
			list -> addEach(list, "Leonidas", "Berta", "Caesar", "Dora", "Emil", "Anton"),
			List.of(true, true, true, true, true, true),
			List.of("Leonidas", "Berta", "Caesar", "Dora", "Emil", "Anton")),
			new ChangeCase(
				"add(0, e) of Leonidas, Berta, Caesar, Dora, Emil, Anton, no comparator: appended, the index ignored",
				SortedList::new, list -> {
					for (final String name : List.of("Leonidas", "Berta", "Caesar", "Dora", "Emil",
						"Anton"))
					{
						list.add(0, name);
					}
					return null;
				}, null, List.of("Leonidas", "Berta", "Caesar", "Dora", "Emil", "Anton")),
			new ChangeCase("add of Leonidas, Berta, Leonidas, no comparator: appended, each true",
				SortedList::new, list -> addEach(list, "Leonidas", "Berta", "Leonidas"),
				List.of(true, true, true), List.of("Leonidas", "Berta", "Leonidas")),
			new ChangeCase("add of Leonidas, Berta, Leonidas, natural order: sorted in, each true",
				() -> sorted(Comparator.naturalOrder()),
				list -> addEach(list, "Leonidas", "Berta", "Leonidas"), List.of(true, true, true),
				List.of("Berta", "Leonidas", "Leonidas")),
			new ChangeCase(
				"add of A and B to a, b, case insensitive: next to the one held equal, each true (#40)",
				() -> sorted(String.CASE_INSENSITIVE_ORDER, "a", "b"),
				list -> addEach(list, "A", "B"), List.of(true, true), List.of("A", "a", "B", "b")),
			new ChangeCase("add(-1, Caesar) and add(5, Anton), no comparator: appended (#40)",
				SortedList::new, list -> {
					list.add(-1, "Caesar");
					list.add(5, "Anton");
					return null;
				}, null, List.of("Caesar", "Anton")),
			new ChangeCase(
				"add(-1, Caesar) and add(5, Anton), natural order: sorted in, the index ignored (#40)",
				() -> sorted(Comparator.naturalOrder()), list -> {
					list.add(-1, "Caesar");
					list.add(5, "Anton");
					return null;
				}, null, List.of("Anton", "Caesar")),
			new ChangeCase("addAll of the names to Emil, Caesar, StringComparator: sorted in",
				() -> {
					final List<String> list = new SortedList<>(new StringComparator());
					list.add("Emil");
					list.add("Caesar");
					return list;
				}, list -> list.addAll(names), true,
				List.of("", "", "Anton", "Anton", "Anton", "Caesar", "Emil", "Emil", "Emil")),
			new ChangeCase("addAll of nothing to Emil, Caesar, no comparator: unchanged, false",
				() -> sorted(null, "Emil", "Caesar"), list -> list.addAll(List.of()), false,
				List.of("Emil", "Caesar")),
			new ChangeCase("addAll(1, the names) to Emil, Caesar, no comparator: appended",
				() -> sorted(null, "Emil", "Caesar"), list -> list.addAll(1, names), true,
				List.of("Emil", "Caesar", "", "Emil", "Anton", "Anton", "Anton", "Emil", "")),
			new ChangeCase("addAll(1, nothing) to Emil, Caesar, no comparator: unchanged, false",
				() -> sorted(null, "Emil", "Caesar"), list -> list.addAll(1, List.of()), false,
				List.of("Emil", "Caesar")),
			new ChangeCase("addAll of Caesar, Emil, Anton to Emil, natural order: sorted in",
				() -> sorted(Comparator.naturalOrder(), "Emil"),
				list -> list.addAll(Arrays.asList("Caesar", "Emil", "Anton")), true,
				List.of("Anton", "Caesar", "Emil", "Emil")),
			new ChangeCase("addAll of nothing to Emil, natural order: unchanged, false",
				() -> sorted(Comparator.naturalOrder(), "Emil"),
				list -> list.addAll(Collections.emptyList()), false, List.of("Emil")),
			new ChangeCase(
				"addAll(0, Caesar, Anton) to Emil, natural order: sorted in, not at the index",
				() -> sorted(Comparator.naturalOrder(), "Emil"),
				list -> list.addAll(0, Arrays.asList("Caesar", "Anton")), true,
				List.of("Anton", "Caesar", "Emil")),
			new ChangeCase("addAll(0, nothing) to Emil, natural order: unchanged, false",
				() -> sorted(Comparator.naturalOrder(), "Emil"),
				list -> list.addAll(0, Collections.emptyList()), false, List.of("Emil")),
			new ChangeCase(
				"addAll(-1, Caesar, Anton) and addAll(5, Berta), no comparator: appended (#40)",
				SortedList::new,
				list -> List.of(list.addAll(-1, Arrays.asList("Caesar", "Anton")),
					list.addAll(5, Arrays.asList("Berta"))),
				List.of(true, true), List.of("Caesar", "Anton", "Berta")),
			new ChangeCase(
				"addAll(-1, Caesar, Anton) and addAll(5, Berta), natural order: sorted in (#40)",
				() -> sorted(Comparator.naturalOrder()),
				list -> List.of(list.addAll(-1, Arrays.asList("Caesar", "Anton")),
					list.addAll(5, Arrays.asList("Berta"))),
				List.of(true, true), List.of("Anton", "Berta", "Caesar")),
			new ChangeCase(
				"addAll of Caesar, Emil, Anton to Emil, no comparator: appended, Emil again (#66)",
				() -> sorted(null, "Emil"),
				list -> list.addAll(Arrays.asList("Caesar", "Emil", "Anton")), true,
				List.of("Emil", "Caesar", "Emil", "Anton")),
			new ChangeCase("addAll of a, b itself, natural order: a copy sorted in (#77)",
				() -> sorted(Comparator.naturalOrder(), "a", "b"), list -> list.addAll(list), true,
				List.of("a", "a", "b", "b")),
			new ChangeCase("addAll of b, a itself, no comparator: a copy appended (#77)",
				() -> sorted(null, "b", "a"), list -> list.addAll(list), true,
				List.of("b", "a", "b", "a")),
			new ChangeCase("set(0, b) of c, a, no comparator: replaced in place, returns c",
				() -> sorted(null, "c", "a"), list -> list.set(0, "b"), "c", List.of("b", "a")),
			new ChangeCase("replaceAll with upper case of c, a, no comparator: C, A",
				() -> sorted(null, "c", "a"), list -> {
					list.replaceAll(String::toUpperCase);
					return null;
				}, null, List.of("C", "A")),
			new ChangeCase(
				"replaceAll doubling a, c, e, natural order: aa, cc, ee, the order kept (#78)",
				() -> sorted(Comparator.naturalOrder(), "a", "c", "e"), list -> {
					list.replaceAll(element -> element + element);
					return null;
				}, null, List.of("aa", "cc", "ee")),
			new ChangeCase(
				"replaceAll with b of a, c, e, natural order: b, b, b, equal neighbours (#78)",
				() -> sorted(Comparator.naturalOrder(), "a", "c", "e"), list -> {
					list.replaceAll(element -> "b");
					return null;
				}, null, List.of("b", "b", "b")),
			new ChangeCase("Collections.swap(0, 3) of a, b, c, d, no comparator: d, b, c, a",
				() -> sorted(null, "a", "b", "c", "d"), list -> {
					Collections.swap(list, 0, 3);
					return null;
				}, null, List.of("d", "b", "c", "a")),
			new ChangeCase("Collections.reverse of a, b, c, d, no comparator: d, c, b, a",
				() -> sorted(null, "a", "b", "c", "d"), list -> {
					Collections.reverse(list);
					return null;
				}, null, List.of("d", "c", "b", "a")),
			new ChangeCase(
				"Collections.shuffle of a, b, c, d with the seed 77, no comparator: as an ArrayList",
				() -> sorted(null, "a", "b", "c", "d"), list -> {
					Collections.shuffle(list, new Random(77));
					return null;
				}, null, shuffled),
			new ChangeCase("Collections.rotate by 1 of a, b, c, d, no comparator: d, a, b, c",
				() -> sorted(null, "a", "b", "c", "d"), list -> {
					Collections.rotate(list, 1);
					return null;
				}, null, List.of("d", "a", "b", "c")));
	}

	/**
	 * Test the calls that change a {@link SortedList}: what each returns and the list afterwards
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
	 * A call on a {@link SortedList} that throws, the exception and the list afterwards
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
	 * The calls on a {@link SortedList} that throw: with a comparator what would put an element at
	 * a position the caller chooses or reorder the list against its comparator, as in a SortedSet
	 * (#77, #78, #87, decided on #91), the writes through the views of subList and reversed (#78,
	 * #87), sort, addFirst and addLast also without a comparator, and replaceAll with an operator
	 * that fails or a value the comparator cannot compare; a refused call leaves the list as it is.
	 * The algorithms of {@link Collections} throw at their first set; with a set that kept the
	 * order, swap(0, 1) left [a, a, c, d], reverse of [a, b] [a, a], shuffle [a, b, d, d], rotate
	 * by 1 [a, a, b, c], fill with a [a, a, a] and copy [a, c, f]
	 *
	 * @return the cases
	 */
	static Stream<RefusalCase> refusals()
	{
		final Supplier<List<String>> ace = () -> sorted(Comparator.naturalOrder(), "a", "c", "e");
		final Supplier<List<String>> ac = () -> sorted(Comparator.naturalOrder(), "a", "c");
		final Supplier<List<String>> bdf = () -> sorted(Comparator.naturalOrder(), "b", "d", "f");
		final Supplier<List<String>> abcd = () -> sorted(Comparator.naturalOrder(), "a", "b", "c",
			"d");
		final Supplier<List<String>> abc = () -> sorted(Comparator.naturalOrder(), "a", "b", "c");
		final UnaryOperator<String> failsForB = element -> {
			if ("b".equals(element))
			{
				throw new IllegalStateException("no value for b");
			}
			return element.toUpperCase();
		};
		return Stream.of(
			refused("set(0, d) of a, c, e, natural order, against the order (#78)", ace,
				list -> list.set(0, "d"), POSITIONING, "a", "c", "e"),
			refused("set(2, b) of a, c, e, natural order, against the order (#78)", ace,
				list -> list.set(2, "b"), null, "a", "c", "e"),
			refused("set(1, d) of a, c, e, natural order, an element that keeps the order (#91)",
				ace, list -> list.set(1, "d"), null, "a", "c", "e"),
			refused("set(0, null) of a, c, natural order, a value it cannot compare (#77, #78)", ac,
				list -> list.set(0, null), null, "a", "c"),
			refused("set(2, b) of a, c, natural order, an index out of range (#78)", ac,
				list -> list.set(2, "b"), null, "a", "c"),
			refused("set(-1, b) of a, c, natural order, a negative index (#78)", ac,
				list -> list.set(-1, "b"), null, "a", "c"),
			refused("set(0, a) of b, d, f, natural order, keeps the order at the first index (#91)",
				bdf, list -> list.set(0, "a"), null, "b", "d", "f"),
			refused("set(0, d) of b, d, f, natural order, equal to the next neighbour (#91)", bdf,
				list -> list.set(0, "d"), null, "b", "d", "f"),
			refused("set(1, b) of b, d, f, natural order, equal to the neighbour in front (#91)",
				bdf, list -> list.set(1, "b"), null, "b", "d", "f"),
			refused("set(1, f) of b, d, f, natural order, equal to the next neighbour (#91)", bdf,
				list -> list.set(1, "f"), null, "b", "d", "f"),
			refused("set(2, d) of b, d, f, natural order, equal to the neighbour in front (#91)",
				bdf, list -> list.set(2, "d"), null, "b", "d", "f"),
			refused("set(2, z) of b, d, f, natural order, keeps the order at the last index (#91)",
				bdf, list -> list.set(2, "z"), null, "b", "d", "f"),
			refused("set(0, z) of m, natural order, the only element (#91)",
				() -> sorted(Comparator.naturalOrder(), "m"), list -> list.set(0, "z"), null, "m"),
			refused("set(1, B) of b, d, f, case insensitive, held equal to its neighbour (#91)",
				() -> sorted(String.CASE_INSENSITIVE_ORDER, "b", "d", "f"),
				list -> list.set(1, "B"), null, "b", "d", "f"),
			new RefusalCase("set(2, b) of c, a, no comparator: Index 2 out of bounds (#77)",
				() -> sorted(null, "c", "a"), list -> list.set(2, "b"),
				IndexOutOfBoundsException.class, "Index 2 out of bounds for length 2",
				List.of("c", "a")),
			new RefusalCase("set(-1, b) of c, a, no comparator: Index -1 out of bounds (#77)",
				() -> sorted(null, "c", "a"), list -> list.set(-1, "b"),
				IndexOutOfBoundsException.class, "Index -1 out of bounds for length 2",
				List.of("c", "a")),
			refused("replaceAll giving f for a of a, c, e, natural order: c at 1 breaks it (#78)",
				ace, list -> list.replaceAll(element -> "a".equals(element) ? "f" : element),
				POSITIONING + ": the value c at the index 1 would break its order", "a", "c", "e"),
			refused("replaceAll giving zzz for a of a, bb, ccc, by length, against the order (#78)",
				() -> sorted(Comparator.comparingInt(String::length), "a", "bb", "ccc"),
				list -> list.replaceAll(element -> "a".equals(element) ? "zzz" : element), null,
				"a", "bb", "ccc"),
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
			refused("Collections.sort(reverse order) of c, b, a, reverse order (#77)",
				() -> sorted(Comparator.reverseOrder(), "a", "b", "c"),
				list -> Collections.sort(list, Comparator.reverseOrder()), null, "c", "b", "a"),
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
			refused(
				"Collections.swap(1, 1) of a, b, c, d, natural order, an index with itself (#91)",
				abcd, list -> Collections.swap(list, 1, 1), null, "a", "b", "c", "d"),
			refused("Collections.swap(0, 3) of a, b, c, d, natural order (#91)", abcd,
				list -> Collections.swap(list, 0, 3), null, "a", "b", "c", "d"),
			refused("Collections.swap(0, 1) of a, b, c, d, natural order, two neighbours (#91)",
				abcd, list -> Collections.swap(list, 0, 1), null, "a", "b", "c", "d"),
			refused("Collections.reverse of a, b, c, d, natural order (#91)", abcd,
				Collections::reverse, null, "a", "b", "c", "d"),
			refused("Collections.reverse of a, b, natural order (#91)",
				() -> sorted(Comparator.naturalOrder(), "a", "b"), Collections::reverse, null, "a",
				"b"),
			refused("Collections.shuffle with the seed 77 of a, b, c, d, natural order (#91)", abcd,
				list -> Collections.shuffle(list, new Random(77)), null, "a", "b", "c", "d"),
			refused("Collections.rotate by 1 of a, b, c, d, natural order (#91)", abcd,
				list -> Collections.rotate(list, 1), null, "a", "b", "c", "d"),
			refused("Collections.rotate by -1 of a, b, c, d, natural order (#91)", abcd,
				list -> Collections.rotate(list, -1), null, "a", "b", "c", "d"),
			refused("Collections.replaceAll of b with x in a, b, c, d, natural order (#91)", abcd,
				list -> Collections.replaceAll(list, "b", "x"), null, "a", "b", "c", "d"),
			refused("Collections.swap(1, 2) of a, cc, bb, ddd, by length, held equal (#91)",
				() -> sorted(Comparator.comparingInt(String::length), "a", "bb", "cc", "ddd"),
				list -> Collections.swap(list, 1, 2), null, "a", "cc", "bb", "ddd"),
			refused("Collections.reverse of cc, bb, aa, by length, all held equal (#91)",
				() -> sorted(Comparator.comparingInt(String::length), "aa", "bb", "cc"),
				Collections::reverse, null, "cc", "bb", "aa"),
			refused("Collections.fill with a of b, c, d, natural order (#91)",
				() -> sorted(Comparator.naturalOrder(), "b", "c", "d"),
				list -> Collections.fill(list, "a"), null, "b", "c", "d"),
			refused("Collections.fill with z of b, c, d, natural order (#91)",
				() -> sorted(Comparator.naturalOrder(), "b", "c", "d"),
				list -> Collections.fill(list, "z"), null, "b", "c", "d"),
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
			refused("subList(0, 1).add(z) of a, c, e, the last case of #78", ace,
				list -> list.subList(0, 1).add("z"), null, "a", "c", "e"),
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
	 * Test the calls on a {@link SortedList} that throw: the exception, its message where a case
	 * names it, and the list afterwards, unchanged where the call is refused
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
		list = new SortedList<>();
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
		list = new SortedList<>(comparator);
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
		((SortedList<String>)list).setComparator(comparator);

		expected = list.indexOf(alex) == 2;
		assertTrue(expected);
		expected = list.indexOf(bill) == 3;
		assertTrue(expected);
		expected = list.indexOf(leo) == 4;
		assertTrue(expected);

		// test with the other constructor
		SortedList<String> sortedArrayList = new SortedList<>(collection, comparator);

		expected = sortedArrayList.indexOf(alex) == 2;
		assertTrue(expected);
		expected = sortedArrayList.indexOf(bill) == 3;
		assertTrue(expected);
		expected = sortedArrayList.indexOf(leo) == 4;
		assertTrue(expected);
		assertEquals(comparator, sortedArrayList.getComparator());
		comparator = StringComparator.of(true);
		sortedArrayList.setComparator(comparator);
		assertEquals(comparator, sortedArrayList.getComparator());

	}

	/**
	 * Test the method {@link SortedList#setComparator(Comparator)}: the elements the list holds are
	 * sorted with the new comparator, and an element added afterwards is inserted in its order
	 */
	@Test
	public void testSetComparator()
	{
		SortedList<String> list = new SortedList<>();
		list.add("Berta");
		list.add("Anton");
		list.add("Caesar");
		assertEquals(Arrays.asList("Berta", "Anton", "Caesar"), list);

		list.setComparator(Comparator.reverseOrder());
		assertEquals(Arrays.asList("Caesar", "Berta", "Anton"), list);

		list.add("Dora");
		assertEquals(Arrays.asList("Dora", "Caesar", "Berta", "Anton"), list);
	}

	/**
	 * Test the method {@link SortedList#add(Object)} with several elements the comparator holds
	 * equal to the given one: it goes in front of one of them, so not after the last of them, and
	 * the list stays in the order of the comparator (#66)
	 */
	@Test
	public void testAddWithSeveralElementsTheComparatorHoldsEqual()
	{
		List<String> list = new SortedList<>(Comparator.comparingInt(String::length));
		list.addAll(Arrays.asList("x", "aa", "bb", "cc", "zzz"));

		assertTrue(list.add("dd"));
		assertEquals("x", list.get(0));
		assertEquals("zzz", list.get(5));
		int index = list.indexOf("dd");
		assertTrue(1 <= index && index <= 3, "dd is not in front of one of aa, bb and cc: " + list);
	}

	/**
	 * Test the method {@link SortedList#set(int, Object)} with a comparator while an iterator runs
	 * over the list: set(1, d) of [a, c, e] after the first next() throws the
	 * UnsupportedOperationException, the list stays [a, c, e], and the iterator goes on with c and
	 * e (#78, decided on #91)
	 */
	@Test
	public void testSetWithComparatorWhileAnIteratorRuns()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "c", "e"),
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
	 * Test the method set of the list iterator, of listIterator() and of listIterator(int), with a
	 * comparator: it calls set(int, E) of the list at the index of the element last returned, which
	 * throws the UnsupportedOperationException whatever its arguments, z and a after next()
	 * returned a, and b after previous() returned c; the list stays [a, c], and the iterators go on
	 * with c and with a (#78, decided on #91)
	 */
	@Test
	public void testListIteratorSet()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "c"), Comparator.naturalOrder());
		ListIterator<String> iterator = list.listIterator();
		assertEquals("a", iterator.next());
		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> iterator.set("z"));
		assertEquals("The comparator of a SortedList determines the position of its elements, so"
			+ " explicit positioning is not supported", exception.getMessage());
		assertThrows(UnsupportedOperationException.class, () -> iterator.set("a"));
		assertEquals(Arrays.asList("a", "c"), list);
		assertEquals("c", iterator.next());

		ListIterator<String> fromIndex = list.listIterator(2);
		assertEquals("c", fromIndex.previous());
		assertThrows(UnsupportedOperationException.class, () -> fromIndex.set("b"));
		assertEquals(Arrays.asList("a", "c"), list);
		assertEquals("a", fromIndex.previous());
	}

	/**
	 * Test the method set of the list iterator without an element to replace: before the first
	 * next() and after a remove it throws an IllegalStateException, and after a structural
	 * modification of the list that the iterator did not make a ConcurrentModificationException, as
	 * the list iterator of ArrayList does, and the list does not change (#78)
	 */
	@Test
	public void testListIteratorSetWithoutElementToReplace()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "b", "c"),
			Comparator.naturalOrder());
		ListIterator<String> iterator = list.listIterator();
		assertThrows(IllegalStateException.class, () -> iterator.set("a"));

		assertEquals("a", iterator.next());
		iterator.remove();
		assertThrows(IllegalStateException.class, () -> iterator.set("a"));
		assertEquals(Arrays.asList("b", "c"), list);

		assertEquals("b", iterator.next());
		list.add("d");
		assertThrows(ConcurrentModificationException.class, () -> iterator.set("b"));
		assertEquals(Arrays.asList("b", "c", "d"), list);
	}

	/**
	 * Test the method add of the list iterator, with and without a comparator: it throws an
	 * UnsupportedOperationException with the message of addFirst, as TreeSet.addFirst does, the
	 * list stays [a, c], the cursor does not move, and next() returns a (#77, #78)
	 */
	@Test
	public void testListIteratorAdd()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "c"), Comparator.naturalOrder());
		ListIterator<String> iterator = list.listIterator();
		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> iterator.add("z"));
		assertEquals("The comparator of a SortedList determines the position of it"
			+ "s elements, so explicit positioning is not supported", exception.getMessage());
		assertEquals(Arrays.asList("a", "c"), list);
		assertEquals(0, iterator.nextIndex());
		assertEquals("a", iterator.next());

		List<String> withoutComparator = new SortedList<>(Arrays.asList("a", "c"));
		ListIterator<String> fromIndex = withoutComparator.listIterator(0);
		assertThrows(UnsupportedOperationException.class, () -> fromIndex.add("z"));
		assertEquals(Arrays.asList("a", "c"), withoutComparator);
		assertEquals("a", fromIndex.next());
	}

	/**
	 * Test the method set of the list iterator of a {@link SortedList} without a comparator: it
	 * sets through set(int, E) of the list, which replaces in place without a comparator, set(b)
	 * after the first next() gives [b, a] for [c, a], and the iterator goes on with a (#78)
	 */
	@Test
	public void testListIteratorSetWithoutComparator()
	{
		List<String> list = new SortedList<>(Arrays.asList("c", "a"));
		ListIterator<String> iterator = list.listIterator();
		assertEquals("c", iterator.next());

		iterator.set("b");
		assertEquals(Arrays.asList("b", "a"), list);
		assertEquals("a", iterator.next());
	}

	/**
	 * Test that the set of the list iterator of a {@link SortedList} without a comparator replaces
	 * the element last returned, after previous() too, and after a remove only once next() or
	 * previous() returned an element again: [c, a, e] gives [c, x, e] with set(x) after next(),
	 * next() and previous(), remove() then gives [c, e], set throws an IllegalStateException, and
	 * set(y) after next() gives [c, y]; after an add to the list set throws a
	 * ConcurrentModificationException, and the list stays [c, y, z]; before and after the decision
	 * of 2026-11-04 on #91
	 */
	@Test
	public void testListIteratorSetWithoutComparatorReplacesTheElementLastReturned()
	{
		List<String> list = new SortedList<>(Arrays.asList("c", "a", "e"));
		ListIterator<String> iterator = list.listIterator();
		assertEquals("c", iterator.next());
		assertEquals("a", iterator.next());
		assertEquals("a", iterator.previous());
		iterator.set("x");
		assertEquals(Arrays.asList("c", "x", "e"), list);

		iterator.remove();
		assertEquals(Arrays.asList("c", "e"), list);
		assertThrows(IllegalStateException.class, () -> iterator.set("y"));
		assertEquals("e", iterator.next());
		iterator.set("y");
		assertEquals(Arrays.asList("c", "y"), list);

		list.add("z");
		assertThrows(ConcurrentModificationException.class, () -> iterator.set("w"));
		assertEquals(Arrays.asList("c", "y", "z"), list);
	}

	/**
	 * Test the list iterator forward and backward over the whole list: next(), previous(), the
	 * indexes and the NoSuchElementException at both ends, as ArrayList's list iterator gives them;
	 * before and after the fix of #77
	 */
	@Test
	public void testListIteratorWalksForwardAndBackward()
	{
		List<String> list = new SortedList<>(Arrays.asList("c", "a", "b"),
			Comparator.naturalOrder());
		ListIterator<String> iterator = list.listIterator();

		assertFalse(iterator.hasPrevious());
		assertEquals(-1, iterator.previousIndex());
		assertThrows(NoSuchElementException.class, iterator::previous);
		assertEquals("a", iterator.next());
		assertTrue(iterator.hasPrevious());
		assertEquals("b", iterator.next());
		assertEquals(2, iterator.nextIndex());
		assertEquals(1, iterator.previousIndex());
		assertTrue(iterator.hasNext());
		assertEquals("c", iterator.next());
		assertFalse(iterator.hasNext());
		assertThrows(NoSuchElementException.class, iterator::next);
		assertEquals("c", iterator.previous());
		assertEquals("b", iterator.previous());
		assertEquals("a", iterator.previous());
		assertFalse(iterator.hasPrevious());
		assertTrue(iterator.hasNext());
	}

	/**
	 * Test the method {@link SortedList#listIterator(int)}: the iterator starts at the given index,
	 * the index size() included, and an index out of range throws an IndexOutOfBoundsException with
	 * the message of ArrayList; before and after the fix of #77
	 */
	@Test
	public void testListIteratorWithIndex()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "b", "c"),
			Comparator.naturalOrder());

		ListIterator<String> iterator = list.listIterator(2);
		assertEquals(2, iterator.nextIndex());
		assertEquals("b", iterator.previous());

		iterator = list.listIterator(3);
		assertFalse(iterator.hasNext());
		assertEquals("c", iterator.previous());

		IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
			() -> list.listIterator(4));
		assertEquals("Index: 4, Size: 3", exception.getMessage());
		exception = assertThrows(IndexOutOfBoundsException.class, () -> list.listIterator(-1));
		assertEquals("Index: -1, Size: 3", exception.getMessage());
	}

	/**
	 * Test the method remove of the list iterator: it removes the element last returned by next()
	 * or previous(), the following next() returns the element behind it, and without an element
	 * returned since the last remove it throws an IllegalStateException, as ArrayList's list
	 * iterator does; before and after the fix of #77
	 */
	@Test
	public void testListIteratorRemove()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "b", "c"),
			Comparator.naturalOrder());
		ListIterator<String> iterator = list.listIterator();
		assertThrows(IllegalStateException.class, iterator::remove);

		assertEquals("a", iterator.next());
		iterator.remove();
		assertEquals(Arrays.asList("b", "c"), list);
		assertThrows(IllegalStateException.class, iterator::remove);
		assertEquals(0, iterator.nextIndex());
		assertEquals("b", iterator.next());

		assertEquals("c", iterator.next());
		assertEquals("c", iterator.previous());
		iterator.remove();
		assertEquals(Arrays.asList("b"), list);
		assertFalse(iterator.hasNext());
	}

	/**
	 * Test that the list iterator fails fast: after a structural modification of the list that is
	 * not made through the iterator, next(), previous() and remove throw a
	 * ConcurrentModificationException, as ArrayList's list iterator does; before and after the fix
	 * of #77
	 */
	@Test
	public void testListIteratorFailsFast()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "b"), Comparator.naturalOrder());

		ListIterator<String> forNext = list.listIterator();
		list.add("c");
		assertThrows(ConcurrentModificationException.class, forNext::next);

		ListIterator<String> forPrevious = list.listIterator(1);
		list.add("d");
		assertThrows(ConcurrentModificationException.class, forPrevious::previous);

		ListIterator<String> forRemove = list.listIterator();
		forRemove.next();
		list.remove("d");
		assertThrows(ConcurrentModificationException.class, forRemove::remove);
		assertEquals(Arrays.asList("a", "b", "c"), list);
	}

	/**
	 * Test set and add of the list iterators of the view of {@link SortedList#subList(int, int)},
	 * of listIterator() and of listIterator(int): they throw an UnsupportedOperationException, for
	 * an element the list holds and for a new one, and neither the list nor the view changes
	 */
	@Test
	public void testSubListListIteratorSetAndAdd()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "b", "c", "d"),
			Comparator.naturalOrder());
		List<String> view = list.subList(1, 3);

		ListIterator<String> iterator = view.listIterator();
		assertEquals("b", iterator.next());
		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> iterator.set("z"));
		assertEquals("A subList view of a SortedList reads and removes only; set and add through"
			+ " the list itself", exception.getMessage());
		assertThrows(UnsupportedOperationException.class, () -> iterator.set("a"));
		assertThrows(UnsupportedOperationException.class, () -> iterator.add("e"));

		ListIterator<String> fromIndex = view.listIterator(1);
		assertThrows(UnsupportedOperationException.class, () -> fromIndex.add("e"));
		assertEquals("c", fromIndex.next());
		assertEquals(Arrays.asList("a", "b", "c", "d"), list);
		assertEquals(Arrays.asList("b", "c"), view);
	}

	/**
	 * Test that the view of {@link SortedList#subList(int, int)} reads as the view of
	 * ArrayList.subList reads; before and after the fix of #78
	 */
	@Test
	public void testSubListReads()
	{
		SubListViewAssertions
			.assertReads(elements -> new SortedList<>(elements, Comparator.naturalOrder()));
	}

	/**
	 * Test that the view of {@link SortedList#subList(int, int)} removes as the view of
	 * ArrayList.subList removes, so that the list stays in the order of its comparator; before and
	 * after the fix of #78
	 */
	@Test
	public void testSubListRemoves()
	{
		SubListViewAssertions
			.assertRemoves(elements -> new SortedList<>(elements, Comparator.naturalOrder()));
	}

	/**
	 * Test that subList(from, to).clear() removes the range from a {@link SortedList}; before and
	 * after the fix of #78
	 */
	@Test
	public void testSubListClearRemovesTheRange()
	{
		SubListViewAssertions.assertClearRemovesTheRange(
			elements -> new SortedList<>(elements, Comparator.naturalOrder()));
	}

	/**
	 * Test that the view of a view of {@link SortedList#subList(int, int)} reads and removes as the
	 * view does; before and after the fix of #78
	 */
	@Test
	public void testSubListOfSubListRemoves()
	{
		SubListViewAssertions.assertSubListOfSubListRemoves(
			elements -> new SortedList<>(elements, Comparator.naturalOrder()));
	}

	/**
	 * Test the method {@link SortedList#setComparator(Comparator)} with a comparator whose sort
	 * throws: it throws that exception, and the list keeps its comparator and its order; null, the
	 * natural order, throws a ClassCastException for two Objects, and the natural order throws a
	 * NullPointerException for [b, a, c, null] without a comparator, after which the list is still
	 * [b, a, c, null] without one, where a sort in place would have left [a, b, c, null] (#87)
	 */
	@Test
	public void testSetComparatorWhenTheSortThrows()
	{
		Comparator<Object> equal = (first, second) -> 0;
		SortedList<Object> objects = new SortedList<>(Arrays.asList(new Object(), new Object()),
			equal);
		List<Object> order = List.copyOf(objects);
		assertThrows(ClassCastException.class, () -> objects.setComparator(null));
		assertSame(equal, objects.getComparator());
		assertEquals(order, objects);

		SortedList<String> list = new SortedList<>(Arrays.asList("b", "a", "c", null));
		assertThrows(NullPointerException.class,
			() -> list.setComparator(Comparator.naturalOrder()));
		assertNull(list.getComparator());
		assertEquals(Arrays.asList("b", "a", "c", null), list);
	}

	/**
	 * Test that {@link SortedList#reversed()} reads the list backwards and removes from it: get,
	 * size, contains, indexOf, iterator, equals, removeFirst, remove(Object) and removeIf of the
	 * view, and that reversed() of the view gives the order of the list again; before and after the
	 * fix of #87
	 */
	@Test
	public void testReversedReadsAndRemoves()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "b", "c", "d"),
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
