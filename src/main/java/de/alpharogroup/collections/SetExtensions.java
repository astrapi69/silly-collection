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
package de.alpharogroup.collections;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import lombok.experimental.UtilityClass;

/**
 * Extensions class for use with {@link Set} objects.
 */
@UtilityClass
public class SetExtensions
{

	/**
	 * Factory method for create new {@link HashSet} and returns as {@link Set}.
	 *
	 * @param <T>
	 *            the generic type
	 * @param elements
	 *            the elements to add in the new {@link HashSet}.
	 * @return the new {@link HashSet} and returns as {@link Set}.
	 */
	@SafeVarargs
	public static final <T> Set<T> newHashSet(final T... elements)
	{
		final Set<T> set = new HashSet<>();
		Collections.addAll(set, elements);
		return set;
	}

}
