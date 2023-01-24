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
 * The class {@link UniqueArrayList} extends the {@link ArrayList} and overwrites all add-methods
 * that checks if the element already exists in the list
 *
 * @param <E>
 *            the generic type of values
 * @see java.util.ArrayList
 */
public class UniqueArrayList<E> extends ArrayList<E>
{

	/**
	 * Constructs an empty {@link UniqueArrayList} instance with the specified initial capacity
	 *
	 * @param initialCapacity
	 *            the initial capacity
	 */
	public UniqueArrayList(int initialCapacity)
	{
		super(initialCapacity);
	}

	/**
	 * Constructs an empty {@link UniqueArrayList} instance with an initial capacity of ten
	 */
	public UniqueArrayList()
	{
	}

	/**
	 * Constructs a {@link UniqueArrayList} instance containing the elements of the given collection
	 *
	 * @param collection
	 *            the collection
	 * @throws NullPointerException
	 *             if the given collection is null
	 */
	public UniqueArrayList(Collection<? extends E> collection)
	{
		super(collection);
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
	 * {@inheritDoc}
	 */
	@Override
	public void add(int index, E element)
	{
		if (contains(element))
		{
			if (remove(element))
			{
				super.add(index, element);
			}
		}
		else
		{
			super.add(index, element);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends E> collection)
	{
		if (collection.size() == 0)
		{
			return false;
		}
		for (E element : collection)
		{
			this.add(element);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> collection)
	{
		if (collection.size() == 0)
		{
			return false;
		}
		int currentIndex = index;
		for (E element : collection)
		{
			if (currentIndex >= size())
			{
				this.add(element);
			}
			else
			{
				this.add(currentIndex, element);
			}
			currentIndex++;
		}
		return true;
	}
}
