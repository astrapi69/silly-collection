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

import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * The view that {@link UniqueList#reversed()} returns: the elements of a {@link UniqueList} in the
 * reverse order, the index {@code i} of the view being the index {@code size() - 1 - i} of the
 * list. It reads the list and writes through the methods of {@link UniqueList}, so that the list
 * holds each element once whatever goes through the view.
 * <p>
 * The view of {@link List#reversed()} that the JDK gives a list maps {@code add(i, e)} to
 * {@code add(size() - i, e)} of the list. That is right for an element that makes the list grow,
 * and one place off for an element the list holds, which {@link UniqueList#add(int, Object)} moves
 * instead (#39): it ended at the index {@code i - 1} of the view (#77). This view adds a new
 * element at the list index {@code size() - i}, as the JDK's view does, and moves an element the
 * list holds to the list index {@code size() - 1 - i}, where {@link UniqueList#add(int, Object)}
 * puts it for every index below {@code size()}.
 * <p>
 * <b>Postcondition:</b> after {@code add(i, e)}, {@code get(i)} is {@code e}, for a new element at
 * every index from 0 to {@code size()}, and for an element the list holds at every index from 0 to
 * {@code size() - 1}; the other elements keep their order.
 * <p>
 * What the operations do:
 * <ul>
 * <li>{@code size}, {@code isEmpty}, {@code get}, {@code contains}, {@code containsAll},
 * {@code indexOf}, {@code lastIndexOf}, {@code toArray}, {@code getFirst}, {@code getLast}, the
 * iterators, {@code forEach} and the stream read the list backwards; {@code equals},
 * {@code hashCode} and {@code toString} are those of a {@link List} of the elements in the order of
 * the view, and {@link #reversed()} is the list itself.</li>
 * <li>{@code add(e)} appends a new element to the end of the view, the front of the list, and
 * returns false for an element the list holds, which stays where it is, as
 * {@link UniqueList#add(Object)} does; {@code addAll(c)} adds as add(e) does, each element once in
 * the order of the collection, and returns whether the list changed.</li>
 * <li>{@code add(i, e)} keeps the postcondition; {@code addAll(i, c)} inserts the new elements at
 * the index of the view in the order of the collection, each once, and leaves the held ones where
 * they are, as {@link UniqueList#addAll(int, Collection)} does.</li>
 * <li>{@code addFirst(e)} and {@code addLast(e)} are {@link UniqueList#addLast(Object)} and
 * {@link UniqueList#addFirst(Object)}: the element is first or last of the view afterwards, and a
 * held element moves there.</li>
 * <li>{@code set(i, e)} is {@link UniqueList#set(int, Object)} at the list index: an element the
 * list holds at another index swaps with the one at the index.</li>
 * <li>{@code remove(i)}, {@code remove(o)}, {@code removeFirst}, {@code removeLast},
 * {@code removeIf}, {@code removeAll}, {@code retainAll} and {@code clear} remove from the list;
 * {@code replaceAll} is {@link UniqueList#replaceAll(UnaryOperator)}, which applies the operator in
 * the order of the list; {@code sort(c)} sorts the list with the reverse of the comparator, which
 * sorts the view with it, stable in the order of the view.</li>
 * <li>The list iterator walks the view; its {@code set}, {@code remove} and {@code add} go through
 * {@code set(i, e)}, {@code remove(i)} and {@code add(i, e)} of the view, with the contract of
 * {@link ListIterator}. The iterators fail fast: they throw a
 * {@link ConcurrentModificationException} after a structural change of the list that they did not
 * make themselves, through the list's modification count.</li>
 * <li>{@link #subList(int, int)} returns a view that reads and removes, as
 * {@link UniqueList#subList(int, int)} does.</li>
 * </ul>
 * Refused with an {@link UnsupportedOperationException}, and the list does not change:
 * <ul>
 * <li>{@code add(size(), e)} with an element the list holds: a move does not make the view grow, so
 * the index {@code size()} does not exist after it and {@code get(size())} cannot return the
 * element. {@code addLast(e)} moves the element to the end of the view.</li>
 * <li>{@code add(e)} of the list iterator with an element the list holds before the cursor or at
 * it. This deviates from the list iterator of {@link UniqueList#listIterator(int)}, which moves
 * such an element (#67): {@link ListIterator#add(Object)} says that a following {@code next()} is
 * unaffected and that {@code nextIndex()} grows by one, and a move of an element at or before the
 * cursor cannot keep that. An element before the cursor is before it already, so that
 * {@code nextIndex()} would stay; the element {@code next()} would return would have to come before
 * the cursor, so that {@code next()} would skip it. An element behind the cursor moves to the
 * cursor and keeps the contract.</li>
 * <li>set, add, addAll, replaceAll and sort through a view of {@link #subList(int, int)}, and the
 * set and add of its list iterators, as on the view of {@link UniqueList#subList(int, int)}: the
 * list moves an element it holds, so that it can leave the range of the view.</li>
 * </ul>
 *
 * @param <E>
 *            the generic type of the elements
 */
final class ReversedUniqueListView<E> extends AbstractList<E> implements RandomAccess
{

	/**
	 * The message of the {@link UnsupportedOperationException} of {@code add(size(), e)} with an
	 * element the list holds
	 */
	private static final String MOVE_TO_SIZE_REFUSAL = "The reversed view of a UniqueList does not"
		+ " move an element the list holds to the index size(), which does not exist after a move;"
		+ " addLast moves it to the end of the view";

	/**
	 * The message of the {@link UnsupportedOperationException} of the list iterator's add with an
	 * element the list holds before the cursor or at it
	 */
	private static final String ITERATOR_ADD_REFUSAL = "The list iterator of the reversed view of a"
		+ " UniqueList does not add an element the list holds before the cursor or at it: next()"
		+ " would skip it or nextIndex() would not grow";

	/**
	 * The message of the {@link UnsupportedOperationException} of the view of
	 * {@link #subList(int, int)}
	 */
	private static final String SUB_LIST_REFUSAL = "A subList view of the reversed view of a"
		+ " UniqueList reads and removes only; set and add through the reversed view or the list"
		+ " itself";

	/**
	 * The list this view shows in the reverse order
	 */
	private final UniqueList<E> list;

	/**
	 * Constructs the view of the given list in the reverse order
	 *
	 * @param list
	 *            the list to show in the reverse order
	 */
	ReversedUniqueListView(UniqueList<E> list)
	{
		this.list = list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size()
	{
		return list.size();
	}

	/**
	 * Returns the element at the given index of this view, the index {@code size() - 1 - index} of
	 * the list
	 *
	 * @param index
	 *            the index of the element in this view
	 * @return the element at the given index of this view
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range ({@code index < 0 || index >= size()})
	 */
	@Override
	public E get(int index)
	{
		return list.get(listIndex(index));
	}

	/**
	 * Replaces the element at the given index of this view as {@link UniqueList#set(int, Object)}
	 * replaces it at the index {@code size() - 1 - index} of the list: an element the list holds at
	 * another index swaps with the one at the index
	 *
	 * @param index
	 *            the index of the element in this view
	 * @param element
	 *            the element to store at the index
	 * @return the element previously at the index
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range ({@code index < 0 || index >= size()})
	 */
	@Override
	public E set(int index, E element)
	{
		return list.set(listIndex(index), element);
	}

	/**
	 * Appends the given element to the end of this view, the front of the list, unless the list
	 * holds it already, as {@link UniqueList#add(Object)} appends it: an element the list holds
	 * stays where it is, {@link #addLast(Object)} moves it to the end of this view
	 *
	 * @param element
	 *            the element to append
	 * @return true if the element was appended, false if the list holds it already
	 */
	@Override
	public boolean add(E element)
	{
		if (list.contains(element))
		{
			return false;
		}
		list.add(0, element);
		return true;
	}

	/**
	 * Inserts the given element at the given index of this view, or moves it there if the list
	 * holds it already, so that {@code get(index)} returns it afterwards.
	 * <p>
	 * A new element goes to the index {@code size() - index} of the list, which grows by one, so
	 * that the element is at the index {@code index} of this view; the elements from the index on
	 * move one position to the right in this view. An element the list holds moves to the index
	 * {@code size() - 1 - index} of the list through {@link UniqueList#add(int, Object)}, which
	 * puts it there for every index below {@code size()}; the size does not change, and the other
	 * elements keep their order.
	 *
	 * @param index
	 *            the index of this view to insert the element at or to move it to
	 * @param element
	 *            the element to insert or to move
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range ({@code index < 0 || index > size()}), before the
	 *             list changes
	 * @throws UnsupportedOperationException
	 *             if the list holds the element and the index is {@code size()}: a move does not
	 *             make this view grow, so the index {@code size()} does not exist after it and
	 *             {@code get(size())} cannot return the element; {@link #addLast(Object)} moves it
	 *             to the end of this view
	 */
	@Override
	public void add(int index, E element)
	{
		int size = list.size();
		rangeCheckForAdd(index, size);
		if (!list.contains(element))
		{
			list.add(size - index, element);
		}
		else if (index < size)
		{
			list.add(size - 1 - index, element);
		}
		else
		{
			throw new UnsupportedOperationException(MOVE_TO_SIZE_REFUSAL);
		}
	}

	/**
	 * Inserts the elements of the given collection that the list does not hold yet at the given
	 * index of this view, in the order of the collection's iterator and each of them once, as
	 * {@link UniqueList#addAll(int, Collection)} inserts them into the list: the elements the list
	 * holds already keep their positions. Each new element goes to the same index of the list, in
	 * front of the ones inserted before it there, so that they are in the order of the collection
	 * in this view.
	 *
	 * @param index
	 *            the index of this view to insert the first new element at
	 * @param collection
	 *            the collection with the elements to insert
	 * @return true if the list changed as a result of the call
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range ({@code index < 0 || index > size()}), before the
	 *             list changes and for an empty collection too
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> collection)
	{
		int size = list.size();
		rangeCheckForAdd(index, size);
		int position = size - index;
		boolean changed = false;
		for (E element : collection)
		{
			if (!list.contains(element))
			{
				list.add(position, element);
				changed = true;
			}
		}
		return changed;
	}

	/**
	 * Inserts the given element at the front of this view, or moves it there if the list holds it,
	 * through {@link UniqueList#addLast(Object)}: the front of this view is the end of the list
	 *
	 * @param element
	 *            the element to insert or to move to the front of this view
	 */
	@Override
	public void addFirst(E element)
	{
		list.addLast(element);
	}

	/**
	 * Appends the given element to the end of this view, or moves it there if the list holds it,
	 * through {@link UniqueList#addFirst(Object)}: the end of this view is the front of the list
	 *
	 * @param element
	 *            the element to append or to move to the end of this view
	 */
	@Override
	public void addLast(E element)
	{
		list.addFirst(element);
	}

	/**
	 * Removes the element at the given index of this view, the index {@code size() - 1 - index} of
	 * the list
	 *
	 * @param index
	 *            the index of the element in this view
	 * @return the removed element
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range ({@code index < 0 || index >= size()})
	 */
	@Override
	public E remove(int index)
	{
		return list.remove(listIndex(index));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object element)
	{
		return list.remove(element);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> collection)
	{
		return list.removeAll(collection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAll(Collection<?> collection)
	{
		return list.retainAll(collection);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeIf(Predicate<? super E> filter)
	{
		return list.removeIf(filter);
	}

	/**
	 * Removes all elements of the list
	 */
	@Override
	public void clear()
	{
		list.clear();
	}

	/**
	 * Replaces each element as {@link UniqueList#replaceAll(UnaryOperator)} replaces it, which
	 * applies the operator in the order of the list, the reverse of this view, and throws an
	 * {@link IllegalArgumentException} for two equal values, leaving the list unchanged
	 *
	 * @param operator
	 *            the operator to apply to each element
	 */
	@Override
	public void replaceAll(UnaryOperator<E> operator)
	{
		list.replaceAll(operator);
	}

	/**
	 * Sorts this view with the given comparator, the natural order for null: the list is sorted
	 * with the reverse of the comparator. The sort of the list is stable, so elements that compare
	 * equal keep their order in the list, which is their order in this view backwards; reversed,
	 * they keep their order in this view, as {@link List#sort(Comparator)} says
	 *
	 * @param order
	 *            the comparator to sort this view with, null for the natural order
	 */
	@Override
	public void sort(Comparator<? super E> order)
	{
		list.sort(Collections.reverseOrder(order));
	}

	/**
	 * Returns an iterator over the elements of this view, the list iterator of
	 * {@link #listIterator()}
	 *
	 * @return an iterator over the elements of this view
	 */
	@Override
	public Iterator<E> iterator()
	{
		return listIterator();
	}

	/**
	 * Returns a list iterator over the elements of this view, starting at the given index. It fails
	 * fast through the modification count of the list, sets, removes and adds through
	 * {@link #set(int, Object)}, {@link #remove(int)} and {@link #add(int, Object)} of this view,
	 * and keeps the contract of {@link ListIterator}: its add inserts a new element, or moves one
	 * the list holds behind the cursor, immediately before the element {@code next()} would return,
	 * which a following {@code next()} returns, and {@code nextIndex()} grows by one. Its add
	 * refuses an element the list holds at or before the cursor with an
	 * {@link UnsupportedOperationException}, where the list iterator of
	 * {@link UniqueList#listIterator(int)} moves it (#67): a move there cannot keep that contract.
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
		rangeCheckForAdd(index, list.size());
		return new ReversedListIterator(index);
	}

	/**
	 * Returns a spliterator over the elements of this view, late-binding and fail-fast through the
	 * iterator of this view, as the reversed view of the JDK gives it
	 *
	 * @return a spliterator over the elements of this view
	 */
	@Override
	public Spliterator<E> spliterator()
	{
		return Spliterators.spliterator(this, Spliterator.ORDERED);
	}

	/**
	 * Returns a view of the elements of this view between the given indexes that reads and removes
	 * and refuses every other change, as the view of {@link UniqueList#subList(int, int)} does, in
	 * the order of this view: set, add, addAll, replaceAll and sort through it, and the set and add
	 * of its list iterators, throw an {@link UnsupportedOperationException}, for an empty
	 * collection too, as the list moves an element it holds and it can leave the range of the view
	 *
	 * @param fromIndex
	 *            the index of the first element of the view, inclusive
	 * @param toIndex
	 *            the index after the last element of the view, exclusive
	 * @return a view of the elements between the given indexes that reads and removes
	 * @throws IndexOutOfBoundsException
	 *             if an index is out of range or fromIndex is greater than toIndex, as
	 *             {@link Objects#checkFromToIndex(int, int, int)} checks it
	 */
	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		int size = list.size();
		Objects.checkFromToIndex(fromIndex, toIndex, size);
		return new ReadAndRemoveSubList<>(list.subList(size - toIndex, size - fromIndex).reversed(),
			SUB_LIST_REFUSAL);
	}

	/**
	 * Returns the list itself, whose order is the reverse of the order of this view
	 *
	 * @return the list itself
	 */
	@Override
	public List<E> reversed()
	{
		return list;
	}

	/**
	 * Checks the given index of this view and returns the index of the list it stands for
	 *
	 * @param index
	 *            the index of an element of this view
	 * @return the index {@code size() - 1 - index} of the list
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range ({@code index < 0 || index >= size()})
	 */
	private int listIndex(int index)
	{
		int size = list.size();
		return size - 1 - Objects.checkIndex(index, size);
	}

	/**
	 * Checks the given index of an insertion as {@link UniqueList} checks it, with the same message
	 *
	 * @param index
	 *            the index to insert at
	 * @param size
	 *            the size of this view
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of range ({@code index < 0 || index > size})
	 */
	private static void rangeCheckForAdd(int index, int size)
	{
		if (index < 0 || index > size)
		{
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
	}

	/**
	 * The list iterator of the view: it walks the view, sets, removes and adds through the methods
	 * of the view, and fails fast through the modification count of the list
	 */
	private final class ReversedListIterator implements ListIterator<E>
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
		private int expectedModCount = list.modificationCount();

		/**
		 * Constructs a list iterator that starts at the given index
		 *
		 * @param index
		 *            the index of the first element {@code next()} returns
		 */
		private ReversedListIterator(int index)
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
		 * Removes the element last returned by {@code next()} or {@code previous()} through
		 * {@link ReversedUniqueListView#remove(int)}
		 */
		@Override
		public void remove()
		{
			if (lastReturned < 0)
			{
				throw new IllegalStateException();
			}
			checkForComodification();
			ReversedUniqueListView.this.remove(lastReturned);
			cursor = lastReturned;
			lastReturned = -1;
			expectedModCount = list.modificationCount();
		}

		/**
		 * Replaces the element last returned by {@code next()} or {@code previous()} through
		 * {@link ReversedUniqueListView#set(int, Object)}: an element the list holds at another
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
			ReversedUniqueListView.this.set(lastReturned, element);
		}

		/**
		 * Inserts the given element immediately before the element {@code next()} would return,
		 * through {@link ReversedUniqueListView#add(int, Object)} at the cursor, and moves the
		 * cursor behind it: a new element, or one the list holds behind the cursor, which moves
		 * there
		 *
		 * @param element
		 *            the element to insert or to move
		 * @throws UnsupportedOperationException
		 *             if the list holds the element before the cursor or at it: it is before the
		 *             cursor already, so that {@code nextIndex()} would not grow, or it is the one
		 *             {@code next()} would return, which would skip it, where the contract of
		 *             {@link ListIterator#add(Object)} says that a following {@code next()} is
		 *             unaffected and {@code nextIndex()} grows by one; a deviation from the list
		 *             iterator of {@link UniqueList#listIterator(int)}, which moves such an element
		 *             (#67)
		 */
		@Override
		public void add(E element)
		{
			checkForComodification();
			int heldAt = indexOf(element);
			if (heldAt >= 0 && heldAt <= cursor)
			{
				throw new UnsupportedOperationException(ITERATOR_ADD_REFUSAL);
			}
			ReversedUniqueListView.this.add(cursor, element);
			cursor++;
			lastReturned = -1;
			expectedModCount = list.modificationCount();
		}

		/**
		 * Checks that the list was not changed structurally other than through this iterator
		 *
		 * @throws ConcurrentModificationException
		 *             if it was
		 */
		private void checkForComodification()
		{
			if (list.modificationCount() != expectedModCount)
			{
				throw new ConcurrentModificationException();
			}
		}
	}
}
