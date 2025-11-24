/**
 * Author: Lon Smith, Ph.D.
 * Description: This is the framework for the database program. Additional requirements and functionality
 *    are to be built by you and your group.
 */
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
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class EmployeeSearchFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtDatabase;
	private JList<String> lstDepartment;
	private DefaultListModel<String> department = new DefaultListModel<String>();
	private JList<String> lstProject;
	private DefaultListModel<String> project = new DefaultListModel<String>();
	private JTextArea textAreaEmployee;
	private JScrollPane scrollPaneDepartment;
	private JScrollPane scrollPaneProject;
	private JScrollPane scrollPaneEmployee;
	private JCheckBox chckbxNotDept;
	private JCheckBox chckbxNotProject;

	private EmployeeSearchQuery searchQuery;
	private Connection connection;
	
    /**
     * Launch the application.
     */
	/**
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    EmployeeSearchFrame frame = new EmployeeSearchFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

	/**
	 * Create the frame.
	 */
	public EmployeeSearchFrame() {

		searchQuery = new EmployeeSearchQuery();

		
		setTitle("Employee Search");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 347);
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
		
		JButton btnDBFill = new JButton("Fill");
		btnDBFill.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fillListsFromDatabase();
            }
        });
		/**
		 * The btnDBFill should fill the department and project JList with the 
		 * departments and projects from your entered database name.
		 */
		
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
		
		// Department list with scroll pane
		lstDepartment = new JList<String>(new DefaultListModel<String>());
		lstDepartment.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lstDepartment.setModel(department);
		lstDepartment.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scrollPaneDepartment = new JScrollPane(lstDepartment);
		scrollPaneDepartment.setBounds(36, 84, 172, 40);
		contentPane.add(scrollPaneDepartment);
		
		// Project list with scroll pane
		lstProject = new JList<String>(new DefaultListModel<String>());
		lstProject.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lstProject.setModel(project);
		lstProject.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		scrollPaneProject = new JScrollPane(lstProject);
		scrollPaneProject.setBounds(225, 84, 150, 42);
		contentPane.add(scrollPaneProject);
		
		chckbxNotDept = new JCheckBox("Not");
		chckbxNotDept.setBounds(71, 133, 59, 23);
		contentPane.add(chckbxNotDept);
		
		chckbxNotProject = new JCheckBox("Not");
		chckbxNotProject.setBounds(270, 133, 59, 23);
		contentPane.add(chckbxNotProject);
		
		JLabel lblEmployee = new JLabel("Employee");
		lblEmployee.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblEmployee.setBounds(52, 179, 89, 14);
		contentPane.add(lblEmployee);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchEmployees();
			}
		});
		btnSearch.setBounds(80, 276, 89, 23);
		contentPane.add(btnSearch);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearSelections();
			}
		});
		btnClear.setBounds(236, 276, 89, 23);
		contentPane.add(btnClear);
		
		// Employee text area with scroll pane
		textAreaEmployee = new JTextArea();
		textAreaEmployee.setEditable(false);
		scrollPaneEmployee = new JScrollPane(textAreaEmployee);
		scrollPaneEmployee.setBounds(36, 197, 339, 68);
		contentPane.add(scrollPaneEmployee);
	}

	/**
	 * Loads department and project lists from database
	 */
	private void fillListsFromDatabase() {
	    String dbName = txtDatabase.getText().trim();

	    if (dbName.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Please enter a database name first.", 
	                "Database Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    // Close previous connection if exists
	    if (connection != null) {
	        try {
	            DatabaseManager.closeconnection();
	        } catch (Exception e) {
	            // Ignore
	        }
	    }

	    try {
	        // Get database connection
	        connection = DatabaseManager.getConnection(dbName);
	        
	        // Clear existing lists
	        department.clear();
	        project.clear();

	        // Load departments using existing method
	        List<String> departments = searchQuery.deparmenteNames(connection);
	        for (String d : departments) {
	            department.addElement(d);
	        }

	        // Load projects using existing method
	        List<String> projects = searchQuery.projectNames(connection);
	        for (String p : projects) {
	            project.addElement(p);
	        }

	        if (department.isEmpty() && project.isEmpty()) {
	            textAreaEmployee.setText("Database opened, but no data found.\nCheck DB name or tables.");
	        } else {
	            textAreaEmployee.setText("Lists loaded successfully.");
	        }
	        
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(this, 
	                "Database could not be opened.\nError: " + e.getMessage(), 
	                "Database Error", JOptionPane.ERROR_MESSAGE);
	        textAreaEmployee.setText("Database connection failed.");
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(this, 
	                "Database could not be opened.\nError: " + e.getMessage(), 
	                "Database Error", JOptionPane.ERROR_MESSAGE);
	        textAreaEmployee.setText("Database connection failed.");
	    }
	}

	/**
	 * Searches for employees based on selected departments and projects
	 */
	private void searchEmployees() {
	    if (connection == null) {
	        JOptionPane.showMessageDialog(this, 
	                "Please load a database first using the Fill button.", 
	                "Search Error", JOptionPane.WARNING_MESSAGE);
	        return;
	    }

	    try {
	        // Get selected departments
	        List<String> selectedDepartments = new ArrayList<>();
	        int[] deptIndices = lstDepartment.getSelectedIndices();
	        for (int i : deptIndices) {
	            selectedDepartments.add(department.getElementAt(i));
	        }

	        // Get selected projects
	        List<String> selectedProjects = new ArrayList<>();
	        int[] projIndices = lstProject.getSelectedIndices();
	        for (int i : projIndices) {
	            selectedProjects.add(project.getElementAt(i));
	        }

	        // Get Not checkbox states
	        boolean notInDepartments = chckbxNotDept.isSelected();
	        boolean notInProjects = chckbxNotProject.isSelected();

	        // Search employees using existing method
	        List<String> employees = searchQuery.searchEmployees(
	                selectedDepartments, notInDepartments,
	                selectedProjects, notInProjects,
	                connection);

	        // Display results
	        if (employees.isEmpty()) {
	            textAreaEmployee.setText("No employees found matching the criteria.");
	        } else {
	            StringBuilder sb = new StringBuilder();
	            for (String employee : employees) {
	                sb.append(employee).append("\n");
	            }
	            textAreaEmployee.setText(sb.toString().trim());
	        }
	        
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(this, 
	                "Error searching employees: " + e.getMessage(), 
	                "Search Error", JOptionPane.ERROR_MESSAGE);
	        textAreaEmployee.setText("Search failed.");
	    }
	}

	/**
	 * Clears the employee list, selections, and unchecks Not checkboxes
	 */
	private void clearSelections() {
	    textAreaEmployee.setText("");
	    lstDepartment.clearSelection();
	    lstProject.clearSelection();
	    chckbxNotDept.setSelected(false);
	    chckbxNotProject.setSelected(false);
	}
}
