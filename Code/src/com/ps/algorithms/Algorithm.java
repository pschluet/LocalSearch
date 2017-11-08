package com.ps.algorithms;

/*
This is a base class for the algorithm classes. Common code resides here, and the specific algorithm implementations
(LocalSearchAlgorithm1 and LocalSearchAlgorithm2) extend this class.
 */

import com.ps.InputArgs;
import com.ps.datacontainers.Solution;
import com.ps.datacontainers.Vertex;
import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

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
        Set<Vertex> vertexCoverCandidate = getInitialVertexCover(graph);

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
            Set<Vertex> exitingVertices = selectExitingVertices(vertexCoverCandidate, graph);
            vertexCoverCandidate.removeAll(exitingVertices);

            // Select some vertices to add to the candidate solution (see child classes for implementation)
            Set<Vertex> enteringVertices = selectEnteringVertices(vertexCoverCandidate, graph);
            vertexCoverCandidate.addAll(enteringVertices);
        }

        return soln;
    }

    protected Set<Vertex> getInitialVertexCover(final UndirectedGraph graph) {
        // This implements the APPROX-VERTEX-COVER algorithm from page 1109 of
        // the CLRS Algorithms textbook (3rd Edition)

        // Make a copy of the input graph
        UndirectedGraph<Vertex, DefaultEdge> changingGraph = new SimpleGraph<Vertex, DefaultEdge>(DefaultEdge.class);
        Graphs.addGraph(changingGraph, graph);

        Set<Vertex> vertexCover = new HashSet<>();

        while (changingGraph.edgeSet().size() > 0) {
            // Pick random edge in remaining edge set
            DefaultEdge nextEdge = changingGraph.edgeSet().iterator().next();

            // Get source (u) and target (v) of the current edge
            Vertex u = changingGraph.getEdgeSource(nextEdge);
            Vertex v = changingGraph.getEdgeTarget(nextEdge);

            // Add both vertices to the vertex cover
            vertexCover.add(u);
            vertexCover.add(v);

            // Remove every edge connected to u and v
            Set<DefaultEdge> edgesConnectedToU = new HashSet<>(changingGraph.edgesOf(u));
            Set<DefaultEdge> edgesConnectedToV = new HashSet<>(changingGraph.edgesOf(v));
            changingGraph.removeAllEdges(edgesConnectedToU);
            changingGraph.removeAllEdges(edgesConnectedToV);
        }

        return vertexCover;
    }

    protected boolean isVertexCover(final Set<Vertex> solutionToCheck, final UndirectedGraph graph) {

        // Iterate over all edges
        for (Object edge : graph.edgeSet()) {
            // Get source (s) and target (t) of the current edge
            Vertex s = (Vertex)graph.getEdgeSource(edge);
            Vertex t = (Vertex)graph.getEdgeTarget(edge);

            // Check to make sure the solution candidate contains the source or target vertex of this edge
            if (!solutionToCheck.contains(s) && !solutionToCheck.contains(t)) {
                return false;
            }
        }

        return true;
    }

    protected void removeNumberOfVertices(Set<Vertex> inputVertices, int numberOfVerticesToRemove) {
        // TODO: Actually make this random
        // Remove the specified number of vertices from the set
        int numRemoved = 0;
        Iterator<Vertex> iter = inputVertices.iterator();
        while (iter.hasNext() && numRemoved < numberOfVerticesToRemove) {
            iter.next();
            iter.remove();
            numRemoved++;
        }
    }

    protected int getQuality(final Set<Vertex> vertexCoverCandidate, final UndirectedGraph graph) {

        return vertexCoverCandidate.size();

    }

    protected int getNumberOfUncoveredEdges(final Set<Vertex> vertexCoverCandidate, final UndirectedGraph graph) {

        int numUncoveredEdges = 0;

        // Iterate over all edges
        for (Object edge : graph.edgeSet()) {
            // Get source (s) and target (t) of the current edge
            Vertex s = (Vertex)graph.getEdgeSource(edge);
            Vertex t = (Vertex)graph.getEdgeTarget(edge);

            // Check to make sure the solution candidate contains the source or target vertex of this edge
            if (!vertexCoverCandidate.contains(s) && !vertexCoverCandidate.contains(t)) {
                numUncoveredEdges++;
            }
        }

        return numUncoveredEdges;
    }

    protected int getCost(final Set<Vertex> vertexCoverCandidate, final UndirectedGraph graph) {
        return getNumberOfUncoveredEdges(vertexCoverCandidate, graph);
    }

    protected abstract Set<Vertex> selectExitingVertices(final Set<Vertex> vertexCoverCandidate, final UndirectedGraph graph);
    protected abstract Set<Vertex> selectEnteringVertices(final Set<Vertex> vertexCoverCandidate, final UndirectedGraph graph);
    protected abstract void removeVertices(Set<Vertex> inputVertices);



}
