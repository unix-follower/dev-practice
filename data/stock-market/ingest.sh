#!/usr/bin/env bash

set -e

export KAFKA_DIR=/opt/kafka_2.13-3.5.0/bin
export BOOTSTRAP_SERVER="$(minikube ip):9094"

send() {
  files=$@
  for file in $files
    do
      echo $file
      ${KAFKA_DIR}/kafka-console-producer.sh \
      --bootstrap-server $BOOTSTRAP_SERVER \
      --topic stock-market < $file
    done
}

data_2024_may=$(
  ls output/AAL/2024-05-*
  ls output/AAPL/2024-05-*
  ls output/ABNB/2024-05-*
  ls output/ACN/2024-05-*
  ls output/ADBE/2024-05-*
  ls output/AMD/2024-05-*
  ls output/AMZN/2024-05-*
  ls output/AVGO/2024-05-*
  ls output/BA/2024-05-*
  ls output/BND/2024-05-*
  ls output/BOTZ/2024-05-*
  ls output/CRM/2024-05-*
  ls output/CSCO/2024-05-*
  ls output/DIS/2024-05-*
  ls output/DPZ/2024-05-*
  ls output/EA/2024-05-*
  ls output/EBAY/2024-05-*
  ls output/EPAM/2024-05-*
  ls output/EXPE/2024-05-*
  ls output/F/2024-05-*
  ls output/GM/2024-05-*
  ls output/GOOGL/2024-05-*
  ls output/GS/2024-05-*
  ls output/HPE/2024-05-*
  ls output/IBM/2024-05-*
  ls output/INTC/2024-05-*
  ls output/IT/2024-05-*
  ls output/KO/2024-05-*
  ls output/MA/2024-05-*
  ls output/MCD/2024-05-*
  ls output/MCK/2024-05-*
  ls output/META/2024-05-*
  ls output/METV/2024-05-*
  ls output/MSFT/2024-05-*
  ls output/NFLX/2024-05-*
  ls output/NKE/2024-05-*
  ls output/NVDA/2024-05-*
  ls output/ORCL/2024-05-*
  ls output/PANW/2024-05-*
  ls output/PEP/2024-05-*
  ls output/PG/2024-05-*
  ls output/PYPL/2024-05-*
  ls output/QCOM/2024-05-*
  ls output/QQQ/2024-05-*
  ls output/ROBT/2024-05-*
  ls output/SBUX/2024-05-*
  ls output/SMH/2024-05-*
  ls output/SPY/2024-05-*
  ls output/T/2024-05-*
  ls output/TECB/2024-05-*
  ls output/TSLA/2024-05-*
  ls output/V/2024-05-*
  ls output/VBTLX/2024-05-*
  ls output/VFIAX/2024-05-*
  ls output/VGT/2024-05-*
  ls output/VIGI/2024-05-*
  ls output/VOO/2024-05-*
  ls output/VTI/2024-05-*
  ls output/VTIAX/2024-05-*
  ls output/VTSAX/2024-05-*
  ls output/VXUS/2024-05-*
  ls output/WBD/2024-05-*
  ls output/WMT/2024-05-*
)

send $data_2024_may
