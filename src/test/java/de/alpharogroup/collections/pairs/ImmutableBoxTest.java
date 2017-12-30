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

import org.testng.annotations.Test;

import de.alpharogroup.test.objects.evaluations.HashcodeEvaluator;
import de.alpharogroup.test.objects.evaluations.ToStringEvaluator;

/**
 * The unit test class for the class {@link ImmutableBox}.
 */
public class ImmutableBoxTest
{

	/**
	 * Test method for {@link ImmutableBox#equals(Object)}
	 */
	@Test
	public void testImmutableBox()
	{
		final ImmutableBox<Integer> expected = ImmutableBox.<Integer> builder().value(2).build();

		final ImmutableBox<String> actual = ImmutableBox.<String> builder().value("Hello").build();

		assertNotSame(expected, actual);
		final ImmutableBox<Integer> twoBox = new ImmutableBox<>(2);
		assertEquals(expected, twoBox);

		assertTrue(HashcodeEvaluator.evaluateConsistency(expected));
		assertTrue(HashcodeEvaluator.evaluateEquality(expected, twoBox));

		final ImmutableBox<Integer> threeBox = ImmutableBox.<Integer> builder().value(3).build();
		assertTrue(HashcodeEvaluator.evaluateUnequality(expected, threeBox));
		assertTrue(ToStringEvaluator.evaluate(ImmutableBox.class));
		assertTrue(ToStringEvaluator.evaluateConsistency(threeBox));
		assertEquals(Integer.valueOf(3), threeBox.getValue());

	}

	/**
	 * Test method for {@link ImmutableBox}
	 */
	@Test(enabled = true, expectedExceptions = NullPointerException.class)
	public void testImmutableBoxNullValue()
	{
		ImmutableBox.<Integer> builder().build();
	}

}
