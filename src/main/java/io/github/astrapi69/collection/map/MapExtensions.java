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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.github.astrapi69.check.Argument;
import io.github.astrapi69.collection.list.ListFactory;

/**
 * Extensions class for use with Map objects.
 *
 * @author Asterios Raptis
 * @version 1.0
 */
public final class MapExtensions
{

	private MapExtensions()
	{
	}

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
		Argument.notNull(map, "map");
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
		Argument.notNull(map, "map");
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
	 * Merge and summarize the values of the given map with the given collection of keys. Every key
	 * of the collection will be summarized with the value of the given map.
	 *
	 * @param <K>
	 *            the generic type of the elements
	 * @param valueCounterMap
	 *            the counter map
	 * @param summarizeWithThisValueCounterMap
	 *            the set with the key values that will be summarized with the first map
	 * @return the new map ready to count elements
	 */
	public static <K> Map<K, Integer> mergeAndSummarize(final Map<K, Integer> valueCounterMap,
		final Collection<K> summarizeWithThisValueCounterMap)
	{
		return mergeAndSummarize(valueCounterMap, summarizeWithThisValueCounterMap, false);
	}

	/**
	 * Merge and summarize the values of the given map with the given collection of keys. Every key
	 * of the collection will be summarized with the value of the given map. If the flag fullMerge
	 * is true all keys from the collection and the map will be considered by the merge otherwise
	 * only the keys of the first map will be considered
	 *
	 * @param <K>
	 *            the generic type of the elements
	 * @param valueCounterMap
	 *            the counter map
	 * @param summarizeWithThisValueCounterMap
	 *            the set with the key values that will be summarized with the first map
	 * @param fullMerge
	 *            if true all keys from both maps will be considered by the merge otherwise only the
	 *            keys of the first map will be considered
	 * @return the new map ready to count elements
	 */
	public static <K> Map<K, Integer> mergeAndSummarize(final Map<K, Integer> valueCounterMap,
		final Collection<K> summarizeWithThisValueCounterMap, boolean fullMerge)
	{
		return mergeAndSummarize(valueCounterMap,
			MapFactory.newCounterMap(MapFactory.newCounterMap(summarizeWithThisValueCounterMap),
				summarizeWithThisValueCounterMap),
			fullMerge);
	}

	/**
	 * Merge and summarize the values of the given maps
	 *
	 * @param <K>
	 *            the generic type of the elements
	 * @param valueCounterMap
	 *            the counter map
	 * @param summarizeWithThisValueCounterMap
	 *            the other counter map that will be summarized with the first map
	 * @return the new map ready to count elements
	 */
	public static <K> Map<K, Integer> mergeAndSummarize(final Map<K, Integer> valueCounterMap,
		final Map<K, Integer> summarizeWithThisValueCounterMap)
	{
		return mergeAndSummarize(valueCounterMap, summarizeWithThisValueCounterMap, false);
	}

	/**
	 * Merge and summarize the values of the given maps with a flag fullMerge. If the flag fullMerge
	 * is true all keys from both maps will be considered by the merge otherwise only the keys of
	 * the first map will be considered
	 *
	 * @param <K>
	 *            the generic type of the elements
	 * @param valueCounterMap
	 *            the counter map
	 * @param summarizeWithThisValueCounterMap
	 *            the other counter map that will be summarized with the first map
	 * @param fullMerge
	 *            if true all keys from both maps will be considered by the merge otherwise only the
	 *            keys of the first map will be considered
	 * @return the new map ready to count elements
	 */
	public static <K> Map<K, Integer> mergeAndSummarize(final Map<K, Integer> valueCounterMap,
		final Map<K, Integer> summarizeWithThisValueCounterMap, boolean fullMerge)
	{
		return fullMerge
			? Stream.of(valueCounterMap, summarizeWithThisValueCounterMap).map(Map::entrySet)
				.flatMap(Collection::stream)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum))
			: Stream.of(valueCounterMap, summarizeWithThisValueCounterMap).map(Map::entrySet)
				.flatMap(Collection::stream)
				.filter(map -> valueCounterMap.containsKey(map.getKey()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));
	}

	/**
	 * Sort the given Map by its values and returns a sorted Map by the values of the given Map
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            the Map to sort
	 * @param reversed
	 *            the flag if the result should be in reversed order
	 * @return a sorted Map by the values of the given Map
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> map,
		boolean reversed)
	{
		Argument.notNull(map, "map");
		return sortByValue(map, reversed ? Comparator.reverseOrder() : Comparator.naturalOrder());
	}

	/**
	 * Swaps the given map where the returned map have the values of the given map as keys and the
	 * values are the keys of the given map
	 * 
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            the map to swap
	 * @return the swapped map
	 */
	public static <K, V> Map<V, K> swap(final Map<K, V> map)
	{
		return map.entrySet().stream()
			.collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
	}

	/**
	 * Sort the given Map by its values and returns a sorted Map by the values of the given Map
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            the Map to sort
	 * @param comparator
	 *            the comparator to sort
	 * @return a sorted Map by the values of the given Map
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> map,
		final Comparator<? super V> comparator)
	{
		Argument.notNull(map, "map");
		Argument.notNull(comparator, "comparator");
		return map.entrySet().stream().sorted(Map.Entry.comparingByValue(comparator))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> k,
				LinkedHashMap::new));
	}

	/**
	 * Sort the given Map by its keys and returns a sorted Map by the values of the given Map
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            the Map to sort
	 * @param comparator
	 *            the comparator to sort
	 * @return a sorted Map by the values of the given Map
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByKey(final Map<K, V> map,
		final Comparator<? super K> comparator)
	{
		Argument.notNull(map, "map");
		Argument.notNull(comparator, "comparator");
		return map.entrySet().stream().sorted(Map.Entry.comparingByKey(comparator))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (k, v) -> k,
				LinkedHashMap::new));
	}

	/**
	 * Gets the maximum key from the given Map with the given comparator that defines which is the
	 * maximum key
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            the Map to sort
	 * @param comparator
	 *            the comparator to sort
	 * @return the maximum key from the given Map
	 */
	public static <K, V extends Comparable<? super V>> K getMaxKey(final Map<K, V> map,
		final Comparator<? super K> comparator)
	{
		Argument.notNull(map, "map");
		Argument.notNull(comparator, "comparator");
		return map.entrySet().stream().max(Map.Entry.comparingByKey(comparator)).get().getKey();
	}

	/**
	 * Sort the given Map by its values and returns a sorted list by the values of the given Map
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            The Map to sort
	 * @param reversed
	 *            the flag if the result should be in reversed order
	 * @return a sorted list by the values of the given Map
	 */
	public static <K, V extends Comparable<? super V>> List<Entry<K, V>> sortByValueAsList(
		final Map<K, V> map, boolean reversed)
	{
		Argument.notNull(map, "map");
		return sortByValueAsList(map,
			reversed ? Comparator.reverseOrder() : Comparator.naturalOrder());
	}

	/**
	 * Sort the given Map by its values and returns a sorted list by the values of the given Map
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            The Map to sort
	 * @param comparator
	 *            the comparator to sort
	 * @return a sorted list by the values of the given Map
	 */
	public static <K, V extends Comparable<? super V>> List<Entry<K, V>> sortByValueAsList(
		final Map<K, V> map, final Comparator<? super V> comparator)
	{
		Argument.notNull(map, "map");
		Argument.notNull(comparator, "comparator");
		return map.entrySet().stream().sorted(Map.Entry.comparingByValue(comparator))
			.collect(Collectors.toList());
	}

	/**
	 * Converts the given two-dimensional array to a {@link Map} object
	 *
	 * @param <T>
	 *            the generic type
	 * @param twoDimArray
	 *            The two-dimensional array
	 * @return The {@link Map} object produced from the given two-dimensional array
	 */
	public static <T> Map<T, T> toGenericMap(final T[][] twoDimArray)
	{
		Argument.notNull(twoDimArray, "twoDimArray");
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

	/**
	 * Converts the given two-dimensional array to a {@link Map} object
	 *
	 * @param <T>
	 *            the generic type
	 * @param twoDimArray
	 *            The two-dimensional array
	 * @return The {@link Map} object produced from the given two-dimensional array
	 */
	public static <T> Map<String, Object> toStringObjectMap(final Object[][] twoDimArray)
	{
		Argument.notNull(twoDimArray, "twoDimArray");
		final Map<String, Object> map = new LinkedHashMap<>();
		for (final Object[] element : twoDimArray)
		{
			final String key = (String)element[0];
			final Object value = element[1];
			map.put(key, value);
		}
		return map;
	}

	/**
	 * Converts the given Map to a Properties object
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            The map to convert
	 * @return The Properties produced from the Map
	 */
	public static <K, V> Properties toProperties(final Map<K, V> map)
	{
		final Properties properties = new Properties();
		if (map != null)
		{
			for (final Entry<K, V> entry2 : map.entrySet())
			{
				final Map.Entry<?, ?> entry = entry2;
				final Object key = entry.getKey().toString();
				final Object value = entry.getValue().toString();
				properties.put(key, value);
			}
		}
		return properties;
	}

	/**
	 * Converts the values from the given Map to a List object
	 *
	 * @param <K>
	 *            the generic type of the key
	 * @param <V>
	 *            the generic type of the value
	 * @param map
	 *            The map to convert
	 * @return The List produced from the Map
	 */
	public static <K, V> List<V> valuesAsList(final Map<K, V> map)
	{
		return new ArrayList<>(map.values());
	}

}
