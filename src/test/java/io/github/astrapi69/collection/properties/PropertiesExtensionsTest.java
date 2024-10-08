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
package io.github.astrapi69.collection.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.io.StringOutputStream;

/**
 * The unit test class for the class {@link PropertiesExtensions}.
 *
 * @author Asterios Raptis
 */
public class PropertiesExtensionsTest
{


	/**
	 * Test method for {@link PropertiesExtensions#export(Properties, OutputStream)} <br>
	 * Scenario of export an existing {@link Properties} object to a given {@link OutputStream} as
	 * string object where the flag loadFromXML and the flag storeToXML are both false. <br>
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testExportToString() throws IOException
	{
		final Properties properties = new Properties();
		properties.setProperty("foo", "bar");
		String actual;
		try (OutputStream outputStream = new StringOutputStream())
		{
			PropertiesExtensions.export(properties, outputStream);
			actual = outputStream.toString();
			assertNotNull(actual);
			assertTrue(actual.contains("foo=bar"));
		}
	}

	/**
	 * Test method for {@link PropertiesExtensions#export(Properties, File)}
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testExportToFile() throws IOException
	{
		final Properties properties = new Properties();
		properties.setProperty("foo", "bar");
		File propertiesFile = new File(".", "output3.properties");
		PropertiesExtensions.export(properties, propertiesFile);

		final Properties propertiesOutput = PropertiesExtensions.loadProperties(propertiesFile);
		assertNotNull(propertiesOutput);
		assertEquals(properties, propertiesOutput);
		DeleteFileExtensions.delete(propertiesFile);
	}


	/**
	 * Test method for
	 * {@link PropertiesExtensions#export(Properties, OutputStream, InputStream, String, boolean, boolean)}.
	 * <br>
	 * Scenario of export an existing {@link Properties} object to an given {@link OutputStream} as
	 * *.properties file where the flag loadFromXML and the flag storeToXML are both false. <br>
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testExportToProperties() throws IOException
	{
		final Properties properties = new Properties();
		properties.setProperty("foo", "bar");
		File propertiesFile = new File(".", "output2.properties");
		try (OutputStream outputStream = new FileOutputStream(propertiesFile))
		{
			PropertiesExtensions.export(properties, outputStream, null, null, false, false);
		}
		final Properties propertiesOutput = PropertiesExtensions.loadProperties(propertiesFile);
		assertNotNull(propertiesOutput);
		assertEquals(properties, propertiesOutput);
		DeleteFileExtensions.delete(propertiesFile);
	}

	/**
	 * Test method for {@link PropertiesExtensions#export(Properties, OutputStream)}. <br>
	 * Scenario of export an existing {@link Properties} object to an given {@link OutputStream} as
	 * *.properties file where the flag loadFromXML and the flag storeToXML are both false. <br>
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testExportToProperties2() throws IOException
	{
		final Properties properties = new Properties();
		properties.setProperty("foo", "bar");
		File propertiesFile = new File(".", "output2.properties");
		try (OutputStream outputStream = new FileOutputStream(propertiesFile))
		{
			PropertiesExtensions.export(properties, outputStream);
		}
		final Properties propertiesOutput = PropertiesExtensions.loadProperties(propertiesFile);
		assertNotNull(propertiesOutput);
		assertEquals(properties, propertiesOutput);
		propertiesFile.delete();
	}

	/**
	 * Test method for {@link PropertiesExtensions#export(Properties, OutputStream, String)}. <br>
	 * Scenario of export an existing {@link Properties} object to an given {@link OutputStream} as
	 * *.properties file where the flag loadFromXML and the flag storeToXML are both false. <br>
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testExportToProperties3() throws IOException
	{
		final Properties properties = new Properties();
		properties.setProperty("foo", "bar");
		File propertiesFile = new File(".", "output2.properties");
		try (OutputStream outputStream = new FileOutputStream(propertiesFile))
		{
			PropertiesExtensions.export(properties, outputStream, "Foo comment");
		}
		final Properties propertiesOutput = PropertiesExtensions.loadProperties(propertiesFile);
		assertNotNull(propertiesOutput);
		assertEquals(properties, propertiesOutput);
		propertiesFile.delete();
	}

	/**
	 * Test method for
	 * {@link PropertiesExtensions#export(Properties, OutputStream, InputStream, String, boolean, boolean)}.
	 *
	 * <br>
	 * Scenario of export an existing {@link Properties} object to an given {@link OutputStream} as
	 * *.xml properties file where the flag loadFromXML and the flag storeToXML are both true. <br>
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testExportToXml() throws IOException
	{
		final Properties properties = new Properties();
		properties.setProperty("foo", "bar");
		File propertiesFile = new File(".", "output1.xml");
		try (OutputStream outputStream = new FileOutputStream(propertiesFile))
		{
			PropertiesExtensions.export(properties, outputStream, null, null, true, true);
		}
		final Properties propertiesOutput = PropertiesExtensions.loadProperties(propertiesFile,
			true);
		assertNotNull(propertiesOutput);
		assertEquals(properties, propertiesOutput);
		DeleteFileExtensions.delete(propertiesFile);
	}

	/**
	 * Test method for
	 * {@link PropertiesExtensions#export(Properties, OutputStream, InputStream, String, boolean, boolean)}.
	 *
	 * <br>
	 * Scenario of export an existing {@link Properties} object to an given {@link OutputStream} as
	 * *.xml properties file where the flag loadFromXML is false and the flag storeToXML is true.
	 * <br>
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testExportToXml2() throws IOException
	{
		final Properties properties = new Properties();
		properties.setProperty("foo", "bar");
		File propertiesFile = new File(".", "output2.xml");
		try (OutputStream outputStream = new FileOutputStream(propertiesFile))
		{
			PropertiesExtensions.export(properties, outputStream, null, null, false, true);
		}
		final Properties propertiesOutput = PropertiesExtensions.loadProperties(propertiesFile,
			true);
		assertNotNull(propertiesOutput);
		assertEquals(properties, propertiesOutput);
		propertiesFile.delete();
	}

	/**
	 * Test for method {@link PropertiesExtensions#findRedundantValues(Properties)}
	 */
	@Test
	public void testFindRedundantValues()
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

		Collections.sort(redundantValues.get("OK"));

		assertEquals(redundantValues.get("Hello, {0} {1} {2}!"),
			ListFactory.newArrayList("com", "foo.redundant.value"));
		assertEquals(redundantValues.get("OK"),
			ListFactory.newArrayList("com.example.gui.window.buttons.ok", "foo.bar"));
	}

	/**
	 * Test for method {@link PropertiesExtensions#getInteger(Properties, String)}.
	 */
	@Test
	public void testGetInteger()
	{
		Integer expected;
		Integer actual;
		Optional<Integer> number;
		Properties properties;
		properties = new Properties();

		properties.put("com", "5");

		number = PropertiesExtensions.getInteger(properties, "com");
		actual = number.get();
		expected = 5;
		assertEquals(actual, expected);

		properties = new Properties();

		properties.put("com", "8");

		number = PropertiesExtensions.getInteger(properties, "com");
		actual = number.get();
		expected = 8;
		assertEquals(actual, expected);

		number = PropertiesExtensions.getInteger(properties, "bar");
		assertFalse(number.isPresent());

	}

	/**
	 * Test for method {@link PropertiesExtensions#getInteger(Properties, String)} where value is
	 * not a number.
	 */
	@Test
	public void testGetIntegerWithNoNumberValue()
	{
		Optional<Integer> actual;
		Optional<Integer> expected;
		Properties properties = new Properties();

		properties.put("com", "foo");

		actual = PropertiesExtensions.getInteger(properties, "com");
		expected = Optional.empty();
		assertEquals(actual, expected);
	}

	/**
	 * Test for method {@link PropertiesExtensions#getMatchedPrefixLists(Properties)}.
	 */
	@Test
	public void testGetMatchedPrefixLists()
	{
		final Properties properties = new Properties();

		properties.put("com", "Hello, {0} {1} {2}!");
		properties.put("foo.redundant.value", "Hello, {0} {1} {2}!");
		properties.put("com.example.gui.window.title", "Hello, {0}!");
		properties.put("com.example.gui.window.buttons.ok", "OK");
		properties.put("foo.bar", "OK");
		properties.put("com.example.gui.window.buttons.cancel", "Cancel");

		final Map<String, List<String>> matchedPrefixLists = PropertiesExtensions
			.getMatchedPrefixLists(properties);

		assertEquals(matchedPrefixLists.size(), 5);
	}

	/**
	 * Test for method {@link PropertiesExtensions#getPropertyParameters(String)}.
	 */
	@Test
	public void testGetPropertyParameters()
	{
		final String propertyValue = "Hello, {0} {1} {2}!";
		final List<String> propertyParameters = PropertiesExtensions
			.getPropertyParameters(propertyValue);
		assertEquals(propertyParameters.size(), 3);
	}

	/**
	 * Test for method {@link PropertiesExtensions#loadProperties(File)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testLoadProperties() throws IOException
	{
		final URL resource = getClass().getClassLoader().getResource("resources.properties");
		final File propertiesFile = new File(resource.getFile());
		final Properties properties = PropertiesExtensions.loadProperties(propertiesFile);
		assertNotNull(properties);
	}

	/**
	 * Test for method {@link PropertiesExtensions#loadProperties(File)} with a file that does not
	 * exist.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testLoadPropertiesNotFound() throws IOException
	{
		Assertions.assertThrows(FileNotFoundException.class, () -> {
			PropertiesExtensions.loadProperties(new File("foo.properties"));
		});
	}

	/**
	 * Test method for
	 * {@link PropertiesExtensions#toPropertiesFile(OutputStream, InputStream, String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testToPropertiesFile() throws IOException
	{
		File propertiesFile = new File("output1.properties");
		try (OutputStream outputStream = new FileOutputStream(propertiesFile);)
		{
			final URL resource = getClass().getClassLoader().getResource("resources.properties");
			final File propertiesInputFile = new File(resource.getFile());
			PropertiesExtensions.toPropertiesFile(outputStream, resource.openStream(), null);
			final Properties propertiesInput = PropertiesExtensions
				.loadProperties(propertiesInputFile);
			assertNotNull(propertiesInput);
			final Properties propertiesOutput = PropertiesExtensions.loadProperties(propertiesFile);
			assertNotNull(propertiesOutput);
			assertEquals(propertiesInput, propertiesOutput);
		}
		propertiesFile.delete();

	}

	/**
	 * Test for method {@link PropertiesExtensions#toProperties(File, File, String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testToPropertiesFileFileString() throws IOException
	{
		URL resource;
		final File propertiesFile = new File("SigninPanel.properties");
		resource = getClass().getClassLoader().getResource("SigninPanel.properties.xml");
		final File xmlFile = new File(resource.getFile());
		PropertiesExtensions.toProperties(propertiesFile, xmlFile, "a comment");
		final Properties properties = PropertiesExtensions.loadProperties(propertiesFile);
		assertNotNull(properties);
		assertTrue(properties.size() == 4);
		propertiesFile.delete();
	}

	/**
	 * Test for method {@link PropertiesExtensions#toXml(File, File, String, String)}.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testToXmlFileFileStringString() throws IOException
	{
		URL resource;
		final File xmlOutputFile = new File("login.properties.xml");
		final File propertiesOutputFile = new File("login.properties");
		final File propertiesFile = new File("SigninPanel.properties");
		resource = getClass().getClassLoader().getResource("SigninPanel.properties.xml");
		final File xmlFile = new File(resource.getFile());
		PropertiesExtensions.toProperties(propertiesFile, xmlFile, "a comment");
		Properties properties = PropertiesExtensions.loadProperties(propertiesFile);
		assertNotNull(properties);
		assertTrue(properties.size() == 4);

		PropertiesExtensions.toXml(propertiesFile, xmlOutputFile, "a comment", "UTF8");

		PropertiesExtensions.toProperties(propertiesOutputFile, xmlOutputFile, "a comment");

		properties = PropertiesExtensions.loadProperties(propertiesOutputFile);
		assertNotNull(properties);
		assertTrue(properties.size() == 4);
		// clean up...
		DeleteFileExtensions.delete(xmlOutputFile);
		DeleteFileExtensions.delete(propertiesOutputFile);
		DeleteFileExtensions.delete(propertiesFile);
	}

	/**
	 * Test method for {@link PropertiesExtensions} with {@link BeanTester}.
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(PropertiesExtensions.class);
	}

}
