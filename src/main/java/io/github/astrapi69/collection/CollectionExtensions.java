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
package io.github.astrapi69.collection;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
	 * Private constructor to prevent instantiation
	 */
	private CollectionExtensions()
	{
	}

	/**
	 * Returns <code>true</code> if the given {@link Collection} contains at least one object of the
	 * given objects otherwise it returns <code>false</code>
	 *
	 * @param <T>
	 *            the generic type
	 * @param collection
	 *            the collection
	 * @param objects
	 *            the objects for check if there are containing in the given collection
	 * @return <code>true</code> if the given {@link Collection} contains at least one object of the
	 *         given objects otherwise it returns <code>false</code>.
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
	 * Returns a hash code based on the contents of the collection that contains array objects: the
	 * hash code of {@link java.util.List#hashCode()}, with {@link Arrays#hashCode(Object[])} for
	 * each array, in the iteration order of the collection.
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
			hashCode = 31 * hashCode + Arrays.hashCode(arrayObject);
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
	 * Compare the given two {@link Collection} objects in equality. They are equal if both are
	 * null, or if they hold equal elements with the same cardinality, whatever their order: each
	 * element occurs in the one as often as in the other, so [1, 1, 2] and [1, 2, 2] are not equal.
	 * The elements compare by their {@link Object#equals(Object)} method, so arrays compare by
	 * identity and not by their content.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param one
	 *            the one
	 * @param other
	 *            the other
	 * @return true, if both given {@link Collection} objects are null or hold equal elements with
	 *         the same cardinality, whatever their order, otherwise false
	 */
	public static <T> boolean isEqualCollection(Collection<T> one, Collection<T> other)
	{
		Optional<Boolean> optionalEvaluation = preconditionOfEqualCollection(one, other);
		if (optionalEvaluation.isPresent())
			return optionalEvaluation.get();
		return CollectionUtils.isEqualCollection(one, other);
	}

	/**
	 * Checks if the given {@link Collection} is not null or empty
	 *
	 * @param <T>
	 *            the generic type
	 * @param collection
	 *            The collection to check
	 * @return true if the given {@link Collection} is neither null nor empty otherwise false
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

	/**
	 * Returns the given {@link Collection} object as a {@link List}: the collection itself if it is
	 * a {@link List}, otherwise a new {@link ArrayList} with the elements of the collection
	 *
	 * @param <T>
	 *            the generic type of the objects in the {@link Collection} object
	 * @param collection
	 *            the collection to transform
	 * @return the given collection itself if it is a {@link List}, otherwise a new
	 *         {@link ArrayList} created from the given {@link Collection} object, or null if the
	 *         given collection is null
	 */
	public static <T> List<T> toList(final Collection<T> collection)
	{
		if (collection == null)
		{
			return null;
		}
		if (collection instanceof List)
		{
			return (List<T>)collection;
		}
		return new ArrayList<>(collection);
	}

	/**
	 * Creates a new generic array from the given {@link Collection} object, with the elements in
	 * the iteration order of the collection.
	 * <p>
	 * The component type of the array is the most specific common superclass of the non-null
	 * elements, found by walking up their class hierarchy with {@link Class#getSuperclass()};
	 * interfaces are not considered. If every element is null, the component type is
	 * {@link Object}. So [null, "a"] gives a {@code String[]} that holds null and "a", [1, 2.5] and
	 * [2.5, 1] give a {@code Number[]}, and ["a", 1] gives an {@code Object[]}. Elements of one
	 * class give an array of that class.
	 * <p>
	 * The result is a {@code T[]} only if that superclass is {@code T} or a subclass of it. A
	 * caller whose {@code T} is an interface, or a class more specific than that superclass, gets a
	 * {@link ClassCastException} where it assigns the result to a {@code T[]}: a
	 * {@code List<Comparable<?>>} of 1 and "a" gives an {@code Object[]}, and so does a
	 * {@code List<String>} that holds only null.
	 *
	 * @param <T>
	 *            the generic type of the objects in the {@link Collection} object
	 * @param collection
	 *            the collection to transform
	 * @return the new generic array created from the given {@link Collection} object, or null if
	 *         the collection is null or empty
	 */
	public static <T> T[] toArray(final Collection<T> collection)
	{
		if (collection == null || collection.isEmpty())
		{
			return null;
		}

		@SuppressWarnings("unchecked")
		T[] array = (T[])Array.newInstance(commonSuperclass(collection), collection.size());
		return collection.toArray(array);
	}

	/**
	 * Finds the most specific common superclass of the non-null elements of the given
	 * {@link Collection} object by walking up the class hierarchy with
	 * {@link Class#getSuperclass()}; interfaces are not considered
	 *
	 * @param collection
	 *            the collection
	 * @return the most specific common superclass of the non-null elements, or {@link Object} if
	 *         every element is null
	 */
	private static Class<?> commonSuperclass(final Collection<?> collection)
	{
		Class<?> common = null;
		for (final Object element : collection)
		{
			if (element != null)
			{
				final Class<?> elementClass = element.getClass();
				if (common == null)
				{
					common = elementClass;
				}
				while (!common.isAssignableFrom(elementClass))
				{
					common = common.getSuperclass();
				}
			}
		}
		return common == null ? Object.class : common;
	}
}
