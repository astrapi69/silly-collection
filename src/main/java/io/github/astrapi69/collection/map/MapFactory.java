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
import io.github.astrapi69.collection.pair.KeyValuePair;

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
	 * Factory method for create a map for and count elements of the given collection
	 *
	 * @param <K>
	 *            the generic type of the elements
	 * @param counterMap
	 *            the counter Map
	 * @param elements
	 *            the elements
	 * @return the new map ready to count elements
	 */
	public static <K> Map<K, Integer> newCounterMap(Map<K, Integer> counterMap,
		Collection<K> elements)
	{
		return newCounterMap(counterMap, elements, true);
	}

	/**
	 * Factory method for create a map for and count elements of the given collection
	 *
	 * @param <K>
	 *            the generic type of the elements
	 * @param counterMap
	 *            the counter Map
	 * @param elements
	 *            the elements
	 * @param startByZero
	 *            if this flag is true the first element will start with 0 count
	 * @return the new map ready to count elements
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
	 * Factory method for create a new {@link HashMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param keyValuePairs
	 *            the collection with the key value pairs
	 * @return The new {@link HashMap}
	 */
	public static <K, V> Map<K, V> newHashMap(final Collection<KeyValuePair<K, V>> keyValuePairs)
	{
		Argument.notNull(keyValuePairs, "keyValuePairs");
		return newHashMap(KeyValuePair.toMap(keyValuePairs));
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
	 * Factory method for create a new {@link HashMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param keyValuePairs
	 *            the key value pairs to add in the new {@link HashMap}
	 * @return The new {@link HashMap}
	 */
	@SafeVarargs
	public static <K, V> Map<K, V> newHashMap(final KeyValuePair<K, V>... keyValuePairs)
	{
		Argument.notNull(keyValuePairs, "keyValuePairs");
		return newHashMap(ListFactory.newArrayList(keyValuePairs));
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
	 * Factory method for create a new {@link InsertionOrderMap} of the given key value pairs, put
	 * one by one in their order, so the map holds its keys in the order of the pairs. A key given
	 * more than once takes the value and the place of its last pair, as
	 * {@link InsertionOrderMap#put(Object, Object)} does
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param keyValuePairs
	 *            the collection with the key value pairs, in the order the map takes
	 * @return The new {@link InsertionOrderMap}
	 */
	public static <K, V> Map<K, V> newInsertionOrderMap(
		final Collection<KeyValuePair<K, V>> keyValuePairs)
	{
		Argument.notNull(keyValuePairs, "keyValuePairs");
		return putAll(new InsertionOrderMap<>(), keyValuePairs);
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
	 * Factory method for create a new {@link InsertionOrderMap} of the given key value pairs, put
	 * one by one in their order, as {@link #newInsertionOrderMap(Collection)} does
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param keyValuePairs
	 *            the key value pairs to add in the new {@link InsertionOrderMap}, in the order the
	 *            map takes
	 * @return The new {@link InsertionOrderMap}
	 */
	@SafeVarargs
	public static <K, V> Map<K, V> newInsertionOrderMap(final KeyValuePair<K, V>... keyValuePairs)
	{
		Argument.notNull(keyValuePairs, "keyValuePairs");
		return newInsertionOrderMap(ListFactory.newArrayList(keyValuePairs));
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
	 * Factory method for create a new {@link LinkedHashMap} of the given key value pairs, put one
	 * by one in their order, so the map holds its keys in the order of the pairs. A key given more
	 * than once takes the value of its last pair and keeps the place of its first one, as the
	 * insertion order of a {@link LinkedHashMap} is not affected if a key is re-inserted
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param keyValuePairs
	 *            the collection with the key value pairs, in the order the map takes
	 * @return The new {@link LinkedHashMap}
	 */
	public static <K, V> Map<K, V> newLinkedHashMap(
		final Collection<KeyValuePair<K, V>> keyValuePairs)
	{
		Argument.notNull(keyValuePairs, "keyValuePairs");
		return putAll(new LinkedHashMap<>(), keyValuePairs);
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
	 * Factory method for create a new {@link LinkedHashMap} of the given key value pairs, put one
	 * by one in their order, as {@link #newLinkedHashMap(Collection)} does
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param keyValuePairs
	 *            the key value pairs to add in the new {@link LinkedHashMap}, in the order the map
	 *            takes
	 * @return The new {@link LinkedHashMap}
	 */
	@SafeVarargs
	public static <K, V> Map<K, V> newLinkedHashMap(final KeyValuePair<K, V>... keyValuePairs)
	{
		Argument.notNull(keyValuePairs, "keyValuePairs");
		return newLinkedHashMap(ListFactory.newArrayList(keyValuePairs));
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
	 * Factory method for create a new {@link TreeMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param keyValuePairs
	 *            the collection with the key value pairs
	 * @return The new {@link TreeMap}
	 */
	public static <K, V> Map<K, V> newTreeMap(final Collection<KeyValuePair<K, V>> keyValuePairs)
	{
		Argument.notNull(keyValuePairs, "keyValuePairs");
		return newTreeMap(KeyValuePair.toMap(keyValuePairs));
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
	 * Factory method for create a new {@link TreeMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param comparator
	 *            the comparator
	 * @param keyValuePairs
	 *            the key value pairs
	 * @return The new {@link TreeMap}
	 */
	public static <K, V> Map<K, V> newTreeMap(final Comparator<? super K> comparator,
		final Collection<KeyValuePair<K, V>> keyValuePairs)
	{
		Argument.notNull(comparator, "comparator");
		Argument.notNull(keyValuePairs, "keyValuePairs");
		TreeMap<K, V> treeMap = new TreeMap<>(comparator);
		for (final KeyValuePair<K, V> keyValuePair : keyValuePairs)
		{
			treeMap.put(keyValuePair.getKey(), keyValuePair.getValue());
		}
		return treeMap;
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
	 * @param keyValuePairs
	 *            the key value pairs to add in the new {@link TreeMap}
	 * @return The new {@link TreeMap}
	 */
	@SafeVarargs
	public static <K, V> Map<K, V> newTreeMap(final Comparator<? super K> comparator,
		final KeyValuePair<K, V>... keyValuePairs)
	{
		Argument.notNull(comparator, "comparator");
		Argument.notNull(keyValuePairs, "keyValuePairs");
		return newTreeMap(comparator, ListFactory.newArrayList(keyValuePairs));
	}

	/**
	 * Factory method for create a new {@link TreeMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param keyValuePairs
	 *            the key value pairs to add in the new {@link TreeMap}
	 * @return The new {@link TreeMap}
	 */
	@SafeVarargs
	public static <K, V> Map<K, V> newTreeMap(final KeyValuePair<K, V>... keyValuePairs)
	{
		Argument.notNull(keyValuePairs, "keyValuePairs");
		return newTreeMap(ListFactory.newArrayList(keyValuePairs));
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
	 * Puts the given key value pairs into the given map one by one, in their order, so a map that
	 * keeps the order of its puts keeps the order of the pairs (#115)
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            the map to put the pairs in
	 * @param keyValuePairs
	 *            the key value pairs
	 * @return the given map
	 */
	private static <K, V> Map<K, V> putAll(final Map<K, V> map,
		final Collection<KeyValuePair<K, V>> keyValuePairs)
	{
		for (final KeyValuePair<K, V> keyValuePair : keyValuePairs)
		{
			map.put(keyValuePair.getKey(), keyValuePair.getValue());
		}
		return map;
	}

}
