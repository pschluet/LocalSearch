package com.ps.algorithms;

import com.ps.InputArgs;
import org.jgrapht.UndirectedGraph;

import java.util.ArrayList;
import java.util.Arrays;

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
        soln.addTracePoint(100);
        soln.addTracePoint(70);
        soln.addTracePoint(51);
        soln.setVertexCoverNodes(new ArrayList<Integer>(Arrays.asList(1, 3, 5, 7, 8, 9, 10)));

        return soln;
    };
}
