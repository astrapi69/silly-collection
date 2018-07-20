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
package de.alpharogroup.collections.pairs;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Set;

import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import de.alpharogroup.collections.set.SetFactory;
import de.alpharogroup.evaluate.object.evaluators.EqualsHashCodeAndToStringEvaluator;
import de.alpharogroup.test.objects.Customer;
import de.alpharogroup.test.objects.Employee;
import de.alpharogroup.test.objects.Person;

/**
 * The unit test class for the class {@link KeySetPair}.
 */
public class KeySetPairTest
{

	/**
	 * Test method for {@link KeySetPair#equals(Object)} , {@link KeySetPair#hashCode()} and
	 * {@link KeySetPair#toString()}
	 */
	@Test
	public void testEqualsHashcodeAndToString()
	{

		boolean expected;
		boolean actual;
		final Person person = Person.builder().name("John").married(Boolean.FALSE).build();
		final Set<Employee> employees = SetFactory.newHashSet();
		employees.add(Employee.builder().id("10").build());
		employees.add(Employee.builder().id("20").build());
		final Employee employee = Employee.builder().id("20").build();
		final KeySetPair<Person, Employee> first = KeySetPair.<Person, Employee> builder()
			.key(person).values(employees).value(employee).build();

		final Set<Customer> customers = SetFactory.newHashSet();
		final Customer customer = Customer.builder().name("jim").build();
		final KeySetPair<Person, Customer> second = KeySetPair.<Person, Customer> builder()
			.key(person).values(customers).value(customer).build();


		final KeySetPair<Person, Employee> third = KeySetPair.<Person, Employee> builder()
			.key(person).values(employees).value(employee).build();


		final KeySetPair<Person, Employee> fourth = KeySetPair.<Person, Employee> builder()
			.key(person).values(employees).value(employee).build();

		actual = EqualsHashCodeAndToStringEvaluator.evaluateEqualsHashcodeAndToString(first, second,
			third, fourth);
		expected = true;
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link KeySetPair#builder()}
	 */
	@Test
	public void testKeyValuesPairWithSet()
	{
		final Person person = Person.builder().name("John").married(Boolean.FALSE).build();
		final Set<Employee> employees = SetFactory.newHashSet();
		employees.add(Employee.builder().id("10").build());
		employees.add(Employee.builder().id("20").build());
		final Employee employee = Employee.builder().id("20").build();
		final KeySetPair<Person, Employee> kvp = KeySetPair.<Person, Employee> builder().key(person)
			.values(employees).value(employee).build();
		assertEquals(person, kvp.getKey());
		assertTrue(kvp.getValues().size() == 2);
	}

	/**
	 * Test method for {@link KeySetPair}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(KeySetPair.class);
	}
}
