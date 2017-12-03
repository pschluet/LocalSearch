#!/bin/bash

# Change to the directory of this script
cd "$(dirname "$0")"

java -jar Main.jar -alg LS2 -inst ../Data/email.graph -seed 234234 -time 600
