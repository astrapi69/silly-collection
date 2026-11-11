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
package io.github.astrapi69.collection.iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import io.github.astrapi69.collection.test.Elements;

/**
 * The unit test class for the class {@link EnumerationIterator}.
 *
 * @author Asterios Raptis
 */
public class EnumerationIteratorTest
{

	/**
	 * Test for the constructor
	 * {@link EnumerationIterator#EnumerationIterator(java.util.Enumeration)} and the methods
	 * {@link EnumerationIterator#hasNext()}, {@link EnumerationIterator#next()} and
	 * {@link EnumerationIterator#remove()}: steps on an iterator of the enumeration of a list, and
	 * what each gives, see {@link IteratorSteps}; remove() is not supported and leaves the
	 * iteration as it is
	 *
	 * @param description
	 *            the name of the case
	 * @param list
	 *            the elements of the list, separated by a space, '' for an empty one
	 * @param steps
	 *            the steps
	 * @param results
	 *            what each step gives
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"one to four: next four times, then no next | one two three four | next next next next hasNext | one two three four false",
			"one alone: has next | one | hasNext | true", "empty: no next | '' | hasNext | false",
			"foo alone: next foo, NoSuchElementException after it | foo | next next | foo NoSuchElementException",
			"foo, bar: next twice | foo bar | next next | foo bar",
			"foo, bar, chu: next three times | foo bar chu | next next next | foo bar chu",
			"empty: next throws NoSuchElementException | '' | next | NoSuchElementException",
			"1, 2: remove throws UnsupportedOperationException | 1 2 | remove | UnsupportedOperationException",
			"1, 2: next, remove throws UnsupportedOperationException, 2 is next | 1 2 | next remove next | 1 UnsupportedOperationException 2" }, delimiter = '|')
	public void testSteps(final String description, final String list, final String steps,
		final String results)
	{
		final EnumerationIterator<String> iterator = new EnumerationIterator<>(
			Collections.enumeration(Elements.list(list)));
		assertEquals(Elements.list(results), IteratorSteps.run(iterator, steps));
	}

}
