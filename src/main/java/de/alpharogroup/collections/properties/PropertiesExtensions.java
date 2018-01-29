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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.alpharogroup.collections.pairs.KeyValuePair;

/**
 * The Class {@link PropertiesExtensions} provides methods loading properties and other related
 * operations for properties like find redundant values or getting all available languages from a
 * bundle.
 */
public final class PropertiesExtensions
{

	/**
	 * The Constant SEARCH_FILE_PATTERN is a regex for searching java and html files.
	 */
	public static final String SEARCH_FILE_PATTERN = "([^\\s]+(\\.(?i)(java|html|htm))$)";
	/**
	 * The Constant PROPERTIES_DELIMITERS contains all valid delimiters for properties files.
	 */
	public static final String[] PROPERTIES_DELIMITERS = { "=", ":", " " };

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
				final List<String> keys = new ArrayList<>();
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
			String subKey = null;
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
				final List<String> fullKeys = new ArrayList<>();
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
		final List<String> parameterList = new ArrayList<>();
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
		Properties properties = null;
		InputStream is = null;
		if (propertiesFile.exists())
		{
			is = propertiesFile.toURI().toURL().openStream();
			if (is != null)
			{
				properties = new Properties();
				properties.load(is);
			}
		}
		else
		{
			throw new FileNotFoundException(propertiesFile.getName() + " not found.");
		}
		return properties;
	}

	/**
	 * Transforms the given {@link Properties} to a list of {@link KeyValuePair}'s.
	 *
	 * @param properties
	 *            the properties
	 * @return the new list with the {@link KeyValuePair}'s.
	 */
	public static List<KeyValuePair<String, String>> toKeyValuePairs(final Properties properties)
	{
		return KeyValuePair.toKeyValuePairs(properties);
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
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void toProperties(final File properties, final File xml, final String comment)
		throws FileNotFoundException, IOException
	{
		toProperties(new FileOutputStream(properties), new FileInputStream(xml), comment);
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
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void toProperties(final OutputStream properties, final InputStream xml,
		final String comment) throws FileNotFoundException, IOException
	{
		final Properties prop = new Properties();
		prop.loadFromXML(xml);
		prop.store(properties, comment);
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
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void toXml(final File properties, final File xml, final String comment,
		final String encoding) throws FileNotFoundException, IOException
	{
		toXml(new FileInputStream(properties), new FileOutputStream(xml), comment, encoding);
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
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void toXml(final InputStream properties, final OutputStream xml,
		final String comment, final String encoding) throws FileNotFoundException, IOException
	{
		final Properties prop = new Properties();
		prop.load(properties);
		prop.storeToXML(xml, comment, encoding);
	}

	/**
	 * Private constructor.
	 */
	private PropertiesExtensions()
	{
	}

}
