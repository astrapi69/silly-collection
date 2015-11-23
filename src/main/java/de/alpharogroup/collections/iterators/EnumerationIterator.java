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
package de.alpharogroup.collections.iterators;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * The adapter class EnumerationIterator.
 *
 * @version 1.0
 * @author Asterios Raptis
 * @param <T>
 *            Generic type of this {@link EnumerationIterator}
 */
public class EnumerationIterator<T> implements Iterator<T>
{

	/** The enumeration. */
	private final Enumeration<T> enumeration;

	/**
	 * Instantiates a new enumeration iterator.
	 *
	 * @param enumeration
	 *            the enumeration to iterate.
	 */
	public EnumerationIterator(final Enumeration<T> enumeration)
	{
		super();
		this.enumeration = enumeration;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return true, if checks for next
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext()
	{
		return this.enumeration.hasMoreElements();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return the object
	 * @see java.util.Iterator#next()
	 */
	@Override
	public T next()
	{
		return this.enumeration.nextElement();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove()
	{
		throw new UnsupportedOperationException(
			"The method remove is not supported in this Version.");
	}

}
