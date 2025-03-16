#!/usr/bin/env bash

set -ex

run() {
  executable=$1
  if [[ -z $executable ]]; then
    echo "No file specfied to execute"
    exit -1
  fi
  $executable
}

run_all() {
  build/build/Release/example1
  build/build/Release/example2
  build/build/Release/example3
}

run_all
