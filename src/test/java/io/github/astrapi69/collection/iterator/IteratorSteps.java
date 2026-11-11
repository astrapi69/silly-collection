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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Runs steps on an {@link Iterator} and writes down what each step gives, so that a row of a
 * {@link org.junit.jupiter.params.provider.CsvSource} can name the steps and their results: the
 * step {@code hasNext} gives true or false, {@code next} the element, {@code remove} the word
 * removed; a step that throws gives the simple name of the class of its exception. An
 * {@link java.util.Enumeration} runs its steps through {@link java.util.Enumeration#asIterator()},
 * whose hasNext calls hasMoreElements and next nextElement (#118)
 */
public final class IteratorSteps
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private IteratorSteps()
	{
	}

	/**
	 * Runs the given steps on the given iterator
	 *
	 * @param iterator
	 *            the iterator
	 * @param steps
	 *            the steps, separated by a space: hasNext, next or remove
	 * @return what each step gave, in their order
	 */
	public static List<String> run(final Iterator<?> iterator, final String steps)
	{
		final List<String> results = new ArrayList<>();
		for (final String step : steps.split(" "))
		{
			try
			{
				results.add(switch (step)
				{
					case "hasNext" -> String.valueOf(iterator.hasNext());
					case "next" -> String.valueOf(iterator.next());
					case "remove" ->
					{
						iterator.remove();
						yield "removed";
					}
					default -> throw new IllegalArgumentException("no step " + step);
				});
			}
			catch (final IllegalArgumentException exception)
			{
				throw exception;
			}
			catch (final RuntimeException exception)
			{
				results.add(exception.getClass().getSimpleName());
			}
		}
		return results;
	}

}
