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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SequencedMap;
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
	 * Test for the writes into an {@link InsertionOrderMap} (#147), on a map of 1 = value1, 2 =
	 * value2, 3 = value3, 4 = value4 (or of a = null, b = 2), called with the key 2 (or a), or with
	 * a key that is not there: every write moves the key to the end, as put does, also the value
	 * the key already has: {@code put}, {@code putIfAbsent} and {@code computeIfAbsent} of a key
	 * that is not there or is mapped to null, {@code compute}, {@code computeIfPresent},
	 * {@code merge}, both {@code replace}; a call that stores nothing moves nothing; a function
	 * that gives null removes the key or stores nothing; {@code replaceAll} writes every entry, so
	 * the order stays. {@link Map.Entry#setValue(Object)} does not move the key, through the stream
	 * or the iterator of {@code entrySet()}, {@code sequencedEntrySet()} and the views of
	 * {@code reversed()}. {@code putFirst} and {@code putLast} follow the contract of
	 * {@link java.util.SequencedMap}: an explicit position. A call with the prefix reversed is made
	 * on the view {@code reversed()}, which writes like the map itself, and its keys are those of
	 * the map
	 *
	 * @param description
	 *            the name of the case
	 * @param map
	 *            the map: 1 to 4, or a=null b=2
	 * @param call
	 *            the call: the name of the method, ToNull for a function that gives null, Same for
	 *            the value the key has, reversed. in front for a call on the reversed view
	 * @param key
	 *            the key
	 * @param result
	 *            what the call gives, nothing for null
	 * @param keys
	 *            the order of the keys of the map afterwards, separated by a space
	 * @param value
	 *            the value of the key afterwards, nothing for null
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"merge, key there: value2+x, 2 moves to the end | 1 to 4 | merge | 2 | value2+x | 1 3 4 2 | value2+x",
			"merge to null, key there: null, 2 removed | 1 to 4 | mergeToNull | 2 |  | 1 3 4 | ",
			"merge, key not there: x, 9 last | 1 to 4 | merge | 9 | x | 1 2 3 4 9 | x",
			"compute, key there: value2!, 2 moves to the end | 1 to 4 | compute | 2 | value2! | 1 3 4 2 | value2!",
			"compute to null, key there: null, 2 removed | 1 to 4 | computeToNull | 2 |  | 1 3 4 | ",
			"compute, key not there: fresh, 9 last | 1 to 4 | compute | 9 | fresh | 1 2 3 4 9 | fresh",
			"computeIfPresent, key there: value2!, 2 moves to the end | 1 to 4 | computeIfPresent | 2 | value2! | 1 3 4 2 | value2!",
			"computeIfPresent to null, key there: null, 2 removed | 1 to 4 | computeIfPresentToNull | 2 |  | 1 3 4 | ",
			"computeIfPresent, key not there: null, nothing | 1 to 4 | computeIfPresent | 9 |  | 1 2 3 4 | ",
			"replace(key, value), key there: value2, 2 moves to the end | 1 to 4 | replace | 2 | value2 | 1 3 4 2 | x",
			"replace(key, value), key not there: null, nothing | 1 to 4 | replace | 9 |  | 1 2 3 4 | ",
			"replace(key, old, new), old is the value: true, 2 moves to the end | 1 to 4 | replaceMatching | 2 | true | 1 3 4 2 | x",
			"replace(key, old, new), old is not the value: false, 2 stays | 1 to 4 | replaceOther | 2 | false | 1 2 3 4 | value2",
			"replace(key, old, new), key not there: false, nothing | 1 to 4 | replaceMatching | 9 | false | 1 2 3 4 | ",
			"put, key there, a new value: value2, 2 moves to the end | 1 to 4 | put | 2 | value2 | 1 3 4 2 | x",
			"put, key there, the same value: value2, 2 moves to the end | 1 to 4 | putSame | 2 | value2 | 1 3 4 2 | value2",
			"put, key not there: null, 9 last | 1 to 4 | put | 9 |  | 1 2 3 4 9 | x",
			"compute, key there, the same value: value2, 2 moves to the end | 1 to 4 | computeSame | 2 | value2 | 1 3 4 2 | value2",
			"computeIfPresent, key there, the same value: value2, 2 moves to the end | 1 to 4 | computeIfPresentSame | 2 | value2 | 1 3 4 2 | value2",
			"merge, key there, the same value: value2, 2 moves to the end | 1 to 4 | mergeSame | 2 | value2 | 1 3 4 2 | value2",
			"replace(key, value), key there, the same value: value2, 2 moves to the end | 1 to 4 | replaceSame | 2 | value2 | 1 3 4 2 | value2",
			"replace(key, old, new), key there, the same value: true, 2 moves to the end | 1 to 4 | replaceMatchingSame | 2 | true | 1 3 4 2 | value2",
			"computeIfAbsent to null, key not there: null, nothing stored | 1 to 4 | computeIfAbsentToNull | 9 |  | 1 2 3 4 | ",
			"replaceAll of the value of 2 only: every key written in turn, so the order stays | 1 to 4 | replaceAllOf2 | 2 |  | 1 2 3 4 | value2!",
			"replaceAll: every value, every key written in turn, so the order stays | 1 to 4 | replaceAll | 2 |  | 1 2 3 4 | value2!",
			"setValue of the entry of 2 through the stream of entrySet(): 2 keeps its place | 1 to 4 | setValue | 2 |  | 1 2 3 4 | x",
			"putIfAbsent, key there: value2, 2 stays | 1 to 4 | putIfAbsent | 2 | value2 | 1 2 3 4 | value2",
			"putIfAbsent, key not there: null, 9 last | 1 to 4 | putIfAbsent | 9 |  | 1 2 3 4 9 | x",
			"computeIfAbsent, key there: value2, 2 stays | 1 to 4 | computeIfAbsent | 2 | value2 | 1 2 3 4 | value2",
			"computeIfAbsent, key not there: x, 9 last | 1 to 4 | computeIfAbsent | 9 | x | 1 2 3 4 9 | x",
			"putIfAbsent(a, x), a mapped to null: a moves to the end | a=null b=2 | putIfAbsent | a |  | b a | x",
			"computeIfAbsent(a, x), a mapped to null: a moves to the end | a=null b=2 | computeIfAbsent | a | x | b a | x",
			"computeIfAbsent(a, null), a mapped to null: nothing stored | a=null b=2 | computeIfAbsentToNull | a |  | a b | ",
			"computeIfPresent(a), a mapped to null: nothing stored | a=null b=2 | computeIfPresent | a |  | a b | ",
			"putIfAbsent(a, null), a mapped to null: no value, a keeps its place | a=null b=2 | putIfAbsentNull | a |  | a b | ",
			"putIfAbsent(b, x), b has the value 2: 2, b stays | a=null b=2 | putIfAbsent | b | 2 | a b | 2",
			"computeIfAbsent(b, x), b has the value 2: 2, b stays | a=null b=2 | computeIfAbsent | b | 2 | a b | 2",
			"putFirst(2): explicit position, 2 first | 1 to 4 | putFirst | 2 | value2 | 2 1 3 4 | x",
			"putFirst(9), a new key: explicit position, 9 first | 1 to 4 | putFirst | 9 |  | 9 1 2 3 4 | x",
			"putLast(2): explicit position, 2 last | 1 to 4 | putLast | 2 | value2 | 1 3 4 2 | x",
			"putLast(9), a new key: explicit position, 9 last | 1 to 4 | putLast | 9 |  | 1 2 3 4 9 | x",
			"setValue of the entry of 2 through the iterator of entrySet(): 2 keeps its place | 1 to 4 | setValueIterator | 2 |  | 1 2 3 4 | x",
			"setValue of the entry of 2 through sequencedEntrySet(): 2 keeps its place | 1 to 4 | setValueSequenced | 2 |  | 1 2 3 4 | x",
			"keySet().remove(2), then put(2): 2 last | 1 to 4 | keySetRemove | 2 |  | 1 3 4 2 | x",
			"reversed().put(2): as put, 2 at the end of the map | 1 to 4 | reversed.put | 2 | value2 | 1 3 4 2 | x",
			"reversed().put(2), the same value: as put, 2 at the end of the map | 1 to 4 | reversed.putSame | 2 | value2 | 1 3 4 2 | value2",
			"reversed().putIfAbsent(2), a key with a value: nothing stored | 1 to 4 | reversed.putIfAbsent | 2 | value2 | 1 2 3 4 | value2",
			"reversed().putIfAbsent(9), a new key: 9 last | 1 to 4 | reversed.putIfAbsent | 9 |  | 1 2 3 4 9 | x",
			"reversed().putIfAbsent(a), a mapped to null: a moves to the end | a=null b=2 | reversed.putIfAbsent | a |  | b a | x",
			"reversed().compute(2): as compute, 2 at the end of the map | 1 to 4 | reversed.compute | 2 | value2! | 1 3 4 2 | value2!",
			"reversed().compute to null, 2: removed | 1 to 4 | reversed.computeToNull | 2 |  | 1 3 4 | ",
			"reversed().computeIfAbsent(2), a key with a value: nothing stored | 1 to 4 | reversed.computeIfAbsent | 2 | value2 | 1 2 3 4 | value2",
			"reversed().computeIfAbsent(a), a mapped to null: a moves to the end | a=null b=2 | reversed.computeIfAbsent | a | x | b a | x",
			"reversed().computeIfPresent(2): as computeIfPresent, 2 at the end of the map | 1 to 4 | reversed.computeIfPresent | 2 | value2! | 1 3 4 2 | value2!",
			"reversed().computeIfPresent(9), a key that is not there: nothing | 1 to 4 | reversed.computeIfPresent | 9 |  | 1 2 3 4 | ",
			"reversed().merge(2): as merge, 2 at the end of the map | 1 to 4 | reversed.merge | 2 | value2+x | 1 3 4 2 | value2+x",
			"reversed().merge to null, 2: removed | 1 to 4 | reversed.mergeToNull | 2 |  | 1 3 4 | ",
			"reversed().replace(2, value): as replace, 2 at the end of the map | 1 to 4 | reversed.replace | 2 | value2 | 1 3 4 2 | x",
			"reversed().replace(2, old, new), old is the value: 2 at the end of the map | 1 to 4 | reversed.replaceMatching | 2 | true | 1 3 4 2 | x",
			"reversed().replace(2, old, new), old is not the value: nothing stored | 1 to 4 | reversed.replaceOther | 2 | false | 1 2 3 4 | value2",
			"reversed().replaceAll: every key written in turn, so the order stays | 1 to 4 | reversed.replaceAll | 2 |  | 1 2 3 4 | value2!",
			"reversed().replaceAll of the value of 2 only: the order stays | 1 to 4 | reversed.replaceAllOf2 | 2 |  | 1 2 3 4 | value2!",
			"reversed().putFirst(2): explicit position in the view, 2 last of the map | 1 to 4 | reversed.putFirst | 2 | value2 | 1 3 4 2 | x",
			"reversed().putLast(2): explicit position in the view, 2 first of the map | 1 to 4 | reversed.putLast | 2 | value2 | 2 1 3 4 | x",
			"reversed().entrySet(), setValue of the entry of 2 through the stream: 2 keeps its place | 1 to 4 | reversed.setValue | 2 |  | 1 2 3 4 | x",
			"reversed().entrySet(), setValue of the entry of 2 through the iterator: 2 keeps its place | 1 to 4 | reversed.setValueIterator | 2 |  | 1 2 3 4 | x",
			"reversed().sequencedEntrySet(), setValue of the entry of 2: 2 keeps its place | 1 to 4 | reversed.setValueSequenced | 2 |  | 1 2 3 4 | x" }, delimiter = '|')
	public void testWrites(final String description, final String map, final String call,
		final String key, final String result, final String keys, final String value)
	{
		final InsertionOrderMap<String, String> insertionOrderMap = new InsertionOrderMap<>();
		if ("a=null b=2".equals(map))
		{
			insertionOrderMap.put("a", null);
			insertionOrderMap.put("b", "2");
		}
		else
		{
			oneToFour(insertionOrderMap);
		}
		final boolean reversed = call.startsWith("reversed.");
		final SequencedMap<String, String> target = reversed
			? insertionOrderMap.reversed()
			: insertionOrderMap;
		final Object actual = switch (reversed ? call.substring("reversed.".length()) : call)
		{
			case "merge" -> target.merge(key, "x", (old, given) -> old + "+" + given);
			case "mergeToNull" -> target.merge(key, "x", (old, given) -> null);
			case "mergeSame" -> target.merge(key, "x", (old, given) -> old);
			case "compute" -> target.compute(key, (k, v) -> v == null ? "fresh" : v + "!");
			case "computeToNull" -> target.compute(key, (k, v) -> null);
			case "computeSame" -> target.compute(key, (k, v) -> v);
			case "computeIfPresent" -> target.computeIfPresent(key, (k, v) -> v + "!");
			case "computeIfPresentToNull" -> target.computeIfPresent(key, (k, v) -> null);
			case "computeIfPresentSame" -> target.computeIfPresent(key, (k, v) -> v);
			case "replace" -> target.replace(key, "x");
			case "replaceSame" -> target.replace(key, "value" + key);
			case "replaceMatching" -> target.replace(key, "value" + key, "x");
			case "replaceMatchingSame" -> target.replace(key, "value" + key, "value" + key);
			case "replaceOther" -> target.replace(key, "other", "x");
			case "put" -> target.put(key, "x");
			case "putSame" -> target.put(key, "value" + key);
			case "putIfAbsent" -> target.putIfAbsent(key, "x");
			case "putIfAbsentNull" -> target.putIfAbsent(key, null);
			case "computeIfAbsent" -> target.computeIfAbsent(key, k -> "x");
			case "computeIfAbsentToNull" -> target.computeIfAbsent(key, k -> null);
			case "putFirst" -> target.putFirst(key, "x");
			case "putLast" -> target.putLast(key, "x");
			case "replaceAll" ->
			{
				target.replaceAll((k, v) -> v + "!");
				yield null;
			}
			case "replaceAllOf2" ->
			{
				target.replaceAll((k, v) -> "2".equals(k) ? v + "!" : v);
				yield null;
			}
			case "setValue" ->
			{
				target.entrySet().stream().filter(entry -> entry.getKey().equals(key)).findFirst()
					.orElseThrow().setValue("x");
				yield null;
			}
			case "setValueIterator" ->
			{
				for (final Iterator<Map.Entry<String, String>> iterator = target.entrySet()
					.iterator(); iterator.hasNext();)
				{
					final Map.Entry<String, String> entry = iterator.next();
					if (entry.getKey().equals(key))
					{
						entry.setValue("x");
					}
				}
				yield null;
			}
			case "setValueSequenced" ->
			{
				for (final Iterator<Map.Entry<String, String>> iterator = target.sequencedEntrySet()
					.iterator(); iterator.hasNext();)
				{
					final Map.Entry<String, String> entry = iterator.next();
					if (entry.getKey().equals(key))
					{
						entry.setValue("x");
					}
				}
				yield null;
			}
			case "keySetRemove" ->
			{
				target.keySet().remove(key);
				target.put(key, "x");
				yield null;
			}
			default -> throw new IllegalArgumentException("no call " + call);
		};
		assertEquals(result, actual == null ? null : actual.toString());
		assertEquals(Arrays.asList(keys.split(" ")), new ArrayList<>(insertionOrderMap.keySet()));
		assertEquals(value, insertionOrderMap.get(key));
	}

}
