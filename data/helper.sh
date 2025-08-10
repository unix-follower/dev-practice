#!/usr/bin/env bash

set -e

export PGHOST=$(minikube ip)
export PGUSER=postgres
export PGPORT=32000
export PGDATABASE=assistant_datahub
# export PGPASSWORD=<YOUR PASSWORD>

# dropdb --echo $PGDATABASE
createdb --echo --encoding='utf-8'

# CREATE ROLE assistant_svc WITH
#   NOSUPERUSER NOCREATEDB NOCREATEROLE NOINHERIT NOREPLICATION NOBYPASSRLS
#   CONNECTION LIMIT 256
#   PASSWORD '<PASSWORD_HERE>';

# CREATE USER pubchem WITH NOCREATEDB NOCREATEROLE PASSWORD '<PASSWORD_HERE>' ROLE assistant_svc;
# CREATE USER foodb WITH PASSWORD '<PASSWORD_HERE>' NOCREATEDB NOCREATEROLE ROLE assistant_svc;
# CREATE USER stock_market WITH PASSWORD '<PASSWORD_HERE>' NOCREATEDB NOCREATEROLE ROLE assistant_svc;

# CREATE SCHEMA IF NOT EXISTS pubchem AUTHORIZATION pubchem;
# CREATE SCHEMA IF NOT EXISTS foodb AUTHORIZATION foodb;
# CREATE SCHEMA IF NOT EXISTS stock_market AUTHORIZATION stock_market;
# ALTER DEFAULT PRIVILEGES IN SCHEMA stock_market GRANT ALL PRIVILEGES ON TABLES TO stock_market;
# ALTER TABLE stock_market.stock OWNER TO stock_market;

# \COPY stock_market.stock (date_at, ticker, open, high, low, close, adjusted_close, volume, dividends, stock_splits, capital_gains)
# FROM './stock-market/shuffled_stock_history-2024-05-15.csv' DELIMITER ',' CSV HEADER;
