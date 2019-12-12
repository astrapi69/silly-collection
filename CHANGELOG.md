## Change log
----------------------

Version 5.7-SNAPSHOT
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


