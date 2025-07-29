#!/usr/bin/env bash

set -e

export KAFKA_DIR=/opt/kafka_2.13-3.5.0/bin
export BOOTSTRAP_SERVER="$(minikube ip):9094"

file=$@
if [[ -z $file ]]
then
  echo 'The input file is not provided'
  exit 1
fi

echo $file

${KAFKA_DIR}/kafka-console-producer.sh \
  --bootstrap-server $BOOTSTRAP_SERVER \
  --topic stock-market < $file
