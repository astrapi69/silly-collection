package de.alpharogroup.comparators;

import java.io.Serializable;
import java.util.Comparator;

import de.alpharogroup.check.Check;
import lombok.Builder;
import lombok.EqualsAndHashCode;

/**
 * The class {@link NullCheckComparator} decorates another {@link Comparator} object. Before the
 * decorated {@link Comparator} will be executed null check will be executed.
 *
 * @param <T>
 *            the generic type of the {@link Comparator} object that will be decorated
 */
@EqualsAndHashCode
@Builder
public class NullCheckComparator<T> implements Comparator<T>, Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The decorated comparator. */
	private final Comparator<T> decoratedComparator;

	/** The flag that specifies if null objects is greater than non null objects. */
	private final boolean nullIsGreaterThan;

	/**
	 * Factory method to create a new {@link NullCheckComparator} object from the given
	 * {@link Comparator} object.
	 *
	 * @param <T>
	 *            the generic type of the {@link Comparator} object that will be decorated
	 * @param decoratedComparator
	 *            the {@link Comparator} object that will be decorated
	 * @return the new {@link NullCheckComparator} object
	 */
	public static <T> NullCheckComparator<T> of(Comparator<T> decoratedComparator)
	{
		return NullCheckComparator.<T> builder().decoratedComparator(decoratedComparator).build();
	}

	/**
	 * Factory method to create a new {@link NullCheckComparator} object from the given
	 * {@link Comparator} object.
	 *
	 * @param <T>
	 *            the generic type of the {@link Comparator} object that will be decorated
	 * @param decoratedComparator
	 *            the {@link Comparator} object that will be decorated
	 * @param nullIsGreaterThan
	 *            the flag that specifies if null objects is greater than non null objects.
	 * @return the new {@link NullCheckComparator} object
	 */
	public static <T> NullCheckComparator<T> of(Comparator<T> decoratedComparator,
		boolean nullIsGreaterThan)
	{
		return NullCheckComparator.<T> builder().decoratedComparator(decoratedComparator)
			.nullIsGreaterThan(nullIsGreaterThan).build();
	}

	/**
	 * Instantiates a {@link NullCheckComparator} from the given {@link Comparator} object. The
	 * flag nullIsGreaterThan is set to false so null objects are smaller then non null objects.
	 *
	 * @param decoratedComparator
	 *            the {@link Comparator} object that will be decorated
	 */
	public NullCheckComparator(Comparator<T> decoratedComparator)
	{
		this(decoratedComparator, false);
	}

	/**
	 * Instantiates a {@link NullCheckComparator} from the given {@link Comparator} object and the
	 * given flag.
	 *
	 * @param decoratedComparator
	 *            the {@link Comparator} object that will be decorated
	 * @param nullIsGreaterThan
	 *            the flag that specifies if null objects is greater than non null objects.
	 */
	public NullCheckComparator(Comparator<T> decoratedComparator, boolean nullIsGreaterThan)
	{
		Check.get().notNull(decoratedComparator, "decoratedComparator");
		this.decoratedComparator = decoratedComparator;
		this.nullIsGreaterThan = nullIsGreaterThan;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(T object, T compareWithObject)
	{
		if (object == compareWithObject)
		{
			return 0;
		}
		if (object == null)
		{
			if (this.nullIsGreaterThan)
			{
				return 1;
			}
			return -1;
		}
		if (compareWithObject == null)
		{

			if (this.nullIsGreaterThan)
			{
				return -1;
			}
			return 1;
		}
		return this.decoratedComparator.compare(object, compareWithObject);
	}

}
