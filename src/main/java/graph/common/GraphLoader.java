package graph.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for loading graphs from JSON files.
 * Expected format:
 * {
 *   "vertices": 5,
 *   "edges": [
 *     {"from": 0, "to": 1, "weight": 2.5},
 *     {"from": 1, "to": 2}
 *   ],
 *   "labels": {
 *     "0": "Task A",
 *     "1": "Task B"
 *   }
 * }
 */
public class GraphLoader {
    
    /**
     * Loads a graph from a JSON file.
     * @param filename path to the JSON file
     * @return the loaded graph
     * @throws IOException if file cannot be read
     */
    public static Graph loadFromJson(String filename) throws IOException {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filename)) {
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            
            // Get number of vertices
            int vertices = json.get("vertices").getAsInt();
            Graph graph = new Graph(vertices);
            
            // Load edges
            JsonArray edges = json.getAsJsonArray("edges");
            for (JsonElement edgeElement : edges) {
                JsonObject edge = edgeElement.getAsJsonObject();
                int from = edge.get("from").getAsInt();
                int to = edge.get("to").getAsInt();
                
                // Weight is optional, defaults to 1.0
                double weight = 1.0;
                if (edge.has("weight")) {
                    weight = edge.get("weight").getAsDouble();
                }
                
                graph.addEdge(from, to, weight);
            }
            
            // Load labels if present
            if (json.has("labels")) {
                JsonObject labels = json.getAsJsonObject("labels");
                for (Map.Entry<String, JsonElement> entry : labels.entrySet()) {
                    int vertex = Integer.parseInt(entry.getKey());
                    String label = entry.getValue().getAsString();
                    graph.setNodeLabel(vertex, label);
                }
            }
            
            return graph;
        }
    }

    /**
     * Creates a simple test graph for debugging.
     * @return a small test graph
     */
    public static Graph createTestGraph() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 2.0);
        graph.addEdge(1, 2, 3.0);
        graph.addEdge(2, 3, 1.0);
        graph.addEdge(3, 1, 1.0); // Creates a cycle
        graph.addEdge(0, 4, 5.0);
        
        graph.setNodeLabel(0, "Start");
        graph.setNodeLabel(1, "Task1");
        graph.setNodeLabel(2, "Task2");
        graph.setNodeLabel(3, "Task3");
        graph.setNodeLabel(4, "End");
        
        return graph;
    }
}
