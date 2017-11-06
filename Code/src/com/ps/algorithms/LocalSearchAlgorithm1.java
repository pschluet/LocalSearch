package com.ps.algorithms;

/*
This file contains the implementation of the first local search algorithm.
 */

import com.ps.InputArgs;
import org.jgrapht.UndirectedGraph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class LocalSearchAlgorithm1 extends Algorithm {

    public LocalSearchAlgorithm1(InputArgs args) {
        super(args);
    }

    @Override
    protected void removeVertices(Set<Integer> inputVertices) {
        int numVerticesToRemove = 1;
        removeNumberOfVertices(inputVertices, numVerticesToRemove);
    }

    @Override
    protected Set<Integer> selectEnteringVertices(Set<Integer> vertexCoverCandidate, UndirectedGraph graph) {
        // TODO: Implement this method
        Set<Integer> vertices = graph.vertexSet();

        for (Integer v : vertices) {
            if (!vertexCoverCandidate.contains(v)) {
                return new HashSet<Integer>(v);
            }
        }

        return null;
    }

    @Override
    protected Set<Integer> selectExitingVertices(Set<Integer> vertexCoverCandidate, UndirectedGraph graph) {
        // TODO: Implement this method

        Iterator<Integer> iter = vertexCoverCandidate.iterator();

        return new HashSet<Integer>(iter.next());
    }
}
