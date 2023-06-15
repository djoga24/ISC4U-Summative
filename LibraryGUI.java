import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.nio.file.*;

public class LibraryGUI {
    private static JFrame mainFrame;
    private static JTextField usernameField;
    private static JPasswordField passwordField;
    private static JRadioButton radioUser;
    private static JRadioButton radioAdmin;
    private static ButtonGroup userTypeGroup;

    public static void main(String[] args) {
        createSignInScreen();
    }
    
    private static void createSignInScreen() {
        mainFrame = new JFrame("Library Management System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 300);

    String imagePath = "C:\\Users\\Hockeykid\\Documents\\Picture1.jpeg";
    ImageIcon imageIcon = new ImageIcon(imagePath);

    JLabel backgroundLabel = new JLabel(imageIcon);
    backgroundLabel.setLayout(new BorderLayout());

    JPanel signInPanel = new JPanel(new GridBagLayout());
    signInPanel.setOpaque(false);

    JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);

    JLabel titleLabel = new JLabel("Bookwise Library Management System");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titlePanel.add(titleLabel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        radioUser = new JRadioButton("User");
        radioAdmin = new JRadioButton("Admin");

        userTypeGroup = new ButtonGroup();
        userTypeGroup.add(radioUser);
        userTypeGroup.add(radioAdmin);
        
        JButton signInButton = new JButton("Sign In");

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> register());

        gbc.gridx = 0;
        gbc.gridy = 0;
        signInPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        signInPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        signInPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        signInPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        signInPanel.add(radioUser, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        signInPanel.add(radioAdmin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        signInPanel.add(signInButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        signInPanel.add(registerButton, gbc);

        backgroundLabel.add(signInPanel, BorderLayout.CENTER);
        backgroundLabel.add(titlePanel,BorderLayout.NORTH);
        mainFrame.setContentPane(backgroundLabel);
        mainFrame.setVisible(true);
    }


private static void registerAccount(String username, String password, String userType) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("accounts.txt", true))) {
        String account = username + "," + password + "," + userType + "\n";
        writer.append(account);
        JOptionPane.showMessageDialog(mainFrame, "Account registered successfully!");
        clearFields();
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(mainFrame, "Error registering account.");
        clearFields();
    }
}


private static void register() {
    String username = usernameField.getText();
    String password = new String(passwordField.getPassword());
    String userType = getUserType();

    if (username.isEmpty() || password.isEmpty() || userType == null) {
        JOptionPane.showMessageDialog(mainFrame, "Please enter username, password, and user type.");
        clearFields();
        return;
    }

    if (DoesUserExist(username)) {
        JOptionPane.showMessageDialog(mainFrame, "Username already exists. Please choose a different username.");
        clearFields();
        return;
    }

    registerAccount(username, password, userType);
}   

private static boolean DoesUserExist(String username) {
    try {
        List<String> accounts = Files.readAllLines(Paths.get("accounts.txt"));
        for (String account : accounts) {
            String[] parts = account.split(",");
            if (parts.length >= 1 && parts[0].trim().equalsIgnoreCase(username)) {
                return true;
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return false;
}

public static String getUser() {
    return usernameField.getText();
}

public static String getPassword() {
    return new String(passwordField.getPassword());
}

public static String getUserType() {
    if (radioUser.isSelected()) {
        return "User";
    } else if (radioAdmin.isSelected()) {
        return "Admin";
    } else {
        return null; // No user type selected
    }
}

private static void clearFields() {
    usernameField.setText("");
    passwordField.setText("");
    userTypeGroup.clearSelection();
}

}

