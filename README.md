# silly-collection

**Java helpers for collections: lists that stay unique or sorted, sets that keep their order or
an index, factories and utilities for lists, sets, maps and arrays, sorted and indexed
`Properties`, and iterators over arrays and enumerations.**

<div style="text-align: center">

[![Java CI with Gradle](https://github.com/astrapi69/silly-collection/actions/workflows/gradle.yml/badge.svg)](https://github.com/astrapi69/silly-collection/actions/workflows/gradle.yml)
[![Coverage Status](https://codecov.io/gh/astrapi69/silly-collection/branch/develop/graph/badge.svg)](https://codecov.io/gh/astrapi69/silly-collection)
[![Mutation Score](https://img.shields.io/endpoint?url=https%3A%2F%2Fraw.githubusercontent.com%2Fastrapi69%2Fsilly-collection%2Fgh-pages%2Fbadges%2Fmutation-score-develop.json)](https://pitest.org/)
[![Open Issues](https://img.shields.io/github/issues/astrapi69/silly-collection.svg?style=flat)](https://github.com/astrapi69/silly-collection/issues)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.astrapi69/silly-collection.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.astrapi69/silly-collection)
[![Javadoc](https://www.javadoc.io/badge/io.github.astrapi69/silly-collection.svg)](https://www.javadoc.io/doc/io.github.astrapi69/silly-collection)
[![MIT license](https://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-25-orange)](https://openjdk.org/projects/jdk/25/)

</div>

silly-collection collects the collection code that otherwise gets written again in every project,
and keeps it tested: every line is covered, and PIT checks that the tests notice when the code
changes.

## Why silly-collection

It fills the gaps the JDK leaves between `List`, `Set` and `Map`: a list that refuses duplicates,
a list that keeps itself sorted by a comparator, a set that keeps the insertion order and gives an
element by its index, `Properties` that list their keys in order, and the small operations on
lists, maps and arrays that are one line each but easy to get wrong. Every line of it is tested,
and a mutation score close to 100% says that the tests check what the code does, not only that
they run it. Its dependencies are Apache Commons Collections and three small libraries of this
family. If you need immutable or multi-valued collections, primitive collections or caches,
[Guava](https://github.com/google/guava), [Apache Commons
Collections](https://commons.apache.org/proper/commons-collections/) or [Eclipse
Collections](https://github.com/eclipse/eclipse-collections) are the better choice: they are far
larger and used almost everywhere.

## Table of contents

- [Why silly-collection](#why-silly-collection)
- [Requirements](#requirements)
- [Installation](#installation)
- [Upgrading from 28.x](#upgrading-from-28x)
- [Quick start](#quick-start)
- [Features](#features)
- [Quality](#quality)
- [Verifying the release signatures](#verifying-the-release-signatures)
- [Releasing](#releasing)
- [Contributing](#contributing)
- [How this project is made](#how-this-project-is-made)
- [License](#license)
- [Support this project](#support-this-project)
- [Similar projects](#similar-projects)
- [Credits](#credits)

## Requirements

**JDK 25 or newer**, at build and at runtime: from 29.0 on its jar holds Java 25 bytecode and does
not load on an older JVM. **28.3 is the last release for Java 17**; a project below Java 25 stays on
it (see [Upgrading from 28.x](#upgrading-from-28x)). The dependencies are Apache Commons Collections 4.6.0, and jobj-core, silly-bean and
comparator-extensions of this family, which run on Java 17 and 25 alike. silly-bean is an API
dependency: `MapFactory` takes its `KeyValuePair`, so the module requires it `transitive`, and the
pom and the Gradle module metadata put it on the compile class path of a project that declares this
library. The other three are for the runtime only.

## Installation

Latest release on Maven Central: 29.1, for Java 25 (for Java 17: 28.3)

[![Maven Central](https://img.shields.io/maven-central/v/io.github.astrapi69/silly-collection.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.astrapi69/silly-collection)

**Gradle** (version catalog, `gradle/libs.versions.toml`):

```toml
[versions]
silly-collection-version = "29.1"

[libraries]
silly-collection = { module = "io.github.astrapi69:silly-collection", version.ref = "silly-collection-version" }
```

```groovy
dependencies {
    implementation libs.silly.collection
}
```

**Gradle** (plain):

```groovy
dependencies {
    implementation "io.github.astrapi69:silly-collection:29.1"
}
```

**Maven**:

```xml
<dependency>
    <groupId>io.github.astrapi69</groupId>
    <artifactId>silly-collection</artifactId>
    <version>29.1</version>
</dependency>
```

The module name is `io.github.astrapisixtynine.silly.collection`.

29.0 publishes silly-bean for the runtime only (#95): a project on 29.0 that calls `MapFactory`
declares `io.github.astrapi69:silly-bean:3.0` beside it, and a module that names `KeyValuePair`
requires `io.github.astrapisixtynine.silly.bean` as well. From 29.1 on the declaration above is
enough.

## Upgrading from 28.x

**28.3 is the last release for Java 17.** The 28.x line is frozen: the only exception are security
fixes, which then come as a patch release on a branch `release/28.x` that starts at `RELEASE-28.3`.
New fixes and features come in 29.x only.

29.0 needs Java 25, and the list classes keep their own rules where the methods they inherit from
`ArrayList` broke them. The CHANGELOG's [MIGRATION FROM 28.x](CHANGELOG.md#version-290) gives for
each breaking change what it was, what it is and what to change; in short:

- `SortedList` and `SortedUniqueList` with a comparator refuse explicit positioning, as `TreeSet`
  does: `set`, `sort`, `addFirst`, `addLast` and the `set` and `add` of their list iterators throw an
  `UnsupportedOperationException`, and so do `Collections.swap`, `reverse`, `shuffle` and the like,
  at their first write, with the list unchanged; `replaceAll` writes only values that keep the order;
- `subList` of the three list classes, and `reversed()` of the sorted lists, read and remove only;
  `subList(from, to).clear()` still removes a range;
- `UniqueList.reversed().add(i, e)` puts an element the list holds at `i` of the view, where it
  landed one place too early; the two moves its view cannot make keep their contract are refused;
- `UniqueList.addFirst` and `addLast` move an element the list holds, as `LinkedHashSet` does, and
  `SortedUniqueList.set` without a comparator swaps, as `UniqueList.set` does.

From 28.2, read [Version 28.3](CHANGELOG.md#version-283) first: its notes mark the changes of 28.3 a
caller is likely to notice, among them `isEqualCollection`, which compares how often each element
occurs.

## Quick start

The examples are checked: each block runs, with the values its comments name as assertions, in
[`ReadmeExamplesTest`](src/test/java/io/github/astrapi69/collection/ReadmeExamplesTest.java).

**Lists that stay unique or sorted.** A `UniqueList` holds each element once, a `SortedList` keeps
the order of its comparator, a `SortedUniqueList` both.

```java
List<String> unique = ListFactory.newUniqueList("b", "a", "b"); // [b, a]
boolean added = unique.add("a");                                // false: held already

List<String> start = List.of("c", "a");
List<String> sorted = ListFactory.newSortedList(start, Comparator.naturalOrder(), "b"); // [a, b, c]
sorted.add("ab");                                                                       // [a, ab, b, c]
List<String> sortedUnique = ListFactory.newSortedUniqueList(start,
    Comparator.naturalOrder(), "a", "b");                                               // [a, b, c]
```

**Working with lists.**

```java
List<String> letters = List.of("A", "B", "C", "D", "E", "F");
ListExtensions.removeLastValues(letters, 2); // [A, B, C, D]
ListExtensions.splitToParts(letters, 4);     // [[A, B, C, D], [E, F]]
ListExtensions.getFirst(letters);            // "A"
ListExtensions.getLast(letters);             // "F"

List<String> words = new ArrayList<>(List.of("ccc", "a", "bb"));
ListExtensions.sortByProperty(words, String::length, true); // [a, bb, ccc], ascending
```

**Sets that keep the order.** An `InsertionOrderSet` moves an element that is added again to the
end; an `IndexableSet` gives an element by its index, and the index of an element.

```java
Set<String> set = SetFactory.newInsertionOrderSet("b", "a");
boolean added = set.add("b"); // false, and "b" moves to the end: [a, b]

IndexableSet<String> indexable = new IndexableSet<>();
indexable.add("c");
indexable.add("a");
indexable.get(1);        // "a"
indexable.getIndex("c"); // 0
```

**Maps.**

```java
Map<Integer, String> numbers = new LinkedHashMap<>();
numbers.put(1, "one");
numbers.put(2, "two");
numbers.put(3, "one");
MapExtensions.getKeyFromValue(numbers, "one");   // 1, the first in iteration order
MapExtensions.getKeysFromValue(numbers, "one");  // 1 and 3
MapExtensions.getKeyFromValue(numbers, "three"); // null
```

**Arrays.** The factories and the operations return a new array; the given one stays as it is.

```java
Integer[] numbers = ArrayFactory.newArray(1, 2, 3);
ArrayExtensions.removeFromStart(numbers, new Integer[] { 1 }); // [2, 3]
ArrayExtensions.removeFromStart(numbers, new Integer[] { 9 }); // [1, 2, 3], a copy: no such prefix
ArrayExtensions.remove(numbers, 2, 0);                         // [2]
ArrayExtensions.concatenate(numbers, new Integer[] { 4 });     // [1, 2, 3, 4]
ArrayFactory.newRangeArray(1, 3);                              // [1, 2, 3], both ends included
```

**Collections.**

```java
CollectionExtensions.isEqualCollection(List.of(1, 1, 2), List.of(2, 1, 1)); // true: the same elements, as often each
CollectionExtensions.isEqualCollection(List.of(1, 2), List.of(3, 4));       // false
Number[] mixed = CollectionExtensions.toArray(List.<Number> of(1, 2.5));    // a Number[]: 1, 2.5
CollectionExtensions.partition(List.of(1, 2, 3, 4, 5), 2);                  // [[1, 2], [3, 4], [5]]
```

**Properties in order.** A `SortedProperties` lists its keys in the order of its comparator; an
`IndexSortedProperties` gives a value by the index of its key in that order.

```java
SortedProperties sorted = SortedProperties.of();
sorted.setProperty("b", "2");
sorted.setProperty("a", "1");
sorted.keySet(); // [a, b], a view in the order of the keys

IndexSortedProperties indexed = IndexSortedProperties.of();
indexed.setProperty("b", "2");
indexed.setProperty("a", "1");
indexed.getProperty(0); // "1", the value of "a"
indexed.indexOf("2");   // 1
```

## Features

| Package `io.github.astrapi69.collection...` | What it holds |
|---|---|
| (root) | `CollectionExtensions`: emptiness, equal collections by element and count, intersection, difference, partitions, a collection as list or array |
| `list` | `UniqueList`, `SortedList`, `SortedUniqueList`; `ListFactory` (array lists, linked lists, vectors, ranges, unique and sorted lists), `ListExtensions` (first and last elements, neighbours, splitting, removing, rearranging, sorting by a property, combinations, an index map, the changes between two lists), `VectorExtensions` |
| `set` | `InsertionOrderSet`, `IndexableSet`; `SetFactory` (hash, linked, tree and insertion order sets), `SetExtensions` (a set as array, set or sorted set) |
| `map` | `InsertionOrderMap`; `MapFactory` (hash, linked, tree, concurrent, lazy and counter maps), `MapExtensions` (keys by value, sorting by key or value, the largest key by a comparator, merging counter maps, swapping keys and values, maps as `Properties`) |
| `array` | `ArrayFactory` (arrays of objects and of each primitive type, empty, ranges, sub-arrays), `ArrayExtensions` (contains, indexes and neighbours, concatenating and joining, removing by index, prefix or suffix, intersection, chunks, lists, sets and two-dimensional arrays) |
| `properties` | `SortedProperties`, `IndexSortedProperties`; `PropertiesExtensions` (loading, storing and exporting, as XML, parameters in values, redundant values) |
| `iterator` | `ArrayIterator`, `EnumerationIterator` |
| `enumeration` | `SetEnumeration` |
| `modification` | `ModifiedState`, `ModifiedCollections`: what was added to and removed from a collection |

The [Javadoc](https://www.javadoc.io/doc/io.github.astrapi69/silly-collection) has the details.

## Quality

As of 29.1, measured by its release gate (Temurin 25.0.5+8, the JDK `.sdkmanrc` names; the tree of
`RELEASE-29.1`); each release takes the numbers of its own gate:

| | |
|---|---|
| tests | 1356 in 27 test classes, from 108 `@Test` and 125 `@ParameterizedTest` methods (#92); 3 are skipped: two `BeanTester` tests that wait for a factory method, and a benchmark |
| line and branch coverage (JaCoCo) | 1523 of 1523 lines, 637 of 640 branches; the build fails below 98% of the lines or 95% of the branches (`jacocoTestCoverageVerification` in `gradle/testing.gradle`) |
| mutation score (PIT) | 972 of 983 mutants detected, 99%; the run fails below 95% (`mutationThreshold` in `build.gradle`). The 11 survivors are equivalent: ten conditions whose edge case gives the same result either way, such as `0 < elements.length` before adding no elements, and the `return true` of `UniqueList.add`, which returns what `ArrayList.add` returns, always true |
| reproducible | two builds of the same commit give the same bytes for all five artifacts (`scripts/check-reproducible.sh`, #99) |

The CI build runs the tests and the coverage check on every pull request, and the workflow
"Mutation testing" runs PIT on GitHub, on every pull request and every push to `develop` and
`master`, and publishes the mutation score of `develop` as the badge above. A pull request into
`develop` is merged only when both are green.

## Verifying the release signatures

The releases on Maven Central are signed with the OpenPGP key

```text
5B2F A6B1 1E29 8BC0 9287  F423 D8C4 0351 8C49 CA75
```

Since 28.3 they are signed by its signing subkey

```text
6D67 F844 2A6F CC96 BD7C  1B5E 9FCF 7C97 10E2 BD8D
```

valid until 2028-10-09; up to 28.2 they were signed by the primary key itself. The key, with the
subkey, is on keys.openpgp.org. Fetch it once, then verify the signature next to each file:

```sh
gpg --keyserver hkps://keys.openpgp.org --recv-keys 5B2FA6B11E298BC09287F423D8C403518C49CA75
gpg --verify silly-collection-29.1.jar.asc silly-collection-29.1.jar
```

gpg answers `Good signature` and prints the `Primary key fingerprint`, and for a release signed by
the subkey the `Subkey fingerprint`; both have to be the ones above. The warning that the key "is not
certified with a trusted signature" only says that you have not marked it as trusted in your own
keyring.

## Releasing

What a release consists of, in the order it is run:

1. **No open issue** but those of a later milestone: every issue of the release's milestone is
   closed, and a finding on the way is fixed in the release or, if it changes what callers rely
   on, assigned to the next major release.
2. **One pull request** with the CHANGELOG entry and `projectVersion` without `-SNAPSHOT`
   (`docs: changelog for X.Y, and the version that carries it`), the full build green on it.
3. **The release gate** on that pull request's tree: the tests and `jacocoTestCoverageVerification`
   with its floor in one run, `--rerun-tasks --no-build-cache`, on JDK 25, the toolchain of the
   build, on the JDK `.sdkmanrc` names, with an empty local Maven repository
   (`-Dmaven.repo.local=<an empty directory>`; `gradle/repositories.gradle` reads `mavenLocal()` only
   with `-PuseMavenLocal`); javadoc prints no warning
   (the build runs it with `-Werror`), the class files are of Java 25 (major version 69), and the
   resolved dependencies hold no `-SNAPSHOT`; `scripts/check-reproducible.sh` builds the five
   artifacts twice and finds them identical. PIT does not run on a local machine: its numbers and
   its floor come from the run of "Mutation testing" on the pull request, and again from its run on
   the merge commit of the pull request, whose tree is the tag's tree. The numbers of the gate go
   into the CHANGELOG entry and into the Quality section above, in the same pull request, before it
   is merged. The GitHub release (step 6) links the run on the merge commit, with the proof that its
   tree is the tag's tree: `git rev-parse "<the run's commit>^{tree}" "RELEASE-X.Y^{tree}"` prints
   the same tree twice (`gh run view <run> --json headSha` names the run's commit).
4. **Tag the merge commit** with `./gradlew tagRelease` (`make tag-release CONFIRM=yes`), which names it
   `RELEASE-X.Y`, once its tree is the tree of the gate and the "Mutation testing" run on it has
   the numbers of the gate, and push the tag. `publish.yml` uploads to the Central Portal, where
   the maintainer releases it. A pushed tag is never moved or deleted; a failed run is fixed on the
   branch and run again.
5. **Verify on Central**: the jar, pom, `.module`, `-sources.jar` and `-javadoc.jar`, their sha1, and
   `gpg --verify` on the `.asc`; a local build of the tag, on the JDK `.sdkmanrc` names, gives the
   same bytes as Central for all five. A release is uploaded only by `publish.yml`, from its tag;
   `make tag-release` creates the tag and nothing else.
6. **GitHub release** from the CHANGELOG entry.
7. **`master` follows the release** by a merge of the tag: on `master`,
   `git merge --no-ff "RELEASE-X.Y^{commit}" -m "Merge tag RELEASE-X.Y into master"`. Before
   `master` is pushed, `git diff RELEASE-X.Y master` has to be empty; if it is not, nothing is
   pushed. Never a force push: `master` is always the latest release, nothing else.
8. **Open the next cycle**: `projectVersion` to the next `-SNAPSHOT`, by a pull request, as
   `develop` takes no direct push.

## Contributing

Pull requests are welcome: fork
[astrapi69/silly-collection](https://github.com/astrapi69/silly-collection/fork) and
[open a pull request](https://github.com/astrapi69/silly-collection/pull/new/develop) against
`develop`. [CONTRIBUTING.md](.github/CONTRIBUTING.md) has the details.

Please add tests for your change; the coverage and mutation floors above hold for it too. The
`Makefile` runs the usual tasks:

```shell
make build          # build: tests, coverage check, formatting check
make spotless-apply # format, and write the license headers
make pitest         # mutation testing on GitHub for the pushed branch, fails below the floor
```

PIT does not run on your machine: `make pitest` starts `mutation-testing.yml` on GitHub for the
current branch, which has to be pushed first, and prints how to follow the run; `make pitest-status`
shows the summary of the newest run of the branch: how many mutants were killed of how many, the
score, the floor and every mutant that survived. PIT's report of a run is its artifact
`pitest-report`: `gh run download <run> -n pitest-report -D <directory>`.

The repository's git hooks live in `.githooks`: `commit-msg` where present, and `pre-commit`, which
refuses a commit whose files Spotless would format. Activate them once per clone or worktree:

```bash
git config core.hooksPath .githooks
```

The versions follow the
[Simplified Semantic Versioning](https://github.com/lightblueseas/mvn-parent-projects/wiki/Simplified-Semantic-Versioning)
guidelines, `<major>.<minor>.<patch>`. Questions, bug reports and feature requests belong on the
[issues page](https://github.com/astrapi69/silly-collection/issues).

## How this project is made

silly-collection was written by its maintainer over many years, since 2015, before there were AI
tools; releases 28.3, 29.0 and 29.1 were implemented mostly by AI agents, under the maintainer's review
and with the maintainer's decision on every change of behaviour. How AI is used in the projects of
this profile: [AI_USAGE.md](https://github.com/astrapi69/astrapi69/blob/main/AI_USAGE.md).

## License

MIT - see [LICENSE](LICENSE).

## Support this project

If silly-collection is useful to you, a star on [GitHub](https://github.com/astrapi69/silly-collection)
helps others find it. Sharing it or reporting a bug helps just as much.

If you would like to contribute financially, the channels are on the repository's Sponsor button,
which reads [.github/FUNDING.yml](.github/FUNDING.yml): GitHub Sponsors, Liberapay, Ko-fi and PayPal.

## Similar projects

- [Guava](https://github.com/google/guava): Google's core libraries, with immutable and multi-valued
  collections
- [Apache Commons Collections](https://commons.apache.org/proper/commons-collections/), on which this
  library builds
- [Eclipse Collections](https://github.com/eclipse/eclipse-collections): collections with primitive
  types and a rich API
- [streamex](https://github.com/amaembo/streamex): enhancing Java 8 streams
- [glazedlists](https://github.com/glazedlists/glazedlists): open source list transformations for
  Java

## Credits

| | |
|---|---|
| [Maven Central](https://central.sonatype.com/artifact/io.github.astrapi69/silly-collection) | for hosting the artifacts of this open source project |
| [codecov.io](https://codecov.io) | for free code coverage reporting |
| [javadoc.io](https://www.javadoc.io) | for free javadoc hosting |
| [PIT](https://pitest.org/) | for mutation testing |

No animals were harmed in the making of this library.
