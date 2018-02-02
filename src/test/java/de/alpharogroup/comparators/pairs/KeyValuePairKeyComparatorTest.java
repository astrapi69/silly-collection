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
package de.alpharogroup.comparators.pairs;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNotSame;

import java.util.Comparator;

import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import de.alpharogroup.collections.pairs.KeyValuePair;

/**
 * The unit test class for the class {@link KeyValuePairKeyComparator}.
 */
public class KeyValuePairKeyComparatorTest
{

	/** For use of the expected result. */
	boolean expected;

	/** For use of the result of the comparator. */
	int actual;

	/**
	 * Test method for {@link KeyValuePairKeyComparator#compare(Object, Object)}
	 */
	@Test
	public void testCompare()
	{
		KeyValuePair<String, String> o1 = KeyValuePair.<String, String> builder().key("1")
			.value("novalue").build();
		KeyValuePair<String, String> o2 = new KeyValuePair<>();
		o2.setKey("2");
		o2.setValue("somevalue");

		assertNotSame(expected, actual);

		Comparator<KeyValuePair<String, String>> comparator = new KeyValuePairKeyComparator<>();

		actual = comparator.compare(o1, o1);
		expected = actual == 0;
		assertTrue(expected);

		actual = comparator.compare(o1, o2);
		expected = 0 > actual;
		assertTrue(expected);

		actual = comparator.compare(o2, o1);
		expected = 0 < actual;
		assertTrue(expected);
	}

	/**
	 * Test method for {@link KeyValuePairKeyComparator}
	 */
	@Test
	public void testWithBeanTester()
	{
		BeanTester beanTester = new BeanTester();
		beanTester.testBean(KeyValuePairKeyComparator.class);
	}
}
