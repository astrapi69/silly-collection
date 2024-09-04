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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.astrapi69.collection.list.ListFactory;

/**
 * The Class {@link PropertiesExtensions} provides methods loading properties and other related
 * operations for properties like find redundant values or getting all available languages from a
 * bundle.
 */
public final class PropertiesExtensions
{

	/**
	 * The constant PROPERTIES_COMMENT_PATTERN is the regex pattern for find comments in properties
	 * file
	 */
	public static final String PROPERTIES_COMMENT_PATTERN = "(?m)^\\s*(\\#|\\!)";

	/**
	 * The constant PROPERTIES_DELIMITERS contains all valid delimiters for properties files
	 */
	public static final String[] PROPERTIES_DELIMITERS = { "=", ":", " " };

	/**
	 * The constant SEARCH_FILE_PATTERN is a regex for searching java and html files
	 */
	public static final String SEARCH_FILE_PATTERN = "([^\\s]+(\\.(?i)(java|html|htm))$)";

	/**
	 * Private constructor to prevent instantiation
	 */
	private PropertiesExtensions()
	{
	}

	/**
	 * Exports the given {@link InputStream} that represents a properties file to the given
	 * properties {@link OutputStream} that represents the output file. The flag xmlFile tells if
	 * the output shell be an xml file or a properties file.
	 *
	 * @param properties
	 *            the properties to store
	 * @param outputStream
	 *            the stream from the output properties file. If the file does not exists a new file
	 *            will be created.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void export(Properties properties, OutputStream outputStream) throws IOException
	{
		export(properties, outputStream, null);
	}

	/**
	 * Exports the given {@link InputStream} that represents a properties file to the given
	 * properties {@link OutputStream} that represents the output file.
	 *
	 * @param properties
	 *            the properties to store
	 * @param outputStream
	 *            the stream from the output properties file. If the file does not exists a new file
	 *            will be created.
	 * @param inputStream
	 *            the input stream
	 * @param comment
	 *            the comment
	 * @param loadFromXML
	 *            the flag that tells if the input shell be loaded from a XML file if true otherwise
	 *            it will be loaded from a properties file
	 * @param storeToXML
	 *            the flag that tells if the output shell be stored to a XML file if true otherwise
	 *            it will be stored to a properties file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void export(Properties properties, OutputStream outputStream,
		InputStream inputStream, String comment, boolean loadFromXML, boolean storeToXML)
		throws IOException
	{ // create a new properties if given properties object is null
		if (properties == null)
		{
			properties = new Properties();
		}
		// load from input stream if not null
		if (inputStream != null)
		{
			if (loadFromXML)
			{
				properties.loadFromXML(inputStream);
			}
			else
			{
				properties.load(inputStream);
			}
		}
		// store as xml or as normal properties format
		if (storeToXML)
		{
			properties.storeToXML(outputStream, comment);
		}
		else
		{
			properties.store(outputStream, comment);
		}
	}

	/**
	 * Exports the given {@link InputStream} that represents a properties file to the given
	 * properties {@link OutputStream} that represents the output file. The flag xmlFile tells if
	 * the output shell be an xml file or a properties file.
	 *
	 * @param properties
	 *            the properties to store
	 * @param outputStream
	 *            the stream from the output properties file. If the file does not exists a new file
	 *            will be created.
	 * @param comment
	 *            the comment
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void export(Properties properties, OutputStream outputStream, String comment)
		throws IOException
	{
		export(properties, outputStream, null, comment, false, false);
	}

	/**
	 * Finds redundant values from the given Properties object and saves it to a Map.
	 *
	 * @param properties
	 *            The Properties to check.
	 * @return A map that contains the redundant value as the key of the map and a List(as value of
	 *         the map) of keys that have the redundant value.
	 */
	public static Map<String, List<String>> findRedundantValues(final Properties properties)
	{
		final Map<String, List<String>> reverseEntries = new LinkedHashMap<>();
		for (final Map.Entry<Object, Object> entry : properties.entrySet())
		{
			final String key = (String)entry.getKey();
			final String value = (String)entry.getValue();
			if (!reverseEntries.containsKey(value))
			{
				final List<String> keys = ListFactory.newArrayList();
				keys.add(key);
				reverseEntries.put(value, keys);
			}
			else
			{
				final List<String> keys = reverseEntries.get(value);
				keys.add(key);
			}
		}
		final Map<String, List<String>> redundantValues = new LinkedHashMap<>();
		for (final Map.Entry<String, List<String>> entry : reverseEntries.entrySet())
		{
			final String key = entry.getKey();
			final List<String> keys = entry.getValue();
			if (1 < keys.size())
			{
				redundantValues.put(key, keys);
			}
		}
		return redundantValues;
	}

	/**
	 * Try to get a number from the given properties key from the given properties. If it does not
	 * exists an empty {@link Optional} will be returned and a log message will be logged.
	 *
	 * @param properties
	 *            the properties
	 * @param propertiesKey
	 *            the properties key
	 * @return the {@linkplain Optional} with the number or an empty {@linkplain Optional} if the
	 *         properties key has no number value or no value at all
	 */
	public static Optional<Integer> getInteger(final Properties properties,
		final String propertiesKey)
	{
		if (properties != null && properties.containsKey(propertiesKey))
		{
			final String portAsString = properties.getProperty(propertiesKey);
			try
			{
				final Integer port = Integer.valueOf(portAsString);
				return Optional.of(port);
			}
			catch (final NumberFormatException e)
			{
				return Optional.empty();
			}
		}
		return Optional.empty();
	}

	/**
	 * Finds all keys with the same key prefixes from the given Properties and saves them to a Map
	 * with the prefix as a key and holds a List with the whole keys the starts with the same key
	 * prefix.
	 *
	 * @param properties
	 *            the en properties
	 * @return the matched prefix lists
	 */
	public static Map<String, List<String>> getMatchedPrefixLists(final Properties properties)
	{
		final Enumeration<?> e = properties.propertyNames();
		final Map<String, List<String>> matchedPrefixes = new LinkedHashMap<>();
		while (e.hasMoreElements())
		{
			final String key = (String)e.nextElement();
			final int lastIndex = key.lastIndexOf(".");
			String subKey;
			if (0 < lastIndex)
			{
				subKey = key.substring(0, lastIndex);
			}
			else
			{
				subKey = key;
			}
			if (matchedPrefixes.containsKey(subKey))
			{
				final List<String> fullKeys = matchedPrefixes.get(subKey);
				fullKeys.add(key);
			}
			else
			{
				final List<String> fullKeys = ListFactory.newArrayList();
				fullKeys.add(key);
				matchedPrefixes.put(subKey, fullKeys);
			}
		}
		return matchedPrefixes;
	}

	/**
	 * Finds the property parameters from the given propertyValue.
	 *
	 * @param propertyValue
	 *            the property value
	 * @return the property parameters as a list.
	 */
	public static List<String> getPropertyParameters(final String propertyValue)
	{
		final List<String> parameterList = ListFactory.newArrayList();
		final Pattern pattern = Pattern.compile("\\{.*?\\}");
		final Matcher matcher = pattern.matcher(propertyValue);
		while (matcher.find())
		{
			final String parameter = matcher.group();
			final String at = parameter.substring(1, parameter.length() - 1);
			parameterList.add(at);
		}
		return parameterList;
	}

	/**
	 * Load a Properties-object from the given File-object.
	 *
	 * @param propertiesFile
	 *            the properties file
	 * @return the properties or null if the file is not found.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Properties loadProperties(final File propertiesFile) throws IOException
	{
		return loadProperties(propertiesFile, false);
	}

	/**
	 * Load a Properties-object from the given File-object.
	 *
	 * @param propertiesFile
	 *            the properties file
	 * @param loadFromXML
	 *            the load from XML
	 * @return the properties or null if the file is not found.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Properties loadProperties(final File propertiesFile, boolean loadFromXML)
		throws IOException
	{
		Properties properties = null;
		if (propertiesFile.exists())
		{
			try (InputStream inputStream = propertiesFile.toURI().toURL().openStream())
			{
				if (inputStream != null)
				{
					properties = new Properties();
					if (loadFromXML)
					{
						properties.loadFromXML(inputStream);
					}
					else
					{
						properties.load(inputStream);
					}
				}
			}
		}
		else
		{
			throw new FileNotFoundException(propertiesFile.getName() + " not found.");
		}
		return properties;
	}

	/**
	 * Converts the given xml file to the given properties file.
	 *
	 * @param properties
	 *            the properties file. The xml file does not have to exist.
	 * @param xml
	 *            the xml file with the properties to convert.
	 * @param comment
	 *            the comment
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void toProperties(final File properties, final File xml, final String comment)
		throws IOException
	{
		try (FileOutputStream fileOutputStream = new FileOutputStream(properties);
			FileInputStream fileInputStream = new FileInputStream(xml))
		{
			toProperties(fileOutputStream, fileInputStream, comment);
		}
	}

	/**
	 * Converts the given xml InputStream to the given properties OutputStream.
	 *
	 * @param properties
	 *            the properties file. The xml file does not have to exist.
	 * @param xml
	 *            the xml file with the properties to convert.
	 * @param comment
	 *            the comment
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void toProperties(final OutputStream properties, final InputStream xml,
		final String comment) throws IOException
	{
		toProperties(properties, xml, comment, true);
	}

	/**
	 * Exports the given {@link InputStream} that represents a properties file to the given
	 * properties {@link OutputStream} that represents the output file. The flag xmlFile tells if
	 * the output shell be an xml file or a properties file.
	 *
	 * @param outputStream
	 *            the stream from the output properties file. If the file does not exists a new file
	 *            will be created.
	 * @param inputStream
	 *            the stream from input file with the properties to convert
	 * @param comment
	 *            the comment
	 * @param xmlFile
	 *            the flag that tells if the output shell be an xml file if true otherwise it will
	 *            be an properties file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void toProperties(final OutputStream outputStream, final InputStream inputStream,
		final String comment, boolean xmlFile) throws IOException
	{
		export(null, outputStream, inputStream, comment, xmlFile, false);
	}

	/**
	 * Converts the given properties InputStream to the given properties {@link OutputStream} that
	 * represents the output file.
	 *
	 * @param outputStream
	 *            the properties file. The xml file does not have to exist.
	 * @param inputStream
	 *            the xml file with the properties to convert.
	 * @param comment
	 *            the comment
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void toPropertiesFile(final OutputStream outputStream,
		final InputStream inputStream, final String comment) throws IOException
	{
		toProperties(outputStream, inputStream, comment, false);
	}

	/**
	 * Converts the given properties file to the given xml file.
	 *
	 * @param properties
	 *            the properties file.
	 * @param xml
	 *            the xml file to write in. The xml file does not have to exist.
	 * @param comment
	 *            the comment
	 * @param encoding
	 *            the encoding for the xml file.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void toXml(final File properties, final File xml, final String comment,
		final String encoding) throws IOException
	{
		try (FileInputStream fileInputStream = new FileInputStream(properties);
			FileOutputStream fileOutputStream = new FileOutputStream(xml))
		{
			toXml(fileInputStream, fileOutputStream, comment, encoding);
		}
	}

	/**
	 * Converts the given properties InputStream to the given xml OutputStream.
	 *
	 * @param properties
	 *            the properties InputStream.
	 * @param xml
	 *            the xml OutputStream to write in.
	 * @param comment
	 *            the comment
	 * @param encoding
	 *            the encoding for the xml file.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void toXml(final InputStream properties, final OutputStream xml,
		final String comment, final String encoding) throws IOException
	{
		final Properties prop = new Properties();
		prop.load(properties);
		prop.storeToXML(xml, comment, encoding);
	}

}
