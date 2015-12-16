package de.alpharogroup.comparators;

import lombok.Getter;

/**
 * The enum {@link CompareOrder}.
 */
public enum CompareOrder
{
	/** The order to sort an object before. */
	BEFORE(-1),
	/** The order to sort an object as equal. */
	EQUAL(0),
	/** The order to sort an object after. */
	AFTER(1);

	/** The order. */
	@Getter
	private final int order;

	/**
	 * Instantiates a new {@link CompareOrder}.
	 *
	 * @param order
	 *            the order
	 */
	CompareOrder(final int order)
	{
		this.order = order;
	}
}