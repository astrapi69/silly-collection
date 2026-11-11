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
package io.github.astrapi69.collection.map;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections4.map.AbstractMapTest;

/**
 * The contract suite of Apache Commons Collections for {@link Map} implementations, run against
 * {@link InsertionOrderMap} (#157)
 */
public class InsertionOrderMapContractTest
	extends
		AbstractMapTest<InsertionOrderMap<Object, Object>, Object, Object>
{

	@Override
	public InsertionOrderMap<Object, Object> makeObject()
	{
		return new InsertionOrderMap<>();
	}

	/**
	 * The confirmed map the suite compares with: a {@link LinkedHashMap}, the class that
	 * {@link InsertionOrderMap} extends
	 */
	@Override
	public Map<Object, Object> makeConfirmedMap()
	{
		return new LinkedHashMap<>();
	}

	/**
	 * InsertionOrderMap documents no serialized form, so the suite reads no canonical file of an
	 * earlier version; the round trip of the serialization tests still runs
	 */
	@Override
	protected boolean skipSerializedCanonicalTests()
	{
		return true;
	}

}
