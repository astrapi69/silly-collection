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
package de.alpharogroup.collections.properties;

import static org.testng.Assert.assertEquals;
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
 * @author Asterios Raptis
 * @version 1.0
 */
public class PropertiesExtensionsTest
{


	/**
	 * Test for method {@link PropertiesExtensions#findRedundantValues(Properties)}
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("serial")
	@Test(enabled = true)
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

	/**
	 * Test for method {@link PropertiesExtensions#toKeyValuePairs(Properties)}
	 */
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

	/**
	 * Test for method {@link PropertiesExtensions#getMatchedPrefixLists(Properties)}
	 */
	@Test(enabled = true)
	public void testGetMatchedPrefixLists()
	{
		final Properties properties = new Properties();

		properties.put("com", "Hello, {0} {1} {2}!");
		properties.put("foo.redundant.value", "Hello, {0} {1} {2}!");
		properties.put("com.example.gui.window.title", "Hello, {0}!");
		properties.put("com.example.gui.window.buttons.ok", "OK");
		properties.put("foo.bar", "OK");
		properties.put("com.example.gui.window.buttons.cancel", "Cancel");

		final Map<String, List<String>> matchedPrefixLists = PropertiesExtensions.getMatchedPrefixLists(properties);

		assertEquals(matchedPrefixLists.size(), 5);
	}

	/**
	 * Test for method {@link PropertiesExtensions#getPropertyParameters(String)}
	 */
	@Test
	public void testGetPropertyParameters()
	{
		final String propertyValue = "Hello, {0} {1} {2}!";
		final List<String> propertyParameters = PropertiesExtensions.getPropertyParameters(propertyValue);
		assertEquals(propertyParameters.size(), 3);
	}

	/**
	 * Test load properties.
	 */
	@Test
	public void testLoadProperties()
	{
	}

	/**
	 * Test to properties file file string.
	 */
	@Test
	public void testToPropertiesFileFileString()
	{
	}

	/**
	 * Test to properties output stream input stream string.
	 */
	@Test
	public void testToPropertiesOutputStreamInputStreamString()
	{
	}

	/**
	 * Test to xml file file string string.
	 */
	@Test
	public void testToXmlFileFileStringString()
	{
	}

	/**
	 * Test to xml input stream output stream string string.
	 */
	@Test
	public void testToXmlInputStreamOutputStreamStringString()
	{
	}

}
