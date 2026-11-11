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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

/**
 * The unit test class for the class {@link PropertiesExtensions}.
 *
 * @author Asterios Raptis
 */
public class PropertiesExtensionsTest
{

	/**
	 * The directory every test writes its files into, so that no test leaves a file behind in the
	 * working directory, also when it fails before its end
	 */
	@TempDir
	Path directory;

	/**
	 * Properties of the given keys and values
	 *
	 * @param keysAndValues
	 *            the keys and values, one after the other
	 * @return the properties
	 */
	static Properties propertiesOf(final String... keysAndValues)
	{
		final Properties properties = new Properties();
		for (int i = 0; i < keysAndValues.length; i += 2)
		{
			properties.setProperty(keysAndValues[i], keysAndValues[i + 1]);
		}
		return properties;
	}

	/**
	 * The file of a resource of the tests
	 *
	 * @param name
	 *            the name of the resource
	 * @return the file
	 */
	static File resource(final String name)
	{
		try
		{
			return new File(
				PropertiesExtensionsTest.class.getClassLoader().getResource(name).toURI());
		}
		catch (final URISyntaxException exception)
		{
			throw new IllegalStateException(exception);
		}
	}

	/**
	 * The entries of the resource {@code resources.properties}
	 *
	 * @return the properties
	 */
	static Properties resourcesProperties()
	{
		return propertiesOf("testkey3", "Hello =", "testkey2", "Street", "testkey1", "testvalue2",
			"bla", "But");
	}

	/**
	 * The entries of the resource {@code SigninPanel.properties.xml}
	 *
	 * @return the properties
	 */
	static Properties signinPanelProperties()
	{
		return propertiesOf("global.email.label", "Email", "global.enter.your.email.label",
			"Enter your email", "global.password.label", "Password",
			"global.enter.your.password.label", "Enter your password");
	}

	/**
	 * A call that writes properties into a file
	 */
	@FunctionalInterface
	interface FileWrite
	{
		/**
		 * Writes into the given file
		 *
		 * @param file
		 *            the file, which does not exist yet
		 * @throws IOException
		 *             Signals that an I/O exception has occurred
		 */
		void write(File file) throws IOException;
	}

	/**
	 * A call of one of the methods that export or convert properties into a file, and what the file
	 * holds
	 *
	 * @param description
	 *            the name of the case
	 * @param write
	 *            the call
	 * @param xml
	 *            true if the file is an XML file
	 * @param expected
	 *            the properties the file holds
	 * @param text
	 *            a text the file holds
	 */
	record Write(String description, FileWrite write, boolean xml, Properties expected,
		String text) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testWrite(Write)}
	 *
	 * @return the cases
	 */
	static Stream<Write> writes()
	{
		final Properties fooBar = propertiesOf("foo", "bar");
		final String fooBarXml = "<entry key=\"foo\">bar</entry>";
		return Stream.of(
			new Write("export(properties, file): a properties file",
				file -> PropertiesExtensions.export(propertiesOf("foo", "bar"), file), false,
				fooBar, "foo=bar"),
			new Write("export(properties, outputStream): a properties file",
				file -> writeTo(file,
					out -> PropertiesExtensions.export(propertiesOf("foo", "bar"), out)),
				false, fooBar, "foo=bar"),
			new Write("export(properties, outputStream, comment): with the comment",
				file -> writeTo(file,
					out -> PropertiesExtensions.export(propertiesOf("foo", "bar"), out,
						"Foo comment")),
				false, fooBar, "#Foo comment"),
			new Write(
				"export(properties, outputStream, null, null, false, false): a properties file",
				file -> writeTo(file,
					out -> PropertiesExtensions.export(propertiesOf("foo", "bar"), out, null, null,
						false, false)),
				false, fooBar, "foo=bar"),
			new Write("export(properties, outputStream, null, null, true, true): an XML file",
				file -> writeTo(file,
					out -> PropertiesExtensions.export(propertiesOf("foo", "bar"), out, null, null,
						true, true)),
				true, fooBar, fooBarXml),
			new Write("export(properties, outputStream, null, null, false, true): an XML file",
				file -> writeTo(file,
					out -> PropertiesExtensions.export(propertiesOf("foo", "bar"), out, null, null,
						false, true)),
				true, fooBar, fooBarXml),
			new Write(
				"export(properties, outputStream, inputStream of a=1, null, false, false): both entries",
				file -> writeTo(file,
					out -> PropertiesExtensions.export(propertiesOf("foo", "bar"), out,
						new ByteArrayInputStream("a=1".getBytes(StandardCharsets.ISO_8859_1)), null,
						false, false)),
				false, propertiesOf("foo", "bar", "a", "1"), "a=1"),
			new Write("export(null, outputStream, null, null, false, false): no entry",
				file -> writeTo(file,
					out -> PropertiesExtensions.export(null, out, null, null, false, false)),
				false, new Properties(), "#"),
			new Write("toPropertiesFile(outputStream, resources.properties, null): its entries",
				file -> writeTo(file,
					out -> readFrom("resources.properties",
						in -> PropertiesExtensions.toPropertiesFile(out, in, null))),
				false, resourcesProperties(), "testkey2=Street"),
			new Write("toProperties(file, SigninPanel.properties.xml, comment): its four entries",
				file -> PropertiesExtensions.toProperties(file,
					resource("SigninPanel.properties.xml"), "a comment"),
				false, signinPanelProperties(), "#a comment"),
			new Write(
				"toProperties(outputStream, SigninPanel.properties.xml, comment): its four entries",
				file -> writeTo(file,
					out -> readFrom("SigninPanel.properties.xml",
						in -> PropertiesExtensions.toProperties(out, in, "a comment"))),
				false, signinPanelProperties(), "#a comment"),
			new Write("toXml(resources.properties, file, comment, UTF8): its entries as XML",
				file -> PropertiesExtensions.toXml(resource("resources.properties"), file,
					"a comment", "UTF8"),
				true, resourcesProperties(), "<comment>a comment</comment>"));
	}

	/**
	 * A call that writes into an output stream
	 */
	@FunctionalInterface
	interface StreamWrite
	{
		/**
		 * Writes into the given output stream
		 *
		 * @param outputStream
		 *            the output stream
		 * @throws IOException
		 *             Signals that an I/O exception has occurred
		 */
		void write(OutputStream outputStream) throws IOException;
	}

	/**
	 * Writes into the given file with the given call
	 *
	 * @param file
	 *            the file
	 * @param write
	 *            the call that writes into the output stream of the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	static void writeTo(final File file, final StreamWrite write) throws IOException
	{
		try (OutputStream outputStream = new FileOutputStream(file))
		{
			write.write(outputStream);
		}
	}

	/**
	 * A call that reads from an input stream
	 */
	@FunctionalInterface
	interface StreamRead
	{
		/**
		 * Reads from the given input stream
		 *
		 * @param inputStream
		 *            the input stream
		 * @throws IOException
		 *             Signals that an I/O exception has occurred
		 */
		void read(InputStream inputStream) throws IOException;
	}

	/**
	 * Reads from the given resource with the given call
	 *
	 * @param name
	 *            the name of the resource
	 * @param read
	 *            the call that reads from the input stream of the resource
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	static void readFrom(final String name, final StreamRead read) throws IOException
	{
		try (InputStream inputStream = new FileInputStream(resource(name)))
		{
			read.read(inputStream);
		}
	}

	/**
	 * Test for the methods {@code export}, {@code toProperties}, {@link PropertiesExtensions#toXml}
	 * and {@link PropertiesExtensions#toPropertiesFile}: the file holds the given properties, as a
	 * properties file or an XML file, which {@link PropertiesExtensions#loadProperties(File)} or
	 * {@link PropertiesExtensions#loadProperties(File, boolean)} read back
	 *
	 * @param write
	 *            the call and what the file holds
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("writes")
	public void testWrite(final Write write) throws IOException
	{
		final File file = new File(directory.toFile(), "output");
		write.write().write(file);
		final Properties actual = write.xml()
			? PropertiesExtensions.loadProperties(file, true)
			: PropertiesExtensions.loadProperties(file);
		assertEquals(write.expected(), actual);
		final String text = Files.readString(file.toPath(), StandardCharsets.ISO_8859_1);
		assertTrue(text.contains(write.text()), () -> "the file does not hold " + write.text());
	}

	/**
	 * Test for the methods {@link PropertiesExtensions#toXml(File, File, String, String)} and
	 * {@link PropertiesExtensions#toProperties(File, File, String)} one after the other: the XML
	 * file of {@code SigninPanel.properties.xml}, converted into a properties file, into an XML
	 * file and back, holds its four entries
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@Test
	public void testToXmlAndBack() throws IOException
	{
		final File properties = new File(directory.toFile(), "SigninPanel.properties");
		final File xml = new File(directory.toFile(), "login.properties.xml");
		final File back = new File(directory.toFile(), "login.properties");
		PropertiesExtensions.toProperties(properties, resource("SigninPanel.properties.xml"),
			"a comment");
		PropertiesExtensions.toXml(properties, xml, "a comment", "UTF8");
		PropertiesExtensions.toProperties(back, xml, "a comment");
		assertEquals(signinPanelProperties(), PropertiesExtensions.loadProperties(back));
	}

	/**
	 * Test for the methods {@link PropertiesExtensions#loadProperties(File)} and
	 * {@link PropertiesExtensions#loadProperties(File, boolean)}: the properties of a properties
	 * file or of an XML file
	 *
	 * @param description
	 *            the name of the case
	 * @param name
	 *            the name of the resource
	 * @param xml
	 *            true if the file is an XML file
	 * @param expected
	 *            the properties
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@MethodSource("loads")
	public void testLoadProperties(final String description, final String name, final boolean xml,
		final Properties expected) throws IOException
	{
		final File file = resource(name);
		assertEquals(expected,
			xml
				? PropertiesExtensions.loadProperties(file, true)
				: PropertiesExtensions.loadProperties(file));
	}

	/**
	 * The cases of {@link #testLoadProperties(String, String, boolean, Properties)}
	 *
	 * @return the name of the case, the resource, the flag and the properties
	 */
	static Stream<Arguments> loads()
	{
		return Stream.of(
			Arguments.of("loadProperties(resources.properties): its four entries",
				"resources.properties", false, resourcesProperties()),
			Arguments.of("loadProperties(SigninPanel.properties.xml, true): its four entries",
				"SigninPanel.properties.xml", true, signinPanelProperties()));
	}

	/**
	 * Test for the methods {@link PropertiesExtensions#loadProperties(File)} and
	 * {@link PropertiesExtensions#loadProperties(File, boolean)} with a file that does not exist:
	 * they throw a {@link FileNotFoundException} with the name of the file
	 *
	 * @param description
	 *            the name of the case
	 * @param xml
	 *            the flag loadFromXML, null for {@link PropertiesExtensions#loadProperties(File)}
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource({ "loadProperties(file), ", "'loadProperties(file, false)', false",
			"'loadProperties(file, true)', true" })
	public void testLoadPropertiesNotFound(final String description, final Boolean xml)
	{
		final File file = new File(directory.toFile(), "foo.properties");
		final FileNotFoundException exception = assertThrows(FileNotFoundException.class, () -> {
			if (xml == null)
			{
				PropertiesExtensions.loadProperties(file);
			}
			else
			{
				PropertiesExtensions.loadProperties(file, xml);
			}
		});
		assertEquals("foo.properties not found.", exception.getMessage());
	}

	/**
	 * The given map with each list sorted, to compare lists whose order follows the hash order of
	 * {@link Properties}
	 *
	 * @param map
	 *            the map
	 * @return a new map with sorted copies of the lists, in the order of the keys of the given map
	 */
	static Map<String, List<String>> sorted(final Map<String, List<String>> map)
	{
		final Map<String, List<String>> sorted = new LinkedHashMap<>();
		map.forEach((key, list) -> {
			final List<String> copy = new ArrayList<>(list);
			Collections.sort(copy);
			sorted.put(key, copy);
		});
		return sorted;
	}

	/**
	 * The six entries of the message bundle that {@link #testFindRedundantValues} and
	 * {@link #testGetMatchedPrefixLists} search
	 *
	 * @return the properties
	 */
	static Properties messages()
	{
		return propertiesOf("com", "Hello, {0} {1} {2}!", "foo.redundant.value",
			"Hello, {0} {1} {2}!", "com.example.gui.window.title", "Hello, {0}!",
			"com.example.gui.window.buttons.ok", "OK", "foo.bar", "OK",
			"com.example.gui.window.buttons.cancel", "Cancel");
	}

	/**
	 * Test for method {@link PropertiesExtensions#findRedundantValues(Properties)}: each value that
	 * two keys or more share, with these keys; a value of one key is not redundant
	 *
	 * @param description
	 *            the name of the case
	 * @param properties
	 *            the properties
	 * @param expected
	 *            the redundant values and their keys
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@MethodSource("redundantValues")
	public void testFindRedundantValues(final String description, final Properties properties,
		final Map<String, List<String>> expected)
	{
		assertEquals(expected, sorted(PropertiesExtensions.findRedundantValues(properties)));
	}

	/**
	 * The cases of {@link #testFindRedundantValues(String, Properties, Map)}
	 *
	 * @return the name of the case, the properties and the redundant values
	 */
	static Stream<Arguments> redundantValues()
	{
		return Stream.of(
			Arguments.of("six messages: two values, each of two keys", messages(),
				Map.of("Hello, {0} {1} {2}!", List.of("com", "foo.redundant.value"), "OK",
					List.of("com.example.gui.window.buttons.ok", "foo.bar"))),
			Arguments.of("a value of one key and a value of two: the value of two",
				propertiesOf("single", "once", "first", "twice", "second", "twice"),
				Map.of("twice", List.of("first", "second"))),
			Arguments.of("a value of three keys: all three",
				propertiesOf("a", "x", "b", "x", "c", "x"), Map.of("x", List.of("a", "b", "c"))),
			Arguments.of("every value of one key: none", propertiesOf("a", "x", "b", "y"),
				Map.of()),
			Arguments.of("no properties: none", new Properties(), Map.of()));
	}

	/**
	 * Test for method {@link PropertiesExtensions#findRedundantValues(Properties)} with an entry
	 * whose key or value is not a String: it is left out, as
	 * {@link Properties#stringPropertyNames()} does (#148), and the others are checked
	 *
	 * @param description
	 *            the name of the case
	 * @param properties
	 *            the properties
	 * @param expected
	 *            the redundant values and their keys, or the class of the exception
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@MethodSource("redundantValuesOfNonStrings")
	public void testFindRedundantValuesWithNonStringEntries(final String description,
		final Properties properties, final Object expected)
	{
		if (expected instanceof Class<?> exception)
		{
			assertThrows(exception.asSubclass(Throwable.class),
				() -> PropertiesExtensions.findRedundantValues(properties));
		}
		else
		{
			assertEquals(expected, sorted(PropertiesExtensions.findRedundantValues(properties)));
		}
	}

	/**
	 * The cases of {@link #testFindRedundantValuesWithNonStringEntries(String, Properties, Object)}
	 *
	 * @return the name of the case, the properties and the redundant values
	 */
	static Stream<Arguments> redundantValuesOfNonStrings()
	{
		final Properties integerKey = propertiesOf("a", "x", "b", "x");
		integerKey.put(1, "x");
		final Properties integerValue = propertiesOf("c", "x", "d", "x");
		integerValue.put("a", 1);
		integerValue.put("b", 1);
		final Properties both = propertiesOf("a", "x", "b", "x");
		both.put(1, 2);
		return Stream.of(
			Arguments.of("the key 1 is an Integer", integerKey, ClassCastException.class),
			Arguments.of("the values of a and b are Integers", integerValue,
				ClassCastException.class),
			Arguments.of("the key and the value of 1 are Integers", both,
				ClassCastException.class));
	}

	/**
	 * Test for method {@link PropertiesExtensions#getMatchedPrefixLists(Properties)}: each prefix
	 * in front of the last dot of a key, with all keys of this prefix; a key whose last dot is its
	 * first character, or that has no dot, is its own prefix
	 *
	 * @param description
	 *            the name of the case
	 * @param properties
	 *            the properties
	 * @param expected
	 *            the prefixes and their keys
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@MethodSource("matchedPrefixLists")
	public void testGetMatchedPrefixLists(final String description, final Properties properties,
		final Map<String, List<String>> expected)
	{
		assertEquals(expected, sorted(PropertiesExtensions.getMatchedPrefixLists(properties)));
	}

	/**
	 * The cases of {@link #testGetMatchedPrefixLists(String, Properties, Map)}
	 *
	 * @return the name of the case, the properties and the prefixes
	 */
	static Stream<Arguments> matchedPrefixLists()
	{
		return Stream.of(
			Arguments.of("six messages: five prefixes", messages(),
				Map.of("com", List.of("com"), "foo.redundant", List.of("foo.redundant.value"),
					"com.example.gui.window", List.of("com.example.gui.window.title"),
					"com.example.gui.window.buttons",
					List.of("com.example.gui.window.buttons.cancel",
						"com.example.gui.window.buttons.ok"),
					"foo", List.of("foo.bar"))),
			Arguments.of("two keys of one prefix and one of another: two prefixes",
				propertiesOf("com.example.gui.window.buttons.ok", "OK",
					"com.example.gui.window.buttons.cancel", "Cancel",
					"com.example.gui.window.title", "Hello"),
				Map.of("com.example.gui.window.buttons",
					List.of("com.example.gui.window.buttons.cancel",
						"com.example.gui.window.buttons.ok"),
					"com.example.gui.window", List.of("com.example.gui.window.title"))),
			Arguments.of("the last dot at the start, at the second character, none: three prefixes",
				propertiesOf(".hidden", "x", "a.b", "y", "plain", "z"),
				Map.of(".hidden", List.of(".hidden"), "a", List.of("a.b"), "plain",
					List.of("plain"))),
			Arguments.of("no properties: none", new Properties(), Map.of()));
	}

	/**
	 * Test for method {@link PropertiesExtensions#getInteger(Properties, String)}: the number of
	 * the key, or an empty {@link Optional} for a key without a value or with one that is no number
	 *
	 * @param description
	 *            the name of the case
	 * @param value
	 *            the value of the key com, null for no entry
	 * @param key
	 *            the key to get
	 * @param expected
	 *            the number, null for an empty {@link Optional}
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource({ "com of 5: 5, 5, com, 5", "com of 8: 8, 8, com, 8", "com of -3: -3, -3, com, -3",
			"'bar, not a key: empty', 8, bar,", "'com of foo, no number: empty', foo, com,",
			"no entry: empty, , com," })
	public void testGetInteger(final String description, final String value, final String key,
		final Integer expected)
	{
		final Properties properties = new Properties();
		if (value != null)
		{
			properties.setProperty("com", value);
		}
		assertEquals(Optional.ofNullable(expected),
			PropertiesExtensions.getInteger(properties, key));
	}

	/**
	 * Test for method {@link PropertiesExtensions#getInteger(Properties, String)} with null instead
	 * of properties: an empty {@link Optional}
	 */
	@Test
	public void testGetIntegerOfNull()
	{
		assertEquals(Optional.empty(), PropertiesExtensions.getInteger(null, "com"));
	}

	/**
	 * Test for method {@link PropertiesExtensions#getPropertyParameters(String)}: the texts between
	 * the curly braces, in their order
	 *
	 * @param description
	 *            the name of the case
	 * @param value
	 *            the property value
	 * @param expected
	 *            the parameters, separated by a space, '' for none
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "three numbered parameters: 0 1 2 | Hello, {0} {1} {2}! | 0 1 2",
			"a named and a numbered one: name 0 | {name} owes {0} | name 0",
			"no braces: none | Hello! | ''" }, delimiter = '|')
	public void testGetPropertyParameters(final String description, final String value,
		final String expected)
	{
		final List<String> parameters = expected.isEmpty()
			? List.of()
			: List.of(expected.split(" "));
		assertEquals(parameters, PropertiesExtensions.getPropertyParameters(value));
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
