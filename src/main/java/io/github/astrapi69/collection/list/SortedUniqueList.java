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
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * The class {@link SortedUniqueList} extends the {@link ArrayList}, holds each element once and
 * keeps its elements in the order of its comparator: every method that puts an element into the
 * list checks, with equals, whether the list holds it already, and puts it at its place in that
 * order, the add methods whatever index they are given, and {@link #set(int, Object)} and
 * {@link #replaceAll(UnaryOperator)} too. What would put an element at a position the caller
 * chooses, or reorder the list against its comparator, throws an
 * {@link UnsupportedOperationException}: {@link #sort(Comparator)}, which
 * {@link Collections#sort(List)} calls, and the set and add of its list iterator.
 * {@link #setComparator(Comparator)} changes the order.
 * <p>
 * A list without a comparator has no order to keep: its add methods append at the end, and set and
 * replaceAll replace in place, as in {@link ArrayList}, with the same checks for an element the
 * list holds.
 *
 * @param <E>
 *            the generic type of values
 */
public class SortedUniqueList<E> extends ArrayList<E>
{

	/**
	 * The message of the {@link UnsupportedOperationException} of {@link #sort(Comparator)}
	 */
	private static final String SORT_REFUSAL = "A SortedUniqueList keeps the order of its"
		+ " comparator; setComparator(Comparator) changes it";

	/**
	 * The message of the {@link UnsupportedOperationException} of the set and add of the list
	 * iterator
	 */
	private static final String LIST_ITERATOR_REFUSAL = "A SortedUniqueList puts an element at"
		+ " its place in the order of its comparator, not at the position of a list iterator";

	/**
	 * The comparator for sorting this list object
	 */
	private Comparator<? super E> comparator;

	/**
	 * Constructs an empty {@link SortedUniqueList} instance with the specified initial capacity
	 *
	 * @param initialCapacity
	 *            the initial capacity
	 */
	public SortedUniqueList(int initialCapacity)
	{
		super(initialCapacity);
	}

	/**
	 * Constructs an empty {@link SortedUniqueList} instance with an initial capacity of ten
	 */
	public SortedUniqueList()
	{
	}

	/**
	 * Constructs a {@link SortedUniqueList} instance and sets the given comparator
	 *
	 * @param comparator
	 *            the comparator
	 */
	public SortedUniqueList(Comparator<? super E> comparator)
	{
		this.comparator = comparator;
	}

	/**
	 * Constructs a {@link SortedUniqueList} instance containing the elements of the given
	 * collection
	 *
	 * @param collection
	 *            the collection
	 */
	public SortedUniqueList(Collection<? extends E> collection)
	{
		addAll(collection);
	}

	/**
	 * Constructs a {@link SortedUniqueList} instance containing the elements of the given
	 * collection and sets the given comparator
	 *
	 * @param collection
	 *            the collection
	 * @param comparator
	 *            the comparator
	 */
	public SortedUniqueList(Collection<? extends E> collection, Comparator<? super E> comparator)
	{
		this(comparator);
		addAll(collection);
	}

	/**
	 * Adds the given element unless this list contains it already, by {@link #contains(Object)},
	 * which compares with equals: at its place in the order of the comparator of this list, the
	 * index that {@link ListExtensions#getIndexToInsert(java.util.List, Object, Comparator)
	 * ListExtensions.getIndexToInsert} gives, or at the end of this list if it has no comparator.
	 * An element this list contains already keeps its position.
	 * <p>
	 * An element that the comparator holds equal to elements of this list but equals does not is
	 * added: it goes in front of the one of them that
	 * {@link Collections#binarySearch(java.util.List, Object, Comparator) Collections.binarySearch}
	 * finds, which can be any of them, so that it does not necessarily come after them, and such
	 * elements do not keep the order in which they were added. The place is not specified if this
	 * list is not in the order of its comparator, as after a change through a view of
	 * {@link #subList(int, int)}, or after an element changed so that the comparator orders it
	 * elsewhere.
	 *
	 * @param element
	 *            the element to add
	 * @return true if the element was added, false if this list contains it already
	 */
	@Override
	public boolean add(E element)
	{
		if (!contains(element))
		{
			int insertIndex = comparator != null
				? ListExtensions.getIndexToInsert(this, element, comparator)
				: size();
			super.add(insertIndex, element);
			return true;
		}
		return false;
	}

	/**
	 * Adds the given element as every add method of this class does: unless this list contains it
	 * already, at its place in the order of the comparator of this list, or at its end if this list
	 * has no comparator. The given index is ignored and not checked; any index is accepted, a
	 * negative one too.
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
	 * Adds the elements of the given collection that this list does not contain yet, one by one in
	 * the order of the collection's iterator and each of them once, as {@link #add(Object)} adds
	 * each of them: each at its place in the order of the comparator of this list, or at the end of
	 * this list if it has no comparator. The elements this list contains already keep their
	 * positions; {@code list.addAll(list)} adds nothing and returns false.
	 *
	 * @param collection
	 *            the collection with the elements to add
	 * @return true if at least one element was added, otherwise false
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
	 * Adds the elements of the given collection as every add method of this class does: each one
	 * that this list does not contain yet at its place in the order of the comparator of this list,
	 * or at its end if this list has no comparator. The given index is ignored and not checked; any
	 * index is accepted, a negative one too.
	 *
	 * @param index
	 *            ignored
	 * @param collection
	 *            the collection with the elements to add
	 * @return true if at least one element was added, otherwise false
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> collection)
	{
		return addAll(collection);
	}

	/**
	 * Replaces the element at the given index with the given element so that this list holds each
	 * element once and stays in the order of its comparator: the element at the index is removed,
	 * and the given one is added as {@link #add(Object)} adds it, at its place in that order, which
	 * need not be the given index; {@code [a, c, e]} becomes {@code [c, d, e]} with
	 * {@code set(0, d)}. A sorted list cannot hold an element at an index of the caller's choice.
	 * <p>
	 * If this list holds the given element at another index, compared with equals as
	 * {@link #add(Object)} compares, this method throws an {@link IllegalArgumentException}, and
	 * this list does not change: it would hold the element twice. {@link UniqueList} swaps the two
	 * elements instead, which a sorted list cannot, as the order and not the index decides where an
	 * element goes; a list without a comparator refuses as well, so that set keeps its rules when
	 * {@link #setComparator(Comparator)} gives the list an order. An element equal to the one at
	 * the index itself replaces that one.
	 * <p>
	 * {@link Collections#swap(List, int, int)}, {@link Collections#reverse(List)},
	 * {@link Collections#shuffle(List, java.util.Random)} and {@link Collections#rotate(List, int)}
	 * call set with an element this list holds at another index, so they throw that exception
	 * before this list changes; a swap of an index with itself works.
	 * <p>
	 * The index is checked first, as {@link ArrayList#set(int, Object)} checks it, before this list
	 * changes and for an element this list holds too. If the comparator throws, as the natural
	 * order does for null, the removed element is put back at the index, and this list is as it
	 * was. As the element can move, set with a comparator is a structural modification: an iterator
	 * that runs over this list throws a {@link ConcurrentModificationException} at its next step.
	 * <p>
	 * Without a comparator this list has no order to keep: after the same checks, the element at
	 * the index is replaced, as in {@link ArrayList#set(int, Object)}, which is no structural
	 * modification.
	 *
	 * @param index
	 *            the index of the element to replace
	 * @param element
	 *            the element to sort in, or to store at the index if this list has no comparator
	 * @return the element previously at the index, as {@link java.util.List#set(int, Object)}
	 *         returns it
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range ({@code index < 0 || index >= size()}), with the
	 *             message of {@link ArrayList#set(int, Object)}
	 * @throws IllegalArgumentException
	 *             if this list holds the given element at another index
	 */
	@Override
	public E set(int index, E element)
	{
		Objects.checkIndex(index, size());
		int heldAt = indexOf(element);
		if (heldAt >= 0 && heldAt != index)
		{
			throw new IllegalArgumentException("This list holds the element " + element
				+ " at the index " + heldAt + ", it cannot hold it at the index " + index + " too");
		}
		if (comparator == null)
		{
			return super.set(index, element);
		}
		E previous = super.remove(index);
		try
		{
			super.add(ListExtensions.getIndexToInsert(this, element, comparator), element);
		}
		catch (RuntimeException exception)
		{
			super.add(index, previous);
			throw exception;
		}
		return previous;
	}

	/**
	 * Replaces each element of this list with the value the given operator gives for it, if the
	 * values are distinct, so that this list holds each element once and stays in the order of its
	 * comparator.
	 * <p>
	 * The operator is applied to every element first, in the order of this list. If it gives two
	 * values that are equal, compared with {@link Object#equals(Object)} as {@link #add(Object)}
	 * compares, this method throws an {@link IllegalArgumentException} and replaces no element, as
	 * {@link UniqueList#replaceAll(UnaryOperator)} does. Otherwise the values are sorted with the
	 * comparator, as {@link ArrayList#sort(Comparator)} sorts, stably, and stored; a value may
	 * equal an element of this list that it does not replace: {@code [a, b, c]} stays
	 * {@code [a, b, c]} with the operator that gives b for a, c for b and a for c. If the operator
	 * or the comparator throws, no element is replaced.
	 * <p>
	 * Without a comparator each element is replaced by its value at its index, as in
	 * {@link ArrayList#replaceAll(UnaryOperator)}, after the same check.
	 *
	 * @param operator
	 *            the operator to apply to each element
	 * @throws NullPointerException
	 *             if the operator is null, on an empty list too
	 * @throws IllegalArgumentException
	 *             if the operator gives two equal values
	 * @throws ConcurrentModificationException
	 *             if the operator changes this list structurally
	 */
	@Override
	public void replaceAll(UnaryOperator<E> operator)
	{
		Objects.requireNonNull(operator);
		int expectedModCount = modCount;
		List<E> values = new UniqueList<>(size());
		for (E element : this)
		{
			E value = operator.apply(element);
			if (!values.add(value))
			{
				throw new IllegalArgumentException(
					"The operator gives the value " + value + " for two elements of this list");
			}
		}
		if (modCount != expectedModCount)
		{
			throw new ConcurrentModificationException();
		}
		if (comparator != null)
		{
			values.sort(comparator);
		}
		Iterator<E> value = values.iterator();
		super.replaceAll(element -> value.next());
	}

	/**
	 * Refuses to sort this list: its order is the order of its comparator, which every method that
	 * puts an element into this list keeps. Another order would break it, and later adds would go
	 * to unspecified places; {@link #setComparator(Comparator)} sorts this list with a new
	 * comparator and keeps that order afterwards. {@link Collections#sort(List)}, which calls
	 * {@code sort(null)}, and {@link Collections#sort(List, Comparator)} throw as well.
	 * <p>
	 * The argument is not looked at: null and the comparator of this list throw too, and so does a
	 * list without a comparator, which setComparator gives an order.
	 *
	 * @param order
	 *            ignored
	 * @throws UnsupportedOperationException
	 *             always
	 */
	@Override
	public void sort(Comparator<? super E> order)
	{
		throw new UnsupportedOperationException(SORT_REFUSAL);
	}

	/**
	 * Returns a list iterator over the elements of this list, starting at its first element, as
	 * {@link #listIterator(int)} with the index 0 returns it
	 *
	 * @return a list iterator over the elements of this list
	 */
	@Override
	public ListIterator<E> listIterator()
	{
		return new ReadAndRemoveListIterator<>(super.listIterator(), LIST_ITERATOR_REFUSAL);
	}

	/**
	 * Returns a list iterator over the elements of this list, starting at the given index. It walks
	 * and removes as the list iterator of {@link ArrayList#listIterator(int)} does, and is
	 * fail-fast as that one.
	 * <p>
	 * Its {@link ListIterator#set(Object)} and {@link ListIterator#add(Object)} throw an
	 * {@link UnsupportedOperationException}, with a comparator and without one. Both put an element
	 * at the position of the cursor, as the contract of {@link ListIterator} says, where this list
	 * puts it at its place in the order of its comparator, or at the end, or does not add it at all
	 * if it holds it already: the cursor would no longer stand in front of the element
	 * {@code next()} returns, and {@code next()} would skip an element. {@link #set(int, Object)}
	 * and {@link #add(Object)} of this list sort an element in. {@link #iterator()} is the one of
	 * {@link ArrayList}, which has neither set nor add.
	 *
	 * @param index
	 *            the index of the first element that {@code next()} returns
	 * @return a list iterator over the elements of this list, starting at the given index
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range ({@code index < 0 || index > size()}), with the
	 *             message of {@link ArrayList#listIterator(int)}
	 */
	@Override
	public ListIterator<E> listIterator(int index)
	{
		return new ReadAndRemoveListIterator<>(super.listIterator(index), LIST_ITERATOR_REFUSAL);
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
	 * Sets the given comparator and sorts the elements of this list with it, as
	 * {@link ArrayList#sort(Comparator)} sorts them, so that this list is in its order and every
	 * later change keeps that order. It is the way to change the order of this list, which
	 * {@link #sort(Comparator)} refuses. With null the elements are sorted in their natural order,
	 * as ArrayList sorts them for null, and elements added later go to the end.
	 *
	 * @param comparator
	 *            the new comparator of this list
	 */
	public void setComparator(Comparator<? super E> comparator)
	{
		this.comparator = comparator;
		super.sort(comparator);
	}
}
