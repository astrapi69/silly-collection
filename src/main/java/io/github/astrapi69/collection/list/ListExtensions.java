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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import io.github.astrapi69.check.Argument;
import io.github.astrapi69.check.Check;
import io.github.astrapi69.collection.CollectionExtensions;
import io.github.astrapi69.collection.array.ArrayFactory;
import io.github.astrapi69.collection.map.MapFactory;
import io.github.astrapi69.collection.modification.ModifiedCollections;
import io.github.astrapi69.comparator.factory.ComparatorFactory;

/**
 * Extensions class for use with {@link List} objects.
 *
 * @author Asterios Raptis
 * @version 1.0
 */
public final class ListExtensions
{

	/**
	 * Private constructor to prevent instantiation
	 */
	private ListExtensions()
	{
	}

	/**
	 * Gets the first object from the given List or null if it does not exist
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the List, may be null
	 * @return Returns the first object from the given List, also when it is null, or null if the
	 *         List is empty or null.
	 */
	public static <T> T getFirst(final List<T> list)
	{
		return CollectionExtensions.isEmpty(list) ? null : list.get(0);
	}

	/**
	 * Gets the last object from the given List.
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the List, may be null
	 * @return Returns the last object from the given List, also when it is null, or null if the
	 *         List is empty or null.
	 */
	public static <T> T getLast(final List<T> list)
	{
		return CollectionExtensions.isEmpty(list) ? null : list.get(list.size() - 1);
	}

	/**
	 * Compute in recursive manner all combinations of the given arguments
	 *
	 * @param allCombinations
	 *            the all combinations
	 * @param possibleNumbers
	 *            the possible numbers
	 * @param currentCombination
	 *            the current combination
	 * @param currentStart
	 *            the current start
	 * @param currentEnd
	 *            the current end
	 * @param currentCombinationIndex
	 *            the current combination index
	 * @param combinationSize
	 *            the combination size
	 */
	private static void computeAllCombinations(List<List<Integer>> allCombinations,
		List<Integer> possibleNumbers, Integer[] currentCombination, int currentStart,
		int currentEnd, int currentCombinationIndex, int combinationSize)
	{
		if (currentCombinationIndex == combinationSize)
		{
			allCombinations.add(ListFactory.newArrayList(currentCombination));
			return;
		}

		for (int i = currentStart; i <= currentEnd
			&& currentEnd - i + 1 >= combinationSize - currentCombinationIndex; i++)
		{
			currentCombination[currentCombinationIndex] = possibleNumbers.get(i);
			computeAllCombinations(allCombinations, possibleNumbers, currentCombination, i + 1,
				currentEnd, currentCombinationIndex + 1, combinationSize);
		}
	}

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
	public static <T> boolean containAtLeastOneObject(final List<T> toSearch, final List<T> search)
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
	 * Clones the given list by creating a new one from it.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the list to clone
	 * @return a new cloned list
	 */
	public static <T> List<T> clone(final List<T> list)
	{
		return ListFactory.newArrayList(list);
	}

	/**
	 * Gets all possible combinations from the given list of {@link Integer} objects
	 *
	 * @param possibleNumbers
	 *            the possible numbers
	 * @param combinationSize
	 *            the size of the combination to generate
	 * @return all possible combinations from the given list of {@link Integer} objects
	 */
	public static List<List<Integer>> getAllCombinations(final List<Integer> possibleNumbers,
		int combinationSize)
	{
		Argument.notNull(possibleNumbers, "possibleNumbers");
		Integer[] currentCombination = new Integer[combinationSize];
		List<List<Integer>> allCombinations = ListFactory.newArrayList();
		int currentEnd = possibleNumbers.size() - 1;
		int currentStart = 0;
		int currentCombinationIndex = 0;
		computeAllCombinations(allCombinations, possibleNumbers, currentCombination, currentStart,
			currentEnd, currentCombinationIndex, combinationSize);
		return allCombinations;
	}

	/**
	 * Gets all possible combinations from the given list
	 *
	 * @param <T>
	 *            the generic type of the elements in the list
	 * @param combinationSize
	 *            the size of the elements of the combinations to generate
	 * @param possibleValues
	 *            the list with the element values
	 * @return all possible combinations from the given list
	 */
	public static <T> List<List<T>> getCombinations(final List<T> possibleValues,
		final int combinationSize)
	{
		List<List<T>> combinations = ListFactory.newArrayList();
		if (combinationSize == 0)
		{
			combinations.add(ListFactory.newArrayList());
			return combinations;
		}
		for (int i = 0; i < possibleValues.size(); i++)
		{
			T element = possibleValues.get(i);
			List<T> rest = getPartialList(possibleValues, i + 1);
			for (List<T> previous : getCombinations(rest, combinationSize - 1))
			{
				previous.add(element);
				combinations.add(previous);
			}
		}
		return combinations;
	}

	/**
	 * Returns an {@link Optional} with the first object from the given {@link List}. An
	 * {@link Optional} cannot hold null, so a null first object throws a
	 * {@link NullPointerException}, as {@link java.util.stream.Stream#findFirst()} does;
	 * {@link #getFirst(List)} gives it as null
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param list
	 *            the {@link List} object
	 * @return an {@link Optional} with the first object from the given {@link List} or an empty
	 *         {@link Optional} if the List is empty
	 * @throws IllegalArgumentException
	 *             if the given list is null
	 * @throws NullPointerException
	 *             if the first object is null
	 */
	public static <T> Optional<T> getFirstElement(final List<T> list)
	{
		Argument.notNull(list, "list");
		if (CollectionExtensions.isNotEmpty(list))
		{
			return Optional.of(list.get(0));
		}
		return Optional.empty();
	}

	/**
	 * Returns an {@link Optional} with the last object from the given {@link List}. An
	 * {@link Optional} cannot hold null, so a null last object throws a
	 * {@link NullPointerException}, as {@link java.util.stream.Stream#findFirst()} does for the
	 * first; {@link #getLast(List)} gives it as null
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param list
	 *            the {@link List} object
	 * @return an {@link Optional} with the last object from the given {@link List} or an empty
	 *         {@link Optional} if the List is empty
	 * @throws IllegalArgumentException
	 *             if the given list is null
	 * @throws NullPointerException
	 *             if the last object is null
	 */
	public static <T> Optional<T> getLastElement(final List<T> list)
	{
		Argument.notNull(list, "list");
		if (CollectionExtensions.isNotEmpty(list))
		{
			return Optional.of(list.get(list.size() - 1));
		}
		return Optional.empty();
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
	 * @return 's the <code>ModifiedCollections</code> in which the <code>Collection</code> objects
	 *         are stored
	 */
	public static <T> ModifiedCollections<T> getModifiedCollections(final Collection<T> previous,
		final Collection<T> next)
	{
		return new ModifiedCollections<T>().getModifiedLists(previous, next);
	}

	/**
	 * Gets the next element from the given {@link List}. As start point is the given element
	 *
	 * @param <T>
	 *            the generic type of elements
	 * @param list
	 *            the list
	 * @param element
	 *            the element
	 * @return an {@link Optional} with the next element from the given {@link List} or an empty
	 *         {@link Optional} if the {@link List} has no next element
	 */
	public static <T> Optional<T> getNextElement(final List<T> list, final T element)
	{
		Argument.notNull(list, "list");
		if (ListExtensions.hasNext(list, element))
		{
			int nextIndex = list.indexOf(element) + 1;
			return Optional.of(list.get(nextIndex));
		}
		return Optional.empty();
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
	private static <T> List<T> getPartialList(List<T> list, int i)
	{
		List<T> partialList = ListFactory.newArrayList();
		for (int j = i; j < list.size(); j++)
		{
			partialList.add(list.get(j));
		}
		return partialList;
	}

	/**
	 * Gets the previous element from the given {@link List}. As start point is the given element
	 *
	 * @param <T>
	 *            the generic type of elements
	 * @param list
	 *            the list
	 * @param element
	 *            the element
	 * @return an {@link Optional} with the previous element from the given {@link List} or an empty
	 *         {@link Optional} if the {@link List} has no previous element
	 */
	public static <T> Optional<T> getPreviousElement(final List<T> list, final T element)
	{
		Argument.notNull(list, "list");
		final int indexOfElement = list.indexOf(element);
		if (indexOfElement == -1 || indexOfElement == 0)
		{
			return Optional.empty();
		}
		int previousIndex = indexOfElement - 1;
		return Optional.of(list.get(previousIndex));
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
		return ListFactory.newArrayList(toSearch);
	}

	/**
	 * Checks if the given {@link List} has a next element from the given element
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the list
	 * @param element
	 *            the element
	 * @return true, if successful
	 */
	public static <T> boolean hasNext(final List<T> list, final T element)
	{
		Argument.notNull(list, "list");
		final int indexOfElement = list.indexOf(element);
		if (indexOfElement == -1)
		{
			return false;
		}
		return indexOfElement < list.size() - 1;
	}

	/**
	 * Checks if the given {@link List} has a previous element from the given element
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the list
	 * @param element
	 *            the element
	 * @return true, if successful
	 */
	public static <T> boolean hasPrevious(final List<T> list, final T element)
	{
		Argument.notNull(list, "list");
		final int indexOfElement = list.indexOf(element);
		return indexOfElement != -1 && indexOfElement != 0;
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
		Optional<Boolean> optionalEvaluation = CollectionExtensions
			.preconditionOfEqualCollection(one, other);
		if (optionalEvaluation.isPresent())
		{
			return optionalEvaluation.get();
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
	 * Checks if the given element is the last in the given list. The element is compared with the
	 * last element of the list with {@link Objects#equals(Object, Object)}, so null is the last of
	 * a list that ends with null. An empty list and null have no last element
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the list, may be null
	 * @param element
	 *            the element, may be null
	 * @return true if the given element is the last otherwise false, also if the list is empty or
	 *         null
	 */
	public static <T> boolean isLast(final List<T> list, final T element)
	{
		return !CollectionExtensions.isEmpty(list)
			&& Objects.equals(list.get(list.size() - 1), element);
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
	 *            the index to rearrange
	 * @return the rearranged {@link List}
	 */
	public static <T> List<T> rearrange(List<T> listToResort, T element, int rearrangeToIndex)
	{
		Argument.notNull(element, "element");
		Argument.notNull(listToResort, "listToResort");
		int index = listToResort.indexOf(element);
		if (index < 0 || index == rearrangeToIndex || listToResort.size() == rearrangeToIndex)
		{
			return listToResort;
		}
		List<T> resortedList;
		resortedList = ListFactory.newArrayList(listToResort);
		resortedList.remove(index);
		resortedList.add(rearrangeToIndex, element);
		return resortedList;
	}

	/**
	 * Relocated the given element in the given {@link List} to the given index
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param list
	 *            the list that contains the element that have to be relocated
	 * @param element
	 *            the element to relocated
	 * @param newIndex
	 *            the new index to relocated
	 */
	public static <T> void relocate(List<T> list, T element, int newIndex)
	{
		Argument.notNull(element, "element");
		Argument.notNull(list, "list");
		int index = list.indexOf(element);
		if (index < 0 || index == newIndex)
		{
			return;
		}
		list.remove(index);
		if (list.size() <= newIndex)
		{
			list.add(element);
		}
		else
		{
			list.add(newIndex, element);
		}
	}

	/**
	 * Removes the first object from the given List.
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the List, may be null
	 * @return Removes and returns the first object from the given List, also when it is null, or
	 *         null if the List is empty or null.
	 */
	public static <T> T removeFirst(final List<T> list)
	{
		return CollectionExtensions.isEmpty(list) ? null : list.remove(0);
	}

	/**
	 * Returns an {@link Optional} with the first object if it was removed from the given
	 * {@link List}. An {@link Optional} cannot hold null, so a null first object throws a
	 * {@link NullPointerException}, as {@link java.util.stream.Stream#findFirst()} does, before
	 * anything is removed; {@link #removeFirst(List)} removes it and gives it as null
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param list
	 *            the {@link List} object
	 * @return returns an {@link Optional} with the first object if it was removed from the given
	 *         {@link List} or an empty {@link Optional} if the {@link List} is empty
	 * @throws IllegalArgumentException
	 *             if the given list is null
	 * @throws NullPointerException
	 *             if the first object is null, and the list stays as it is
	 */
	public static <T> Optional<T> removeFirstElement(final List<T> list)
	{
		Argument.notNull(list, "list");
		if (!CollectionExtensions.isEmpty(list))
		{
			// Optional.of throws for null before the element is removed
			final Optional<T> first = Optional.of(list.get(0));
			list.remove(0);
			return first;
		}
		return Optional.empty();
	}

	/**
	 * Removes the last object from the given List.
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the List, may be null
	 * @return Removes and returns the last object from the given List, also when it is null, or
	 *         null if the List is empty or null.
	 */
	public static <T> T removeLast(final List<T> list)
	{
		return CollectionExtensions.isEmpty(list) ? null : list.remove(list.size() - 1);
	}

	/**
	 * Returns an {@link Optional} with the last object if it was removed from the given
	 * {@link List}. An {@link Optional} cannot hold null, so a null last object throws a
	 * {@link NullPointerException}, as {@link java.util.stream.Stream#findFirst()} does for the
	 * first, before anything is removed; {@link #removeLast(List)} removes it and gives it as null
	 *
	 * @param <T>
	 *            the generic type of the elements
	 * @param list
	 *            the {@link List} object
	 * @return returns an {@link Optional} with the last object if it was removed from the given
	 *         {@link List} or an empty {@link Optional} if the {@link List} is empty
	 * @throws IllegalArgumentException
	 *             if the given list is null
	 * @throws NullPointerException
	 *             if the last object is null, and the list stays as it is
	 */
	public static <T> Optional<T> removeLastElement(final List<T> list)
	{
		Argument.notNull(list, "list");
		if (!CollectionExtensions.isEmpty(list))
		{
			final int lastIndex = list.size() - 1;
			// Optional.of throws for null before the element is removed
			final Optional<T> last = Optional.of(list.get(lastIndex));
			list.remove(lastIndex);
			return last;
		}
		return Optional.empty();
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
	 * @return the given list without its last values
	 * @throws IllegalArgumentException
	 *             if remove is greater than the size of the given list
	 */
	public static <T> List<T> removeLastValues(final List<T> v, final int remove)
	{
		if (remove <= v.size())
		{
			return v.subList(0, v.size() - remove);
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
		final List<T> revertedList = ListFactory.newArrayList();

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
	 * <p>
	 * The elements at the given indexes of the source, each index once, are added to the end of the
	 * destination in the order of the indexes and removed from the source; the other elements stay
	 * in the source in their order. The indexes are those of the source before the call, in any
	 * order. The given array is not changed. An index out of the range of the source throws an
	 * {@link IndexOutOfBoundsException} before any element moves.
	 * </p>
	 *
	 * @param <T>
	 *            the generic type
	 * @param source
	 *            the source
	 * @param destination
	 *            the destination
	 * @param selectedElements
	 *            the indexes of the selected elements in the source
	 * @throws IndexOutOfBoundsException
	 *             if an index is out of the range of the source
	 */
	public static <T> void shuffle(final List<T> source, final List<T> destination,
		final int[] selectedElements)
	{
		final int[] indexes = Arrays.stream(selectedElements).distinct().toArray();
		// take the elements first, so that no removal moves an element another index names
		final List<T> selected = Arrays.stream(indexes).mapToObj(source::get).toList();
		// then remove them from the highest index down, as a removal moves only the elements
		// behind it
		final int[] ascending = Arrays.stream(indexes).sorted().toArray();
		for (int i = ascending.length - 1; 0 <= i; i--)
		{
			source.remove(ascending[i]);
		}
		destination.addAll(selected);
	}

	/**
	 * Extracts a portion of a list between the specified {@code fromIndex}, inclusive, and
	 * {@code toIndex}, exclusive. This method provides a safe way to get a sublist, ensuring that
	 * indices are within the bounds of the original list. If the indices are out of bounds or
	 * {@code fromIndex} is not less than {@code toIndex}, it returns an empty list.
	 *
	 * @param <T>
	 *            the type of elements in the list
	 * @param list
	 *            the original list from which to extract the sublist
	 * @param fromIndex
	 *            the start index(inclusive), inclusive, adjusted to zero if it is negative
	 * @param toIndex
	 *            the end index(exclusive), exclusive, adjusted to the size of the list if it
	 *            exceeds
	 * @return a view of the specified range within the original list, or an empty list if indices
	 *         are invalid
	 * @throws IndexOutOfBoundsException
	 *             if the specified indices are out of bounds (though this method handles typical
	 *             out-of-bounds issues by returning an empty list)
	 * @throws NullPointerException
	 *             if the original list is null
	 */
	public static <T> List<T> subList(List<T> list, int fromIndex, int toIndex)
	{
		if (fromIndex >= list.size() || toIndex <= 0 || fromIndex >= toIndex)
		{
			return ListFactory.newArrayList();
		}
		fromIndex = Math.max(0, fromIndex);
		toIndex = Math.min(list.size(), toIndex);
		return list.subList(fromIndex, toIndex);
	}

	/**
	 * Sort over the given property. Note: the property should implement the Comparable interface.
	 *
	 * @param <T>
	 *            the generic type of the list
	 * @param <U>
	 *            the type of the result of the function
	 * @param list
	 *            the list to sort.
	 * @param beanPropertyFunction
	 *            the function used to extract the bean property and create the {@link Comparator}
	 *            to sort
	 * @param ascending
	 *            if true the sort will be ascending otherwise descending
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T, U extends Comparable<? super U>> void sortByProperty(final List<T> list,
		final Function<? super T, ? extends U> beanPropertyFunction, final boolean ascending)
	{
		Comparator comparator = Comparator.comparing(beanPropertyFunction,
			Comparator.nullsFirst(Comparator.naturalOrder()));
		if (!ascending)
		{
			comparator = comparator.reversed();
		}
		list.sort(comparator);
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
	 *            How to split
	 * @return a List with the split Parts
	 */
	public static <T> List<List<T>> splitToParts(final Collection<T> collection, final int size)
	{
		final List<List<T>> returnList = ListFactory.newArrayList();
		List<T> tmp = ListFactory.newArrayList();
		final Iterator<T> it = collection.iterator();
		int count = 0;
		while (it.hasNext())
		{
			if (count == size)
			{
				returnList.add(tmp);
				tmp = ListFactory.newArrayList();
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
	 * Creates a new array from the given {@link List}, with the elements in the order of the list.
	 * <p>
	 * The component type of the array is the most specific common superclass of the non-null
	 * elements, found by walking up their class hierarchy with {@link Class#getSuperclass()};
	 * interfaces are not considered. If every element is null, the component type is
	 * {@link Object}. So [null, "a"] gives a {@code String[]} that holds null and "a", [1, 2.5] and
	 * [2.5, 1] give a {@code Number[]}, and ["a", 1] gives an {@code Object[]}. Elements of one
	 * class give an array of that class. See {@link CollectionExtensions#toArray(Collection)}.
	 * <p>
	 * The result is a {@code T[]} only if that superclass is {@code T} or a subclass of it. A
	 * caller whose {@code T} is an interface, or a class more specific than that superclass, gets a
	 * {@link ClassCastException} where it assigns the result to a {@code T[]}: a
	 * {@code List<Comparable<?>>} of 1 and "a" gives an {@code Object[]}, and so does a
	 * {@code List<String>} that holds only null.
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the list
	 * @return the new array with the elements of the given list
	 * @throws IllegalArgumentException
	 *             if the given list is null or empty
	 */
	public static <T> T[] toArray(final List<T> list)
	{
		Check.get().notNull(list, "list").notEmpty(list, "list");
		return CollectionExtensions.toArray(list);
	}

	/**
	 * Returns a new array with the given elements, as {@link ArrayFactory#newArray(Object...)}
	 * does.
	 * <p>
	 * The result is a new array, also when no elements are given: a copy of the given array, so
	 * that a write to the result does not change the array of the caller. Null gives null.
	 * </p>
	 *
	 * @param <T>
	 *            the generic type
	 * @param elements
	 *            the elements, may be null
	 * @return a new array with the given elements, or null if the given elements are null
	 */
	@SafeVarargs
	public static <T> T[] toArray(final T... elements)
	{
		return ArrayFactory.newArray(elements);
	}

	/**
	 * Generates an index map from the given list
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the list
	 * @return an index map from the given list
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<Integer, T> toIndexMap(final List<T> list)
	{
		Check.get().notNull(list, "list").notEmpty(list, "list");
		Map<Integer, T> map = MapFactory.newLinkedHashMap(list.size());
		for (int i = 0; i < list.size(); i++)
		{

			map.put(i, list.get(i));
		}
		return map;
	}

	/**
	 * Converts the given enumeration to an ArrayList.
	 *
	 * @param <T>
	 *            the generic type
	 * @param enumeration
	 *            The Enumeration to convert.
	 *
	 * @return A new ArrayList with the content of the given Enumeration.
	 */
	public static <T> List<T> toList(final Enumeration<T> enumeration)
	{
		final List<T> list = ListFactory.newArrayList();
		while (enumeration.hasMoreElements())
		{
			list.add(enumeration.nextElement());
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
		return ListFactory.newArrayList(set);
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


	/**
	 * Gets the index where to insert to another list which is still in construction and needs a
	 * defined order
	 *
	 * @param <T>
	 *            the generic type
	 * @param newSortedList
	 *            the new sorted list
	 * @param sortedList
	 *            the sorted list that is used for the defined order
	 * @param elementToAdd
	 *            the element for resolve the index to add
	 * @return the index where to insert in the new sorted list
	 */
	public static <T> int getIndexToInsert(List<T> newSortedList, List<T> sortedList,
		T elementToAdd)
	{
		List<T> listWithNewElement = ListFactory.newArrayList(newSortedList, elementToAdd);
		listWithNewElement.sort(ComparatorFactory.newDefinedOrderComparator(sortedList));
		return listWithNewElement.indexOf(elementToAdd);
	}

	/**
	 * Gets the index where the given element to insert to the given list with the order of the
	 * given comparator
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the list, sorted in the order of the given comparator
	 * @param element
	 *            the element for resolve the index to add
	 * @param comparator
	 *            the comparator
	 * @return the index of an element of the given list that the given comparator holds equal to
	 *         the given element, whether equals does or not, otherwise the index of the first
	 *         element greater than the given element, or the size of the list if there is none
	 */
	public static <T> int getIndexToInsert(List<? extends T> list, T element,
		Comparator<? super T> comparator)
	{
		int indexToInsert = Collections.binarySearch(list, element, comparator);
		if (indexToInsert < 0)
		{
			indexToInsert = -indexToInsert - 1;
		}
		return indexToInsert;
	}

	/**
	 * Checks if the second given element is before the first given element in the given list
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the list
	 * @param element
	 *            the element
	 * @param elementToCheck
	 *            the element to check if it is before the first given element
	 * @return true if the second given element is before the first given element in the given list
	 *         otherwise false
	 */
	public static <T> boolean isBefore(final List<T> list, final T element, final T elementToCheck)
	{
		nullChecks(list, element, elementToCheck);
		if (list.contains(element) && list.contains(elementToCheck))
		{
			int indexOfElement = list.indexOf(element);
			int indexOfElementToCheck = list.indexOf(elementToCheck);
			return indexOfElementToCheck < indexOfElement;
		}
		return false;
	}

	private static <T> void nullChecks(List<T> list, T element, T elementToCheck)
	{
		Objects.requireNonNull(list);
		Objects.requireNonNull(element);
		Objects.requireNonNull(elementToCheck);
	}

	/**
	 * Checks if the second given element is after the first given element in the given list
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the list
	 * @param element
	 *            the element
	 * @param elementToCheck
	 *            the element to check if it is after the first given element
	 * @return true if the second given element is after the first given element in the given list
	 *         otherwise false
	 */
	public static <T> boolean isAfter(final List<T> list, final T element, final T elementToCheck)
	{
		Objects.requireNonNull(list);
		Objects.requireNonNull(element);
		Objects.requireNonNull(elementToCheck);
		if (list.contains(element) && list.contains(elementToCheck))
		{
			int indexOfElement = list.indexOf(element);
			int indexOfElementToCheck = list.indexOf(elementToCheck);
			return indexOfElement < indexOfElementToCheck;
		}
		return false;
	}

}
