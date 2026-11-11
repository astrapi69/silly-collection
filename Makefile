.PHONY: build build-stacktrace build-warning clean \
	dependencies dependency-updates jacoco-coverage jacoco-report jar javadoc \
	license-format pitest pitest-status publish-local spotless-apply \
	spotless-check spotless-java spotless-misc tag-release test \
	version-catalog-format version-catalog-update

VERSION := $(shell sed -n 's/^projectVersion=//p' gradle.properties)

build:
	./gradlew build

build-stacktrace:
	./gradlew build --stacktrace --warning-mode all

build-warning:
	./gradlew build --warning-mode all

clean:
	./gradlew clean

test:
	./gradlew test

# --- mirrors Gradle "Run Configurations" panel ---

dependencies:
	./gradlew dependencies

dependency-updates:
	./gradlew dependencyUpdates

jacoco-coverage:
	./gradlew jacocoTestCoverageVerification

jacoco-report:
	./gradlew jacocoTestReport

# PIT runs on GitHub, not here: local PIT runs crashed the maintainer's machine on 2026-11-05
# (.github/workflows/mutation-testing.yml, #98; as astrapi69/comparator-extensions#28 and #33).
# This starts the workflow for the current branch, which has to be pushed as it is, since the run
# tests what GitHub holds, and prints how to follow the run; it does not wait for it. The summary
# of the run lists killed of total, the score, the floor and every surviving mutant, make
# pitest-status prints it.
pitest:
	@branch=$$(git rev-parse --abbrev-ref HEAD); \
	test "$$branch" != HEAD || { echo "pitest runs on GitHub for a branch, and HEAD is detached"; exit 1; }; \
	test "$$(git rev-parse HEAD)" = "$$(git rev-parse --verify --quiet "origin/$$branch")" || { \
		echo "pitest runs on GitHub: push $$branch first, PIT tests what origin/$$branch holds"; exit 1; }; \
	runs="gh run list --workflow mutation-testing.yml --branch $$branch --event workflow_dispatch --limit 1"; \
	before=$$($$runs --json databaseId --jq '.[0].databaseId // empty'); \
	gh workflow run mutation-testing.yml --ref "$$branch" || exit 1; \
	run=; \
	for attempt in 1 2 3 4 5 6 7 8 9 10; do \
		sleep 2; \
		run=$$($$runs --json databaseId --jq '.[0].databaseId // empty'); \
		test -n "$$run" && test "$$run" != "$$before" && break; \
		run=; \
	done; \
	test -n "$$run" || { \
		echo "mutation-testing.yml was started for $$branch; find its run with: $$runs"; exit 0; }; \
	echo "Mutation testing runs on GitHub for $$branch at $$(git rev-parse --short HEAD):"; \
	echo "  $$(gh run view "$$run" --json url --jq .url)"; \
	echo "follow it:     gh run watch $$run"; \
	echo "the result:    make pitest-status, the summary of the run"; \
	echo "PIT's report:  gh run download $$run -n pitest-report -D <directory>  (the artifact pitest-report)"

# the summary mutation-testing.yml wrote into the log of the newest run of the current branch,
# whatever started it: a push, a pull request or make pitest
pitest-status:
	@branch=$$(git rev-parse --abbrev-ref HEAD); \
	test "$$branch" != HEAD || { echo "pitest-status looks for the runs of a branch, and HEAD is detached"; exit 1; }; \
	run=$$(gh run list --workflow mutation-testing.yml --branch "$$branch" --limit 1 \
		--json databaseId --jq '.[0].databaseId // empty'); \
	test -n "$$run" || { echo "no Mutation testing run of $$branch yet; make pitest starts one"; exit 1; }; \
	gh run view "$$run" --json event,headSha,status,conclusion,createdAt,url --jq \
		'"Mutation testing of \(.headSha[0:7]) (\(.event), \(.createdAt)): \(.status) \(.conclusion)\n  \(.url)"'; \
	test "$$(gh run view "$$run" --json status --jq .status)" = completed || { \
		echo "the summary follows when the run is complete: gh run watch $$run"; exit 0; }; \
	summary=$$(gh run view "$$run" --log | awk -F '\t' ' \
		{ line = $$0; sub(/^[^\t]*\t[^\t]*\t[^ ]*Z ?/, "", line) } \
		step == "" && line ~ /^## Mutation testing/ { step = $$2 } \
		step != "" { if ($$2 != step) exit; print line }'); \
	test -n "$$summary" || { echo "the log of run $$run holds no summary: the run stopped before it"; exit 1; }; \
	printf '\n%s\n\n' "$$summary"; \
	echo "PIT's report:  gh run download $$run -n pitest-report -D <directory>  (the artifact pitest-report)"

jar:
	./gradlew jar

javadoc:
	./gradlew javadoc

# license headers are managed by the spotless licenseHeaderFile step
license-format:
	./gradlew spotlessApply

publish-local:
	./gradlew publishMavenJavaPublicationToMavenLocal

spotless-apply:
	./gradlew spotlessApply

spotless-check:
	./gradlew spotlessCheck

spotless-java:
	./gradlew spotlessJavaApply

spotless-misc:
	./gradlew spotlessMiscApply

# Creates the RELEASE tag on the checked-out commit. A release leaves only from CI: pushing the tag runs
# publish.yml, which uploads to the Central Portal (README "Releasing"). There is no target that publishes from
# this machine or into ~/.m2 for a release: the former `release` ran publishMavenJavaPublicationToMavenLocal,
# publish and central-upload from here (#99, as astrapi69/comparator-extensions' tag-release, #28). An upload
# leaves only from CI, so no target uploads from here: `publish` and the central-* targets, which called the
# old staging API with credentials from ~/.gradle/gradle.properties, are gone (#133).
tag-release:
	@case "$(VERSION)" in *-SNAPSHOT) \
		echo "tag-release refuses $(VERSION): a release tag carries a release version (README.md, \"Releasing\")"; \
		exit 1;; esac
	@test "$(CONFIRM)" = "yes" || { \
		echo "tag-release creates a RELEASE tag, and pushing it triggers publish.yml, which uploads"; \
		echo "to the Central Portal. Re-run with CONFIRM=yes if that is intended."; \
		exit 1; }
	./gradlew tagRelease

version-catalog-format:
	./gradlew versionCatalogFormat

version-catalog-update:
	./gradlew versionCatalogUpdate
