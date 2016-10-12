package de.alpharogroup.collections.pairs;

import java.util.Collections;
import java.util.Set;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import de.alpharogroup.test.objects.Employee;
import de.alpharogroup.test.objects.Person;

public class KeyValuesPairTest
{

	@Test
	public void testKeyValuesPairBuilder()
	{
		final Person person = Person.builder().name("John").married(Boolean.FALSE).build();
		final Set<Employee> employees = Collections.emptySet();
		employees.add(Employee.builder().id("10").build());
		employees.add(Employee.builder().id("20").build());
		final Employee employee = Employee.builder().id("20").build();
		final KeyValuesPair<Person, Employee> kvp = KeyValuesPair
			.<Person, Employee>builder()
			.key(person)
			.values(employees)
			.value(employee)
			.build();
		AssertJUnit.assertEquals(person, kvp.getKey());
		AssertJUnit.assertTrue(kvp.getValues().size() == 3);
	}

}
