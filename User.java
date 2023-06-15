import javax.swing.*;
import java.awt.*;

public class User {
    private JFrame userListFrame;
    private JEditorPane usersEditorPane;

    public void createUsersScreen() {
        userListFrame = new JFrame("User List");
        userListFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        userListFrame.setSize(400, 300);
        userListFrame.setLocationRelativeTo(null);

        JPanel userListPanel = new JPanel(new BorderLayout());
        userListPanel.setBackground(new Color(240, 240, 240));

        JLabel headingLabel = new JLabel("User List");
        headingLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headingLabel.setForeground(new Color(70, 130, 180));
        userListPanel.add(headingLabel, BorderLayout.NORTH);

        usersEditorPane = new JEditorPane();
        usersEditorPane.setEditable(false);
        usersEditorPane.setContentType("text/html");
        usersEditorPane.setBackground(new Color(255, 255, 240));
        JScrollPane scrollPane = new JScrollPane(usersEditorPane);
        userListPanel.add(scrollPane, BorderLayout.CENTER);

        userListFrame.add(userListPanel);
        userListFrame.setVisible(true);
    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User user = new User();
            user.createUsersScreen();
        });
    }
}






