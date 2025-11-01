# 🏙️ Smart City/Smart Campus Scheduling System

[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

A comprehensive implementation of graph algorithms for solving task scheduling problems with cyclic dependencies in smart city and campus environments.

## 📋 Features

- **Strongly Connected Components (SCC)** detection using Tarjan's algorithm
- **Topological Sorting** using Kahn's algorithm
- **Shortest/Longest Path** computation in DAGs
- **Complete metrics and instrumentation** for performance analysis
- **9 pre-configured test datasets** covering various graph structures
- **Comprehensive JUnit test suite**
- **Detailed analysis and reporting**

## 🎯 Problem Statement

Smart cities manage complex service tasks (street cleaning, repairs, sensor maintenance) with interdependencies that may contain cycles. This project provides algorithms to:

1. **Detect circular dependencies** (Strongly Connected Components)
2. **Compress cycles** into manageable units (Condensation Graph)
3. **Find valid execution order** (Topological Sort)
4. **Optimize scheduling** (Shortest/Longest Paths)

## 🚀 Quick Start

### Prerequisites

- Java JDK 11 or higher
- Apache Maven 3.6 or higher

See [INSTALL.md](INSTALL.md) for detailed installation instructions.

### Build and Run

**Windows:**
```powershell
.\build-and-run.bat
```

**Manual (all platforms):**
```bash
mvn clean compile
mvn test
mvn exec:java -Dexec.mainClass="Main"
```

## 📊 Datasets

The project includes 9 carefully designed test datasets:

### Small Graphs (6-10 vertices)
- `small_cycle.json` - Graph with one cycle
- `small_dag.json` - Pure acyclic graph
- `small_mixed.json` - Mixed with 2 SCCs

### Medium Graphs (10-20 vertices)
- `medium_sparse.json` - Sparse with 3 SCCs
- `medium_dense.json` - Dense with multiple SCCs
- `medium_dag.json` - Large pure DAG

### Large Graphs (20-50 vertices)
- `large_sparse.json` - 30 vertices, 4 SCCs
- `large_dense.json` - 40 vertices, 6 SCCs
- `large_dag.json` - 50 vertices, performance testing

## 💻 Usage Example

```java
// Load graph
Graph graph = GraphLoader.loadFromJson("data/tasks.json");

// Find SCCs
TarjanSCC scc = new TarjanSCC(graph);
List<List<Integer>> components = scc.findSCCs();
scc.printSCCs();

// Build condensation (DAG)
Graph dag = scc.buildCondensation();

// Topological sort
KahnTopologicalSort topo = new KahnTopologicalSort(dag);
List<Integer> order = topo.sort();

// Find critical path
DAGShortestPath paths = new DAGShortestPath(dag);
PathResult critical = paths.longestPath(0);
critical.printCriticalPath();

// Get performance metrics
scc.getMetrics().printMetrics();
```

See [QUICKSTART.md](QUICKSTART.md) for more examples.

## 🏗️ Project Structure

```
asik4daa/
├── src/
│   ├── main/java/
│   │   ├── Main.java                    # Main application
│   │   └── graph/
│   │       ├── common/                  # Common utilities
│   │       │   ├── Graph.java          # Graph data structure
│   │       │   ├── GraphLoader.java    # JSON loader
│   │       │   ├── Metrics.java        # Performance metrics
│   │       │   └── Examples.java       # Usage examples
│   │       ├── scc/                     # SCC detection
│   │       │   └── TarjanSCC.java      # Tarjan's algorithm
│   │       ├── topo/                    # Topological sorting
│   │       │   └── KahnTopologicalSort.java
│   │       └── dagsp/                   # DAG paths
│   │           └── DAGShortestPath.java
│   └── test/java/graph/                 # JUnit tests
├── data/                                 # 9 JSON datasets
├── pom.xml                              # Maven configuration
├── README.md                            # This file
├── REPORT.md                            # Detailed analysis
├── QUICKSTART.md                        # Usage guide
└── INSTALL.md                           # Installation guide
```

## 🧪 Testing

Run all tests:
```bash
mvn test
```

Run specific test:
```bash
mvn test -Dtest=TarjanSCCTest
```

All algorithms include comprehensive unit tests with edge cases.

## 📈 Performance

All algorithms achieve **O(V + E)** time complexity:

- **Tarjan SCC**: Single DFS pass
- **Kahn Topo Sort**: Single pass with queue
- **DAG Paths**: DP over topological order

See [REPORT.md](REPORT.md) for detailed performance analysis.

## 📖 Documentation

- **[README.md](README.md)** - This overview
- **[INSTALL.md](INSTALL.md)** - Installation and setup
- **[QUICKSTART.md](QUICKSTART.md)** - Quick usage guide
- **[REPORT.md](REPORT.md)** - Complete analysis and results

## 🎓 Algorithms Implemented

### 1. Tarjan's SCC Algorithm
- Detects strongly connected components
- Time: O(V + E), Space: O(V)
- Single DFS pass with low-link values

### 2. Kahn's Topological Sort
- Computes topological ordering
- Time: O(V + E), Space: O(V)
- BFS-based with in-degree tracking

### 3. DAG Shortest/Longest Paths
- Finds optimal paths in DAGs
- Time: O(V + E), Space: O(V)
- DP over topological order

## 🔧 Extending the Project

### Adding Custom Datasets

Create JSON file in `data/`:
```json
{
  "vertices": 5,
  "edges": [
    {"from": 0, "to": 1, "weight": 2.0}
  ],
  "labels": {
    "0": "Task Name"
  }
}
```

### Generating Random Datasets

```bash
mvn exec:java -Dexec.mainClass="graph.common.DatasetGenerator"
```

## 📊 Results Summary

| Algorithm | Complexity | Best For |
|-----------|------------|----------|
| Tarjan SCC | O(V + E) | Cycle detection |
| Kahn Topo | O(V + E) | Task ordering |
| DAG Paths | O(V + E) | Critical path |

All algorithms tested on graphs up to 50 vertices with sub-millisecond performance.

## 👥 Author

Developed as part of Algorithms and Data Structures course (2025)

## 📝 License

MIT License - see [LICENSE](LICENSE) file for details

## 🤝 Contributing

This is an academic project. Feel free to fork and extend for educational purposes.

## 🐛 Troubleshooting

**Maven not found:**
- Install from [maven.apache.org](https://maven.apache.org/)
- Add to PATH environment variable

**Compilation errors:**
- Run `mvn clean` first
- Check Java version: `java -version`
- Verify JDK 11+ is installed

**Tests failing:**
- Check `target/surefire-reports/` for details
- All tests should pass with correct implementation

See [INSTALL.md](INSTALL.md) for more troubleshooting tips.

## 📚 References

1. Tarjan, R. (1972). "Depth-first search and linear graph algorithms"
2. Kahn, A. B. (1962). "Topological sorting of large networks"
3. Cormen, T. H., et al. (2009). "Introduction to Algorithms" (3rd ed.)

---

**⭐ If you find this project useful, please give it a star!**
