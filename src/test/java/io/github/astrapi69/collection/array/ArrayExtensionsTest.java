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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.check.Argument;
import io.github.astrapi69.collection.list.ListExtensions;
import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.collection.map.MapExtensions;
import io.github.astrapi69.collection.set.SetFactory;

/**
 * The unit test class for the class {@link ArrayExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class ArrayExtensionsTest
{

	/**
	 * Test joining two non-null arrays. Verifies that the result contains all elements of both
	 * input arrays.
	 */
	@Test
	void testJoinWithBothArraysNotNull()
	{
		String[] array1 = { "A", "B", "C" };
		String[] array2 = { "D", "E" };
		String[] result = ArrayExtensions.join(array1, array2);

		assertNotNull(result, "The resulting array should not be null.");
		assertArrayEquals(new String[] { "A", "B", "C", "D", "E" }, result,
			"The arrays should be joined correctly.");
	}

	/**
	 * Test joining when the first array is null. Verifies that the second array is returned.
	 */
	@Test
	void testJoinWithFirstArrayNull()
	{
		String[] array1 = null;
		String[] array2 = { "D", "E" };
		String[] result = ArrayExtensions.join(array1, array2);

		assertNotNull(result, "The resulting array should not be null.");
		assertArrayEquals(array2, result,
			"The result should be the second array when the first is null.");
	}

	/**
	 * Test joining when the second array is null. Verifies that the first array is returned.
	 */
	@Test
	void testJoinWithSecondArrayNull()
	{
		String[] array1 = { "A", "B", "C" };
		String[] array2 = null;
		String[] result = ArrayExtensions.join(array1, array2);

		assertNotNull(result, "The resulting array should not be null.");
		assertArrayEquals(array1, result,
			"The result should be the first array when the second is null.");
	}

	/**
	 * Test joining when both arrays are null. Verifies that the result is null.
	 */
	@Test
	void testJoinWithBothArraysNull()
	{
		String[] array1 = null;
		String[] array2 = null;
		String[] result = ArrayExtensions.join(array1, array2);

		assertNull(result, "The result should be null when both arrays are null.");
	}

	/**
	 * Test joining two arrays with incompatible types. Verifies that an
	 * {@link IllegalArgumentException} is thrown when trying to join arrays of incompatible types.
	 */
	@Test
	void testJoinWithIncompatibleTypes()
	{
		Integer[] array1 = { 1, 2, 3 };
		String[] array2 = { "A", "B" };

		assertThrows(IllegalArgumentException.class, () -> {
			ArrayExtensions.join(array1, array2);
		}, "An IllegalArgumentException should be thrown for incompatible types.");
	}

	/**
	 * Test for the Method {@link ArrayExtensions#toSet(Object[])}
	 */
	@Test
	public void testToSet()
	{
		Set<Integer> actual;
		Set<Integer> expected;
		Integer[] original = { 1, 2, 3, 4, 5 };
		actual = ArrayExtensions.toSet(original);
		expected = SetFactory.newLinkedHashSet(original);
		assertEquals(expected, actual);
	}

	/**
	 * Test for the Method {@link ArrayExtensions#subArray(Object[], int, int)}
	 */
	@Test
	public void testSubArrayWithValidRange()
	{
		Integer[] original = { 1, 2, 3, 4, 5 };
		Integer[] expected = { 2, 3, 4 };
		assertArrayEquals(expected, ArrayExtensions.subArray(original, 1, 4));
	}

	/**
	 * Test for the Method {@link ArrayExtensions#subArray(Object[], int, int)}
	 */
	@Test
	public void testSubArrayWithFullRange()
	{
		Integer[] original = { 1, 2, 3, 4, 5 };
		Integer[] expected = { 1, 2, 3, 4, 5 };
		assertArrayEquals(expected, ArrayExtensions.subArray(original, 0, 5));
	}

	/**
	 * Test for the Method {@link ArrayExtensions#subArray(Object[], int, int)}
	 */
	@Test
	public void testSubArrayWithSingleElement()
	{
		Integer[] original = { 1, 2, 3, 4, 5 };
		Integer[] expected = { 3 };
		assertArrayEquals(expected, ArrayExtensions.subArray(original, 2, 3));
	}

	/**
	 * Test for the Method {@link ArrayExtensions#subArray(Object[], int, int)}
	 */
	@Test
	public void testSubArrayWithEmptyRange()
	{
		Integer[] original = { 1, 2, 3, 4, 5 };
		Integer[] expected = { };
		assertArrayEquals(expected, ArrayExtensions.subArray(original, 2, 2));
	}

	/**
	 * Test for the Method {@link ArrayExtensions#subArray(Object[], int, int)}
	 */
	@Test
	public void testSubArrayWithInvalidRange()
	{
		Integer[] original = { 1, 2, 3, 4, 5 };
		assertThrows(ArrayIndexOutOfBoundsException.class,
			() -> ArrayExtensions.subArray(original, -1, 3));
	}

	/**
	 * Test for the Method {@link ArrayExtensions#subArray(Object[], int, int)}
	 */
	@Test
	public void testSubArrayWithStartGreaterThanEnd()
	{
		Integer[] original = { 1, 2, 3, 4, 5 };
		assertThrows(IllegalArgumentException.class,
			() -> ArrayExtensions.subArray(original, 4, 3));
	}

	/**
	 * Converts the given two-dimensional array to a {@link Map} object
	 *
	 * @param <T>
	 *            the generic type
	 * @param twoDimArray
	 *            The two-dimensional array
	 * @return The {@link Map} object produced from the given two-dimensional array
	 */
	public static <T> Map<String, Object> toStringObjectMap(final Object[][] twoDimArray)
	{
		Argument.notNull(twoDimArray, "twoDimArray");
		final Map<String, Object> map = new LinkedHashMap<>();
		for (final Object[] element : twoDimArray)
		{
			final String key = (String)element[0];
			final Object value = element[1];
			map.put(key, value);
		}
		return map;
	}

	/**
	 * Test for the Method {@link ArrayExtensions#toTwoDimensionalArray(Map, Class)}
	 */
	@Test
	public void testToTwoDimensionalArray()
	{
		final String[][] twoDimArray = { { "1", "value1" }, { "3", "value3" }, { "4", "value4" },
				{ "2", "value2" } };
		final Map<String, String> map = MapExtensions.toGenericMap(twoDimArray);

		String[][] toTwoDimensionalArray = ArrayExtensions.toTwoDimensionalArray(map, String.class);
		assertArrayEquals(toTwoDimensionalArray, twoDimArray);
	}

	/**
	 * Test method for {@link ArrayExtensions#getLastIndex(Object[])}
	 */
	@Test
	public void testGetLastIndexObjectArray()
	{
		int actual;
		int expected;
		Integer[] source;

		source = ArrayFactory.newArray(1, 2, 3, 4, 5, 6, 7, 8, 9);
		actual = ArrayExtensions.getLastIndex(source);
		expected = 8;
		assertEquals(expected, actual);

		source = null;
		actual = ArrayExtensions.getLastIndex(source);
		expected = -1;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ArrayExtensions#getLastIndex(int[])}
	 */
	@Test
	public void testGetLastIndexIntArray()
	{

		int actual;
		int expected;
		int[] source;

		source = ArrayFactory.newIntArray(1, 2, 3, 4, 5, 6, 7, 8, 9);
		actual = ArrayExtensions.getLastIndex(source);
		expected = 8;
		assertEquals(expected, actual);

		source = null;
		actual = ArrayExtensions.getLastIndex(source);
		expected = -1;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ArrayExtensions#getLastIndex(boolean[])}
	 */
	@Test
	public void testGetLastIndexBooleanArray()
	{
		int actual;
		int expected;
		boolean[] source;
		source = ArrayFactory.newBooleanArray(true, true, false);
		actual = ArrayExtensions.getLastIndex(source);
		expected = 2;
		assertEquals(expected, actual);

		source = null;
		actual = ArrayExtensions.getLastIndex(source);
		expected = -1;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ArrayExtensions#getLastIndex(boolean[])}
	 */
	@Test
	public void testGetLastIndexByteArray()
	{
		int actual;
		int expected;
		byte[] source;
		source = ArrayFactory.newByteArray((byte)-84, (byte)-19, (byte)0, (byte)5, (byte)116,
			(byte)0, (byte)7, (byte)70, (byte)111, (byte)111, (byte)32, (byte)98, (byte)97);
		actual = ArrayExtensions.getLastIndex(source);
		expected = 12;
		assertEquals(expected, actual);

		source = null;
		actual = ArrayExtensions.getLastIndex(source);
		expected = -1;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ArrayExtensions#getLastIndex(char[])}
	 */
	@Test
	public void testGetLastIndexCharArray()
	{
		int actual;
		int expected;
		char[] source;
		source = ArrayFactory.newCharArray('f', 'o', 'o');
		actual = ArrayExtensions.getLastIndex(source);
		expected = 2;
		assertEquals(expected, actual);

		source = null;
		actual = ArrayExtensions.getLastIndex(source);
		expected = -1;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ArrayExtensions#getLastIndex(double[])}
	 */
	@Test
	public void testGetLastIndexDoubleArray()
	{
		int actual;
		int expected;
		double[] source;
		source = ArrayFactory.newDoubleArray(1.1D, 2.1D, 3.1D);
		actual = ArrayExtensions.getLastIndex(source);
		expected = 2;
		assertEquals(expected, actual);

		source = null;
		actual = ArrayExtensions.getLastIndex(source);
		expected = -1;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ArrayExtensions#getLastIndex(float[])}
	 */
	@Test
	public void testGetLastIndexFloatArray()
	{
		int actual;
		int expected;
		float[] source;
		source = ArrayFactory.newFloatArray(1.0F, 2.0F, 3.0F);
		actual = ArrayExtensions.getLastIndex(source);
		expected = 2;
		assertEquals(expected, actual);

		source = null;
		actual = ArrayExtensions.getLastIndex(source);
		expected = -1;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ArrayExtensions#getLastIndex(long[])}
	 */
	@Test
	public void testGetLastIndexLongArray()
	{
		int actual;
		int expected;
		long[] source;
		source = ArrayFactory.newLongArray(1L, 2L, 3L);
		actual = ArrayExtensions.getLastIndex(source);
		expected = 2;
		assertEquals(expected, actual);

		source = null;
		actual = ArrayExtensions.getLastIndex(source);
		expected = -1;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ArrayExtensions#getLastIndex(short[])}
	 */
	@Test
	public void testGetLastIndexShortArray()
	{
		int actual;
		int expected;
		short[] source;
		source = ArrayFactory.newShortArray((short)1, (short)2, (short)3);
		actual = ArrayExtensions.getLastIndex(source);
		expected = 2;
		assertEquals(expected, actual);

		source = null;
		actual = ArrayExtensions.getLastIndex(source);
		expected = -1;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ArrayExtensions#arraycopyWithSystem(Object[], Object[])}.
	 */
	@Test
	public void testArraycopyWithSystem()
	{
		boolean actual;
		boolean expected;
		Integer[] source;
		Integer[] destination;

		source = ArrayFactory.newArray(1, 2, 3, 4, 5, 6, 7, 8, 9);
		destination = new Integer[source.length];
		destination = ArrayExtensions.arraycopyWithSystem(source, destination);
		actual = Arrays.deepEquals(source, destination);
		expected = true;
		assertEquals(expected, actual);

		source = null;
		destination = ArrayExtensions.arraycopyWithSystem(source, destination);
		assertNull(destination);
	}

	/**
	 * Test for method {@link ArrayExtensions#asList(Object[])}
	 */
	@Test
	public void testAsList()
	{
		String actual;
		String expected;
		final String[] numbers = { "1", "2", "3", "4", "5", "6", "7" };
		final List<String> numberList = ArrayExtensions.asList(numbers);
		for (int i = 0; i < numbers.length; i++)
		{
			expected = numbers[i];
			actual = numberList.get(i);
			assertEquals(expected, actual);
		}
	}

	/**
	 * Test for method {@link ArrayExtensions#asSet(Object[])}
	 */
	@Test
	public void testAsSet()
	{
		String actual;
		String expected;
		final String[] numbers = { "1", "2", "3", "4", "5", "6", "7" };
		final Set<String> numberSet = ArrayExtensions.asSet("1", "2", "3", "4", "5", "6", "7");

		Iterator<String> iterator = numberSet.iterator();
		int i = 0;
		while (iterator.hasNext())
		{
			actual = iterator.next();
			expected = numbers[i];
			assertEquals(expected, actual);
			i++;
		}
	}

	/**
	 * Test for method {@link ArrayExtensions#asStream(Object[])}
	 */
	@Test
	public void testAsStream()
	{
		final String[] numbers = { "1", "2", "3", "4", "5", "6", "7" };
		final Stream<String> numberSet = ArrayExtensions.asStream("1", "2", "3", "4", "5", "6",
			"7");

		numberSet.forEach(i -> {
			assertTrue(ArrayExtensions.contains(numbers, i));
		});
	}

	/**
	 * Test method for {@link ArrayExtensions#contains(Object[], Object)} .
	 */
	@Test
	public void testContains()
	{
		boolean expected;
		boolean actual;

		final String last = "7";
		final String[] numbers = { "1", "2", "3", "4", "5", "6", last };
		// Old vanilla java with static method...
		actual = ArrayExtensions.contains(numbers, last);
		expected = true;
		assertEquals(expected, actual);
		final String[] empty = { };
		expected = false;
		actual = ArrayExtensions.contains(empty, last);
		assertEquals(expected, actual);

		actual = ArrayExtensions.contains(empty, null);
		assertEquals(expected, actual);

		actual = ArrayExtensions.contains(empty, 8);
		assertEquals(expected, actual);

	}

	/**
	 * Test method for {@link ArrayExtensions#containsAtLeastOne(Object[], Object[])}
	 */
	@Test
	public void testContainsAtLeastOne()
	{
		boolean expected;
		boolean actual;
		String last;

		last = "7";
		final String[] numbers = { "1", "2", "3", "4", "5", "6", last };
		// Old vanilla java with static method...
		actual = ArrayExtensions.containsAtLeastOne(numbers, last, "6");
		expected = true;
		assertEquals(expected, actual);
		final String[] empty = { };
		expected = false;
		actual = ArrayExtensions.containsAtLeastOne(empty, last);
		assertEquals(expected, actual);

		actual = ArrayExtensions.containsAtLeastOne(empty, 8);
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#getFirst(Object[])}
	 */
	@Test
	public void testGetFirst()
	{
		String expected;
		String actual;
		expected = "1";
		final String[] numbers = { expected, "2", "3", "4", "5", "6", "7" };
		// Old vanilla java with static method...
		actual = ArrayExtensions.getFirst(numbers);
		assertEquals(expected, actual);
		final String[] empty = { };
		expected = null;
		actual = ArrayExtensions.getFirst(empty);
		assertEquals(expected, actual);

		actual = ArrayExtensions.getFirst(null);
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#getIndex(Object[], Object)}
	 */
	@Test
	public void testGetIndex()
	{
		int expected;
		int actual;
		expected = 6;
		final String last = "7";
		final String[] numbers = { "1", "2", "3", "4", "5", "6", last };
		// Old vanilla java with static method...
		actual = ArrayExtensions.getIndex(numbers, last);
		assertEquals(expected, actual);
		final String[] empty = { };
		expected = -1;
		actual = ArrayExtensions.getIndex(empty, last);
		assertEquals(expected, actual);

		actual = ArrayExtensions.getIndex(empty, null);
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#getLast(Object[])}
	 */
	@Test
	public void testGetLast()
	{
		String expected;
		String actual;
		expected = "7";
		final String[] numbers = { "1", "2", "3", "4", "5", "6", expected };
		// Old vanilla java with static method...
		actual = ArrayExtensions.getLast(numbers);
		assertEquals(expected, actual);
		final String[] empty = { };
		expected = null;
		actual = ArrayExtensions.getLast(empty);
		assertEquals(expected, actual);

		actual = ArrayExtensions.getLast(null);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ArrayExtensions#hasNext(Object[], Object)}
	 */
	@Test
	public void testHasNext()
	{
		boolean expected;
		boolean actual;
		String last;
		String first;
		String[] numbers;

		last = "7";
		first = "1";
		numbers = ArrayFactory.newArray(first, "2", "3", "4", "5", "6", last);

		expected = false;
		actual = ArrayExtensions.hasNext(numbers, null);
		assertEquals(expected, actual);

		expected = true;
		actual = ArrayExtensions.hasNext(numbers, "6");
		assertEquals(expected, actual);

		expected = false;
		actual = ArrayExtensions.hasNext(numbers, last);
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#getNextIndex(Object[], Object)}
	 */
	@Test
	public void testGetNextIndex()
	{
		int expected;
		int actual;
		String last;
		String first;
		String[] numbers;

		last = "7";
		first = "1";
		numbers = ArrayFactory.newArray(first, "2", "3", "4", "5", "6", last);
		expected = 0;

		// use case with last...
		// Old vanilla java with static method...
		actual = ArrayExtensions.getNextIndex(numbers, last);
		assertEquals(expected, actual);
		// use case with first...
		expected = 1;
		actual = ArrayExtensions.getNextIndex(numbers, first);
		assertEquals(expected, actual);
		// scenarios of empty or null value...
		final String[] empty = { };
		expected = -1;
		actual = ArrayExtensions.getNextIndex(empty, last);
		assertEquals(expected, actual);

		actual = ArrayExtensions.getNextIndex(empty, null);
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#getNextIndexes(Object[], Object, int)}
	 */
	@Test
	public void testGetNextIndexes()
	{
		int[] actual;
		int[] expected;
		String last;
		String first;
		String[] numbers;

		last = "7";
		first = "1";
		numbers = ArrayFactory.newArray(first, "2", "3", "4", "5", "6", last);

		// use case with last...
		// Old vanilla java with static method...
		actual = ArrayExtensions.getNextIndexes(numbers, last, 2);
		expected = ArrayFactory.newIntArray(0, 1);
		assertEquals(expected[0], actual[0]);
		assertEquals(expected[1], actual[1]);
		// use case with first...
		expected[0] = 1;
		expected[1] = 2;
		actual = ArrayExtensions.getNextIndexes(numbers, first, 2);
		assertEquals(expected[0], actual[0]);
		assertEquals(expected[1], actual[1]);
		// scenarios of empty or null value...
		final String[] empty = { };
		expected = null;
		actual = ArrayExtensions.getNextIndexes(empty, last, 2);
		assertEquals(expected, actual);

		actual = ArrayExtensions.getNextIndexes(empty, null, 2);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ArrayExtensions#hasPrevious(Object[], Object)}
	 */
	@Test
	public void testHasPrevious()
	{
		boolean expected;
		boolean actual;
		String last;
		String first;
		String[] numbers;
		String[] empty;

		last = "7";
		first = "1";
		numbers = ArrayFactory.newArray(first, "2", "3", "4", "5", "6", last);

		expected = true;
		// use case with last...
		// Old vanilla java with static method...
		actual = ArrayExtensions.hasPrevious(numbers, last);
		assertEquals(expected, actual);
		// use case with first...
		expected = false;
		actual = ArrayExtensions.hasPrevious(numbers, first);
		assertEquals(expected, actual);
		// scenarios of empty or null value...
		empty = ArrayFactory.newArray();
		expected = false;
		actual = ArrayExtensions.hasPrevious(empty, last);
		assertEquals(expected, actual);

		actual = ArrayExtensions.hasPrevious(empty, null);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ArrayExtensions#getNextElement(Object[], Object)}
	 */
	@Test
	public final void testGetNextElement()
	{
		Optional<String> expected;
		Optional<String> actual;
		List<String> search;

		String caeser = "Caesar";
		String dora = "Dora";
		String emil = "Emil";
		String anton = "Anton";
		search = ListFactory.newArrayList();
		search.add(caeser);
		search.add(dora);
		search.add(emil);
		search.add(anton);

		actual = ArrayExtensions.getNextElement(ListExtensions.toArray(search), "foo");
		expected = Optional.empty();
		assertEquals(expected, actual);

		actual = ArrayExtensions.getNextElement(ListExtensions.toArray(search), null);
		expected = Optional.empty();
		assertEquals(expected, actual);

		actual = ArrayExtensions.getNextElement(ListExtensions.toArray(search), anton);
		expected = Optional.empty();
		assertEquals(expected, actual);

		actual = ArrayExtensions.getNextElement(ListExtensions.toArray(search), emil);
		expected = Optional.of(anton);
		assertEquals(expected, actual);

		actual = ArrayExtensions.getNextElement(ListExtensions.toArray(search), dora);
		expected = Optional.of(emil);
		assertEquals(expected, actual);

		actual = ArrayExtensions.getNextElement(ListExtensions.toArray(search), caeser);
		expected = Optional.of(dora);
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#getPreviousElement(Object[], Object)}
	 */
	@Test
	public void testGetPreviousElement()
	{
		Optional<String> expected;
		Optional<String> actual;
		List<String> search;

		String caeser = "Caesar";
		String dora = "Dora";
		String emil = "Emil";
		String anton = "Anton";
		search = ListFactory.newArrayList();
		search.add(caeser);
		search.add(dora);
		search.add(emil);
		search.add(anton);

		actual = ArrayExtensions.getPreviousElement(ListExtensions.toArray(search), "foo");
		expected = Optional.empty();
		assertEquals(expected, actual);

		actual = ArrayExtensions.getPreviousElement(ListExtensions.toArray(search), null);
		expected = Optional.empty();
		assertEquals(expected, actual);

		actual = ArrayExtensions.getPreviousElement(ListExtensions.toArray(search), caeser);
		expected = Optional.empty();
		assertEquals(expected, actual);

		actual = ArrayExtensions.getPreviousElement(ListExtensions.toArray(search), dora);
		expected = Optional.of(caeser);
		assertEquals(expected, actual);

		actual = ArrayExtensions.getPreviousElement(ListExtensions.toArray(search), emil);
		expected = Optional.of(dora);
		assertEquals(expected, actual);

		actual = ArrayExtensions.getPreviousElement(ListExtensions.toArray(search), anton);
		expected = Optional.of(emil);
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#getPreviousIndex(Object[], Object)}
	 */
	@Test
	public void testGetPreviousIndex()
	{
		int expected;
		int actual;
		String last;
		String first;
		String[] numbers;

		last = "7";
		first = "1";
		numbers = ArrayFactory.newArray(first, "2", "3", "4", "5", "6", last);

		expected = 5;
		// use case with last...
		// Old vanilla java with static method...
		actual = ArrayExtensions.getPreviousIndex(numbers, last);
		assertEquals(expected, actual);
		// use case with first...
		expected = 6;
		actual = ArrayExtensions.getPreviousIndex(numbers, first);
		assertEquals(expected, actual);
		// scenarios of empty or null value...
		final String[] empty = { };
		expected = -1;
		actual = ArrayExtensions.getPreviousIndex(empty, last);
		assertEquals(expected, actual);

		actual = ArrayExtensions.getPreviousIndex(empty, null);
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#getPreviousIndexes(Object[], Object, int)}
	 */
	@Test
	public void testGetPreviousIndexes()
	{
		int[] expected = { 5, 4 };
		final String last = "7";
		final String first = "1";
		final String[] numbers = { first, "2", "3", "4", "5", "6", last };
		// use case with last...
		// Old vanilla java with static method...
		int[] actual = ArrayExtensions.getPreviousIndexes(numbers, last, 2);
		assertEquals(expected[0], actual[0]);
		assertEquals(expected[1], actual[1]);
		// use case with first...
		expected[0] = 6;
		expected[1] = 5;
		actual = ArrayExtensions.getPreviousIndexes(numbers, first, 2);
		assertEquals(expected[0], actual[0]);
		assertEquals(expected[1], actual[1]);
		// scenarios of empty or null value...
		final String[] empty = { };
		expected = null;
		actual = ArrayExtensions.getPreviousIndexes(empty, last, 2);
		assertEquals(expected, actual);

		actual = ArrayExtensions.getPreviousIndexes(empty, null, 2);
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#indexOf(Object[], Object)}
	 */
	@Test
	public void testIndexOf()
	{
		int expected;
		int actual;
		expected = 6;
		final String last = "7";
		final String[] numbers = { "1", "2", "3", "4", "5", "6", last };
		// Old vanilla java with static method...
		actual = ArrayExtensions.indexOf(numbers, last);
		assertEquals(expected, actual);
		final String[] empty = { };
		expected = -1;
		actual = ArrayExtensions.indexOf(empty, last);
		assertEquals(expected, actual);

		actual = ArrayExtensions.indexOf(empty, null);
		assertEquals(expected, actual);
	}

	/**
	 * Test the method for {@link ArrayExtensions#intersection(Object[], Object[])}
	 */
	@Test
	public void testIntersection()
	{
		String[] actual;
		String[] expected;
		String[] one;
		String[] other;

		one = ArrayFactory.newArray("Anton", "Alex", "Berta", "Brad", "Caesar", "Leonardo");

		other = ArrayFactory.newArray("Alex", "Berta", "Brad", "Caesar", "Leonardo");

		actual = ArrayExtensions.intersection(one, other);

		expected = ArrayFactory.newArray("Alex", "Berta", "Brad", "Caesar", "Leonardo");
		assertArrayEquals(actual, expected);

	}

	/**
	 * Test method for {@link ArrayExtensions#isFirst(Object[], Object)} .
	 */
	@Test
	public void testIsFirst()
	{
		boolean expected;
		boolean actual;
		final String[] numbers = { "1", "2", "3", "4", "5", "6", "7" };

		actual = ArrayExtensions.isFirst(numbers, "1");
		expected = true;
		assertEquals(expected, actual);

		actual = ArrayExtensions.isFirst(numbers, null);
		expected = false;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ArrayExtensions#isLast(Object[], Object)} .
	 */
	@Test
	public void testIsLast()
	{
		boolean expected;
		boolean actual;

		final String[] numbers = { "1", "2", "3", "4", "5", "6", "7" };

		actual = ArrayExtensions.isLast(numbers, "7");
		expected = true;
		assertEquals(expected, actual);

		actual = ArrayExtensions.isLast(numbers, null);
		expected = false;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ArrayExtensions#removeAll(Object[], Object[])}
	 */
	@Test
	public void testRemoveAll()
	{
		String[] actual;
		String[] expected;
		String[] source;
		String[] arrayToRemove;

		source = ArrayFactory.newArray("1", "2", "3", "4");
		arrayToRemove = ArrayFactory.newArray("2", "3", "4");
		actual = ArrayExtensions.removeAll(source, arrayToRemove);
		expected = ArrayFactory.newArray("1");
		assertArrayEquals(actual, expected);
	}

	/**
	 * Test method for {@link ArrayExtensions#removeFirst(Object[])}
	 */
	@Test
	public void testRemoveFirst()
	{
		String[] actual;
		String[] expected;
		String[] source;

		source = ArrayFactory.newArray("1", "2", "3", "4");
		actual = ArrayExtensions.removeFirst(source);
		expected = ArrayFactory.newArray("2", "3", "4");
		assertArrayEquals(actual, expected);
	}

	/**
	 * Test method for {@link ArrayExtensions#removeFromEnd(Object[], Object[])}
	 */
	@Test
	public void testRemoveFromEnd()
	{
		Integer[] actual;
		Integer[] expected;
		Integer[] source;
		Integer[] toRemove;

		source = ArrayFactory.newArray(1, 2, 3, 4, 5, 6, 7, 8, 9);
		toRemove = ArrayFactory.newArray(7, 8, 9);
		actual = ArrayExtensions.removeFromEnd(source, toRemove);
		expected = ArrayFactory.newArray(1, 2, 3, 4, 5, 6);
		assertArrayEquals(actual, expected);
	}

	/**
	 * Test method for {@link ArrayExtensions#removeFromStart(Object[], Object[])}
	 */
	@Test
	public void testRemoveFromStart()
	{
		Integer[] actual;
		Integer[] expected;
		Integer[] source;
		Integer[] toRemove;

		source = ArrayFactory.newArray(1, 2, 3, 4, 5, 6, 7, 8, 9);
		toRemove = ArrayFactory.newArray(1, 2, 3);
		actual = ArrayExtensions.removeFromStart(source, toRemove);
		expected = ArrayFactory.newArray(4, 5, 6, 7, 8, 9);
		assertArrayEquals(actual, expected);

		source = ArrayFactory.newArray();
		toRemove = ArrayFactory.newArray(1, 2, 3);
		actual = ArrayExtensions.removeFromStart(source, toRemove);
		expected = ArrayFactory.newArray();
		assertArrayEquals(actual, expected);
	}

	/**
	 * Test method for {@link ArrayExtensions#removeLast(Object[])}
	 */
	@Test
	public void testRemoveLast()
	{
		String[] actual;
		String[] expected;
		String[] source;

		source = ArrayFactory.newArray("1", "2", "3", "4");
		actual = ArrayExtensions.removeLast(source);
		expected = ArrayFactory.newArray("1", "2", "3");
		assertArrayEquals(actual, expected);
	}

	/**
	 * Test method for {@link ArrayExtensions#remove(Object[], int... )}
	 */
	@Test
	public void testRemoveVarArgs()
	{

		String[] actual;
		String[] expected;
		String[] source;
		// new scenario...
		source = ArrayFactory.newArray("1", "2", "3", "4");
		actual = ArrayExtensions.remove(source, 2, 3);
		expected = ArrayFactory.newArray("1", "2");
		assertArrayEquals(actual, expected);
		// new scenario unsorted
		source = ArrayFactory.newArray("1", "2", "3", "4", "5", "6");
		actual = ArrayExtensions.remove(source, 5, 3, 1);
		expected = ArrayFactory.newArray("1", "3", "5");
		assertArrayEquals(actual, expected);
		// new scenario unsorted
		source = ArrayFactory.newArray("1", "2", "3", "4", "5", "6");
		actual = ArrayExtensions.remove(source, 3, 5, 1);
		expected = ArrayFactory.newArray("1", "3", "5");
		assertArrayEquals(actual, expected);
	}

	/**
	 * Test for method {@link ArrayExtensions#splitInChunks(byte[], int)}
	 */
	@Test
	public void testSplit()
	{
		int expected;
		int actual;
		final String byteString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

		final byte[] byteArray = byteString.getBytes();

		expected = 56;
		actual = byteArray.length;
		assertEquals(expected, actual);

		final byte[][] splitedBytes = ArrayExtensions.splitInChunks(byteArray, 5);

		expected = 12;
		actual = splitedBytes.length;
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayExtensions#toList(Object[])}
	 */
	@Test
	public void testToList()
	{
		String actual;
		String expected;
		final String[] numbers = { "1", "2", "3", "4", "5", "6", "7" };
		final List<String> numberList = ArrayExtensions.toList(numbers);
		for (int i = 0; i < numbers.length; i++)
		{
			expected = numbers[i];
			actual = numberList.get(i);
			assertEquals(expected, actual);
		}
	}

	/**
	 * Test for method {@link ArrayFactory#newArray(Object[], Object[])}
	 */
	@Test
	public void testNewArrayWithExistingArray()
	{
		Integer[] actual;
		Integer[] expected;
		final Integer[] numbers = { 1, 2, 3 };
		final Integer[] moreNumbers = { 4, 5, 6 };

		actual = ArrayExtensions.concatenate(numbers, moreNumbers);
		expected = ArrayFactory.newArray(1, 2, 3, 4, 5, 6);
		assertArrayEquals(actual, expected);

		actual = ArrayExtensions.concatenate(ArrayFactory.newArray(), numbers);
		expected = ArrayFactory.newArray(numbers);
		assertArrayEquals(actual, expected);

		actual = ArrayExtensions.concatenate(null, numbers);
		assertArrayEquals(actual, expected);

		actual = ArrayExtensions.concatenate(null, null);
		expected = null;
		assertArrayEquals(actual, expected);
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
