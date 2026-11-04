

# Overview

<div style="text-align: center">

[![Java CI with Gradle](https://github.com/astrapi69/silly-collection/actions/workflows/gradle.yml/badge.svg)](https://github.com/astrapi69/silly-collection/actions/workflows/gradle.yml)
[![Coverage Status](https://codecov.io/gh/astrapi69/silly-collection/branch/develop/graph/badge.svg)](https://codecov.io/gh/astrapi69/silly-collection)
[![Mutation Score](https://img.shields.io/endpoint?url=https%3A%2F%2Fraw.githubusercontent.com%2Fastrapi69%2Fsilly-collection%2Fgh-pages%2Fbadges%2Fmutation-score-develop.json)](https://pitest.org/)
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

## Quality

As of 28.3, measured by its release gate (JDK 17, the tree of `RELEASE-28.3`); each release takes
the numbers of its own gate:

| | |
|---|---|
| tests | 518 in 24 test classes; 3 are skipped: two `BeanTester` tests that wait for a factory method, and a benchmark |
| line and branch coverage (JaCoCo) | 1247 of 1247 lines, 546 of 558 branches; the build fails below 98% of the lines or 95% of the branches (`jacocoTestCoverageVerification` in `gradle/testing.gradle`) |
| mutation score (PIT) | 781 of 794 mutants killed, 98%; the build fails below 95% (`mutationThreshold` in `build.gradle`). The 13 survivors are equivalent: ten conditions whose edge case gives the same result either way, such as `0 < elements.length` before adding no elements, and three `return true` mutants of `add` methods that always return true, as `ArrayList.add` does |

The CI build runs both checks on every pull request, and publishes the mutation score of `develop`
as the badge above.

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

## Releasing

What a release consists of, in the order it is run:

1. **No open issue** but those of a later milestone: every issue of the release's milestone is
   closed, and a finding on the way is fixed in the release or, if it changes what callers rely
   on, assigned to the next major release.
2. **One pull request** with the CHANGELOG entry and `projectVersion` without `-SNAPSHOT`
   (`docs: changelog for X.Y, and the version that carries it`), the full build green on it.
3. **The release gate** on that pull request's tree: the tests, `pitest` with its floor and
   `jacocoTestCoverageVerification` with its floor in one run, `--rerun-tasks --no-build-cache`, on
   JDK 25, the toolchain of the build; the class files are of Java 25 (major version 69), and the
   resolved dependencies hold no `-SNAPSHOT`. Its numbers go into the CHANGELOG entry and into the
   Quality section above, in the same pull request, before it is merged.
4. **Tag the merge commit** with `./gradlew tagRelease` (`make tag-release`), which names it
   `RELEASE-X.Y`, once its tree is the tree of the gate, and push the tag. `publish.yml` uploads to
   the Central Portal, where the maintainer releases it. A pushed tag is never moved or deleted; a
   failed run is fixed on the branch and run again.
5. **Verify on Central**: the jar, pom, `.module`, `-sources.jar` and `-javadoc.jar`, their sha1, and
   `gpg --verify` on the `.asc`.
6. **GitHub release** from the CHANGELOG entry.
7. **`master` follows the release** by a merge of the tag: on `master`,
   `git merge --no-ff "RELEASE-X.Y^{commit}" -m "Merge tag RELEASE-X.Y into master"`. Before
   `master` is pushed, `git diff RELEASE-X.Y master` has to be empty; if it is not, nothing is
   pushed. Never a force push: `master` is always the latest release, nothing else.
8. **Open the next cycle**: `projectVersion` to the next `-SNAPSHOT`.

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
