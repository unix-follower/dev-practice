#!/usr/bin/env bash

set -e

export PGHOST=$(minikube ip)
export PGUSER=postgres
export PGPORT=32000
export PGDATABASE=chembl_35
# export PGPASSWORD=<YOUR PASSWORD>

create_db() {
  dropdb --echo
  createdb --echo --encoding='utf-8'
}

insert_data() {
  pg_restore --no-owner -h $host -U $user -p $port -d $db_name chembl/chembl_35/chembl_35_postgresql/chembl_35_postgresql.dmp
}

create_db
