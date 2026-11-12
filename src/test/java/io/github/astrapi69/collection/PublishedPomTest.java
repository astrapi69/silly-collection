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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Test of the pom that a publication of this library carries, the dependencies a Maven consumer
 * gets from it and their scopes (#95). Maven puts a dependency of the compile scope on a consumer's
 * compile class path, one of the runtime scope only on its runtime and test class paths. Gradle's
 * test task hands over the generated pom in the system property {@value #POM_PROPERTY}
 * (gradle/testing.gradle)
 */
public class PublishedPomTest
{

	/**
	 * The system property with the path of the generated pom
	 */
	private static final String POM_PROPERTY = "published.pom.test.pom";

	/**
	 * The child elements of the given element with the given name
	 *
	 * @param parent
	 *            the element
	 * @param name
	 *            the name of the children
	 * @return the children with that name, in their order
	 */
	private static List<Element> children(final Element parent, final String name)
	{
		final NodeList nodes = parent.getChildNodes();
		return IntStream.range(0, nodes.getLength()).mapToObj(nodes::item)
			.filter(node -> node.getNodeType() == Node.ELEMENT_NODE).map(Element.class::cast)
			.filter(element -> name.equals(element.getTagName())).toList();
	}

	/**
	 * The text of the one child element of the given element with the given name, or null if it has
	 * none
	 *
	 * @param parent
	 *            the element
	 * @param name
	 *            the name of the child
	 * @return the text of the child, or null
	 */
	private static String text(final Element parent, final String name)
	{
		return children(parent, name).stream().findFirst().map(Element::getTextContent)
			.map(String::strip).orElse(null);
	}

	/**
	 * Reads the dependencies of the generated pom, the elements {@code dependency} of the element
	 * {@code dependencies} of the project. A missing property fails the test: a check that has
	 * nothing to read does not pass
	 *
	 * @return the scope of each dependency by its group id and artifact id, as
	 *         {@code groupId:artifactId}, in the order of the pom
	 * @throws ParserConfigurationException
	 *             if no XML parser can be made
	 * @throws SAXException
	 *             if the pom is no XML
	 * @throws IOException
	 *             if the pom cannot be read
	 */
	private static Map<String, String> scopesOfTheDependencies()
		throws ParserConfigurationException, SAXException, IOException
	{
		final String pom = System.getProperty(POM_PROPERTY);
		assertNotNull(pom, "the system property " + POM_PROPERTY + " is not set");
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		final Element project = factory.newDocumentBuilder().parse(new File(pom))
			.getDocumentElement();
		final Map<String, String> scopes = new LinkedHashMap<>();
		for (final Element dependencies : children(project, "dependencies"))
		{
			for (final Element dependency : children(dependencies, "dependency"))
			{
				// a dependency without a scope has Maven's default scope, compile
				scopes.put(text(dependency, "groupId") + ":" + text(dependency, "artifactId"),
					Objects.requireNonNullElse(text(dependency, "scope"), "compile"));
			}
		}
		return scopes;
	}

	/**
	 * Test that the pom gives every dependency the runtime scope: no type of theirs is in a public
	 * signature of this library, so a Maven consumer needs none of them to compile. silly-bean,
	 * which {@code MapFactory} took with {@code KeyValuePair} and which therefore had the compile
	 * scope (#95), is no dependency any more (#158)
	 *
	 * @throws ParserConfigurationException
	 *             if no XML parser can be made
	 * @throws SAXException
	 *             if the pom is no XML
	 * @throws IOException
	 *             if the pom cannot be read
	 */
	@Test
	public void testScopesOfTheDependencies()
		throws ParserConfigurationException, SAXException, IOException
	{
		assertEquals(Map.of("org.apache.commons:commons-collections4", "runtime",
			"io.github.astrapi69:comparator-extensions", "runtime", "io.github.astrapi69:jobj-core",
			"runtime"), scopesOfTheDependencies());
	}

}
