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
package de.alpharogroup.check;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test class for the class {@link Check}.
 */
public class CheckTest
{
	/**
	 * A rule for expecting exceptions
	 */
	@Rule
	public ExpectedException throwable = ExpectedException.none();

	/**
	 * Test of get method, of class Check.
	 */
	@Test
	public void testGet()
	{
		final Check actual = Check.get();
		assertNotNull(actual);
	}

	/**
	 * Test for {@link Check#isInRange(Double, Double, Double, String)}
	 */
	@Test
	public void testIsInRangeDouble()
	{
		final Double min = 0.0d;
		final Double max = 5.0d;
		final Double value = 6.0d;
		final String name = "parameter";
		throwable.expect(IllegalArgumentException.class);
		throwable.expectMessage("Given argument '" + name + "' should have a value between " + min
			+ " - " + max + ", but given argument is currently:" + value + "");

		Check.get().isInRange(min, max, value, name);
	}

	/**
	 * Test for {@link Check#isInRange(Float, Float, Float, String)}
	 */
	@Test
	public void testIsInRangeFloat()
	{
		final Float min = 0.0f;
		final Float max = 5.0f;
		final Float value = 6.0f;
		final String name = "parameter";
		throwable.expect(IllegalArgumentException.class);
		throwable.expectMessage("Given argument '" + name + "' should have a value between " + min
			+ " - " + max + ", but given argument is currently:" + value + "");

		Check.get().isInRange(min, max, value, name);
	}

	/**
	 * Test for {@link Check#isInRange(Integer, Integer, Integer, String)}
	 */
	@Test
	public void testIsInRangeInteger()
	{
		final Integer min = 0;
		final Integer max = 5;
		final Integer value = 6;
		final String name = "parameter";
		throwable.expect(IllegalArgumentException.class);
		throwable.expectMessage("Given argument '" + name + "' should have a value between " + min
			+ " - " + max + ", but given argument is currently:" + value + "");

		Check.get().isInRange(min, max, value, name);
	}

	/**
	 * Test for {@link Check#isInRange(Long, Long, Long, String)}
	 */
	@Test
	public void testIsInRangeLong()
	{
		final Long min = 0l;
		final Long max = 5l;
		final Long value = 6l;
		final String name = "parameter";
		throwable.expect(IllegalArgumentException.class);
		throwable.expectMessage("Given argument '" + name + "' should have a value between " + min
			+ " - " + max + ", but given argument is currently:" + value + "");

		Check.get().isInRange(min, max, value, name);
	}

	/**
	 * Test for {@link Check#notEmpty(java.util.Collection, String)}
	 */
	@Test
	public void testNotEmptyCollection()
	{
		final String name = "list";
		final List<String> list = new ArrayList<>();
		throwable.expect(IllegalArgumentException.class);
		throwable.expectMessage("Given collection '" + name + "' may not be empty.");

		Check.get().notEmpty(list, name);
	}


	/**
	 * Test for {@link Check#notEmpty(java.util.Collection, String)}
	 */
	@Test
	public void testNotEmptyMap()
	{
		final String name = "map";
		final Map<String, String> emptyMap = new HashMap<>();
		throwable.expect(IllegalArgumentException.class);
		throwable.expectMessage("Given map '" + name + "' may not be empty.");

		Check.get().notEmpty(emptyMap, name);
	}

	/**
	 * Test for {@link Check#notEmpty(CharSequence, String)}
	 */
	@Test
	public void testNotEmptyString()
	{
		final String name = "parameter";
		final String argument = "";
		throwable.expect(IllegalArgumentException.class);
		throwable.expectMessage("Given argument '" + name + "' may not be empty.");

		Check.get().notEmpty(argument, name);
	}

	/**
	 * Test for {@link Check#notNull(Object, String)}
	 */
	@Test
	public void testNotNull()
	{
		final String name = "parameter";
		throwable.expect(IllegalArgumentException.class);
		throwable.expectMessage("Given argument '" + name + "' may not be null.");

		Check.get().notNull(null, name);
	}

}
