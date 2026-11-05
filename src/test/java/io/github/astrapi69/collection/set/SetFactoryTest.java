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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.collection.list.ListFactory;
import io.github.astrapi69.comparator.SortOrder;
import io.github.astrapi69.comparator.object.StringLengthComparator;

/**
 * The unit test class for the class {@link SetFactory}.
 */
public class SetFactoryTest
{

	/**
	 * A new list of foo and fasel, the collection the factory methods are given
	 *
	 * @return the list
	 */
	static List<String> fooFasel()
	{
		return ListFactory.newArrayList("foo", "fasel");
	}

	/**
	 * A new list of b and d
	 *
	 * @return the list
	 */
	static List<String> bd()
	{
		return ListFactory.newArrayList("b", "d");
	}

	/**
	 * A new empty list of strings
	 *
	 * @return the list
	 */
	static List<String> none()
	{
		return ListFactory.newArrayList();
	}

	/**
	 * A call of a factory method of a {@link HashSet}, {@link LinkedHashSet} or
	 * {@link InsertionOrderSet}, and the set it makes
	 *
	 * @param description
	 *            the name of the case
	 * @param factory
	 *            the call
	 * @param type
	 *            the type of the set, the one the Javadoc of the factory method names
	 * @param expected
	 *            the elements of the set, in its order if the type keeps one
	 * @param ordered
	 *            true if the type keeps an order
	 */
	record Factory(String description, Supplier<Set<String>> factory, Class<?> type,
		List<String> expected, boolean ordered) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testFactory(Factory)}
	 *
	 * @return the cases
	 */
	static Stream<Factory> factories()
	{
		return Stream.of(
			new Factory("newHashSet(): empty", SetFactory::newHashSet, HashSet.class, List.of(),
				false),
			new Factory("newHashSet(foo, bar, foo): foo and bar, each once",
				() -> SetFactory.newHashSet("foo", "bar", "foo"), HashSet.class,
				List.of("foo", "bar"), false),
			new Factory("newHashSet(foo, fasel; foo, bar, foo): foo, fasel, bar",
				() -> SetFactory.newHashSet(fooFasel(), "foo", "bar", "foo"), HashSet.class,
				List.of("foo", "fasel", "bar"), false),
			new Factory("newHashSet(an empty collection; foo, bar, foo): foo, bar",
				() -> SetFactory.newHashSet(none(), "foo", "bar", "foo"), HashSet.class,
				List.of("foo", "bar"), false),
			new Factory("newHashSet(null; foo), the collection is optional: foo",
				() -> SetFactory.newHashSet((Collection<String>)null, "foo"), HashSet.class,
				List.of("foo"), false),
			new Factory("newHashSet(foo, fasel; exclude foo; foo, bar, foo): fasel, bar",
				() -> SetFactory.newHashSet((Collection<String>)fooFasel(), List.of("foo"), "foo",
					"bar", "foo"),
				HashSet.class, List.of("fasel", "bar"), false),
			new Factory("newHashSet(foo, fasel; exclude foo, fasel): empty",
				() -> SetFactory.newHashSet((Collection<String>)fooFasel(), fooFasel()),
				HashSet.class, List.of(), false),
			new Factory("newInsertionOrderSet(): empty", SetFactory::newInsertionOrderSet,
				InsertionOrderSet.class, List.of(), true),
			new Factory("newInsertionOrderSet(foo, bar, foo): foo moves behind bar",
				() -> SetFactory.newInsertionOrderSet("foo", "bar", "foo"), InsertionOrderSet.class,
				List.of("bar", "foo"), true),
			new Factory("newInsertionOrderSet(foo, fasel; foo, bar, foo): fasel, bar, foo",
				() -> SetFactory.newInsertionOrderSet(fooFasel(), "foo", "bar", "foo"),
				InsertionOrderSet.class, List.of("fasel", "bar", "foo"), true),
			new Factory("newInsertionOrderSet(an empty collection; foo, bar, foo): bar, foo",
				() -> SetFactory.newInsertionOrderSet(none(), "foo", "bar", "foo"),
				InsertionOrderSet.class, List.of("bar", "foo"), true),
			new Factory("newLinkedHashSet(): empty", SetFactory::newLinkedHashSet,
				LinkedHashSet.class, List.of(), true),
			new Factory("newLinkedHashSet(foo, bar, foo): foo keeps its place",
				() -> SetFactory.newLinkedHashSet("foo", "bar", "foo"), LinkedHashSet.class,
				List.of("foo", "bar"), true),
			new Factory("newLinkedHashSet(foo, fasel; foo, bar, foo): foo, fasel, bar",
				() -> SetFactory.newLinkedHashSet(fooFasel(), "foo", "bar", "foo"),
				LinkedHashSet.class, List.of("foo", "fasel", "bar"), true),
			new Factory("newLinkedHashSet(an empty collection; foo, bar, foo): foo, bar",
				() -> SetFactory.newLinkedHashSet(none(), "foo", "bar", "foo"), LinkedHashSet.class,
				List.of("foo", "bar"), true));
	}

	/**
	 * Test for the factory methods of {@link HashSet}, {@link LinkedHashSet} and
	 * {@link InsertionOrderSet}: the new set is of the type the Javadoc names and holds the
	 * elements of the optional collection and the given elements, each once, without the excluded
	 * ones; a {@link LinkedHashSet} keeps the place of an element added again, an
	 * {@link InsertionOrderSet} moves it to the end
	 *
	 * @param factory
	 *            the call and the set it makes
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("factories")
	public void testFactory(final Factory factory)
	{
		final Set<String> set = factory.factory().get();
		assertInstanceOf(factory.type(), set);
		if (factory.ordered())
		{
			assertEquals(factory.expected(), new ArrayList<>(set));
		}
		else
		{
			assertEquals(Set.copyOf(factory.expected()), set);
		}
	}

	/**
	 * A call of a factory method of a {@link TreeSet}, and the set it makes
	 *
	 * @param description
	 *            the name of the case
	 * @param factory
	 *            the call
	 * @param comparator
	 *            the comparator of the set, null for the natural order
	 * @param expected
	 *            the elements of the set in its order
	 */
	record TreeFactory(String description, Supplier<SortedSet<String>> factory,
		Comparator<String> comparator, List<String> expected) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The cases of {@link #testTreeFactory(TreeFactory)}
	 *
	 * @return the cases
	 */
	static Stream<TreeFactory> treeFactories()
	{
		final Comparator<String> longerFirst = StringLengthComparator.of(SortOrder.ASCENDING);
		final Comparator<String> reverse = Comparator.reverseOrder();
		final Comparator<String> natural = Comparator.naturalOrder();
		final Comparator<String> none = null;
		return Stream.of(
			new TreeFactory("newTreeSet(): empty", SetFactory::newTreeSet, null, List.of()),
			new TreeFactory("newTreeSet(foo, bar, foo): bar, foo",
				() -> SetFactory.newTreeSet("foo", "bar", "foo"), null, List.of("bar", "foo")),
			new TreeFactory("newTreeSet(foo, fasel; foo, bar, foo): bar, fasel, foo",
				() -> SetFactory.newTreeSet(fooFasel(), "foo", "bar", "foo"), null,
				List.of("bar", "fasel", "foo")),
			new TreeFactory("newTreeSet(an empty collection; foo, bar, foo): bar, foo",
				() -> SetFactory.newTreeSet(none(), "foo", "bar", "foo"), null,
				List.of("bar", "foo")),
			new TreeFactory(
				"newTreeSet(foo, fasel; the longer first; food, barista, fao): five, the longer first",
				() -> SetFactory.newTreeSet(fooFasel(), longerFirst, "food", "barista", "fao"),
				longerFirst, List.of("barista", "fasel", "food", "fao", "foo")),
			new TreeFactory(
				"newTreeSet(an empty collection; the longer first; foo, bar, foo): bar, foo",
				() -> SetFactory.newTreeSet(none(), longerFirst, "foo", "bar", "foo"), longerFirst,
				List.of("bar", "foo")),
			new TreeFactory("newTreeSet(b, d; reverseOrder; a, c): d, c, b, a",
				() -> SetFactory.newTreeSet(bd(), reverse, "a", "c"), reverse,
				List.of("d", "c", "b", "a")),
			new TreeFactory("newTreeSet(b, d; a null comparator; a, c): the natural order",
				() -> SetFactory.newTreeSet(bd(), none, "a", "c"), null,
				List.of("a", "b", "c", "d")),
			new TreeFactory("newTreeSet(naturalOrder; foo, fasel, food, barista, fao): sorted",
				() -> SetFactory.newTreeSet(natural, "foo", "fasel", "food", "barista", "fao"),
				natural, List.of("barista", "fao", "fasel", "foo", "food")),
			new TreeFactory(
				"newTreeSetSupplier(naturalOrder), collecting foo, fasel, food, barista, fao: sorted",
				() -> Stream.of("foo", "fasel", "food", "barista", "fao")
					.collect(Collectors.toCollection(SetFactory.newTreeSetSupplier(natural))),
				natural, List.of("barista", "fao", "fasel", "foo", "food")));
	}

	/**
	 * Test for the factory methods of a {@link TreeSet} and for
	 * {@link SetFactory#newTreeSetSupplier(Comparator)}: the new set is a {@link TreeSet} with the
	 * given comparator or none, and holds the elements of the optional collection and the given
	 * elements in the order of its comparator
	 *
	 * @param factory
	 *            the call and the set it makes
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("treeFactories")
	public void testTreeFactory(final TreeFactory factory)
	{
		final SortedSet<String> set = factory.factory().get();
		assertInstanceOf(TreeSet.class, set);
		assertSame(factory.comparator(), set.comparator());
		assertEquals(factory.expected(), new ArrayList<>(set));
	}

	/**
	 * Test for method {@link SetFactory#newTreeSetSupplier(Comparator)}: the supplier makes a new
	 * empty {@link TreeSet} with the given comparator each time
	 */
	@Test
	public void testNewTreeSetSupplierMakesANewSetEachTime()
	{
		final Comparator<String> comparator = Comparator.naturalOrder();
		final Supplier<SortedSet<String>> supplier = SetFactory.newTreeSetSupplier(comparator);
		final SortedSet<String> first = supplier.get();
		first.add("a");
		final SortedSet<String> second = supplier.get();
		assertNotSame(first, second);
		assertEquals(Set.of(), second);
		assertSame(comparator, second.comparator());
	}

	/**
	 * Test method for {@link SetFactory} with {@link BeanTester}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(SetFactory.class);
	}

}
