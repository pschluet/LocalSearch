package com.ps.algorithms;

/*
This file contains the implementation of the first local search algorithm.
 */

import com.ps.InputArgs;
import com.ps.datacontainers.Vertex;
import org.jgrapht.UndirectedGraph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class LocalSearchAlgorithm1 extends Algorithm {

    public LocalSearchAlgorithm1(InputArgs args) {
        super(args);
    }

    @Override
    protected void removeVertices(Set<Vertex> inputVertices) {
        int numVerticesToRemove = 1;
        removeNumberOfVertices(inputVertices, numVerticesToRemove);
    }

    @Override
    protected Set<Vertex> selectEnteringVertices(Set<Vertex> vertexCoverCandidate, UndirectedGraph graph) {
        // TODO: Implement this method (choose random uncovered edge & pick vertex with the higher dscore)

        Iterator<Vertex> iter = graph.vertexSet().iterator();
        HashSet<Vertex> vSet = new HashSet<>();
        while (iter.hasNext() && vSet.size() < getNumberOfVerticesToSwap()) {
            Vertex vertex = iter.next();
            if (!vertexCoverCandidate.contains(vertex)) {
                vSet.add(vertex);
            }
        }

        return vSet;
    }

    @Override
    protected Set<Vertex> selectExitingVertices(Set<Vertex> vertexCoverCandidate, UndirectedGraph graph) {
        // Choose vertex/vertices with the highest scores
        return getVerticesWithHighestScores(vertexCoverCandidate, getNumberOfVerticesToSwap());
    }

    @Override
    protected int getNumberOfVerticesToSwap() {
        return 1;
    }
}
