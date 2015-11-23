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
package de.alpharogroup.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class overwrites the put-method from the <tt>LinkedHashSet</tt>. That inserts the value to
 * the right order it was inserted in the Map. Note that the difference in the LinkedHashMap is the
 * order does not change if we put the same key with a new value. In this class the order changes
 * when we put a new value with the same key.
 * 
 * @author Asterios Raptis
 * @version 1.0
 * @param <E>
 *            the type of elements maintained by this set
 * @see java.util.LinkedHashSet
 */
public class InsertionOrderSet<E> extends LinkedHashSet<E>
{

	/**
	 * The serialVersionUID.
	 */
	private static final long serialVersionUID = -2010420135340052455L;

	/**
	 * Factory method for create an InsertionOrderSet with the given elements.
	 * 
	 * @param <E>
	 *            the generic type of the elements
	 * @param elements
	 *            The given elements.
	 * @return a new InsertionOrderSet that contains the given elements.
	 */
	@SuppressWarnings("unchecked")
	public static <E> Set<E> setOf(final E... elements)
	{
		return new InsertionOrderSet<>(Arrays.asList(elements));
	}

	/**
	 * Constructs an empty insertion-ordered <tt>InsertionOrderSet</tt> instance with a default
	 * capacity (16) and load factor (0.75).
	 */
	public InsertionOrderSet()
	{
		super();
	}

	/**
	 * Constructs a new insertion order hash set with the same elements as the specified collection.
	 * The insertion order hash set is created with an initial capacity sufficient to hold the
	 * elements in the specified collection and the default load factor (0.75).
	 * 
	 * @param c
	 *            the collection whose elements are to be placed into this set.
	 * @throws NullPointerException
	 *             if the specified collection is null.
	 */
	public InsertionOrderSet(final Collection<? extends E> c)
	{
		super(c);
	}

	/**
	 * Constructs an empty insertion-ordered <tt>InsertionOrderSet</tt> instance with the specified
	 * initial capacity and a default load factor (0.75).
	 * 
	 * @param initialCapacity
	 *            the initial capacity.
	 * @throws IllegalArgumentException
	 *             if the initial capacity is negative.
	 */
	public InsertionOrderSet(final int initialCapacity)
	{
		super(initialCapacity);
	}

	/**
	 * Constructs an empty insertion-ordered <tt>InsertionOrderMap</tt> instance with the specified
	 * initial capacity and load factor.
	 * 
	 * @param initialCapacity
	 *            the initial capacity.
	 * @param loadFactor
	 *            the load factor.
	 * @throws IllegalArgumentException
	 *             if the initial capacity is negative or the load factor is nonpositive.
	 */
	public InsertionOrderSet(final int initialCapacity, final float loadFactor)
	{
		super(initialCapacity, loadFactor);
	}

	/**
	 * Adds the specified object to this set. If the set previously contained a the same object, the
	 * old object is removed and then insert again. That inserts the object to the right order it
	 * was inserted in the Set. Note that the difference in the LinkedHashSet is the order does not
	 * change if we try to add the same object. In this class the order changes when we add a object
	 * that allready exists.
	 * 
	 * @param o
	 *            The object to add.
	 * @return true if the set did not already contain the specified element.
	 */
	@Override
	public boolean add(final E o)
	{
		if (super.contains(o))
		{
			super.remove(o);
		}
		return super.add(o);
	}

}
