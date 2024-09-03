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
package io.github.astrapi69.collection.array;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;

/**
 * The unit test class for the class {@link ArrayFactory}.
 */
public class ArrayFactoryTest
{

	/**
	 * Test for method {@link ArrayFactory#newArray(Object...)}
	 */
	@Test
	public void testNewArray()
	{
		Integer actual;
		Integer expected;
		final Integer[] numbers = { 1, 2, 3 };
		final Integer[] array = ArrayFactory.newArray(1, 2, 3);
		for (int i = 0; i < numbers.length; i++)
		{
			expected = numbers[i];
			actual = array[i];
			assertEquals(expected, actual);
		}

		final Object[] objects = ArrayFactory.newArray();
		expected = 0;
		actual = objects.length;
		assertEquals(expected, actual);
	}

	/**
	 * Test for method {@link ArrayFactory#newArray(Object[], Object[])}
	 */
	@Test
	public void testNewArrayWithExistingArray()
	{
		Integer[] actual;
		Integer[] expected;
		final Integer[] numbers = { 1, 2, 3 };

		actual = ArrayFactory.newArray(numbers, 4, 5, 6);
		expected = ArrayFactory.newArray(1, 2, 3, 4, 5, 6);
		assertArrayEquals(actual, expected);

		actual = ArrayFactory.newArray(ArrayFactory.newArray(), 1, 2, 3);
		expected = ArrayFactory.newArray(numbers);
		assertArrayEquals(actual, expected);

		actual = ArrayFactory.newArray(null, 1, 2, 3);
		expected = ArrayFactory.newArray(numbers);
		assertArrayEquals(actual, expected);
	}

	/**
	 * Test for method {@link ArrayFactory#newArray(Class, int)}
	 */
	@Test
	public void testNewArrayTypeWithLength()
	{
		int actual;
		int expected;
		final Integer[] array = ArrayFactory.newArray(Integer.class, 3);
		actual = array.length;
		expected = 3;
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link ArrayFactory#newBooleanArray(boolean[])}.
	 */
	@Test
	public void testNewBooleanArray()
	{
		boolean[] actual;
		boolean[] expected = { true, true, false };
		actual = ArrayFactory.newBooleanArray(true, true, false);
		assertTrue(Arrays.equals(actual, expected));

		expected = new boolean[0];
		actual = ArrayFactory.newBooleanArray();
		assertTrue(Arrays.equals(actual, expected));
	}

	/**
	 * Test method for {@link ArrayFactory#newByteArray(byte[])}.
	 */
	@Test
	public void testNewByteArray()
	{
		byte[] actual;
		byte[] expected = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };
		actual = ArrayFactory.newByteArray((byte)-84, (byte)-19, (byte)0, (byte)5, (byte)116,
			(byte)0, (byte)7, (byte)70, (byte)111, (byte)111, (byte)32, (byte)98, (byte)97,
			(byte)114);
		assertTrue(Arrays.equals(actual, expected));

		expected = new byte[0];
		actual = ArrayFactory.newByteArray();
		assertTrue(Arrays.equals(actual, expected));
	}

	/**
	 * Test method for {@link ArrayFactory#newByteArray(int[])}
	 */
	@Test
	public void testNewByteArrayWithIntArray()
	{
		byte[] actual;
		byte[] expected = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };
		int[] intArray = { -84, -19, 0, 5, 116, 0, 7, 70, 111, 111, 32, 98, 97, 114 };
		actual = ArrayFactory.newByteArray(intArray);
		assertTrue(Arrays.equals(actual, expected));

		expected = new byte[0];
		actual = ArrayFactory.newByteArray(new int[0]);
		assertTrue(Arrays.equals(actual, expected));
	}

	/**
	 * Test method for {@link ArrayFactory#newCharArray(char[])}.
	 */
	@Test
	public void testNewCharArray()
	{
		char[] actual;
		char[] expected = { 'f', 'o', 'o' };
		actual = ArrayFactory.newCharArray('f', 'o', 'o');
		assertTrue(Arrays.equals(actual, expected));

		expected = new char[0];
		actual = ArrayFactory.newCharArray();
		assertTrue(Arrays.equals(actual, expected));
	}

	/**
	 * Test method for {@link ArrayFactory#newDoubleArray(double[])}.
	 */
	@Test
	public void testNewDoubleArray()
	{
		double[] actual;
		double[] expected = { 1.1D, 2.1D, 3.1D };
		actual = ArrayFactory.newDoubleArray(1.1D, 2.1D, 3.1D);
		assertTrue(Arrays.equals(actual, expected));

		expected = new double[0];
		actual = ArrayFactory.newDoubleArray();
		assertTrue(Arrays.equals(actual, expected));
	}

	/**
	 * Test for method {@link ArrayFactory#newEmptyArray(Object[])}
	 */
	@Test
	public void testNewEmptyArray()
	{
		Integer[] actual;
		Integer[] expected;
		final Integer[] numbers = { 1, 2, 3 };
		expected = new Integer[3];
		actual = ArrayFactory.newEmptyArray(numbers);
		assertTrue(Arrays.equals(actual, expected));

	}

	/**
	 * Test method for {@link ArrayFactory#newFloatArray(float[])}.
	 */
	@Test
	public void testNewFloatArray()
	{
		float[] actual;
		float[] expected = { 1.0F, 2.0F, 3.0F };
		actual = ArrayFactory.newFloatArray(1.0F, 2.0F, 3.0F);
		assertTrue(Arrays.equals(actual, expected));

		expected = new float[0];
		actual = ArrayFactory.newFloatArray();
		assertTrue(Arrays.equals(actual, expected));
	}

	/**
	 * Test method for {@link ArrayFactory#newIntArray(int[])}.
	 */
	@Test
	public void testNewIntArray()
	{
		int[] actual;
		int[] expected = { 1, 2, 3 };
		actual = ArrayFactory.newIntArray(1, 2, 3);
		assertTrue(Arrays.equals(actual, expected));

		expected = new int[0];
		actual = ArrayFactory.newIntArray();
		assertTrue(Arrays.equals(actual, expected));
	}

	/**
	 * Test method for {@link ArrayFactory#newLongArray(long[])}.
	 */
	@Test
	public void testNewLongArray()
	{
		long[] actual;
		long[] expected = { 1L, 2L, 3L };
		actual = ArrayFactory.newLongArray(1L, 2L, 3L);
		assertTrue(Arrays.equals(actual, expected));

		expected = new long[0];
		actual = ArrayFactory.newLongArray();
		assertTrue(Arrays.equals(actual, expected));
	}

	/**
	 * Test the method {@link ArrayFactory#newRangeArray(int, int)}
	 */
	@Test
	public void testNewRangeArray()
	{
		Integer[] actual;
		Integer[] expected;
		actual = ArrayFactory.newRangeArray(5, 9);
		expected = ArrayFactory.newArray(5, 6, 7, 8, 9);
		assertTrue(Arrays.deepEquals(actual, expected));

		actual = ArrayFactory.newRangeArray(1, 49);
		expected = ArrayFactory.newArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
			18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
			40, 41, 42, 43, 44, 45, 46, 47, 48, 49);

		assertTrue(Arrays.deepEquals(actual, expected));
	}

	/**
	 * Test the method {@link ArrayFactory#newRangeArray(int, int)} where end is smaller then start
	 */
	@Test
	public void testNewRangeArrayException()
	{
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ArrayFactory.newRangeArray(9, 8);
		});
	}

	/**
	 * Test method for {@link ArrayFactory#newShortArray(short[])}.
	 */
	@Test
	public void testNewShortArray()
	{
		short[] actual;
		short[] expected = { 1, 2, 3 };
		actual = ArrayFactory.newShortArray((short)1, (short)2, (short)3);
		assertTrue(Arrays.equals(actual, expected));

		expected = new short[0];
		actual = ArrayFactory.newShortArray();
		assertTrue(Arrays.equals(actual, expected));
	}

	/**
	 * Test method for {@link ArrayFactory#newSubArray(Object[], int, int)}
	 */
	@Test
	public void testnewSubArray()
	{
		Integer[] actual;
		Integer[] expected;
		Integer[] data;
		data = ArrayFactory.newArray(1, 2, 3, 4, 5, 6, 7);
		actual = ArrayFactory.newSubArray(data, 2, 5);
		expected = ArrayFactory.newArray(3, 4, 5);
		assertTrue(Arrays.deepEquals(actual, expected));
	}

	/**
	 * Test method for {@link ArrayFactory} with {@link BeanTester}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ArrayFactory.class);
	}

}
