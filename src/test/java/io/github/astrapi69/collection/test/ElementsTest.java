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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Test class for {@link Elements}
 */
public class ElementsTest
{

	/**
	 * Test for the methods {@link Elements#list(String)}, {@link Elements#modifiableList(String)}
	 * and {@link Elements#array(String)}: the words of the column in the order of the column, none
	 * for an empty or a blank column, the word null as the string "null", a duplicate kept
	 *
	 * @param description
	 *            the name of the case
	 * @param column
	 *            the column
	 * @param expected
	 *            the words in the order of the column, separated by a comma
	 */
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = { "empty column,'',''", "blank column of one space,' ',''",
			"blank column of three spaces,'   ',''", "one word,a,a", "two words,a b,'a,b'",
			"duplicate kept,a b a,'a,b,a'", "order of the column kept,c b a,'c,b,a'",
			"the word null is a string,a null b,'a,null,b'", "the word null alone,null,null" })
	void wordsOfAColumn(final String description, final String column, final String expected)
	{
		final List<String> words = expected.isEmpty() ? List.of() : List.of(expected.split(","));

		assertEquals(words, Elements.list(column));
		assertEquals(words, Elements.modifiableList(column));
		assertArrayEquals(words.toArray(new String[0]), Elements.array(column));
	}

	/**
	 * Test for the method {@link Elements#list(String)}: the list can not be changed, also not an
	 * empty one
	 *
	 * @param column
	 *            the column
	 */
	@ParameterizedTest(name = "[{index}] ''{0}''")
	@CsvSource(value = { "''", "' '", "a", "a b" })
	void listIsUnmodifiable(final String column)
	{
		final List<String> list = Elements.list(column);

		assertThrows(UnsupportedOperationException.class, () -> list.add("x"));
		assertThrows(UnsupportedOperationException.class, () -> list.addAll(List.of()));
	}

	/**
	 * Test for the method {@link Elements#modifiableList(String)}: the list can be changed, also an
	 * empty one, and it is an {@link ArrayList}
	 *
	 * @param column
	 *            the column
	 */
	@ParameterizedTest(name = "[{index}] ''{0}''")
	@CsvSource(value = { "''", "' '", "a", "a b" })
	void modifiableListCanBeChanged(final String column)
	{
		final List<String> list = Elements.modifiableList(column);
		final int size = list.size();

		list.add("x");

		assertEquals(ArrayList.class, list.getClass());
		assertEquals(size + 1, list.size());
		assertEquals("x", list.get(size));
	}

	/**
	 * Test for the method {@link Elements#array(String)}: every call gives a new array
	 *
	 * @param column
	 *            the column
	 */
	@ParameterizedTest(name = "[{index}] ''{0}''")
	@CsvSource(value = { "''", "' '", "a b" })
	void arrayIsNew(final String column)
	{
		final String[] array = Elements.array(column);

		Arrays.fill(array, "x");

		assertEquals(Elements.array(column).length, array.length);
		assertEquals(0, Arrays.stream(Elements.array(column)).filter("x"::equals).count());
	}

	/**
	 * Test for the method {@link Elements#listWithNull(String)}: the word null is a null element,
	 * the other words are kept, none for an empty or a blank column, the list can not be changed
	 *
	 * @param description
	 *            the name of the case
	 * @param column
	 *            the column
	 * @param expected
	 *            the words, separated by a comma, the word null for a null element
	 */
	@ParameterizedTest(name = "{0}")
	@CsvSource(value = { "empty column,'',''", "blank column,' ',''", "one word,a,a",
			"the word null alone,null,null", "the word null between words,a null b,'a,null,b'",
			"the word null twice,null null,'null,null'",
			"a word that contains null,nullable,nullable" })
	void listWithNullReadsTheWordNullAsNull(final String description, final String column,
		final String expected)
	{
		final List<String> words = new ArrayList<>();
		if (!expected.isEmpty())
		{
			for (final String word : expected.split(","))
			{
				words.add("null".equals(word) ? null : word);
			}
		}

		final List<String> actual = Elements.listWithNull(column);

		assertEquals(words, actual);
		assertThrows(UnsupportedOperationException.class, () -> actual.add("x"));
	}

	/**
	 * Test for the four methods: a null column throws a {@link NullPointerException}
	 */
	@Test
	void nullColumnThrows()
	{
		assertThrows(NullPointerException.class, () -> Elements.list(null));
		assertThrows(NullPointerException.class, () -> Elements.listWithNull(null));
		assertThrows(NullPointerException.class, () -> Elements.modifiableList(null));
		assertThrows(NullPointerException.class, () -> Elements.array(null));
	}

}
