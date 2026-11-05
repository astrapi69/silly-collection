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
package io.github.astrapi69.collection.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An Iterator for an Array. It gives every element of the array, null elements too, in the order of
 * the array. {@link #remove()} removes the last element {@link #next()} gave from the array: as an
 * array keeps its length, the elements after it move one place to the front, and the array ends
 * with a null for each removed element, which the iteration does not give
 * 
 * @author Asterios Raptis
 * @param <E>
 *            Generic type of this {@link ArrayIterator}
 */
public class ArrayIterator<E> implements Iterator<E>
{

	/**
	 * The array.
	 */
	private final E[] array;

	/**
	 * The index of the element {@link #next()} gives next.
	 */
	private int pos = 0;

	/**
	 * The index of the element {@link #next()} gave last, -1 if {@link #next()} has not been called
	 * yet or {@link #remove()} has removed it (#118, as lastRet in the iterator of
	 * {@link java.util.ArrayList}).
	 */
	private int lastReturned = -1;

	/**
	 * The number of the elements {@link #remove()} has removed, so many places at the end of the
	 * array are no longer part of the iteration.
	 */
	private int removed = 0;

	/**
	 * Default constructor.
	 * 
	 * @param array
	 *            The array to iterate.
	 */
	public ArrayIterator(final E[] array)
	{
		this.array = array;
	}

	/**
	 * Gets the array, with the elements {@link #remove()} has removed taken out and as many nulls
	 * at its end
	 * 
	 * @return the array
	 */
	public E[] getArray()
	{
		return array;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext()
	{
		return this.pos < this.array.length - this.removed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E next() throws NoSuchElementException
	{
		if (!hasNext())
		{
			throw new NoSuchElementException("");
		}
		this.lastReturned = this.pos;
		this.pos = this.pos + 1;
		return this.array[this.lastReturned];
	}

	/**
	 * Removes from the array the last element returned by this iterator. This method can be called
	 * only once per call to {@link #next()}. The elements after the removed one move one place to
	 * the front, the place the last of them leaves becomes null, and {@link #next()} gives the
	 * element after the removed one next
	 *
	 * @throws IllegalStateException
	 *             if the {@code next} method has not yet been called, or the {@code remove} method
	 *             has already been called after the last call to the {@code next} method
	 */
	@Override
	public void remove()
	{
		if (this.lastReturned < 0)
		{
			throw new IllegalStateException(
				"The method next() was not invoked, or remove() was already invoked after the last next().");
		}
		final int newSize = this.array.length - this.removed - 1;
		System.arraycopy(this.array, this.lastReturned + 1, this.array, this.lastReturned,
			newSize - this.lastReturned);
		this.array[newSize] = null;
		this.removed = this.removed + 1;
		this.pos = this.lastReturned;
		this.lastReturned = -1;
	}
}
