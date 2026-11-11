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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.test.Elements;

/**
 * The unit test class for the class {@link ArrayIterator}.
 *
 * @author Asterios Raptis
 */
public class ArrayIteratorTest
{

	/**
	 * Test for the constructor and the methods {@link ArrayIterator#hasNext()},
	 * {@link ArrayIterator#next()} and {@link ArrayIterator#remove()}: steps on an iterator of an
	 * array, what each gives, see {@link IteratorSteps}, and the array afterwards, which remove()
	 * changes; {@link ArrayIterator#getArray()} gives the given array
	 *
	 * @param description
	 *            the name of the case
	 * @param array
	 *            the elements of the array, separated by a space, '' for an empty one, null for a
	 *            null element
	 * @param steps
	 *            the steps
	 * @param results
	 *            what each step gives
	 * @param arrayAfterwards
	 *            the elements of the array afterwards, null for a null element
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"0 to 3: next four times, then no next | 0 1 2 3 | next next next next hasNext | 0 1 2 3 false | 0 1 2 3",
			"0 to 3: next five times, NoSuchElementException after the last | 0 1 2 3 | next next next next next | 0 1 2 3 NoSuchElementException | 0 1 2 3",
			"empty: no next | '' | hasNext | false | ''",
			"empty: next throws NoSuchElementException | '' | next | NoSuchElementException | ''",
			"foo alone: has next | foo | hasNext | true | foo",
			"foo alone: next foo, then no next | foo | next hasNext | foo false | foo",
			"foo, bar: next foo, then bar | foo bar | next next | foo bar | foo bar",
			"foo alone: next, remove, no next | foo | next remove hasNext | foo removed false | null",
			"foo, bar: next, remove, bar moves to 0 and is next (#118) | foo bar | next remove hasNext next | foo removed true bar | bar null",
			"foo, bar, jack: next, remove, has next, remove again throws IllegalStateException, next bar (#118) | foo bar jack | next remove hasNext remove next | foo removed true IllegalStateException bar | bar jack null",
			"foo, bar, jack: next twice, remove bar, next jack (#118) | foo bar jack | next next remove next hasNext | foo bar removed jack false | foo jack null",
			"foo, null, bar: next twice, remove null, bar moves to 1 (#118) | foo null bar | next next remove next hasNext | foo null removed bar false | foo bar null",
			"foo, bar: next twice, remove bar, the last, no next | foo bar | next next remove hasNext | foo bar removed false | foo null",
			"foo, bar, jack: next and remove twice, next jack, the array ends with two nulls (#118) | foo bar jack | next remove next remove next hasNext | foo removed bar removed jack false | jack null null",
			"foo alone: next, remove, next throws NoSuchElementException | foo | next remove next | foo removed NoSuchElementException | null",
			"foo, null, bar: next foo, has next, next null, then bar (#118) | foo null bar | next hasNext next next hasNext | foo true null bar false | foo null bar",
			"empty: remove throws IllegalStateException | '' | remove | IllegalStateException | ''",
			"foo, bar: remove before next throws IllegalStateException | foo bar | remove next | IllegalStateException foo | foo bar" }, delimiter = '|')
	public void testSteps(final String description, final String array, final String steps,
		final String results, final String arrayAfterwards)
	{
		final String[] elements = Elements.listWithNull(array).toArray(new String[0]);
		final ArrayIterator<String> iterator = new ArrayIterator<>(elements);
		assertSame(elements, iterator.getArray());
		assertEquals(Elements.list(results), IteratorSteps.run(iterator, steps));
		assertArrayEquals(Elements.listWithNull(arrayAfterwards).toArray(new String[0]), elements,
			() -> "the array afterwards " + Arrays.toString(elements));
	}

	/**
	 * Test method for {@link ArrayIterator#next()} of an iterator with no element left: a
	 * {@link NoSuchElementException} without a message, as the iterators of the JDK (#145)
	 *
	 * @param description
	 *            the name of the case
	 * @param array
	 *            the elements of the array, separated by a space, '' for an empty one
	 * @param nextCalls
	 *            how often next is called before the one that throws
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "an empty array | '' | 0",
			"foo alone, after its next | foo | 1" }, delimiter = '|')
	public void testNextWithNoElementLeft(final String description, final String array,
		final int nextCalls)
	{
		final ArrayIterator<String> iterator = new ArrayIterator<>(
			Elements.list(array).toArray(new String[0]));
		for (int i = 0; i < nextCalls; i++)
		{
			iterator.next();
		}
		assertNull(assertThrows(NoSuchElementException.class, iterator::next).getMessage());
	}

	/**
	 * Test method for {@link ArrayIterator}
	 */
	@Test
	@Disabled("add factory method to config beanTester")
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ArrayIterator.class);
	}

}
