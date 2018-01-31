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
package de.alpharogroup.collections;

import java.util.Collection;

import lombok.experimental.UtilityClass;

/**
 * The class {@link CollectionExtensions} is an extension class for use with {@link Collection}
 * objects.
 */
@UtilityClass
public final class CollectionExtensions
{

	/**
	 * Difference.
	 *
	 * @param <T>
	 *            the generic type
	 * @param collection1
	 *            the collection 1
	 * @param collection2
	 *            the collection 2
	 */
	public static <T> void difference(final Collection<T> collection1,
		final Collection<T> collection2)
	{
		// collection1.stream().filter(e -> !collection1.contains(e)).collect(Collectors.toSet());
		collection1.removeAll(collection2);
	}

	/**
	 * Intersection of the given collections. Internally this method uses the
	 * {@link Collection#retainAll(Collection)} for come to the result.
	 *
	 * @param <T>
	 *            the generic type
	 * @param toIntersect
	 *            the to intersect
	 * @return the result of the intersection. This will be the first collection that contains the
	 *         result.
	 */
	@SafeVarargs
	public static <T> Collection<T> intersection(final Collection<T>... toIntersect)
	{
		if (1 < toIntersect.length)
		{
			final Collection<T> first = toIntersect[0];
			for (int i = 1; i < toIntersect.length; i++)
			{
				first.retainAll(toIntersect[i]);
			}
			return first;
		}
		return null;
	}

}