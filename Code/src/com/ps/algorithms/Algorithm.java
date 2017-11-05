package com.ps.algorithms;

import com.ps.InputArgs;
import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Algorithm {

    protected UndirectedGraph graph;
    protected int randomSeed;
    protected int cutoffTimeSec;

    public Algorithm(UndirectedGraph graph, InputArgs args) {
        this.graph = graph;
        this.randomSeed = args.getRandomSeed();
        this.cutoffTimeSec = args.getCutoffTimeSec();

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
