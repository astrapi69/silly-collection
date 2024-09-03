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
package io.github.astrapi69.collection.enumeration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.set.SetExtensions;

/**
 * The unit test class for the class {@link SetEnumeration}.
 *
 * @author Asterios Raptis
 */
public class SetEnumerationTest
{
	/** The List for the test. */
	private final List<String> list = new ArrayList<String>()
	{
		/**
		 * The serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		{
			add("one");
			add("two");
			add("three");
			add("four");
		}
	};

	/**
	 * Test method for {@link SetEnumeration#hasMoreElements()}
	 */
	@Test
	public void testHasMoreElements() throws Exception
	{
		Set<String> set = SetExtensions.toSet(list);
		Enumeration<String> numbers;
		Vector<String> numbersNames = new Vector<>(list);
		numbers = numbersNames.elements();

		SetEnumeration<String> stringSetEnumeration = new SetEnumeration<>(set, numbers);
		assertNotNull(stringSetEnumeration);

		boolean hasMoreElements = stringSetEnumeration.hasMoreElements();
		assertTrue(hasMoreElements);

		hasMoreElements = stringSetEnumeration.hasMoreElements();
		assertTrue(hasMoreElements);

		String nextElement = stringSetEnumeration.nextElement();
		assertNotNull(nextElement);

		hasMoreElements = stringSetEnumeration.hasMoreElements();
		assertTrue(hasMoreElements);

		nextElement = stringSetEnumeration.nextElement();
		assertNotNull(nextElement);

		hasMoreElements = stringSetEnumeration.hasMoreElements();
		assertTrue(hasMoreElements);

		nextElement = stringSetEnumeration.nextElement();
		assertNotNull(nextElement);

		hasMoreElements = stringSetEnumeration.hasMoreElements();
		assertTrue(hasMoreElements);

		nextElement = stringSetEnumeration.nextElement();
		assertNotNull(nextElement);

		hasMoreElements = stringSetEnumeration.hasMoreElements();
		assertFalse(hasMoreElements);
	}

	/**
	 * Test method for {@link SetEnumeration#hasMoreElements()} with null value from the
	 * {@link Enumeration}
	 */
	@Test
	public void testHasMoreElementsEnumerationNull() throws Exception
	{
		Set<String> set = SetExtensions.toSet(list);
		set.clear();
		Enumeration<String> numbers;
		Vector<String> numbersNames = new Vector<>(list);
		numbers = numbersNames.elements();

		SetEnumeration<String> stringSetEnumeration = new SetEnumeration<>(set, numbers);
		assertNotNull(stringSetEnumeration);

		boolean hasMoreElements = stringSetEnumeration.hasMoreElements();
		assertTrue(hasMoreElements);
	}


	/**
	 * Test method for {@link SetEnumeration#hasMoreElements()}
	 */
	@Test
	public void testHasMoreElementsThrowNoSuchElementException()
	{
		Set<String> set = SetExtensions.toSet(list);
		Enumeration<String> numbers;
		Vector<String> numbersNames = new Vector<>(list);
		numbers = numbersNames.elements();

		SetEnumeration<String> stringSetEnumeration = new SetEnumeration<>(set, numbers);
		assertNotNull(stringSetEnumeration);

		boolean hasMoreElements = stringSetEnumeration.hasMoreElements();
		assertTrue(hasMoreElements);

		Assertions.assertThrows(NoSuchElementException.class, () -> {
			boolean hasMoreElements2 = stringSetEnumeration.hasMoreElements();
			assertTrue(hasMoreElements2);

			String nextElement = stringSetEnumeration.nextElement();
			assertNotNull(nextElement);

			hasMoreElements2 = stringSetEnumeration.hasMoreElements();
			assertTrue(hasMoreElements2);

			nextElement = stringSetEnumeration.nextElement();
			assertNotNull(nextElement);

			hasMoreElements2 = stringSetEnumeration.hasMoreElements();
			assertTrue(hasMoreElements2);

			nextElement = stringSetEnumeration.nextElement();
			assertNotNull(nextElement);

			hasMoreElements2 = stringSetEnumeration.hasMoreElements();
			assertTrue(hasMoreElements2);

			nextElement = stringSetEnumeration.nextElement();
			assertNotNull(nextElement);

			hasMoreElements2 = stringSetEnumeration.hasMoreElements();
			assertFalse(hasMoreElements2);

			nextElement = stringSetEnumeration.nextElement();
		});
	}

	/**
	 * Test method for {@link SetEnumeration#nextElement()}
	 */
	@Test
	public void testNextElement() throws Exception
	{
		Set<String> set = SetExtensions.toSet(list);
		Enumeration<String> numbers;
		Vector<String> numbersNames = new Vector<>(list);
		numbers = numbersNames.elements();

		SetEnumeration<String> stringSetEnumeration = new SetEnumeration<>(set, numbers);
		assertNotNull(stringSetEnumeration);

		String nextElement = stringSetEnumeration.nextElement();
		assertNotNull(nextElement);
	}

	/**
	 * Test method for the SetEnumeration constructor
	 */
	@Test
	public void testSetEnumeration() throws Exception
	{
		Set<String> set = SetExtensions.toSet(list);
		Enumeration<String> numbers;
		Vector<String> numbersNames = new Vector<>(list);
		numbers = numbersNames.elements();

		SetEnumeration<String> stringSetEnumeration = new SetEnumeration<>(set, numbers);
		assertNotNull(stringSetEnumeration);

	}

	/**
	 * Test method for {@link SetEnumeration}
	 */
	@Test
	@Disabled("add factory method to config beanTester")
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(SetEnumeration.class);
	}

}
