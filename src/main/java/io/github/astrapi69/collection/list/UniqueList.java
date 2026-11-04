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
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * The class {@link UniqueList} extends the {@link ArrayList} and holds each element once: it
 * overrides the methods of {@link ArrayList} that put an element into the list, the add methods,
 * {@link #set(int, Object)}, {@link #replaceAll(UnaryOperator)} and the add and set of its list
 * iterator, so that they check whether the list holds the element already. The view of
 * {@link #subList(int, int)} reads and removes, and refuses to set, add, replace or sort.
 *
 * @param <E>
 *            the generic type of values
 * @see java.util.ArrayList
 */
public class UniqueList<E> extends ArrayList<E>
{

	/**
	 * The message of the {@link UnsupportedOperationException} of the view of
	 * {@link #subList(int, int)}
	 */
	private static final String SUB_LIST_REFUSAL = "A subList view of a UniqueList reads and"
		+ " removes only; set and add through the list itself";

	/**
	 * Constructs an empty {@link UniqueList} instance with the specified initial capacity
	 *
	 * @param initialCapacity
	 *            the initial capacity
	 */
	public UniqueList(int initialCapacity)
	{
		super(initialCapacity);
	}

	/**
	 * Constructs an empty {@link UniqueList} instance with an initial capacity of ten
	 */
	public UniqueList()
	{
	}

	/**
	 * Constructs a {@link UniqueList} instance containing the elements of the given collection
	 *
	 * @param collection
	 *            the collection
	 */
	public UniqueList(Collection<? extends E> collection)
	{
		addAll(collection);
	}

	/**
	 * Appends the given element to the end of this list unless this list contains it already, by
	 * {@link #contains(Object)}, which compares with equals. An element this list contains already
	 * keeps its position, unlike in {@link #add(int, Object)}, which moves such an element.
	 *
	 * @param element
	 *            the element to append
	 * @return true if the element was appended, false if this list contains it already
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
	 * Inserts the given element at the given index if this list does not contain it yet: the
	 * element at the index and the elements after it move one position to the right, as in
	 * {@link ArrayList#add(int, Object)}.
	 * <p>
	 * If this list contains the element already, the element moves instead: it is removed from its
	 * position and inserted again, so that it ends at the given index, or at the end of this list
	 * if the index is {@code size()}. The size of this list does not change then; {@code [a, b]}
	 * becomes {@code [b, a]} with {@code add(0, b)} and with {@code add(2, a)}.
	 *
	 * @param index
	 *            the index to insert the element at
	 * @param element
	 *            the element to insert or to move
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range ({@code index < 0 || index > size()}), checked as
	 *             {@link ArrayList#add(int, Object)} checks it, before this list changes
	 */
	@Override
	public void add(int index, E element)
	{
		rangeCheckForAdd(index);
		// an element this list contains leaves its position first; the list is one shorter then,
		// and the index size() it had before is its end
		remove(element);
		super.add(Math.min(index, size()), element);
	}

	/**
	 * Appends the elements of the given collection that this list does not contain yet to the end
	 * of this list, in the order of the collection's iterator and each of them once, as
	 * {@link #add(Object)} appends each of them. The elements this list contains already keep their
	 * positions.
	 *
	 * @param collection
	 *            the collection with the elements to append
	 * @return true if this list changed as a result of the call, that is if the collection holds an
	 *         element this list did not contain
	 */
	@Override
	public boolean addAll(Collection<? extends E> collection)
	{
		boolean changed = false;
		for (E element : collection)
		{
			changed |= add(element);
		}
		return changed;
	}

	/**
	 * Inserts the elements of the given collection that this list does not contain yet at the given
	 * index, in the order of the collection's iterator and each of them once: the element at the
	 * index and the elements after it move to the right, as in
	 * {@link ArrayList#addAll(int, Collection)}. The elements this list contains already keep their
	 * positions, unlike in {@link #add(int, Object)}, which moves such an element; {@code [x]}
	 * becomes {@code [a, b, x]} with {@code addAll(0, [a, x, a, b])}.
	 *
	 * @param index
	 *            the index to insert the first new element at
	 * @param collection
	 *            the collection with the elements to insert
	 * @return true if this list changed as a result of the call, that is if the collection holds an
	 *         element this list did not contain
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range ({@code index < 0 || index > size()}), checked as
	 *             {@link ArrayList#addAll(int, Collection)} checks it, before this list changes and
	 *             for an empty collection too
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> collection)
	{
		rangeCheckForAdd(index);
		int position = index;
		for (E element : collection)
		{
			if (!contains(element))
			{
				super.add(position++, element);
			}
		}
		return position != index;
	}

	/**
	 * Replaces the element at the given index with the given element, so that this list holds each
	 * element once afterwards too.
	 * <p>
	 * If this list holds the given element at another index, the two elements swap: the given
	 * element goes to the given index, and the element that was at the given index goes to the
	 * index the given element was at; {@code [a, b, c]} becomes {@code [c, b, a]} with
	 * {@code set(0, c)}. A {@code set} that threw for such an element, or that removed its other
	 * occurrence, would break the algorithms of {@link java.util.Collections} that work through
	 * {@code set}: {@link java.util.Collections#swap(java.util.List, int, int)}, which calls
	 * {@code l.set(i, l.set(j, l.get(i)))}, and
	 * {@link java.util.Collections#reverse(java.util.List)},
	 * {@link java.util.Collections#shuffle(java.util.List, java.util.Random)} and
	 * {@link java.util.Collections#rotate(java.util.List, int)}, which swap or pass on the element
	 * {@code set} returns; with the swap they keep working on this list.
	 * <p>
	 * If this list holds the given element at the given index, or does not hold it, the element at
	 * the index is replaced, as in {@link ArrayList#set(int, Object)}.
	 * <p>
	 * The size of this list does not change, and neither case is a structural modification: an
	 * iterator running over this list goes on without a {@link ConcurrentModificationException}.
	 *
	 * @param index
	 *            the index of the element to replace
	 * @param element
	 *            the element to store at the index
	 * @return the element previously at the index, as {@link java.util.List#set(int, Object)}
	 *         returns it
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range ({@code index < 0 || index >= size()}), checked as
	 *             {@link ArrayList#set(int, Object)} checks it, before this list changes
	 */
	@Override
	public E set(int index, E element)
	{
		int heldAt = indexOf(element);
		E previous = super.set(index, element);
		if (heldAt >= 0 && heldAt != index)
		{
			super.set(heldAt, previous);
		}
		return previous;
	}

	/**
	 * Replaces each element of this list with the value the given operator gives for it, as
	 * {@link ArrayList#replaceAll(UnaryOperator)} does, if the values are distinct, so that this
	 * list holds each element once afterwards too.
	 * <p>
	 * The operator is applied to every element first, in the order of this list. If it gives two
	 * values that are equal, compared with {@link Object#equals(Object)} as {@link #add(Object)}
	 * compares, this method throws an {@link IllegalArgumentException} and replaces no element.
	 * Otherwise it replaces every element with its value. A value may equal an element of this list
	 * that it does not replace: {@code [a, b, c]} becomes {@code [b, c, a]} with the operator that
	 * gives b for a, c for b and a for c.
	 *
	 * @param operator
	 *            the operator to apply to each element
	 * @throws NullPointerException
	 *             if the operator is null
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
		UniqueList<E> values = new UniqueList<>(size());
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
		Iterator<E> value = values.iterator();
		super.replaceAll(element -> value.next());
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
		return new UniqueListIterator(0);
	}

	/**
	 * Returns a list iterator over the elements of this list, starting at the given index, as
	 * {@link ArrayList#listIterator(int)} does; it is fail-fast as that one.
	 * <p>
	 * Its {@link ListIterator#set(Object)} replaces the element last returned as
	 * {@link #set(int, Object)} replaces it, and swaps an element this list holds at another index.
	 * Its {@link ListIterator#add(Object)} inserts the element immediately before the element
	 * {@link ListIterator#next()} would return, as the contract of {@link ListIterator} says: a
	 * following {@code next()} returns that element, and a following {@code previous()} returns the
	 * given one. An element this list holds moves there, as {@link #add(int, Object)} moves it; the
	 * size of this list does not change then, and {@code nextIndex()} does not grow if the element
	 * was before the cursor. If the given element is the one {@code next()} would return, it stays
	 * where it is and the cursor moves behind it.
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
		rangeCheckForAdd(index);
		return new UniqueListIterator(index);
	}

	/**
	 * Returns a view of the elements of this list between the given indexes, as
	 * {@link ArrayList#subList(int, int)} does, that reads and removes and refuses every other
	 * change.
	 * <p>
	 * Reading, {@code remove}, {@code clear}, {@code removeIf}, {@code removeAll} and
	 * {@code retainAll} through the view, and the remove of its iterators, work as on the view of
	 * {@link ArrayList}: a removal leaves each element once, and
	 * {@code list.subList(from, to).clear()} removes a range of this list.
	 * <p>
	 * Set, add, addAll, replaceAll and sort through the view, and the set and add of its list
	 * iterators, throw an {@link UnsupportedOperationException}, whatever the arguments: this list
	 * moves an element it holds when it is added again, and swaps it in set, so that an element can
	 * leave the view or come into it from outside, which the view of ArrayList, which counts every
	 * added element as one of its own, does not follow; set and replaceAll of that view wrote the
	 * backing array and put an element into this list twice. A sort only reorders inside the view
	 * and keeps each element once; it is refused as well, so that the views of this list and of the
	 * sorted lists of this package follow one rule: they read and remove.
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
	 * Checks the given index of an insertion as {@link ArrayList} checks it, with the same message
	 *
	 * @param index
	 *            the index to insert at
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range ({@code index < 0 || index > size()})
	 */
	private void rangeCheckForAdd(int index)
	{
		if (index < 0 || index > size())
		{
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
		}
	}

	/**
	 * The list iterator of {@link UniqueList}: it walks, removes and fails fast as the list
	 * iterator of {@link ArrayList}, sets through {@link UniqueList#set(int, Object)}, and keeps
	 * its cursor in front of the element {@code next()} would return when it adds an element the
	 * list holds before the cursor
	 */
	private final class UniqueListIterator implements ListIterator<E>
	{
		/**
		 * The index of the element {@code next()} returns
		 */
		private int cursor;

		/**
		 * The index of the element {@code next()} or {@code previous()} returned last, -1 if there
		 * is none or it was removed, or an element was added since
		 */
		private int lastReturned = -1;

		/**
		 * The modification count of the list this iterator expects, as it changed it last
		 */
		private int expectedModCount = modCount;

		/**
		 * Constructs a list iterator that starts at the given index
		 *
		 * @param index
		 *            the index of the first element {@code next()} returns
		 */
		private UniqueListIterator(int index)
		{
			cursor = index;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext()
		{
			return cursor != size();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public E next()
		{
			checkForComodification();
			int index = cursor;
			if (index >= size())
			{
				throw new NoSuchElementException();
			}
			cursor = index + 1;
			lastReturned = index;
			return get(index);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasPrevious()
		{
			return cursor != 0;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public E previous()
		{
			checkForComodification();
			int index = cursor - 1;
			if (index < 0)
			{
				throw new NoSuchElementException();
			}
			cursor = index;
			lastReturned = index;
			return get(index);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int nextIndex()
		{
			return cursor;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int previousIndex()
		{
			return cursor - 1;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove()
		{
			if (lastReturned < 0)
			{
				throw new IllegalStateException();
			}
			checkForComodification();
			UniqueList.this.remove(lastReturned);
			cursor = lastReturned;
			lastReturned = -1;
			expectedModCount = modCount;
		}

		/**
		 * Replaces the element last returned by {@code next()} or {@code previous()} as
		 * {@link UniqueList#set(int, Object)} replaces it: an element the list holds at another
		 * index swaps with it
		 *
		 * @param element
		 *            the element to store
		 */
		@Override
		public void set(E element)
		{
			if (lastReturned < 0)
			{
				throw new IllegalStateException();
			}
			checkForComodification();
			UniqueList.this.set(lastReturned, element);
		}

		/**
		 * Inserts the given element immediately before the element {@code next()} would return, as
		 * {@link UniqueList#listIterator(int)} describes it: an element the list holds before the
		 * cursor moves to the index before the cursor, which then stays where it is; any other
		 * element goes to the cursor, which moves one behind it
		 *
		 * @param element
		 *            the element to insert or to move
		 */
		@Override
		public void add(E element)
		{
			checkForComodification();
			int heldAt = indexOf(element);
			int index = heldAt >= 0 && heldAt < cursor ? cursor - 1 : cursor;
			UniqueList.this.add(index, element);
			cursor = index + 1;
			lastReturned = -1;
			expectedModCount = modCount;
		}

		/**
		 * Checks that the list was not changed structurally other than through this iterator
		 *
		 * @throws ConcurrentModificationException
		 *             if it was
		 */
		private void checkForComodification()
		{
			if (modCount != expectedModCount)
			{
				throw new ConcurrentModificationException();
			}
		}
	}
}
