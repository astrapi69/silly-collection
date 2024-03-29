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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import io.github.astrapi69.collection.CollectionExtensions;
import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.comparator.NullCheckComparator;

/**
 * The class {@link IndexSortedProperties} extends SortedProperties and holds an intern list with
 * the keys for get values over an index.
 *
 * @author Asterios Raptis
 */
public class IndexSortedProperties extends SortedProperties
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	/** The keys. */
	private List<Object> keys;

	/**
	 * Instantiates a new {@link IndexSortedProperties}.
	 */
	public IndexSortedProperties()
	{
		this(new Properties());
	}

	/**
	 * Instantiates a new {@link IndexSortedProperties}.
	 *
	 * @param defaults
	 *            the defaults
	 */
	public IndexSortedProperties(final Properties defaults)
	{
		super(defaults);
		sortKeyList(defaults.keySet());
	}

	/**
	 * Factory method to create a new {@link IndexSortedProperties} object.
	 *
	 * @return the new {@link IndexSortedProperties} object
	 */
	public static IndexSortedProperties of()
	{
		return new IndexSortedProperties();
	}

	/**
	 * Factory method to create a new {@link IndexSortedProperties} object.
	 *
	 * @param defaults
	 *            the defaults
	 * @return the new {@link IndexSortedProperties} object
	 */
	public static IndexSortedProperties of(final Properties defaults)
	{
		return new IndexSortedProperties(defaults);
	}

	/**
	 * Factory method to create a new {@link IndexSortedProperties} object.
	 *
	 * @param defaults
	 *            the defaults
	 * @param comparator
	 *            the comparator
	 * @return the new {@link IndexSortedProperties} object
	 */
	public static IndexSortedProperties of(final Properties defaults,
		final Comparator<Object> comparator)
	{
		return new IndexSortedProperties(defaults)
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
	 * Factory method to create a new {@link IndexSortedProperties} object.
	 *
	 * @param defaults
	 *            the defaults
	 * @param comparator
	 *            the comparator
	 * @param nullIsGreaterThan
	 *            the null is greater than
	 * @return the new {@link IndexSortedProperties} object
	 */
	public static IndexSortedProperties of(final Properties defaults,
		final Comparator<Object> comparator, final boolean nullIsGreaterThan)
	{
		return new IndexSortedProperties(defaults)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Comparator<Object> newComparator()
			{
				return NullCheckComparator.of(comparator, nullIsGreaterThan);
			}
		};
	}

	/**
	 * Adds the key.
	 *
	 * @param key
	 *            the key
	 */
	private void addKey(final Object key)
	{
		keys.add(key);
		resortKeyList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void clear()
	{
		if (CollectionExtensions.isNotEmpty(keys))
		{
			keys.clear();
		}
		super.clear();
	}

	/**
	 * Gets the entry from the given index.
	 *
	 * @param index
	 *            the index
	 * @return the object that was found over the index or null if the index was out of range.
	 */
	public synchronized Object get(final int index)
	{
		if (index < keys.size())
			return getProperty((String)keys.get(index));
		return null;
	}

	/**
	 * Gets the property over the index.
	 *
	 * @param index
	 *            the index
	 * @return the property that was found over the index or null if the index was out of range.
	 */
	public String getProperty(final int index)
	{
		if (index < keys.size())
			return getProperty((String)keys.get(index));
		return null;
	}

	/**
	 * Gets the index of the given object.
	 *
	 * @param object
	 *            the object
	 * @return the index of the given object or -1 if not found.
	 */
	public synchronized int indexOf(final Object object)
	{
		for (final Object key : keys)
		{
			final Object value = getProperty((String)key);
			if (object.equals(value))
			{
				return keys.indexOf(key);
			}
		}
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Object put(final Object key, final Object value)
	{
		addKey(key);
		return super.put(key, value);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void putAll(final Map<? extends Object, ? extends Object> t)
	{
		super.putAll(t);
		sortKeyList(keySet());
	}

	/**
	 * Removes the entry from the given index.
	 *
	 * @param index
	 *            the index
	 * @return the object that was removed or null if the index was out of range.
	 */
	public synchronized Object remove(final int index)
	{
		if (index < keys.size())
			return remove(keys.get(index));
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Object remove(final Object key)
	{
		removeKey(key);
		return super.remove(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized boolean remove(final Object key, final Object value)
	{
		final boolean removed = super.remove(key, value);
		if (removed)
			removeKey(key);
		return removed;
	}

	/**
	 * Removes the key.
	 *
	 * @param key
	 *            the key
	 */
	private void removeKey(final Object key)
	{
		keys.remove(key);
		resortKeyList();
	}

	/**
	 * Resort the intern key list.
	 */
	private void resortKeyList()
	{
		Collections.sort(keys, getComparator());
	}

	/**
	 * Sort intern key list.
	 *
	 * @param keySet
	 *            the key set
	 */
	private void sortKeyList(final Set<Object> keySet)
	{
		keys = ListFactory.newArrayList(keySet);
		resortKeyList();
	}

}
