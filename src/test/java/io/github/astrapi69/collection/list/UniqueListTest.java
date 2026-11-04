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
import java.util.List;

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
}
