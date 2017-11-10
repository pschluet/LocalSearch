package com.ps.fileIO;

/*
This file contains code for file IO (reading input data, and writing output files)
 */

import com.ps.datacontainers.Solution;
import com.ps.datacontainers.TracePoint;
import com.ps.datacontainers.Vertex;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static UndirectedGraph<Vertex,DefaultEdge> readDataFile(String filePath) {
        // Create graph
        UndirectedGraph<Vertex, DefaultEdge> graph = new SimpleGraph<Vertex, DefaultEdge>(DefaultEdge.class);

        String line = null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            Vertex sourceVertex = new Vertex(1);
            Vertex destinationVertex;

            // Ignore header line
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                // Add source vertex if it doesn't exist
                if (!graph.containsVertex(sourceVertex)) graph.addVertex(sourceVertex);

                if (!line.isEmpty()) {
                    for (String dv : line.split(" ")) {
                        destinationVertex = new Vertex(Integer.parseInt(dv));

                        // Add destination vertex if it doesn't exist
                        if (!graph.containsVertex(destinationVertex)) graph.addVertex(destinationVertex);

                        // Add edge
                        graph.addEdge(sourceVertex, destinationVertex);
                    }
                }
                sourceVertex = new Vertex(sourceVertex.getId() + 1);
            }

            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graph;
    }

    public static void writeSolutionFile(Solution solution, String filePath) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            // Write best quality
            writer.write(String.format("%d\n",solution.getBestQualityAchieved()));

            // Write out vertex cover nodes
            List<Vertex> vcNodes = new ArrayList<>(solution.getVertexCoverNodes());
            for (int i = 0; i < vcNodes.size() - 1; i++) {
                writer.write(String.format("%d,", vcNodes.get(i).getId()));
            }
            writer.write(String.format("%d", vcNodes.get(vcNodes.size() - 1).getId()));

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void writeTraceFile(Solution solution, String filePath) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            for (TracePoint tp : solution.getTracePoints()) {
                writer.write(String.format("%.2f,%d\n", tp.getTimeSec(), tp.getQuality()));
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
