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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.experimental.UtilityClass;

/**
 * Extensions class for use with {@link Set} objects.
 */
@UtilityClass
public class SetExtensions
{

	/**
	 * Factory method for create new {@link HashSet} and returns as {@link Set}.
	 *
	 * @param <T>
	 *            the generic type
	 * @param elements
	 *            the elements to add in the new {@link HashSet}.
	 * @return the new {@link HashSet} and returns as {@link Set}.
	 */
	@SafeVarargs
	public static final <T> Set<T> newHashSet(final T... elements)
	{
		return newHashSet(null, elements);
	}

	/**
	 * Factory method for create new {@link HashSet} and returns as {@link Set}.
	 *
	 * @param <T>
	 *            the generic type
	 * @param elements
	 *            the elements to add in the new {@link HashSet}.
	 * @return the new {@link HashSet} and returns as {@link Set}.
	 */
	@SafeVarargs
	public static final <T> Set<T> newHashSet(final Collection<T> collection, final T... elements)
	{
		final Set<T> set;
		if (collection != null && !collection.isEmpty())
		{
			set = new HashSet<>(collection);
			Collections.addAll(set, elements);
		}
		else
		{
			set = new HashSet<>();
			Collections.addAll(set, elements);
		}
		return set;
	}

	/**
	 * Checks if a Set is null or empty.
	 *
	 * @param <T>
	 *            the generic type
	 * @param set
	 *            The Set to check.
	 * @return true if the set is null or empty otherwise false.
	 */
	public static <T> boolean isEmpty(final Set<T> set)
	{
		return set == null || set.isEmpty();
	}

	/**
	 * Checks if the given Set is not null or empty.
	 *
	 * @param <T>
	 *            the generic type
	 * @param set
	 *            The Set to check.
	 * @return true if the set is null or empty otherwise false.
	 */
	public static <T> boolean isNotEmpty(final Set<T> set)
	{
		return set != null && !set.isEmpty();
	}

	/**
	 * Converts the given {@link List} to a {@link Set}.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param list
	 *            the list
	 * @return A new {@link Set}
	 */
	public static <T> Set<T> toSet(final List<T> list)
	{
		return new HashSet<>(list);
	}

}
