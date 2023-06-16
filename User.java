// Necessary Imports

import javax.swing.*;
import java.awt.*;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class User {
    // Make necessary GUI components
    private JFrame userListFrame;
    private JEditorPane usersEditorPane;

    // Variable for the ability to read the text file 
    private static final String FILE_PATH = "accounts.txt";

    public void createUsersScreen() {
        // Create UserList Frame
        userListFrame = new JFrame("User List");
        userListFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        userListFrame.setSize(400, 300);
        userListFrame.setLocationRelativeTo(null);

        // Create UserListPanel
        JPanel userListPanel = new JPanel(new BorderLayout());
        userListPanel.setBackground(new Color(240, 240, 240));

        // Create the Label on the Panel
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

        // Load and display the list of users
        viewUsers(); 

        userListFrame.add(userListPanel);
        userListFrame.setVisible(true);
    }

    // View and display the list of users
    private void viewUsers() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));

            // Filter the lines to include only user accounts
            List<String> filteredLines = lines.stream()
                    .map(String::trim)
                    .filter(line -> {
                        String[] parts = line.split(",");
                        return parts.length >= 3 && parts[2].trim().equalsIgnoreCase("User");
                    })
                    .collect(Collectors.toList());

            // Make a table with a html table with 3 headings; Username, Password, Books Owned
            StringBuilder sb = new StringBuilder();
            sb.append("<html><table>");
            sb.append("<tr><th style='background-color:#ADD8E6; padding:5px;'>Username</th><th style='background-color:#ADD8E6; padding:5px;'>Password</th><th style='background-color:#ADD8E6; padding:5px;'>Books Owned</th></tr>");

            // Iterate over each user account and append the information to the StringBuilder
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

            // Update the content of the JEditorPane to display the user information
            if (usersEditorPane != null) {
                usersEditorPane.setText(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to extract book titles from an array of account parts
    private String extractBookTitles(String[] parts, int startIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < parts.length; i++) {
            sb.append(parts[i]);
            if (i < parts.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    
    
     // Adds a new user to the user list.
    public void addUser(String username, String password) {
        try {
            Path filePath = Paths.get(FILE_PATH);
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            // Read all existing lines from the user list file
            List<String> lines = Files.readAllLines(filePath);

            // Check if the user already exists
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

            // Append the new user's information to the user list file
            Files.write(filePath, (username + "," + password + ",User\n").getBytes(), StandardOpenOption.APPEND);

            // Refresh the displayed user list
            viewUsers();

            JOptionPane.showMessageDialog(userListFrame, "User added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(userListFrame, "Error occurred while adding a user.");
        }
    }

    
     // Removes a user from the user list.
    public void removeUser(String username) {
        try {
            Path filePath = Paths.get(FILE_PATH);
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            // Read all existing lines from the user list file
            List<String> lines = Files.readAllLines(filePath);
            boolean removed = false;

            // Iterate through the lines and find the user to be removed
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");

                if (parts.length >= 1) {
                    String storedUsername = parts[0].trim();
                    String storedUserType = parts[2].trim();

                    // If the user is of type admin, produce error message
                    if (storedUsername.equalsIgnoreCase(username)) {
                        if (storedUserType.equals("Admin")) {
                            JOptionPane.showMessageDialog(userListFrame, "Cannot remove an Admin user.");
                            return;
                        }

                        // Remove the user from the list
                        lines.remove(i);
                        removed = true;
                        break;
                    }
                }
            }

            if (removed) {
                // Write the updated user list back to the file
                Files.write(filePath, lines);

                // Refresh the displayed user list
                viewUsers();

                JOptionPane.showMessageDialog(userListFrame, "User removed successfully.");
            } else {
                // If user not found, produce error message
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


