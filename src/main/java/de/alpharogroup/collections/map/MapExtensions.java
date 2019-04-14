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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.alpharogroup.collections.list.ListFactory;
import lombok.experimental.UtilityClass;

/**
 * Extensions class for use with Map objects.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
@UtilityClass
public final class MapExtensions
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
		final Collection<K> keys = ListFactory.newArrayList();
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
