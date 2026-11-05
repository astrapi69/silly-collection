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
package io.github.astrapi69.collection.set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.list.ListFactory;

/**
 * The unit test class for the class {@link SetExtensions}.
 */
public class SetExtensionsTest
{

	/**
	 * A set and the array {@link SetExtensions#toArray(Set)} makes of it
	 *
	 * @param description
	 *            the name of the case
	 * @param set
	 *            makes the set
	 * @param expected
	 *            the array, of the expected component type
	 */
	record ToArray(String description, Supplier<Set<?>> set, Object[] expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * A new {@link LinkedHashSet} of the given elements, in their order
	 *
	 * @param elements
	 *            the elements
	 * @return the set
	 */
	static Set<?> linkedSetOf(final Object... elements)
	{
		return new LinkedHashSet<>(Arrays.asList(elements));
	}

	/**
	 * The cases of {@link #testToArray(ToArray)}, the examples of the Javadoc of
	 * {@link SetExtensions#toArray(Set)} among them
	 *
	 * @return the cases
	 */
	static Stream<ToArray> toArrays()
	{
		return Stream.of(
			new ToArray("the TreeSet 1, 2, 3: an Integer[] 1, 2, 3",
				() -> SetFactory.newTreeSet(1, 2, 3), new Integer[] { 1, 2, 3 }),
			new ToArray("an empty TreeSet: an empty Object[]", SetFactory::newTreeSet,
				new Object[0]),
			new ToArray("null, a: a String[] of null and a (#64)", () -> linkedSetOf(null, "a"),
				new String[] { null, "a" }),
			new ToArray("2.5, 1: a Number[] in the order of the set (#64)",
				() -> linkedSetOf(2.5, 1), new Number[] { 2.5, 1 }),
			new ToArray("1, 2.5: a Number[]", () -> linkedSetOf(1, 2.5), new Number[] { 1, 2.5 }),
			new ToArray("a, 1: an Object[], no common class below Object",
				() -> linkedSetOf("a", 1), new Object[] { "a", 1 }),
			new ToArray("null alone: an Object[]", () -> linkedSetOf((Object)null),
				new Object[] { null }));
	}

	/**
	 * Test for method {@link SetExtensions#toArray(Set)}: the elements in the iteration order of
	 * the set, in an array of their most specific common superclass, as its Javadoc says
	 *
	 * @param toArray
	 *            the set and the array
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("toArrays")
	public void testToArray(final ToArray toArray)
	{
		final Object[] actual = SetExtensions.toArray(toArray.set().get());
		assertEquals(toArray.expected().getClass(), actual.getClass());
		assertArrayEquals(toArray.expected(), actual);
	}

	/**
	 * Test for method {@link SetExtensions#toArray(Set)} with what it refuses: null, and the empty
	 * {@code Object[]} of an empty set assigned to a {@code String[]}, which its Javadoc names
	 *
	 * @param description
	 *            the name of the case
	 * @param exception
	 *            the class of the exception
	 * @param call
	 *            the call
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@MethodSource("refusedToArrays")
	public void testToArrayRefused(final String description,
		final Class<? extends RuntimeException> exception, final Executable call)
	{
		assertThrows(exception, call);
	}

	/**
	 * The cases of {@link #testToArrayRefused(String, Class, Executable)}
	 *
	 * @return the name of the case, the class of the exception and the call
	 */
	static Stream<Arguments> refusedToArrays()
	{
		return Stream.of(
			Arguments.of("null: IllegalArgumentException of Argument.notNull (#64)",
				IllegalArgumentException.class, (Executable)() -> SetExtensions.toArray(null)),
			Arguments.of("an empty HashSet<String> into a String[]: ClassCastException (#64)",
				ClassCastException.class, (Executable)() -> {
					final String[] strings = SetExtensions.toArray(new HashSet<String>());
					assertEquals(0, strings.length);
				}));
	}

	/**
	 * Test for the methods {@link SetExtensions#toSet(java.util.Collection)} and
	 * {@link SetExtensions#toSortedSet(java.util.Collection)}: the set holds each element of the
	 * collection once, the sorted set in their natural order
	 *
	 * @param description
	 *            the name of the case
	 * @param set
	 *            the conversion
	 * @param expected
	 *            the elements, in their order for a sorted set
	 * @param sorted
	 *            true for a sorted set
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@MethodSource("toSets")
	public void testToSetAndToSortedSet(final String description, final Supplier<Set<String>> set,
		final List<String> expected, final boolean sorted)
	{
		final Set<String> actual = set.get();
		if (sorted)
		{
			assertEquals(expected, new ArrayList<>(actual));
		}
		else
		{
			assertEquals(Set.copyOf(expected), actual);
		}
	}

	/**
	 * Six names, not in their natural order
	 *
	 * @return a new list of the names
	 */
	static List<String> names()
	{
		return ListFactory.newArrayList("Anton", "Alex", "Berta", "Brad", "Caesar", "Leonardo");
	}

	/**
	 * The cases of {@link #testToSetAndToSortedSet(String, Supplier, List, boolean)}
	 *
	 * @return the name of the case, the conversion, the elements and the flag sorted
	 */
	static Stream<Arguments> toSets()
	{
		final List<String> sortedNames = List.of("Alex", "Anton", "Berta", "Brad", "Caesar",
			"Leonardo");
		return Stream.of(
			Arguments.of("toSet, six names: each of them",
				(Supplier<Set<String>>)() -> SetExtensions.toSet(names()), names(), false),
			Arguments.of("toSet, Anton twice: once",
				(Supplier<Set<String>>)() -> SetExtensions
					.toSet(ListFactory.newArrayList("Anton", "Alex", "Anton")),
				List.of("Anton", "Alex"), false),
			Arguments.of("toSet, an empty collection: an empty set",
				(Supplier<Set<String>>)() -> SetExtensions
					.toSet(ListFactory.<String> newArrayList()),
				List.of(), false),
			Arguments.of(
				"toSet, null: an empty set, as the collection of SetFactory.newHashSet is optional",
				(Supplier<Set<String>>)() -> SetExtensions.toSet(null), List.of(), false),
			Arguments.of("toSortedSet, six names: in their natural order",
				(Supplier<Set<String>>)() -> SetExtensions.toSortedSet(names()), sortedNames, true),
			Arguments.of("toSortedSet, Anton twice: once, in the natural order",
				(Supplier<Set<String>>)() -> SetExtensions
					.toSortedSet(ListFactory.newArrayList("Anton", "Alex", "Anton")),
				List.of("Alex", "Anton"), true),
			Arguments.of("toSortedSet, an empty collection: an empty set",
				(Supplier<Set<String>>)() -> SetExtensions
					.toSortedSet(ListFactory.<String> newArrayList()),
				List.of(), true));
	}

	/**
	 * Test method for {@link SetExtensions} with {@link BeanTester}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(SetExtensions.class);
	}

}
