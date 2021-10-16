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

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

public class OptionalListExtensionsTest
{

	/**
	 * Test method for {@link OptionalListExtensions#getFirst(List)}
	 */
	@Test
	public final void testGetFirst()
	{
		Optional<String> expected;
		Optional<String> actual;
		List<String> search;

		expected = Optional.of("Leonidas");
		search = ListFactory.newArrayList();
		search.add(expected.get());
		search.add("Berta");
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add("Anton");

		actual = OptionalListExtensions.getFirst(search);
		assertEquals(expected, actual);

		search = ListFactory.newArrayList();
		actual = OptionalListExtensions.getFirst(search);
		expected = Optional.empty();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link OptionalListExtensions#getLast(List)}
	 */
	@Test
	public final void testGetLast()
	{
		Optional<String> expected;
		Optional<String> actual;

		final List<String> search = ListFactory.newArrayList();
		actual = OptionalListExtensions.getLast(search);
		expected = Optional.empty();
		assertEquals(expected, actual);
		search.add("Anton");
		search.add("Berta");
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add("Leonidas");
		actual = OptionalListExtensions.getLast(search);
		expected = Optional.of("Leonidas");
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link OptionalListExtensions#getNext(List, Object)}
	 */
	@Test
	public final void testGetNext()
	{
		Optional<String> expected;
		Optional<String> actual;
		List<String> search;

		String caeser = "Caesar";
		String dora = "Dora";
		String emil = "Emil";
		String anton = "Anton";
		search = ListFactory.newArrayList();
		search.add(caeser);
		search.add(dora);
		search.add(emil);
		search.add(anton);

		actual = OptionalListExtensions.getNext(search, "foo");
		expected = Optional.empty();
		assertEquals(expected, actual);

		actual = OptionalListExtensions.getNext(search, null);
		expected = Optional.empty();
		assertEquals(expected, actual);

		actual = OptionalListExtensions.getNext(search, anton);
		expected = Optional.empty();
		assertEquals(expected, actual);

		actual = OptionalListExtensions.getNext(search, emil);
		expected = Optional.of(anton);
		assertEquals(expected, actual);

		actual = OptionalListExtensions.getNext(search, dora);
		expected = Optional.of(emil);
		assertEquals(expected, actual);

		actual = OptionalListExtensions.getNext(search, caeser);
		expected = Optional.of(dora);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link OptionalListExtensions#getPrevious(List, Object)}
	 */
	@Test
	public final void testGetPrevious()
	{
		Optional<String> expected;
		Optional<String> actual;
		List<String> search;

		String caeser = "Caesar";
		String dora = "Dora";
		String emil = "Emil";
		String anton = "Anton";
		search = ListFactory.newArrayList();
		search.add(caeser);
		search.add(dora);
		search.add(emil);
		search.add(anton);

		actual = OptionalListExtensions.getPrevious(search, "foo");
		expected = Optional.empty();
		assertEquals(expected, actual);

		actual = OptionalListExtensions.getPrevious(search, null);
		expected = Optional.empty();
		assertEquals(expected, actual);

		actual = OptionalListExtensions.getPrevious(search, caeser);
		expected = Optional.empty();
		assertEquals(expected, actual);

		actual = OptionalListExtensions.getPrevious(search, dora);
		expected = Optional.of(caeser);
		assertEquals(expected, actual);

		actual = OptionalListExtensions.getPrevious(search, emil);
		expected = Optional.of(dora);
		assertEquals(expected, actual);

		actual = OptionalListExtensions.getPrevious(search, anton);
		expected = Optional.of(emil);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link OptionalListExtensions#removeFirst(List)}
	 */
	@Test
	public final void testRemoveFirst()
	{
		Optional<String> expected;
		Optional<String> actual;
		String removed;
		List<String> search;
		expected = Optional.of("Leonidas");
		removed = "Berta";
		search = ListFactory.newArrayList();
		search.add(removed);
		search.add(expected.get());
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add("Anton");

		actual = OptionalListExtensions.removeFirst(search);
		assertTrue(removed.equals(actual.get()));

		actual = OptionalListExtensions.getFirst(search);
		assertEquals(expected, actual);

		search.clear();

		actual = OptionalListExtensions.removeFirst(search);
		expected = Optional.empty();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link OptionalListExtensions#removeLast(List)}
	 */
	@Test
	public final void testRemoveLast()
	{
		Optional<String> expected;
		Optional<String> actual;
		String removed;
		List<String> search;
		expected = Optional.of("Leonidas");
		removed = "Berta";
		search = ListFactory.newArrayList();

		search.add("Anton");
		search.add("Caesar");
		search.add("Dora");
		search.add("Emil");
		search.add(expected.get());
		search.add(removed);
		actual = OptionalListExtensions.removeLast(search);
		assertTrue("", removed.equals(actual.get()));

		actual = OptionalListExtensions.getLast(search);
		assertEquals(expected, actual);

		search.clear();

		actual = OptionalListExtensions.removeLast(search);
		expected = Optional.empty();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link OptionalListExtensions} with {@link BeanTester}
	 */
	@Test(expectedExceptions = { BeanTestException.class, InvocationTargetException.class,
			UnsupportedOperationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(OptionalListExtensions.class);
	}

}
