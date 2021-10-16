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
package io.github.astrapi69.collections.list;

import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

/**
 * The unit test class for the class {@link ListFactory}.
 */
public class ListFactoryTest
{

	/**
	 * Test the method {@link ListFactory#newCharacterArrayList(Collection, char...)}
	 */
	@Test
	public void testNewCharacterArrayListCollectionCharArray()
	{
		List<Character> characters;
		List<Character> newCharacterArrayList;

		characters = null;
		newCharacterArrayList = ListFactory.newCharacterArrayList(characters, 'c', 'd');
		assertNotNull(newCharacterArrayList);
		assertTrue(newCharacterArrayList.size() == 2);
		assertTrue(newCharacterArrayList.get(0).equals('c'));
		assertTrue(newCharacterArrayList.get(1).equals('d'));

		characters = ListFactory.newArrayList(Character.valueOf('a'), Character.valueOf('b'));
		newCharacterArrayList = ListFactory.newCharacterArrayList(characters, 'c', 'd');
		assertNotNull(newCharacterArrayList);
		assertTrue(newCharacterArrayList.size() == 4);
		assertTrue(newCharacterArrayList.get(0).equals('a'));
		assertTrue(newCharacterArrayList.get(1).equals('b'));
		assertTrue(newCharacterArrayList.get(2).equals('c'));
		assertTrue(newCharacterArrayList.get(3).equals('d'));
	}

	/**
	 * Test the method {@link ListFactory#newArrayList(java.util.Collection, Object...)}.
	 */
	@Test
	public void testNewArrayListCollectionObjects()
	{
		List<String> strings = ListFactory.newArrayList((Collection<String>)null, "foo");
		assertNotNull(strings);
		assertTrue(strings.size() == 1);
		assertTrue(strings.get(0).equals("foo"));

		strings = ListFactory.newArrayList(strings, "foo");
		assertNotNull(strings);
		assertTrue(strings.size() == 2);
		assertTrue(strings.get(0).equals("foo"));
	}

	/**
	 * Test the method {@link ListFactory#newArrayList(Iterable, Object...)}.
	 */
	@Test
	public void testNewArrayListIterableObjects()
	{
		List<String> strings = ListFactory.newArrayList((Iterable<String>)null, "foo");
		assertNotNull(strings);
		assertTrue(strings.size() == 1);
		assertTrue(strings.get(0).equals("foo"));
		Iterable<String> iterable = strings;
		strings = ListFactory.newArrayList(iterable, "foo");
		assertNotNull(strings);
		assertTrue(strings.size() == 2);
		assertTrue(strings.get(0).equals("foo"));
	}

	/**
	 * Test the method {@link ListFactory#newArrayList(java.util.Iterator, Object...)}.
	 */
	@Test
	public void testNewArrayListIteratorObjects()
	{
		List<String> strings = ListFactory.newArrayList((Iterator<String>)null, "foo");
		assertNotNull(strings);
		assertTrue(strings.size() == 1);
		assertTrue(strings.get(0).equals("foo"));
		Iterator<String> iterator = strings.iterator();
		strings = ListFactory.newArrayList(iterator, "bar");
		assertNotNull(strings);
		assertTrue(strings.size() == 2);
		assertTrue(strings.get(0).equals("foo"));
	}

	/**
	 * Test the method {@link ListFactory#newArrayList(Object...)}.
	 */
	@Test
	public void testNewArrayListObjects()
	{
		final List<String> strings = ListFactory.newArrayList("foo", "bar");
		assertNotNull(strings);
		assertTrue(strings.size() == 2);
		assertTrue(strings.get(0).equals("foo"));
		assertTrue(strings.get(1).equals("bar"));
	}

	/**
	 * Test the method {@link ListFactory#newLinkedList(java.util.Collection, Object...)}.
	 */
	@Test
	public void testNewLinkedListCollectionObjects()
	{
		List<String> strings = ListFactory.newLinkedList(null, "foo");
		assertNotNull(strings);
		assertTrue(strings.size() == 1);
		assertTrue(strings.get(0).equals("foo"));

		strings = ListFactory.newLinkedList(strings, "foo");
		assertNotNull(strings);
		assertTrue(strings.size() == 2);
		assertTrue(strings.get(0).equals("foo"));
	}

	/**
	 * Test the method {@link ListFactory#newLinkedList(Object...)}.
	 */
	@Test
	public void testNewLinkedListObjects()
	{
		final List<String> strings = ListFactory.newLinkedList("foo", "bar");
		assertNotNull(strings);
		assertTrue(strings.size() == 2);
		assertTrue(strings.get(0).equals("foo"));
		assertTrue(strings.get(1).equals("bar"));
	}

	/**
	 * Test the method {@link ListFactory#newRangeList(int, int)}
	 */
	@Test
	public void testNewRangeList()
	{
		final List<Integer> actual = ListFactory.newRangeList(5, 9);
		final List<Integer> expected = new ArrayList<Integer>();
		expected.add(5);
		expected.add(6);
		expected.add(7);
		expected.add(8);
		expected.add(9);
		assertTrue(actual.equals(expected));
	}

	/**
	 * Test method for {@link ListFactory} with {@link BeanTester}
	 */
	@Test(expectedExceptions = { BeanTestException.class, InvocationTargetException.class,
			UnsupportedOperationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ListFactory.class);
	}

}
