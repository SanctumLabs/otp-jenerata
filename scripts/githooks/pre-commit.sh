#!/usr/bin/env bash

files_to_lint=$(git diff --cached --name-only --diff-filter=ACM | grep '\.kt$')
if [[ -n "$files_to_lint" ]]; then
    echo "Running static analysis..."
    ./gradlew detekt --daemon

    status=$?

    if [[ "$status" = 0 ]] ; then
        echo "Static analysis found no problems."
        exit 0
    else
        echo 1>&2 "Static analysis found violations it could not fix."
        echo "Run ./gradlew detektIdeaFormat to fix issues"
        exit 1
    fi
else
    echo  "Static analysis not needed - No Kotlin file modified"
    exit 0
fi