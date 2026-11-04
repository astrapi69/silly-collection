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
	 * Test the method {@link SortedList#set(int, Object)} with a comparator: it removes the element
	 * at the index and sorts the given one in, behind and in front of the index; [a, c, e] with
	 * set(0, d) returns a and gives [c, d, e], and with set(2, b) returns e and gives [a, b, c]
	 * (#77)
	 */
	@Test
	public void testSetWithComparator()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "c", "e"),
			Comparator.naturalOrder());
		assertEquals("a", list.set(0, "d"));
		assertEquals(Arrays.asList("c", "d", "e"), list);

		list = new SortedList<>(Arrays.asList("a", "c", "e"), Comparator.naturalOrder());
		assertEquals("e", list.set(2, "b"));
		assertEquals(Arrays.asList("a", "b", "c"), list);
	}

	/**
	 * Test the method {@link SortedList#set(int, Object)} with an element the comparator cannot
	 * compare, null for the natural order: the comparator throws a NullPointerException, the
	 * removed element is put back at its index, and the list stays [a, c] (#77)
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
	 * Test the method {@link SortedList#replaceAll(UnaryOperator)} with a comparator: the values
	 * are sorted in, the operator that gives f for a gives [c, e, f] for [a, c, e]; values the
	 * comparator holds equal keep the order of the elements they replace, with the comparator of
	 * the length the operator that gives zzz for a gives [bb, zzz, ccc] for [a, bb, ccc] (#77)
	 */
	@Test
	public void testReplaceAllWithComparator()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "c", "e"),
			Comparator.naturalOrder());
		list.replaceAll(element -> "a".equals(element) ? "f" : element);
		assertEquals(Arrays.asList("c", "e", "f"), list);

		list = new SortedList<>(Arrays.asList("a", "bb", "ccc"),
			Comparator.comparingInt(String::length));
		list.replaceAll(element -> "a".equals(element) ? "zzz" : element);
		assertEquals(Arrays.asList("bb", "zzz", "ccc"), list);
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
	 * Test the method set of the list iterator, of listIterator() and of listIterator(int): it
	 * throws an UnsupportedOperationException, and the list stays [a, c] (#77)
	 */
	@Test
	public void testListIteratorSet()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "c"), Comparator.naturalOrder());
		ListIterator<String> iterator = list.listIterator();
		assertEquals("a", iterator.next());
		UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
			() -> iterator.set("z"));
		assertEquals("A SortedList puts an element at its place in the order of its comparator,"
			+ " not at the position of a list iterator", exception.getMessage());
		assertEquals(Arrays.asList("a", "c"), list);

		ListIterator<String> fromIndex = list.listIterator(2);
		assertEquals("c", fromIndex.previous());
		assertThrows(UnsupportedOperationException.class, () -> fromIndex.set("b"));
		assertEquals(Arrays.asList("a", "c"), list);
	}

	/**
	 * Test the method add of the list iterator, with and without a comparator: it throws an
	 * UnsupportedOperationException, the list stays [a, c], the cursor does not move, and next()
	 * returns a (#77)
	 */
	@Test
	public void testListIteratorAdd()
	{
		List<String> list = new SortedList<>(Arrays.asList("a", "c"), Comparator.naturalOrder());
		ListIterator<String> iterator = list.listIterator();
		assertThrows(UnsupportedOperationException.class, () -> iterator.add("z"));
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
	 * Test {@link Collections#swap(List, int, int)}, {@link Collections#reverse(List)},
	 * {@link Collections#shuffle(List, Random)} and {@link Collections#rotate(List, int)} on a
	 * {@link SortedList} with the natural order: they work through set, which sorts each element
	 * in, so the list [a, b, c, d] stays as it is after each of them, also with a seed that
	 * reorders an ArrayList (#77)
	 */
	@Test
	public void testCollectionsAlgorithmsWithComparator()
	{
		List<String> elements = Arrays.asList("a", "b", "c", "d");
		List<String> list;

		list = new SortedList<>(elements, Comparator.naturalOrder());
		Collections.swap(list, 0, 3);
		assertEquals(elements, list);

		list = new SortedList<>(elements, Comparator.naturalOrder());
		Collections.reverse(list);
		assertEquals(elements, list);

		List<String> shuffled = new ArrayList<>(elements);
		Collections.shuffle(shuffled, new Random(77));
		assertNotEquals(elements, shuffled);
		list = new SortedList<>(elements, Comparator.naturalOrder());
		Collections.shuffle(list, new Random(77));
		assertEquals(elements, list);

		list = new SortedList<>(elements, Comparator.naturalOrder());
		Collections.rotate(list, 1);
		assertEquals(elements, list);
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
}
