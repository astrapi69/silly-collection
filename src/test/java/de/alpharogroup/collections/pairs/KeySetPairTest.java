package de.alpharogroup.collections.pairs;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Set;

import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import de.alpharogroup.collections.set.SetExtensions;
import de.alpharogroup.test.objects.Customer;
import de.alpharogroup.test.objects.Employee;
import de.alpharogroup.test.objects.Person;
import de.alpharogroup.test.objects.evaluations.EqualsHashCodeAndToStringEvaluator;

/**
 * The unit test class for the class {@link KeySetPair}.
 */
public class KeySetPairTest
{

	/**
	 * Test for method {@link KeySetPair#builder()}
	 */
	@Test
	public void testKeyValuesPairWithSet()
	{
		final Person person = Person.builder().name("John").married(Boolean.FALSE).build();
		final Set<Employee> employees = SetExtensions.newHashSet();
		employees.add(Employee.builder().id("10").build());
		employees.add(Employee.builder().id("20").build());
		final Employee employee = Employee.builder().id("20").build();
		final KeySetPair<Person, Employee> kvp = KeySetPair.<Person, Employee> builder()
			.key(person).values(employees).value(employee).build();
		assertEquals(person, kvp.getKey());
		assertTrue(kvp.getValues().size() == 2);
	}

	/**
	 * Test method for {@link KeySetPair#equals(Object)} , {@link KeySetPair#hashCode()} and {@link KeySetPair#toString()}
	 */
	@Test
	public void testEqualsHashcodeAndToString() {

		boolean expected;
		boolean actual;
		final Person person = Person.builder().name("John").married(Boolean.FALSE).build();
		final Set<Employee> employees = SetExtensions.newHashSet();
		employees.add(Employee.builder().id("10").build());
		employees.add(Employee.builder().id("20").build());
		final Employee employee = Employee.builder().id("20").build();
		final KeySetPair<Person, Employee> first = KeySetPair.<Person, Employee> builder()
			.key(person).values(employees).value(employee).build();

		final Set<Customer> customers = SetExtensions.newHashSet();
		final Customer customer = Customer.builder().name("jim").build();
		final KeySetPair<Person, Customer> second = KeySetPair.<Person, Customer> builder()
			.key(person).values(customers).value(customer).build();


		final KeySetPair<Person, Employee> third = KeySetPair.<Person, Employee> builder()
			.key(person).values(employees).value(employee).build();


		final KeySetPair<Person, Employee> fourth = KeySetPair.<Person, Employee> builder()
			.key(person).values(employees).value(employee).build();

		actual = EqualsHashCodeAndToStringEvaluator.evaluateEqualsHashcodeAndToString(first, second, third, fourth);
		expected = true;
		assertEquals(expected, actual);
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
