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
package io.github.astrapi69.comparators;

import static org.testng.AssertJUnit.assertTrue;

import java.util.BitSet;
import java.util.Comparator;
import java.util.List;

import org.testng.annotations.Test;

import io.github.astrapi69.comparators.BeanPropertyComparator;
import io.github.astrapi69.test.objects.Employee;
import io.github.astrapi69.test.objects.Person;
import io.github.astrapi69.collections.list.ListFactory;

/**
 * The unit test class for the class {@link ChainableComparator}.
 */
public class ChainableComparatorTest
{

	/** For use of the result of the comparator. */
	int actual;

	/** The comparator. */
	ChainableComparator<Employee> comparator;

	/** For use of the expected result. */
	boolean expected;

	/**
	 * Test method for {@link ChainableComparator#compare(Object, Object)}
	 */
	@Test
	public void testCompare()
	{

		final BitSet bitSet = new BitSet();
		bitSet.set(0);
		bitSet.set(1);

		final Comparator<Employee> idComparator = new BeanPropertyComparator<>("id");
		final Comparator<Employee> personComparator = new BeanPropertyComparator<>("person");

		final List<Comparator<Employee>> comparators = ListFactory.newArrayList(idComparator,
			personComparator);

		final Person person1 = Person.builder().name("Alex").married(Boolean.FALSE).build();
		final Person person2 = Person.builder().name("John").married(Boolean.FALSE).build();

		final Employee employee1 = Employee.builder().person(person1).id("1").build();
		final Employee employee2 = Employee.builder().person(person2).id("2").build();

		comparator = ChainableComparator.<Employee> of();
		comparator.addComparator(idComparator);
		comparator.addComparator(personComparator);

		actual = comparator.compare(employee1, employee1);
		expected = actual == 0;
		assertTrue(expected);

		actual = comparator.compare(employee1, employee2);
		expected = actual < 0;
		assertTrue(expected);

		actual = comparator.compare(employee2, employee1);
		expected = actual > 0;
		assertTrue(expected);

		comparator = ChainableComparator.<Employee> of(idComparator);

		actual = comparator.compare(employee1, employee1);
		expected = actual == 0;
		assertTrue(expected);

		actual = comparator.compare(employee1, employee2);
		expected = actual < 0;
		assertTrue(expected);

		actual = comparator.compare(employee2, employee1);
		expected = actual > 0;
		assertTrue(expected);

		comparator = ChainableComparator.<Employee> of(comparators);

		actual = comparator.compare(employee1, employee1);
		expected = actual == 0;
		assertTrue(expected);

		actual = comparator.compare(employee1, employee2);
		expected = actual < 0;
		assertTrue(expected);

		actual = comparator.compare(employee2, employee1);
		expected = actual > 0;
		assertTrue(expected);

		comparator = ChainableComparator.<Employee> of(comparators, bitSet);

		actual = comparator.compare(employee1, employee1);
		expected = actual == 0;
		assertTrue(expected);

		actual = comparator.compare(employee1, employee2);
		expected = actual > 0;
		assertTrue(expected);

		actual = comparator.compare(employee2, employee1);
		expected = actual < 0;
		assertTrue(expected);

		comparator = ChainableComparator.<Employee> of(idComparator, true);

		actual = comparator.compare(employee1, employee1);
		expected = actual == 0;
		assertTrue(expected);

		actual = comparator.compare(employee1, employee2);
		expected = actual > 0;
		assertTrue(expected);

		actual = comparator.compare(employee2, employee1);
		expected = actual < 0;
		assertTrue(expected);
	}

}
