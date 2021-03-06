package com.ps.algorithms;

/*
This file contains the implementation of the second local search algorithm.
 */

import com.ps.InputArgs;
import com.ps.datacontainers.Vertex;
import java.util.Set;

public class LocalSearchAlgorithm2 extends Algorithm {

    public LocalSearchAlgorithm2(InputArgs args) {
        super(args);
    }

    @Override
    protected void removeVertices(Set<Vertex> inputVertices) {
        int numVerticesToRemove = Math.max((int)(inputVertices.size() * 0.01), 1);
        //System.out.println(String.format("%d, %d", inputVertices.size(), numVerticesToRemove));
        removeNumberOfVertices(inputVertices, numVerticesToRemove);
    }

    @Override
    protected int getNumberOfVerticesToSwap() {
        return 1;
    }
}
