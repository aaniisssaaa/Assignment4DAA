package graph.topo;

import graph.common.Graph;
import graph.common.Metrics;
import graph.common.MetricsImpl;

import java.util.*;

/**
 * Kahn's algorithm for topological sorting.
 * Works only on DAGs (Directed Acyclic Graphs).
 * Complexity: O(V + E)
 */
public class KahnTopologicalSort {
    private final Graph graph;
    private final Metrics metrics;

    /**
     * Creates a topological sort solver for the given graph.
     * @param graph the input DAG
     */
    public KahnTopologicalSort(Graph graph) {
        this.graph = graph;
        this.metrics = new MetricsImpl();
    }

    /**
     * Computes a topological ordering of the vertices.
     * @return list of vertices in topological order, or empty if graph has cycle
     */
    public List<Integer> sort() {
        metrics.reset();
        metrics.startTimer();
        
        int n = graph.getVertexCount();
        int[] inDegree = new int[n];
        
        // Calculate in-degrees
        for (int u = 0; u < n; u++) {
            for (Graph.Edge edge : graph.getEdges(u)) {
                inDegree[edge.to]++;
                metrics.incrementCounter("in_degree_calculations");
            }
        }
        
        // Initialize queue with vertices having in-degree 0
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
                metrics.incrementCounter("queue_pushes");
            }
        }
        
        List<Integer> topoOrder = new ArrayList<>();
        
        // Process vertices in topological order
        while (!queue.isEmpty()) {
            int u = queue.poll();
            topoOrder.add(u);
            metrics.incrementCounter("queue_pops");
            metrics.incrementCounter("vertices_processed");
            
            // Reduce in-degree of neighbors
            for (Graph.Edge edge : graph.getEdges(u)) {
                int v = edge.to;
                inDegree[v]--;
                metrics.incrementCounter("edges_processed");
                
                if (inDegree[v] == 0) {
                    queue.offer(v);
                    metrics.incrementCounter("queue_pushes");
                }
            }
        }
        
        metrics.stopTimer();
        
        // Check if all vertices were processed (cycle detection)
        if (topoOrder.size() != n) {
            metrics.incrementCounter("cycle_detected");
            return Collections.emptyList(); // Graph has a cycle
        }
        
        return topoOrder;
    }

    /**
     * Checks if the graph is a DAG (has no cycles).
     * @return true if graph is acyclic
     */
    public boolean isDAG() {
        List<Integer> order = sort();
        return order.size() == graph.getVertexCount();
    }

    /**
     * Gets the metrics collected during sorting.
     * @return metrics object
     */
    public Metrics getMetrics() {
        return metrics;
    }

    /**
     * Prints the topological order to console.
     */
    public void printTopologicalOrder() {
        List<Integer> order = sort();
        
        if (order.isEmpty()) {
            System.out.println("Graph contains a cycle - no topological order exists!");
            return;
        }
        
        System.out.println("=== Topological Order ===");
        System.out.print("Order: ");
        for (int i = 0; i < order.size(); i++) {
            if (i > 0) System.out.print(" -> ");
            int vertex = order.get(i);
            System.out.print(vertex + "(" + graph.getNodeLabel(vertex) + ")");
        }
        System.out.println();
    }
}
