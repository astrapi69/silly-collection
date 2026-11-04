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
import io.github.astrapi69.collection.CollectionExtensions;

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
	 * Converts the given {@link Set} to an array, with the elements in the iteration order of the
	 * set.
	 * <p>
	 * The component type of the array is the most specific common superclass of the non-null
	 * elements, found by walking up their class hierarchy with {@link Class#getSuperclass()};
	 * interfaces are not considered. If every element is null, the component type is
	 * {@link Object}. So [null, "a"] gives a {@code String[]} that holds null and "a", [1, 2.5] and
	 * [2.5, 1] give a {@code Number[]}, and ["a", 1] gives an {@code Object[]}. Elements of one
	 * class give an array of that class. See {@link CollectionExtensions#toArray(Collection)}.
	 * <p>
	 * The result is a {@code T[]} only if that superclass is {@code T} or a subclass of it. A
	 * caller whose {@code T} is an interface, or a class more specific than that superclass, gets a
	 * {@link ClassCastException} where it assigns the result to a {@code T[]}: a
	 * {@code Set<Comparable<?>>} of 1 and "a" gives an {@code Object[]}, and so does a
	 * {@code Set<String>} that holds only null.
	 * <p>
	 * An empty set gives an empty {@code Object[]}, whatever {@code T} is. A caller that assigns it
	 * to an array of another type gets a {@link ClassCastException}:
	 * {@code String[] array = SetExtensions.toArray(new HashSet<String>())} throws.
	 *
	 * @param <T>
	 *            the generic type
	 * @param set
	 *            the set
	 * @return the array from the given {@link Set}, an empty {@code Object[]} if the set is empty
	 * @throws IllegalArgumentException
	 *             if the given set is null, thrown by {@code Argument.notNull}
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(final Set<T> set)
	{
		Argument.notNull(set, "set");
		if (set.isEmpty())
		{
			return (T[])new Object[0];
		}
		return CollectionExtensions.toArray(set);
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
