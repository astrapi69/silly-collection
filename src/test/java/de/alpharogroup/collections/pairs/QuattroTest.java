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

import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import de.alpharogroup.evaluate.object.evaluators.EqualsHashCodeAndToStringEvaluator;
import de.alpharogroup.test.objects.Customer;
import de.alpharogroup.test.objects.Employee;
import de.alpharogroup.test.objects.Person;
import de.alpharogroup.test.objects.enums.Brands;

/**
 * The unit test class for the class {@link Quattro}.
 */
public class QuattroTest
{

	/**
	 * Test method for {@link Quattro#equals(Object)} , {@link Quattro#hashCode()} and
	 * {@link Quattro#toString()}
	 */
	@Test
	public void testEqualsHashcodeAndToString()
	{

		boolean expected;
		boolean actual;

		final Customer customer = Customer.builder().build();

		final Brands ferrari = Brands.FERRARI;

		final Person person = Person.builder().name("John").married(Boolean.FALSE).build();

		final Employee employee = Employee.builder().id("20").person(person).build();

		final Quattro<Integer, Employee, Customer, Brands> first = Quattro
			.<Integer, Employee, Customer, Brands> builder().topLeft(1).topRight(employee)
			.bottomLeft(customer).bottomRight(ferrari).build();
		final Quattro<String, Employee, Customer, Brands> second = Quattro
			.<String, Employee, Customer, Brands> builder().topLeft("left").topRight(employee)
			.bottomLeft(customer).bottomRight(ferrari).build();
		Integer topLeft = 1;
		final Quattro<Integer, Employee, Customer, Brands> third = new Quattro<>(customer, ferrari,
			topLeft, employee);
		final Quattro<Integer, Employee, Customer, Brands> fourth = new Quattro<>();
		fourth.setTopLeft(1);
		fourth.setTopRight(employee);
		fourth.setBottomLeft(customer);
		fourth.setBottomRight(ferrari);

		actual = EqualsHashCodeAndToStringEvaluator.evaluateEqualsHashcodeAndToString(first, second,
			third, fourth);
		expected = true;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link Quattro}
	 */
	@Test(enabled = false)
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(Quattro.class);
	}

}
