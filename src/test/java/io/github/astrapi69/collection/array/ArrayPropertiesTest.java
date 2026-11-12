/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.collection.array;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.collection.test.EdgeCases;

/**
 * Properties of the methods of {@link ArrayFactory} and {@link ArrayExtensions} that their Javadoc
 * promises, checked for the inputs of {@link EdgeCases} (#179). A table of rows finds the cases
 * someone wrote down; these find the edge of an {@code int} and of a collection for every method at
 * once, as #113 (an overflow at {@link Integer#MAX_VALUE}) needed
 */
public class ArrayPropertiesTest
{

	private static final String LISTS = "io.github.astrapi69.collection.test.EdgeCases#stringLists";
	private static final String LIST_PAIRS = "io.github.astrapi69.collection.test.EdgeCases#stringListPairs";
	private static final String SHORT_RANGES = "io.github.astrapi69.collection.test.EdgeCases#shortIntPairs";
	private static final String TOO_LONG_RANGES = "io.github.astrapi69.collection.test.EdgeCases#tooLongIntPairs";
	private static final String SEEDS = "io.github.astrapi69.collection.test.EdgeCases#seeds";

	/**
	 * The array of the elements of a list
	 *
	 * @param list
	 *            the list
	 * @return the array
	 */
	private static String[] array(final List<String> list)
	{
		return list.toArray(new String[0]);
	}

	/**
	 * Every collection with every {@code int} edge case as the index
	 *
	 * @return the collection and the index
	 */
	static Stream<Arguments> listsWithIntEdgeCase()
	{
		return EdgeCases.stringLists().flatMap(
			list -> EdgeCases.ints().map(index -> Arguments.of(new ArrayList<>(list), index)));
	}

	/**
	 * Every collection with every pair of {@code int} edge cases, in both orders
	 *
	 * @return the collection and the pair as first and second
	 */
	static Stream<Arguments> listsWithIntPairs()
	{
		return EdgeCases.stringLists().flatMap(list -> EdgeCases.allIntPairs()
			.map(pair -> Arguments.of(new ArrayList<>(list), pair.get()[0], pair.get()[1])));
	}

	/**
	 * Every seed with every {@code int} edge case as the chunk size
	 *
	 * @return the seed and the chunk size
	 */
	static Stream<Arguments> seedsWithChunkSize()
	{
		return EdgeCases.seeds()
			.flatMap(seed -> EdgeCases.ints().map(chunkSize -> Arguments.of(seed, chunkSize)));
	}

	/**
	 * Property of {@link ArrayFactory#newRangeArray(int, int)}: "Creates a new Integer array with
	 * the given range that is defined through start and end, both inclusive. For instance if the
	 * start is 5 and the end is 9 the resulted array will be [5,6,7,8,9]": the array has
	 * {@code end - start + 1} elements, the first is the start, the last is the end and each one is
	 * one more than the one before, for every short range of the edge cases, also at
	 * {@link Integer#MIN_VALUE} and {@link Integer#MAX_VALUE} (#113). The range of a longer array
	 * is not made
	 *
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 */
	@ParameterizedTest(name = "{0} to {1}")
	@MethodSource(SHORT_RANGES)
	public void testNewRangeArrayHoldsTheNumbersFromStartToEnd(final int start, final int end)
	{
		final Integer[] actual = ArrayFactory.newRangeArray(start, end);
		assertEquals(EdgeCases.rangeSize(start, end), actual.length);
		assertEquals(start, actual[0]);
		assertEquals(end, actual[actual.length - 1]);
		for (int i = 0; i < actual.length; i++)
		{
			assertEquals((long)start + i, actual[i].longValue());
		}
	}

	/**
	 * Property of {@link ArrayFactory#newRangeArray(int, int)} for random ranges: "@throws
	 * IllegalArgumentException if end is less than start" is not the case, so the array has
	 * {@code end - start + 1} elements, the first is the start and the last is the end. The start
	 * is any {@code int} and the length of the range up to 50, from the seed (shown in the name)
	 *
	 * @param seed
	 *            the seed of the random numbers
	 */
	@ParameterizedTest(name = "seed {0}")
	@MethodSource(SEEDS)
	public void testNewRangeArrayOfRandomRanges(final long seed)
	{
		final Random random = EdgeCases.random(seed);
		for (int run = 0; run < 100; run++)
		{
			final int length = 1 + random.nextInt(50);
			final int start = (int)Math.min(random.nextInt(), (long)Integer.MAX_VALUE - length + 1);
			final int end = start + length - 1;
			final Integer[] actual = ArrayFactory.newRangeArray(start, end);
			final String message = "seed " + seed + ", run " + run + ": " + start + " to " + end;
			assertEquals(length, actual.length, message);
			assertEquals(start, actual[0], message);
			assertEquals(end, actual[length - 1], message);
		}
	}

	/**
	 * Property of {@link ArrayFactory#newRangeArray(int, int)}: "@throws IllegalArgumentException
	 * if end is less than start, or if the range holds more than {@link Integer#MAX_VALUE} numbers"
	 * (#113), for every range of the edge cases that holds more
	 *
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 */
	@ParameterizedTest(name = "{0} to {1}")
	@MethodSource(TOO_LONG_RANGES)
	public void testNewRangeArrayThrowsForMoreNumbersThanAnArrayHolds(final int start,
		final int end)
	{
		assertThrows(IllegalArgumentException.class, () -> ArrayFactory.newRangeArray(start, end));
	}

	/**
	 * Property of {@link ArrayFactory#newRangeArray(int, int)}: "@throws IllegalArgumentException
	 * if end is less than start", for every pair of the edge cases with the end less than the start
	 *
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 */
	@ParameterizedTest(name = "{0} to {1}")
	@MethodSource("io.github.astrapi69.collection.test.EdgeCases#allIntPairs")
	public void testNewRangeArrayThrowsForEndLessThanStart(final int start, final int end)
	{
		if (end < start)
		{
			assertThrows(IllegalArgumentException.class,
				() -> ArrayFactory.newRangeArray(start, end));
		}
	}

	/**
	 * Property of {@link ArrayFactory#newSubArray(Object[], int, int)}: "@throws
	 * IndexOutOfBoundsException if {@code startIndex < 0}, {@code endIndex > source.length} or
	 * {@code startIndex > endIndex}", otherwise the elements from the start index, inclusive, to
	 * the end index, exclusive, for every collection and every pair of the {@code int} edge cases
	 *
	 * @param list
	 *            the elements of the source
	 * @param startIndex
	 *            the start index
	 * @param endIndex
	 *            the end index
	 */
	@ParameterizedTest(name = "{0}, {1} to {2}")
	@MethodSource("listsWithIntPairs")
	public void testNewSubArrayOfEdgeIndexes(final List<String> list, final int startIndex,
		final int endIndex)
	{
		final String[] source = array(list);
		if (startIndex < 0 || endIndex > source.length || startIndex > endIndex)
		{
			assertThrows(IndexOutOfBoundsException.class,
				() -> ArrayFactory.newSubArray(source, startIndex, endIndex));
		}
		else
		{
			assertEquals(list.subList(startIndex, endIndex),
				Arrays.asList(ArrayFactory.newSubArray(source, startIndex, endIndex)));
		}
	}

	/**
	 * Property of {@link ArrayFactory#newSubArray(Object[], int, int)}: the new partial array holds
	 * the elements from the start index to the end index, for every range inside the array
	 *
	 * @param list
	 *            the elements of the source
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource(LISTS)
	public void testNewSubArrayOfEveryRangeInsideTheArray(final List<String> list)
	{
		final String[] source = array(list);
		for (int from = 0; from <= source.length; from++)
		{
			for (int to = from; to <= source.length; to++)
			{
				assertEquals(list.subList(from, to),
					Arrays.asList(ArrayFactory.newSubArray(source, from, to)), from + " to " + to);
			}
		}
	}

	/**
	 * Property of {@link ArrayFactory#newArray(Object...)}: "The result is a new array, also when
	 * no elements are given: a copy of the given array, so that a write to the result does not
	 * change the array of the caller"
	 *
	 * @param list
	 *            the elements
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource(LISTS)
	public void testNewArrayIsACopy(final List<String> list)
	{
		final String[] elements = array(list);
		final String[] actual = ArrayFactory.newArray(elements);
		assertNotSame(elements, actual);
		assertArrayEquals(elements, actual);
		Arrays.fill(actual, "changed");
		assertEquals(list, Arrays.asList(elements));
	}

	/**
	 * Property of {@link ArrayFactory#newArray(Object[], Object...)}: "The result is a new array
	 * with the elements of the existing array followed by the given elements", for every pair of
	 * collections
	 *
	 * @param existing
	 *            the elements of the existing array
	 * @param elements
	 *            the given elements
	 */
	@ParameterizedTest(name = "{0} and {1}")
	@MethodSource(LIST_PAIRS)
	public void testNewArrayFromExistingArrayFollowedByElements(final List<String> existing,
		final List<String> elements)
	{
		final List<String> expected = new ArrayList<>(existing);
		expected.addAll(elements);
		final String[] existingArray = array(existing);
		final String[] elementsArray = array(elements);
		final String[] actual = ArrayFactory.newArray(existingArray, elementsArray);
		assertEquals(expected, Arrays.asList(actual));
		assertNotSame(existingArray, actual);
		assertNotSame(elementsArray, actual);
	}

	/**
	 * Property of {@link ArrayExtensions#remove(Object[], int...)}: "@throws
	 * IndexOutOfBoundsException if one of the given indexes is negative or not less than the length
	 * of the given array", otherwise the new array is without the element at the index and the
	 * given array is unchanged, for every collection and every {@code int} edge case
	 *
	 * @param list
	 *            the elements of the array
	 * @param index
	 *            the index
	 */
	@ParameterizedTest(name = "{0}, index {1}")
	@MethodSource("listsWithIntEdgeCase")
	public void testRemoveAnIndex(final List<String> list, final int index)
	{
		final String[] array = array(list);
		if (index < 0 || index >= array.length)
		{
			assertThrows(IndexOutOfBoundsException.class,
				() -> ArrayExtensions.remove(array, index));
		}
		else
		{
			final List<String> expected = new ArrayList<>(list);
			expected.remove(index);
			assertEquals(expected, Arrays.asList(ArrayExtensions.remove(array, index)));
		}
		assertEquals(list, Arrays.asList(array));
	}

	/**
	 * Property of {@link ArrayExtensions#removeFirst(Object[])} and
	 * {@link ArrayExtensions#removeLast(Object[])}: "Creates a new array cloned from the given
	 * array with the difference that the first element is removed" (the last), "@throws
	 * IndexOutOfBoundsException if the given array is empty"
	 *
	 * @param list
	 *            the elements of the array
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource(LISTS)
	public void testRemoveFirstAndRemoveLast(final List<String> list)
	{
		final String[] array = array(list);
		if (array.length == 0)
		{
			assertThrows(IndexOutOfBoundsException.class, () -> ArrayExtensions.removeFirst(array));
			assertThrows(IndexOutOfBoundsException.class, () -> ArrayExtensions.removeLast(array));
		}
		else
		{
			assertEquals(list.subList(1, list.size()),
				Arrays.asList(ArrayExtensions.removeFirst(array)));
			assertEquals(list.subList(0, list.size() - 1),
				Arrays.asList(ArrayExtensions.removeLast(array)));
		}
		assertEquals(list, Arrays.asList(array));
	}

	/**
	 * Property of {@link ArrayExtensions#removeFromStart(Object[], Object[])}: "The array starts
	 * with the prefix if its first elements equal those of the prefix, in their order; an empty
	 * prefix is a prefix of every array. Otherwise, also for a prefix longer than the array, the
	 * result is a copy of the given array", for every prefix of the collection
	 *
	 * @param list
	 *            the elements of the array
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource(LISTS)
	public void testRemoveFromStart(final List<String> list)
	{
		final String[] array = array(list);
		for (int length = 0; length <= array.length; length++)
		{
			final String[] prefix = Arrays.copyOf(array, length);
			assertEquals(list.subList(length, list.size()),
				Arrays.asList(ArrayExtensions.removeFromStart(array, prefix)), "prefix " + length);
		}
		final List<String> longer = new ArrayList<>(list);
		longer.add("z");
		final String[] copy = ArrayExtensions.removeFromStart(array, array(longer));
		assertEquals(list, Arrays.asList(copy));
		assertNotSame(array, copy);
	}

	/**
	 * Property of {@link ArrayExtensions#removeFromEnd(Object[], Object[])}: "The array ends with
	 * the suffix if its last elements equal those of the suffix, in their order; an empty suffix is
	 * a suffix of every array. Otherwise, also for a suffix longer than the array, the result is a
	 * copy of the given array", for every suffix of the collection
	 *
	 * @param list
	 *            the elements of the array
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource(LISTS)
	public void testRemoveFromEnd(final List<String> list)
	{
		final String[] array = array(list);
		for (int length = 0; length <= array.length; length++)
		{
			final String[] suffix = Arrays.copyOfRange(array, array.length - length, array.length);
			assertEquals(list.subList(0, list.size() - length),
				Arrays.asList(ArrayExtensions.removeFromEnd(array, suffix)), "suffix " + length);
		}
		final List<String> longer = new ArrayList<>(list);
		longer.add(0, "z");
		final String[] copy = ArrayExtensions.removeFromEnd(array, array(longer));
		assertEquals(list, Arrays.asList(copy));
		assertNotSame(array, copy);
	}

	/**
	 * Property of {@link ArrayExtensions#intersection(Object[], Object[])}: "the elements of the
	 * first array that the other array holds, in the order of the first array and with its
	 * duplicates", for every pair of collections
	 *
	 * @param one
	 *            the elements of the first array
	 * @param other
	 *            the elements of the other array
	 */
	@ParameterizedTest(name = "{0} and {1}")
	@MethodSource(LIST_PAIRS)
	public void testIntersectionKeepsTheElementsOfTheFirstArrayThatTheOtherHolds(
		final List<String> one, final List<String> other)
	{
		final List<String> expected = new ArrayList<>(one);
		expected.removeIf(element -> !other.contains(element));
		assertEquals(expected,
			Arrays.asList(ArrayExtensions.intersection(array(one), array(other))));
	}

	/**
	 * Property of {@link ArrayExtensions#toSet(Object[])}: a {@link Set} with each element once, in
	 * the order of the array ({@code SetFactory#newLinkedHashSet}), for every collection
	 *
	 * @param list
	 *            the elements of the array
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource(LISTS)
	public void testToSetHoldsEachElementOnceInTheOrderOfTheArray(final List<String> list)
	{
		final Set<String> expected = new LinkedHashSet<>(list);
		final Set<String> actual = ArrayExtensions.toSet(array(list));
		assertEquals(expected, actual);
		assertEquals(new ArrayList<>(expected), new ArrayList<>(actual));
	}

	/**
	 * Property of {@link ArrayExtensions#splitInChunks(byte[], int)}: "Splits the given byte array
	 * in to consecutive chunks of the given size, in the order of the bytes; the last chunk may be
	 * smaller. An empty array gives no chunk", "@throws IllegalArgumentException if the chunk size
	 * is less than 1", for random bytes of the seed (shown in the name) and every {@code int} edge
	 * case as the chunk size
	 *
	 * @param seed
	 *            the seed of the random bytes
	 * @param chunkSize
	 *            the chunk size
	 */
	@ParameterizedTest(name = "seed {0}, chunk size {1}")
	@MethodSource("seedsWithChunkSize")
	public void testSplitInChunks(final long seed, final int chunkSize)
	{
		final Random random = EdgeCases.random(seed);
		final byte[] bytes = new byte[random.nextInt(40)];
		random.nextBytes(bytes);
		if (chunkSize < 1)
		{
			assertThrows(IllegalArgumentException.class,
				() -> ArrayExtensions.splitInChunks(bytes, chunkSize));
			return;
		}
		final byte[][] chunks = ArrayExtensions.splitInChunks(bytes, chunkSize);
		final String message = "seed " + seed + ", " + bytes.length + " bytes, chunk size "
			+ chunkSize;
		assertEquals(bytes.length == 0 ? 0 : (bytes.length - 1) / (long)chunkSize + 1,
			chunks.length, message);
		final java.io.ByteArrayOutputStream joined = new java.io.ByteArrayOutputStream();
		for (int i = 0; i < chunks.length; i++)
		{
			if (i < chunks.length - 1)
			{
				assertEquals(chunkSize, chunks[i].length, message);
			}
			joined.writeBytes(chunks[i]);
		}
		assertArrayEquals(bytes, joined.toByteArray(), message);
	}

}
