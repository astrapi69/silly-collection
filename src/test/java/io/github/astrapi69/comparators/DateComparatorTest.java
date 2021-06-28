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
package io.github.astrapi69.comparators;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.testng.annotations.Test;

import io.github.astrapi69.BaseComparatorTestCase;
import io.github.astrapi69.collections.list.ListFactory;
import io.github.astrapi69.date.CreateDateExtensions;

/**
 * The unit test class for the class {@link DateComparator}
 */
public class DateComparatorTest extends BaseComparatorTestCase<Date>
{

	private final Date after = CreateDateExtensions.newDate(2010, 3, 27, 10, 37, 04);
	private final Date before = CreateDateExtensions.newDate(2010, 3, 26, 10, 37, 04);

	private final Date future = CreateDateExtensions.newDate(2011, 3, 27, 10, 37, 04);
	private final Date past = CreateDateExtensions.newDate(2009, 3, 26, 10, 37, 04);

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<Date> newActualList()
	{
		final List<Date> actual = ListFactory.newArrayList(before, future, past, after, null);
		return actual;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Comparator<Date> newComparator()
	{
		return new DateComparator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<Date> newExpectedSortedList()
	{
		final List<Date> expected = ListFactory.newArrayList(past, before, after, future);
		expected.add(0, null);
		return expected;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<Date> newExpectedUnsortedList()
	{
		final List<Date> expected = ListFactory.newArrayList(before, future, past, after, null);
		return expected;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Date newO1Equal()
	{
		return CreateDateExtensions.newDate(2009, 3, 26, 10, 37, 04);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Date newO1GreaterThan()
	{
		return CreateDateExtensions.newDate(2011, 3, 27, 10, 37, 04);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Date newO1LessThan()
	{
		return CreateDateExtensions.newDate(2009, 3, 26, 10, 37, 04);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Date newO2GreaterThan()
	{
		return CreateDateExtensions.newDate(2010, 3, 26, 10, 37, 04);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Date newO2LessThan()
	{
		return CreateDateExtensions.newDate(2010, 3, 26, 10, 37, 04);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Test
	public void testCompare()
	{
		super.testCompare();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Test
	public void testEqual()
	{
		super.testEqual();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Test
	public void testGreaterThan()
	{
		super.testGreaterThan();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Test
	public void testLessThan()
	{
		super.testLessThan();
	}

}
