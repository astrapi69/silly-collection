package de.alpharogroup.comparators;

import java.util.Comparator;
import java.util.Locale;

/**
 * The class {@link LocaleComparator} compares {@linkplain Locale} objects. This Comparator does not
 * check null, for null check you can decorate it with the
 * {@link NullCheckComparator#of(Comparator)}. an example is in the unit test class from
 * {@linkplain NullCheckComparator}.
 */
public class LocaleComparator implements Comparator<Locale>
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(Locale o1, Locale o2)
	{
		return o1.toString().compareTo(o2.toString());
	}

	/**
	 * Factory method to create a new {@link LocaleComparator} object.
	 *
	 * @param nullIsGreaterThan
	 *            the flag that specifies if null objects is greater than non null objects.
	 * @return the new {@link LocaleComparator} object
	 */
	public static Comparator<Locale> of(boolean nullIsGreaterThan)
	{
		return NullCheckComparator.<Locale> of(new LocaleComparator(), nullIsGreaterThan);
	}

	/**
	 * Factory method to create a new {@link LocaleComparator} object.
	 *
	 * @return the new {@link LocaleComparator} object
	 */
	public static Comparator<Locale> of()
	{
		return LocaleComparator.of(false);
	}

}