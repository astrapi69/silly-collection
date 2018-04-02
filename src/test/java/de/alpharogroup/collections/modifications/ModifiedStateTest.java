/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.alpharogroup.collections.modifications;

import static org.testng.AssertJUnit.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

/**
 * The unit test class for the class {@link ModifiedState}.
 */
public class ModifiedStateTest
{

	/**
	 * Test for method {@link ModifiedState#isModified(java.util.Collection, java.util.Collection)}
	 */
	@Test
	public void testIsModified()
	{
		// Create two lists...
		final List<String> previous = new ArrayList<>();
		final List<String> next = new ArrayList<>();
		// Get the current modified state...
		ModifiedState state = ModifiedState.isModified(previous, next);
		assertTrue(
			"ModifiedState should have state <ModifiedState.UNMODIFIED> but was <ModifiedState."
				+ state.name() + ">",
			state.equals(ModifiedState.UNMODIFIED));
		// Add an initial entry...
		next.add("one");
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		assertTrue(
			"ModifiedState should have state <ModifiedState.FIRST_MATCH> but was <ModifiedState."
				+ state.name() + ">",
			state.equals(ModifiedState.FIRST_MATCH));
		// Set the two lists to equal entries so the state gets to
		// 'unmodified'...
		previous.add("one");
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		assertTrue(
			"ModifiedState should have state <ModifiedState.UNMODIFIED> but was <ModifiedState."
				+ state.name() + ">",
			state.equals(ModifiedState.UNMODIFIED));
		// Add a new entry to the existing entries...
		next.add("two");
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		assertTrue(
			"ModifiedState should have state <ModifiedState.NEW_MATCH> but was <ModifiedState."
				+ state.name() + ">",
			state.equals(ModifiedState.NEW_MATCH));
		// Set the two lists to equal entries so the state gets to
		// 'unmodified'...
		previous.add("two");
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		assertTrue(
			"ModifiedState should have state <ModifiedState.UNMODIFIED> but was <ModifiedState."
				+ state.name() + ">",
			state.equals(ModifiedState.UNMODIFIED));
		// Remove existing value...
		next.remove("two");
		// And put another value so the state gets to 'new match'...
		next.add("three");
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		assertTrue(
			"ModifiedState should have state <ModifiedState.NEW_MATCH> but was <ModifiedState."
				+ state.name() + ">",
			state.equals(ModifiedState.NEW_MATCH));
		// Now remove an existing entry...
		next.remove("three");
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		assertTrue("ModifiedState should have state <ModifiedState.REMOVED> but was <ModifiedState."
			+ state.name() + ">", state.equals(ModifiedState.REMOVED));
		// Clear now the next list...
		next.clear();
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		assertTrue("ModifiedState should have state <ModifiedState.CLEAR> but was <ModifiedState."
			+ state.name() + ">", state.equals(ModifiedState.CLEARED));
	}

	/**
	 * Test for method {@link ModifiedState#isModified(java.util.Collection, java.util.Collection)}
	 * with null value for next
	 */
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testIsModifiedNextNull()
	{
		ModifiedState.isModified(new ArrayList<>(), null);
	}

	/**
	 * Test for method {@link ModifiedState#isModified(java.util.Collection, java.util.Collection)}
	 * with null value for previous
	 */
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testIsModifiedPreviousNull()
	{
		ModifiedState.isModified(null, new ArrayList<>());
	}

	/**
	 * Test method for {@link ModifiedState}
	 */
	@Test(expectedExceptions = { BeanTestException.class, InvocationTargetException.class,
			UnsupportedOperationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ModifiedState.class);
	}

}
