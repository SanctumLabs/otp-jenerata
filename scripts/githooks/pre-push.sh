#!/usr/bin/env bash

echo "Running tests before pushing to Github"

remote="$1"
url="$2"

# Skipping e2e tests as they require running docker compose. This is a slow process and not really required when
# handling pre-push hook
./gradlew test -x e2e
status=$?

if [[ "$status" = 0 ]] ; then
    echo "Tests are successful! - pushing to $remote ($url)"
    exit 0
else
    echo "Push Rejected -  Please ensure all tests are successful"
    exit 1
fi