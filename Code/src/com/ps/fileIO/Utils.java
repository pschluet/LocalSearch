package com.ps.fileIO;

import com.ps.algorithms.Solution;
import com.ps.algorithms.TracePoint;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.io.*;
import java.util.List;

public class Utils {
    public static UndirectedGraph readDataFile(String filePath) {
        // Create graph
        UndirectedGraph<Integer, DefaultEdge> graph = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);

        String line = null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            int sourceVertex = 1;
            int destinationVertex;

            // Ignore header line
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                // Add source vertex if it doesn't exist
                if (!graph.containsVertex(sourceVertex)) graph.addVertex(sourceVertex);

                for (String dv : line.split(" ")) {
                    destinationVertex = Integer.parseInt(dv);

                    // Add destination vertex if it doesn't exist
                    if (!graph.containsVertex(destinationVertex)) graph.addVertex(destinationVertex);

                    // Add edge
                    graph.addEdge(sourceVertex, destinationVertex);
                }
                sourceVertex++;
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
            List<Integer> vcNodes = solution.getVertexCoverNodes();
            for (int i = 0; i < vcNodes.size() - 1; i++) {
                writer.write(String.format("%d,", vcNodes.get(i)));
            }
            writer.write(String.format("%d", vcNodes.get(vcNodes.size() - 1)));

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
