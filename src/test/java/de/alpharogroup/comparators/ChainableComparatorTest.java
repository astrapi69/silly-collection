package de.alpharogroup.comparators;

import static org.testng.Assert.assertTrue;

import java.util.BitSet;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.beanutils.BeanComparator;
import org.testng.annotations.Test;

import de.alpharogroup.collections.list.ListExtensions;
import de.alpharogroup.test.objects.Employee;
import de.alpharogroup.test.objects.Person;

/**
 * The unit test class for the class {@link ChainableComparator}.
 */
public class ChainableComparatorTest
{
	boolean expected;
	int actual;
	ChainableComparator<Employee> comparator;


	/**
	 * Test method for {@link ChainableComparator#compare(Locale, Locale)}
	 */
	@Test
	public void testCompare()
	{

	    final BitSet bitSet = new BitSet();
	    bitSet.set(0);
        bitSet.set(1);

		final Comparator<Employee> idComparator = new BeanComparator<>("id");
		final Comparator<Employee> personComparator = new BeanComparator<>("person");

		final List<Comparator<Employee>> comparators = ListExtensions.newArrayList(idComparator,
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
