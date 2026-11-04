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
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.Test;

import io.github.astrapi69.comparator.object.StringComparator;

/**
 * The unit test class for the class {@link SortedList}
 */
public class SortedListTest
{

	/**
	 * Test the method {@link SortedList#add(Object)}
	 */
	@Test
	public void testAdd()
	{
		List<String> list;

		list = new SortedList<>(6);
		list.add("Leonidas");
		assertEquals(list.size(), 1);
		list.add("Berta");
		assertEquals(list.size(), 2);
		list.add("Caesar");
		assertEquals(list.size(), 3);
		list.add("Dora");
		assertEquals(list.size(), 4);
		list.add("Emil");
		assertEquals(list.size(), 5);
		list.add("Anton");
		assertEquals(list.size(), 6);
	}

	/**
	 * Test the method {@link SortedList#add(Object)}
	 */
	@Test
	public void testAddWithIndex()
	{
		List<String> list;

		list = new SortedList<>();
		list.add(0, "Leonidas");
		assertEquals(list.size(), 1);
		list.add(0, "Berta");
		assertEquals(list.size(), 2);
		list.add(0, "Caesar");
		assertEquals(list.size(), 3);
		list.add(0, "Dora");
		assertEquals(list.size(), 4);
		list.add(0, "Emil");
		assertEquals(list.size(), 5);
		list.add(0, "Anton");
		assertEquals(list.size(), 6);
	}

	/**
	 * Test the method {@link SortedList#addAll(Collection)}
	 */
	@Test
	public void testAddAll()
	{
		List<String> list;
		List<String> addition;

		addition = ListFactory.newArrayList("", "Emil", "Anton", "Anton", "Anton", "Emil", "");
		list = new SortedList<>(new StringComparator());
		list.add("Emil");
		list.add("Caesar");
		list.addAll(addition);
		assertEquals(list.size(), 9);

		addition = ListFactory.newArrayList();
		list = new SortedList<>();
		list.add("Emil");
		list.add("Caesar");
		list.addAll(addition);
		assertEquals(list.size(), 2);
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
	 * Test the method {@link SortedList#add(Object)}
	 */
	@Test
	public void testAddAllWithIndex()
	{
		List<String> list;
		List<String> addition;

		addition = ListFactory.newArrayList("", "Emil", "Anton", "Anton", "Anton", "Emil", "");
		list = new SortedList<>();
		list.add("Emil");
		list.add("Caesar");
		list.addAll(1, addition);
		assertEquals(list.size(), 9);

		addition = ListFactory.newArrayList();
		list = new SortedList<>();
		list.add("Emil");
		list.add("Caesar");
		list.addAll(1, addition);
		assertEquals(list.size(), 2);
	}

	/**
	 * Test the method {@link SortedList#add(Object)} without a comparator: the element is appended
	 * and the method returns true, also for an element the list already contains
	 */
	@Test
	public void testAddWithoutComparator()
	{
		List<String> list = new SortedList<>();

		assertTrue(list.add("Leonidas"));
		assertTrue(list.add("Berta"));
		assertTrue(list.add("Leonidas"));
		assertEquals(Arrays.asList("Leonidas", "Berta", "Leonidas"), list);
	}

	/**
	 * Test the method {@link SortedList#add(Object)} with a comparator: the element is inserted at
	 * its place in the order of the comparator and the method returns true, also for an element the
	 * list already contains
	 */
	@Test
	public void testAddWithComparator()
	{
		List<String> list = new SortedList<>(Comparator.naturalOrder());

		assertTrue(list.add("Leonidas"));
		assertTrue(list.add("Berta"));
		assertTrue(list.add("Leonidas"));
		assertEquals(Arrays.asList("Berta", "Leonidas", "Leonidas"), list);
	}

	/**
	 * Test the method {@link SortedList#add(Object)} with a comparator that holds "a" and "A", and
	 * "b" and "B", equal, which equals does not: each goes in next to the element the comparator
	 * holds it equal to, and the method returns true (#40)
	 */
	@Test
	public void testAddWithComparatorInconsistentWithEquals()
	{
		List<String> list = new SortedList<>(String.CASE_INSENSITIVE_ORDER);
		list.add("a");
		list.add("b");

		assertTrue(list.add("A"));
		assertTrue(list.add("B"));
		assertEquals(Arrays.asList("A", "a", "B", "b"), list);
	}

	/**
	 * Test the method {@link SortedList#addAll(Collection)} with a collection of elements: they are
	 * inserted in the order of the comparator and the method returns true
	 */
	@Test
	public void testAddAllWithElements()
	{
		List<String> list = new SortedList<>(Comparator.naturalOrder());
		list.add("Emil");

		assertTrue(list.addAll(Arrays.asList("Caesar", "Emil", "Anton")));
		assertEquals(Arrays.asList("Anton", "Caesar", "Emil", "Emil"), list);
	}

	/**
	 * Test the method {@link SortedList#addAll(Collection)} with an empty collection, which leaves
	 * the list unchanged and returns false
	 */
	@Test
	public void testAddAllWithEmptyCollection()
	{
		List<String> list = new SortedList<>(Comparator.naturalOrder());
		list.add("Emil");

		assertFalse(list.addAll(Collections.emptyList()));
		assertEquals(Arrays.asList("Emil"), list);
	}

	/**
	 * Test the method {@link SortedList#addAll(int, Collection)} with a collection of elements: as
	 * with every add method of the class, they are inserted in the order of the comparator and not
	 * at the given index, and the method returns true
	 */
	@Test
	public void testAddAllWithIndexAndElements()
	{
		List<String> list = new SortedList<>(Comparator.naturalOrder());
		list.add("Emil");

		assertTrue(list.addAll(0, Arrays.asList("Caesar", "Anton")));
		assertEquals(Arrays.asList("Anton", "Caesar", "Emil"), list);
	}

	/**
	 * Test the method {@link SortedList#addAll(int, Collection)} with an empty collection, which
	 * leaves the list unchanged and returns false
	 */
	@Test
	public void testAddAllWithIndexAndEmptyCollection()
	{
		List<String> list = new SortedList<>(Comparator.naturalOrder());
		list.add("Emil");

		assertFalse(list.addAll(0, Collections.emptyList()));
		assertEquals(Arrays.asList("Emil"), list);
	}

	/**
	 * Test the method {@link SortedList#add(int, Object)} with a negative index and one past the
	 * end of the list: the index is ignored and not checked, and the element goes in at its place
	 * in the order of the comparator, or at the end of a list without one (#40)
	 */
	@Test
	public void testAddWithIndexOutOfRange()
	{
		List<String> list = new SortedList<>();
		list.add(-1, "Caesar");
		list.add(5, "Anton");
		assertEquals(Arrays.asList("Caesar", "Anton"), list);

		list = new SortedList<>(Comparator.naturalOrder());
		list.add(-1, "Caesar");
		list.add(5, "Anton");
		assertEquals(Arrays.asList("Anton", "Caesar"), list);
	}

	/**
	 * Test the method {@link SortedList#addAll(int, Collection)} with a negative index and one past
	 * the end of the list: the index is ignored and not checked, the elements go in at their place
	 * in the order of the comparator, or at the end of a list without one, and the method returns
	 * true (#40)
	 */
	@Test
	public void testAddAllWithIndexOutOfRange()
	{
		List<String> list = new SortedList<>();
		assertTrue(list.addAll(-1, Arrays.asList("Caesar", "Anton")));
		assertTrue(list.addAll(5, Arrays.asList("Berta")));
		assertEquals(Arrays.asList("Caesar", "Anton", "Berta"), list);

		list = new SortedList<>(Comparator.naturalOrder());
		assertTrue(list.addAll(-1, Arrays.asList("Caesar", "Anton")));
		assertTrue(list.addAll(5, Arrays.asList("Berta")));
		assertEquals(Arrays.asList("Anton", "Berta", "Caesar"), list);
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
	 * Test the method {@link SortedList#addAll(Collection)} without a comparator: the elements are
	 * appended in the order of the collection, an element the list contains already too, and the
	 * method returns true (#66)
	 */
	@Test
	public void testAddAllWithoutComparator()
	{
		List<String> list = new SortedList<>();
		list.add("Emil");

		assertTrue(list.addAll(Arrays.asList("Caesar", "Emil", "Anton")));
		assertEquals(Arrays.asList("Emil", "Caesar", "Emil", "Anton"), list);
	}

	/**
	 * Test the method {@link SortedList#set(int, Object)} with a comparator and elements that break
	 * the order at the index, behind and in front of it: set(0, d) and set(2, b) of [a, c, e] throw
	 * an UnsupportedOperationException, as TreeSet.addFirst does, with the element and the index in
	 * the message, and the list stays [a, c, e] (#78)
	 */
	@Test
	public void testSetWithComparator()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "c", "e"),
			Comparator.naturalOrder());

		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> list.set(0, "d"));
		assertEquals("The comparator of a SortedList determines the position of it"
			+ "s elements, so explicit positioning is not supported: the element d at the index 0 would break its order",
			exception.getMessage());
		assertThrows(UnsupportedOperationException.class, () -> list.set(2, "b"));
		assertEquals(Arrays.asList("a", "c", "e"), list);
	}

	/**
	 * Test the method {@link SortedList#set(int, Object)} with an element the comparator cannot
	 * compare, null for the natural order: the comparator throws a NullPointerException before the
	 * list changes, and the list stays [a, c] (#77, #78)
	 */
	@Test
	public void testSetWithElementTheComparatorCannotCompare()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "c"), Comparator.naturalOrder());

		assertThrows(NullPointerException.class, () -> list.set(0, null));
		assertEquals(Arrays.asList("a", "c"), list);
	}

	/**
	 * Test the method {@link SortedList#set(int, Object)} without a comparator: it replaces the
	 * element at the index, as ArrayList does, [c, a].set(0, b) returns c and gives [b, a]; before
	 * and after the fix of #77, as a list without a comparator has no order to keep
	 */
	@Test
	public void testSetWithoutComparator()
	{
		List<String> list = new SortedList<>(Arrays.asList("c", "a"));

		assertEquals("c", list.set(0, "b"));
		assertEquals(Arrays.asList("b", "a"), list);
	}

	/**
	 * Test the method {@link SortedList#set(int, Object)} with an index out of range, with and
	 * without a comparator: it throws an IndexOutOfBoundsException with the message of ArrayList,
	 * and the list does not change; before and after the fix of #77
	 */
	@Test
	public void testSetWithIndexOutOfRange()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "c"), Comparator.naturalOrder());
		IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
			() -> list.set(2, "b"));
		assertEquals("Index 2 out of bounds for length 2", exception.getMessage());
		exception = assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, "b"));
		assertEquals("Index -1 out of bounds for length 2", exception.getMessage());
		assertEquals(Arrays.asList("a", "c"), list);

		List<String> withoutComparator = new SortedList<>(Arrays.asList("c", "a"));
		exception = assertThrows(IndexOutOfBoundsException.class,
			() -> withoutComparator.set(2, "b"));
		assertEquals("Index 2 out of bounds for length 2", exception.getMessage());
		assertEquals(Arrays.asList("c", "a"), withoutComparator);
	}

	/**
	 * Test the method {@link SortedList#set(int, Object)} with a comparator and elements that keep
	 * the order at the index, at the first, a middle and the last index, and beside a neighbour
	 * equal to them: [b, d, f] gives [a, d, f] with set(0, a), [d, d, f] with set(0, d), [b, b, f]
	 * with set(1, b), [b, f, f] with set(1, f), [b, d, d] with set(2, d) and [b, d, z] with set(2,
	 * z), each set returns the element that was at the index, and a list of one element takes any
	 * element; before and after the change for #78
	 */
	@Test
	public void testSetWithComparatorAndElementThatKeepsTheOrder()
	{
		List<String> elements = Arrays.asList("b", "d", "f");

		assertSet(elements, 0, "a", Arrays.asList("a", "d", "f"));
		assertSet(elements, 0, "d", Arrays.asList("d", "d", "f"));
		assertSet(elements, 1, "b", Arrays.asList("b", "b", "f"));
		assertSet(elements, 1, "f", Arrays.asList("b", "f", "f"));
		assertSet(elements, 2, "d", Arrays.asList("b", "d", "d"));
		assertSet(elements, 2, "z", Arrays.asList("b", "d", "z"));
		assertSet(Arrays.asList("m"), 0, "z", Arrays.asList("z"));
	}

	/**
	 * Calls set with the given index and element on a {@link SortedList} of the given elements in
	 * their natural order, and asserts that it returns the element that was at the index and that
	 * the list holds the expected elements afterwards
	 *
	 * @param elements
	 *            the elements of the list, in their natural order
	 * @param index
	 *            the index to set
	 * @param element
	 *            the element to set
	 * @param expected
	 *            the elements of the list after the set
	 */
	private static void assertSet(List<String> elements, int index, String element,
		List<String> expected)
	{
		List<String> list = new SortedList<>(elements, Comparator.naturalOrder());
		assertEquals(elements.get(index), list.set(index, element));
		assertEquals(expected, list);
	}

	/**
	 * Test the method {@link SortedList#set(int, Object)} with a comparator inconsistent with
	 * equals and an element it holds equal to the neighbour in front of the index: with
	 * {@link String#CASE_INSENSITIVE_ORDER}, B keeps the order at the index 1 of [b, d, f] and
	 * replaces d there, set(1, B) gives [b, B, f], where the sorting in of #77 gave [B, b, f] (#78)
	 */
	@Test
	public void testSetWithComparatorInconsistentWithEquals()
	{
		List<String> list = new SortedList<>(Arrays.asList("b", "d", "f"),
			String.CASE_INSENSITIVE_ORDER);

		assertEquals("d", list.set(1, "B"));
		assertEquals(Arrays.asList("b", "B", "f"), list);
	}

	/**
	 * Test the method {@link SortedList#set(int, Object)} with a comparator while an iterator runs
	 * over the list: d keeps the order at the index 1 of [a, c, e] and replaces c in place, which
	 * is no structural modification, so set(1, d) after the first next() gives [a, d, e], and the
	 * iterator goes on with d and e (#78)
	 */
	@Test
	public void testSetWithComparatorWhileAnIteratorRuns()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "c", "e"),
			Comparator.naturalOrder());
		Iterator<String> iterator = list.iterator();
		assertEquals("a", iterator.next());

		list.set(1, "d");
		assertEquals(Arrays.asList("a", "d", "e"), list);
		assertEquals("d", iterator.next());
		assertEquals("e", iterator.next());
		assertFalse(iterator.hasNext());
	}

	/**
	 * Test the method {@link SortedList#replaceAll(UnaryOperator)} with a comparator and values
	 * that break the order, each at the index of the element it replaces: the operator that gives f
	 * for a gives [f, c, e] for [a, c, e], and with the comparator of the length the one that gives
	 * zzz for a gives [zzz, bb, ccc] for [a, bb, ccc]; replaceAll throws an
	 * UnsupportedOperationException, with the first value that breaks the order and its index in
	 * the message, and no element is replaced (#78)
	 */
	@Test
	public void testReplaceAllWithComparator()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "c", "e"),
			Comparator.naturalOrder());
		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> list.replaceAll(element -> "a".equals(element) ? "f" : element));
		assertEquals("The comparator of a SortedList determines the position of it"
			+ "s elements, so explicit positioning is not supported: the value c at the index 1 would break its order",
			exception.getMessage());
		assertEquals(Arrays.asList("a", "c", "e"), list);

		List<String> byLength = new SortedList<>(Arrays.asList("a", "bb", "ccc"),
			Comparator.comparingInt(String::length));
		assertThrows(UnsupportedOperationException.class,
			() -> byLength.replaceAll(element -> "a".equals(element) ? "zzz" : element));
		assertEquals(Arrays.asList("a", "bb", "ccc"), byLength);
	}

	/**
	 * Test the method {@link SortedList#replaceAll(UnaryOperator)} with a value the comparator
	 * cannot compare, null for the natural order: the comparator throws a NullPointerException, no
	 * element is replaced, and the list stays [a, c] (#77)
	 */
	@Test
	public void testReplaceAllWithValueTheComparatorCannotCompare()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "c"), Comparator.naturalOrder());

		assertThrows(NullPointerException.class,
			() -> list.replaceAll(element -> "a".equals(element) ? null : element));
		assertEquals(Arrays.asList("a", "c"), list);
	}

	/**
	 * Test the method {@link SortedList#replaceAll(UnaryOperator)} without a comparator: each
	 * element is replaced by its value at its index, as ArrayList does, [c, a] gives [C, A]; before
	 * and after the fix of #77
	 */
	@Test
	public void testReplaceAllWithoutComparator()
	{
		List<String> list = new SortedList<>(Arrays.asList("c", "a"));

		list.replaceAll(String::toUpperCase);
		assertEquals(Arrays.asList("C", "A"), list);
	}

	/**
	 * Test the method {@link SortedList#replaceAll(UnaryOperator)} with a null operator: it throws
	 * a NullPointerException, on an empty list too, as List.replaceAll says; before and after the
	 * fix of #77
	 */
	@Test
	public void testReplaceAllWithNullOperator()
	{
		List<String> list = new SortedList<>(Comparator.naturalOrder());

		assertThrows(NullPointerException.class, () -> list.replaceAll(null));
	}

	/**
	 * Test the method {@link SortedList#replaceAll(UnaryOperator)} with an operator that removes an
	 * element of the list: it throws a ConcurrentModificationException, as ArrayList does, also
	 * where the removal ends the iteration over the list; before and after the fix of #77
	 */
	@Test
	public void testReplaceAllWithOperatorThatChangesTheList()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "b"), Comparator.naturalOrder());

		assertThrows(ConcurrentModificationException.class, () -> list.replaceAll(element -> {
			list.remove("b");
			return element.toUpperCase();
		}));
	}

	/**
	 * Test the method {@link SortedList#replaceAll(UnaryOperator)} with a comparator and values
	 * that keep the order, each at the index of the element it replaces: [a, c, e] gives [aa, cc,
	 * ee] with the operator that doubles each element, and [b, b, b] with the one that gives b for
	 * each, values equal to their neighbours; before and after the change for #78
	 */
	@Test
	public void testReplaceAllWithComparatorAndValuesThatKeepTheOrder()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "c", "e"),
			Comparator.naturalOrder());
		list.replaceAll(element -> element + element);
		assertEquals(Arrays.asList("aa", "cc", "ee"), list);

		list.replaceAll(element -> "b");
		assertEquals(Arrays.asList("b", "b", "b"), list);
	}

	/**
	 * Test the method {@link SortedList#replaceAll(UnaryOperator)} with an operator that throws for
	 * the second element, with and without a comparator: the exception goes to the caller, and no
	 * element is replaced, [a, b, c] stays as it is, as all values are computed before the first is
	 * stored; before and after the change for #78
	 */
	@Test
	public void testReplaceAllWithOperatorThatThrows()
	{
		UnaryOperator<String> operator = element -> {
			if ("b".equals(element))
			{
				throw new IllegalStateException("no value for b");
			}
			return element.toUpperCase();
		};
		List<String> list = new SortedList<>(Arrays.asList("a", "b", "c"),
			Comparator.naturalOrder());
		assertThrows(IllegalStateException.class, () -> list.replaceAll(operator));
		assertEquals(Arrays.asList("a", "b", "c"), list);

		List<String> withoutComparator = new SortedList<>(Arrays.asList("a", "b", "c"));
		assertThrows(IllegalStateException.class, () -> withoutComparator.replaceAll(operator));
		assertEquals(Arrays.asList("a", "b", "c"), withoutComparator);
	}

	/**
	 * Test the method {@link SortedList#sort(Comparator)}: it throws an
	 * UnsupportedOperationException for every argument, another comparator, null and the comparator
	 * of the list, also on a list without a comparator, and the list does not change (#77)
	 */
	@Test
	public void testSort()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "b", "c"),
			Comparator.naturalOrder());

		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> list.sort(Comparator.reverseOrder()));
		assertEquals(
			"A SortedList keeps the order of its comparator; setComparator(Comparator) changes it",
			exception.getMessage());
		assertThrows(UnsupportedOperationException.class, () -> list.sort(null));
		assertThrows(UnsupportedOperationException.class,
			() -> list.sort(Comparator.naturalOrder()));
		assertEquals(Arrays.asList("a", "b", "c"), list);

		List<String> withoutComparator = new SortedList<>(Arrays.asList("b", "a"));
		assertThrows(UnsupportedOperationException.class, () -> withoutComparator.sort(null));
		assertEquals(Arrays.asList("b", "a"), withoutComparator);
	}

	/**
	 * Test {@link Collections#sort(List)} and {@link Collections#sort(List, Comparator)} on a
	 * {@link SortedList} with the reverse order: they call list.sort, null for the first, which
	 * throws an UnsupportedOperationException, and the list stays [c, b, a] (#77)
	 */
	@Test
	public void testCollectionsSort()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "b", "c"),
			Comparator.reverseOrder());
		assertEquals(Arrays.asList("c", "b", "a"), list);

		assertThrows(UnsupportedOperationException.class, () -> Collections.sort(list));
		assertThrows(UnsupportedOperationException.class,
			() -> Collections.sort(list, Comparator.reverseOrder()));
		assertEquals(Arrays.asList("c", "b", "a"), list);
	}

	/**
	 * Test the method set of the list iterator, of listIterator() and of listIterator(int): it sets
	 * through set(int, E) of the list at the index of the element last returned, with the same
	 * semantics: z after next() returned a breaks the order at the index 0 and throws the
	 * UnsupportedOperationException of set, the list stays [a, c] and the iterator goes on with c;
	 * b after previous() returned c keeps the order at the index 1 and gives [a, b], and previous()
	 * returns a (#78)
	 */
	@Test
	public void testListIteratorSet()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "c"), Comparator.naturalOrder());
		ListIterator<String> iterator = list.listIterator();
		assertEquals("a", iterator.next());
		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> iterator.set("z"));
		assertEquals("The comparator of a SortedList determines the position of it"
			+ "s elements, so explicit positioning is not supported: the element z at the index 0 would break its order",
			exception.getMessage());
		assertEquals(Arrays.asList("a", "c"), list);
		assertEquals("c", iterator.next());

		ListIterator<String> fromIndex = list.listIterator(2);
		assertEquals("c", fromIndex.previous());
		fromIndex.set("b");
		assertEquals(Arrays.asList("a", "b"), list);
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
	 * Test the method {@link SortedList#addAll(Collection)} with the list itself: it adds a copy of
	 * its elements, as ArrayList does, [a, b] gives [a, a, b, b] with the natural order and [b, a,
	 * b, a] without a comparator, and the method returns true (#77)
	 */
	@Test
	public void testAddAllOfTheListItself()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "b"), Comparator.naturalOrder());
		assertTrue(list.addAll(list));
		assertEquals(Arrays.asList("a", "a", "b", "b"), list);

		List<String> withoutComparator = new SortedList<>(Arrays.asList("b", "a"));
		assertTrue(withoutComparator.addAll(withoutComparator));
		assertEquals(Arrays.asList("b", "a", "b", "a"), withoutComparator);
	}

	/**
	 * Test {@link Collections#swap(List, int, int)} on a {@link SortedList} with the natural order:
	 * it calls set(j, get(i)) and then set(i, previous), and each set replaces only where the
	 * element keeps the order. A swap of an index with itself works. swap(0, 3) of [a, b, c, d]
	 * throws an UnsupportedOperationException at its first set, a at 3, and the list does not
	 * change. swap(0, 1), two neighbours, sets a at 1, which keeps the order, and throws at its
	 * second set, b at 0: the list is [a, a, c, d] and has lost b (#78)
	 */
	@Test
	public void testCollectionsSwapWithComparator()
	{
		List<String> elements = Arrays.asList("a", "b", "c", "d");
		List<String> list = new SortedList<>(elements, Comparator.naturalOrder());

		Collections.swap(list, 1, 1);
		assertEquals(elements, list);
		assertThrows(UnsupportedOperationException.class, () -> Collections.swap(list, 0, 3));
		assertEquals(elements, list);
		assertThrows(UnsupportedOperationException.class, () -> Collections.swap(list, 0, 1));
		assertEquals(Arrays.asList("a", "a", "c", "d"), list);
	}

	/**
	 * Test {@link Collections#reverse(List)}, {@link Collections#shuffle(List, Random)} and
	 * {@link Collections#rotate(List, int)} on a {@link SortedList} with the natural order: they
	 * set one element after another, each set replaces only where the element keeps the order, and
	 * the first set that does not throws an UnsupportedOperationException, with the elements set
	 * before it set. Reverse of [a, b, c, d] throws at its first swap, a at 3, and the list does
	 * not change; reverse of [a, b] sets a at 1 and throws for b at 0, [a, a]. Shuffle with the
	 * seed 77 swaps 3 and 2 first, sets d at 2 and throws for c at 3, [a, b, d, d]. Rotate by 1
	 * sets a at 1, b at 2 and c at 3 and throws for d at 0, [a, a, b, c]; rotate by -1 throws at
	 * its first set, a at 3, and the list does not change (#78)
	 */
	@Test
	public void testCollectionsAlgorithmsWithComparator()
	{
		List<String> elements = Arrays.asList("a", "b", "c", "d");

		List<String> reversed = new SortedList<>(elements, Comparator.naturalOrder());
		assertThrows(UnsupportedOperationException.class, () -> Collections.reverse(reversed));
		assertEquals(elements, reversed);
		List<String> two = new SortedList<>(Arrays.asList("a", "b"), Comparator.naturalOrder());
		assertThrows(UnsupportedOperationException.class, () -> Collections.reverse(two));
		assertEquals(Arrays.asList("a", "a"), two);

		List<String> shuffled = new SortedList<>(elements, Comparator.naturalOrder());
		assertThrows(UnsupportedOperationException.class,
			() -> Collections.shuffle(shuffled, new Random(77)));
		assertEquals(Arrays.asList("a", "b", "d", "d"), shuffled);

		List<String> rotated = new SortedList<>(elements, Comparator.naturalOrder());
		assertThrows(UnsupportedOperationException.class, () -> Collections.rotate(rotated, 1));
		assertEquals(Arrays.asList("a", "a", "b", "c"), rotated);
		List<String> rotatedBack = new SortedList<>(elements, Comparator.naturalOrder());
		assertThrows(UnsupportedOperationException.class,
			() -> Collections.rotate(rotatedBack, -1));
		assertEquals(elements, rotatedBack);
	}

	/**
	 * Test {@link Collections#swap(List, int, int)} and {@link Collections#reverse(List)} on a
	 * {@link SortedList} with the comparator of the length and elements it holds equal, which the
	 * list holds in front of each other in the order opposite to the one they were added in: each
	 * set keeps the order, so swap(1, 2) of [a, cc, bb, ddd] gives [a, bb, cc, ddd], and reverse of
	 * [cc, bb, aa] gives [aa, bb, cc], as on an ArrayList, where the sorting in of #77 gave [aa,
	 * cc, bb] (#78)
	 */
	@Test
	public void testCollectionsAlgorithmsWithElementsTheComparatorHoldsEqual()
	{
		Comparator<String> byLength = Comparator.comparingInt(String::length);

		List<String> list = new SortedList<>(Arrays.asList("a", "bb", "cc", "ddd"), byLength);
		assertEquals(Arrays.asList("a", "cc", "bb", "ddd"), list);
		Collections.swap(list, 1, 2);
		assertEquals(Arrays.asList("a", "bb", "cc", "ddd"), list);

		list = new SortedList<>(Arrays.asList("aa", "bb", "cc"), byLength);
		assertEquals(Arrays.asList("cc", "bb", "aa"), list);
		Collections.reverse(list);
		assertEquals(Arrays.asList("aa", "bb", "cc"), list);
	}

	/**
	 * Test {@link Collections#swap(List, int, int)}, {@link Collections#reverse(List)},
	 * {@link Collections#shuffle(List, Random)} and {@link Collections#rotate(List, int)} on a
	 * {@link SortedList} without a comparator: they work as on an ArrayList, as set replaces in
	 * place; before and after the fix of #77
	 */
	@Test
	public void testCollectionsAlgorithmsWithoutComparator()
	{
		List<String> elements = Arrays.asList("a", "b", "c", "d");
		List<String> list;

		list = new SortedList<>(elements);
		Collections.swap(list, 0, 3);
		assertEquals(Arrays.asList("d", "b", "c", "a"), list);

		list = new SortedList<>(elements);
		Collections.reverse(list);
		assertEquals(Arrays.asList("d", "c", "b", "a"), list);

		List<String> shuffled = new ArrayList<>(elements);
		Collections.shuffle(shuffled, new Random(77));
		list = new SortedList<>(elements);
		Collections.shuffle(list, new Random(77));
		assertEquals(shuffled, list);

		list = new SortedList<>(elements);
		Collections.rotate(list, 1);
		assertEquals(Arrays.asList("d", "a", "b", "c"), list);
	}

	/**
	 * Test set and replaceAll of the view of {@link SortedList#subList(int, int)} and of a view of
	 * it: each throws an UnsupportedOperationException, for an element the list holds and for a new
	 * one, and neither the list nor the view changes
	 */
	@Test
	public void testSubListSetAndReplaceAll()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "b", "c", "d"),
			Comparator.naturalOrder());
		List<String> view = list.subList(1, 3);

		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> view.set(0, "z"));
		assertEquals("A subList view of a SortedList reads and removes only; set and add through"
			+ " the list itself", exception.getMessage());
		assertThrows(UnsupportedOperationException.class, () -> view.set(0, "a"));
		assertThrows(UnsupportedOperationException.class,
			() -> view.replaceAll(String::toUpperCase));
		assertThrows(UnsupportedOperationException.class, () -> view.subList(0, 1).set(0, "z"));
		assertThrows(UnsupportedOperationException.class,
			() -> view.subList(0, 1).replaceAll(String::toUpperCase));
		assertEquals(Arrays.asList("a", "b", "c", "d"), list);
		assertEquals(Arrays.asList("b", "c"), view);
	}

	/**
	 * Test the add methods of the view of {@link SortedList#subList(int, int)} and of a view of it:
	 * add(E), add(int, E), addAll and addAll(int, Collection) throw an
	 * UnsupportedOperationException, with an empty collection too, and neither the list nor the
	 * view changes; the last case of #78, add(z) on subList(0, 1) of [a, c, e], too
	 */
	@Test
	public void testSubListAdd()
	{
		List<String> sorted = new SortedList<>(Arrays.asList("a", "c", "e"),
			Comparator.naturalOrder());
		List<String> first = sorted.subList(0, 1);
		assertThrows(UnsupportedOperationException.class, () -> first.add("z"));
		assertEquals(Arrays.asList("a", "c", "e"), sorted);
		assertEquals(Arrays.asList("a"), first);

		List<String> list = new SortedList<>(Arrays.asList("a", "b", "c", "d"),
			Comparator.naturalOrder());
		List<String> view = list.subList(1, 3);

		assertThrows(UnsupportedOperationException.class, () -> view.add("e"));
		assertThrows(UnsupportedOperationException.class, () -> view.add(0, "e"));
		assertThrows(UnsupportedOperationException.class, () -> view.addAll(Arrays.asList("e")));
		assertThrows(UnsupportedOperationException.class, () -> view.addAll(0, Arrays.asList("e")));
		assertThrows(UnsupportedOperationException.class,
			() -> view.addAll(Collections.emptyList()));
		assertThrows(UnsupportedOperationException.class,
			() -> view.addAll(0, Collections.emptyList()));
		assertThrows(UnsupportedOperationException.class, () -> view.subList(0, 1).add("e"));
		assertThrows(UnsupportedOperationException.class,
			() -> view.subList(0, 1).addAll(Arrays.asList("e")));
		assertEquals(Arrays.asList("a", "b", "c", "d"), list);
		assertEquals(Arrays.asList("b", "c"), view);
	}

	/**
	 * Test sort of the view of {@link SortedList#subList(int, int)} and of a view of it, and
	 * {@link Collections#reverse(List)} and {@link Collections#swap(List, int, int)}, which work
	 * through its set: each throws an UnsupportedOperationException, sort with null, with the
	 * comparator of the list and on an empty view too, and the list does not change
	 */
	@Test
	public void testSubListSort()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "b", "c", "d"),
			Comparator.naturalOrder());
		List<String> view = list.subList(1, 3);

		assertThrows(UnsupportedOperationException.class,
			() -> view.sort(Comparator.reverseOrder()));
		assertThrows(UnsupportedOperationException.class, () -> view.sort(null));
		assertThrows(UnsupportedOperationException.class,
			() -> view.sort(Comparator.naturalOrder()));
		assertThrows(UnsupportedOperationException.class, () -> list.subList(2, 2).sort(null));
		assertThrows(UnsupportedOperationException.class, () -> view.subList(0, 2).sort(null));
		assertThrows(UnsupportedOperationException.class, () -> Collections.reverse(view));
		assertThrows(UnsupportedOperationException.class, () -> Collections.swap(view, 0, 1));
		assertEquals(Arrays.asList("a", "b", "c", "d"), list);
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
	 * Test the methods {@link SortedList#addFirst(Object)} and {@link SortedList#addLast(Object)}:
	 * they throw an UnsupportedOperationException, as those of SortedSet and TreeSet do, the
	 * comparator determines the position; with and without a comparator, for a new element and for
	 * one the list holds, and the list does not change (#87)
	 */
	@Test
	public void testAddFirstAndAddLast()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "b"), Comparator.naturalOrder());
		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> list.addFirst("z"));
		assertEquals("The comparator of a SortedList determines the position of its elements, so"
			+ " explicit positioning is not supported", exception.getMessage());
		assertThrows(UnsupportedOperationException.class, () -> list.addLast("0"));
		assertThrows(UnsupportedOperationException.class, () -> list.addFirst("b"));
		assertEquals(Arrays.asList("a", "b"), list);

		List<String> withoutComparator = new SortedList<>(Arrays.asList("b", "a"));
		assertThrows(UnsupportedOperationException.class, () -> withoutComparator.addFirst("z"));
		assertThrows(UnsupportedOperationException.class, () -> withoutComparator.addLast("z"));
		assertEquals(Arrays.asList("b", "a"), withoutComparator);
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
	 * Test the changes through {@link SortedList#reversed()}: the view reads and removes, as the
	 * view of subList, and add, add(int, E), addFirst, addLast, addAll, set, replaceAll and sort
	 * through it, and the set and add of its list iterator, throw an UnsupportedOperationException,
	 * and the list does not change (#87)
	 */
	@Test
	public void testReversed()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "b", "c"),
			Comparator.naturalOrder());
		List<String> reversed = list.reversed();

		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> reversed.addFirst("0"));
		assertEquals("The reversed view of a SortedList reads and removes only; add through the"
			+ " list itself", exception.getMessage());
		assertThrows(UnsupportedOperationException.class, () -> reversed.addLast("z"));
		assertThrows(UnsupportedOperationException.class, () -> reversed.add("z"));
		assertThrows(UnsupportedOperationException.class, () -> reversed.add(0, "z"));
		assertThrows(UnsupportedOperationException.class,
			() -> reversed.addAll(Arrays.asList("z")));
		assertThrows(UnsupportedOperationException.class, () -> reversed.set(0, "y"));
		assertThrows(UnsupportedOperationException.class,
			() -> reversed.replaceAll(String::toUpperCase));
		assertThrows(UnsupportedOperationException.class, () -> reversed.sort(null));
		ListIterator<String> iterator = reversed.listIterator();
		assertEquals("c", iterator.next());
		assertThrows(UnsupportedOperationException.class, () -> iterator.set("y"));
		assertThrows(UnsupportedOperationException.class, () -> iterator.add("y"));
		assertThrows(UnsupportedOperationException.class,
			() -> reversed.subList(0, 2).addFirst("0"));
		assertEquals(Arrays.asList("a", "b", "c"), list);
		assertEquals(Arrays.asList("c", "b", "a"), reversed);
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
