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
package de.alpharogroup.collections.modifications;

import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import de.alpharogroup.collections.CollectionExtensions;
import de.alpharogroup.collections.array.ArrayFactory;

/**
 * The unit test class for the class {@link ModifiedCollections}
 */
public class ModifiedCollectionsTest
{

	/**
	 * Test method for
	 * {@link ModifiedCollections#getModifiedLists(java.util.Collection, java.util.Collection)}
	 */
	@Test
	public void testGetModifiedLists()
	{
		String previous[];
		String next[];
		String expectedadded[];
		String expectedremoved[];
		List<String> previousList;
		List<String> nextList;
		List<String> expectedaddedList;
		List<String> expectedremovedList;
		ModifiedCollections<String> result;

		// initialize test data
		previous = ArrayFactory.newArray("1", "2", "3", "4", "5", "6", "7");
		next = ArrayFactory.newArray("2", "5", "6", "7", "8", "9");

		expectedadded = ArrayFactory.newArray("8", "9");
		expectedremoved = ArrayFactory.newArray("1", "3", "4");

		expectedaddedList = Arrays.asList(expectedadded);
		expectedremovedList = Arrays.asList(expectedremoved);

		// new scenario...
		previousList = new ArrayList<>(Arrays.asList(previous));
		nextList = new ArrayList<>(Arrays.asList(next));

		result = new ModifiedCollections<>();
		result = result.getModifiedLists(previousList, nextList);

		assertTrue(result.getRemovedElements().equals(expectedremovedList));
		assertTrue(result.getAddedElements().equals(expectedaddedList));

		// new scenario...
		previousList = new ArrayList<>(Arrays.asList(previous));
		nextList = new ArrayList<>(Arrays.asList(next));

		nextList.removeAll(previousList);

		assertTrue(CollectionExtensions.isEqualCollection(nextList, expectedaddedList));

		// new scenario...
		previousList = new ArrayList<>(Arrays.asList(previous));
		nextList = Arrays.asList(next);

		previousList.removeAll(nextList);

		assertTrue(CollectionExtensions.isEqualCollection(previousList, expectedremovedList));

	}

}
