# Installation and Setup Guide

## Prerequisites

This project requires:
- **Java JDK 11 or higher**
- **Apache Maven 3.6 or higher**

## Installing Java

### Windows

1. Download Java JDK from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [Adoptium](https://adoptium.net/)
2. Run the installer
3. Add Java to PATH:
   - Open System Properties → Advanced → Environment Variables
   - Add `JAVA_HOME` pointing to JDK installation directory
   - Add `%JAVA_HOME%\bin` to PATH

### Verify Installation
```powershell
java -version
```

## Installing Maven

### Windows

**Option 1: Download Binary**
1. Download Maven from [Apache Maven](https://maven.apache.org/download.cgi)
2. Extract to `C:\Program Files\Apache\maven`
3. Add to PATH:
   - Open System Properties → Advanced → Environment Variables
   - Add `MAVEN_HOME` = `C:\Program Files\Apache\maven`
   - Add `%MAVEN_HOME%\bin` to PATH

**Option 2: Using Chocolatey**
```powershell
choco install maven
```

**Option 3: Using Scoop**
```powershell
scoop install maven
```

### Verify Installation
```powershell
mvn -version
```

Expected output:
```
Apache Maven 3.x.x
Maven home: C:\...
Java version: 11.x.x, vendor: ...
```

## Building the Project

### Option 1: Using Batch Script (Recommended)
```powershell
.\build-and-run.bat
```

### Option 2: Manual Maven Commands

**Clean and Compile:**
```powershell
mvn clean compile
```

**Run Tests:**
```powershell
mvn test
```

**Run Main Program:**
```powershell
mvn exec:java -Dexec.mainClass="Main"
```

**Package as JAR:**
```powershell
mvn package
```

## Project Structure

After building, your directory structure should look like:
```
asik4daa/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── Main.java
│   │       └── graph/
│   │           ├── common/
│   │           ├── scc/
│   │           ├── topo/
│   │           └── dagsp/
│   └── test/
│       └── java/
│           └── graph/
├── data/                    # 9 JSON datasets
├── target/                  # Compiled classes (auto-generated)
├── pom.xml
├── README.md
└── REPORT.md
```

## Running the Application

The application automatically processes all JSON files in the `data/` directory.

**Expected output:**
- SCC detection results for each graph
- Topological ordering of components
- Shortest and longest paths
- Performance metrics

## Troubleshooting

### Maven not found
- Ensure Maven is installed and in PATH
- Restart terminal/IDE after PATH changes
- Try `where mvn` (Windows) or `which mvn` (Unix) to verify

### Java version mismatch
- Ensure Java 11+ is installed
- Check `java -version`
- Set `JAVA_HOME` correctly

### Compilation errors
- Run `mvn clean` to remove old build artifacts
- Check internet connection (Maven downloads dependencies)
- Delete `~/.m2/repository` if dependency corruption suspected

### Tests failing
- Review test output in `target/surefire-reports/`
- Tests are designed to catch implementation errors
- All tests should pass in correct implementation

## IDE Setup

### IntelliJ IDEA
1. Open project folder
2. IDE will auto-detect Maven project
3. Wait for dependency download
4. Run `Main.java` or use Maven tool window

### Eclipse
1. File → Import → Existing Maven Project
2. Select project directory
3. Right-click project → Maven → Update Project
4. Run as Java Application

### VS Code
1. Install "Java Extension Pack"
2. Install "Maven for Java"
3. Open project folder
4. Use Maven sidebar or run via terminal

## Additional Resources

- [Maven Getting Started](https://maven.apache.org/guides/getting-started/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Java 11 Documentation](https://docs.oracle.com/en/java/javase/11/)
