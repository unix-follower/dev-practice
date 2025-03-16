#!/usr/bin/env bash

set -e

sed -i '' "s/\$PGPASSWORD/$PGPASSWORD/g" postgres-values.yaml

helm install postgres17 -n assistant --values postgres-values.yaml oci://registry-1.docker.io/bitnamicharts/postgresql
