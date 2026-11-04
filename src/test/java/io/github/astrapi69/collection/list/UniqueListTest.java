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
}
