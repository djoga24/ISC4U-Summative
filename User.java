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
        sb.append("<tr><th style='background-color:#ADD8E6; padding:5px;'>Username</th><th style='background-color:#ADD8E6; padding:5px;'>Password</th><th style='background-color:#ADD8E6; padding:5px;'>Books Owned</th></tr>");

        for (String line : filteredLines) {
            String[] parts = line.split(",");
            String storedUsername = parts[0];
            String storedPassword = parts[1];
            String storedBooksOwned = (parts.length > 3) ? extractBookTitles(parts, 3) : "None";

            sb.append("<tr><td style='padding:5px;'>").append(storedUsername).append("</td><td style='padding:5px;'>")
                    .append(storedPassword).append("</td><td style='padding:5px;'>")
                    .append(storedBooksOwned).append("</td></tr>");
        }
        sb.append("</table></html>");

        if (usersEditorPane != null) {
            usersEditorPane.setText(sb.toString());
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    private String extractBookTitles(String[] parts, int startIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < parts.length; i += 3) {
            sb.append(parts[i].trim());
            if (i < parts.length - 3) {
                sb.append("<br>");
            }
        }
        return sb.toString();
    }

    public void addUser(String username, String password) {
        try {
            Path filePath = Paths.get(FILE_PATH);
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            List<String> lines = Files.readAllLines(filePath);
            boolean userExists = lines.stream()
                    .map(String::trim)
                    .anyMatch(line -> {
                        String storedUsername = line.split(",")[0].trim();
                        return storedUsername.equalsIgnoreCase(username);
                    });

            if (userExists) {
                JOptionPane.showMessageDialog(userListFrame, "User already exists. Please enter a different username.");
                return;
            }

            Files.write(filePath, (username + "," + password + ",User\n").getBytes(), StandardOpenOption.APPEND);
            populateUserList();
            JOptionPane.showMessageDialog(userListFrame, "User added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(userListFrame, "Error occurred while adding a user.");
        }
    }

    public void removeUser(String username) {
        try {
            Path filePath = Paths.get(FILE_PATH);
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            List<String> lines = Files.readAllLines(filePath);
            boolean removed = false;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");

                if (parts.length >= 1) {
                    String storedUsername = parts[0].trim();
                    String storedUserType = parts[2].trim();

                    if (storedUsername.equalsIgnoreCase(username)) {
                        if (storedUserType.equals("Admin")) {
                            JOptionPane.showMessageDialog(userListFrame, "Cannot remove an Admin user.");
                            return;
                        }

                        lines.remove(i);
                        removed = true;
                        break;
                    }
                }
            }

            if (removed) {
                Files.write(filePath, lines);
                populateUserList();
                JOptionPane.showMessageDialog(userListFrame, "User removed successfully.");
            } else {
                JOptionPane.showMessageDialog(userListFrame, "User not found. Cannot remove the user.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(userListFrame, "Error occurred while removing the user.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User user = new User();
            user.createUsersScreen();
        });
    }
}


