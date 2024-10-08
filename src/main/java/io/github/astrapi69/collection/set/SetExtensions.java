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
package io.github.astrapi69.collection.set;

import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;

import io.github.astrapi69.check.Argument;
import io.github.astrapi69.collection.array.ArrayFactory;

/**
 * Extensions class for use with {@link Set} objects
 */
public final class SetExtensions
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private SetExtensions()
	{
	}

	/**
	 * Converts the given {@link Set} to an array
	 *
	 * @param <T>
	 *            the generic type
	 * @param set
	 *            the set
	 * @return the array from the given {@link Set}
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(final Set<T> set)
	{
		Argument.notNull(set, "set");
		if (set.isEmpty())
		{
			return (T[])new Object[0];
		}
		T[] newArray = (T[])ArrayFactory.newArray(set.iterator().next().getClass(), set.size());
		return set.toArray(newArray);
	}

	/**
	 * Converts the given {@link Collection} to a {@link Set}
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param collection
	 *            the collection
	 * @return A new {@link Set}
	 */
	public static <T> Set<T> toSet(final Collection<T> collection)
	{
		return SetFactory.newHashSet(collection);
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
		return SetFactory.newTreeSet(collection);
	}

}
