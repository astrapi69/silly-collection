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

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ListIterator;
import java.util.function.IntSupplier;

/**
 * The list iterator of {@link SortedList} and {@link SortedUniqueList}: it walks and removes as the
 * list iterator of {@link java.util.ArrayList#listIterator(int)} it wraps does, sets through the
 * {@code set(int, E)} of the list, and refuses {@link #add(Object)} with an
 * {@link UnsupportedOperationException}.
 * <p>
 * It keeps the index of the element {@code next()} or {@code previous()} returned last, and its
 * {@link #set(Object)} calls {@code set(int, E)} of the list with that index itself, as the list
 * iterator of {@link java.util.AbstractList} does, whose
 * {@link java.util.AbstractList#listIterator() listIterator} "relies on the backing list's
 * {@code set(int, E)}" method. So the set of this iterator has the semantics of the set of the
 * list: a sorted list with a comparator throws an {@link UnsupportedOperationException}, one
 * without a comparator replaces in place, and a {@link SortedUniqueList} without a comparator swaps
 * an element it holds at another index. It does not rely on the set of the list iterator of
 * {@link java.util.ArrayList} calling the set of the list, which ArrayList does but does not
 * specify.
 * <p>
 * The add of a list iterator inserts the element immediately before the cursor, as the contract of
 * {@link ListIterator} says. A sorted list decides itself where an element goes: the order of its
 * comparator determines the position of its elements, so explicit positioning is not supported, as
 * {@link java.util.TreeSet#addFirst(Object)} says, and without a comparator its add methods append
 * at the end. The cursor would no longer stand in front of the element {@code next()} returns, and
 * {@code next()} would skip an element or return one twice.
 * <p>
 * It is fail-fast as the wrapped iterator is: the list iterator of an {@link java.util.ArrayList}
 * throws a {@link ConcurrentModificationException} after a structural modification of the list that
 * it did not make itself, and so does the set of this iterator, which compares the modification
 * count of the list with the one it expects.
 *
 * @param <E>
 *            the generic type of the elements
 */
final class AddRefusingListIterator<E> implements ListIterator<E>
{

	/**
	 * The list iterator of {@link java.util.ArrayList} this one walks and removes with
	 */
	private final ListIterator<E> iterator;

	/**
	 * The list this iterator runs over, whose {@code set(int, E)} the set of this iterator calls
	 */
	private final List<E> list;

	/**
	 * Gives the modification count of the list
	 */
	private final IntSupplier modCount;

	/**
	 * The message of the {@link UnsupportedOperationException} of add
	 */
	private final String refusal;

	/**
	 * The index of the element {@code next()} or {@code previous()} returned last, -1 if there is
	 * none or it was removed
	 */
	private int lastReturned = -1;

	/**
	 * The modification count of the list this iterator expects, as it changed the list last
	 */
	private int expectedModCount;

	/**
	 * Constructs a list iterator that walks and removes with the given one, sets through the set of
	 * the given list, and refuses add with the given message
	 *
	 * @param iterator
	 *            the list iterator of {@link java.util.ArrayList} over the given list to walk and
	 *            remove with
	 * @param list
	 *            the list the iterator runs over
	 * @param modCount
	 *            gives the modification count of the list
	 * @param refusal
	 *            the message of the {@link UnsupportedOperationException} of add, which says why
	 *            the list refuses it
	 */
	AddRefusingListIterator(ListIterator<E> iterator, List<E> list, IntSupplier modCount,
		String refusal)
	{
		this.iterator = iterator;
		this.list = list;
		this.modCount = modCount;
		this.refusal = refusal;
		this.expectedModCount = modCount.getAsInt();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext()
	{
		return iterator.hasNext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E next()
	{
		E element = iterator.next();
		lastReturned = iterator.previousIndex();
		return element;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasPrevious()
	{
		return iterator.hasPrevious();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E previous()
	{
		E element = iterator.previous();
		lastReturned = iterator.nextIndex();
		return element;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int nextIndex()
	{
		return iterator.nextIndex();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int previousIndex()
	{
		return iterator.previousIndex();
	}

	/**
	 * Removes the element last returned by {@code next()} or {@code previous()}, as the wrapped
	 * list iterator removes it
	 */
	@Override
	public void remove()
	{
		iterator.remove();
		lastReturned = -1;
		expectedModCount = modCount.getAsInt();
	}

	/**
	 * Replaces the element last returned by {@code next()} or {@code previous()} through
	 * {@code set(int, E)} of the list at the index of that element, with the result and the
	 * exceptions of that set
	 *
	 * @param element
	 *            the element to store
	 * @throws IllegalStateException
	 *             if neither {@code next()} nor {@code previous()} returned an element since the
	 *             iterator was made or since the last remove
	 * @throws ConcurrentModificationException
	 *             if the list was changed structurally other than through this iterator
	 */
	@Override
	public void set(E element)
	{
		if (lastReturned < 0)
		{
			throw new IllegalStateException();
		}
		if (modCount.getAsInt() != expectedModCount)
		{
			throw new ConcurrentModificationException();
		}
		list.set(lastReturned, element);
	}

	/**
	 * Refuses to insert an element before the cursor: the list decides itself where an element
	 * goes, so explicit positioning is not supported
	 *
	 * @param element
	 *            ignored
	 * @throws UnsupportedOperationException
	 *             always
	 */
	@Override
	public void add(E element)
	{
		throw new UnsupportedOperationException(refusal);
	}
}
