package de.alpharogroup.collections;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Test class for the class {@link ArrayExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class ArrayExtensionsTest
{

	/**
	 * Test for method {@link ArrayExtensions#getFirst(Object[])}
	 */
	@Test
	public void testGetFirst()
	{
		String expected = "1";
		final String numbers[] = { expected, "2", "3", "4", "5", "6", "7" };
		String actual = ArrayExtensions.getFirst(numbers);
		AssertJUnit.assertEquals(expected, actual);
		final String empty[] = { };
		expected = null;
		actual = ArrayExtensions.getFirst(empty);
		AssertJUnit.assertEquals(expected, actual);
		expected = null;
		actual = ArrayExtensions.getFirst(null);
		AssertJUnit.assertEquals(expected, actual);
	}

}
