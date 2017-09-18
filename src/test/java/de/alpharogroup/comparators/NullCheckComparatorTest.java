package de.alpharogroup.comparators;

import static org.testng.AssertJUnit.*;

import java.util.Comparator;
import java.util.Locale;

import org.testng.annotations.Test;

public class NullCheckComparatorTest
{

	@Test
	public void testCompare()
	{
		int expected;
		int actual;
		Comparator<Locale> localeComparator = NullCheckComparator
			.<Locale> of(new LocaleComparator());
		actual = localeComparator.compare(Locale.CANADA, null);
		expected = 1;
		assertEquals(expected, actual);

		actual = localeComparator.compare(null, null);
		expected = 0;
		assertEquals(expected, actual);

		actual = localeComparator.compare(null, Locale.CANADA);
		expected = -1;
		assertEquals(expected, actual);

		// set null flag to true so null are greater...
		localeComparator = LocaleComparator.of(true);

		actual = localeComparator.compare(Locale.CANADA, null);
		expected = -1;
		assertEquals(expected, actual);

		actual = localeComparator.compare(null, null);
		expected = 0;
		assertEquals(expected, actual);

		actual = localeComparator.compare(null, Locale.CANADA);
		expected = 1;
		assertEquals(expected, actual);

	}

}
