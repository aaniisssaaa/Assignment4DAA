package graph.topo;

import graph.common.Graph;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for KahnTopologicalSort algorithm.
 */
class KahnTopologicalSortTest {

    @Test
    @DisplayName("Test topological sort on simple DAG")
    void testSimpleDAG() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        
        KahnTopologicalSort topoSort = new KahnTopologicalSort(graph);
        List<Integer> order = topoSort.sort();
        
        assertEquals(4, order.size(), "Should have all 4 vertices");
        assertTrue(isValidTopologicalOrder(graph, order), 
                  "Order should be valid topological ordering");
    }

    @Test
    @DisplayName("Test topological sort on diamond DAG")
    void testDiamondDAG() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        
        KahnTopologicalSort topoSort = new KahnTopologicalSort(graph);
        List<Integer> order = topoSort.sort();
        
        assertEquals(4, order.size(), "Should have all 4 vertices");
        assertTrue(isValidTopologicalOrder(graph, order), 
                  "Order should be valid topological ordering");
        assertEquals(0, order.get(0), "Source vertex 0 should be first");
        assertEquals(3, order.get(3), "Sink vertex 3 should be last");
    }

    @Test
    @DisplayName("Test cycle detection")
    void testCycleDetection() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 0); // Creates a cycle
        
        KahnTopologicalSort topoSort = new KahnTopologicalSort(graph);
        List<Integer> order = topoSort.sort();
        
        assertTrue(order.isEmpty(), "Should return empty list for graph with cycle");
        assertFalse(topoSort.isDAG(), "Should detect cycle");
    }

    @Test
    @DisplayName("Test isDAG on acyclic graph")
    void testIsDAG() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        
        KahnTopologicalSort topoSort = new KahnTopologicalSort(graph);
        
        assertTrue(topoSort.isDAG(), "Should recognize DAG");
    }

    @Test
    @DisplayName("Test empty graph")
    void testEmptyGraph() {
        Graph graph = new Graph(0);
        KahnTopologicalSort topoSort = new KahnTopologicalSort(graph);
        List<Integer> order = topoSort.sort();
        
        assertEquals(0, order.size(), "Empty graph should produce empty order");
    }

    @Test
    @DisplayName("Test single vertex")
    void testSingleVertex() {
        Graph graph = new Graph(1);
        KahnTopologicalSort topoSort = new KahnTopologicalSort(graph);
        List<Integer> order = topoSort.sort();
        
        assertEquals(1, order.size(), "Should have 1 vertex");
        assertEquals(0, order.get(0), "Single vertex should be vertex 0");
    }

    @Test
    @DisplayName("Test disconnected DAG")
    void testDisconnectedDAG() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1);
        graph.addEdge(2, 3);
        
        KahnTopologicalSort topoSort = new KahnTopologicalSort(graph);
        List<Integer> order = topoSort.sort();
        
        assertEquals(4, order.size(), "Should have all 4 vertices");
        assertTrue(isValidTopologicalOrder(graph, order), 
                  "Order should be valid topological ordering");
    }

    @Test
    @DisplayName("Test complex DAG")
    void testComplexDAG() {
        // More complex DAG with multiple paths
        Graph graph = new Graph(6);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(3, 5);
        
        KahnTopologicalSort topoSort = new KahnTopologicalSort(graph);
        List<Integer> order = topoSort.sort();
        
        assertEquals(6, order.size(), "Should have all 6 vertices");
        assertTrue(isValidTopologicalOrder(graph, order), 
                  "Order should be valid topological ordering");
    }

    // Helper method to validate topological order
    private boolean isValidTopologicalOrder(Graph graph, List<Integer> order) {
        if (order.size() != graph.getVertexCount()) {
            return false;
        }
        
        // Create position map
        int[] position = new int[graph.getVertexCount()];
        for (int i = 0; i < order.size(); i++) {
            position[order.get(i)] = i;
        }
        
        // Check all edges: for edge u->v, position[u] must be < position[v]
        for (int u = 0; u < graph.getVertexCount(); u++) {
            for (Graph.Edge edge : graph.getEdges(u)) {
                int v = edge.to;
                if (position[u] >= position[v]) {
                    return false;
                }
            }
        }
        
        return true;
    }
}
