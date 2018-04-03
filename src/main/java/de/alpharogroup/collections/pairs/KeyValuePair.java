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
package de.alpharogroup.collections.pairs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * The class {@link KeyValuePair} represents a key value pair with generic parameters for the key
 * and value type.
 *
 *
 * @param <K>
 *            The generic type of the key
 * @param <V>
 *            The generic type of the value
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class KeyValuePair<K, V> implements Serializable
{

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Transforms the given {@link Map} to a list of {@link KeyValuePair}'s.
	 *
	 * @param <K>
	 *            The generic type of the key
	 * @param <V>
	 *            The generic type of the value
	 * @param map
	 *            the map
	 * @return the new list with the {@link KeyValuePair}'s.
	 */
	public static <K, V> List<KeyValuePair<K, V>> toKeyValuePairs(final Map<K, V> map)
	{
		final List<KeyValuePair<K, V>> list = new ArrayList<>();
		for (final Entry<K, V> entry : map.entrySet())
		{
			list.add(
				KeyValuePair.<K, V> builder().key(entry.getKey()).value(entry.getValue()).build());
		}
		return list;
	}

	/**
	 * Transforms the given {@link Properties} to a list of {@link KeyValuePair}'s.
	 *
	 * @param properties
	 *            the properties
	 * @return the new list with the {@link KeyValuePair}'s.
	 */
	public static List<KeyValuePair<String, String>> toKeyValuePairs(final Properties properties)
	{
		final List<KeyValuePair<String, String>> list = new ArrayList<>();
		for (final Entry<Object, Object> entry : properties.entrySet())
		{
			list.add(KeyValuePair.<String, String> builder().key((String)entry.getKey())
				.value((String)entry.getValue()).build());
		}
		return list;
	}

	/**
	 * Transforms the given {@link List} of {@link KeyValuePair}'s to a {@link Map}.
	 *
	 * @param <K>
	 *            The generic type of the key
	 * @param <V>
	 *            The generic type of the value
	 * @param list
	 *            the list
	 * @return the new map.
	 */
	public static <K, V> Map<K, V> toMap(final Collection<KeyValuePair<K, V>> list)
	{
		final Map<K, V> map = new HashMap<>();
		for (KeyValuePair<K, V> keyValuePair : list)
		{
			map.put(keyValuePair.getKey(), keyValuePair.getValue());
		}
		return map;
	}


	/** The key. */
	K key;

	/** The value. */
	V value;

}
