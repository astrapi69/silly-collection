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
package io.github.astrapi69.collections.map;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import de.alpharogroup.collections.pairs.KeyValuePair;
import io.github.astrapi69.collections.list.ListFactory;

/**
 * The unit test class for the class {@link MapFactory}.
 *
 * @author Asterios Raptis
 */
public class MapFactoryTest
{

	/**
	 * Test for the Method {@link MapFactory#newAssosiativeArrayMap()}. <br>
	 * Here an example:<br>
	 * <br>
	 * in js you can create and fetch associative arrays like this:<br>
	 * <br>
	 * $arrayObj[0]['firstName'] = 'Albert';<br>
	 * $arrayObj[0]['lastName'] = 'Einstein';<br>
	 * $arrayObj[1]['firstName'] = 'Neil';<br>
	 * $arrayObj[1]['lastName'] = 'Armstrong';<br>
	 * <br>
	 * to do the same in java we can do as the following code: final Map<Integer, Map<String,
	 * String>> arrayMap = MapFactory.newAssosiativeArrayMap();<br>
	 * <br>
	 * arrayMap.get(0).put("firstName", "Albert");<br>
	 * arrayMap.get(0).put("lastName", "Einstein");<br>
	 * arrayMap.get(1).put("firstName", "Neil");<br>
	 * arrayMap.get(1).put("lastName", "Armstrong");<br>
	 */
	@Test
	public void testNewAssosiativeArrayMap()
	{
		final Map<Integer, Map<String, String>> arrayMap = MapFactory.newAssosiativeArrayMap();

		arrayMap.get(0).put("firstName", "Albert");
		arrayMap.get(0).put("lastName", "Einstein");
		arrayMap.get(1).put("firstName", "Neil");
		arrayMap.get(1).put("lastName", "Armstrong");

		String expected = "Albert";
		String actual = arrayMap.get(0).get("firstName");
		assertEquals(expected, actual);

		expected = "Einstein";
		actual = arrayMap.get(0).get("lastName");
		assertEquals(expected, actual);

		expected = "Neil";
		actual = arrayMap.get(1).get("firstName");
		assertEquals(expected, actual);

		expected = "Armstrong";
		actual = arrayMap.get(1).get("lastName");
		assertEquals(expected, actual);

		expected = null;
		actual = arrayMap.get(2).get("firstName");
		assertEquals(expected, actual);
	}

	/**
	 * Test for the Method {@link MapFactory#newConcurrentHashMap()}.
	 */
	@Test
	public void testNewConcurrentHashMap()
	{
		final ConcurrentHashMap<Object, Object> concurrentHashMap = MapFactory
			.newConcurrentHashMap();
		assertNotNull(concurrentHashMap);
	}

	/**
	 * Test for the Method {@link MapFactory#newConcurrentHashMap(int)}.
	 */
	@Test
	public void testNewConcurrentHashMapInt()
	{
		final ConcurrentHashMap<Object, Object> concurrentHashMap = MapFactory
			.newConcurrentHashMap(5);
		assertNotNull(concurrentHashMap);
	}

	/**
	 * Test method for {@link MapFactory#newConcurrentHashMap(Map)}.
	 */
	@Test
	public void testNewConcurrentHashMapMapOfKV()
	{
		final Map<String, String> map = new HashMap<>();
		map.put("1", "novalue");
		map.put("2", "somevalue");
		map.put("3", "othervalue");
		map.put("5", "value");
		Map<String, String> concurrentHashMap = MapFactory.newConcurrentHashMap(map);
		assertNotNull(concurrentHashMap);
		assertTrue(concurrentHashMap.size() == 4);
	}

	/**
	 * Test for the Method {@link MapFactory#newCounterMap(java.util.Collection)}
	 */
	@Test
	public void testNewCounterMap()
	{
		Map<Integer, Integer> actual;
		Map<Integer, Integer> expected;
		List<Integer> rangeList;
		// new scenario...
		rangeList = ListFactory.newRangeList(1, 5);
		actual = MapFactory.newCounterMap(rangeList);
		expected = MapFactory.newHashMap();
		expected.put(1, 0);
		expected.put(2, 0);
		expected.put(3, 0);
		expected.put(4, 0);
		expected.put(5, 0);
		assertEquals(expected, actual);
		// new scenario...
		rangeList = ListFactory.newArrayList(1, 2, 3, 4, 5, 5);
		actual = MapFactory.newCounterMap(rangeList);
		expected.put(5, 1);
		assertEquals(expected, actual);
	}

	/**
	 * Test for the Method {@link MapFactory#newHashMap()}.
	 */
	@Test
	public void testNewHashMap()
	{
		final Map<Object, Object> hashMap = MapFactory.newHashMap();
		assertNotNull(hashMap);
	}

	/**
	 * Test for the Method {@link MapFactory#newHashMap(int))}.
	 */
	@Test
	public void testNewHashMapInt()
	{
		final Map<Object, Object> hashMap = MapFactory.newHashMap(5);
		assertNotNull(hashMap);
	}

	/**
	 * Test method for {@link MapFactory#newHashMap(java.util.Collection)}
	 */
	@Test
	public void testNewHashMapListOfKeyValuePairs()
	{
		List<KeyValuePair<String, String>> keyValuePairs;
		Map<String, String> map;
		Map<String, String> hashMap;

		map = new HashMap<>();
		map.put("1", "novalue");
		map.put("2", "somevalue");
		map.put("3", "othervalue");
		map.put("5", "value");
		keyValuePairs = KeyValuePair.toKeyValuePairs(map);
		hashMap = MapFactory.newHashMap(keyValuePairs);
		assertNotNull(hashMap);
		assertTrue(hashMap.size() == 4);

		keyValuePairs = ListFactory.newArrayList(
			KeyValuePair.<String, String> builder().key("1").value("novalue").build(),
			KeyValuePair.<String, String> builder().key("2").value("somevalue").build(),
			KeyValuePair.<String, String> builder().key("3").value("othervalue").build(),
			KeyValuePair.<String, String> builder().key("5").value("value").build());
		hashMap = MapFactory.newHashMap(keyValuePairs);
		assertNotNull(hashMap);
		assertTrue(hashMap.size() == 4);

	}

	/**
	 * Test method for {@link MapFactory#newHashMap(Map)}.
	 */
	@Test
	public void testNewHashMapMapOfKV()
	{
		final Map<String, String> map = new HashMap<>();
		map.put("1", "novalue");
		map.put("2", "somevalue");
		map.put("3", "othervalue");
		map.put("5", "value");
		Map<String, String> concurrentHashMap = MapFactory.newHashMap(map);
		assertNotNull(concurrentHashMap);
		assertTrue(concurrentHashMap.size() == 4);
	}

	/**
	 * Test method for {@link MapFactory#newHashMap(KeyValuePair[])}
	 */
	@Test
	public void testNewHashMapObjects()
	{
		Map<String, String> hashMap;

		hashMap = MapFactory.newHashMap(
			KeyValuePair.<String, String> builder().key("1").value("novalue").build(),
			KeyValuePair.<String, String> builder().key("2").value("somevalue").build(),
			KeyValuePair.<String, String> builder().key("3").value("othervalue").build(),
			KeyValuePair.<String, String> builder().key("5").value("value").build());
		assertNotNull(hashMap);
		assertTrue(hashMap.size() == 4);
	}

	/**
	 * Test for the Method {@link MapFactory#newInsertionOrderMap()}.
	 */
	@Test
	public void testNewInsertionOrderMap() throws Exception
	{
		final Map<Integer, String> map = MapFactory.<Integer, String> newInsertionOrderMap();
		assertNotNull(map);
	}

	/**
	 * Test method for {@link MapFactory#newInsertionOrderMap(int)}.
	 */
	@Test
	public void testNewInsertionOrderMapInt()
	{
		Map<String, String> insertionOrderMap = MapFactory.newInsertionOrderMap(4);
		assertNotNull(insertionOrderMap);
	}

	/**
	 * Test method for {@link MapFactory#newInsertionOrderMap(java.util.Collection)}
	 */
	@Test
	public void testNewInsertionOrderMapListOfKeyValuePairs()
	{
		List<KeyValuePair<String, String>> keyValuePairs;
		Map<String, String> map;
		Map<String, String> insertionOrderMap;

		map = new HashMap<>();
		map.put("1", "novalue");
		map.put("2", "somevalue");
		map.put("3", "othervalue");
		map.put("5", "value");
		keyValuePairs = KeyValuePair.toKeyValuePairs(map);
		insertionOrderMap = MapFactory.newInsertionOrderMap(keyValuePairs);
		assertNotNull(insertionOrderMap);
		assertTrue(insertionOrderMap.size() == 4);

		keyValuePairs = ListFactory.newArrayList(
			KeyValuePair.<String, String> builder().key("1").value("novalue").build(),
			KeyValuePair.<String, String> builder().key("2").value("somevalue").build(),
			KeyValuePair.<String, String> builder().key("3").value("othervalue").build(),
			KeyValuePair.<String, String> builder().key("5").value("value").build());
		insertionOrderMap = MapFactory.newInsertionOrderMap(keyValuePairs);
		assertNotNull(insertionOrderMap);
		assertTrue(insertionOrderMap.size() == 4);
	}

	/**
	 * Test method for {@link MapFactory#newInsertionOrderMap(Map)}.
	 */
	@Test
	public void testNewInsertionOrderMapMapOfKV()
	{
		final Map<String, String> map = new HashMap<>();
		map.put("1", "novalue");
		map.put("2", "somevalue");
		map.put("3", "othervalue");
		map.put("5", "value");
		Map<String, String> insertionOrderMap = MapFactory.newInsertionOrderMap(map);
		assertNotNull(insertionOrderMap);
		assertTrue(insertionOrderMap.size() == 4);
	}

	/**
	 * Test method for {@link MapFactory#newInsertionOrderMap(KeyValuePair[])}
	 */
	@Test
	public void testNewInsertionOrderMapObjects()
	{
		Map<String, String> insertionOrderMap;

		insertionOrderMap = MapFactory.newInsertionOrderMap(
			KeyValuePair.<String, String> builder().key("1").value("novalue").build(),
			KeyValuePair.<String, String> builder().key("2").value("somevalue").build(),
			KeyValuePair.<String, String> builder().key("3").value("othervalue").build(),
			KeyValuePair.<String, String> builder().key("5").value("value").build());
		assertNotNull(insertionOrderMap);
		assertTrue(insertionOrderMap.size() == 4);
	}

	/**
	 * Test method for {@link MapFactory#newLazyHashMap(HashMap)}
	 */
	@Test
	public void testNewLazyHashMap()
	{
		final HashMap<String, String> map = new HashMap<>();
		map.put("1", "novalue");
		map.put("2", "somevalue");
		map.put("3", "othervalue");
		map.put("5", "value");
		Map<String, String> lazyMap = MapFactory.newLazyHashMap(map);
		assertNotNull(lazyMap);
	}

	/**
	 * Test for the Method {@link MapFactory#newLazyTreeMap()}.
	 */
	@Test
	public void testNewLazyTreeMap() throws Exception
	{
		final Map<Integer, String> treeMap = MapFactory.<Integer, String> newLazyTreeMap();
		assertNotNull(treeMap);
	}

	/**
	 * Test for the Method {@link MapFactory#newLazyTreeMap(Comparator)}.
	 */
	@Test
	public void testNewLazyTreeMapComparator() throws Exception
	{
		final Map<Integer, String> treeMap = MapFactory
			.<Integer, String> newLazyTreeMap((o1, o2) -> o1 - o2);
		assertNotNull(treeMap);
	}

	/**
	 * Test method for {@link MapFactory#newLazyTreeMap(TreeMap)}.
	 */
	@Test
	public void testNewLazyTreeMapTreeMap()
	{
		final TreeMap<String, String> map = new TreeMap<>();
		map.put("1", "novalue");
		map.put("2", "somevalue");
		map.put("3", "othervalue");
		map.put("5", "value");
		Map<String, String> lazyMap = MapFactory.newLazyTreeMap(map);
		assertNotNull(lazyMap);
	}

	/**
	 * Test for the Method {@link MapFactory#newLinkedHashMap()}.
	 */
	@Test
	public void testNewLinkedHashMap()
	{
		final Map<Object, Object> hashMap = MapFactory.newLinkedHashMap();
		assertNotNull(hashMap);
	}


	/**
	 * Test for the Method {@link MapFactory#newLinkedHashMap(int)}.
	 */
	@Test
	public void testNewLinkedHashMapInt()
	{
		final Map<Object, Object> hashMap = MapFactory.newLinkedHashMap(5);
		assertNotNull(hashMap);
	}

	/**
	 * Test method for {@link MapFactory#newLinkedHashMap(java.util.Collection)}
	 */
	@Test
	public void testNewLinkedHashMapListOfKeyValuePairs()
	{
		List<KeyValuePair<String, String>> keyValuePairs;
		Map<String, String> map;
		Map<String, String> hashMap;

		map = new HashMap<>();
		map.put("1", "novalue");
		map.put("2", "somevalue");
		map.put("3", "othervalue");
		map.put("5", "value");
		keyValuePairs = KeyValuePair.toKeyValuePairs(map);
		hashMap = MapFactory.newLinkedHashMap(keyValuePairs);
		assertNotNull(hashMap);
		assertTrue(hashMap.size() == 4);

		keyValuePairs = ListFactory.newArrayList(
			KeyValuePair.<String, String> builder().key("1").value("novalue").build(),
			KeyValuePair.<String, String> builder().key("2").value("somevalue").build(),
			KeyValuePair.<String, String> builder().key("3").value("othervalue").build(),
			KeyValuePair.<String, String> builder().key("5").value("value").build());
		hashMap = MapFactory.newLinkedHashMap(keyValuePairs);
		assertNotNull(hashMap);
		assertTrue(hashMap.size() == 4);

	}

	/**
	 * Test method for {@link MapFactory#newLinkedHashMap(Map)}.
	 */
	@Test
	public void testNewLinkedHashMapMapOfKV()
	{
		final Map<String, String> map = new HashMap<>();
		map.put("1", "novalue");
		map.put("2", "somevalue");
		map.put("3", "othervalue");
		map.put("5", "value");
		Map<String, String> concurrentHashMap = MapFactory.newLinkedHashMap(map);
		assertNotNull(concurrentHashMap);
		assertTrue(concurrentHashMap.size() == 4);
	}

	/**
	 * Test method for {@link MapFactory#newLinkedHashMap(KeyValuePair[])}
	 */
	@Test
	public void testNewLinkedHashMapObjects()
	{
		Map<String, String> hashMap;

		hashMap = MapFactory.newLinkedHashMap(
			KeyValuePair.<String, String> builder().key("1").value("novalue").build(),
			KeyValuePair.<String, String> builder().key("2").value("somevalue").build(),
			KeyValuePair.<String, String> builder().key("3").value("othervalue").build(),
			KeyValuePair.<String, String> builder().key("5").value("value").build());
		assertNotNull(hashMap);
		assertTrue(hashMap.size() == 4);
	}

	/**
	 * Test method for {@link MapFactory#newNumberCounterMap(int, int)}
	 */
	@Test
	public void testNewNumberCounterMap()
	{
		int minVolume;
		int maxVolume;
		minVolume = 1;
		maxVolume = 10;
		Map<Integer, Integer> numberCounterMap = MapFactory.newNumberCounterMap(minVolume,
			maxVolume);
		assertNotNull(numberCounterMap);
		assertEquals(numberCounterMap.size(), maxVolume);
	}

	/**
	 * Test method for {@link MapFactory#newNumberCounterMap(int, int)}
	 */
	@Test
	public void testNewNumberCounterMapIntIntMap()
	{
		int minVolume;
		int maxVolume;
		Map<Integer, Integer> initialNumberCounterMap;
		Map<Integer, Integer> numberCounterMap;

		minVolume = 1;
		maxVolume = 10;
		initialNumberCounterMap = MapFactory.newNumberCounterMap(minVolume, maxVolume);
		for (int i = minVolume; i <= maxVolume; i++)
		{
			initialNumberCounterMap.merge(i, 1 + new Random().nextInt() * 4, Integer::sum);
		}
		numberCounterMap = MapFactory.newNumberCounterMap(minVolume, maxVolume,
			initialNumberCounterMap);
		assertNotNull(numberCounterMap);
		assertEquals(numberCounterMap.size(), maxVolume);
	}

	/**
	 * Test for the Method {@link MapFactory#newTreeMap()}.
	 */
	@Test
	public void testNewTreeMap()
	{
		final Map<Integer, String> treeMap = MapFactory.<Integer, String> newTreeMap();
		assertNotNull(treeMap);
	}

	/**
	 * Test for the Method {@link MapFactory#newTreeMap(Comparator)}.
	 */
	@Test
	public void testNewTreeMapComparator()
	{
		final Map<Integer, String> treeMap = MapFactory
			.<Integer, String> newTreeMap((o1, o2) -> o1 - o2);
		assertNotNull(treeMap);
	}

	/**
	 * Test method for {@link MapFactory#newTreeMap(Comparator, java.util.Collection)}
	 */
	@Test
	public void testNewTreeMapComparatorListOfKeyValuePairs()
	{
		List<KeyValuePair<Integer, String>> keyValuePairs;
		Map<Integer, String> map;
		Map<Integer, String> treeMap;
		Comparator<Integer> comparator;

		map = new HashMap<>();
		map.put(1, "novalue");
		map.put(2, "somevalue");
		map.put(3, "othervalue");
		map.put(5, "value");
		keyValuePairs = KeyValuePair.toKeyValuePairs(map);
		comparator = (o1, o2) -> o1 - o2;
		treeMap = MapFactory.newTreeMap(comparator, keyValuePairs);
		assertNotNull(treeMap);
		assertTrue(treeMap.size() == 4);

		keyValuePairs = ListFactory.newArrayList(
			KeyValuePair.<Integer, String> builder().key(1).value("novalue").build(),
			KeyValuePair.<Integer, String> builder().key(2).value("somevalue").build(),
			KeyValuePair.<Integer, String> builder().key(3).value("othervalue").build(),
			KeyValuePair.<Integer, String> builder().key(5).value("value").build());
		treeMap = MapFactory.newTreeMap(comparator, keyValuePairs);
		assertNotNull(treeMap);
		assertTrue(treeMap.size() == 4);
	}

	/**
	 * Test method for {@link MapFactory#newTreeMap(Map)}.
	 */
	@Test
	public void testNewTreeMapMapOfKV()
	{
		final Map<String, String> map = new HashMap<>();
		map.put("1", "novalue");
		map.put("2", "somevalue");
		map.put("3", "othervalue");
		map.put("5", "value");
		Map<String, String> concurrentHashMap = MapFactory.newTreeMap(map);
		assertNotNull(concurrentHashMap);
		assertTrue(concurrentHashMap.size() == 4);
	}

	/**
	 * Test method for {@link MapFactory#newTreeMap(Comparator, KeyValuePair...)}
	 */
	@Test
	public void testNewTreeMapObjects()
	{
		Map<String, String> treeMap;
		Comparator<String> comparator;

		comparator = (o1, o2) -> o1.compareTo(o2);
		treeMap = MapFactory.newTreeMap(comparator,
			KeyValuePair.<String, String> builder().key("1").value("novalue").build(),
			KeyValuePair.<String, String> builder().key("2").value("somevalue").build(),
			KeyValuePair.<String, String> builder().key("3").value("othervalue").build(),
			KeyValuePair.<String, String> builder().key("5").value("value").build());
		assertNotNull(treeMap);
		assertTrue(treeMap.size() == 4);
	}

	/**
	 * Test method for {@link MapFactory} with {@link BeanTester}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(MapFactory.class);
	}

}
