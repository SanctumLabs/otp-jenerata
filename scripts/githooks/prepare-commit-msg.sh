#!/usr/bin/env bash

firstLine=`head -2 $1 | tail -1`
if [[ $firstLine == \#* ]]; then # Testing that the file starts with a comment, not yet a real commit ;)
  echo '<type>(<component>): <subject>' > .prepare-commit-msg-temp
  echo '' >> .prepare-commit-msg-temp
  echo '<body>' >> .prepare-commit-msg-temp
  echo '' >> .prepare-commit-msg-temp
  echo '# types: build, ci, chore, docs, feat, fix, style, perf, refactor, style, test' >> .prepare-commit-msg-temp

  { cat .prepare-commit-msg-temp; cat $1; } > .prepare-commit-msg-temp2
  cat .prepare-commit-msg-temp2 > $1
  rm .prepare-commit-msg-temp .prepare-commit-msg-temp2
fi