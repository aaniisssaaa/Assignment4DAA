package graph.scc;

import graph.common.Graph;
import graph.common.Metrics;
import graph.common.MetricsImpl;

import java.util.*;

/**
 * Tarjan's algorithm for finding Strongly Connected Components (SCCs).
 * Complexity: O(V + E)
 */
public class TarjanSCC {
    private final Graph graph;
    private final Metrics metrics;
    
    private int[] disc;      // Discovery time
    private int[] low;       // Lowest reachable vertex
    private boolean[] onStack;
    private Stack<Integer> stack;
    private List<List<Integer>> sccs;
    private int time;

    /**
     * Creates a Tarjan SCC detector for the given graph.
     * @param graph the input directed graph
     */
    public TarjanSCC(Graph graph) {
        this.graph = graph;
        this.metrics = new MetricsImpl();
    }

    /**
     * Finds all strongly connected components.
     * @return list of SCCs, each represented as a list of vertices
     */
    public List<List<Integer>> findSCCs() {
        metrics.reset();
        metrics.startTimer();
        
        int n = graph.getVertexCount();
        disc = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new Stack<>();
        sccs = new ArrayList<>();
        time = 0;
        
        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);
        
        // Run DFS from all unvisited vertices
        for (int v = 0; v < n; v++) {
            if (disc[v] == -1) {
                dfs(v);
            }
        }
        
        metrics.stopTimer();
        return sccs;
    }

    /**
     * DFS traversal to find SCCs using Tarjan's algorithm.
     * @param u current vertex
     */
    private void dfs(int u) {
        metrics.incrementCounter("dfs_visits");
        
        // Initialize discovery time and low value
        disc[u] = low[u] = time++;
        stack.push(u);
        onStack[u] = true;
        
        // Visit all neighbors
        for (Graph.Edge edge : graph.getEdges(u)) {
            int v = edge.to;
            metrics.incrementCounter("edges_traversed");
            
            if (disc[v] == -1) {
                // Tree edge - recurse
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                // Back edge - update low value
                low[u] = Math.min(low[u], disc[v]);
            }
        }
        
        // If u is a root node, pop the stack and create SCC
        if (low[u] == disc[u]) {
            List<Integer> scc = new ArrayList<>();
            int v;
            do {
                v = stack.pop();
                onStack[v] = false;
                scc.add(v);
                metrics.incrementCounter("stack_pops");
            } while (v != u);
            
            sccs.add(scc);
            metrics.incrementCounter("sccs_found");
        }
    }

    /**
     * Builds a condensation graph (DAG of SCCs).
     * Each SCC becomes a single vertex in the condensation.
     * @return the condensation graph
     */
    public Graph buildCondensation() {
        if (sccs == null || sccs.isEmpty()) {
            throw new IllegalStateException("Must call findSCCs() first");
        }
        
        // Map each vertex to its SCC index
        Map<Integer, Integer> vertexToSCC = new HashMap<>();
        for (int i = 0; i < sccs.size(); i++) {
            for (int vertex : sccs.get(i)) {
                vertexToSCC.put(vertex, i);
            }
        }
        
        // Create condensation graph
        Graph condensation = new Graph(sccs.size());
        Set<String> addedEdges = new HashSet<>();
        
        // Add edges between different SCCs
        for (int u = 0; u < graph.getVertexCount(); u++) {
            int sccU = vertexToSCC.get(u);
            for (Graph.Edge edge : graph.getEdges(u)) {
                int v = edge.to;
                int sccV = vertexToSCC.get(v);
                
                // Only add edge if connecting different SCCs
                if (sccU != sccV) {
                    String edgeKey = sccU + "->" + sccV;
                    if (!addedEdges.contains(edgeKey)) {
                        condensation.addEdge(sccU, sccV, edge.weight);
                        addedEdges.add(edgeKey);
                    }
                }
            }
        }
        
        // Set labels for condensation nodes
        for (int i = 0; i < sccs.size(); i++) {
            List<Integer> scc = sccs.get(i);
            StringBuilder label = new StringBuilder("SCC" + i + "{");
            for (int j = 0; j < scc.size(); j++) {
                if (j > 0) label.append(",");
                label.append(graph.getNodeLabel(scc.get(j)));
            }
            label.append("}");
            condensation.setNodeLabel(i, label.toString());
        }
        
        return condensation;
    }

    /**
     * Gets the metrics collected during SCC detection.
     * @return metrics object
     */
    public Metrics getMetrics() {
        return metrics;
    }

    /**
     * Gets the detected SCCs.
     * @return list of SCCs
     */
    public List<List<Integer>> getSCCs() {
        return sccs;
    }

    /**
     * Prints the detected SCCs to console.
     */
    public void printSCCs() {
        if (sccs == null || sccs.isEmpty()) {
            System.out.println("No SCCs found. Run findSCCs() first.");
            return;
        }
        
        System.out.println("=== Strongly Connected Components ===");
        System.out.println("Total SCCs found: " + sccs.size());
        for (int i = 0; i < sccs.size(); i++) {
            List<Integer> scc = sccs.get(i);
            System.out.print("SCC " + i + " (size=" + scc.size() + "): [");
            for (int j = 0; j < scc.size(); j++) {
                if (j > 0) System.out.print(", ");
                int vertex = scc.get(j);
                System.out.print(vertex + "(" + graph.getNodeLabel(vertex) + ")");
            }
            System.out.println("]");
        }
    }
}
