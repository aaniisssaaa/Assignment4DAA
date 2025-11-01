# Quick Start Guide

## Running the Application

### Method 1: Automated Script (Windows)
```powershell
.\build-and-run.bat
```

### Method 2: Maven Commands
```powershell
# Compile
mvn clean compile

# Run tests
mvn test

# Run main application
mvn exec:java -Dexec.mainClass="Main"

# Run examples
mvn exec:java -Dexec.mainClass="graph.common.Examples"
```

## Using the Library

### Basic Example

```java
import graph.common.*;
import graph.scc.TarjanSCC;
import graph.topo.KahnTopologicalSort;
import graph.dagsp.DAGShortestPath;

// 1. Load or create a graph
Graph graph = GraphLoader.loadFromJson("data/tasks.json");

// 2. Find strongly connected components
TarjanSCC scc = new TarjanSCC(graph);
List<List<Integer>> components = scc.findSCCs();
scc.printSCCs();

// 3. Build condensation (DAG)
Graph dag = scc.buildCondensation();

// 4. Topological sort
KahnTopologicalSort topo = new KahnTopologicalSort(dag);
List<Integer> order = topo.sort();

// 5. Find shortest/longest paths
DAGShortestPath paths = new DAGShortestPath(dag);
PathResult shortest = paths.shortestPath(0);
PathResult longest = paths.longestPath(0);

// 6. Get metrics
scc.getMetrics().printMetrics();
```

### Creating Custom Graphs

```java
// Create graph with 5 vertices
Graph graph = new Graph(5);

// Add labeled nodes
graph.setNodeLabel(0, "Start");
graph.setNodeLabel(1, "Task A");
graph.setNodeLabel(2, "Task B");

// Add edges (directed, with weights)
graph.addEdge(0, 1, 2.5);  // from 0 to 1, weight 2.5
graph.addEdge(1, 2, 3.0);
graph.addEdge(2, 1, 1.0);  // Creates a cycle

// Save to JSON (manual format)
{
  "vertices": 5,
  "edges": [
    {"from": 0, "to": 1, "weight": 2.5},
    {"from": 1, "to": 2, "weight": 3.0}
  ],
  "labels": {
    "0": "Start",
    "1": "Task A"
  }
}
```

## Understanding Output

### SCC Detection Output
```
=== Strongly Connected Components ===
Total SCCs found: 3
SCC 0 (size=1): [0(Start)]
SCC 1 (size=3): [1(TaskA), 2(TaskB), 3(TaskC)]  <- Cycle
SCC 2 (size=2): [4(End), 5(Report)]
```

### Topological Sort Output
```
=== Topological Order ===
Order: 0(SCC0) -> 1(SCC1) -> 2(SCC2)
```

### Path Finding Output
```
=== Critical Path ===
Length: 15.50
Path: 0(Start) -> 1(Process) -> 3(Verify) -> 5(Complete)
```

### Metrics Output
```
=== Metrics Report ===
Execution Time: 0.234 ms (234567 ns)
Operation Counters:
  dfs_visits: 8
  edges_traversed: 12
  sccs_found: 3
```

## Common Use Cases

### 1. Detect Circular Dependencies
```java
TarjanSCC scc = new TarjanSCC(graph);
scc.findSCCs();
if (scc.getSCCs().size() < graph.getVertexCount()) {
    System.out.println("Warning: Circular dependencies detected!");
}
```

### 2. Find Valid Execution Order
```java
TarjanSCC scc = new TarjanSCC(graph);
scc.findSCCs();
Graph dag = scc.buildCondensation();

KahnTopologicalSort topo = new KahnTopologicalSort(dag);
List<Integer> order = topo.sort();

if (!order.isEmpty()) {
    System.out.println("Valid execution order found!");
}
```

### 3. Find Critical Path (Project Duration)
```java
// After getting condensation DAG
DAGShortestPath paths = new DAGShortestPath(dag);
PathResult critical = paths.longestPath(startComponent);

double projectDuration = critical.getDistance(endComponent);
List<Integer> criticalPath = critical.getPath(endComponent);

System.out.println("Project will take: " + projectDuration + " days");
System.out.println("Critical path: " + criticalPath);
```

### 4. Optimize Resource Allocation
```java
// Find shortest path for cost optimization
PathResult shortestCost = paths.shortestPath(source);
double minCost = shortestCost.getDistance(target);

// Find longest path for time management
PathResult longestTime = paths.longestPath(source);
double maxTime = longestTime.getDistance(target);

System.out.println("Best case cost: " + minCost);
System.out.println("Worst case time: " + maxTime);
```

## Testing

Run unit tests:
```powershell
mvn test
```

Run specific test class:
```powershell
mvn test -Dtest=TarjanSCCTest
mvn test -Dtest=KahnTopologicalSortTest
mvn test -Dtest=DAGShortestPathTest
```

## Data Files

Place JSON files in `data/` directory with this format:

```json
{
  "vertices": 5,
  "edges": [
    {"from": 0, "to": 1, "weight": 2.0},
    {"from": 1, "to": 2, "weight": 3.0}
  ],
  "labels": {
    "0": "TaskName",
    "1": "AnotherTask"
  }
}
```

## Troubleshooting

**Q: Tests are failing**
- Run `mvn clean test` to rebuild
- Check test output in `target/surefire-reports/`

**Q: Graph has cycles but I need a valid order**
- Use SCC detection first
- Work with the condensation graph
- Each SCC becomes one schedulable unit

**Q: Performance is slow**
- Check graph size (V + E)
- For V > 10,000, consider optimization
- Use iterative instead of recursive DFS

**Q: Path not found**
- Ensure source and target are connected
- Check if graph is a DAG (no cycles)
- Verify edge directions are correct

## Next Steps

1. **Read REPORT.md** for detailed algorithm analysis
2. **Check Examples.java** for more usage patterns
3. **Modify datasets** in `data/` to test your scenarios
4. **Extend algorithms** for your specific needs

## Support

For questions or issues:
- Check INSTALL.md for setup problems
- Review test cases for usage examples
- See REPORT.md for algorithm details
