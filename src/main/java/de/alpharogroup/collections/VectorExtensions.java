package de.alpharogroup.collections;

import java.util.Enumeration;
import java.util.Vector;

import lombok.experimental.UtilityClass;

/**
 * The extensions {@link VectorExtensions} class can be used with {@link Vector} objects.
 */
@UtilityClass
public class VectorExtensions
{

	/**
	 * Converts the given enumaration to a Vector.
	 *
	 * @param <T>
	 *            the generic type
	 * @param enumaration
	 *            The Enumeration to convert.
	 *
	 * @return A new Vector with the content of the given Enumeration.
	 */
	public static <T> Vector<T> toVector(final Enumeration<T> enumaration)
	{
		final Vector<T> vector = new Vector<>();
		while (enumaration.hasMoreElements())
		{
			vector.add(enumaration.nextElement());
		}
		return vector;
	}
}
