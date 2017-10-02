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
package de.alpharogroup.comparators;

import java.io.Serializable;
import java.util.Comparator;

import de.alpharogroup.check.Check;
import lombok.Builder;
import lombok.EqualsAndHashCode;

/**
 * The class {@link NullCheckComparator} decorates another {@link Comparator} object to compare null
 * and non-null values. Before the decorated {@link Comparator} will be executed null check will be
 * executed. Over the flag 'nullIsGreaterThan' can be controlled whether null object values are
 * greater or less than non-null object values.
 *
 * @param <T>
 *            the generic type of the {@link Comparator} object that will be decorated
 */
@EqualsAndHashCode
@Builder(toBuilder = true)
public class NullCheckComparator<T> implements Comparator<T>, Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Factory method to create a new {@link NullCheckComparator} object from the given
	 * {@link Comparator} object.
	 *
	 * @param <T>
	 *            the generic type of the {@link Comparator} object that will be decorated
	 * @param decoratedComparator
	 *            the {@link Comparator} object that will be decorated
	 * @return the new decorated {@link Comparator} object
	 */
	public static <T> Comparator<T> of(Comparator<T> decoratedComparator)
	{
		return NullCheckComparator.<T> builder().decoratedComparator(decoratedComparator).build();
	}

	/**
	 * Factory method to create a new {@link NullCheckComparator} object from the given
	 * {@link Comparator} object.
	 *
	 * @param <T>
	 *            the generic type of the {@link Comparator} object that will be decorated
	 * @param decoratedComparator
	 *            the {@link Comparator} object that will be decorated
	 * @param nullIsGreaterThan
	 *            the flag that specifies if null objects is greater than non null objects.
	 * @return the new {@link NullCheckComparator} object
	 */
	public static <T> NullCheckComparator<T> of(Comparator<T> decoratedComparator,
		boolean nullIsGreaterThan)
	{
		return NullCheckComparator.<T> builder().decoratedComparator(decoratedComparator)
			.nullIsGreaterThan(nullIsGreaterThan).build();
	}

	/** The decorated comparator. */
	private final Comparator<T> decoratedComparator;

	/** The flag that specifies if null objects is greater than non null objects. */
	private final boolean nullIsGreaterThan;

	/**
	 * Instantiates a {@link NullCheckComparator} from the given {@link Comparator} object. The flag
	 * nullIsGreaterThan is set to false so null objects are smaller then non null objects.
	 *
	 * @param decoratedComparator
	 *            the {@link Comparator} object that will be decorated
	 */
	public NullCheckComparator(Comparator<T> decoratedComparator)
	{
		this(decoratedComparator, false);
	}

	/**
	 * Instantiates a {@link NullCheckComparator} from the given {@link Comparator} object and the
	 * given flag.
	 *
	 * @param decoratedComparator
	 *            the {@link Comparator} object that will be decorated
	 * @param nullIsGreaterThan
	 *            the flag that specifies if null objects is greater than non null objects.
	 */
	public NullCheckComparator(Comparator<T> decoratedComparator, boolean nullIsGreaterThan)
	{
		Check.get().notNull(decoratedComparator, "decoratedComparator");
		this.decoratedComparator = decoratedComparator;
		this.nullIsGreaterThan = nullIsGreaterThan;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(T object, T compareWithObject)
	{
		final Integer nullCheck = ComparatorExtensions.nullCheck(object, compareWithObject,
			this.nullIsGreaterThan);
		if (nullCheck != null)
		{
			return nullCheck;
		}
		return this.decoratedComparator.compare(object, compareWithObject);
	}

}
