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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

/**
 * The unit test class for the class {@link ArrayFactory}.
 */
public class ArrayFactoryTest
{

	/**
	 * A call of one of the factory methods that copy the given values into a new array, and what it
	 * returns
	 *
	 * @param description
	 *            the name of the case
	 * @param given
	 *            makes the array the factory method is given, may make null
	 * @param factory
	 *            the factory method
	 * @param expected
	 *            the values of the returned array, or null if it returns null
	 */
	record CopyCase(String description, Supplier<Object> given, UnaryOperator<Object> factory,
		Object expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The three cases of a factory method that copies the given values: values, no values and null
	 *
	 * @param name
	 *            the name of the factory method
	 * @param factory
	 *            the factory method
	 * @param values
	 *            makes an array of values
	 * @param empty
	 *            makes an empty array of the same type
	 * @return the cases
	 */
	static Stream<CopyCase> copiesOf(final String name, final UnaryOperator<Object> factory,
		final Supplier<Object> values, final Supplier<Object> empty)
	{
		return Stream.of(
			new CopyCase(name + ", values: a new array with the same values", values, factory,
				values.get()),
			new CopyCase(name + ", no values: a new empty array", empty, factory, empty.get()),
			new CopyCase(name + ", null: null", () -> null, factory, null));
	}

	/**
	 * The cases of {@link ArrayFactory#newArray(Object...)} and of the factory methods of the
	 * primitive types, which copy the given values: a new array also when no values are given (#62,
	 * #75), and null for null
	 *
	 * @return the cases
	 */
	static Stream<CopyCase> copies()
	{
		return Stream.of(
			copiesOf("newArray(Object...)", given -> ArrayFactory.newArray((Integer[])given),
				() -> new Integer[] { 1, 2, 3 }, () -> new Integer[0]),
			copiesOf("newBooleanArray", given -> ArrayFactory.newBooleanArray((boolean[])given),
				() -> new boolean[] { true, true, false }, () -> new boolean[0]),
			copiesOf("newByteArray(byte...)", given -> ArrayFactory.newByteArray((byte[])given),
				() -> new byte[] { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 },
				() -> new byte[0]),
			copiesOf("newCharArray", given -> ArrayFactory.newCharArray((char[])given),
				() -> new char[] { 'f', 'o', 'o' }, () -> new char[0]),
			copiesOf("newDoubleArray", given -> ArrayFactory.newDoubleArray((double[])given),
				() -> new double[] { 1.1D, 2.1D, 3.1D }, () -> new double[0]),
			copiesOf("newFloatArray", given -> ArrayFactory.newFloatArray((float[])given),
				() -> new float[] { 1.0F, 2.0F, 3.0F }, () -> new float[0]),
			copiesOf("newIntArray", given -> ArrayFactory.newIntArray((int[])given),
				() -> new int[] { 1, 2, 3 }, () -> new int[0]),
			copiesOf("newLongArray", given -> ArrayFactory.newLongArray((long[])given),
				() -> new long[] { 1L, 2L, 3L }, () -> new long[0]),
			copiesOf("newShortArray", given -> ArrayFactory.newShortArray((short[])given),
				() -> new short[] { 1, 2, 3 }, () -> new short[0]))
			.flatMap(Function.identity());
	}

	/**
	 * Test for the methods {@link ArrayFactory#newArray(Object...)},
	 * {@link ArrayFactory#newBooleanArray(boolean...)}, {@link ArrayFactory#newByteArray(byte...)},
	 * {@link ArrayFactory#newCharArray(char...)}, {@link ArrayFactory#newDoubleArray(double...)},
	 * {@link ArrayFactory#newFloatArray(float...)}, {@link ArrayFactory#newIntArray(int...)},
	 * {@link ArrayFactory#newLongArray(long...)} and {@link ArrayFactory#newShortArray(short...)}:
	 * the result has the given values, and it is a new array, not the given one, so a write to it
	 * does not change the array of the caller. Before the fix of #62 and #75 they returned the
	 * given array itself
	 *
	 * @param copy
	 *            the factory method, what it is given and what it returns
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("copies")
	public void testNewArrayCopiesTheGivenValues(final CopyCase copy)
	{
		final Object given = copy.given().get();
		final Object actual = copy.factory().apply(given);
		assertTrue(Objects.deepEquals(copy.expected(), actual),
			() -> "expected " + Arrays.deepToString(new Object[] { copy.expected() }) + " but was "
				+ Arrays.deepToString(new Object[] { actual }));
		if (actual != null)
		{
			assertEquals(given.getClass(), actual.getClass());
			assertNotSame(given, actual);
		}
	}

	/**
	 * A call of {@link ArrayFactory#newByteArray(int...)} and the bytes it returns
	 *
	 * @param description
	 *            the name of the case
	 * @param given
	 *            the given int values, may be null
	 * @param expected
	 *            the bytes of the returned array
	 */
	record ByteArrayOfInts(String description, int[] given, byte[] expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link ArrayFactory#newByteArray(int...)}: unlike the other factory methods it
	 * returns an empty array for null
	 *
	 * @return the cases
	 */
	static Stream<ByteArrayOfInts> byteArraysOfInts()
	{
		return Stream.of(
			new ByteArrayOfInts("values: the same values as bytes",
				new int[] { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 },
				new byte[] { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 }),
			new ByteArrayOfInts("no values: an empty array", new int[0], new byte[0]),
			new ByteArrayOfInts("null: an empty array, not null", null, new byte[0]));
	}

	/**
	 * Test for method {@link ArrayFactory#newByteArray(int...)}
	 *
	 * @param byteArray
	 *            the given int values and the returned bytes
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("byteArraysOfInts")
	public void testNewByteArrayOfInts(final ByteArrayOfInts byteArray)
	{
		assertArrayEquals(byteArray.expected(), ArrayFactory.newByteArray(byteArray.given()));
	}

	/**
	 * A call of {@link ArrayFactory#newArray(Object[], Object...)} and what it returns
	 *
	 * @param description
	 *            the name of the case
	 * @param existingArray
	 *            the existing array, may be null
	 * @param elements
	 *            the elements, may be null
	 * @param expected
	 *            the returned array, or null if it returns null
	 */
	record ExistingArrayCase(String description, Integer[] existingArray, Integer[] elements,
		Integer[] expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link ArrayFactory#newArray(Object[], Object...)}: the existing array followed
	 * by the elements, a copy of one of them if the other is null or empty (#62), and null if there
	 * is neither, as before the fix of #62
	 *
	 * @return the cases
	 */
	static Stream<ExistingArrayCase> existingArrays()
	{
		return Stream.of(
			new ExistingArrayCase("1, 2, 3 followed by 4, 5, 6: 1 to 6", new Integer[] { 1, 2, 3 },
				new Integer[] { 4, 5, 6 }, new Integer[] { 1, 2, 3, 4, 5, 6 }),
			new ExistingArrayCase(
				"an empty existing array followed by 1, 2, 3: a new array of 1, 2, 3",
				new Integer[0], new Integer[] { 1, 2, 3 }, new Integer[] { 1, 2, 3 }),
			new ExistingArrayCase("no existing array followed by 1, 2, 3: a new array of 1, 2, 3",
				null, new Integer[] { 1, 2, 3 }, new Integer[] { 1, 2, 3 }),
			new ExistingArrayCase("1, 2 followed by no elements: a new array of 1, 2",
				new Integer[] { 1, 2 }, new Integer[0], new Integer[] { 1, 2 }),
			new ExistingArrayCase("1, 2 followed by null: a new array of 1, 2",
				new Integer[] { 1, 2 }, null, new Integer[] { 1, 2 }),
			new ExistingArrayCase("no existing array and no elements: null", null, new Integer[0],
				null),
			new ExistingArrayCase("no existing array and null elements: null", null, null, null));
	}

	/**
	 * Test for method {@link ArrayFactory#newArray(Object[], Object...)}: the result is never one
	 * of the given arrays itself. Before the fix of #62 the method returned the existing array if
	 * no elements were given, and the elements array if no existing array was given
	 *
	 * @param existingArray
	 *            the given arrays and the returned one
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("existingArrays")
	public void testNewArrayWithExistingArray(final ExistingArrayCase existingArray)
	{
		final Integer[] actual = ArrayFactory.newArray(existingArray.existingArray(),
			existingArray.elements());
		assertArrayEquals(existingArray.expected(), actual);
		if (actual != null)
		{
			assertNotSame(existingArray.existingArray(), actual, "the existing array itself");
			assertNotSame(existingArray.elements(), actual, "the elements array itself");
		}
	}

	/**
	 * Test for method {@link ArrayFactory#newArray(Class, int)}: a new array of the given component
	 * type and length, which holds nulls only
	 *
	 * @param description
	 *            the name of the case
	 * @param componentType
	 *            the given class type
	 * @param length
	 *            the given length
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource({ "Integer of length 3: three nulls, java.lang.Integer, 3",
			"String of length 0: an empty array, java.lang.String, 0" })
	public void testNewArrayTypeWithLength(final String description, final Class<?> componentType,
		final int length)
	{
		final Object[] actual = ArrayFactory.newArray(componentType, length);
		assertEquals(componentType, actual.getClass().getComponentType());
		assertEquals(length, actual.length);
		assertTrue(Arrays.stream(actual).allMatch(Objects::isNull));
	}

	/**
	 * A call of {@link ArrayFactory#newEmptyArray(Object[])} or
	 * {@link ArrayFactory#newEmptyArray(Object[], int)} and the array it returns
	 *
	 * @param description
	 *            the name of the case
	 * @param call
	 *            the call
	 * @param componentType
	 *            the component type of the returned array
	 * @param length
	 *            the length of the returned array
	 */
	record EmptyArrayCase(String description, Supplier<Object[]> call, Class<?> componentType,
		int length) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link ArrayFactory#newEmptyArray(Object[])}, which takes the length of the
	 * given array, and of {@link ArrayFactory#newEmptyArray(Object[], int)}, which takes the given
	 * length
	 *
	 * @return the cases
	 */
	static Stream<EmptyArrayCase> emptyArrays()
	{
		return Stream.of(
			new EmptyArrayCase("newEmptyArray of 1, 2, 3: three nulls",
				() -> ArrayFactory.newEmptyArray(new Integer[] { 1, 2, 3 }), Integer.class, 3),
			new EmptyArrayCase("newEmptyArray of an empty String array: an empty String array",
				() -> ArrayFactory.newEmptyArray(new String[0]), String.class, 0),
			new EmptyArrayCase("newEmptyArray of 1, 2, 3 with length 5: five nulls",
				() -> ArrayFactory.newEmptyArray(new Integer[] { 1, 2, 3 }, 5), Integer.class, 5),
			new EmptyArrayCase("newEmptyArray of 1, 2, 3 with length 0: an empty Integer array",
				() -> ArrayFactory.newEmptyArray(new Integer[] { 1, 2, 3 }, 0), Integer.class, 0));
	}

	/**
	 * Test for the methods {@link ArrayFactory#newEmptyArray(Object[])} and
	 * {@link ArrayFactory#newEmptyArray(Object[], int)}: a new array of the component type of the
	 * given array, which holds nulls only
	 *
	 * @param emptyArray
	 *            the call and the returned array
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("emptyArrays")
	public void testNewEmptyArray(final EmptyArrayCase emptyArray)
	{
		final Object[] actual = emptyArray.call().get();
		assertEquals(emptyArray.componentType(), actual.getClass().getComponentType());
		assertEquals(emptyArray.length(), actual.length);
		assertTrue(Arrays.stream(actual).allMatch(Objects::isNull));
	}

	/**
	 * A range of {@link ArrayFactory#newRangeArray(int, int)} and the numbers it holds
	 *
	 * @param description
	 *            the name of the case
	 * @param start
	 *            the start, inclusive
	 * @param end
	 *            the end, inclusive
	 * @param expected
	 *            the numbers of the range
	 */
	record RangeCase(String description, int start, int end, Integer[] expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link ArrayFactory#newRangeArray(int, int)}, both ends inclusive
	 *
	 * @return the cases
	 */
	static Stream<RangeCase> ranges()
	{
		return Stream.of(
			new RangeCase("5 to 9: 5, 6, 7, 8, 9", 5, 9, new Integer[] { 5, 6, 7, 8, 9 }),
			new RangeCase("1 to 49: the 49 numbers", 1, 49,
				IntStream.rangeClosed(1, 49).boxed().toArray(Integer[]::new)),
			new RangeCase("5 to 5, start equal to end: 5 alone", 5, 5, new Integer[] { 5 }),
			new RangeCase("-2 to 1, across zero: -2, -1, 0, 1", -2, 1,
				new Integer[] { -2, -1, 0, 1 }),
			new RangeCase("Integer.MIN_VALUE to Integer.MIN_VALUE + 1: both", Integer.MIN_VALUE,
				Integer.MIN_VALUE + 1, new Integer[] { Integer.MIN_VALUE, Integer.MIN_VALUE + 1 }));
	}

	/**
	 * Test for method {@link ArrayFactory#newRangeArray(int, int)}
	 *
	 * @param range
	 *            the range and its numbers
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("ranges")
	public void testNewRangeArray(final RangeCase range)
	{
		assertArrayEquals(range.expected(), ArrayFactory.newRangeArray(range.start(), range.end()));
	}

	/**
	 * Test for method {@link ArrayFactory#newRangeArray(int, int)} with a range it throws for, and
	 * the exception with its message: an end less than the start. At the end of the int values it
	 * throws today, as its counter and its length overflow (#113): from Integer.MAX_VALUE - 1 and
	 * from Integer.MAX_VALUE to Integer.MAX_VALUE the counter passes Integer.MAX_VALUE and writes
	 * beyond the array, and a range of more than Integer.MAX_VALUE numbers gets a length of 0 or a
	 * negative one. The fix of #113 changes these four rows
	 *
	 * @param description
	 *            the name of the case
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param exception
	 *            the class of the exception
	 * @param message
	 *            the message of the exception
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource({
			"9 to 8: the end one less than the start, 9, 8, java.lang.IllegalArgumentException, Parameter end must not be less than parameter start.",
			"9 to -9: the end far less than the start, 9, -9, java.lang.IllegalArgumentException, Parameter end must not be less than parameter start.",
			"Integer.MAX_VALUE - 1 to Integer.MAX_VALUE: today the counter overflows (#113), 2147483646, 2147483647, java.lang.ArrayIndexOutOfBoundsException, Index 2 out of bounds for length 2",
			"Integer.MAX_VALUE to Integer.MAX_VALUE: today the counter overflows (#113), 2147483647, 2147483647, java.lang.ArrayIndexOutOfBoundsException, Index 1 out of bounds for length 1",
			"Integer.MIN_VALUE to Integer.MAX_VALUE: today the length overflows to 0 (#113), -2147483648, 2147483647, java.lang.ArrayIndexOutOfBoundsException, Index 0 out of bounds for length 0",
			"0 to Integer.MAX_VALUE: today the length overflows to a negative (#113), 0, 2147483647, java.lang.NegativeArraySizeException, -2147483648" })
	public void testNewRangeArrayThrows(final String description, final int start, final int end,
		final Class<? extends RuntimeException> exception, final String message)
	{
		final RuntimeException actual = assertThrows(exception,
			() -> ArrayFactory.newRangeArray(start, end));
		assertEquals(message, actual.getMessage());
	}

	/**
	 * Test for method {@link ArrayFactory#newSubArray(Object[], int, int)}: the elements from the
	 * start index, inclusive, to the end index, exclusive, of 1 to 7, in a new array of the
	 * component type of the source
	 *
	 * @param description
	 *            the name of the case
	 * @param startIndex
	 *            the start index, inclusive
	 * @param endIndex
	 *            the end index, exclusive
	 * @param expected
	 *            the elements of the sub array, separated by a space
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "2 to 5: 3, 4, 5 | 2 | 5 | 3 4 5", "0 to 1: the first | 0 | 1 | 1",
			"6 to 7: the last | 6 | 7 | 7", "0 to 7: all of them | 0 | 7 | 1 2 3 4 5 6 7",
			"2 to 2: none | 2 | 2 | ''" }, delimiter = '|')
	public void testNewSubArray(final String description, final int startIndex, final int endIndex,
		final String expected)
	{
		final Integer[] source = { 1, 2, 3, 4, 5, 6, 7 };
		final Integer[] actual = ArrayFactory.newSubArray(source, startIndex, endIndex);
		assertEquals(Integer[].class, actual.getClass());
		assertArrayEquals(
			expected.isEmpty()
				? new Integer[0]
				: Arrays.stream(expected.split(" ")).map(Integer::valueOf).toArray(Integer[]::new),
			actual);
		assertNotSame(source, actual);
	}

	/**
	 * Test method for {@link ArrayFactory} with {@link BeanTester}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ArrayFactory.class);
	}

}
