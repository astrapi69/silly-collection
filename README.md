# Overview

<div align="center">

[![Build Status](https://travis-ci.org/lightblueseas/silly-collections.svg?branch=master)](https://travis-ci.org/lightblueseas/silly-collections) 
[![Coverage Status](https://coveralls.io/repos/github/lightblueseas/silly-collections/badge.svg?branch=master)](https://coveralls.io/github/lightblueseas/silly-collections?branch=master) 
[![Open Issues](https://img.shields.io/github/issues/lightblueseas/silly-collections.svg?style=flat)](https://github.com/lightblueseas/silly-collections/issues) 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.alpharogroup/silly-collections/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.alpharogroup/silly-collections)
[![Javadocs](http://www.javadoc.io/badge/de.alpharogroup/silly-collections.svg)](http://www.javadoc.io/doc/de.alpharogroup/silly-collections)
[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](http://opensource.org/licenses/MIT)
[![Donate](https://img.shields.io/badge/donate-❤-ff2244.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=GVBTWLRAZ7HB8)

</div>

Utility library for collections, comparators and iterator classes.

If you like this project put a ⭐ and donate

# Donations

If you like this library, please consider a donation through paypal: <a href="https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=MJ7V43GU2H386" target="_blank">
<img src="https://www.paypalobjects.com/en_US/GB/i/btn/btn_donateCC_LG.gif" alt="PayPal this" title="PayPal – The safer, easier way to pay online!" border="0" />
</a>

or over bitcoin or bitcoin-cash with:

36JxRRDfRazLNqUV6NsywCw1q7TK38ukpC

or over ether with:

0x588Aa02De98B1Ef70afeDC3ec5290130a3E5e273

or over flattr:
  
<a href="https://flattr.com/submit/auto?fid=r7vp62&url=https%3A%2F%2Fgithub.com%2Flightblueseas%2Fsilly-collections" target="_blank">
<img src="http://api.flattr.com/button/flattr-badge-large.png" alt="Flattr this" title="Flattr this" border="0" />
</a>

## License

The source code comes under the liberal MIT License, making silly-collections great for all types of applications.

## Note

No animals were harmed in the making of this library.

## Maven dependency

Maven dependency is now on sonatype.
Check out [sonatype repository](https://oss.sonatype.org/index.html#nexus-search;gav~de.alpharogroup~silly-collections~~~) for latest snapshots and releases.

Add the following maven dependency to your project `pom.xml` if you want to import the core functionality of silly-collections:

Than you can add the dependency to your dependencies:

	<properties>
			...
		<!-- SILLY-COLLECTIONS version -->
		<silly-collections.version>8.4</silly-collections.version>
			...
	</properties>
			...
		<dependencies>
			...
			<!-- SILLY-COLLECTIONS DEPENDENCY -->
			<dependency>
				<groupId>de.alpharogroup</groupId>
				<artifactId>silly-collections</artifactId>
				<version>${silly-collections.version}</version>
			</dependency>
			...
		</dependencies>

			
## gradle dependency

You can first define the version in the ext section and add than the following gradle dependency to your project `build.gradle` if you want to import the core functionality of silly-collections:

```
ext {
			...
    sillyCollectionsVersion = "8.4"
			...
}
dependencies {
			...
implementation("de.alpharogroup:silly-collections:$sillyCollectionsVersion")
			...
}
```

## Semantic Versioning

The versions of silly-collections are maintained with the Simplified Semantic Versioning guidelines.

Release version numbers will be incremented in the following format:

`<major>.<minor>.<patch>`

For detailed information on versioning for this project you can visit this [wiki page](https://github.com/lightblueseas/mvn-parent-projects/wiki/Simplified-Semantic-Versioning).

## Want to Help and improve it? ###

The source code for silly-collections are on GitHub. Please feel free to fork and send pull requests!

Create your own fork of [lightblueseas/silly-collections/fork](https://github.com/lightblueseas/silly-collections/fork)

To share your changes, [submit a pull request](https://github.com/lightblueseas/silly-collections/pull/new/develop).

Don't forget to add new units tests on your changes.

## Contacting the Developers

Do not hesitate to contact the silly-collections developers with your questions, concerns, comments, bug reports, or feature requests.
- Feature requests, questions and bug reports can be reported at the [issues page](https://github.com/lightblueseas/silly-collections/issues).

## Similar projects

Here is a list of awesome similar projects:

Open Source:

 * [streamex](https://github.com/amaembo/streamex) Enhancing Java 8 Streams
 * [glazedlists](https://github.com/glazedlists/glazedlists) Open Source List Transformations for Java

## Credits

|**Travis CI**|
|     :---:      |
|[![Travis CI](https://travis-ci.com/images/logos/TravisCI-Full-Color.png)](https://coveralls.io/github/lightblueseas/silly-collections?branch=master)|
|Special thanks to [Travis CI](https://travis-ci.org) for providing a free continuous integration service for open source projects|
|     <img width=1000/>     |

|**Nexus Sonatype repositories**|
|     :---:      |
|[![sonatype repository](https://img.shields.io/nexus/r/https/oss.sonatype.org/de.alpharogroup/silly-collections.svg?style=for-the-badge)](https://oss.sonatype.org/index.html#nexus-search;gav~de.alpharogroup~silly-collections~~~)|
|Special thanks to [sonatype repository](https://www.sonatype.com) for providing a free maven repository service for open source projects|
|     <img width=1000/>     |

|**coveralls.io**|
|     :---:      |
|[![Coverage Status](https://coveralls.io/repos/github/lightblueseas/silly-collections/badge.svg?branch=master)](https://coveralls.io/github/lightblueseas/silly-collections?branch=master)|
|Special thanks to [coveralls.io](https://coveralls.io) for providing a free code coverage for open source projects|
|     <img width=1000/>     |

|**javadoc.io**|
|     :---:      |
|[![Javadocs](http://www.javadoc.io/badge/de.alpharogroup/silly-collections.svg)](http://www.javadoc.io/doc/de.alpharogroup/silly-collections)|
|Special thanks to [javadoc.io](http://www.javadoc.io) for providing a free javadoc documentation for open source projects|
|     <img width=1000/>     |
