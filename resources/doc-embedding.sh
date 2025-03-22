#!/usr/bin/env bash

set -e

if [[ -z $SERVER_URL ]]; then
  echo 'The environment variable SERVER_URL is not set'
  exit -1
fi

insert() {
  curl $SERVER_URL/api/v1/ml/embedding \
    -H 'Content-Type: application/json' \
    -d @embedding-data.json | jq > __tmp-resp.json
}

similarity_search() {
  curl $SERVER_URL/api/v1/ml/embedding/similarity \
    -H 'Content-Type: application/json' \
    -d @query-with-embedding.json | jq > __tmp-resp.json
}
