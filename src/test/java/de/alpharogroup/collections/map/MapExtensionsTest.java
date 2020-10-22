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

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import de.alpharogroup.collections.list.ListFactory;
import de.alpharogroup.collections.set.SetFactory;

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
		for (final String key : foundedKeys)
		{
			assertTrue("Key should be in the expected List.", expected.contains(key));

		}
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
		minVolume = 1;
		maxVolume = 10;
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
		minVolume = 1;
		maxVolume = 10;
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

        mergedMap = MapExtensions.mergeAndSummarize(mergedMap, summarizeWith,
                fullMerge);
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
	 * Test for the Method {@link MapExtensions#sortByValue(Map, boolean)}
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
		// new scenario...
		actual = MapExtensions.sortByValue(map, true);
		expected = MapFactory.newLinkedHashMap();
		expected.put("13", "4");
		expected.put("23", "3");
		expected.put("21", "2");
		expected.put("5", "1");
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
	 * Test for the Method {@link MapExtensions#sortByValue(Map, Comparator)}
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
		// new scenario...
		actual = MapExtensions.sortByValue(map, Comparator.reverseOrder());
		expected = MapFactory.newLinkedHashMap();
		expected.put("13", "4");
		expected.put("23", "3");
		expected.put("21", "2");
		expected.put("5", "1");
		assertEquals(expected, actual);
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
	 * Test for the Method {@link MapExtensions#toMap(String[][])}.
	 */
	@Test
	public void testToMap()
	{
		final String[][] twoDimArray = { { "1", "value1" }, { "3", "value3" }, { "4", "value4" },
				{ "2", "value2" } };
		final Map<String, String> map = MapExtensions.toMap(twoDimArray);
		assertMapToArray(map, twoDimArray);
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
