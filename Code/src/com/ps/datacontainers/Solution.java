package com.ps.datacontainers;

/*
This class contains all the data components that are necessary for writing the output files. It is populated
as the algorithms run. It also handles keeping track of algorithm running times.
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Solution {
    protected Set<Integer> vertexCoverNodes;
    protected List<TracePoint> tracePoints;
    protected long startTimeNs;

    public Solution() {
        vertexCoverNodes = new HashSet<Integer>();
        tracePoints = new ArrayList<TracePoint>();
    }

    public Set<Integer> getVertexCoverNodes() {
        return vertexCoverNodes;
    }

    public void setVertexCoverNodes(Set<Integer> vertexCoverNodes) {
        this.vertexCoverNodes = vertexCoverNodes;
    }

    public void addTracePoint(int quality) {
        tracePoints.add(new TracePoint(getElapsedTimeSec(), quality));
    }

    public List<TracePoint> getTracePoints() {
        return tracePoints;
    }

    public int getBestQualityAchieved() {
        int bestQuality;

        if (tracePoints.size() == 0) {
            bestQuality = Integer.MAX_VALUE; // Higher numbers are worse
        } else {
            bestQuality = tracePoints.get(tracePoints.size() - 1).quality;
        }

        return bestQuality;
    }

    public void startTiming() {
        startTimeNs = System.nanoTime();
    }

    public double getElapsedTimeSec() {
        long currentTimeNs = System.nanoTime();
        return (double)(currentTimeNs - startTimeNs) * 1E-9;
    }
}
