package com.ps.algorithms;

/*
This file contains the implementation of the second local search algorithm.
 */

import com.ps.InputArgs;
import com.ps.datacontainers.Vertex;
import org.jgrapht.UndirectedGraph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class LocalSearchAlgorithm2 extends Algorithm {

    public LocalSearchAlgorithm2(InputArgs args) {
        super(args);
    }

    @Override
    protected void removeVertices(Set<Vertex> inputVertices) {
        int numVerticesToRemove = 1;
        removeNumberOfVertices(inputVertices, numVerticesToRemove);
    }

    @Override
    protected Set<Vertex> selectEnteringVertices(Set<Vertex> vertexCoverCandidate, UndirectedGraph graph) {
        // TODO: Implement this method (and actually make this random)
        Set<Vertex> vertices = graph.vertexSet();

        for (Vertex v : vertices) {
            if (!vertexCoverCandidate.contains(v.getId())) {
                HashSet<Vertex> s = new HashSet<>();
                s.add(v);
                return s;
            }
        }

        return null;
    }

    @Override
    protected Set<Vertex> selectExitingVertices(Set<Vertex> vertexCoverCandidate, UndirectedGraph graph) {
        // TODO: Implement this method

        Iterator<Vertex> iter = vertexCoverCandidate.iterator();
        HashSet<Vertex> v = new HashSet<>();
        v.add(iter.next());

        return v;
    }
}
