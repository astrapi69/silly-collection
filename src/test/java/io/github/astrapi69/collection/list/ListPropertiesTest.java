/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.collection.list;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;
import java.util.function.Supplier;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.astrapi69.collection.array.ArrayExtensions;
import io.github.astrapi69.collection.array.ArrayFactory;
import io.github.astrapi69.collection.test.EdgeCases;

/**
 * Properties of the methods of {@link ListFactory} and {@link ListExtensions} that their Javadoc
 * promises, checked for the inputs of {@link EdgeCases} (#179), among them the methods that
 * {@link ArrayExtensions} has for an array too, which must answer the same for the list and the
 * array of the same elements
 */
public class ListPropertiesTest
{

	private static final String LISTS = "io.github.astrapi69.collection.test.EdgeCases#stringLists";
	private static final String LISTS_WITH_ELEMENT = "io.github.astrapi69.collection.test.EdgeCases#stringListsWithElement";
	private static final String LIST_PAIRS = "io.github.astrapi69.collection.test.EdgeCases#stringListPairs";
	private static final String SHORT_RANGES = "io.github.astrapi69.collection.test.EdgeCases#shortIntPairs";
	private static final String TOO_LONG_RANGES = "io.github.astrapi69.collection.test.EdgeCases#tooLongIntPairs";

	/**
	 * The array of the elements of a list
	 *
	 * @param list
	 *            the list
	 * @return the array
	 */
	private static String[] array(final List<String> list)
	{
		return list.toArray(new String[0]);
	}

	/**
	 * What a call gives, or the type of the exception it throws, to compare the list with the array
	 *
	 * @param call
	 *            the call
	 * @return the result, or the class of the exception
	 */
	private static Object outcome(final Supplier<Object> call)
	{
		try
		{
			return call.get();
		}
		catch (final RuntimeException e)
		{
			return e.getClass();
		}
	}

	/**
	 * Property of {@link ListFactory#newArrayList(Object...)},
	 * {@link ListFactory#newLinkedList(Object...)} and {@link ListFactory#newVector(Object...)}:
	 * the new {@link ArrayList}, {@link LinkedList} and {@link Vector} holds "the given optional
	 * elements", each one, in their order, also null and a duplicate
	 *
	 * @param list
	 *            the elements
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource(LISTS)
	public void testNewListsHoldTheElementsInTheirOrder(final List<String> list)
	{
		final String[] elements = array(list);
		assertInstanceOf(ArrayList.class, ListFactory.newArrayList(elements));
		assertEquals(list, ListFactory.newArrayList(elements));
		assertInstanceOf(LinkedList.class, ListFactory.newLinkedList(elements));
		assertEquals(list, ListFactory.newLinkedList(elements));
		assertInstanceOf(Vector.class, ListFactory.newVector(elements));
		assertEquals(list, ListFactory.newVector(elements));
	}

	/**
	 * Property of {@link ListFactory#newArrayList(java.util.Collection, Object...)},
	 * {@link ListFactory#newLinkedList(java.util.Collection, Object...)} and
	 * {@link ListFactory#newVector(java.util.Collection, Object...)}: "from the given optional
	 * collection and the given optional elements", the elements of the collection and then the
	 * given elements, for every pair of collections
	 *
	 * @param collection
	 *            the collection
	 * @param elements
	 *            the given elements
	 */
	@ParameterizedTest(name = "{0} and {1}")
	@MethodSource(LIST_PAIRS)
	public void testNewListsHoldTheCollectionAndThenTheElements(final List<String> collection,
		final List<String> elements)
	{
		final List<String> expected = new ArrayList<>(collection);
		expected.addAll(elements);
		final String[] given = array(elements);
		assertEquals(expected, ListFactory.newArrayList(collection, given));
		assertEquals(expected, ListFactory.newLinkedList(collection, given));
		assertEquals(expected, ListFactory.newVector(collection, given));
	}

	/**
	 * Property of {@link ListFactory#newUniqueList(Object...)}: "The list holds the given elements,
	 * each element once where it comes first", for every collection
	 *
	 * @param list
	 *            the elements
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource(LISTS)
	public void testNewUniqueListHoldsEachElementOnceWhereItComesFirst(final List<String> list)
	{
		assertEquals(new ArrayList<>(new LinkedHashSet<>(list)),
			ListFactory.newUniqueList(array(list)));
	}

	/**
	 * Property of {@link ListFactory#newUniqueList(java.util.Collection, Object...)}: "The list
	 * holds the elements of the collection first, then the given elements, each element once where
	 * it comes first", for every pair of collections
	 *
	 * @param collection
	 *            the collection
	 * @param elements
	 *            the given elements
	 */
	@ParameterizedTest(name = "{0} and {1}")
	@MethodSource(LIST_PAIRS)
	public void testNewUniqueListHoldsTheCollectionFirstThenTheElements(
		final List<String> collection, final List<String> elements)
	{
		final List<String> all = new ArrayList<>(collection);
		all.addAll(elements);
		assertEquals(new ArrayList<>(new LinkedHashSet<>(all)),
			ListFactory.newUniqueList(collection, array(elements)));
	}

	/**
	 * Property of {@link ListFactory#newRangeList(int, int)}: "Creates a new Integer List with the
	 * given range that is defined through start and end", the same numbers as
	 * {@link ArrayFactory#newRangeArray(int, int)}, from the start, with {@code end - start + 1}
	 * elements, for every short range of the edge cases (#113)
	 *
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 */
	@ParameterizedTest(name = "{0} to {1}")
	@MethodSource(SHORT_RANGES)
	public void testNewRangeListHoldsTheNumbersOfTheRangeArray(final int start, final int end)
	{
		final List<Integer> actual = ListFactory.newRangeList(start, end);
		assertEquals(EdgeCases.rangeSize(start, end), actual.size());
		assertEquals(Arrays.asList(ArrayFactory.newRangeArray(start, end)), actual);
		assertEquals(start, actual.get(0));
		assertEquals(end, actual.get(actual.size() - 1));
	}

	/**
	 * Property of {@link ListFactory#newRangeList(int, int)}: "@throws IllegalArgumentException if
	 * end is less than start, or if the range holds more than {@link Integer#MAX_VALUE} numbers",
	 * for every range of the edge cases that holds more
	 *
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 */
	@ParameterizedTest(name = "{0} to {1}")
	@MethodSource(TOO_LONG_RANGES)
	public void testNewRangeListThrowsForMoreNumbersThanAnArrayHolds(final int start, final int end)
	{
		assertThrows(IllegalArgumentException.class, () -> ListFactory.newRangeList(start, end));
	}

	/**
	 * Property of {@link ListExtensions#revertOrder(List)}: "Reverts the order from the given
	 * List": the first is the last of the given list and so on, the given list is not changed, and
	 * reverting the result gives the given list again, for every collection
	 *
	 * @param list
	 *            the list
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource(LISTS)
	public void testRevertOrderReversesAndRevertingAgainGivesTheList(final List<String> list)
	{
		final List<String> expected = new ArrayList<>(list);
		java.util.Collections.reverse(expected);
		final List<String> reverted = ListExtensions.revertOrder(list);
		assertEquals(expected, reverted);
		assertEquals(list, ListExtensions.revertOrder(reverted));
	}

	/**
	 * Property of {@link ListExtensions#toIndexMap(List)}: "an index map from the given list", the
	 * element at index i is the value of the key i, and the map has the size of the list, for every
	 * collection
	 *
	 * @param list
	 *            the list
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource(LISTS)
	public void testToIndexMapMapsEachIndexToItsElement(final List<String> list)
	{
		final Map<Integer, String> actual = ListExtensions.toIndexMap(list);
		assertEquals(list.size(), actual.size());
		for (int i = 0; i < list.size(); i++)
		{
			assertEquals(list.get(i), actual.get(i), "index " + i);
		}
	}

	/**
	 * Property of {@link ListExtensions#getFirst(List)} and {@link ListExtensions#getLast(List)}
	 * against {@link ArrayExtensions#getFirst(Object[])} and
	 * {@link ArrayExtensions#getLast(Object[])}: the first (the last) "object from the given List,
	 * also when it is null, or null if the List is empty", the same as for the array of the
	 * elements
	 *
	 * @param list
	 *            the list
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource(LISTS)
	public void testGetFirstAndGetLastAnswerTheSameForListAndArray(final List<String> list)
	{
		final String[] array = array(list);
		assertEquals(ArrayExtensions.getFirst(array), ListExtensions.getFirst(list));
		assertEquals(ArrayExtensions.getLast(array), ListExtensions.getLast(list));
	}

	/**
	 * Property of {@link ListExtensions#isFirst(List, Object)} and
	 * {@link ListExtensions#isLast(List, Object)} against
	 * {@link ArrayExtensions#isFirst(Object[], Object)} and
	 * {@link ArrayExtensions#isLast(Object[], Object)}: the element is compared with the first (the
	 * last) "with {@link java.util.Objects#equals(Object, Object)}, so null is the first of a list
	 * that starts with null", an empty list has none, the same as for the array of the elements,
	 * for every collection and each of a, b, c, null and an element no list holds
	 *
	 * @param list
	 *            the list
	 * @param element
	 *            the element
	 */
	@ParameterizedTest(name = "{0}, {1}")
	@MethodSource(LISTS_WITH_ELEMENT)
	public void testIsFirstAndIsLastAnswerTheSameForListAndArray(final List<String> list,
		final String element)
	{
		final String[] array = array(list);
		assertEquals(ArrayExtensions.isFirst(array, element),
			ListExtensions.isFirst(list, element));
		assertEquals(ArrayExtensions.isLast(array, element), ListExtensions.isLast(list, element));
		assertEquals(!list.isEmpty() && java.util.Objects.equals(list.get(0), element),
			ListExtensions.isFirst(list, element));
		assertEquals(
			!list.isEmpty() && java.util.Objects.equals(list.get(list.size() - 1), element),
			ListExtensions.isLast(list, element));
	}

	/**
	 * Property of {@link ListExtensions#hasNext(List, Object)} and
	 * {@link ListExtensions#hasPrevious(List, Object)} against
	 * {@link ArrayExtensions#hasNext(Object[], Object)} and
	 * {@link ArrayExtensions#hasPrevious(Object[], Object)}: the answer for the first occurrence of
	 * the element, false at the last (the first) element and if the element is not there, the same
	 * as for the array of the elements, and {@link ArrayExtensions#indexOf(Object[], Object)} gives
	 * the index of the first occurrence, or -1, as {@link List#indexOf(Object)} does
	 *
	 * @param list
	 *            the list
	 * @param element
	 *            the element
	 */
	@ParameterizedTest(name = "{0}, {1}")
	@MethodSource(LISTS_WITH_ELEMENT)
	public void testHasNextHasPreviousAndIndexOfAnswerTheSameForListAndArray(
		final List<String> list, final String element)
	{
		final String[] array = array(list);
		final int index = list.indexOf(element);
		assertEquals(index, ArrayExtensions.indexOf(array, element));
		assertEquals(index, ArrayExtensions.getIndex(array, element));
		assertEquals(index >= 0, ArrayExtensions.contains(array, element));
		assertEquals(index >= 0 && index < list.size() - 1, ListExtensions.hasNext(list, element));
		assertEquals(index > 0, ListExtensions.hasPrevious(list, element));
		assertEquals(ArrayExtensions.hasNext(array, element),
			ListExtensions.hasNext(list, element));
		assertEquals(ArrayExtensions.hasPrevious(array, element),
			ListExtensions.hasPrevious(list, element));
	}

	/**
	 * Property of {@link ListExtensions#getNextElement(List, Object)} and
	 * {@link ListExtensions#getPreviousElement(List, Object)} against
	 * {@link ArrayExtensions#getNextElement(Object[], Object)} and
	 * {@link ArrayExtensions#getPreviousElement(Object[], Object)}: the element after (before) the
	 * first occurrence of the element, "an empty {@link Optional} if the {@link List} has no next
	 * element", the same as for the array of the elements; where the neighbour is null an
	 * {@link Optional} cannot hold it, and both throw the same exception
	 *
	 * @param list
	 *            the list
	 * @param element
	 *            the element
	 */
	@ParameterizedTest(name = "{0}, {1}")
	@MethodSource(LISTS_WITH_ELEMENT)
	public void testGetNextAndGetPreviousElementAnswerTheSameForListAndArray(
		final List<String> list, final String element)
	{
		final String[] array = array(list);
		assertEquals(outcome(() -> ArrayExtensions.getNextElement(array, element)),
			outcome(() -> ListExtensions.getNextElement(list, element)));
		assertEquals(outcome(() -> ArrayExtensions.getPreviousElement(array, element)),
			outcome(() -> ListExtensions.getPreviousElement(list, element)));
	}

	/**
	 * Property of {@link ListExtensions#getFirstElement(List)} and
	 * {@link ListExtensions#getLastElement(List)} against
	 * {@link ArrayExtensions#getFirstElement(Object[])} and
	 * {@link ArrayExtensions#getLastElement(Object[])}: an {@link Optional} with the first (the
	 * last) object, empty for an empty collection, the same for the list and the array of the
	 * elements; "An {@link Optional} cannot hold null, so a null first object throws a
	 * {@link NullPointerException}" for the list, and the array "an empty {@link Optional} if the
	 * array is null or empty or its first object is null"
	 *
	 * @param list
	 *            the list
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource(LISTS)
	public void testGetFirstElementAndGetLastElementOfListAndArray(final List<String> list)
	{
		final String[] array = array(list);
		if (list.isEmpty() || list.get(0) != null)
		{
			assertEquals(ArrayExtensions.getFirstElement(array),
				ListExtensions.getFirstElement(list));
		}
		else
		{
			assertThrows(NullPointerException.class, () -> ListExtensions.getFirstElement(list));
			assertEquals(Optional.empty(), ArrayExtensions.getFirstElement(array));
		}
		if (list.isEmpty() || list.get(list.size() - 1) != null)
		{
			assertEquals(ArrayExtensions.getLastElement(array),
				ListExtensions.getLastElement(list));
		}
		else
		{
			assertThrows(NullPointerException.class, () -> ListExtensions.getLastElement(list));
			assertEquals(Optional.empty(), ArrayExtensions.getLastElement(array));
		}
	}

	/**
	 * Property of {@link ListExtensions#removeFirst(List)} and
	 * {@link ListExtensions#removeLast(List)}: "Removes and returns the first object from the given
	 * List, also when it is null, or null if the List is empty" (the last): the list is left
	 * without that element, and without the first (the last) the same as
	 * {@link ArrayExtensions#removeFirst(Object[])} gives for the array
	 * ({@link ArrayExtensions#removeLast(Object[])})
	 *
	 * @param list
	 *            the list
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource(LISTS)
	public void testRemoveFirstAndRemoveLastOfListAndArray(final List<String> list)
	{
		final String[] array = array(list);
		final List<String> first = new ArrayList<>(list);
		final List<String> last = new ArrayList<>(list);
		assertEquals(ListExtensions.getFirst(list), ListExtensions.removeFirst(first));
		assertEquals(ListExtensions.getLast(list), ListExtensions.removeLast(last));
		if (list.isEmpty())
		{
			assertEquals(list, first);
			assertEquals(list, last);
		}
		else
		{
			assertEquals(Arrays.asList(ArrayExtensions.removeFirst(array)), first);
			assertEquals(Arrays.asList(ArrayExtensions.removeLast(array)), last);
		}
	}

	/**
	 * Property of {@link ListExtensions#toArray(List)}: "the new array with the elements of the
	 * given list", the same as {@link ListExtensions#toArray(Object...)} gives for the array of the
	 * elements, and "@throws IllegalArgumentException if the given list is null or empty", for
	 * every collection
	 *
	 * @param list
	 *            the list
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource(LISTS)
	public void testToArrayHoldsTheElementsOfTheList(final List<String> list)
	{
		if (list.isEmpty())
		{
			assertThrows(IllegalArgumentException.class, () -> ListExtensions.toArray(list));
		}
		else
		{
			assertArrayEquals(array(list), ListExtensions.toArray(list));
		}
	}

}
