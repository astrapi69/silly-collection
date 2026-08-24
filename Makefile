.PHONY: build build-stacktrace build-warning central-drop central-list central-upload \
	central-upload-repository clean \
	dependencies dependency-updates jacoco-coverage jacoco-report jar javadoc \
	license-format publish publish-local release spotless-apply \
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
