import graph.common.*;
import graph.scc.TarjanSCC;
import graph.topo.KahnTopologicalSort;
import graph.dagsp.DAGShortestPath;

import java.io.File;
import java.io.IOException;

/**
 * Main entry point for Smart City Scheduling application.
 * Demonstrates SCC detection, topological sorting, and shortest/longest paths.
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  Smart City/Smart Campus Scheduling System");
        System.out.println("=================================================\n");
        
        // Process all datasets in the data directory
        File dataDir = new File("data");
        if (!dataDir.exists() || !dataDir.isDirectory()) {
            System.out.println("Data directory not found. Creating test graph...\n");
            runTestGraph();
            return;
        }
        
        File[] jsonFiles = dataDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (jsonFiles == null || jsonFiles.length == 0) {
            System.out.println("No JSON files found in data directory. Creating test graph...\n");
            runTestGraph();
            return;
        }
        
        // Process each dataset
        for (File file : jsonFiles) {
            System.out.println("\n" + "=".repeat(80));
            System.out.println("Processing: " + file.getName());
            System.out.println("=".repeat(80));
            
            try {
                processGraph(file.getPath());
            } catch (Exception e) {
                System.err.println("Error processing " + file.getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("Processing complete!");
        System.out.println("=".repeat(80));
    }
    
    /**
     * Processes a graph from a JSON file.
     * @param filename path to the JSON file
     */
    private static void processGraph(String filename) throws IOException {
        // Load graph
        Graph graph = GraphLoader.loadFromJson(filename);
        System.out.println("\nLoaded graph: " + graph.getVertexCount() + " vertices, " + 
                         graph.getEdgeCount() + " edges");
        
        // Step 1: Find Strongly Connected Components
        System.out.println("\n" + "-".repeat(80));
        System.out.println("STEP 1: Finding Strongly Connected Components (Tarjan's Algorithm)");
        System.out.println("-".repeat(80));
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        tarjan.findSCCs();
        tarjan.printSCCs();
        System.out.println();
        tarjan.getMetrics().printMetrics();
        
        // Step 2: Build Condensation Graph
        System.out.println("\n" + "-".repeat(80));
        System.out.println("STEP 2: Building Condensation Graph (DAG of SCCs)");
        System.out.println("-".repeat(80));
        
        Graph condensation = tarjan.buildCondensation();
        System.out.println("Condensation: " + condensation.getVertexCount() + " components, " + 
                         condensation.getEdgeCount() + " edges between components");
        
        // Step 3: Topological Sort of Condensation
        System.out.println("\n" + "-".repeat(80));
        System.out.println("STEP 3: Topological Sort of Condensation (Kahn's Algorithm)");
        System.out.println("-".repeat(80));
        
        KahnTopologicalSort topoSort = new KahnTopologicalSort(condensation);
        topoSort.printTopologicalOrder();
        System.out.println();
        topoSort.getMetrics().printMetrics();
        
        // Step 4: Shortest and Longest Paths in DAG
        if (topoSort.isDAG() && condensation.getVertexCount() > 0) {
            System.out.println("\n" + "-".repeat(80));
            System.out.println("STEP 4: Shortest and Longest Paths in DAG");
            System.out.println("-".repeat(80));
            
            DAGShortestPath dagSP = new DAGShortestPath(condensation);
            
            // Compute from first component
            int source = 0;
            
            System.out.println("\n--- Shortest Paths ---");
            DAGShortestPath.PathResult shortest = dagSP.shortestPath(source);
            shortest.print();
            System.out.println();
            dagSP.getMetrics().printMetrics();
            
            System.out.println("\n--- Longest Paths (Critical Path) ---");
            DAGShortestPath.PathResult longest = dagSP.longestPath(source);
            longest.print();
            longest.printCriticalPath();
            System.out.println();
            dagSP.getMetrics().printMetrics();
        }
    }
    
    /**
     * Runs a demonstration with a hardcoded test graph.
     */
    private static void runTestGraph() {
        System.out.println("Running with test graph...\n");
        
        // Create a test graph with cycles
        Graph graph = GraphLoader.createTestGraph();
        System.out.println("Test graph created:");
        System.out.println(graph);
        
        try {
            // Find SCCs
            TarjanSCC tarjan = new TarjanSCC(graph);
            tarjan.findSCCs();
            tarjan.printSCCs();
            System.out.println();
            tarjan.getMetrics().printMetrics();
            
            // Build condensation
            Graph condensation = tarjan.buildCondensation();
            System.out.println("\nCondensation graph:");
            System.out.println(condensation);
            
            // Topological sort
            KahnTopologicalSort topoSort = new KahnTopologicalSort(condensation);
            topoSort.printTopologicalOrder();
            System.out.println();
            topoSort.getMetrics().printMetrics();
            
            // Paths
            DAGShortestPath dagSP = new DAGShortestPath(condensation);
            System.out.println("\n--- Shortest Paths from component 0 ---");
            DAGShortestPath.PathResult shortest = dagSP.shortestPath(0);
            shortest.print();
            
            System.out.println("\n--- Longest Paths from component 0 ---");
            DAGShortestPath.PathResult longest = dagSP.longestPath(0);
            longest.print();
            longest.printCriticalPath();
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
