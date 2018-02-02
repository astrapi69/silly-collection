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
package de.alpharogroup.comparators.pairs;

import static org.testng.Assert.assertTrue;

import java.util.Comparator;
import java.util.List;

import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import de.alpharogroup.collections.list.ListExtensions;
import de.alpharogroup.collections.pairs.KeyValuesPair;
import de.alpharogroup.test.objects.Employee;
import de.alpharogroup.test.objects.Person;

/**
 * The unit test class for the class {@link KeyValuesPairKeyComparator}.
 */
public class KeyValuesPairKeyComparatorTest
{

	/** For use of the expected result. */
	boolean expected;

	/** For use of the result of the comparator. */
	int actual;

	/**
	 * Test method for {@link KeyValuesPairKeyComparator#compare(Object, Object)}
	 */
	@Test
	public void testCompare()
	{
		final Person person = Person.builder().name("John").married(Boolean.FALSE).build();
		final Person person2 = Person.builder().name("Anton").married(Boolean.FALSE).build();
		final List<Employee> employees = ListExtensions.newArrayList();
		employees.add(Employee.builder().id("10").build());
		employees.add(Employee.builder().id("20").build());
		final Employee employee = Employee.builder().id("20").build();
		final KeyValuesPair<Person, Employee> o1 = KeyValuesPair.<Person, Employee> builder()
			.key(person).values(employees).value(employee).build();

		final List<Employee> employees2 = ListExtensions.newArrayList();
		employees2.add(Employee.builder().id("30").build());
		employees2.add(Employee.builder().id("40").build());
		final Employee employee2 = Employee.builder().id("30").build();
		final KeyValuesPair<Person, Employee> o2 = KeyValuesPair.<Person, Employee> builder()
			.key(person2).values(employees).value(employee2).build();

		final Comparator<KeyValuesPair<Person, Employee>> comparator = new KeyValuesPairKeyComparator<>();

		actual = comparator.compare(o1, o1);
		expected = actual == 0;
		assertTrue(expected);

		actual = comparator.compare(o1, o2);
		expected = 0 < actual;
		assertTrue(expected);

		actual = comparator.compare(o2, o1);
		expected = 0 > actual;
		assertTrue(expected);
	}

	/**
	 * Test method for {@link KeyValuesPairKeyComparator}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(KeyValuesPairKeyComparator.class);
	}

}