package graph.scc;

import graph.common.Graph;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TarjanSCC algorithm.
 */
class TarjanSCCTest {

    @Test
    @DisplayName("Test SCC detection in simple cycle")
    void testSimpleCycle() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 0);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        assertEquals(1, sccs.size(), "Should have exactly 1 SCC");
        assertEquals(3, sccs.get(0).size(), "SCC should contain all 3 vertices");
    }

    @Test
    @DisplayName("Test SCC detection in DAG")
    void testDAG() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        assertEquals(4, sccs.size(), "DAG should have 4 SCCs (one per vertex)");
        for (List<Integer> scc : sccs) {
            assertEquals(1, scc.size(), "Each SCC should be a single vertex");
        }
    }

    @Test
    @DisplayName("Test SCC detection in mixed graph")
    void testMixedGraph() {
        // Graph with one cycle and separate vertices
        Graph graph = new Graph(5);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 1); // Cycle between 1 and 2
        graph.addEdge(2, 3);
        graph.addEdge(0, 4);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        assertEquals(4, sccs.size(), "Should have 4 SCCs");
        
        // Find the SCC with size 2 (vertices 1 and 2)
        boolean foundCycle = false;
        for (List<Integer> scc : sccs) {
            if (scc.size() == 2) {
                foundCycle = true;
                assertTrue(scc.contains(1) && scc.contains(2), 
                          "Cycle SCC should contain vertices 1 and 2");
            }
        }
        assertTrue(foundCycle, "Should find the cycle SCC");
    }

    @Test
    @DisplayName("Test condensation graph creation")
    void testCondensation() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 1); // Cycle
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        tarjan.findSCCs();
        Graph condensation = tarjan.buildCondensation();
        
        assertTrue(condensation.getVertexCount() < graph.getVertexCount(), 
                  "Condensation should have fewer vertices than original");
        
        // Condensation should be a DAG
        assertFalse(hasCycle(condensation), "Condensation should be acyclic");
    }

    @Test
    @DisplayName("Test empty graph")
    void testEmptyGraph() {
        Graph graph = new Graph(0);
        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        assertEquals(0, sccs.size(), "Empty graph should have 0 SCCs");
    }

    @Test
    @DisplayName("Test single vertex")
    void testSingleVertex() {
        Graph graph = new Graph(1);
        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        assertEquals(1, sccs.size(), "Single vertex should form 1 SCC");
        assertEquals(1, sccs.get(0).size(), "SCC should contain exactly 1 vertex");
    }

    @Test
    @DisplayName("Test disconnected components")
    void testDisconnectedGraph() {
        Graph graph = new Graph(6);
        // First component: 0 -> 1 -> 2 -> 0
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 0);
        // Second component: 3 -> 4 -> 5 -> 3
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);
        graph.addEdge(5, 3);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        List<List<Integer>> sccs = tarjan.findSCCs();
        
        assertEquals(2, sccs.size(), "Should have 2 SCCs");
        for (List<Integer> scc : sccs) {
            assertEquals(3, scc.size(), "Each SCC should have 3 vertices");
        }
    }

    // Helper method to check if graph has a cycle
    private boolean hasCycle(Graph graph) {
        int n = graph.getVertexCount();
        boolean[] visited = new boolean[n];
        boolean[] recStack = new boolean[n];
        
        for (int i = 0; i < n; i++) {
            if (hasCycleDFS(graph, i, visited, recStack)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCycleDFS(Graph graph, int v, boolean[] visited, boolean[] recStack) {
        if (recStack[v]) return true;
        if (visited[v]) return false;
        
        visited[v] = true;
        recStack[v] = true;
        
        for (Graph.Edge edge : graph.getEdges(v)) {
            if (hasCycleDFS(graph, edge.to, visited, recStack)) {
                return true;
            }
        }
        
        recStack[v] = false;
        return false;
    }
}
