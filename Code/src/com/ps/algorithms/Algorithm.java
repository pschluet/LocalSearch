package com.ps.algorithms;

import com.ps.InputArgs;
import org.jgrapht.UndirectedGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        List<Integer> vertices = new ArrayList<Integer>();
        vertices.addAll(graph.vertexSet());
        soln.setVertexCoverNodes(vertices);

        return soln;
    };
}
