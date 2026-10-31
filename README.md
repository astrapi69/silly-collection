

# Overview

<div style="text-align: center">

[![Java CI with Gradle](https://github.com/astrapi69/silly-collection/actions/workflows/gradle.yml/badge.svg)](https://github.com/astrapi69/silly-collection/actions/workflows/gradle.yml)
[![Coverage Status](https://codecov.io/gh/astrapi69/silly-collection/branch/develop/graph/badge.svg)](https://codecov.io/gh/astrapi69/silly-collection)
[![Open Issues](https://img.shields.io/github/issues/astrapi69/silly-collection.svg?style=flat)](https://github.com/astrapi69/silly-collection/issues)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.astrapi69/silly-collection)](https://central.sonatype.com/artifact/io.github.astrapi69/silly-collection)
[![Javadocs](http://www.javadoc.io/badge/io.github.astrapi69/silly-collection.svg)](http://www.javadoc.io/doc/io.github.astrapi69/silly-collection)
[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](http://opensource.org/licenses/MIT)
[![Donate](https://img.shields.io/badge/donate-❤-ff2244.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=GVBTWLRAZ7HB8)

</div>

Utility library for collections, comparators and iterator classes.

> Please support this project by simply putting a Github <!-- Place this tag where you want the button to render. --><a class="github-button" href="https://github.com/astrapi69/silly-collection" data-icon="octicon-star" aria-label="Star astrapi69/silly-collection on GitHub">
 Star ⭐</a>
>
> Share this library with friends on Twitter and everywhere else you can
>
> If you love this project [![donation](https://img.shields.io/badge/donate-❤-ff2244.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=GVBTWLRAZ7HB8)

## Maven dependency

The library is on Maven Central. Check out [Maven Central](https://central.sonatype.com/artifact/io.github.astrapi69/silly-collection) for the latest release.

Replace the variable ${latestVersion} with the current latest version: [![Maven Central](https://img.shields.io/maven-central/v/io.github.astrapi69/silly-collection)](https://central.sonatype.com/artifact/io.github.astrapi69/silly-collection)

Add the following maven dependency to your project `pom.xml` if you want to import the core
functionality of silly-collection:

Then you can add the dependency to your dependencies:

    <properties>
            ...
        <!-- SILLY-COLLECTION version -->
        <silly-collection.version>${latestVersion}</silly-collection.version>
            ...
    </properties>
            ...
        <dependencies>
            ...
            <!-- SILLY-COLLECTION DEPENDENCY -->
            <dependency>
                <groupId>io.github.astrapi69</groupId>
                <artifactId>silly-collection</artifactId>
                <version>${silly-collection.version}</version>
            </dependency>
            ...
        </dependencies>

## gradle dependency

You can first define the version in the ext section and add than the following gradle dependency to
your project `build.gradle` if you want to import the core functionality of silly-collection:

define version in file gradle.properties

```
sillyCollectionVersion=${latestVersion}
```

or in build.gradle ext area

```
ext {
            ...
    sillyCollectionVersion = "${latestVersion}"
            ...
}
```

then add the dependency to the dependencies area

```
dependencies {
            ...
    implementation("io.github.astrapi69:silly-collection:$sillyCollectionVersion")
            ...
}
```

## Support this project

If silly-collection is useful to you, a star on [GitHub](https://github.com/astrapi69/silly-collection) helps others find it.
Sharing it or reporting a bug helps just as much.

If you would like to contribute financially, the channels are on the repository's Sponsor button, which
reads [.github/FUNDING.yml](.github/FUNDING.yml): GitHub Sponsors, Liberapay, Ko-fi and PayPal.

## License

The source code comes under the liberal MIT License, making silly-collection great for all types of
applications.

## Note

No animals were harmed in the making of this library.

## Semantic Versioning

The versions of silly-collection are maintained with the Simplified Semantic Versioning guidelines.

Release version numbers will be incremented in the following format:

`<major>.<minor>.<patch>`

For detailed information on versioning for this project you can visit
this [wiki page](https://github.com/lightblueseas/mvn-parent-projects/wiki/Simplified-Semantic-Versioning)
.

## Want to Help and improve it? ###

The source code for silly-collection are on GitHub. Please feel free to fork and send pull
requests!

Create your own fork
of [astrapi69/silly-collection/fork](https://github.com/astrapi69/silly-collection/fork)

To share your
changes, [submit a pull request](https://github.com/astrapi69/silly-collection/pull/new/develop)
.

Don't forget to add new units tests on your changes.

## Contacting the Developers

Do not hesitate to contact the silly-collection developers with your questions, concerns, comments,
bug reports, or feature requests.

- Feature requests, questions and bug reports can be reported at
  the [issues page](https://github.com/astrapi69/silly-collection/issues).

## Similar projects

Here is a list of awesome similar projects:

Open Source:

* [streamex](https://github.com/amaembo/streamex) Enhancing Java 8 Streams
* [glazedlists](https://github.com/glazedlists/glazedlists) Open Source List Transformations for
  Java

## Credits

|**Maven Central**|
|     :---:      |
|[![Maven Central](https://img.shields.io/maven-central/v/io.github.astrapi69/silly-collection?style=for-the-badge)](https://central.sonatype.com/artifact/io.github.astrapi69/silly-collection)|
|Special thanks to [sonatype repository](https://www.sonatype.com) for providing a free maven repository service for open source projects|
|     <img width=1000/>     |

|**javadoc.io**|
|     :---:      |
|[![Javadocs](http://www.javadoc.io/badge/io.github.astrapi69/silly-collection.svg)](http://www.javadoc.io/doc/io.github.astrapi69/silly-collection)|
|Special thanks to [javadoc.io](http://www.javadoc.io) for providing a free javadoc documentation for open source projects|
|     <img width=1000/>     |

## Git hooks

The repository's git hooks live in `.githooks`: `commit-msg` where present, and `pre-commit`, which refuses a
commit whose files Spotless would format. Activate them once per clone or worktree:

```bash
git config core.hooksPath .githooks
```
