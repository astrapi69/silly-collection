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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class overwrites the put-method from the <tt>LinkedHashMap</tt>. That inserts the value to
 * the right order it was inserted in the Map. Note that the difference in the LinkedHashMap is the
 * order does not change if we put the same key with a new value. In this class the order changes
 * when we put a new value with the same key.
 * 
 * @author Asterios Raptis
 * @version 1.0
 * @param <K>
 *            the type of keys maintained by this map
 * @param <V>
 *            the type of mapped values
 * @see java.util.LinkedHashMap
 */
public class InsertionOrderMap<K, V> extends LinkedHashMap<K, V>
{

	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = -3585706680928306464L;

	/**
	 * Constructs an empty insertion-ordered <tt>InsertionOrderMap</tt> instance with a default
	 * capacity (16) and load factor (0.75).
	 */
	public InsertionOrderMap()
	{
		super();
	}

	/**
	 * Constructs an empty insertion-ordered <tt>InsertionOrderMap</tt> instance with the specified
	 * initial capacity and a default load factor (0.75).
	 * 
	 * @param initialCapacity
	 *            the initial capacity.
	 * @throws IllegalArgumentException
	 *             if the initial capacity is negative.
	 */
	public InsertionOrderMap(final int initialCapacity)
	{
		super(initialCapacity);
	}

	/**
	 * Constructs an empty insertion-ordered <tt>InsertionOrderMap</tt> instance with the specified
	 * initial capacity and load factor.
	 * 
	 * @param initialCapacity
	 *            the initial capacity.
	 * @param loadFactor
	 *            the load factor.
	 * @throws IllegalArgumentException
	 *             if the initial capacity is negative or the load factor is nonpositive.
	 */
	public InsertionOrderMap(final int initialCapacity, final float loadFactor)
	{
		super(initialCapacity, loadFactor);
	}

	/**
	 * Constructs an empty <tt>InsertionOrderMap</tt> instance with the specified initial capacity,
	 * load factor and ordering mode.
	 * 
	 * @param initialCapacity
	 *            the initial capacity.
	 * @param loadFactor
	 *            the load factor.
	 * @param accessOrder
	 *            the ordering mode - <tt>true</tt> for access-order, <tt>false</tt> for
	 *            insertion-order.
	 * @throws IllegalArgumentException
	 *             if the initial capacity is negative or the load factor is nonpositive.
	 */
	public InsertionOrderMap(final int initialCapacity, final float loadFactor,
		final boolean accessOrder)
	{
		super(initialCapacity, loadFactor, accessOrder);
	}

	/**
	 * Constructs an insertion-ordered <tt>InsertionOrderMap</tt> instance with the same mappings as
	 * the specified map. The <tt>InsertionOrderMap</tt> instance is created with a a default load
	 * factor (0.75) and an initial capacity sufficient to hold the mappings in the specified map.
	 * 
	 * @param m
	 *            the map whose mappings are to be placed in this map.
	 * @throws NullPointerException
	 *             if the specified map is null.
	 */
	public InsertionOrderMap(final Map<? extends K, ? extends V> m)
	{
		super(m);
	}

	/**
	 * Associates the specified value with the specified key in this map. If the map previously
	 * contained a mapping for this key, the old value is removed and then insert new with the same
	 * key and the new value. That inserts the value to the right order it was inserted in the Map.
	 * Note that the difference in the LinkedHashMap is the order does not change if we put the same
	 * key with a new value. In this class the order changes when we put a new value with the same
	 * key.
	 * 
	 * @param key
	 *            key with which the specified value is to be associated.
	 * @param value
	 *            value to be associated with the specified key.
	 * @return previous value associated with specified key, or <tt>null</tt> if there was no
	 *         mapping for key. A <tt>null</tt> return can also indicate that the HashMap previously
	 *         associated <tt>null</tt> with the specified key.
	 */
	@Override
	public V put(final K key, final V value)
	{
		V retObj = null;
		if (super.containsKey(key))
		{
			retObj = super.remove(key);
			super.put(key, value);
		}
		else
		{
			retObj = super.put(key, value);
		}
		return retObj;
	}

}
