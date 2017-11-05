package com.ps.algorithms;

/*
This class contains all the data components that are necessary for writing the output files. It is populated
as the algorithms run.
 */

import java.util.ArrayList;
import java.util.List;

public class Solution {
    protected List<Integer> vertexCoverNodes;
    protected List<TracePoint> tracePoints;
    protected long startTimeNs;

    public Solution() {
        vertexCoverNodes = new ArrayList<Integer>();
        tracePoints = new ArrayList<TracePoint>();
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

    public int getBestQualityAchieved() {
        return tracePoints.get(tracePoints.size() - 1).quality;
    }

    public void startTiming() {
        startTimeNs = System.nanoTime();
    }
}
