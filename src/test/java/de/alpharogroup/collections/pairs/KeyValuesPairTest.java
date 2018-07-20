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

import java.util.List;
import java.util.Set;

import org.testng.annotations.Test;

import de.alpharogroup.collections.list.ListFactory;
import de.alpharogroup.collections.set.SetFactory;
import de.alpharogroup.evaluate.object.evaluators.EqualsHashCodeAndToStringEvaluator;
import de.alpharogroup.test.objects.Customer;
import de.alpharogroup.test.objects.Employee;
import de.alpharogroup.test.objects.Person;

/**
 * The unit test class for the class {@link KeyValuesPair}.
 */
public class KeyValuesPairTest
{

	/**
	 * Test method for {@link KeyValuesPair#equals(Object)} , {@link KeyValuesPair#hashCode()} and
	 * {@link KeyValuesPair#toString()}
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
		final KeyValuesPair<Person, Employee> first = KeyValuesPair.<Person, Employee> builder()
			.key(person).values(employees).value(employee).build();

		final Set<Customer> customers = SetFactory.newHashSet();
		final Customer customer = Customer.builder().name("jim").build();
		final KeyValuesPair<Person, Customer> second = KeyValuesPair.<Person, Customer> builder()
			.key(person).values(customers).value(customer).build();


		final KeyValuesPair<Person, Employee> third = KeyValuesPair.<Person, Employee> builder()
			.key(person).values(employees).value(employee).build();


		final KeyValuesPair<Person, Employee> fourth = KeyValuesPair.<Person, Employee> builder()
			.key(person).values(employees).value(employee).build();

		actual = EqualsHashCodeAndToStringEvaluator.evaluateEqualsHashcodeAndToString(first, second,
			third, fourth);
		expected = true;
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link KeyValuesPair#builder()} with List as collection
	 */
	@Test
	public void testKeyValuesPairWithList()
	{
		final Person person = Person.builder().name("John").married(Boolean.FALSE).build();
		final List<Employee> employees = ListFactory.newArrayList();
		employees.add(Employee.builder().id("10").build());
		employees.add(Employee.builder().id("20").build());
		final Employee employee = Employee.builder().id("20").build();
		employees.add(employee);
		final KeyValuesPair<Person, Employee> kvp = new KeyValuesPair<>();
		kvp.setKey(person);
		kvp.setValues(employees);

		assertEquals(person, kvp.getKey());
		assertTrue(kvp.getValues().size() == 3);
	}

}
