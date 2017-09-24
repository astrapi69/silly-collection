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
package de.alpharogroup.collections;

import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import de.alpharogroup.comparators.NullCheckComparator;

/**
 * The class {@link SortedProperties} extends Properties and adds sort functionality for the keys.
 */
public class SortedProperties extends Properties
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Factory method to create a new {@link SortedProperties} object.
	 *
	 * @return the new {@link SortedProperties} object
	 */
	public static SortedProperties of()
	{
		return new SortedProperties();
	}

	/**
	 * Factory method to create a new {@link SortedProperties} object.
	 *
	 * @param defaults
	 *            the defaults
	 * @return the new {@link SortedProperties} object
	 */
	public static SortedProperties of(final Properties defaults)
	{
		return new SortedProperties(defaults);
	}

	/**
	 * Factory method to create a new {@link SortedProperties} object.
	 *
	 * @param defaults
	 *            the defaults
	 * @param comparator
	 *            the comparator
	 * @return the new {@link SortedProperties} object
	 */
	public static SortedProperties of(final Properties defaults,
		final Comparator<Object> comparator)
	{
		return new SortedProperties(defaults)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Comparator<Object> newComparator()
			{
				return NullCheckComparator.<Object> of(comparator, false);
			}
		};
	}

	/**
	 * Factory method to create a new {@link SortedProperties} object.
	 *
	 * @param defaults
	 *            the defaults
	 * @param comparator
	 *            the comparator
	 * @param nullIsGreaterThan
	 *            the null is greater than
	 * @return the new {@link SortedProperties} object
	 */
	public static SortedProperties of(final Properties defaults,
		final Comparator<Object> comparator, final boolean nullIsGreaterThan)
	{
		return new SortedProperties(defaults)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Comparator<Object> newComparator()
			{
				return NullCheckComparator.<Object> of(comparator, nullIsGreaterThan);
			}
		};
	}

	/** The {@link Comparator} object. */
	private Comparator<Object> comparator;

	/**
	 * Instantiates a new {@link SortedProperties}.
	 */
	public SortedProperties()
	{
	}

	/**
	 * Instantiates a new {@link SortedProperties}.
	 *
	 * @param defaults
	 *            the defaults
	 */
	public SortedProperties(final Properties defaults)
	{
		super(defaults);
	}

	/**
	 * Gets the {@link Comparator} for sort this {@link SortedProperties}.
	 *
	 * @return The {@link Comparator}.
	 */
	public Comparator<Object> getComparator()
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
		final Vector<Object> keys = VectorExtensions.toVector(super.keys());
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
	 * Factory method for creating a new {@link Comparator} for sort this {@link SortedProperties}.
	 * This method is invoked in the methods keys and can be overridden so users can provide their
	 * own version of a {@link Comparator}.
	 *
	 * @return the new {@link Comparator}.
	 */
	protected Comparator<Object> newComparator()
	{
		return NullCheckComparator.<Object> of(new Comparator<Object>()
		{
			@Override
			public int compare(final Object o1, final Object o2)
			{
				return o1.toString().compareTo(o2.toString());
			}
		}, false);
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
