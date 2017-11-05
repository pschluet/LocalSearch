package com.ps;

import com.ps.algorithms.Algorithm;
import com.ps.algorithms.LocalSearchAlgorithm1;
import com.ps.algorithms.LocalSearchAlgorithm2;
import com.ps.algorithms.Solution;
import com.ps.enums.AlgorithmType;
import com.ps.fileIO.Utils;
import org.jgrapht.UndirectedGraph;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        // Parse input arguments
        InputArgs inputArgs = new InputArgs(args);

        // Read input data
        UndirectedGraph graph = Utils.readDataFile("../Data/" + inputArgs.fileName);

        // Run one of the algorithms depending on the command line arguments supplied
        Algorithm algorithm;
        if (inputArgs.getAlgorithmType() == AlgorithmType.LocalSearch1)
            algorithm = new LocalSearchAlgorithm1(graph, inputArgs);
        else
            algorithm = new LocalSearchAlgorithm2(graph, inputArgs);

        Solution solution = algorithm.run();

        // Write the output files
        Utils.writeSolutionFile(solution, "../output/" + inputArgs.getFileNameBase() + ".sol");
        Utils.writeTraceFile(solution, "../output/" + inputArgs.getFileNameBase() + ".trace");
    }
}
