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
package io.github.astrapi69.collection.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import io.github.astrapi69.collection.CollectionExtensions;
import io.github.astrapi69.collection.array.ArrayFactory;

/**
 * The factory class {@link ListFactory} provides factory methods for create new {@link Map} objects
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class ListFactory
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private ListFactory()
	{
	}

	/**
	 * Factory method for create new {@link Vector} from the given optional collection and the given
	 * optional elements.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param collection
	 *            the optional collection that will be added to the new list
	 * @param elements
	 *            the optional elements to be added in the new {@link Vector}.
	 * @return the new {@link Vector} as {@link List}.
	 */
	@SafeVarargs
	public static <T> List<T> newVector(final Collection<T> collection, final T... elements)
	{
		final List<T> list;
		if (CollectionExtensions.isNotEmpty(collection))
		{
			list = new Vector<>(collection);
			Collections.addAll(list, elements);
		}
		else
		{
			list = new Vector<>();
			Collections.addAll(list, elements);
		}
		return list;
	}

	/**
	 * Factory method for create new {@link Vector} from the given optional collection and the given
	 * optional elements.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param elements
	 *            the optional elements to be added in the new {@link Vector}.
	 * @return the new {@link Vector} as {@link List}.
	 */
	@SafeVarargs
	public static <T> List<T> newVector(final T... elements)
	{
		return newVector(null, elements);
	}

	/**
	 * Factory method for create new {@link UniqueList} from the given optional collection and the
	 * given optional elements.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param collection
	 *            the optional collection that will be added to the new list
	 * @param elements
	 *            the optional elements to be added in the new {@link UniqueList}.
	 * @return the new {@link UniqueList} as {@link List}.
	 */
	@SafeVarargs
	public static <T> List<T> newUniqueList(final Collection<T> collection, final T... elements)
	{
		final List<T> list;
		if (CollectionExtensions.isNotEmpty(collection))
		{
			list = new SortedUniqueList<>(collection);
			Collections.addAll(list, elements);
		}
		else
		{
			list = new SortedUniqueList<>();
			Collections.addAll(list, elements);
		}
		return list;
	}

	/**
	 * Factory method for create new {@link UniqueList} from the given optional collection and the
	 * given optional elements.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param elements
	 *            the optional elements to be added in the new {@link UniqueList}.
	 * @return the new {@link UniqueList} as {@link List}.
	 */
	@SafeVarargs
	public static <T> List<T> newUniqueList(final T... elements)
	{
		return newUniqueList(null, elements);
	}

	/**
	 * Factory method for create new {@link SortedUniqueList} from the given optional collection and
	 * the given optional elements.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param collection
	 *            the optional collection that will be added to the new list
	 * @param comparator
	 *            the comparator
	 * @param elements
	 *            the optional elements to be added in the new {@link SortedUniqueList}.
	 * @return the new {@link SortedUniqueList} as {@link List}.
	 */
	@SafeVarargs
	public static <T> List<T> newSortedUniqueList(final Collection<T> collection,
		Comparator<? super T> comparator, final T... elements)
	{
		final List<T> list;
		if (CollectionExtensions.isNotEmpty(collection))
		{
			if (comparator != null)
			{
				list = new SortedUniqueList<>(collection, comparator);
			}
			else
			{
				list = new SortedUniqueList<>(collection);
			}
			Collections.addAll(list, elements);
		}
		else
		{
			if (comparator != null)
			{
				list = new SortedUniqueList<>(comparator);
			}
			else
			{
				list = new SortedUniqueList<>();
			}
			Collections.addAll(list, elements);
		}
		return list;
	}

	/**
	 * Factory method for create new {@link SortedUniqueList} from the given optional collection and
	 * the given optional elements.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param collection
	 *            the optional collection that will be added to the new list
	 * @param elements
	 *            the optional elements to be added in the new {@link SortedUniqueList}.
	 * @return the new {@link SortedUniqueList} as {@link List}.
	 */
	@SafeVarargs
	public static <T> List<T> newSortedUniqueList(final Collection<T> collection,
		final T... elements)
	{
		return newSortedUniqueList(collection, null, elements);
	}

	/**
	 * Factory method for create new {@link SortedUniqueList} from the given optional collection and
	 * the given optional elements.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param elements
	 *            the optional elements to be added in the new {@link SortedUniqueList}.
	 * @return the new {@link SortedUniqueList} as {@link List}.
	 */
	@SafeVarargs
	public static <T> List<T> newSortedUniqueList(final T... elements)
	{
		return newSortedUniqueList(null, elements);
	}

	/**
	 * Factory method for create new {@link SortedList} from the given optional collection and the
	 * given optional elements.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param collection
	 *            the optional collection that will be added to the new list
	 * @param comparator
	 *            the comparator
	 * @param elements
	 *            the optional elements to be added in the new {@link SortedList}.
	 * @return the new {@link SortedList} as {@link List}.
	 */
	@SafeVarargs
	public static <T> List<T> newSortedList(final Collection<T> collection,
		Comparator<? super T> comparator, final T... elements)
	{
		final List<T> list;
		if (CollectionExtensions.isNotEmpty(collection))
		{
			if (comparator != null)
			{
				list = new SortedList<>(collection, comparator);
			}
			else
			{
				list = new SortedList<>(collection);
			}
			Collections.addAll(list, elements);
		}
		else
		{
			if (comparator != null)
			{
				list = new SortedList<>(comparator);
			}
			else
			{
				list = new SortedList<>();
			}
			Collections.addAll(list, elements);
		}
		return list;
	}

	/**
	 * Factory method for create new {@link SortedList} from the given optional collection and the
	 * given optional elements.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param collection
	 *            the optional collection that will be added to the new list
	 * @param elements
	 *            the optional elements to be added in the new {@link SortedList}.
	 * @return the new {@link SortedList} as {@link List}.
	 */
	@SafeVarargs
	public static <T> List<T> newSortedList(final Collection<T> collection, final T... elements)
	{
		return newSortedList(collection, null, elements);
	}


	/**
	 * Factory method for create new {@link SortedList} from the given optional collection and the
	 * given optional elements.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param elements
	 *            the optional elements to be added in the new {@link SortedList}.
	 * @return the new {@link SortedList} as {@link List}.
	 */
	@SafeVarargs
	public static <T> List<T> newSortedList(final T... elements)
	{
		return newSortedList(null, elements);
	}

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
	 * Factory method for create new {@link ArrayList} from the given optional iterable and the
	 * given optional elements.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param iterable
	 *            the optional iterable that will be added to the new list
	 * @param elements
	 *            the optional elements to be added in the new {@link ArrayList}.
	 * @return the new {@link ArrayList} as {@link List}.
	 */
	@SafeVarargs
	public static <T> List<T> newArrayList(final Iterable<T> iterable, final T... elements)
	{
		final List<T> list = new ArrayList<>();
		if (iterable != null)
		{
			for (T t : iterable)
			{
				list.add(t);
			}
		}
		Collections.addAll(list, elements);
		return list;
	}

	/**
	 * Factory method for create new {@link ArrayList} from the given optional iterator and the
	 * given optional elements.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param iterator
	 *            the optional iterator that will be added to the new list
	 * @param elements
	 *            the optional elements to be added in the new {@link ArrayList}.
	 * @return the new {@link ArrayList} as {@link List}.
	 */
	@SafeVarargs
	public static <T> List<T> newArrayList(final Iterator<T> iterator, final T... elements)
	{
		final List<T> list = new ArrayList<>();
		if (iterator != null)
		{
			while (iterator.hasNext())
			{
				list.add(iterator.next());
			}
		}
		Collections.addAll(list, elements);
		return list;
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
		return newArrayList((Collection<T>)null, elements);
	}

	/**
	 * Factory method for create new {@link ArrayList} from the given optional primitive type
	 * elements.
	 *
	 * @param collection
	 *            the optional collection that will be added to the new list
	 * @param elements
	 *            the primitive type elements to add in the new {@link ArrayList}.
	 * @return the new {@link ArrayList} as {@link List}.
	 */
	@SafeVarargs
	public static List<Character> newCharacterArrayList(final Collection<Character> collection,
		final char... elements)
	{
		final List<Character> list = new ArrayList<>();
		if (CollectionExtensions.isNotEmpty(collection))
		{
			list.addAll(collection);
		}
		if (elements != null && 0 < elements.length)
		{
			List<Character> elementList = new String(elements).chars().mapToObj(c -> (char)c)
				.collect(Collectors.toList());
			list.addAll(elementList);
		}
		return list;
	}

	/**
	 * Factory method for create new {@link LinkedList} from the given optional collection and the
	 * given optional elements.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param collection
	 *            the optional collection that will be added to the new list
	 * @param elements
	 *            the optional elements to be added in the new {@link LinkedList}.
	 * @return the new {@link LinkedList} as {@link List}.
	 */
	@SafeVarargs
	public static <T> List<T> newLinkedList(final Collection<T> collection, final T... elements)
	{
		final List<T> list;
		if (CollectionExtensions.isNotEmpty(collection))
		{
			list = new LinkedList<>(collection);
			Collections.addAll(list, elements);
		}
		else
		{
			list = new LinkedList<>();
			Collections.addAll(list, elements);
		}
		return list;
	}

	/**
	 * Factory method for create new {@link LinkedList} from the given optional elements.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param elements
	 *            the elements to add in the new {@link LinkedList}.
	 * @return the new {@link LinkedList} as {@link List}.
	 */
	@SafeVarargs
	public static <T> List<T> newLinkedList(final T... elements)
	{
		return newLinkedList(null, elements);
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
		return Arrays.asList(ArrayFactory.newRangeArray(start, end));
	}

}
