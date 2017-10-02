package de.alpharogroup.collections.properties;

import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import de.alpharogroup.collections.pairs.KeyValuePair;

/**
 * Test class for the class {@link PropertiesExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class PropertiesExtensionsTest
{


	@SuppressWarnings("serial")
	@Test(enabled = false)
	public void testFindRedundantValues() throws IOException
	{
		final Properties properties = new Properties();

		properties.put("com", "Hello, {0} {1} {2}!");
		properties.put("foo.redundant.value", "Hello, {0} {1} {2}!");
		properties.put("com.example.gui.window.title", "Hello, {0}!");
		properties.put("com.example.gui.window.buttons.ok", "OK");
		properties.put("foo.bar", "OK");
		properties.put("com.example.gui.window.buttons.cancel", "Cancel");

		final Map<String, List<String>> redundantValues = PropertiesExtensions
			.findRedundantValues(properties);
		AssertJUnit.assertEquals(redundantValues.get("Hello, {0} {1} {2}!"), new ArrayList<String>()
		{
			{
				add("com");
				add("foo.redundant.value");
			}
		});
		AssertJUnit.assertEquals(redundantValues.get("OK"), new ArrayList<String>()
		{
			{
				add("com.example.gui.window.buttons.ok");
				add("foo.bar");
			}
		});
	}

	@Test
	public void testToKeyValuePairs()
	{
		String key;
		String value;
		final Properties properties = new Properties();

		key = "foo";
		value = "bar";
		properties.setProperty(key, value);

		key = "bla";
		value = "fasel";
		properties.setProperty(key, value);

		final List<KeyValuePair<String, String>> list = PropertiesExtensions
			.toKeyValuePairs(properties);

		assertNotNull(list);
		assertTrue(list.size() == 2);

	}

}
