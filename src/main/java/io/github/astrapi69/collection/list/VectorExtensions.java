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
package io.github.astrapi69.collection.list;

import java.util.Enumeration;
import java.util.Vector;

/**
 * The extensions {@link VectorExtensions} class can be used with {@link Vector} objects.
 */
public final class VectorExtensions
{

	private VectorExtensions()
	{
	}

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
