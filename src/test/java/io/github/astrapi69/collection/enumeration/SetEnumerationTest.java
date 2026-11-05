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
package io.github.astrapi69.collection.enumeration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.iterator.IteratorSteps;

/**
 * The unit test class for the class {@link SetEnumeration}.
 *
 * @author Asterios Raptis
 */
public class SetEnumerationTest
{

	/**
	 * The elements of a row of a {@link CsvSource}
	 *
	 * @param elements
	 *            the elements, separated by a space, may be empty
	 * @return the elements
	 */
	static List<String> elements(final String elements)
	{
		return elements.isEmpty() ? List.of() : List.of(elements.split(" "));
	}

	/**
	 * Test for the constructor and the methods {@link SetEnumeration#hasMoreElements()} and
	 * {@link SetEnumeration#nextElement()}: the enumeration gives the elements of the set first,
	 * then those of the given enumeration that the set does not hold, each once; hasMoreElements()
	 * may be called more than once, nextElement() also without it, and after the last element
	 * hasMoreElements() is false and nextElement() throws a {@link NoSuchElementException}
	 *
	 * @param description
	 *            the name of the case
	 * @param set
	 *            the elements of the set, separated by a space, '' for an empty one
	 * @param enumeration
	 *            the elements of the enumeration, '' for an empty one, null for no enumeration
	 * @param expected
	 *            all elements the enumeration gives, those of the set first
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"the set and the enumeration of one to four: each once | one two three four | one two three four | one two three four",
			"an empty set and the enumeration of one to four: the four of the enumeration | '' | one two three four | one two three four",
			"the set a, b and the enumeration b, c: a and b, then c | a b | b c | a b c",
			"the set a and no enumeration: a | a | | a",
			"an empty set and no enumeration: none | '' | | ''",
			"an empty set and an empty enumeration: none | '' | '' | ''" }, delimiter = '|')
	public void testEnumeration(final String description, final String set,
		final String enumeration, final String expected)
	{
		final Set<String> setElements = new HashSet<>(elements(set));
		final Enumeration<String> enumerationElements = enumeration == null
			? null
			: new Vector<>(elements(enumeration)).elements();
		final SetEnumeration<String> setEnumeration = new SetEnumeration<>(setElements,
			enumerationElements);
		final List<String> actual = new ArrayList<>();
		final List<String> expectedElements = elements(expected);
		if (!expectedElements.isEmpty())
		{
			actual.add(setEnumeration.nextElement());
		}
		while (setEnumeration.hasMoreElements())
		{
			assertTrue(setEnumeration.hasMoreElements());
			actual.add(setEnumeration.nextElement());
		}
		assertFalse(setEnumeration.hasMoreElements());
		assertThrows(NoSuchElementException.class, setEnumeration::nextElement);
		assertEquals(expectedElements.size(), actual.size(), "each element once");
		assertEquals(Set.copyOf(expectedElements), Set.copyOf(actual));
		assertEquals(setElements, Set.copyOf(actual.subList(0, setElements.size())),
			"the elements of the set first");
	}

	/**
	 * Test for the methods {@link SetEnumeration#hasMoreElements()} and
	 * {@link SetEnumeration#nextElement()} step by step, also with null elements: steps on the
	 * enumeration through {@link Enumeration#asIterator()}, hasNext for hasMoreElements() and next
	 * for nextElement(), and what each gives, see {@link IteratorSteps}
	 *
	 * @param description
	 *            the name of the case
	 * @param set
	 *            the elements of the set in their order, separated by a space, null for a null
	 *            element
	 * @param enumeration
	 *            the elements of the enumeration, null for a null element, nothing for no
	 *            enumeration
	 * @param steps
	 *            the steps
	 * @param results
	 *            what each step gives
	 */
	@ParameterizedTest(name = "{0}", quoteTextArguments = false)
	@CsvSource(value = {
			"the set a and the enumeration a, b: more elements as often as asked, a, then b | a | a b | hasNext hasNext next next hasNext | true true a b false",
			"the set null, a and no enumeration: null, then a (#118) | null a | | hasNext next next hasNext | true null a false",
			"the set a and the enumeration null, b: a, then the null and b of the enumeration (#118) | a | null b | next next next hasNext | a null b false",
			"the set null and the enumeration null, b: null once, then b (#118) | null | null b | next next next hasNext | null b NoSuchElementException false" }, delimiter = '|')
	public void testSteps(final String description, final String set, final String enumeration,
		final String steps, final String results)
	{
		final SetEnumeration<String> setEnumeration = new SetEnumeration<>(
			new LinkedHashSet<>(IteratorSteps.elementsWithNull(set)),
			enumeration == null
				? null
				: new Vector<>(IteratorSteps.elementsWithNull(enumeration)).elements());
		assertEquals(IteratorSteps.elements(results),
			IteratorSteps.run(setEnumeration.asIterator(), steps));
	}

	/**
	 * Test method for {@link SetEnumeration}
	 */
	@Test
	@Disabled("add factory method to config beanTester")
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(SetEnumeration.class);
	}

}
