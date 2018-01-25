package de.alpharogroup.comparators;

import static org.testng.Assert.assertTrue;

import java.util.Comparator;
import java.util.Locale;

import org.testng.annotations.Test;

/**
 * The unit test class for the class {@link LocaleComparator}.
 */
public class LocaleComparatorTest
{

	boolean expected;
	int actual;

	/**
	 * Test method for {@link LocaleComparator#compare(Locale, Locale)}
	 */
	@Test
	public void testCompare()
	{
		final Comparator<Locale> comparator = LocaleComparator.of();

		actual = comparator.compare(Locale.CANADA, Locale.CANADA);
		expected = actual == 0;
		assertTrue(expected);

		actual = comparator.compare(Locale.CANADA, Locale.GERMAN);
		expected = actual > 0;
		assertTrue(expected);
	}

}
