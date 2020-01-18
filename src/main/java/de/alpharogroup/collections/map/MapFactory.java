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
package de.alpharogroup.collections.map;

import de.alpharogroup.collections.list.ListFactory;
import de.alpharogroup.collections.pairs.KeyValuePair;
import lombok.NonNull;
import org.apache.commons.collections4.functors.InstantiateFactory;
import org.apache.commons.collections4.map.LazyMap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The factory class {@link MapFactory} provides factory methods for create new {@link Map} objects
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public final class MapFactory
{

    private MapFactory() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
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
	public static <K> Map<K, Integer> newCounterMap(final @NonNull Collection<K> elements)
	{
		Map<K, Integer> elementsCount = MapFactory.newHashMap();
		for (K element : elements)
		{
			if (elementsCount.containsKey(element))
			{
				elementsCount.merge(element, 1, Integer::sum);
				continue;
			}
			elementsCount.put(element, 0);
		}
		return elementsCount;
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
	public static <K, V> Map<K, V> newInsertionOrderMap(final @NonNull Map<K, V> map)
	{
		return new InsertionOrderMap<>(map);
	}

	/**
	 * Factory method for create a new {@link InsertionOrderMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param keyValuePairs
	 *            the collection with the key value pairs
	 * @return The new {@link InsertionOrderMap}
	 */
	public static <K, V> Map<K, V> newInsertionOrderMap(
            final @NonNull Collection<KeyValuePair<K, V>> keyValuePairs)
	{
		return newInsertionOrderMap(KeyValuePair.toMap(keyValuePairs));
	}

	/**
	 * Factory method for create a new {@link InsertionOrderMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param keyValuePairs
	 *            the key value pairs to add in the new {@link InsertionOrderMap}
	 * @return The new {@link InsertionOrderMap}
	 */
	@SafeVarargs
	public static <K, V> Map<K, V> newInsertionOrderMap(
            final @NonNull KeyValuePair<K, V>... keyValuePairs)
	{
		return newInsertionOrderMap(ListFactory.newArrayList(keyValuePairs));
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
	 * @param map
	 *            the map
	 * @return The new {@link ConcurrentHashMap}
	 */
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(final @NonNull Map<K, V> map)
	{
		return new ConcurrentHashMap<>(map);
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
	 * @param map
	 *            the map
	 * @return The new {@link HashMap}
	 */
	public static <K, V> Map<K, V> newHashMap(final @NonNull Map<K, V> map)
	{
		return new HashMap<>(map);
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
	public static <K, V> Map<K, V> newHashMap(
            final @NonNull Collection<KeyValuePair<K, V>> keyValuePairs)
	{
		return newHashMap(KeyValuePair.toMap(keyValuePairs));
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
	public static <K, V> Map<K, V> newHashMap(final @NonNull KeyValuePair<K, V>... keyValuePairs)
	{
		return newHashMap(ListFactory.newArrayList(keyValuePairs));
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
	public static <K, V> Map<K, V> newLinkedHashMap(final @NonNull Map<K, V> map)
	{
		return new LinkedHashMap<>(map);
	}

	/**
	 * Factory method for create a new {@link LinkedHashMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param keyValuePairs
	 *            the collection with the key value pairs
	 * @return The new {@link LinkedHashMap}
	 */
	public static <K, V> Map<K, V> newLinkedHashMap(
            final @NonNull Collection<KeyValuePair<K, V>> keyValuePairs)
	{
		return newLinkedHashMap(KeyValuePair.toMap(keyValuePairs));
	}

	/**
	 * Factory method for create a new {@link LinkedHashMap}
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param keyValuePairs
	 *            the key value pairs to add in the new {@link LinkedHashMap}
	 * @return The new {@link LinkedHashMap}
	 */
	@SafeVarargs
	public static <K, V> Map<K, V> newLinkedHashMap(
            final @NonNull KeyValuePair<K, V>... keyValuePairs)
	{
		return newLinkedHashMap(ListFactory.newArrayList(keyValuePairs));
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
		return newLazyHashMap(new HashMap<K, V>());
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
		return newLazyTreeMap(new TreeMap<K, V>());
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
	public static <K, V> Map<K, V> newLazyTreeMap(final @NonNull TreeMap<K, V> map)
	{
		return LazyMap.lazyMap(map, new InstantiateFactory(TreeMap.class));
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
	public static <K, V> Map<K, V> newLazyTreeMap(final @NonNull Comparator<? super K> comparator)
	{
		return LazyMap.lazyMap(new TreeMap<K, V>(comparator),
			new InstantiateFactory(TreeMap.class));
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
	 * @param map
	 *            the map
	 * @return The new {@link TreeMap}
	 */
	public static <K, V> Map<K, V> newTreeMap(final @NonNull Map<K, V> map)
	{
		return new TreeMap<>(map);
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
	public static <K, V> Map<K, V> newTreeMap(final @NonNull Comparator<? super K> comparator)
	{
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
	public static <K, V> Map<K, V> newTreeMap(final @NonNull Comparator<? super K> comparator,
                                              final @NonNull Collection<KeyValuePair<K, V>> keyValuePairs)
	{
		TreeMap<K, V> treeMap = new TreeMap<>(comparator);
		treeMap.putAll(newTreeMap(keyValuePairs));
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
	public static <K, V> Map<K, V> newTreeMap(final @NonNull Comparator<? super K> comparator,
                                              final @NonNull KeyValuePair<K, V>... keyValuePairs)
	{
		TreeMap<K, V> treeMap = new TreeMap<>(comparator);
		treeMap.putAll(newTreeMap(keyValuePairs));
		return treeMap;
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
	public static <K, V> Map<K, V> newTreeMap(
            final @NonNull Collection<KeyValuePair<K, V>> keyValuePairs)
	{
		return newTreeMap(KeyValuePair.toMap(keyValuePairs));
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
	public static <K, V> Map<K, V> newTreeMap(final @NonNull KeyValuePair<K, V>... keyValuePairs)
	{
		return newTreeMap(ListFactory.newArrayList(keyValuePairs));
	}

}
