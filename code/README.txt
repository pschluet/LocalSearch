===============================================================================
Code Structure
===============================================================================
- This is a Java Maven project, so it has a Maven project folder structure
- The following shows the structure of the source code and what each file is
  used for:
-src/main/java/com/ps
	- algorithms: contains classes for the algorithm logic
		- Algorithm.java: base class for both local search algorithms
		- LocalSearchAlgorithm1.java: class containing logic specific to the
			first local search algorithm (inherits from Algorithm.java)
		- LocalSearchAlgorithm2.java: class containing logic specific to the
			second local search algorithm (inherits from Algorithm.java)
	- datacontainers: contains data structure type classes for holding data
		- Solution.java: holds data representing a vertex cover solution; also
			handles timing for the trace files
		- TracePoint.java: holds a single trace point for the trace file
			output (i.e. a (time, quality) pair)
		- Vertex.java: class for representing the vertices of a graph
	- enums
		- AlgorithmType.java: enum for representing algorithm type (i.e. LS1
			or LS2)
	- fileIO
		- Utils.java: class for reading input data and writing output files
	- InputArgs.java: class for parsing the command line parameters
	- Main.java: main entry point for the application

===============================================================================
Execution Instructions
===============================================================================
- Ensure Java 8 runtime is installed
- Main-1.0-SNAPSHOT.jar is the executable .jar file
- Change to the directory that contains Main-1.0-SNAPSHOT.jar
- The following shows an example of running via command line:

	java -jar Main-1.0-SNAPSHOT.jar -alg LS2 -inst ../Data/email.graph -seed 234234 -time 600

- The above example takes a path to the data file for the -inst argument, and
  output files are written to the same folder as the executable .jar.

===============================================================================
Build Instructions
===============================================================================
- Install the latest version of Java Development Kit (JDK) 8
- Install the latest version of Apache Maven (3.5.0)
- Change to the directory that contains the pom.xml file
- Run the following Maven command to build an executable .jar called 
  Main-1.0-SNAPSHOT.jar. It is created in the same folder as the pom.xml file.

	mvn clean compile package
