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

    protected UndirectedGraph<Vertex,DefaultEdge> graph;
    protected int cutoffTimeSec;
    protected Random randomGenerator;

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
        Set<Vertex> vertexCoverCandidate = getInitialVertexCover(graph);
        soln.addTracePoint(getQuality(vertexCoverCandidate, graph));
        soln.setVertexCoverNodes(new HashSet<>(vertexCoverCandidate));

        updateScores(vertexCoverCandidate, graph);

        while (soln.getElapsedTimeSec() < cutoffTimeSec) {
            if (isVertexCover(vertexCoverCandidate, graph)) {

                // Add point to the solution trace if the quality is better
                int currentQuality = getQuality(vertexCoverCandidate, graph);
                if (currentQuality < soln.getBestQualityAchieved()) {
                    soln.addTracePoint(currentQuality);

                    soln.setVertexCoverNodes(new HashSet<>(vertexCoverCandidate));
                }

                // Remove certain number of vertices and continue the search
                removeVertices(vertexCoverCandidate);
                updateScores(vertexCoverCandidate, graph);
            }

            // Select some vertices to remove from the candidate solution
            Set<Vertex> exitingVertices = selectExitingVertices(vertexCoverCandidate, graph);
            vertexCoverCandidate.removeAll(exitingVertices);

            // Select some vertices to add to the candidate solution
            Set<Vertex> enteringVertices = selectEnteringVertices(vertexCoverCandidate, graph);
            vertexCoverCandidate.addAll(enteringVertices);
            updateScores(vertexCoverCandidate, graph);
        }

        return soln;
    }

    protected Set<Vertex> getInitialVertexCover(final UndirectedGraph<Vertex,DefaultEdge> graph) {
        // This implements the APPROX-VERTEX-COVER algorithm from page 1109 of
        // the CLRS Algorithms textbook (3rd Edition)

        // Make a copy of the input graph
        UndirectedGraph<Vertex, DefaultEdge> changingGraph = new SimpleGraph<Vertex, DefaultEdge>(DefaultEdge.class);
        Graphs.addGraph(changingGraph, graph);

        Set<Vertex> vertexCover = new HashSet<>();

        while (changingGraph.edgeSet().size() > 0) {
            // Pick random edge in remaining edge set
            Object[] edges = changingGraph.edgeSet().toArray();
            int ndx = randomGenerator.nextInt(edges.length);
            DefaultEdge nextEdge = (DefaultEdge)edges[ndx];

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

    protected boolean isVertexCover(final Set<Vertex> solutionToCheck, final UndirectedGraph<Vertex,DefaultEdge> graph) {

        // Iterate over all edges
        for (DefaultEdge edge : graph.edgeSet()) {
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
        // Remove the specified number of vertices from the set
        inputVertices.removeAll(getVerticesWithLowestScores(inputVertices, numberOfVerticesToRemove));
    }

    protected int getQuality(final Set<Vertex> vertexCoverCandidate, final UndirectedGraph<Vertex,DefaultEdge> graph) {

        return vertexCoverCandidate.size();

    }

    protected int getCostDelta(final Set<Vertex> vertexCoverCandidate, final UndirectedGraph<Vertex, DefaultEdge> graph, Vertex vertexToRemove) {
        int costDelta = 0;

        // Get vertices connected to the vertex I want to remove
        Set<DefaultEdge> edges = graph.edgesOf(vertexToRemove);
        for (DefaultEdge edge : edges) {
            // Get source (s) and target (t) of the current edge
            Vertex s = (Vertex)graph.getEdgeSource(edge);
            Vertex t = (Vertex)graph.getEdgeTarget(edge);

            Vertex otherEdge = s == vertexToRemove ? t : s;

            // If the other vertex is not in the vertex cover, removing this vertex would uncover an edge, so the cost
            // (number of uncovered edges) would go up by one
            if (!vertexCoverCandidate.contains(otherEdge)) {
                costDelta += 1;
            }
        }

        return costDelta;
    }

    protected void updateScores(Set<Vertex> vertexCoverCandidate, final UndirectedGraph<Vertex,DefaultEdge> graph) {

        for (Vertex v : vertexCoverCandidate) {

            int costDelta = getCostDelta(vertexCoverCandidate, graph, v);

            v.setScore(costDelta);
        }
    }

    protected Set<Vertex> getVerticesWithLowestScores(final Set<Vertex> vertexCoverCandidate, int numVertices) {
        // Sort by score
        List<Vertex> sortedVertices = new ArrayList<>(vertexCoverCandidate);
        sortedVertices.sort(Comparator.comparingInt(Vertex::getScore));

        // Get the vertices with the highest scores
        Set<Vertex> highScoreVertices = new HashSet<>();
        highScoreVertices.addAll(sortedVertices.subList(0, numVertices));

        return highScoreVertices;
    }

    protected Set<Vertex> selectEnteringVertices(final Set<Vertex> vertexCoverCandidate, final UndirectedGraph<Vertex,DefaultEdge> graph) {

        Set<Vertex> enteringVertices = new HashSet<>();

        for (int i = 0; i < getNumberOfVerticesToSwap(); i++) {
            DefaultEdge uncoveredEdge = getRandomUncoveredEdge(vertexCoverCandidate, graph);
            Vertex vertexToAdd;
            if (uncoveredEdge == null) {
                vertexToAdd = getRandomVertexNotInCover(vertexCoverCandidate, graph);
            } else {
                vertexToAdd = (Vertex)graph.getEdgeSource(uncoveredEdge);
            }
            vertexCoverCandidate.add(vertexToAdd);
            enteringVertices.add(vertexToAdd);
        }

        vertexCoverCandidate.removeAll(enteringVertices);

        return enteringVertices;
    }

    protected DefaultEdge getRandomUncoveredEdge(final Set<Vertex> vertexCoverCandidate, final UndirectedGraph<Vertex,DefaultEdge> graph) {
        // Make a copy of the input graph
        UndirectedGraph<Vertex, DefaultEdge> graphCopy = new SimpleGraph<Vertex, DefaultEdge>(DefaultEdge.class);
        Graphs.addGraph(graphCopy, graph);

        // Remove vertices that are part of the current VC candidate solution
        graphCopy.removeAllVertices(vertexCoverCandidate);

        if (graphCopy.edgeSet().size() == 0)
            return null;

        Object[] edges = graphCopy.edgeSet().toArray();

        // Get random index
        int ndx = randomGenerator.nextInt(edges.length);

        return (DefaultEdge)edges[ndx];
    }

    protected Vertex getRandomVertexNotInCover(final Set<Vertex> vertexCoverCandidate, final UndirectedGraph<Vertex,DefaultEdge> graph) {
        // Make a copy of the input graph
        UndirectedGraph<Vertex, DefaultEdge> graphCopy = new SimpleGraph<Vertex, DefaultEdge>(DefaultEdge.class);
        Graphs.addGraph(graphCopy, graph);

        graphCopy.removeAllVertices(vertexCoverCandidate);

        Object[] unusedVertices = graphCopy.vertexSet().toArray();

        // Get random index
        int ndx = randomGenerator.nextInt(unusedVertices.length);

        return (Vertex)unusedVertices[ndx];
    }

    protected Set<Vertex> selectExitingVertices(final Set<Vertex> vertexCoverCandidate, final UndirectedGraph graph) {
        // Choose vertex/vertices with the highest scores
        return getVerticesWithLowestScores(vertexCoverCandidate, getNumberOfVerticesToSwap());
    }
}
