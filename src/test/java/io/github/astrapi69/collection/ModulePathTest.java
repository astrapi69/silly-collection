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
package io.github.astrapi69.collection;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test that a consumer that declares this library alone compiles against it: a module on the module
 * path that requires this module alone, and a class on the class path (#95). {@code MapFactory}
 * takes {@code KeyValuePair} of silly-bean, so the compiler of every consumer that calls
 * {@code MapFactory} needs silly-bean, also one that never names {@code KeyValuePair}: javac reads
 * the class file of every overload when it resolves a call. A consumer module that names
 * {@code KeyValuePair} needs more: this module has to require silly-bean {@code transitive}, or
 * javac reports that the module of the consumer does not read it. Gradle's test task hands over
 * what such a consumer compiles against in the system property
 * {@value #CONSUMER_COMPILE_CLASSPATH_PROPERTY}: this project resolved as a consumer's compile
 * class path resolves it, its apiElements, that is the built jar and the api dependencies
 * (gradle/testing.gradle). silly-bean is an api dependency, so it is on that path; as an
 * implementation dependency it was not, and no consumer compiled (#95)
 */
public class ModulePathTest
{

	/**
	 * The system property with the compile class path of a consumer that declares this library
	 * alone, separated by the platform's path separator
	 */
	private static final String CONSUMER_COMPILE_CLASSPATH_PROPERTY = "module.path.test.consumer.compile.classpath";

	/**
	 * The module descriptor of a consumer module, which requires this module and nothing else
	 */
	private static final String CONSUMER_MODULE_INFO = String.join("\n", "module consumer.check",
		"{", "	requires io.github.astrapisixtynine.silly.collection;", "}", "");

	/**
	 * A consumer: what it compiles, and on which path
	 *
	 * @param description
	 *            the description of the consumer, the name of the case
	 * @param pathOption
	 *            the javac option that names the path: {@code --module-path} or
	 *            {@code --class-path}
	 * @param sources
	 *            the sources of the consumer, by their path relative to the source directory
	 */
	record Consumer(String description, String pathOption, Map<String, String> sources) {
		@Override
		public String toString()
		{
			return description;
		}
	}

	/**
	 * The consumers, on the module path and on the class path each: one that names
	 * {@code KeyValuePair} to build a map with {@code MapFactory}, and one that calls
	 * {@code MapFactory} as {@code SplitStringExtensions} of silly-strings does, without naming
	 * {@code KeyValuePair}, the case of #95
	 *
	 * @return the consumers
	 */
	static Stream<Consumer> consumers()
	{
		final String namesKeyValuePair = String.join("\n", "package consumer.check;", "",
			"import java.util.List;", "import java.util.Map;", "",
			"import io.github.astrapi69.collection.map.MapFactory;",
			"import io.github.astrapi69.collection.pair.KeyValuePair;", "", "public class Pairs",
			"{", "	public static Map<String, Integer> map()", "	{",
			"		return MapFactory.newHashMap(List.of(new KeyValuePair<>(\"a\", 1)));", "	}",
			"}", "");
		final String callsAsSillyStrings = String.join("\n", "package consumer.check;", "",
			"import java.util.List;", "import java.util.Map;", "",
			"import io.github.astrapi69.collection.map.MapFactory;", "", "public class Words", "{",
			"	public static Map<String, Integer> count()", "	{",
			"		return MapFactory.newCounterMap(MapFactory.newHashMap(), List.of(\"a\", \"b\", \"a\"),",
			"			false);", "	}", "}", "");
		return Stream.of(
			new Consumer("a module that names KeyValuePair", "--module-path",
				Map.of("module-info.java", CONSUMER_MODULE_INFO, "consumer/check/Pairs.java",
					namesKeyValuePair)),
			new Consumer("a module that calls MapFactory as silly-strings does", "--module-path",
				Map.of("module-info.java", CONSUMER_MODULE_INFO, "consumer/check/Words.java",
					callsAsSillyStrings)),
			new Consumer("a class on the class path that names KeyValuePair", "--class-path",
				Map.of("consumer/check/Pairs.java", namesKeyValuePair)),
			new Consumer("a class on the class path that calls MapFactory as silly-strings does",
				"--class-path", Map.of("consumer/check/Words.java", callsAsSillyStrings)));
	}

	/**
	 * The compile class path of a consumer that declares this library alone. A missing property
	 * fails the test: a check that has nothing to compile against does not pass
	 *
	 * @return the compile class path
	 */
	private static String consumerCompileClasspath()
	{
		final String classpath = System.getProperty(CONSUMER_COMPILE_CLASSPATH_PROPERTY);
		assertNotNull(classpath,
			"the system property " + CONSUMER_COMPILE_CLASSPATH_PROPERTY + " is not set");
		return classpath;
	}

	/**
	 * Writes the given content to the given file, with its parent directories
	 *
	 * @param file
	 *            the file
	 * @param content
	 *            the content
	 * @return the file
	 * @throws IOException
	 *             if the file cannot be written
	 */
	private static Path write(final Path file, final String content) throws IOException
	{
		Files.createDirectories(file.getParent());
		return Files.writeString(file, content, StandardCharsets.UTF_8);
	}

	/**
	 * Test that a consumer that declares this library alone compiles against it, as silly-bean is
	 * on its compile class path and this module requires it {@code transitive} (#95)
	 *
	 * @param consumer
	 *            the consumer
	 * @param directory
	 *            the directory for the sources and the classes of the consumer
	 * @throws IOException
	 *             if a source file cannot be written
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("consumers")
	public void testConsumerCompiles(final Consumer consumer, @TempDir final Path directory)
		throws IOException
	{
		final List<Path> sources = new ArrayList<>();
		for (final Map.Entry<String, String> source : consumer.sources().entrySet())
		{
			sources
				.add(write(directory.resolve("src").resolve(source.getKey()), source.getValue()));
		}
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		assertNotNull(compiler, "no system Java compiler");
		final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
		try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics,
			Locale.ENGLISH, StandardCharsets.UTF_8))
		{
			final Boolean compiled = compiler.getTask(null, fileManager, diagnostics,
				List.of(consumer.pathOption(), consumerCompileClasspath(), "-d",
					directory.resolve("classes").toString()),
				null, fileManager.getJavaFileObjects(sources.toArray(Path[]::new))).call();
			final String messages = diagnostics.getDiagnostics().stream()
				.map(diagnostic -> diagnostic.getMessage(Locale.ENGLISH))
				.collect(Collectors.joining("; "));
			assertTrue(compiled, () -> "the consumer does not compile: " + messages);
		}
	}

}
