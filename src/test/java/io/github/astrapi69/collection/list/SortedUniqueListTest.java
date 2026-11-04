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
import java.util.List;

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
}
