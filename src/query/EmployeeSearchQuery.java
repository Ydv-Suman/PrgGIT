package src.query;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import src.database.DatabaseManager;

public class EmployeeSearchQuery {

    // Load department names from database
    public List<String> loadDepartments(String dbName) {
        List<String> departments = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        
        try {
            conn = DatabaseManager.getConnection(dbName);
            if(!conn.isClosed()) {
                System.out.println("Successfully connected to database for departments");
                stmt = conn.createStatement();
                String query = """
                   SELECT Dname FROM DEPARTMENT ORDER BY Dname;
                   """;
                result = stmt.executeQuery(query);
                
                while(result.next()) {
                    departments.add(result.getString("Dname"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (conn != null && !conn.isClosed()) {
                    DatabaseManager.closeConnection();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return departments;
    }

    // Load project names from database
    public List<String> loadProjects(String dbName) {
        List<String> projects = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        
        try {
            conn = DatabaseManager.getConnection(dbName);
            if(!conn.isClosed()) {
                System.out.println("Successfully connected to database for projects");
                stmt = conn.createStatement();
                String query = """
                   SELECT Pname FROM PROJECT ORDER BY Pname;
                   """;
                result = stmt.executeQuery(query);
                
                while(result.next()) {
                    // FIXED: Changed "Dname" to "Pname"
                    projects.add(result.getString("Pname"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (conn != null && !conn.isClosed()) {
                    DatabaseManager.closeConnection();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return projects;
    }

    // Query to search the employee name based on the selection
    public List<String> searchEmployees(List<String> selectedDepartments, boolean noDepartment, List<String> selectedProjects, boolean noProjects, String dbName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet result = null;
        List<String> employees = new ArrayList<>();
        
        selectedDepartments = (selectedDepartments == null) ? Collections.emptyList() : selectedDepartments;
        selectedProjects = (selectedProjects == null) ? Collections.emptyList() : selectedProjects;

        try {
            conn = DatabaseManager.getConnection(dbName);
            
            if(!conn.isClosed()) {
                System.out.println("Successfully connected to " + dbName + " database for search...");
                
                StringBuilder sql = new StringBuilder();
                List<String> params = new ArrayList<>();

                sql.append("""
                    SELECT DISTINCT e.Fname, e.Lname 
                    FROM EMPLOYEE e 
                    WHERE 1=1 
                    """);

                // Department Filter
                if (!selectedDepartments.isEmpty()) {
                    sql.append(" AND e.Dno ");
                    if (noDepartment) {
                        sql.append("NOT ");
                    }
                    sql.append("IN (SELECT d.Dnumber FROM DEPARTMENT d WHERE d.Dname IN (")
                       .append(makePlaceholders(selectedDepartments.size()))
                       .append("))");
                    params.addAll(selectedDepartments);
                }

                // Project Filter
                if (!selectedProjects.isEmpty()) {
                    sql.append(" AND e.Ssn ");
                    if (noProjects) {
                        sql.append("NOT ");
                    }
                    sql.append("""
                        IN (
                            SELECT w.Essn 
                            FROM WORKS_ON w 
                            JOIN PROJECT p ON p.Pnumber = w.Pno 
                            WHERE p.Pname IN ("""
                        )
                       .append(makePlaceholders(selectedProjects.size()))
                       .append("))");
                    params.addAll(selectedProjects);
                }

                sql.append(" ORDER BY e.Lname, e.Fname");

                pstmt = conn.prepareStatement(sql.toString());
                
                // Set parameters
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setString(i + 1, params.get(i));
                }

                result = pstmt.executeQuery();
                
                while(result.next()) {
                    String employeeName = result.getString("Fname") + " " + result.getString("Lname");
                    employees.add(employeeName);
                    System.out.println("Found employee: " + employeeName);
                }
                
                System.out.println("Total employees found: " + employees.size());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (conn != null && !conn.isClosed()) {
                    DatabaseManager.closeConnection();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return employees;
    }

    private String makePlaceholders(int count) {
        return String.join(",", Collections.nCopies(count, "?"));
    }
}