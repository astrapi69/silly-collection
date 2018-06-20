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

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.functors.InstantiateFactory;
import org.apache.commons.collections4.map.LazyMap;

import lombok.experimental.UtilityClass;

/**
 * The factory class {@link MapFactory} provides factory methods for create new {@link Map} objects
 *
 * @version 1.0
 * @author Asterios Raptis
 */
@UtilityClass
public final class MapFactory
{

	/**
	 * Factory method for create a new {@link InsertionOrderMap}.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 *
	 * @return The new {@link InsertionOrderMap}.
	 */
	public static <K, V> Map<K, V> newInsertionOrderMap()
	{
		return new InsertionOrderMap<>();
	}

	/**
	 * Factory method for create a new {@link InsertionOrderMap}.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param initialCapacity
	 *            the initial capacity
	 *
	 * @return The new {@link InsertionOrderMap}.
	 */
	public static <K, V> Map<K, V> newInsertionOrderMap(final int initialCapacity)
	{
		return new InsertionOrderMap<>(initialCapacity);
	}

	/**
	 * Factory method for create a new {@link InsertionOrderMap}.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            the map
	 * @return The new {@link InsertionOrderMap}.
	 */
	public static <K, V> Map<K, V> newInsertionOrderMap(Map<K, V> map)
	{
		return new InsertionOrderMap<>(map);
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
	 * @return The {@link java.util.Map} that acts like a javascript associative array.
	 */
	public static Map<Integer, Map<String, String>> newAssosiativeArrayMap()
	{
		return newLazyMap();
	}

	/**
	 * Factory method for create a new {@link ConcurrentHashMap}.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 *
	 * @return The new {@link ConcurrentHashMap}.
	 */
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap()
	{
		return new ConcurrentHashMap<>();
	}

	/**
	 * Factory method for create a new {@link ConcurrentHashMap}.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            the map
	 * @return The new {@link ConcurrentHashMap}.
	 */
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(Map<K, V> map)
	{
		return new ConcurrentHashMap<>(map);
	}

	/**
	 * Factory method for create a new {@link ConcurrentHashMap}.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param initialCapacity
	 *            the initial capacity
	 * @return The new {@link ConcurrentHashMap}.
	 */
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(final int initialCapacity)
	{
		return new ConcurrentHashMap<>(initialCapacity);
	}

	/**
	 * Factory method for create a new {@link HashMap}.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 *
	 * @return The new {@link HashMap}.
	 */
	public static <K, V> Map<K, V> newHashMap()
	{
		return new HashMap<>();
	}

	/**
	 * Factory method for create a new {@link HashMap}.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            the map
	 * @return The new {@link HashMap}.
	 */
	public static <K, V> Map<K, V> newHashMap(Map<K, V> map)
	{
		return new HashMap<>(map);
	}

	/**
	 * Factory method for create a new {@link LinkedHashMap}.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 *
	 * @return The new {@link LinkedHashMap}.
	 */
	public static <K, V> Map<K, V> newLinkedHashMap()
	{
		return new LinkedHashMap<>();
	}

	/**
	 * Factory method for create a new {@link LinkedHashMap}.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            the map
	 * @return The new {@link LinkedHashMap}.
	 */
	public static <K, V> Map<K, V> newLinkedHashMap(Map<K, V> map)
	{
		return new LinkedHashMap<>(map);
	}

	/**
	 * Factory method for create a new {@link LinkedHashMap}.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param initialCapacity
	 *            the initial capacity
	 *
	 * @return The new {@link LinkedHashMap}.
	 */
	public static <K, V> Map<K, V> newLinkedHashMap(final int initialCapacity)
	{
		return new LinkedHashMap<>(initialCapacity);
	}

	/**
	 * Factory method for create a new {@link HashMap}.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param initialCapacity
	 *            the initial capacity
	 * @return The new {@link HashMap}.
	 */
	public static <K, V> Map<K, V> newHashMap(final int initialCapacity)
	{
		return new HashMap<>(initialCapacity);
	}

	/**
	 * Factory method for create a new {@link LazyMap} from commons-collections4 that encapsulates a
	 * {@link HashMap}.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 *
	 * @return The new {@link LazyMap}.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <K, V> Map<K, V> newLazyMap()
	{
		return LazyMap.lazyMap(new HashMap<K, V>(), new InstantiateFactory(HashMap.class));
	}

	/**
	 * Factory method for create a new {@link LazyMap} from commons-collections4 that encapsulates a
	 * {@link TreeMap}.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 *
	 * @return The new {@link LazyMap}.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <K, V> Map<K, V> newLazyTreeMap()
	{
		return LazyMap.lazyMap(new TreeMap<K, V>(), new InstantiateFactory(TreeMap.class));
	}

	/**
	 * Factory method for create a new {@link LazyMap} from commons-collections4 that encapsulates a
	 * {@link TreeMap}.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param comparator
	 *            the comparator
	 *
	 * @return The new {@link LazyMap}.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <K, V> Map<K, V> newLazyTreeMap(final Comparator<? super K> comparator)
	{
		return LazyMap.lazyMap(new TreeMap<K, V>(comparator),
			new InstantiateFactory(TreeMap.class));
	}

	/**
	 * Factory method for create a new {@link TreeMap}.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 *
	 * @return The new {@link TreeMap}.
	 */
	public static <K, V> Map<K, V> newTreeMap()
	{
		return new TreeMap<>();
	}

	/**
	 * Factory method for create a new {@link TreeMap}.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            the map
	 * @return The new {@link TreeMap}.
	 */
	public static <K, V> Map<K, V> newTreeMap(Map<K, V> map)
	{
		return new TreeMap<>(map);
	}

	/**
	 * Factory method for create a new {@link TreeMap}.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param comparator
	 *            the comparator
	 * @return The new {@link TreeMap}.
	 */
	public static <K, V> Map<K, V> newTreeMap(final Comparator<? super K> comparator)
	{
		return new TreeMap<>(comparator);
	}

}
