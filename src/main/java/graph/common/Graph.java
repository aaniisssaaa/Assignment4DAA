package graph.common;

import java.util.*;

/**
 * Represents a directed weighted graph.
 */
public class Graph {
    private final int vertices;
    private final Map<Integer, List<Edge>> adjacencyList;
    private final Map<Integer, String> nodeLabels;

    /**
     * Creates a graph with the specified number of vertices.
     * @param vertices number of vertices
     */
    public Graph(int vertices) {
        this.vertices = vertices;
        this.adjacencyList = new HashMap<>();
        this.nodeLabels = new HashMap<>();
        for (int i = 0; i < vertices; i++) {
            adjacencyList.put(i, new ArrayList<>());
        }
    }

    /**
     * Adds a directed edge from source to destination with weight.
     * @param from source vertex
     * @param to destination vertex
     * @param weight edge weight
     */
    public void addEdge(int from, int to, double weight) {
        if (from < 0 || from >= vertices || to < 0 || to >= vertices) {
            throw new IllegalArgumentException("Invalid vertex: " + from + " or " + to);
        }
        adjacencyList.get(from).add(new Edge(to, weight));
    }

    /**
     * Adds a directed edge with default weight of 1.0.
     * @param from source vertex
     * @param to destination vertex
     */
    public void addEdge(int from, int to) {
        addEdge(from, to, 1.0);
    }

    /**
     * Sets a label for a vertex.
     * @param vertex the vertex
     * @param label the label
     */
    public void setNodeLabel(int vertex, String label) {
        nodeLabels.put(vertex, label);
    }

    /**
     * Gets the label for a vertex.
     * @param vertex the vertex
     * @return the label or vertex number as string if no label exists
     */
    public String getNodeLabel(int vertex) {
        return nodeLabels.getOrDefault(vertex, String.valueOf(vertex));
    }

    /**
     * Gets all edges from a vertex.
     * @param vertex the source vertex
     * @return list of edges
     */
    public List<Edge> getEdges(int vertex) {
        return adjacencyList.getOrDefault(vertex, Collections.emptyList());
    }

    /**
     * Gets the number of vertices.
     * @return vertex count
     */
    public int getVertexCount() {
        return vertices;
    }

    /**
     * Gets the total number of edges.
     * @return edge count
     */
    public int getEdgeCount() {
        int count = 0;
        for (List<Edge> edges : adjacencyList.values()) {
            count += edges.size();
        }
        return count;
    }

    /**
     * Checks if the graph has an edge from source to destination.
     * @param from source vertex
     * @param to destination vertex
     * @return true if edge exists
     */
    public boolean hasEdge(int from, int to) {
        for (Edge edge : getEdges(from)) {
            if (edge.to == to) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a reverse graph (all edges reversed).
     * @return reversed graph
     */
    public Graph reverse() {
        Graph reversed = new Graph(vertices);
        for (int from = 0; from < vertices; from++) {
            for (Edge edge : getEdges(from)) {
                reversed.addEdge(edge.to, from, edge.weight);
            }
        }
        // Copy labels
        for (Map.Entry<Integer, String> entry : nodeLabels.entrySet()) {
            reversed.setNodeLabel(entry.getKey(), entry.getValue());
        }
        return reversed;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph with ").append(vertices).append(" vertices and ")
          .append(getEdgeCount()).append(" edges:\n");
        for (int i = 0; i < vertices; i++) {
            sb.append(i).append(" (").append(getNodeLabel(i)).append("): ");
            for (Edge edge : getEdges(i)) {
                sb.append("->").append(edge.to).append("(w=").append(edge.weight).append(") ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Represents a directed edge with weight.
     */
    public static class Edge {
        public final int to;
        public final double weight;

        public Edge(int to, double weight) {
            this.to = to;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "Edge{to=" + to + ", weight=" + weight + "}";
        }
    }
}
