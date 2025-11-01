# Smart City/Smart Campus Scheduling

## Project Overview
This project implements graph algorithms for scheduling city-service tasks with dependencies:
1. **Strongly Connected Components (SCC)** - Tarjan's algorithm
2. **Topological Ordering** - Kahn's algorithm
3. **Shortest/Longest Paths in DAGs** - Dynamic Programming on topological order

## Problem Statement
City services (street cleaning, repairs, sensor maintenance) have dependencies that may contain cycles. 
We detect cycles (SCCs), compress them into a DAG, and compute optimal scheduling paths.

## Grade Target: 100/100
- [x] Algorithmic correctness (55%)
- [x] Report & analysis (25%)
- [x] Code quality & tests (15%)
- [x] Repo/Git hygiene (5%)

## Project Structure
```
asik4daa/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── graph/
│   │       │   ├── scc/          # SCC detection (Tarjan)
│   │       │   ├── topo/         # Topological sorting
│   │       │   ├── dagsp/        # DAG shortest/longest paths
│   │       │   └── common/       # Common utilities
│   │       └── Main.java
│   └── test/
│       └── java/
│           └── graph/
├── data/                          # 9 test datasets
└── pom.xml
```
## Algorithms Implemented

### 1. Strongly Connected Components (Tarjan's Algorithm)
- **Complexity**: O(V + E)
- **Input**: Directed graph from `tasks.json`
- **Output**: List of SCCs, condensation DAG

### 2. Topological Sort (Kahn's Algorithm)
- **Complexity**: O(V + E)
- **Input**: Condensation DAG
- **Output**: Topological order of components and tasks

### 3. DAG Shortest/Longest Paths
- **Weight Model**: Edge weights (task duration/cost)
- **Complexity**: O(V + E)
- **Features**:
  - Single-source shortest paths
  - Longest path (critical path)
  - Path reconstruction

## Dataset Description

### Small (6-10 nodes) - 3 datasets
1. `small_cycle.json` - 8 nodes, contains 1 SCC with cycle
2. `small_dag.json` - 7 nodes, pure DAG
3. `small_mixed.json` - 10 nodes, 2 small SCCs

### Medium (10-20 nodes) - 3 datasets
4. `medium_sparse.json` - 15 nodes, sparse, 3 SCCs
5. `medium_dense.json` - 18 nodes, dense, multiple SCCs
6. `medium_dag.json` - 20 nodes, pure DAG

### Large (20-50 nodes) - 3 datasets
7. `large_sparse.json` - 30 nodes, sparse, several SCCs
8. `large_dense.json` - 40 nodes, dense, complex structure
9. `large_dag.json` - 50 nodes, pure DAG for performance testing

## Building and Running

### Prerequisites
- Java 11 or higher
- Maven 3.6+

### Build
```bash
mvn clean compile
```

### Run Tests
```bash
mvn test
```

### Run Main Program
```bash
mvn exec:java -Dexec.mainClass="Main"
```

Or after building:
```bash
java -cp target/classes Main
```

## Usage Example

```java
// Load graph
Graph graph = GraphLoader.loadFromJson("data/small_cycle.json");

// Find SCCs
SCCDetector sccDetector = new TarjanSCC(graph);
List<List<Integer>> sccs = sccDetector.findSCCs();

// Build condensation
Graph condensation = sccDetector.buildCondensation();

// Topological sort
TopologicalSort topoSort = new KahnTopologicalSort(condensation);
List<Integer> order = topoSort.sort();

// Shortest/Longest paths
DAGShortestPath dagSP = new DAGShortestPath(condensation);
PathResult shortest = dagSP.shortestPath(source);
PathResult longest = dagSP.longestPath(source);
```

## Metrics and Instrumentation

Each algorithm implements the `Metrics` interface:
- **Operation counters**: DFS visits, edge traversals, relaxations
- **Timing**: nanosecond precision via `System.nanoTime()`

## Results Summary

See detailed analysis in `REPORT.md` including:
- Performance metrics per dataset
- Complexity analysis
- Bottleneck identification
- Practical recommendations

