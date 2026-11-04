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

import org.junit.jupiter.api.Test;

/**
 * The unit test class for the class {@link UniqueList}
 */
public class UniqueListTest
{

	/**
	 * Test the method {@link UniqueList#add(Object)}
	 */
	@Test
	public void testAdd()
	{
		List<String> list;

		list = new UniqueList<>(6);
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
	 * Test the method {@link UniqueList#add(Object)}
	 */
	@Test
	public void testAddWithIndex()
	{
		List<String> list;

		list = new UniqueList<>();
		list.add(0, "Leonidas");
		list.add(0, "Leonidas");
		assertEquals(list.size(), 1);
		list.add(0, "Berta");
		list.add(0, "Berta");
		assertEquals(list.size(), 2);
		list.add(0, "Caesar");
		list.add(0, "Caesar");
		assertEquals(list.size(), 3);
		list.add(0, "Dora");
		list.add(0, "Dora");
		assertEquals(list.size(), 4);
		list.add(0, "Emil");
		list.add(0, "Emil");
		assertEquals(list.size(), 5);
		list.add(0, "Anton");
		list.add(0, "Anton");
		assertEquals(list.size(), 6);
	}

	/**
	 * Test the method {@link UniqueList#add(Object)}
	 */
	@Test
	public void testAddAll()
	{
		List<String> list;
		List<String> addition;

		addition = ListFactory.newArrayList("", "Emil", "Anton", "Anton", "Anton", "Emil", "");
		list = new UniqueList<>(ListFactory.newArrayList());
		list.add("Emil");
		list.add("Caesar");
		list.addAll(addition);
		assertEquals(list.size(), 4);

		addition = ListFactory.newArrayList();
		list = new UniqueList<>();
		list.add("Emil");
		list.add("Caesar");
		list.addAll(addition);
		assertEquals(list.size(), 2);
	}

	/**
	 * Test the method {@link UniqueList#add(Object)}
	 */
	@Test
	public void testAddAllWithIndex()
	{
		List<String> list;
		List<String> addition;

		addition = ListFactory.newArrayList("", "Emil", "Anton", "Anton", "Anton", "Emil", "");
		list = new UniqueList<>();
		list.add("Emil");
		list.add("Caesar");
		list.addAll(1, addition);
		assertEquals(list.size(), 4);

		addition = ListFactory.newArrayList();
		list = new UniqueList<>();
		list.add("Emil");
		list.add("Caesar");
		list.addAll(1, addition);
		assertEquals(list.size(), 2);
	}

	/**
	 * Test the method {@link UniqueList#add(Object)} with a new element, which it appends and
	 * returns true for, and with an element the list already contains, which it leaves out and
	 * returns false for
	 */
	@Test
	public void testAddNewAndContainedElement()
	{
		List<String> list = new UniqueList<>();

		assertTrue(list.add("Leonidas"));
		assertTrue(list.add("Berta"));
		assertFalse(list.add("Leonidas"));
		assertEquals(Arrays.asList("Leonidas", "Berta"), list);
	}

	/**
	 * Test the method {@link UniqueList#addAll(Collection)} with a collection of new elements and
	 * one the list already contains: the new ones are appended in the order of the collection and
	 * the method returns true
	 */
	@Test
	public void testAddAllWithNewElements()
	{
		List<String> list = new UniqueList<>();
		list.add("Emil");

		assertTrue(list.addAll(Arrays.asList("Anton", "Emil", "Berta")));
		assertEquals(Arrays.asList("Emil", "Anton", "Berta"), list);
	}

	/**
	 * Test the method {@link UniqueList#addAll(Collection)} with an empty collection, which leaves
	 * the list unchanged and returns false
	 */
	@Test
	public void testAddAllWithEmptyCollection()
	{
		List<String> list = new UniqueList<>();
		list.add("Emil");

		assertFalse(list.addAll(Collections.emptyList()));
		assertEquals(Arrays.asList("Emil"), list);
	}

	/**
	 * Test the method {@link UniqueList#addAll(int, Collection)} with a collection of new elements:
	 * they are inserted at the given index in the order of the collection, the element that was at
	 * the index follows them, and the method returns true
	 */
	@Test
	public void testAddAllWithIndexAndNewElements()
	{
		List<String> list = new UniqueList<>();
		list.add("Emil");
		list.add("Caesar");

		assertTrue(list.addAll(1, Arrays.asList("Anton", "Berta")));
		assertEquals(Arrays.asList("Emil", "Anton", "Berta", "Caesar"), list);
	}

	/**
	 * Test the method {@link UniqueList#addAll(int, Collection)} with an empty collection, which
	 * leaves the list unchanged and returns false
	 */
	@Test
	public void testAddAllWithIndexAndEmptyCollection()
	{
		List<String> list = new UniqueList<>();
		list.add("Emil");

		assertFalse(list.addAll(0, Collections.emptyList()));
		assertEquals(Arrays.asList("Emil"), list);
	}

	/**
	 * Test the method {@link UniqueList#addAll(Collection)} with an element the list contains
	 * already: [a].addAll([a]) leaves the list as it is and returns false, as the list did not
	 * change (#39)
	 */
	@Test
	public void testAddAllWithContainedElement()
	{
		List<String> list = new UniqueList<>();
		list.add("a");

		assertFalse(list.addAll(Arrays.asList("a")));
		assertEquals(Arrays.asList("a"), list);
	}

	/**
	 * Test the method {@link UniqueList#addAll(int, Collection)} with an element the list contains
	 * already: [a].addAll(1, [a]) leaves the list as it is and returns false, as the list did not
	 * change (#39)
	 */
	@Test
	public void testAddAllWithIndexAndContainedElement()
	{
		List<String> list = new UniqueList<>();
		list.add("a");

		assertFalse(list.addAll(1, Arrays.asList("a")));
		assertEquals(Arrays.asList("a"), list);
	}

	/**
	 * Test the method {@link UniqueList#addAll(int, Collection)} with an index greater than the
	 * size: [a].addAll(5, [b]) throws an IndexOutOfBoundsException with the message of ArrayList,
	 * and the list stays [a] (#39)
	 */
	@Test
	public void testAddAllWithIndexOverSize()
	{
		List<String> list = new UniqueList<>();
		list.add("a");

		IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
			() -> list.addAll(5, Arrays.asList("b")));
		assertEquals("Index: 5, Size: 1", exception.getMessage());
		assertEquals(Arrays.asList("a"), list);
	}

	/**
	 * Test the method {@link UniqueList#addAll(int, Collection)} with an index greater than the
	 * size and an empty collection: [a].addAll(5, []) throws an IndexOutOfBoundsException, as
	 * ArrayList does (#39)
	 */
	@Test
	public void testAddAllWithIndexOverSizeAndEmptyCollection()
	{
		List<String> list = new UniqueList<>();
		list.add("a");

		IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
			() -> list.addAll(5, Collections.emptyList()));
		assertEquals("Index: 5, Size: 1", exception.getMessage());
		assertEquals(Arrays.asList("a"), list);
	}

	/**
	 * Test the method {@link UniqueList#add(int, Object)} with the index size() and an element the
	 * list contains already: [a, b].add(2, a) moves a to the end and gives [b, a] (#39)
	 */
	@Test
	public void testAddWithIndexOfSizeAndContainedElement()
	{
		List<String> list = new UniqueList<>();
		list.add("a");
		list.add("b");

		list.add(2, "a");
		assertEquals(Arrays.asList("b", "a"), list);
	}

	/**
	 * Test the method {@link UniqueList#addAll(int, Collection)} with an element the collection
	 * holds twice: [x].addAll(0, [a, a, b]) gives [a, b, x], as [x].addAll(0, [a, b]) gives (#39)
	 */
	@Test
	public void testAddAllWithIndexAndRepeatedElement()
	{
		List<String> list = new UniqueList<>();
		list.add("x");

		assertTrue(list.addAll(0, Arrays.asList("a", "a", "b")));
		assertEquals(Arrays.asList("a", "b", "x"), list);
	}

	/**
	 * Test the method {@link UniqueList#add(int, Object)} with elements the list contains already:
	 * each moves so that it ends at the given index, to the front, to the back or to where it is,
	 * and the size stays the same (#39)
	 */
	@Test
	public void testAddWithIndexAndContainedElement()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));

		list.add(0, "c");
		assertEquals(Arrays.asList("c", "a", "b"), list);
		list.add(2, "c");
		assertEquals(Arrays.asList("a", "b", "c"), list);
		list.add(1, "a");
		assertEquals(Arrays.asList("b", "a", "c"), list);
		list.add(1, "a");
		assertEquals(Arrays.asList("b", "a", "c"), list);
	}

	/**
	 * Test the method {@link UniqueList#add(int, Object)} with new elements: each is inserted at
	 * the given index, the index size() included, as ArrayList inserts it (#39)
	 */
	@Test
	public void testAddWithIndexAndNewElement()
	{
		List<String> list = new UniqueList<>();
		list.add("a");

		list.add(1, "b");
		assertEquals(Arrays.asList("a", "b"), list);
		list.add(0, "c");
		assertEquals(Arrays.asList("c", "a", "b"), list);
		list.add(1, "d");
		assertEquals(Arrays.asList("c", "d", "a", "b"), list);
	}

	/**
	 * Test the method {@link UniqueList#add(int, Object)} with an index out of range and an element
	 * the list contains already: it throws an IndexOutOfBoundsException with the message of
	 * ArrayList, before it moves the element, and the list does not change (#39)
	 */
	@Test
	public void testAddWithIndexOutOfRangeAndContainedElement()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b"));

		IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
			() -> list.add(3, "a"));
		assertEquals("Index: 3, Size: 2", exception.getMessage());
		assertEquals(Arrays.asList("a", "b"), list);

		exception = assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, "a"));
		assertEquals("Index: -1, Size: 2", exception.getMessage());
		assertEquals(Arrays.asList("a", "b"), list);
	}

	/**
	 * Test the method {@link UniqueList#add(int, Object)} with an index out of range and a new
	 * element: it throws an IndexOutOfBoundsException with the message of ArrayList, and the list
	 * does not change (#39)
	 */
	@Test
	public void testAddWithIndexOutOfRangeAndNewElement()
	{
		List<String> list = new UniqueList<>();
		list.add("a");

		IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
			() -> list.add(2, "b"));
		assertEquals("Index: 2, Size: 1", exception.getMessage());

		exception = assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, "b"));
		assertEquals("Index: -1, Size: 1", exception.getMessage());
		assertEquals(Arrays.asList("a"), list);
	}

	/**
	 * Test the method {@link UniqueList#addAll(int, Collection)} with a negative index: it throws
	 * an IndexOutOfBoundsException with the message of ArrayList, for an empty collection too, and
	 * the list does not change (#39)
	 */
	@Test
	public void testAddAllWithNegativeIndex()
	{
		List<String> list = new UniqueList<>();
		list.add("a");

		IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
			() -> list.addAll(-1, Arrays.asList("b")));
		assertEquals("Index: -1, Size: 1", exception.getMessage());

		exception = assertThrows(IndexOutOfBoundsException.class,
			() -> list.addAll(-1, Collections.emptyList()));
		assertEquals("Index: -1, Size: 1", exception.getMessage());
		assertEquals(Arrays.asList("a"), list);
	}

	/**
	 * Test the method {@link UniqueList#addAll(int, Collection)} with the index size(): the new
	 * elements are appended in the order of the collection, the one the list contains already is
	 * left out, and the method returns true (#39)
	 */
	@Test
	public void testAddAllWithIndexOfSize()
	{
		List<String> list = new UniqueList<>();
		list.add("a");

		assertTrue(list.addAll(1, Arrays.asList("b", "a", "c")));
		assertEquals(Arrays.asList("a", "b", "c"), list);
	}

	/**
	 * Test the method {@link UniqueList#addAll(int, Collection)} with a new element and one the
	 * list contains already: the new one is inserted at the index, and the contained one keeps its
	 * position, unlike with {@link UniqueList#add(int, Object)}, which moves it (#39)
	 */
	@Test
	public void testAddAllWithIndexAndNewAndContainedElement()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b"));

		assertTrue(list.addAll(0, Arrays.asList("b", "c")));
		assertEquals(Arrays.asList("c", "a", "b"), list);
	}

	/**
	 * Test the method {@link UniqueList#set(int, Object)} with an element the list holds at another
	 * index, before and behind the given one: the two elements swap, [a, b, c].set(0, c) returns a
	 * and gives [c, b, a], and [a, b, c].set(2, a) returns c and gives [c, b, a] too (#67)
	 */
	@Test
	public void testSetWithElementHeldAtAnotherIndex()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		assertEquals("a", list.set(0, "c"));
		assertEquals(Arrays.asList("c", "b", "a"), list);

		list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		assertEquals("c", list.set(2, "a"));
		assertEquals(Arrays.asList("c", "b", "a"), list);
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
	 * Test the method {@link UniqueList#set(int, Object)} with an index out of range: it throws an
	 * IndexOutOfBoundsException with the message of ArrayList, for an element the list holds too,
	 * and the list does not change (#67)
	 */
	@Test
	public void testSetWithIndexOutOfRange()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b"));

		IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
			() -> list.set(2, "a"));
		assertEquals("Index 2 out of bounds for length 2", exception.getMessage());
		exception = assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, "b"));
		assertEquals("Index -1 out of bounds for length 2", exception.getMessage());
		assertEquals(Arrays.asList("a", "b"), list);
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
	 * Test that {@link Collections#swap(List, int, int)}, which calls
	 * {@code l.set(i, l.set(j, l.get(i)))}, swaps two elements of the list, before and after the
	 * fix of #67, which makes {@link UniqueList#set(int, Object)} swap an element the list holds
	 */
	@Test
	public void testCollectionsSwap()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c", "d"));

		Collections.swap(list, 0, 3);
		assertEquals(Arrays.asList("d", "b", "c", "a"), list);
		Collections.swap(list, 2, 1);
		assertEquals(Arrays.asList("d", "c", "b", "a"), list);
		Collections.swap(list, 1, 1);
		assertEquals(Arrays.asList("d", "c", "b", "a"), list);
	}

	/**
	 * Test that {@link Collections#reverse(List)}, which swaps through set, reverses the list of an
	 * odd and of an even size, before and after the fix of #67
	 */
	@Test
	public void testCollectionsReverse()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c", "d", "e"));
		Collections.reverse(list);
		assertEquals(Arrays.asList("e", "d", "c", "b", "a"), list);

		list = new UniqueList<>(Arrays.asList("a", "b", "c", "d"));
		Collections.reverse(list);
		assertEquals(Arrays.asList("d", "c", "b", "a"), list);
	}

	/**
	 * Test that {@link Collections#shuffle(List, Random)}, which swaps through set, gives the list
	 * the order it gives an ArrayList with the same elements and the same seed, each element once,
	 * before and after the fix of #67
	 */
	@Test
	public void testCollectionsShuffle()
	{
		List<String> elements = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
		List<String> expected = new ArrayList<>(elements);
		Collections.shuffle(expected, new Random(67));
		assertNotEquals(elements, expected);

		List<String> list = new UniqueList<>(elements);
		Collections.shuffle(list, new Random(67));
		assertEquals(expected, list);
	}

	/**
	 * Test that {@link Collections#rotate(List, int)}, which calls
	 * {@code displaced = list.set(i, displaced)} along each cycle, rotates the list, in one cycle
	 * (distance 1 and -1) and in several (distance 2 and 3 of six elements), before and after the
	 * fix of #67
	 */
	@Test
	public void testCollectionsRotate()
	{
		List<String> elements = Arrays.asList("a", "b", "c", "d", "e", "f");
		List<String> list;

		list = new UniqueList<>(elements);
		Collections.rotate(list, 1);
		assertEquals(Arrays.asList("f", "a", "b", "c", "d", "e"), list);

		list = new UniqueList<>(elements);
		Collections.rotate(list, -1);
		assertEquals(Arrays.asList("b", "c", "d", "e", "f", "a"), list);

		list = new UniqueList<>(elements);
		Collections.rotate(list, 2);
		assertEquals(Arrays.asList("e", "f", "a", "b", "c", "d"), list);

		list = new UniqueList<>(elements);
		Collections.rotate(list, 3);
		assertEquals(Arrays.asList("d", "e", "f", "a", "b", "c"), list);
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
	 * Test the method {@link UniqueList#replaceAll(java.util.function.UnaryOperator)} with
	 * operators that give two equal values: it throws an IllegalArgumentException and replaces no
	 * element, the list stays [a, b, c], also where the equal values come last (#67)
	 */
	@Test
	public void testReplaceAllWithEqualValues()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));

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
	 * Test the method {@link UniqueList#replaceAll(java.util.function.UnaryOperator)} with
	 * operators that give distinct values: each element is replaced by its value, also where the
	 * value equals another element of the list, as a to b, b to c and c to a
	 */
	@Test
	public void testReplaceAllWithDistinctValues()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));

		list.replaceAll(String::toUpperCase);
		assertEquals(Arrays.asList("A", "B", "C"), list);

		list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		list.replaceAll(element -> element.equals("a") ? "b" : element.equals("b") ? "c" : "a");
		assertEquals(Arrays.asList("b", "c", "a"), list);
	}

	/**
	 * Test the method {@link UniqueList#replaceAll(java.util.function.UnaryOperator)} with a null
	 * operator: it throws a NullPointerException, on an empty list too, as List.replaceAll says
	 */
	@Test
	public void testReplaceAllWithNullOperator()
	{
		List<String> list = new UniqueList<>();

		assertThrows(NullPointerException.class, () -> list.replaceAll(null));
	}

	/**
	 * Test the method {@link UniqueList#replaceAll(java.util.function.UnaryOperator)} with an
	 * operator that removes an element of the list: it throws a ConcurrentModificationException, as
	 * ArrayList does, also where the removal ends the iteration over the list
	 */
	@Test
	public void testReplaceAllWithOperatorThatChangesTheList()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b"));

		assertThrows(ConcurrentModificationException.class, () -> list.replaceAll(element -> {
			list.remove("b");
			return element.toUpperCase();
		}));
	}

	/**
	 * Test set and replaceAll of the view of {@link UniqueList#subList(int, int)} and of a view of
	 * it: each throws an UnsupportedOperationException, for an element the list holds and for a new
	 * one, and neither the list nor the view changes; the first case of #78, [a, b, c,
	 * d].subList(1, 3).set(0, a) and [a, b, c].subList(0, 2).replaceAll(e -> c), too
	 */
	@Test
	public void testSubListSetAndReplaceAll()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c", "d"));
		List<String> view = list.subList(1, 3);

		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> view.set(0, "a"));
		assertEquals("A subList view of a UniqueList reads and removes only; set and add through"
			+ " the list itself", exception.getMessage());
		assertThrows(UnsupportedOperationException.class, () -> view.set(0, "x"));
		assertThrows(UnsupportedOperationException.class, () -> view.replaceAll(element -> "x"));
		assertThrows(UnsupportedOperationException.class, () -> view.subList(0, 1).set(0, "a"));
		assertThrows(UnsupportedOperationException.class,
			() -> view.subList(0, 1).replaceAll(element -> "x"));
		assertEquals(Arrays.asList("a", "b", "c", "d"), list);
		assertEquals(Arrays.asList("b", "c"), view);

		List<String> other = new UniqueList<>(Arrays.asList("a", "b", "c"));
		assertThrows(UnsupportedOperationException.class,
			() -> other.subList(0, 2).replaceAll(element -> "c"));
		assertEquals(Arrays.asList("a", "b", "c"), other);
	}

	/**
	 * Test the add methods of the view of {@link UniqueList#subList(int, int)} and of a view of it:
	 * add(E), add(int, E), addAll and addAll(int, Collection) throw an
	 * UnsupportedOperationException, with an empty collection too, and neither the list nor the
	 * view changes; the second case of #78, add(0, c) on subList(1, 3) of [a, b, c, d], too
	 */
	@Test
	public void testSubListAdd()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c", "d"));
		List<String> view = list.subList(1, 3);

		assertThrows(UnsupportedOperationException.class, () -> view.add(0, "c"));
		assertThrows(UnsupportedOperationException.class, () -> view.add("e"));
		assertThrows(UnsupportedOperationException.class, () -> view.add(1, "e"));
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
	 * Test sort of the view of {@link UniqueList#subList(int, int)} and of a view of it, and
	 * {@link Collections#reverse(List)} and {@link Collections#swap(List, int, int)}, which work
	 * through its set: each throws an UnsupportedOperationException, sort with null and on an empty
	 * view too, and the list does not change
	 */
	@Test
	public void testSubListSort()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c", "d"));
		List<String> view = list.subList(1, 3);

		assertThrows(UnsupportedOperationException.class,
			() -> view.sort(Comparator.reverseOrder()));
		assertThrows(UnsupportedOperationException.class, () -> view.sort(null));
		assertThrows(UnsupportedOperationException.class, () -> list.subList(2, 2).sort(null));
		assertThrows(UnsupportedOperationException.class, () -> view.subList(0, 2).sort(null));
		assertThrows(UnsupportedOperationException.class, () -> Collections.reverse(view));
		assertThrows(UnsupportedOperationException.class, () -> Collections.swap(view, 0, 1));
		assertEquals(Arrays.asList("a", "b", "c", "d"), list);
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
	 * Test the method {@link UniqueList#addLast(Object)} with an element the list holds: it moves
	 * the element to the end, as LinkedHashSet.addLast does, [a, b] with addLast(a) gives [b, a],
	 * and getLast() is a; for the element that is last already the list does not change (#87)
	 */
	@Test
	public void testAddLastWithHeldElement()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b"));

		list.addLast("a");
		assertEquals(Arrays.asList("b", "a"), list);
		assertEquals("a", list.getLast());
		list.addLast("a");
		assertEquals(Arrays.asList("b", "a"), list);
	}

	/**
	 * Test the methods addFirst and addLast of {@link UniqueList} with a new element and addFirst
	 * with an element the list holds: each puts the element first or last, and addFirst moves a
	 * held element to the front, as LinkedHashSet does; [a, b] with addFirst(z), addLast(y) and
	 * addFirst(b) gives [b, z, a, y]; before and after the fix of #87
	 */
	@Test
	public void testAddFirstAndAddLast()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b"));

		list.addFirst("z");
		assertEquals(Arrays.asList("z", "a", "b"), list);
		list.addLast("y");
		assertEquals(Arrays.asList("z", "a", "b", "y"), list);
		assertEquals("y", list.getLast());
		list.addFirst("b");
		assertEquals(Arrays.asList("b", "z", "a", "y"), list);
		assertEquals("b", list.getFirst());
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
	 * Test add(int, E) of {@link UniqueList#reversed()} with an element the list holds: the view of
	 * List.reversed calls add(size() - index, element) of the list, which is right for an element
	 * that makes the list grow and one place off for one that moves, so that the element ends at
	 * the index - 1 of the view, and at 0 for the index 0; [a, b, c].reversed().add(1, a) gives the
	 * view [a, c, b], not [c, a, b]. The same over every index of lists of one to four elements,
	 * with the held element at every position; the list holds each element once. The fix of #77
	 * changes the result: the element ends at the index, and the index size() throws an
	 * UnsupportedOperationException.
	 */
	@Test
	public void testReversedAddWithIndexAndHeldElement()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		List<String> reversed = list.reversed();
		reversed.add(1, "a");
		assertEquals(Arrays.asList("a", "c", "b"), reversed);
		assertEquals(Arrays.asList("b", "c", "a"), list);

		for (int size = 1; size <= 4; size++)
		{
			for (int position = 0; position < size; position++)
			{
				for (int index = 0; index <= size; index++)
				{
					List<String> view = newListOf(size).reversed();
					String element = view.get(position);
					List<String> expected = new ArrayList<>(view);
					expected.remove(element);
					expected.add(Math.max(index - 1, 0), element);

					view.add(index, element);
					assertEquals(expected, view,
						"size " + size + ", position " + position + ", index " + index);
				}
			}
		}
	}

	/**
	 * Test add(size(), E) of {@link UniqueList#reversed()} with an element the list holds: the view
	 * of [a, b, c] with add(3, c) moves c to the end of the view, [b, a, c], where get(3) does not
	 * exist, as the view does not grow by a move. The fix of #77 changes the result: it throws an
	 * UnsupportedOperationException and the view stays [c, b, a]
	 */
	@Test
	public void testReversedAddAtTheEndWithHeldElement()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		List<String> reversed = list.reversed();

		reversed.add(3, "c");
		assertEquals(Arrays.asList("b", "a", "c"), reversed);
		assertEquals(Arrays.asList("c", "a", "b"), list);
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
	 * Test add(E) of {@link UniqueList#reversed()} with an element the list holds: the view of
	 * List.reversed calls add(0, element) of the list, which moves a held element to the front of
	 * the list, the end of the view, and returns true; the view of [a, b, c] with add(c) gives [b,
	 * a, c]. The fix of #77 changes the result: as {@link UniqueList#add(Object)}, it returns false
	 * and the view stays [c, b, a]
	 */
	@Test
	public void testReversedAddWithHeldElement()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		List<String> reversed = list.reversed();

		assertTrue(reversed.add("c"));
		assertEquals(Arrays.asList("b", "a", "c"), reversed);
		assertEquals(Arrays.asList("c", "a", "b"), list);
	}

	/**
	 * Test addAll and addAll(int, Collection) of {@link UniqueList#reversed()} with elements the
	 * list holds and with a repeated new element: the view of List.reversed returns true for any
	 * collection that is not empty, also when the list does not change, and adds the reversed
	 * collection to the list, so that a repeated element counts by its last occurrence; the view of
	 * [a, b, c] with addAll([x, y, x]) gives [c, b, a, y, x], with addAll(1, [p, q, p]) [c, q, p,
	 * b, a]. The fix of #77 changes the result: as {@link UniqueList#addAll(Collection)}, they
	 * return false when the list does not change, and add the new elements in the order of their
	 * first occurrence, [c, b, a, x, y] and [c, p, q, b, a]
	 */
	@Test
	public void testReversedAddAllWithHeldAndRepeatedElements()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		List<String> reversed = list.reversed();

		assertTrue(reversed.addAll(Arrays.asList("a", "c")));
		assertTrue(reversed.addAll(1, Arrays.asList("b")));
		assertEquals(Arrays.asList("c", "b", "a"), reversed);

		assertTrue(reversed.addAll(Arrays.asList("x", "y", "x")));
		assertEquals(Arrays.asList("c", "b", "a", "y", "x"), reversed);

		List<String> other = new UniqueList<>(Arrays.asList("a", "b", "c"));
		List<String> otherReversed = other.reversed();
		assertTrue(otherReversed.addAll(1, Arrays.asList("p", "q", "p")));
		assertEquals(Arrays.asList("c", "q", "p", "b", "a"), otherReversed);
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
	 * before the cursor or at it: the view of [a, b, c] with next() twice and add(c) gives [b, c,
	 * a], and nextIndex() stays 2; with next() and add(b), b being the element next() would return,
	 * the view stays [c, b, a], but nextIndex() becomes 2 and next() returns a, so that b is
	 * skipped. The fix of #77 changes the result: both throw an UnsupportedOperationException, as
	 * the contract of ListIterator.add, a following next() unaffected and nextIndex() one higher,
	 * cannot hold for them; the view stays [c, b, a], and next() returns a and b
	 */
	@Test
	public void testReversedListIteratorAddWithElementHeldAtOrBeforeTheCursor()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c"));
		ListIterator<String> iterator = list.reversed().listIterator();
		assertEquals("c", iterator.next());
		assertEquals("b", iterator.next());

		iterator.add("c");
		assertEquals(Arrays.asList("b", "c", "a"), list.reversed());
		assertEquals(2, iterator.nextIndex());
		assertEquals("a", iterator.next());

		List<String> other = new UniqueList<>(Arrays.asList("a", "b", "c"));
		ListIterator<String> otherIterator = other.reversed().listIterator();
		assertEquals("c", otherIterator.next());

		otherIterator.add("b");
		assertEquals(Arrays.asList("c", "b", "a"), other.reversed());
		assertEquals(2, otherIterator.nextIndex());
		assertEquals("a", otherIterator.next());
	}

	/**
	 * Test set and remove of the list iterator of {@link UniqueList#reversed()} after its add: the
	 * view of List.reversed calls previous() of the list's iterator after its add, so that set and
	 * remove act on the added element, where the contract of ListIterator says that they throw an
	 * IllegalStateException; the view of [a, b] with next(), add(z), set(y) gives [b, y, a], and
	 * remove() then [b, a]. The fix of #77 changes the result: set and remove throw an
	 * IllegalStateException and the view stays [b, z, a]
	 */
	@Test
	public void testReversedListIteratorSetAndRemoveAfterAdd()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b"));
		ListIterator<String> iterator = list.reversed().listIterator();
		assertEquals("b", iterator.next());
		iterator.add("z");

		iterator.set("y");
		assertEquals(Arrays.asList("b", "y", "a"), list.reversed());
		iterator.remove();
		assertEquals(Arrays.asList("b", "a"), list.reversed());
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
	 * an empty collection: the view of List.reversed refuses through the view of
	 * {@link UniqueList#subList(int, int)}, with its message, and returns false for an empty
	 * collection without asking it. The fix of #77 changes the result: the message names the
	 * reversed view, and addAll with an empty collection throws an UnsupportedOperationException
	 * too, as on the view of subList
	 */
	@Test
	public void testReversedSubListRefusal()
	{
		List<String> list = new UniqueList<>(Arrays.asList("a", "b", "c", "d"));
		List<String> view = list.reversed().subList(1, 3);

		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> view.set(0, "a"));
		assertEquals("A subList view of a UniqueList reads and removes only; set and add through"
			+ " the list itself", exception.getMessage());
		assertFalse(view.addAll(Collections.emptyList()));
		assertFalse(view.addAll(0, Collections.emptyList()));
		assertEquals(Arrays.asList("a", "b", "c", "d"), list);
	}
}
