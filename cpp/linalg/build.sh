#!/usr/bin/env bash

set -ex

rm -rf build

conan install . --output-folder=build --build=missing
cmake --list-presets
cmake --preset conan-release
cmake --build --preset conan-release
