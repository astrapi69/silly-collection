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

	/**
	 * Test for the methods of {@link InsertionOrderMap} that give a key a new value besides put, on
	 * a map of 1 = value1, 2 = value2, 3 = value3, 4 = value4, called with the key 2, which is
	 * there, or 9, which is not (#147): {@code merge}, {@code compute}, {@code computeIfPresent}
	 * and {@code replace} move a key that takes a new value to the end, as put does; a function
	 * that gives null removes the key; a replace that does not replace, {@code replaceAll},
	 * {@link Map.Entry#setValue(Object)}, {@code putIfAbsent} and {@code computeIfAbsent} of a key
	 * that is there leave the order
	 *
	 * @param description
	 *            the name of the case
	 * @param call
	 *            the call: the name of the method, ToNull for a function that gives null
	 * @param key
	 *            the key
	 * @param result
	 *            what the call gives, nothing for null
	 * @param keys
	 *            the order of the keys afterwards, separated by a space
	 * @param value
	 *            the value of the key afterwards, nothing for null
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"merge, key there: value2+x, 2 moves to the end | merge | 2 | value2+x | 1 3 4 2 | value2+x",
			"merge to null, key there: null, 2 removed | mergeToNull | 2 | | 1 3 4 | ",
			"merge, key not there: x, 9 last | merge | 9 | x | 1 2 3 4 9 | x",
			"compute, key there: value2!, 2 moves to the end | compute | 2 | value2! | 1 3 4 2 | value2!",
			"compute to null, key there: null, 2 removed | computeToNull | 2 | | 1 3 4 | ",
			"compute, key not there: fresh, 9 last | compute | 9 | fresh | 1 2 3 4 9 | fresh",
			"computeIfPresent, key there: value2!, 2 moves to the end | computeIfPresent | 2 | value2! | 1 3 4 2 | value2!",
			"computeIfPresent to null, key there: null, 2 removed | computeIfPresentToNull | 2 | | 1 3 4 | ",
			"computeIfPresent, key not there: null, nothing | computeIfPresent | 9 | | 1 2 3 4 | ",
			"replace(key, value), key there: value2, 2 moves to the end | replace | 2 | value2 | 1 3 4 2 | x",
			"replace(key, value), key not there: null, nothing | replace | 9 | | 1 2 3 4 | ",
			"replace(key, old, new), old is the value: true, 2 moves to the end | replaceMatching | 2 | true | 1 3 4 2 | x",
			"replace(key, old, new), old is not the value: false, 2 stays | replaceOther | 2 | false | 1 2 3 4 | value2",
			"replace(key, old, new), key not there: false, nothing | replaceMatching | 9 | false | 1 2 3 4 | ",
			"put, key there, a new value: value2, 2 moves to the end | put | 2 | value2 | 1 3 4 2 | x",
			"put, key there, the same value: value2, 2 moves to the end | putSame | 2 | value2 | 1 3 4 2 | value2",
			"put, key not there: null, 9 last | put | 9 | | 1 2 3 4 9 | x",
			"compute, key there, the same value: value2, 2 moves to the end | computeSame | 2 | value2 | 1 3 4 2 | value2",
			"computeIfPresent, key there, the same value: value2, 2 moves to the end | computeIfPresentSame | 2 | value2 | 1 3 4 2 | value2",
			"merge, key there, the same value: value2, 2 moves to the end | mergeSame | 2 | value2 | 1 3 4 2 | value2",
			"replace(key, value), key there, the same value: value2, 2 moves to the end | replaceSame | 2 | value2 | 1 3 4 2 | value2",
			"replace(key, old, new), key there, the same value: true, 2 moves to the end | replaceMatchingSame | 2 | true | 1 3 4 2 | value2",
			"computeIfAbsent to null, key not there: null, nothing stored | computeIfAbsentToNull | 9 | | 1 2 3 4 | ",
			"replaceAll of the value of 2 only: every key written in turn, so the order stays | replaceAllOf2 | 2 | | 1 2 3 4 | value2!",
			"replaceAll: every value, every key written in turn, so the order stays | replaceAll | 2 | | 1 2 3 4 | value2!",
			"setValue of the entry of 2: 2 keeps its place | setValue | 2 | | 1 2 3 4 | x",
			"putIfAbsent, key there: value2, 2 stays | putIfAbsent | 2 | value2 | 1 2 3 4 | value2",
			"putIfAbsent, key not there: null, 9 last | putIfAbsent | 9 | | 1 2 3 4 9 | x",
			"computeIfAbsent, key there: value2, 2 stays | computeIfAbsent | 2 | value2 | 1 2 3 4 | value2",
			"computeIfAbsent, key not there: x, 9 last | computeIfAbsent | 9 | x | 1 2 3 4 9 | x" }, delimiter = '|')
	public void testWrites(final String description, final String call, final String key,
		final String result, final String keys, final String value)
	{
		final Map<String, String> map = oneToFour(new InsertionOrderMap<>());
		final Object actual = switch (call)
		{
			case "merge" -> map.merge(key, "x", (old, given) -> old + "+" + given);
			case "mergeToNull" -> map.merge(key, "x", (old, given) -> null);
			case "compute" -> map.compute(key, (k, v) -> v == null ? "fresh" : v + "!");
			case "computeToNull" -> map.compute(key, (k, v) -> null);
			case "computeIfPresent" -> map.computeIfPresent(key, (k, v) -> v + "!");
			case "computeIfPresentToNull" -> map.computeIfPresent(key, (k, v) -> null);
			case "replace" -> map.replace(key, "x");
			case "replaceMatching" -> map.replace(key, "value" + key, "x");
			case "replaceOther" -> map.replace(key, "other", "x");
			case "put" -> map.put(key, "x");
			case "putSame" -> map.put(key, "value" + key);
			case "computeSame" -> map.compute(key, (k, v) -> v);
			case "computeIfPresentSame" -> map.computeIfPresent(key, (k, v) -> v);
			case "mergeSame" -> map.merge(key, "x", (old, given) -> old);
			case "replaceSame" -> map.replace(key, "value" + key);
			case "replaceMatchingSame" -> map.replace(key, "value" + key, "value" + key);
			case "computeIfAbsentToNull" -> map.computeIfAbsent(key, k -> null);
			case "replaceAllOf2" ->
			{
				map.replaceAll((k, v) -> "2".equals(k) ? v + "!" : v);
				yield null;
			}
			case "replaceAll" ->
			{
				map.replaceAll((k, v) -> v + "!");
				yield null;
			}
			case "setValue" ->
			{
				map.entrySet().stream().filter(entry -> entry.getKey().equals(key)).findFirst()
					.orElseThrow().setValue("x");
				yield null;
			}
			case "putIfAbsent" -> map.putIfAbsent(key, "x");
			case "computeIfAbsent" -> map.computeIfAbsent(key, k -> "x");
			default -> throw new IllegalArgumentException("no call " + call);
		};
		assertEquals(result, actual == null ? null : actual.toString());
		assertEquals(Arrays.asList(keys.split(" ")), new ArrayList<>(map.keySet()));
		assertEquals(value, map.get(key));
	}

	/**
	 * Test for {@code putIfAbsent} and {@code computeIfAbsent} of {@link InsertionOrderMap} on a
	 * map of a = null, b = 2 (#147): as {@link Map} specifies them, they also write a key that is
	 * mapped to null, which then takes a new value like put does; a function that gives null, or a
	 * key with a value, changes nothing
	 *
	 * @param description
	 *            the name of the case
	 * @param call
	 *            the call on the key a, or on b
	 * @param result
	 *            what the call gives, nothing for null
	 * @param keys
	 *            the order of the keys afterwards, separated by a space
	 * @param value
	 *            the value of the key a afterwards, nothing for null
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"putIfAbsent(a, 3), a mapped to null: a moves to the end | putIfAbsent | | b a | 3",
			"computeIfAbsent(a, 3), a mapped to null: a moves to the end | computeIfAbsent | 3 | b a | 3",
			"computeIfAbsent(a, null), a mapped to null | computeIfAbsentToNull | | a b | ",
			"computeIfPresent(a, 3), a mapped to null: nothing stored | computeIfPresentOfA | | a b | ",
			"putIfAbsent(a, null), a mapped to null: no value, a keeps its place | putIfAbsentNull | | a b | ",
			"putIfAbsent(b, 3), b has the value 2 | putIfAbsentOfB | 2 | a b | ",
			"computeIfAbsent(b, 3), b has the value 2 | computeIfAbsentOfB | 2 | a b | " }, delimiter = '|')
	public void testAbsentWritesOfAKeyMappedToNull(final String description, final String call,
		final String result, final String keys, final String value)
	{
		final Map<String, String> map = new InsertionOrderMap<>();
		map.put("a", null);
		map.put("b", "2");
		final String actual = switch (call)
		{
			case "putIfAbsent" -> map.putIfAbsent("a", "3");
			case "computeIfAbsent" -> map.computeIfAbsent("a", k -> "3");
			case "computeIfAbsentToNull" -> map.computeIfAbsent("a", k -> null);
			case "computeIfPresentOfA" -> map.computeIfPresent("a", (k, v) -> "3");
			case "putIfAbsentNull" -> map.putIfAbsent("a", null);
			case "putIfAbsentOfB" -> map.putIfAbsent("b", "3");
			case "computeIfAbsentOfB" -> map.computeIfAbsent("b", k -> "3");
			default -> throw new IllegalArgumentException("no call " + call);
		};
		assertEquals(result, actual);
		assertEquals(Arrays.asList(keys.split(" ")), new ArrayList<>(map.keySet()));
		assertEquals(value, map.get("a"));
	}

	/**
	 * Test for the other ways to write into an {@link InsertionOrderMap} (#147), on a map of 1 to 4
	 * and the key 2: {@code putFirst} and {@code putLast} of {@link java.util.SequencedMap}
	 * position the key explicitly, first and last; the writes through {@code reversed()} are the
	 * writes of the map itself, so a key moves to the end of the map, which is the first of the
	 * reversed view; {@link Map.Entry#setValue(Object)} writes without moving, as moving during an
	 * iteration would break it
	 *
	 * @param description
	 *            the name of the case
	 * @param call
	 *            the call
	 * @param keys
	 *            the order of the keys afterwards, separated by a space
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "putFirst(2): 2 first | putFirst | 2 1 3 4",
			"putFirst(9), a new key: 9 first | putFirstNew | 9 1 2 3 4",
			"putLast(2): 2 last | putLast | 1 3 4 2",
			"reversed().put(2): 2 at the end of the map | reversedPut | 1 3 4 2",
			"reversed().merge(2): 2 at the end of the map | reversedMerge | 1 3 4 2",
			"reversed().putLast(2): 2 first of the map | reversedPutLast | 2 1 3 4",
			"reversed().putFirst(2): 2 last of the map | reversedPutFirst | 1 3 4 2",
			"the entry of 2, setValue: 2 keeps its place | setValue | 1 2 3 4",
			"the sequenced entry set, setValue of the first: 1 keeps its place | sequencedSetValue | 1 2 3 4",
			"keySet().remove(2), then put(2): 2 last | keySetRemove | 1 3 4 2" }, delimiter = '|')
	public void testOtherWrites(final String description, final String call, final String keys)
	{
		final InsertionOrderMap<Integer, Integer> map = new InsertionOrderMap<>();
		for (int i = 1; i <= 4; i++)
		{
			map.put(i, i * 10);
		}
		switch (call)
		{
			case "putFirst" -> map.putFirst(2, 99);
			case "putFirstNew" -> map.putFirst(9, 99);
			case "putLast" -> map.putLast(2, 99);
			case "reversedPut" -> map.reversed().put(2, 99);
			case "reversedMerge" -> map.reversed().merge(2, 1, Integer::sum);
			case "reversedPutLast" -> map.reversed().putLast(2, 99);
			case "reversedPutFirst" -> map.reversed().putFirst(2, 99);
			case "setValue" -> map.entrySet().stream().filter(entry -> entry.getKey() == 2)
				.findFirst().orElseThrow().setValue(5);
			case "sequencedSetValue" -> map.sequencedEntrySet().iterator().next().setValue(1);
			case "keySetRemove" -> {
				map.keySet().remove(2);
				map.put(2, 99);
			}
			default -> throw new IllegalArgumentException("no call " + call);
		}
		assertEquals(Arrays.stream(keys.split(" ")).map(Integer::valueOf).toList(),
			new ArrayList<>(map.keySet()));
	}

}
