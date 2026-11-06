"""Writes the mutation score of a PIT report as a shields.io endpoint badge.

Usage: mutation-score-badge.py <mutations.xml> <badge.json>

The score is the share of the mutants that the tests detected, rounded as PIT rounds the score it
checks against mutationThreshold, so the badge shows the number the build was held to. The badge
follows https://shields.io/badges/endpoint-badge; README.md reads it from the gh-pages branch, to
which .github/workflows/mutation-testing.yml publishes it on a push to develop or master.
"""

import json
import math
import sys
import xml.etree.ElementTree as ElementTree
from pathlib import Path

# the lowest score of each color, from the best down
COLORS = [(95, "brightgreen"), (90, "green"), (80, "yellowgreen"), (70, "yellow"), (60, "orange")]


def color_of(score):
    for minimum, color in COLORS:
        if score >= minimum:
            return color
    return "red"


def badge_of(report):
    mutations = ElementTree.parse(report).getroot().findall("mutation")
    if not mutations:
        raise SystemExit(f"{report} holds no mutation, so it gives no score")
    detected = sum(1 for mutation in mutations if mutation.get("detected") == "true")
    # as the score PIT checks mutationThreshold against, PercentageCalculator.getPercentage(long,
    # long, int) with the precision 0: half up, not half to even, and no more than 99 while a mutant
    # survives. Its other overload, Math.round of a float, is the one of the console and the HTML
    # report, and differs in some cases (15 of 24 detected: 62 there, 63 here and for the floor)
    score = math.floor(100 * detected / len(mutations) + 0.5)
    if detected < len(mutations):
        score = min(score, 99)
    print(f"{detected} of {len(mutations)} mutants detected: {score}%")
    return {
        "schemaVersion": 1,
        "label": "mutation score",
        "message": f"{score}%",
        "color": color_of(score),
    }


def main(arguments):
    if len(arguments) != 2:
        raise SystemExit(__doc__)
    report, badge = (Path(argument) for argument in arguments)
    badge.parent.mkdir(parents=True, exist_ok=True)
    badge.write_text(json.dumps(badge_of(report)) + "\n", encoding="utf-8")


if __name__ == "__main__":
    main(sys.argv[1:])
