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
package io.github.astrapi69.collection.modification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * The unit test class for the class {@link ModifiedCollections}
 */
public class ModifiedCollectionsTest
{

	/**
	 * The elements of a row of a {@link CsvSource}
	 *
	 * @param elements
	 *            the elements, separated by a space, may be empty
	 * @return the elements
	 */
	static List<String> elements(final String elements)
	{
		return elements.isEmpty() ? List.of() : List.of(elements.split(" "));
	}

	/**
	 * Test for method
	 * {@link ModifiedCollections#getModifiedLists(java.util.Collection, java.util.Collection)}: the
	 * elements of the next collection that the previous does not hold are added, those of the
	 * previous that the next does not hold are removed, each in the order of its collection; the
	 * method returns the object it is called on
	 *
	 * @param description
	 *            the name of the case
	 * @param previous
	 *            the previous collection, the elements separated by a space
	 * @param next
	 *            the next collection, the same way
	 * @param added
	 *            the added elements, the same way
	 * @param removed
	 *            the removed elements, the same way
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"1 to 7 and 2, 5, 6, 7, 8, 9: 8, 9 added, 1, 3, 4 removed | 1 2 3 4 5 6 7 | 2 5 6 7 8 9 | 8 9 | 1 3 4",
			"the same elements: nothing | a b | a b | '' | ''",
			"the same elements in another order: nothing | a b | b a | '' | ''",
			"from empty to a, b: a, b added | '' | a b | a b | ''",
			"from a, b to empty: a, b removed | a b | '' | '' | a b" }, delimiter = '|')
	public void testGetModifiedLists(final String description, final String previous,
		final String next, final String added, final String removed)
	{
		final ModifiedCollections<String> modifiedCollections = new ModifiedCollections<>();
		final ModifiedCollections<String> result = modifiedCollections
			.getModifiedLists(new ArrayList<>(elements(previous)), new ArrayList<>(elements(next)));
		assertSame(modifiedCollections, result);
		assertEquals(elements(added), result.getAddedElements());
		assertEquals(elements(removed), result.getRemovedElements());
	}

}
