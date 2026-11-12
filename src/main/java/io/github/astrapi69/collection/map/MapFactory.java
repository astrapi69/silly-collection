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

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.functors.InstantiateFactory;
import org.apache.commons.collections4.map.LazyMap;

import io.github.astrapi69.check.Argument;
import io.github.astrapi69.collection.list.ListFactory;

/**
 * The factory class {@link MapFactory} provides factory methods for create new {@link Map} objects
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class MapFactory
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private MapFactory()
	{
	}

	/**
	 * Factory method for {@link java.util.Map} that acts like a javascript associative array.
	 *
	 * This Map is the counterpart for the associative array in js.
	 *
	 * For instance: in js you can create and fetch associative arrays like this:
	 *
	 * <pre>
	 * $arrayObj[0]['firstName'] = 'Albert';
	 * $arrayObj[0]['lastName'] = 'Einstein';
	 * $arrayObj[1]['firstName'] = 'Neil';
	 * $arrayObj[0]['lastName'] = 'Armstrong';
	 * </pre>
	 *
	 * With this method you can do the same like this:
	 *
	 * <pre>
	 * final Map&lt;Integer, Map&lt;String, String&gt;&gt; arrayMap = MapExtensions.newAssosiativeArrayMap();
	 *
	 * arrayMap.get(0).put(&quot;firstName&quot;, &quot;Albert&quot;);
	 * arrayMap.get(0).put(&quot;lastName&quot;, &quot;Einstein&quot;);
	 * arrayMap.get(1).put(&quot;firstName&quot;, &quot;Neil&quot;);
	 * arrayMap.get(1).put(&quot;lastName&quot;, &quot;Armstrong&quot;);
	 * </pre>
	 *
	 * @return The {@link java.util.Map} that acts like a javascript associative array
	 */
	public static Map<Integer, Map<String, String>> newAssosiativeArrayMap()
	{
		return newLazyMap();
	}

	/**
	 * Factory method for create a new {@link ConcurrentHashMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 *
	 * @return The new {@link ConcurrentHashMap}
	 */
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap()
	{
		return new ConcurrentHashMap<>();
	}

	/**
	 * Factory method for create a new {@link ConcurrentHashMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param initialCapacity
	 *            the initial capacity
	 * @return The new {@link ConcurrentHashMap}
	 */
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(final int initialCapacity)
	{
		return new ConcurrentHashMap<>(initialCapacity);
	}

	/**
	 * Factory method for create a new {@link ConcurrentHashMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            the map
	 * @return The new {@link ConcurrentHashMap}
	 */
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(final Map<K, V> map)
	{
		Argument.notNull(map, "map");
		return new ConcurrentHashMap<>(map);
	}

	/**
	 * Factory method for create a map for counting elements of the given collection
	 *
	 * @param <K>
	 *            the generic type of the elements
	 * @param elements
	 *            the elements
	 * @return the new map ready to count elements
	 */
	public static <K> Map<K, Integer> newCounterMap(final Collection<K> elements)
	{
		return newCounterMap(elements, true);
	}

	/**
	 * Factory method for create a map for counting elements of the given collection
	 *
	 * @param <K>
	 *            the generic type of the elements
	 * @param elements
	 *            the elements
	 * @param startByZero
	 *            if this flag is true the first element will start with 0 count
	 * @return the new map ready to count elements
	 */
	public static <K> Map<K, Integer> newCounterMap(final Collection<K> elements,
		boolean startByZero)
	{
		Objects.requireNonNull(elements);
		return newCounterMap(MapFactory.newHashMap(), elements, startByZero);
	}

	/**
	 * Counts the given elements into the given map and returns it
	 *
	 * @param <K>
	 *            the generic type of the elements
	 * @param counterMap
	 *            the counter Map
	 * @param elements
	 *            the elements
	 * @return the given counter map, with the counts of the elements
	 */
	public static <K> Map<K, Integer> newCounterMap(Map<K, Integer> counterMap,
		Collection<K> elements)
	{
		return newCounterMap(counterMap, elements, true);
	}

	/**
	 * Counts the given elements into the given map and returns it
	 *
	 * @param <K>
	 *            the generic type of the elements
	 * @param counterMap
	 *            the counter Map
	 * @param elements
	 *            the elements
	 * @param startByZero
	 *            if this flag is true the first element will start with 0 count
	 * @return the given counter map, with the counts of the elements
	 */
	public static <K> Map<K, Integer> newCounterMap(Map<K, Integer> counterMap,
		Collection<K> elements, boolean startByZero)
	{
		Objects.requireNonNull(counterMap);
		for (K element : elements)
		{
			if (counterMap.containsKey(element))
			{
				counterMap.merge(element, 1, Integer::sum);
				continue;
			}
			if (startByZero)
			{
				counterMap.put(element, 0);
			}
			else
			{
				counterMap.put(element, 1);
			}
		}
		return counterMap;
	}

	/**
	 * Factory method for create a new {@link HashMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 *
	 * @return The new {@link HashMap}
	 */
	public static <K, V> Map<K, V> newHashMap()
	{
		return new HashMap<>();
	}

	/**
	 * Factory method for create a new {@link HashMap} of the given entries, put one by one with
	 * {@link HashMap#put(Object, Object)}. A key given more than once takes the value of its last
	 * entry, and the map keeps no order. An entry may have a null key or a null value; a null entry
	 * throws a {@link NullPointerException}. Pass {@link Map#entry(Object, Object)}, or
	 * {@link java.util.AbstractMap.SimpleEntry} where the key or the value is null
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param entries
	 *            the collection with the entries
	 * @return The new {@link HashMap}
	 * @throws IllegalArgumentException
	 *             if the collection is null
	 */
	public static <K, V> Map<K, V> newHashMap(
		final Collection<? extends Map.Entry<? extends K, ? extends V>> entries)
	{
		Argument.notNull(entries, "entries");
		return putAll(new HashMap<>(), entries);
	}

	/**
	 * Factory method for create a new {@link HashMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param initialCapacity
	 *            the initial capacity
	 * @return The new {@link HashMap}
	 */
	public static <K, V> Map<K, V> newHashMap(final int initialCapacity)
	{
		return new HashMap<>(initialCapacity);
	}

	/**
	 * Factory method for create a new {@link HashMap} of the given entries, as
	 * {@link #newHashMap(Collection)} does
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param entries
	 *            the entries to add in the new {@link HashMap}
	 * @return The new {@link HashMap}
	 * @throws IllegalArgumentException
	 *             if the array is null
	 */
	@SafeVarargs
	public static <K, V> Map<K, V> newHashMap(final Map.Entry<? extends K, ? extends V>... entries)
	{
		Argument.notNull(entries, "entries");
		return newHashMap(Arrays.asList(entries));
	}

	/**
	 * Factory method for create a new {@link HashMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            the map
	 * @return The new {@link HashMap}
	 */
	public static <K, V> Map<K, V> newHashMap(final Map<K, V> map)
	{
		Argument.notNull(map, "map");
		return new HashMap<>(map);
	}

	/**
	 * Factory method for create a new {@link HashMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param keys
	 *            the keys
	 * @param values
	 *            the values
	 * @return The new {@link HashMap}
	 */
	public static <K, V> Map<K, V> newHashMap(final List<K> keys, final List<V> values)
	{
		Argument.notNull(keys, "keys");
		Argument.notNull(values, "values");
		if (keys.size() != values.size())
		{
			throw new IllegalArgumentException(
				"The list of keys and values must have the same size");
		}
		Map<K, V> map = newHashMap();
		for (int i = 0; i < keys.size(); i++)
		{
			map.put(keys.get(i), values.get(i));
		}
		return map;
	}

	/**
	 * Factory method for create a new {@link InsertionOrderMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 *
	 * @return The new {@link InsertionOrderMap}
	 */
	public static <K, V> Map<K, V> newInsertionOrderMap()
	{
		return new InsertionOrderMap<>();
	}

	/**
	 * Factory method for create a new {@link InsertionOrderMap} of the given entries, put one by
	 * one in their order, so the map holds its keys in the order of the entries. A key given more
	 * than once takes the value and the place of its last entry, as
	 * {@link InsertionOrderMap#put(Object, Object)} does. An entry may have a null key or a null
	 * value; a null entry throws a {@link NullPointerException}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param entries
	 *            the collection with the entries, in the order the map takes
	 * @return The new {@link InsertionOrderMap}
	 * @throws IllegalArgumentException
	 *             if the collection is null
	 */
	public static <K, V> Map<K, V> newInsertionOrderMap(
		final Collection<? extends Map.Entry<? extends K, ? extends V>> entries)
	{
		Argument.notNull(entries, "entries");
		return putAll(new InsertionOrderMap<>(), entries);
	}

	/**
	 * Factory method for create a new {@link InsertionOrderMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param initialCapacity
	 *            the initial capacity
	 *
	 * @return The new {@link InsertionOrderMap}
	 */
	public static <K, V> Map<K, V> newInsertionOrderMap(final int initialCapacity)
	{
		return new InsertionOrderMap<>(initialCapacity);
	}

	/**
	 * Factory method for create a new {@link InsertionOrderMap} of the given entries, put one by
	 * one in their order, as {@link #newInsertionOrderMap(Collection)} does
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param entries
	 *            the entries to add in the new {@link InsertionOrderMap}, in the order the map
	 *            takes
	 * @return The new {@link InsertionOrderMap}
	 * @throws IllegalArgumentException
	 *             if the array is null
	 */
	@SafeVarargs
	public static <K, V> Map<K, V> newInsertionOrderMap(
		final Map.Entry<? extends K, ? extends V>... entries)
	{
		Argument.notNull(entries, "entries");
		return newInsertionOrderMap(Arrays.asList(entries));
	}

	/**
	 * Factory method for create a new {@link InsertionOrderMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            the map
	 * @return The new {@link InsertionOrderMap}
	 */
	public static <K, V> Map<K, V> newInsertionOrderMap(final Map<K, V> map)
	{
		Argument.notNull(map, "map");
		return new InsertionOrderMap<>(map);
	}

	/**
	 * Factory method for create a new {@link LazyMap} from commons-collections4 that encapsulates a
	 * {@link HashMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 *
	 * @return The new {@link LazyMap}
	 */
	public static <K, V> Map<K, V> newLazyHashMap()
	{
		return newLazyHashMap(new HashMap<>());
	}

	/**
	 * Factory method for create a new {@link LazyMap} from commons-collections4 that encapsulates a
	 * {@link HashMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            the map to initialize with it
	 * @return The new {@link LazyMap}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <K, V> Map<K, V> newLazyHashMap(HashMap<K, V> map)
	{
		return LazyMap.lazyMap(map, new InstantiateFactory(HashMap.class));
	}

	/**
	 * Factory method for create a new {@link LazyMap} from commons-collections4 that encapsulates a
	 * {@link HashMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 *
	 * @return The new {@link LazyMap}
	 */
	public static <K, V> Map<K, V> newLazyMap()
	{
		return newLazyHashMap();
	}

	/**
	 * Factory method for create a new {@link LazyMap} from commons-collections4 that encapsulates a
	 * {@link TreeMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 *
	 * @return The new {@link LazyMap}
	 */
	public static <K, V> Map<K, V> newLazyTreeMap()
	{
		return newLazyTreeMap(new TreeMap<>());
	}

	/**
	 * Factory method for create a new {@link LazyMap} from commons-collections4 that encapsulates a
	 * {@link TreeMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param comparator
	 *            the comparator
	 *
	 * @return The new {@link LazyMap}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <K, V> Map<K, V> newLazyTreeMap(final Comparator<? super K> comparator)
	{
		Argument.notNull(comparator, "comparator");
		return LazyMap.lazyMap(new TreeMap<K, V>(comparator),
			new InstantiateFactory(TreeMap.class));
	}

	/**
	 * Factory method for create a new {@link LazyMap} from commons-collections4 that encapsulates a
	 * {@link TreeMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            the map
	 * @return The new {@link LazyMap}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <K, V> Map<K, V> newLazyTreeMap(final TreeMap<K, V> map)
	{
		Argument.notNull(map, "map");
		return LazyMap.lazyMap(map, new InstantiateFactory(TreeMap.class));
	}

	/**
	 * Factory method for create a new {@link LinkedHashMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 *
	 * @return The new {@link LinkedHashMap}
	 */
	public static <K, V> Map<K, V> newLinkedHashMap()
	{
		return new LinkedHashMap<>();
	}

	/**
	 * Factory method for create a new {@link LinkedHashMap} of the given entries, put one by one in
	 * their order, so the map holds its keys in the order of the entries. A key given more than
	 * once takes the value of its last entry and keeps the place of its first one, as the insertion
	 * order of a {@link LinkedHashMap} is not affected if a key is re-inserted. An entry may have a
	 * null key or a null value; a null entry throws a {@link NullPointerException}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param entries
	 *            the collection with the entries, in the order the map takes
	 * @return The new {@link LinkedHashMap}
	 * @throws IllegalArgumentException
	 *             if the collection is null
	 */
	public static <K, V> Map<K, V> newLinkedHashMap(
		final Collection<? extends Map.Entry<? extends K, ? extends V>> entries)
	{
		Argument.notNull(entries, "entries");
		return putAll(new LinkedHashMap<>(), entries);
	}

	/**
	 * Factory method for create a new {@link LinkedHashMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param initialCapacity
	 *            the initial capacity
	 *
	 * @return The new {@link LinkedHashMap}
	 */
	public static <K, V> Map<K, V> newLinkedHashMap(final int initialCapacity)
	{
		return new LinkedHashMap<>(initialCapacity);
	}

	/**
	 * Factory method for create a new {@link LinkedHashMap} of the given entries, put one by one in
	 * their order, as {@link #newLinkedHashMap(Collection)} does
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param entries
	 *            the entries to add in the new {@link LinkedHashMap}, in the order the map takes
	 * @return The new {@link LinkedHashMap}
	 * @throws IllegalArgumentException
	 *             if the array is null
	 */
	@SafeVarargs
	public static <K, V> Map<K, V> newLinkedHashMap(
		final Map.Entry<? extends K, ? extends V>... entries)
	{
		Argument.notNull(entries, "entries");
		return newLinkedHashMap(Arrays.asList(entries));
	}

	/**
	 * Factory method for create a new {@link LinkedHashMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            the map
	 * @return The new {@link LinkedHashMap}
	 */
	public static <K, V> Map<K, V> newLinkedHashMap(final Map<K, V> map)
	{
		Argument.notNull(map, "map");
		return new LinkedHashMap<>(map);
	}

	/**
	 * Factory method for create a map for count drawn numbers
	 *
	 * @param minVolume
	 *            the min volume
	 * @param maxVolume
	 *            the max volume
	 * @return the new map with the initial values
	 */
	public static Map<Integer, Integer> newNumberCounterMap(int minVolume, int maxVolume)
	{
		return MapFactory.newCounterMap(ListFactory.newRangeList(minVolume, maxVolume));
	}

	/**
	 * Factory method for create a map for count drawn numbers and will be summarized with the given
	 * Map
	 *
	 * @param minVolume
	 *            the min volume
	 * @param maxVolume
	 *            the max volume
	 * @param numberCounterMap
	 *            the Map that will be summarized
	 * @return the new map with the initial values
	 */
	public static Map<Integer, Integer> newNumberCounterMap(int minVolume, int maxVolume,
		Map<Integer, Integer> numberCounterMap)
	{
		Argument.notNull(numberCounterMap, "numberCounterMap");
		return MapExtensions.mergeAndSummarize(newNumberCounterMap(minVolume, maxVolume),
			numberCounterMap);
	}

	/**
	 * Factory method for create a new {@link TreeMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 *
	 * @return The new {@link TreeMap}
	 */
	public static <K, V> Map<K, V> newTreeMap()
	{
		return new TreeMap<>();
	}

	/**
	 * Factory method for create a new {@link TreeMap} of the given entries, put one by one in the
	 * given order; the map holds them in the natural order of the keys. A key given more than once,
	 * in the sense of {@link Comparable#compareTo(Object)}, keeps the key of its first entry and
	 * takes the value of its last, as {@link TreeMap#put(Object, Object)} does. A key that is null
	 * throws a {@link NullPointerException}, as {@link TreeMap#put(Object, Object)} does without a
	 * comparator; a value may be null. A null entry throws a {@link NullPointerException}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param entries
	 *            the collection with the entries
	 * @return The new {@link TreeMap}
	 * @throws IllegalArgumentException
	 *             if the collection is null
	 */
	public static <K, V> Map<K, V> newTreeMap(
		final Collection<? extends Map.Entry<? extends K, ? extends V>> entries)
	{
		Argument.notNull(entries, "entries");
		return putAll(new TreeMap<>(), entries);
	}

	/**
	 * Factory method for create a new {@link TreeMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param comparator
	 *            the comparator
	 * @return The new {@link TreeMap}
	 */
	public static <K, V> Map<K, V> newTreeMap(final Comparator<? super K> comparator)
	{
		Argument.notNull(comparator, "comparator");
		return new TreeMap<>(comparator);
	}

	/**
	 * Factory method for create a new {@link TreeMap} of the given entries, put one by one in the
	 * given order; the map holds them in the order of the given comparator. A key given more than
	 * once, in the sense of the comparator, keeps the key of its first entry and takes the value of
	 * its last, as {@link TreeMap#put(Object, Object)} does. A null key is possible if the
	 * comparator allows it; a value may be null. A null entry throws a {@link NullPointerException}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param comparator
	 *            the comparator
	 * @param entries
	 *            the collection with the entries
	 * @return The new {@link TreeMap}
	 * @throws IllegalArgumentException
	 *             if the comparator or the collection is null
	 */
	public static <K, V> Map<K, V> newTreeMap(final Comparator<? super K> comparator,
		final Collection<? extends Map.Entry<? extends K, ? extends V>> entries)
	{
		Argument.notNull(comparator, "comparator");
		Argument.notNull(entries, "entries");
		return putAll(new TreeMap<>(comparator), entries);
	}

	/**
	 * Factory method for create a new {@link TreeMap} of the given entries, as
	 * {@link #newTreeMap(Comparator, Collection)} does
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param comparator
	 *            the comparator
	 * @param entries
	 *            the entries to add in the new {@link TreeMap}
	 * @return The new {@link TreeMap}
	 * @throws IllegalArgumentException
	 *             if the comparator or the array is null
	 */
	@SafeVarargs
	public static <K, V> Map<K, V> newTreeMap(final Comparator<? super K> comparator,
		final Map.Entry<? extends K, ? extends V>... entries)
	{
		Argument.notNull(comparator, "comparator");
		Argument.notNull(entries, "entries");
		return newTreeMap(comparator, Arrays.asList(entries));
	}

	/**
	 * Factory method for create a new {@link TreeMap} of the given entries, as
	 * {@link #newTreeMap(Collection)} does
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param entries
	 *            the entries to add in the new {@link TreeMap}
	 * @return The new {@link TreeMap}
	 * @throws IllegalArgumentException
	 *             if the array is null
	 */
	@SafeVarargs
	public static <K, V> Map<K, V> newTreeMap(final Map.Entry<? extends K, ? extends V>... entries)
	{
		Argument.notNull(entries, "entries");
		return newTreeMap(Arrays.asList(entries));
	}

	/**
	 * Factory method for create a new {@link TreeMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            the map
	 * @return The new {@link TreeMap}
	 */
	public static <K, V> Map<K, V> newTreeMap(final Map<K, V> map)
	{
		Argument.notNull(map, "map");
		return new TreeMap<>(map);
	}

	/**
	 * Puts the given entries into the given map one by one, in their order, so a map that keeps the
	 * order of its puts keeps the order of the entries (#115)
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            the map to put the entries in
	 * @param entries
	 *            the entries
	 * @param <M>
	 *            the type of the map
	 * @return the given map
	 */
	private static <K, V, M extends Map<K, V>> M putAll(final M map,
		final Collection<? extends Map.Entry<? extends K, ? extends V>> entries)
	{
		for (final Map.Entry<? extends K, ? extends V> entry : entries)
		{
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}

}
