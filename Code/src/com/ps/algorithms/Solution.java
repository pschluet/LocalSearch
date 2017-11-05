package com.ps.algorithms;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    protected List<Integer> vertexCoverNodes;
    protected List<TracePoint> tracePoints;
    protected final long startTimeNs;

    public Solution() {
        vertexCoverNodes = new ArrayList<Integer>();
        tracePoints = new ArrayList<TracePoint>();
        startTimeNs = System.nanoTime(); // Start timing when object is instantiated
    }

    public List<Integer> getVertexCoverNodes() {
        return vertexCoverNodes;
    }

    public void setVertexCoverNodes(List<Integer> vertexCoverNodes) {
        this.vertexCoverNodes = vertexCoverNodes;
    }

    public void addTracePoint(int quality) {
        long currentTimeNs = System.nanoTime();
        double elapsedTimeSec = (double)(currentTimeNs - startTimeNs) * 1E-9;
        tracePoints.add(new TracePoint(elapsedTimeSec, quality));
    }

    public List<TracePoint> getTracePoints() {
        return tracePoints;
    }
}
