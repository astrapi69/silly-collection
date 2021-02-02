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
package io.github.astrapi69.collections.enumerations;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * The class {@link SetEnumeration} associates the elements of the given {@link Set} and the given
 * {@link Enumeration}.
 *
 * @param <T>
 *            the generic type
 */
public class SetEnumeration<T> implements Enumeration<T>
{

	/** The enumeration. */
	private Enumeration<T> enumeration;

	/** The iterator. */
	private Iterator<T> iterator;

	/** The next. */
	private T next;

	/** The set. */
	private Set<T> set;

	/**
	 * Instantiates a new {@link SetEnumeration} from the given {@link Set} and the given
	 * {@link Enumeration}.
	 *
	 * @param set
	 *            the set
	 * @param enumeration
	 *            the enumeration
	 */
	public SetEnumeration(final Set<T> set, final Enumeration<T> enumeration)
	{
		this.set = set;
		this.iterator = this.set.iterator();
		this.enumeration = enumeration;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasMoreElements()
	{
		if (next == null)
		{
			if (iterator.hasNext())
			{
				next = iterator.next();
			}
			else if (enumeration != null)
			{
				while (next == null && enumeration.hasMoreElements())
				{
					next = enumeration.nextElement();
					if (set.contains(next))
					{
						next = null;
					}
				}
			}
		}
		return next != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T nextElement()
	{
		if (hasMoreElements())
		{
			T result = next;
			next = null;
			return result;
		}
		else
		{
			throw new NoSuchElementException();
		}
	}

}
