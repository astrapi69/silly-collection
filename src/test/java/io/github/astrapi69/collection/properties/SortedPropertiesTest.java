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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.comparator.NullCheckComparator;
import io.github.astrapi69.io.SerializedObjectExtensions;

/**
 * The unit test class for the class {@link SortedProperties}.
 *
 * @author Asterios Raptis
 */
public class SortedPropertiesTest
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
	 * Puts B = 2, C = 3, A = 1 and D = 4 into the given properties, in this order
	 *
	 * @param properties
	 *            the properties
	 * @return the given properties
	 */
	static <P extends Properties> P filled(final P properties)
	{
		properties.put("B", "2");
		properties.put("C", "3");
		properties.put("A", "1");
		properties.put("D", "4");
		return properties;
	}

	/**
	 * A {@link SortedProperties} with the given defaults whose
	 * {@link SortedProperties#newComparator()} sorts the keys in the reverse order of their string
	 * form
	 *
	 * @param defaults
	 *            the defaults
	 * @return the new {@link SortedProperties}
	 */
	static SortedProperties reversed(final Properties defaults)
	{
		return new SortedProperties(defaults)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Comparator<Object> newComparator()
			{
				return NullCheckComparator.of(
					(final Object o1, final Object o2) -> o2.toString().compareTo(o1.toString()),
					false);
			}
		};
	}

	/**
	 * The comparator in the reverse order of the string form, for
	 * {@link SortedProperties#of(Properties, Comparator)}
	 */
	static final Comparator<Object> DESCENDING = (o1, o2) -> o2.toString().compareTo(o1.toString());

	/**
	 * A view of the keys of a {@link SortedProperties}, as a list in its order
	 *
	 * @param name
	 *            the name of the view
	 * @param view
	 *            the view
	 */
	record View(String name, Function<SortedProperties, List<Object>> view) {
	}

	/** The view {@link SortedProperties#keys()} */
	static final View KEYS = new View("keys()", properties -> Collections.list(properties.keys()));

	/** The view {@link SortedProperties#keySet()} */
	static final View KEY_SET = new View("keySet()",
		properties -> new ArrayList<>(properties.keySet()));

	/** The view {@link SortedProperties#propertyNames()} */
	static final View PROPERTY_NAMES = new View("propertyNames()",
		properties -> new ArrayList<>(Collections.list(properties.propertyNames())));

	/**
	 * A {@link SortedProperties}, a view of its keys and their order
	 *
	 * @param description
	 *            the name of the case
	 * @param properties
	 *            makes the properties
	 * @param view
	 *            the view
	 * @param expected
	 *            the keys in their order
	 */
	record Order(String description, Supplier<SortedProperties> properties, View view,
		List<Object> expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The rows of the three views of the same properties, which have no defaults
	 *
	 * @param name
	 *            the name of the properties
	 * @param properties
	 *            makes the properties
	 * @param expected
	 *            the keys in their order
	 * @return a row for each view
	 */
	static Stream<Order> inEachView(final String name, final Supplier<SortedProperties> properties,
		final List<Object> expected)
	{
		return Stream.of(KEYS, KEY_SET, PROPERTY_NAMES)
			.map(view -> new Order(
				name + ", " + view.name() + ": "
					+ expected.stream().map(String::valueOf).collect(Collectors.joining(", ")),
				properties, view, expected));
	}

	/**
	 * The cases of {@link #testOrder(Order)}
	 *
	 * @return the cases
	 */
	static Stream<Order> orders()
	{
		final List<Object> ascending = List.of("A", "B", "C", "D");
		final List<Object> descending = List.of("D", "C", "B", "A");
		return Stream.of(
			inEachView("new SortedProperties() with B, C, A, D",
				() -> filled(new SortedProperties()), ascending),
			inEachView("of() with B, C, A, D", () -> filled(SortedProperties.of()), ascending),
			inEachView("a reverse newComparator with B, C, A, D",
				() -> filled(reversed(new Properties())), descending),
			inEachView("of(new Properties(), descending) with B, C, A", () -> {
				final SortedProperties properties = SortedProperties.of(new Properties(),
					DESCENDING);
				properties.setProperty("B", "2");
				properties.setProperty("C", "3");
				properties.setProperty("A", "1");
				return properties;
			}, List.of("C", "B", "A")),
			Stream.of(
				new Order("the keys 10, 9, 100, keys(): their string form, 10, 100, 9",
					SortedPropertiesTest::numbers, KEYS, List.of(10, 100, 9)),
				new Order("the keys 10, 9, 100, keySet(): their string form, 10, 100, 9",
					SortedPropertiesTest::numbers, KEY_SET, List.of(10, 100, 9)),
				new Order("defaults E with B, C, A, D, keys(): A, B, C, D, not E",
					() -> filled(new SortedProperties(propertiesOf("E", "5"))), KEYS, ascending),
				new Order("defaults E with B, C, A, D, keySet(): A, B, C, D, not E",
					() -> filled(new SortedProperties(propertiesOf("E", "5"))), KEY_SET, ascending),
				new Order("defaults E with B, C, A, D, propertyNames(): A, B, C, D, E",
					() -> filled(new SortedProperties(propertiesOf("E", "5"))), PROPERTY_NAMES,
					List.of("A", "B", "C", "D", "E")),
				new Order("of(defaults E) with B, C, A, D, propertyNames(): A, B, C, D, E",
					() -> filled(SortedProperties.of(propertiesOf("E", "5"))), PROPERTY_NAMES,
					List.of("A", "B", "C", "D", "E")),
				new Order("a reverse newComparator, defaults E with B, C, A, D, keys(): D, C, B, A",
					() -> filled(reversed(propertiesOf("E", "5"))), KEYS, descending),
				new Order("of(defaults B, C, A, D, descending), propertyNames(): D, C, B, A",
					() -> SortedProperties.of(filled(new Properties()), DESCENDING), PROPERTY_NAMES,
					descending),
				new Order("of(defaults B, C, A, D, descending), keys(): none of the defaults",
					() -> SortedProperties.of(filled(new Properties()), DESCENDING), KEYS,
					List.of()),
				new Order("defaults A, B, propertyNames(): A, B",
					() -> new SortedProperties(propertiesOf("A", "1", "B", "2")), PROPERTY_NAMES,
					List.of("A", "B")),
				new Order("defaults A, B with C and B, propertyNames(): A, B, C, each once",
					SortedPropertiesTest::withDefaults, PROPERTY_NAMES, List.of("A", "B", "C")),
				new Order(
					"of(defaults A, B, descending) with C and B, propertyNames(): C, B, A, each once",
					() -> {
						final SortedProperties properties = SortedProperties
							.of(propertiesOf("A", "1", "B", "2"), DESCENDING);
						properties.setProperty("C", "3");
						properties.setProperty("B", "20");
						return properties;
					}, PROPERTY_NAMES, List.of("C", "B", "A")),
				new Order(
					"the defaults of the defaults, with D, propertyNames(): A, B, C, D, of all of them",
					() -> {
						final SortedProperties nested = new SortedProperties(withDefaults());
						nested.setProperty("D", "4");
						return nested;
					}, PROPERTY_NAMES, List.of("A", "B", "C", "D"))))
			.flatMap(Function.identity());
	}

	/**
	 * A {@link SortedProperties} of the keys 10, 9 and 100, which are no strings
	 *
	 * @return the new properties
	 */
	static SortedProperties numbers()
	{
		final SortedProperties properties = new SortedProperties();
		properties.put(10, "ten");
		properties.put(9, "nine");
		properties.put(100, "hundred");
		return properties;
	}

	/**
	 * A {@link SortedProperties} with the defaults A = 1 and B = 2 and the entries C = 3 and B = 20
	 *
	 * @return the new properties
	 */
	static SortedProperties withDefaults()
	{
		final SortedProperties properties = new SortedProperties(propertiesOf("A", "1", "B", "2"));
		properties.setProperty("C", "3");
		properties.setProperty("B", "20");
		return properties;
	}

	/**
	 * Test for the constructors, the factory methods and a subclass that overrides
	 * {@link SortedProperties#newComparator()}, with the methods {@link SortedProperties#keys()},
	 * {@link SortedProperties#keySet()} and {@link SortedProperties#propertyNames()}: each lists
	 * the keys sorted with the comparator, by default by their string form; keys() and keySet()
	 * list the keys of the entries, propertyNames() also those of the defaults, each once, as
	 * {@link Properties#propertyNames()} does
	 *
	 * @param order
	 *            the properties, the view and the order of the keys
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("orders")
	public void testOrder(final Order order)
	{
		assertEquals(order.expected(), order.view().view().apply(order.properties().get()));
	}

	/**
	 * Test for the constructor {@link SortedProperties#SortedProperties(Properties)} and the
	 * factory methods {@link SortedProperties#of(Properties)} and
	 * {@link SortedProperties#of(Properties, Comparator)}: the given properties are the defaults,
	 * whose values {@link Properties#getProperty(String)} finds, unless an entry of the same key
	 * has a value of its own
	 *
	 * @param description
	 *            the name of the case
	 * @param properties
	 *            makes the properties
	 * @param key
	 *            the key
	 * @param value
	 *            the value getProperty finds
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@MethodSource("defaults")
	public void testDefaults(final String description, final Supplier<Properties> properties,
		final String key, final String value)
	{
		assertEquals(value, properties.get().getProperty(key));
	}

	/**
	 * The cases of {@link #testDefaults(String, Supplier, String, String)}
	 *
	 * @return the name of the case, the properties, the key and the value
	 */
	static Stream<Arguments> defaults()
	{
		return Stream.of(
			Arguments.of("new SortedProperties(defaults E = 5) with B, C, A, D: E is 5",
				(Supplier<Properties>)() -> filled(new SortedProperties(propertiesOf("E", "5"))),
				"E", "5"),
			Arguments.of("a reverse newComparator, defaults E = 5 with B, C, A, D: E is 5",
				(Supplier<Properties>)() -> filled(reversed(propertiesOf("E", "5"))), "E", "5"),
			Arguments.of("of(defaults E = 5): E is 5",
				(Supplier<Properties>)() -> SortedProperties.of(propertiesOf("E", "5")), "E", "5"),
			Arguments.of("of(defaults B, C, A, D, descending): D is 4",
				(Supplier<Properties>)() -> SortedProperties.of(filled(new Properties()),
					DESCENDING),
				"D", "4"),
			Arguments.of("defaults B = 2 with B = 20: B is 20, its own value",
				(Supplier<Properties>)SortedPropertiesTest::withDefaults, "B", "20"));
	}

	/**
	 * A change of the properties A = 1, B = 2, C = 3, D = 4 through their {@link Set} of keys, what
	 * it returns and the keys that stay
	 *
	 * @param description
	 *            the name of the case
	 * @param change
	 *            the change
	 * @param result
	 *            what it returns, null for nothing
	 * @param keys
	 *            the keys that stay, in their order
	 */
	record KeySetChange(String description, Function<Set<Object>, Object> change, Object result,
		List<Object> keys) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testKeySetChange(KeySetChange)}
	 *
	 * @return the cases
	 */
	static Stream<KeySetChange> keySetChanges()
	{
		final List<Object> all = List.of("A", "B", "C", "D");
		return Stream.of(
			new KeySetChange("remove(A): true, A goes", keySet -> keySet.remove("A"), true,
				List.of("B", "C", "D")),
			new KeySetChange("remove(E), not a key: false, all stay", keySet -> keySet.remove("E"),
				false, all),
			new KeySetChange("removeAll(A, E): true, A goes",
				keySet -> keySet.removeAll(List.of("A", "E")), true, List.of("B", "C", "D")),
			new KeySetChange("removeAll(E), not a key: false, all stay",
				keySet -> keySet.removeAll(List.of("E")), false, all),
			new KeySetChange("retainAll(C, D): true, A and B go",
				keySet -> keySet.retainAll(List.of("C", "D")), true, List.of("C", "D")),
			new KeySetChange("retainAll(A, B, C, D), all of them: false, all stay",
				keySet -> keySet.retainAll(all), false, all),
			new KeySetChange("clear(): all go", keySet -> {
				keySet.clear();
				return null;
			}, null, List.of()),
			new KeySetChange("iterator(), next() and remove(): A goes", keySet -> {
				final Iterator<Object> iterator = keySet.iterator();
				final Object next = iterator.next();
				iterator.remove();
				return next;
			}, "A", List.of("B", "C", "D")));
	}

	/**
	 * Test for the {@link Set} of {@link SortedProperties#keySet()}: it is backed by the
	 * properties, as {@link java.util.Map#keySet()} specifies, so removing keys from it removes
	 * their entries, and the set and the properties hold the same keys afterwards
	 *
	 * @param change
	 *            the change, what it returns and the keys that stay
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("keySetChanges")
	public void testKeySetChange(final KeySetChange change)
	{
		final SortedProperties properties = new SortedProperties();
		properties.setProperty("A", "1");
		properties.setProperty("B", "2");
		properties.setProperty("C", "3");
		properties.setProperty("D", "4");
		final Set<Object> keySet = properties.keySet();
		assertEquals(change.result(), change.change().apply(keySet));
		assertEquals(change.keys(), Collections.list(properties.keys()));
		assertEquals(change.keys(), new ArrayList<>(keySet));
		assertEquals(change.keys().size(), keySet.size());
		assertEquals(change.keys().size(), properties.size());
		for (final String key : List.of("A", "B", "C", "D"))
		{
			assertEquals(change.keys().contains(key), keySet.contains(key), "contains " + key);
			assertEquals(change.keys().contains(key), properties.containsKey(key),
				"containsKey " + key);
		}
	}

	/**
	 * Test for the methods {@link Set#add(Object)} and {@link Set#addAll(java.util.Collection)} of
	 * {@link SortedProperties#keySet()}: the set does not support them, as
	 * {@link java.util.Map#keySet()} specifies, and the properties stay as they are
	 *
	 * @param description
	 *            the name of the case
	 * @param add
	 *            the call of add or addAll
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@MethodSource("keySetAdds")
	public void testKeySetAdd(final String description, final Function<Set<Object>, Object> add)
	{
		final SortedProperties properties = new SortedProperties();
		properties.setProperty("A", "1");
		final Set<Object> keySet = properties.keySet();
		assertThrows(UnsupportedOperationException.class, () -> add.apply(keySet));
		assertFalse(properties.containsKey("B"));
		assertEquals(1, properties.size());
	}

	/**
	 * The cases of {@link #testKeySetAdd(String, Function)}
	 *
	 * @return the name of the case and the call
	 */
	static Stream<Arguments> keySetAdds()
	{
		return Stream.of(
			Arguments.of("add(B): UnsupportedOperationException",
				(Function<Set<Object>, Object>)keySet -> keySet.add("B")),
			Arguments.of("addAll(B): UnsupportedOperationException",
				(Function<Set<Object>, Object>)keySet -> keySet
					.addAll(ListFactory.newArrayList("B"))));
	}

	/**
	 * Test the method {@link Iterator#remove()} of the iterator of
	 * {@link SortedProperties#keySet()}: it removes the entry of the key that next() returned last,
	 * and throws an {@link IllegalStateException} if next() was not called since the last remove(),
	 * as {@link Iterator#remove()} specifies
	 */
	@Test
	public void testKeySetIteratorRemove()
	{
		final SortedProperties properties = new SortedProperties();
		properties.setProperty("B", "2");
		properties.setProperty("A", "1");

		final Iterator<Object> iterator = properties.keySet().iterator();
		assertThrows(IllegalStateException.class, iterator::remove);
		assertEquals("A", iterator.next());
		iterator.remove();
		assertFalse(properties.containsKey("A"));
		assertEquals(1, properties.size());
		assertThrows(IllegalStateException.class, iterator::remove);

		assertEquals("B", iterator.next());
		assertFalse(iterator.hasNext());
		assertEquals("2", properties.getProperty("B"));
	}

	/**
	 * Test the method {@link SortedProperties#keySet()} after a change of the properties: the set
	 * is backed by them, so it shows the change, sorted
	 */
	@Test
	public void testKeySetAfterAChangeOfTheProperties()
	{
		final SortedProperties properties = new SortedProperties();
		properties.setProperty("B", "2");
		properties.setProperty("A", "1");

		final Set<Object> keySet = properties.keySet();
		properties.setProperty("C", "3");
		properties.remove("A");
		assertEquals(ListFactory.newArrayList("B", "C"), new ArrayList<>(keySet));
		assertFalse(keySet.contains("A"));
		assertTrue(keySet.contains("C"));
		assertEquals(2, keySet.size());
	}

	/**
	 * Test the method {@link SortedProperties#propertyNames()} with keys that are no strings: it
	 * throws a {@link ClassCastException}, as {@link Properties#propertyNames()} specifies
	 */
	@Test
	public void testPropertyNamesWithKeysThatAreNoStrings()
	{
		final SortedProperties properties = new SortedProperties();
		properties.put(10, "ten");
		properties.put(9, "nine");
		properties.put(100, "hundred");
		assertThrows(ClassCastException.class, properties::propertyNames);
	}

	/**
	 * Test the serialization of a {@link SortedProperties}: its comparator, a lambda, is not part
	 * of its serialized form, so it serializes also after it created its comparator, and the copy
	 * creates one of its own with {@link SortedProperties#newComparator()} when it needs one. The
	 * copy has the entries and the defaults of the original and sorts the keys by their string form
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred
	 * @throws ClassNotFoundException
	 *             is thrown when a class of the serialized object is not found
	 */
	@Test
	public void testSerialization() throws IOException, ClassNotFoundException
	{
		final Properties defaults = new Properties();
		defaults.setProperty("C", "3");
		final SortedProperties properties = new SortedProperties(defaults);
		properties.setProperty("B", "2");
		properties.setProperty("A", "1");

		// keys() creates the comparator
		assertEquals(ListFactory.newArrayList("A", "B"), Collections.list(properties.keys()));
		final SortedProperties copy = (SortedProperties)SerializedObjectExtensions
			.toObject(SerializedObjectExtensions.toByteArray(properties));
		assertEquals(properties, copy);
		assertEquals("3", copy.getProperty("C"));
		assertEquals(ListFactory.newArrayList("A", "B"), Collections.list(copy.keys()));
		assertEquals(ListFactory.newArrayList("A", "B", "C"),
			Collections.list(copy.propertyNames()));

		// by their string form "10" < "9" < "A" < "B"; integers and strings have no common order
		copy.put(10, "ten");
		copy.put(9, "nine");
		assertEquals(ListFactory.<Object> newArrayList(10, 9, "A", "B"),
			new ArrayList<>(copy.keySet()));
	}

}
