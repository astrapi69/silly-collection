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

import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.testng.annotations.Test;

/**
 * The unit test class for the class {@link KeyValuePair}
 */
public class KeyValuePairTest
{

	/**
	 * Test method for {@link KeyValuePair#toKeyValuePairs(Map)}.
	 */
	@Test
	public void testToKeyValuePairsMap()
	{
		final Map<String, String> map = new HashMap<>();
		map.put("1", "novalue");
		map.put("2", "somevalue");
		map.put("3", "othervalue");
		map.put("4", "value");

		List<KeyValuePair<String, String>> list = KeyValuePair.toKeyValuePairs(map);

		assertNotNull(list);
		assertTrue(list.size() == 4);
	}

	/**
	 * Test method for {@link KeyValuePair#toKeyValuePairs(Properties)}.
	 */
	@Test
	public void testToKeyValuePairsProperties()
	{
		String key;
		String value;
		Properties properties = new Properties();

		key = "foo";
		value = "bar";
		properties.setProperty(key, value);

		key = "bla";
		value = "fasel";
		properties.setProperty(key, value);

		List<KeyValuePair<String, String>> objectList = KeyValuePair.toKeyValuePairs(properties);

		assertNotNull(objectList);
		assertTrue(objectList.size() == 2);
	}

	/**
	 * Test method for {@link KeyValuePair#toMap(java.util.Collection)}.
	 */
	@Test
	public void testToMap()
	{
		List<KeyValuePair<String, String>> list = new ArrayList<>();
		list.add(KeyValuePair.<String, String> builder().key("1").value("novalue").build());
		list.add(KeyValuePair.<String, String> builder().key("2").value("somevalue").build());
		list.add(KeyValuePair.<String, String> builder().key("3").value("othervalue").build());
		list.add(KeyValuePair.<String, String> builder().key("4").value("value").build());

		Map<String, String> map = KeyValuePair.toMap(list);

		assertNotNull(map);
		assertTrue(map.size() == 4);
	}

}
