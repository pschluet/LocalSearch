package com.ps.datacontainers;

/*
This is used to capture quality vs. time results as the algorithm runs.
 */

public class TracePoint {
    protected double timeSec;
    protected int quality;

    public TracePoint(double timeSec, int quality) {
        this.timeSec = timeSec;
        this.quality = quality;
    }

    public double getTimeSec() {
        return timeSec;
    }

    public int getQuality() {
        return quality;
    }
}
