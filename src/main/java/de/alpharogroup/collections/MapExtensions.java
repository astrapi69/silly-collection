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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.functors.InstantiateFactory;
import org.apache.commons.collections4.map.LazyMap;

/**
 * Extensions class for use with Map objects.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class MapExtensions
{

	/**
	 * Returns the first founded key from the given value or null if nothing is found.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            The Map.
	 * @param value
	 *            The value.
	 * @return Returns the first key from the given value or null if nothing found.
	 */
	public static <K, V> K getKeyFromValue(final Map<K, V> map, final V value)
	{
		K key = null;
		for (final Entry<K, V> entry : map.entrySet())
		{
			key = entry.getKey();
			if (map.get(key).equals(value))
			{
				break;
			}
		}
		return key;
	}

	/**
	 * Returns a Collection from all founded keys from the given value or null if nothing found.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            The Map.
	 * @param value
	 *            The value.
	 * @return Returns the key from the given value or an empty Collection if nothing found.
	 */
	public static <K, V> Collection<K> getKeysFromValue(final Map<K, V> map, final V value)
	{
		final Collection<K> keys = new ArrayList<>();
		for (final Entry<K, V> entry : map.entrySet())
		{
			final V val = entry.getValue();
			if (val.equals(value))
			{
				final K key = entry.getKey();
				keys.add(key);
			}
		}
		return keys;
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
	 * Factory method for create a new {@link LazyMap} from commons-collections4.
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
	 * The Method printMap prints the HashMap to the console.
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param msg
	 *            The map to print.
	 */
	public static <K, V> void printMap(final Map<K, V> msg)
	{
		for (final Entry<K, V> entry : msg.entrySet())
		{
			final K key = entry.getKey();
			final V value = entry.getValue();
			System.out.println("[" + key.toString() + "=" + value.toString() + "]");
		}
	}

	/**
	 * Converts a two dimensional Array to a Map.
	 *
	 * @param <T>
	 *            the generic type
	 * @param twoDimArray
	 *            The two dimensional Array.
	 * @return The map produced from the two dimensional Array.
	 */
	public static <T> Map<T, T> toGenericMap(final T[][] twoDimArray)
	{
		final Map<T, T> map = new LinkedHashMap<>();
		for (final T[] element : twoDimArray)
		{
			final T key = element[0];
			final T value = element[1];
			map.put(key, value);
		}
		return map;
	}

	/**
	 * Converts a two dimensional String Array to a Map.
	 *
	 * @param twoDimArray
	 *            The two dimensional String Array.
	 * @return The map produced from the two dimensional String Array.
	 */
	public static Map<String, String> toMap(final String[][] twoDimArray)
	{
		return toGenericMap(twoDimArray);
	}

}
