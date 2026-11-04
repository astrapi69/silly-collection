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
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.astrapi69.comparator.object.StringComparator;

/**
 * The unit test class for {@link SortedUniqueList}
 */
public class SortedUniqueListTest
{

	/**
	 * Test the method {@link SortedUniqueList#add(Object)}
	 */
	@Test
	public void testAddUniqueElement()
	{
		List<String> list;

		list = new SortedUniqueList<>(6);
		list.add("Leonidas");
		list.add("Leonidas");
		assertEquals(list.size(), 1);
		list.add("Berta");
		list.add("Berta");
		assertEquals(list.size(), 2);
		list.add("Caesar");
		list.add("Caesar");
		assertEquals(list.size(), 3);
		list.add("Dora");
		list.add("Dora");
		assertEquals(list.size(), 4);
		list.add("Emil");
		list.add("Emil");
		assertEquals(list.size(), 5);
		list.add("Anton");
		list.add("Anton");
		assertEquals(list.size(), 6);
	}

	/**
	 * Test the method {@link SortedUniqueList#add(Object)}
	 */
	@Test
	public void testAddAllWithIndex()
	{
		List<String> list;
		List<String> addition;

		addition = ListFactory.newArrayList("", "Emil", "Anton", "Anton", "Anton", "Emil", "");
		list = new SortedUniqueList<>();
		list.add("Emil");
		list.add("Caesar");
		list.addAll(1, addition);
		assertEquals(list.size(), 4);

		addition = ListFactory.newArrayList();
		list = new SortedUniqueList<>();
		list.add("Emil");
		list.add("Caesar");
		list.addAll(1, addition);
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
		list.sort(comparator);

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
	 * Test the method {@link SortedUniqueList#addAll(Collection)}
	 */
	@Test
	public void testAddAll()
	{
		List<String> list;
		List<String> addition;

		addition = ListFactory.newArrayList("", "Emil", "Anton", "Anton", "Anton", "Emil", "");
		list = new SortedUniqueList<>(new StringComparator());
		list.add("Emil");
		list.add("Caesar");
		list.addAll(addition);
		assertEquals(list.size(), 4);

		addition = ListFactory.newArrayList();
		list = new SortedUniqueList<>();
		list.add("Emil");
		list.add("Caesar");
		list.addAll(addition);
		assertEquals(list.size(), 2);
	}

	/**
	 * Test the method {@link SortedUniqueList#add(Object)}
	 */
	@Test
	public void testAddWithIndex()
	{
		List<String> list;

		list = new SortedUniqueList<>();
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
	 * Test the method {@link SortedUniqueList#add(Object)}
	 */
	@Test
	public void testAddWithString()
	{
		List<String> list;

		list = new SortedUniqueList<>(Comparator.naturalOrder());
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
	 * Test method for {@link SortedUniqueList#add(Object)}.
	 */
	@Test
	@DisplayName("Test adding elements and maintaining sorted uniqueness")
	public void testAdd()
	{
		SortedUniqueList<Integer> list = new SortedUniqueList<>(Comparator.naturalOrder());
		assertTrue(list.add(3));
		assertTrue(list.add(1));
		assertTrue(list.add(2));
		assertFalse(list.add(3));
		assertEquals(Arrays.asList(1, 2, 3), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#add(Object)} with a comparator that holds "a" and
	 * "A", and "b" and "B", equal, which equals does not: each goes in next to the element the
	 * comparator holds it equal to, and the method returns true. The list stays unique by equals:
	 * an element it contains is not added again, and the method returns false (#40)
	 */
	@Test
	public void testAddWithComparatorInconsistentWithEquals()
	{
		List<String> list = new SortedUniqueList<>(String.CASE_INSENSITIVE_ORDER);
		list.add("a");
		list.add("b");

		assertTrue(list.add("A"));
		assertTrue(list.add("B"));
		assertFalse(list.add("a"));
		assertFalse(list.add("B"));
		assertEquals(Arrays.asList("A", "a", "B", "b"), list);
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
	 * Test the method {@link SortedUniqueList#addAll(Collection)} with a collection of new elements
	 * and ones the list already contains: the new ones are inserted in the order of the comparator
	 * and the method returns true
	 */
	@Test
	public void testAddAllWithNewElements()
	{
		List<String> list = new SortedUniqueList<>(Comparator.naturalOrder());
		list.add("Emil");

		assertTrue(list.addAll(Arrays.asList("Emil", "Caesar", "Anton", "Emil")));
		assertEquals(Arrays.asList("Anton", "Caesar", "Emil"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#addAll(Collection)} with a collection of elements the
	 * list already contains, which leaves the list unchanged and returns false
	 */
	@Test
	public void testAddAllWithContainedElements()
	{
		List<String> list = new SortedUniqueList<>(Comparator.naturalOrder());
		list.add("Anton");
		list.add("Emil");

		assertFalse(list.addAll(Arrays.asList("Emil", "Anton")));
		assertEquals(Arrays.asList("Anton", "Emil"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#addAll(Collection)} with an empty collection, which
	 * leaves the list unchanged and returns false
	 */
	@Test
	public void testAddAllWithEmptyCollection()
	{
		List<String> list = new SortedUniqueList<>(Comparator.naturalOrder());
		list.add("Emil");

		assertFalse(list.addAll(Collections.emptyList()));
		assertEquals(Arrays.asList("Emil"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#addAll(int, Collection)} with a collection of new
	 * elements and one the list already contains: as with every add method of the class, the new
	 * ones are inserted in the order of the comparator and not at the given index, and the method
	 * returns true
	 */
	@Test
	public void testAddAllWithIndexAndNewElements()
	{
		List<String> list = new SortedUniqueList<>(Comparator.naturalOrder());
		list.add("Emil");

		assertTrue(list.addAll(0, Arrays.asList("Emil", "Caesar", "Anton")));
		assertEquals(Arrays.asList("Anton", "Caesar", "Emil"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#addAll(int, Collection)} with a collection of
	 * elements the list already contains, which leaves the list unchanged and returns false
	 */
	@Test
	public void testAddAllWithIndexAndContainedElements()
	{
		List<String> list = new SortedUniqueList<>(Comparator.naturalOrder());
		list.add("Anton");
		list.add("Emil");

		assertFalse(list.addAll(0, Arrays.asList("Emil", "Anton")));
		assertEquals(Arrays.asList("Anton", "Emil"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#add(int, Object)} with a negative index and one past
	 * the end of the list: the index is ignored and not checked, and an element the list does not
	 * contain yet goes in at its place in the order of the comparator, or at the end of a list
	 * without one (#40)
	 */
	@Test
	public void testAddWithIndexOutOfRange()
	{
		List<String> list = new SortedUniqueList<>();
		list.add(-1, "Caesar");
		list.add(5, "Anton");
		list.add(-1, "Caesar");
		assertEquals(Arrays.asList("Caesar", "Anton"), list);

		list = new SortedUniqueList<>(Comparator.naturalOrder());
		list.add(-1, "Caesar");
		list.add(5, "Anton");
		list.add(5, "Caesar");
		assertEquals(Arrays.asList("Anton", "Caesar"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#addAll(int, Collection)} with a negative index and
	 * one past the end of the list: the index is ignored and not checked, the elements the list
	 * does not contain yet go in at their place in the order of the comparator, or at the end of a
	 * list without one, and the method returns true (#40)
	 */
	@Test
	public void testAddAllWithIndexOutOfRange()
	{
		List<String> list = new SortedUniqueList<>();
		assertTrue(list.addAll(-1, Arrays.asList("Caesar", "Anton")));
		assertTrue(list.addAll(5, Arrays.asList("Anton", "Berta")));
		assertEquals(Arrays.asList("Caesar", "Anton", "Berta"), list);

		list = new SortedUniqueList<>(Comparator.naturalOrder());
		assertTrue(list.addAll(-1, Arrays.asList("Caesar", "Anton")));
		assertTrue(list.addAll(5, Arrays.asList("Anton", "Berta")));
		assertEquals(Arrays.asList("Anton", "Berta", "Caesar"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#add(Object)} without a comparator: an element the
	 * list does not contain yet is appended and the method returns true, for an element the list
	 * contains already it returns false and the list does not change (#66)
	 */
	@Test
	public void testAddWithoutComparator()
	{
		List<String> list = new SortedUniqueList<>();

		assertTrue(list.add("Leonidas"));
		assertTrue(list.add("Berta"));
		assertFalse(list.add("Leonidas"));
		assertEquals(Arrays.asList("Leonidas", "Berta"), list);
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
	 * Test the method {@link SortedUniqueList#addAll(Collection)} without a comparator: the
	 * elements the list does not contain yet are appended in the order of the collection, each of
	 * them once, and the method returns true (#66)
	 */
	@Test
	public void testAddAllWithoutComparator()
	{
		List<String> list = new SortedUniqueList<>();
		list.add("Emil");

		assertTrue(list.addAll(Arrays.asList("Caesar", "Emil", "Anton", "Caesar")));
		assertEquals(Arrays.asList("Emil", "Caesar", "Anton"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#set(int, Object)} with an element the list holds at
	 * another index, with and without a comparator: it replaces the element at the index, as
	 * ArrayList does, and the list holds the element twice; [a, b, c].set(0, c) returns a and gives
	 * [c, b, c], [a, b, c].set(2, a) gives [a, b, a], and [b, a].set(0, a) without a comparator
	 * gives [a, a] (#77).
	 * <p>
	 * The fix of #77 changes the result: each call throws an IllegalArgumentException, and the list
	 * does not change.
	 */
	@Test
	public void testSetWithElementHeldAtAnotherIndex()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"),
			Comparator.naturalOrder());
		assertEquals("a", list.set(0, "c"));
		assertEquals(Arrays.asList("c", "b", "c"), list);

		list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"), Comparator.naturalOrder());
		assertEquals("c", list.set(2, "a"));
		assertEquals(Arrays.asList("a", "b", "a"), list);

		list = new SortedUniqueList<>(Arrays.asList("b", "a"));
		assertEquals("b", list.set(0, "a"));
		assertEquals(Arrays.asList("a", "a"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#set(int, Object)} with a comparator and an element
	 * the list does not hold: it replaces the element at the index, as ArrayList does, and the list
	 * leaves the order of its comparator; [a, c, e].set(0, d) returns a and gives [d, c, e], and
	 * [a, c, e].set(2, b) returns e and gives [a, c, b] (#77).
	 * <p>
	 * The fix of #77 changes the result: the given element is sorted in, [c, d, e] and [a, b, c].
	 */
	@Test
	public void testSetWithComparator()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "c", "e"),
			Comparator.naturalOrder());
		assertEquals("a", list.set(0, "d"));
		assertEquals(Arrays.asList("d", "c", "e"), list);

		list = new SortedUniqueList<>(Arrays.asList("a", "c", "e"), Comparator.naturalOrder());
		assertEquals("e", list.set(2, "b"));
		assertEquals(Arrays.asList("a", "c", "b"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#set(int, Object)} with an element equal to the one at
	 * the index, with and without a comparator: the given instance replaces the one at the index,
	 * the method returns the previous one, and the list keeps its order; before and after the fix
	 * of #77
	 */
	@Test
	public void testSetWithElementHeldAtTheIndex()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"),
			Comparator.naturalOrder());
		String previous = list.get(1);
		String equal = new String("b");
		assertSame(previous, list.set(1, equal));
		assertSame(equal, list.get(1));
		assertEquals(Arrays.asList("a", "b", "c"), list);

		list = new SortedUniqueList<>(Arrays.asList("b", "a"));
		previous = list.get(0);
		equal = new String("b");
		assertSame(previous, list.set(0, equal));
		assertSame(equal, list.get(0));
		assertEquals(Arrays.asList("b", "a"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#set(int, Object)} without a comparator and with an
	 * element the list does not hold: it replaces the element at the index, as ArrayList does, [c,
	 * a].set(0, b) returns c and gives [b, a]; before and after the fix of #77, as a list without a
	 * comparator has no order to keep
	 */
	@Test
	public void testSetWithoutComparator()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("c", "a"));

		assertEquals("c", list.set(0, "b"));
		assertEquals(Arrays.asList("b", "a"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#set(int, Object)} with an index out of range, with
	 * and without a comparator: it throws an IndexOutOfBoundsException with the message of
	 * ArrayList, for an element the list holds too, and the list does not change; before and after
	 * the fix of #77
	 */
	@Test
	public void testSetWithIndexOutOfRange()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b"),
			Comparator.naturalOrder());
		IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
			() -> list.set(2, "a"));
		assertEquals("Index 2 out of bounds for length 2", exception.getMessage());
		exception = assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, "b"));
		assertEquals("Index -1 out of bounds for length 2", exception.getMessage());
		assertEquals(Arrays.asList("a", "b"), list);

		List<String> withoutComparator = new SortedUniqueList<>(Arrays.asList("b", "a"));
		exception = assertThrows(IndexOutOfBoundsException.class,
			() -> withoutComparator.set(2, "a"));
		assertEquals("Index 2 out of bounds for length 2", exception.getMessage());
		assertEquals(Arrays.asList("b", "a"), withoutComparator);
	}

	/**
	 * Test the method {@link SortedUniqueList#set(int, Object)} with an element the comparator
	 * cannot compare, null for the natural order: it is stored as ArrayList stores it, [a,
	 * c].set(0, null) gives [null, c] (#77).
	 * <p>
	 * The fix of #77 changes the result: the comparator throws a NullPointerException, and the list
	 * stays [a, c].
	 */
	@Test
	public void testSetWithElementTheComparatorCannotCompare()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "c"),
			Comparator.naturalOrder());

		assertEquals("a", list.set(0, null));
		assertEquals(Arrays.asList(null, "c"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#replaceAll(UnaryOperator)} with an operator that
	 * gives two equal values: it writes them, as ArrayList does, and the list holds x three times,
	 * [x, x, x] (#77).
	 * <p>
	 * The fix of #77 changes the result: it throws an IllegalArgumentException and replaces no
	 * element, the list stays [a, b, c].
	 */
	@Test
	public void testReplaceAllWithEqualValues()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"),
			Comparator.naturalOrder());

		list.replaceAll(element -> "x");
		assertEquals(Arrays.asList("x", "x", "x"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#replaceAll(UnaryOperator)} with a comparator and
	 * distinct values: it writes each value at the index of the element it replaces, as ArrayList
	 * does, and the list leaves the order of its comparator; the operator that gives f for a gives
	 * [f, c, e] for [a, c, e], and the one that gives b for a, c for b and a for c gives [b, c, a]
	 * for [a, b, c] (#77).
	 * <p>
	 * The fix of #77 changes the result: the values are sorted in, [c, e, f] and [a, b, c].
	 */
	@Test
	public void testReplaceAllWithComparator()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "c", "e"),
			Comparator.naturalOrder());
		list.replaceAll(element -> "a".equals(element) ? "f" : element);
		assertEquals(Arrays.asList("f", "c", "e"), list);

		list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"), Comparator.naturalOrder());
		list.replaceAll(element -> element.equals("a") ? "b" : element.equals("b") ? "c" : "a");
		assertEquals(Arrays.asList("b", "c", "a"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#replaceAll(UnaryOperator)} with a value the
	 * comparator cannot compare, null for the natural order: it is stored as ArrayList stores it,
	 * [a, c] gives [null, c] (#77).
	 * <p>
	 * The fix of #77 changes the result: the comparator throws a NullPointerException, and the list
	 * stays [a, c].
	 */
	@Test
	public void testReplaceAllWithValueTheComparatorCannotCompare()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "c"),
			Comparator.naturalOrder());

		list.replaceAll(element -> "a".equals(element) ? null : element);
		assertEquals(Arrays.asList(null, "c"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#replaceAll(UnaryOperator)} without a comparator and
	 * with distinct values: each element is replaced by its value at its index, as ArrayList does,
	 * [c, a] gives [C, A]; before and after the fix of #77
	 */
	@Test
	public void testReplaceAllWithoutComparator()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("c", "a"));

		list.replaceAll(String::toUpperCase);
		assertEquals(Arrays.asList("C", "A"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#replaceAll(UnaryOperator)} with a null operator: it
	 * throws a NullPointerException, on an empty list too, as List.replaceAll says; before and
	 * after the fix of #77
	 */
	@Test
	public void testReplaceAllWithNullOperator()
	{
		List<String> list = new SortedUniqueList<>(Comparator.naturalOrder());

		assertThrows(NullPointerException.class, () -> list.replaceAll(null));
	}

	/**
	 * Test the method {@link SortedUniqueList#replaceAll(UnaryOperator)} with an operator that
	 * removes an element of the list: it throws a ConcurrentModificationException, as ArrayList
	 * does, also where the removal ends the iteration over the list; before and after the fix of
	 * #77
	 */
	@Test
	public void testReplaceAllWithOperatorThatChangesTheList()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b"),
			Comparator.naturalOrder());

		assertThrows(ConcurrentModificationException.class, () -> list.replaceAll(element -> {
			list.remove("b");
			return element.toUpperCase();
		}));
	}

	/**
	 * Test the method {@link SortedUniqueList#sort(Comparator)}: it sorts the list with the given
	 * comparator, as ArrayList does, also against the comparator of the list; [a, b, c] with the
	 * natural order gives [c, b, a] with sort(reverseOrder()), and sort(null) sorts in the natural
	 * order again (#77).
	 * <p>
	 * The fix of #77 changes the result: sort throws an UnsupportedOperationException for every
	 * argument, null and the comparator of the list too, and the list does not change.
	 */
	@Test
	public void testSort()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"),
			Comparator.naturalOrder());

		list.sort(Comparator.reverseOrder());
		assertEquals(Arrays.asList("c", "b", "a"), list);
		list.sort(null);
		assertEquals(Arrays.asList("a", "b", "c"), list);
	}

	/**
	 * Test {@link Collections#sort(List)} on a {@link SortedUniqueList} with the reverse order: it
	 * calls list.sort(null) and sorts the list against its comparator; [c, b, a] gives [a, b, c]
	 * (#77).
	 * <p>
	 * The fix of #77 changes the result: it throws an UnsupportedOperationException, and the list
	 * stays [c, b, a].
	 */
	@Test
	public void testCollectionsSort()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"),
			Comparator.reverseOrder());
		assertEquals(Arrays.asList("c", "b", "a"), list);

		Collections.sort(list);
		assertEquals(Arrays.asList("a", "b", "c"), list);
	}

	/**
	 * Test the method set of the list iterator: it is ArrayList's, which replaces the element last
	 * returned in place, [a, c] with next() and set(c) gives [c, c], a duplicate, and the iterator
	 * of listIterator(2) with previous() and set(b) gives [c, b] (#77).
	 * <p>
	 * The fix of #77 changes the result: set throws an UnsupportedOperationException, and the list
	 * stays [a, c].
	 */
	@Test
	public void testListIteratorSet()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "c"),
			Comparator.naturalOrder());
		ListIterator<String> iterator = list.listIterator();
		assertEquals("a", iterator.next());
		iterator.set("c");
		assertEquals(Arrays.asList("c", "c"), list);

		ListIterator<String> fromIndex = list.listIterator(2);
		assertEquals("c", fromIndex.previous());
		fromIndex.set("b");
		assertEquals(Arrays.asList("c", "b"), list);
	}

	/**
	 * Test the method add of the list iterator: it is ArrayList's, which calls add(cursor, e) and
	 * moves the cursor one on, also where add(int, E) of the list adds nothing for an element it
	 * holds; [a, c] with next() and add(a) stays [a, c], but nextIndex() is 2 and hasNext() false,
	 * so c is skipped; the iterator of listIterator(1) with add(b) gives [a, b, c], where the
	 * sorted place happens to be the cursor (#77).
	 * <p>
	 * The fix of #77 changes the result: add throws an UnsupportedOperationException, the list
	 * stays [a, c], nextIndex() stays 1 and next() returns c.
	 */
	@Test
	public void testListIteratorAdd()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "c"),
			Comparator.naturalOrder());
		ListIterator<String> iterator = list.listIterator();
		assertEquals("a", iterator.next());
		iterator.add("a");
		assertEquals(Arrays.asList("a", "c"), list);
		assertEquals(2, iterator.nextIndex());
		assertFalse(iterator.hasNext());

		ListIterator<String> fromIndex = list.listIterator(1);
		fromIndex.add("b");
		assertEquals(Arrays.asList("a", "b", "c"), list);
		assertEquals("c", fromIndex.next());
	}

	/**
	 * Test the method {@link SortedUniqueList#addAll(Collection)} with the list itself: it adds
	 * nothing, as the list holds each of its elements, returns false and does not throw; before and
	 * after the fix of #77
	 */
	@Test
	public void testAddAllOfTheListItself()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b"),
			Comparator.naturalOrder());

		assertFalse(list.addAll(list));
		assertEquals(Arrays.asList("a", "b"), list);
	}

	/**
	 * Test {@link Collections#swap(List, int, int)}, {@link Collections#reverse(List)},
	 * {@link Collections#shuffle(List, Random)} and {@link Collections#rotate(List, int)} on a
	 * {@link SortedUniqueList} with the natural order: they work through set, which replaces in
	 * place today, so they reorder the list against its comparator: swap(0, 3) of [a, b, c, d]
	 * gives [d, b, c, a], reverse gives [d, c, b, a], shuffle the order of an ArrayList shuffled
	 * with the same seed, and rotate by 1 gives [d, a, b, c] (#77).
	 * <p>
	 * The fix of #77 changes the result: each of them calls set with an element the list holds at
	 * another index, which throws an IllegalArgumentException before the list changes, and the list
	 * stays [a, b, c, d].
	 */
	@Test
	public void testCollectionsAlgorithmsWithComparator()
	{
		List<String> elements = Arrays.asList("a", "b", "c", "d");
		List<String> list;

		list = new SortedUniqueList<>(elements, Comparator.naturalOrder());
		Collections.swap(list, 0, 3);
		assertEquals(Arrays.asList("d", "b", "c", "a"), list);

		list = new SortedUniqueList<>(elements, Comparator.naturalOrder());
		Collections.reverse(list);
		assertEquals(Arrays.asList("d", "c", "b", "a"), list);

		List<String> shuffled = new ArrayList<>(elements);
		Collections.shuffle(shuffled, new Random(77));
		assertNotEquals(elements, shuffled);
		list = new SortedUniqueList<>(elements, Comparator.naturalOrder());
		Collections.shuffle(list, new Random(77));
		assertEquals(shuffled, list);

		list = new SortedUniqueList<>(elements, Comparator.naturalOrder());
		Collections.rotate(list, 1);
		assertEquals(Arrays.asList("d", "a", "b", "c"), list);
	}

	/**
	 * Test {@link Collections#swap(List, int, int)}, {@link Collections#reverse(List)},
	 * {@link Collections#shuffle(List, Random)} and {@link Collections#rotate(List, int)} on a
	 * {@link SortedUniqueList} without a comparator: they work as on an ArrayList, as set replaces
	 * in place; swap(0, 3) of [a, b, c, d] gives [d, b, c, a], reverse gives [d, c, b, a], shuffle
	 * the order of an ArrayList shuffled with the same seed, and rotate by 1 gives [d, a, b, c]
	 * (#77).
	 * <p>
	 * The fix of #77 changes the result: set refuses an element the list holds at another index
	 * also without a comparator, so each of them throws an IllegalArgumentException before the list
	 * changes, and the list stays [a, b, c, d]. A swap of an index with itself still works.
	 */
	@Test
	public void testCollectionsAlgorithmsWithoutComparator()
	{
		List<String> elements = Arrays.asList("a", "b", "c", "d");
		List<String> list;

		list = new SortedUniqueList<>(elements);
		Collections.swap(list, 0, 3);
		assertEquals(Arrays.asList("d", "b", "c", "a"), list);

		list = new SortedUniqueList<>(elements);
		Collections.reverse(list);
		assertEquals(Arrays.asList("d", "c", "b", "a"), list);

		List<String> shuffled = new ArrayList<>(elements);
		Collections.shuffle(shuffled, new Random(77));
		list = new SortedUniqueList<>(elements);
		Collections.shuffle(list, new Random(77));
		assertEquals(shuffled, list);

		list = new SortedUniqueList<>(elements);
		Collections.rotate(list, 1);
		assertEquals(Arrays.asList("d", "a", "b", "c"), list);

		list = new SortedUniqueList<>(elements);
		Collections.swap(list, 1, 1);
		assertEquals(elements, list);
	}
}
