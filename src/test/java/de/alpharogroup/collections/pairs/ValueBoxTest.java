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
package de.alpharogroup.collections.pairs;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotSame;
import static org.testng.AssertJUnit.assertTrue;

import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import de.alpharogroup.test.objects.evaluations.HashcodeEvaluator;

/**
 * The unit test class for the class {@link ValueBox}.
 */
public class ValueBoxTest
{

	/**
	 * Test method for {@link ValueBox#equals(Object)}
	 */
	@Test
	public void testEqualsObject()
	{
		final ValueBox<Integer> expected = ValueBox.<Integer>builder()
			.value(2).build();
		final ValueBox<String> actual = new ValueBox<>("Hello");

		assertNotSame(expected, actual);
		final ValueBox<Integer> twoBox = new ValueBox<>(2);
		assertEquals(expected, twoBox);
		assertEquals(expected.hashCode(), twoBox.hashCode());

		assertTrue(HashcodeEvaluator.evaluateConsistency(expected));
		assertTrue(HashcodeEvaluator.evaluateEqualityOfHashCode(expected, twoBox));
	}

	/**
	 * Test method for {@link ValueBox}
	 */
	@Test
	public void testWithBeanTester()
	{
		BeanTester beanTester = new BeanTester();
		beanTester.testBean(ValueBox.class);
	}

}
