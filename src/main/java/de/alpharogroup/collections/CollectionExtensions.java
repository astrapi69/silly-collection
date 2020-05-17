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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

/**
 * The class {@link CollectionExtensions} is an extension class for use with {@link Collection}
 * objects.
 */
public final class CollectionExtensions
{

	/**
	 * Returns <tt>true</tt> if the given {@link Collection} contains at least one object of the
	 * given objects otherwise it returns <tt>false</tt>
	 *
	 * @param <T>
	 *            the generic type
	 * @param collection
	 *            the collection
	 * @param objects
	 *            the objects for check if there are containing in the given collection
	 * @return <tt>true</tt> if the given {@link Collection} contains at least one object of the
	 *         given objects otherwise it returns <tt>false</tt>.
	 */
	@SafeVarargs
	public static <T> boolean containsAtLeastOne(Collection<T> collection, T... objects)
	{
		if (0 < objects.length)
		{
			for (T object : objects)
			{
				if (collection.contains(object))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Removes all of the first given collection's elements that are also contained in the second
	 * given collection.
	 *
	 * @param <T>
	 *            the generic type
	 * @param one
	 *            the collection where the element will be removed if any containing elements exists
	 * @param other
	 *            collection containing elements to be removed from the first given collection
	 */
	public static <T> void difference(final Collection<T> one, final Collection<T> other)
	{
		one.removeAll(other);
	}

	/**
	 * Returns a hash code based on the contents of the collection that contains array objects.
	 *
	 * @param <T>
	 *            the generic type of the array objects
	 * @param arrayObjects
	 *            the collection that contains array objects whose content-based hash code to
	 *            compute
	 * @return the content-based hash code for the given collection that contains array objects
	 */
	public static <T> int hashCode(Collection<T[]> arrayObjects)
	{
		int hashCode = 1;
		for (T[] arrayObject : arrayObjects)
		{
			hashCode = 31 * hashCode * Arrays.hashCode(arrayObject);
		}
		return hashCode;
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

	/**
	 * Checks if the given {@link Collection} is null or empty.
	 *
	 * @param <T>
	 *            the generic type
	 * @param collection
	 *            The collection to check
	 * @return true if the given {@link Collection} is null or empty otherwise false.
	 */
	public static <T> boolean isEmpty(final Collection<T> collection)
	{
		return collection == null || collection.isEmpty();
	}

	/**
	 * Compare the given two {@link Collection} objects in equality.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param one
	 *            the one
	 * @param other
	 *            the other
	 * @return true, if the given two {@link Collection} objects are equal otherwise false
	 */
	public static <T> boolean isEqualCollection(Collection<T> one, Collection<T> other)
	{
		Optional<Boolean> optionalEvaluation = preconditionOfEqualCollection(one, other);
		if (optionalEvaluation.isPresent())
			return optionalEvaluation.get();
		Collection<T> retainAll = CollectionUtils.retainAll(one, other);
		return retainAll.isEmpty() || one.containsAll(other) && other.containsAll(one);
	}

	/**
	 * Checks if the given {@link Collection} is not null or empty
	 *
	 * @param <T>
	 *            the generic type
	 * @param collection
	 *            The collection to check
	 * @return true if the given {@link Collection} is null or empty otherwise false
	 */
	public static <T> boolean isNotEmpty(final Collection<T> collection)
	{
		return collection != null && !collection.isEmpty();
	}

	/**
	 * Groups the given {@link Collection} to parts from the specified size.
	 *
	 * @param <T>
	 *            the generic type
	 * @param collection
	 *            the collection
	 * @param size
	 *            the size
	 * @return the collection
	 */
	public static <T> Collection<List<T>> partition(Collection<T> collection, int size)
	{
		final AtomicInteger counter = new AtomicInteger(0);

		return collection.stream()
			.collect(Collectors.groupingBy(it -> counter.getAndIncrement() / size)).values();
	}


	/**
	 * Checks the given two {@link Collection} objects if there are null and return the appropriate
	 * {@link Optional} boolean value
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param one
	 *            the one
	 * @param other
	 *            the other
	 * @return the {@link Optional} boolean if value is true both are null, if value is false given
	 *         two {@link Collection} objects are not equal otherwise the {@link Optional} is empty
	 */
	public static <T> Optional<Boolean> preconditionOfEqualCollection(Collection<T> one,
		Collection<T> other)
	{
		if (one == null && other == null)
		{
			return Optional.of(true);
		}

		if (one == null || other == null || one.size() != other.size())
		{
			return Optional.of(false);
		}
		return Optional.empty();
	}

	private CollectionExtensions()
	{
	}

}
