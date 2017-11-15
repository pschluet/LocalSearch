package com.ps.algorithms;

/*
This is a base class for the algorithm classes. Common code resides here, and the specific algorithm implementations
(LocalSearchAlgorithm1 and LocalSearchAlgorithm2) extend this class.
 */

import com.ps.InputArgs;
import com.ps.datacontainers.Solution;
import com.ps.datacontainers.Vertex;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public abstract class Algorithm {

    protected UndirectedGraph<Vertex,DefaultEdge> graph;
    protected int cutoffTimeSec;
    protected Random randomGenerator;
    // If the algorithm has not found a new vertex cover in this many seconds, it returns
    protected static final int STAGNANT_TIME_CUTOFF_SEC = 10;

    public Algorithm(InputArgs args) {
        this.cutoffTimeSec = args.getCutoffTimeSec();
        // Set random seed
        this.randomGenerator = new Random(args.getRandomSeed());
    }

    protected abstract void removeVertices(Set<Vertex> inputVertices);
    protected abstract int getNumberOfVerticesToSwap();

    public Solution run(final UndirectedGraph<Vertex,DefaultEdge> graph) {
        this.graph = graph;

        Solution soln = new Solution();
        soln.startTiming();

        // Get initial vertex cover
        Set<Vertex> vertexCoverCandidate = getInitialVertexCover();
        soln.addTracePoint(getQuality(vertexCoverCandidate));
        soln.setVertexCoverNodes(new HashSet<>(vertexCoverCandidate));

        while (soln.getElapsedTimeSec() < cutoffTimeSec && soln.getTimeSinceLastTracePointAddedSec() < STAGNANT_TIME_CUTOFF_SEC) {
            if (isVertexCover(vertexCoverCandidate)) {

                // Add point to the solution trace if the quality is better
                int currentQuality = getQuality(vertexCoverCandidate);
                if (currentQuality < soln.getBestQualityAchieved()) {
                    soln.addTracePoint(currentQuality);

                    soln.setVertexCoverNodes(new HashSet<>(vertexCoverCandidate));
                }

                // Remove certain number of vertices and continue the search
                removeVertices(vertexCoverCandidate);
            }

            // Select some vertices to remove from the candidate solution
            Set<Vertex> exitingVertices = selectExitingVertices(vertexCoverCandidate);
            vertexCoverCandidate.removeAll(exitingVertices);

            // Select some vertices to add to the candidate solution
            Set<Vertex> enteringVertices = selectEnteringVertices(vertexCoverCandidate);
            vertexCoverCandidate.addAll(enteringVertices);
        }

        return soln;
    }

    protected Set<Vertex> getInitialVertexCover() {
        // This implements the APPROX-VERTEX-COVER algorithm from page 1109 of
        // the CLRS Algorithms textbook (3rd Edition)

        Set<DefaultEdge> edgesRemaining = new HashSet<>(graph.edgeSet());

        Set<Vertex> vertexCover = new HashSet<>();

        while (edgesRemaining.size() > 0) {
            // Pick random edge in remaining edge set
            Object[] edges = edgesRemaining.toArray();
            int ndx = randomGenerator.nextInt(edges.length);
            DefaultEdge nextEdge = (DefaultEdge)edges[ndx];

            // Get source (u) and target (v) of the current edge
            Vertex u = graph.getEdgeSource(nextEdge);
            Vertex v = graph.getEdgeTarget(nextEdge);

            // Add both vertices to the vertex cover
            vertexCover.add(u);
            vertexCover.add(v);

            // Remove every edge connected to u and v
            edgesRemaining.removeAll(graph.edgesOf(u));
            edgesRemaining.removeAll(graph.edgesOf(v));
        }

        return vertexCover;
    }

    protected boolean isVertexCover(final Set<Vertex> solutionToCheck) {

        // Iterate over all edges
        for (DefaultEdge edge : graph.edgeSet()) {
            // Get source (s) and target (t) of the current edge
            Vertex s = graph.getEdgeSource(edge);
            Vertex t = graph.getEdgeTarget(edge);

            // Check to make sure the solution candidate contains the source or target vertex of this edge
            if (!solutionToCheck.contains(s) && !solutionToCheck.contains(t)) {
                return false;
            }
        }

        return true;
    }

    protected void removeNumberOfVertices(Set<Vertex> inputVertices, int numberOfVerticesToRemove) {
        // Remove the specified number of vertices from the set
        inputVertices.removeAll(getVerticesWithLowestScores(inputVertices, numberOfVerticesToRemove));
    }

    protected int getQuality(final Set<Vertex> vertexCoverCandidate) {

        return vertexCoverCandidate.size();

    }

    protected int getCostDelta(final Set<Vertex> vertexCoverCandidate, Vertex vertexToRemove) {
        int costDelta = 0;

        // Get vertices connected to the vertex I want to remove
        Set<DefaultEdge> edges = graph.edgesOf(vertexToRemove);
        for (DefaultEdge edge : edges) {
            // Get source (s) and target (t) of the current edge
            Vertex s = graph.getEdgeSource(edge);
            Vertex t = graph.getEdgeTarget(edge);

            Vertex otherEdge = s == vertexToRemove ? t : s;

            // If the other vertex is not in the vertex cover, removing this vertex would uncover an edge, so the cost
            // (number of uncovered edges) would go up by one
            if (!vertexCoverCandidate.contains(otherEdge)) {
                costDelta += 1;
            }
        }

        return costDelta;
    }

    protected void updateScores(Set<Vertex> vertexCoverCandidate) {

        for (Vertex v : vertexCoverCandidate) {

            int costDelta = getCostDelta(vertexCoverCandidate, v);

            v.setScore(costDelta);
        }
    }

    protected Set<Vertex> getVerticesWithLowestScores(final Set<Vertex> vertexCoverCandidate, int numVertices) {
        updateScores(vertexCoverCandidate);

        // Sort by score
        List<Vertex> sortedVertices = new ArrayList<>(vertexCoverCandidate);
        sortedVertices.sort(Comparator.comparingInt(Vertex::getScore));

        // Get the vertices with the highest scores
        Set<Vertex> highScoreVertices = new HashSet<>();
        highScoreVertices.addAll(sortedVertices.subList(0, numVertices));

        return highScoreVertices;
    }

    protected Set<Vertex> selectEnteringVertices(final Set<Vertex> vertexCoverCandidate) {

        Set<Vertex> enteringVertices = new HashSet<>();

        for (int i = 0; i < getNumberOfVerticesToSwap(); i++) {
            DefaultEdge uncoveredEdge = getRandomUncoveredEdge(vertexCoverCandidate);
            Vertex vertexToAdd;
            if (uncoveredEdge == null) {
                vertexToAdd = getRandomVertexNotInCover(vertexCoverCandidate);
            } else {
                // Select a random vertex from this edge
                vertexToAdd = randomGenerator.nextBoolean() ? graph.getEdgeSource(uncoveredEdge) : graph.getEdgeTarget(uncoveredEdge);
            }
            vertexCoverCandidate.add(vertexToAdd);
            enteringVertices.add(vertexToAdd);
        }

        vertexCoverCandidate.removeAll(enteringVertices);

        return enteringVertices;
    }

    protected DefaultEdge getRandomUncoveredEdge(final Set<Vertex> vertexCoverCandidate) {

        Set<DefaultEdge> uncoveredEdges = getUncoveredEdges(vertexCoverCandidate);

        if (uncoveredEdges.size() == 0)
            return null;

        Object[] edges = uncoveredEdges.toArray();

        // Get random index
        int ndx = randomGenerator.nextInt(edges.length);

        return (DefaultEdge)edges[ndx];
    }

    protected Set<DefaultEdge> getUncoveredEdges(final Set<Vertex> vertexCoverCandidate) {

        Set<DefaultEdge> uncoveredEdges = new HashSet<>();

        // Iterate over all edges
        for (DefaultEdge edge : graph.edgeSet()) {
            // Get source (s) and target (t) of the current edge
            Vertex s = graph.getEdgeSource(edge);
            Vertex t = graph.getEdgeTarget(edge);

            // Check to see if the solution candidate covers this edge
            if (!vertexCoverCandidate.contains(s) && !vertexCoverCandidate.contains(t)) {
                uncoveredEdges.add(edge);
            }
        }

        return uncoveredEdges;

    }

    protected Vertex getRandomVertexNotInCover(final Set<Vertex> vertexCoverCandidate) {

        Set<Vertex> unusedVertexSet = getVerticesNotInCover(vertexCoverCandidate);

        Object[] unusedVertices = unusedVertexSet.toArray();

        // Get random index
        int ndx = randomGenerator.nextInt(unusedVertices.length);

        return (Vertex)unusedVertices[ndx];
    }

    protected Set<Vertex> getVerticesNotInCover(final Set<Vertex> vertexCoverCandidate) {
        Set<Vertex> unusedVertices = new HashSet<>();

        for (Vertex v : graph.vertexSet()) {
            if (!vertexCoverCandidate.contains(v)) {
                unusedVertices.add(v);
            }
        }

        return unusedVertices;
    }

    protected Set<Vertex> selectExitingVertices(final Set<Vertex> vertexCoverCandidate) {
        // Choose vertex/vertices with the highest scores
        return getVerticesWithLowestScores(vertexCoverCandidate, getNumberOfVerticesToSwap());
    }
}
