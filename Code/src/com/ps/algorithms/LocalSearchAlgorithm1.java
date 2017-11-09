package com.ps.algorithms;

/*
This file contains the implementation of the first local search algorithm.
 */

import com.ps.InputArgs;
import com.ps.datacontainers.Vertex;

import java.util.*;

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
    protected int getNumberOfVerticesToSwap() {
        return 1;
    }
}
