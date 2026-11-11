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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.comparator.NullCheckComparator;
import io.github.astrapi69.io.SerializedObjectExtensions;

/**
 * The unit test class for the class {@link IndexSortedProperties}.
 *
 * @author Asterios Raptis
 */
public class IndexSortedPropertiesTest
{

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
	 * The properties B = 2, C = 3, A = 1, D = 4, E = 5
	 *
	 * @return new properties
	 */
	static Properties aToE()
	{
		return propertiesOf("B", "2", "C", "3", "A", "1", "D", "4", "E", "5");
	}

	/**
	 * The properties B = 2, C = 3, A = 1, D = 4
	 *
	 * @return new properties
	 */
	static Properties aToD()
	{
		return propertiesOf("B", "2", "C", "3", "A", "1", "D", "4");
	}

	/**
	 * The comparator in the reverse order of the string form
	 */
	static final Comparator<Object> DESCENDING = (o1, o2) -> o2.toString().compareTo(o1.toString());

	/**
	 * The comparator in the order of the string form
	 */
	static final Comparator<Object> ASCENDING = NullCheckComparator
		.of((final Object o1, final Object o2) -> o1.toString().compareTo(o2.toString()));

	/**
	 * Puts all entries of the given map into the given properties with
	 * {@link IndexSortedProperties#putAll(Map)}
	 *
	 * @param properties
	 *            the properties
	 * @param map
	 *            the entries
	 * @return the given properties
	 */
	static IndexSortedProperties putAll(final IndexSortedProperties properties, final Map<?, ?> map)
	{
		properties.putAll(map);
		return properties;
	}

	/**
	 * Asserts that the index of the given properties holds the given values in their order:
	 * {@link IndexSortedProperties#get(int)} and {@link IndexSortedProperties#getProperty(int)}
	 * give them, {@link IndexSortedProperties#indexOf(Object)} finds them at their index, and an
	 * index out of range, the size or -1, gives null; indexOf gives -1 for a value of no key and
	 * for null, which a {@link Properties} never holds, also for an empty index (#117)
	 *
	 * @param values
	 *            the values in the order of the index, all different
	 * @param properties
	 *            the properties
	 */
	static void assertIndex(final List<String> values, final IndexSortedProperties properties)
	{
		assertEquals(values.size(), properties.size(), "size");
		for (int index = 0; index < values.size(); index++)
		{
			assertEquals(values.get(index), properties.get(index), "get(" + index + ")");
			assertEquals(values.get(index), properties.getProperty(index),
				"getProperty(" + index + ")");
			assertEquals(index, properties.indexOf(values.get(index)),
				"indexOf(" + values.get(index) + ")");
		}
		assertNull(properties.get(values.size()), "get(size)");
		assertNull(properties.getProperty(values.size()), "getProperty(size)");
		assertNull(properties.get(-1), "get(-1)");
		assertNull(properties.getProperty(-1), "getProperty(-1)");
		assertEquals(-1, properties.indexOf("6"), "indexOf(6), a value of no key");
		assertEquals(-1, properties.indexOf(null), "indexOf(null), never a value (#117)");
	}

	/**
	 * An {@link IndexSortedProperties} made and filled in some way, the values of its index and
	 * values that getProperty finds by their key
	 *
	 * @param description
	 *            the name of the case
	 * @param properties
	 *            makes the properties
	 * @param values
	 *            the values in the order of the index
	 * @param found
	 *            keys and values that {@link Properties#getProperty(String)} finds, those of the
	 *            defaults too
	 */
	record Construction(String description, Supplier<IndexSortedProperties> properties,
		List<String> values, Map<String, String> found) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testConstruction(Construction)}
	 *
	 * @return the cases
	 */
	static Stream<Construction> constructions()
	{
		final List<String> oneToFive = List.of("1", "2", "3", "4", "5");
		final List<String> oneToFour = List.of("1", "2", "3", "4");
		return Stream.of(
			new Construction("of(), putAll of A to E: 1 to 5",
				() -> putAll(IndexSortedProperties.of(), aToE()), oneToFive, Map.of()),
			new Construction("new IndexSortedProperties(), setProperty B, C, A, D, E: 1 to 5",
				() -> {
					final IndexSortedProperties properties = new IndexSortedProperties();
					properties.setProperty("B", "2");
					properties.setProperty("C", "3");
					properties.setProperty("A", "1");
					properties.setProperty("D", "4");
					properties.setProperty("E", "5");
					return properties;
				}, oneToFive, Map.of()),
			new Construction("of(defaults A to E), nothing put: empty, getProperty finds E",
				() -> IndexSortedProperties.of(aToE()), List.of(), Map.of("E", "5")),
			new Construction("of(defaults A to E), putAll of them: 1 to 5",
				() -> putAll(IndexSortedProperties.of(aToE()), aToE()), oneToFive, Map.of()),
			new Construction("new IndexSortedProperties(defaults A to D), nothing put: empty",
				() -> new IndexSortedProperties(aToD()), List.of(), Map.of("A", "1")),
			new Construction("new IndexSortedProperties(defaults A to D), putAll of them: 1 to 4",
				() -> putAll(new IndexSortedProperties(aToD()), aToD()), oneToFour, Map.of()),
			new Construction("a reverse newComparator, defaults A to D, nothing put: empty",
				() -> reversed(aToD()), List.of(), Map.of("A", "1")),
			new Construction("a reverse newComparator, defaults A to D, putAll of them: 4 to 1",
				() -> putAll(reversed(aToD()), aToD()), List.of("4", "3", "2", "1"), Map.of()),
			new Construction(
				"new IndexSortedProperties(defaults A to D), setProperty E: 5 alone, the defaults by key",
				() -> {
					final IndexSortedProperties properties = new IndexSortedProperties(aToD());
					properties.setProperty("E", "5");
					return properties;
				}, List.of("5"), Map.of("A", "1", "E", "5")),
			new Construction("of(defaults A to E, ascending), nothing put: empty",
				() -> IndexSortedProperties.of(aToE(), ASCENDING), List.of(), Map.of("E", "5")),
			new Construction("of(defaults A to E, ascending), putAll of them: 1 to 5",
				() -> putAll(IndexSortedProperties.of(aToE(), ASCENDING), aToE()), oneToFive,
				Map.of()),
			new Construction("of(defaults A to E, ascending, false), nothing put: empty",
				() -> IndexSortedProperties.of(aToE(), ASCENDING, false), List.of(),
				Map.of("E", "5")),
			new Construction("of(defaults A to E, ascending, false), putAll of them: 1 to 5",
				() -> putAll(IndexSortedProperties.of(aToE(), ASCENDING, false), aToE()), oneToFive,
				Map.of()),
			new Construction(
				"defaults that iterate C, B, A, nothing put: empty, getProperty finds A",
				() -> new IndexSortedProperties(descendingDefaults()), List.of(), Map.of("A", "1")),
			new Construction(
				"defaults that iterate C, B, A, putAll of them: 1, 2, 3, sorted by the index",
				() -> putAll(new IndexSortedProperties(descendingDefaults()), descendingDefaults()),
				List.of("1", "2", "3"), Map.of()),
			new Construction("of(defaults A, C, descending), putAll of them: 3, 1",
				() -> putAll(IndexSortedProperties.of(propertiesOf("A", "1", "C", "3"), DESCENDING),
					propertiesOf("A", "1", "C", "3")),
				List.of("3", "1"), Map.of()),
			new Construction("of(defaults A, C, descending), putAll of them, then B: 3, 2, 1",
				() -> withB(
					putAll(IndexSortedProperties.of(propertiesOf("A", "1", "C", "3"), DESCENDING),
						propertiesOf("A", "1", "C", "3"))),
				List.of("3", "2", "1"), Map.of()),
			new Construction(
				"of(defaults A, C, descending, true), nothing put: empty, getProperty finds A",
				() -> IndexSortedProperties.of(propertiesOf("A", "1", "C", "3"), DESCENDING, true),
				List.of(), Map.of("A", "1")),
			new Construction("of(defaults A, C, descending, true), putAll of them, then B: 3, 2, 1",
				() -> withB(putAll(
					IndexSortedProperties.of(propertiesOf("A", "1", "C", "3"), DESCENDING, true),
					propertiesOf("A", "1", "C", "3"))),
				List.of("3", "2", "1"), Map.of()),
			new Construction("defaults A, B, put C: 3 alone, getProperty finds A", () -> {
				final IndexSortedProperties properties = new IndexSortedProperties(
					propertiesOf("A", "1", "B", "2"));
				properties.put("C", "3");
				return properties;
			}, List.of("3"), Map.of("A", "1")),
			new Construction("defaults A, B, putAll of C: 3 alone, getProperty finds A",
				() -> putAll(new IndexSortedProperties(propertiesOf("A", "1", "B", "2")),
					propertiesOf("C", "3")),
				List.of("3"), Map.of("A", "1")),
			new Construction("of(), put A = 1 and A = 2, a key twice: 2 alone", () -> {
				final IndexSortedProperties properties = IndexSortedProperties.of();
				properties.put("A", "1");
				properties.put("A", "2");
				return properties;
			}, List.of("2"), Map.of()),
			new Construction("of(), put A twice, remove A, put B = 3: 3 alone", () -> {
				final IndexSortedProperties properties = IndexSortedProperties.of();
				properties.put("A", "1");
				properties.put("A", "2");
				properties.remove("A");
				properties.put("B", "3");
				return properties;
			}, List.of("3"), Map.of()),
			new Construction("of(), load of B=2, A=1, A=3, a key twice: 3, 2", () -> {
				final IndexSortedProperties properties = IndexSortedProperties.of();
				try
				{
					properties.load(new StringReader("B=2\nA=1\nA=3\n"));
				}
				catch (final IOException exception)
				{
					throw new UncheckedIOException(exception);
				}
				return properties;
			}, List.of("3", "2"), Map.of()),
			new Construction("of(), clear() when empty: empty", () -> {
				final IndexSortedProperties properties = IndexSortedProperties.of();
				properties.clear();
				return properties;
			}, List.of(), Map.of()));
	}

	/**
	 * An {@link IndexSortedProperties} with the given defaults whose newComparator() sorts the keys
	 * in the reverse order of their string form
	 *
	 * @param defaults
	 *            the defaults
	 * @return the new properties
	 */
	static IndexSortedProperties reversed(final Properties defaults)
	{
		return new IndexSortedProperties(defaults)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Comparator<Object> newComparator()
			{
				return NullCheckComparator.of(DESCENDING, false);
			}
		};
	}

	/**
	 * A {@link SortedProperties} A = 1, B = 2, C = 3 that iterates its keys in descending order, C,
	 * B, A
	 *
	 * @return the new properties
	 */
	static SortedProperties descendingDefaults()
	{
		final SortedProperties defaults = SortedProperties.of(new Properties(), DESCENDING);
		defaults.setProperty("A", "1");
		defaults.setProperty("B", "2");
		defaults.setProperty("C", "3");
		return defaults;
	}

	/**
	 * Sets the property B = 2 of the given properties
	 *
	 * @param properties
	 *            the properties
	 * @return the given properties
	 */
	static IndexSortedProperties withB(final IndexSortedProperties properties)
	{
		properties.setProperty("B", "2");
		return properties;
	}

	/**
	 * Test for the constructors, the factory methods, a subclass that overrides
	 * {@link IndexSortedProperties#newComparator()}, and the ways to fill them: the index holds
	 * exactly the keys of the entries, sorted with the comparator, and not the keys of the
	 * defaults, whose values {@link Properties#getProperty(String)} finds and
	 * {@link IndexSortedProperties#indexOf(Object)} does not
	 *
	 * @param construction
	 *            the properties, the values of the index and the values found by key
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("constructions")
	public void testConstruction(final Construction construction)
	{
		final IndexSortedProperties properties = construction.properties().get();
		assertIndex(construction.values(), properties);
		construction.found().forEach((key, value) -> {
			assertEquals(value, properties.getProperty(key), "getProperty(" + key + ")");
			if (!construction.values().contains(value))
			{
				assertEquals(-1, properties.indexOf(value), "indexOf(" + value + ")");
			}
		});
	}

	/**
	 * A change of the properties A = 1, B = 2, C = 3, what it returns and the values of the index
	 * afterwards
	 *
	 * @param description
	 *            the name of the case
	 * @param change
	 *            the change
	 * @param result
	 *            what it returns, null for nothing
	 * @param values
	 *            the values in the order of the index afterwards
	 */
	record Change(String description, Function<IndexSortedProperties, Object> change, Object result,
		List<String> values) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testChange(Change)}
	 *
	 * @return the cases
	 */
	static Stream<Change> changes()
	{
		final List<String> unchanged = List.of("1", "2", "3");
		final List<String> withD = List.of("1", "2", "3", "4");
		return Stream.of(
			new Change("put(A, 10), a key there: 1, the index holds A once",
				properties -> properties.put("A", "10"), "1", List.of("10", "2", "3")),
			new Change("put(D, 4), a new key: null", properties -> properties.put("D", "4"), null,
				withD),
			new Change("setProperty(A, 10): 1", properties -> properties.setProperty("A", "10"),
				"1", List.of("10", "2", "3")),
			new Change("putAll of D = 4 and A = 10: both in the index",
				properties -> putAll(properties, propertiesOf("D", "4", "A", "10")).size(), 4,
				List.of("10", "2", "3", "4")),
			new Change("remove(A): 1", properties -> properties.remove("A"), "1",
				List.of("2", "3")),
			new Change("remove(X), not a key: null, all stay", properties -> properties.remove("X"),
				null, unchanged),
			new Change(
				"put(D, the Integer 4), whose getProperty is null: indexOf(null) -1 (#117), remove(D)",
				properties -> {
					properties.put("D", 4);
					final List<Object> found = Arrays.asList(properties.getProperty("D"),
						properties.indexOf(null));
					properties.remove("D");
					return found;
				}, Arrays.asList(null, -1), unchanged),
			new Change("remove(A), remove(B), remove(C) and remove(C) again: 1, 2, 3, null",
				properties -> Arrays.asList(properties.remove("A"), properties.remove("B"),
					properties.remove("C"), properties.remove("C")),
				Arrays.asList("1", "2", "3", null), List.of()),
			new Change("remove(B, 2): true", properties -> properties.remove("B", "2"), true,
				List.of("1", "3")),
			new Change("remove(B, 3), another value: false, B stays",
				properties -> properties.remove("B", "3"), false, unchanged),
			new Change("remove(A, 1), (B, 2), (C, 3) and (C, 3) again: true, true, true, false",
				properties -> List.of(properties.remove("A", "1"), properties.remove("B", "2"),
					properties.remove("C", "3"), properties.remove("C", "3")),
				List.of(true, true, true, false), List.of()),
			new Change("remove(0): 1, the first", properties -> properties.remove(0), "1",
				List.of("2", "3")),
			new Change("remove(2): 3, the last", properties -> properties.remove(2), "3",
				List.of("1", "2")),
			new Change("remove(3), the size: null, all stay", properties -> properties.remove(3),
				null, unchanged),
			new Change("remove(-1): null, all stay", properties -> properties.remove(-1), null,
				unchanged),
			new Change("remove(0) four times: 1, 2, 3, null",
				properties -> Arrays.asList(properties.remove(0), properties.remove(0),
					properties.remove(0), properties.remove(0)),
				Arrays.asList("1", "2", "3", null), List.of()),
			new Change("clear(): empty", properties -> {
				properties.clear();
				return null;
			}, null, List.of()), new Change("clear(), then C = 3: 3 is the first", properties -> {
				properties.clear();
				return properties.setProperty("C", "3");
			}, null, List.of("3")),
			new Change("putIfAbsent(A, 10), A there: 1, A stays 1",
				properties -> properties.putIfAbsent("A", "10"), "1", unchanged),
			new Change("putIfAbsent(D, 4): null", properties -> properties.putIfAbsent("D", "4"),
				null, withD),
			new Change("merge(D, 4), a new key: 4",
				properties -> properties.merge("D", "4", (oldValue, value) -> value), "4", withD),
			new Change("merge(B, 20) to null: null, B goes",
				properties -> properties.merge("B", "20", (oldValue, value) -> null), null,
				List.of("1", "3")),
			new Change("compute(D) to 4: 4",
				properties -> properties.compute("D", (key, value) -> "4"), "4", withD),
			new Change("compute(A) to null: null, A goes",
				properties -> properties.compute("A", (key, value) -> null), null,
				List.of("2", "3")),
			new Change("computeIfAbsent(D) to 4: 4",
				properties -> properties.computeIfAbsent("D", key -> "4"), "4", withD),
			new Change("computeIfPresent(A) to 10: 10",
				properties -> properties.computeIfPresent("A", (key, value) -> "10"), "10",
				List.of("10", "2", "3")),
			new Change("computeIfPresent(A) to null: null, A goes",
				properties -> properties.computeIfPresent("A", (key, value) -> null), null,
				List.of("2", "3")),
			new Change("replace(A, 10): 1", properties -> properties.replace("A", "10"), "1",
				List.of("10", "2", "3")),
			new Change("replace(B, 2, 20): true", properties -> properties.replace("B", "2", "20"),
				true, List.of("1", "20", "3")),
			new Change("replace(D, 4), not a key: null, all stay",
				properties -> properties.replace("D", "4"), null, unchanged),
			new Change("replaceAll with a 0 appended: 10, 20, 30", properties -> {
				properties.replaceAll((key, value) -> value + "0");
				return null;
			}, null, List.of("10", "20", "30")),
			new Change("entrySet().removeIf of B: true, B goes",
				properties -> properties.entrySet().removeIf(entry -> "B".equals(entry.getKey())),
				true, List.of("1", "3")),
			new Change("values().remove(1): true, A goes",
				properties -> properties.values().remove("1"), true, List.of("2", "3")),
			new Change("keySet().remove(B): true, B goes",
				properties -> properties.keySet().remove("B"), true, List.of("1", "3")),
			new Change("keySet().iterator(), next() and remove(): A goes", properties -> {
				final Iterator<Object> iterator = properties.keySet().iterator();
				final Object next = iterator.next();
				iterator.remove();
				return next;
			}, "A", List.of("2", "3")));
	}

	/**
	 * Test for the methods that change the keys or the values, the views {@code keySet()},
	 * {@code entrySet()} and {@code values()} included: each returns what {@link java.util.Map}
	 * specifies, and the index follows the change, as the class Javadoc says, also an index read
	 * before the change
	 *
	 * @param change
	 *            the change, what it returns and the values of the index afterwards
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("changes")
	public void testChange(final Change change)
	{
		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.setProperty("B", "2");
		properties.setProperty("A", "1");
		properties.setProperty("C", "3");
		// read the index before the change, so that the change has to update it
		assertIndex(List.of("1", "2", "3"), properties);
		assertEquals(change.result(), change.change().apply(properties));
		assertIndex(change.values(), properties);
	}

	/**
	 * Test for the factory methods
	 * {@link IndexSortedProperties#of(Properties, Comparator, boolean)} and
	 * {@link IndexSortedProperties#of(Properties, Comparator)} and of
	 * {@link IndexSortedProperties#of()}: the comparator puts null after any other key when the
	 * flag nullIsGreaterThan is true, and before it otherwise
	 *
	 * @param description
	 *            the name of the case
	 * @param properties
	 *            makes the properties
	 * @param sign
	 *            the sign of the comparison of null with A
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@MethodSource("nullComparisons")
	public void testComparatorWithNull(final String description,
		final Supplier<IndexSortedProperties> properties, final int sign)
	{
		assertEquals(sign, Integer.signum(properties.get().getComparator().compare(null, "A")));
	}

	/**
	 * The cases of {@link #testComparatorWithNull(String, Supplier, int)}
	 *
	 * @return the name of the case, the properties and the sign
	 */
	static Stream<Arguments> nullComparisons()
	{
		return Stream.of(
			Arguments.of("of(defaults, descending, true): null after A",
				(Supplier<IndexSortedProperties>)() -> IndexSortedProperties.of(new Properties(),
					DESCENDING, true),
				1),
			Arguments.of("of(defaults, descending, false): null before A",
				(Supplier<IndexSortedProperties>)() -> IndexSortedProperties.of(new Properties(),
					DESCENDING, false),
				-1),
			Arguments.of("of(defaults, descending): null before A",
				(Supplier<IndexSortedProperties>)() -> IndexSortedProperties.of(new Properties(),
					DESCENDING),
				-1),
			Arguments.of("of(): null before A",
				(Supplier<IndexSortedProperties>)IndexSortedProperties::of, -1));
	}

	/**
	 * Test the serialization of an {@link IndexSortedProperties}: the comparator, a lambda, is not
	 * part of its serialized form, so it serializes; the copy has the entries and the index of the
	 * original, and sorts a key put into it later into its own index with a comparator of its own
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 * @throws ClassNotFoundException
	 *             is thrown when a class of the serialized object is not found
	 */
	@Test
	public void testSerialization() throws IOException, ClassNotFoundException
	{
		final IndexSortedProperties properties = IndexSortedProperties.of();
		properties.setProperty("C", "3");
		properties.setProperty("B", "2");

		final IndexSortedProperties copy = (IndexSortedProperties)SerializedObjectExtensions
			.toObject(SerializedObjectExtensions.toByteArray(properties));
		assertEquals(properties, copy);
		assertEquals("2", copy.get(0));
		assertEquals("3", copy.get(1));
		assertNull(copy.get(2));

		copy.setProperty("A", "1");
		assertEquals("1", copy.get(0));
		assertEquals("2", copy.get(1));
		assertEquals("3", copy.get(2));
		assertEquals(0, copy.indexOf("1"));
		assertNull(properties.get(2));
	}

	/**
	 * Test the method {@link IndexSortedProperties#clone()}: the copy has an index of its own, so
	 * that a change of the copy leaves the index of the original as it is, and the other way round
	 */
	@Test
	public void testClone()
	{
		final IndexSortedProperties original = IndexSortedProperties.of();
		original.setProperty("A", "1");
		assertEquals("1", original.get(0));

		final IndexSortedProperties copy = (IndexSortedProperties)original.clone();
		copy.setProperty("B", "2");
		copy.remove("A");
		assertEquals("1", original.getProperty("A"));
		assertEquals("1", original.get(0));
		assertNull(original.get(1));
		assertEquals(0, original.indexOf("1"));
		assertEquals("2", copy.get(0));
		assertNull(copy.get(1));

		original.setProperty("C", "3");
		assertEquals("3", original.get(1));
		assertEquals("2", copy.get(0));
		assertNull(copy.get(1));
	}

	/**
	 * Test the constructor with a subclass whose {@link IndexSortedProperties#newComparator()}
	 * returns a descending comparator from a field of its own: the constructor does not call
	 * newComparator(), the keys of the defaults are not in the index, and the first read of the
	 * index calls newComparator(), with the field set, and sorts with its comparator
	 */
	@Test
	public void testConstructorWithTheComparatorFromAFieldOfASubclass()
	{
		final Properties defaults = new Properties();
		defaults.setProperty("A", "1");
		defaults.setProperty("B", "2");
		defaults.setProperty("C", "3");

		// newComparator() records whether the field of the subclass was set when it was called
		final List<Boolean> calls = new ArrayList<>();
		final IndexSortedProperties properties = new IndexSortedProperties(defaults)
		{
			private static final long serialVersionUID = 1L;

			private final Comparator<Object> descending = (o1, o2) -> o2.toString()
				.compareTo(o1.toString());

			@Override
			protected Comparator<Object> newComparator()
			{
				calls.add(descending != null);
				return descending;
			}
		};
		assertTrue(calls.isEmpty());
		assertNull(properties.getProperty(0));
		assertEquals("1", properties.getProperty("A"));

		properties.putAll(defaults);
		assertEquals("3", properties.getProperty(0));
		assertEquals("2", properties.getProperty(1));
		assertEquals("1", properties.getProperty(2));
		assertEquals(ListFactory.newArrayList(true), calls);
	}

	/**
	 * Test for the methods {@link IndexSortedProperties#get(int)},
	 * {@link IndexSortedProperties#getProperty(int)} and
	 * {@link IndexSortedProperties#indexOf(Object)} on properties of the entries a = x, 1 = y and b
	 * = 2, whose index in the order of the string form of the keys is 1, 0 and 2: an entry whose
	 * key is not a String has no property, so the index methods give null for it and skip it, as
	 * {@link Properties#getProperty(String)} and {@link Properties#stringPropertyNames()} do
	 * (#148); a value that is not a String is not found, as before
	 *
	 * @param description
	 *            the name of the case
	 * @param call
	 *            the method
	 * @param argument
	 *            the index or the object
	 * @param expected
	 *            what the call gives, nothing for null, or the simple name of its exception
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "get(0), the key 1 is an Integer: null | get | 0 | ",
			"get(1), the key a: x | get | 1 | x", "get(2), the value 2 is an Integer | get | 2 | ",
			"getProperty(0), the key 1 is an Integer: null | getProperty | 0 | ",
			"getProperty(1), the key a: x | getProperty | 1 | x",
			"getProperty(2), the value 2 is an Integer | getProperty | 2 | ",
			"indexOf(x), which key a holds, after the key 1: its index 1 | indexOf | x | 1",
			"indexOf(y), which the key 1 holds: skipped, -1 | indexOf | y | -1",
			"indexOf(2), the value of the key b is an Integer: -1 | indexOf | 2 | -1" }, delimiter = '|')
	public void testNonStringEntries(final String description, final String call,
		final String argument, final String expected)
	{
		final IndexSortedProperties properties = new IndexSortedProperties();
		properties.setProperty("a", "x");
		properties.put(1, "y");
		properties.put("b", 2);
		final Supplier<Object> read = switch (call)
		{
			case "get" -> () -> properties.get(Integer.parseInt(argument));
			case "getProperty" -> () -> properties.getProperty(Integer.parseInt(argument));
			case "indexOf" -> () -> "2".equals(argument)
				? properties.indexOf(2)
				: properties.indexOf(argument);
			default -> throw new IllegalArgumentException("no call " + call);
		};
		if (expected != null && expected.endsWith("Exception"))
		{
			assertEquals(expected,
				assertThrows(RuntimeException.class, read::get).getClass().getSimpleName());
		}
		else
		{
			assertEquals(expected, read.get() == null ? null : read.get().toString());
		}
	}

}
