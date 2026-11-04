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
 * list checks, with equals, whether the list holds it already; the add methods put an element at
 * its place in that order, whatever index they are given, and {@link #set(int, Object)},
 * {@link #replaceAll(UnaryOperator)} and the set of its list iterator replace an element in place
 * where the new one keeps that order. What would put an element at a position the caller chooses,
 * or reorder the list against its comparator, throws an {@link UnsupportedOperationException}, as
 * in a {@link java.util.SortedSet}: the order of the comparator determines the position of the
 * elements, so explicit positioning is not supported. That is set and replaceAll where the new
 * element does not keep the order, {@link #sort(Comparator)}, which {@link Collections#sort(List)}
 * calls, the add of its list iterator, and {@link #addFirst(Object)} and {@link #addLast(Object)}.
 * The views of {@link #subList(int, int)} and {@link #reversed()} read and remove, and refuse to
 * set, add, replace or sort. {@link #setComparator(Comparator)} changes the order.
 * <p>
 * A list without a comparator has no order to keep: its add methods append at the end, replaceAll
 * replaces in place, as in {@link ArrayList}, with the same check for equal values, and set and the
 * set of its list iterator swap an element the list holds at another index, as
 * {@link UniqueList#set(int, Object)} does.
 *
 * @param <E>
 *            the generic type of values
 */
public class SortedUniqueList<E> extends ArrayList<E>
{

	/**
	 * The message of the {@link UnsupportedOperationException} of the view of
	 * {@link #subList(int, int)}
	 */
	private static final String SUB_LIST_REFUSAL = "A subList view of a SortedUniqueList reads and"
		+ " removes only; set and add through the list itself";

	/**
	 * The message of the {@link UnsupportedOperationException} of {@link #addFirst(Object)},
	 * {@link #addLast(Object)} and the add of the list iterator, and the start of the one of
	 * {@link #set(int, Object)} and {@link #replaceAll(UnaryOperator)}, as
	 * {@link java.util.TreeSet#addFirst(Object)} says it
	 */
	private static final String POSITIONING_REFUSAL = "The comparator of a SortedUniqueList"
		+ " determines the position of its elements, so explicit positioning is not supported";

	/**
	 * The message of the {@link UnsupportedOperationException} of the view of {@link #reversed()}
	 */
	private static final String REVERSED_REFUSAL = "The reversed view of a SortedUniqueList reads and"
		+ " removes only; add through the list itself";

	/**
	 * The message of the {@link UnsupportedOperationException} of {@link #sort(Comparator)}
	 */
	private static final String SORT_REFUSAL = "A SortedUniqueList keeps the order of its"
		+ " comparator; setComparator(Comparator) changes it";

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
	 * list is not in the order of its comparator, as after an element changed so that the
	 * comparator orders it elsewhere.
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
	 * element once afterwards too, and, if it has a comparator, where the element keeps the order
	 * of the comparator; where it does not, this method throws an
	 * {@link UnsupportedOperationException}.
	 * <p>
	 * <b>With a comparator</b> the checks come in this order, and each that fails throws before
	 * this list changes:
	 * <ol>
	 * <li>the index, as {@link ArrayList#set(int, Object)} checks it;</li>
	 * <li>whether this list holds the given element at another index, compared with equals as
	 * {@link #add(Object)} compares: then it throws an {@link IllegalArgumentException}, as this
	 * list would hold the element twice, whatever the order; a swap, as {@link UniqueList} does,
	 * cannot help where the order and not the index decides where an element goes;</li>
	 * <li>whether the element keeps the order: this list stays in the order of its comparator with
	 * the element at the index, the element in front of the index, if there is one, compares less
	 * than or equal to it, and the element behind the index, if there is one, greater than or
	 * equal. Otherwise it throws an {@link UnsupportedOperationException}: the encounter order
	 * induced by the comparator of this list determines the position of elements, so explicit
	 * positioning is not supported, as {@link java.util.TreeSet#addFirst(Object)} says.</li>
	 * </ol>
	 * Then the element at the index is replaced, as in ArrayList; {@code [a, c, e]} becomes
	 * {@code [a, d, e]} with {@code set(1, d)}, and {@code set(0, d)} throws. Set does not move the
	 * element to its place itself, as {@link java.util.List#set(int, Object) List.set} replaces the
	 * element "at the specified position", and a following {@code get(index)} is to return the
	 * given element; {@link #add(Object)} puts an element at its place.
	 * <p>
	 * The uniqueness comes first because it is a property of the element alone, as
	 * {@link java.util.List#set(int, Object) List.set} describes its IllegalArgumentException ("if
	 * some property of the specified element prevents it from being added to this list"), and so
	 * that the algorithms of {@link Collections} that swap through set,
	 * {@link Collections#swap(List, int, int)}, {@link Collections#reverse(List)},
	 * {@link Collections#shuffle(List, java.util.Random)} and
	 * {@link Collections#rotate(List, int)}, throw the IllegalArgumentException at their first set
	 * with an element this list holds at another index, before this list changes, whether the two
	 * indexes are neighbours or not; a swap of an index with itself works.
	 * {@link Collections#sort(List)} throws before any change, as {@link #sort(Comparator)} does.
	 * <p>
	 * The comparator compares the element with the neighbours of the index only: if this list is
	 * not in its order, as after an element changed so that the comparator orders it elsewhere, set
	 * keeps the order of the neighbours. A list of one element takes any element. If the comparator
	 * throws, as the natural order does for null, this list does not change either. The replacement
	 * is no structural modification: an iterator that runs over this list goes on.
	 * <p>
	 * <b>Without a comparator</b> this list has no order to keep, and set works as
	 * {@link UniqueList#set(int, Object)}: if this list holds the given element at another index,
	 * the two elements swap, the given element goes to the given index and the element that was
	 * there goes to the index the given element was at; {@code [a, b, c]} becomes {@code [c, b, a]}
	 * with {@code set(0, c)}. Otherwise the element at the index is replaced, as in
	 * {@link ArrayList#set(int, Object)}. Neither case is a structural modification, and the
	 * algorithms of {@link Collections} above work as on a {@link UniqueList}.
	 * <p>
	 * In both cases an element equal to the one at the index itself replaces that one, with a
	 * comparator where it keeps the order, and the index is checked first, as
	 * {@link ArrayList#set(int, Object)} checks it, for an element this list holds too.
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
	 * @throws IllegalArgumentException
	 *             if this list has a comparator and holds the given element at another index
	 * @throws UnsupportedOperationException
	 *             if this list has a comparator and the element does not keep its order at the
	 *             index
	 */
	@Override
	public E set(int index, E element)
	{
		Objects.checkIndex(index, size());
		int heldAt = indexOf(element);
		boolean heldAtAnotherIndex = heldAt >= 0 && heldAt != index;
		if (comparator == null)
		{
			E previous = super.set(index, element);
			if (heldAtAnotherIndex)
			{
				super.set(heldAt, previous);
			}
			return previous;
		}
		if (heldAtAnotherIndex)
		{
			throw new IllegalArgumentException("This list holds the element " + element
				+ " at the index " + heldAt + ", it cannot hold it at the index " + index + " too");
		}
		if (!keepsTheOrder(index, element))
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
	 * at the index of the element it replaces, if the values are distinct, so that this list holds
	 * each element once, and, if it has a comparator, where the values keep its order; where they
	 * do not, this method throws an {@link UnsupportedOperationException}. It follows
	 * {@link #set(int, Object)} for all elements at once, and is failure-atomic: an exception
	 * leaves this list as it was.
	 * <p>
	 * The operator is applied to every element first, in the order of this list. If it gives two
	 * values that are equal, compared with {@link Object#equals(Object)} as {@link #add(Object)}
	 * compares, this method throws an {@link IllegalArgumentException} and replaces no element, as
	 * {@link UniqueList#replaceAll(UnaryOperator)} does, whatever the order of the values, as set
	 * checks the uniqueness before the order. A value may equal an element of this list that it
	 * does not replace. <b>With a comparator</b> the values then keep the order if each compares
	 * greater than or equal to the one in front of it; then they are stored, {@code [a, b, c]}
	 * becomes {@code [b, c, d]} with the operator that gives the next letter. Otherwise this method
	 * throws an {@link UnsupportedOperationException}, as set does, and no element is replaced:
	 * {@code [a, c, e]} stays as it is with the operator that gives f for a. Element by element, as
	 * the default of {@link java.util.List#replaceAll(UnaryOperator)} sets them through the list
	 * iterator, set would refuse the operator that gives the next letter at its first value, b for
	 * a, which this list holds at another index, although {@code [b, c, d]} holds each element once
	 * and keeps the order.
	 * <p>
	 * <b>Without a comparator</b> each element is replaced by its value at its index, as in
	 * {@link ArrayList#replaceAll(UnaryOperator)}, after the same check.
	 * <p>
	 * In both cases an operator that throws, or that changes this list structurally, leaves this
	 * list as it was, and so does a comparator that throws, as the natural order does for null.
	 *
	 * @param operator
	 *            the operator to apply to each element
	 * @throws NullPointerException
	 *             if the operator is null, on an empty list too
	 * @throws IllegalArgumentException
	 *             if the operator gives two equal values
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
	 * same semantics, it throws an {@link IllegalArgumentException} for an element this list holds
	 * at another index, replaces in place where the element keeps the order of the comparator and
	 * throws an {@link UnsupportedOperationException} where it does not; without a comparator it
	 * swaps an element this list holds at another index, so that the iterator can return that
	 * element again, and replaces in place otherwise. As that set is no structural modification,
	 * the cursor stays where it is.
	 * <p>
	 * Its {@link ListIterator#add(Object)} throws an {@link UnsupportedOperationException}, with a
	 * comparator and without one: the comparator of this list determines the position of its
	 * elements, so explicit positioning is not supported, as
	 * {@link java.util.TreeSet#addFirst(Object)} says. The add of a list iterator inserts the
	 * element immediately before the cursor, as the contract of {@link ListIterator} says, where
	 * {@link #add(Object)} of this list puts it at its place in the order of its comparator, or at
	 * the end, or does not add it at all if it holds it already: the cursor would no longer stand
	 * in front of the element {@code next()} returns, and {@code next()} would skip an element. It
	 * throws without a comparator too, as {@link #addFirst(Object)} does, so that what this list
	 * supports does not depend on whether {@link #setComparator(Comparator)} was called.
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
		return new AddRefusingListIterator<>(super.listIterator(index), POSITIONING_REFUSAL);
	}

	/**
	 * Returns a view of the elements of this list between the given indexes, as
	 * {@link ArrayList#subList(int, int)} does, that reads and removes and refuses every other
	 * change.
	 * <p>
	 * Reading, {@code remove}, {@code clear}, {@code removeIf}, {@code removeAll} and
	 * {@code retainAll} through the view, and the remove of its iterators, work as on the view of
	 * {@link ArrayList}: a removal keeps the order of the comparator and each element once, and
	 * {@code list.subList(from, to).clear()} removes a range of this list.
	 * <p>
	 * Set, add, addAll, replaceAll and sort through the view, and the set and add of its list
	 * iterators, throw an {@link UnsupportedOperationException}, whatever the arguments: this list
	 * puts an element at its place in the order of its comparator, which can be outside the view,
	 * so that the view of ArrayList, which counts every added element as one of its own, showed
	 * elements from outside its range, and a set or a replaceAll inside the view would break the
	 * order or hold an element twice, and a sort would break the order.
	 * {@link java.util.Collections#swap(List, int, int)} and
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
