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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The unit test class for the class {@link IndexableSet}
 *
 * @author Asterios Raptis
 */
public class IndexableSetTest
{

	/**
	 * A new {@link IndexableSet} of the given elements, added in their order
	 *
	 * @param elements
	 *            the elements
	 * @return the set
	 */
	static IndexableSet<String> setOf(final String... elements)
	{
		final IndexableSet<String> set = new IndexableSet<>();
		for (final String element : elements)
		{
			set.add(element);
		}
		return set;
	}

	/**
	 * Asserts that {@link IndexableSet#get(int)} and {@link IndexableSet#getIndex(Object)} give the
	 * given elements at their index, that getIndex gives -1 for an element the set does not hold,
	 * and that get throws an {@link IndexOutOfBoundsException} for an index out of range, -1, the
	 * size and beyond it, as its Javadoc says
	 *
	 * @param expected
	 *            the elements in their order
	 * @param set
	 *            the set
	 */
	static void assertIndexes(final List<String> expected, final IndexableSet<String> set)
	{
		assertEquals(expected.size(), set.size(), "size");
		for (int index = 0; index < expected.size(); index++)
		{
			assertEquals(expected.get(index), set.get(index), "get(" + index + ")");
			assertEquals(index, set.getIndex(expected.get(index)),
				"getIndex(" + expected.get(index) + ")");
		}
		assertEquals(-1, set.getIndex("value5"), "getIndex of an element the set does not hold");
		for (final int index : new int[] { -1, expected.size(), expected.size() + 1 })
		{
			assertThrows(IndexOutOfBoundsException.class, () -> set.get(index),
				"get(" + index + ")");
		}
	}

	/**
	 * Test for the method {@link IndexableSet#get(int)} with an index out of range, -1, the size
	 * and beyond it (#151): an {@link IndexOutOfBoundsException} with the message of the index and
	 * the size
	 *
	 * @param description
	 *            the name of the case
	 * @param elements
	 *            the elements of the set, separated by a space, '' for an empty one
	 * @param index
	 *            the index
	 * @param message
	 *            the message of the exception
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = { "a, b: -1 | a b | -1 | Index -1 out of bounds for length 2",
			"a, b: 2, the size | a b | 2 | Index 2 out of bounds for length 2",
			"a, b: 3, beyond the size | a b | 3 | Index 3 out of bounds for length 2",
			"an empty set: 0 | '' | 0 | Index 0 out of bounds for length 0",
			"an empty set: -1 | '' | -1 | Index -1 out of bounds for length 0" }, delimiter = '|')
	public void testGetOutOfRangeMessage(final String description, final String elements,
		final int index, final String message)
	{
		final IndexableSet<String> set = setOf(
			elements.isEmpty() ? new String[0] : elements.split(" "));
		assertEquals(message,
			assertThrows(IndexOutOfBoundsException.class, () -> set.get(index)).getMessage());
	}

	/**
	 * A change of an {@link IndexableSet}, what it returns and the order of the elements afterwards
	 *
	 * @param description
	 *            the name of the case
	 * @param set
	 *            makes the set
	 * @param change
	 *            the change, null for none
	 * @param result
	 *            what the change returns, null for none
	 * @param expected
	 *            the elements in their order afterwards
	 */
	record IndexCase(String description, Supplier<IndexableSet<String>> set,
		Function<IndexableSet<String>, Boolean> change, Boolean result, List<String> expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testGetAndGetIndex(IndexCase)}
	 *
	 * @return the cases
	 */
	static Stream<IndexCase> indexes()
	{
		final Supplier<IndexableSet<String>> oneToFour = () -> setOf("value1", "value2", "value3",
			"value4");
		return Stream.of(
			new IndexCase("value1 to value4: in their order", oneToFour, null, null,
				List.of("value1", "value2", "value3", "value4")),
			new IndexCase("value1 to value4, value2 added again: false, value2 at the end",
				oneToFour, set -> set.add("value2"), false,
				List.of("value1", "value3", "value4", "value2")),
			new IndexCase("an empty set: no index", IndexableSet::new, null, null, List.of()),
			new IndexCase("null and a: null at 0, a at 1 (#41)", () -> setOf(null, "a"), null, null,
				Arrays.asList(null, "a")),
			new IndexCase("a, b, a added again: false, a at 1 (#41)", () -> setOf("a", "b"),
				set -> set.add("a"), false, List.of("b", "a")),
			new IndexCase("a, addAll of a: false, the set holds no new element (#41)",
				() -> setOf("a"), set -> set.addAll(List.of("a")), false, List.of("a")),
			new IndexCase("a, add of b: true, b at 1", () -> setOf("a"), set -> set.add("b"), true,
				List.of("a", "b")));
	}

	/**
	 * Test for the methods {@link IndexableSet#get(int)} and {@link IndexableSet#getIndex(Object)},
	 * also after {@link IndexableSet#add(Object)} and
	 * {@link IndexableSet#addAll(java.util.Collection)}, which it inherits from
	 * {@link InsertionOrderSet}: an element added again moves to the last index
	 *
	 * @param indexCase
	 *            the set, the change and the elements afterwards
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("indexes")
	public void testGetAndGetIndex(final IndexCase indexCase)
	{
		final IndexableSet<String> set = indexCase.set().get();
		if (indexCase.change() != null)
		{
			assertEquals(indexCase.result(), indexCase.change().apply(set));
		}
		assertIndexes(indexCase.expected(), set);
	}

}
