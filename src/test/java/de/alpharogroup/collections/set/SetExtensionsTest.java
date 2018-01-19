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
package de.alpharogroup.collections.set;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Set;

import org.testng.annotations.Test;

import de.alpharogroup.collections.list.ListExtensions;

/**
 * The class {@link SetExtensionsTest}.
 */
public class SetExtensionsTest
{
	/**
	 * Test the method {@link SetExtensions#isEmpty(Set)}
	 */
	@Test
	public void testIsNullOrEmpty()
	{
		Set<String> nullSet = null;

		boolean isNull = SetExtensions.isEmpty(nullSet);

		assertTrue("Set should be null.", isNull);

		nullSet = SetExtensions.newHashSet();

		isNull = SetExtensions.isEmpty(nullSet);

		assertTrue("Set should be empty.", isNull);

		nullSet.add("");

		isNull = SetExtensions.isEmpty(nullSet);

		assertFalse("Set should not be empty.", isNull);
	}

	/**
	 * Test for method {@link SetExtensions#newHashSet(java.util.Collection, Object...)}
	 */
	@Test
	public void testNewHashSetCollectionObjects()
	{

		Set<String> set = SetExtensions.newHashSet();
		assertTrue(set.size() == 0);
		set.add("foo");
		assertTrue(set.size() == 1);
		set = SetExtensions.newHashSet(ListExtensions.newArrayList("foo", "fasel"), "foo", "bar",
			"foo");
		assertTrue(set.size() == 3);
	}


	/**
	 * Test for method {@link SetExtensions#newHashSet(Object...)}
	 */
	@Test
	public void testNewHashSetObjects()
	{
		Set<String> set = SetExtensions.newHashSet();
		assertTrue(set.size() == 0);
		set = SetExtensions.newHashSet("foo", "bar", "foo");
		assertTrue(set.size() == 2);
	}

}
