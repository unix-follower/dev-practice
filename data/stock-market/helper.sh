#!/usr/bin/env bash

set -e

export PGHOST=$(minikube ip)
export PGUSER=postgres
export PGPORT=32000
export PGDATABASE=stock_market
# export PGPASSWORD=<YOUR PASSWORD>


# dropdb --echo $PGDATABASE
createdb --echo --encoding='utf-8'
