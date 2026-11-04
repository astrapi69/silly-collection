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
package io.github.astrapi69.collection.properties;

import java.util.AbstractSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import io.github.astrapi69.collection.list.VectorExtensions;
import io.github.astrapi69.comparator.NullCheckComparator;

/**
 * The class {@link SortedProperties} extends Properties and adds sort functionality for the keys.
 */
public class SortedProperties extends Properties
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	/**
	 * The {@link Comparator} object. It is not part of the serialized form, as the default one is a
	 * lambda, which does not serialize: {@link #getComparator()} creates it again with
	 * {@link #newComparator()} when it is needed
	 */
	private transient Comparator<Object> comparator;

	/**
	 * Instantiates a new {@link SortedProperties}.
	 */
	public SortedProperties()
	{
		this(new Properties());
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
				return NullCheckComparator.of(comparator, false);
			}
		};
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
	 * <p>
	 * The set iterates the keys in the order of the {@link Comparator} of this
	 * {@link SortedProperties}.
	 */
	@Override
	public Set<Object> keySet()
	{
		return new SortedKeySet();
	}

	/**
	 * Factory method for creating a new {@link Comparator} for sort this {@link SortedProperties}.
	 * This method is invoked when the {@link Comparator} is needed first, also after a
	 * deserialization, and can be overridden so users can provide their own version of a
	 * {@link Comparator}.
	 *
	 * @return the new {@link Comparator}.
	 */
	protected Comparator<Object> newComparator()
	{
		return NullCheckComparator.of(
			(final Object o1, final Object o2) -> o1.toString().compareTo(o2.toString()), false);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * The enumeration lists the keys in the order of the {@link Comparator} of this
	 * {@link SortedProperties}.
	 */
	@Override
	public Enumeration<?> propertyNames()
	{
		final Vector<?> names = VectorExtensions.toVector(super.propertyNames());
		Collections.sort(names, getComparator());
		return names.elements();
	}

	/**
	 * The keys of this {@link SortedProperties} as a {@link Set} backed by it, as
	 * {@link java.util.Map#keySet()} specifies: it iterates the keys in the order of the
	 * {@link Comparator}, reflects every change of the properties, and removing a key through the
	 * set or its {@link Iterator} removes its entry; it does not support {@link #add(Object)} and
	 * {@link #addAll(java.util.Collection)}. Each removal goes through
	 * {@link SortedProperties#remove(Object)} or {@link SortedProperties#clear()}, so that a
	 * subclass that overrides them sees it
	 */
	private final class SortedKeySet extends AbstractSet<Object>
	{

		/**
		 * {@inheritDoc}
		 * <p>
		 * The iterator walks a sorted copy of the keys of the moment it is created.
		 */
		@Override
		public Iterator<Object> iterator()
		{
			final Iterator<Object> keys = Collections.list(keys()).iterator();
			return new Iterator<>()
			{
				private Object current;

				@Override
				public boolean hasNext()
				{
					return keys.hasNext();
				}

				@Override
				public Object next()
				{
					current = keys.next();
					return current;
				}

				@Override
				public void remove()
				{
					// throws an IllegalStateException if next() was not called since the last
					// remove()
					keys.remove();
					SortedProperties.this.remove(current);
				}
			};
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int size()
		{
			return SortedProperties.this.size();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean contains(final Object key)
		{
			return containsKey(key);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean remove(final Object key)
		{
			return SortedProperties.this.remove(key) != null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void clear()
		{
			SortedProperties.this.clear();
		}

	}

}
