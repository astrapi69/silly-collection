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

import static org.testng.AssertJUnit.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The unit test class for the class {@link MapExtensions}.
 *
 * @author Asterios Raptis
 */
public class MapExtensionsTest
{

	private void assertMapToArray(Map<String, String> map, String[][] twoDimArray)
	{
		int count = 0;
		for (final Entry<String, String> entry : map.entrySet())
		{
			final String key = entry.getKey();
			final String value = entry.getValue();
			assertTrue(key.equals(twoDimArray[count][0]));
			assertTrue(value.equals(twoDimArray[count][1]));
			count++;
		}
	}

	/**
	 * Sets up method will be invoked before every unit test method
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@BeforeMethod
	protected void setUp() throws Exception
	{
	}

	/**
	 * Tear down method will be invoked after every unit test method
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
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
		assertTrue("Expected value is not equal with key.", foundedKey.equals(expected));

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
			assertTrue("Key should be in the expected List.", expected.contains(key));

		}
	}

	/**
	 * Test for the Method {@link MapExtensions#toGenericMap(Object[][])}.
	 */
	@Test
	public void testToGenericMap()
	{
		final String twoDimArray[][] = { { "1", "value1" }, { "3", "value3" }, { "4", "value4" },
				{ "2", "value2" } };
		final Map<String, String> map = MapExtensions.toGenericMap(twoDimArray);
		assertMapToArray(map, twoDimArray);
	}

	/**
	 * Test for the Method {@link MapExtensions#toMap(String[][])}.
	 */
	@Test
	public void testToMap()
	{
		final String twoDimArray[][] = { { "1", "value1" }, { "3", "value3" }, { "4", "value4" },
				{ "2", "value2" } };
		final Map<String, String> map = MapExtensions.toMap(twoDimArray);
		assertMapToArray(map, twoDimArray);
	}

	/**
	 * Test method for {@link MapExtensions} with {@link BeanTester}
	 */
	@Test(enabled = false, expectedExceptions = { BeanTestException.class, InvocationTargetException.class,
			UnsupportedOperationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(MapExtensions.class);
	}

}
