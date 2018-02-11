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
package de.alpharogroup.collections.set;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import de.alpharogroup.collections.CollectionExtensions;
import lombok.experimental.UtilityClass;

/**
 * Extensions class for use with {@link Set} objects.
 */
@UtilityClass
public class SetExtensions
{

	/**
	 * Factory method for create new {@link HashSet} and will be returned as {@link Set}
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param elements
	 *            the elements to add in the new {@link HashSet}
	 * @return the new {@link HashSet}
	 */
	@SafeVarargs
	public static final <T> Set<T> newHashSet(final T... elements)
	{
		return newHashSet(null, elements);
	}

	/**
	 * Factory method for create new {@link HashSet} and will be returned as {@link Set}
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param collection
	 *            the optional collection that will be added to the new list
	 * @param elements
	 *            the elements to add in the new {@link HashSet}
	 * @return the new {@link HashSet}
	 */
	@SafeVarargs
	public static final <T> Set<T> newHashSet(final Collection<T> collection, final T... elements)
	{
		final Set<T> set;
		if (CollectionExtensions.isNotEmpty(collection))
		{
			set = new HashSet<>(collection);
		}
		else
		{
			set = new HashSet<>();
		}
		if (0 < elements.length)
		{
			Collections.addAll(set, elements);
		}
		return set;
	}

	/**
	 * Factory method for create new {@link TreeSet} and will be returned as {@link Set}
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param elements
	 *            the elements to add in the new {@link TreeSet}
	 * @return the new {@link TreeSet}
	 */
	@SafeVarargs
	public static final <T> SortedSet<T> newTreeSet(final T... elements)
	{
		return newTreeSet(null, elements);
	}

	/**
	 * Factory method for create new {@link TreeSet} and will be returned as {@link SortedSet}
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param collection
	 *            the optional collection that will be added to the new list
	 * @param elements
	 *            the elements to add in the new {@link TreeSet}
	 * @return the new {@link TreeSet}
	 */
	@SafeVarargs
	public static final <T> SortedSet<T> newTreeSet(final Collection<T> collection,
		final T... elements)
	{
		return newTreeSet(collection, null, elements);
	}

	/**
	 * Factory method for create new {@link TreeSet} and will be returned as {@link SortedSet}
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param collection
	 *            the optional collection that will be added to the new list
	 * @param comparator
	 *            the comparator
	 * @param elements
	 *            the elements to add in the new {@link TreeSet}
	 * @return the new {@link TreeSet}
	 */
	@SafeVarargs
	public static final <T> SortedSet<T> newTreeSet(final Collection<T> collection,
		final Comparator<T> comparator, final T... elements)
	{
		final SortedSet<T> sortedSet;
		if (comparator != null)
		{
			sortedSet = new TreeSet<>(comparator);
		}
		else
		{
			sortedSet = new TreeSet<>();
		}
		if (CollectionExtensions.isNotEmpty(collection))
		{
			sortedSet.addAll(collection);
		}
		if (0 < elements.length)
		{
			Collections.addAll(sortedSet, elements);
		}
		return sortedSet;
	}

	/**
	 * Checks if a Set is null or empty
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param set
	 *            The Set to check
	 * @return true if the set is null or empty otherwise false
	 * @deprecated use instead the same name method in the class CollectionExtensions. Note: will be
	 *             removed in the next minor release
	 */
	@Deprecated
	public static <T> boolean isEmpty(final Set<T> set)
	{
		return CollectionExtensions.isEmpty(set);
	}

	/**
	 * Checks if the given Set is not null or empty
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param set
	 *            The Set to check
	 * @return true if the set is null or empty otherwise false.
	 * @deprecated use instead the same name method in the class CollectionExtensions. Note: will be
	 *             removed in the next minor release
	 */
	@Deprecated
	public static <T> boolean isNotEmpty(final Set<T> set)
	{
		return CollectionExtensions.isNotEmpty(set);
	}

	/**
	 * Converts the given {@link Collection} to a {@link Set}.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param collection
	 *            the collection
	 * @return A new {@link Set}
	 */
	public static <T> Set<T> toSet(final Collection<T> collection)
	{
		return newHashSet(collection);
	}

	/**
	 * Converts the given {@link Collection} to a {@link SortedSet}.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param collection
	 *            the collection
	 * @return A new {@link SortedSet}
	 */
	public static <T> SortedSet<T> toSortedSet(final Collection<T> collection)
	{
		return newTreeSet(collection);
	}

}
