package io.github.astrapi69.collection.list;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import io.github.astrapi69.comparator.object.StringComparator;

/**
 * The unit test class for {@link SortedUniqueList}
 */
public class SortedUniqueListTest
{

	/**
	 * Test the method {@link SortedUniqueList#add(Object)}
	 */
	@Test
	public void testAddUniqueElement()
	{
		List<String> list;

		list = new SortedUniqueList<>(6);
		list.add("Leonidas");
		list.add("Leonidas");
		assertEquals(list.size(), 1);
		list.add("Berta");
		list.add("Berta");
		assertEquals(list.size(), 2);
		list.add("Caesar");
		list.add("Caesar");
		assertEquals(list.size(), 3);
		list.add("Dora");
		list.add("Dora");
		assertEquals(list.size(), 4);
		list.add("Emil");
		list.add("Emil");
		assertEquals(list.size(), 5);
		list.add("Anton");
		list.add("Anton");
		assertEquals(list.size(), 6);
	}

	/**
	 * Test the method {@link SortedUniqueList#add(Object)}
	 */
	@Test
	public void testAddAllWithIndex()
	{
		List<String> list;
		List<String> addition;

		addition = ListFactory.newArrayList("", "Emil", "Anton", "Anton", "Anton", "Emil", "");
		list = new SortedUniqueList<>();
		list.add("Emil");
		list.add("Caesar");
		list.addAll(1, addition);
		assertEquals(list.size(), 4);

		addition = ListFactory.newArrayList();
		list = new SortedUniqueList<>();
		list.add("Emil");
		list.add("Caesar");
		list.addAll(1, addition);
		assertEquals(list.size(), 2);
	}

	/**
	 * Test method for {@link StringComparator#compare(String, String)} with null smaller.
	 */
	@Test
	public void testCompare()
	{
		Comparator<String> comparator;
		Collection<String> collection;
		int actual;
		boolean expected;
		List<String> list;

		String alex = "Alex";
		String bill = "Billy";
		String leo = "Leon";

		// Now lets see a demo on a list...
		list = new SortedUniqueList<>();
		list.add(leo);
		list.add(alex);
		list.add(bill);
		list.add(null);
		list.add(null);
		collection = ListFactory.newArrayList(list);

		expected = list.indexOf(leo) == 0;
		assertTrue(expected);
		expected = list.indexOf(alex) == 1;
		assertTrue(expected);
		expected = list.indexOf(bill) == 2;
		assertTrue(expected);

		comparator = StringComparator.of(true);

		// Now lets see a demo on a list...
		list = new SortedUniqueList<>(comparator);
		list.add(leo);
		list.add(alex);
		list.add(bill);
		list.add(null);
		list.add(null);

		expected = list.indexOf(leo) == 2;
		assertTrue(expected);
		expected = list.indexOf(alex) == 0;
		assertTrue(expected);
		expected = list.indexOf(bill) == 1;
		assertTrue(expected);

		comparator = StringComparator.of(false);
		list.sort(comparator);

		expected = list.indexOf(alex) == 1;
		assertTrue(expected);
		expected = list.indexOf(bill) == 2;
		assertTrue(expected);
		expected = list.indexOf(leo) == 3;
		assertTrue(expected);

		// test with the other constructor
		SortedUniqueList<String> sortedArrayList = new SortedUniqueList<>(collection, comparator);

		expected = sortedArrayList.indexOf(alex) == 1;
		assertTrue(expected);
		expected = sortedArrayList.indexOf(bill) == 2;
		assertTrue(expected);
		expected = sortedArrayList.indexOf(leo) == 3;
		assertTrue(expected);
		assertEquals(comparator, sortedArrayList.getComparator());
		comparator = StringComparator.of(true);
		sortedArrayList.setComparator(comparator);
		assertEquals(comparator, sortedArrayList.getComparator());

	}

	/**
	 * Test the method {@link SortedUniqueList#addAll(Collection)}
	 */
	@Test
	public void testAddAll()
	{
		List<String> list;
		List<String> addition;

		addition = ListFactory.newArrayList("", "Emil", "Anton", "Anton", "Anton", "Emil", "");
		list = new SortedUniqueList<>(new StringComparator());
		list.add("Emil");
		list.add("Caesar");
		list.addAll(addition);
		assertEquals(list.size(), 4);

		addition = ListFactory.newArrayList();
		list = new SortedList<>();
		list.add("Emil");
		list.add("Caesar");
		list.addAll(addition);
		assertEquals(list.size(), 2);
	}

	/**
	 * Test the method {@link SortedUniqueList#add(Object)}
	 */
	@Test
	public void testAddWithIndex()
	{
		List<String> list;

		list = new SortedUniqueList<>();
		list.add(0, "Leonidas");
		assertEquals(list.size(), 1);
		list.add(0, "Berta");
		assertEquals(list.size(), 2);
		list.add(0, "Caesar");
		assertEquals(list.size(), 3);
		list.add(0, "Dora");
		assertEquals(list.size(), 4);
		list.add(0, "Emil");
		assertEquals(list.size(), 5);
		list.add(0, "Anton");
		assertEquals(list.size(), 6);
	}

	/**
	 * Test the method {@link SortedUniqueList#add(Object)}
	 */
	@Test
	public void testAddWithString()
	{
		List<String> list;

		list = new SortedUniqueList<>(Comparator.naturalOrder());
		list.add("Leonidas");
		assertEquals(list.size(), 1);
		list.add("Berta");
		assertEquals(list.size(), 2);
		list.add("Caesar");
		assertEquals(list.size(), 3);
		list.add("Dora");
		assertEquals(list.size(), 4);
		list.add("Emil");
		assertEquals(list.size(), 5);
		list.add("Anton");
		assertEquals(list.size(), 6);
	}

	/**
	 * Test method for {@link SortedUniqueList#add(Object)}.
	 */
	@Test
	@DisplayName("Test adding elements and maintaining sorted uniqueness")
	public void testAdd()
	{
		SortedUniqueList<Integer> list = new SortedUniqueList<>(Comparator.naturalOrder());
		assertTrue(list.add(3));
		assertTrue(list.add(1));
		assertTrue(list.add(2));
		assertFalse(list.add(3));
		assertEquals(Arrays.asList(1, 2, 3), list);
	}

	/**
	 * Test method for {@link SortedUniqueList#setComparator(Comparator)}.
	 */
	@Test
	@DisplayName("Test setting comparator")
	public void testSetComparator()
	{
		SortedUniqueList<String> list = new SortedUniqueList<>();
		list.add("banana");
		list.add("apple");
		list.add("cherry");
		list.setComparator(Comparator.reverseOrder());
		assertEquals(Arrays.asList("cherry", "banana", "apple"), list);
	}
}
