#!/usr/bin/env bash

set -e

unpack() {
  gzip -d PubChem_Food-Additives-and-Ingredients.csv.gz
  gzip -d PubChem_Food-Additives-and-Ingredients.sdf.gz
  unzip PubChem_Food-Additives-and-Ingredients_images.zip -d PubChem_Food-Additives-and-Ingredients_images
}

unpack
