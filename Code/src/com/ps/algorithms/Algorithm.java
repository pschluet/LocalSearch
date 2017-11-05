package com.ps.algorithms;

/*
This is a base class for the algorithm classes. Common code resides here, and the specific algorithm implementations
(LocalSearchAlgorithm1 and LocalSearchAlgorithm2) extend this class.
 */

import com.ps.InputArgs;
import org.jgrapht.UndirectedGraph;

import java.util.*;

public abstract class Algorithm {

    protected UndirectedGraph graph;
    protected int cutoffTimeSec;
    protected Random randomGenerator;

    public Algorithm(UndirectedGraph graph, InputArgs args) {
        this.graph = graph;
        this.cutoffTimeSec = args.getCutoffTimeSec();
        // Set random seed
        this.randomGenerator = new Random(args.getRandomSeed());

    }

    public Solution run() {
        Solution soln = new Solution();
        soln.startTiming();

        try {
            Thread.sleep(500);
            soln.addTracePoint(100);

            Thread.sleep(500);
            soln.addTracePoint(70);

            Thread.sleep(500);
            soln.addTracePoint(51);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        soln.setVertexCoverNodes(graph.vertexSet());

        return soln;
    };
}
