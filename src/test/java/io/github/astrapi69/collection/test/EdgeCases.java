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
package io.github.astrapi69.collection.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

/**
 * The inputs for the parameterized tests that check a property of a method for many inputs: the
 * edge cases of an {@code int}, of a collection of strings and of a range, and random values from a
 * fixed seed. It is a source for {@link org.junit.jupiter.params.provider.MethodSource}, for
 * example {@code @MethodSource("io.github.astrapi69.collection.test.EdgeCases#ints")}; each call of
 * a source makes new collections, so a test may change what it gets (#179)
 * <p>
 * The {@code int} edge cases are those that jqwik lists for {@code Arbitraries.integers()}, and
 * {@link Integer#MIN_VALUE} + 1, which jqwik does not list, as the counterpart of
 * {@link Integer#MAX_VALUE} - 1: "It's well-known that many programming bugs and specification gaps
 * happen at the border of allowed value ranges. For example, in the domain of integer numbers the
 * minimum (Integer.MIN_VALUE) and maximum (Integer.MAX_VALUE) belong in the set of those edge
 * cases", see the user guide <a href=
 * "https://jqwik.net/docs/current/user-guide.html">https://jqwik.net/docs/current/user-guide.html</a>,
 * "Generation of Edge Cases". The library itself is not used: it asks not to be used by AI coding
 * agents, and the family's randomizer 10.7 depends on silly-collection 28.1, an old copy of this
 * very library; random values come from {@link Random} with a fixed seed. Unlike a property-based
 * library it does not shrink: a failing case shows as it was generated
 * </p>
 */
public final class EdgeCases
{

	/**
	 * The {@code int} edge cases: -2, -1, 0, 1, 2, {@link Integer#MIN_VALUE},
	 * {@link Integer#MIN_VALUE} + 1, {@link Integer#MAX_VALUE} - 1 and {@link Integer#MAX_VALUE}
	 */
	private static final int[] INTS = { -2, -1, 0, 1, 2, Integer.MIN_VALUE, Integer.MIN_VALUE + 1,
			Integer.MAX_VALUE - 1, Integer.MAX_VALUE };

	/**
	 * The most numbers of a range that a test makes the array for, the longer ranges that an array
	 * can hold are left out
	 */
	public static final int SHORT_RANGE = 5;

	/**
	 * An element that none of the collections of {@link #stringLists()} holds
	 */
	public static final String ABSENT = "x";

	/**
	 * Private constructor to prevent instantiation
	 */
	private EdgeCases()
	{
	}

	/**
	 * The {@code int} edge cases
	 *
	 * @return a new array with -2, -1, 0, 1, 2, {@link Integer#MIN_VALUE},
	 *         {@link Integer#MIN_VALUE} + 1, {@link Integer#MAX_VALUE} - 1 and
	 *         {@link Integer#MAX_VALUE}
	 */
	public static int[] intValues()
	{
		return INTS.clone();
	}

	/**
	 * The {@code int} edge cases as the arguments of a test
	 *
	 * @return the edge cases, one argument each
	 */
	public static Stream<Integer> ints()
	{
		return Arrays.stream(INTS).boxed();
	}

	/**
	 * The pairs of {@code int} edge cases for a range: every start less than or equal to the end,
	 * so 45 pairs
	 *
	 * @return the pairs as start and end
	 */
	public static Stream<Arguments> intPairs()
	{
		return allIntPairs().filter(pair -> (int)pair.get()[0] <= (int)pair.get()[1]);
	}

	/**
	 * The pairs of {@code int} edge cases in both orders, the start also greater than the end, so
	 * 81 pairs
	 *
	 * @return the pairs as first and second
	 */
	public static Stream<Arguments> allIntPairs()
	{
		return Arrays.stream(INTS).boxed()
			.flatMap(first -> Arrays.stream(INTS).mapToObj(second -> Arguments.of(first, second)));
	}

	/**
	 * The numbers a range holds, counted in a {@code long} as {@code end - start + 1} overflows an
	 * {@code int} for a long range
	 *
	 * @param start
	 *            the start, inclusive
	 * @param end
	 *            the end, inclusive
	 * @return the count of numbers from the start to the end
	 */
	public static long rangeSize(final int start, final int end)
	{
		return (long)end - start + 1;
	}

	/**
	 * The pairs of {@link #intPairs()} with at most {@link #SHORT_RANGE} numbers: a test can make
	 * their array and check each number
	 *
	 * @return the pairs as start and end
	 */
	public static Stream<Arguments> shortIntPairs()
	{
		return intPairs()
			.filter(pair -> rangeSize((int)pair.get()[0], (int)pair.get()[1]) <= SHORT_RANGE);
	}

	/**
	 * The pairs of {@link #intPairs()} with more numbers than an array holds, more than
	 * {@link Integer#MAX_VALUE}
	 *
	 * @return the pairs as start and end
	 */
	public static Stream<Arguments> tooLongIntPairs()
	{
		return intPairs()
			.filter(pair -> rangeSize((int)pair.get()[0], (int)pair.get()[1]) > Integer.MAX_VALUE);
	}

	/**
	 * The collections of strings: empty, one element, a null element, a duplicate and the reversed
	 * order of a few elements. Each is a new list that a test may change
	 *
	 * @return the lists, one argument each
	 */
	public static Stream<List<String>> stringLists()
	{
		return Stream.of(list(), list("a"), list((String)null), list("a", null, "b"),
			list("a", "a"), list("a", "b", "a"), list("a", "b", "c"), list("c", "b", "a"),
			list(null, "a", null));
	}

	/**
	 * Every collection of {@link #stringLists()} with each of the elements a, b, c, null and
	 * {@link #ABSENT}, the last one in none of the lists
	 *
	 * @return the lists and the element as list and element
	 */
	public static Stream<Arguments> stringListsWithElement()
	{
		return stringLists().flatMap(list -> Stream.of("a", "b", "c", null, ABSENT)
			.map(element -> Arguments.of(list, element)));
	}

	/**
	 * Every collection of {@link #stringLists()} with every collection of it, as the first and the
	 * second
	 *
	 * @return the lists as one and other
	 */
	public static Stream<Arguments> stringListPairs()
	{
		final List<List<String>> lists = stringLists().toList();
		return lists.stream().flatMap(one -> lists.stream()
			.map(other -> Arguments.of(new ArrayList<>(one), new ArrayList<>(other))));
	}

	/**
	 * The fixed seeds of the random values, shown in the name of a test so that a failure can be
	 * rerun with {@link #random(long)}
	 *
	 * @return the seeds 1 to 5, one argument each
	 */
	public static Stream<Long> seeds()
	{
		return Stream.of(1L, 2L, 3L, 4L, 5L);
	}

	/**
	 * The random number generator for a seed
	 *
	 * @param seed
	 *            the seed, from {@link #seeds()}
	 * @return a new {@link Random} with the seed
	 */
	public static Random random(final long seed)
	{
		return new Random(seed);
	}

	/**
	 * A list of strings as {@link #stringLists()} makes them, with null elements
	 *
	 * @param elements
	 *            the elements
	 * @return a new list that a test may change
	 */
	private static List<String> list(final String... elements)
	{
		return new ArrayList<>(Arrays.asList(elements));
	}

}
