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
import java.util.stream.Collectors;

/**
 * Turns a column of a {@link org.junit.jupiter.params.provider.CsvSource} row, the words separated
 * by a space, into a list or an array. For every method: an empty column and a blank column (one
 * space or more) give no elements, the word {@code null} is the string "null" unless the method
 * says otherwise, and a {@code null} column throws a {@link NullPointerException}
 * <p>
 * A stand-in for the converter of <a href=
 * "https://github.com/astrapi69/junit-jupiter-extensions/issues/13">astrapi69/junit-jupiter-extensions#13</a>:
 * once a release of junit-jupiter-extensions has that converter, the parameters that are lists or
 * arrays take it with {@code @ConvertWith} and this class is removed (#156)
 * </p>
 */
public final class Elements
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private Elements()
	{
	}

	/**
	 * The words of a column in an unmodifiable list
	 *
	 * @param column
	 *            the words separated by a space, empty or blank for no elements
	 * @return the words in an unmodifiable list, empty for an empty or a blank column, the word
	 *         null as the string "null"
	 */
	public static List<String> list(final String column)
	{
		return column.isEmpty() ? List.of() : List.of(column.split(" "));
	}

	/**
	 * The words of a column in an unmodifiable list, the word null as a null element
	 *
	 * @param column
	 *            the words separated by a space, empty or blank for no elements
	 * @return the words in an unmodifiable list, empty for an empty or a blank column, with null
	 *         for each word null
	 */
	public static List<String> listWithNull(final String column)
	{
		return list(column).stream().map(element -> "null".equals(element) ? null : element)
			.toList();
	}

	/**
	 * The words of a column in a new list that the test may change
	 *
	 * @param column
	 *            the words separated by a space, empty or blank for no elements
	 * @return the words in a new {@link ArrayList}, empty for an empty or a blank column, the word
	 *         null as the string "null"
	 */
	public static List<String> modifiableList(final String column)
	{
		return column.isEmpty()
			? new ArrayList<>()
			: Arrays.stream(column.split(" ")).collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * The words of a column in a new array
	 *
	 * @param column
	 *            the words separated by a space, empty or blank for no elements
	 * @return the words in a new array, of length 0 for an empty or a blank column, the word null
	 *         as the string "null"
	 */
	public static String[] array(final String column)
	{
		return column.isEmpty() ? new String[0] : column.split(" ");
	}

}
