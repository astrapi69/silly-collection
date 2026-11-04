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
		list.sort(comparator);

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
}
