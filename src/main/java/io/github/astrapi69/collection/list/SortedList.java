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
 * its comparator: the add methods put an element at its place in that order, whatever index they
 * are given, and {@link #set(int, Object)}, {@link #replaceAll(UnaryOperator)} and the set of its
 * list iterator replace an element in place where the new one keeps that order. What would put an
 * element at a position the caller chooses, or reorder the list against its comparator, throws an
 * {@link UnsupportedOperationException}, as in a {@link java.util.SortedSet}: the order of the
 * comparator determines the position of the elements, so explicit positioning is not supported.
 * That is set and replaceAll where the new element does not keep the order,
 * {@link #sort(Comparator)}, which {@link Collections#sort(List)} calls, the add of its list
 * iterator, and {@link #addFirst(Object)} and {@link #addLast(Object)}. The views of
 * {@link #subList(int, int)} and {@link #reversed()} read and remove, and refuse to set, add,
 * replace or sort. {@link #setComparator(Comparator)} changes the order.
 * <p>
 * A list without a comparator has no order to keep: its add methods append at the end, and set,
 * replaceAll and the set of its list iterator replace in place, as in {@link ArrayList}.
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
	 * The message of the {@link UnsupportedOperationException} of {@link #addFirst(Object)},
	 * {@link #addLast(Object)} and the add of the list iterator, and the start of the one of
	 * {@link #set(int, Object)} and {@link #replaceAll(UnaryOperator)}, as
	 * {@link java.util.TreeSet#addFirst(Object)} says it
	 */
	private static final String POSITIONING_REFUSAL = "The comparator of a SortedList determines"
		+ " the position of its elements, so explicit positioning is not supported";

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
	 * Replaces the element at the given index with the given element where it keeps the order of
	 * the comparator of this list, and throws an {@link UnsupportedOperationException} where it
	 * does not.
	 * <p>
	 * <b>With a comparator</b> the element keeps the order if this list stays in that order with
	 * the element at the index: the element in front of the index, if there is one, compares less
	 * than or equal to it, and the element behind the index, if there is one, greater than or
	 * equal. Then it replaces the element at the index, as {@link ArrayList#set(int, Object)} does;
	 * {@code [a, c, e]} becomes {@code [a, d, e]} with {@code set(1, d)}. Otherwise this method
	 * throws an {@link UnsupportedOperationException}, and this list does not change; with
	 * {@code set(0, d)} for instance. The encounter order induced by the comparator of this list
	 * determines the position of elements, so explicit positioning is not supported, as
	 * {@link java.util.TreeSet#addFirst(Object)} says; {@link #add(Object)} puts an element at its
	 * place. Set does not move the element to its place itself, as
	 * {@link java.util.List#set(int, Object)} replaces the element "at the specified position", and
	 * a following {@code get(index)} is to return the given element.
	 * <p>
	 * The comparator compares the element with the neighbours of the index only: if this list is
	 * not in its order, as after an element changed so that the comparator orders it elsewhere, set
	 * keeps the order of the neighbours. A list of one element takes any element. If the comparator
	 * throws, as the natural order does for null, this list does not change either. The replacement
	 * is no structural modification, as in ArrayList: an iterator that runs over this list goes on.
	 * <p>
	 * The algorithms of {@link Collections} that set one element after another throw at the first
	 * set that does not keep the order, and the elements they set before stay set.
	 * {@link Collections#swap(List, int, int) Collections.swap(list, i, j)} calls {@code set(j,
	 * get(i))} and then {@code set(i, previous)}: two elements the comparator holds equal swap, and
	 * so does an index with itself; for two elements it does not hold equal it throws. This list
	 * then does not change, unless every element between the two indexes is equal to the one at
	 * {@code i} by the comparator, as for two neighbours: then the first set keeps the order, the
	 * second throws, and this list holds the element of {@code i} at both indexes and has lost the
	 * element of {@code j}; {@code [a, b]} becomes {@code [a, a]} with {@code swap(list, 0, 1)}.
	 * {@link Collections#reverse(List)} and {@link Collections#shuffle(List, java.util.Random)}
	 * swap, and the first swap that throws ends them; reverse throws at its first swap if the first
	 * and the last element are not equal by the comparator. {@link Collections#rotate(List, int)}
	 * sets each element at its new index, one after another: {@code [a, b, c, d]} with
	 * {@code rotate(list, 1)} sets a at 1, b at 2 and c at 3, which keep the order, throws for d at
	 * 0 and leaves {@code [a, a, b, c]}. {@link Collections#sort(List)} throws before any change,
	 * as {@link #sort(Comparator)} does.
	 * <p>
	 * <b>Without a comparator</b> this list has no order to keep: the element at the index is
	 * replaced, as in {@link ArrayList#set(int, Object)}, and the algorithms of {@link Collections}
	 * work as on an {@link ArrayList}.
	 * <p>
	 * In both cases the index is checked first, as {@link ArrayList#set(int, Object)} checks it.
	 *
	 * @param index
	 *            the index of the element to replace
	 * @param element
	 *            the element to store at the index
	 * @return the element previously at the index, as {@link java.util.List#set(int, Object)}
	 *         returns it
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range ({@code index < 0 || index >= size()}), with the
	 *             message of {@link ArrayList#set(int, Object)}
	 * @throws UnsupportedOperationException
	 *             if this list has a comparator and the element does not keep its order at the
	 *             index
	 */
	@Override
	public E set(int index, E element)
	{
		Objects.checkIndex(index, size());
		if (comparator != null && !keepsTheOrder(index, element))
		{
			throw new UnsupportedOperationException(POSITIONING_REFUSAL + ": the element " + element
				+ " at the index " + index + " would break its order");
		}
		return super.set(index, element);
	}

	/**
	 * Checks whether this list stays in the order of its comparator with the given element at the
	 * given index: the element in front of the index, if there is one, compares less than or equal
	 * to it, and the element behind the index, if there is one, greater than or equal
	 *
	 * @param index
	 *            the index, in range
	 * @param element
	 *            the element to check
	 * @return true if the element keeps the order at the index
	 */
	private boolean keepsTheOrder(int index, E element)
	{
		return (index == 0 || comparator.compare(get(index - 1), element) <= 0)
			&& (index == size() - 1 || comparator.compare(element, get(index + 1)) <= 0);
	}

	/**
	 * Replaces each element of this list with the value the given operator gives for it, each value
	 * at the index of the element it replaces, as {@link ArrayList#replaceAll(UnaryOperator)} does,
	 * where the values keep the order of the comparator of this list, and throws an
	 * {@link UnsupportedOperationException} where they do not. It follows {@link #set(int, Object)}
	 * for all elements at once, and is failure-atomic: an exception leaves this list as it was.
	 * <p>
	 * The operator is applied to every element first, in the order of this list. <b>With a
	 * comparator</b> the values keep the order if each compares greater than or equal to the one in
	 * front of it; then they are stored, {@code [a, c, e]} becomes {@code [aa, cc, ee]} with the
	 * operator that doubles each element. Otherwise this method throws an
	 * {@link UnsupportedOperationException}, as set does, and no element is replaced:
	 * {@code [a, c, e]} stays as it is with the operator that gives f for a. Element by element, as
	 * the default of {@link java.util.List#replaceAll(UnaryOperator)} sets them through the list
	 * iterator, the result would depend on the order of the sets, and a refusal would leave the
	 * values in front of it stored: {@code [a, b]} with the operator that gives c for a and d for b
	 * would throw at the first value, c in front of b, although {@code [c, d]} keeps the order.
	 * <p>
	 * <b>Without a comparator</b> each element is replaced by its value at its index, as in
	 * {@link ArrayList#replaceAll(UnaryOperator)}, after all values are computed.
	 * <p>
	 * In both cases an operator that throws, or that changes this list structurally, leaves this
	 * list as it was, and so does a comparator that throws, as the natural order does for null.
	 *
	 * @param operator
	 *            the operator to apply to each element
	 * @throws NullPointerException
	 *             if the operator is null, on an empty list too
	 * @throws ConcurrentModificationException
	 *             if the operator changes this list structurally
	 * @throws UnsupportedOperationException
	 *             if this list has a comparator and the values do not keep its order
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
			requireTheOrder(values);
		}
		Iterator<E> value = values.iterator();
		super.replaceAll(element -> value.next());
	}

	/**
	 * Checks that the given values are in the order of the comparator of this list: each compares
	 * greater than or equal to the one in front of it
	 *
	 * @param values
	 *            the values, each at the index of the element it is to replace
	 * @throws UnsupportedOperationException
	 *             if a value compares less than the one in front of it
	 */
	private void requireTheOrder(List<E> values)
	{
		for (int index = 1; index < values.size(); index++)
		{
			if (comparator.compare(values.get(index - 1), values.get(index)) > 0)
			{
				throw new UnsupportedOperationException(POSITIONING_REFUSAL + ": the value "
					+ values.get(index) + " at the index " + index + " would break its order");
			}
		}
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
		return new AddRefusingListIterator<>(super.listIterator(), POSITIONING_REFUSAL);
	}

	/**
	 * Returns a list iterator over the elements of this list, starting at the given index. It
	 * walks, removes and sets as the list iterator of {@link ArrayList#listIterator(int)} does, and
	 * is fail-fast as that one.
	 * <p>
	 * Its {@link ListIterator#set(Object)} replaces the element last returned by {@code next()} or
	 * {@code previous()} through {@link #set(int, Object)} of this list, at the index of that
	 * element, as the list iterator of {@link java.util.AbstractList#listIterator()} does: with the
	 * same semantics, it replaces in place where the element keeps the order of the comparator and
	 * throws an {@link UnsupportedOperationException} where it does not, and without a comparator
	 * it replaces in place. As that set is no structural modification, the cursor stays where it
	 * is.
	 * <p>
	 * Its {@link ListIterator#add(Object)} throws an {@link UnsupportedOperationException}, with a
	 * comparator and without one: the comparator of this list determines the position of its
	 * elements, so explicit positioning is not supported, as
	 * {@link java.util.TreeSet#addFirst(Object)} says. The add of a list iterator inserts the
	 * element immediately before the cursor, as the contract of {@link ListIterator} says, where
	 * {@link #add(Object)} of this list puts it at its place in the order of its comparator, or at
	 * the end: the cursor would no longer stand in front of the element {@code next()} returns, and
	 * {@code next()} would skip an element. It throws without a comparator too, as
	 * {@link #addFirst(Object)} does, so that what this list supports does not depend on whether
	 * {@link #setComparator(Comparator)} was called. {@link #iterator()} is the one of
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
		return new AddRefusingListIterator<>(super.listIterator(index), POSITIONING_REFUSAL);
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
		throw new UnsupportedOperationException(POSITIONING_REFUSAL);
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
		throw new UnsupportedOperationException(POSITIONING_REFUSAL);
	}

	/**
	 * Returns a view of this list in the reverse order, as {@link List#reversed()} does, that reads
	 * and removes and refuses every other change, as the view of {@link #subList(int, int)} does.
	 * <p>
	 * The view of List.reversed writes through add(int, E) of this list, which sorts the element
	 * in, and through its set, and its addFirst and addLast go through add(int, E) as well, past
	 * the refusal of {@link #addFirst(Object)} and {@link #addLast(Object)}: the positions a
	 * reversed view promises, an element added first or last, cannot hold in a sorted list.
	 * Reading, {@code remove}, {@code clear}, {@code removeIf}, {@code removeAll} and
	 * {@code retainAll} through the view work and keep the order; set, add, addFirst, addLast,
	 * addAll, replaceAll and sort through it, and the set and add of its list iterators, throw an
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
