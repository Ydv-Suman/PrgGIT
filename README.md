# PrgGIT

# 📁 Project Structure
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
│   ├── service/
│   │   └── EmployeeSearchService.java  # Loads lists, builds queries, searches DB
│   │
│   ├── ui/
│   │   └── EmployeeSearchFrame.java    # Main GUI frame
│   │
│   └── Main.java                       #  Starts the program
│
└── README.md
```
