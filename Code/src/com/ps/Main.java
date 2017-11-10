package com.ps;

/*
This file is the main entry point to the code and contains the high-level program flow.
 */

import com.ps.algorithms.Algorithm;
import com.ps.algorithms.LocalSearchAlgorithm1;
import com.ps.algorithms.LocalSearchAlgorithm2;
import com.ps.datacontainers.Solution;
import com.ps.datacontainers.Vertex;
import com.ps.enums.AlgorithmType;
import com.ps.fileIO.Utils;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        // Parse input arguments
        InputArgs inputArgs = new InputArgs(args);

        // Read input data
        UndirectedGraph<Vertex,DefaultEdge> graph = Utils.readDataFile("../Data/" + inputArgs.fileName);

        // Run one of the algorithms depending on the command line arguments supplied
        Algorithm algorithm;
        if (inputArgs.getAlgorithmType() == AlgorithmType.LocalSearch1)
            algorithm = new LocalSearchAlgorithm1(inputArgs);
        else
            algorithm = new LocalSearchAlgorithm2(inputArgs);

        Solution solution = algorithm.run(graph);

        // Write the output files
        Utils.writeSolutionFile(solution, "../output/" + inputArgs.getFileNameBase() + ".sol");
        Utils.writeTraceFile(solution, "../output/" + inputArgs.getFileNameBase() + ".trace");
    }
}
