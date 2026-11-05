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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.list.ListFactory;

/**
 * The unit test class for the class {@link ModifiedState}.
 */
public class ModifiedStateTest
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
		return elements.isEmpty()
			? new ArrayList<>()
			: ListFactory.newArrayList(elements.split(" "));
	}

	/**
	 * Test for method {@link ModifiedState#isModified(java.util.Collection, java.util.Collection)}:
	 * the state of the next collection compared with the previous one
	 *
	 * @param description
	 *            the name of the case
	 * @param previous
	 *            the previous collection, the elements separated by a space
	 * @param next
	 *            the next collection, the same way
	 * @param expected
	 *            the state
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "both empty: UNMODIFIED | '' | '' | UNMODIFIED",
			"one in the next, none before: FIRST_MATCH | '' | one | FIRST_MATCH",
			"one in both: UNMODIFIED | one | one | UNMODIFIED",
			"two added to one: NEW_MATCH | one | one two | NEW_MATCH",
			"one and two in both: UNMODIFIED | one two | one two | UNMODIFIED",
			"one and two in another order: UNMODIFIED | one two | two one | UNMODIFIED",
			"two replaced by three: NEW_MATCH | one two | one three | NEW_MATCH",
			"two removed: REMOVED | one two | one | REMOVED",
			"all removed: CLEARED | one two | '' | CLEARED" }, delimiter = '|')
	public void testIsModified(final String description, final String previous, final String next,
		final ModifiedState expected)
	{
		assertEquals(expected, ModifiedState.isModified(elements(previous), elements(next)));
	}

	/**
	 * Test for method {@link ModifiedState#isModified(java.util.Collection, java.util.Collection)}
	 * with null for the previous or the next collection: it throws an
	 * {@link IllegalArgumentException}
	 *
	 * @param description
	 *            the name of the case
	 * @param previousNull
	 *            true for a null previous collection, false for a null next one
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource({ "the previous collection null: IllegalArgumentException, true",
			"the next collection null: IllegalArgumentException, false" })
	public void testIsModifiedWithNull(final String description, final boolean previousNull)
	{
		final List<String> empty = ListFactory.newArrayList();
		assertThrows(IllegalArgumentException.class, () -> ModifiedState
			.isModified(previousNull ? null : empty, previousNull ? empty : null));
	}

	/**
	 * Test method for {@link ModifiedState}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ModifiedState.class);
	}

}
