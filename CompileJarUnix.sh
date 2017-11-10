cd ./Code/src

# Java class path
classPath=../lib/commons-cli-1.4.jar:../lib/jgrapht-core-1.0.1.jar

# Compile
javac -cp $classPath $(find ./ -name "*.java")

# Create .jar file
jar cfe Main.jar com.ps.Main $(find ./ -name "*.class")

# Remove .class files
rm $(find ./ -name "*.class")

# Move the jar file
mv Main.jar ../Main.jar

cd ../..

# Make directory for output solution files
mkdir output