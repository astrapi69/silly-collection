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

}
