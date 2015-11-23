/**
 * The MIT License
 *
 * Copyright (C) 2007 Asterios Raptis
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
package de.alpharogroup.comparators;

import java.io.Serializable;
import java.util.Comparator;

/**
 * This class compare two Objects by the hashcode.
 *
 * @param <T>
 *            the generic type
 * @version 1.0
 * @author Asterios Raptis
 */
public class HashCodeComparator<T> implements Comparator<T>, Serializable
{

	/**
	 * The generated serialVersionUID.
	 */
	private static final long serialVersionUID = -6270968560769922192L;

	/**
	 * Compare two Objects by the hashcode.
	 *
	 * @param object
	 *            One Object.
	 * @param compareWithObject
	 *            The other Object to compare.
	 * @return the result.
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	@Override
	public int compare(final T object, final T compareWithObject)
	{
		final Integer nc = ComparatorExtensions.nullCheck(object, compareWithObject);
		if (nc != null)
		{
			return nc;
		}
		return ComparatorExtensions.compare(object.hashCode(), compareWithObject.hashCode());
	}

}
