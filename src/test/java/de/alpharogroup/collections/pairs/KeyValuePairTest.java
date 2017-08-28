package de.alpharogroup.collections.pairs;

import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.testng.annotations.Test;


public class KeyValuePairTest
{

	@Test
	public void testToKeyValuePairsMap()
	{
		final Map<String, String> map = new HashMap<>();
		map.put("1", "novalue");
		map.put("2", "somevalue");
		map.put("3", "othervalue");
		map.put("5", "value");

		List<KeyValuePair<String, String>> list = KeyValuePair.toKeyValuePairs(map);

		assertNotNull(list);
		assertTrue(list.size() == 4);
	}

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

}
