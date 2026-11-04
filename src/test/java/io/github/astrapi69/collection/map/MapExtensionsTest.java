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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.CollectionExtensions;
import io.github.astrapi69.collection.array.ArrayExtensions;
import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.collection.set.SetFactory;

/**
 * The unit test class for the class {@link MapExtensions}
 *
 * @author Asterios Raptis
 */
public class MapExtensionsTest
{
	private static int getRandomIntBetween(int min, int max)
	{
		return (int)(Math.random() * ((max - min) + 1)) + min;
	}

	private void assertMapToArray(Map<String, String> map, String[][] twoDimArray)
	{
		assertEquals(twoDimArray.length, map.size());
		int count = 0;
		for (final Entry<String, String> entry : map.entrySet())
		{
			final String key = entry.getKey();
			final String value = entry.getValue();
			assertEquals(key, twoDimArray[count][0]);
			assertEquals(value, twoDimArray[count][1]);
			count++;
		}
	}

	/**
	 * Test for the Method {@link MapExtensions#getKeyFromValue(Map, Object)}.
	 */
	@Test
	public void testGetKeyFromValue()
	{
		String actual;
		String expected;
		String value;

		value = "value";
		final Map<String, String> map = new HashMap<>();
		map.put("1", "novalue");
		map.put("2", "somevalue");
		map.put("3", "othervalue");
		map.put("5", "value");
		actual = MapExtensions.getKeyFromValue(map, value);
		expected = "5";
		assertEquals(actual, expected);
	}

	/**
	 * Test the method {@link MapExtensions#getKeyFromValue(Map, Object)} with values that come
	 * before the last entry, one of them held by two keys: the first key in the iteration order of
	 * the map that holds the value is returned
	 */
	@Test
	public void testGetKeyFromValueReturnsTheFirstKey()
	{
		final Map<String, String> map = MapFactory.newLinkedHashMap();
		map.put("1", "novalue");
		map.put("2", "value");
		map.put("3", "othervalue");
		map.put("4", "value");
		assertEquals("2", MapExtensions.getKeyFromValue(map, "value"));
		assertEquals("1", MapExtensions.getKeyFromValue(map, "novalue"));
	}

	/**
	 * Test the method {@link MapExtensions#getKeyFromValue(Map, Object)} with an empty map: no key
	 * is found, so null is returned
	 */
	@Test
	public void testGetKeyFromValueWithEmptyMap()
	{
		assertNull(MapExtensions.getKeyFromValue(MapFactory.newLinkedHashMap(), "value"));
	}

	/**
	 * Test the method {@link MapExtensions#getKeyFromValue(Map, Object)} with a value that no entry
	 * holds. Pins the result of today, the last key of the map, which #44 reports as a bug: the fix
	 * of #44 changes it to null
	 */
	@Test
	public void testGetKeyFromValueWithoutMatch()
	{
		String actual;
		String expected;
		final Map<String, String> map = MapFactory.newLinkedHashMap();
		map.put("1", "novalue");
		map.put("2", "somevalue");
		actual = MapExtensions.getKeyFromValue(map, "missing");
		expected = "2";
		assertEquals(expected, actual);
	}

	/**
	 * Test the method {@link MapExtensions#getKeyFromValue(Map, Object)} with a map that holds null
	 * as the value of an entry before the one that holds the value. Pins the result of today, a
	 * {@link NullPointerException}, which #44 reports as a bug: the fix of #44 changes it to the
	 * key of the value
	 */
	@Test
	public void testGetKeyFromValueWithNullValueInMap()
	{
		final Map<String, String> map = MapFactory.newLinkedHashMap();
		map.put("1", null);
		map.put("2", "value");
		assertThrows(NullPointerException.class, () -> MapExtensions.getKeyFromValue(map, "value"));
	}

	/**
	 * Test the method {@link MapExtensions#getKeyFromValue(Map, Object)} with null as the value to
	 * look for. Pins the result of today, a {@link NullPointerException}, which #44 reports as a
	 * bug: the fix of #44 changes it to the first key mapped to null
	 */
	@Test
	public void testGetKeyFromValueWithNullValue()
	{
		final Map<String, String> map = MapFactory.newLinkedHashMap();
		map.put("1", "value");
		map.put("2", null);
		map.put("3", null);
		assertThrows(NullPointerException.class, () -> MapExtensions.getKeyFromValue(map, null));
	}

	/**
	 * Test for the Method {@link MapExtensions#getKeysFromValue(Map, Object)}.
	 */
	@Test
	public void testGetKeysFromValue()
	{
		final String value = "value";
		final List<String> expected = ListFactory.newArrayList();
		expected.add("4");
		expected.add("5");
		final Map<String, String> map = new HashMap<>();
		map.put("1", "novalue");
		map.put("2", "somevalue");
		map.put("3", "othervalue");
		map.put("4", "value");
		map.put("5", "value");
		final Collection<String> foundedKeys = MapExtensions.getKeysFromValue(map, value);
		assertEquals(expected.size(), foundedKeys.size());
		for (final String key : foundedKeys)
		{
			assertTrue(expected.contains(key), "Key should be in the expected List.");

		}
	}

	/**
	 * Test the method {@link MapExtensions#getKeysFromValue(Map, Object)} with a value that no
	 * entry holds: an empty collection is returned, as its return tag says
	 */
	@Test
	public void testGetKeysFromValueWithoutMatch()
	{
		final Map<String, String> map = MapFactory.newLinkedHashMap();
		map.put("1", "novalue");
		map.put("2", "value");
		assertTrue(MapExtensions.getKeysFromValue(map, "missing").isEmpty());
	}

	/**
	 * Test the method {@link MapExtensions#getKeysFromValue(Map, Object)} with a map that holds
	 * null as the value of an entry. Pins the result of today, a {@link NullPointerException},
	 * which #44 reports as a bug: the fix of #44 changes it to the keys of the value
	 */
	@Test
	public void testGetKeysFromValueWithNullValueInMap()
	{
		final Map<String, String> map = MapFactory.newLinkedHashMap();
		map.put("1", null);
		map.put("2", "value");
		assertThrows(NullPointerException.class,
			() -> MapExtensions.getKeysFromValue(map, "value"));
	}

	/**
	 * Test the method {@link MapExtensions#getKeysFromValue(Map, Object)} with null as the value to
	 * look for. Pins the result of today, a {@link NullPointerException}, which #44 reports as a
	 * bug: the fix of #44 changes it to the keys mapped to null
	 */
	@Test
	public void testGetKeysFromValueWithNullValue()
	{
		final Map<String, String> map = MapFactory.newLinkedHashMap();
		map.put("1", null);
		map.put("2", "value");
		map.put("3", null);
		assertThrows(NullPointerException.class, () -> MapExtensions.getKeysFromValue(map, null));
	}

	/**
	 * Test method for {@link MapExtensions#mergeAndSummarize(Map, Map)}
	 */
	@Test
	public void testMergeAndSummarize()
	{
		int minVolume;
		int maxVolume;
		Map<Integer, Integer> initialNumberCounterMap;
		Map<Integer, Integer> numberCounterMap;
		Map<Integer, Integer> mergedMap;
		List<Integer> integerList;
		// first scenario ...
		minVolume = 1;
		maxVolume = 10;
		integerList = ListFactory.newRangeList(minVolume, maxVolume);
		numberCounterMap = MapFactory.newCounterMap(integerList);
		for (int i = minVolume; i <= maxVolume; i++)
		{
			numberCounterMap.merge(i, getRandomIntBetween(1, 4), Integer::sum);
		}
		initialNumberCounterMap = MapFactory.newCounterMap(integerList);
		for (int i = minVolume; i <= maxVolume; i++)
		{
			initialNumberCounterMap.merge(i, getRandomIntBetween(1, 4), Integer::sum);
		}
		mergedMap = MapExtensions.mergeAndSummarize(initialNumberCounterMap, numberCounterMap);
		for (int i = minVolume; i <= maxVolume; i++)
		{
			int actual = mergedMap.get(i);
			int expected = numberCounterMap.get(i) + initialNumberCounterMap.get(i);
			assertEquals(actual, expected);
		}
		// other scenario ...
		mergedMap.clear();
		integerList = ListFactory.newRangeList(minVolume, maxVolume);
		numberCounterMap = MapFactory.newCounterMap(integerList);
		for (int i = minVolume; i <= maxVolume; i++)
		{
			numberCounterMap.merge(i, getRandomIntBetween(1, 4), Integer::sum);
		}
		minVolume = 6;
		maxVolume = 15;
		integerList = ListFactory.newRangeList(minVolume, maxVolume);
		initialNumberCounterMap = MapFactory.newCounterMap(integerList);
		for (int i = minVolume; i <= maxVolume; i++)
		{
			initialNumberCounterMap.merge(i, getRandomIntBetween(1, 4), Integer::sum);
		}
		mergedMap = MapExtensions.mergeAndSummarize(initialNumberCounterMap, numberCounterMap);
		for (int i = minVolume; i <= maxVolume; i++)
		{
			if (numberCounterMap.containsKey(i) && initialNumberCounterMap.containsKey(i))
			{
				int actual = mergedMap.get(i);
				int expected = numberCounterMap.get(i) + initialNumberCounterMap.get(i);
				assertEquals(actual, expected);
			}
		}
	}

	/**
	 * Test method for {@link MapExtensions#mergeAndSummarize(Map, Map, boolean)}
	 */
	@Test
	public void testMergeAndSummarizeWithFullMergeOption()
	{
		int minVolume;
		int maxVolume;
		boolean fullMerge;
		Map<Integer, Integer> initialNumberCounterMap;
		Map<Integer, Integer> numberCounterMap;
		Map<Integer, Integer> mergedMap;
		List<Integer> integerList;
		// first scenario ...
		minVolume = 1;
		maxVolume = 10;
		fullMerge = true;
		integerList = ListFactory.newRangeList(minVolume, maxVolume);
		numberCounterMap = MapFactory.newCounterMap(integerList);
		for (int i = minVolume; i <= maxVolume; i++)
		{
			numberCounterMap.merge(i, getRandomIntBetween(1, 4), Integer::sum);
		}
		initialNumberCounterMap = MapFactory.newCounterMap(integerList);
		for (int i = minVolume; i <= maxVolume; i++)
		{
			initialNumberCounterMap.merge(i, getRandomIntBetween(1, 4), Integer::sum);
		}
		mergedMap = MapExtensions.mergeAndSummarize(initialNumberCounterMap, numberCounterMap,
			fullMerge);
		for (int i = minVolume; i <= maxVolume; i++)
		{
			int actual = mergedMap.get(i);
			int expected = numberCounterMap.get(i) + initialNumberCounterMap.get(i);
			assertEquals(actual, expected);
		}
		// other scenario ...
		mergedMap.clear();
		fullMerge = false;
		integerList = ListFactory.newRangeList(minVolume, maxVolume);
		numberCounterMap = MapFactory.newCounterMap(integerList);
		for (int i = minVolume; i <= maxVolume; i++)
		{
			numberCounterMap.merge(i, getRandomIntBetween(1, 4), Integer::sum);
		}
		minVolume = 6;
		maxVolume = 15;
		integerList = ListFactory.newRangeList(minVolume, maxVolume);
		initialNumberCounterMap = MapFactory.newCounterMap(integerList);
		for (int i = minVolume; i <= maxVolume; i++)
		{
			initialNumberCounterMap.merge(i, getRandomIntBetween(1, 4), Integer::sum);
		}
		mergedMap = MapExtensions.mergeAndSummarize(initialNumberCounterMap, numberCounterMap,
			fullMerge);
		for (int i = minVolume; i <= maxVolume; i++)
		{
			if (numberCounterMap.containsKey(i) && initialNumberCounterMap.containsKey(i))
			{
				int actual = mergedMap.get(i);
				int expected = numberCounterMap.get(i) + initialNumberCounterMap.get(i);
				assertEquals(actual, expected);
			}
		}
	}


	/**
	 * Test method for {@link MapExtensions#mergeAndSummarize(Map, Collection)} and
	 * {@link MapExtensions#mergeAndSummarize(Map, Collection, boolean)}
	 */
	@Test
	public void testMergeAndSummarizeWithSet()
	{
		int minVolume;
		int maxVolume;
		boolean fullMerge;
		Map<Integer, Integer> initialNumberCounterMap;
		Map<Integer, Integer> mergedMap;
		Set<Integer> summarizeWith;
		List<Integer> integerList;
		// new scenario ...
		minVolume = 1;
		maxVolume = 10;
		fullMerge = true;
		integerList = ListFactory.newRangeList(minVolume, maxVolume);
		summarizeWith = SetFactory.newHashSet(1, 5, 4, 3, 9, 11);

		initialNumberCounterMap = MapFactory.newCounterMap(integerList);

		mergedMap = MapExtensions.mergeAndSummarize(initialNumberCounterMap, summarizeWith,
			fullMerge);
		assertTrue(mergedMap.size() == 11);
		for (int i = minVolume; i <= 11; i++)
		{
			int expected = 0;
			int actual = mergedMap.get(i);
			if (summarizeWith.contains(i))
			{
				expected = 1;
			}
			assertEquals(actual, expected);
		}

		mergedMap = MapExtensions.mergeAndSummarize(mergedMap, summarizeWith, fullMerge);
		assertTrue(mergedMap.size() == 11);
		for (int i = minVolume; i <= 11; i++)
		{
			int expected = 0;
			int actual = mergedMap.get(i);
			if (summarizeWith.contains(i))
			{
				expected = 2;
			}
			assertEquals(actual, expected);
		}
		// new scenario ...
		minVolume = 1;
		maxVolume = 10;
		integerList = ListFactory.newRangeList(minVolume, maxVolume);
		summarizeWith = SetFactory.newHashSet(1, 5, 4, 3, 9, 11);

		initialNumberCounterMap = MapFactory.newCounterMap(integerList);

		mergedMap = MapExtensions.mergeAndSummarize(initialNumberCounterMap, summarizeWith);
		assertTrue(mergedMap.size() == 10);
		for (int i = minVolume; i <= maxVolume; i++)
		{
			int expected = 0;
			int actual = mergedMap.get(i);
			if (summarizeWith.contains(i))
			{
				expected = 1;
			}
			assertEquals(actual, expected);
		}

	}

	/**
	 * Test for the Method {@link MapExtensions#toProperties(Map)}
	 */
	@Test
	public void testToProperties()
	{
		Map<String, String> map;
		Properties actual;
		Properties expected;
		// map with test data ...
		map = new HashMap<>();
		map.put("23", "3");
		map.put("21", "2");
		map.put("13", "4");
		map.put("5", "1");
		actual = MapExtensions.toProperties(map);
		expected = new Properties();
		expected.put("5", "1");
		expected.put("21", "2");
		expected.put("23", "3");
		expected.put("13", "4");
		assertEquals(expected, actual);
	}

	/**
	 * Test for the Method {@link MapExtensions#valuesAsList(Map)}
	 */
	@Test
	public void testValuesAsList()
	{
		Map<String, String> map;
		List<String> actual;
		List<String> expected;
		// map with test data ...
		map = new HashMap<>();
		map.put("23", "3");
		map.put("21", "2");
		map.put("13", "4");
		map.put("5", "1");
		actual = MapExtensions.valuesAsList(map);
		expected = ListFactory.newArrayList("1", "2", "3", "4");
		assertTrue(CollectionExtensions.isEqualCollection(actual, expected));
	}

	/**
	 * Test the method {@link MapExtensions#sortByValue(Map, boolean)}: the entries come in the
	 * natural order of the values, and in the reversed order if the flag is true
	 */
	@Test
	public void testSortByValue()
	{
		Map<String, String> map;
		Map<String, String> actual;
		Map<String, String> expected;
		// map with test data ...
		map = new HashMap<>();
		map.put("23", "3");
		map.put("21", "2");
		map.put("13", "4");
		map.put("5", "1");
		// new scenario...
		actual = MapExtensions.sortByValue(map, false);
		expected = MapFactory.newLinkedHashMap();
		expected.put("5", "1");
		expected.put("21", "2");
		expected.put("23", "3");
		expected.put("13", "4");
		assertEquals(expected, actual);
		assertEquals(new ArrayList<>(expected.keySet()), new ArrayList<>(actual.keySet()));
		// new scenario...
		actual = MapExtensions.sortByValue(map, true);
		expected = MapFactory.newLinkedHashMap();
		expected.put("13", "4");
		expected.put("23", "3");
		expected.put("21", "2");
		expected.put("5", "1");
		assertEquals(expected, actual);
		assertEquals(new ArrayList<>(expected.keySet()), new ArrayList<>(actual.keySet()));
	}

	/**
	 * Test the method {@link MapExtensions#sortByValue(Map, boolean)} with an
	 * {@link IdentityHashMap} that holds two keys that are equal but not the same: the sorted map
	 * holds the key once instead of dropping it. Both keys hold the same value, because the Javadoc
	 * does not say which of two values would be kept.
	 */
	@Test
	public void testSortByValueWithEqualKeys()
	{
		Map<String, Integer> actual;
		Map<String, Integer> expected;
		final Map<String, Integer> map = new IdentityHashMap<>();
		// two keys that are equal but not the same
		map.put(new String("a"), 1);
		map.put(new String("a"), 1);
		map.put("b", 2);
		// new scenario...
		actual = MapExtensions.sortByValue(map, false);
		expected = MapFactory.newLinkedHashMap();
		expected.put("a", 1);
		expected.put("b", 2);
		assertEquals(expected, actual);
		assertEquals(new ArrayList<>(expected.keySet()), new ArrayList<>(actual.keySet()));
		// new scenario...
		actual = MapExtensions.sortByValue(map, true);
		expected = MapFactory.newLinkedHashMap();
		expected.put("b", 2);
		expected.put("a", 1);
		assertEquals(expected, actual);
		assertEquals(new ArrayList<>(expected.keySet()), new ArrayList<>(actual.keySet()));
	}

	/**
	 * Test the method {@link MapExtensions#sortByKey(Map, Comparator)} with an
	 * {@link IdentityHashMap} that holds two keys that are equal but not the same: the sorted map
	 * holds the key once instead of dropping it. Both keys hold the same value, because the Javadoc
	 * does not say which of two values would be kept.
	 */
	@Test
	public void testSortByKeyWithEqualKeys()
	{
		final Map<String, Integer> map = new IdentityHashMap<>();
		// two keys that are equal but not the same
		map.put(new String("a"), 1);
		map.put(new String("a"), 1);
		map.put("b", 2);
		final Map<String, Integer> actual = MapExtensions.sortByKey(map, Comparator.naturalOrder());
		final Map<String, Integer> expected = MapFactory.newLinkedHashMap();
		expected.put("a", 1);
		expected.put("b", 2);
		assertEquals(expected, actual);
		assertEquals(new ArrayList<>(expected.keySet()), new ArrayList<>(actual.keySet()));
	}

	/**
	 * Test the method {@link MapExtensions#sortByKey(Map, Comparator)}: the entries come in the
	 * order of the keys that the given comparator defines
	 */
	@Test
	public void testSortByKey()
	{
		Map<String, String> map;
		Map<String, String> actual;
		Map<String, String> expected;
		// map with test data ...
		map = new HashMap<>();
		map.put("2", "21");
		map.put("1", "5");
		map.put("4", "13");
		map.put("3", "23");
		// new scenario...
		actual = MapExtensions.sortByKey(map, Comparator.naturalOrder());
		expected = MapFactory.newLinkedHashMap();
		expected.put("1", "5");
		expected.put("2", "21");
		expected.put("3", "23");
		expected.put("4", "13");
		assertEquals(expected, actual);
		assertEquals(new ArrayList<>(expected.keySet()), new ArrayList<>(actual.keySet()));
		// new scenario...
		actual = MapExtensions.sortByKey(map, Comparator.reverseOrder());
		expected = MapFactory.newLinkedHashMap();
		expected.put("4", "13");
		expected.put("3", "23");
		expected.put("2", "21");
		expected.put("1", "5");
		assertEquals(expected, actual);
		assertEquals(new ArrayList<>(expected.keySet()), new ArrayList<>(actual.keySet()));
	}

	/**
	 * Test the method {@link MapExtensions#sortByKey(Map, Comparator)} with integer keys: the
	 * entries come in the order of the keys that the given comparator defines
	 */
	@Test
	public void testSortByKeyWithInteger()
	{
		Map<Integer, String> map;
		Map<Integer, String> actual;
		Map<Integer, String> expected;
		// map with test data ...
		map = new HashMap<>();
		map.put(2, "21");
		map.put(1, "5");
		map.put(4, "13");
		map.put(3, "23");
		// new scenario...
		actual = MapExtensions.sortByKey(map, Comparator.naturalOrder());
		expected = MapFactory.newLinkedHashMap();
		expected.put(1, "5");
		expected.put(2, "21");
		expected.put(3, "23");
		expected.put(4, "13");
		assertEquals(expected, actual);
		assertEquals(new ArrayList<>(expected.keySet()), new ArrayList<>(actual.keySet()));
		// new scenario...
		actual = MapExtensions.sortByKey(map, Comparator.reverseOrder());
		expected = MapFactory.newLinkedHashMap();
		expected.put(4, "13");
		expected.put(3, "23");
		expected.put(2, "21");
		expected.put(1, "5");
		assertEquals(expected, actual);
		assertEquals(new ArrayList<>(expected.keySet()), new ArrayList<>(actual.keySet()));
	}

	/**
	 * Test for the Method {@link MapExtensions#getMaxKey(Map, Comparator)}
	 */
	@Test
	public void testGetMaxKeyByComparator()
	{
		Map<String, String> map;
		String actual;
		String expected;
		// map with test data ...
		map = new HashMap<>();
		map.put("2", "21");
		map.put("1", "5");
		map.put("4", "13");
		map.put("3", "23");
		// new scenario...
		actual = MapExtensions.getMaxKey(map, Comparator.naturalOrder());
		expected = "4";
		assertEquals(expected, actual);
		// new scenario...
		actual = MapExtensions.getMaxKey(map, Comparator.reverseOrder());
		expected = "1";
		assertEquals(expected, actual);
	}

	/**
	 * Test for the Method {@link MapExtensions#getMaxKey(Map, Comparator)}
	 */
	@Test
	public void testGetMaxKeyByComparatorWithInteger()
	{
		Map<Integer, String> map;
		Integer actual;
		Integer expected;
		// map with test data ...
		map = new HashMap<>();
		map.put(2, "21");
		map.put(1, "5");
		map.put(4, "13");
		map.put(3, "23");
		// new scenario...
		actual = MapExtensions.getMaxKey(map, Comparator.naturalOrder());
		expected = 4;
		assertEquals(expected, actual);
		// new scenario...
		actual = MapExtensions.getMaxKey(map, Comparator.reverseOrder());
		expected = 1;
		assertEquals(expected, actual);
	}

	/**
	 * Test for the Method {@link MapExtensions#swap(Map)}
	 */
	@Test
	public void testSwap()
	{
		Map<String, String> map;
		Map<String, String> actual;
		Map<String, String> expected;
		// map with test data ...
		map = new HashMap<>();
		map.put("23", "3");
		map.put("21", "2");
		map.put("13", "4");
		map.put("5", "1");
		// new scenario...
		actual = MapExtensions.swap(map);
		expected = MapFactory.newLinkedHashMap();
		expected.put("1", "5");
		expected.put("2", "21");
		expected.put("3", "23");
		expected.put("4", "13");
		assertEquals(expected, actual);
	}

	/**
	 * Test for the Method {@link MapExtensions#sortByValueAsList(Map, boolean)}
	 */
	@Test
	public void testSortByValueAsList()
	{
		Map<String, String> map;
		List<Entry<String, String>> actual;
		List<Entry<String, String>> expected;
		// map with test data ...
		map = new HashMap<>();
		map.put("23", "3");
		map.put("21", "2");
		map.put("13", "4");
		map.put("5", "1");
		// new scenario...
		actual = MapExtensions.sortByValueAsList(map, false);
		expected = ListFactory.newArrayList();
		expected.add(new AbstractMap.SimpleEntry<>("5", "1"));
		expected.add(new AbstractMap.SimpleEntry<>("21", "2"));
		expected.add(new AbstractMap.SimpleEntry<>("23", "3"));
		expected.add(new AbstractMap.SimpleEntry<>("13", "4"));
		assertEquals(expected, actual);
		// new scenario...
		actual = MapExtensions.sortByValueAsList(map, true);
		expected = ListFactory.newArrayList();
		expected.add(new AbstractMap.SimpleEntry<>("13", "4"));
		expected.add(new AbstractMap.SimpleEntry<>("23", "3"));
		expected.add(new AbstractMap.SimpleEntry<>("21", "2"));
		expected.add(new AbstractMap.SimpleEntry<>("5", "1"));
		assertEquals(expected, actual);
	}

	/**
	 * Test for the Method {@link MapExtensions#sortByValueAsList(Map, Comparator)}
	 */
	@Test
	public void testSortByValueAsListWithComparator()
	{
		Map<String, String> map;
		List<Entry<String, String>> actual;
		List<Entry<String, String>> expected;
		// map with test data ...
		map = new HashMap<>();
		map.put("23", "3");
		map.put("21", "2");
		map.put("13", "4");
		map.put("5", "1");
		// new scenario...
		actual = MapExtensions.sortByValueAsList(map, Comparator.naturalOrder());
		expected = ListFactory.newArrayList();
		expected.add(new AbstractMap.SimpleEntry<>("5", "1"));
		expected.add(new AbstractMap.SimpleEntry<>("21", "2"));
		expected.add(new AbstractMap.SimpleEntry<>("23", "3"));
		expected.add(new AbstractMap.SimpleEntry<>("13", "4"));
		assertEquals(expected, actual);
		// new scenario...
		actual = MapExtensions.sortByValueAsList(map, Comparator.reverseOrder());
		expected = ListFactory.newArrayList();
		expected.add(new AbstractMap.SimpleEntry<>("13", "4"));
		expected.add(new AbstractMap.SimpleEntry<>("23", "3"));
		expected.add(new AbstractMap.SimpleEntry<>("21", "2"));
		expected.add(new AbstractMap.SimpleEntry<>("5", "1"));
		assertEquals(expected, actual);
	}

	/**
	 * Test the method {@link MapExtensions#sortByValue(Map, Comparator)}: the entries come in the
	 * order of the values that the given comparator defines
	 */
	@Test
	public void testSortByValueWithComparator()
	{
		Map<String, String> map;
		Map<String, String> actual;
		Map<String, String> expected;
		// map with test data ...
		map = new HashMap<>();
		map.put("23", "3");
		map.put("21", "2");
		map.put("13", "4");
		map.put("5", "1");
		// new scenario...
		actual = MapExtensions.sortByValue(map, Comparator.naturalOrder());
		expected = MapFactory.newLinkedHashMap();
		expected.put("5", "1");
		expected.put("21", "2");
		expected.put("23", "3");
		expected.put("13", "4");
		assertEquals(expected, actual);
		assertEquals(new ArrayList<>(expected.keySet()), new ArrayList<>(actual.keySet()));
		// new scenario...
		actual = MapExtensions.sortByValue(map, Comparator.reverseOrder());
		expected = MapFactory.newLinkedHashMap();
		expected.put("13", "4");
		expected.put("23", "3");
		expected.put("21", "2");
		expected.put("5", "1");
		assertEquals(expected, actual);
		assertEquals(new ArrayList<>(expected.keySet()), new ArrayList<>(actual.keySet()));
	}

	/**
	 * Test for the Method {@link MapExtensions#toGenericMap(Object[][])}.
	 */
	@Test
	public void testToGenericMap()
	{
		final String[][] twoDimArray = { { "1", "value1" }, { "3", "value3" }, { "4", "value4" },
				{ "2", "value2" } };
		final Map<String, String> map = MapExtensions.toGenericMap(twoDimArray);
		assertMapToArray(map, twoDimArray);
	}

	/**
	 * Test the method {@link MapExtensions#toMap(String[][])}: the map holds one entry for each row
	 * of the array, in the order of the rows, and nothing for an empty array
	 */
	@Test
	public void testToMap()
	{
		final String[][] twoDimArray = { { "1", "value1" }, { "3", "value3" }, { "4", "value4" },
				{ "2", "value2" } };
		final Map<String, String> map = MapExtensions.toMap(twoDimArray);
		assertMapToArray(map, twoDimArray);
		// new scenario...
		assertTrue(MapExtensions.toMap(new String[0][]).isEmpty());
	}

	/**
	 * Test for the Method {@link MapExtensions#toStringObjectMap(Object[][])}
	 */
	@Test
	public void testToTwoDimensionalArrayStringObject()
	{
		final Object[][] twoDimArray = { { "1", Boolean.TRUE }, { "3", Boolean.FALSE },
				{ "4", Boolean.TRUE }, { "2", Boolean.TRUE } };
		final Map<String, Object> map = MapExtensions.toStringObjectMap(twoDimArray);

		Object[][] toTwoDimensionalArray = ArrayExtensions.toTwoDimensionalArray(map);
		assertArrayEquals(toTwoDimensionalArray, twoDimArray);
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
