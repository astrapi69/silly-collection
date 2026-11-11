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
package io.github.astrapi69.collection.enumeration;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * The class {@link SetEnumeration} associates the elements of the given {@link Set} and the given
 * {@link Enumeration}: it gives the elements of the set first, then those of the enumeration that
 * the set does not contain, null elements too. An element of the enumeration that the set cannot
 * hold, such as null for a set that does not permit null, is not contained in it and is given.
 *
 * @param <T>
 *            the generic type
 */
public class SetEnumeration<T> implements Enumeration<T>
{

	/** The enumeration. */
	private final Enumeration<T> enumeration;

	/** The iterator. */
	private final Iterator<T> iterator;
	/** The set. */
	private final Set<T> set;
	/** The next. */
	private T next;
	/**
	 * True if {@link #next} holds the element {@link #nextElement()} gives next, which may be null
	 * (#118).
	 */
	private boolean hasNext;

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
		if (!hasNext)
		{
			if (iterator.hasNext())
			{
				next = iterator.next();
				hasNext = true;
			}
			else if (enumeration != null)
			{
				while (!hasNext && enumeration.hasMoreElements())
				{
					next = enumeration.nextElement();
					hasNext = !contains(next);
				}
			}
		}
		return hasNext;
	}

	/**
	 * Checks if the set contains the given element, as {@link java.util.AbstractSet#equals} treats
	 * a query: an element the set rejects, with a {@link NullPointerException} or a
	 * {@link ClassCastException}, is not in it
	 *
	 * @param element
	 *            the element
	 * @return true if the set contains the element, false if not or if the set cannot hold it
	 */
	private boolean contains(final T element)
	{
		try
		{
			return set.contains(element);
		}
		catch (final NullPointerException | ClassCastException e)
		{
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T nextElement()
	{
		if (hasMoreElements())
		{
			hasNext = false;
			return next;
		}
		else
		{
			throw new NoSuchElementException();
		}
	}

}
