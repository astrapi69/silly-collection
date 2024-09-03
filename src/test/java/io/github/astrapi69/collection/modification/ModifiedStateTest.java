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
package io.github.astrapi69.collection.modification;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.list.ListFactory;

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
		final List<String> previous = ListFactory.newArrayList();
		final List<String> next = ListFactory.newArrayList();
		// Get the current modified state...
		ModifiedState state = ModifiedState.isModified(previous, next);
		assertTrue(state.equals(ModifiedState.UNMODIFIED),
			"ModifiedState should have state <ModifiedState.UNMODIFIED> but was <ModifiedState."
				+ state.name() + ">");
		// Add an initial entry...
		next.add("one");
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		assertTrue(state.equals(ModifiedState.FIRST_MATCH),
			"ModifiedState should have state <ModifiedState.FIRST_MATCH> but was <ModifiedState."
				+ state.name() + ">");
		// Set the two lists to equal entries so the state gets to
		// 'unmodified'...
		previous.add("one");
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		assertTrue(state.equals(ModifiedState.UNMODIFIED),
			"ModifiedState should have state <ModifiedState.UNMODIFIED> but was <ModifiedState."
				+ state.name() + ">");
		// Add a new entry to the existing entries...
		next.add("two");
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		assertTrue(state.equals(ModifiedState.NEW_MATCH),
			"ModifiedState should have state <ModifiedState.NEW_MATCH> but was <ModifiedState."
				+ state.name() + ">");
		// Set the two lists to equal entries so the state gets to
		// 'unmodified'...
		previous.add("two");
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		assertTrue(state.equals(ModifiedState.UNMODIFIED),
			"ModifiedState should have state <ModifiedState.UNMODIFIED> but was <ModifiedState."
				+ state.name() + ">");
		// Remove existing value...
		next.remove("two");
		// And put another value so the state gets to 'new match'...
		next.add("three");
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		assertTrue(state.equals(ModifiedState.NEW_MATCH),
			"ModifiedState should have state <ModifiedState.NEW_MATCH> but was <ModifiedState."
				+ state.name() + ">");
		// Now remove an existing entry...
		next.remove("three");
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		assertTrue(state.equals(ModifiedState.REMOVED),
			"ModifiedState should have state <ModifiedState.REMOVED> but was <ModifiedState."
				+ state.name() + ">");
		// Clear now the next list...
		next.clear();
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		assertTrue(state.equals(ModifiedState.CLEARED),
			"ModifiedState should have state <ModifiedState.CLEAR> but was <ModifiedState."
				+ state.name() + ">");
	}

	/**
	 * Test for method {@link ModifiedState#isModified(java.util.Collection, java.util.Collection)}
	 * with null value for next
	 */
	@Test
	public void testIsModifiedNextNull()
	{
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ModifiedState.isModified(ListFactory.newArrayList(), null);
		});
	}

	/**
	 * Test for method {@link ModifiedState#isModified(java.util.Collection, java.util.Collection)}
	 * with null value for previous
	 */
	@Test
	public void testIsModifiedPreviousNull()
	{
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ModifiedState.isModified(null, ListFactory.newArrayList());
		});
	}

	/**
	 * Test method for {@link ModifiedState}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ModifiedState.class);
	}

}
