# Overview

<div align="center">

[![Build Status](https://api.travis-ci.com/lightblueseas/silly-collections.svg?branch=master)](https://travis-ci.com/github/lightblueseas/silly-collections)
[![Coverage Status](https://coveralls.io/repos/github/lightblueseas/silly-collections/badge.svg?branch=master)](https://coveralls.io/github/lightblueseas/silly-collections?branch=master)
[![Open Issues](https://img.shields.io/github/issues/lightblueseas/silly-collections.svg?style=flat)](https://github.com/lightblueseas/silly-collections/issues)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.astrapi69/silly-collections/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.astrapi69/silly-collections)
[![Javadocs](http://www.javadoc.io/badge/io.github.astrapi69/silly-collections.svg)](http://www.javadoc.io/doc/io.github.astrapi69/silly-collections)
[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](http://opensource.org/licenses/MIT)
[![Donate](https://img.shields.io/badge/donate-❤-ff2244.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=GVBTWLRAZ7HB8)

</div>

Utility library for collections, comparators and iterator classes.


> Please support this project by simply putting a Github <!-- Place this tag where you want the button to render. -->
                                                         <a class="github-button" href="https://github.com/lightblueseas/silly-collections" data-icon="octicon-star" aria-label="Star lightblueseas/silly-collections on GitHub">Star ⭐</a>
>
> Share this library with friends on Twitter and everywhere else you can
>
> If you love this project [![donation](https://img.shields.io/badge/donate-❤-ff2244.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=GVBTWLRAZ7HB8)


# Donations

This project is kept as an open source product and relies on contributions to remain being
developed. If you like this library, please consider a donation

over paypal: <br><br>
<a href="https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=MJ7V43GU2H386" target="_blank">
<img src="https://www.paypalobjects.com/en_US/GB/i/btn/btn_donateCC_LG.gif" alt="PayPal this" title="PayPal – The safer, easier way to pay online!" style="border: none" />
</a>
<br><br>
or over bitcoin(BTC) with this address:

bc1ql2y99q7e8psndhcc3gferk03esw3qqf677rhjy

<img src="https://github.com/astrapi69/jgeohash/blob/master/src/main/resources/img/bc1ql2y99q7e8psndhcc3gferk03esw3qqf677rhjy.png"
alt="Donation Bitcoin Wallet" width="250"/>

or over FIO with this address:

FIO7tFMUVAA9cHiPPqKMfMXiSxHrbpiFyRYqTketNuM67aULuwjop

<img src="https://github.com/astrapi69/jgeohash/blob/master/src/main/resources/img/FIO7tFMUVAA9cHiPPqKMfMXiSxHrbpiFyRYqTketNuM67aULuwjop.png"
alt="Donation FIO Wallet" width="250"/>

or over Ethereum(ETH) with:

0xc057D159D3C8f3311E73568b334FF6fE82EB2b7D

<img src="https://github.com/astrapi69/jgeohash/blob/master/src/main/resources/img/0xc057D159D3C8f3311E73568b334FF6fE82EB2b7D.png"
alt="Donation Ethereum Wallet" width="250"/>

or over Ethereum Classic(ETC) with:

0xF708cA86D86C246B69c3F4BAe431eBbe0c2bfddD

<img src="https://github.com/astrapi69/jgeohash/blob/master/src/main/resources/img/0xF708cA86D86C246B69c3F4BAe431eBbe0c2bfddD.png"
alt="Donation Ethereum Classic Wallet" width="250"/>

or over Dogecoin(DOGE) with:

D5yi4Um8cpakd6yPRm2hGWuQ5nrVzhSSW1

<img src="https://github.com/astrapi69/jgeohash/blob/master/src/main/resources/img/D5yi4Um8cpakd6yPRm2hGWuQ5nrVzhSSW1.png"
alt="Donation Dogecoin Wallet" width="250"/>

or over Monero(XMR) with:

49bqeRQ7Bf49oJFVC72pqpe5hFbb62pfXDYPdLsadGGF81KZW2ZfrPZ8PbAVu5X2v1TYAspeczMya3cYQysNS4usRRPQHVw

<img src="https://github.com/astrapi69/jgeohash/blob/master/src/main/resources/img/49bqeRQ7Bf49oJFVC72pqpe5hFbb62pfXDYPdLsadGGF81KZW2ZfrPZ8PbAVu5X2v1TYAspeczMya3cYQysNS4usRRPQHVw.png"
alt="Donation Monero Wallet" width="250"/>

or over flattr:

<a href="https://flattr.com/submit/auto?fid=r7vp62&url=https%3A%2F%2Fgithub.com%2Flightblueseas%2Fsilly-collections" target="_blank">
<img src="http://api.flattr.com/button/flattr-badge-large.png" alt="Flattr this" title="Flattr this" border="0" />
</a>

## License

The source code comes under the liberal MIT License, making silly-collections great for all types of
applications.

## Note

No animals were harmed in the making of this library.

## Maven dependency

Maven dependency is now on sonatype. Check
out [sonatype repository](https://oss.sonatype.org/index.html#nexus-search;gav~io.github.astrapi69~silly-collections~~~)
for latest snapshots and releases.

Add the following maven dependency to your project `pom.xml` if you want to import the core
functionality of silly-collections:

Than you can add the dependency to your dependencies:

	<properties>
			...
		<!-- SILLY-COLLECTIONS version -->
		<silly-collections.version>9.1</silly-collections.version>
			...
	</properties>
			...
		<dependencies>
			...
			<!-- SILLY-COLLECTIONS DEPENDENCY -->
			<dependency>
				<groupId>io.github.astrapi69</groupId>
				<artifactId>silly-collections</artifactId>
				<version>${silly-collections.version}</version>
			</dependency>
			...
		</dependencies>

## gradle dependency

You can first define the version in the ext section and add than the following gradle dependency to
your project `build.gradle` if you want to import the core functionality of silly-collections:

define version in file gradle.properties

```

sillyCollectionsVersion=9.1
```

or in build.gradle ext area

```
ext {
			...
    sillyCollectionsVersion = "9.1"
			...
}
```

and than add the dependency to the dependencies area

```
dependencies {
			...
    implementation("io.github.astrapi69:silly-collections:$sillyCollectionsVersion")
			...
}
```

## Semantic Versioning

The versions of silly-collections are maintained with the Simplified Semantic Versioning guidelines.

Release version numbers will be incremented in the following format:

`<major>.<minor>.<patch>`

For detailed information on versioning for this project you can visit
this [wiki page](https://github.com/lightblueseas/mvn-parent-projects/wiki/Simplified-Semantic-Versioning)
.

## Want to Help and improve it? ###

The source code for silly-collections are on GitHub. Please feel free to fork and send pull
requests!

Create your own fork
of [lightblueseas/silly-collections/fork](https://github.com/lightblueseas/silly-collections/fork)

To share your
changes, [submit a pull request](https://github.com/lightblueseas/silly-collections/pull/new/develop)
.

Don't forget to add new units tests on your changes.

## Contacting the Developers

Do not hesitate to contact the silly-collections developers with your questions, concerns, comments,
bug reports, or feature requests.

- Feature requests, questions and bug reports can be reported at
  the [issues page](https://github.com/lightblueseas/silly-collections/issues).

## Similar projects

Here is a list of awesome similar projects:

Open Source:

* [streamex](https://github.com/amaembo/streamex) Enhancing Java 8 Streams
* [glazedlists](https://github.com/glazedlists/glazedlists) Open Source List Transformations for
  Java

## Credits

|**Travis CI**|
|     :---:      |
|[![Travis CI](https://travis-ci.com/images/logos/TravisCI-Full-Color.png)](https://travis-ci.com)|
|Special thanks to [Travis CI](https://travis-ci.org) for providing a free continuous integration service for open source projects|
|     <img width=1000/>     |

|**Nexus Sonatype repositories**|
|     :---:      |
|[![sonatype repository](https://img.shields.io/nexus/r/https/oss.sonatype.org/io.github.astrapi69/silly-collections.svg?style=for-the-badge)](https://oss.sonatype.org/index.html#nexus-search;gav~io.github.astrapi69~silly-collections~~~)|
|Special thanks to [sonatype repository](https://www.sonatype.com) for providing a free maven repository service for open source projects|
|     <img width=1000/>     |

|**coveralls.io**|
|     :---:      |
|[![Coverage Status](https://coveralls.io/repos/github/lightblueseas/silly-collections/badge.svg?branch=master)](https://coveralls.io/github/lightblueseas/silly-collections?branch=master)|
|Special thanks to [coveralls.io](https://coveralls.io) for providing a free code coverage for open source projects|
|     <img width=1000/>     |

|**javadoc.io**|
|     :---:      |
|[![Javadocs](http://www.javadoc.io/badge/io.github.astrapi69/silly-collections.svg)](http://www.javadoc.io/doc/io.github.astrapi69/silly-collections)|
|Special thanks to [javadoc.io](http://www.javadoc.io) for providing a free javadoc documentation for open source projects|
|     <img width=1000/>     |
