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

---

#  Prerequisites
SQLite JDBC driver (sqlite-jdbc-3.51.0.0.jar)

---

# Initial State:
```
Database connected and ready
Departments loaded (3 items)
Projects loaded (6 items)
Ready for user to make selections and search
No departments selected
No projects selected
"Not" checkboxes unchecked
No employee results displayed
```

---

# How to Run
```

1. git clone <repo link>>
2. cd to directory

for windows
3. sqlite3 resources\company.db < resources\company.sql
4. javac -cp "lib\sqlite-jdbc-3.51.0.0.jar" -d . src\database\DatabaseManager.java rc\query\EmployeeSearchQuery.java ui\EmployeeSearchFrame.java Main.java
5. java -cp ".;lib\sqlite-jdbc-3.51.0.0.jar" Main

for mac
3. sqlite3 resources/company.db < resources/company.sql
4. javac -cp "lib/sqlite-jdbc-3.51.0.0.jar" -d . src/database/DatabaseManager.java src/query/EmployeeSearchQuery.java ui/EmployeeSearchFrame.java Main.java
5. java -cp ".:lib/sqlite-jdbc-3.51.0.0.jar" Main

```