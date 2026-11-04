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
 * The class {@link SortedList} extends the {@link ArrayList} and overwrites all add-methods that
 * sorts the elements with the given comparator.
 *
 * @param <E>
 *            the generic type of values
 * @see ArrayList
 */
public class SortedList<E> extends ArrayList<E>
{

	/**
	 * the comparator for sort this list object
	 */
	private Comparator<? super E> comparator;

	/**
	 * Constructs an empty {@link SortedList} instance with the specified initial capacity
	 *
	 * @param initialCapacity
	 *            the initial capacity
	 */
	public SortedList(int initialCapacity)
	{
		super(initialCapacity);
	}

	/**
	 * Constructs an empty {@link SortedList} instance with an initial capacity of ten
	 */
	public SortedList()
	{
	}

	/**
	 * Constructs a {@link SortedList} instance and set the given comparator
	 *
	 * @param comparator
	 *            the comparator
	 */
	public SortedList(Comparator<? super E> comparator)
	{
		this.comparator = comparator;
	}

	/**
	 * Constructs a {@link SortedList} instance containing the elements of the given collection
	 *
	 * @param collection
	 *            the collection
	 */
	public SortedList(Collection<? extends E> collection)
	{
		addAll(collection);
	}

	/**
	 * Constructs a {@link SortedList} instance containing the elements of the given collection and
	 * set the given comparator
	 *
	 * @param collection
	 *            the collection
	 * @param comparator
	 *            the comparator
	 */
	public SortedList(Collection<? extends E> collection, Comparator<? super E> comparator)
	{
		this(comparator);
		addAll(collection);
	}

	/**
	 * Adds the given element at its place in the order of the comparator of this list, the index
	 * that {@link ListExtensions#getIndexToInsert(java.util.List, Object, Comparator)
	 * ListExtensions.getIndexToInsert} gives, or at the end of this list if it has no comparator.
	 * An element this list contains already is added again.
	 * <p>
	 * If this list holds elements that the comparator holds equal to the given one, the given
	 * element goes in front of the one of them that
	 * {@link Collections#binarySearch(java.util.List, Object, Comparator) Collections.binarySearch}
	 * finds, which can be any of them: it does not necessarily come after them, and such elements
	 * do not keep the order in which they were added. The place is not specified if this list is
	 * not in the order of its comparator, as after {@link #set(int, Object)}, or
	 * {@link #sort(Comparator)} with another comparator, which this list inherits from
	 * {@link ArrayList}.
	 *
	 * @param element
	 *            the element to add
	 * @return true, always: this list changes with every call
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
	 * Adds the given element as every add method of this class does: at its place in the order of
	 * the comparator of this list, or at its end if this list has no comparator. The given index is
	 * ignored and not checked; any index is accepted, a negative one too.
	 *
	 * @param index
	 *            ignored
	 * @param element
	 *            the element to add
	 */
	@Override
	public void add(int index, E element)
	{
		add(element);
	}

	/**
	 * Adds the elements of the given collection one by one, in the order of the collection's
	 * iterator, as {@link #add(Object)} adds each of them: each at its place in the order of the
	 * comparator of this list, or at the end of this list if it has no comparator. Elements this
	 * list contains already are added again.
	 *
	 * @param collection
	 *            the collection with the elements to add
	 * @return true if this list changed as a result of the call, that is if the collection is not
	 *         empty
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
	 * Adds the elements of the given collection as every add method of this class does: each at its
	 * place in the order of the comparator of this list, or at its end if this list has no
	 * comparator. The given index is ignored and not checked; any index is accepted, a negative one
	 * too.
	 *
	 * @param index
	 *            ignored
	 * @param collection
	 *            the collection with the elements to add
	 * @return true if this list changed as a result of the call
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
