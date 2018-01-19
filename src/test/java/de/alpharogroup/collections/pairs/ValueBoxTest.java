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

import de.alpharogroup.test.objects.evaluations.EqualsEvaluator;
import de.alpharogroup.test.objects.evaluations.HashcodeEvaluator;
import de.alpharogroup.test.objects.evaluations.ToStringEvaluator;

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
		final ValueBox<Integer> expected = ValueBox.<Integer> builder().value(2).build();
		final ValueBox<String> actual = new ValueBox<>("Hello");

		assertNotSame(expected, actual);
		final ValueBox<Integer> integerBox = new ValueBox<>(2);
		assertEquals(expected, integerBox);
		assertTrue(
			EqualsEvaluator.evaluateReflexivityNonNullSymmetricAndConsistency(expected, actual));
		assertTrue(EqualsEvaluator.evaluateReflexivityNonNullSymmetricConsistencyAndTransitivity(
			expected, integerBox, new ValueBox<>(2)));
	}

	/**
	 * Test method for {@link ValueBox#hashCode()}
	 */
	@Test
	public void testHashcode()
	{
		boolean expected;
		boolean actual;
		final ValueBox<Integer> integerBox = ValueBox.<Integer> builder().value(2).build();
		ValueBox<String> stringBox = ValueBox.<String> builder().value("Hello").build();
		actual = HashcodeEvaluator.evaluateEquality(integerBox, new ValueBox<>(2));
		expected = true;
		assertEquals(expected, actual);

		expected = true;
		actual = HashcodeEvaluator.evaluateUnequality(integerBox, stringBox);
		assertEquals(expected, actual);

		actual = HashcodeEvaluator.evaluateConsistency(integerBox);
		expected = true;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ValueBox#toString()}
	 */
	@Test
	public void testToString()
	{
		boolean expected;
		boolean actual;
		actual = ToStringEvaluator.evaluate(ValueBox.class);
		expected = true;
		assertEquals(expected, actual);

		final ValueBox<Integer> integerBox = ValueBox.<Integer> builder().value(2).build();

		actual = ToStringEvaluator.evaluateConsistency(integerBox);
		expected = true;
		assertEquals(expected, actual);
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
