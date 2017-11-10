#!/usr/bin/env bash

cd Code

# Arguments to run with
args="-inst netscience.graph -alg LS1 -time 10 -seed 8"

# Java class path
classPath=Main.jar:lib/commons-cli-1.4.jar:lib/jgrapht-core-1.0.1.jar

# Run
java -cp $classPath com.ps.Main $args

cd ..