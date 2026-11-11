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
package io.github.astrapi69.collection.set;

import java.util.Iterator;
import java.util.Objects;

/**
 * The class {@link IndexableSet}
 *
 * @param <E>
 *            the element type
 */
public class IndexableSet<E> extends InsertionOrderSet<E>
{

	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an empty {@link IndexableSet} with the default capacity (16) and load factor
	 * (0.75) of {@link InsertionOrderSet#InsertionOrderSet()}.
	 */
	public IndexableSet()
	{
	}

	/**
	 * Gets the element of the given index <br>
	 * <br>
	 * Note: use with caution
	 *
	 * @param index
	 *            the index
	 * @return the element of the given index or throws an <code>IndexOutOfBoundsException</code>
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range (<code>index &lt; 0 || index &gt;= size()</code>)
	 */
	public E get(final int index)
	{
		Objects.checkIndex(index, size());
		final Iterator<E> iterator = iterator();
		for (int i = 0; i < index; i++)
		{
			iterator.next();
		}
		return iterator.next();
	}

	/**
	 * Gets the index of the given element
	 *
	 * @param element
	 *            the element
	 * @return the index of the element or -1 if its not exists
	 */
	public int getIndex(E element)
	{
		int index = 0;
		for (E entry : this)
		{
			if (Objects.equals(entry, element))
			{
				return index;
			}
			index++;
		}
		return -1;
	}

}
