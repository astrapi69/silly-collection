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
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Supplier;

import de.alpharogroup.collections.CollectionExtensions;
import de.alpharogroup.collections.list.ListFactory;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * The factory class {@link SetFactory} provides factory methods for create new {@link Set} objects
 */
@UtilityClass
public final class SetFactory
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
	public static <T> Set<T> newHashSet(final T... elements)
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
	public static <T> Set<T> newHashSet(final Collection<T> collection, final T... elements)
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
	 * Factory method for create new {@link LinkedHashSet} and will be returned as {@link Set}
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param elements
	 *            the elements to add in the new {@link HashSet}
	 * @return the new {@link LinkedHashSet}
	 */
	@SafeVarargs
	public static <T> Set<T> newLinkedHashSet(final T... elements)
	{
		return newLinkedHashSet(null, elements);
	}

	/**
	 * Factory method for create new {@link LinkedHashSet} and will be returned as {@link Set}
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param collection
	 *            the optional collection that will be added to the new list
	 * @param elements
	 *            the elements to add in the new {@link LinkedHashSet}
	 * @return the new {@link HashSet}
	 */
	@SafeVarargs
	public static <T> Set<T> newLinkedHashSet(final Collection<T> collection, final T... elements)
	{
		final Set<T> set;
		if (CollectionExtensions.isNotEmpty(collection))
		{
			set = new LinkedHashSet<>(collection);
		}
		else
		{
			set = new LinkedHashSet<>();
		}
		if (0 < elements.length)
		{
			Collections.addAll(set, elements);
		}
		return set;
	}

	/**
	 * Factory method for create new {@link InsertionOrderSet} and will be returned as {@link Set}
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param elements
	 *            the elements to add in the new {@link InsertionOrderSet}
	 * @return the new {@link InsertionOrderSet}
	 */
	@SafeVarargs
	public static <T> Set<T> newInsertionOrderSet(final T... elements)
	{
		return newInsertionOrderSet(null, elements);
	}

	/**
	 * Factory method for create new {@link InsertionOrderSet} and will be returned as {@link Set}
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param collection
	 *            the optional collection that will be added to the new list
	 * @param elements
	 *            the elements to add in the new {@link InsertionOrderSet}
	 * @return the new {@link InsertionOrderSet}
	 */
	@SafeVarargs
	public static <T> Set<T> newInsertionOrderSet(final Collection<T> collection,
		final T... elements)
	{
		final Set<T> set;
		if (CollectionExtensions.isNotEmpty(collection))
		{
			set = new InsertionOrderSet<>(collection);
		}
		else
		{
			set = new InsertionOrderSet<>();
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
	 * @return the new {@link SortedSet}
	 */
	@SafeVarargs
	public static <T> SortedSet<T> newTreeSet(final T... elements)
	{
		return newTreeSet(ListFactory.newArrayList(), elements);
	}

	/**
	 * Factory method for create new {@link TreeSet} and will be returned as {@link Set}
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param comparator
	 *            the comparator
	 * @param elements
	 *            the elements to add in the new {@link TreeSet}
	 * @return the new {@link SortedSet}
	 */
	@SafeVarargs
	public static <T> SortedSet<T> newTreeSet(final @NonNull Comparator<T> comparator,
		final T... elements)
	{
		return newTreeSet(ListFactory.newArrayList(), comparator, elements);
	}

	/**
	 * Factory method for create new {@link TreeSet} and will be returned as {@link SortedSet}.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param collection
	 *            the optional collection that will be added to the new list
	 * @param elements
	 *            the elements to add in the new {@link TreeSet}
	 * @return the new {@link SortedSet}
	 */
	@SafeVarargs
	public static <T> SortedSet<T> newTreeSet(final Collection<T> collection, final T... elements)
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
	public static <T> SortedSet<T> newTreeSet(final Collection<T> collection,
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
	 * Factory method for create new {@link Supplier} for a {@link TreeSet} with a
	 * {@link Comparator}
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param comparator
	 *            the comparator
	 * @return the new {@link Supplier}
	 */
	public static <T> Supplier<SortedSet<T>> newTreeSetSupplier(
		final @NonNull Comparator<T> comparator)
	{
		return () -> new TreeSet<>(comparator);
	}

}
