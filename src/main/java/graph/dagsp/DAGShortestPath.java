package graph.dagsp;

import graph.common.Graph;
import graph.common.Metrics;
import graph.common.MetricsImpl;
import graph.topo.KahnTopologicalSort;

import java.util.*;

/**
 * Shortest and longest path algorithms for DAGs using dynamic programming.
 * Complexity: O(V + E)
 */
public class DAGShortestPath {
    private final Graph graph;
    private final Metrics metrics;

    /**
     * Creates a DAG shortest path solver.
     * @param graph the input DAG
     */
    public DAGShortestPath(Graph graph) {
        this.graph = graph;
        this.metrics = new MetricsImpl();
    }

    /**
     * Computes shortest paths from a source vertex to all other vertices.
     * @param source the source vertex
     * @return PathResult containing distances and predecessors
     */
    public PathResult shortestPath(int source) {
        metrics.reset();
        metrics.startTimer();
        
        int n = graph.getVertexCount();
        double[] dist = new double[n];
        int[] pred = new int[n];
        
        // Initialize distances
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(pred, -1);
        dist[source] = 0;
        
        // Get topological order
        KahnTopologicalSort topoSort = new KahnTopologicalSort(graph);
        List<Integer> topoOrder = topoSort.sort();
        
        if (topoOrder.isEmpty()) {
            metrics.stopTimer();
            throw new IllegalStateException("Graph contains a cycle");
        }
        
        // Process vertices in topological order
        for (int u : topoOrder) {
            if (dist[u] != Double.POSITIVE_INFINITY) {
                for (Graph.Edge edge : graph.getEdges(u)) {
                    int v = edge.to;
                    double newDist = dist[u] + edge.weight;
                    metrics.incrementCounter("relaxations");
                    
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        pred[v] = u;
                        metrics.incrementCounter("updates");
                    }
                }
            }
        }
        
        metrics.stopTimer();
        return new PathResult(dist, pred, source, false);
    }

    /**
     * Computes longest paths from a source vertex to all other vertices.
     * This finds the critical path in the DAG.
     * @param source the source vertex
     * @return PathResult containing distances and predecessors
     */
    public PathResult longestPath(int source) {
        metrics.reset();
        metrics.startTimer();
        
        int n = graph.getVertexCount();
        double[] dist = new double[n];
        int[] pred = new int[n];
        
        // Initialize distances
        Arrays.fill(dist, Double.NEGATIVE_INFINITY);
        Arrays.fill(pred, -1);
        dist[source] = 0;
        
        // Get topological order
        KahnTopologicalSort topoSort = new KahnTopologicalSort(graph);
        List<Integer> topoOrder = topoSort.sort();
        
        if (topoOrder.isEmpty()) {
            metrics.stopTimer();
            throw new IllegalStateException("Graph contains a cycle");
        }
        
        // Process vertices in topological order
        for (int u : topoOrder) {
            if (dist[u] != Double.NEGATIVE_INFINITY) {
                for (Graph.Edge edge : graph.getEdges(u)) {
                    int v = edge.to;
                    double newDist = dist[u] + edge.weight;
                    metrics.incrementCounter("relaxations");
                    
                    if (newDist > dist[v]) {
                        dist[v] = newDist;
                        pred[v] = u;
                        metrics.incrementCounter("updates");
                    }
                }
            }
        }
        
        metrics.stopTimer();
        return new PathResult(dist, pred, source, true);
    }

    /**
     * Finds the critical path (longest path) in the entire DAG.
     * @return PathResult with the longest path information
     */
    public PathResult findCriticalPath() {
        int n = graph.getVertexCount();
        PathResult bestResult = null;
        double maxLength = Double.NEGATIVE_INFINITY;
        
        // Try all possible source vertices
        for (int source = 0; source < n; source++) {
            PathResult result = longestPath(source);
            for (int dest = 0; dest < n; dest++) {
                if (result.getDistance(dest) > maxLength && 
                    result.getDistance(dest) != Double.NEGATIVE_INFINITY) {
                    maxLength = result.getDistance(dest);
                    bestResult = result;
                }
            }
        }
        
        return bestResult;
    }

    /**
     * Gets the metrics collected during path computation.
     * @return metrics object
     */
    public Metrics getMetrics() {
        return metrics;
    }

    /**
     * Result of a path computation containing distances and predecessors.
     */
    public class PathResult {
        private final double[] distances;
        private final int[] predecessors;
        private final int source;
        private final boolean isLongest;

        public PathResult(double[] distances, int[] predecessors, int source, boolean isLongest) {
            this.distances = distances;
            this.predecessors = predecessors;
            this.source = source;
            this.isLongest = isLongest;
        }

        /**
         * Gets the distance to a vertex.
         * @param vertex the target vertex
         * @return distance from source to vertex
         */
        public double getDistance(int vertex) {
            return distances[vertex];
        }

        /**
         * Gets all distances.
         * @return array of distances
         */
        public double[] getDistances() {
            return distances;
        }

        /**
         * Reconstructs the path from source to a destination vertex.
         * @param dest the destination vertex
         * @return list of vertices in the path, or empty if no path exists
         */
        public List<Integer> getPath(int dest) {
            if (isUnreachable(dest)) {
                return Collections.emptyList();
            }
            
            LinkedList<Integer> path = new LinkedList<>();
            int current = dest;
            
            while (current != -1) {
                path.addFirst(current);
                if (current == source) break;
                current = predecessors[current];
            }
            
            return path;
        }

        /**
         * Checks if a vertex is reachable from the source.
         * @param vertex the target vertex
         * @return true if unreachable
         */
        public boolean isUnreachable(int vertex) {
            return isLongest ? 
                   (distances[vertex] == Double.NEGATIVE_INFINITY) :
                   (distances[vertex] == Double.POSITIVE_INFINITY);
        }

        /**
         * Gets the source vertex.
         * @return source vertex
         */
        public int getSource() {
            return source;
        }

        /**
         * Prints the path result to console.
         */
        public void print() {
            String pathType = isLongest ? "Longest" : "Shortest";
            System.out.println("=== " + pathType + " Paths from vertex " + source + 
                             " (" + graph.getNodeLabel(source) + ") ===");
            
            for (int i = 0; i < distances.length; i++) {
                if (!isUnreachable(i)) {
                    List<Integer> path = getPath(i);
                    System.out.print("To " + i + " (" + graph.getNodeLabel(i) + "): ");
                    System.out.printf("distance = %.2f, path = ", distances[i]);
                    
                    for (int j = 0; j < path.size(); j++) {
                        if (j > 0) System.out.print(" -> ");
                        System.out.print(path.get(j) + "(" + graph.getNodeLabel(path.get(j)) + ")");
                    }
                    System.out.println();
                }
            }
        }

        /**
         * Finds and prints the critical path (longest path in the result).
         */
        public void printCriticalPath() {
            double maxDist = Double.NEGATIVE_INFINITY;
            int maxVertex = -1;
            
            for (int i = 0; i < distances.length; i++) {
                if (!isUnreachable(i) && distances[i] > maxDist) {
                    maxDist = distances[i];
                    maxVertex = i;
                }
            }
            
            if (maxVertex != -1) {
                List<Integer> path = getPath(maxVertex);
                System.out.println("=== Critical Path ===");
                System.out.printf("Length: %.2f\n", maxDist);
                System.out.print("Path: ");
                for (int j = 0; j < path.size(); j++) {
                    if (j > 0) System.out.print(" -> ");
                    System.out.print(path.get(j) + "(" + graph.getNodeLabel(path.get(j)) + ")");
                }
                System.out.println();
            }
        }
    }
}
