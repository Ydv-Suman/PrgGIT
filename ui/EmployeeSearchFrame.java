/**
 * Author: Lon Smith, Ph.D.
 * Description: This is the framework for the database program. Additional requirements and functionality
 *    are to be built by you and your group.
 */

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import src.database.DatabaseManager;
import src.query.EmployeeSearchQuery;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import java.util.List;

public class EmployeeSearchFrame extends JFrame {

	private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtDatabase;
    private JList<String> lstDepartment;
    private DefaultListModel<String> department = new DefaultListModel<String>();
    private JList<String> lstProject;
    private DefaultListModel<String> project = new DefaultListModel<String>();
    private JTextArea textAreaEmployee;
    private JCheckBox chckbxNotDept;
    private JCheckBox chckbxNotProject;
    private JScrollPane scrollDepartment;
    private JScrollPane scrollProject;
    private JScrollPane scrollEmployee;

    private EmployeeSearchQuery searchQuery;

	/**
	 * Create the frame.
	 */
	public EmployeeSearchFrame() {
        searchQuery = new EmployeeSearchQuery();

        setTitle("Employee Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 450);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Database:");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblNewLabel.setBounds(21, 23, 59, 14);
        contentPane.add(lblNewLabel);

        txtDatabase = new JTextField();
        txtDatabase.setBounds(90, 20, 193, 20);
        contentPane.add(txtDatabase);
        txtDatabase.setColumns(10);
        txtDatabase.setText("company"); // Set default database name

        JButton btnDBFill = new JButton("Fill");
        btnDBFill.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fillListsFromDatabase();
            }
        });
        btnDBFill.setFont(new Font("Times New Roman", Font.BOLD, 12));
        btnDBFill.setBounds(307, 19, 68, 23);
        contentPane.add(btnDBFill);

        JLabel lblDepartment = new JLabel("Department");
        lblDepartment.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblDepartment.setBounds(52, 63, 89, 14);
        contentPane.add(lblDepartment);

        JLabel lblProject = new JLabel("Project");
        lblProject.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblProject.setBounds(255, 63, 47, 14);
        contentPane.add(lblProject);

        // Department List with Scroll Pane
        lstDepartment = new JList<String>(department);
        lstDepartment.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lstDepartment.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        scrollDepartment = new JScrollPane(lstDepartment);
        scrollDepartment.setBounds(36, 84, 172, 80);
        contentPane.add(scrollDepartment);

        // Project List with Scroll Pane
        lstProject = new JList<String>(project);
        lstProject.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lstProject.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        scrollProject = new JScrollPane(lstProject);
        scrollProject.setBounds(225, 84, 150, 80);
        contentPane.add(scrollProject);

        chckbxNotDept = new JCheckBox("Not");
        chckbxNotDept.setBounds(71, 175, 59, 23);
        contentPane.add(chckbxNotDept);

        chckbxNotProject = new JCheckBox("Not");
        chckbxNotProject.setBounds(270, 175, 59, 23);
        contentPane.add(chckbxNotProject);

        JLabel lblEmployee = new JLabel("Employee Results");
        lblEmployee.setFont(new Font("Times New Roman", Font.BOLD, 12));
        lblEmployee.setBounds(36, 210, 120, 14);
        contentPane.add(lblEmployee);

        // Employee Results with Scroll Pane
        textAreaEmployee = new JTextArea();
        textAreaEmployee.setLineWrap(true);
        textAreaEmployee.setWrapStyleWord(true);
        scrollEmployee = new JScrollPane(textAreaEmployee);
        scrollEmployee.setBounds(36, 230, 339, 120);
        contentPane.add(scrollEmployee);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performEmployeeSearch();
            }
        });
        btnSearch.setBounds(80, 370, 89, 23);
        contentPane.add(btnSearch);

        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearAllSelections();
            }
        });
        btnClear.setBounds(236, 370, 89, 23);
        contentPane.add(btnClear);

        // Load default database on startup
        EventQueue.invokeLater(() -> {
            fillListsFromDatabase();
        });
    }
	private void fillListsFromDatabase() {
        String dbName = txtDatabase.getText().trim();

        if (dbName.isEmpty()) {
            textAreaEmployee.setText("Enter a database name first.");
            return;
        }

        department.clear();
        project.clear();

        try {
            // Load departments
            List<String> departments = searchQuery.loadDepartments(dbName);
            for (String d : departments) {
                department.addElement(d);
            }

            // Load projects
            List<String> projects = searchQuery.loadProjects(dbName);
            for (String p : projects) {
                project.addElement(p);
            }

            if (department.isEmpty() && project.isEmpty()) {
                textAreaEmployee.setText("Database opened, but no data found.\nCheck DB name or tables.");
            } else {
                textAreaEmployee.setText("Lists loaded successfully.\n" + 
                                       "Departments: " + department.size() + 
                                       ", Projects: " + project.size());
            }

        } catch (Exception e) {
            textAreaEmployee.setText("Error: Could not open database '" + dbName + "'\n" + 
                                   e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Performs employee search based on selected criteria
     */
    private void performEmployeeSearch() {
        String dbName = txtDatabase.getText().trim();
        if (dbName.isEmpty()) {
            textAreaEmployee.setText("Please enter a database name first.");
            return;
        }
        
        // Get selected departments and projects
        List<String> selectedDepts = lstDepartment.getSelectedValuesList();
        List<String> selectedProjects = lstProject.getSelectedValuesList();
        
        // Get NOT checkbox states - parameter names match your searchQuery method
        boolean noDept = chckbxNotDept.isSelected();
        boolean noProject = chckbxNotProject.isSelected();
        
        // Perform search using the updated method signature
        List<String> employees = searchQuery.searchEmployees(
            selectedDepts, noDept, selectedProjects, noProject, dbName);
        
        // Display results
        if (employees.isEmpty()) {
            textAreaEmployee.setText("No employees found.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Found ").append(employees.size()).append(" employee(s):\n\n");
            for (String employee : employees) {
                sb.append("• ").append(employee).append("\n");
            }
            textAreaEmployee.setText(sb.toString());
        }
    }

    /**
     * Clears all selections and results
     */
    private void clearAllSelections() {
        textAreaEmployee.setText("");
        lstDepartment.clearSelection();
        lstProject.clearSelection();
        chckbxNotDept.setSelected(false);
        chckbxNotProject.setSelected(false);
    }
}