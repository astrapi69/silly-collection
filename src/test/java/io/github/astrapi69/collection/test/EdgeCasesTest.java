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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

/**
 * The unit test class for the class {@link EdgeCases}
 */
public class EdgeCasesTest
{

	/**
	 * The {@code int} edge cases that jqwik lists for {@code Arbitraries.integers()}
	 */
	@Test
	public void testInts()
	{
		assertEquals(List.of(-2, -1, 0, 1, 2, Integer.MIN_VALUE, Integer.MIN_VALUE + 1,
			Integer.MAX_VALUE - 1, Integer.MAX_VALUE), EdgeCases.ints().toList());
		assertEquals(9, EdgeCases.intValues().length);
	}

	/**
	 * The array of the {@code int} values is a copy
	 */
	@Test
	public void testIntValuesIsCopy()
	{
		final int[] values = EdgeCases.intValues();
		values[0] = 42;
		assertEquals(-2, EdgeCases.intValues()[0]);
	}

	/**
	 * Every pair of a range has its start less than or equal to its end, and all 45 are there
	 */
	@Test
	public void testIntPairs()
	{
		assertEquals(45, EdgeCases.intPairs().count());
		assertTrue(EdgeCases.intPairs().allMatch(pair -> (int)pair.get()[0] <= (int)pair.get()[1]));
		assertEquals(81, EdgeCases.allIntPairs().count());
	}

	/**
	 * The short and the too long ranges, with the first and the last of each
	 */
	@Test
	public void testShortAndTooLongIntPairs()
	{
		final List<String> shortPairs = EdgeCases.shortIntPairs()
			.map(pair -> pair.get()[0] + ".." + pair.get()[1]).collect(Collectors.toList());
		assertEquals(21, shortPairs.size());
		assertTrue(shortPairs.contains("-2..2"));
		assertTrue(shortPairs.contains(Integer.MAX_VALUE + ".." + Integer.MAX_VALUE));
		assertTrue(shortPairs.contains(Integer.MIN_VALUE + ".." + (Integer.MIN_VALUE + 1)));
		final List<String> tooLong = EdgeCases.tooLongIntPairs()
			.map(pair -> pair.get()[0] + ".." + pair.get()[1]).collect(Collectors.toList());
		assertTrue(tooLong.contains(Integer.MIN_VALUE + ".." + Integer.MAX_VALUE));
		assertTrue(tooLong.contains("0.." + Integer.MAX_VALUE));
		assertTrue(!tooLong.contains(Integer.MIN_VALUE + "..-2"));
	}

	/**
	 * The count of a range does not overflow
	 */
	@Test
	public void testRangeSize()
	{
		assertEquals(1L, EdgeCases.rangeSize(3, 3));
		assertEquals(4294967296L, EdgeCases.rangeSize(Integer.MIN_VALUE, Integer.MAX_VALUE));
	}

	/**
	 * The collections of strings: empty, one element, null, a duplicate, the reversed order
	 */
	@Test
	public void testStringLists()
	{
		final List<List<String>> lists = EdgeCases.stringLists().toList();
		assertTrue(lists.contains(List.of()));
		assertTrue(lists.contains(List.of("a")));
		assertTrue(lists.stream().anyMatch(list -> list.contains(null)));
		assertTrue(lists.contains(java.util.Arrays.asList("a", "b", "a")));
		assertTrue(lists.contains(List.of("a", "b", "c")));
		assertTrue(lists.contains(List.of("c", "b", "a")));
		assertTrue(lists.stream().noneMatch(list -> list.contains(EdgeCases.ABSENT)));
	}

	/**
	 * Each call makes new lists that a test may change
	 */
	@Test
	public void testStringListsAreNew()
	{
		final List<String> first = EdgeCases.stringLists().findFirst().orElseThrow();
		first.add("changed");
		assertEquals(List.of(), EdgeCases.stringLists().findFirst().orElseThrow());
		final List<List<String>> one = EdgeCases.stringLists().toList();
		final List<List<String>> other = EdgeCases.stringLists().toList();
		assertNotSame(one.get(1), other.get(1));
	}

	/**
	 * The lists with each element and the pairs of lists
	 */
	@Test
	public void testCrossProducts()
	{
		final long lists = EdgeCases.stringLists().count();
		assertEquals(lists * 5, EdgeCases.stringListsWithElement().count());
		assertEquals(lists * lists, EdgeCases.stringListPairs().count());
	}

	/**
	 * The same seed gives the same values, another seed other values
	 */
	@Test
	public void testRandom()
	{
		assertEquals(EdgeCases.random(7L).nextInt(), EdgeCases.random(7L).nextInt());
		assertTrue(EdgeCases.random(1L).nextLong() != EdgeCases.random(2L).nextLong());
		assertEquals(List.of(1L, 2L, 3L, 4L, 5L), EdgeCases.seeds().toList());
	}

}
