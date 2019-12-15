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

import java.util.List;
import java.util.Optional;

import de.alpharogroup.collections.CollectionExtensions;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * Extensions class for use with {@link List} objects
 */
@UtilityClass
public class OptionalListExtensions
{

	/**
	 * Returns an {@link Optional} with the first object from the given {@link List}
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param list
	 *            the {@link List} object
	 * @return an {@link Optional} with the first object from the given {@link List} or an empty
	 *         {@link Optional} if the List is empty
	 */
	public static <T> Optional<T> getFirst(final @NonNull List<T> list)
	{
		if (CollectionExtensions.isNotEmpty(list))
		{
			return Optional.of(list.get(0));
		}
		return Optional.empty();
	}

	/**
	 * Returns an {@link Optional} with the last object from the given {@link List}
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param list
	 *            the {@link List} object
	 * @return an {@link Optional} with the last object from the given {@link List} or an empty
	 *         {@link Optional} if the List is empty
	 */
	public static <T> Optional<T> getLast(final @NonNull List<T> list)
	{
		if (CollectionExtensions.isNotEmpty(list))
		{
			return Optional.of(list.get(list.size() - 1));
		}
		return Optional.empty();
	}

	/**
	 * Returns an {@link Optional} with the first object if it was removed from the given
	 * {@link List}
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param list
	 *            the {@link List} object
	 * @return returns an {@link Optional} with the first object if it was removed from the given
	 *         {@link List} or an empty {@link Optional} if the {@link List} is empty
	 */
	public static <T> Optional<T> removeFirst(final @NonNull List<T> list)
	{
		if (!CollectionExtensions.isEmpty(list))
		{
			return Optional.of(list.remove(0));
		}
		return Optional.empty();
	}

	/**
	 * Returns an {@link Optional} with the last object if it was removed from the given
	 * {@link List}
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param list
	 *            the {@link List} object
	 * @return returns an {@link Optional} with the last object if it was removed from the given
	 *         {@link List} or an empty {@link Optional} if the {@link List} is empty
	 */
	public static <T> Optional<T> removeLast(final @NonNull List<T> list)
	{
		if (!CollectionExtensions.isEmpty(list))
		{
			return Optional.of(list.remove(list.size() - 1));
		}
		return Optional.empty();
	}

	/**
	 * Gets the previous element from the given {@link List}. As start point is the given element
	 *
	 * @param <T>
	 *            the generic type of elements
	 * @param list
	 *            the list
	 * @param element
	 *            the element
	 * @return an {@link Optional} with the previous element from the given {@link List} or an empty
	 *         {@link Optional} if the {@link List} has no previous element
	 */
	public static <T> Optional<T> getPrevious(final @NonNull List<T> list, final T element)
	{
		final int indexOfElement = list.indexOf(element);
		if (indexOfElement == -1 || indexOfElement == 0)
		{
			return Optional.empty();
		}
		int previousIndex = indexOfElement - 1;
		return Optional.of(list.get(previousIndex));
	}

	/**
	 * Gets the next element from the given {@link List}. As start point is the given element
	 *
	 * @param <T>
	 *            the generic type of elements
	 * @param list
	 *            the list
	 * @param element
	 *            the element
	 * @return an {@link Optional} with the next element from the given {@link List} or an empty
	 *         {@link Optional} if the {@link List} has no next element
	 */
	public static <T> Optional<T> getNext(final @NonNull List<T> list, final T element)
	{
		if (ListExtensions.hasNext(list, element))
		{
			int nextIndex = list.indexOf(element) + 1;
			return Optional.of(list.get(nextIndex));
		}
		return Optional.empty();
	}

}
