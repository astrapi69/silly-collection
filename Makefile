.PHONY: build build-stacktrace build-warning central-drop central-list central-upload \
	central-upload-repository clean \
	dependencies dependency-updates jacoco-coverage jacoco-report jar javadoc \
	license-format pitest pitest-status publish publish-local release spotless-apply \
	spotless-check spotless-java spotless-misc tag-release test \
	version-catalog-format version-catalog-update

# Central Portal settings. The credentials come from the environment variables
# CENTRAL_USERNAME/CENTRAL_PASSWORD or from ~/.gradle/gradle.properties
# (centralUsername/centralPassword). PUBLISHING_TYPE is user_managed (review and
# publish in the Portal) or automatic (publish directly after validation).
CENTRAL_NAMESPACE ?= io.github.astrapi69
CENTRAL_STAGING_API ?= https://ossrh-staging-api.central.sonatype.com
PUBLISHING_TYPE ?= user_managed
CENTRAL_USERNAME ?= $(shell sed -n 's/^centralUsername=//p' $(HOME)/.gradle/gradle.properties 2>/dev/null)
CENTRAL_PASSWORD ?= $(shell sed -n 's/^centralPassword=//p' $(HOME)/.gradle/gradle.properties 2>/dev/null)
CENTRAL_TOKEN = $(shell printf '%s:%s' '$(CENTRAL_USERNAME)' '$(CENTRAL_PASSWORD)' | base64 -w0)

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

publish:
	./gradlew publish

publish-local:
	./gradlew publishMavenJavaPublicationToMavenLocal

# lists the staging repositories of the namespace on the OSSRH staging api
central-list:
	@curl -sf -H "Authorization: Bearer $(CENTRAL_TOKEN)" \
		"$(CENTRAL_STAGING_API)/manual/search/repositories?ip=any" | python3 -m json.tool

# moves the open staging repository of the namespace to the Central Portal, where the
# deployment becomes visible under https://central.sonatype.com/publishing/deployments
central-upload:
	@curl -sf -X POST -H "Authorization: Bearer $(CENTRAL_TOKEN)" \
		"$(CENTRAL_STAGING_API)/manual/upload/defaultRepository/$(CENTRAL_NAMESPACE)?publishing_type=$(PUBLISHING_TYPE)" \
		&& echo "staging repository uploaded to the Central Portal ($(PUBLISHING_TYPE))"

# moves the staging repository with the given key to the Central Portal, for instance
# make central-upload-repository REPOSITORY_KEY=46IpjV/1.2.3.4/io.github.astrapi69--default-repository
central-upload-repository:
	@test -n "$(REPOSITORY_KEY)" || { echo "REPOSITORY_KEY is required, see make central-list"; exit 1; }
	@curl -sf -X POST -H "Authorization: Bearer $(CENTRAL_TOKEN)" \
		"$(CENTRAL_STAGING_API)/manual/upload/repository/$(REPOSITORY_KEY)?publishing_type=$(PUBLISHING_TYPE)" \
		&& echo "staging repository $(REPOSITORY_KEY) uploaded to the Central Portal ($(PUBLISHING_TYPE))"

# drops the staging repository with the given key
central-drop:
	@test -n "$(REPOSITORY_KEY)" || { echo "REPOSITORY_KEY is required, see make central-list"; exit 1; }
	@curl -sf -X DELETE -H "Authorization: Bearer $(CENTRAL_TOKEN)" \
		"$(CENTRAL_STAGING_API)/manual/drop/repository/$(REPOSITORY_KEY)" \
		&& echo "staging repository $(REPOSITORY_KEY) dropped"

# full release: clean, publish to local and remote, upload to the portal, tag the release
release:
	./gradlew clean publishMavenJavaPublicationToMavenLocal publish tagRelease
	$(MAKE) central-upload

spotless-apply:
	./gradlew spotlessApply

spotless-check:
	./gradlew spotlessCheck

spotless-java:
	./gradlew spotlessJavaApply

spotless-misc:
	./gradlew spotlessMiscApply

tag-release:
	./gradlew tagRelease

version-catalog-format:
	./gradlew versionCatalogFormat

version-catalog-update:
	./gradlew versionCatalogUpdate
