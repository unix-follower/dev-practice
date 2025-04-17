#!/usr/bin/env bash

set -e

export PGHOST=$(minikube ip)
export PGUSER=postgres
export PGPORT=32000
export PGDATABASE=chebi
# export PGPASSWORD=<YOUR PASSWORD>

create_db() {
  dropdb --echo
  createdb --echo --encoding='utf-8'
}

create_tables() {
  psql --echo-errors -f pgsql_create_tables.sql
}

# To split data run this:
# grep -n $reference_id generic_dump_3star/references.sql > matches.txt
# tail -n +$line_no generic_dump_3star/references.sql > generic_dump_3star/out.sql

insert_data() {
  echo 'insert data from compounds.sql'
  psql --echo-errors -f generic_dump_3star/compounds.sql
  echo 'insert data from chemical_data.sql'
  psql --echo-errors -f generic_dump_3star/chemical_data.sql
  echo 'insert data from names.sql'
  psql --echo-errors -f generic_dump_3star/names.sql
  echo 'insert data from comments.sql'
  psql --echo-errors -f generic_dump_3star/comments.sql
  echo 'insert data from structures.sql'
  psql --echo-errors -f generic_dump_3star/structures.sql
  echo 'insert data from database_accession.sql'
  psql --echo-errors -f generic_dump_3star/database_accession.sql
  echo 'insert data from references.sql'
  psql --echo-errors -f generic_dump_3star/references.sql
  echo 'insert data from relation.sql'
  psql --echo-errors -f generic_dump_3star/relation.sql
  echo 'insert data from compound_origins.sql'
  psql --echo-errors -f generic_dump_3star/compound_origins.sql
}

insert_data
