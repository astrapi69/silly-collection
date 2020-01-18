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
package de.alpharogroup.collections.modifications;

import lombok.NonNull;

import java.util.Collection;

/**
 * The Enum ModifiedState represents states if a collection have been modified. Checks the same( or
 * different) collection in different states. If a reference of a collection in the past is same or
 * modified with the reference of the collection now.
 */
public enum ModifiedState
{

	/**
	 * The cleared state. This indicates that no entry exists anymore in the collection.
	 **/
	CLEARED,

	/**
	 * The first match state. This indicates that there is a total new match. Precondition is that
	 * no entries exists in the collection.
	 **/
	FIRST_MATCH,

	/**
	 * The new match state. This indicates that an new entry is added. Precondition is that entries
	 * exists in the collection.
	 **/
	NEW_MATCH,

	/**
	 * The removed state. This indicates that an entry has been removed. Precondition is that
	 * minimun an entry exists in the collection.
	 */
	REMOVED,

	/**
	 * The unmodified state. This indicates that no entry added or removed from the collection.
	 **/
	UNMODIFIED;

	/**
	 * Checks if the given previous(for instance in the past) Collection is modified comparing the
	 * next(for instance now) Collection.
	 *
	 * @param <T>
	 *            the type.
	 * @param previous
	 *            the previous collection.
	 * @param next
	 *            the next collection.
	 * @return the modified
	 */
	public static <T> ModifiedState isModified(final @NonNull Collection<T> previous,
		final @NonNull Collection<T> next)
	{
		final boolean collectionsEqual = previous.containsAll(next) && next.containsAll(previous);

		if (collectionsEqual)
		{
			return ModifiedState.UNMODIFIED;
		}

		final boolean cleared = next.isEmpty() && !previous.isEmpty();

		if (cleared)
		{
			return ModifiedState.CLEARED;
		}
		final boolean removed = next.size() < previous.size();

		if (removed)
		{
			return ModifiedState.REMOVED;
		}
		final boolean initialEntry = previous.isEmpty() && !next.isEmpty();

		if (initialEntry)
		{
			return ModifiedState.FIRST_MATCH;
		}
		return ModifiedState.NEW_MATCH;
	}

}
