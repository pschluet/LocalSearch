cd ./Code/src

# Java class path
classPath=../lib/commons-cli-1.4.jar:../lib/jgrapht-core-1.0.1.jar

# Compile
javac -cp $classPath $(find ./ -name "*.java")

# Create manifest file
#printf "Main-Class: com.ps.Main\nClass-Path: ./Code/lib/commons-cli-1.4.jar ./Code/lib/jgrapht-core-1.0.1.jar" > manifest.txt

# Create .jar file
#jar cmvf manifest.txt Main.jar $(find ./ -name "*.class")
jar cfe Main.jar com.ps.Main $(find ./ -name "*.class")

# Remove .class files
rm $(find ./ -name "*.class")
#rm manifest.txt

# Move the jar file
mv Main.jar ../Main.jar

# Then run with java -cp all:jar:paths com.ps.Main args...

cd ../..

mkdir output

#From the directory containing the com folder:
#jar cvfe Main.jar com.ps.Main com

