package com.ps.fileIO;

import com.ps.InputArgs;
import com.ps.algorithms.Solution;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Utils {
    public static UndirectedGraph readDataFile(String filePath) {
        return new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);
    }

    public static void writeSolutionFile(Solution solution, InputArgs args) {

    }

    public static void writeTraceFile(Solution solution, InputArgs args) {

    }
}
