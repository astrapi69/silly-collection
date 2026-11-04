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
 * A list iterator that walks over a list and removes from it as the list iterator it wraps does,
 * and refuses {@link #set(Object)} and {@link #add(Object)} with an
 * {@link UnsupportedOperationException}.
 * <p>
 * The views that return it, {@link ReadAndRemoveSubList}, read and remove only, and refuse set and
 * add whatever the arguments, as the view refuses its own set and add methods; the reasons are on
 * {@link ReadAndRemoveSubList}. A removal keeps the order and the uniqueness, so it is allowed. The
 * list iterators of {@link SortedList} and {@link SortedUniqueList} themselves set through the
 * list, {@link AddRefusingListIterator}.
 * <p>
 * It is fail-fast as the wrapped iterator is: the iterator of an {@link java.util.ArrayList} throws
 * a {@link java.util.ConcurrentModificationException} after a structural modification of the list
 * that it did not make itself.
 *
 * @param <E>
 *            the generic type of the elements
 */
final class ReadAndRemoveListIterator<E> implements ListIterator<E>
{

	/**
	 * The list iterator this one walks and removes with
	 */
	private final ListIterator<E> iterator;

	/**
	 * The message of the {@link UnsupportedOperationException} of set and add
	 */
	private final String refusal;

	/**
	 * Constructs a list iterator that walks and removes with the given one and refuses set and add
	 * with the given message
	 *
	 * @param iterator
	 *            the list iterator to walk and remove with
	 * @param refusal
	 *            the message of the {@link UnsupportedOperationException} of set and add, which
	 *            says why the list refuses them
	 */
	ReadAndRemoveListIterator(ListIterator<E> iterator, String refusal)
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
	 * Refuses to replace the element last returned: the list decides where an element goes, not the
	 * position of this iterator
	 *
	 * @param element
	 *            ignored
	 * @throws UnsupportedOperationException
	 *             always
	 */
	@Override
	public void set(E element)
	{
		throw new UnsupportedOperationException(refusal);
	}

	/**
	 * Refuses to insert an element before the cursor: the list decides where an element goes, not
	 * the position of this iterator
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
