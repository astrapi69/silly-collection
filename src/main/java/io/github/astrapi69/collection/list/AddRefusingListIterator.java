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

import java.util.ListIterator;

/**
 * The list iterator of {@link SortedList} and {@link SortedUniqueList}: it walks, removes and sets
 * as the list iterator of {@link java.util.ArrayList#listIterator(int)} it wraps does, and refuses
 * {@link #add(Object)} with an {@link UnsupportedOperationException}.
 * <p>
 * The set of the wrapped iterator replaces the element last returned through the
 * {@code set(int, E)} of the list, at the index of that element, as the list iterator of
 * {@link java.util.AbstractList#listIterator()} does, so that it has the semantics of the set of
 * the list: a sorted list replaces in place where the element keeps the order and throws where it
 * does not, and a {@link SortedUniqueList} without a comparator swaps an element it holds at
 * another index. That set is no structural modification, so the cursor stays where it is.
 * <p>
 * The add of a list iterator inserts the element immediately before the cursor, as the contract of
 * {@link ListIterator} says. A sorted list decides itself where an element goes: the order of its
 * comparator determines the position of its elements, so explicit positioning is not supported, as
 * {@link java.util.TreeSet#addFirst(Object)} says, and without a comparator its add methods append
 * at the end. The cursor would no longer stand in front of the element {@code next()} returns, and
 * {@code next()} would skip an element or return one twice.
 * <p>
 * It is fail-fast as the wrapped iterator is: the list iterator of an {@link java.util.ArrayList}
 * throws a {@link java.util.ConcurrentModificationException} after a structural modification of the
 * list that it did not make itself.
 *
 * @param <E>
 *            the generic type of the elements
 */
final class AddRefusingListIterator<E> implements ListIterator<E>
{

	/**
	 * The list iterator this one walks, removes and sets with
	 */
	private final ListIterator<E> iterator;

	/**
	 * The message of the {@link UnsupportedOperationException} of add
	 */
	private final String refusal;

	/**
	 * Constructs a list iterator that walks, removes and sets with the given one and refuses add
	 * with the given message
	 *
	 * @param iterator
	 *            the list iterator of {@link java.util.ArrayList} to walk, remove and set with
	 * @param refusal
	 *            the message of the {@link UnsupportedOperationException} of add, which says why
	 *            the list refuses it
	 */
	AddRefusingListIterator(ListIterator<E> iterator, String refusal)
	{
		this.iterator = iterator;
		this.refusal = refusal;
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
		return iterator.next();
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
		return iterator.previous();
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
	}

	/**
	 * Replaces the element last returned by {@code next()} or {@code previous()} through the
	 * {@code set(int, E)} of the list at the index of that element, as the wrapped list iterator
	 * does: with the result and the exceptions of that set. Without an element returned since the
	 * last remove it throws an {@link IllegalStateException}, and after a structural modification
	 * of the list that this iterator did not make a
	 * {@link java.util.ConcurrentModificationException}
	 *
	 * @param element
	 *            the element to store
	 */
	@Override
	public void set(E element)
	{
		iterator.set(element);
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
