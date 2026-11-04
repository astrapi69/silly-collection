package io.github.astrapi69.collection;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.astrapi69.collection.array.ArrayExtensions;
import io.github.astrapi69.collection.array.ArrayFactory;
import io.github.astrapi69.collection.list.ListExtensions;
import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.collection.map.MapExtensions;
import io.github.astrapi69.collection.properties.IndexSortedProperties;
import io.github.astrapi69.collection.properties.SortedProperties;
import io.github.astrapi69.collection.set.IndexableSet;
import io.github.astrapi69.collection.set.SetFactory;

/**
 * The examples of the README's Quick start, each block as a test, with the values its comments name
 * as assertions. A change here belongs into the README too, and the other way round.
 */
class ReadmeExamplesTest
{

	/**
	 * README: "Lists that stay unique or sorted"
	 */
	@Test
	void uniqueAndSortedLists()
	{
		List<String> unique = ListFactory.newUniqueList("b", "a", "b"); // [b, a]
		boolean added = unique.add("a"); // false: held already
		assertEquals(List.of("b", "a"), unique);
		assertFalse(added);

		List<String> start = List.of("c", "a");
		// [a, b, c]
		List<String> sorted = ListFactory.newSortedList(start, Comparator.naturalOrder(), "b");
		sorted.add("ab"); // [a, ab, b, c]
		assertEquals(List.of("a", "ab", "b", "c"), sorted);

		List<String> sortedUnique = ListFactory.newSortedUniqueList(start,
			Comparator.naturalOrder(), "a", "b"); // [a, b, c]
		assertEquals(List.of("a", "b", "c"), sortedUnique);
	}

	/**
	 * README: "Working with lists"
	 */
	@Test
	void listExtensions()
	{
		List<String> letters = List.of("A", "B", "C", "D", "E", "F");
		List<String> withoutLastTwo = ListExtensions.removeLastValues(letters, 2); // [A, B, C, D]
		// [[A, B, C, D], [E, F]]
		List<List<String>> parts = ListExtensions.splitToParts(letters, 4);
		String first = ListExtensions.getFirst(letters); // "A"
		String last = ListExtensions.getLast(letters); // "F"
		assertEquals(List.of("A", "B", "C", "D"), withoutLastTwo);
		assertEquals(List.of(List.of("A", "B", "C", "D"), List.of("E", "F")), parts);
		assertEquals("A", first);
		assertEquals("F", last);

		List<String> words = new ArrayList<>(List.of("ccc", "a", "bb"));
		ListExtensions.sortByProperty(words, String::length, true); // [a, bb, ccc]
		assertEquals(List.of("a", "bb", "ccc"), words);
	}

	/**
	 * README: "Sets that keep the order"
	 */
	@Test
	void sets()
	{
		Set<String> set = SetFactory.newInsertionOrderSet("b", "a");
		boolean added = set.add("b"); // false, and "b" moves to the end: [a, b]
		assertFalse(added);
		assertEquals(List.of("a", "b"), new ArrayList<>(set));

		IndexableSet<String> indexable = new IndexableSet<>();
		indexable.add("c");
		indexable.add("a");
		String second = indexable.get(1); // "a"
		int index = indexable.getIndex("c"); // 0
		assertEquals("a", second);
		assertEquals(0, index);
	}

	/**
	 * README: "Maps"
	 */
	@Test
	void maps()
	{
		Map<Integer, String> numbers = new LinkedHashMap<>();
		numbers.put(1, "one");
		numbers.put(2, "two");
		numbers.put(3, "one");
		// 1, the first in iteration order
		Integer key = MapExtensions.getKeyFromValue(numbers, "one");
		Collection<Integer> keys = MapExtensions.getKeysFromValue(numbers, "one"); // 1 and 3
		Integer none = MapExtensions.getKeyFromValue(numbers, "three"); // null
		assertEquals(1, key);
		assertEquals(Set.of(1, 3), new HashSet<>(keys));
		assertNull(none);
	}

	/**
	 * README: "Arrays"
	 */
	@Test
	void arrays()
	{
		Integer[] numbers = ArrayFactory.newArray(1, 2, 3);
		Integer[] tail = ArrayExtensions.removeFromStart(numbers, new Integer[] { 1 }); // [2, 3]
		// [1, 2, 3], a copy
		Integer[] unchanged = ArrayExtensions.removeFromStart(numbers, new Integer[] { 9 });
		Integer[] withoutFirstAndLast = ArrayExtensions.remove(numbers, 2, 0); // [2]
		// [1, 2, 3, 4]
		Integer[] longer = ArrayExtensions.concatenate(numbers, new Integer[] { 4 });
		Integer[] range = ArrayFactory.newRangeArray(1, 3); // [1, 2, 3], both ends included
		assertArrayEquals(new Integer[] { 2, 3 }, tail);
		assertArrayEquals(new Integer[] { 1, 2, 3 }, unchanged);
		assertNotSame(numbers, unchanged);
		assertArrayEquals(new Integer[] { 2 }, withoutFirstAndLast);
		assertArrayEquals(new Integer[] { 1, 2, 3, 4 }, longer);
		assertArrayEquals(new Integer[] { 1, 2, 3 }, range);
		assertArrayEquals(new Integer[] { 1, 2, 3 }, numbers);
	}

	/**
	 * README: "Collections"
	 */
	@Test
	void collections()
	{
		// true
		boolean same = CollectionExtensions.isEqualCollection(List.of(1, 1, 2), List.of(2, 1, 1));
		// false
		boolean other = CollectionExtensions.isEqualCollection(List.of(1, 2), List.of(3, 4));
		// a Number[]: 1, 2.5
		Number[] mixed = CollectionExtensions.toArray(List.<Number> of(1, 2.5));
		// [[1, 2], [3, 4], [5]]
		Collection<List<Integer>> pairs = CollectionExtensions.partition(List.of(1, 2, 3, 4, 5), 2);
		assertTrue(same);
		assertFalse(other);
		assertEquals(Number[].class, mixed.getClass());
		assertArrayEquals(new Number[] { 1, 2.5 }, mixed);
		assertEquals(List.of(List.of(1, 2), List.of(3, 4), List.of(5)), new ArrayList<>(pairs));
	}

	/**
	 * README: "Properties in order"
	 */
	@Test
	void properties()
	{
		SortedProperties sorted = SortedProperties.of();
		sorted.setProperty("b", "2");
		sorted.setProperty("a", "1");
		Set<Object> keys = sorted.keySet(); // [a, b], a view in the order of the keys
		assertEquals(List.of("a", "b"), new ArrayList<>(keys));

		IndexSortedProperties indexed = IndexSortedProperties.of();
		indexed.setProperty("b", "2");
		indexed.setProperty("a", "1");
		String firstValue = indexed.getProperty(0); // "1", the value of "a"
		int indexOfTwo = indexed.indexOf("2"); // 1
		assertEquals("1", firstValue);
		assertEquals(1, indexOfTwo);
	}

}
