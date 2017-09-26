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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.alpharogroup.collections.map.MapExtensions;

/**
 * Test class for the class {@link MapExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class MapExtensionsTest
{

	/**
	 * Sets up method will be invoked before every unit test method in this class.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@BeforeMethod
	protected void setUp() throws Exception
	{
	}

	/**
	 * Tear down method will be invoked after every unit test method in this class.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@AfterMethod
	protected void tearDown() throws Exception
	{
	}

	/**
	 * Test for the Method {@link MapExtensions#getKeyFromValue(Map, Object)}.
	 */
	@Test
	public void testGetKeyFromValue()
	{
		final String value = "value";
		final String expected = "5";
		final Map<String, String> map = new HashMap<>();
		map.put("1", "novalue");
		map.put("2", "somevalue");
		map.put("3", "othervalue");
		map.put("5", "value");
		final String foundedKey = MapExtensions.getKeyFromValue(map, value);
		AssertJUnit.assertTrue("Expected value is not equal with key.",
			foundedKey.equals(expected));

	}

	/**
	 * Test for the Method {@link MapExtensions#getKeysFromValue(Map, Object)}.
	 */
	@Test
	public void testGetKeysFromValue()
	{
		final String value = "value";
		final List<String> expected = new ArrayList<>();
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
			AssertJUnit.assertTrue("Key should be in the expected List.", expected.contains(key));

		}
	}

	/**
	 * Test for the Method {@link MapExtensions#newAssosiativeArrayMap()}.
	 */
	@Test
	public void testNewAssosiativeArrayMap()
	{
		// in js you can create and fetch associative arrays like this:
		// $arrayObj[0]['firstName'] = 'Albert';
		// $arrayObj[0]['lastName'] = 'Einstein';
		// $arrayObj[1]['firstName'] = 'Neil';
		// $arrayObj[1]['lastName'] = 'Armstrong';
		// to do the same in java we can do as the following code:
		final Map<Integer, Map<String, String>> arrayMap = MapExtensions.newAssosiativeArrayMap();

		arrayMap.get(0).put("firstName", "Albert");
		arrayMap.get(0).put("lastName", "Einstein");
		arrayMap.get(1).put("firstName", "Neil");
		arrayMap.get(1).put("lastName", "Armstrong");

		String expected = "Albert";
		String actual = arrayMap.get(0).get("firstName");
		AssertJUnit.assertEquals(expected, actual);

		expected = "Einstein";
		actual = arrayMap.get(0).get("lastName");
		AssertJUnit.assertEquals(expected, actual);

		expected = "Neil";
		actual = arrayMap.get(1).get("firstName");
		AssertJUnit.assertEquals(expected, actual);

		expected = "Armstrong";
		actual = arrayMap.get(1).get("lastName");
		AssertJUnit.assertEquals(expected, actual);

		expected = null;
		actual = arrayMap.get(2).get("firstName");
		AssertJUnit.assertEquals(expected, actual);
	}


	/**
	 * Test for the Method {@link MapExtensions#toMap(String[][])}.
	 */
	public void testToMap()
	{
		final String twoDimArray[][] = { { "1", "value1" }, { "3", "value3" }, { "4", "value4" },
				{ "2", "value2" } };
		final Map<String, String> map = MapExtensions.toMap(twoDimArray);
		int count = 0;
		for (final Entry<String, String> entry : map.entrySet())
		{
			final String key = entry.getKey();
			final String value = entry.getValue();
			AssertJUnit.assertTrue(key.equals(twoDimArray[count][0]));
			AssertJUnit.assertTrue(value.equals(twoDimArray[count][1]));
			count++;
		}
	}
}
