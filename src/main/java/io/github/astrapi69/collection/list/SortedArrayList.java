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
package io.github.astrapi69.collection.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * The class {@link SortedArrayList} extends the {@link ArrayList} and overwrites all add-methods
 * that sorts the elements with the given comparator.
 *
 * @param <E>
 *            the generic type of values
 * @see ArrayList
 */
public class SortedArrayList<E> extends ArrayList<E>
{

	/**
	 * the comparator for sort this list object
	 */
	private Comparator<? super E> comparator;

	/**
	 * Constructs an empty {@link SortedArrayList} instance with the specified initial capacity
	 *
	 * @param initialCapacity
	 *            the initial capacity
	 */
	public SortedArrayList(int initialCapacity)
	{
		super(initialCapacity);
	}

	/**
	 * Constructs an empty {@link SortedArrayList} instance with an initial capacity of ten
	 */
	public SortedArrayList()
	{
	}

	/**
	 * Constructs a {@link SortedArrayList} instance and set the given comparator
	 *
	 * @param comparator
	 *            the comparator
	 */
	public SortedArrayList(Comparator<? super E> comparator)
	{
		this.comparator = comparator;
	}

	/**
	 * Constructs a {@link SortedArrayList} instance containing the elements of the given collection
	 * and set the given comparator
	 *
	 * @param collection
	 *            the collection
	 * @param comparator
	 *            the comparator
	 */
	public SortedArrayList(Collection<? extends E> collection, Comparator<? super E> comparator)
	{
		this(comparator);
		addAll(collection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(E element)
	{
		int currentSize = this.size();
		if (comparator == null)
		{
			return super.add(element);
		}
		ListExtensions.getIndexToInsert(this, element, comparator);
		int insertIndex = ListExtensions.getIndexToInsert(this, element, comparator);
		super.add(insertIndex, element);
		return this.size() != currentSize;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(int index, E element)
	{
		add(element);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends E> collection)
	{
		boolean result = false;
		for (E element : collection)
		{
			result |= add(element);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> collection)
	{
		return addAll(collection);
	}

	/**
	 * Gets the comparator of this list
	 * 
	 * @return the comparator of this list
	 */
	public Comparator<? super E> getComparator()
	{
		return comparator;
	}

	/**
	 * Sets the comparator of this list
	 * 
	 * @param comparator
	 *            the new comparator of this list
	 */
	public void setComparator(Comparator<? super E> comparator)
	{
		this.comparator = comparator;
		Collections.sort(this, comparator);
	}
}
