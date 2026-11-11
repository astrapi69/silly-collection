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
package io.github.astrapi69.collection.iterator;

import java.util.Iterator;

import org.apache.commons.collections4.iterators.AbstractIteratorTest;

/**
 * The contract suite of Apache Commons Collections for {@link Iterator} implementations, run
 * against {@link ArrayIterator} (#157)
 */
public class ArrayIteratorContractTest extends AbstractIteratorTest<String>
{

	@Override
	public Iterator<String> makeEmptyIterator()
	{
		return new ArrayIterator<>(new String[0]);
	}

	@Override
	public Iterator<String> makeObject()
	{
		return new ArrayIterator<>(new String[] { "a", "b", "c" });
	}

	@Override
	public boolean supportsRemove()
	{
		return true;
	}

	/**
	 * {@link ArrayIterator} does not implement Serializable
	 */
	@Override
	public boolean isTestSerialization()
	{
		return false;
	}

}
