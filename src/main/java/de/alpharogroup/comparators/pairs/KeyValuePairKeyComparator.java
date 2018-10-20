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
package de.alpharogroup.comparators.pairs;

import java.util.Comparator;

import de.alpharogroup.collections.pairs.KeyValuePair;

/**
 * The class {@link KeyValuePairKeyComparator} compares {@linkplain KeyValuePair} objects over the
 * key where the key have to implements the {@linkplain Comparable} interface.
 *
 * @param <K>
 *            The generic type of the key
 * @param <V>
 *            The generic type of the value
 * @deprecated use instead java 8 comparators <br>
 *             Note: will be removed on next minor release.
 */
@Deprecated
public class KeyValuePairKeyComparator<K extends Comparable<K>, V>
	implements
		Comparator<KeyValuePair<K, V>>
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(final KeyValuePair<K, V> o1, final KeyValuePair<K, V> o2)
	{
		return o1.getKey().compareTo(o2.getKey());
	}
}