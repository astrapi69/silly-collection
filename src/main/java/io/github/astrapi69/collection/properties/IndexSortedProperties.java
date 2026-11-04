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
import java.util.function.BiFunction;
import java.util.function.Function;

import io.github.astrapi69.comparator.NullCheckComparator;

/**
 * The class {@link IndexSortedProperties} extends {@link SortedProperties} with an index, so that a
 * value can be read and removed over its position.
 * <p>
 * The index holds exactly the keys of the entries of this object, the keys that
 * {@link #get(Object)} and {@link #keySet()} give, in the order of {@link #keySet()}: sorted with
 * the comparator of this object. The keys of the defaults are not in it, as they are not in
 * {@link #keySet()}. The index follows every change of the keys, also through the views
 * {@link #keySet()}, {@link #entrySet()} and {@link #values()}. The methods {@link #get(int)},
 * {@link #getProperty(int)}, {@link #remove(int)} and {@link #indexOf(Object)} read it.
 *
 * @author Asterios Raptis
 */
public class IndexSortedProperties extends SortedProperties
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	/**
	 * The index: the keys of the entries of this object, sorted with its comparator, built from
	 * them by {@link #sortedKeys()} when it is read. Every method of this class that may change the
	 * keys sets it to null, so that the next read builds it again. The views of {@link #entrySet()}
	 * and {@link #values()} remove entries without such a method, but cannot add any, so an index
	 * of another size than this object is built again, too. The list is replaced, never changed, so
	 * a clone, which starts with the list of the original, has an index of its own. Built from the
	 * keys, it is not part of the serialized form
	 */
	private transient List<Object> keys;

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
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void clear()
	{
		super.clear();
		keys = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Object compute(final Object key,
		final BiFunction<? super Object, ? super Object, ?> remappingFunction)
	{
		final Object value = super.compute(key, remappingFunction);
		keys = null;
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Object computeIfAbsent(final Object key,
		final Function<? super Object, ?> mappingFunction)
	{
		final Object value = super.computeIfAbsent(key, mappingFunction);
		keys = null;
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Object computeIfPresent(final Object key,
		final BiFunction<? super Object, ? super Object, ?> remappingFunction)
	{
		final Object value = super.computeIfPresent(key, remappingFunction);
		keys = null;
		return value;
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
		return getProperty(index);
	}

	/**
	 * Gets the property over the index.
	 *
	 * @param index
	 *            the index
	 * @return the property that was found over the index or null if the index was out of range.
	 */
	public synchronized String getProperty(final int index)
	{
		final List<Object> sortedKeys = sortedKeys();
		if (0 <= index && index < sortedKeys.size())
			return getProperty((String)sortedKeys.get(index));
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
		final List<Object> sortedKeys = sortedKeys();
		for (final Object key : sortedKeys)
		{
			final Object value = getProperty((String)key);
			if (object.equals(value))
			{
				return sortedKeys.indexOf(key);
			}
		}
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Object merge(final Object key, final Object value,
		final BiFunction<? super Object, ? super Object, ?> remappingFunction)
	{
		final Object merged = super.merge(key, value, remappingFunction);
		keys = null;
		return merged;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Object put(final Object key, final Object value)
	{
		final Object previous = super.put(key, value);
		keys = null;
		return previous;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void putAll(final Map<? extends Object, ? extends Object> t)
	{
		super.putAll(t);
		keys = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Object putIfAbsent(final Object key, final Object value)
	{
		final Object previous = super.putIfAbsent(key, value);
		keys = null;
		return previous;
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
		final List<Object> sortedKeys = sortedKeys();
		if (0 <= index && index < sortedKeys.size())
			return remove(sortedKeys.get(index));
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Object remove(final Object key)
	{
		final Object removed = super.remove(key);
		keys = null;
		return removed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized boolean remove(final Object key, final Object value)
	{
		final boolean removed = super.remove(key, value);
		keys = null;
		return removed;
	}

	/**
	 * Gets the index, and builds it from the keys of the entries of this object first if a change
	 * of the keys dropped it or if its size differs from the number of entries. Its callers hold
	 * the lock of this object
	 *
	 * @return the keys of the entries of this object, sorted with its comparator
	 */
	private List<Object> sortedKeys()
	{
		// a dropped index counts as one of another size than this object
		final int indexed = keys == null ? -1 : keys.size();
		if (indexed != size())
		{
			keys = Collections.unmodifiableList(Collections.list(keys()));
		}
		return keys;
	}

}
