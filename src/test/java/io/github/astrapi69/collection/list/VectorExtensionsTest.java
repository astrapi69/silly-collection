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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.meanbean.test.BeanTester;

/**
 * The unit test class for the class {@link VectorExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class VectorExtensionsTest
{

	/**
	 * Test the method {@link VectorExtensions#toVector(Enumeration)}: a new vector that holds all
	 * elements of the enumeration in their order, and the enumeration has no more elements
	 *
	 * @param description
	 *            the name of the case
	 * @param elements
	 *            the elements of the enumeration, separated by a space
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "C, D, A, B: in their order | C D A B", "a single element A: A | A",
			"C, A, C, a duplicate: both C | C A C",
			"no elements: an empty vector | ''" }, delimiter = '|')
	public void testToVector(final String description, final String elements)
	{
		final Vector<String> source = elements.isEmpty()
			? new Vector<>()
			: new Vector<>(Arrays.asList(elements.split(" ")));
		final Enumeration<String> enumeration = source.elements();
		final Vector<String> actual = VectorExtensions.toVector(enumeration);
		assertEquals(source, actual);
		assertNotSame(source, actual);
		assertFalse(enumeration.hasMoreElements());
	}

	/**
	 * Test method for {@link VectorExtensions} with {@link BeanTester}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(VectorExtensions.class);
	}

}
