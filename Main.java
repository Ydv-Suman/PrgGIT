import ui.EmployeeSearchFrame;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
    /**
	 * Launch the application.
	 */
     java.awt.EventQueue.invokeLater(new Runnable() {
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
}