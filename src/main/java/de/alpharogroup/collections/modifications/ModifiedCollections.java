/**
 * The MIT License
 *
 * Copyright (C) 2007 Asterios Raptis
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
package de.alpharogroup.collections.modifications;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The Class ModifiedLists holds two collections. It is used as return type for finding from an old
 * list which elements have removed and which have added.
 *
 * @param <T>
 *            the generic type of the collections
 */
@NoArgsConstructor
public class ModifiedCollections<T>
{

	/** The added elements. */
	@Getter
	private final Collection<T> addedElements = new ArrayList<>();

	/** The removed elements. */
	@Getter
	private final Collection<T> removedElements = new ArrayList<>();

	/**
	 * Gets the modified lists.
	 *
	 * @param previous
	 *            the previous
	 * @param next
	 *            the next
	 * @return the modified lists
	 */
	public ModifiedCollections<T> getModifiedLists(final Collection<T> previous,
		final Collection<T> next)
	{
		// find the elements that are new...
		for (final T added : next)
		{
			// elements from the previous list that are not exist in the next
			// list are new,
			// so added to the addedElements list...
			if (!previous.contains(added))
			{
				getAddedElements().add(added);
			}
		}
		// find the elements that are removed...
		for (final T removed : previous)
		{
			// elements from the next list that are not exist in the previous
			// list are removed,
			// so added to the removedElements list...
			if (!next.contains(removed))
			{
				getRemovedElements().add(removed);
			}
		}
		return this;
	}

}
