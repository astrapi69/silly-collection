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
package io.github.astrapi69.collection.array;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

/**
 * The unit test class for the class {@link ArrayExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class ArrayExtensionsTest
{

	/**
	 * The names the lookup tables search in: {@code Caesar, Dora, Emil, Anton}, in this order, so
	 * that the order is not the alphabetical one
	 *
	 * @return a new array of the names
	 */
	static String[] names()
	{
		return new String[] { "Caesar", "Dora", "Emil", "Anton" };
	}

	/**
	 * A call of {@link ArrayExtensions#join(Object[], Object...)} or
	 * {@link ArrayExtensions#concatenate(Object[], Object[])} with two arrays, and what it returns
	 *
	 * @param description
	 *            the name of the case
	 * @param method
	 *            join or concatenate
	 * @param first
	 *            the first array, may be null
	 * @param second
	 *            the second array, may be null
	 * @param expected
	 *            the returned array, null if it returns null
	 * @param itself
	 *            true if the expected array is a given array itself, false if it is a new array
	 */
	record TwoArrays(String description, BinaryOperator<Object[]> method, Object[] first,
		Object[] second, Object[] expected, boolean itself) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link ArrayExtensions#join(Object[], Object...)}: a new array of the component
	 * type of the first array, or the other array itself if one of them is null; and of
	 * {@link ArrayExtensions#concatenate(Object[], Object[])}, which also hands back the existing
	 * array itself if the elements are empty, as its Javadoc says
	 *
	 * @return the cases
	 */
	static Stream<TwoArrays> twoArrays()
	{
		final BinaryOperator<Object[]> join = ArrayExtensions::join;
		final BinaryOperator<Object[]> concatenate = ArrayExtensions::concatenate;
		final String[] abc = { "A", "B", "C" };
		final String[] de = { "D", "E" };
		final Integer[] numbers = { 1, 2, 3 };
		final Integer[] empty = { };
		return Stream.of(
			new TwoArrays("join, A, B, C and D, E: a new array A to E", join, abc, de,
				new String[] { "A", "B", "C", "D", "E" }, false),
			new TwoArrays("join, null and D, E: the second array itself", join, null, de, de, true),
			new TwoArrays(
				"join, A, B, C and null: the first array itself", join, abc, null, abc, true),
			new TwoArrays("join, null and null: null", join, null, null, null, true),
			new TwoArrays("join, the Number[] 1.5 and the Integer[] 2: a new Number[]", join,
				new Number[] { 1.5 }, new Integer[] { 2 }, new Number[] { 1.5, 2 }, false),
			new TwoArrays("concatenate, 1, 2, 3 and 4, 5, 6: a new array 1 to 6", concatenate,
				numbers, new Integer[] { 4, 5, 6 }, new Integer[] { 1, 2, 3, 4, 5, 6 }, false),
			new TwoArrays("concatenate, an empty array and 1, 2, 3: a new array 1, 2, 3",
				concatenate, empty, numbers, new Integer[] { 1, 2, 3 }, false),
			new TwoArrays("concatenate, the Number[] 1.5 and 2: a new Number[]", concatenate,
				new Number[] { 1.5 }, new Number[] { 2 }, new Number[] { 1.5, 2 }, false),
			new TwoArrays("concatenate, null and 1, 2, 3: the elements array itself", concatenate,
				null, numbers, numbers, true),
			new TwoArrays("concatenate, 1, 2, 3 and an empty array: the existing array itself",
				concatenate, numbers, empty, numbers, true),
			new TwoArrays("concatenate, 1, 2, 3 and null: the existing array itself", concatenate,
				numbers, null, numbers, true),
			new TwoArrays("concatenate, an empty array and null: the empty array itself",
				concatenate, empty, null, empty, true),
			new TwoArrays("concatenate, null and an empty array: null", concatenate, null, empty,
				null, true),
			new TwoArrays("concatenate, null and null: null", concatenate, null, null, null, true));
	}

	/**
	 * Test for the methods {@link ArrayExtensions#join(Object[], Object...)} and
	 * {@link ArrayExtensions#concatenate(Object[], Object[])}: a new array holds the elements of
	 * the first array followed by those of the second, in the component type of the first
	 *
	 * @param arrays
	 *            the method, the two arrays and what it returns
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("twoArrays")
	public void testJoinAndConcatenate(final TwoArrays arrays)
	{
		final Object[] actual = arrays.method().apply(arrays.first(), arrays.second());
		if (arrays.itself())
		{
			assertSame(arrays.expected(), actual);
			return;
		}
		assertArrayEquals(arrays.expected(), actual);
		assertEquals(arrays.expected().getClass(), actual.getClass());
		assertNotSame(arrays.first(), actual);
		assertNotSame(arrays.second(), actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#join(Object[], Object...)} with arrays whose component
	 * types are incompatible: the second cannot be stored in an array of the first
	 *
	 * @param description
	 *            the name of the case
	 * @param first
	 *            the first array
	 * @param second
	 *            the second array
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@MethodSource("incompatibleArrays")
	public void testJoinWithIncompatibleTypes(final String description, final Object[] first,
		final Object[] second)
	{
		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> ArrayExtensions.join(first, second));
		assertEquals("Cannot store " + second.getClass().getComponentType().getName()
			+ " in an array of " + first.getClass().getComponentType().getName(),
			exception.getMessage());
	}

	/**
	 * The arrays of incompatible component types for
	 * {@link #testJoinWithIncompatibleTypes(String, Object[], Object[])}
	 *
	 * @return the name of the case and the two arrays
	 */
	static Stream<Arguments> incompatibleArrays()
	{
		return Stream.of(
			Arguments.of("an Integer[] and a String[]", new Integer[] { 1, 2, 3 },
				new String[] { "A", "B" }),
			Arguments.of("an Integer[] and a Number[], a supertype", new Integer[] { 1 },
				new Number[] { 2 }));
	}

	/**
	 * A conversion of an array into a list, a set or a stream, and what it holds
	 *
	 * @param description
	 *            the name of the case
	 * @param conversion
	 *            the conversion; a stream is collected into a list, and the result of
	 *            {@link ArrayExtensions#toSet(Object[])} into a list, to compare its order too
	 * @param expected
	 *            what the conversion holds
	 */
	record Conversion(String description, Supplier<Object> conversion, Object expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link ArrayExtensions#asList(Object[])},
	 * {@link ArrayExtensions#toList(Object[])}, {@link ArrayExtensions#asSet(Object...)},
	 * {@link ArrayExtensions#asStream(Object...)} and {@link ArrayExtensions#toSet(Object[])}
	 *
	 * @return the cases
	 */
	static Stream<Conversion> conversions()
	{
		final List<String> oneToSeven = List.of("1", "2", "3", "4", "5", "6", "7");
		return Stream.of(
			new Conversion("asList of 1 to 7: the elements in their order",
				() -> ArrayExtensions.asList(new String[] { "1", "2", "3", "4", "5", "6", "7" }),
				oneToSeven),
			new Conversion("toList of 1 to 7: the elements in their order",
				() -> ArrayExtensions.toList(new String[] { "1", "2", "3", "4", "5", "6", "7" }),
				oneToSeven),
			new Conversion("asSet of 1 to 7: each element",
				() -> ArrayExtensions.asSet("1", "2", "3", "4", "5", "6", "7"),
				Set.copyOf(oneToSeven)),
			new Conversion("asSet of 1, 2, 3, 2: each element once",
				() -> ArrayExtensions.asSet("1", "2", "3", "2"), Set.of("1", "2", "3")),
			new Conversion("asSet of no elements: an empty set", ArrayExtensions::asSet, Set.of()),
			new Conversion("asStream of 1 to 7: the elements in their order",
				() -> ArrayExtensions.asStream("1", "2", "3", "4", "5", "6", "7")
					.collect(Collectors.toList()),
				oneToSeven),
			new Conversion("toSet of 1 to 5: each element, in their order",
				() -> List.copyOf(ArrayExtensions.toSet(new Integer[] { 1, 2, 3, 4, 5 })),
				List.of(1, 2, 3, 4, 5)),
			new Conversion("toSet of 3, 1, 2, 1: each element once, in the order of the first",
				() -> List.copyOf(ArrayExtensions.toSet(new Integer[] { 3, 1, 2, 1 })),
				List.of(3, 1, 2)));
	}

	/**
	 * Test for the methods {@link ArrayExtensions#asList(Object[])},
	 * {@link ArrayExtensions#toList(Object[])}, {@link ArrayExtensions#asSet(Object...)},
	 * {@link ArrayExtensions#asStream(Object...)} and {@link ArrayExtensions#toSet(Object[])}
	 *
	 * @param conversion
	 *            the conversion and what it holds
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("conversions")
	public void testConversion(final Conversion conversion)
	{
		assertEquals(conversion.expected(), conversion.conversion().get());
	}

	/**
	 * Test for method {@link ArrayExtensions#subArray(Object[], int, int)}: the elements from the
	 * start, inclusive, to the end, exclusive, of 1 to 5, as {@link Arrays#copyOfRange} gives them,
	 * which fills an end beyond the array with null
	 *
	 * @param description
	 *            the name of the case
	 * @param start
	 *            the start index
	 * @param end
	 *            the end index
	 * @param expected
	 *            the elements of the sub array, separated by a space, null for a null element
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "1 to 4: 2, 3, 4 | 1 | 4 | 2 3 4",
			"0 to 5: all of them | 0 | 5 | 1 2 3 4 5", "2 to 3: one element | 2 | 3 | 3",
			"2 to 2: none | 2 | 2 | ''", "5 to 5, at the end: none | 5 | 5 | ''",
			"3 to 7, beyond the end: 4, 5 and two nulls | 3 | 7 | 4 5 null null" }, delimiter = '|')
	public void testSubArray(final String description, final int start, final int end,
		final String expected)
	{
		final Integer[] expectedArray = expected.isEmpty()
			? new Integer[0]
			: Arrays.stream(expected.split(" "))
				.map(element -> "null".equals(element) ? null : Integer.valueOf(element))
				.toArray(Integer[]::new);
		final Integer[] actual = ArrayExtensions.subArray(new Integer[] { 1, 2, 3, 4, 5 }, start,
			end);
		assertArrayEquals(expectedArray, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#subArray(Object[], int, int)} with a range that
	 * {@link Arrays#copyOfRange} refuses, on 1 to 5
	 *
	 * @param description
	 *            the name of the case
	 * @param start
	 *            the start index
	 * @param end
	 *            the end index
	 * @param exception
	 *            the class of the exception it throws
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource({
			"a negative start: ArrayIndexOutOfBoundsException, -1, 3, java.lang.ArrayIndexOutOfBoundsException",
			"a start beyond the length: ArrayIndexOutOfBoundsException, 6, 7, java.lang.ArrayIndexOutOfBoundsException",
			"a start greater than the end: IllegalArgumentException, 4, 3, java.lang.IllegalArgumentException" })
	public void testSubArrayWithInvalidRange(final String description, final int start,
		final int end, final Class<? extends Exception> exception)
	{
		final Integer[] original = { 1, 2, 3, 4, 5 };
		assertThrows(exception, () -> ArrayExtensions.subArray(original, start, end));
	}

	/**
	 * Test for method {@link ArrayExtensions#toTwoDimensionalArray(Map, Class)}: a row of key and
	 * value for each entry, in the order of the map
	 *
	 * @param description
	 *            the name of the case
	 * @param expected
	 *            the rows, the entries of the map
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@MethodSource("twoDimensionalArrays")
	public void testToTwoDimensionalArray(final String description, final String[][] expected)
	{
		final Map<String, String> map = new LinkedHashMap<>();
		for (final String[] row : expected)
		{
			map.put(row[0], row[1]);
		}
		final String[][] actual = ArrayExtensions.toTwoDimensionalArray(map, String.class);
		assertEquals(String[][].class, actual.getClass());
		assertArrayEquals(expected, actual);
	}

	/**
	 * The maps of {@link #testToTwoDimensionalArray(String, String[][])}, as the rows they are made
	 * of
	 *
	 * @return the name of the case and the rows
	 */
	static Stream<Arguments> twoDimensionalArrays()
	{
		return Stream.of(
			Arguments
				.of("four entries, not in the order of their keys: four rows in their order",
					new String[][] { { "1", "value1" }, { "3", "value3" }, { "4", "value4" },
							{ "2", "value2" } }),
			Arguments.of("no entry: no row", new String[0][]));
	}

	/**
	 * A call of one of the methods {@code getLastIndex} and the index it returns
	 *
	 * @param description
	 *            the name of the case
	 * @param call
	 *            the call
	 * @param expected
	 *            the last index
	 */
	record LastIndex(String description, IntSupplier call, int expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The three cases of a method {@code getLastIndex}: values, an empty array and null
	 *
	 * @param name
	 *            the type of the array
	 * @param values
	 *            the call with values
	 * @param lastIndex
	 *            the last index of the values
	 * @param empty
	 *            the call with an empty array
	 * @param none
	 *            the call with null
	 * @return the cases
	 */
	static Stream<LastIndex> lastIndexesOf(final String name, final IntSupplier values,
		final int lastIndex, final IntSupplier empty, final IntSupplier none)
	{
		return Stream.of(
			new LastIndex(name + ", " + (lastIndex + 1) + " values: " + lastIndex, values,
				lastIndex),
			new LastIndex(name + ", empty: -1", empty, -1),
			new LastIndex(name + ", null: -1", none, -1));
	}

	/**
	 * The cases of {@link ArrayExtensions#getLastIndex(Object[])} and of the methods
	 * {@code getLastIndex} of the primitive types
	 *
	 * @return the cases
	 */
	static Stream<LastIndex> lastIndexes()
	{
		return Stream.of(
			lastIndexesOf("Object[]",
				() -> ArrayExtensions.getLastIndex(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }), 8,
				() -> ArrayExtensions.getLastIndex(new Integer[0]),
				() -> ArrayExtensions.getLastIndex((Integer[])null)),
			lastIndexesOf("int[]",
				() -> ArrayExtensions.getLastIndex(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }), 8,
				() -> ArrayExtensions.getLastIndex(new int[0]),
				() -> ArrayExtensions.getLastIndex((int[])null)),
			lastIndexesOf("boolean[]",
				() -> ArrayExtensions.getLastIndex(new boolean[] { true, true, false }), 2,
				() -> ArrayExtensions.getLastIndex(new boolean[0]),
				() -> ArrayExtensions.getLastIndex((boolean[])null)),
			lastIndexesOf("byte[]",
				() -> ArrayExtensions.getLastIndex(
					new byte[] { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97 }),
				12, () -> ArrayExtensions.getLastIndex(new byte[0]),
				() -> ArrayExtensions.getLastIndex((byte[])null)),
			lastIndexesOf("char[]",
				() -> ArrayExtensions.getLastIndex(new char[] { 'f', 'o', 'o' }), 2,
				() -> ArrayExtensions.getLastIndex(new char[0]),
				() -> ArrayExtensions.getLastIndex((char[])null)),
			lastIndexesOf("double[]",
				() -> ArrayExtensions.getLastIndex(new double[] { 1.1D, 2.1D, 3.1D }), 2,
				() -> ArrayExtensions.getLastIndex(new double[0]),
				() -> ArrayExtensions.getLastIndex((double[])null)),
			lastIndexesOf("float[]",
				() -> ArrayExtensions.getLastIndex(new float[] { 1.0F, 2.0F, 3.0F }), 2,
				() -> ArrayExtensions.getLastIndex(new float[0]),
				() -> ArrayExtensions.getLastIndex((float[])null)),
			lastIndexesOf("long[]", () -> ArrayExtensions.getLastIndex(new long[] { 1L, 2L, 3L }),
				2, () -> ArrayExtensions.getLastIndex(new long[0]),
				() -> ArrayExtensions.getLastIndex((long[])null)),
			lastIndexesOf("short[]", () -> ArrayExtensions.getLastIndex(new short[] { 1, 2, 3 }), 2,
				() -> ArrayExtensions.getLastIndex(new short[0]),
				() -> ArrayExtensions.getLastIndex((short[])null)))
			.flatMap(Function.identity());
	}

	/**
	 * Test for method {@link ArrayExtensions#getLastIndex(Object[])} and the methods
	 * {@code getLastIndex} of the primitive types: the length minus one, -1 for an empty array and
	 * for null
	 *
	 * @param lastIndex
	 *            the call and the last index
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("lastIndexes")
	public void testGetLastIndex(final LastIndex lastIndex)
	{
		assertEquals(lastIndex.expected(), lastIndex.call().getAsInt());
	}

	/**
	 * Test for method {@link ArrayExtensions#arraycopyWithSystem(Object[], Object[])}: the elements
	 * of the source in the destination, which is returned; null for a null source
	 *
	 * @param description
	 *            the name of the case
	 * @param source
	 *            the source, may be null
	 * @param destination
	 *            the destination
	 * @param expected
	 *            the returned array, or null if it returns null
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@MethodSource("arraycopies")
	public void testArraycopyWithSystem(final String description, final Integer[] source,
		final Integer[] destination, final Integer[] expected)
	{
		final Integer[] actual = ArrayExtensions.arraycopyWithSystem(source, destination);
		assertArrayEquals(expected, actual);
		if (actual != null)
		{
			assertSame(destination, actual);
		}
	}

	/**
	 * The cases of {@link #testArraycopyWithSystem(String, Integer[], Integer[], Integer[])}
	 *
	 * @return the name of the case, the source, the destination and the returned array
	 */
	static Stream<Arguments> arraycopies()
	{
		return Stream.of(
			Arguments.of("1 to 9 into a destination of the same length: the destination with them",
				new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }, new Integer[9],
				new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }),
			Arguments.of("1, 2 into the longer 9, 9, 9: the destination 1, 2, 9",
				new Integer[] { 1, 2 }, new Integer[] { 9, 9, 9 }, new Integer[] { 1, 2, 9 }),
			Arguments.of("a null source: null", null, new Integer[9], null));
	}

	/**
	 * A search for an element in an array, and where it is
	 *
	 * @param description
	 *            the name of the case
	 * @param array
	 *            the array
	 * @param element
	 *            the element, may be null
	 * @param index
	 *            the index of the element, -1 if it is not in the array
	 */
	record Search(String description, Object[] array, Object element, int index) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testIndexOfGetIndexAndContains(Search)}
	 *
	 * @return the cases
	 */
	static Stream<Search> searches()
	{
		final String[] numbers = { "1", "2", "3", "4", "5", "6", "7" };
		final String[] empty = { };
		return Stream.of(new Search("7, the last of 1 to 7: 6", numbers, "7", 6),
			new Search("1, the first of 1 to 7: 0", numbers, "1", 0),
			new Search("8, not in 1 to 7: -1", numbers, "8", -1),
			new Search("null, not in 1 to 7: -1", numbers, null, -1),
			new Search("2, twice in 1, 2, 2: 1, the first", new String[] { "1", "2", "2" }, "2", 1),
			new Search("null in 1, null: 1", new String[] { "1", null }, null, 1),
			new Search("7 in an empty array: -1", empty, "7", -1),
			new Search("null in an empty array: -1", empty, null, -1),
			new Search("the Integer 8 in an empty String array: -1", empty, 8, -1));
	}

	/**
	 * Test for the methods {@link ArrayExtensions#indexOf(Object[], Object)},
	 * {@link ArrayExtensions#getIndex(Object[], Object)} and
	 * {@link ArrayExtensions#contains(Object[], Object)}: the index of the first occurrence, and
	 * contained if and only if there is one
	 *
	 * @param search
	 *            the array, the element and its index
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("searches")
	public void testIndexOfGetIndexAndContains(final Search search)
	{
		assertEquals(search.index(), ArrayExtensions.indexOf(search.array(), search.element()),
			"indexOf");
		assertEquals(search.index(), ArrayExtensions.getIndex(search.array(), search.element()),
			"getIndex");
		assertEquals(search.index() != -1,
			ArrayExtensions.contains(search.array(), search.element()), "contains");
	}

	/**
	 * Test for method {@link ArrayExtensions#containsAtLeastOne(Object[], Object...)}
	 *
	 * @param description
	 *            the name of the case
	 * @param array
	 *            the array
	 * @param elements
	 *            the elements to search
	 * @param expected
	 *            true if at least one of the elements is in the array
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@MethodSource("containsAtLeastOne")
	public void testContainsAtLeastOne(final String description, final Object[] array,
		final Object[] elements, final boolean expected)
	{
		assertEquals(expected, ArrayExtensions.containsAtLeastOne(array, elements));
	}

	/**
	 * The cases of {@link #testContainsAtLeastOne(String, Object[], Object[], boolean)}
	 *
	 * @return the name of the case, the array, the elements and the answer
	 */
	static Stream<Arguments> containsAtLeastOne()
	{
		final String[] numbers = { "1", "2", "3", "4", "5", "6", "7" };
		final String[] empty = { };
		return Stream.of(
			Arguments.of("7 or 6 in 1 to 7, both in it: true", numbers, new String[] { "7", "6" },
				true),
			Arguments.of("8 or 7 in 1 to 7, the second in it: true", numbers,
				new String[] { "8", "7" }, true),
			Arguments.of("8 or 9 in 1 to 7, neither in it: false", numbers,
				new String[] { "8", "9" }, false),
			Arguments.of("no element in 1 to 7: false", numbers, new String[0], false),
			Arguments.of("7 in an empty array: false", empty, new String[] { "7" }, false),
			Arguments.of("the Integer 8 in an empty array: false", empty, new Object[] { 8 },
				false));
	}

	/**
	 * An array and its first and last element
	 *
	 * @param description
	 *            the name of the case
	 * @param array
	 *            the array, may be null
	 * @param first
	 *            the first element, null if there is none
	 * @param last
	 *            the last element, null if there is none
	 */
	record Ends(String description, String[] array, String first, String last) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testFirstAndLast(Ends)}
	 *
	 * @return the cases
	 */
	static Stream<Ends> ends()
	{
		return Stream.of(
			new Ends("1 to 7: 1 and 7", new String[] { "1", "2", "3", "4", "5", "6", "7" }, "1",
				"7"),
			new Ends("a single element: it, first and last", new String[] { "x" }, "x", "x"),
			new Ends("an empty array: none", new String[0], null, null),
			new Ends("null: none", null, null, null),
			new Ends("null, a: null and a (#114)", new String[] { null, "a" }, null, "a"),
			new Ends("a, null: a and null (#114)", new String[] { "a", null }, "a", null));
	}

	/**
	 * Test for the methods {@link ArrayExtensions#getFirst(Object[])},
	 * {@link ArrayExtensions#getFirstElement(Object[])}, {@link ArrayExtensions#getLast(Object[])}
	 * and {@link ArrayExtensions#getLastElement(Object[])}: the first and the last element, null or
	 * an empty {@link Optional} for an empty array and for null, and for a first or last element
	 * that is null (#114)
	 *
	 * @param ends
	 *            the array and its first and last element
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("ends")
	public void testFirstAndLast(final Ends ends)
	{
		assertEquals(ends.first(), ArrayExtensions.getFirst(ends.array()), "getFirst");
		assertEquals(Optional.ofNullable(ends.first()),
			ArrayExtensions.getFirstElement(ends.array()), "getFirstElement");
		assertEquals(ends.last(), ArrayExtensions.getLast(ends.array()), "getLast");
		assertEquals(Optional.ofNullable(ends.last()), ArrayExtensions.getLastElement(ends.array()),
			"getLastElement");
	}

	/**
	 * Test for method {@link ArrayExtensions#isFirst(Object[], Object)}
	 *
	 * @param description
	 *            the name of the case
	 * @param array
	 *            the array, its elements separated by a space, '' for an empty one
	 * @param element
	 *            the element, may be null
	 * @param expected
	 *            the answer of isFirst
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource({ "1 in 1 to 7: first, 1 2 3 4 5 6 7, 1, true",
			"7 in 1 to 7: not first, 1 2 3 4 5 6 7, 7, false",
			"4 in 1 to 7: not first, 1 2 3 4 5 6 7, 4, false",
			"8 not in 1 to 7: not first, 1 2 3 4 5 6 7, 8, false",
			"null not in 1 to 7: not first, 1 2 3 4 5 6 7, , false", "x alone: first, x, x, true",
			"2 twice at the start of 2 2 3: first, 2 2 3, 2, true",
			"2 twice at the end of 1 2 2: not first, 1 2 2, 2, false",
			"x in an empty array: not first, '', x, false",
			"null in an empty array: not first, '', , false" })
	public void testIsFirst(final String description, final String array, final String element,
		final boolean expected)
	{
		assertEquals(expected, ArrayExtensions.isFirst(elements(array), element));
	}

	/**
	 * Test for method {@link ArrayExtensions#isLast(Object[], Object)}: the element is compared
	 * with the last element, so an element twice at the end is last, and an empty array and null
	 * have no last element (#114)
	 *
	 * @param description
	 *            the name of the case
	 * @param array
	 *            the array, its elements separated by a space, null for a null element, '' for an
	 *            empty array, empty for null
	 * @param element
	 *            the element, may be null
	 * @param expected
	 *            the answer of isLast
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource({ "7 in 1 to 7: last, 1 2 3 4 5 6 7, 7, true",
			"1 in 1 to 7: not last, 1 2 3 4 5 6 7, 1, false",
			"4 in 1 to 7: not last, 1 2 3 4 5 6 7, 4, false",
			"8 not in 1 to 7: not last, 1 2 3 4 5 6 7, 8, false",
			"null not in 1 to 7: not last, 1 2 3 4 5 6 7, , false", "x alone: last, x, x, true",
			"2 twice at the start of 2 2 3: not last, 2 2 3, 2, false",
			"2 twice at the end of 1 2 2: last (#114), 1 2 2, 2, true",
			"x in an empty array: not last (#114), '', x, false",
			"null in an empty array: not last (#114), '', , false",
			"null at the end of a null: last (#114), a null, , true",
			"null at the start of null a: not last, null a, , false",
			"x in a null array: not last (#114), , x, false",
			"null in a null array: not last (#114), , , false" })
	public void testIsLast(final String description, final String array, final String element,
		final boolean expected)
	{
		final String[] elements = array == null
			? null
			: Arrays.stream(elements(array)).map(each -> "null".equals(each) ? null : each)
				.toArray(String[]::new);
		assertEquals(expected, ArrayExtensions.isLast(elements, element));
	}

	/**
	 * A call of a method of {@link ArrayExtensions} with a null array and what comes of it
	 *
	 * @param description
	 *            the name of the method
	 * @param call
	 *            the call with the null array
	 * @param outcome
	 *            the simple name of the exception it throws, or what it returns as text
	 */
	record NullArrayCall(String description, Supplier<Object> call, String outcome) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testNullArray(NullArrayCall)}: what a null array gives, for each method
	 * of {@link ArrayExtensions} that takes one (#144); isFirst is left to #143
	 *
	 * @return the cases
	 */
	static Stream<NullArrayCall> nullArrayCalls()
	{
		final String[] none = null;
		final String npe = "NullPointerException";
		final String iae = "IllegalArgumentException";
		return Stream.of(
			new NullArrayCall("indexOf", () -> ArrayExtensions.indexOf(none, "a"), npe),
			new NullArrayCall("getIndex", () -> ArrayExtensions.getIndex(none, "a"), npe),
			new NullArrayCall("contains", () -> ArrayExtensions.contains(none, "a"), npe),
			new NullArrayCall("containsAtLeastOne",
				() -> ArrayExtensions.containsAtLeastOne(none, "a"), npe),
			new NullArrayCall("containsAtLeastOne, null elements",
				() -> ArrayExtensions.containsAtLeastOne(new String[] { "a" }, (String[])null),
				npe),
			new NullArrayCall("getNextIndex", () -> ArrayExtensions.getNextIndex(none, "a"), npe),
			new NullArrayCall("getPreviousIndex", () -> ArrayExtensions.getPreviousIndex(none, "a"),
				npe),
			new NullArrayCall("getNextIndexes", () -> ArrayExtensions.getNextIndexes(none, "a", 1),
				npe),
			new NullArrayCall("getPreviousIndexes",
				() -> ArrayExtensions.getPreviousIndexes(none, "a", 1), npe),
			new NullArrayCall("hasNext", () -> ArrayExtensions.hasNext(none, "a"), iae),
			new NullArrayCall("hasPrevious", () -> ArrayExtensions.hasPrevious(none, "a"), iae),
			new NullArrayCall("getNextElement", () -> ArrayExtensions.getNextElement(none, "a"),
				iae),
			new NullArrayCall("getPreviousElement",
				() -> ArrayExtensions.getPreviousElement(none, "a"), iae),
			new NullArrayCall("intersection, the first array",
				() -> ArrayExtensions.intersection(none, new String[0]), iae),
			new NullArrayCall("intersection, the other array",
				() -> ArrayExtensions.intersection(new String[0], none), iae),
			new NullArrayCall("getLastIndex", () -> ArrayExtensions.getLastIndex(none), "-1"),
			new NullArrayCall("isLast", () -> ArrayExtensions.isLast(none, "a"), "false"),
			new NullArrayCall("getFirst", () -> ArrayExtensions.getFirst(none), "null"),
			new NullArrayCall("getLast", () -> ArrayExtensions.getLast(none), "null"),
			new NullArrayCall("getFirstElement", () -> ArrayExtensions.getFirstElement(none),
				"Optional.empty"),
			new NullArrayCall("getLastElement", () -> ArrayExtensions.getLastElement(none),
				"Optional.empty"),
			new NullArrayCall("arraycopyWithSystem",
				() -> ArrayExtensions.arraycopyWithSystem(none, new String[1]), "null"));
	}

	/**
	 * Test for the methods of {@link ArrayExtensions} with a null array: the exception, or what
	 * they return (#144)
	 *
	 * @param nullArrayCall
	 *            the call and its outcome
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("nullArrayCalls")
	public void testNullArray(final NullArrayCall nullArrayCall)
	{
		String actual;
		try
		{
			actual = String.valueOf(nullArrayCall.call().get());
		}
		catch (final RuntimeException e)
		{
			actual = e.getClass().getSimpleName();
		}
		assertEquals(nullArrayCall.outcome(), actual);
	}

	/**
	 * An element of an array and its neighbours, as the methods that look for the next and the
	 * previous element find them: {@link ArrayExtensions#getNextIndex(Object[], Object)} and
	 * {@link ArrayExtensions#getPreviousIndex(Object[], Object)} wrap around at the ends, the other
	 * methods do not
	 *
	 * @param description
	 *            the name of the case
	 * @param array
	 *            the array
	 * @param element
	 *            the element, may be null
	 * @param nextIndex
	 *            the answer of getNextIndex
	 * @param previousIndex
	 *            the answer of getPreviousIndex
	 * @param next
	 *            the next element, null if there is none
	 * @param previous
	 *            the previous element, null if there is none
	 */
	record Neighbours(String description, String[] array, String element, int nextIndex,
		int previousIndex, String next, String previous) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testNeighbours(Neighbours)}, in {@link #names()}
	 *
	 * @return the cases
	 */
	static Stream<Neighbours> neighbours()
	{
		return Stream.of(
			new Neighbours("Caesar, the first: next Dora, no previous, previous index wraps to 3",
				names(), "Caesar", 1, 3, "Dora", null),
			new Neighbours("Dora: next Emil, previous Caesar", names(), "Dora", 2, 0, "Emil",
				"Caesar"),
			new Neighbours("Emil: next Anton, previous Dora", names(), "Emil", 3, 1, "Anton",
				"Dora"),
			new Neighbours("Anton, the last: no next, next index wraps to 0, previous Emil",
				names(), "Anton", 0, 2, null, "Emil"),
			new Neighbours("foo, not in the array: none, -1", names(), "foo", -1, -1, null, null),
			new Neighbours("null, not in the array: none, -1", names(), null, -1, -1, null, null),
			new Neighbours("x alone: none, both indexes wrap to 0", new String[] { "x" }, "x", 0, 0,
				null, null),
			new Neighbours("Anton in an empty array: none, -1", new String[0], "Anton", -1, -1,
				null, null),
			new Neighbours("null in an empty array: none, -1", new String[0], null, -1, -1, null,
				null));
	}

	/**
	 * Test for the methods {@link ArrayExtensions#getNextIndex(Object[], Object)},
	 * {@link ArrayExtensions#getPreviousIndex(Object[], Object)},
	 * {@link ArrayExtensions#hasNext(Object[], Object)},
	 * {@link ArrayExtensions#hasPrevious(Object[], Object)},
	 * {@link ArrayExtensions#getNextElement(Object[], Object)} and
	 * {@link ArrayExtensions#getPreviousElement(Object[], Object)}
	 *
	 * @param neighbours
	 *            the array, the element and its neighbours
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("neighbours")
	public void testNeighbours(final Neighbours neighbours)
	{
		final String[] array = neighbours.array();
		final String element = neighbours.element();
		assertEquals(neighbours.nextIndex(), ArrayExtensions.getNextIndex(array, element),
			"getNextIndex");
		assertEquals(neighbours.previousIndex(), ArrayExtensions.getPreviousIndex(array, element),
			"getPreviousIndex");
		assertEquals(neighbours.next() != null, ArrayExtensions.hasNext(array, element), "hasNext");
		assertEquals(neighbours.previous() != null, ArrayExtensions.hasPrevious(array, element),
			"hasPrevious");
		assertEquals(Optional.ofNullable(neighbours.next()),
			ArrayExtensions.getNextElement(array, element), "getNextElement");
		assertEquals(Optional.ofNullable(neighbours.previous()),
			ArrayExtensions.getPreviousElement(array, element), "getPreviousElement");
	}

	/**
	 * Test for the methods {@link ArrayExtensions#getNextIndexes(Object[], Object, int)} and
	 * {@link ArrayExtensions#getPreviousIndexes(Object[], Object, int)} on 1 to 7, which go on from
	 * index to index as {@link ArrayExtensions#getNextIndex(Object[], Object)} and
	 * {@link ArrayExtensions#getPreviousIndex(Object[], Object)} do, around the ends
	 *
	 * @param description
	 *            the name of the case
	 * @param array
	 *            the array, its elements separated by a space, '' for an empty one
	 * @param element
	 *            the element, may be null
	 * @param count
	 *            the count of indexes
	 * @param next
	 *            the next indexes, separated by a space, '' for none, null if there are none
	 * @param previous
	 *            the previous indexes, the same way
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"from 7, the last, 2 indexes: next 0 1, previous 5 4 | 1 2 3 4 5 6 7 | 7 | 2 | 0 1 | 5 4",
			"from 1, the first, 2 indexes: next 1 2, previous 6 5 | 1 2 3 4 5 6 7 | 1 | 2 | 1 2 | 6 5",
			"from 7, 9 indexes, more than the length: around the ends again | 1 2 3 4 5 6 7 | 7 | 9 | 0 1 2 3 4 5 6 0 1 | 5 4 3 2 1 0 6 5 4",
			"from 7, 0 indexes: none | 1 2 3 4 5 6 7 | 7 | 0 | '' | ''",
			"from 8, not in the array: null | 1 2 3 4 5 6 7 | 8 | 2 | | ",
			"from null: null | 1 2 3 4 5 6 7 | | 2 | | ",
			"from 7 in an empty array: null | '' | 7 | 2 | | ",
			"from null in an empty array: null | '' | | 2 | | " }, delimiter = '|')
	public void testGetNextAndPreviousIndexes(final String description, final String array,
		final String element, final int count, final String next, final String previous)
	{
		final String[] elements = elements(array);
		assertArrayEquals(indexes(next), ArrayExtensions.getNextIndexes(elements, element, count),
			"getNextIndexes");
		assertArrayEquals(indexes(previous),
			ArrayExtensions.getPreviousIndexes(elements, element, count), "getPreviousIndexes");
	}

	/**
	 * The indexes of a row of {@link #testGetNextAndPreviousIndexes}
	 *
	 * @param indexes
	 *            the indexes, separated by a space, may be empty or null
	 * @return the indexes, null for null
	 */
	private static int[] indexes(final String indexes)
	{
		if (indexes == null)
		{
			return null;
		}
		return indexes.isEmpty()
			? new int[0]
			: Arrays.stream(indexes.split(" ")).mapToInt(Integer::parseInt).toArray();
	}

	/**
	 * Test for method {@link ArrayExtensions#intersection(Object[], Object[])}: the elements of the
	 * first array that are in the other, in the order of the first, in an array of its component
	 * type
	 *
	 * @param description
	 *            the name of the case
	 * @param one
	 *            the first array, its elements separated by a space
	 * @param other
	 *            the other array, the same way, '' for an empty one
	 * @param expected
	 *            the intersection, the same way
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"all but Anton in common | Anton Alex Berta Brad Caesar Leonardo | Alex Berta Brad Caesar Leonardo | Alex Berta Brad Caesar Leonardo",
			"in the order of the first array | c b a | a b | b a",
			"nothing in common: empty | a b | c | ''",
			"an empty other array: empty | a b | '' | ''",
			"an empty first array: empty | '' | a b | ''" }, delimiter = '|')
	public void testIntersection(final String description, final String one, final String other,
		final String expected)
	{
		final String[] actual = ArrayExtensions.intersection(elements(one), elements(other));
		assertEquals(String[].class, actual.getClass());
		assertArrayEquals(elements(expected), actual);
	}

	/**
	 * The elements of an array in a row of a {@link CsvSource}
	 *
	 * @param elements
	 *            the elements, separated by a space, may be empty
	 * @return the array of the elements
	 */
	private static String[] elements(final String elements)
	{
		return elements.isEmpty() ? new String[0] : elements.split(" ");
	}

	/**
	 * A call of one of the methods that remove elements from an array and give a new one, and what
	 * it gives
	 *
	 * @param description
	 *            the name of the case
	 * @param array
	 *            makes the given array
	 * @param removal
	 *            the call with the given array
	 * @param expected
	 *            the new array, of the component type of the given array
	 */
	record Removal(String description, Supplier<Object[]> array, UnaryOperator<Object[]> removal,
		Object[] expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * A case of {@link ArrayExtensions#removeFromStart(Object[], Object[])} or
	 * {@link ArrayExtensions#removeFromEnd(Object[], Object[])}, which also checks that the prefix
	 * or the suffix is not changed
	 *
	 * @param description
	 *            the name of the case
	 * @param array
	 *            makes the given array
	 * @param affix
	 *            makes the prefix or suffix
	 * @param method
	 *            removeFromStart or removeFromEnd
	 * @param expected
	 *            the new array
	 * @return the case
	 */
	static Removal affix(final String description, final Supplier<Object[]> array,
		final Supplier<Object[]> affix, final BinaryOperator<Object[]> method,
		final Object[] expected)
	{
		return new Removal(description, array, given -> {
			final Object[] givenAffix = affix.get();
			final Object[] result = method.apply(given, givenAffix);
			assertArrayEquals(affix.get(), givenAffix, "the prefix or suffix is changed");
			return result;
		}, expected);
	}

	/**
	 * The cases of {@link ArrayExtensions#removeFromStart(Object[], Object[])} and
	 * {@link ArrayExtensions#removeFromEnd(Object[], Object[])} (#64, #76), of
	 * {@link ArrayExtensions#removeAll(Object[], Object[])}, of
	 * {@link ArrayExtensions#removeFirst(Object[])} and
	 * {@link ArrayExtensions#removeLast(Object[])}, and of
	 * {@link ArrayExtensions#remove(Object[], int...)} (#63)
	 *
	 * @return the cases
	 */
	static Stream<Removal> removals()
	{
		final BinaryOperator<Object[]> start = ArrayExtensions::removeFromStart;
		final BinaryOperator<Object[]> end = ArrayExtensions::removeFromEnd;
		return Stream.of(
			affix("removeFromStart, 1 to 9 without 1, 2, 3: 4 to 9",
				() -> new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }, () -> new Integer[] { 1, 2, 3 },
				start, new Integer[] { 4, 5, 6, 7, 8, 9 }),
			affix("removeFromEnd, 1 to 9 without 7, 8, 9: 1 to 6",
				() -> new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }, () -> new Integer[] { 7, 8, 9 },
				end, new Integer[] { 1, 2, 3, 4, 5, 6 }),
			affix("removeFromStart, the Number[] 1, 2.5, 3 without 1: the Number[] 2.5, 3 (#64)",
				() -> new Number[] { 1, 2.5, 3 }, () -> new Number[] { 1 }, start,
				new Number[] { 2.5, 3 }),
			affix("removeFromEnd, the Number[] 1, 2.5, 3 without 3: the Number[] 1, 2.5",
				() -> new Number[] { 1, 2.5, 3 }, () -> new Number[] { 3 }, end,
				new Number[] { 1, 2.5 }),
			affix("removeFromStart, null, a, b without null: a, b (#64)",
				() -> new String[] { null, "a", "b" }, () -> new String[] { null }, start,
				new String[] { "a", "b" }),
			affix("removeFromStart, the Object[] a, b, c without a: the Object[] b, c (#64)",
				() -> new Object[] { "a", "b", "c" }, () -> new Object[] { "a" }, start,
				new Object[] { "b", "c" }),
			affix("removeFromStart, 1, 2, 3 without 9, no prefix: a copy (#76)",
				() -> new Integer[] { 1, 2, 3 }, () -> new Integer[] { 9 }, start,
				new Integer[] { 1, 2, 3 }),
			affix("removeFromEnd, 1, 2, 3 without 9, no suffix: a copy (#76)",
				() -> new Integer[] { 1, 2, 3 }, () -> new Integer[] { 9 }, end,
				new Integer[] { 1, 2, 3 }),
			affix("removeFromStart, 1, 2, 3 without 1, 9, a prefix in part only: a copy (#76)",
				() -> new Integer[] { 1, 2, 3 }, () -> new Integer[] { 1, 9 }, start,
				new Integer[] { 1, 2, 3 }),
			affix("removeFromEnd, 1, 2, 3 without 9, 3, a suffix in part only: a copy (#76)",
				() -> new Integer[] { 1, 2, 3 }, () -> new Integer[] { 9, 3 }, end,
				new Integer[] { 1, 2, 3 }),
			affix("removeFromStart, 1 without the longer 1, 2: a copy (#76)",
				() -> new Integer[] { 1 }, () -> new Integer[] { 1, 2 }, start,
				new Integer[] { 1 }),
			affix("removeFromEnd, 1 without the longer 1, 2: a copy (#76)",
				() -> new Integer[] { 1 }, () -> new Integer[] { 1, 2 }, end, new Integer[] { 1 }),
			affix("removeFromStart, an empty array without 1, 2, 3: a new empty array (#76)",
				() -> new Integer[0], () -> new Integer[] { 1, 2, 3 }, start, new Integer[0]),
			affix("removeFromStart, an empty array without 1: a new empty array (#76)",
				() -> new Integer[0], () -> new Integer[] { 1 }, start, new Integer[0]),
			affix("removeFromEnd, an empty array without 1: a new empty array",
				() -> new Integer[0], () -> new Integer[] { 1 }, end, new Integer[0]),
			affix("removeFromStart, 1, 2 without an empty prefix: a copy",
				() -> new Integer[] { 1, 2 }, () -> new Integer[0], start, new Integer[] { 1, 2 }),
			affix("removeFromEnd, 1, 2 without an empty suffix: a copy",
				() -> new Integer[] { 1, 2 }, () -> new Integer[0], end, new Integer[] { 1, 2 }),
			affix("removeFromStart, 1, 2 without the whole 1, 2: empty",
				() -> new Integer[] { 1, 2 }, () -> new Integer[] { 1, 2 }, start, new Integer[0]),
			affix("removeFromEnd, 1, 2 without the whole 1, 2: empty", () -> new Integer[] { 1, 2 },
				() -> new Integer[] { 1, 2 }, end, new Integer[0]),
			affix("removeFromEnd, a, null without null: a", () -> new String[] { "a", null },
				() -> new String[] { null }, end, new String[] { "a" }),
			affix("removeFromStart, null, a without null: a", () -> new String[] { null, "a" },
				() -> new String[] { null }, start, new String[] { "a" }),
			new Removal("removeAll, 1, 2, 3, 4 without 2, 3, 4: 1",
				() -> new String[] { "1", "2", "3", "4" },
				array -> ArrayExtensions.removeAll(array, new String[] { "2", "3", "4" }),
				new String[] { "1" }),
			new Removal("removeAll, 1, 2, 2, 3 without 2: 1, 3, every 2",
				() -> new String[] { "1", "2", "2", "3" },
				array -> ArrayExtensions.removeAll(array, new String[] { "2" }),
				new String[] { "1", "3" }),
			new Removal("removeAll, 1, 2 without 9, not in it: a copy",
				() -> new String[] { "1", "2" },
				array -> ArrayExtensions.removeAll(array, new String[] { "9" }),
				new String[] { "1", "2" }),
			new Removal("removeAll, the Number[] 1, 2.5 without nothing: a Number[] copy",
				() -> new Number[] { 1, 2.5 },
				array -> ArrayExtensions.removeAll(array, new Number[0]), new Number[] { 1, 2.5 }),
			new Removal("removeFirst, 1, 2, 3, 4: 2, 3, 4",
				() -> new String[] { "1", "2", "3", "4" }, ArrayExtensions::removeFirst,
				new String[] { "2", "3", "4" }),
			new Removal("removeFirst, a alone: empty", () -> new String[] { "a" },
				ArrayExtensions::removeFirst, new String[0]),
			new Removal("removeFirst, the Number[] 1, 2.5: the Number[] 2.5",
				() -> new Number[] { 1, 2.5 }, ArrayExtensions::removeFirst, new Number[] { 2.5 }),
			new Removal("removeLast, 1, 2, 3, 4: 1, 2, 3",
				() -> new String[] { "1", "2", "3", "4" }, ArrayExtensions::removeLast,
				new String[] { "1", "2", "3" }),
			new Removal("removeLast, a alone: empty", () -> new String[] { "a" },
				ArrayExtensions::removeLast, new String[0]),
			removal("remove, 1, 2, 3, 4 without 2, 3: 1, 2",
				() -> new String[] { "1", "2", "3", "4" }, new int[] { 2, 3 },
				new String[] { "1", "2" }),
			removal("remove, 1 to 6 without 5, 3, 1, unsorted: 1, 3, 5",
				() -> new String[] { "1", "2", "3", "4", "5", "6" }, new int[] { 5, 3, 1 },
				new String[] { "1", "3", "5" }),
			removal("remove, 1 to 6 without 3, 5, 1, unsorted: 1, 3, 5",
				() -> new String[] { "1", "2", "3", "4", "5", "6" }, new int[] { 3, 5, 1 },
				new String[] { "1", "3", "5" }),
			removal("remove, 0, 1, 2, 3 without 2, 0: 1, 3, the indexes stay unsorted (#63)",
				() -> new Integer[] { 0, 1, 2, 3 }, new int[] { 2, 0 }, new Integer[] { 1, 3 }),
			removal("remove, 0, 1, 2, 3 without 1 twice: 0, 2, 3, the element at 1 once (#63)",
				() -> new Integer[] { 0, 1, 2, 3 }, new int[] { 1, 1 }, new Integer[] { 0, 2, 3 }),
			removal("remove, 0, 1, 2, 3 without the last index 3 twice: 0, 1, 2 (#63)",
				() -> new Integer[] { 0, 1, 2, 3 }, new int[] { 3, 3 }, new Integer[] { 0, 1, 2 }),
			removal("remove, the Number[] 1, 2.5 without any index: a Number[] copy",
				() -> new Number[] { 1, 2.5 }, new int[0], new Number[] { 1, 2.5 }));
	}

	/**
	 * A case of {@link ArrayExtensions#remove(Object[], int...)}, which also checks that the
	 * indexes array of the caller is not changed (#63)
	 *
	 * @param description
	 *            the name of the case
	 * @param array
	 *            makes the given array
	 * @param indexes
	 *            the indexes to remove
	 * @param expected
	 *            the new array
	 * @return the case
	 */
	static Removal removal(final String description, final Supplier<Object[]> array,
		final int[] indexes, final Object[] expected)
	{
		return new Removal(description, array, given -> {
			final int[] givenIndexes = indexes.clone();
			final Object[] result = ArrayExtensions.remove(given, givenIndexes);
			assertArrayEquals(indexes, givenIndexes, "the indexes are changed");
			return result;
		}, expected);
	}

	/**
	 * Test for the methods {@link ArrayExtensions#removeFromStart(Object[], Object[])},
	 * {@link ArrayExtensions#removeFromEnd(Object[], Object[])},
	 * {@link ArrayExtensions#removeAll(Object[], Object[])},
	 * {@link ArrayExtensions#removeFirst(Object[])}, {@link ArrayExtensions#removeLast(Object[])}
	 * and {@link ArrayExtensions#remove(Object[], int...)}: the result is a new array of the
	 * component type of the given array, and the given array is not changed
	 *
	 * @param removal
	 *            the call, the given array and the new one
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("removals")
	public void testRemove(final Removal removal)
	{
		final Object[] array = removal.array().get();
		final Object[] actual = removal.removal().apply(array);
		assertArrayEquals(removal.expected(), actual);
		assertEquals(array.getClass(), actual.getClass(), "the component type");
		assertNotSame(array, actual);
		assertArrayEquals(removal.array().get(), array, "the given array is changed");
	}

	/**
	 * A call of one of the methods that remove elements from an array, which it refuses, and the
	 * exception it throws
	 *
	 * @param description
	 *            the name of the case
	 * @param array
	 *            makes the given array, may make null
	 * @param removal
	 *            the call with the given array
	 * @param exception
	 *            the class of the exception
	 * @param message
	 *            the message of the exception, null if the case does not check it
	 */
	record RefusedRemoval(String description, Supplier<Object[]> array,
		UnaryOperator<Object[]> removal, Class<? extends RuntimeException> exception,
		String message) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testRemoveRefused(RefusedRemoval)}
	 *
	 * @return the cases
	 */
	static Stream<RefusedRemoval> refusedRemovals()
	{
		final Supplier<Object[]> zeroToThree = () -> new Integer[] { 0, 1, 2, 3 };
		return Stream.of(
			new RefusedRemoval("removeFirst, an empty array: Index 0 out of bounds (#63)",
				() -> new String[0], ArrayExtensions::removeFirst, IndexOutOfBoundsException.class,
				"Index 0 out of bounds for length 0"),
			new RefusedRemoval("removeLast, an empty array: Index -1 out of bounds (#63)",
				() -> new String[0], ArrayExtensions::removeLast, IndexOutOfBoundsException.class,
				"Index -1 out of bounds for length 0"),
			new RefusedRemoval("remove, 0, 1, 2, 3 without 1 and 4: Index 4 out of bounds",
				zeroToThree, array -> ArrayExtensions.remove(array, 1, 4),
				IndexOutOfBoundsException.class, "Index 4 out of bounds for length 4"),
			new RefusedRemoval("remove, 0, 1, 2, 3 without -1: Index -1 out of bounds", zeroToThree,
				array -> ArrayExtensions.remove(array, -1), IndexOutOfBoundsException.class,
				"Index -1 out of bounds for length 4"),
			new RefusedRemoval(
				"remove, 0, 1, 2, 3 without -1 and 2: the length of the given array, 4 (#76)",
				zeroToThree, array -> ArrayExtensions.remove(array, -1, 2),
				IndexOutOfBoundsException.class, "Index -1 out of bounds for length 4"),
			new RefusedRemoval("removeFromEnd, a null array: IllegalArgumentException (#76)",
				() -> null, array -> ArrayExtensions.removeFromEnd(array, new Integer[] { 1 }),
				IllegalArgumentException.class, null),
			new RefusedRemoval("removeFromEnd, a null suffix: IllegalArgumentException (#76)",
				() -> new Integer[] { 1 }, array -> ArrayExtensions.removeFromEnd(array, null),
				IllegalArgumentException.class, null),
			new RefusedRemoval("removeFromStart, a null array: IllegalArgumentException",
				() -> null, array -> ArrayExtensions.removeFromStart(array, new Integer[] { 1 }),
				IllegalArgumentException.class, null),
			new RefusedRemoval("removeFromStart, a null prefix: IllegalArgumentException",
				() -> new Integer[] { 1 }, array -> ArrayExtensions.removeFromStart(array, null),
				IllegalArgumentException.class, null));
	}

	/**
	 * Test for the methods {@link ArrayExtensions#removeFirst(Object[])},
	 * {@link ArrayExtensions#removeLast(Object[])},
	 * {@link ArrayExtensions#remove(Object[], int...)},
	 * {@link ArrayExtensions#removeFromStart(Object[], Object[])} and
	 * {@link ArrayExtensions#removeFromEnd(Object[], Object[])} with an argument they refuse: the
	 * exception, its message, and the given array is not changed
	 *
	 * @param removal
	 *            the call and the exception
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("refusedRemovals")
	public void testRemoveRefused(final RefusedRemoval removal)
	{
		final Object[] array = removal.array().get();
		final RuntimeException exception = assertThrows(removal.exception(),
			() -> removal.removal().apply(array));
		if (removal.message() != null)
		{
			assertEquals(removal.message(), exception.getMessage());
		}
		assertArrayEquals(removal.array().get(), array, "the given array is changed");
	}

	/**
	 * Test for method {@link ArrayExtensions#splitInChunks(byte[], int)}: chunks of the chunk size
	 * in the order of the bytes, the last one with the rest
	 *
	 * @param description
	 *            the name of the case
	 * @param text
	 *            the text whose bytes are split
	 * @param chunkSize
	 *            the chunk size
	 * @param chunks
	 *            the chunks, as texts separated by a '|'
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"56 bytes in chunks of 5: 12, the last of 1 byte; Lorem ipsum dolor sit amet, consectetur adipiscing elit.; 5; Lorem| ipsu|m dol|or si|t ame|t, co|nsect|etur |adipi|scing| elit|.",
			"6 bytes in chunks of 3, a multiple: 2 full ones; abcdef; 3; abc|def",
			"5 bytes in chunks of 2: 3, the last of 1 byte; abcde; 2; ab|cd|e",
			"5 bytes in chunks of 9, more than the length: 1; abcde; 9; abcde",
			"2 bytes in chunks of 1: 2; ab; 1; a|b" }, delimiter = ';')
	public void testSplitInChunks(final String description, final String text, final int chunkSize,
		final String chunks)
	{
		final byte[][] expected = Arrays.stream(chunks.split("\\|", -1))
			.map(chunk -> chunk.getBytes(StandardCharsets.US_ASCII)).toArray(byte[][]::new);
		assertArrayEquals(expected,
			ArrayExtensions.splitInChunks(text.getBytes(StandardCharsets.US_ASCII), chunkSize));
	}

	/**
	 * Test method for {@link ArrayExtensions} with {@link BeanTester}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ArrayExtensions.class);
	}

}
