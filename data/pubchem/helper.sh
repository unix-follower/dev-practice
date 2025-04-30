#!/usr/bin/env bash

set -e

unpack_food_additives_and_ingredients() {
  gzip -d PubChem_Food-Additives-and-Ingredients.csv.gz
  gzip -d PubChem_Food-Additives-and-Ingredients.sdf.gz
  unzip PubChem_Food-Additives-and-Ingredients_images.zip -d PubChem_Food-Additives-and-Ingredients_images
}

unpack_pubchem_fda_substances_added_to_food() {
  gzip -d PubChem_FDA-Substances-Added-to-Food.csv.gz
  gzip -d PubChem_FDA-Substances-Added-to-Food.sdf.gz
  unzip PubChem_FDA-Substances-Added-to-Food_images.zip -d PubChem_FDA-Substances-Added-to-Food_images
}

unpack_pubchem_food_additive_classes() {
  gzip -d PubChem_Food-Additive-Classes.csv.gz
  gzip -d PubChem_Food-Additive-Classes.sdf.gz
  unzip PubChem_Food-Additive-Classes_images.zip -d PubChem_Food-Additive-Classes_images
}

unpack_pubchem_food_additive_definition() {
  gzip -d PubChem_Food-Additive-Definition.csv.gz
  gzip -d PubChem_Food-Additive-Definition.sdf.gz
  unzip PubChem_Food-Additive-Definition_images.zip -d PubChem_Food-Additive-Definition_images
}

unpack() {
  gzip -d PubChem_BioAssay-Classification.csv.gz
}

unpack
