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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.apache.commons.collections4.map.LazyMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.list.ListFactory;

/**
 * The unit test class for the class {@link MapFactory}.
 *
 * @author Asterios Raptis
 */
public class MapFactoryTest
{

	/**
	 * The entries the factory methods are given: 5 = value, 1 = novalue, 3 = othervalue, 2 =
	 * somevalue, in this order, which is neither the order of the keys nor the reverse
	 *
	 * @return a new {@link LinkedHashMap} of the entries
	 */
	static Map<String, String> given()
	{
		final Map<String, String> map = new LinkedHashMap<>();
		map.put("5", "value");
		map.put("1", "novalue");
		map.put("3", "othervalue");
		map.put("2", "somevalue");
		return map;
	}

	/**
	 * The entries of {@link #given()} as entries, in the same order
	 *
	 * @return a new list of the entries
	 */
	static List<Map.Entry<String, String>> givenEntries()
	{
		return ListFactory.newArrayList(entry("5", "value"), entry("1", "novalue"),
			entry("3", "othervalue"), entry("2", "somevalue"));
	}

	/**
	 * Entries with the key 3 twice: 3 = a, 5 = b, 1 = c, 3 = d, in this order, in which neither the
	 * first nor the last entry of 3 puts it in the order of the hashes of the keys
	 *
	 * @return a new list of the entries
	 */
	static List<Map.Entry<String, String>> repeatedEntries()
	{
		return ListFactory.newArrayList(entry("3", "a"), entry("5", "b"), entry("1", "c"),
			entry("3", "d"));
	}

	/**
	 * Entries with a null key and a null value: null = a, b = null, c = d, in this order
	 *
	 * @return a new list of the entries
	 */
	static List<Map.Entry<String, String>> nullEntryList()
	{
		return ListFactory.newArrayList(nullable(null, "a"), nullable("b", null), entry("c", "d"));
	}

	/**
	 * The entries of {@link #nullEntryList()} as a map, in the same order
	 *
	 * @return a new {@link LinkedHashMap} of the entries
	 */
	static Map<String, String> nullEntries()
	{
		final Map<String, String> map = new LinkedHashMap<>();
		map.put(null, "a");
		map.put("b", null);
		map.put("c", "d");
		return map;
	}

	/**
	 * The entries of {@link #given()} as an array of entries, in the same order
	 *
	 * @return a new array of the entries
	 */
	@SuppressWarnings("unchecked")
	static Map.Entry<String, String>[] entryArray()
	{
		return givenEntries().toArray(new Map.Entry[0]);
	}

	/**
	 * A list of entries as an array
	 *
	 * @param entries
	 *            the entries
	 * @return a new array of the entries
	 */
	@SuppressWarnings("unchecked")
	static Map.Entry<String, String>[] array(final List<Map.Entry<String, String>> entries)
	{
		return entries.toArray(new Map.Entry[0]);
	}

	/**
	 * An entry
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @return the entry
	 */
	static <K, V> Map.Entry<K, V> entry(final K key, final V value)
	{
		return Map.entry(key, value);
	}

	/**
	 * An entry that allows a null key or a null value, which {@link Map#entry(Object, Object)} does
	 * not
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @return the entry
	 */
	static <K, V> Map.Entry<K, V> nullable(final K key, final V value)
	{
		return new AbstractMap.SimpleEntry<>(key, value);
	}

	/**
	 * An entry with a class as key, which is not {@link Comparable}
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @return the entry
	 */
	static Map.Entry<Class<?>, String> classEntry(final Class<?> key, final String value)
	{
		return Map.entry(key, value);
	}

	/**
	 * A call of a factory method of a {@link java.util.HashMap}, {@link LinkedHashMap},
	 * {@link InsertionOrderMap} or {@link ConcurrentHashMap}, and the map it makes
	 *
	 * @param description
	 *            the name of the case
	 * @param factory
	 *            the call
	 * @param type
	 *            the type of the map, the one the Javadoc of the factory method names
	 * @param entries
	 *            the entries of the map
	 * @param keys
	 *            the order of the keys, null if the type does not promise one
	 */
	record Factory(String description, Supplier<Map<?, ?>> factory, Class<?> type,
		Map<?, ?> entries, List<?> keys) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testFactory(Factory)}
	 *
	 * @return the cases
	 */
	static Stream<Factory> factories()
	{
		final Map<String, String> entries = given();
		final List<String> givenOrder = List.of("5", "1", "3", "2");
		final Map<String, String> repeated = Map.of("3", "d", "5", "b", "1", "c");
		return Stream.of(
			new Factory("newConcurrentHashMap(): empty", MapFactory::newConcurrentHashMap,
				ConcurrentHashMap.class, Map.of(), null),
			new Factory("newConcurrentHashMap(5): empty", () -> MapFactory.newConcurrentHashMap(5),
				ConcurrentHashMap.class, Map.of(), null),
			new Factory("newConcurrentHashMap(map): its entries",
				() -> MapFactory.newConcurrentHashMap(given()), ConcurrentHashMap.class, entries,
				null),
			new Factory("newHashMap(): empty", MapFactory::newHashMap, HashMap.class, Map.of(),
				null),
			new Factory("newHashMap(10): empty", () -> MapFactory.newHashMap(10), HashMap.class,
				Map.of(), null),
			new Factory("newHashMap(map): its entries", () -> MapFactory.newHashMap(given()),
				HashMap.class, entries, null),
			new Factory("newHashMap(collection of entries): the entries",
				() -> MapFactory.newHashMap(givenEntries()), HashMap.class, entries, null),
			new Factory("newHashMap(entries...): the entries",
				() -> MapFactory.newHashMap(entryArray()), HashMap.class, entries, null),
			new Factory("newHashMap(collection of entries 3, 5, 1, 3 again): the last value of 3",
				() -> MapFactory.newHashMap(repeatedEntries()), HashMap.class, repeated, null),
			new Factory("newHashMap(entries... 3, 5, 1, 3 again): the last value of 3",
				() -> MapFactory.newHashMap(array(repeatedEntries())), HashMap.class, repeated,
				null),
			new Factory(
				"newHashMap(collection of entries with null key and null value): the entries",
				() -> MapFactory.newHashMap(nullEntryList()), HashMap.class, nullEntries(), null),
			new Factory("newHashMap(entries... with null key and null value): the entries",
				() -> MapFactory.newHashMap(array(nullEntryList())), HashMap.class, nullEntries(),
				null),
			new Factory("newHashMap(entrySet of a map): the entries of the map",
				() -> MapFactory.newHashMap(given().entrySet()), HashMap.class, entries, null),
			new Factory("newHashMap(no entries in a collection): empty",
				() -> MapFactory.newHashMap(List.<Map.Entry<String, String>> of()), HashMap.class,
				Map.of(), null),
			new Factory("newHashMap(no entries in an array): empty",
				() -> MapFactory.newHashMap(array(List.of())), HashMap.class, Map.of(), null),
			new Factory("newHashMap(keys 1 to 4, values one to four): the entries",
				() -> MapFactory.newHashMap(List.of(1, 2, 3, 4),
					List.of("one", "two", "three", "four")),
				HashMap.class, Map.of(1, "one", 2, "two", 3, "three", 4, "four"), null),
			new Factory("newHashMap(no keys, no values): empty",
				() -> MapFactory.newHashMap(List.of(), List.of()), HashMap.class, Map.of(), null),
			new Factory("newInsertionOrderMap(): empty", MapFactory::newInsertionOrderMap,
				InsertionOrderMap.class, Map.of(), List.of()),
			new Factory("newInsertionOrderMap(4): empty", () -> MapFactory.newInsertionOrderMap(4),
				InsertionOrderMap.class, Map.of(), List.of()),
			new Factory("newInsertionOrderMap(map): its entries in its order",
				() -> MapFactory.newInsertionOrderMap(given()), InsertionOrderMap.class, entries,
				givenOrder),
			new Factory(
				"newInsertionOrderMap(collection of entries): the entries in their order (#115)",
				() -> MapFactory.newInsertionOrderMap(givenEntries()), InsertionOrderMap.class,
				entries, givenOrder),
			new Factory("newInsertionOrderMap(entries...): the entries in their order (#115)",
				() -> MapFactory.newInsertionOrderMap(entryArray()), InsertionOrderMap.class,
				entries, givenOrder),
			new Factory(
				"newInsertionOrderMap(entries 3, 5, 1, 3 again): the last value of 3, at the place of its last entry (#115)",
				() -> MapFactory.newInsertionOrderMap(repeatedEntries()), InsertionOrderMap.class,
				repeated, List.of("5", "1", "3")),
			new Factory(
				"newInsertionOrderMap(entries... 3, 5, 1, 3 again): the last value of 3, at the place of its last entry (#115)",
				() -> MapFactory.newInsertionOrderMap(array(repeatedEntries())),
				InsertionOrderMap.class, repeated, List.of("5", "1", "3")),
			new Factory(
				"newInsertionOrderMap(collection of entries with null key and null value): the entries in their order",
				() -> MapFactory.newInsertionOrderMap(nullEntryList()), InsertionOrderMap.class,
				nullEntries(), Arrays.asList(null, "b", "c")),
			new Factory(
				"newInsertionOrderMap(entries... with null key and null value): the entries in their order",
				() -> MapFactory.newInsertionOrderMap(array(nullEntryList())),
				InsertionOrderMap.class, nullEntries(), Arrays.asList(null, "b", "c")),
			new Factory("newInsertionOrderMap(no entries in an array): empty",
				() -> MapFactory.newInsertionOrderMap(array(List.of())), InsertionOrderMap.class,
				Map.of(), List.of()),
			new Factory(
				"newInsertionOrderMap(entrySet of a map): the entries of the map in its order",
				() -> MapFactory.newInsertionOrderMap(given().entrySet()), InsertionOrderMap.class,
				entries, givenOrder),
			new Factory("newInsertionOrderMap(no entries): empty",
				() -> MapFactory.newInsertionOrderMap(List.<Map.Entry<String, String>> of()),
				InsertionOrderMap.class, Map.of(), List.of()),
			new Factory("newLinkedHashMap(): empty", MapFactory::newLinkedHashMap,
				LinkedHashMap.class, Map.of(), List.of()),
			new Factory("newLinkedHashMap(5): empty", () -> MapFactory.newLinkedHashMap(5),
				LinkedHashMap.class, Map.of(), List.of()),
			new Factory("newLinkedHashMap(map): its entries in its order",
				() -> MapFactory.newLinkedHashMap(given()), LinkedHashMap.class, entries,
				givenOrder),
			new Factory(
				"newLinkedHashMap(collection of entries): the entries in their order (#115)",
				() -> MapFactory.newLinkedHashMap(givenEntries()), LinkedHashMap.class, entries,
				givenOrder),
			new Factory("newLinkedHashMap(entries...): the entries in their order (#115)",
				() -> MapFactory.newLinkedHashMap(entryArray()), LinkedHashMap.class, entries,
				givenOrder),
			new Factory(
				"newLinkedHashMap(entries 3, 5, 1, 3 again): the last value of 3, at the place of its first entry (#115)",
				() -> MapFactory.newLinkedHashMap(repeatedEntries()), LinkedHashMap.class, repeated,
				List.of("3", "5", "1")),
			new Factory(
				"newLinkedHashMap(entries... 3, 5, 1, 3 again): the last value of 3, at the place of its first entry (#115)",
				() -> MapFactory.newLinkedHashMap(array(repeatedEntries())), LinkedHashMap.class,
				repeated, List.of("3", "5", "1")),
			new Factory(
				"newLinkedHashMap(collection of entries with null key and null value): the entries in their order",
				() -> MapFactory.newLinkedHashMap(nullEntryList()), LinkedHashMap.class,
				nullEntries(), Arrays.asList(null, "b", "c")),
			new Factory(
				"newLinkedHashMap(entries... with null key and null value): the entries in their order",
				() -> MapFactory.newLinkedHashMap(array(nullEntryList())), LinkedHashMap.class,
				nullEntries(), Arrays.asList(null, "b", "c")),
			new Factory("newLinkedHashMap(no entries in an array): empty",
				() -> MapFactory.newLinkedHashMap(array(List.of())), LinkedHashMap.class, Map.of(),
				List.of()),
			new Factory("newLinkedHashMap(entrySet of a map): the entries of the map in its order",
				() -> MapFactory.newLinkedHashMap(given().entrySet()), LinkedHashMap.class, entries,
				givenOrder),
			new Factory("newLinkedHashMap(no entries): empty",
				() -> MapFactory.newLinkedHashMap(List.<Map.Entry<String, String>> of()),
				LinkedHashMap.class, Map.of(), List.of()));
	}

	/**
	 * Test for the factory methods of {@link java.util.HashMap}, {@link LinkedHashMap},
	 * {@link InsertionOrderMap} and {@link ConcurrentHashMap}: the new map is of the type the
	 * Javadoc names and holds the given entries, a map that keeps an order in the order of the
	 * given map or entries
	 *
	 * @param factory
	 *            the call and the map it makes
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("factories")
	public void testFactory(final Factory factory)
	{
		final Map<?, ?> map = factory.factory().get();
		assertInstanceOf(factory.type(), map);
		assertEquals(factory.entries(), map);
		if (factory.keys() != null)
		{
			assertEquals(factory.keys(), new ArrayList<>(map.keySet()));
		}
	}

	/**
	 * Test for method {@link MapFactory#newHashMap(List, List)} with lists of different sizes: it
	 * throws an {@link IllegalArgumentException}
	 *
	 * @param description
	 *            the name of the case
	 * @param keys
	 *            the keys
	 * @param values
	 *            the values
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@MethodSource("listsOfDifferentSizes")
	public void testNewHashMapWithListsOfDifferentSizes(final String description,
		final List<Integer> keys, final List<String> values)
	{
		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> MapFactory.newHashMap(keys, values));
		assertEquals("The list of keys and values must have the same size", exception.getMessage());
	}

	/**
	 * The cases of {@link #testNewHashMapWithListsOfDifferentSizes(String, List, List)}
	 *
	 * @return the name of the case, the keys and the values
	 */
	static Stream<Arguments> listsOfDifferentSizes()
	{
		return Stream.of(
			Arguments.of("4 keys and 3 values", List.of(1, 2, 3, 4),
				List.of("one", "two", "three")),
			Arguments.of("3 keys and 4 values", List.of(1, 2, 3),
				List.of("one", "two", "three", "four")));
	}

	/**
	 * A call of a factory method that has to throw
	 *
	 * @param description
	 *            the name of the case
	 * @param call
	 *            the call
	 * @param expected
	 *            the type of the exception
	 */
	record Refused(String description, Executable call, Class<? extends Throwable> expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testRefused(Refused)}
	 *
	 * @return the cases
	 */
	static Stream<Refused> refusals()
	{
		final Class<IllegalArgumentException> iae = IllegalArgumentException.class;
		final Comparator<String> descending = Comparator.reverseOrder();
		final List<Map.Entry<String, String>> noEntries = null;
		final Map.Entry<String, String>[] noArray = null;
		@SuppressWarnings("unchecked")
		final Map.Entry<String, String>[] nullEntry = new Map.Entry[] { null };
		return Stream.of(
			new Refused("newHashMap(null collection): IllegalArgumentException",
				() -> MapFactory.newHashMap(noEntries), iae),
			new Refused("newHashMap(null array): IllegalArgumentException",
				() -> MapFactory.newHashMap(noArray), iae),
			new Refused("newInsertionOrderMap(null collection): IllegalArgumentException",
				() -> MapFactory.newInsertionOrderMap(noEntries), iae),
			new Refused("newInsertionOrderMap(null array): IllegalArgumentException",
				() -> MapFactory.newInsertionOrderMap(noArray), iae),
			new Refused("newLinkedHashMap(null collection): IllegalArgumentException",
				() -> MapFactory.newLinkedHashMap(noEntries), iae),
			new Refused("newLinkedHashMap(null array): IllegalArgumentException",
				() -> MapFactory.newLinkedHashMap(noArray), iae),
			new Refused("newTreeMap(null collection): IllegalArgumentException",
				() -> MapFactory.newTreeMap(noEntries), iae),
			new Refused("newTreeMap(null array): IllegalArgumentException",
				() -> MapFactory.newTreeMap(noArray), iae),
			new Refused("newTreeMap(comparator, null collection): IllegalArgumentException",
				() -> MapFactory.newTreeMap(descending, noEntries), iae),
			new Refused("newTreeMap(comparator, null array): IllegalArgumentException",
				() -> MapFactory.newTreeMap(descending, noArray), iae),
			new Refused(
				"newTreeMap(null comparator, collection of entries): IllegalArgumentException",
				() -> MapFactory.newTreeMap((Comparator<String>)null, givenEntries()), iae),
			new Refused("newTreeMap(null comparator, entries...): IllegalArgumentException",
				() -> MapFactory.newTreeMap((Comparator<String>)null, entryArray()), iae),
			new Refused(
				"newHashMap(array with a null entry): NullPointerException, as Map.ofEntries",
				() -> MapFactory.newHashMap(nullEntry), NullPointerException.class),
			new Refused(
				"newInsertionOrderMap(array with a null entry): NullPointerException, as Map.ofEntries",
				() -> MapFactory.newInsertionOrderMap(nullEntry), NullPointerException.class),
			new Refused(
				"newLinkedHashMap(array with a null entry): NullPointerException, as Map.ofEntries",
				() -> MapFactory.newLinkedHashMap(nullEntry), NullPointerException.class),
			new Refused(
				"newTreeMap(array with a null entry): NullPointerException, as Map.ofEntries",
				() -> MapFactory.newTreeMap(nullEntry), NullPointerException.class),
			new Refused(
				"newTreeMap(comparator, array with a null entry): NullPointerException, as Map.ofEntries",
				() -> MapFactory.newTreeMap(descending, nullEntry), NullPointerException.class),
			new Refused(
				"newTreeMap(collection of entries with a null key): NullPointerException, as TreeMap.put",
				() -> MapFactory.newTreeMap(nullEntryList()), NullPointerException.class),
			new Refused(
				"newTreeMap(entries... with a null key): NullPointerException, as TreeMap.put",
				() -> MapFactory.newTreeMap(array(nullEntryList())), NullPointerException.class));
	}

	/**
	 * Test that the factory methods take entries of a subtype of the key and the value, and a
	 * collection of entries of any kind, as {@link Map#ofEntries(Map.Entry...)} takes
	 * {@code Map.Entry<? extends K, ? extends V>}
	 */
	@Test
	public void testEntriesOfSubtypes()
	{
		final Map.Entry<Integer, String> one = Map.entry(1, "one");
		final Map.Entry<Double, StringBuilder> two = Map.entry(2.5, new StringBuilder("two"));
		final Map<Number, CharSequence> linked = MapFactory.newLinkedHashMap(one, two);
		assertEquals(List.of(1, 2.5), new ArrayList<>(linked.keySet()));
		final Map<Number, CharSequence> hash = MapFactory.newHashMap(List.of(one, two));
		assertEquals(linked, hash);
		final Map<Integer, CharSequence> tree = MapFactory.newTreeMap(Comparator.reverseOrder(),
			Map.entry(1, "a"), Map.entry(2, new StringBuilder("b")));
		assertEquals(List.of(2, 1), new ArrayList<>(tree.keySet()));
	}

	/**
	 * Test for the factory methods that take entries: a null collection, array or comparator is
	 * refused, and a null key in a {@link TreeMap} without a comparator that allows it
	 *
	 * @param refused
	 *            the call and the exception it throws
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("refusals")
	public void testRefused(final Refused refused)
	{
		assertThrows(refused.expected(), refused.call());
	}

	/**
	 * A call of a factory method of a {@link TreeMap}, and the map it makes
	 *
	 * @param description
	 *            the name of the case
	 * @param factory
	 *            the call
	 * @param comparator
	 *            the comparator of the map, null for the natural order of the keys
	 * @param entries
	 *            the entries of the map
	 * @param keys
	 *            the order of the keys
	 */
	record TreeFactory(String description, Supplier<Map<?, ?>> factory, Comparator<?> comparator,
		Map<?, ?> entries, List<?> keys) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testTreeFactory(TreeFactory)}
	 *
	 * @return the cases
	 */
	static Stream<TreeFactory> treeFactories()
	{
		final Map<String, String> entries = given();
		final List<String> ascending = List.of("1", "2", "3", "5");
		final Comparator<String> descending = Comparator.reverseOrder();
		final Comparator<Class<?>> byName = Comparator.comparing(Class::getName);
		final Map<Class<?>, String> classes = Map.of(Integer.class, "integer", String.class,
			"string");
		final Map<String, String> repeated = Map.of("3", "d", "5", "b", "1", "c");
		final Comparator<String> nullsFirst = Comparator.nullsFirst(Comparator.naturalOrder());
		final Map<String, String> nullValue = new HashMap<>();
		nullValue.put("a", "x");
		nullValue.put("b", null);
		final Map<String, String> nullKey = new HashMap<>();
		nullKey.put(null, "a");
		nullKey.put("b", "x");
		final List<Class<?>> classesByName = List.of(Integer.class, String.class);
		// 1.00 and 1.0 are equal by compareTo, not by equals: put in the given order, TreeMap keeps
		// the first key and takes the last value
		final BigDecimal oneScaleTwo = new BigDecimal("1.00");
		final BigDecimal oneScaleOne = new BigDecimal("1.0");
		final Map<BigDecimal, String> firstKeyLastValue = Map.of(oneScaleTwo, "last");
		return Stream.of(new TreeFactory(
			"newTreeMap(collection of entries 1.00 then 1.0, equal by compareTo): the first key with the last value",
			() -> MapFactory.newTreeMap(
				ListFactory.newArrayList(entry(oneScaleTwo, "first"), entry(oneScaleOne, "last"))),
			null, firstKeyLastValue, List.of(oneScaleTwo)),
			new TreeFactory(
				"newTreeMap(entries... 1.00 then 1.0, equal by compareTo): the first key with the last value",
				() -> MapFactory.newTreeMap(entry(oneScaleTwo, "first"),
					entry(oneScaleOne, "last")),
				null, firstKeyLastValue, List.of(oneScaleTwo)),
			new TreeFactory("newTreeMap(): empty, the natural order", MapFactory::newTreeMap, null,
				Map.of(), List.of()),
			new TreeFactory("newTreeMap(map): its entries in the natural order",
				() -> MapFactory.newTreeMap(given()), null, entries, ascending),
			new TreeFactory("newTreeMap(collection of entries): the entries in the natural order",
				() -> MapFactory.newTreeMap(givenEntries()), null, entries, ascending),
			new TreeFactory("newTreeMap(entries...): the entries in the natural order",
				() -> MapFactory.newTreeMap(entryArray()), null, entries, ascending),
			new TreeFactory(
				"newTreeMap(collection of entries 3, 5, 1, 3 again): the last value of 3",
				() -> MapFactory.newTreeMap(repeatedEntries()), null, repeated,
				List.of("1", "3", "5")),
			new TreeFactory("newTreeMap(entries... 3, 5, 1, 3 again): the last value of 3",
				() -> MapFactory.newTreeMap(array(repeatedEntries())), null, repeated,
				List.of("1", "3", "5")),
			new TreeFactory(
				"newTreeMap(comparator, collection of entries 3, 5, 1, 3 again): the last value of 3",
				() -> MapFactory.newTreeMap(descending, repeatedEntries()), descending, repeated,
				List.of("5", "3", "1")),
			new TreeFactory(
				"newTreeMap(comparator, entries... 3, 5, 1, 3 again): the last value of 3",
				() -> MapFactory.newTreeMap(descending, array(repeatedEntries())), descending,
				repeated, List.of("5", "3", "1")),
			new TreeFactory("newTreeMap(collection of entries with a null value): the null value",
				() -> MapFactory
					.newTreeMap(ListFactory.newArrayList(nullable("b", null), entry("a", "x"))),
				null, nullValue, List.of("a", "b")),
			new TreeFactory("newTreeMap(entries... with a null value): the null value",
				() -> MapFactory.newTreeMap(nullable("b", null), entry("a", "x")), null, nullValue,
				List.of("a", "b")),
			new TreeFactory(
				"newTreeMap(comparator, collection of entries with a null key): the null key, first",
				() -> MapFactory.newTreeMap(nullsFirst,
					ListFactory.newArrayList(entry("b", "x"), nullable(null, "a"))),
				nullsFirst, nullKey, Arrays.asList(null, "b")),
			new TreeFactory(
				"newTreeMap(comparator, entries... with a null key): the null key, first",
				() -> MapFactory.newTreeMap(nullsFirst, entry("b", "x"), nullable(null, "a")),
				nullsFirst, nullKey, Arrays.asList(null, "b")),
			new TreeFactory("newTreeMap(no entries in a collection): empty, the natural order",
				() -> MapFactory.newTreeMap(List.<Map.Entry<String, String>> of()), null, Map.of(),
				List.of()),
			new TreeFactory("newTreeMap(no entries in an array): empty, the natural order",
				() -> MapFactory.newTreeMap(array(List.of())), null, Map.of(), List.of()),
			new TreeFactory("newTreeMap(comparator, no entries in a collection): empty",
				() -> MapFactory.newTreeMap(descending, List.<Map.Entry<String, String>> of()),
				descending, Map.of(), List.of()),
			new TreeFactory("newTreeMap(comparator, no entries in an array): empty",
				() -> MapFactory.newTreeMap(descending, array(List.of())), descending, Map.of(),
				List.of()),
			new TreeFactory("newTreeMap(comparator): empty, the order of the comparator",
				() -> MapFactory.newTreeMap(descending), descending, Map.of(), List.of()),
			new TreeFactory(
				"newTreeMap(comparator, collection of entries): the entries in the order of the comparator",
				() -> MapFactory.newTreeMap(descending, givenEntries()), descending, entries,
				List.of("5", "3", "2", "1")),
			new TreeFactory(
				"newTreeMap(comparator, entries...): the entries in the order of the comparator",
				() -> MapFactory.newTreeMap(descending, entryArray()), descending, entries,
				List.of("5", "3", "2", "1")),
			new TreeFactory(
				"newTreeMap(comparator, collection of entries), keys not Comparable: the order of the comparator (#44)",
				() -> MapFactory.newTreeMap(byName,
					ListFactory.newArrayList(classEntry(String.class, "string"),
						classEntry(Integer.class, "integer"))),
				byName, classes, classesByName),
			new TreeFactory(
				"newTreeMap(comparator, entries...), keys not Comparable: the order of the comparator (#44)",
				() -> MapFactory.newTreeMap(byName, classEntry(String.class, "string"),
					classEntry(Integer.class, "integer")),
				byName, classes, classesByName));
	}

	/**
	 * Test for the factory methods of a {@link TreeMap}: the new map is a {@link TreeMap}, the type
	 * the Javadoc names, with the given comparator or none, and holds the given entries in the
	 * order of its keys
	 *
	 * @param factory
	 *            the call and the map it makes
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("treeFactories")
	public void testTreeFactory(final TreeFactory factory)
	{
		final Map<?, ?> map = factory.factory().get();
		assertSame(factory.comparator(), assertInstanceOf(TreeMap.class, map).comparator());
		assertEquals(factory.entries(), map);
		assertEquals(factory.keys(), new ArrayList<>(map.keySet()));
	}

	/**
	 * A call of a factory method of a {@link LazyMap}, and what the map creates
	 *
	 * @param description
	 *            the name of the case
	 * @param factory
	 *            the call
	 * @param keys
	 *            the keys to get, in this order
	 * @param order
	 *            the order of the keys afterwards, null if the map does not promise one
	 * @param created
	 *            the type of the value the map creates for a key it does not hold yet
	 */
	record LazyFactory(String description, Supplier<Map<?, ?>> factory, List<?> keys, List<?> order,
		Class<?> created) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testLazyFactory(LazyFactory)}
	 *
	 * @return the cases
	 */
	static Stream<LazyFactory> lazyFactories()
	{
		return Stream.of(
			new LazyFactory("newLazyHashMap(): a HashMap for each key", MapFactory::newLazyHashMap,
				List.of("b", "a"), null, HashMap.class),
			new LazyFactory("newLazyHashMap(an empty HashMap): a HashMap for each key",
				() -> MapFactory.newLazyHashMap(new HashMap<>()), List.of("b", "a"), null,
				HashMap.class),
			new LazyFactory("newLazyMap(): a HashMap for each key", MapFactory::newLazyMap,
				List.of("b", "a"), null, HashMap.class),
			new LazyFactory("newAssosiativeArrayMap(): a HashMap for each index",
				MapFactory::newAssosiativeArrayMap, List.of(1, 0), null, HashMap.class),
			new LazyFactory("newLazyTreeMap(): a TreeMap for each key, in the natural order",
				MapFactory::newLazyTreeMap, List.of("b", "a"), List.of("a", "b"), TreeMap.class),
			new LazyFactory(
				"newLazyTreeMap(reverseOrder): a TreeMap for each key, in the order of the comparator",
				() -> MapFactory.<Integer, Object> newLazyTreeMap(Comparator.reverseOrder()),
				List.of(1, 3, 2), List.of(3, 2, 1), TreeMap.class),
			new LazyFactory(
				"newLazyTreeMap(an empty TreeMap): a TreeMap for each key, in the natural order",
				() -> MapFactory.newLazyTreeMap(new TreeMap<>()), List.of("b", "a"),
				List.of("a", "b"), TreeMap.class));
	}

	/**
	 * Test for the factory methods of a {@link LazyMap}: the new map is an empty {@link LazyMap},
	 * the type the Javadoc names, that creates a value for a key it does not hold yet and keeps it,
	 * and that holds its keys in the order of the map it encapsulates
	 *
	 * @param factory
	 *            the call and what the map creates
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("lazyFactories")
	public void testLazyFactory(final LazyFactory factory)
	{
		final Map<?, ?> map = assertInstanceOf(LazyMap.class, factory.factory().get());
		assertTrue(map.isEmpty());
		for (final Object key : factory.keys())
		{
			final Object created = assertInstanceOf(factory.created(), map.get(key));
			assertSame(created, map.get(key));
		}
		if (factory.order() != null)
		{
			assertEquals(factory.order(), new ArrayList<>(map.keySet()));
		}
		assertEquals(Set.copyOf(factory.keys()), map.keySet());
	}

	/**
	 * Test for the methods {@link MapFactory#newLazyHashMap(HashMap)} and
	 * {@link MapFactory#newLazyTreeMap(TreeMap)}: the new {@link LazyMap} encapsulates the given
	 * map instead of copying it, so it holds the entries of the given map, and the value it creates
	 * for a key it does not hold yet goes into the given map
	 *
	 * @param description
	 *            the name of the case
	 * @param given
	 *            makes the given map
	 * @param factory
	 *            the factory method
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@MethodSource("encapsulatedMaps")
	public void testLazyMapEncapsulatesTheGivenMap(final String description,
		final Supplier<Map<String, Object>> given, final UnaryOperator<Map<String, Object>> factory)
	{
		final Map<String, Object> map = given.get();
		final Map<String, Object> lazyMap = factory.apply(map);
		assertInstanceOf(LazyMap.class, lazyMap);
		assertEquals(given.get(), lazyMap);
		final Object created = lazyMap.get("a");
		assertNotNull(created);
		assertSame(created, map.get("a"));
	}

	/**
	 * The cases of {@link #testLazyMapEncapsulatesTheGivenMap(String, Supplier, UnaryOperator)}
	 *
	 * @return the name of the case, the given map and the factory method
	 */
	static Stream<Arguments> encapsulatedMaps()
	{
		return Stream.of(
			Arguments.of("newLazyHashMap(HashMap of four entries)",
				(Supplier<Map<String, Object>>)() -> new HashMap<>(given()),
				(UnaryOperator<Map<String, Object>>)map -> MapFactory
					.newLazyHashMap((HashMap<String, Object>)map)),
			Arguments.of("newLazyTreeMap(TreeMap of four entries)",
				(Supplier<Map<String, Object>>)() -> new TreeMap<>(given()),
				(UnaryOperator<Map<String, Object>>)map -> MapFactory
					.newLazyTreeMap((TreeMap<String, Object>)map)));
	}

	/**
	 * A call of one of the methods that make counter maps, and the counts
	 *
	 * @param description
	 *            the name of the case
	 * @param counter
	 *            the call
	 * @param expected
	 *            the counts
	 */
	record Counter(String description, Supplier<Map<Integer, Integer>> counter,
		Map<Integer, Integer> expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The counts of the keys 1 to 5, each the given count, and 5 the other one
	 *
	 * @param each
	 *            the count of 1 to 4
	 * @param five
	 *            the count of 5
	 * @return the counts
	 */
	static Map<Integer, Integer> oneToFive(final int each, final int five)
	{
		return Map.of(1, each, 2, each, 3, each, 4, each, 5, five);
	}

	/**
	 * The cases of {@link #testCounterMap(Counter)}
	 *
	 * @return the cases
	 */
	static Stream<Counter> counters()
	{
		final List<Integer> oneToFive = List.of(1, 2, 3, 4, 5);
		final List<Integer> fiveTwice = List.of(1, 2, 3, 4, 5, 5);
		final Map<Integer, Integer> oneToTenZero = new HashMap<>();
		for (int i = 1; i <= 10; i++)
		{
			oneToTenZero.put(i, 0);
		}
		return Stream.of(
			new Counter("newCounterMap(1 to 5): each 0", () -> MapFactory.newCounterMap(oneToFive),
				oneToFive(0, 0)),
			new Counter("newCounterMap(1 to 5 and 5 again): each 0, 5 once more",
				() -> MapFactory.newCounterMap(fiveTwice), oneToFive(0, 1)),
			new Counter("newCounterMap(1 to 5, true): each 0",
				() -> MapFactory.newCounterMap(oneToFive, true), oneToFive(0, 0)),
			new Counter("newCounterMap(1 to 5, false): each 1",
				() -> MapFactory.newCounterMap(oneToFive, false), oneToFive(1, 1)),
			new Counter("newCounterMap(no elements): empty",
				() -> MapFactory.newCounterMap(List.<Integer> of()), Map.of()),
			new Counter("newCounterMap(map, 1 to 5): each 0",
				() -> MapFactory.newCounterMap(new HashMap<>(), oneToFive), oneToFive(0, 0)),
			new Counter("newCounterMap(map, 1 to 5, false): each 1",
				() -> MapFactory.newCounterMap(new HashMap<>(), oneToFive, false), oneToFive(1, 1)),
			new Counter("newCounterMap(map, 1 to 5 and 5 again, false): each 1, 5 twice",
				() -> MapFactory.newCounterMap(new HashMap<>(), fiveTwice, false), oneToFive(1, 2)),
			new Counter("newCounterMap(map of 1 counted 1, 1, 2): 1 counted on, 2 counted 0",
				() -> MapFactory.newCounterMap(new HashMap<>(Map.of(1, 1)), List.of(1, 2)),
				Map.of(1, 2, 2, 0)),
			new Counter("newNumberCounterMap(1, 10): 1 to 10, each 0",
				() -> MapFactory.newNumberCounterMap(1, 10), oneToTenZero),
			new Counter("newNumberCounterMap(5, 5): 5 alone, 0",
				() -> MapFactory.newNumberCounterMap(5, 5), Map.of(5, 0)),
			new Counter("newNumberCounterMap(1, 3, map of 2 counted 5): 2 counted 5",
				() -> MapFactory.newNumberCounterMap(1, 3, new HashMap<>(Map.of(2, 5))),
				Map.of(1, 0, 2, 5, 3, 0)),
			new Counter("newNumberCounterMap(1, 3, map of 9 counted 1): 9 not taken",
				() -> MapFactory.newNumberCounterMap(1, 3, new HashMap<>(Map.of(9, 1))),
				Map.of(1, 0, 2, 0, 3, 0)));
	}

	/**
	 * Test for the methods {@code newCounterMap} and {@code newNumberCounterMap}: each element is a
	 * key, counted 0 the first time, or 1 with the flag startByZero false, and one more each time
	 * it comes again
	 *
	 * @param counter
	 *            the call and the counts
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("counters")
	public void testCounterMap(final Counter counter)
	{
		assertEquals(counter.expected(), counter.counter().get());
	}

	/**
	 * Test for the methods {@code newCounterMap(Map, Collection)} and
	 * {@code newCounterMap(Map, Collection, boolean)}: they count the elements into the given map
	 * and return that same map, not a new one, which keeps the counts it held
	 *
	 * @param description
	 *            the name of the case
	 * @param startByZero
	 *            the flag startByZero, null for the method without the flag
	 * @param expected
	 *            the counts of the given map afterwards
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "newCounterMap(map of 9 counted 5, 1 to 2 and 2 again) | | 9=5 1=0 2=1",
			"newCounterMap(map of 9 counted 5, 1 to 2 and 2 again, true) | true | 9=5 1=0 2=1",
			"newCounterMap(map of 9 counted 5, 1 to 2 and 2 again, false) | false | 9=5 1=1 2=2" }, delimiter = '|')
	public void testCounterMapReturnsTheGivenMap(final String description,
		final Boolean startByZero, final String expected)
	{
		final Map<Integer, Integer> given = new HashMap<>(Map.of(9, 5));
		final List<Integer> elements = List.of(1, 2, 2);
		final Map<Integer, Integer> returned = startByZero == null
			? MapFactory.newCounterMap(given, elements)
			: MapFactory.newCounterMap(given, elements, startByZero);
		assertSame(given, returned);
		final Map<Integer, Integer> counts = new HashMap<>();
		for (final String entry : expected.split(" "))
		{
			final String[] keyAndCount = entry.split("=");
			counts.put(Integer.valueOf(keyAndCount[0]), Integer.valueOf(keyAndCount[1]));
		}
		assertEquals(counts, returned);
	}

	/**
	 * Test for the Method {@link MapFactory#newAssosiativeArrayMap()}, with the example of its
	 * Javadoc: in js you can create and fetch associative arrays like this:<br>
	 * <br>
	 * $arrayObj[0]['firstName'] = 'Albert';<br>
	 * $arrayObj[0]['lastName'] = 'Einstein';<br>
	 * $arrayObj[1]['firstName'] = 'Neil';<br>
	 * $arrayObj[1]['lastName'] = 'Armstrong';<br>
	 * <br>
	 * and in java with the map so: arrayMap.get(0).put("firstName", "Albert");
	 */
	@Test
	public void testNewAssosiativeArrayMap()
	{
		final Map<Integer, Map<String, String>> arrayMap = MapFactory.newAssosiativeArrayMap();

		arrayMap.get(0).put("firstName", "Albert");
		arrayMap.get(0).put("lastName", "Einstein");
		arrayMap.get(1).put("firstName", "Neil");
		arrayMap.get(1).put("lastName", "Armstrong");

		assertEquals("Albert", arrayMap.get(0).get("firstName"));
		assertEquals("Einstein", arrayMap.get(0).get("lastName"));
		assertEquals("Neil", arrayMap.get(1).get("firstName"));
		assertEquals("Armstrong", arrayMap.get(1).get("lastName"));
		assertNull(arrayMap.get(2).get("firstName"));
	}

	/**
	 * Test method for {@link MapFactory} with {@link BeanTester}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(MapFactory.class);
	}

}
