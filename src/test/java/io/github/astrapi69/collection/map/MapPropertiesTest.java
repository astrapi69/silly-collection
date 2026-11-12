/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.collection.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.collection.test.EdgeCases;

/**
 * Properties of the methods of {@link MapExtensions} that their Javadoc promises, checked for the
 * inputs of {@link EdgeCases} (#179), as #116 (a key that appears n times counted 2n - 1 times)
 * needed. {@code MapFactory}'s {@code KeyValuePair} methods are not tested here: they leave the API
 * in 30.0 (#158)
 */
public class MapPropertiesTest
{

	private static final String LISTS = "io.github.astrapi69.collection.test.EdgeCases#stringLists";

	/**
	 * The numbers of times that the keys a, b and c appear in a collection of keys: 1, 2 and 3
	 * times each, so 27 triples, with each seed of the random order of the keys
	 *
	 * @return the number of a, of b, of c and the seed
	 */
	static Stream<Arguments> repetitions()
	{
		return EdgeCases.seeds().flatMap(seed -> Stream.of(1, 2, 3).flatMap(a -> Stream.of(1, 2, 3)
			.flatMap(b -> Stream.of(1, 2, 3).map(c -> Arguments.of(a, b, c, seed)))));
	}

	/**
	 * The collection of keys with a, b and c as many times as given, in a random order of the seed
	 *
	 * @param a
	 *            the times a appears
	 * @param b
	 *            the times b appears
	 * @param c
	 *            the times c appears
	 * @param seed
	 *            the seed of the order
	 * @return the keys
	 */
	private static List<String> keys(final int a, final int b, final int c, final long seed)
	{
		final List<String> keys = new ArrayList<>();
		keys.addAll(Collections.nCopies(a, "a"));
		keys.addAll(Collections.nCopies(b, "b"));
		keys.addAll(Collections.nCopies(c, "c"));
		Collections.shuffle(keys, EdgeCases.random(seed));
		return keys;
	}

	/**
	 * The {@code int} edge cases that a count of up to 3 can be added to without an overflow
	 *
	 * @return the start values of a counter
	 */
	private static List<Integer> startValues()
	{
		return EdgeCases.ints().filter(value -> value <= Integer.MAX_VALUE - 3).toList();
	}

	/**
	 * A map of the keys a, b and c to the start value
	 *
	 * @param start
	 *            the value of each key
	 * @return a new map
	 */
	private static Map<String, Integer> counterMap(final int start)
	{
		final Map<String, Integer> map = new LinkedHashMap<>();
		map.put("a", start);
		map.put("b", start);
		map.put("c", start);
		return map;
	}

	/**
	 * Property of {@link MapExtensions#mergeAndSummarize(Map, java.util.Collection)}: "each
	 * occurrence of a key in the collection counts one, so a key that appears n times adds n to its
	 * value" (#116), for 1, 2 and 3 repetitions of each key, any order of the keys (random from the
	 * seed, shown in the name) and each start value from the {@code int} edge cases that a count of
	 * up to 3 can be added to
	 *
	 * @param a
	 *            the times a appears
	 * @param b
	 *            the times b appears
	 * @param c
	 *            the times c appears
	 * @param seed
	 *            the seed of the order of the keys
	 */
	@ParameterizedTest(name = "a {0}, b {1}, c {2} times, seed {3}")
	@MethodSource("repetitions")
	public void testMergeAndSummarizeAddsTheNumberOfOccurrencesOfAKey(final int a, final int b,
		final int c, final long seed)
	{
		final List<String> keys = keys(a, b, c, seed);
		for (final int start : startValues())
		{
			final Map<String, Integer> actual = MapExtensions.mergeAndSummarize(counterMap(start),
				keys);
			final String message = "start " + start + ", keys " + keys;
			assertEquals(start + a, actual.get("a"), message);
			assertEquals(start + b, actual.get("b"), message);
			assertEquals(start + c, actual.get("c"), message);
			assertEquals(3, actual.size(), message);
		}
	}

	/**
	 * Property of {@link MapExtensions#mergeAndSummarize(Map, java.util.Collection, boolean)}: "If
	 * the flag fullMerge is true all keys from the collection and the map will be considered by the
	 * merge otherwise only the keys of the first map will be considered", a key that the map does
	 * not hold appears n times: with fullMerge it has the value n, without it is left out; the keys
	 * of the map add their occurrences as before
	 *
	 * @param a
	 *            the times a appears
	 * @param b
	 *            the times b appears
	 * @param c
	 *            the times c appears
	 * @param seed
	 *            the seed of the order of the keys
	 */
	@ParameterizedTest(name = "a {0}, b {1}, c {2} times, seed {3}")
	@MethodSource("repetitions")
	public void testMergeAndSummarizeOfAKeyTheMapDoesNotHold(final int a, final int b, final int c,
		final long seed)
	{
		final List<String> keys = keys(a, b, c, seed);
		keys.addAll(Collections.nCopies(c, "z"));
		Collections.shuffle(keys, EdgeCases.random(seed));
		final Map<String, Integer> only = MapExtensions.mergeAndSummarize(counterMap(10), keys);
		assertFalse(only.containsKey("z"), "keys " + keys);
		assertEquals(10 + a, only.get("a"));
		final Map<String, Integer> full = MapExtensions.mergeAndSummarize(counterMap(10), keys,
			true);
		assertEquals(c, full.get("z"), "keys " + keys);
		assertEquals(10 + b, full.get("b"), "keys " + keys);
		assertEquals(4, full.size(), "keys " + keys);
	}

	/**
	 * Property of {@link MapExtensions#getKeyFromValue(Map, Object)} and
	 * {@link MapExtensions#getKeysFromValue(Map, Object)}: "the first key from the given value, in
	 * the iteration order of the map, or null if nothing found", "all founded keys or an empty
	 * Collection", the values compare by {@link java.util.Objects#equals(Object, Object)} so null
	 * finds the keys mapped to null; for a map of the index to each element of every collection,
	 * and for each of a, b, c, null and an element no collection holds
	 *
	 * @param list
	 *            the elements, mapped from their index
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource(LISTS)
	public void testGetKeyFromValueAndGetKeysFromValue(final List<String> list)
	{
		final Map<Integer, String> map = new LinkedHashMap<>();
		for (int i = 0; i < list.size(); i++)
		{
			map.put(i, list.get(i));
		}
		for (final String value : java.util.Arrays.asList("a", "b", "c", null, EdgeCases.ABSENT))
		{
			final List<Integer> expected = new ArrayList<>();
			for (int i = 0; i < list.size(); i++)
			{
				if (java.util.Objects.equals(list.get(i), value))
				{
					expected.add(i);
				}
			}
			assertEquals(expected, new ArrayList<>(MapExtensions.getKeysFromValue(map, value)),
				"value " + value);
			assertEquals(expected.isEmpty() ? null : expected.get(0),
				MapExtensions.getKeyFromValue(map, value), "value " + value);
		}
	}

	/**
	 * Property of {@link MapExtensions#valuesAsList(Map)}: the values of the map as a list, in the
	 * iteration order of the map, for the map of the index to each element of every collection
	 *
	 * @param list
	 *            the elements, mapped from their index
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource(LISTS)
	public void testValuesAsListHoldsTheValuesInTheOrderOfTheMap(final List<String> list)
	{
		final Map<Integer, String> map = new LinkedHashMap<>();
		for (int i = 0; i < list.size(); i++)
		{
			map.put(i, list.get(i));
		}
		assertEquals(list, MapExtensions.valuesAsList(map));
	}

	/**
	 * Property of {@link MapExtensions#swap(Map)}: "the returned map have the values of the given
	 * map as keys and the values are the keys of the given map", so swapping twice gives the map
	 * again, for the map of each {@code int} edge case to its index
	 */
	@Test
	public void testSwapTwiceGivesTheMapAgain()
	{
		final Map<Integer, Integer> map = new LinkedHashMap<>();
		final int[] values = EdgeCases.intValues();
		for (int i = 0; i < values.length; i++)
		{
			map.put(values[i], i);
		}
		final Map<Integer, Integer> swapped = MapExtensions.swap(map);
		assertEquals(map.size(), swapped.size());
		for (final Map.Entry<Integer, Integer> entry : map.entrySet())
		{
			assertEquals(entry.getKey(), swapped.get(entry.getValue()));
		}
		assertEquals(map, MapExtensions.swap(swapped));
	}

	/**
	 * Property of {@link MapExtensions#swap(Map)}: "@throws IllegalStateException if two keys have
	 * the same value", for a map of an {@code int} edge case and of the one after it to the same
	 * value
	 *
	 * @param key
	 *            an edge case, the other key is the next one
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("io.github.astrapi69.collection.test.EdgeCases#ints")
	public void testSwapThrowsForTwoKeysWithTheSameValue(final int key)
	{
		final Map<Integer, Integer> map = new LinkedHashMap<>();
		map.put(key, key);
		map.put(key == Integer.MAX_VALUE ? key - 1 : key + 1, key);
		assertThrows(IllegalStateException.class, () -> MapExtensions.swap(map));
	}

}
