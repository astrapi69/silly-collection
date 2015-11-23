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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections4.ComparatorUtils;

import de.alpharogroup.collections.modifications.ModifiedCollections;
import de.alpharogroup.comparators.ComparableComparator;

/**
 * Extensions class for use with List objects.
 *
 * @author Asterios Raptis
 * @version 1.0
 */
public class ListExtensions
{

	/**
	 * This Method look in the List toSearch if at least one Object exists in the List search.
	 *
	 * @param <T>
	 *            the generic type
	 * @param toSearch
	 *            The List to search.
	 * @param search
	 *            The List to inspect.
	 * @return Returns true if there is at least one Object equal from the two List otherwise false.
	 */
	public static <T> boolean containAtleastOneObject(final List<T> toSearch, final List<T> search)
	{
		boolean contains = false;
		final int size = toSearch.size();
		for (int i = 0; i < size; i++)
		{
			contains = search.contains(toSearch.get(i));
			if (contains)
			{
				break;
			}
		}
		return contains;
	}

	/**
	 * Gets the first object from the given List.
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the List.
	 * @return Returns the first object from the given List or null if the List is empty or null.
	 */
	public static <T> T getFirst(final List<T> list)
	{
		if (list != null && !list.isEmpty())
		{
			return list.get(0);
		}
		return null;
	}

	/**
	 * Gets the last object from the given List.
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the List.
	 * @return Returns the last object from the given List or null if the List is empty or null.
	 */
	public static <T> T getLast(final List<T> list)
	{
		if (!isEmpty(list) && 0 < list.size())
		{
			return list.get(list.size() - 1);
		}
		return null;
	}

	/**
	 * Gets the modified lists. finding from an old list which elements have been removed and which
	 * have been added.
	 *
	 * @param <T>
	 *            the generic type
	 * @param previous
	 *            the previous collection i.e. the collection from database.
	 * @param next
	 *            the next collection i.e. the current collection in the view.
	 * @return 's the ModifiedLists in which the lists are keeped.
	 */
	public static <T> ModifiedCollections<T> getModifiedCollections(final Collection<T> previous,
		final Collection<T> next)
	{
		return new ModifiedCollections<T>().getModifiedLists(previous, next);
	}

	/**
	 * The Method looks at both List and if they have same objects they are added to one List and
	 * will returns the result.
	 *
	 * @param <T>
	 *            the generic type
	 * @param toSearch
	 *            The List to search.
	 * @param search
	 *            The List to inspect.
	 * @return The List with the same objects.
	 */
	public static <T> List<T> getSameElementsFromLists(final List<T> toSearch, final List<T> search)
	{
		List<T> foundElements = null;
		final int size = toSearch.size();
		for (int i = 0; i < size; i++)
		{
			final T element = toSearch.get(i);
			if (search.contains(element))
			{
				if (foundElements == null)
				{
					foundElements = new ArrayList<>();
				}
				foundElements.add(element);
			}
		}
		return foundElements;
	}

	/**
	 * Checks if a List is null or empty.
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            The List to check.
	 * @return true if the list is null or empty otherwise false.
	 */
	public static <T> boolean isEmpty(final List<T> list)
	{
		return list == null || list.isEmpty();
	}

	/**
	 * Checks if the given List is not null or empty.
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            The List to check.
	 * @return true if the list is null or empty otherwise false.
	 */
	public static <T> boolean isNotEmpty(final List<T> list)
	{
		return list != null && !list.isEmpty();
	}

	/**
	 * Creates a new {@link Integer} array with the given range that is defined through start and
	 * end. For instance if the start is 5 and the end is 9 the resulted array will be [5,6,7,8,9]
	 *
	 * @param start
	 *            The number to start
	 * @param end
	 *            The number to end minus one
	 * @return the generated {@link Integer} array
	 */
	public static Integer[] newRangeArray(final int start, final int end)
	{
		if (end < start)
		{
			throw new IllegalArgumentException(
				"Parameter end should be greater than parameter start.");
		}
		final int length = end - start + 1;
		final Integer[] array = new Integer[length];
		for (int i = start; i <= end; i++)
		{
			array[i - start] = i;
		}
		return array;
	}

	/**
	 * Creates a new {@link Integer} {@link List} with the given range that is defined through start
	 * and end. For instance if the start is 5 and the end is 9 the resulted {@link List} will be
	 * [5,6,7,8,9]
	 *
	 * @param start
	 *            The number to start
	 * @param end
	 *            The number to end minus one
	 * @return the generated {@link Integer} List
	 */
	public static List<Integer> newRangeList(final int start, final int end)
	{
		return Arrays.asList(newRangeArray(start, end));
	}

	/**
	 * Helper-Method for printing a Collection in the console.
	 *
	 * @param <T>
	 *            the generic type
	 * @param collection
	 *            The Collection to print.
	 */
	public static <T> void printCollection(final Collection<T> collection)
	{
		int count = 1;
		for (final T element : collection)
		{
			System.err.println(count + ".)element:" + element);
			count++;
		}
	}

	/**
	 * Removes the first object from the given List.
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the List.
	 * @return Removes and returns the first object from the given List or null if the List is empty
	 *         or null.
	 */
	public static <T> T removeFirst(final List<T> list)
	{
		if (!isEmpty(list) && 0 < list.size())
		{
			return list.remove(0);
		}
		return null;
	}

	/**
	 * Removes the last object from the given List.
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the List.
	 * @return Removes and returns the last object from the given List or null if the List is empty
	 *         or null.
	 */
	public static <T> T removeLast(final List<T> list)
	{
		if (!isEmpty(list) && 0 < list.size())
		{
			return list.remove(list.size() - 1);
		}
		return null;
	}

	/**
	 * The Method removeLastValues(ArrayList, int) remove the last Values.
	 *
	 * @param <T>
	 *            the generic type
	 * @param v
	 *            The Vector with the received Messages.
	 * @param remove
	 *            How much to remove.
	 * @return the list
	 */
	public static <T> List<T> removeLastValues(final ArrayList<T> v, final int remove)
	{
		if (remove < v.size())
		{
			final List<T> l = v.subList(remove, v.size());
			return l;
		}
		throw new IllegalArgumentException("You cannot remove "
			+ "more element than in the ArrayList exists. \nSize from ArrayList:" + v.size() + "\n"
			+ "Elements to be removed:" + remove + "\n The same ArrayList will be returned.");
	}

	/**
	 * Reverts the order from the given List.
	 *
	 * @param <T>
	 *            the generic type
	 * @param listToRevert
	 *            The List to revert.
	 * @return The reverted List.
	 */
	public static <T> List<T> revertOrder(final List<T> listToRevert)
	{
		final List<T> revertedList = new ArrayList<>();

		int size = listToRevert.size();

		while (0 < size)
		{
			revertedList.add(listToRevert.get(--size));
		}

		return revertedList;
	}

	/**
	 * Shuffle selected elements in the source list to the destination list from the given indexes
	 * in the array selectedElements.
	 *
	 * @param <T>
	 *            the generic type
	 * @param source
	 *            the source
	 * @param destination
	 *            the destination
	 * @param selectedElements
	 *            the selected elements
	 */
	public static <T> void shuffle(final List<T> source, final List<T> destination,
		final int[] selectedElements)
	{
		final int lastIndex = selectedElements.length - 1;
		for (int i = lastIndex; -1 < i; i--)
		{
			final int selectedRow = selectedElements[i];
			final T row = source.remove(selectedRow);
			destination.add(row);
		}
	}

	/**
	 * Sort over the given property. Note: the property should be implement the Comparable
	 * interface.
	 *
	 * @param <T>
	 *            the generic type of the list
	 * @param list
	 *            the list to sort.
	 * @param property
	 *            the property to sort.
	 * @param ascending
	 *            if true the sort will be ascending ohterwise descending.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> void sortByProperty(final List<T> list, final String property,
		final boolean ascending)
	{
		Comparator comparator = new BeanComparator(property, new ComparableComparator());
		if (ascending)
		{
			comparator = ComparatorUtils.reversedComparator(comparator);
		}
		Collections.sort(list, comparator);
	}

	/**
	 * Splits the List to Parts to the specified times.
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            The List to Split
	 * @param times
	 *            How to split.
	 * @return An List with the Splitted Parts
	 */
	public static <T> List<List<T>> splitListToParts(final List<T> list, final int times)
	{
		final List<List<T>> returnList = new ArrayList<>();
		List<T> tmp = new ArrayList<>();
		final Iterator<T> it = list.iterator();
		int count = 0;
		while (it.hasNext())
		{
			if (count == times)
			{
				returnList.add(tmp);
				tmp = new ArrayList<>();
				tmp.add(it.next());
				count = 1;
			}
			else
			{
				tmp.add(it.next());
				count++;
			}
		}
		if (!tmp.isEmpty())
		{
			returnList.add(tmp);
		}
		return returnList;
	}

	/**
	 * Splits the Set to Parts to the specified times.
	 *
	 * @param <T>
	 *            the generic type
	 * @param set
	 *            The Set to Split
	 * @param times
	 *            How to split.
	 * @return An ArrayList with the Splitted Parts
	 */
	public static <T> List<List<T>> splitSetToParts(final Set<T> set, final int times)
	{
		final List<List<T>> returnList = new ArrayList<>();
		ArrayList<T> tmp = new ArrayList<>();
		final Iterator<T> it = set.iterator();
		int count = 0;
		while (it.hasNext())
		{
			if (count == times)
			{
				returnList.add(tmp);
				tmp = new ArrayList<>();
				tmp.add(it.next());
				count = 1;
			}
			else
			{
				tmp.add(it.next());
				count++;
			}
		}
		if (!tmp.isEmpty())
		{
			returnList.add(tmp);
		}
		return returnList;
	}

	/**
	 * Converts the given parameters to an object array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param t
	 *            The objects that will be in the returned object array
	 * 
	 * @return An Object array.
	 */
	@SafeVarargs
	public static <T> Object[] toObjectArray(final T... t)
	{
		final Object[] decorator = new Object[t.length];
		System.arraycopy(t, 0, decorator, 0, t.length);
		return decorator;
	}

	/**
	 * Converts the given enumaration to a Vector.
	 *
	 * @param <T>
	 *            the generic type
	 * @param enumaration
	 *            The Enumeration to convert.
	 * 
	 * @return A new Vector with the content of the given Enumeration.
	 */
	public static <T> Vector<T> toVector(final Enumeration<T> enumaration)
	{
		final Vector<T> vector = new Vector<T>();
		while (enumaration.hasMoreElements())
		{
			vector.add(enumaration.nextElement());
		}
		return vector;
	}
}
