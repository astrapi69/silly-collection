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
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The unit test class for the class {@link UniqueList}
 */
public class UniqueListTest
{

	/**
	 * The message of the UnsupportedOperationException of the view of subList
	 */
	private static final String SUB_LIST = "A subList view of a UniqueList reads and removes only;"
		+ " set and add through the list itself";

	/**
	 * A new {@link UniqueList} of the given elements
	 *
	 * @param elements
	 *            the elements
	 * @return the list
	 */
	private static UniqueList<String> unique(final String... elements)
	{
		return new UniqueList<>(Arrays.asList(elements));
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
	 * A call that changes a {@link UniqueList}, what it returns and the list afterwards
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
	 * The calls that change a {@link UniqueList}: add and addAll append the new elements and leave
	 * the held ones where they are; add(int, E) inserts a new element at the index and moves a held
	 * one so that it ends there (#39); addAll(int, Collection) inserts the new ones at the index;
	 * set swaps an element held at another index (#67), so that the algorithms of
	 * {@link Collections}, which swap through set, work as on an ArrayList; replaceAll replaces
	 * with distinct values; addFirst and addLast put an element first or last and move a held one
	 * there (#87). "The names" are "", Emil, Anton, Anton, Anton, Emil, ""
	 *
	 * @return the cases
	 */
	static Stream<ChangeCase> changes()
	{
		final List<String> names = Arrays.asList("", "Emil", "Anton", "Anton", "Anton", "Emil", "");
		final Supplier<List<String>> aToF = () -> unique("a", "b", "c", "d", "e", "f");
		return Stream.of(new ChangeCase(
			"add of Leonidas, Berta, Caesar, Dora, Emil, Anton, each twice, initial capacity 6: each once",
			() -> new UniqueList<>(6),
			list -> addEach(list, "Leonidas", "Leonidas", "Berta", "Berta", "Caesar", "Caesar",
				"Dora", "Dora", "Emil", "Emil", "Anton", "Anton"),
			List.of(true, false, true, false, true, false, true, false, true, false, true, false),
			List.of("Leonidas", "Berta", "Caesar", "Dora", "Emil", "Anton")),
			new ChangeCase(
				"add(0, e) of Leonidas, Berta, Caesar, Dora, Emil, Anton, each twice: each at the front once",
				UniqueList::new, list -> none(list, each -> {
					for (final String name : List.of("Leonidas", "Berta", "Caesar", "Dora", "Emil",
						"Anton"))
					{
						each.add(0, name);
						each.add(0, name);
					}
				}), null, List.of("Anton", "Emil", "Dora", "Caesar", "Berta", "Leonidas")),
			new ChangeCase("add of Leonidas, Berta, Leonidas: the second Leonidas false",
				UniqueList::new, list -> addEach(list, "Leonidas", "Berta", "Leonidas"),
				List.of(true, true, false), List.of("Leonidas", "Berta")),
			new ChangeCase("addAll of the names to Emil, Caesar: the new ones appended once",
				() -> unique("Emil", "Caesar"), list -> list.addAll(names), true,
				List.of("Emil", "Caesar", "", "Anton")),
			new ChangeCase("addAll of nothing to Emil, Caesar: unchanged, false",
				() -> unique("Emil", "Caesar"), list -> list.addAll(List.of()), false,
				List.of("Emil", "Caesar")),
			new ChangeCase("addAll of Anton, Emil, Berta to Emil: the new ones appended",
				() -> unique("Emil"), list -> list.addAll(Arrays.asList("Anton", "Emil", "Berta")),
				true, List.of("Emil", "Anton", "Berta")),
			new ChangeCase("addAll of nothing to Emil: unchanged, false", () -> unique("Emil"),
				list -> list.addAll(Collections.emptyList()), false, List.of("Emil")),
			new ChangeCase("addAll of a to a: held, unchanged, false", () -> unique("a"),
				list -> list.addAll(Arrays.asList("a")), false, List.of("a")),
			new ChangeCase("addAll(1, the names) to Emil, Caesar: the new ones at 1, once",
				() -> unique("Emil", "Caesar"), list -> list.addAll(1, names), true,
				List.of("Emil", "", "Anton", "Caesar")),
			new ChangeCase("addAll(1, nothing) to Emil, Caesar: unchanged, false",
				() -> unique("Emil", "Caesar"), list -> list.addAll(1, List.of()), false,
				List.of("Emil", "Caesar")),
			new ChangeCase("addAll(1, Anton, Berta) to Emil, Caesar: inserted at 1 in their order",
				() -> unique("Emil", "Caesar"),
				list -> list.addAll(1, Arrays.asList("Anton", "Berta")), true,
				List.of("Emil", "Anton", "Berta", "Caesar")),
			new ChangeCase("addAll(0, nothing) to Emil: unchanged, false", () -> unique("Emil"),
				list -> list.addAll(0, Collections.emptyList()), false, List.of("Emil")),
			new ChangeCase("addAll(1, a) to a: held, unchanged, false", () -> unique("a"),
				list -> list.addAll(1, Arrays.asList("a")), false, List.of("a")),
			new ChangeCase("addAll(0, a, a, b) to x: a once, then b, in front of x",
				() -> unique("x"), list -> list.addAll(0, Arrays.asList("a", "a", "b")), true,
				List.of("a", "b", "x")),
			new ChangeCase("addAll(1, b, a, c) to a, the index size(): b and c appended, a stays",
				() -> unique("a"), list -> list.addAll(1, Arrays.asList("b", "a", "c")), true,
				List.of("a", "b", "c")),
			new ChangeCase("addAll(0, b, c) to a, b: c at 0, the held b stays",
				() -> unique("a", "b"), list -> list.addAll(0, Arrays.asList("b", "c")), true,
				List.of("c", "a", "b")),
			new ChangeCase("add(2, a) of a, b, the index size(): a moves to the end (#39)",
				() -> unique("a", "b"), list -> none(list, each -> each.add(2, "a")), null,
				List.of("b", "a")),
			new ChangeCase("add(0, c) of a, b, c: c moves to the front (#39)",
				() -> unique("a", "b", "c"), list -> none(list, each -> each.add(0, "c")), null,
				List.of("c", "a", "b")),
			new ChangeCase("add(2, c) of c, a, b: c moves back to the end (#39)",
				() -> unique("c", "a", "b"), list -> none(list, each -> each.add(2, "c")), null,
				List.of("a", "b", "c")),
			new ChangeCase("add(1, a) of a, b, c: a moves to 1, in the middle (#39)",
				() -> unique("a", "b", "c"), list -> none(list, each -> each.add(1, "a")), null,
				List.of("b", "a", "c")),
			new ChangeCase("add(1, a) of b, a, c: a at 1 already, unchanged (#39)",
				() -> unique("b", "a", "c"), list -> none(list, each -> each.add(1, "a")), null,
				List.of("b", "a", "c")),
			new ChangeCase("add(1, b) of a, the index size(): b appended (#39)", () -> unique("a"),
				list -> none(list, each -> each.add(1, "b")), null, List.of("a", "b")),
			new ChangeCase("add(0, c) of a, b: c at the front (#39)", () -> unique("a", "b"),
				list -> none(list, each -> each.add(0, "c")), null, List.of("c", "a", "b")),
			new ChangeCase("add(1, d) of c, a, b: d at 1 (#39)", () -> unique("c", "a", "b"),
				list -> none(list, each -> each.add(1, "d")), null, List.of("c", "d", "a", "b")),
			new ChangeCase("set(0, c) of a, b, c: c held at 2 swaps with a, returns a (#67)",
				() -> unique("a", "b", "c"), list -> list.set(0, "c"), "a", List.of("c", "b", "a")),
			new ChangeCase("set(2, a) of a, b, c: a held at 0 swaps with c, returns c (#67)",
				() -> unique("a", "b", "c"), list -> list.set(2, "a"), "c", List.of("c", "b", "a")),
			new ChangeCase("Collections.swap(0, 3) of a, b, c, d: d, b, c, a (#67)",
				() -> unique("a", "b", "c", "d"),
				list -> none(list, each -> Collections.swap(each, 0, 3)), null,
				List.of("d", "b", "c", "a")),
			new ChangeCase("Collections.swap(2, 1) of d, b, c, a: d, c, b, a (#67)",
				() -> unique("d", "b", "c", "a"),
				list -> none(list, each -> Collections.swap(each, 2, 1)), null,
				List.of("d", "c", "b", "a")),
			new ChangeCase("Collections.swap(1, 1) of d, c, b, a, an index with itself: unchanged",
				() -> unique("d", "c", "b", "a"),
				list -> none(list, each -> Collections.swap(each, 1, 1)), null,
				List.of("d", "c", "b", "a")),
			new ChangeCase("Collections.reverse of a to e, an odd size: e to a (#67)",
				() -> unique("a", "b", "c", "d", "e"), list -> none(list, Collections::reverse),
				null, List.of("e", "d", "c", "b", "a")),
			new ChangeCase("Collections.reverse of a, b, c, d, an even size: d, c, b, a (#67)",
				() -> unique("a", "b", "c", "d"), list -> none(list, Collections::reverse), null,
				List.of("d", "c", "b", "a")),
			new ChangeCase(
				"Collections.shuffle with the seed 67 of a to h: a, b, d, h, e, g, c, f, as an ArrayList (#67)",
				() -> unique("a", "b", "c", "d", "e", "f", "g", "h"),
				list -> none(list, each -> Collections.shuffle(each, new Random(67))), null,
				List.of("a", "b", "d", "h", "e", "g", "c", "f")),
			new ChangeCase("Collections.rotate by 1 of a to f, one cycle: f, a, b, c, d, e (#67)",
				aToF, list -> none(list, each -> Collections.rotate(each, 1)), null,
				List.of("f", "a", "b", "c", "d", "e")),
			new ChangeCase("Collections.rotate by -1 of a to f, one cycle: b, c, d, e, f, a (#67)",
				aToF, list -> none(list, each -> Collections.rotate(each, -1)), null,
				List.of("b", "c", "d", "e", "f", "a")),
			new ChangeCase("Collections.rotate by 2 of a to f, two cycles: e, f, a, b, c, d (#67)",
				aToF, list -> none(list, each -> Collections.rotate(each, 2)), null,
				List.of("e", "f", "a", "b", "c", "d")),
			new ChangeCase(
				"Collections.rotate by 3 of a to f, three cycles: d, e, f, a, b, c (#67)", aToF,
				list -> none(list, each -> Collections.rotate(each, 3)), null,
				List.of("d", "e", "f", "a", "b", "c")),
			new ChangeCase("replaceAll with upper case of a, b, c: A, B, C",
				() -> unique("a", "b", "c"),
				list -> none(list, each -> each.replaceAll(String::toUpperCase)), null,
				List.of("A", "B", "C")),
			new ChangeCase(
				"replaceAll giving b, c, a for a, b, c: b, c, a, each a value the list holds",
				() -> unique("a", "b", "c"),
				list -> none(list,
					each -> each.replaceAll(
						element -> element.equals("a") ? "b" : element.equals("b") ? "c" : "a")),
				null, List.of("b", "c", "a")),
			new ChangeCase("addLast(a) of a, b: a moves to the end (#87)", () -> unique("a", "b"),
				list -> none(list, each -> each.addLast("a")), null, List.of("b", "a")),
			new ChangeCase("addLast(a) of b, a: a last already, unchanged (#87)",
				() -> unique("b", "a"), list -> none(list, each -> each.addLast("a")), null,
				List.of("b", "a")),
			new ChangeCase("addFirst(z) of a, b: z first (#87)", () -> unique("a", "b"),
				list -> none(list, each -> each.addFirst("z")), null, List.of("z", "a", "b")),
			new ChangeCase("addLast(y) of z, a, b: y last (#87)", () -> unique("z", "a", "b"),
				list -> none(list, each -> each.addLast("y")), null, List.of("z", "a", "b", "y")),
			new ChangeCase("addFirst(b) of z, a, b, y: b moves to the front (#87)",
				() -> unique("z", "a", "b", "y"), list -> none(list, each -> each.addFirst("b")),
				null, List.of("b", "z", "a", "y")));
	}

	/**
	 * Test the calls that change a {@link UniqueList}: what each returns and the list afterwards
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
	 * A call on a {@link UniqueList} that throws, the exception and the list afterwards
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
	 * A call that throws an IndexOutOfBoundsException and leaves the list unchanged
	 *
	 * @param description
	 *            the name of the case
	 * @param list
	 *            makes the list
	 * @param call
	 *            the call
	 * @param message
	 *            the message of the exception, the one of ArrayList
	 * @param expected
	 *            the elements of the list, before and after the call
	 * @return the case
	 */
	private static RefusalCase outOfBounds(final String description,
		final Supplier<List<String>> list, final Consumer<List<String>> call, final String message,
		final String... expected)
	{
		return new RefusalCase(description, list, call, IndexOutOfBoundsException.class, message,
			List.of(expected));
	}

	/**
	 * The calls on a {@link UniqueList} that throw: an index out of range throws the
	 * IndexOutOfBoundsException of ArrayList, for a new element, for a held one and for an empty
	 * collection (#39); replaceAll with two equal values, a null operator or an operator that
	 * changes the list; the writes through the view of subList (#78); none of them changes the list
	 *
	 * @return the cases
	 */
	static Stream<RefusalCase> refusals()
	{
		final Supplier<List<String>> abcd = () -> unique("a", "b", "c", "d");
		return Stream.of(
			outOfBounds("addAll(5, b) of a, an index over the size (#39)", () -> unique("a"),
				list -> list.addAll(5, Arrays.asList("b")), "Index: 5, Size: 1", "a"),
			outOfBounds("addAll(5, nothing) of a, an index over the size (#39)", () -> unique("a"),
				list -> list.addAll(5, Collections.emptyList()), "Index: 5, Size: 1", "a"),
			outOfBounds("addAll(-1, b) of a, a negative index (#39)", () -> unique("a"),
				list -> list.addAll(-1, Arrays.asList("b")), "Index: -1, Size: 1", "a"),
			outOfBounds("addAll(-1, nothing) of a, a negative index (#39)", () -> unique("a"),
				list -> list.addAll(-1, Collections.emptyList()), "Index: -1, Size: 1", "a"),
			outOfBounds("add(3, a) of a, b, a held element over the size (#39)",
				() -> unique("a", "b"), list -> list.add(3, "a"), "Index: 3, Size: 2", "a", "b"),
			outOfBounds("add(-1, a) of a, b, a held element at a negative index (#39)",
				() -> unique("a", "b"), list -> list.add(-1, "a"), "Index: -1, Size: 2", "a", "b"),
			outOfBounds("add(2, b) of a, a new element over the size (#39)", () -> unique("a"),
				list -> list.add(2, "b"), "Index: 2, Size: 1", "a"),
			outOfBounds("add(-1, b) of a, a new element at a negative index (#39)",
				() -> unique("a"), list -> list.add(-1, "b"), "Index: -1, Size: 1", "a"),
			outOfBounds("set(2, a) of a, b, an index out of range (#67)", () -> unique("a", "b"),
				list -> list.set(2, "a"), "Index 2 out of bounds for length 2", "a", "b"),
			outOfBounds("set(-1, b) of a, b, a negative index (#67)", () -> unique("a", "b"),
				list -> list.set(-1, "b"), "Index -1 out of bounds for length 2", "a", "b"),
			new RefusalCase(
				"replaceAll giving x for each of a, b, c: IllegalArgumentException, x twice (#67)",
				() -> unique("a", "b", "c"), list -> list.replaceAll(element -> "x"),
				IllegalArgumentException.class,
				"The operator gives the value x for two elements of this list",
				List.of("a", "b", "c")),
			new RefusalCase(
				"replaceAll giving A for a and B for the others of a, b, c: IllegalArgumentException (#67)",
				() -> unique("a", "b", "c"),
				list -> list.replaceAll(element -> element.equals("a") ? "A" : "B"),
				IllegalArgumentException.class, null, List.of("a", "b", "c")),
			new RefusalCase("replaceAll(null) of an empty list: NullPointerException",
				UniqueList::new, list -> list.replaceAll(null), NullPointerException.class, null,
				List.of()),
			new RefusalCase(
				"replaceAll with an operator that removes b of a, b: ConcurrentModificationException",
				() -> unique("a", "b"), list -> list.replaceAll(element -> {
					list.remove("b");
					return element.toUpperCase();
				}), ConcurrentModificationException.class, null, null),
			refused("subList(1, 3).set(0, a) of a, b, c, d, an element the list holds (#78)", abcd,
				list -> list.subList(1, 3).set(0, "a"), SUB_LIST, "a", "b", "c", "d"),
			refused("subList(1, 3).set(0, x) of a, b, c, d, a new element (#78)", abcd,
				list -> list.subList(1, 3).set(0, "x"), null, "a", "b", "c", "d"),
			refused("subList(1, 3).replaceAll with x of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).replaceAll(element -> "x"), null, "a", "b", "c", "d"),
			refused("subList(1, 3).subList(0, 1).set(0, a) of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).subList(0, 1).set(0, "a"), null, "a", "b", "c", "d"),
			refused("subList(1, 3).subList(0, 1).replaceAll with x of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).subList(0, 1).replaceAll(element -> "x"), null, "a", "b",
				"c", "d"),
			refused("subList(0, 2).replaceAll with c of a, b, c, the value c held behind (#78)",
				() -> unique("a", "b", "c"), list -> list.subList(0, 2).replaceAll(element -> "c"),
				null, "a", "b", "c"),
			refused("subList(1, 3).add(0, c) of a, b, c, d, an element the view holds (#78)", abcd,
				list -> list.subList(1, 3).add(0, "c"), null, "a", "b", "c", "d"),
			refused("subList(1, 3).add(e) of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).add("e"), null, "a", "b", "c", "d"),
			refused("subList(1, 3).add(1, e) of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).add(1, "e"), null, "a", "b", "c", "d"),
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
			refused("subList(2, 2).sort(null) of a, b, c, d, an empty view (#78)", abcd,
				list -> list.subList(2, 2).sort(null), null, "a", "b", "c", "d"),
			refused("subList(1, 3).subList(0, 2).sort(null) of a, b, c, d (#78)", abcd,
				list -> list.subList(1, 3).subList(0, 2).sort(null), null, "a", "b", "c", "d"),
			refused("Collections.reverse of subList(1, 3) of a, b, c, d (#78)", abcd,
				list -> Collections.reverse(list.subList(1, 3)), null, "a", "b", "c", "d"),
			refused("Collections.swap(0, 1) of subList(1, 3) of a, b, c, d (#78)", abcd,
				list -> Collections.swap(list.subList(1, 3), 0, 1), null, "a", "b", "c", "d"));
	}

	/**
	 * Test the calls on a {@link UniqueList} that throw: the exception, its message where a case
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
	 * Test the method {@link UniqueList#set(int, Object)} with a new element and with an element
	 * the list holds at the index itself: each replaces the element at the index and returns the
	 * previous one, as ArrayList does; an equal element at the index is replaced by the given
	 * instance (#67)
	 */
	@Test
	public void testSetWithNewElementOrElementHeldAtTheIndex()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b"));
		assertEquals("a", list.set(0, "x"));
		assertEquals(Arrays.asList("x", "b"), list);

		String previous = list.get(1);
		String equal = new String("b");
		assertSame(previous, list.set(1, equal));
		assertSame(equal, list.get(1));
		assertEquals(Arrays.asList("x", "b"), list);
	}

	/**
	 * Test the method {@link UniqueList#set(int, Object)} while an iterator runs over the list: set
	 * is no structural modification, the iterator goes on without a
	 * ConcurrentModificationException; [a, b, c].set(0, c) after the first next() gives [c, b, a],
	 * and the iterator returns b and a (#67)
	 */
	@Test
	public void testSetWhileAnIteratorRuns()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		Iterator<String> iterator = list.iterator();
		assertEquals("a", iterator.next());

		list.set(0, "c");
		assertEquals("b", iterator.next());
		assertEquals("a", iterator.next());
		assertFalse(iterator.hasNext());
		assertEquals(Arrays.asList("c", "b", "a"), list);
	}

	/**
	 * Test the method set of the list iterator with an element the list holds at another index: it
	 * swaps as {@link UniqueList#set(int, Object)} does, [a, b] with next(), next() and set(a)
	 * gives [b, a], and previous() returns a and b (#67)
	 */
	@Test
	public void testListIteratorSetWithElementHeldAtAnotherIndex()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b"));
		ListIterator<String> iterator = list.listIterator();
		iterator.next();
		iterator.next();

		iterator.set("a");
		assertEquals(Arrays.asList("b", "a"), list);
		assertEquals("a", iterator.previous());
		assertEquals("b", iterator.previous());
	}

	/**
	 * Test the method add of the list iterator with an element the list holds before the cursor: it
	 * ends immediately before the element next() would return, and the following next() returns
	 * that element, as the contract of ListIterator.add says; [a, b, c] with next() and add(a)
	 * stays [a, b, c] and next() returns b, [a, b, c, d] with next(), next() and add(a) gives [b,
	 * a, c, d], and previous() returns a at the index 1 (#67)
	 */
	@Test
	public void testListIteratorAddWithElementHeldBeforeTheCursor()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		ListIterator<String> iterator = list.listIterator();
		assertEquals("a", iterator.next());

		iterator.add("a");
		assertEquals(Arrays.asList("a", "b", "c"), list);
		assertEquals(1, iterator.nextIndex());
		assertEquals("b", iterator.next());

		list = new UniqueList<>(Arrays.asList("a", "b", "c", "d"));
		iterator = list.listIterator();
		iterator.next();
		iterator.next();

		iterator.add("a");
		assertEquals(Arrays.asList("b", "a", "c", "d"), list);
		assertEquals(1, iterator.previousIndex());
		assertEquals("a", iterator.previous());
	}

	/**
	 * Test the method add of the list iterator at the end of the list with an element the list
	 * holds: [a, b, c] with a list iterator at its end and add(a) gives [b, c, a], and the cursor
	 * stays at the end, nextIndex() is 3, hasNext() is false, and previous() returns a (#67)
	 */
	@Test
	public void testListIteratorAddAtTheEndWithElementHeldBeforeTheCursor()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		ListIterator<String> iterator = list.listIterator(3);

		iterator.add("a");
		assertEquals(Arrays.asList("b", "c", "a"), list);
		assertEquals(3, iterator.nextIndex());
		assertFalse(iterator.hasNext());
		assertEquals("a", iterator.previous());
	}

	/**
	 * Test the method add of the list iterator with a new element, with an element the list holds
	 * behind the cursor and with the element next() would return: the new element is inserted
	 * before the cursor, the one behind the cursor moves to before the cursor, and next() returns
	 * the element it would have returned; the element next() would return stays where it is and the
	 * cursor moves behind it, so previous() returns it. Before and after the fix of #67.
	 */
	@Test
	public void testListIteratorAddWithNewElementOrElementHeldAtOrBehindTheCursor()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "c"));
		ListIterator<String> iterator = list.listIterator();
		iterator.next();

		iterator.add("b");
		assertEquals(Arrays.asList("a", "b", "c"), list);
		assertEquals(2, iterator.nextIndex());
		assertEquals("c", iterator.next());

		list = new UniqueList<>(Arrays.asList("a", "b", "c", "d"));
		iterator = list.listIterator();
		iterator.next();

		iterator.add("d");
		assertEquals(Arrays.asList("a", "d", "b", "c"), list);
		assertEquals(2, iterator.nextIndex());
		assertEquals("b", iterator.next());

		list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		iterator = list.listIterator();
		iterator.next();

		iterator.add("b");
		assertEquals(Arrays.asList("a", "b", "c"), list);
		assertEquals(2, iterator.nextIndex());
		assertEquals("b", iterator.previous());
	}

	/**
	 * Test the list iterator forward and backward over the whole list: next(), previous(), the
	 * indexes and the NoSuchElementException at both ends, as ArrayList's list iterator gives them
	 */
	@Test
	public void testListIteratorWalksForwardAndBackward()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
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
	 * Test the method {@link UniqueList#listIterator(int)}: the iterator starts at the given index,
	 * the index size() included, and an index out of range throws an IndexOutOfBoundsException with
	 * the message of ArrayList
	 */
	@Test
	public void testListIteratorWithIndex()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));

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
	 * returned since the last remove it throws an IllegalStateException
	 */
	@Test
	public void testListIteratorRemove()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
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
	 * Test the method set of the list iterator with a new element: it replaces the element last
	 * returned, and without an element returned, or after add, it throws an IllegalStateException
	 */
	@Test
	public void testListIteratorSetWithNewElement()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b"));
		ListIterator<String> iterator = list.listIterator();
		assertThrows(IllegalStateException.class, () -> iterator.set("x"));

		assertEquals("a", iterator.next());
		iterator.set("x");
		assertEquals(Arrays.asList("x", "b"), list);

		iterator.add("y");
		assertThrows(IllegalStateException.class, () -> iterator.set("z"));
		assertThrows(IllegalStateException.class, iterator::remove);
		assertEquals(Arrays.asList("x", "y", "b"), list);
		assertEquals("b", iterator.next());
	}

	/**
	 * Test that the list iterator fails fast: after a structural modification of the list that is
	 * not made through the iterator, next(), previous(), set, remove and add throw a
	 * ConcurrentModificationException, as ArrayList's list iterator does
	 */
	@Test
	public void testListIteratorFailsFast()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b"));

		ListIterator<String> forNext = list.listIterator();
		list.add("c");
		assertThrows(ConcurrentModificationException.class, forNext::next);

		ListIterator<String> forPrevious = list.listIterator(1);
		list.add("d");
		assertThrows(ConcurrentModificationException.class, forPrevious::previous);

		ListIterator<String> forSet = list.listIterator();
		forSet.next();
		list.remove("d");
		assertThrows(ConcurrentModificationException.class, () -> forSet.set("x"));

		ListIterator<String> forRemove = list.listIterator();
		forRemove.next();
		list.remove("c");
		assertThrows(ConcurrentModificationException.class, forRemove::remove);

		ListIterator<String> forAdd = list.listIterator();
		list.add("c");
		assertThrows(ConcurrentModificationException.class, () -> forAdd.add("x"));
		assertEquals(Arrays.asList("a", "b", "c"), list);
	}

	/**
	 * Test set and add of the list iterators of the view of {@link UniqueList#subList(int, int)},
	 * of listIterator() and of listIterator(int): they throw an UnsupportedOperationException, and
	 * neither the list nor the view changes
	 */
	@Test
	public void testSubListListIteratorSetAndAdd()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c", "d"));
		List<String> view = list.subList(1, 3);

		ListIterator<String> iterator = view.listIterator();
		assertEquals("b", iterator.next());
		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> iterator.set("a"));
		assertEquals("A subList view of a UniqueList reads and removes only; set and add through"
			+ " the list itself", exception.getMessage());
		assertThrows(UnsupportedOperationException.class, () -> iterator.add("e"));

		ListIterator<String> fromIndex = view.listIterator(1);
		assertThrows(UnsupportedOperationException.class, () -> fromIndex.add("e"));
		assertEquals("c", fromIndex.next());
		assertThrows(UnsupportedOperationException.class, () -> fromIndex.set("x"));
		assertEquals(Arrays.asList("a", "b", "c", "d"), list);
		assertEquals(Arrays.asList("b", "c"), view);
	}

	/**
	 * Test that the view of {@link UniqueList#subList(int, int)} reads as the view of
	 * ArrayList.subList reads; before and after the fix of #78
	 */
	@Test
	public void testSubListReads()
	{
		SubListViewAssertions.assertReads(UniqueList::new);
	}

	/**
	 * Test that the view of {@link UniqueList#subList(int, int)} removes as the view of
	 * ArrayList.subList removes; before and after the fix of #78
	 */
	@Test
	public void testSubListRemoves()
	{
		SubListViewAssertions.assertRemoves(UniqueList::new);
	}

	/**
	 * Test that subList(from, to).clear() removes the range from a {@link UniqueList}; before and
	 * after the fix of #78
	 */
	@Test
	public void testSubListClearRemovesTheRange()
	{
		SubListViewAssertions.assertClearRemovesTheRange(UniqueList::new);
	}

	/**
	 * Test that the view of a view of {@link UniqueList#subList(int, int)} reads and removes as the
	 * view does; before and after the fix of #78
	 */
	@Test
	public void testSubListOfSubListRemoves()
	{
		SubListViewAssertions.assertSubListOfSubListRemoves(UniqueList::new);
	}

	/**
	 * Test the changes through {@link UniqueList#reversed()}, the view of List.reversed, which
	 * writes through the methods of the list, so that the list holds each element once: on the view
	 * of [a, b, c], addFirst(z) puts z first in the view and last in the list, addLast(c) moves c
	 * to the end of the view and the front of the list, set(0, a) swaps, sort(null) sorts the view,
	 * and add of its list iterator inserts behind the element next() returned; before and after the
	 * fix of #87
	 */
	@Test
	public void testReversed()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		List<String> reversed = list.reversed();
		assertEquals(Arrays.asList("c", "b", "a"), reversed);

		reversed.addFirst("z");
		assertEquals(Arrays.asList("a", "b", "c", "z"), list);
		assertEquals("z", reversed.getFirst());
		reversed.addLast("c");
		assertEquals(Arrays.asList("c", "a", "b", "z"), list);
		assertEquals("c", reversed.getLast());

		list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		reversed = list.reversed();
		assertEquals("c", reversed.set(0, "a"));
		assertEquals(Arrays.asList("c", "b", "a"), list);
		reversed.sort(null);
		assertEquals(Arrays.asList("a", "b", "c"), reversed);
		assertEquals(Arrays.asList("c", "b", "a"), list);

		list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		ListIterator<String> iterator = list.reversed().listIterator();
		assertEquals("c", iterator.next());
		iterator.add("a");
		assertEquals(Arrays.asList("c", "a", "b"), list.reversed());
		assertEquals("b", iterator.next());
	}

	/**
	 * Returns a new {@link UniqueList} of the first elements of a, b, c and d
	 *
	 * @param size
	 *            the number of elements, from 0 to 4
	 * @return a new list of the first elements of a, b, c and d
	 */
	private static List<String> newListOf(int size)
	{
		return new UniqueList<>(Arrays.asList("a", "b", "c", "d").subList(0, size));
	}

	/**
	 * Test add(int, E) of {@link UniqueList#reversed()} with an element the list holds, the
	 * postcondition of the view: after reversed().add(i, e), reversed().get(i) is e, the element
	 * moves there and the other elements keep their order; [a, b, c].reversed().add(1, a) gives the
	 * view [c, a, b], where the view of List.reversed gave [a, c, b]. The same over every index
	 * from 0 to size() - 1 of lists of one to four elements, with the held element at every
	 * position (#77)
	 */
	@Test
	public void testReversedAddWithIndexAndHeldElement()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		List<String> reversed = list.reversed();
		reversed.add(1, "a");
		assertEquals(Arrays.asList("c", "a", "b"), reversed);
		assertEquals(Arrays.asList("b", "a", "c"), list);

		for (int size = 1; size <= 4; size++)
		{
			for (int position = 0; position < size; position++)
			{
				for (int index = 0; index < size; index++)
				{
					List<String> view = newListOf(size).reversed();
					String element = view.get(position);
					List<String> expected = new ArrayList<>(view);
					expected.remove(element);
					expected.add(index, element);

					view.add(index, element);
					String message = "size " + size + ", position " + position + ", index " + index;
					assertEquals(element, view.get(index), message);
					assertEquals(expected, view, message);
				}
			}
		}
	}

	/**
	 * Test add(size(), E) of {@link UniqueList#reversed()} with an element the list holds: a move
	 * does not make the view grow, so that get(size()) cannot return the element afterwards; it
	 * throws an UnsupportedOperationException that says so, and the view of [a, b, c] with add(3,
	 * c) stays [c, b, a], where the view of List.reversed moved c to its end; the same for the held
	 * element at every position of lists of one to four elements. addLast moves the element to the
	 * end of the view (#77)
	 */
	@Test
	public void testReversedAddAtTheEndWithHeldElement()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		List<String> reversed = list.reversed();

		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> reversed.add(3, "c"));
		assertEquals("The reversed view of a UniqueList does not move an element the list holds to"
			+ " the index size(), which does not exist after a move; addLast moves it to the end of"
			+ " the view", exception.getMessage());
		assertEquals(Arrays.asList("c", "b", "a"), reversed);
		reversed.addLast("c");
		assertEquals(Arrays.asList("b", "a", "c"), reversed);

		for (int size = 1; size <= 4; size++)
		{
			for (int position = 0; position < size; position++)
			{
				List<String> view = newListOf(size).reversed();
				String element = view.get(position);
				List<String> before = new ArrayList<>(view);
				int end = size;

				assertThrows(UnsupportedOperationException.class, () -> view.add(end, element),
					"size " + size + ", position " + position);
				assertEquals(before, view, "size " + size + ", position " + position);
			}
		}
	}

	/**
	 * Test add(int, E) of {@link UniqueList#reversed()} with a new element: after add(index, z),
	 * get(index) is z and the other elements keep their order, over every index of lists of none to
	 * four elements; an index out of range throws an IndexOutOfBoundsException, for a held element
	 * too, and the list does not change; before and after the fix of #77
	 */
	@Test
	public void testReversedAddWithIndexAndNewElement()
	{
		for (int size = 0; size <= 4; size++)
		{
			for (int index = 0; index <= size; index++)
			{
				List<String> list = newListOf(size);
				List<String> view = list.reversed();
				List<String> expected = new ArrayList<>(view);
				expected.add(index, "z");

				view.add(index, "z");
				assertEquals("z", view.get(index), "size " + size + ", index " + index);
				assertEquals(expected, view, "size " + size + ", index " + index);
				assertEquals(size + 1, list.size());
			}
		}

		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		List<String> reversed = list.reversed();
		IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
			() -> reversed.add(4, "z"));
		assertEquals("Index: 4, Size: 3", exception.getMessage());
		assertThrows(IndexOutOfBoundsException.class, () -> reversed.add(-1, "z"));
		assertThrows(IndexOutOfBoundsException.class, () -> reversed.add(4, "a"));
		assertThrows(IndexOutOfBoundsException.class, () -> reversed.add(-1, "a"));
		assertEquals(Arrays.asList("a", "b", "c"), list);
	}

	/**
	 * Test add(E) of {@link UniqueList#reversed()} with an element the list holds: as
	 * {@link UniqueList#add(Object)}, it returns false and leaves the element where it is, the view
	 * of [a, b, c] with add(c) and add(a) stays [c, b, a], where the view of List.reversed moved c
	 * to its end and returned true (#77)
	 */
	@Test
	public void testReversedAddWithHeldElement()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		List<String> reversed = list.reversed();

		assertFalse(reversed.add("c"));
		assertFalse(reversed.add("a"));
		assertEquals(Arrays.asList("c", "b", "a"), reversed);
		assertEquals(Arrays.asList("a", "b", "c"), list);
	}

	/**
	 * Test addAll and addAll(int, Collection) of {@link UniqueList#reversed()} with elements the
	 * list holds and with a repeated new element: as {@link UniqueList#addAll(Collection)} and
	 * {@link UniqueList#addAll(int, Collection)}, they return false when the list does not change,
	 * and add the new elements in the order of their first occurrence; the view of [a, b, c] with
	 * addAll([x, y, x]) gives [c, b, a, x, y], with addAll(1, [p, q, p]) [c, p, q, b, a], where the
	 * view of List.reversed returned true for any collection that is not empty and gave [c, b, a,
	 * y, x] and [c, q, p, b, a] (#77)
	 */
	@Test
	public void testReversedAddAllWithHeldAndRepeatedElements()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		List<String> reversed = list.reversed();

		assertFalse(reversed.addAll(Arrays.asList("a", "c")));
		assertFalse(reversed.addAll(1, Arrays.asList("b")));
		assertEquals(Arrays.asList("c", "b", "a"), reversed);

		assertTrue(reversed.addAll(Arrays.asList("x", "y", "x")));
		assertEquals(Arrays.asList("c", "b", "a", "x", "y"), reversed);

		List<String> other = new UniqueList<>(Arrays.asList("a", "b", "c"));
		List<String> otherReversed = other.reversed();
		assertTrue(otherReversed.addAll(1, Arrays.asList("p", "q", "p")));
		assertEquals(Arrays.asList("c", "p", "q", "b", "a"), otherReversed);
	}

	/**
	 * Test addAll and addAll(int, Collection) of {@link UniqueList#reversed()} with new elements:
	 * they come at the end of the view or at the index in the order of the collection, an empty
	 * collection changes nothing and gives false, and an index out of range throws an
	 * IndexOutOfBoundsException, for an empty collection too; the view [x] with addAll(0, [a, x, a,
	 * b]) gives [a, b, x], as {@link UniqueList#addAll(int, Collection)} gives it; before and after
	 * the fix of #77
	 */
	@Test
	public void testReversedAddAllWithNewElements()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		List<String> reversed = list.reversed();

		assertTrue(reversed.addAll(Arrays.asList("x", "y")));
		assertEquals(Arrays.asList("c", "b", "a", "x", "y"), reversed);
		assertTrue(reversed.addAll(0, Arrays.asList("p", "q")));
		assertEquals(Arrays.asList("p", "q", "c", "b", "a", "x", "y"), reversed);
		assertTrue(reversed.addAll(3, Arrays.asList("r")));
		assertEquals(Arrays.asList("p", "q", "c", "r", "b", "a", "x", "y"), reversed);
		assertFalse(reversed.addAll(Collections.emptyList()));
		assertFalse(reversed.addAll(8, Collections.emptyList()));
		assertThrows(IndexOutOfBoundsException.class,
			() -> reversed.addAll(9, Collections.emptyList()));
		assertThrows(IndexOutOfBoundsException.class,
			() -> reversed.addAll(-1, Arrays.asList("z")));
		assertEquals(Arrays.asList("y", "x", "a", "b", "r", "c", "q", "p"), list);

		List<String> single = new UniqueList<>(Arrays.asList("x"));
		List<String> singleReversed = single.reversed();
		assertTrue(singleReversed.addAll(0, Arrays.asList("a", "x", "a", "b")));
		assertEquals(Arrays.asList("a", "b", "x"), singleReversed);
	}

	/**
	 * Test add(E), addFirst and addLast of {@link UniqueList#reversed()}: add(z) appends a new
	 * element to the end of the view, addFirst moves a held element to the front of the view and
	 * addLast puts a new one at its end, and for the element that is first or last already the view
	 * does not change; the view of [a, b, c] with add(z), addFirst(a) and addLast(y) gives [a, c,
	 * b, z, y]; before and after the fix of #77
	 */
	@Test
	public void testReversedAddWithNewElementAddFirstAndAddLast()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		List<String> reversed = list.reversed();

		assertTrue(reversed.add("z"));
		assertEquals(Arrays.asList("c", "b", "a", "z"), reversed);
		assertEquals(Arrays.asList("z", "a", "b", "c"), list);
		reversed.addFirst("a");
		assertEquals(Arrays.asList("a", "c", "b", "z"), reversed);
		assertEquals("a", reversed.getFirst());
		reversed.addLast("y");
		assertEquals(Arrays.asList("a", "c", "b", "z", "y"), reversed);
		assertEquals("y", reversed.getLast());
		reversed.addFirst("a");
		reversed.addLast("y");
		assertEquals(Arrays.asList("a", "c", "b", "z", "y"), reversed);
	}

	/**
	 * Test add of the list iterator of {@link UniqueList#reversed()} with an element the list holds
	 * before the cursor or at it: it throws an UnsupportedOperationException that says why, as the
	 * contract of ListIterator.add, a following next() unaffected and nextIndex() one higher,
	 * cannot hold for them, and neither the view nor the cursor changes; the view of [a, b, c] with
	 * next() twice and add(c) or add(b) stays [c, b, a], nextIndex() 2 and next() a, with next()
	 * and add(b) nextIndex() 1 and next() b. The view of List.reversed gave [b, c, a] with
	 * nextIndex() 2 for the first, and skipped b for the second (#77)
	 */
	@Test
	public void testReversedListIteratorAddWithElementHeldAtOrBeforeTheCursor()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		ListIterator<String> iterator = list.reversed().listIterator();
		assertEquals("c", iterator.next());
		assertEquals("b", iterator.next());

		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> iterator.add("c"));
		assertEquals("The list iterator of the reversed view of a UniqueList does not add an"
			+ " element the list holds before the cursor or at it: next() would skip it or"
			+ " nextIndex() would not grow", exception.getMessage());
		assertThrows(UnsupportedOperationException.class, () -> iterator.add("b"));
		assertEquals(Arrays.asList("c", "b", "a"), list.reversed());
		assertEquals(2, iterator.nextIndex());
		assertEquals("a", iterator.next());

		List<String> other = new UniqueList<>(Arrays.asList("a", "b", "c"));
		ListIterator<String> otherIterator = other.reversed().listIterator();
		assertEquals("c", otherIterator.next());

		assertThrows(UnsupportedOperationException.class, () -> otherIterator.add("b"));
		assertEquals(Arrays.asList("c", "b", "a"), other.reversed());
		assertEquals(1, otherIterator.nextIndex());
		assertEquals("b", otherIterator.next());
	}

	/**
	 * Test set and remove of the list iterator of {@link UniqueList#reversed()} after its add: they
	 * throw an IllegalStateException, as the contract of ListIterator says, and the view of [a, b]
	 * with next() and add(z) stays [b, z, a]; the view of List.reversed called previous() of the
	 * list's iterator after its add, so that set and remove acted on the added element (#77)
	 */
	@Test
	public void testReversedListIteratorSetAndRemoveAfterAdd()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b"));
		ListIterator<String> iterator = list.reversed().listIterator();
		assertEquals("b", iterator.next());
		iterator.add("z");

		assertThrows(IllegalStateException.class, () -> iterator.set("y"));
		assertThrows(IllegalStateException.class, iterator::remove);
		assertEquals(Arrays.asList("b", "z", "a"), list.reversed());
	}

	/**
	 * Test add of the list iterator of {@link UniqueList#reversed()} with new elements and with an
	 * element the list holds behind the cursor: each comes immediately before the element next()
	 * would return, which next() then returns, previous() returns the added element, and
	 * nextIndex() grows by one, as the contract of ListIterator.add says; the held element moves
	 * there, the view of [a, b, c] with next(), add(z), add(a) and at the end add(y) gives [c, z,
	 * a, b, y]; before and after the fix of #77
	 */
	@Test
	public void testReversedListIteratorAddWithNewElementOrElementHeldBehindTheCursor()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		ListIterator<String> iterator = list.reversed().listIterator();
		assertEquals("c", iterator.next());

		iterator.add("z");
		assertEquals(Arrays.asList("c", "z", "b", "a"), list.reversed());
		assertEquals(2, iterator.nextIndex());
		assertEquals("z", iterator.previous());
		assertEquals("z", iterator.next());

		iterator.add("a");
		assertEquals(Arrays.asList("c", "z", "a", "b"), list.reversed());
		assertEquals(3, iterator.nextIndex());
		assertEquals("b", iterator.next());
		assertFalse(iterator.hasNext());

		iterator.add("y");
		assertEquals(Arrays.asList("c", "z", "a", "b", "y"), list.reversed());
		assertEquals(5, iterator.nextIndex());
		assertFalse(iterator.hasNext());
		assertEquals("y", iterator.previous());
	}

	/**
	 * Test that {@link UniqueList#reversed()} reads the list backwards: size, isEmpty, get and its
	 * range check, contains, containsAll, indexOf, lastIndexOf, toArray, getFirst, getLast, the
	 * iterator and the stream, equals, hashCode and toString as a list of the elements in the
	 * reverse order, NoSuchElementException from getFirst, getLast, removeFirst and removeLast of
	 * an empty view, and reversed() of the view is the list itself; before and after the fix of #77
	 */
	@Test
	public void testReversedReads()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		List<String> reversed = list.reversed();

		assertEquals(3, reversed.size());
		assertFalse(reversed.isEmpty());
		assertEquals("c", reversed.get(0));
		assertEquals("b", reversed.get(1));
		assertEquals("a", reversed.get(2));
		assertThrows(IndexOutOfBoundsException.class, () -> reversed.get(3));
		assertThrows(IndexOutOfBoundsException.class, () -> reversed.get(-1));
		assertTrue(reversed.contains("a"));
		assertFalse(reversed.contains("z"));
		assertTrue(reversed.containsAll(Arrays.asList("c", "a")));
		assertFalse(reversed.containsAll(Arrays.asList("a", "z")));
		assertEquals(0, reversed.indexOf("c"));
		assertEquals(2, reversed.indexOf("a"));
		assertEquals(-1, reversed.indexOf("z"));
		assertEquals(0, reversed.lastIndexOf("c"));
		assertEquals(2, reversed.lastIndexOf("a"));
		assertEquals(-1, reversed.lastIndexOf("z"));
		assertArrayEquals(new Object[] { "c", "b", "a" }, reversed.toArray());
		assertArrayEquals(new String[] { "c", "b", "a" }, reversed.toArray(new String[0]));
		assertEquals("c", reversed.getFirst());
		assertEquals("a", reversed.getLast());
		List<String> walked = new ArrayList<>();
		for (String element : reversed)
		{
			walked.add(element);
		}
		assertEquals(Arrays.asList("c", "b", "a"), walked);
		assertEquals(Arrays.asList("c", "b", "a"), reversed.stream().toList());
		assertEquals(Arrays.asList("c", "b", "a"), reversed);
		assertNotEquals(list, reversed);
		assertEquals(Arrays.asList("c", "b", "a").hashCode(), reversed.hashCode());
		assertEquals("[c, b, a]", reversed.toString());
		assertSame(list, reversed.reversed());

		List<String> empty = new UniqueList<String>().reversed();
		assertTrue(empty.isEmpty());
		assertEquals(0, empty.size());
		assertEquals("[]", empty.toString());
		assertThrows(NoSuchElementException.class, empty::getFirst);
		assertThrows(NoSuchElementException.class, empty::getLast);
		assertThrows(NoSuchElementException.class, empty::removeFirst);
		assertThrows(NoSuchElementException.class, empty::removeLast);
	}

	/**
	 * Test the removals through {@link UniqueList#reversed()}: remove(int) at the index of the view
	 * and its range check, remove(Object), removeFirst, removeLast, removeIf, removeAll, retainAll
	 * and clear remove from the list and return what List says; before and after the fix of #77
	 */
	@Test
	public void testReversedRemoves()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c", "d", "e"));
		List<String> reversed = list.reversed();

		assertEquals("d", reversed.remove(1));
		assertEquals(Arrays.asList("a", "b", "c", "e"), list);
		assertThrows(IndexOutOfBoundsException.class, () -> reversed.remove(4));
		assertThrows(IndexOutOfBoundsException.class, () -> reversed.remove(-1));
		assertTrue(reversed.remove("b"));
		assertFalse(reversed.remove("z"));
		assertEquals("e", reversed.removeFirst());
		assertEquals("a", reversed.removeLast());
		assertEquals(Arrays.asList("c"), reversed);
		assertEquals(Arrays.asList("c"), list);

		List<String> other = new UniqueList<>(Arrays.asList("a", "b", "c", "d", "e"));
		List<String> otherReversed = other.reversed();
		assertTrue(otherReversed.removeIf(element -> element.compareTo("c") > 0));
		assertFalse(otherReversed.removeIf("z"::equals));
		assertEquals(Arrays.asList("c", "b", "a"), otherReversed);
		assertTrue(otherReversed.removeAll(Arrays.asList("a", "z")));
		assertFalse(otherReversed.removeAll(Arrays.asList("z")));
		assertEquals(Arrays.asList("c", "b"), otherReversed);
		assertTrue(otherReversed.retainAll(Arrays.asList("b")));
		assertFalse(otherReversed.retainAll(Arrays.asList("b")));
		assertEquals(Arrays.asList("b"), other);
		otherReversed.clear();
		assertTrue(other.isEmpty());
		assertTrue(otherReversed.isEmpty());
	}

	/**
	 * Test set of {@link UniqueList#reversed()}: it replaces the element at the index of the view
	 * and returns the one that was there, swaps an element the list holds at another index, as
	 * {@link UniqueList#set(int, Object)} does, and checks the index, so that
	 * {@link Collections#swap(List, int, int)} and {@link Collections#reverse(List)} work on the
	 * view; before and after the fix of #77
	 */
	@Test
	public void testReversedSet()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		List<String> reversed = list.reversed();

		assertEquals("b", reversed.set(1, "a"));
		assertEquals(Arrays.asList("c", "a", "b"), reversed);
		assertEquals(Arrays.asList("b", "a", "c"), list);
		assertEquals("c", reversed.set(0, "z"));
		assertEquals(Arrays.asList("z", "a", "b"), reversed);
		assertThrows(IndexOutOfBoundsException.class, () -> reversed.set(3, "y"));
		assertThrows(IndexOutOfBoundsException.class, () -> reversed.set(-1, "y"));
		assertEquals(Arrays.asList("z", "a", "b"), reversed);

		Collections.swap(reversed, 0, 2);
		assertEquals(Arrays.asList("b", "a", "z"), reversed);
		Collections.reverse(reversed);
		assertEquals(Arrays.asList("z", "a", "b"), reversed);
		assertEquals(Arrays.asList("b", "a", "z"), list);
	}

	/**
	 * Test replaceAll and sort of {@link UniqueList#reversed()}: replaceAll replaces each element
	 * and throws an IllegalArgumentException for an operator that gives two equal values, as
	 * {@link UniqueList#replaceAll(java.util.function.UnaryOperator)} does, and the list stays
	 * unchanged; sort sorts the view, stable as List.sort says, the elements of equal length keep
	 * their order in the view, and with null in the natural order; before and after the fix of #77
	 */
	@Test
	public void testReversedReplaceAllAndSort()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		List<String> reversed = list.reversed();

		reversed.replaceAll(String::toUpperCase);
		assertEquals(Arrays.asList("C", "B", "A"), reversed);
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> reversed.replaceAll(element -> "X"));
		assertEquals("The operator gives the value X for two elements of this list",
			exception.getMessage());
		assertEquals(Arrays.asList("C", "B", "A"), reversed);

		List<String> other = new UniqueList<>(Arrays.asList("aa", "b", "cc", "d"));
		List<String> otherReversed = other.reversed();
		otherReversed.sort(Comparator.comparingInt(String::length));
		assertEquals(Arrays.asList("d", "b", "cc", "aa"), otherReversed);
		otherReversed.sort(null);
		assertEquals(Arrays.asList("aa", "b", "cc", "d"), otherReversed);
		assertEquals(Arrays.asList("d", "cc", "b", "aa"), other);
	}

	/**
	 * Test the list iterator of {@link UniqueList#reversed()} forward and backward: next(),
	 * previous(), the indexes and the NoSuchElementException at both ends, listIterator(int) and
	 * its range check, set, which swaps a held element, remove, which keeps the cursor in front of
	 * the element next() returns, and the IllegalStateException of set and remove before next() and
	 * after remove; before and after the fix of #77
	 */
	@Test
	public void testReversedListIteratorWalksSetsAndRemoves()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		ListIterator<String> iterator = list.reversed().listIterator();

		assertFalse(iterator.hasPrevious());
		assertTrue(iterator.hasNext());
		assertEquals(0, iterator.nextIndex());
		assertEquals(-1, iterator.previousIndex());
		assertThrows(NoSuchElementException.class, iterator::previous);
		assertThrows(IllegalStateException.class, iterator::remove);
		assertThrows(IllegalStateException.class, () -> iterator.set("z"));
		assertEquals("c", iterator.next());
		assertEquals("b", iterator.next());
		assertEquals("a", iterator.next());
		assertFalse(iterator.hasNext());
		assertTrue(iterator.hasPrevious());
		assertThrows(NoSuchElementException.class, iterator::next);
		assertEquals(3, iterator.nextIndex());
		assertEquals(2, iterator.previousIndex());
		assertEquals("a", iterator.previous());
		assertEquals("b", iterator.previous());
		assertEquals(1, iterator.nextIndex());

		iterator.set("a");
		assertEquals(Arrays.asList("c", "a", "b"), list.reversed());
		iterator.remove();
		assertEquals(Arrays.asList("c", "b"), list.reversed());
		assertEquals(1, iterator.nextIndex());
		assertThrows(IllegalStateException.class, iterator::remove);
		assertEquals("b", iterator.next());

		ListIterator<String> fromIndex = list.reversed().listIterator(2);
		assertFalse(fromIndex.hasNext());
		assertEquals("b", fromIndex.previous());
		assertEquals("c", list.reversed().listIterator(0).next());
		IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
			() -> list.reversed().listIterator(3));
		assertEquals("Index: 3, Size: 2", exception.getMessage());
		assertThrows(IndexOutOfBoundsException.class, () -> list.reversed().listIterator(-1));
	}

	/**
	 * Test that the iterators of {@link UniqueList#reversed()} fail fast: after a change of the
	 * list that is not made through the iterator, next(), previous(), set, remove and add throw a
	 * ConcurrentModificationException, as do forEach and the stream of the view when their action
	 * changes the list; before and after the fix of #77
	 */
	@Test
	public void testReversedListIteratorFailsFast()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b"));
		List<String> reversed = list.reversed();

		ListIterator<String> forNext = reversed.listIterator();
		list.add("c");
		assertThrows(ConcurrentModificationException.class, forNext::next);

		ListIterator<String> forPrevious = reversed.listIterator(1);
		list.add("d");
		assertThrows(ConcurrentModificationException.class, forPrevious::previous);

		ListIterator<String> forSet = reversed.listIterator();
		forSet.next();
		list.remove("d");
		assertThrows(ConcurrentModificationException.class, () -> forSet.set("x"));

		ListIterator<String> forRemove = reversed.listIterator();
		forRemove.next();
		list.remove("c");
		assertThrows(ConcurrentModificationException.class, forRemove::remove);

		ListIterator<String> forAdd = reversed.listIterator();
		list.add("c");
		assertThrows(ConcurrentModificationException.class, () -> forAdd.add("x"));
		assertEquals(Arrays.asList("a", "b", "c"), list);

		assertThrows(ConcurrentModificationException.class,
			() -> reversed.forEach(element -> list.add(element + "1")));
		assertThrows(ConcurrentModificationException.class,
			() -> reversed.stream().forEach(element -> list.add(element + "2")));
	}

	/**
	 * Test that subList of {@link UniqueList#reversed()} reads and removes, in the order of the
	 * view and with its range check, and refuses set, add, addAll, replaceAll, sort and the set and
	 * add of its list iterator with an UnsupportedOperationException, as the view of
	 * {@link UniqueList#subList(int, int)} does, and the list does not change then; before and
	 * after the fix of #77
	 */
	@Test
	public void testReversedSubList()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c", "d"));
		List<String> reversed = list.reversed();
		List<String> view = reversed.subList(1, 3);

		assertEquals(Arrays.asList("c", "b"), view);
		assertEquals(2, view.size());
		assertEquals("c", view.get(0));
		assertEquals(1, view.indexOf("b"));
		assertFalse(view.contains("d"));
		assertEquals(Arrays.asList("b"), reversed.subList(0, 3).subList(2, 3));
		assertThrows(IndexOutOfBoundsException.class, () -> reversed.subList(-1, 2));
		assertThrows(IndexOutOfBoundsException.class, () -> reversed.subList(0, 5));
		assertThrows(IndexOutOfBoundsException.class, () -> reversed.subList(3, 1));

		assertThrows(UnsupportedOperationException.class, () -> view.set(0, "a"));
		assertThrows(UnsupportedOperationException.class, () -> view.add("z"));
		assertThrows(UnsupportedOperationException.class, () -> view.add(0, "z"));
		assertThrows(UnsupportedOperationException.class, () -> view.addAll(Arrays.asList("z")));
		assertThrows(UnsupportedOperationException.class, () -> view.addAll(0, Arrays.asList("z")));
		assertThrows(UnsupportedOperationException.class,
			() -> view.replaceAll(String::toUpperCase));
		assertThrows(UnsupportedOperationException.class, () -> view.sort(null));
		ListIterator<String> iterator = view.listIterator();
		assertEquals("c", iterator.next());
		assertThrows(UnsupportedOperationException.class, () -> iterator.set("a"));
		assertThrows(UnsupportedOperationException.class, () -> iterator.add("z"));
		assertEquals(Arrays.asList("a", "b", "c", "d"), list);

		assertEquals("b", view.remove(1));
		assertEquals(Arrays.asList("a", "c", "d"), list);
		assertTrue(view.remove("c"));
		assertTrue(view.isEmpty());
		assertEquals(Arrays.asList("a", "d"), list);

		List<String> other = new UniqueList<>(Arrays.asList("a", "b", "c", "d"));
		other.reversed().subList(1, 3).clear();
		assertEquals(Arrays.asList("a", "d"), other);
	}

	/**
	 * Test the message of the refusals of subList of {@link UniqueList#reversed()}, and addAll with
	 * an empty collection: the message names the reversed view, and addAll with an empty collection
	 * throws an UnsupportedOperationException too, as on the view of
	 * {@link UniqueList#subList(int, int)}, where the view of List.reversed refused with the
	 * message of that view and returned false for an empty collection (#77)
	 */
	@Test
	public void testReversedSubListRefusal()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c", "d"));
		List<String> view = list.reversed().subList(1, 3);

		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> view.set(0, "a"));
		assertEquals(
			"A subList view of the reversed view of a UniqueList reads and removes only;"
				+ " set and add through the reversed view or the list itself",
			exception.getMessage());
		assertThrows(UnsupportedOperationException.class,
			() -> view.addAll(Collections.emptyList()));
		assertThrows(UnsupportedOperationException.class,
			() -> view.addAll(0, Collections.emptyList()));
		assertEquals(Arrays.asList("a", "b", "c", "d"), list);
	}
}
