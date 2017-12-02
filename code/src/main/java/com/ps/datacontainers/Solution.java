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
    protected Set<Vertex> vertexCoverNodes;
    protected List<TracePoint> tracePoints;
    protected long startTimeNs = 0;
    protected long lastTracePointAddTimeNs = 0;

    public Solution() {
        vertexCoverNodes = new HashSet<Vertex>();
        tracePoints = new ArrayList<TracePoint>();
    }

    public Set<Vertex> getVertexCoverNodes() {
        return vertexCoverNodes;
    }

    public void setVertexCoverNodes(Set<Vertex> vertexCoverNodes) {
        this.vertexCoverNodes = vertexCoverNodes;
    }

    public void addTracePoint(int quality) {
        TracePoint tp = new TracePoint(getElapsedTimeSec(), quality);
        //System.out.println(tp);
        tracePoints.add(tp);

        lastTracePointAddTimeNs = System.nanoTime();
    }

    public double getTimeSinceLastTracePointAddedSec() {
        if (lastTracePointAddTimeNs == 0)
            return 0;
        else
            return getElapsedTimeSec(lastTracePointAddTimeNs);
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
        return getElapsedTimeSec(startTimeNs);
    }

    private static double getElapsedTimeSec(long startTimeNanoSec) {
        long currentTimeNs = System.nanoTime();
        return (double)(currentTimeNs - startTimeNanoSec) * 1E-9;
    }
}
