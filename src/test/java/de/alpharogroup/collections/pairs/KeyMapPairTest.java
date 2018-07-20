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

import java.util.HashMap;
import java.util.Map;

import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import de.alpharogroup.evaluate.object.evaluators.EqualsHashCodeAndToStringEvaluator;
import de.alpharogroup.test.objects.Customer;
import de.alpharogroup.test.objects.Employee;
import de.alpharogroup.test.objects.Person;

/**
 * The unit test class for the class {@link KeyMapPair}.
 */
public class KeyMapPairTest
{

	/**
	 * Test method for {@link KeyMapPair#equals(Object)} , {@link KeyMapPair#hashCode()} and
	 * {@link KeyMapPair#toString()}
	 */
	@Test
	public void testEqualsHashcodeAndToString()
	{


		final boolean expected;
		final boolean actual;
		final Person person = Person.builder().name("John").married(Boolean.FALSE).build();

		final Employee employee = Employee.builder().id("20").person(person).build();

		final Customer customer = Customer.builder().build();

		final Map<Employee, Customer> map1 = new HashMap<>();
		map1.put(employee, customer);

		final KeyMapPair<Integer, Employee, Customer> first = KeyMapPair
			.<Integer, Employee, Customer> builder().key(1).values(map1).build();

		final KeyMapPair<String, Employee, Customer> second = KeyMapPair
			.<String, Employee, Customer> builder().key("1").values(map1).build();

		final KeyMapPair<Integer, Employee, Customer> third = KeyMapPair
			.<Integer, Employee, Customer> builder().key(1).values(map1).build();

		final KeyMapPair<Integer, Employee, Customer> fourth = KeyMapPair
			.<Integer, Employee, Customer> builder().key(1).values(map1).build();

		actual = EqualsHashCodeAndToStringEvaluator.evaluateEqualsHashcodeAndToString(first, second,
			third, fourth);
		expected = true;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link KeyMapPair}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(KeyMapPair.class);
	}

}
