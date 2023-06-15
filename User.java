import javax.swing.*;
import java.awt.*;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class User {
    private JFrame userListFrame;
    private JEditorPane usersEditorPane;

    private static final String FILE_PATH = "accounts.txt";

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

        populateUserList();
        userListFrame.add(userListPanel);
        userListFrame.setVisible(true);
    }

    
    
private void populateUserList() {
    try {
        List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
        List<String> filteredLines = lines.stream()
                .map(String::trim)
                .filter(line -> {
                    String[] parts = line.split(",");
                    return parts.length >= 3 && parts[2].trim().equalsIgnoreCase("User");
                })
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append("<html><table>");
        sb.append("<tr><th style='background-color:#ADD8E6; padding:5px;'>Username</th><th style='background-color:#ADD8E6; padding:5px;'>Password</th></tr>");

        for (String line : filteredLines) {
            String[] parts = line.split(",");
            String storedUsername = parts[0];
            String storedPassword = parts[1];

            sb.append("<tr><td style='padding:5px;'>").append(storedUsername).append("</td><td style='padding:5px;'>")
                    .append(storedPassword).append("</td><td style='padding:5px;'>");        }
        sb.append("</table></html>");

        if (usersEditorPane != null) {
            usersEditorPane.setText(sb.toString());
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User user = new User();
            user.createUsersScreen();
        });
    }
}






