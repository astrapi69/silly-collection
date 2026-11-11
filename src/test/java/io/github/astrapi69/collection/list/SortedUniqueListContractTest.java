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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.list.ListContractTestBase;
import org.junit.jupiter.api.Test;

/**
 * The contract suite of Apache Commons Collections for {@link List} implementations, run against
 * {@link SortedUniqueList} with the order of the strings (#157). Its Javadoc says that it differs
 * from the contract of {@link List} in the add methods, which put an element at its place in the
 * order and ignore the index, and in the set and the add of the list iterator, which it refuses;
 * the suite's own switches switch off what it refuses, and the tests of the add methods are
 * replaced by ones that pin what the Javadoc says. The confirmed collection is an {@link ArrayList}
 * that keeps the order of the strings as well, so that the suite compares like with like.
 */
public class SortedUniqueListContractTest extends ListContractTestBase<Object>
{

	/**
	 * The order of the list under test: the one of the strings of the suite; serializable, as the
	 * list under test holds it and the suite serializes the list
	 */
	private static final Comparator<Object> ORDER = new StringOrder();

	/**
	 * The comparator of the strings, serializable
	 */
	private static final class StringOrder implements Comparator<Object>, Serializable
	{

		private static final long serialVersionUID = 1L;

		@Override
		public int compare(Object first, Object second)
		{
			return first.toString().compareTo(second.toString());
		}
	}

	@Override
	public List<Object> makeObject()
	{
		return new SortedUniqueList<>(ORDER);
	}

	/**
	 * A list that keeps the order of the strings by sorting after each add, the model the suite
	 * compares the list under test with
	 */
	@Override
	public Collection<Object> makeConfirmedCollection()
	{
		return new ArrayList<>()
		{

			private static final long serialVersionUID = 1L;

			@Override
			public boolean add(Object element)
			{
				boolean added = true
					? !contains(element) && super.add(element)
					: super.add(element);
				sort(ORDER);
				return added;
			}

			@Override
			public boolean addAll(Collection<?> collection)
			{
				boolean added = false;
				for (Object element : new ArrayList<>(collection))
				{
					added |= add(element);
				}
				return added;
			}
		};
	}

	@Override
	public Collection<Object> makeConfirmedFullCollection()
	{
		Collection<Object> confirmed = makeConfirmedCollection();
		confirmed.addAll(Arrays.asList(getFullElements()));
		return confirmed;
	}

	/**
	 * Distinct strings that are already in their order
	 */
	@Override
	public Object[] getFullNonNullElements()
	{
		return new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j" };
	}

	/**
	 * Strings that none of {@link #getFullNonNullElements()} equals, and that are after them in the
	 * order
	 */
	@Override
	public Object[] getOtherNonNullElements()
	{
		return new String[] { "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v" };
	}

	/**
	 * The order of the suite is the one of the strings, which has no place for null: the comparator
	 * throws a NullPointerException
	 */
	@Override
	public boolean isNullSupported()
	{
		return false;
	}

	/**
	 * Javadoc: "That is set(int, Object) and the set and add of its list iterator ... throws an
	 * UnsupportedOperationException", with a comparator
	 */
	@Override
	public boolean isSetSupported()
	{
		return false;
	}

	/**
	 * Javadoc: the set of the list iterator is refused with a comparator, see
	 * {@link #isSetSupported()}
	 */
	@Override
	protected boolean isListIteratorSetSupported()
	{
		return false;
	}

	/**
	 * SortedUniqueList documents no serialized form, so the suite reads no canonical file of an
	 * earlier version; the round trip of the serialization tests still runs
	 */
	@Override
	protected boolean skipSerializedCanonicalTests()
	{
		return true;
	}

	/**
	 * The suite expects the list to hold the full elements twice after addAll of them; with the
	 * order, the list under test puts each element at its place
	 */
	@Override
	@Test
	public void testCollectionAddAll()
	{
		resetEmpty();
		List<Object> expected = Arrays.asList(getFullElements());
		assertTrue(getCollection().addAll(expected));
		assertEquals(expected, getCollection());
		resetFull();
		List<Object> others = Arrays.asList(getOtherElements());
		assertTrue(getCollection().addAll(others));
		List<Object> all = new ArrayList<>(expected);
		all.addAll(others);
		all.sort(ORDER);
		assertEquals(all, getCollection());
	}

	/**
	 * Javadoc: "The given index is ignored and not checked; any index is accepted, a negative one
	 * too", the element goes to its place in the order
	 */
	@Override
	protected void listAddByIndex()
	{
		resetFull();
		List<Object> list = getCollection();
		int size = list.size();
		list.add(0, "zz");
		list.add(size, "aa");
		list.add(-1, "bb");
		list.add(Integer.MAX_VALUE, "cc");
		assertEquals(size + 4, list.size());
		List<Object> sorted = new ArrayList<>(list);
		sorted.sort(ORDER);
		assertEquals(sorted, list);
	}

	/**
	 * Javadoc: any index is accepted, so none throws an IndexOutOfBoundsException
	 */
	@Override
	protected void listAddByIndexBoundsChecking()
	{
		resetEmpty();
		List<Object> list = getCollection();
		list.add(Integer.MIN_VALUE, "b");
		list.add(1, "a");
		list.add(Integer.MAX_VALUE, "c");
		assertEquals(Arrays.asList("a", "b", "c"), list);
	}

	/**
	 * Javadoc: any index is accepted, so none throws an IndexOutOfBoundsException
	 */
	@Override
	protected void listAddByIndexBoundsChecking2()
	{
		resetFull();
		List<Object> list = getCollection();
		int size = list.size();
		list.add(-1, "0");
		list.add(size + 5, "z");
		assertEquals(size + 2, list.size());
		assertEquals("0", list.get(0));
		assertEquals("z", list.get(size + 1));
	}

	/**
	 * Javadoc: the add of the list iterator throws an UnsupportedOperationException, and the list
	 * does not change
	 */
	@Override
	protected void listIteratorAdd()
	{
		resetFull();
		List<Object> list = getCollection();
		List<Object> before = new ArrayList<>(list);
		assertThrows(UnsupportedOperationException.class,
			() -> list.listIterator().add(getListIteratorAddSetValue()));
		assertEquals(before, list);
		assertTrue(Collections.disjoint(list, Arrays.asList(getListIteratorAddSetValue())));
	}

}
