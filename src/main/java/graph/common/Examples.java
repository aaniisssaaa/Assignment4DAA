package graph.common;

import graph.scc.TarjanSCC;
import graph.topo.KahnTopologicalSort;
import graph.dagsp.DAGShortestPath;

import java.io.IOException;
import java.util.List;

/**
 * Example usage demonstrations for the Smart City Scheduling algorithms.
 * These examples show how to use each component of the library.
 */
public class Examples {

    /**
     * Example 1: Detecting SCCs in a task dependency graph
     */
    public static void example1_SCCDetection() {
        System.out.println("=== Example 1: SCC Detection ===\n");
        
        // Create a graph with cyclic dependencies
        Graph graph = new Graph(6);
        graph.setNodeLabel(0, "InitSystem");
        graph.setNodeLabel(1, "CleanSensors");
        graph.setNodeLabel(2, "CheckCameras");
        graph.setNodeLabel(3, "UpdateFirmware");
        graph.setNodeLabel(4, "VerifySystem");
        graph.setNodeLabel(5, "GenerateReport");
        
        // Add edges (with cycles)
        graph.addEdge(0, 1, 2.0);
        graph.addEdge(1, 2, 3.0);
        graph.addEdge(2, 3, 1.5);
        graph.addEdge(3, 1, 1.0);  // Cycle: 1->2->3->1
        graph.addEdge(2, 4, 2.5);
        graph.addEdge(4, 5, 1.0);
        
        // Detect SCCs
        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        // Print results
        tarjan.printSCCs();
        System.out.println();
        tarjan.getMetrics().printMetrics();
    }

    /**
     * Example 2: Building condensation graph and topological ordering
     */
    public static void example2_TopologicalSort() {
        System.out.println("\n=== Example 2: Topological Sort ===\n");
        
        // Load a graph with cycles
        Graph graph = new Graph(5);
        graph.setNodeLabel(0, "Start");
        graph.setNodeLabel(1, "TaskA");
        graph.setNodeLabel(2, "TaskB");
        graph.setNodeLabel(3, "TaskC");
        graph.setNodeLabel(4, "End");
        
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 1);  // Cycle
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        
        // First, find SCCs
        TarjanSCC tarjan = new TarjanSCC(graph);
        tarjan.findSCCs();
        
        // Build condensation (DAG)
        Graph condensation = tarjan.buildCondensation();
        System.out.println("Condensation graph:");
        System.out.println(condensation);
        
        // Topological sort
        KahnTopologicalSort topoSort = new KahnTopologicalSort(condensation);
        topoSort.printTopologicalOrder();
        System.out.println();
        topoSort.getMetrics().printMetrics();
    }

    /**
     * Example 3: Finding shortest and critical paths
     */
    public static void example3_PathFinding() {
        System.out.println("\n=== Example 3: Path Finding ===\n");
        
        // Create a DAG representing project tasks
        Graph dag = new Graph(7);
        dag.setNodeLabel(0, "ProjectStart");
        dag.setNodeLabel(1, "Requirements");
        dag.setNodeLabel(2, "Design");
        dag.setNodeLabel(3, "Development");
        dag.setNodeLabel(4, "Testing");
        dag.setNodeLabel(5, "Documentation");
        dag.setNodeLabel(6, "Deployment");
        
        // Add edges with durations (in days)
        dag.addEdge(0, 1, 3.0);   // Requirements: 3 days
        dag.addEdge(1, 2, 5.0);   // Design: 5 days
        dag.addEdge(2, 3, 10.0);  // Development: 10 days
        dag.addEdge(2, 5, 7.0);   // Documentation can start after design
        dag.addEdge(3, 4, 5.0);   // Testing: 5 days
        dag.addEdge(4, 6, 2.0);   // Deployment: 2 days
        dag.addEdge(5, 6, 1.0);   // Docs must be done before deployment
        
        DAGShortestPath dagSP = new DAGShortestPath(dag);
        
        // Find shortest paths (if we want to rush)
        System.out.println("--- Shortest Paths (Minimum Duration) ---");
        DAGShortestPath.PathResult shortest = dagSP.shortestPath(0);
        shortest.print();
        
        // Find critical path (project completion time)
        System.out.println("\n--- Critical Path (Maximum Duration) ---");
        DAGShortestPath.PathResult longest = dagSP.longestPath(0);
        longest.print();
        longest.printCriticalPath();
        
        System.out.println();
        dagSP.getMetrics().printMetrics();
    }

    /**
     * Example 4: Loading from JSON file
     */
    public static void example4_LoadFromJSON() {
        System.out.println("\n=== Example 4: Load from JSON ===\n");
        
        try {
            // Load graph from file
            Graph graph = GraphLoader.loadFromJson("data/small_cycle.json");
            System.out.println("Loaded graph from file:");
            System.out.println(graph);
            
            // Process it
            TarjanSCC tarjan = new TarjanSCC(graph);
            tarjan.findSCCs();
            tarjan.printSCCs();
            
        } catch (IOException e) {
            System.err.println("Error loading file: " + e.getMessage());
        }
    }

    /**
     * Example 5: Complete workflow
     */
    public static void example5_CompleteWorkflow() {
        System.out.println("\n=== Example 5: Complete Workflow ===\n");
        
        // Step 1: Create/load task graph
        Graph graph = new Graph(8);
        for (int i = 0; i < 8; i++) {
            graph.setNodeLabel(i, "Task" + i);
        }
        
        // Add complex dependencies (including cycles)
        graph.addEdge(0, 1, 2.0);
        graph.addEdge(1, 2, 3.0);
        graph.addEdge(2, 3, 1.0);
        graph.addEdge(3, 1, 1.0);  // Cycle
        graph.addEdge(0, 4, 5.0);
        graph.addEdge(2, 5, 2.0);
        graph.addEdge(4, 6, 3.0);
        graph.addEdge(5, 6, 4.0);
        graph.addEdge(6, 7, 1.0);
        
        System.out.println("Original graph:");
        System.out.println("Vertices: " + graph.getVertexCount());
        System.out.println("Edges: " + graph.getEdgeCount());
        
        // Step 2: Detect and compress cycles
        System.out.println("\nStep 1: Detecting SCCs...");
        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();
        System.out.println("Found " + sccs.size() + " strongly connected components");
        
        // Step 3: Build condensation
        System.out.println("\nStep 2: Building condensation DAG...");
        Graph condensation = tarjan.buildCondensation();
        System.out.println("Condensed to " + condensation.getVertexCount() + " components");
        
        // Step 4: Topological sort
        System.out.println("\nStep 3: Computing topological order...");
        KahnTopologicalSort topoSort = new KahnTopologicalSort(condensation);
        List<Integer> order = topoSort.sort();
        System.out.println("Execution order found: " + order.size() + " components");
        
        // Step 5: Find optimal paths
        System.out.println("\nStep 4: Finding critical path...");
        DAGShortestPath dagSP = new DAGShortestPath(condensation);
        DAGShortestPath.PathResult critical = dagSP.longestPath(0);
        critical.printCriticalPath();
        
        System.out.println("\n=== Workflow Complete ===");
    }

    /**
     * Run all examples
     */
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║  Smart City Scheduling - Usage Examples       ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        
        example1_SCCDetection();
        example2_TopologicalSort();
        example3_PathFinding();
        example4_LoadFromJSON();
        example5_CompleteWorkflow();
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("All examples completed!");
        System.out.println("=".repeat(80));
    }
}
