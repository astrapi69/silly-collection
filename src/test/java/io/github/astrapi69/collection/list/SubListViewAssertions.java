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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

/**
 * Assertions on the view that subList of {@link UniqueList}, {@link SortedList} and
 * {@link SortedUniqueList} returns, shared by the tests of the three classes (#78). Each method
 * takes the function that makes a list of the class under test from the given elements; the
 * elements are distinct and in their natural order, so that they suit each of the three.
 */
final class SubListViewAssertions
{

	/**
	 * No instances: a holder of static assertions
	 */
	private SubListViewAssertions()
	{
	}

	/**
	 * Asserts that the view [b, c] of [a, b, c, d] reads as a list of b and c: size, isEmpty, get
	 * and its range check, contains, containsAll, indexOf, lastIndexOf, toArray, iterator and list
	 * iterator, equals, hashCode and toString, as the view of ArrayList.subList reads
	 *
	 * @param newList
	 *            the function that makes a list of the class under test from the given elements
	 */
	static void assertReads(Function<Collection<String>, List<String>> newList)
	{
		List<String> list = newList.apply(Arrays.asList("a", "b", "c", "d"));
		List<String> view = list.subList(1, 3);

		assertEquals(2, view.size());
		assertFalse(view.isEmpty());
		assertTrue(list.subList(2, 2).isEmpty());
		assertEquals("b", view.get(0));
		assertEquals("c", view.get(1));
		assertThrows(IndexOutOfBoundsException.class, () -> view.get(2));
		assertTrue(view.contains("c"));
		assertFalse(view.contains("a"));
		assertTrue(view.containsAll(Arrays.asList("c", "b")));
		assertFalse(view.containsAll(Arrays.asList("b", "d")));
		assertEquals(1, view.indexOf("c"));
		assertEquals(-1, view.indexOf("d"));
		assertEquals(1, view.lastIndexOf("c"));
		assertEquals(-1, view.lastIndexOf("a"));
		assertArrayEquals(new Object[] { "b", "c" }, view.toArray());
		assertArrayEquals(new String[] { "b", "c" }, view.toArray(new String[0]));

		Iterator<String> iterator = view.iterator();
		assertEquals("b", iterator.next());
		assertEquals("c", iterator.next());
		assertFalse(iterator.hasNext());
		ListIterator<String> listIterator = view.listIterator(2);
		assertEquals(2, listIterator.nextIndex());
		assertEquals(1, listIterator.previousIndex());
		assertEquals("c", listIterator.previous());
		assertEquals("b", listIterator.previous());
		assertFalse(listIterator.hasPrevious());

		assertTrue(view.equals(Arrays.asList("b", "c")));
		assertFalse(view.equals(Arrays.asList("b", "d")));
		assertTrue(Arrays.asList("b", "c").equals(view));
		assertEquals(Arrays.asList("b", "c").hashCode(), view.hashCode());
		assertEquals("[b, c]", view.toString());
		assertEquals(Arrays.asList("a", "b", "c", "d"), list);
	}

	/**
	 * Asserts that the view [b, c] of [a, b, c, d] removes from the list, as the view of
	 * ArrayList.subList removes: remove(int), remove(Object), removeIf, removeAll, retainAll and
	 * the remove of its iterator and list iterator each remove inside the view only, and the
	 * methods that return whether the list changed return false where nothing is removed
	 *
	 * @param newList
	 *            the function that makes a list of the class under test from the given elements
	 */
	static void assertRemoves(Function<Collection<String>, List<String>> newList)
	{
		List<String> elements = Arrays.asList("a", "b", "c", "d");
		List<String> list;
		List<String> view;

		list = newList.apply(elements);
		view = list.subList(1, 3);
		assertEquals("b", view.remove(0));
		assertEquals(Arrays.asList("c"), view);
		assertEquals(Arrays.asList("a", "c", "d"), list);

		list = newList.apply(elements);
		view = list.subList(1, 3);
		assertTrue(view.remove("c"));
		assertFalse(view.remove("a"));
		assertEquals(Arrays.asList("b"), view);
		assertEquals(Arrays.asList("a", "b", "d"), list);

		list = newList.apply(elements);
		view = list.subList(1, 3);
		assertTrue(view.removeIf("b"::equals));
		assertFalse(view.removeIf("a"::equals));
		assertEquals(Arrays.asList("c"), view);
		assertEquals(Arrays.asList("a", "c", "d"), list);

		list = newList.apply(elements);
		view = list.subList(1, 3);
		assertTrue(view.removeAll(Arrays.asList("c", "d")));
		assertFalse(view.removeAll(Arrays.asList("a")));
		assertEquals(Arrays.asList("b"), view);
		assertEquals(Arrays.asList("a", "b", "d"), list);

		list = newList.apply(elements);
		view = list.subList(1, 3);
		assertTrue(view.retainAll(Arrays.asList("c", "d")));
		assertFalse(view.retainAll(Arrays.asList("c")));
		assertEquals(Arrays.asList("c"), view);
		assertEquals(Arrays.asList("a", "c", "d"), list);

		list = newList.apply(elements);
		view = list.subList(1, 3);
		Iterator<String> iterator = view.iterator();
		iterator.next();
		iterator.remove();
		assertEquals(Arrays.asList("c"), view);
		assertEquals(Arrays.asList("a", "c", "d"), list);

		list = newList.apply(elements);
		view = list.subList(1, 3);
		ListIterator<String> listIterator = view.listIterator(2);
		listIterator.previous();
		listIterator.remove();
		assertEquals(Arrays.asList("b"), view);
		assertEquals(Arrays.asList("a", "b", "d"), list);
	}

	/**
	 * Asserts that subList(from, to).clear() removes the range from the list, the idiom of
	 * List.subList: [a, b, c, d, e] with subList(1, 4).clear() gives [a, e], and subList(0,
	 * size()).clear() empties the list
	 *
	 * @param newList
	 *            the function that makes a list of the class under test from the given elements
	 */
	static void assertClearRemovesTheRange(Function<Collection<String>, List<String>> newList)
	{
		List<String> list = newList.apply(Arrays.asList("a", "b", "c", "d", "e"));

		List<String> view = list.subList(1, 4);
		view.clear();
		assertTrue(view.isEmpty());
		assertEquals(Arrays.asList("a", "e"), list);

		list.subList(0, list.size()).clear();
		assertTrue(list.isEmpty());
	}

	/**
	 * Asserts that the view of a view reads and removes as the view does, and that a removal
	 * through it shrinks the view it was taken from as well: [c, d] of [b, c, d] of [a, b, c, d, e]
	 *
	 * @param newList
	 *            the function that makes a list of the class under test from the given elements
	 */
	static void assertSubListOfSubListRemoves(Function<Collection<String>, List<String>> newList)
	{
		List<String> list = newList.apply(Arrays.asList("a", "b", "c", "d", "e"));
		List<String> view = list.subList(1, 4);
		List<String> inner = view.subList(1, 3);

		assertEquals(Arrays.asList("c", "d"), inner);
		assertEquals("c", inner.remove(0));
		assertEquals(Arrays.asList("d"), inner);
		assertEquals(Arrays.asList("b", "d"), view);
		assertEquals(Arrays.asList("a", "b", "d", "e"), list);

		inner.clear();
		assertTrue(inner.isEmpty());
		assertEquals(Arrays.asList("b"), view);
		assertEquals(Arrays.asList("a", "b", "e"), list);
	}
}
