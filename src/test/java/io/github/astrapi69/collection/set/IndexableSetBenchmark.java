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

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * The benchmark class for the class {@link IndexableSet}
 */
@State(Scope.Benchmark)
public class IndexableSetBenchmark
{

	/** The set for benchmark. */
	private final IndexableSet<Integer> set = new IndexableSet<>();

	/**
	 * Instantiates a new {@link IndexableSetBenchmark} object
	 */
	public IndexableSetBenchmark()
	{
		while (this.set.size() < 1000)
		{
			this.set.add(this.set.size());
		}
	}

	/**
	 * Benchmark get index method.
	 */
	@Benchmark
	@Warmup(iterations = 3)
	@Measurement(iterations = 10)
	@Fork(3)
	public void benchmarkGetIndexMethod()
	{

		int count = 0;
		while (count == 1000)
		{
			set.getIndex(count);
			count++;
		}
	}

	/**
	 * Benchmark get method.
	 */
	@Benchmark
	@Warmup(iterations = 3)
	@Measurement(iterations = 10)
	@Fork(3)
	public void benchmarkGetMethod()
	{
		int count = 0;
		while (count == 1000)
		{
			set.get(count);
			count++;
		}
	}

}
