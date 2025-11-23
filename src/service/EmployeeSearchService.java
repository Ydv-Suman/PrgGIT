package src.service;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Objects;

public class EmployeeSearchService {

    public List<String> searchEmployees(
            List<String> selectedDepartments,
            boolean notInDepartments,
            List<String> selectedProjects,
            boolean notInProjects,
            Connection conn) throws SQLException {

        // Defensive checks: treat null lists as empty and require a non-null Connection
        selectedDepartments = (selectedDepartments == null) ? Collections.emptyList() : selectedDepartments;
        selectedProjects = (selectedProjects == null) ? Collections.emptyList() : selectedProjects;
        Objects.requireNonNull(conn, "Connection must not be null");

        StringBuilder sql = new StringBuilder();
        List<String> params = new ArrayList<>();

        sql.append("SELECT e.Fname, e.Lname FROM employee e WHERE 1=1 ");

        // ---------------------------------------------------------
        // DEPARTMENT FILTER (Dname → Dnumber)
        // ---------------------------------------------------------
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

        // ---------------------------------------------------------
        // PROJECT FILTER
        // ---------------------------------------------------------
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
