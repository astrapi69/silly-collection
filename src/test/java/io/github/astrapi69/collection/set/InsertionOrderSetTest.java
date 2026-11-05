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
package io.github.astrapi69.collection.set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The unit test class for the class {@link InsertionOrderSet}
 *
 * @author Asterios Raptis
 */
public class InsertionOrderSetTest
{

	/**
	 * Adds value1 to value4 to the given set, in this order
	 *
	 * @param set
	 *            the set
	 * @return the given set
	 */
	static Set<String> oneToFour(final Set<String> set)
	{
		set.add("value1");
		set.add("value2");
		set.add("value3");
		set.add("value4");
		return set;
	}

	/**
	 * A set of value1 to value4, made in some way, and the order of its elements after value2 is
	 * added again
	 *
	 * @param description
	 *            the name of the case
	 * @param set
	 *            makes the set
	 * @param afterAddAgain
	 *            the order after value2 is added again
	 */
	record OrderCase(String description, Supplier<Set<String>> set, List<String> afterAddAgain) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testOrder(OrderCase)}
	 *
	 * @return the cases
	 */
	static Stream<OrderCase> orders()
	{
		final List<String> moved = List.of("value1", "value3", "value4", "value2");
		return Stream.of(
			new OrderCase("InsertionOrderSet(): value2 moves to the end",
				() -> oneToFour(new InsertionOrderSet<>()), moved),
			new OrderCase("InsertionOrderSet(4): value2 moves to the end",
				() -> oneToFour(new InsertionOrderSet<>(4)), moved),
			new OrderCase("InsertionOrderSet(4, 4f): value2 moves to the end",
				() -> oneToFour(new InsertionOrderSet<>(4, 4f)), moved),
			new OrderCase(
				"InsertionOrderSet(collection of value1 to value4): value2 moves to the end",
				() -> new InsertionOrderSet<>(List.of("value1", "value2", "value3", "value4")),
				moved),
			new OrderCase("setOf(value1 to value4): value2 moves to the end",
				() -> InsertionOrderSet.setOf("value1", "value2", "value3", "value4"), moved),
			new OrderCase("LinkedHashSet, for the difference: value2 keeps its place",
				() -> oneToFour(new LinkedHashSet<>()),
				List.of("value1", "value2", "value3", "value4")));
	}

	/**
	 * Test for the constructors of {@link InsertionOrderSet}, its factory method
	 * {@link InsertionOrderSet#setOf(Object...)} and its method
	 * {@link InsertionOrderSet#add(Object)}: the elements come in the order they were added, and an
	 * element added again moves to the end, which a {@link LinkedHashSet} does not do; both return
	 * false for it
	 *
	 * @param order
	 *            the set and the order after value2 is added again
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("orders")
	public void testOrder(final OrderCase order)
	{
		final Set<String> set = order.set().get();
		assertEquals(List.of("value1", "value2", "value3", "value4"), new ArrayList<>(set));
		assertFalse(set.add("value2"));
		assertEquals(order.afterAddAgain(), new ArrayList<>(set));
	}

	/**
	 * A change of the set a, b, what it returns and the order of the elements afterwards
	 *
	 * @param description
	 *            the name of the case
	 * @param change
	 *            the change
	 * @param result
	 *            what it returns
	 * @param expected
	 *            the order of the elements afterwards
	 */
	record Change(String description, Function<Set<String>, Boolean> change, boolean result,
		List<String> expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testAdd(Change)}
	 *
	 * @return the cases
	 */
	static Stream<Change> changes()
	{
		return Stream.of(
			new Change("add(c), a new element: true, c last", set -> set.add("c"), true,
				List.of("a", "b", "c")),
			new Change("add(a), a contained element: false, a moves to the end (#41)",
				set -> set.add("a"), false, List.of("b", "a")),
			new Change("addAll(a), a contained element: false, as the set holds no new one (#41)",
				set -> set.addAll(List.of("a")), false, List.of("b", "a")),
			new Change(
				"addAll(a, c), a contained and a new element: true, both behind b in their order (#41)",
				set -> set.addAll(List.of("a", "c")), true, List.of("b", "a", "c")),
			new Change("addAll(), no element: false", set -> set.addAll(List.of()), false,
				List.of("a", "b")));
	}

	/**
	 * Test for the methods {@link InsertionOrderSet#add(Object)} and
	 * {@link InsertionOrderSet#addAll(java.util.Collection)}: they return true if the set did not
	 * contain an element, as their Javadoc and the {@link Set} contract say, and an element added
	 * again moves to the end
	 *
	 * @param change
	 *            the change, what it returns and the order afterwards
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("changes")
	public void testAdd(final Change change)
	{
		final Set<String> set = new InsertionOrderSet<>();
		set.add("a");
		set.add("b");
		assertEquals(change.result(), change.change().apply(set));
		assertEquals(change.expected(), new ArrayList<>(set));
	}

}
