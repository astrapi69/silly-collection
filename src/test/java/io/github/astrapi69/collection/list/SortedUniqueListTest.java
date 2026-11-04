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
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
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
	 * Test the method {@link SortedUniqueList#set(int, Object)} with a comparator and an element
	 * the list holds at another index, in front of and behind the given one: it throws an
	 * IllegalArgumentException, and the list does not change; set(0, c) and set(2, a) of [a, b, c]
	 * (#77)
	 */
	@Test
	public void testSetWithElementHeldAtAnotherIndex()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"),
			Comparator.naturalOrder());
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> list.set(0, "c"));
		assertEquals("This list holds the element c at the index 2, it cannot hold it at the index"
			+ " 0 too", exception.getMessage());
		assertThrows(IllegalArgumentException.class, () -> list.set(2, "a"));
		assertEquals(Arrays.asList("a", "b", "c"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#set(int, Object)} with a comparator and an element
	 * the list holds at another index, where it would keep the order: set(1, a) and set(1, c) of
	 * [a, b, c] throw the IllegalArgumentException as well, and the list does not change; before
	 * and after the change for #78
	 */
	@Test
	public void testSetWithElementHeldAtAnotherIndexThatKeepsTheOrder()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"),
			Comparator.naturalOrder());

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> list.set(1, "a"));
		assertEquals("This list holds the element a at the index 0, it cannot hold it at the index"
			+ " 1 too", exception.getMessage());
		assertThrows(IllegalArgumentException.class, () -> list.set(1, "c"));
		assertEquals(Arrays.asList("a", "b", "c"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#set(int, Object)} with a comparator and elements the
	 * list does not hold that keep the order at the index, at the first, a middle and the last
	 * index: [b, d, f] gives [a, d, f] with set(0, a), [c, d, f] with set(0, c), [b, c, f] with
	 * set(1, c), [b, e, f] with set(1, e), [b, d, e] with set(2, e) and [b, d, z] with set(2, z),
	 * each set returns the element that was at the index, and a list of one element takes any
	 * element; before and after the change for #78
	 */
	@Test
	public void testSetWithComparatorAndElementThatKeepsTheOrder()
	{
		List<String> elements = Arrays.asList("b", "d", "f");

		assertSet(elements, 0, "a", Arrays.asList("a", "d", "f"));
		assertSet(elements, 0, "c", Arrays.asList("c", "d", "f"));
		assertSet(elements, 1, "c", Arrays.asList("b", "c", "f"));
		assertSet(elements, 1, "e", Arrays.asList("b", "e", "f"));
		assertSet(elements, 2, "e", Arrays.asList("b", "d", "e"));
		assertSet(elements, 2, "z", Arrays.asList("b", "d", "z"));
		assertSet(Arrays.asList("m"), 0, "z", Arrays.asList("z"));
	}

	/**
	 * Calls set with the given index and element on a {@link SortedUniqueList} of the given
	 * elements in their natural order, and asserts that it returns the element that was at the
	 * index and that the list holds the expected elements afterwards
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
		List<String> list = new SortedUniqueList<>(elements, Comparator.naturalOrder());
		assertEquals(elements.get(index), list.set(index, element));
		assertEquals(expected, list);
	}

	/**
	 * Test the method {@link SortedUniqueList#set(int, Object)} with a comparator inconsistent with
	 * equals and an element it holds equal to the neighbour in front of the index, which equals
	 * does not: with {@link String#CASE_INSENSITIVE_ORDER}, set(1, B) of [b, d, f] removes d and
	 * sorts B in, in front of b, and gives [B, b, f]. The change for #78 changes the result: B
	 * keeps the order at the index 1 and replaces d there, [b, B, f].
	 */
	@Test
	public void testSetWithComparatorInconsistentWithEquals()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("b", "d", "f"),
			String.CASE_INSENSITIVE_ORDER);

		assertEquals("d", list.set(1, "B"));
		assertEquals(Arrays.asList("B", "b", "f"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#set(int, Object)} with a comparator while an iterator
	 * runs over the list: set(1, d) of [a, c, e] after the first next() removes c and sorts d in, a
	 * structural modification, so the list is [a, d, e] and the next step of the iterator throws a
	 * ConcurrentModificationException. The change for #78 changes the result: d keeps the order at
	 * the index 1 and replaces c in place, which is no structural modification, and the iterator
	 * goes on with d and e.
	 */
	@Test
	public void testSetWithComparatorWhileAnIteratorRuns()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "c", "e"),
			Comparator.naturalOrder());
		Iterator<String> iterator = list.iterator();
		assertEquals("a", iterator.next());

		list.set(1, "d");
		assertEquals(Arrays.asList("a", "d", "e"), list);
		assertThrows(ConcurrentModificationException.class, iterator::next);
	}

	/**
	 * Test the method {@link SortedUniqueList#set(int, Object)} without a comparator and with an
	 * element the list holds at another index, in front of and behind the given one: the two
	 * elements swap, as in {@link UniqueList}; set(0, c) of [a, b, c] returns a and set(2, a)
	 * returns c, and both give [c, b, a] (#77)
	 */
	@Test
	public void testSetWithoutComparatorAndElementHeldAtAnotherIndex()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"));
		assertEquals("a", list.set(0, "c"));
		assertEquals(Arrays.asList("c", "b", "a"), list);

		List<String> other = new SortedUniqueList<>(Arrays.asList("a", "b", "c"));
		assertEquals("c", other.set(2, "a"));
		assertEquals(Arrays.asList("c", "b", "a"), other);
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
	 * Test the method {@link SortedUniqueList#set(int, Object)} with a comparator and an element
	 * the list does not hold: it removes the element at the index and sorts the given one in,
	 * behind and in front of the index; [a, c, e] with set(0, d) returns a and gives [c, d, e], and
	 * with set(2, b) returns e and gives [a, b, c] (#77). The change for #78 changes the result: d
	 * at the index 0 and b at the index 2 break the order, both throw an
	 * UnsupportedOperationException, and the list stays [a, c, e].
	 */
	@Test
	public void testSetWithComparator()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "c", "e"),
			Comparator.naturalOrder());
		assertEquals("a", list.set(0, "d"));
		assertEquals(Arrays.asList("c", "d", "e"), list);

		list = new SortedUniqueList<>(Arrays.asList("a", "c", "e"), Comparator.naturalOrder());
		assertEquals("e", list.set(2, "b"));
		assertEquals(Arrays.asList("a", "b", "c"), list);
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
	 * cannot compare, null for the natural order: the comparator throws a NullPointerException, the
	 * removed element is put back at its index, and the list stays [a, c] (#77)
	 */
	@Test
	public void testSetWithElementTheComparatorCannotCompare()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "c"),
			Comparator.naturalOrder());

		assertThrows(NullPointerException.class, () -> list.set(0, null));
		assertEquals(Arrays.asList("a", "c"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#replaceAll(UnaryOperator)} with operators that give
	 * two equal values: it throws an IllegalArgumentException and replaces no element, the list
	 * stays [a, b, c], also where the equal values come last (#77)
	 */
	@Test
	public void testReplaceAllWithEqualValues()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"),
			Comparator.naturalOrder());

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> list.replaceAll(element -> "x"));
		assertEquals("The operator gives the value x for two elements of this list",
			exception.getMessage());
		assertEquals(Arrays.asList("a", "b", "c"), list);

		assertThrows(IllegalArgumentException.class,
			() -> list.replaceAll(element -> element.equals("a") ? "A" : "B"));
		assertEquals(Arrays.asList("a", "b", "c"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#replaceAll(UnaryOperator)} with a comparator and
	 * distinct values: the values are sorted in, the operator that gives f for a gives [c, e, f]
	 * for [a, c, e]; a value may equal an element it does not replace, the one that gives b for a,
	 * c for b and a for c leaves [a, b, c] as it is (#77). The change for #78 changes the result:
	 * the values [f, c, e] and [b, c, a], each at the index of the element it replaces, break the
	 * order, replaceAll throws an UnsupportedOperationException, and no element is replaced.
	 */
	@Test
	public void testReplaceAllWithComparator()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "c", "e"),
			Comparator.naturalOrder());
		list.replaceAll(element -> "a".equals(element) ? "f" : element);
		assertEquals(Arrays.asList("c", "e", "f"), list);

		list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"), Comparator.naturalOrder());
		list.replaceAll(element -> element.equals("a") ? "b" : element.equals("b") ? "c" : "a");
		assertEquals(Arrays.asList("a", "b", "c"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#replaceAll(UnaryOperator)} with a value the
	 * comparator cannot compare, null for the natural order: the comparator throws a
	 * NullPointerException, no element is replaced, and the list stays [a, c] (#77)
	 */
	@Test
	public void testReplaceAllWithValueTheComparatorCannotCompare()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "c"),
			Comparator.naturalOrder());

		assertThrows(NullPointerException.class,
			() -> list.replaceAll(element -> "a".equals(element) ? null : element));
		assertEquals(Arrays.asList("a", "c"), list);
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
	 * Test the method {@link SortedUniqueList#replaceAll(UnaryOperator)} with a comparator and
	 * distinct values that keep the order, each at the index of the element it replaces: [a, c, e]
	 * gives [aa, cc, ee] with the operator that doubles each element, and [b, c, d] with the one
	 * that gives the next letter, values the list holds at another index; before and after the
	 * change for #78
	 */
	@Test
	public void testReplaceAllWithComparatorAndValuesThatKeepTheOrder()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "c", "e"),
			Comparator.naturalOrder());
		list.replaceAll(element -> element + element);
		assertEquals(Arrays.asList("aa", "cc", "ee"), list);

		list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"), Comparator.naturalOrder());
		list.replaceAll(element -> String.valueOf((char)(element.charAt(0) + 1)));
		assertEquals(Arrays.asList("b", "c", "d"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#replaceAll(UnaryOperator)} with a comparator and
	 * values that are equal and out of order, c, b and c for [a, b, c]: the equal values throw the
	 * IllegalArgumentException, and the list stays [a, b, c]; before and after the change for #78
	 */
	@Test
	public void testReplaceAllWithEqualValuesOutOfOrder()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"),
			Comparator.naturalOrder());

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> list.replaceAll(element -> "b".equals(element) ? "b" : "c"));
		assertEquals("The operator gives the value c for two elements of this list",
			exception.getMessage());
		assertEquals(Arrays.asList("a", "b", "c"), list);
	}

	/**
	 * Test the method {@link SortedUniqueList#replaceAll(UnaryOperator)} with an operator that
	 * throws for the second element, with and without a comparator: the exception goes to the
	 * caller, and no element is replaced, [a, b, c] stays as it is, as all values are computed
	 * before the first is stored; before and after the change for #78
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
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"),
			Comparator.naturalOrder());
		assertThrows(IllegalStateException.class, () -> list.replaceAll(operator));
		assertEquals(Arrays.asList("a", "b", "c"), list);

		List<String> withoutComparator = new SortedUniqueList<>(Arrays.asList("a", "b", "c"));
		assertThrows(IllegalStateException.class, () -> withoutComparator.replaceAll(operator));
		assertEquals(Arrays.asList("a", "b", "c"), withoutComparator);
	}

	/**
	 * Test the method {@link SortedUniqueList#sort(Comparator)}: it throws an
	 * UnsupportedOperationException for every argument, another comparator, null and the comparator
	 * of the list, also on a list without a comparator, and the list does not change (#77)
	 */
	@Test
	public void testSort()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"),
			Comparator.naturalOrder());

		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> list.sort(Comparator.reverseOrder()));
		assertEquals("A SortedUniqueList keeps the order of its comparator;"
			+ " setComparator(Comparator) changes it", exception.getMessage());
		assertThrows(UnsupportedOperationException.class, () -> list.sort(null));
		assertThrows(UnsupportedOperationException.class,
			() -> list.sort(Comparator.naturalOrder()));
		assertEquals(Arrays.asList("a", "b", "c"), list);

		List<String> withoutComparator = new SortedUniqueList<>(Arrays.asList("b", "a"));
		assertThrows(UnsupportedOperationException.class, () -> withoutComparator.sort(null));
		assertEquals(Arrays.asList("b", "a"), withoutComparator);
	}

	/**
	 * Test {@link Collections#sort(List)} on a {@link SortedUniqueList} with the reverse order: it
	 * calls list.sort(null), which throws an UnsupportedOperationException, and the list stays [c,
	 * b, a] (#77)
	 */
	@Test
	public void testCollectionsSort()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"),
			Comparator.reverseOrder());
		assertEquals(Arrays.asList("c", "b", "a"), list);

		assertThrows(UnsupportedOperationException.class, () -> Collections.sort(list));
		assertEquals(Arrays.asList("c", "b", "a"), list);
	}

	/**
	 * Test the method set of the list iterator, of listIterator() and of listIterator(int): it
	 * throws an UnsupportedOperationException, and the list stays [a, c] (#77). The change for #78
	 * changes the result: the iterator sets through set(int, E) of the list, c, which the list
	 * holds at the index 1, throws the IllegalArgumentException of that set, b at the index 1 keeps
	 * the order and gives [a, b].
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
		assertEquals("A SortedUniqueList puts an element at its place in the order of its"
			+ " comparator, not at the position of a list iterator", exception.getMessage());
		assertEquals(Arrays.asList("a", "c"), list);

		ListIterator<String> fromIndex = list.listIterator(2);
		assertEquals("c", fromIndex.previous());
		assertThrows(UnsupportedOperationException.class, () -> fromIndex.set("b"));
		assertEquals(Arrays.asList("a", "c"), list);
	}

	/**
	 * Test the method add of the list iterator, of listIterator() and of listIterator(int): it
	 * throws an UnsupportedOperationException, for an element the list holds too, the list stays
	 * [a, c], the cursor does not move, and next() returns c (#77)
	 */
	@Test
	public void testListIteratorAdd()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "c"),
			Comparator.naturalOrder());
		ListIterator<String> iterator = list.listIterator();
		assertEquals("a", iterator.next());
		assertThrows(UnsupportedOperationException.class, () -> iterator.add("a"));
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
	 * it throws an UnsupportedOperationException, and the list stays [a, b, c]. The change for #78
	 * changes the result: the iterator sets through set(int, E) of the list, which swaps an element
	 * the list holds at another index without a comparator, set(c) after the first next() gives [c,
	 * b, a], and the iterator goes on with b and a.
	 */
	@Test
	public void testListIteratorSetWithoutComparator()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"));
		ListIterator<String> iterator = list.listIterator();
		assertEquals("a", iterator.next());

		assertThrows(UnsupportedOperationException.class, () -> iterator.set("c"));
		assertEquals(Arrays.asList("a", "b", "c"), list);
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
	 * {@link SortedUniqueList} with the natural order: each of them calls set with an element the
	 * list holds at another index, which throws an IllegalArgumentException before the list
	 * changes, and the list stays [a, b, c, d] (#77)
	 */
	@Test
	public void testCollectionsAlgorithmsWithComparator()
	{
		List<String> elements = Arrays.asList("a", "b", "c", "d");
		List<String> list = new SortedUniqueList<>(elements, Comparator.naturalOrder());

		assertThrows(IllegalArgumentException.class, () -> Collections.swap(list, 0, 3));
		assertEquals(elements, list);
		assertThrows(IllegalArgumentException.class, () -> Collections.reverse(list));
		assertEquals(elements, list);
		assertThrows(IllegalArgumentException.class,
			() -> Collections.shuffle(list, new Random(77)));
		assertEquals(elements, list);
		assertThrows(IllegalArgumentException.class, () -> Collections.rotate(list, 1));
		assertEquals(elements, list);
	}

	/**
	 * Test {@link Collections#swap(List, int, int)}, {@link Collections#reverse(List)} of an even
	 * and of an odd size, {@link Collections#shuffle(List, Random)} and
	 * {@link Collections#rotate(List, int)} in one cycle (distance 1 and -1) and in several
	 * (distance 2 and 3 of six elements) on a {@link SortedUniqueList} without a comparator: set
	 * swaps an element the list holds at another index, so each of them works as on a
	 * {@link UniqueList}, and shuffle gives the order it gives a UniqueList with the same seed
	 * (#77)
	 */
	@Test
	public void testCollectionsAlgorithmsWithoutComparator()
	{
		List<String> elements = Arrays.asList("a", "b", "c", "d", "e", "f");
		List<String> list;

		list = new SortedUniqueList<>(elements);
		Collections.swap(list, 0, 5);
		assertEquals(Arrays.asList("f", "b", "c", "d", "e", "a"), list);
		Collections.swap(list, 1, 1);
		assertEquals(Arrays.asList("f", "b", "c", "d", "e", "a"), list);

		list = new SortedUniqueList<>(elements);
		Collections.reverse(list);
		assertEquals(Arrays.asList("f", "e", "d", "c", "b", "a"), list);
		list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"));
		Collections.reverse(list);
		assertEquals(Arrays.asList("c", "b", "a"), list);

		List<String> shuffled = new UniqueList<>(elements);
		Collections.shuffle(shuffled, new Random(77));
		assertNotEquals(elements, shuffled);
		list = new SortedUniqueList<>(elements);
		Collections.shuffle(list, new Random(77));
		assertEquals(shuffled, list);

		list = new SortedUniqueList<>(elements);
		Collections.rotate(list, 1);
		assertEquals(Arrays.asList("f", "a", "b", "c", "d", "e"), list);
		list = new SortedUniqueList<>(elements);
		Collections.rotate(list, -1);
		assertEquals(Arrays.asList("b", "c", "d", "e", "f", "a"), list);
		list = new SortedUniqueList<>(elements);
		Collections.rotate(list, 2);
		assertEquals(Arrays.asList("e", "f", "a", "b", "c", "d"), list);
		list = new SortedUniqueList<>(elements);
		Collections.rotate(list, 3);
		assertEquals(Arrays.asList("d", "e", "f", "a", "b", "c"), list);
	}

	/**
	 * Test set and replaceAll of the view of {@link SortedUniqueList#subList(int, int)} and of a
	 * view of it: each throws an UnsupportedOperationException, for an element the list holds and
	 * for a new one, and neither the list nor the view changes
	 */
	@Test
	public void testSubListSetAndReplaceAll()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c", "d"),
			Comparator.naturalOrder());
		List<String> view = list.subList(1, 3);

		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> view.set(0, "z"));
		assertEquals(
			"A subList view of a SortedUniqueList reads and removes only; set and add through"
				+ " the list itself",
			exception.getMessage());
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
	 * Test the add methods of the view of {@link SortedUniqueList#subList(int, int)} and of a view
	 * of it: add(E), add(int, E), addAll and addAll(int, Collection) throw an
	 * UnsupportedOperationException, with an empty collection too, and neither the list nor the
	 * view changes; the third case of #78, add(a) on subList(0, 2) of [a, c], too
	 */
	@Test
	public void testSubListAdd()
	{
		List<String> unique = new SortedUniqueList<>(Arrays.asList("a", "c"),
			Comparator.naturalOrder());
		List<String> whole = unique.subList(0, 2);
		assertThrows(UnsupportedOperationException.class, () -> whole.add("a"));
		assertEquals(Arrays.asList("a", "c"), unique);
		assertEquals(2, whole.size());
		assertEquals(Arrays.asList("a", "c"), whole);

		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c", "d"),
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
	 * Test sort of the view of {@link SortedUniqueList#subList(int, int)} and of a view of it, and
	 * {@link Collections#reverse(List)} and {@link Collections#swap(List, int, int)}, which work
	 * through its set: each throws an UnsupportedOperationException, sort with null, with the
	 * comparator of the list and on an empty view too, and the list does not change
	 */
	@Test
	public void testSubListSort()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c", "d"),
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
	 * Test the methods {@link SortedUniqueList#addFirst(Object)} and
	 * {@link SortedUniqueList#addLast(Object)}: they throw an UnsupportedOperationException, as
	 * those of SortedSet and TreeSet do, the comparator determines the position; with and without a
	 * comparator, for a new element and for one the list holds, and the list does not change (#87)
	 */
	@Test
	public void testAddFirstAndAddLast()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b"),
			Comparator.naturalOrder());
		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> list.addFirst("z"));
		assertEquals(
			"The comparator of a SortedUniqueList determines the position of its elements, so"
				+ " explicit positioning is not supported",
			exception.getMessage());
		assertThrows(UnsupportedOperationException.class, () -> list.addLast("0"));
		assertThrows(UnsupportedOperationException.class, () -> list.addFirst("b"));
		assertEquals(Arrays.asList("a", "b"), list);

		List<String> withoutComparator = new SortedUniqueList<>(Arrays.asList("b", "a"));
		assertThrows(UnsupportedOperationException.class, () -> withoutComparator.addFirst("z"));
		assertThrows(UnsupportedOperationException.class, () -> withoutComparator.addLast("z"));
		assertEquals(Arrays.asList("b", "a"), withoutComparator);
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
	 * Test the changes through {@link SortedUniqueList#reversed()}: the view reads and removes, as
	 * the view of subList, and add, add(int, E), addFirst, addLast, addAll, set, replaceAll and
	 * sort through it, and the set and add of its list iterator, throw an
	 * UnsupportedOperationException, and the list does not change (#87)
	 */
	@Test
	public void testReversed()
	{
		List<String> list = new SortedUniqueList<>(Arrays.asList("a", "b", "c"),
			Comparator.naturalOrder());
		List<String> reversed = list.reversed();

		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> reversed.addFirst("0"));
		assertEquals(
			"The reversed view of a SortedUniqueList reads and removes only; add through the"
				+ " list itself",
			exception.getMessage());
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
