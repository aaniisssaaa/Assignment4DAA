package graph.dagsp;

import graph.common.Graph;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DAGShortestPath algorithm.
 */
class DAGShortestPathTest {

    @Test
    @DisplayName("Test shortest path in linear DAG")
    void testShortestPathLinear() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 2.0);
        graph.addEdge(1, 2, 3.0);
        graph.addEdge(2, 3, 1.0);
        
        DAGShortestPath dagSP = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagSP.shortestPath(0);
        
        assertEquals(0.0, result.getDistance(0), 0.001);
        assertEquals(2.0, result.getDistance(1), 0.001);
        assertEquals(5.0, result.getDistance(2), 0.001);
        assertEquals(6.0, result.getDistance(3), 0.001);
    }

    @Test
    @DisplayName("Test shortest path in diamond DAG")
    void testShortestPathDiamond() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 5.0);
        graph.addEdge(0, 2, 2.0);
        graph.addEdge(1, 3, 1.0);
        graph.addEdge(2, 3, 4.0);
        
        DAGShortestPath dagSP = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagSP.shortestPath(0);
        
        assertEquals(0.0, result.getDistance(0), 0.001);
        assertEquals(6.0, result.getDistance(3), 0.001); // 0->2->3 = 2+4 = 6
        
        List<Integer> path = result.getPath(3);
        assertTrue(path.contains(0) && path.contains(2) && path.contains(3), 
                  "Path should go through 0, 2, 3");
    }

    @Test
    @DisplayName("Test longest path in linear DAG")
    void testLongestPathLinear() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 2.0);
        graph.addEdge(1, 2, 3.0);
        graph.addEdge(2, 3, 1.0);
        
        DAGShortestPath dagSP = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagSP.longestPath(0);
        
        assertEquals(0.0, result.getDistance(0), 0.001);
        assertEquals(2.0, result.getDistance(1), 0.001);
        assertEquals(5.0, result.getDistance(2), 0.001);
        assertEquals(6.0, result.getDistance(3), 0.001);
    }

    @Test
    @DisplayName("Test longest path in diamond DAG")
    void testLongestPathDiamond() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 5.0);
        graph.addEdge(0, 2, 2.0);
        graph.addEdge(1, 3, 1.0);
        graph.addEdge(2, 3, 4.0);
        
        DAGShortestPath dagSP = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagSP.longestPath(0);
        
        assertEquals(0.0, result.getDistance(0), 0.001);
        assertEquals(6.0, result.getDistance(3), 0.001); // 0->1->3 = 5+1 = 6
        
        List<Integer> path = result.getPath(3);
        assertTrue(path.contains(0) && path.contains(1) && path.contains(3), 
                  "Path should go through 0, 1, 3");
    }

    @Test
    @DisplayName("Test unreachable vertices")
    void testUnreachableVertices() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 2.0);
        graph.addEdge(2, 3, 1.0);
        
        DAGShortestPath dagSP = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagSP.shortestPath(0);
        
        assertFalse(result.isUnreachable(0));
        assertFalse(result.isUnreachable(1));
        assertTrue(result.isUnreachable(2));
        assertTrue(result.isUnreachable(3));
    }

    @Test
    @DisplayName("Test path reconstruction")
    void testPathReconstruction() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 1.0);
        graph.addEdge(1, 2, 1.0);
        graph.addEdge(2, 3, 1.0);
        graph.addEdge(3, 4, 1.0);
        
        DAGShortestPath dagSP = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagSP.shortestPath(0);
        
        List<Integer> path = result.getPath(4);
        assertEquals(5, path.size(), "Path should have 5 vertices");
        assertEquals(0, path.get(0), "Path should start at 0");
        assertEquals(4, path.get(4), "Path should end at 4");
        
        // Check path is sequential
        for (int i = 0; i < path.size(); i++) {
            assertEquals(i, path.get(i), "Path should be 0->1->2->3->4");
        }
    }

    @Test
    @DisplayName("Test single vertex")
    void testSingleVertex() {
        Graph graph = new Graph(1);
        
        DAGShortestPath dagSP = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagSP.shortestPath(0);
        
        assertEquals(0.0, result.getDistance(0), 0.001);
        
        List<Integer> path = result.getPath(0);
        assertEquals(1, path.size());
        assertEquals(0, path.get(0));
    }

    @Test
    @DisplayName("Test cycle detection throws exception")
    void testCycleThrowsException() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, 1.0);
        graph.addEdge(1, 2, 1.0);
        graph.addEdge(2, 0, 1.0); // Creates cycle
        
        DAGShortestPath dagSP = new DAGShortestPath(graph);
        
        assertThrows(IllegalStateException.class, () -> {
            dagSP.shortestPath(0);
        }, "Should throw exception for graph with cycle");
    }

    @Test
    @DisplayName("Test multiple paths - shortest")
    void testMultiplePathsShortest() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 10.0);
        graph.addEdge(0, 2, 5.0);
        graph.addEdge(1, 3, 1.0);
        graph.addEdge(2, 1, 2.0);
        graph.addEdge(2, 3, 9.0);
        
        DAGShortestPath dagSP = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagSP.shortestPath(0);
        
        // Shortest to 3: 0->2->1->3 = 5+2+1 = 8
        assertEquals(8.0, result.getDistance(3), 0.001);
    }

    @Test
    @DisplayName("Test multiple paths - longest")
    void testMultiplePathsLongest() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 10.0);
        graph.addEdge(0, 2, 5.0);
        graph.addEdge(1, 3, 1.0);
        graph.addEdge(2, 1, 2.0);
        graph.addEdge(2, 3, 9.0);
        
        DAGShortestPath dagSP = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagSP.longestPath(0);
        
        // Longest to 3: 0->2->3 = 5+9 = 14
        assertEquals(14.0, result.getDistance(3), 0.001);
    }
}
