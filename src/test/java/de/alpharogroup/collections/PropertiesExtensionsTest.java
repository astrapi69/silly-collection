package de.alpharogroup.collections;

import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;
import java.util.Properties;

import org.testng.annotations.Test;

import de.alpharogroup.collections.pairs.KeyValuePair;

public class PropertiesExtensionsTest
{

	@Test
	public void test()
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

		List<KeyValuePair<String, String>> list = PropertiesExtensions.toKeyValuePairs(properties);

		assertNotNull(list);
		assertTrue(list.size() == 2);

	}

}
