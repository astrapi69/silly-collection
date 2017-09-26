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
package de.alpharogroup.collections.list;

import static org.testng.AssertJUnit.assertTrue;

import java.util.Enumeration;
import java.util.Vector;

import org.testng.annotations.Test;

import de.alpharogroup.collections.list.ListExtensions;
import de.alpharogroup.collections.list.VectorExtensions;
import lombok.experimental.ExtensionMethod;

/**
 * Tests for the class {@link VectorExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
@ExtensionMethod(ListExtensions.class)
public class VectorExtensionsTest
{

	/**
	 * Test the method {@link VectorExtensions#toVector(Object...)}.
	 */
	@Test
	public void testToVector()
	{
		final Vector<String> expectedValues = new Vector<>();
		expectedValues.add("C");
		expectedValues.add("D");
		expectedValues.add("A");
		expectedValues.add("B");
		final Enumeration<String> elements = expectedValues.elements();
		final Vector<String> actuals = VectorExtensions.toVector(elements);
		for (final String actual : actuals)
		{
			assertTrue(expectedValues.contains(actual));
		}
	}

}
