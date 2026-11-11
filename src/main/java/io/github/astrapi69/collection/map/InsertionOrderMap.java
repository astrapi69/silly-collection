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
package io.github.astrapi69.collection.map;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * The class {@link InsertionOrderMap} is a <code>LinkedHashMap</code> in which every write moves
 * the key to the end. It differs from the insertion order of <code>LinkedHashMap</code>, where
 * "encounter order is not affected if a key is re-inserted into the map with the put method", in
 * that a key that is written again comes last; and from its access order, which moves a key also
 * when it is read ({@link #get(Object)}, <code>getOrDefault</code>), in that a read moves nothing.
 * <p>
 * A write is any call that stores a value for the key, also the value it already has:
 * {@link #put(Object, Object)}, {@link #putAll(Map)} (which calls put for each mapping),
 * {@link #putIfAbsent(Object, Object)} and
 * {@link #computeIfAbsent(Object, java.util.function.Function)} (which store for a new key or a key
 * that is mapped to null), {@link #compute(Object, BiFunction)},
 * {@link #computeIfPresent(Object, BiFunction)}, {@link #merge(Object, Object, BiFunction)},
 * {@link #replace(Object, Object)}, {@link #replace(Object, Object, Object)} (when it replaces) and
 * {@link #replaceAll(BiFunction)}. The key moves to the end. A call that stores nothing moves
 * nothing: <code>putIfAbsent</code> and <code>computeIfAbsent</code> of a key with a value, a
 * <code>replace</code> whose old value does not match, <code>computeIfPresent</code> of a key that
 * is not there or is mapped to null. A function that gives null removes the key
 * (<code>compute</code>, <code>computeIfPresent</code>, <code>merge</code>) or stores nothing
 * (<code>computeIfAbsent</code>). <code>replaceAll</code> writes every entry, so every key moves to
 * the end in turn, and the order stays as it was, also if the function changes only some values.
 * </p>
 * <p>
 * The exception to the rule is {@link java.util.Map.Entry#setValue(Object)}: it changes the value
 * and does not move the key, through the stream and the iterator of {@link #entrySet()}, and
 * through the views <code>sequencedEntrySet()</code>, <code>reversed().entrySet()</code> and
 * <code>reversed().sequencedEntrySet()</code>. The reason is that the iteration stays stable: as in
 * the access-order mode of <code>LinkedHashMap</code>, where <code>setValue</code> is not among the
 * methods that generate an entry access ("No other methods generate entry accesses"), moving an
 * entry during an iteration would break it.
 * </p>
 * <p>
 * <code>putFirst</code> and <code>putLast</code> follow the contract of <code>SequencedMap</code>
 * (an explicit position): the key comes first or last, and the rule of the end does not apply to
 * them.
 * </p>
 * <p>
 * A write through the view <code>reversed()</code> behaves like the corresponding method of this
 * map: the key moves to the end of this map, which is the first of the reversed view; only
 * <code>putFirst</code> and <code>putLast</code> of the view position the key in the view, so
 * <code>reversed().putFirst</code> makes it the last of this map. The views {@link #keySet()} and
 * {@link #values()} take no writes but removals.
 * </p>
 * 
 * @author Asterios Raptis
 * @version 1.0
 * @param <K>
 *            the generic type of keys maintained by this map
 * @param <V>
 *            the generic type of mapped values
 * @see java.util.LinkedHashMap
 */
public class InsertionOrderMap<K, V> extends LinkedHashMap<K, V>
{

	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = -3585706680928306464L;

	/**
	 * Constructs an empty insertion-ordered {@link InsertionOrderMap} instance with a default
	 * capacity (16) and load factor (0.75).
	 */
	public InsertionOrderMap()
	{
	}

	/**
	 * Constructs an empty insertion-ordered {@link InsertionOrderMap} instance with the specified
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
	 * Constructs an empty insertion-ordered {@link InsertionOrderMap} instance with the specified
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
	 * Constructs an empty {@link InsertionOrderMap} instance with the specified initial capacity,
	 * load factor and ordering mode.
	 * 
	 * @param initialCapacity
	 *            the initial capacity.
	 * @param loadFactor
	 *            the load factor.
	 * @param accessOrder
	 *            the ordering mode - <code>true</code> for access-order, <code>false</code> for
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
	 * Constructs an insertion-ordered {@link InsertionOrderMap} instance with the same mappings as
	 * the specified map. The <code>InsertionOrderMap</code> instance is created with a a default
	 * load factor (0.75) and an initial capacity sufficient to hold the mappings in the specified
	 * map.
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
	 * @return previous value associated with specified key, or <code>null</code> if there was no
	 *         mapping for key. A <code>null</code> return can also indicate that the HashMap
	 *         previously associated <code>null</code> with the specified key.
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

	/**
	 * Copies all of the mappings from the specified map to this map. As the contract of
	 * {@link Map#putAll(Map)} says, the effect is that of calling {@link #put(Object, Object)} once
	 * for each mapping of the specified map, in its order: a key this map holds already takes the
	 * new value and moves to the end, a new key comes last (#127).
	 *
	 * @param m
	 *            the mappings to be stored in this map
	 * @throws NullPointerException
	 *             if the specified map is null
	 */
	@Override
	public void putAll(final Map<? extends K, ? extends V> m)
	{
		for (final Map.Entry<? extends K, ? extends V> entry : m.entrySet())
		{
			put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Moves the given key to the end, with the given value, as {@link #put(Object, Object)} does
	 * for a key that is there
	 *
	 * @param key
	 *            the key, which is there
	 * @param value
	 *            the value of the key
	 */
	private void moveToEnd(final K key, final V value)
	{
		super.remove(key);
		super.put(key, value);
	}

	/**
	 * If the specified key is there, the new value is the result of the given function of the old
	 * value and the given value, and the key moves to the end, as {@link #put(Object, Object)}
	 * does; if the function gives null the mapping is removed. A key that is not there comes last.
	 * Otherwise as {@link LinkedHashMap#merge(Object, Object, BiFunction)}.
	 *
	 * @param key
	 *            key with which the resulting value is to be associated
	 * @param value
	 *            the non-null value to be merged with the existing value
	 * @param remappingFunction
	 *            the function to recompute a value if present
	 * @return the new value associated with the specified key, or null if no value is associated
	 *         with the key
	 * @throws NullPointerException
	 *             if the value or the remapping function is null
	 */
	@Override
	public V merge(final K key, final V value,
		final BiFunction<? super V, ? super V, ? extends V> remappingFunction)
	{
		final boolean contained = super.containsKey(key);
		final V result = super.merge(key, value, remappingFunction);
		if (contained && result != null)
		{
			moveToEnd(key, result);
		}
		return result;
	}

	/**
	 * If the specified key is not there or is mapped to null, associates it with the given value,
	 * and a key that was mapped to null moves to the end, as {@link #put(Object, Object)} does.
	 * Otherwise as {@link LinkedHashMap#putIfAbsent(Object, Object)}.
	 *
	 * @param key
	 *            key with which the specified value is to be associated
	 * @param value
	 *            value to be associated with the specified key
	 * @return the previous value associated with the specified key, or null if there was no mapping
	 *         for the key or it was mapped to null
	 */
	@Override
	public V putIfAbsent(final K key, final V value)
	{
		final boolean mappedToNull = super.containsKey(key) && super.get(key) == null;
		final V previous = super.putIfAbsent(key, value);
		if (mappedToNull && value != null)
		{
			moveToEnd(key, value);
		}
		return previous;
	}

	/**
	 * If the specified key is not there or is mapped to null, attempts to compute its value, and a
	 * key that was mapped to null and takes a value moves to the end, as
	 * {@link #put(Object, Object)} does; a function that gives null changes nothing. Otherwise as
	 * {@link LinkedHashMap#computeIfAbsent(Object, java.util.function.Function)}.
	 *
	 * @param key
	 *            key with which the specified value is to be associated
	 * @param mappingFunction
	 *            the function to compute a value
	 * @return the current (existing or computed) value associated with the specified key, or null
	 *         if the computed value is null
	 */
	@Override
	public V computeIfAbsent(final K key,
		final java.util.function.Function<? super K, ? extends V> mappingFunction)
	{
		final boolean mappedToNull = super.containsKey(key) && super.get(key) == null;
		final V result = super.computeIfAbsent(key, mappingFunction);
		if (mappedToNull && result != null)
		{
			moveToEnd(key, result);
		}
		return result;
	}

	/**
	 * Attempts to compute a mapping for the specified key and its current value. If the key is
	 * there and the mapping is not removed, the key moves to the end, as
	 * {@link #put(Object, Object)} does; a key that is not there comes last. Otherwise as
	 * {@link LinkedHashMap#compute(Object, BiFunction)}.
	 *
	 * @param key
	 *            key with which the specified value is to be associated
	 * @param remappingFunction
	 *            the function to compute a value
	 * @return the new value associated with the specified key, or null if none
	 */
	@Override
	public V compute(final K key,
		final BiFunction<? super K, ? super V, ? extends V> remappingFunction)
	{
		final boolean contained = super.containsKey(key);
		final V result = super.compute(key, remappingFunction);
		if (contained && result != null)
		{
			moveToEnd(key, result);
		}
		return result;
	}

	/**
	 * If the value for the specified key is there and non-null, computes a new mapping and moves
	 * the key to the end, as {@link #put(Object, Object)} does; if the function gives null the
	 * mapping is removed. Otherwise as {@link LinkedHashMap#computeIfPresent(Object, BiFunction)}.
	 *
	 * @param key
	 *            key with which a value may be associated
	 * @param remappingFunction
	 *            the function to compute a value
	 * @return the new value associated with the specified key, or null if none
	 */
	@Override
	public V computeIfPresent(final K key,
		final BiFunction<? super K, ? super V, ? extends V> remappingFunction)
	{
		final V result = super.computeIfPresent(key, remappingFunction);
		if (result != null)
		{
			moveToEnd(key, result);
		}
		return result;
	}

	/**
	 * Replaces the entry for the specified key only if it is currently mapped to some value, and
	 * moves the key to the end, as {@link #put(Object, Object)} does.
	 *
	 * @param key
	 *            key with which the specified value is associated
	 * @param value
	 *            value to be associated with the specified key
	 * @return the previous value associated with the specified key, or null if there was no mapping
	 *         for the key
	 */
	@Override
	public V replace(final K key, final V value)
	{
		final boolean contained = super.containsKey(key);
		final V previous = super.replace(key, value);
		if (contained)
		{
			moveToEnd(key, value);
		}
		return previous;
	}

	/**
	 * Replaces the entry for the specified key only if currently mapped to the specified value, and
	 * then moves the key to the end, as {@link #put(Object, Object)} does; if the value is not
	 * replaced the order does not change.
	 *
	 * @param key
	 *            key with which the specified value is associated
	 * @param oldValue
	 *            value expected to be associated with the specified key
	 * @param newValue
	 *            value to be associated with the specified key
	 * @return true if the value was replaced
	 */
	@Override
	public boolean replace(final K key, final V oldValue, final V newValue)
	{
		final boolean replaced = super.replace(key, oldValue, newValue);
		if (replaced)
		{
			moveToEnd(key, newValue);
		}
		return replaced;
	}

}
