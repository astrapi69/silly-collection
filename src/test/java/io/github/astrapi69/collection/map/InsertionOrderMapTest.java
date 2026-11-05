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
package io.github.astrapi69.collection.map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The unit test class for the class {@link InsertionOrderMap}.
 *
 * @author Asterios Raptis
 */
public class InsertionOrderMapTest
{

	/**
	 * Puts the keys 1 to 4 with the values value1 to value4 into the given map, in this order
	 *
	 * @param map
	 *            the map
	 * @return the given map
	 */
	static Map<String, String> oneToFour(final Map<String, String> map)
	{
		map.put("1", "value1");
		map.put("2", "value2");
		map.put("3", "value3");
		map.put("4", "value4");
		return map;
	}

	/**
	 * A new {@link InsertionOrderMap} of 1 = value1, 3 = null, 4 = value4, in this order
	 *
	 * @return the new map
	 */
	static Map<String, String> oneNullFour()
	{
		final Map<String, String> map = new InsertionOrderMap<>();
		map.put("1", "value1");
		map.put("3", null);
		map.put("4", "value4");
		return map;
	}

	/**
	 * A map made by a constructor and filled with four entries, and the order of its keys before
	 * and after the key 2 is put again with a new value
	 *
	 * @param description
	 *            the name of the case
	 * @param map
	 *            makes the filled map
	 * @param keys
	 *            the order of the keys of the filled map
	 * @param keysAfterPutAgain
	 *            the order of the keys after the key 2 is put again
	 */
	record OrderCase(String description, Supplier<Map<String, String>> map, List<String> keys,
		List<String> keysAfterPutAgain) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testOrder(OrderCase)}: each constructor of {@link InsertionOrderMap},
	 * and a {@link LinkedHashMap} for the difference, which keeps the place of a key that is put
	 * again
	 *
	 * @return the cases
	 */
	static Stream<OrderCase> orders()
	{
		final List<String> oneToFour = List.of("1", "2", "3", "4");
		final List<String> twoMoved = List.of("1", "3", "4", "2");
		return Stream.of(
			new OrderCase("InsertionOrderMap(): 2 moves to the end",
				() -> oneToFour(new InsertionOrderMap<>()), oneToFour, twoMoved),
			new OrderCase("InsertionOrderMap(4): 2 moves to the end",
				() -> oneToFour(new InsertionOrderMap<>(4)), oneToFour, twoMoved),
			new OrderCase("InsertionOrderMap(4, 4f): 2 moves to the end",
				() -> oneToFour(new InsertionOrderMap<>(4, 4f)), oneToFour, twoMoved),
			new OrderCase("InsertionOrderMap(4, 4f, true), access order: 2 moves to the end",
				() -> oneToFour(new InsertionOrderMap<>(4, 4f, true)), oneToFour, twoMoved),
			new OrderCase(
				"InsertionOrderMap(map of 2, 3, 1, 4): the order of the given map, 2 moves to the end",
				() -> {
					final Map<String, String> given = new LinkedHashMap<>();
					given.put("2", "value2");
					given.put("3", "value3");
					given.put("1", "value1");
					given.put("4", "value4");
					return new InsertionOrderMap<>(given);
				}, List.of("2", "3", "1", "4"), List.of("3", "1", "4", "2")),
			new OrderCase("LinkedHashMap, for the difference: 2 keeps its place",
				() -> oneToFour(new LinkedHashMap<>()), oneToFour, oneToFour));
	}

	/**
	 * Test for the constructors of {@link InsertionOrderMap} and its method
	 * {@link InsertionOrderMap#put(Object, Object)}: the keys come in the order they were put, and
	 * a key that is put again with a new value moves to the end, which a {@link LinkedHashMap} does
	 * not do
	 *
	 * @param order
	 *            the map and the orders of its keys
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("orders")
	public void testOrder(final OrderCase order)
	{
		final Map<String, String> map = order.map().get();
		assertEquals(order.keys(), new ArrayList<>(map.keySet()));
		assertEquals("value2", map.put("2", "value5"));
		assertEquals(order.keysAfterPutAgain(), new ArrayList<>(map.keySet()));
		assertEquals("value5", map.get("2"));
	}

	/**
	 * Test for method {@link InsertionOrderMap#put(Object, Object)} on a map of 1 = value1, 3 =
	 * null, 4 = value4: as its Javadoc and the {@link Map} contract say, it returns the previous
	 * value of the key, or null if the key had no mapping or was mapped to null, and the key comes
	 * last afterwards
	 *
	 * @param description
	 *            the name of the case
	 * @param key
	 *            the key to put
	 * @param value
	 *            the value to put, may be null
	 * @param previous
	 *            the value put returns, may be null
	 * @param keys
	 *            the order of the keys afterwards, separated by a space
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "the new key 2: null, 2 last | 2 | value2 | | 1 3 4 2",
			"the new key 2 with null: null, 2 last | 2 | | | 1 3 4 2",
			"1, which holds value1: value1, 1 last | 1 | value5 | value1 | 3 4 1",
			"3, which holds null: null, 3 last | 3 | value5 | | 1 4 3" }, delimiter = '|')
	public void testPutReturnsThePreviousValue(final String description, final String key,
		final String value, final String previous, final String keys)
	{
		final Map<String, String> map = oneNullFour();
		assertEquals(previous, map.put(key, value));
		assertEquals(value, map.get(key));
		assertEquals(Arrays.asList(keys.split(" ")), new ArrayList<>(map.keySet()));
	}

	/**
	 * Test for method {@link InsertionOrderMap#putAll(Map)} on a map of 1 = value1, 3 = null, 4 =
	 * value4: as {@link Map#putAll(Map)} says, the effect is that of put for each mapping, in the
	 * order of the given map, so each key takes its value, a key that is there moves to the end and
	 * a new key comes last
	 *
	 * @param description
	 *            the name of the case
	 * @param entries
	 *            the entries of the given map in its order, key=value separated by a space, nothing
	 *            after = for a null value, '' for none
	 * @param keys
	 *            the order of the keys afterwards, separated by a space
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "no entries: the map unchanged | '' | 1 3 4",
			"the new key 2: 2 last | 2=value2 | 1 3 4 2",
			"the new keys 5, 2: last, in the order of the given map | 5=value5 2=value2 | 1 3 4 5 2",
			"1, which holds value1: 1 moves to the end, as put does (#127) | 1=value5 | 3 4 1",
			"1 with null, which holds value1: 1 moves to the end, as put does (#127) | 1= | 3 4 1",
			"3, which holds null: 3 moves to the end, as put does (#127) | 3=value5 | 1 4 3",
			"4, 2, 1: each moves to the end in turn, as put does (#127) | 4=value5 2=value2 1=value6 | 3 4 2 1" }, delimiter = '|')
	public void testPutAll(final String description, final String entries, final String keys)
	{
		final Map<String, String> given = new LinkedHashMap<>();
		for (final String entry : entries.isEmpty() ? new String[0] : entries.split(" "))
		{
			final String[] keyAndValue = entry.split("=", -1);
			given.put(keyAndValue[0], keyAndValue[1].isEmpty() ? null : keyAndValue[1]);
		}
		final Map<String, String> map = oneNullFour();
		map.putAll(given);
		assertEquals(Arrays.asList(keys.split(" ")), new ArrayList<>(map.keySet()));
		given.forEach((key, value) -> assertEquals(value, map.get(key), "the value of " + key));
	}

}
