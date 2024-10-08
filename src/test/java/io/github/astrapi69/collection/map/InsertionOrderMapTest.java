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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

/**
 * The unit test class for the class {@link InsertionOrderMap}.
 *
 * @author Asterios Raptis
 */
public class InsertionOrderMapTest
{

	/**
	 * Test the no argument constructor of {@link InsertionOrderMap}.
	 */
	@Test
	public void testInsertionOrderMap()
	{
		final String[][] expected = { { "1", "value1" }, { "3", "value3" }, { "4", "value4" },
				{ "2", "value5" } };
		int count = 0;
		final Map<String, String> map = new InsertionOrderMap<>();

		// Add some elements
		map.put("1", "value1");
		map.put("2", "value2");
		map.put("3", "value3");
		map.put("4", "value4");
		/*
		 * Note that insertion order is affected if a key is re-inserted into the map.
		 */
		map.put("2", "value5");
		for (final Entry<String, String> entry : map.entrySet())
		{
			final Object key = entry.getKey();
			final Object value = entry.getValue();
			final String expectedKey = expected[count][0];
			final String expectedValue = expected[count][1];
			assertEquals(key.toString(), expectedKey);
			assertEquals(value.toString(), expectedValue);
			count++;
		}
		map.forEach((key, value) -> System.out.println("[" + key + "=" + value + "]"));
		map.clear();
	}

	/**
	 * Test the constructor with the initial capacity of {@link InsertionOrderMap}.
	 */
	@Test
	public void testInsertionOrderMapInt()
	{
		/* Shows that the elements are ordered in insertion-order from the Map. */
		final Map<String, String> map = new InsertionOrderMap<>(4);

		// Add some elements
		map.put("1", "value1");
		map.put("2", "value2");
		map.put("3", "value3");
		map.put("4", "value4");
		final String[][] expected = { { "1", "value1" }, { "2", "value2" }, { "3", "value3" },
				{ "4", "value4" } };
		int count = 0;
		for (final Entry<String, String> entry : map.entrySet())
		{
			final Object key = entry.getKey();
			final Object value = entry.getValue();
			final String expectedKey = expected[count][0];
			final String expectedValue = expected[count][1];
			assertEquals(key.toString(), expectedKey);
			assertEquals(value.toString(), expectedValue);
			count++;
		}
		map.clear();
	}

	/**
	 * Test the constructor with the initial capacity and the load factor of
	 * {@link InsertionOrderMap}.
	 */
	@Test
	public void testInsertionOrderMapIntFloat()
	{
		/* Shows that the elements are ordered in insertion-order from the Map. */
		final Map<String, String> map = new InsertionOrderMap<>(4, 4f);

		// Add some elements
		map.put("1", "value1");
		map.put("2", "value2");
		map.put("3", "value3");
		map.put("4", "value4");
		final String[][] expected = { { "1", "value1" }, { "2", "value2" }, { "3", "value3" },
				{ "4", "value4" } };
		int count = 0;
		for (final Entry<String, String> entry : map.entrySet())
		{
			final Object key = entry.getKey();
			final Object value = entry.getValue();
			final String expectedKey = expected[count][0];
			final String expectedValue = expected[count][1];
			assertEquals(key.toString(), expectedKey);
			assertEquals(value.toString(), expectedValue);
			count++;
		}
		map.clear();
	}

	/**
	 * Test the constructor with the initial capacity, load factor and the ordering mode of
	 * {@link InsertionOrderMap}.
	 */
	@Test
	public void testInsertionOrderMapIntFloatBoolean()
	{
		/* Shows that the elements are ordered in insertion-order from the Map. */
		final Map<String, String> map = new InsertionOrderMap<>(4, 4f, true);

		// Add some elements
		map.put("1", "value1");
		map.put("2", "value2");
		map.put("3", "value3");
		map.put("4", "value4");
		final String[][] expected = { { "1", "value1" }, { "2", "value2" }, { "3", "value3" },
				{ "4", "value4" } };
		int count = 0;
		for (final Entry<String, String> entry : map.entrySet())
		{
			final Object key = entry.getKey();
			final Object value = entry.getValue();
			final String expectedKey = expected[count][0];
			final String expectedValue = expected[count][1];
			assertEquals(key.toString(), expectedKey);
			assertEquals(value.toString(), expectedValue);
			count++;
		}
		map.clear();
	}

	/**
	 * Test the constructor with a map of {@link InsertionOrderMap}.
	 */
	@Test
	public void testInsertionOrderMapMapOfQextendsKQextendsV()
	{
		final Map<String, String> map3 = new HashMap<>();
		// Add some elements
		map3.put("1", "value1");
		map3.put("2", "value2");
		map3.put("3", "value3");
		map3.put("4", "value4");
		/* Shows that the elements are ordered in insertion-order from the Map. */
		final Map<String, String> map = new InsertionOrderMap<>(map3);

		final String[][] expected = { { "1", "value1" }, { "2", "value2" }, { "3", "value3" },
				{ "4", "value4" } };
		int count = 0;
		for (final Entry<String, String> entry : map.entrySet())
		{
			final Object key = entry.getKey();
			final Object value = entry.getValue();
			final String expectedKey = expected[count][0];
			final String expectedValue = expected[count][1];
			assertEquals(key.toString(), expectedKey);
			assertEquals(value.toString(), expectedValue);
			count++;

		}
		map.clear();
	}

	/**
	 * Test the LinkedHashMap to show the difference.
	 */
	@Test
	public void testLinkedHashMap()
	{
		/* Shows that the elements are ordered in insertion-order from the Map. */
		final Map<String, String> map = new LinkedHashMap<>();

		// Add some elements
		map.put("1", "value1");
		map.put("2", "value2");
		map.put("3", "value3");
		map.put("4", "value4");
		final String[][] expected = { { "1", "value1" }, { "2", "value2" }, { "3", "value3" },
				{ "4", "value4" } };
		int count = 0;
		for (final Entry<String, String> entry : map.entrySet())
		{
			final Object key = entry.getKey();
			final Object value = entry.getValue();
			final String expectedKey = expected[count][0];
			final String expectedValue = expected[count][1];
			assertEquals(key.toString(), expectedKey);
			assertEquals(value.toString(), expectedValue);
			count++;

		}
		map.clear();
		/*
		 * Note that insertion order is not affected if a key is re-inserted into the map.
		 */

		final Map<String, String> map2 = new LinkedHashMap<>();

		// Add some elements
		map2.put("1", "value1");
		map2.put("2", "value2");
		map2.put("3", "value3");
		map2.put("4", "value4");
		map2.put("2", "value5");
		expected[1][1] = "value5";
		count = 0;
		for (final Entry<String, String> entry : map2.entrySet())
		{
			final Object key = entry.getKey();
			final Object value = entry.getValue();
			final String expectedKey = expected[count][0];
			final String expectedValue = expected[count][1];
			assertEquals(key.toString(), expectedKey);
			assertEquals(value.toString(), expectedValue);
			count++;
		}
		map2.clear();
		/*
		 * Constructs an HashMap instance with the same mappings as the specified map.
		 */
		final Map<String, String> map3 = new HashMap<>();
		// Add some elements
		map3.put("1", "value1");
		map3.put("2", "value2");
		map3.put("3", "value3");
		map3.put("4", "value4");
		map3.put("2", "value5");
		System.out.println("----------initHashMap--------------");

		map3.forEach((key, value) -> System.out.println("[" + key + "=" + value + "]"));

		System.out.println("------------------------");
		System.out.println("----------Map linkedMap = new LinkedHashMap(map3);--------------");
		final Map<String, String> linkedMap = new LinkedHashMap<>(map3);

		linkedMap.forEach((key, value) -> System.out.println("[" + key + "=" + value + "]"));

		System.out.println("---------initInsertionOrderMap---------------");
		map3.clear();
	}

}
