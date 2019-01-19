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
package de.alpharogroup.collections.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections4.ComparatorUtils;

import de.alpharogroup.collections.CollectionExtensions;
import de.alpharogroup.collections.array.ArrayFactory;
import de.alpharogroup.collections.modifications.ModifiedCollections;
import de.alpharogroup.comparators.SortOrderComparator;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * Extensions class for use with {@link List} objects.
 *
 * @author Asterios Raptis
 * @version 1.0
 */
@UtilityClass
public final class ListExtensions
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
		if (CollectionExtensions.isNotEmpty(list))
		{
			return list.get(0);
		}
		return null;
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
	public static <T> Optional<T> getOptionalFirst(final List<T> list)
	{
		if (CollectionExtensions.isNotEmpty(list))
		{
			return Optional.of(list.get(0));
		}
		return Optional.empty();
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
		if (CollectionExtensions.isNotEmpty(list))
		{
			return list.get(list.size() - 1);
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
	public static <T> Optional<T> getOptionalLast(final List<T> list)
	{
		if (CollectionExtensions.isNotEmpty(list))
		{
			return Optional.of(list.get(list.size() - 1));
		}
		return Optional.empty();
	}

	/**
	 * Gets all possible combinations from the given list
	 *
	 * @param <T>
	 *            the generic type of the elements in the list
	 * @param numberOf
	 *            the number of elements to generate the combinations
	 * @param values
	 *            the list with the element values
	 * @return all possible combinations from the given list
	 */
	public static <T> List<List<T>> getCombinations(int numberOf, List<T> values)
	{
		List<List<T>> combinations = new ArrayList<>();
		if (numberOf == 0)
		{
			combinations.add(new ArrayList<T>());
			return combinations;
		}
		for (int i = 0; i < values.size(); i++)
		{
			T element = values.get(i);
			List<T> rest = getPartialList(values, i + 1);
			for (List<T> previous : getCombinations(numberOf - 1, rest))
			{
				previous.add(element);
				combinations.add(previous);
			}
		}
		return combinations;
	}

	/**
	 * Gets the partial list
	 *
	 * @param <T>
	 *            the generic type of the elements in the list
	 * @param list
	 *            the list
	 * @param i
	 *            the i
	 * @return the partial list
	 */
	public static <T> List<T> getPartialList(List<T> list, int i)
	{
		List<T> partialList = ListFactory.newArrayList();
		for (int j = i; j < list.size(); j++)
		{
			partialList.add(list.get(j));
		}
		return partialList;
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
	 * This method decorates the retainAll method and returns the result in a new list
	 *
	 * @param <T>
	 *            the generic type
	 * @param toSearch
	 *            The list to search
	 * @param search
	 *            the list to inspect
	 * @return the new list with the intersection of the objects
	 */
	public static <T> List<T> getSameElementsFromLists(final List<T> toSearch, final List<T> search)
	{
		toSearch.retainAll(search);
		List<T> foundElements = ListFactory.newArrayList(toSearch);
		return foundElements;
	}

	/**
	 * Compare the given two {@link Collection} objects in equality.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param one
	 *            the one
	 * @param other
	 *            the other
	 * @return true, if the given two {@link Collection} objects are equal otherwise false
	 */
	public static <T> boolean isEqualListOfArrays(List<T[]> one, List<T[]> other)
	{
		if (one == null && other == null)
		{
			return true;
		}

		if ((one == null && other != null) || one != null && other == null
			|| one.size() != other.size())
		{
			return false;
		}
		for (int i = 0; i < one.size(); i++)
		{
			if (!Arrays.deepEquals(one.get(i), other.get(i)))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the given element is the first in the given list.
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the list
	 * @param element
	 *            the element
	 * @return true if the given element is the first otherwise false
	 */
	public static <T> boolean isFirst(final List<T> list, final T element)
	{
		final int indexOfElement = list.indexOf(element);
		return indexOfElement == 0;
	}

	/**
	 * Checks if the given element is the last in the given list.
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the list
	 * @param element
	 *            the element
	 * @return true if the given element is the last otherwise false
	 */
	public static <T> boolean isLast(final List<T> list, final T element)
	{
		Optional<T> optionalLast = getOptionalLast(list);
		if (optionalLast.isPresent())
		{
			return optionalLast.get().equals(element);
		}
		return false;
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
	 * Rearrange the order from the given {@link List} to the given rearranged index
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param listToResort
	 *            the list to resort
	 * @param element
	 *            the element to rearrange
	 * @param rearrangeToIndex
	 *            the rearrange to index
	 * @return the rearranged {@link List}
	 */
	public static <T> List<T> rearrange(@NonNull T element, @NonNull List<T> listToResort,
		int rearrangeToIndex)
	{
		int index = listToResort.indexOf(element);
		if (index < 0 || index == rearrangeToIndex || listToResort.size() == rearrangeToIndex)
		{
			return listToResort;
		}
		List<T> resortedList;
		resortedList = new ArrayList<>(listToResort);
		resortedList.remove(index);
		resortedList.add(rearrangeToIndex, element);
		return resortedList;
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
		if (!CollectionExtensions.isEmpty(list) && 0 < list.size())
		{
			return list.remove(0);
		}
		return null;
	}

	/**
	 * Removes the first object from the given List.
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the List.
	 * @return Removes and returns the first object from the given List or an empty {@link Optional}
	 *         if the List is empty or null.
	 */
	public static <T> Optional<T> removeOptionalFirst(final List<T> list)
	{
		if (!CollectionExtensions.isEmpty(list) && 0 < list.size())
		{
			return Optional.of(list.remove(0));
		}
		return Optional.empty();
	}

	/**
	 * Removes the last object from the given List.
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the List.
	 * @return Removes and returns the last object from the given List or an empty {@link Optional}
	 *         if the List is empty or null.
	 */
	public static <T> Optional<T> removeOptionalLast(final List<T> list)
	{
		if (!CollectionExtensions.isEmpty(list) && 0 < list.size())
		{
			return Optional.of(list.remove(list.size() - 1));
		}
		return Optional.empty();
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
		if (!CollectionExtensions.isEmpty(list) && 0 < list.size())
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
	public static <T> List<T> removeLastValues(final List<T> v, final int remove)
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
		Comparator comparator = new BeanComparator(property, SortOrderComparator.of());
		if (ascending)
		{
			comparator = ComparatorUtils.reversedComparator(comparator);
		}
		Collections.sort(list, comparator);
	}

	/**
	 * Splits the given {@link Collection} to parts to the specified size and returns a list with
	 * the parts.
	 *
	 * @param <T>
	 *            the generic type
	 * @param collection
	 *            The collection to split
	 * @param size
	 *            How to split.
	 * @return a List with the splited Parts
	 */
	public static <T> List<List<T>> splitToParts(final Collection<T> collection, final int size)
	{
		final List<List<T>> returnList = new ArrayList<>();
		ArrayList<T> tmp = new ArrayList<>();
		final Iterator<T> it = collection.iterator();
		int count = 0;
		while (it.hasNext())
		{
			if (count == size)
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
	 * To array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param elements
	 *            the elements
	 * @return the t[]
	 */
	@SafeVarargs
	public static <T> T[] toArray(final T... elements)
	{
		final T[] decorator = ArrayFactory.newArray(elements);
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
	public static <T> List<T> toList(final Enumeration<T> enumaration)
	{
		final List<T> list = new ArrayList<>();
		while (enumaration.hasMoreElements())
		{
			list.add(enumaration.nextElement());
		}
		return list;
	}

	/**
	 * Converts the given {@link Set} to a list.
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param set
	 *            the set
	 * @return A new list
	 */
	public static <T> List<T> toList(final Set<T> set)
	{
		return new ArrayList<>(set);
	}

	/**
	 * Converts the given parameter elements to an object array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param elements
	 *            The elements that will be in the returned object array
	 *
	 * @return An Object array.
	 */
	@SafeVarargs
	public static <T> Object[] toObjectArray(final T... elements)
	{
		final Object[] decorator = new Object[elements.length];
		System.arraycopy(elements, 0, decorator, 0, elements.length);
		return decorator;
	}

}
