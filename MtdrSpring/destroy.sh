#!/bin/bash
# Copyright (c) 2022 Oracle and/or its affiliates.
# Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.

# Make sure this is run via source or .

if ! (return 0 2>/dev/null); then
  echo "ERROR: Usage 'source destroy.sh'"
  exit
fi

$MTDRWORKSHOP_LOCATION/utils/main-destroy.sh

deleteDir=toDelete_$(date +%Y%m%d_%H%M%S)
mkdir $deleteDir
mv state $deleteDir
mv tls $deleteDir
mv wallet $deleteDir
mv log $deleteDir
