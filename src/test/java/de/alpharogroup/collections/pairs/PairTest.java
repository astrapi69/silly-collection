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

import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import de.alpharogroup.evaluate.object.evaluators.EqualsHashCodeAndToStringEvaluator;

/**
 * The unit test class for the class {@link Pair}.
 */
public class PairTest
{

	/**
	 * Test method for {@link Pair#equals(Object)} , {@link Pair#hashCode()} and
	 * {@link Pair#toString()}
	 */
	@Test
	public void testEqualsHashcodeAndToString()
	{

		boolean expected;
		boolean actual;

		final Pair<String, String> first = Pair.<String, String> builder().leftContent("1")
			.rightContent("value").build();
		final Pair<Integer, String> second = new Pair<>();
		second.setLeftContent(1);
		second.setRightContent("somevalue");

		final Pair<String, String> third = Pair.<String, String> builder().leftContent("1")
			.rightContent("value").build();

		final Pair<String, String> fourth = Pair.<String, String> builder().leftContent("1")
			.rightContent("value").build();

		actual = EqualsHashCodeAndToStringEvaluator.evaluateEqualsHashcodeAndToString(first, second,
			third, fourth);
		expected = true;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link Pair}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(Pair.class);
	}

}
