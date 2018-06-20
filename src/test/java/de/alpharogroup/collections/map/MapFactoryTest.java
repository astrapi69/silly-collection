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

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

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
	 * Test for the Method {@link MapFactory#newInsertionOrderMap()}.
	 */
	@Test
	public void testNewInsertionOrderMap() throws Exception
	{
		final Map<Integer, String> map = MapFactory.<Integer, String> newInsertionOrderMap();
		assertNotNull(map);
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
	 * Test method for {@link MapFactory} with {@link BeanTester}
	 */
	@Test(expectedExceptions = { BeanTestException.class, InvocationTargetException.class,
			UnsupportedOperationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(MapFactory.class);
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
		assertTrue(insertionOrderMap.size()==4);
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
		assertTrue(concurrentHashMap.size()==4);
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
		assertTrue(concurrentHashMap.size()==4);
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
		assertTrue(concurrentHashMap.size()==4);
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
		assertTrue(concurrentHashMap.size()==4);
	}

}
