#!/bin/bash

# Change to the directory of this script
cd "$(dirname "$0")"

mvn clean
mvn compile
mvn package
mkdir output