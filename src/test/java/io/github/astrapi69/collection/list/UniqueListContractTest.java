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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections4.list.ListContractTestBase;
import org.junit.jupiter.api.Test;

/**
 * The contract suite of Apache Commons Collections for {@link List} implementations, run against
 * {@link UniqueList} (#157): an {@link ArrayList} is the confirmed collection the suite compares
 * with. Where the Javadoc of the class says that it differs from the contract of {@link List}, the
 * test of the suite is replaced by one that pins what the Javadoc says.
 */
public class UniqueListContractTest extends ListContractTestBase<Object>
{

	@Override
	public List<Object> makeObject()
	{
		return new UniqueList<>();
	}

	@Override
	public Collection<Object> makeConfirmedCollection()
	{
		return new ArrayList<>();
	}

	/**
	 * The elements of the default fixture of the suite hold one element twice, which a list that
	 * holds each element once cannot hold; these are distinct (UniqueList: "holds each element
	 * once")
	 */
	@Override
	public Object[] getFullNonNullElements()
	{
		return new String[] { "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight",
				"Nine", "Ten", "Eleven", "Twelve" };
	}

	/**
	 * Elements that are none of {@link #getFullNonNullElements()}, as many as the full ones and the
	 * null, so that the tests of the suite that set one other element per index set distinct
	 * elements
	 */
	@Override
	public Object[] getOtherNonNullElements()
	{
		return new String[] { "Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta",
				"Iota", "Kappa", "Lambda", "Mu", "Nu", "Xi", "Omicron" };
	}

	/**
	 * The value for the add and set of the list iterator: none the full list holds
	 */
	@Override
	protected Object getListIteratorAddSetValue()
	{
		return "Omega";
	}

	/**
	 * UniqueList documents no serialized form, so the suite reads no canonical file of an earlier
	 * version; the round trip of the serialization tests still runs
	 */
	@Override
	protected boolean skipSerializedCanonicalTests()
	{
		return true;
	}

	/**
	 * The suite adds the full elements to a list that holds them already and expects the list to
	 * grow; the addAll of {@link UniqueList} adds "the elements of the given collection that this
	 * list does not contain yet", so this is the documented behaviour instead: the held elements
	 * keep their places, and true is returned only if the collection holds a new element
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
		assertFalse(getCollection().addAll(expected));
		assertEquals(expected, getCollection());
		List<Object> others = Arrays.asList(getOtherElements());
		assertTrue(getCollection().addAll(others));
		List<Object> all = new ArrayList<>(expected);
		all.addAll(others);
		assertEquals(all, getCollection());
	}

	/**
	 * The suite adds the full elements through the list iterator to a list that holds them already
	 * and expects the list to grow; the add of the list iterator of {@link UniqueList} inserts a
	 * new element "immediately before the element next() would return", and an element the list
	 * holds "moves there", so the size stays, which this test pins
	 */
	@Override
	protected void listIteratorAdd()
	{
		resetEmpty();
		List<Object> list = getCollection();
		ListIterator<Object> iterator = list.listIterator();
		for (Object element : getFullElements())
		{
			iterator.add(element);
		}
		assertEquals(Arrays.asList(getFullElements()), list);
		resetFull();
		list = getCollection();
		int size = list.size();
		Object first = list.get(0);
		iterator = list.listIterator();
		iterator.add(getListIteratorAddSetValue());
		assertEquals(size + 1, list.size());
		assertEquals(getListIteratorAddSetValue(), list.get(0));
		assertEquals(first, iterator.next());
		Object last = list.get(list.size() - 1);
		list.listIterator(1).add(last);
		assertEquals(size + 1, list.size());
		assertEquals(last, list.get(1));
	}

}
