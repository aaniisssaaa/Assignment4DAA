package graph.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Generator for creating test datasets with various structures.
 * Useful for generating additional test cases beyond the provided 9 datasets.
 */
public class DatasetGenerator {

    private static final Random random = new Random();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Generates a random DAG (Directed Acyclic Graph).
     * @param n number of vertices
     * @param density edge density (0.0 to 1.0)
     * @return generated graph
     */
    public static Graph generateDAG(int n, double density) {
        Graph graph = new Graph(n);
        
        // Add edges only from lower to higher numbered vertices (ensures DAG)
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (random.nextDouble() < density) {
                    double weight = 1.0 + random.nextDouble() * 9.0; // Weight between 1.0 and 10.0
                    graph.addEdge(i, j, weight);
                }
            }
        }
        
        // Add labels
        for (int i = 0; i < n; i++) {
            graph.setNodeLabel(i, "Task" + i);
        }
        
        return graph;
    }

    /**
     * Generates a graph with specified number of SCCs.
     * @param n number of vertices
     * @param numSCCs desired number of SCCs
     * @param sccSize average size of each SCC
     * @return generated graph
     */
    public static Graph generateGraphWithSCCs(int n, int numSCCs, int sccSize) {
        Graph graph = new Graph(n);
        
        int verticesPerSCC = n / numSCCs;
        
        // Create SCCs
        for (int scc = 0; scc < numSCCs; scc++) {
            int start = scc * verticesPerSCC;
            int end = (scc == numSCCs - 1) ? n : (scc + 1) * verticesPerSCC;
            
            // Create cycle within SCC
            for (int i = start; i < end; i++) {
                int next = (i + 1 < end) ? (i + 1) : start;
                double weight = 1.0 + random.nextDouble() * 4.0;
                graph.addEdge(i, next, weight);
            }
            
            // Add extra edges within SCC
            for (int i = start; i < end; i++) {
                if (random.nextDouble() < 0.3) {
                    int target = start + random.nextInt(end - start);
                    if (target != i) {
                        double weight = 1.0 + random.nextDouble() * 4.0;
                        graph.addEdge(i, target, weight);
                    }
                }
            }
        }
        
        // Add edges between SCCs (creates DAG of SCCs)
        for (int i = 0; i < numSCCs - 1; i++) {
            int fromSCC = i;
            int toSCC = i + 1;
            
            int fromStart = fromSCC * verticesPerSCC;
            int fromEnd = (fromSCC == numSCCs - 1) ? n : (fromSCC + 1) * verticesPerSCC;
            int toStart = toSCC * verticesPerSCC;
            int toEnd = (toSCC == numSCCs - 1) ? n : (toSCC + 1) * verticesPerSCC;
            
            // Add 1-2 edges between consecutive SCCs
            for (int j = 0; j < 1 + random.nextInt(2); j++) {
                int from = fromStart + random.nextInt(fromEnd - fromStart);
                int to = toStart + random.nextInt(toEnd - toStart);
                double weight = 1.0 + random.nextDouble() * 4.0;
                graph.addEdge(from, to, weight);
            }
        }
        
        // Add labels
        for (int i = 0; i < n; i++) {
            int sccNum = i / verticesPerSCC;
            graph.setNodeLabel(i, "SCC" + sccNum + "_V" + (i % verticesPerSCC));
        }
        
        return graph;
    }

    /**
     * Saves a graph to JSON file.
     * @param graph the graph to save
     * @param filename output filename
     * @throws IOException if file cannot be written
     */
    public static void saveToJson(Graph graph, String filename) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("vertices", graph.getVertexCount());
        
        JsonArray edges = new JsonArray();
        for (int i = 0; i < graph.getVertexCount(); i++) {
            for (Graph.Edge edge : graph.getEdges(i)) {
                JsonObject edgeObj = new JsonObject();
                edgeObj.addProperty("from", i);
                edgeObj.addProperty("to", edge.to);
                edgeObj.addProperty("weight", edge.weight);
                edges.add(edgeObj);
            }
        }
        json.add("edges", edges);
        
        JsonObject labels = new JsonObject();
        for (int i = 0; i < graph.getVertexCount(); i++) {
            labels.addProperty(String.valueOf(i), graph.getNodeLabel(i));
        }
        json.add("labels", labels);
        
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(json, writer);
        }
    }

    /**
     * Main method for generating additional test datasets.
     */
    public static void main(String[] args) {
        System.out.println("Dataset Generator");
        System.out.println("=================\n");
        
        try {
            // Generate additional small graphs
            System.out.println("Generating small_sparse.json...");
            Graph smallSparse = generateDAG(8, 0.25);
            saveToJson(smallSparse, "data/small_sparse_generated.json");
            
            System.out.println("Generating small_dense_gen.json...");
            Graph smallDense = generateDAG(9, 0.6);
            saveToJson(smallDense, "data/small_dense_generated.json");
            
            // Generate medium graph with multiple SCCs
            System.out.println("Generating medium_sccs.json...");
            Graph mediumSCCs = generateGraphWithSCCs(16, 4, 4);
            saveToJson(mediumSCCs, "data/medium_sccs_generated.json");
            
            // Generate large test graphs
            System.out.println("Generating large_complex.json...");
            Graph largeComplex = generateGraphWithSCCs(35, 5, 7);
            saveToJson(largeComplex, "data/large_complex_generated.json");
            
            System.out.println("Generating large_dag_gen.json...");
            Graph largeDag = generateDAG(45, 0.1);
            saveToJson(largeDag, "data/large_dag_generated.json");
            
            System.out.println("\nAll datasets generated successfully!");
            System.out.println("Files saved to data/ directory");
            
        } catch (IOException e) {
            System.err.println("Error generating datasets: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
