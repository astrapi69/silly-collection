#!/usr/bin/env bash
#
# Builds the five artifacts this project publishes twice from the checked-out commit - each time from a clean
# build directory into a fresh local Maven repository - and compares them by SHA-256 (#99, as in
# astrapi69/silly-strings#14 and astrapi69/file-worker#97). A release can be rebuilt bit for bit from its tag
# only if two builds of the same commit give the same bytes.
#
# Usage:
#   scripts/check-reproducible.sh                     both builds on the toolchain Gradle finds
#   scripts/check-reproducible.sh <jdk>               both builds on that JDK only
#   scripts/check-reproducible.sh <jdk> <other jdk>   the second build on the other JDK, to show that the JDK's
#                                                     patch level does not reach the artifacts
#
# Exit codes: 0 all five identical; 1 an artifact differs (the archive entries that differ are listed);
# 2 a build failed or the arguments are wrong.
set -uo pipefail

[[ $# -le 2 ]] || { echo "usage: scripts/check-reproducible.sh [<jdk home> [<jdk home of the second build>]]" >&2; exit 2; }
cd "$(dirname "$0")/.."
work="$(mktemp -d "${TMPDIR:-/tmp}/check-reproducible.XXXXXX")"
trap 'rm -rf "$work"' EXIT
# The signatures are not compared, and signing needs the key: on a machine with signing.gnupg.keyName the gpg
# command could ask for its passphrase. The two builds therefore sign nothing (#99).
printf 'allprojects { tasks.withType(Sign).configureEach { enabled = false } }\n' > "$work/no-signing.gradle"

build()
{
	local n="$1" jdk="${2:-}" args=()
	if [[ -n "$jdk" ]]; then
		[[ -x "$jdk/bin/javac" ]] || { echo "no JDK at $jdk" >&2; exit 2; }
		args=(-Porg.gradle.java.installations.auto-detect=false -Porg.gradle.java.installations.auto-download=false
			"-Porg.gradle.java.installations.paths=$jdk")
	fi
	echo "build $n${jdk:+ on $jdk}"
	# Gradle prefers the JVM it runs on as the toolchain when it matches, so the build runs on that JDK as well
	env ${jdk:+JAVA_HOME="$jdk"} ./gradlew --no-daemon --no-build-cache --console=plain -q --init-script "$work/no-signing.gradle" \
		"-Dmaven.repo.local=$work/repo$n" "${args[@]}" \
		clean publishMavenJavaPublicationToMavenLocal > "$work/build$n.log" 2>&1 \
		|| { echo "build $n failed:" >&2; tail -20 "$work/build$n.log" >&2; exit 2; }
	find "$work/repo$n" -path '*/io/github/astrapi69/*' \( -name '*.jar' -o -name '*.pom' -o -name '*.module' \) \
		-exec sha256sum {} + | sed "s|$work/repo$n/||" | sort -k2 > "$work/sha$n.txt"
}

build 1 "${1:-}"
build 2 "${2:-${1:-}}"

if diff -q "$work/sha1.txt" "$work/sha2.txt" > /dev/null; then
	echo "identical, $(wc -l < "$work/sha1.txt") artifacts:"
	sed 's/^/  /' "$work/sha1.txt"
	exit 0
fi
echo "different:"
join -j 2 "$work/sha1.txt" "$work/sha2.txt" | awk '$2 != $3 {print "  " $1}' | while read -r file; do
	echo "  $file"
	case "$file" in
	*.jar)
		python3 - "$work/repo1/$file" "$work/repo2/$file" <<'PY'
import sys, zipfile
a, b = zipfile.ZipFile(sys.argv[1]), zipfile.ZipFile(sys.argv[2])
names = [i.filename for i in a.infolist()]
if names != [i.filename for i in b.infolist()]:
    print('    the entries or their order differ')
for name in names:
    if name in b.namelist() and a.read(name) != b.read(name):
        print('    entry ' + name)
PY
		;;
	esac
done
exit 1
