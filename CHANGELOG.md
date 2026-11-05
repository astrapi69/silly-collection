## Change log
----------------------

Version 29.1-SNAPSHOT
-------------

FIXED:

- `IndexableSet` declares its public no-argument constructor with a Javadoc comment, so javadoc prints no warning;
  29.0 had the compiler's default constructor, about which the javadoc of JDK 25 warns ("use of default constructor,
  which does not provide a comment"), and the release gate of 29.0 did not notice it, as it searched the German
  output for "warning". The binary interface stays the same, as the default constructor was public too (#96)

CHANGED:

- a javadoc warning fails the build: the `javadoc` task runs with `-Werror` and `failOnError = true`, and `check`
  depends on it, so `./gradlew build` and CI run javadoc and fail on a warning in every locale (#96).
  **Note:** a javadoc error fails the build now too; `failOnError` was `false`, also through the `javadoc` block
  inside the `javadocJar` task, which configured the project's `javadoc` task

Version 29.0
-------------

29.0 needs Java 25, and the list classes keep their own rules where the methods they inherit from `ArrayList` broke
them. Every breaking change has an entry in "MIGRATION FROM 28.x" below. 28.3 is the last release for Java 17; the
28.x line is frozen but for security fixes (the README's "Upgrading from 28.x").

BREAKING:

- Java 25 is the minimum, up from 17: the family builds, tests and releases on JDK 25 (astrapi69/mystic-crypt#235),
  so this release is 29.0 instead of 28.4 (#51). Gradle refuses a library built for Java 25 on the class path of a
  project below 25; such a project stays on 28.3 until it moves to JDK 25 itself. The projects of the family it
  concerns are listed on #51
- `SortedList` and `SortedUniqueList` with a comparator refuse explicit positioning, as `TreeSet` does: `set`,
  `sort`, `addFirst`, `addLast`, and the `set` and `add` of their list iterators throw an
  `UnsupportedOperationException`, and `replaceAll` writes only values that keep the order; so the algorithms of
  `Collections` that write through `set` throw at their first write and leave the list unchanged (#77, #78, #87)
- `subList` of `UniqueList`, `SortedList` and `SortedUniqueList`, and `reversed()` of the two sorted lists, read and
  remove only: `set`, `add`, `addAll`, `replaceAll` and `sort` through them throw an `UnsupportedOperationException`;
  `subList(from, to).clear()` still removes a range (#78, #87)
- `UniqueList.reversed()` refuses the two moves its view cannot make keep their contract: `add(size(), e)` and the
  `add` of its list iterator at or before the cursor, with an element the list holds (#77)

FIXED:

- `UniqueList.reversed()` returns a view of its own that adds at the index of the view: after `reversed().add(i, e)`,
  `reversed().get(i)` is `e`, for an element the list holds too, which moves there. The JDK's view of `List.reversed`
  calls `add(size() - i, e)` of the list, which assumes that the list grows, so that a held element ended one place
  too early: `[a, b, c].reversed().add(1, a)` gave the view `[a, c, b]`, not `[c, a, b]`. The view writes through the
  methods of `UniqueList` as before; its `add(e)` and `addAll` return false for elements the list holds, as
  `UniqueList.add` and `addAll` do, where they returned true, and `set` and `remove` of its list iterator after its
  `add` throw an `IllegalStateException`, as `ListIterator` says, where they acted on the added element (#77).
  **Note:** with an element the list holds, `add(size(), e)` of the view and the `add` of its list iterator at or
  before the cursor throw an `UnsupportedOperationException`: no index `size()` exists after a move, and the cursor
  cannot keep the contract of `ListIterator.add`; `addLast(e)` of the view moves an element to its end
- `set(int, E)` and the `set` of the list iterator of `SortedList` and `SortedUniqueList` with a comparator throw
  `UnsupportedOperationException`, whatever the arguments, as `TreeSet.addFirst` does ("explicit positioning is not
  supported"), and the list does not change; `replaceAll` replaces the elements in place where the values keep the
  order of the comparator, and otherwise throws `UnsupportedOperationException` and replaces none. They wrote any
  element at any index, so that the list left the order of its comparator and a `SortedUniqueList` could hold an
  element twice. `replaceAll` computes all values first; on a `SortedUniqueList` two equal values throw
  `IllegalArgumentException`. The `add` of their list iterator throws `UnsupportedOperationException`, with and
  without a comparator; it sorted the element in, wherever the cursor stood. Without a comparator `SortedList.set`
  replaces in place, as before (#78).
  **Note:** `Collections.swap`, `reverse`, `shuffle`, `rotate`, `fill`, `copy` and `replaceAll` throw
  `UnsupportedOperationException` on a sorted list with a comparator, at their first write, and the list does not
  change; they reordered it as an `ArrayList`. To replace an element, `remove(i)` it and `add(e)` the new one; to
  reorder, work on a copy, `new ArrayList<>(list)`

- `SortedUniqueList.set` without a comparator swaps an element the list holds at another index with the one at the
  index, as `UniqueList.set` does since 28.3 (#67), where it stored the element a second time; the `set` of its list
  iterator goes through it (#77, #78)
- `UniqueList.addFirst` and `addLast` put the element first or last, and move it there if the list holds it, as
  `LinkedHashSet` does; `addLast` left a held element where it was (#87)
- `setComparator` of `SortedList` and `SortedUniqueList` sorts with the new comparator first, and keeps the old
  comparator and order when that sort throws; it kept the new comparator on a list it had sorted in part (#87)
- `SortedList.addAll` of the list itself adds a copy of its elements, as `ArrayList` does, where it threw a
  `ConcurrentModificationException` (#77)

CHANGED:

- CI, the dependency submission and publishing build on JDK 25 (`gradle.yml`, `dependency-submission.yml`,
  `publish.yml`) (#51)
- the tests take silly-io 5.0, its release for Java 25, which `gradle/version-catalog-update.gradle` no longer pins
  to 3.6 (#37, #51); PIT 1.30.1, as silly-io on JDK 25
- the README as silly-io's (#85): what the library is for and when another one fits better, the requirements, the
  installation, upgrading, a quick start whose examples run in `ReadmeExamplesTest`, the packages, the quality, how
  to verify the release signatures; "Upgrading from 28.x" says that 28.3 is the last release for Java 17 and that the
  28.x line is frozen but for security fixes, on a branch `release/28.x` from `RELEASE-28.3`
- mutation testing and coverage, measured by the release gate of 29.0 on JDK 25: the tests kill 960 of the
  971 mutants of PIT 1.30.1, 98.9%; the 11 that survive are equivalent. JaCoCo covers 1502 of 1502 lines
  and 611 of 622 branches; 640 tests in 25 classes

MIGRATION FROM 28.x:

- **Java 25 (#51).** Before: 28.3 runs on Java 17. After: 29.0 holds Java 25 bytecode and does not load on an older
  JVM. What to change: build and run on JDK 25, or stay on 28.3.
- **`set` of a sorted list with a comparator, and the `set` of its list iterator (#78).** Before: the element was
  written at the index, so that the list could leave its order and a `SortedUniqueList` could hold an element twice.
  After: `UnsupportedOperationException`, the list unchanged. What to change: `remove(i)` the old element and
  `add(e)` the new one, which sorts it in.
- **`replaceAll` of a sorted list with a comparator (#78).** Before: every value was written in place. After: the
  values are written only if they keep the order of the comparator; otherwise `UnsupportedOperationException` and
  nothing replaced; a `SortedUniqueList` throws `IllegalArgumentException` for two equal values. What to change:
  compute the values, then `clear()` and `addAll(values)`.
- **`sort(c)` and `Collections.sort(list)` of a sorted list (#77).** Before: the list was sorted by any comparator,
  against its own. After: `UnsupportedOperationException`. What to change: `setComparator(c)` to change the order
  for good, or sort a copy, `new ArrayList<>(list)`.
- **`add` of the list iterator of a sorted list (#77, #78).** Before: the element was sorted in wherever the cursor
  stood, and the cursor moved as if it were there. After: `UnsupportedOperationException`. What to change: `add` to
  the list after the iteration.
- **`Collections.swap`, `reverse`, `shuffle`, `rotate`, `fill` and `copy` on a sorted list with a comparator (#78).**
  Before: they reordered the list as an `ArrayList` and broke its order. After: `UnsupportedOperationException` at
  their first write, the list unchanged. What to change: work on a copy, `new ArrayList<>(list)`.
- **`addFirst` and `addLast` of a sorted list, on Java 21 or later (#87).** Before: the element was sorted in, so
  it was neither first nor last. After: `UnsupportedOperationException`, as `TreeSet.addFirst`. What to change:
  `add(e)`, which sorts the element in.
- **`reversed()` of a sorted list (#87).** Before: the JDK's view, whose writes sorted the element in. After: a view
  that reads and removes; its writes throw `UnsupportedOperationException`. What to change: write through the list.
- **`subList` of `UniqueList`, `SortedList` and `SortedUniqueList` (#78).** Before: `ArrayList`'s view, whose writes
  could add a duplicate, break the order or corrupt the view. After: the view reads and removes; `set`, `add`,
  `addAll`, `replaceAll` and `sort` throw `UnsupportedOperationException`. What to change: write through the list,
  for instance `list.set(from + i, e)` on a `UniqueList`, or work on a copy, `new ArrayList<>(list.subList(a, b))`.
- **`UniqueList.reversed()`, on Java 21 or later (#77).** Before: `add(i, e)` with an element the list holds and
  `0 < i < size()` put it one place too early in the view. After: it lands at `i`; `add(size(), e)` with a held
  element and the `add` of the view's list iterator of a held element at or before the cursor throw
  `UnsupportedOperationException`. What to change: pass `i`, not `i + 1`; use `addLast(e)` of the view to move an
  element to its end.
- **`UniqueList.addLast` of a held element, on Java 21 or later (#87).** Before: the element stayed where it was.
  After: it moves to the end. What to change: `add(e)`, if a held element is to stay where it is.
- **`SortedUniqueList.set` without a comparator (#77).** Before: an element held at another index was stored a
  second time. After: the two swap. What to change: nothing, unless the code relied on the duplicate.


Version 28.3
-------------

Every change of this release is binary compatible with 28.2: no public or protected method is removed or changes
its signature. The fixes make methods do what their contract says, where they did something else, so a caller that
relied on the old behavior sees a difference. Those that callers are likely to notice are marked **Note** below.

FIXED:

- `CollectionExtensions.isEqualCollection` compares the elements and how often each occurs, whatever their order,
  through `CollectionUtils.isEqualCollection` of commons-collections4. It returned true for any two collections of
  the same size without a common element, `[1, 2]` and `[3, 4]`, and did not compare how often an element occurs,
  `[1, 1, 2]` and `[1, 2, 2]`. The elements compare by `equals`, so arrays by identity (#42).
  **Note:** the tests of a caller that relied on the old result can turn red with 28.3. That shows real differences
  between the collections they compare, which the old result hid; it is not a fault of the update. Of the family,
  nine assertions of json-extensions, two of gson-extensions and one of csv-worker rely on it, listed on #42
  (astrapi69/json-extensions#29, astrapi69/gson-extensions#27)
- `UniqueList.addAll(Collection)` and `addAll(int, Collection)` return whether the list changed; they returned true
  for every non-empty collection. `addAll(int, Collection)` and `add(int, E)` check the index as `ArrayList` does,
  before the list changes. `add(int, E)` of an element the list holds moves it to the index; it removed the element
  first, so that a valid index could be out of range by then, and the exception left the element removed (#39)
- `SortedList` and `SortedUniqueList` take an element that their comparator holds equal to another one but `equals`
  does not, `"A"` beside `"a"` with `String.CASE_INSENSITIVE_ORDER`, where they threw an `IndexOutOfBoundsException`
  (`ListExtensions.getIndexToInsert`). The Javadoc of their `add(int, E)` and `addAll(int, Collection)` says that the
  index is ignored, as the class Javadoc says of every add method (#40)
- `InsertionOrderSet.add`, and through it `addAll` and the same methods of `IndexableSet`, return false for an
  element the set holds, as `Set.add` says; the element still moves to the end. `IndexableSet.getIndex` takes a null
  element, where it threw a `NullPointerException` (#41)
- `ListExtensions.removeLastValues(list, n)` removes the last n values, where it removed the first ones, and
  `sortByProperty(list, function, true)` sorts ascending and `false` descending, where the flag worked the wrong way
  round (#43)
- `MapExtensions.getKeyFromValue` returns null when no entry holds the value, where it returned the last key. It and
  `getKeysFromValue` compare with `Objects.equals`, and no longer throw for a null value in the map.
  `MapFactory.newTreeMap` with a comparator takes keys that are not `Comparable` (#44)
- `SortedProperties.keySet()` is a view backed by the properties, as `Map.keySet` specifies, in the order of the
  comparator, where it was a copy: a removal through it removes the entry, `add` throws
  `UnsupportedOperationException`. `propertyNames()` lists the keys of the defaults too, as
  `Properties.propertyNames` specifies. `SortedProperties` and `IndexSortedProperties` serialize (#46).
  **Note:** `propertyNames()` throws a `ClassCastException` for a key that is no `String`, as
  `Properties.propertyNames` specifies; it listed such keys before
- the index of `IndexSortedProperties` holds exactly the keys of the entries of the object itself, in the order of
  the comparator: a key put twice is in it once, a removed key leaves nothing behind, `putIfAbsent`, `compute`,
  `computeIfAbsent`, `computeIfPresent`, `merge` and removals through `keySet()`, `entrySet()` and `values()` reach
  it, a clone has an index of its own, and `get(int)`, `getProperty(int)` and `remove(int)` return null for any
  index out of range, a negative one included (#45).
  **Note:** the index no longer holds the keys of the defaults. A caller that read a default through `get(int)` or
  `getProperty(int)` no longer finds it there, and the indexes of the own keys can be different;
  `getProperty(String)` still finds the defaults
- the Javadoc says what the code does, where it contradicted it: `ArrayExtensions.concatenate`,
  `remove`, `removeAll`, `removeLast` and `hasPrevious`, `CollectionExtensions.toList`, `toArray` and `isNotEmpty`,
  `ListExtensions.toArray`, `toList(Enumeration)` and `hasPrevious`, `ArrayFactory.newRangeArray` and
  `MapExtensions.getKeysFromValue`. No method returns or throws anything else; only the message of `newRangeArray`
  changes, to "Parameter end must not be less than parameter start." (#47)
- the Javadoc of `add(E)` and `addAll(Collection)` of `SortedList` and `SortedUniqueList`, and of
  `UniqueList.add(E)`, says where the element goes and what they return, where it inherited "Appends ... to the end"
  from `ArrayList`: at the place of the comparator, in front of one of the elements the comparator holds equal; false
  for an element a unique list holds; `SortedList.add` always true (#66)
- `ArrayFactory.newArray(T...)` and `ListExtensions.toArray(T...)` return a new array, also when no elements are
  given, where they returned the given array itself; `newArray(T[], T...)` returns a copy where `concatenate` hands
  back one of the given arrays. The factory methods of the primitive types, `newBooleanArray` to `newShortArray`,
  return a new array too (#75). Null still gives null (#62).
  **Note:** a caller that wrote to the result to change its own array, or compared it with `==`, sees a difference;
  no caller in the family does
- `ArrayExtensions.remove(array, indexes)` no longer sorts the caller's indexes array, and an index given twice
  removes one element, where it removed two, `remove({0, 1, 2, 3}, 1, 1)` gave `{0, 3}`, or threw for the last
  index. An index out of range names the length of the given array in its message (#76). `removeLast` of an empty
  array throws an `IndexOutOfBoundsException`, as `removeFirst` does, where it threw a `NegativeArraySizeException`
  (#63)
- `ArrayExtensions.removeFromStart` and `removeFromEnd` remove the prefix or suffix only when the array starts or
  ends with it, compared with `Objects.equals`, and otherwise return a copy of the array, as
  `StringUtils.removeStart` and `removeEnd` of Apache Commons Lang do for strings. They removed as many elements as
  the prefix or suffix had, whatever they were, and threw a `NegativeArraySizeException` for a longer one. The
  result is always a new array; `removeFromEnd` checks its arguments as `removeFromStart` does, so null throws an
  `IllegalArgumentException` (#76).
  **Note:** a caller that removed a prefix or suffix by its length alone gets the array unchanged; no caller in the
  family does
- `CollectionExtensions.toArray(Collection)`, `ListExtensions.toArray(List)` and `SetExtensions.toArray(Set)` take as
  component type the most specific common superclass of the non-null elements, `Object` if all are null, not the
  class of the first element: a null first element no longer throws a `NullPointerException`, and elements of
  different classes no longer throw an `ArrayStoreException`, `[1, 2.5]` gives a `Number[]`.
  `ArrayExtensions.removeFromStart` takes the component type of the given array (#64).
  **Note:** a caller whose `T` is an interface, or whose collection holds only nulls, gets an `Object[]` and a
  `ClassCastException` where it assigns it to a `T[]`. Of the family, `PropertiesKeyExtensions.concatenate` of
  resourcebundle-core gives `"null.foo"` for a list whose first element is null, where it threw a
  `NullPointerException`
- `ListFactory.newUniqueList` returns a `UniqueList`, as its Javadoc says, where it built a `SortedUniqueList`
  without comparator: the order of the elements is the same, but `add(int, E)` inserts at the index instead of
  appending; the sorted one is `newSortedUniqueList` (#65)
- `UniqueList.set(int, E)` and the `set` of its list iterator swap an element the list holds at another index with
  the one at the index, where they stored it a second time, so that `Collections.swap`, `reverse`, `shuffle` and
  `rotate` keep working. `replaceAll` throws an `IllegalArgumentException` for an operator that gives two equal
  values and leaves the list unchanged. The `add` of its list iterator inserts before the element `next()` returns,
  also for an element held before the cursor, where `next()` skipped an element (#67).
  **Note:** `replaceAll` with an operator that maps two elements to equal values throws now, and `Collections.fill`
  and `copy` give a permutation without duplicates instead of the repeated values

CHANGED:

- publishing as in the rest of the family (#31): the Central Portal through the nmcp settings plugin, a release
  waiting for the maintainer (USER_MANAGED), the signing subkey through GPG_KEY_ID, a refused upload when only half
  of the key is set, and uploads only from CI, a release only from its RELEASE-* tag
- mutation testing (#36): the tests kill 781 of the 794 mutants of PIT 1.30.0 with its default mutators, 98.4%, up
  from 625 of 721, 87%, and the CI build fails below 95% (`mutationThreshold` in `build.gradle`, 85% before; PIT
  rounds the score half up to a whole percent before it compares). The 13 that survive are equivalent. Four more
  survived when the floor was set, at 703 of 721; they pointed at the bugs and the Javadoc of #41, #43 and #47, and
  their fixes kill them. The README's mutation score badge shows the score of `develop`, which CI publishes on the
  `gh-pages` branch from each push to it, also when the score fails the floor; a push to `master` publishes
  master's. `make pitest` runs PIT
- a coverage floor (#38): the tests cover all 1247 lines and 546 of the 558 branches (JaCoCo), 518 tests in 24
  classes, and the build fails below 98% of the lines or 95% of the branches (`jacocoTestCoverageVerification` in
  `gradle/testing.gradle`), each measure minus about two points, rounded down, as for the mutation score. The README
  has a Quality section with the numbers of the release gate, and a Releasing section with its steps (#38)
- the tests that PIT found weak check what `add` and `addAll` of the list classes return, the boundaries at their
  exact value, the content and order of results that were checked by their size, the types the factories document,
  the order of the sorted maps, and the index of `IndexSortedProperties` after `put`, `remove` and `clear`; two
  constructor tests whose loops never ran check what they are named for (#36). The bugs found on the way are
  #39 to #47
- the tests of `PropertiesExtensionsTest` write their files into a temporary directory, no longer into the working
  directory, where a run of PIT left five of them behind (#36)
- the test runtime class path no longer holds this project's own jar: file-worker depends on an older
  silly-collection, which Gradle resolved to this project, so `test` needed `mustRunAfter(jar)` and
  `./gradlew build pitest` failed Gradle's validation (#36). file-worker is no test dependency any more, and with
  it the exclude of #36 is gone: `testRuntimeClasspath` holds no silly-collection. `MigrateToTomlVersions`, its only
  user, a test class whose only test printed two paths and asserted nothing, is removed; silly-io 3.6 is the only
  library of the family in the tests (#37)
- dependencies raised with versionCatalogUpdate: commons-collections4 4.6.0, a release instead of the milestone
  4.5.0-M2 on the consumers' runtime class path (#35); for the tests junit-jupiter 6.1.3, mockito-core 5.24.0 and
  test-object 9.1; the Gradle plugins spotless 8.11.0, versions 0.67.0 and version-catalog-update 1.1.2. silly-io
  goes to 3.6, its last release for Java 17, not to 5.0, which Gradle refuses for this project's Java 17;
  `gradle/version-catalog-update.gradle` pins it (#37)
- the Gradle wrapper at 9.8.2, the current release; its jar is the one Gradle publishes for 9.8.2
- the test dependency random-beans 3.9.0 is gone, which no test used, and with it jackson-databind and jackson-core
  2.9.8 and classgraph 4.6.18: the source of 65 of the 67 Dependabot alerts, all 12 critical ones among them

KNOWN ISSUES, for 29.0:

- `set`, `replaceAll` and the list iterator of `SortedList` and `SortedUniqueList` can break the order or the
  uniqueness, and `SortedList.addAll` of the list itself throws a `ConcurrentModificationException` (#77)
- changes through a `subList` of `UniqueList`, `SortedList` and `SortedUniqueList` can break the list or the view
  (#78)
- on a JDK 21 or later runtime, `UniqueList.reversed().add(i, e)` with 0 < i < size and an element the list holds
  puts it one place too early in the view, as the JDK's reversed view expects the list to grow; the list stays
  unique. The methods of `SequencedCollection` behave as follows there: `addFirst` and `addLast` of `SortedList` and
  `SortedUniqueList` sort the element in, and `UniqueList.addLast` leaves a held element where it is (#87)
- `setComparator` of `SortedList` and `SortedUniqueList` keeps the new comparator when the sort with it throws, on
  any runtime (#87)

Version 28.2
-------------

ADDED:

- new method in class ArrayExtensions that joins two arrays of the same type into a single array
- new method in class CollectionExtensions that creates a new array from the given Collection object

CHANGED:

- update gradle to new version 8.11-rc-1
- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new beta version 7.0.0.BETA3
- update of dependency jobj-core in new version 9.1
- update of test dependency file-worker to new version 18.0
- update of test dependency mockito-core to new version 5.14.2

Version 28.1
-------------

ADDED:

- new method in class PropertiesExtensions that exports a Properties object to the given file object

CHANGED:

- update gradle to new version 8.10.2
- update of gradle-plugin dependency 'org.ajoberstar.grgit:grgit-gradle' in version 5.3.0
- update of dependency silly-bean in to new major version 3.0
- update of dependency comparator-extensions to new major version 2
- update of test dependency test-object to new major version 9
- update of test dependency junit-jupiter to new patch version 5.11.2
- dependencies are structured now over bundles

Version 28
-------------

ADDED:

- new factory method in class ListFactory for generate SortedList objects
- new factory method in class ListFactory for generate SortedUniqueList objects
- new factory method in class ListFactory for generate UniqueList objects
- new factory method in class ListFactory for generate Vector objects

CHANGED:

- rename of module to new name 'io.github.astrapisixtynine.silly.collection'
- major version to 28.*
- update of dependency jobj-core in new major version 9

Version 27.2
-------------

ADDED:

- new test dependency junit-jupiter in version 5.11.0
- new class SortedUniqueList that is sorted and holds unique elements
- new method in class ArrayExtensions that creates a Set object from a given array

CHANGED:

- update gradle to new version 8.10
- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new beta version 7.0.0.BETA2
- method toArray from SetExtensions returns now an empty array
- rename of class SortedArrayList to SortedList
- rename of class UniqueArrayList to UniqueList
- removed test dependency testng
- update of dependency commons-collections4 to new version 4.5.0-M2
- update of dependency jobj-core in new version 8.4
- update of test dependency file-worker to new version 17.4
- update of test dependency silly-io to new version 3.1
- update of test dependency mockito-core to new version 5.13.0

Version 27.1
-------------

ADDED:

- new method in class ListExtensions subList that returns a sub array from the specified start and end point
- new method in class ArrayExtensions subArray that returns a sub array from the specified start and end point
- new method in class ArrayExtensions getFirstElement that returns the first element as Optional
- new method in class ArrayExtensions getLastElement that returns the last element as Optional
- new method in class ArrayExtensions getNextElement that returns the next element as Optional
- new method in class ArrayExtensions getPreviousElement that returns the previous element as Optional
- new method in class ArrayExtensions hasNext
- new method in class ArrayExtensions hasPrevious
- new method in class MapFactory that creates a new hashmap with two given lists with keys and values
- new package-info.java files to packages
- new libs.versions.toml file for new automatic catalog versions update
- replaced obsolete package.html with package-info.java files

CHANGED:

- update gradle to new version 8.8
- update of com.github.ben-manes.versions.gradle.plugin to new version 0.51.0
- update of gradle-plugin dependency 'org.ajoberstar.grgit:grgit-gradle' in version 5.2.2
- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new beta version 7.0.0.BETA1

Version 27
-------------

CHANGED:

- update to jdk version 17
- update gradle to new version 8.3
- update of com.github.ben-manes.versions.gradle.plugin to new version 0.47.0
- update of gradle-plugin dependency 'org.ajoberstar.grgit:grgit-gradle' in version 5.2.0
- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new minor
  version 6.20.0
- new extension method in class ListExtensions that determines the index to insert from a given list with a given
  element and searches with the 'binary search' algorithm
- update of dependency jobj-core in new version 7.1
- update of test dependency org.openjdk.jmh:jmh-core to new version 1.36
- update of test dependency testng to new version 7.8.0

Version 21
-------------

ADDED:

- new list class SortedArrayList that sorts the values with the given comparator
- new method relocate in extension class ListExtensions that relocated a given element to the new given index

CHANGED:

- update gradle to new version 8.0
- update of gradle-plugin com.github.ben-manes.versions.gradle.plugin to new version 0.45.0
- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new version
  6.15.0
- update of test dependency 'com.github.meanbeanlib:meanbean' to new version 3.0.0-M9
- replaced argument parameters of method getIndexToInsert for be extension method conform

Version 20.4
-------------

ADDED:

- new list class UniqueArrayList that holds unique values

CHANGED:

- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new version
  6.13.0

Version 20.3
-------------

ADDED:

- new methods for get first and last from a list

CHANGED:

- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new version
  6.12.1
- update of test dependency testng to new version 7.7.1

Version 20.2
-------------

ADDED:

- new file importorder.properties that defines the order of the imports while formatting with spotless
- new methods for check in list if an element is before or after a given element
- new method that determines the index to insert to a list the is constructed and needs a specified order

CHANGED:

- update gradle to new version 7.6
- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new version
  6.12.0
- update of gradle-plugin com.github.ben-manes.versions.gradle.plugin to new version 0.44.0
- update of dependency comparator-extensions in new version 1.5
- update of test dependency test-object to new version 7.2
- update of test dependency org.openjdk.jmh:jmh-core to new version 1.36
- update of test dependency testng to new version 7.7.0
- rename of module name from silly.collection.main to silly.collection
- remove of class OptionalListExtensions and move all method to class ListExtensions
- javadoc optimized

Version 20.1
-------------

ADDED:

- new method in SetFactory with collection to exclude
- module-info.java

CHANGED:

- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new version
  6.11.0
- update of dependency comparator-extensions in new version 1.4
- update of dependency jobj-core in new version 7
- update of test dependency test-object to new version 7.1
- remove of dependency jobj-compare

Version 20
-------------

CHANGED:

- rename of module and repository from plural-form silly-collections to singular-form
  silly-collection

Version 19
-------------

ADDED:

- new dependency comparator-extensions in new minor version 1.2
- new method that converts a generic map to a two-dimensional array where key an value are from the
  same type
- new method that converts a string(as key) and an object(as value) map to a two-dimensional array
- new method that converts a a two-dimensional array to a string(as key) and an object(as value) map

CHANGED:

- update to jdk version 11
- update gradle to new version 7.5.1
- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new version
  6.9.1
- update of dependency jobj-compare in new version 11.1
- update of dependency jobj-core in new version 6
- update of test dependency testng to new version 7.6.1
- update of dependency comparator-extensions to new minor version 1.1
- rename all packages that had plural form to singular form

Version 18.2
-------------

ADDED:

- new method for sort a map with a given comparator
- new method for get the maximum key from a given map with a given comparator that defines what is
  the maximum key
- extracted sections from build.gradle to its own gradle files for clearness

CHANGED:

- update of gradle-plugin dependency 'com.diffplug.spotless:spotless-plugin-gradle' to new minor
  version 6.5.0

Version 18.1
-------------

ADDED:

- new method for concatenate two arrays
- improve gradle build performance by adding new gradle parameters for caching, parallel, configure
  on demand and file watch
- new method for clone a list
- new methods for get last index of an array
- new gradle-plugin dependency of 'org.ajoberstar.grgit:grgit-gradle' in version 4.4.1 for create
  git release tags
- new gradle-plugin dependency of 'com.diffplug.spotless:spotless-plugin-gradle' in version 6.4.1
  for format the source files
- new method in class CollectionExtensions that converts a collection to a list

CHANGED:

- update gradle to new version 7.4.2
- update of dependency jobj-core in new version 5.3
- update of test dependency testng to new version 7.5
- update of test dependency test-objects to new version 6
- update of test dependency org.openjdk.jmh:jmh-core to new version 1.35
- update of com.github.ben-manes.versions.gradle.plugin to new version 0.42.0

Version 18
-------------

ADDED:

- new method for generate an index map from a given list
- new method for swap a map keys and values
- new factory method for create a Character object list from a given char array

  CHANGED:

- update gradle to new version 7.2
- changed all dependencies from groupid de.alpharogroup to new groupid io.github.astrapi69
- update gradle-plugin dependency of gradle.plugin.com.hierynomus.gradle.plugins:
  license-gradle-plugin to new version 0.16.1
- update of test dependency test-objects to new version 5.5
- update of test dependency org.openjdk.jmh:jmh-core to new version 1.33
- update of dependency jobj-core in new version 3.9
- update of dependency jobj-compare in new version 3.9

Version 8.7
-------------

CHANGED:

- update of dependency silly-beans in new version 1.2
- update of test dependency vintage-time to new version 5.3
- update of test dependency test-objects to new version 5.4
- removed unused test dependency jobj-contract-verifier
- removed unused test dependency jobj-clone
- update of dependency jobj-compare in new version 3.8

Version 8.6
-------------

ADDED:

- new factory method for create a number map with a collection, and a flag for start from one or
  zero
- new factory method for create a byte array from an int array in factory class ArrayFactory
- new factory method for create a number map with a map, a collection, and a flag for start from one
  or zero

CHANGED:

- update of gradle to new version 6.9
- update of test dependency org.openjdk.jmh:jmh-core to new version 1.32
- update of test dependency testng to new version 7.4.0
- update of com.github.ben-manes.versions.gradle.plugin to new version 0.39.0

Version 8.5
-------------

ADDED:

- new method that creates a stream from a given array

CHANGED:

- update of gradle to new version 6.8.1
- update of test dependency org.openjdk.jmh:jmh-core to new version 1.27
- update of com.github.ben-manes.versions.gradle.plugin to new version 0.36.0
- changed to new package io.github.astrapi69

Version 8.4
-------------

ADDED:

- new factory method for create a number map for count integer values with an existing counter map

CHANGED:

- update of gradle to new version 6.7
- update of dependency jobj-compare in new version 3.7
- order of build tasks changed in the build.gradle file

Version 8.3
-------------

ADDED:

- new area 'gradle-plugins versions' for hold the versions of the gradle plugins
- new factory method for create a number map for count integer values

CHANGED:

- update of gradle to new version 6.6.1
- update of com.github.ben-manes.versions.gradle.plugin to new version 0.33.0
- extracted project properties to gradle.properties
- extracted project gradle plugin versions to buildscript.ext area in gradle.properties
- extended jar task for build manifest file
- update of dependency jobj-core in new version 3.6
- update of test dependency testng to new version 7.3.0
- update of test dependency org.openjdk.jmh:jmh-core to new version 1.26

Version 8.2
-------------

ADDED:

- new extension method created that provides the containsAtLeastOne with varargs objects in a
  Collection
- new extension method created that provides the containsAtLeastOne with varargs objects in an Array

CHANGED:

- update of gradle to new version 6.4.1
- update of dependency jobj-compare in new version 3.6.1
- update of test dependency testng to new version 7.2.0

Version 8.1
-------------

ADDED:

- new method created that can summarize a given counter Maps with a given collection of keys with
  the possibility of full merge
- new factory method created for create a counter map with a already initialized counter Map

CHANGED:

- update of test dependency jobj-clone to new version 3.3
- update of test dependency jobj-contract-verifier to new version 3.3
- update of test dependency test-objects to new version 5.3
- update of test dependency vintage-time to new version 5.2
- update of gradle-plugin com.github.ben-manes.versions to new version 0.28.0

Version 8
-------------

CHANGED:

- update of dependency silly-beans in new version 1.1
- update of dependency jobj-compare in new version 3.3
- removed unused test dependency junit and mockito-core

Version 5.9
-------------

ADDED:

- new method created that can summarize two counter Maps and return a new Map that contains the sum
  of the counts
- new major release branch for jdk 11 created
- new constant created for a regular expression pattern that can find comments in properties file
- new dependency jobj-core in version 3.5 added

CHANGED:

- method from PropertiesExtensions getInteger returns now an empty Optional if no integer is found
- update of test dependency testng version to 7.1.1
- update of test dependency junit version to 4.13
- update of test dependency mockito-core version to 3.2.4
- update of test benchmark dependency jmh version to 1.23
- removed lombok dependency

Version 5.8
-------------

ADDED:

- new factory methods for maps created that can take now key value pairs
- new idea run configurations for gradle builds created

CHANGED:

- update of gradle version 5.6.4
- removed idea run configurations for maven

Version 5.7.1
-------------

CHANGED:

- downgraded gradle to version 5.5.1

Version 5.7
-------------

ADDED:

- gradle as new build system

CHANGED:

- changed project nature from maven to gradle nature
- removed maven related files
- code coverage increased to 100%

Version 5.6
-------------

ADDED:

- new method factory for for counting elements from the given collection with a map

CHANGED:

- update of jobj-compare version to 3.2
- replaced obsolete jobject-clone with the new jobj-clone test dependency

Version 5.5.1
-------------

ADDED:

- new method created for sort a map by value with a comparator

Version 5.5
-------------

ADDED:

- new methods created for sort a map by value

Version 5.4.1
-------------

ADDED:

- new factory method for create a supplier for a tree set from a comparator

CHANGED:

- update of jmh version to 1.22
- removed deprecated enum class CompareOrder

Version 5.4
-------------

ADDED:

- new method for convert a not empty existing list to array
- new method for convert a not empty existing set to array

Version 5.3
-------------

ADDED:

- new factory method for create an array from a given class type and a given length
- new factory method for create an empty array from array with the length of the given array
- new factory method for create an empty array from array with a given length
- new extension method created that removes a given prefix array from a given array
- new extension method created that removes a given suffix array from a given array

CHANGED:

- update of parent version to 5.3
- update of jobject-clone version to 3.1.2
- update of jobj-contract-verifier version to 3.2
- update of test-objects version to 5.2
- update of commons-collections4 version to 4.4

Version 5.2.1
-------------

ADDED:

- new method created for make an intersection of arrays
- new factory method for create a sub array from a given array
- new jobj-contract-verifier dependency in test scope added for unit test of beans
- new maven launch script created for execution of clean package

CHANGED:

- update of jobject-clone version to 3.1.1
- tagged enum CompareOrder as deprecated and moved to the appropriate project
- update of jobj-compare dependency version to 3.1

Version 5.2
-------------

ADDED:

- new method created for get element from index in the class IndexableSet
- new class OptionalListExtensions created
- new method hasNext in class ListExtensions created
- new benchmark jmh-core and jmh-generator-annprocess dependencies in version 1.21 added
- new BenchmarkRunner created that starts the benchmarking process
- new benchmark class for IndexableSet created
- new methods for getNext and getPrevious created in class OptionalListExtensions
- new method for an array to remove all element from a given array

CHANGED:

- update of parent version to 4.8

Version 5.1
-------------

ADDED:

- new method created for get all combinations of a given list of values
- new method created partition created in java 8 style
- new method created for remove the first element in a given array
- new method created for remove the last element in a given array
- new method created for remove an element over an index in a given array

CHANGED:

- update of parent version to 4.5
- update of test-objects dependency version to 5.0.1

Version 5
-------------

CHANGED:

- update of parent version to 4.4
- update of jobject-extensions version to 2.5
- update of vintage-time version to 5.1
- update of test-objects dependency version to 5
- removed unneeded dependency jobject-core

Version 4.35
-------------

ADDED:

- new methods created that returns an Optional instead of null values
- new factory methods created for iterables and iterators

CHANGED:

- update of parent version to 4.2
- removed all classes that still used pair classes

Version 4.34.1
-------------

CHANGED:

- tagged classes as deprecated that use still pair classes

Version 4.34
-------------

ADDED:

- new factory methods for LinkedList objects created
- new IndexableSet class created that can get the index of an element in the set
- new method for rearrange an element of a List to a specified index created
- new method that converts an array to a Set

CHANGED:

- update of parent version to 4.1
- moved pair bean classes to new model-core project and tagged as deprecated

Version 4.33
-------------

CHANGED:

- removed deprecated factory methods from the factory classes

Version 4.32
-------------

CHANGED:

- update of parent version to 4
- update of vintage-time version to 4.12
- update of jobject-extensions version to 1.12
- update of commons-collections4 version to 4.2
- removed experimental lombok annotation ExtensionMethod from unit test classes

Version 4.31
-------------

ADDED:

- new factory methods with initial capacity for create set objects created
- new factory methods for primitive array types created

Version 4.30.1
-------------

ADDED:

- new factory methods for create map objects with a given map
- new method isEqualListOfArrays in ListExtensions created

CHANGED:

- javadoc extended and improved

Version 4.30
-------------

ADDED:

- new factory methods for LinkedHashSet and InsertionOrderSet created
- new hashcode method for collections that contains array objects
- new isEqualCollection method for collections created
- new method for copy an array created

CHANGED:

- removed deprecated factory methods from the extension classes
- update of test-objects dependency version from 4.26 to 4.28
- DateComparatorTest extends now from BaseComparatorTestCase

- update of gradle to new version 6.4
- update of test dependency testng version to 7.2.0

Version 4.29
-------------

ADDED:

- new section 'Semantic Versioning' in README.md file
- new ArrayFactory class created and moved all factory method to it
- new ListFactory class created and moved all factory method to it
- new SetFactory class created and moved all factory method to it

CHANGED:

- update of parent version to 3.11
- removed unneeded .0 at the end of version
- update of test-objects dependency version from 4.24.0 to 4.26
- update of jobject-extensions version to 1.11

Version 4.28.0
-------------

ADDED:

- new generic Enumeration class created that associates a Set and an Enumeration

CHANGED:

- update of parent version to 3.10.0

Version 4.27.0
-------------

CHANGED:

- removed all depracated methods
- optimization of readability

Version 4.26.0
-------------

ADDED:

- new factory methods for LinkedHashMap created
- new factory class for maps created and moved all factory methods from MapExtensions to it

CHANGED:

- update of parent version to 3.9.0
- update of test-objects dependency version from 4.23.0 to 4.24.0
- update of jobject-extensions dependency version from 1.8.0 to 1.10.0
- moved method newRangeArray from ListExtensions to ArrayExtensions
- tagged methods with int parameter for initialCapacity in ListFactory and SetFactory as deprecated

Version 4.25.0
-------------

ADDED:

- created new method for get an Integer from properties object with a given properties key
- new factory methods for create new Treeset as SortedSet objects
- new factory methods for create new TreeMap as SortedMap objects
- new factory method for create new InsertionOrderMap
- new methods created for export properties objects to xml and properties format

CHANGED:

- javadoc extended and improved
- moved method isEmpty and isNotEmpty to class CollectionExtensions
- update of parent version
- unit tests extended for new created methods

Version 4.24.0
-------------

ADDED:

- new extension class CollectionExtensions for collections
- new pair class with key and set

CHANGED:

- removed deprecated classes and methods
- update of documentation of README.md
- unit tests extended for improve code coverage to 100%
- several bugfixes

Version 4.23.0
-------------

ADDED:

- this changelog file
- created PULL_REQUEST_TEMPLATE.md file
- created CODE_OF_CONDUCT.md file
- created CONTRIBUTING.md file
- provide package.html for the javadoc of packages

CHANGED:

- update of parent version
- update of documentation of README.md
- unit tests extended for improve code coverage
