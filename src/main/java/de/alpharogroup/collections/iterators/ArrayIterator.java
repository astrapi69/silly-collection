/**
 * The MIT License
 *
 * Copyright (C) 2007 Asterios Raptis
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
package de.alpharogroup.collections.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

import lombok.Getter;

/**
 * An Iterator for an Array.
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
	@Getter
	private final E[] array;

	/**
	 * The position.
	 */
	private int pos = 0;

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
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext()
	{
		return !(this.pos >= this.array.length || this.array[this.pos] == null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E next() throws NoSuchElementException
	{
		E next;
		try
		{
			next = this.array[this.pos];
		}
		catch (final RuntimeException e)
		{
			throw new NoSuchElementException("");
		}
		this.pos = this.pos + 1;
		return next;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove()
	{
		if (this.pos <= 0)
		{
			throw new IllegalStateException("The method next() was not invoked.");
		}
		if (this.array[this.pos - 1] != null)
		{
			for (int i = this.pos - 1; i < this.array.length - 1; i++)
			{
				this.array[i] = this.array[i + 1];
			}
			this.array[this.array.length - 1] = null;
		}
	}
}
