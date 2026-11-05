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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.array.ArrayExtensions;

/**
 * The unit test class for the class {@link MapExtensions}
 *
 * @author Asterios Raptis
 */
public class MapExtensionsTest
{

	/**
	 * A {@link LinkedHashMap} of the given keys and values
	 *
	 * @param keysAndValues
	 *            the keys and values, one after the other
	 * @return the map, in the order of the keys
	 */
	@SafeVarargs
	static <T> Map<T, T> mapOf(final T... keysAndValues)
	{
		final Map<T, T> map = new LinkedHashMap<>();
		for (int i = 0; i < keysAndValues.length; i += 2)
		{
			map.put(keysAndValues[i], keysAndValues[i + 1]);
		}
		return map;
	}

	/**
	 * A search of the keys that hold a value, and what
	 * {@link MapExtensions#getKeyFromValue(Map, Object)} and
	 * {@link MapExtensions#getKeysFromValue(Map, Object)} find
	 *
	 * @param description
	 *            the name of the case
	 * @param map
	 *            the map, in the order of its keys
	 * @param value
	 *            the value to look for, may be null
	 * @param firstKey
	 *            the first key that holds the value, null if there is none
	 * @param keys
	 *            all keys that hold the value
	 */
	record KeySearch(String description, Map<String, String> map, String value, String firstKey,
		Set<String> keys) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testGetKeyFromValueAndGetKeysFromValue(KeySearch)}
	 *
	 * @return the cases
	 */
	static Stream<KeySearch> keySearches()
	{
		return Stream.of(
			new KeySearch("value, held by 5 alone: 5",
				mapOf("1", "novalue", "2", "somevalue", "3", "othervalue", "5", "value"), "value",
				"5", Set.of("5")),
			new KeySearch("value, held by 4 and 5: 4 first, both",
				mapOf("1", "novalue", "2", "somevalue", "3", "othervalue", "4", "value", "5",
					"value"),
				"value", "4", Set.of("4", "5")),
			new KeySearch("value, held by 2 and 4, not the last: 2 first, both",
				mapOf("1", "novalue", "2", "value", "3", "othervalue", "4", "value"), "value", "2",
				Set.of("2", "4")),
			new KeySearch("novalue, held by the first entry: 1",
				mapOf("1", "novalue", "2", "value", "3", "othervalue", "4", "value"), "novalue",
				"1", Set.of("1")),
			new KeySearch("missing, held by no entry: none (#44)",
				mapOf("1", "novalue", "2", "somevalue"), "missing", null, Set.of()),
			new KeySearch("value in an empty map: none", mapOf(), "value", null, Set.of()),
			new KeySearch("value, after an entry that holds null: 2 (#44)",
				mapOf("1", null, "2", "value"), "value", "2", Set.of("2")),
			new KeySearch("null, held by 2 and 3: 2 first, both (#44)",
				mapOf("1", "value", "2", null, "3", null), null, "2", Set.of("2", "3")),
			new KeySearch("null, held by 1 and 3: 1 first, both (#44)",
				mapOf("1", null, "2", "value", "3", null), null, "1", Set.of("1", "3")));
	}

	/**
	 * Test for the methods {@link MapExtensions#getKeyFromValue(Map, Object)} and
	 * {@link MapExtensions#getKeysFromValue(Map, Object)}: the first key that holds the value, in
	 * the iteration order of the map, or null, and all keys that hold it, or an empty collection
	 *
	 * @param search
	 *            the map, the value and the keys that hold it
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("keySearches")
	public void testGetKeyFromValueAndGetKeysFromValue(final KeySearch search)
	{
		assertEquals(search.firstKey(), MapExtensions.getKeyFromValue(search.map(), search.value()),
			"getKeyFromValue");
		final List<String> keys = new ArrayList<>(
			MapExtensions.getKeysFromValue(search.map(), search.value()));
		assertEquals(search.keys().size(), keys.size(), "getKeysFromValue, the number of keys");
		assertEquals(search.keys(), new HashSet<>(keys), "getKeysFromValue");
	}

	/**
	 * A call of one of the methods {@code mergeAndSummarize} and the map it returns
	 *
	 * @param description
	 *            the name of the case
	 * @param merge
	 *            the call
	 * @param expected
	 *            the returned map
	 */
	record Merge(String description, Supplier<Map<Integer, Integer>> merge,
		Map<Integer, Integer> expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * A counter map of the keys 1 to 10, each counted 0, as
	 * {@link MapFactory#newCounterMap(java.util.Collection)} makes it
	 *
	 * @return the counter map
	 */
	static Map<Integer, Integer> oneToTenCountedZero()
	{
		final Map<Integer, Integer> map = new HashMap<>();
		for (int i = 1; i <= 10; i++)
		{
			map.put(i, 0);
		}
		return map;
	}

	/**
	 * The counts of {@link #oneToTenCountedZero()} after each of 1, 3, 4, 5, 9 and 11 is counted
	 * the given times
	 *
	 * @param times
	 *            how often each of them is counted
	 * @param withEleven
	 *            true if 11, which is not a key of the counter map, is in the result
	 * @return the counts
	 */
	static Map<Integer, Integer> countedOneToTen(final int times, final boolean withEleven)
	{
		final Map<Integer, Integer> map = oneToTenCountedZero();
		for (final int key : new int[] { 1, 3, 4, 5, 9 })
		{
			map.put(key, times);
		}
		if (withEleven)
		{
			map.put(11, times);
		}
		return map;
	}

	/**
	 * The cases of {@link MapExtensions#mergeAndSummarize(Map, Map)},
	 * {@link MapExtensions#mergeAndSummarize(Map, Map, boolean)},
	 * {@link MapExtensions#mergeAndSummarize(Map, java.util.Collection)} and
	 * {@link MapExtensions#mergeAndSummarize(Map, java.util.Collection, boolean)}
	 *
	 * @return the cases
	 */
	static Stream<Merge> merges()
	{
		final Set<Integer> keys = Set.of(1, 5, 4, 3, 9, 11);
		return Stream.of(
			new Merge("mergeAndSummarize(map, map), the same keys 1, 2: the sums",
				() -> MapExtensions.mergeAndSummarize(mapOf(1, 1, 2, 2), mapOf(1, 3, 2, 4)),
				mapOf(1, 4, 2, 6)),
			new Merge(
				"mergeAndSummarize(map, map), keys 1, 2 with 2, 3: only the keys of the first map",
				() -> MapExtensions.mergeAndSummarize(mapOf(1, 1, 2, 2), mapOf(2, 3, 3, 4)),
				mapOf(1, 1, 2, 5)),
			new Merge(
				"mergeAndSummarize(map, map, false), keys 1, 2 with 2, 3: only the keys of the first map",
				() -> MapExtensions.mergeAndSummarize(mapOf(1, 1, 2, 2), mapOf(2, 3, 3, 4), false),
				mapOf(1, 1, 2, 5)),
			new Merge(
				"mergeAndSummarize(map, map, true), keys 1, 2 with 2, 3: the keys of both maps",
				() -> MapExtensions.mergeAndSummarize(mapOf(1, 1, 2, 2), mapOf(2, 3, 3, 4), true),
				mapOf(1, 1, 2, 5, 3, 4)),
			new Merge("mergeAndSummarize(map, map, true), the same keys 1, 2: the sums",
				() -> MapExtensions.mergeAndSummarize(mapOf(1, 1, 2, 2), mapOf(1, 3, 2, 4), true),
				mapOf(1, 4, 2, 6)),
			new Merge(
				"mergeAndSummarize(map, set), 1 to 10 with 1, 3, 4, 5, 9, 11: 1 for each, 11 not taken",
				() -> MapExtensions.mergeAndSummarize(oneToTenCountedZero(), keys),
				countedOneToTen(1, false)),
			new Merge(
				"mergeAndSummarize(map, set, false), 1 to 10 with 1, 3, 4, 5, 9, 11: 11 not taken",
				() -> MapExtensions.mergeAndSummarize(oneToTenCountedZero(), keys, false),
				countedOneToTen(1, false)),
			new Merge(
				"mergeAndSummarize(map, set, true), 1 to 10 with 1, 3, 4, 5, 9, 11: 1 for each, 11 too",
				() -> MapExtensions.mergeAndSummarize(oneToTenCountedZero(), keys, true),
				countedOneToTen(1, true)),
			new Merge("mergeAndSummarize(map, set, true) twice: 2 for each",
				() -> MapExtensions.mergeAndSummarize(
					MapExtensions.mergeAndSummarize(oneToTenCountedZero(), keys, true), keys, true),
				countedOneToTen(2, true)),
			new Merge(
				"mergeAndSummarize(map, collection), 1, 2, 3 with 1, 3, 3, 9: 3 twice, but counted 3 times (#116)",
				() -> MapExtensions.mergeAndSummarize(mapOf(1, 0, 2, 0, 3, 0), List.of(1, 3, 3, 9)),
				mapOf(1, 1, 2, 0, 3, 3)),
			new Merge(
				"mergeAndSummarize(map, collection, false), 1, 2, 3 with 1, 3, 3, 9: 3 twice, but counted 3 times (#116)",
				() -> MapExtensions.mergeAndSummarize(mapOf(1, 0, 2, 0, 3, 0), List.of(1, 3, 3, 9),
					false),
				mapOf(1, 1, 2, 0, 3, 3)),
			new Merge(
				"mergeAndSummarize(map, collection, true), 1, 2, 3 with 1, 3, 3, 9: 3 twice, but counted 3 times, 9 once (#116)",
				() -> MapExtensions.mergeAndSummarize(mapOf(1, 0, 2, 0, 3, 0), List.of(1, 3, 3, 9),
					true),
				mapOf(1, 1, 2, 0, 3, 3, 9, 1)),
			new Merge(
				"mergeAndSummarize(map, collection, true), 1, 2, 3 with 1, 3, 9, 9, 9: 9 three times, but counted 5 times (#116)",
				() -> MapExtensions.mergeAndSummarize(mapOf(1, 2, 2, 2, 3, 2),
					List.of(1, 3, 9, 9, 9), true),
				mapOf(1, 3, 2, 2, 3, 3, 9, 5)),
			new Merge("mergeAndSummarize(map, empty collection): the counts of the map",
				() -> MapExtensions.mergeAndSummarize(mapOf(1, 1, 2, 2), List.<Integer> of()),
				mapOf(1, 1, 2, 2)),
			new Merge("mergeAndSummarize(map, empty collection, true): the counts of the map",
				() -> MapExtensions.mergeAndSummarize(mapOf(1, 1, 2, 2), List.<Integer> of(), true),
				mapOf(1, 1, 2, 2)));
	}

	/**
	 * Test for the methods {@code mergeAndSummarize}: the values of the keys of both are summed up,
	 * and only the keys of the first map are taken unless the flag fullMerge is true
	 *
	 * @param merge
	 *            the call and the returned map
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("merges")
	public void testMergeAndSummarize(final Merge merge)
	{
		assertEquals(merge.expected(), merge.merge().get());
	}

	/**
	 * A sort of a map and the entries in the order it gives them
	 *
	 * @param description
	 *            the name of the case
	 * @param sort
	 *            the sort, the entries of a sorted map in their order
	 * @param expected
	 *            the entries in their order
	 */
	record Sort(String description, Supplier<List<? extends Entry<?, ?>>> sort,
		List<? extends Entry<?, ?>> expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The map the sorts by value sort: 23 = 3, 21 = 2, 13 = 4, 5 = 1, in a {@link HashMap}
	 *
	 * @return a new map
	 */
	static Map<String, String> byValue()
	{
		return new HashMap<>(mapOf("23", "3", "21", "2", "13", "4", "5", "1"));
	}

	/**
	 * The map the sorts by key sort: 2 = 21, 1 = 5, 4 = 13, 3 = 23, in a {@link HashMap}
	 *
	 * @param key
	 *            makes a key of its text
	 * @return a new map
	 */
	static <K> Map<K, String> byKey(final Function<String, K> key)
	{
		final Map<K, String> map = new HashMap<>();
		map.put(key.apply("2"), "21");
		map.put(key.apply("1"), "5");
		map.put(key.apply("4"), "13");
		map.put(key.apply("3"), "23");
		return map;
	}

	/**
	 * The map of {@link #byKey(Function)} with Integer keys
	 *
	 * @return a new map
	 */
	static Map<Integer, String> integerKeys()
	{
		return byKey(Integer::valueOf);
	}

	/**
	 * An {@link IdentityHashMap} with two keys a that are equal but not the same, both with the
	 * value 1, and b = 2. Both keys hold the same value, because the Javadoc does not say which of
	 * two values would be kept
	 *
	 * @return a new map
	 */
	static Map<String, Integer> withEqualKeys()
	{
		final Map<String, Integer> map = new IdentityHashMap<>();
		map.put(new String("a"), 1);
		map.put(new String("a"), 1);
		map.put("b", 2);
		return map;
	}

	/**
	 * The entries of the given map in their order
	 *
	 * @param map
	 *            the map
	 * @return the entries
	 */
	static List<? extends Entry<?, ?>> entries(final Map<?, ?> map)
	{
		return new ArrayList<>(map.entrySet());
	}

	/**
	 * Entries of the given keys and values
	 *
	 * @param keysAndValues
	 *            the keys and values, one after the other
	 * @return the entries, in the given order
	 */
	static List<? extends Entry<?, ?>> entriesOf(final Object... keysAndValues)
	{
		final List<Entry<?, ?>> entries = new ArrayList<>();
		for (int i = 0; i < keysAndValues.length; i += 2)
		{
			entries.add(new SimpleEntry<>(keysAndValues[i], keysAndValues[i + 1]));
		}
		return entries;
	}

	/**
	 * The cases of {@link MapExtensions#sortByValue(Map, boolean)},
	 * {@link MapExtensions#sortByValue(Map, Comparator)},
	 * {@link MapExtensions#sortByValueAsList(Map, boolean)},
	 * {@link MapExtensions#sortByValueAsList(Map, Comparator)} and
	 * {@link MapExtensions#sortByKey(Map, Comparator)}
	 *
	 * @return the cases
	 */
	static Stream<Sort> sorts()
	{
		final List<? extends Entry<?, ?>> ascending = entriesOf("5", "1", "21", "2", "23", "3",
			"13", "4");
		final List<? extends Entry<?, ?>> descending = entriesOf("13", "4", "23", "3", "21", "2",
			"5", "1");
		return Stream.of(
			new Sort("sortByValue(map, false): ascending values",
				() -> entries(MapExtensions.sortByValue(byValue(), false)), ascending),
			new Sort("sortByValue(map, true): descending values",
				() -> entries(MapExtensions.sortByValue(byValue(), true)), descending),
			new Sort("sortByValue(map, naturalOrder): ascending values",
				() -> entries(MapExtensions.sortByValue(byValue(), Comparator.naturalOrder())),
				ascending),
			new Sort("sortByValue(map, reverseOrder): descending values",
				() -> entries(MapExtensions.sortByValue(byValue(), Comparator.reverseOrder())),
				descending),
			new Sort("sortByValueAsList(map, false): ascending values",
				() -> MapExtensions.sortByValueAsList(byValue(), false), ascending),
			new Sort("sortByValueAsList(map, true): descending values",
				() -> MapExtensions.sortByValueAsList(byValue(), true), descending),
			new Sort("sortByValueAsList(map, naturalOrder): ascending values",
				() -> MapExtensions.sortByValueAsList(byValue(), Comparator.naturalOrder()),
				ascending),
			new Sort("sortByValueAsList(map, reverseOrder): descending values",
				() -> MapExtensions.sortByValueAsList(byValue(), Comparator.reverseOrder()),
				descending),
			new Sort("sortByKey(map, naturalOrder), String keys: ascending keys",
				() -> entries(
					MapExtensions.sortByKey(byKey(Function.identity()), Comparator.naturalOrder())),
				entriesOf("1", "5", "2", "21", "3", "23", "4", "13")),
			new Sort("sortByKey(map, reverseOrder), String keys: descending keys",
				() -> entries(
					MapExtensions.sortByKey(byKey(Function.identity()), Comparator.reverseOrder())),
				entriesOf("4", "13", "3", "23", "2", "21", "1", "5")),
			new Sort("sortByKey(map, naturalOrder), Integer keys: ascending keys",
				() -> entries(MapExtensions.sortByKey(integerKeys(), Comparator.naturalOrder())),
				entriesOf(1, "5", 2, "21", 3, "23", 4, "13")),
			new Sort("sortByKey(map, reverseOrder), Integer keys: descending keys",
				() -> entries(MapExtensions.sortByKey(integerKeys(), Comparator.reverseOrder())),
				entriesOf(4, "13", 3, "23", 2, "21", 1, "5")),
			new Sort("sortByValue(map, false), two equal keys a: a once, then b",
				() -> entries(MapExtensions.sortByValue(withEqualKeys(), false)),
				entriesOf("a", 1, "b", 2)),
			new Sort("sortByValue(map, true), two equal keys a: b, then a once",
				() -> entries(MapExtensions.sortByValue(withEqualKeys(), true)),
				entriesOf("b", 2, "a", 1)),
			new Sort("sortByKey(map, naturalOrder), two equal keys a: a once, then b",
				() -> entries(MapExtensions.sortByKey(withEqualKeys(), Comparator.naturalOrder())),
				entriesOf("a", 1, "b", 2)));
	}

	/**
	 * Test for the methods {@link MapExtensions#sortByValue(Map, boolean)},
	 * {@link MapExtensions#sortByValue(Map, Comparator)},
	 * {@link MapExtensions#sortByValueAsList(Map, boolean)},
	 * {@link MapExtensions#sortByValueAsList(Map, Comparator)} and
	 * {@link MapExtensions#sortByKey(Map, Comparator)}: the entries come in the order of the values
	 * or keys that the flag or the comparator defines; of two equal keys of an
	 * {@link IdentityHashMap} the sorted map holds the key once instead of dropping it
	 *
	 * @param sort
	 *            the sort and the entries in their order
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("sorts")
	public void testSort(final Sort sort)
	{
		assertEquals(sort.expected(), sort.sort().get());
	}

	/**
	 * Test for method {@link MapExtensions#getMaxKey(Map, Comparator)}: the greatest key in the
	 * order of the comparator
	 *
	 * @param description
	 *            the name of the case
	 * @param maxKey
	 *            the call
	 * @param expected
	 *            the greatest key
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@MethodSource("maxKeys")
	public void testGetMaxKey(final String description, final Supplier<Object> maxKey,
		final Object expected)
	{
		assertEquals(expected, maxKey.get());
	}

	/**
	 * The cases of {@link #testGetMaxKey(String, Supplier, Object)}
	 *
	 * @return the name of the case, the call and the greatest key
	 */
	static Stream<Arguments> maxKeys()
	{
		return Stream.of(
			Arguments.of("String keys, naturalOrder: 4",
				(Supplier<Object>)() -> MapExtensions.getMaxKey(byKey(Function.identity()),
					Comparator.naturalOrder()),
				"4"),
			Arguments.of("String keys, reverseOrder: 1",
				(Supplier<Object>)() -> MapExtensions.getMaxKey(byKey(Function.identity()),
					Comparator.reverseOrder()),
				"1"),
			Arguments.of("Integer keys, naturalOrder: 4",
				(Supplier<Object>)() -> MapExtensions.getMaxKey(integerKeys(),
					Comparator.naturalOrder()),
				4),
			Arguments.of("Integer keys, reverseOrder: 1", (Supplier<Object>)() -> MapExtensions
				.getMaxKey(integerKeys(), Comparator.reverseOrder()), 1));
	}

	/**
	 * A conversion of a map into another map, a list or properties, and what it gives
	 *
	 * @param description
	 *            the name of the case
	 * @param conversion
	 *            the conversion
	 * @param expected
	 *            what it gives
	 */
	record Conversion(String description, Supplier<Object> conversion, Object expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * Properties of the given keys and values
	 *
	 * @param keysAndValues
	 *            the keys and values, one after the other
	 * @return the properties
	 */
	static Properties propertiesOf(final String... keysAndValues)
	{
		final Properties properties = new Properties();
		for (int i = 0; i < keysAndValues.length; i += 2)
		{
			properties.put(keysAndValues[i], keysAndValues[i + 1]);
		}
		return properties;
	}

	/**
	 * The cases of {@link MapExtensions#swap(Map)}, {@link MapExtensions#toProperties(Map)} and
	 * {@link MapExtensions#valuesAsList(Map)}
	 *
	 * @return the cases
	 */
	static Stream<Conversion> conversions()
	{
		return Stream.of(new Conversion("swap, four entries: the values are the keys",
			() -> MapExtensions.swap(byValue()), mapOf("3", "23", "2", "21", "4", "13", "1", "5")),
			new Conversion("swap, an empty map: an empty map",
				() -> MapExtensions.swap(new HashMap<String, String>()), Map.of()),
			new Conversion("toProperties, four entries: a property for each",
				() -> MapExtensions.toProperties(byValue()),
				propertiesOf("5", "1", "21", "2", "23", "3", "13", "4")),
			new Conversion("toProperties, Integer keys and values: their texts",
				() -> MapExtensions.toProperties(mapOf(1, 2)), propertiesOf("1", "2")),
			new Conversion("toProperties, null: no property",
				() -> MapExtensions.toProperties(null), new Properties()),
			new Conversion("valuesAsList, four entries: the four values, in any order",
				() -> MapExtensions.valuesAsList(byValue()).stream().sorted().toList(),
				List.of("1", "2", "3", "4")),
			new Conversion("valuesAsList, an empty map: an empty list",
				() -> MapExtensions.valuesAsList(new HashMap<String, String>()), List.of()));
	}

	/**
	 * Test for the methods {@link MapExtensions#swap(Map)}, {@link MapExtensions#toProperties(Map)}
	 * and {@link MapExtensions#valuesAsList(Map)}
	 *
	 * @param conversion
	 *            the conversion and what it gives
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("conversions")
	public void testConversion(final Conversion conversion)
	{
		assertEquals(conversion.expected(), conversion.conversion().get());
	}

	/**
	 * Test for the methods {@link MapExtensions#toGenericMap(Object[][])},
	 * {@link MapExtensions#toMap(String[][])} and
	 * {@link MapExtensions#toStringObjectMap(Object[][])}: the map holds one entry for each row of
	 * the array, in the order of the rows, and {@link ArrayExtensions#toTwoDimensionalArray(Map)}
	 * gives the rows back
	 *
	 * @param description
	 *            the name of the case
	 * @param rows
	 *            the rows of key and value
	 * @param conversion
	 *            the conversion of the rows into a map
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@MethodSource("twoDimensionalArrays")
	public void testToMap(final String description, final Object[][] rows,
		final Function<Object[][], Map<?, ?>> conversion)
	{
		final Map<?, ?> map = conversion.apply(rows);
		final List<Object> expected = new ArrayList<>();
		for (final Object[] row : rows)
		{
			expected.add(new SimpleEntry<>(row[0], row[1]));
		}
		assertEquals(expected, new ArrayList<>(map.entrySet()));
	}

	/**
	 * The cases of {@link #testToMap(String, Object[][], Function)}
	 *
	 * @return the name of the case, the rows and the conversion
	 */
	static Stream<Arguments> twoDimensionalArrays()
	{
		final String[][] strings = { { "1", "value1" }, { "3", "value3" }, { "4", "value4" },
				{ "2", "value2" } };
		final Object[][] booleans = { { "1", Boolean.TRUE }, { "3", Boolean.FALSE },
				{ "4", Boolean.TRUE }, { "2", Boolean.TRUE } };
		final Function<Object[][], Map<?, ?>> toGenericMap = MapExtensions::toGenericMap;
		final Function<Object[][], Map<?, ?>> toMap = rows -> MapExtensions.toMap((String[][])rows);
		final Function<Object[][], Map<?, ?>> toStringObjectMap = MapExtensions::toStringObjectMap;
		return Stream.of(
			Arguments.of("toGenericMap, four rows: four entries in their order", strings,
				toGenericMap),
			Arguments.of("toMap, four rows: four entries in their order", strings, toMap),
			Arguments.of("toMap, no row: an empty map", new String[0][], toMap),
			Arguments.of("toStringObjectMap, four rows of booleans: four entries in their order",
				booleans, toStringObjectMap));
	}

	/**
	 * Test for method {@link MapExtensions#toStringObjectMap(Object[][])} with
	 * {@link ArrayExtensions#toTwoDimensionalArray(Map)}: the map gives the rows it was made of
	 * back, in their order
	 */
	@Test
	public void testToStringObjectMapAndBack()
	{
		final Object[][] twoDimArray = { { "1", Boolean.TRUE }, { "3", Boolean.FALSE },
				{ "4", Boolean.TRUE }, { "2", Boolean.TRUE } };
		final Map<String, Object> map = MapExtensions.toStringObjectMap(twoDimArray);
		assertArrayEquals(twoDimArray, ArrayExtensions.toTwoDimensionalArray(map));
	}

	/**
	 * Test method for {@link MapExtensions} with {@link BeanTester}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(MapExtensions.class);
	}

}
