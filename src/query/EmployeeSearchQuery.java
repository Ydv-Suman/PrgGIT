package src.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Objects;

public class EmployeeSearchService {

    Statement stmt;
    ResultSet result;
    String query;

    // Load department name
    public List<String> deparmenteNames(Connection conn) {
        List<String> departments = new ArrayList<>();
        stmt=null;
        result=null;

        try {
            stmt = conn.createStatement();
            query = """
            SELECT Dname 
            FROM DEPARTMENT 
            ORDER BY Dname;
            """;
            result = stmt.executeQuery(query);

            while (result.next()) {
                departments.add(result.getString("DepartmentName"));
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        finally {
            try {
                if (result != null) result.close();
            } catch (SQLException e1) {
                e1.getStackTrace();
            }
            try {
                if (stmt !=null) stmt.close();
            } catch (SQLException e2) {
                e2.getStackTrace();
            }
        }
        return departments;

    }

    // load projects names
    public List<String> projectNames(Connection conn) {
        List<String> projects = new ArrayList<>();
        stmt=null;
        result=null;

        try {
            stmt = conn.createStatement();
            query = """
            SELECT Pname 
            FROM PROJECT
            ORDER BY Pname;
""";
            result = stmt.executeQuery(query);

            while (result.next()) {
                projects.add(result.getString("DepartmentName"));
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        finally {
            try {
                if (result != null) result.close();
            } catch (SQLException e1) {
                e1.getStackTrace();
            }
            try {
                if (stmt !=null) stmt.close();
            } catch (SQLException e2) {
                e2.getStackTrace();
            }
        }
        return projects;

    }



    public List<String> searchEmployees(
            List<String> selectedDepartments,
            boolean notInDepartments,
            List<String> selectedProjects,
            boolean notInProjects,
            Connection conn) throws SQLException {

        selectedDepartments = (selectedDepartments == null) ? Collections.emptyList() : selectedDepartments;
        selectedProjects = (selectedProjects == null) ? Collections.emptyList() : selectedProjects;
        Objects.requireNonNull(conn, "Connection must not be null");

        StringBuilder sql = new StringBuilder();
        List<String> params = new ArrayList<>();

        sql.append("SELECT DISTINCT e.Fname, e.Lname FROM employee e WHERE 1=1 ");


        // DEPARTMENT FILTER (Dname → Dnumber)
        if (!selectedDepartments.isEmpty()) {

            sql.append(" AND e.Dno ");
            if (notInDepartments) {
                sql.append("NOT ");
            }
            sql.append("IN (SELECT d.Dnumber FROM department d WHERE d.Dname IN (")
                    .append(makePlaceholders(selectedDepartments.size()))
                    .append("))");

            params.addAll(selectedDepartments);
        }

        // PROJECT FILTER
        if (!selectedProjects.isEmpty()) {

            sql.append(" AND e.SSN ");
            if (notInProjects) {
                sql.append("NOT ");
            }

            sql.append("IN (SELECT w.Essn FROM works_on w ")
                    .append("JOIN project p ON p.Pnumber = w.Pno ")
                    .append("WHERE p.Pname IN (")
                    .append(makePlaceholders(selectedProjects.size()))
                    .append("))");

            params.addAll(selectedProjects);
        }

        List<String> results = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(rs.getString("Fname") + " " + rs.getString("Lname"));
                }
            }
        }

        return results;
    }

    private String makePlaceholders(int count) {
        return String.join(",", java.util.Collections.nCopies(count, "?"));
    }
}
