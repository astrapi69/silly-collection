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
import java.util.List;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ModifiedStateTest
{

	@BeforeMethod
	public void setUp() throws Exception
	{
	}

	@AfterMethod
	public void tearDown() throws Exception
	{
	}

	/**
	 * Test is modified.
	 */
	@Test
	public void testIsModified()
	{
		// Create two lists...
		final List<String> previous = new ArrayList<>();
		final List<String> next = new ArrayList<>();
		// Get the current modified state...
		ModifiedState state = ModifiedState.isModified(previous, next);
		AssertJUnit.assertTrue(
			"ModifiedState should have state <ModifiedState.UNMODIFIED> but was <ModifiedState."
				+ state.name() + ">", state.equals(ModifiedState.UNMODIFIED));
		// Add an initial entry...
		next.add("one");
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		AssertJUnit.assertTrue(
			"ModifiedState should have state <ModifiedState.FIRST_MATCH> but was <ModifiedState."
				+ state.name() + ">", state.equals(ModifiedState.FIRST_MATCH));
		// Set the two lists to equal entries so the state gets to
		// 'unmodified'...
		previous.add("one");
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		AssertJUnit.assertTrue(
			"ModifiedState should have state <ModifiedState.UNMODIFIED> but was <ModifiedState."
				+ state.name() + ">", state.equals(ModifiedState.UNMODIFIED));
		// Add a new entry to the existing entries...
		next.add("two");
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		AssertJUnit.assertTrue(
			"ModifiedState should have state <ModifiedState.NEW_MATCH> but was <ModifiedState."
				+ state.name() + ">", state.equals(ModifiedState.NEW_MATCH));
		// Set the two lists to equal entries so the state gets to
		// 'unmodified'...
		previous.add("two");
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		AssertJUnit.assertTrue(
			"ModifiedState should have state <ModifiedState.UNMODIFIED> but was <ModifiedState."
				+ state.name() + ">", state.equals(ModifiedState.UNMODIFIED));
		// Remove existing value...
		next.remove("two");
		// And put another value so the state gets to 'new match'...
		next.add("three");
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		AssertJUnit.assertTrue(
			"ModifiedState should have state <ModifiedState.NEW_MATCH> but was <ModifiedState."
				+ state.name() + ">", state.equals(ModifiedState.NEW_MATCH));
		// Now remove an existing entry...
		next.remove("three");
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		AssertJUnit.assertTrue(
			"ModifiedState should have state <ModifiedState.REMOVED> but was <ModifiedState."
				+ state.name() + ">", state.equals(ModifiedState.REMOVED));
		// Clear now the next list...
		next.clear();
		// And get the current modified state...
		state = ModifiedState.isModified(previous, next);
		AssertJUnit.assertTrue(
			"ModifiedState should have state <ModifiedState.CLEAR> but was <ModifiedState."
				+ state.name() + ">", state.equals(ModifiedState.CLEARED));
	}

}
