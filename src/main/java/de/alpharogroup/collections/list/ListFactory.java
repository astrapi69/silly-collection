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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.alpharogroup.collections.CollectionExtensions;
import de.alpharogroup.collections.array.ArrayExtensions;
import lombok.experimental.UtilityClass;

/**
 * The factory class {@link ListFactory} provides factory methods for create new {@link Map} objects
 *
 * @version 1.0
 * @author Asterios Raptis
 */
@UtilityClass
public final class ListFactory
{

	/**
	 * Factory method for create new {@link ArrayList} from the given optional collection and the
	 * given optional elements.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param collection
	 *            the optional collection that will be added to the new list
	 * @param elements
	 *            the optional elements to be added in the new {@link ArrayList}.
	 * @return the new {@link ArrayList} as {@link List}.
	 */
	@SafeVarargs
	public static <T> List<T> newArrayList(final Collection<T> collection, final T... elements)
	{
		final List<T> list;
		if (CollectionExtensions.isNotEmpty(collection))
		{
			list = new ArrayList<>(collection);
			Collections.addAll(list, elements);
		}
		else
		{
			list = new ArrayList<>();
			Collections.addAll(list, elements);
		}
		return list;
	}

	/**
	 * Factory method for create new {@link ArrayList} and returns as {@link List}.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param initialCapacity
	 *            the initial capacity
	 * @return the new {@link ArrayList} as {@link List}.
	 */
	public static <T> List<T> newArrayList(final int initialCapacity)
	{
		return new ArrayList<>(initialCapacity);
	}

	/**
	 * Factory method for create new {@link ArrayList} from the given optional elements.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param elements
	 *            the elements to add in the new {@link ArrayList}.
	 * @return the new {@link ArrayList} as {@link List}.
	 */
	@SafeVarargs
	public static <T> List<T> newArrayList(final T... elements)
	{
		return newArrayList(null, elements);
	}

	/**
	 * Creates a new {@link Integer} {@link List} with the given range that is defined through start
	 * and end. For instance if the start is 5 and the end is 9 the resulted {@link List} will be
	 * [5,6,7,8,9]
	 *
	 * @param start
	 *            The number to start
	 * @param end
	 *            The number to end minus one
	 * @return the generated {@link Integer} List
	 */
	public static List<Integer> newRangeList(final int start, final int end)
	{
		return Arrays.asList(ArrayExtensions.newRangeArray(start, end));
	}
	
}
