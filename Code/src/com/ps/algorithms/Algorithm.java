package com.ps.algorithms;

/*
This is a base class for the algorithm classes. Common code resides here, and the specific algorithm implementations
(LocalSearchAlgorithm1 and LocalSearchAlgorithm2) extend this class.
 */

import com.ps.InputArgs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.interfaces.MinimumVertexCoverAlgorithm;
import org.jgrapht.alg.vertexcover.GreedyVCImpl;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public abstract class Algorithm {

    protected UndirectedGraph graph;
    protected int cutoffTimeSec;
    protected Random randomGenerator;

    public Algorithm(InputArgs args) {
        this.cutoffTimeSec = args.getCutoffTimeSec();
        // Set random seed
        this.randomGenerator = new Random(args.getRandomSeed());
    }

    public Solution run(final UndirectedGraph graph) {
        Solution soln = new Solution();
        soln.startTiming();

        // Get initial vertex cover
        Set<Integer> vertexCoverCandidate = getInitialVertexCover(graph);

        while (soln.getElapsedTimeSec() < cutoffTimeSec) {
            if (isVertexCover(vertexCoverCandidate, graph)) {

                // Add point to the solution trace if the quality is better
                int currentQuality = getQuality(vertexCoverCandidate, graph);
                if (currentQuality < soln.getBestQualityAchieved()) {
                    soln.addTracePoint(currentQuality);

                    soln.setVertexCoverNodes(new HashSet<>(vertexCoverCandidate));
                }

                // Remove certain number of vertices and continue the search (see child classes for implementation)
                removeVertices(vertexCoverCandidate);
            }

            // Select some vertices to remove from the candidate solution (see child classes for implementation)
            Set<Integer> exitingVertices = selectExitingVertices(vertexCoverCandidate, graph);
            vertexCoverCandidate.removeAll(exitingVertices);

            // Select some vertices to add to the candidate solution (see child classes for implementation)
            Set<Integer> enteringVertices = selectEnteringVertices(vertexCoverCandidate, graph);
            vertexCoverCandidate.addAll(enteringVertices);
        }

        return soln;
    }

    protected Set<Integer> getInitialVertexCover(final UndirectedGraph graph) {
        // TODO: Implement this method myself
        MinimumVertexCoverAlgorithm<Integer, DefaultEdge> mvc = new GreedyVCImpl<>();
        MinimumVertexCoverAlgorithm.VertexCover<Integer> vertexCover = mvc.getVertexCover(graph);
        return new HashSet<Integer>(vertexCover.getVertices());
    }

    protected boolean isVertexCover(final Set<Integer> solutionToCheck, final UndirectedGraph graph) {

        // Iterate over all edges
        for (Object edge : graph.edgeSet()) {
            // Get source (s) and target (t) of the current edge
            int s = (int)graph.getEdgeSource(edge);
            int t = (int)graph.getEdgeTarget(edge);

            // Check to make sure the solution candidate contains the source or target vertex of this edge
            if (!solutionToCheck.contains(s) && !solutionToCheck.contains(t)) {
                return false;
            }
        }

        return true;
    }

    protected void removeNumberOfVertices(Set<Integer> inputVertices, int numberOfVerticesToRemove) {
        // Remove the specified number of vertices from the set
        int numRemoved = 0;
        Iterator<Integer> iter = inputVertices.iterator();
        while (iter.hasNext() && numRemoved < numberOfVerticesToRemove) {
            iter.next();
            iter.remove();
            numRemoved++;
        }
    }

    protected int getQuality(final Set<Integer> vertexCoverCandidate, final UndirectedGraph graph) {
        return vertexCoverCandidate.size();
    }

    protected abstract Set<Integer> selectExitingVertices(final Set<Integer> vertexCoverCandidate, final UndirectedGraph graph);
    protected abstract Set<Integer> selectEnteringVertices(final Set<Integer> vertexCoverCandidate, final UndirectedGraph graph);
    protected abstract void removeVertices(Set<Integer> inputVertices);



}
