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

/**
 * The class {@link UniqueList} extends the {@link ArrayList} and overwrites all add-methods that
 * checks if the element already exists in the list
 *
 * @param <E>
 *            the generic type of values
 * @see java.util.ArrayList
 */
public class UniqueList<E> extends ArrayList<E>
{

	/**
	 * Constructs an empty {@link UniqueList} instance with the specified initial capacity
	 *
	 * @param initialCapacity
	 *            the initial capacity
	 */
	public UniqueList(int initialCapacity)
	{
		super(initialCapacity);
	}

	/**
	 * Constructs an empty {@link UniqueList} instance with an initial capacity of ten
	 */
	public UniqueList()
	{
	}

	/**
	 * Constructs a {@link UniqueList} instance containing the elements of the given collection
	 *
	 * @param collection
	 *            the collection
	 */
	public UniqueList(Collection<? extends E> collection)
	{
		addAll(collection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(E element)
	{
		if (!contains(element))
		{
			return super.add(element);
		}
		return false;
	}

	/**
	 * Inserts the given element at the given index if this list does not contain it yet: the
	 * element at the index and the elements after it move one position to the right, as in
	 * {@link ArrayList#add(int, Object)}.
	 * <p>
	 * If this list contains the element already, the element moves instead: it is removed from its
	 * position and inserted again, so that it ends at the given index, or at the end of this list
	 * if the index is {@code size()}. The size of this list does not change then; {@code [a, b]}
	 * becomes {@code [b, a]} with {@code add(0, b)} and with {@code add(2, a)}.
	 *
	 * @param index
	 *            the index to insert the element at
	 * @param element
	 *            the element to insert or to move
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range ({@code index < 0 || index > size()}), checked as
	 *             {@link ArrayList#add(int, Object)} checks it, before this list changes
	 */
	@Override
	public void add(int index, E element)
	{
		rangeCheckForAdd(index);
		// an element this list contains leaves its position first; the list is one shorter then,
		// and the index size() it had before is its end
		remove(element);
		super.add(Math.min(index, size()), element);
	}

	/**
	 * Appends the elements of the given collection that this list does not contain yet to the end
	 * of this list, in the order of the collection's iterator and each of them once, as
	 * {@link #add(Object)} appends each of them. The elements this list contains already keep their
	 * positions.
	 *
	 * @param collection
	 *            the collection with the elements to append
	 * @return true if this list changed as a result of the call, that is if the collection holds an
	 *         element this list did not contain
	 */
	@Override
	public boolean addAll(Collection<? extends E> collection)
	{
		boolean changed = false;
		for (E element : collection)
		{
			changed |= add(element);
		}
		return changed;
	}

	/**
	 * Inserts the elements of the given collection that this list does not contain yet at the given
	 * index, in the order of the collection's iterator and each of them once: the element at the
	 * index and the elements after it move to the right, as in
	 * {@link ArrayList#addAll(int, Collection)}. The elements this list contains already keep their
	 * positions, unlike in {@link #add(int, Object)}, which moves such an element; {@code [x]}
	 * becomes {@code [a, b, x]} with {@code addAll(0, [a, x, a, b])}.
	 *
	 * @param index
	 *            the index to insert the first new element at
	 * @param collection
	 *            the collection with the elements to insert
	 * @return true if this list changed as a result of the call, that is if the collection holds an
	 *         element this list did not contain
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range ({@code index < 0 || index > size()}), checked as
	 *             {@link ArrayList#addAll(int, Collection)} checks it, before this list changes and
	 *             for an empty collection too
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> collection)
	{
		rangeCheckForAdd(index);
		int position = index;
		for (E element : collection)
		{
			if (!contains(element))
			{
				super.add(position++, element);
			}
		}
		return position != index;
	}

	/**
	 * Checks the given index of an insertion as {@link ArrayList} checks it, with the same message
	 *
	 * @param index
	 *            the index to insert at
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range ({@code index < 0 || index > size()})
	 */
	private void rangeCheckForAdd(int index)
	{
		if (index < 0 || index > size())
		{
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
		}
	}
}
