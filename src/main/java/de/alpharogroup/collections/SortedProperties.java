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
package de.alpharogroup.collections;

import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

/**
 * The Class SortedProperties extends Properties and adds sort functionality for the keys.
 */
public class SortedProperties extends Properties
{
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private Comparator<Object> comparator;

	/**
	 * Gets the Comparator for this SortedProperties.
	 * 
	 * @return The Comparator.
	 */
	private Comparator<Object> getComparator()
	{
		if (this.comparator == null)
		{
			this.comparator = newComparator();
		}
		return this.comparator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Enumeration<Object> keys()
	{
		final Vector<Object> keys = ListExtensions.toVector(super.keys());
		Collections.sort(keys, getComparator());
		return keys.elements();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Object> keySet()
	{
		final Set<Object> set = new TreeSet<>(getComparator());
		set.addAll(super.keySet());
		return set;
	}

	/**
	 * Factory method for creating a new Comparator for sort this SortedProperties. This method is
	 * invoked in the methods keys and can be overridden so users can provide their own version of a
	 * Comparator.
	 *
	 * @return the new Comparator.
	 */
	protected Comparator<Object> newComparator()
	{
		return new Comparator<Object>()
		{
			@Override
			public int compare(final Object o1, final Object o2)
			{
				return o1.toString().compareTo(o2.toString());
			}
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumeration<?> propertyNames()
	{
		final Set<Object> set = new TreeSet<>(getComparator());
		set.addAll(super.keySet());
		return Collections.enumeration(set);
	}

}
