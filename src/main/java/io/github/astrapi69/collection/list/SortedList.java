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
 * The class {@link SortedList} extends the {@link ArrayList} and keeps its elements in the order of
 * its comparator: every method that puts an element into the list puts it at its place in that
 * order, the add methods whatever index they are given, and {@link #set(int, Object)} and
 * {@link #replaceAll(UnaryOperator)} too. What would put an element at a position the caller
 * chooses, or reorder the list against its comparator, throws an
 * {@link UnsupportedOperationException}: {@link #sort(Comparator)}, which
 * {@link Collections#sort(List)} calls, the set and add of its list iterator, and
 * {@link #addFirst(Object)} and {@link #addLast(Object)}, as in a {@link java.util.SortedSet}. The
 * views of {@link #subList(int, int)} and {@link #reversed()} read and remove, and refuse to set,
 * add, replace or sort. {@link #setComparator(Comparator)} changes the order.
 * <p>
 * A list without a comparator has no order to keep: its add methods append at the end, and set and
 * replaceAll replace in place, as in {@link ArrayList}.
 * <p>
 * The list may hold an element more than once; {@link SortedUniqueList} holds each element once.
 *
 * @param <E>
 *            the generic type of values
 * @see ArrayList
 */
public class SortedList<E> extends ArrayList<E>
{

	/**
	 * The message of the {@link UnsupportedOperationException} of the view of
	 * {@link #subList(int, int)}
	 */
	private static final String SUB_LIST_REFUSAL = "A subList view of a SortedList reads and"
		+ " removes only; set and add through the list itself";

	/**
	 * The message of the {@link UnsupportedOperationException} of {@link #addFirst(Object)} and
	 * {@link #addLast(Object)}
	 */
	private static final String SEQUENCED_REFUSAL = "The comparator of a SortedList determines the"
		+ " position of its elements, so explicit positioning is not supported";

	/**
	 * The message of the {@link UnsupportedOperationException} of the view of {@link #reversed()}
	 */
	private static final String REVERSED_REFUSAL = "The reversed view of a SortedList reads and"
		+ " removes only; add through the list itself";

	/**
	 * The message of the {@link UnsupportedOperationException} of {@link #sort(Comparator)}
	 */
	private static final String SORT_REFUSAL = "A SortedList keeps the order of its comparator;"
		+ " setComparator(Comparator) changes it";

	/**
	 * The message of the {@link UnsupportedOperationException} of the set and add of the list
	 * iterator
	 */
	private static final String LIST_ITERATOR_REFUSAL = "A SortedList puts an element at its"
		+ " place in the order of its comparator, not at the position of a list iterator";

	/**
	 * the comparator for sort this list object
	 */
	private Comparator<? super E> comparator;

	/**
	 * Constructs an empty {@link SortedList} instance with the specified initial capacity
	 *
	 * @param initialCapacity
	 *            the initial capacity
	 */
	public SortedList(int initialCapacity)
	{
		super(initialCapacity);
	}

	/**
	 * Constructs an empty {@link SortedList} instance with an initial capacity of ten
	 */
	public SortedList()
	{
	}

	/**
	 * Constructs a {@link SortedList} instance and set the given comparator
	 *
	 * @param comparator
	 *            the comparator
	 */
	public SortedList(Comparator<? super E> comparator)
	{
		this.comparator = comparator;
	}

	/**
	 * Constructs a {@link SortedList} instance containing the elements of the given collection
	 *
	 * @param collection
	 *            the collection
	 */
	public SortedList(Collection<? extends E> collection)
	{
		addAll(collection);
	}

	/**
	 * Constructs a {@link SortedList} instance containing the elements of the given collection and
	 * set the given comparator
	 *
	 * @param collection
	 *            the collection
	 * @param comparator
	 *            the comparator
	 */
	public SortedList(Collection<? extends E> collection, Comparator<? super E> comparator)
	{
		this(comparator);
		addAll(collection);
	}

	/**
	 * Adds the given element at its place in the order of the comparator of this list, the index
	 * that {@link ListExtensions#getIndexToInsert(java.util.List, Object, Comparator)
	 * ListExtensions.getIndexToInsert} gives, or at the end of this list if it has no comparator.
	 * An element this list contains already is added again.
	 * <p>
	 * If this list holds elements that the comparator holds equal to the given one, the given
	 * element goes in front of the one of them that
	 * {@link Collections#binarySearch(java.util.List, Object, Comparator) Collections.binarySearch}
	 * finds, which can be any of them: it does not necessarily come after them, and such elements
	 * do not keep the order in which they were added. The place is not specified if this list is
	 * not in the order of its comparator, as after an element changed so that the comparator orders
	 * it elsewhere.
	 *
	 * @param element
	 *            the element to add
	 * @return true, always: this list changes with every call
	 */
	@Override
	public boolean add(E element)
	{
		int index = comparator != null
			? ListExtensions.getIndexToInsert(this, element, comparator)
			: size();
		super.add(index, element);
		return true;
	}

	/**
	 * Adds the given element as every add method of this class does: at its place in the order of
	 * the comparator of this list, or at its end if this list has no comparator. The given index is
	 * ignored and not checked; any index is accepted, a negative one too.
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
	 * Adds the elements of the given collection one by one, in the order of the collection's
	 * iterator, as {@link #add(Object)} adds each of them: each at its place in the order of the
	 * comparator of this list, or at the end of this list if it has no comparator. Elements this
	 * list contains already are added again.
	 * <p>
	 * The elements are copied from the collection first, as {@link ArrayList#addAll(Collection)}
	 * copies them, so that the collection may be this list itself: {@code list.addAll(list)} adds
	 * each element a second time, where adding while iterating over this list would throw a
	 * {@link ConcurrentModificationException}.
	 *
	 * @param collection
	 *            the collection with the elements to add
	 * @return true if this list changed as a result of the call, that is if the collection is not
	 *         empty
	 */
	@Override
	public boolean addAll(Collection<? extends E> collection)
	{
		boolean result = false;
		for (E element : new ArrayList<E>(collection))
		{
			result |= add(element);
		}
		return result;
	}

	/**
	 * Adds the elements of the given collection as every add method of this class does: each at its
	 * place in the order of the comparator of this list, or at its end if this list has no
	 * comparator. The given index is ignored and not checked; any index is accepted, a negative one
	 * too.
	 *
	 * @param index
	 *            ignored
	 * @param collection
	 *            the collection with the elements to add
	 * @return true if this list changed as a result of the call
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> collection)
	{
		return addAll(collection);
	}

	/**
	 * Replaces the element at the given index with the given element so that this list stays in the
	 * order of its comparator: the element at the index is removed, and the given one is added as
	 * {@link #add(Object)} adds it, at its place in that order, which need not be the given index;
	 * {@code [a, c, e]} becomes {@code [c, d, e]} with {@code set(0, d)}. A sorted list cannot hold
	 * an element at an index of the caller's choice, and a set that refused would break the
	 * algorithms of {@link Collections} that work through it.
	 * <p>
	 * Those algorithms, {@link Collections#swap(List, int, int)},
	 * {@link Collections#reverse(List)}, {@link Collections#shuffle(List, java.util.Random)} and
	 * {@link Collections#rotate(List, int)}, therefore leave this list in the order of its
	 * comparator: with a comparator consistent with equals they leave it as it was.
	 * <p>
	 * The index is checked as {@link ArrayList#set(int, Object)} checks it, before this list
	 * changes. If the comparator throws, as the natural order does for null, the removed element is
	 * put back at the index, and this list is as it was. As the element can move, set with a
	 * comparator is a structural modification: an iterator that runs over this list throws a
	 * {@link ConcurrentModificationException} at its next step.
	 * <p>
	 * Without a comparator this list has no order to keep: the element at the index is replaced, as
	 * in {@link ArrayList#set(int, Object)}, which is no structural modification, and the
	 * algorithms of {@link Collections} work as on an {@link ArrayList}.
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
	 */
	@Override
	public E set(int index, E element)
	{
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
	 * Replaces each element of this list with the value the given operator gives for it so that
	 * this list stays in the order of its comparator: the operator is applied to every element
	 * first, in the order of this list, then the values are sorted with the comparator and stored;
	 * {@code [a, c, e]} becomes {@code [c, e, f]} with the operator that gives f for a. The values
	 * are sorted as {@link ArrayList#sort(Comparator)} sorts, stably: values the comparator holds
	 * equal keep the order of the elements they replace. If the operator or the comparator throws,
	 * no element is replaced.
	 * <p>
	 * Without a comparator each element is replaced by its value at its index, as in
	 * {@link ArrayList#replaceAll(UnaryOperator)}.
	 *
	 * @param operator
	 *            the operator to apply to each element
	 * @throws NullPointerException
	 *             if the operator is null, on an empty list too
	 * @throws ConcurrentModificationException
	 *             if the operator changes this list structurally
	 */
	@Override
	public void replaceAll(UnaryOperator<E> operator)
	{
		Objects.requireNonNull(operator);
		int expectedModCount = modCount;
		List<E> values = new ArrayList<>(size());
		for (E element : this)
		{
			values.add(operator.apply(element));
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
	 * puts it at its place in the order of its comparator, or at the end: the cursor would no
	 * longer stand in front of the element {@code next()} returns, and {@code next()} would skip an
	 * element. {@link #set(int, Object)} and {@link #add(Object)} of this list sort an element in.
	 * {@link #iterator()} is the one of {@link ArrayList}, which has neither set nor add.
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
	 * Returns a view of the elements of this list between the given indexes, as
	 * {@link ArrayList#subList(int, int)} does, that reads and removes and refuses every other
	 * change.
	 * <p>
	 * Reading, {@code remove}, {@code clear}, {@code removeIf}, {@code removeAll} and
	 * {@code retainAll} through the view, and the remove of its iterators, work as on the view of
	 * {@link ArrayList}: a removal keeps the order of the comparator, and
	 * {@code list.subList(from, to).clear()} removes a range of this list.
	 * <p>
	 * Set, add, addAll, replaceAll and sort through the view, and the set and add of its list
	 * iterators, throw an {@link UnsupportedOperationException}, whatever the arguments: this list
	 * puts an element at its place in the order of its comparator, which can be outside the view,
	 * so that the view of ArrayList, which counts every added element as one of its own, showed
	 * elements from outside its range, and a set, a replaceAll or a sort inside the view would
	 * break the order. {@link java.util.Collections#swap(List, int, int)} and
	 * {@link java.util.Collections#reverse(List)} work through set and throw on the view too. The
	 * view of a view follows the same rules.
	 *
	 * @param fromIndex
	 *            the index of the first element of the view, inclusive
	 * @param toIndex
	 *            the index after the last element of the view, exclusive
	 * @return a view of the elements between the given indexes that reads and removes
	 * @throws IndexOutOfBoundsException
	 *             if an index is out of range ({@code fromIndex < 0 || toIndex > size()}), as
	 *             {@link ArrayList#subList(int, int)} checks it
	 * @throws IllegalArgumentException
	 *             if fromIndex is greater than toIndex
	 */
	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		return new ReadAndRemoveSubList<>(super.subList(fromIndex, toIndex), SUB_LIST_REFUSAL);
	}

	/**
	 * Throws an {@link UnsupportedOperationException}. The comparator of this list determines the
	 * position of its elements, so explicit positioning is not supported, as in
	 * {@link java.util.SortedSet#addFirst(Object)}; {@link #add(Object)} sorts the element in. It
	 * throws also if this list has no comparator, whose add appends at the end, as
	 * {@link #sort(Comparator)} does, so that what this list supports does not depend on whether
	 * {@link #setComparator(Comparator)} was called.
	 *
	 * @param element
	 *            ignored
	 * @throws UnsupportedOperationException
	 *             always
	 */
	@Override
	public void addFirst(E element)
	{
		throw new UnsupportedOperationException(SEQUENCED_REFUSAL);
	}

	/**
	 * Throws an {@link UnsupportedOperationException}, as {@link #addFirst(Object)} does: the
	 * comparator of this list determines the position of its elements, so explicit positioning is
	 * not supported, as in {@link java.util.SortedSet#addLast(Object)}
	 *
	 * @param element
	 *            ignored
	 * @throws UnsupportedOperationException
	 *             always
	 */
	@Override
	public void addLast(E element)
	{
		throw new UnsupportedOperationException(SEQUENCED_REFUSAL);
	}

	/**
	 * Returns a view of this list in the reverse order, as {@link List#reversed()} does, that reads
	 * and removes and refuses every other change, as the view of {@link #subList(int, int)} does.
	 * <p>
	 * The view of List.reversed writes through add(int, E) and set of this list, which sort the
	 * element in, and its addFirst and addLast go through add(int, E) as well, past the refusal of
	 * {@link #addFirst(Object)} and {@link #addLast(Object)}: the positions a reversed view
	 * promises, an element added first or last, cannot hold in a sorted list. Reading,
	 * {@code remove}, {@code clear}, {@code removeIf}, {@code removeAll} and {@code retainAll}
	 * through the view work and keep the order; set, add, addFirst, addLast, addAll, replaceAll and
	 * sort through it, and the set and add of its list iterators, throw an
	 * {@link UnsupportedOperationException}.
	 *
	 * @return a view of this list in the reverse order that reads and removes
	 */
	@Override
	public List<E> reversed()
	{
		return new ReadAndRemoveSubList<>(super.reversed(), REVERSED_REFUSAL);
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
	 * <p>
	 * The elements are sorted in a copy first, and the comparator is set and this list reordered
	 * only when that sort succeeded: if the sort throws, as the natural order does for elements
	 * that are not {@link Comparable} or for null, this method throws that exception, and this list
	 * keeps its comparator and its order. A sort in place could leave this list half sorted, and a
	 * comparator set before the sort would claim an order the list does not have.
	 *
	 * @param comparator
	 *            the new comparator of this list
	 * @throws ClassCastException
	 *             if the comparator is null and an element is not {@link Comparable}, or the
	 *             comparator cannot compare two of the elements
	 */
	public void setComparator(Comparator<? super E> comparator)
	{
		List<E> sorted = new ArrayList<>(this);
		sorted.sort(comparator);
		Iterator<E> element = sorted.iterator();
		super.replaceAll(each -> element.next());
		this.comparator = comparator;
	}
}
