/**
 * The MIT License
 *
 * Copyright (C) 2007 Asterios Raptis
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
package de.alpharogroup.collections.modifications;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

public class ModifiedCollectionsTest
{

	@Test
	public void testGetModifiedLists()
	{

		final String previous[] = { "1", "2", "3", "4", "5", "6", "7" };
		final String next[] = { "2", "5", "6", "7", "8", "9" };

		final String expectedadded[] = { "8", "9" };
		final String expectedremoved[] = { "1", "3", "4" };

		final List<String> previousList = new ArrayList<>(Arrays.asList(previous));
		final List<String> nextList = Arrays.asList(next);

		final List<String> expectedaddedList = Arrays.asList(expectedadded);
		final List<String> expectedremovedList = Arrays.asList(expectedremoved);

		ModifiedCollections<String> result = new ModifiedCollections<>();
		result = result.getModifiedLists(previousList, nextList);

		AssertJUnit.assertTrue(result.getRemovedElements().equals(expectedremovedList));
		AssertJUnit.assertTrue(result.getAddedElements().equals(expectedaddedList));

	}

}
