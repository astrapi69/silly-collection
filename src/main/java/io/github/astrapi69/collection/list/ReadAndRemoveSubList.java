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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * The view that {@code subList} of {@link UniqueList}, {@link SortedList} and
 * {@link SortedUniqueList} returns: it reads and removes through the view of
 * {@link java.util.ArrayList#subList(int, int)} it wraps, and refuses every other change with an
 * {@link UnsupportedOperationException}.
 * <p>
 * The view of ArrayList writes the backing array directly in set and replaceAll, past the checks of
 * these lists, so that a {@link UniqueList} held an element twice and a sorted list left the order
 * of its comparator. Its add methods call the add methods of the list and then add one, or the size
 * of the collection, to the size of the view; these lists move an element they hold, add nothing
 * for it, or put it at its sorted place, which can be outside the view, so that the view showed
 * elements from outside it or null past the end of the list. A view cannot keep its range when the
 * list decides where an element goes, so it does not set or add at all; the list itself does.
 * <p>
 * A removal breaks neither the uniqueness nor the order, and goes to the view of ArrayList, which
 * removes through {@link java.util.ArrayList#remove(int)} and the range and batch removals of
 * ArrayList, none of which these lists override: {@code subList(from, to).clear()} stays the idiom
 * to remove a range. Reading goes to the view of ArrayList as well, so that equals, hashCode and
 * toString are those of a {@link List}, and it is fail-fast as that view is.
 * <p>
 * Refused, whatever the arguments: {@link #set(int, Object)}, {@link #add(Object)},
 * {@link #add(int, Object)}, {@link #addAll(Collection)}, {@link #addAll(int, Collection)},
 * {@link #replaceAll(UnaryOperator)}, {@link #sort(Comparator)}, and the set and add of its list
 * iterators. The view of a view follows the same rules.
 *
 * @param <E>
 *            the generic type of the elements
 */
final class ReadAndRemoveSubList<E> implements List<E>, RandomAccess
{

	/**
	 * The view of {@link java.util.ArrayList#subList(int, int)} this view reads and removes with
	 */
	private final List<E> view;

	/**
	 * The message of the {@link UnsupportedOperationException} of the refused methods
	 */
	private final String refusal;

	/**
	 * Constructs a view that reads and removes with the given view and refuses every other change
	 * with the given message
	 *
	 * @param view
	 *            the view of {@link java.util.ArrayList#subList(int, int)} to read and remove with
	 * @param refusal
	 *            the message of the {@link UnsupportedOperationException} of the refused methods,
	 *            which says why the view refuses them
	 */
	ReadAndRemoveSubList(List<E> view, String refusal)
	{
		this.view = view;
		this.refusal = refusal;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size()
	{
		return view.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty()
	{
		return view.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object element)
	{
		return view.contains(element);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> collection)
	{
		return view.containsAll(collection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E get(int index)
	{
		return view.get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int indexOf(Object element)
	{
		return view.indexOf(element);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int lastIndexOf(Object element)
	{
		return view.lastIndexOf(element);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray()
	{
		return view.toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T[] toArray(T[] array)
	{
		return view.toArray(array);
	}

	/**
	 * Returns an iterator over the elements of this view, the list iterator of
	 * {@link #listIterator()}: the iterator of the wrapped view is a list iterator whose set and
	 * add would bypass this view
	 *
	 * @return an iterator over the elements of this view
	 */
	@Override
	public Iterator<E> iterator()
	{
		return listIterator();
	}

	/**
	 * Returns a list iterator over the elements of this view, starting at its first element, as
	 * {@link #listIterator(int)} with the index 0 returns it
	 *
	 * @return a list iterator over the elements of this view
	 */
	@Override
	public ListIterator<E> listIterator()
	{
		return new ReadAndRemoveListIterator<>(view.listIterator(), refusal);
	}

	/**
	 * Returns a list iterator over the elements of this view, starting at the given index: it
	 * walks, removes and fails fast as the list iterator of the wrapped view, and its set and add
	 * throw an {@link UnsupportedOperationException}, as the methods of this view do
	 *
	 * @param index
	 *            the index of the first element that {@code next()} returns
	 * @return a list iterator over the elements of this view, starting at the given index
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range ({@code index < 0 || index > size()})
	 */
	@Override
	public ListIterator<E> listIterator(int index)
	{
		return new ReadAndRemoveListIterator<>(view.listIterator(index), refusal);
	}

	/**
	 * Returns a view of the elements of this view between the given indexes, which reads and
	 * removes as this view does and refuses what this view refuses
	 *
	 * @param fromIndex
	 *            the index of the first element of the view, inclusive
	 * @param toIndex
	 *            the index after the last element of the view, exclusive
	 * @return a view of the elements of this view between the given indexes
	 * @throws IndexOutOfBoundsException
	 *             if an index is out of range
	 * @throws IllegalArgumentException
	 *             if fromIndex is greater than toIndex
	 */
	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		return new ReadAndRemoveSubList<>(view.subList(fromIndex, toIndex), refusal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E remove(int index)
	{
		return view.remove(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object element)
	{
		return view.remove(element);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> collection)
	{
		return view.removeAll(collection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAll(Collection<?> collection)
	{
		return view.retainAll(collection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeIf(Predicate<? super E> filter)
	{
		return view.removeIf(filter);
	}

	/**
	 * Removes the elements of this view from the list, the range the view covers
	 */
	@Override
	public void clear()
	{
		view.clear();
	}

	/**
	 * Refuses to replace an element: the list decides where an element goes, which can be outside
	 * this view
	 *
	 * @param index
	 *            ignored
	 * @param element
	 *            ignored
	 * @return nothing, it throws
	 * @throws UnsupportedOperationException
	 *             always
	 */
	@Override
	public E set(int index, E element)
	{
		throw new UnsupportedOperationException(refusal);
	}

	/**
	 * Refuses to add an element: the list decides where an element goes, which can be outside this
	 * view, or does not add it
	 *
	 * @param element
	 *            ignored
	 * @return nothing, it throws
	 * @throws UnsupportedOperationException
	 *             always
	 */
	@Override
	public boolean add(E element)
	{
		throw new UnsupportedOperationException(refusal);
	}

	/**
	 * Refuses to insert an element, as {@link #add(Object)} refuses
	 *
	 * @param index
	 *            ignored
	 * @param element
	 *            ignored
	 * @throws UnsupportedOperationException
	 *             always
	 */
	@Override
	public void add(int index, E element)
	{
		throw new UnsupportedOperationException(refusal);
	}

	/**
	 * Refuses to add elements, as {@link #add(Object)} refuses, for an empty collection too
	 *
	 * @param collection
	 *            ignored
	 * @return nothing, it throws
	 * @throws UnsupportedOperationException
	 *             always
	 */
	@Override
	public boolean addAll(Collection<? extends E> collection)
	{
		throw new UnsupportedOperationException(refusal);
	}

	/**
	 * Refuses to insert elements, as {@link #add(Object)} refuses, for an empty collection too
	 *
	 * @param index
	 *            ignored
	 * @param collection
	 *            ignored
	 * @return nothing, it throws
	 * @throws UnsupportedOperationException
	 *             always
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> collection)
	{
		throw new UnsupportedOperationException(refusal);
	}

	/**
	 * Refuses to replace the elements, as {@link #set(int, Object)} refuses, for an empty view too
	 *
	 * @param operator
	 *            ignored
	 * @throws UnsupportedOperationException
	 *             always
	 */
	@Override
	public void replaceAll(UnaryOperator<E> operator)
	{
		throw new UnsupportedOperationException(refusal);
	}

	/**
	 * Refuses to sort the elements: the order of the list is the one of its comparator, or the one
	 * its methods give it, for an empty view too
	 *
	 * @param order
	 *            ignored
	 * @throws UnsupportedOperationException
	 *             always
	 */
	@Override
	public void sort(Comparator<? super E> order)
	{
		throw new UnsupportedOperationException(refusal);
	}

	/**
	 * Compares this view with the given object as {@link List#equals(Object)} says: true if it is a
	 * list with the same elements in the same order
	 *
	 * @param object
	 *            the object to compare with
	 * @return true if the given object is a list with the elements of this view in their order
	 */
	@Override
	public boolean equals(Object object)
	{
		return view.equals(object);
	}

	/**
	 * Returns the hash code of this view as {@link List#hashCode()} says it
	 *
	 * @return the hash code of this view
	 */
	@Override
	public int hashCode()
	{
		return view.hashCode();
	}

	/**
	 * Returns the elements of this view in their order, as the view of ArrayList shows them,
	 * {@code [b, c]}
	 *
	 * @return the elements of this view in their order
	 */
	@Override
	public String toString()
	{
		return view.toString();
	}
}
