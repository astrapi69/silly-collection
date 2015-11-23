/**
 * The MIT License
 *
 * Copyright (C) 2007 Asterios Raptis
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
package de.alpharogroup.comparators;

import lombok.experimental.ExtensionMethod;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import de.alpharogroup.test.objects.Person;

@ExtensionMethod({ ComparatorExtensions.class })
public class ComparatorExtensionsTest
{

	@Test
	public void testCompare()
	{
		final Person person = Person.builder().name("al").build();
		final Person otherPerson = Person.builder().name("bert").build();
		final int actual = person.compare(otherPerson);
		AssertJUnit.assertTrue(actual == -1);
	}

	@Test
	public void testCompareSortOrder()
	{
		final Person person = Person.builder().name("al").build();
		final Person otherPerson = Person.builder().name("bert").build();
		int actual = person.compare(otherPerson, SortOrder.DESCENDING);
		AssertJUnit.assertTrue(actual == 1);
		actual = person.compare(otherPerson, SortOrder.ASCENDING);
		AssertJUnit.assertTrue(actual == -1);
	}

	@Test
	public void testNullCheck()
	{
		Integer actual = ComparatorExtensions.nullCheck(Person.builder().build(), null);
		AssertJUnit.assertTrue(actual == 1);
		Person person = Person.builder().build();
		actual = person.nullCheck(null);
		AssertJUnit.assertTrue(actual == 1);
		final Person otherPerson = Person.builder().name("s").build();
		actual = person.nullCheck(otherPerson);
		AssertJUnit.assertTrue(actual == null);
		person = null;
		actual = person.nullCheck(null);
		AssertJUnit.assertTrue(actual == 0);
		actual = person.nullCheck(otherPerson);
		AssertJUnit.assertTrue(actual == -1);
	}

}
