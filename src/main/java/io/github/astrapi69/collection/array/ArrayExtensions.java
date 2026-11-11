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
package io.github.astrapi69.collection.array;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.github.astrapi69.check.Argument;
import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.collection.set.SetFactory;

/**
 * The class {@link ArrayExtensions} is an extensions class for use with array objects
 */
public final class ArrayExtensions
{
	/**
	 * Private constructor to prevent instantiation
	 */
	private ArrayExtensions()
	{
	}

	/**
	 * Copy the given source array to the given destination array.<br>
	 * <br>
	 * Note: the given destination array have to be already initialized with the size of the
	 * source.<br>
	 * <br>
	 * Example: <br>
	 * <br>
	 * <code>
	 * Integer[] source = {1,2,3,4,5,6,7,8,9};<br>
	 * Integer[] destination = new Integer[source.length];<br>
	 * destination = ArrayExtensions.arraycopyWithSystem(source, destination);<br>
	 * </code>
	 *
	 *
	 * @param <T>
	 *            the generic type of the objects in the array
	 * @param source
	 *            the source
	 * @param destination
	 *            the destination
	 * @return the t[]
	 */
	public static <T> T[] arraycopyWithSystem(T[] source, T[] destination)
	{
		if (source == null)
		{
			return null;
		}
		System.arraycopy(source, 0, destination, 0, source.length);
		return destination;
	}

	/**
	 * Creates a new {@link List} from the given array <br>
	 * <br>
	 * Note: This wraps only the method asList from {@link Arrays#asList(Object...)}
	 *
	 * @param <T>
	 *            the generic type of the objects in the array
	 * @param array
	 *            the array
	 * @return the new {@link List} created from the given array
	 */
	public static <T> List<T> asList(final T[] array)
	{
		return Arrays.asList(array);
	}

	/**
	 * Creates a new {@link Set} from the given array <br>
	 * <br>
	 *
	 * @param <T>
	 *            the generic type of the objects in the array
	 * @param array
	 *            the array
	 * @return the new {@link Set} created from the given array
	 */
	@SafeVarargs
	public static <T> Set<T> asSet(T... array)
	{
		return Stream.of(array).collect(Collectors.toSet());
	}

	/**
	 * Creates a new {@link Stream} from the given array <br>
	 * <br>
	 *
	 * @param <T>
	 *            the generic type of the objects in the array
	 * @param array
	 *            the array
	 * @return the new {@link Stream} created from the given array
	 */
	@SafeVarargs
	public static <T> Stream<T> asStream(T... array)
	{
		return Arrays.stream(array);
	}

	/**
	 * Returns <code>true</code> if and only if the given element is in the given array
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @return <code>true</code> if and only if the given element is in the given array otherwise
	 *         <code>false</code>
	 */
	public static <T> boolean contains(final T[] array, final T element)
	{
		return indexOf(array, element) >= 0;
	}

	/**
	 * Returns a concatenated array from given arrays.
	 * <p>
	 * If the given elements are null or empty, the existing array itself is returned, also when it
	 * is null: {@code concatenate(null, new Integer[0])} returns null and
	 * {@code concatenate(new Integer[0], null)} returns the given empty array. Otherwise, if the
	 * existing array is null, the elements array itself is returned. In all other cases the result
	 * is a new array of the component type of the existing array, with the elements of the existing
	 * array followed by the given elements.
	 * </p>
	 *
	 * @param <T>
	 *            the generic type
	 * @param existingArray
	 *            the existing array, may be null
	 * @param elements
	 *            the elements that will be concatenated, may be null
	 * @return the existing array itself if the given elements are null or empty, else the elements
	 *         array itself if the existing array is null, else a new concatenated array from the
	 *         given arrays
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] concatenate(T[] existingArray, T[] elements)
	{
		if (elements == null || elements.length == 0)
		{
			return existingArray;
		}
		if (existingArray == null)
		{
			return elements;
		}
		int existingArrayLength = existingArray.length;
		int elementsLength = elements.length;

		T[] concatenatedArray = (T[])ArrayFactory.newArray(
			existingArray.getClass().getComponentType(), existingArrayLength + elementsLength);
		System.arraycopy(existingArray, 0, concatenatedArray, 0, existingArrayLength);
		System.arraycopy(elements, 0, concatenatedArray, existingArrayLength, elementsLength);
		return concatenatedArray;
	}

	/**
	 * Returns <code>true</code> if at least one of given elements is in the given array
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param elements
	 *            the elements to check if it is containing in the given array
	 * @return <code>true</code> if at least one of given elements is in the given array otherwise
	 *         <code>false</code>
	 */
	@SafeVarargs
	public static <T> boolean containsAtLeastOne(final T[] array, final T... elements)
	{
		boolean contains = false;
		for (T element : elements)
		{
			if (contains(array, element))
			{
				contains = true;
				break;
			}
		}
		return contains;
	}

	/**
	 * Gets the first object from the given array
	 *
	 * @param <T>
	 *            the generic type of the elements in the array
	 * @param array
	 *            the array
	 * @return Returns the first object from the given array or null if the array is empty or null
	 */
	public static <T> T getFirst(final T[] array)
	{
		return getFirstElement(array).orElse(null);
	}

	/**
	 * Returns an {@link Optional} with the first object from the given array
	 *
	 * @param <T>
	 *            the generic type of the elements in the array
	 * @param array
	 *            the array
	 * @return an {@link Optional} with the first object from the given array or an empty
	 *         {@link Optional} if the array is null or empty or its first object is null
	 */
	public static <T> Optional<T> getFirstElement(final T[] array)
	{
		if (array != null && array.length != 0)
		{
			return Optional.ofNullable(array[0]);
		}
		return Optional.empty();
	}

	/**
	 * Gets the index of the given element in the given array
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @return the int
	 */
	public static <T> int getIndex(final T[] array, final T element)
	{
		return ArrayExtensions.indexOf(array, element);
	}

	/**
	 * Gets the last object from the given array
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @return Returns the last object from the given array or null if the array is empty or null
	 */
	public static <T> T getLast(final T[] array)
	{
		return getLastElement(array).orElse(null);
	}

	/**
	 * Returns an {@link Optional} with the last object from the given array
	 *
	 * @param <T>
	 *            the generic type of the elements in the array
	 * @param array
	 *            the array
	 * @return an {@link Optional} with the last object from the given array or an empty
	 *         {@link Optional} if the array is null or empty or its last object is null
	 */
	public static <T> Optional<T> getLastElement(final T[] array)
	{
		if (array != null && array.length != 0)
		{
			return Optional.ofNullable(array[getLastIndex(array)]);
		}
		return Optional.empty();
	}

	/**
	 * Gets the last index from the given array.
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @return Returns the last index from the given array or -1 if the array is null
	 */
	public static <T> int getLastIndex(final T[] array)
	{
		if (array == null)
		{
			return -1;
		}
		return array.length - 1;
	}

	/**
	 * Gets the last index from the given array
	 *
	 * @param array
	 *            the array
	 * @return Returns the last index from the given array or -1 if the array is null
	 */
	public static int getLastIndex(final boolean[] array)
	{
		if (array == null)
		{
			return -1;
		}
		return array.length - 1;
	}

	/**
	 * Gets the last index from the given array
	 *
	 * @param array
	 *            the array
	 * @return Returns the last index from the given array or -1 if the array is null
	 */
	public static int getLastIndex(final byte[] array)
	{
		if (array == null)
		{
			return -1;
		}
		return array.length - 1;
	}

	/**
	 * Gets the last index from the given array
	 *
	 * @param array
	 *            the array
	 * @return Returns the last index from the given array or -1 if the array is null
	 */
	public static int getLastIndex(final char[] array)
	{
		if (array == null)
		{
			return -1;
		}
		return array.length - 1;
	}

	/**
	 * Gets the last index from the given array
	 *
	 * @param array
	 *            the array
	 * @return Returns the last index from the given array or -1 if the array is null
	 */
	public static int getLastIndex(final float[] array)
	{
		if (array == null)
		{
			return -1;
		}
		return array.length - 1;
	}

	/**
	 * Gets the last index from the given array
	 *
	 * @param array
	 *            the array
	 * @return Returns the last index from the given array or -1 if the array is null
	 */
	public static int getLastIndex(final long[] array)
	{
		if (array == null)
		{
			return -1;
		}
		return array.length - 1;
	}

	/**
	 * Gets the last index from the given array
	 *
	 * @param array
	 *            the array
	 * @return Returns the last index from the given array or -1 if the array is null
	 */
	public static int getLastIndex(final short[] array)
	{
		if (array == null)
		{
			return -1;
		}
		return array.length - 1;
	}

	/**
	 * Gets the last index from the given array
	 *
	 * @param array
	 *            the array
	 * @return Returns the last index from the given array or -1 if the array is null
	 */
	public static int getLastIndex(final double[] array)
	{
		if (array == null)
		{
			return -1;
		}
		return array.length - 1;
	}

	/**
	 * Gets the last index from the given array
	 *
	 * @param array
	 *            the array
	 * @return Returns the last index from the given array or -1 if the array is null
	 */
	public static int getLastIndex(final int[] array)
	{
		if (array == null)
		{
			return -1;
		}
		return array.length - 1;
	}

	/**
	 * Gets the next index of the given array
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @return the next index
	 */
	public static <T> int getNextIndex(final T[] array, final T element)
	{
		final int indexOfElement = ArrayExtensions.indexOf(array, element);
		if (indexOfElement == -1)
		{
			return indexOfElement;
		}
		final int lastIndex = getLastIndex(array);
		if (indexOfElement == lastIndex)
		{
			return 0;
		}
		return indexOfElement + 1;
	}

	/**
	 * Gets the next indexes from the given count of the given array
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @param count
	 *            the count
	 * @return the next indexes or null if there is no next indexes
	 */
	public static <T> int[] getNextIndexes(final T[] array, final T element, final int count)
	{
		if (element != null)
		{
			final int[] nextIndexes = new int[count];
			T currentElement = element;
			for (int i = 0; i < count; i++)
			{
				final int nextIndex = getNextIndex(array, currentElement);
				if (nextIndex == -1)
				{
					return null;
				}
				nextIndexes[i] = nextIndex;
				currentElement = array[nextIndex];
			}
			return nextIndexes;
		}
		return null;
	}

	/**
	 * Gets the previous index of the given array
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @return the previous index
	 */
	public static <T> int getPreviousIndex(final T[] array, final T element)
	{
		final int lastIndex = getLastIndex(array);
		final int indexOfElement = ArrayExtensions.indexOf(array, element);
		if (indexOfElement == -1)
		{
			return indexOfElement;
		}
		if (indexOfElement == 0)
		{
			return lastIndex;
		}
		return indexOfElement - 1;
	}

	/**
	 * Gets the previous indexes from the given count of the given array
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @param count
	 *            the count
	 * @return the previous indexes or null if there is no previous indexes
	 */
	public static <T> int[] getPreviousIndexes(final T[] array, final T element, final int count)
	{
		if (element != null)
		{
			final int[] previousIndexes = new int[count];
			T currentElement = element;
			for (int i = 0; i < count; i++)
			{
				final int previousIndex = getPreviousIndex(array, currentElement);
				if (previousIndex == -1)
				{
					return null;
				}
				previousIndexes[i] = previousIndex;
				currentElement = array[previousIndex];
			}
			return previousIndexes;
		}
		return null;
	}

	/**
	 * Checks if the given array has a next element from the given element
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @return true, if successful
	 */
	public static <T> boolean hasNext(final T[] array, final T element)
	{
		Argument.notNull(array, "array");
		final int indexOfElement = indexOf(array, element);
		if (indexOfElement == -1)
		{
			return false;
		}
		return indexOfElement < array.length - 1;
	}

	/**
	 * Checks if the given array has a previous element from the given element
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @return true, if successful
	 */
	public static <T> boolean hasPrevious(final T[] array, final T element)
	{
		Argument.notNull(array, "array");
		final int indexOfElement = indexOf(array, element);
		return indexOfElement != -1 && indexOfElement != 0;
	}

	/**
	 * Gets the previous element from the given array. As start point is the given element
	 *
	 * @param <T>
	 *            the generic type of elements
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @return an {@link Optional} with the previous element from the given array or an empty
	 *         {@link Optional} if the array has no previous element
	 */
	public static <T> Optional<T> getPreviousElement(final T[] array, final T element)
	{
		Argument.notNull(array, "array");
		final int indexOfElement = indexOf(array, element);
		if (indexOfElement == -1 || indexOfElement == 0)
		{
			return Optional.empty();
		}
		int previousIndex = indexOfElement - 1;
		return Optional.of(array[previousIndex]);
	}

	/**
	 * Gets the next element from the given array. As start point is the given element
	 *
	 * @param <T>
	 *            the generic type of elements
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @return an {@link Optional} with the next element from the given array or an empty
	 *         {@link Optional} if the array has no next element
	 */
	public static <T> Optional<T> getNextElement(final T[] array, final T element)
	{
		Argument.notNull(array, "array");
		if (ArrayExtensions.hasNext(array, element))
		{
			int nextIndex = getNextIndex(array, element);
			return Optional.of(array[nextIndex]);
		}
		return Optional.empty();
	}

	/**
	 * Gets the index of the given element in the given array
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @return the int
	 */
	public static <T> int indexOf(final T[] array, final T element)
	{
		return Arrays.asList(array).indexOf(element);
	}

	/**
	 * Intersection of the given two arrays: the elements of the first array that the other array
	 * holds, in the order of the first array and with its duplicates, as
	 * {@link java.util.Collection#retainAll(java.util.Collection)} keeps them, so {a, a, b} and {a}
	 * give {a, a} and {a} and {a, a} give {a}. It is not the intersection of Commons Collections'
	 * {@code CollectionUtils#intersection}, which gives the minimum cardinality of each element
	 *
	 * @param <T>
	 *            the generic type
	 * @param one
	 *            the first array
	 * @param other
	 *            the other array
	 * @return a new array of the component type of the first array with the elements of the first
	 *         array that the other array holds
	 * @throws IllegalArgumentException
	 *             if one of the arrays is null
	 */
	public static <T> T[] intersection(final T[] one, final T[] other)
	{
		Argument.notNull(one, "one");
		Argument.notNull(other, "other");
		T[] intersection = ArrayFactory.newEmptyArray(one);
		int j = 0;
		for (int i = 0; i < one.length; i++)
		{
			if (contains(other, one[i]))
			{
				intersection[j++] = one[i];
			}
		}
		return ArrayFactory.newSubArray(intersection, 0, j);
	}

	/**
	 * Checks if the given element is the first in the given array
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array
	 * @param element
	 *            the element
	 * @return true if the given element is the first otherwise false
	 */
	public static <T> boolean isFirst(final T[] array, final T element)
	{
		final int indexOfElement = Arrays.asList(array).indexOf(element);
		return indexOfElement == 0;
	}

	/**
	 * Checks if the given element is the last in the given array. The element is compared with the
	 * last element of the array with {@link Objects#equals(Object, Object)}, so an element that is
	 * also at an index before the last is the last too, and null is the last of an array that ends
	 * with null. An empty array and null have no last element
	 *
	 * @param <T>
	 *            the generic type
	 * @param array
	 *            the array, may be null
	 * @param element
	 *            the element, may be null
	 * @return true if the given element is the last otherwise false, also if the array is empty or
	 *         null
	 */
	public static <T> boolean isLast(final T[] array, final T element)
	{
		return array != null && array.length != 0
			&& Objects.equals(array[array.length - 1], element);
	}

	/**
	 * Joins two arrays of the same type into a single array.
	 * <p>
	 * This method combines the elements of the two input arrays {@code array1} and {@code array2}
	 * into a new array. If either of the arrays is {@code null}, the other array is returned. If
	 * both arrays are {@code null}, the result is {@code null}. The resulting array is of the same
	 * component type as the first array, and it contains all elements from both input arrays.
	 * </p>
	 *
	 * @param <T>
	 *            the component type of the arrays
	 * @param array1
	 *            the first array, may be {@code null}
	 * @param array2
	 *            the second array, may be {@code null}
	 * @return a new array containing all elements of {@code array1} and {@code array2}, or one of
	 *         the arrays if the other is {@code null}
	 * @throws IllegalArgumentException
	 *             if the component types of the two arrays are incompatible
	 * @throws ArrayStoreException
	 *             if an incompatible element is found when copying the arrays
	 */
	@SafeVarargs
	public static <T> T[] join(final T[] array1, final T... array2)
	{
		if (array1 == null)
		{
			return array2;
		}
		if (array2 == null)
		{
			return array1;
		}
		final Class<?> type1 = array1.getClass().getComponentType();
		final Class<?> type2 = array2.getClass().getComponentType();
		if (!type1.isAssignableFrom(type2))
		{
			throw new IllegalArgumentException(
				"Cannot store " + type2.getName() + " in an array of " + type1.getName());
		}
		@SuppressWarnings("unchecked") // OK, because array is of type T
		final T[] joinedArray = (T[])Array.newInstance(type1, array1.length + array2.length);
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}


	/**
	 * Creates a new array from the given array without the elements at the given indexes.
	 * <p>
	 * The indexes refer to the given array and may be given in any order. Each distinct index
	 * removes one element: an index given twice removes the element at this index once, so
	 * {@code remove({ 0, 1, 2, 3 }, 1, 1)} returns {@code { 0, 2, 3 }}. Neither the given array nor
	 * the given indexes array is changed.
	 * </p>
	 *
	 * @param <T>
	 *            the generic type of the objects in the array
	 * @param array
	 *            the origin array
	 * @param indexes
	 *            the indexes of the elements to remove, in any order
	 * @return the new created array without the elements at the given indexes
	 * @throws IndexOutOfBoundsException
	 *             if one of the given indexes is negative or not less than the length of the given
	 *             array, with the message of {@link Objects#checkIndex(int, int)} for the length of
	 *             the given array; the given array is unchanged then too
	 */
	public static <T> T[] remove(final T[] array, int... indexes)
	{
		Argument.notNull(array, "array");
		List<T> list = ListFactory.newArrayList(array);
		// a sorted copy without duplicates, the indexes array of the caller stays as it is
		final int[] distinctIndexes = Arrays.stream(indexes).distinct().sorted().toArray();
		// every index is checked against the given array before any removal, so that the message
		// names the length of the given array
		for (final int index : distinctIndexes)
		{
			Objects.checkIndex(index, array.length);
		}
		// from the highest index down, so that a removal does not move the elements still to remove
		for (int i = distinctIndexes.length - 1; -1 < i; i--)
		{
			list.remove(distinctIndexes[i]);
		}
		return list.toArray(Arrays.copyOf(array, list.size()));
	}

	/**
	 * Creates a new array from the given array without all of its elements that are also contained
	 * in the given array to remove
	 *
	 * @param <T>
	 *            the generic type of the objects in the array
	 * @param array
	 *            the origin array
	 * @param arrayToRemove
	 *            the array to remove
	 * @return the new created array without the elements of the given array to remove
	 */
	public static <T> T[] removeAll(final T[] array, final T[] arrayToRemove)
	{
		Argument.notNull(array, "array");
		Argument.notNull(arrayToRemove, "arrayToRemove");
		List<T> list = ListFactory.newArrayList(array);
		List<T> listToRemove = ListFactory.newArrayList(arrayToRemove);
		list.removeAll(listToRemove);
		return list.toArray(Arrays.copyOf(array, list.size()));
	}

	/**
	 * Creates a new array cloned from the given array with the difference that the first element is
	 * removed
	 *
	 * @param <T>
	 *            the generic type of the objects in the array
	 * @param original
	 *            the origin array
	 * @return the new created array without the first element
	 * @throws IndexOutOfBoundsException
	 *             if the given array is empty
	 */
	public static <T> T[] removeFirst(final T[] original)
	{
		Argument.notNull(original, "original");
		return remove(original, 0);
	}

	/**
	 * Creates a new array from the given array without the given suffix, if the given array ends
	 * with it.
	 * <p>
	 * The array ends with the suffix if its last elements equal those of the suffix, in their
	 * order, compared with {@link Objects#equals(Object, Object)}; an empty suffix is a suffix of
	 * every array. Otherwise, also for a suffix longer than the array, the result is a copy of the
	 * given array, as {@code StringUtils.removeEnd} of Apache Commons Lang does for strings. The
	 * result is always a new array of the component type of the given array; neither of the given
	 * arrays is changed.
	 * </p>
	 *
	 * @param <T>
	 *            the generic type of the objects in the arrays
	 *
	 * @param array
	 *            the array, not null
	 * @param suffix
	 *            the suffix to remove, not null
	 * @return a new array without the suffix, or a copy of the given array if it does not end with
	 *         the suffix
	 * @throws IllegalArgumentException
	 *             if the given array or suffix is null
	 */
	public static <T> T[] removeFromEnd(final T[] array, T[] suffix)
	{
		Argument.notNull(array, "array");
		Argument.notNull(suffix, "suffix");
		final int end = array.length - suffix.length;
		if (end < 0 || !Arrays.equals(array, end, array.length, suffix, 0, suffix.length))
		{
			return array.clone();
		}
		return Arrays.copyOf(array, end);
	}

	/**
	 * Creates a new array from the given array without the given prefix, if the given array starts
	 * with it.
	 * <p>
	 * The array starts with the prefix if its first elements equal those of the prefix, in their
	 * order, compared with {@link Objects#equals(Object, Object)}; an empty prefix is a prefix of
	 * every array. Otherwise, also for a prefix longer than the array, the result is a copy of the
	 * given array, as {@code StringUtils.removeStart} of Apache Commons Lang does for strings. The
	 * result is always a new array of the component type of the given array; neither of the given
	 * arrays is changed.
	 * </p>
	 *
	 * @param <T>
	 *            the generic type of the objects in the arrays
	 *
	 * @param array
	 *            the array, not null
	 * @param prefix
	 *            the prefix to remove, not null
	 * @return a new array without the prefix, or a copy of the given array if it does not start
	 *         with the prefix
	 * @throws IllegalArgumentException
	 *             if the given array or prefix is null
	 */
	public static <T> T[] removeFromStart(final T[] array, final T[] prefix)
	{
		Argument.notNull(array, "array");
		Argument.notNull(prefix, "prefix");
		if (array.length < prefix.length
			|| !Arrays.equals(array, 0, prefix.length, prefix, 0, prefix.length))
		{
			return array.clone();
		}
		return Arrays.copyOfRange(array, prefix.length, array.length);
	}

	/**
	 * Creates a new array cloned from the given array with the difference that the last element is
	 * removed
	 *
	 * @param <T>
	 *            the generic type of the objects in the array
	 * @param original
	 *            the origin array
	 * @return the new created array without the last element
	 * @throws IndexOutOfBoundsException
	 *             if the given array is empty, as {@link #removeFirst(Object[])} does
	 */
	public static <T> T[] removeLast(final T[] original)
	{
		Argument.notNull(original, "original");
		final int lastIndex = Objects.checkIndex(original.length - 1, original.length);
		return Arrays.copyOf(original, lastIndex);
	}

	/**
	 * Split the given byte array in to new arrays with the chunk size
	 *
	 * @param bytes
	 *            the bytes
	 * @param chunkSize
	 *            the chunk size
	 * @return the byte[][]
	 */
	public static byte[][] splitInChunks(final byte[] bytes, final int chunkSize)
	{
		final int size = ((bytes.length - 1) / chunkSize) + 1;
		final byte[][] dataChunks = new byte[size][];
		int to = bytes.length;
		int count = size - 1;
		int from = count * chunkSize;
		while (-1 < count)
		{
			dataChunks[count] = Arrays.copyOfRange(bytes, from, to);
			to = from;
			from = to - chunkSize;
			count--;
		}
		return dataChunks;
	}

	/**
	 * Decorates the <code>Arrays#copyOfRange</code> method.
	 * 
	 * @see Arrays#copyOfRange(Object[], int, int)
	 * @param <T>
	 *            the type of elements in the array
	 * @param original
	 *            the original array
	 * @param start
	 *            the start index
	 * @param end
	 *            the end index
	 * @return a new array from the specified start and end point
	 */
	public static <T> T[] subArray(T[] original, int start, int end)
	{
		return Arrays.copyOfRange(original, start, end);
	}

	/**
	 * Creates a new {@link List} from the given array <br>
	 * <br>
	 * Note: This wraps only the method asList from {@link Arrays#asList(Object...)}
	 *
	 * @param <T>
	 *            the generic type of the objects in the array
	 * @param array
	 *            the array
	 * @return the new {@link List} created from the given array
	 */
	public static <T> List<T> toList(final T[] array)
	{
		return asList(array);
	}

	/**
	 * Creates a new {@link Set} from the given array <br>
	 * <br>
	 * Note: This wraps only the method asList from {@link SetFactory#newLinkedHashSet(Object...)}
	 *
	 * @param <T>
	 *            the generic type of the objects in the array
	 * @param array
	 *            the array
	 * @return the new {@link Set} created from the given array
	 */
	public static <T> Set<T> toSet(final T[] array)
	{
		return SetFactory.newLinkedHashSet(array);
	}

	/**
	 * Converts the given {@link Map} object to a two-dimensional array
	 *
	 * @param <T>
	 *            the generic type
	 * @param map
	 *            the map
	 * @param cls
	 *            the class
	 * @return The two-dimensional array produced from the given {@link Map} object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[][] toTwoDimensionalArray(final Map<T, T> map, Class<T> cls)
	{
		Argument.notNull(map, "map");
		Argument.notNull(cls, "cls");
		T[][] array = (T[][])Array.newInstance(cls, map.size(), 2);
		int count = 0;
		for (Map.Entry<T, T> entry : map.entrySet())
		{
			array[count][0] = entry.getKey();
			array[count][1] = entry.getValue();
			count++;
		}
		return array;
	}

	/**
	 * Converts the given {@link Map} object to a two-dimensional array
	 *
	 * @param map
	 *            the map
	 * @return The two-dimensional array produced from the given {@link Map} object
	 */
	public static Object[][] toTwoDimensionalArray(Map<String, Object> map)
	{
		return map.entrySet().stream()
			.map(entry -> new Object[] { entry.getKey(), entry.getValue() })
			.toArray(Object[][]::new);
	}

}
