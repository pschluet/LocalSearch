#!/usr/bin/env bash

# Arguments to run with
args="-inst netscience.graph -alg LS1 -time 10 -seed 8"

cd ./Code

# Java class path
classPath=src:lib/commons-cli-1.4.jar:lib/jgrapht-core-1.0.1.jar

# Compile
javac -cp $classPath $(find ./src/ -name "*.java")

# Run
java -cp $classPath com.ps.Main $args

# Remove compiled .class files
rm $(find ./src/ -name "*.class")

cd ..
