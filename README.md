# PrgGIT

## 🚀 Quick Start

**One command to compile and run:**
```bash
javac -cp ".:lib/sqlite-jdbc-3.51.0.0.jar" Main.java ui/EmployeeSearchFrame.java src/database/DatabaseManager.java src/query/EmployeeSearchQuery.java && java -cp ".:ui:src/database:src/query:lib/sqlite-jdbc-3.51.0.0.jar" Main
```

**Or compile and run separately:**
```bash
# Compile
javac -cp ".:lib/sqlite-jdbc-3.51.0.0.jar" Main.java ui/EmployeeSearchFrame.java src/database/DatabaseManager.java src/query/EmployeeSearchQuery.java

# Run
java -cp ".:ui:src/database:src/query:lib/sqlite-jdbc-3.51.0.0.jar" Main
```

**To use the application:**
1. Enter `company` in the Database field
2. Click "Fill" to load departments and projects
3. Select departments/projects and click "Search"
4. Use "Clear" to reset selections

## 📁 Project Structure
```
PrgGIT/
│
├── lib/
│   └── sqlite-jdbc.jar                 # SQLite JDBC driver
│
├── resources/
│   ├── company.sql                     # SQL schema + sample data
│   └── company.db                      # Generated SQLite database
│
├── src/
│   ├── database/
│   │   └── DatabaseManager.java        # Handles SQLite connection
│   │
│   ├── query/
│   │   └── EmployeeSearchQuery.java  # Loads lists, builds queries, searches DB
│
├── ui/
│   └── EmployeeSearchFrame.java    # Main GUI frame
│
│── Main.java                       #  Starts the program
│
└── README.md
```
