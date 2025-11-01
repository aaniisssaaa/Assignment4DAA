package graph.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Graph data structure.
 */
class GraphTest {

    @Test
    @DisplayName("Test graph creation")
    void testGraphCreation() {
        Graph graph = new Graph(5);
        assertEquals(5, graph.getVertexCount());
        assertEquals(0, graph.getEdgeCount());
    }

    @Test
    @DisplayName("Test adding edges")
    void testAddEdges() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, 2.5);
        graph.addEdge(1, 2, 3.0);
        
        assertEquals(2, graph.getEdgeCount());
        assertTrue(graph.hasEdge(0, 1));
        assertTrue(graph.hasEdge(1, 2));
        assertFalse(graph.hasEdge(2, 0));
    }

    @Test
    @DisplayName("Test edge weight")
    void testEdgeWeight() {
        Graph graph = new Graph(2);
        graph.addEdge(0, 1, 5.5);
        
        Graph.Edge edge = graph.getEdges(0).get(0);
        assertEquals(5.5, edge.weight, 0.001);
    }

    @Test
    @DisplayName("Test node labels")
    void testNodeLabels() {
        Graph graph = new Graph(3);
        graph.setNodeLabel(0, "Start");
        graph.setNodeLabel(1, "Middle");
        graph.setNodeLabel(2, "End");
        
        assertEquals("Start", graph.getNodeLabel(0));
        assertEquals("Middle", graph.getNodeLabel(1));
        assertEquals("End", graph.getNodeLabel(2));
    }

    @Test
    @DisplayName("Test reverse graph")
    void testReverseGraph() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, 2.0);
        graph.addEdge(1, 2, 3.0);
        
        Graph reversed = graph.reverse();
        
        assertEquals(graph.getVertexCount(), reversed.getVertexCount());
        assertEquals(graph.getEdgeCount(), reversed.getEdgeCount());
        
        assertTrue(reversed.hasEdge(1, 0));
        assertTrue(reversed.hasEdge(2, 1));
        assertFalse(reversed.hasEdge(0, 1));
    }

    @Test
    @DisplayName("Test invalid vertex throws exception")
    void testInvalidVertex() {
        Graph graph = new Graph(3);
        
        assertThrows(IllegalArgumentException.class, () -> {
            graph.addEdge(-1, 1, 1.0);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            graph.addEdge(0, 5, 1.0);
        });
    }

    @Test
    @DisplayName("Test multiple edges same direction")
    void testMultipleEdges() {
        Graph graph = new Graph(2);
        graph.addEdge(0, 1, 2.0);
        graph.addEdge(0, 1, 3.0);  // Second edge same direction
        
        assertEquals(2, graph.getEdgeCount());
        assertEquals(2, graph.getEdges(0).size());
    }

    @Test
    @DisplayName("Test empty graph edges")
    void testEmptyGraphEdges() {
        Graph graph = new Graph(3);
        
        assertTrue(graph.getEdges(0).isEmpty());
        assertTrue(graph.getEdges(1).isEmpty());
        assertTrue(graph.getEdges(2).isEmpty());
    }

    @Test
    @DisplayName("Test default edge weight")
    void testDefaultEdgeWeight() {
        Graph graph = new Graph(2);
        graph.addEdge(0, 1);  // No weight specified
        
        Graph.Edge edge = graph.getEdges(0).get(0);
        assertEquals(1.0, edge.weight, 0.001);
    }
}
