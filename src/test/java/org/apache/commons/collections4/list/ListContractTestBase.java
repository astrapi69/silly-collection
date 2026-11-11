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
package org.apache.commons.collections4.list;

import org.junit.jupiter.api.Test;

/**
 * Makes the four tests of {@link AbstractListTest} that a list with a documented deviation of its
 * add methods has to replace overridable from another package (#157).
 * <p>
 * The tests are package-private in the tests jar of Apache Commons Collections, so a subclass in
 * another package cannot override them; Commons itself overrides them in the same package, as
 * {@code SetUniqueListTest} does. This class lies in that package, runs each of the four tests as
 * the suite defines it, and lets a subclass replace it through the protected method of the same
 * name without the prefix {@code test}. A subclass replaces one only where the Javadoc of the class
 * under test says that its add differs from the one of {@link java.util.List}, and says so in a
 * comment that names the sentence of the Javadoc.
 *
 * @param <E>
 *            the type of the elements
 */
public abstract class ListContractTestBase<E> extends AbstractListTest<E>
{

	/**
	 * Runs {@code testListIteratorAdd} of the suite, unless a subclass replaces it
	 */
	@Override
	@Test
	void testListIteratorAdd()
	{
		listIteratorAdd();
	}

	/**
	 * The add of the list iterator, as the suite defines it
	 */
	protected void listIteratorAdd()
	{
		super.testListIteratorAdd();
	}

	/**
	 * Runs {@code testListAddByIndex} of the suite, unless a subclass replaces it
	 */
	@Override
	@Test
	void testListAddByIndex()
	{
		listAddByIndex();
	}

	/**
	 * The add of an element at an index, as the suite defines it
	 */
	protected void listAddByIndex()
	{
		super.testListAddByIndex();
	}

	/**
	 * Runs {@code testListAddByIndexBoundsChecking} of the suite, unless a subclass replaces it
	 */
	@Override
	@Test
	void testListAddByIndexBoundsChecking()
	{
		listAddByIndexBoundsChecking();
	}

	/**
	 * The bounds check of the add at an index of an empty list, as the suite defines it
	 */
	protected void listAddByIndexBoundsChecking()
	{
		super.testListAddByIndexBoundsChecking();
	}

	/**
	 * Runs {@code testListAddByIndexBoundsChecking2} of the suite, unless a subclass replaces it
	 */
	@Override
	@Test
	void testListAddByIndexBoundsChecking2()
	{
		listAddByIndexBoundsChecking2();
	}

	/**
	 * The bounds check of the add at an index of a full list, as the suite defines it
	 */
	protected void listAddByIndexBoundsChecking2()
	{
		super.testListAddByIndexBoundsChecking2();
	}

}
