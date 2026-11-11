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
package io.github.astrapi69.collection.set;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.collections4.set.AbstractSetTest;

/**
 * The contract suite of Apache Commons Collections for {@link Set} implementations, run against
 * {@link IndexableSet} (#157)
 */
public class IndexableSetContractTest extends AbstractSetTest<Object>
{

	@Override
	public Set<Object> makeObject()
	{
		return new IndexableSet<>();
	}

	/**
	 * The confirmed collection the suite compares with: a {@link LinkedHashSet}, the class that
	 * {@link InsertionOrderSet} extends
	 */
	@Override
	public Collection<Object> makeConfirmedCollection()
	{
		return new LinkedHashSet<>();
	}

	@Override
	public Collection<Object> makeConfirmedFullCollection()
	{
		return new LinkedHashSet<>(Arrays.asList(getFullElements()));
	}

	/**
	 * IndexableSet documents no serialized form, so the suite reads no canonical file of an earlier
	 * version; the round trip of the serialization tests still runs
	 */
	@Override
	protected boolean skipSerializedCanonicalTests()
	{
		return true;
	}

}
