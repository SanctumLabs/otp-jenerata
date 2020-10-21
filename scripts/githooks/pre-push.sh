#!/usr/bin/env bash

echo "Running tests before pushing to Github"

remote="$1"
url="$2"

./gradlew test
status=$?

if [[ "$status" = 0 ]] ; then
    echo "Tests are successful! - pushing to $remote ($url)"
    exit 0
else
    echo "Push Rejected -  Please ensure all tests are successful"
    exit 1
fi