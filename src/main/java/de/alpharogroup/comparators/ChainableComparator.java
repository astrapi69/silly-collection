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

import org.apache.commons.collections4.comparators.ComparatorChain;

import java.util.BitSet;
import java.util.Comparator;
import java.util.List;

/**
 * The class {@link ChainableComparator} extends the {@link ComparatorChain} and provides factory
 * methods for create {@link ChainableComparator} objects.
 *
 * @param <T>
 *            the generic type of the object that will be compared
 *
 * @see <code> ComparatorChain </code>
 */
public class ChainableComparator<T> extends ComparatorChain<T>
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Factory method to create a new chainable {@link Comparator} object.
	 *
	 * @param <T>
	 *            the generic type of the {@link Comparator} object that will be compared
	 *
	 * @return the new chainable {@link Comparator} object
	 *
	 * @see <code> ComparatorChain </code>
	 */
	public static <T> ChainableComparator<T> of()
	{
		return new ChainableComparator<T>();
	}

	/**
	 * Factory method to create a new chainable {@link Comparator} object from the given
	 * {@link Comparator} object.
	 *
	 * @param <T>
	 *            the generic type of the {@link Comparator} object that will be compared
	 * @param comparator
	 *            the {@link Comparator} object that will be in the chain
	 *
	 * @return the new chainable {@link Comparator} object
	 *
	 * @see <code> ComparatorChain </code>
	 */
	public static <T> ChainableComparator<T> of(final Comparator<T> comparator)
	{
		return new ChainableComparator<T>(comparator);
	}

	/**
	 * Factory method to create a new chainable {@link Comparator} object from the given
	 * {@link Comparator} object and the given reverse flag.
	 *
	 * @param <T>
	 *            the generic type of the {@link Comparator} object that will be compared
	 * @param comparator
	 *            the {@link Comparator} object that will be in the chain
	 * @param reverse
	 *            the reverse flag
	 *
	 * @return the new chainable {@link Comparator} object
	 *
	 * @see <code> ComparatorChain </code>
	 */
	public static <T> ChainableComparator<T> of(final Comparator<T> comparator,
		final boolean reverse)
	{
		return new ChainableComparator<T>(comparator, reverse);
	}

	/**
	 * Factory method to create a new chainable {@link Comparator} object from the given
	 * {@link List} of {@link Comparator} objects.
	 *
	 * @param <T>
	 *            the generic type of the {@link Comparator} object that will be compared
	 * @param list
	 *            the list with the {@link Comparator} objects.
	 *
	 * @return the new chainable {@link Comparator} object
	 *
	 * @see <code> ComparatorChain </code>
	 */
	public static <T> ChainableComparator<T> of(final List<Comparator<T>> list)
	{
		return new ChainableComparator<T>(list);
	}

	/**
	 * Factory method to create a new chainable {@link Comparator} object from the given
	 * {@link List} of {@link Comparator} objects.
	 *
	 * @param <T>
	 *            the generic type of the {@link Comparator} object that will be compared
	 * @param list
	 *            the list with the {@link Comparator} objects.
	 * @param bits
	 *            Sort order for each Comparator. Extra bits are ignored, unless extra Comparators
	 *            are added by another method.
	 *
	 * @return the new chainable {@link Comparator} object
	 *
	 * @see <code> ComparatorChain </code>
	 */
	public static <T> ChainableComparator<T> of(final List<Comparator<T>> list, final BitSet bits)
	{
		return new ChainableComparator<T>(list, bits);
	}

	/**
	 * Instantiates a new {@link ChainableComparator}.
	 */
	private ChainableComparator()
	{
	}

	/**
	 * Instantiates a new {@link ChainableComparator}.
	 *
	 * @param comparator
	 *            the comparator
	 */
	private ChainableComparator(final Comparator<T> comparator)
	{
		super(comparator);
	}

	/**
	 * Instantiates a new {@link ChainableComparator}.
	 *
	 * @param comparator
	 *            the comparator
	 * @param reverse
	 *            the reverse
	 */
	private ChainableComparator(final Comparator<T> comparator, final boolean reverse)
	{
		super(comparator, reverse);
	}

	/**
	 * Instantiates a new {@link ChainableComparator}.
	 *
	 * @param list
	 *            the list
	 */
	private ChainableComparator(final List<Comparator<T>> list)
	{
		super(list);
	}

	/**
	 * Instantiates a new {@link ChainableComparator}.
	 *
	 * @param list
	 *            the list
	 * @param bits
	 *            Sort order for each Comparator. Extra bits are ignored, unless extra Comparators
	 *            are added by another method.
	 */
	private ChainableComparator(final List<Comparator<T>> list, final BitSet bits)
	{
		super(list, bits);
	}

}
