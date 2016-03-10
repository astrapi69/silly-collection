package de.alpharogroup.collections;

/**
 * The class {@link ArrayExtensions} is an extensions class for use with array objects.
 */
public final class ArrayExtensions
{

	/**
	 * Gets the first object from the given List.
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the List.
	 * @return Returns the first object from the given List or null if the List is empty or null.
	 */
	public static <T> T getFirst(final T[] array)
	{
		if(array != null && array.length != 0) {
			return array[0];
		}
		return null;
	}
}
