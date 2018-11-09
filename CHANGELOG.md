## Change log
----------------------

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


