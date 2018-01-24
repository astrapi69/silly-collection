package de.alpharogroup.collections;

import java.util.Collection;

/**
 * The class {@link CollectionExtensions} is an extension class for use with {@link Collection}
 * objects. .
 */
public class CollectionExtensions
{

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
