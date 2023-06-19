/*
Author: Dev Jogalekar, Rodney-bill-gray Idukunda and Shantanu Nath
Date of Last Update: 2023-06-16

Description: App that a user-friendly program that streamlines book management, borrowing, and transactions in libraries.
Librarians can add books, track loans and overall improve administrative tasks and overall operations.
*/



// Necessary Imports

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.nio.file.*;

public class LibraryGUI {
    // Make Necessary GUI elements
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
    // Create the main frame
    mainFrame = new JFrame("Library Management System");
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setSize(400, 300);

    // Set the background image of the frame
    String imagePath = "C:\\Users\\Gamer\\Documents\\Picture1.jpeg";
    ImageIcon imageIcon = new ImageIcon(imagePath);
    JLabel backgroundLabel = new JLabel(imageIcon);
    backgroundLabel.setLayout(new BorderLayout());

    // Create the sign-in panel
    JPanel signInPanel = new JPanel(new GridBagLayout());
    signInPanel.setOpaque(false);

    // Create the title panel
    JPanel titlePanel = new JPanel(new GridBagLayout());
    titlePanel.setOpaque(false);

    // Create the title label
    JLabel titleLabel = new JLabel("Bookwise Library Management System");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titlePanel.add(titleLabel);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);

    // Create username label and text field
    JLabel usernameLabel = new JLabel("Username:");
    usernameLabel.setForeground(Color.BLACK);
    usernameField = new JTextField(20);

    // Create password label and password field
    JLabel passwordLabel = new JLabel("Password:");
    passwordLabel.setForeground(Color.BLACK);
    passwordField = new JPasswordField(20);

    // Create radio buttons for user type
    radioUser = new JRadioButton("User");
    radioAdmin = new JRadioButton("Admin");
    Color lightRed = new Color(255, 0, 0);
    radioAdmin.setForeground(lightRed);
    Color lightBlue = new Color(0,0,255);
    radioUser.setForeground(lightBlue);

    // Create button group for user type radio buttons
    userTypeGroup = new ButtonGroup();
    userTypeGroup.add(radioUser);
    userTypeGroup.add(radioAdmin);

    // Create sign-in and register buttons
    JButton signInButton = new JButton("Sign In");
    signInButton.addActionListener(e -> signIn());
    signInButton.setForeground(Color.WHITE);
    signInButton.setBackground(new Color(59, 89, 182));
    signInButton.setFocusPainted(false);

    JButton registerButton = new JButton("Register");
    registerButton.addActionListener(e -> register());
    registerButton.setForeground(Color.WHITE);
    registerButton.setBackground(new Color(59, 89, 182));
    registerButton.setFocusPainted(false);

    // Place the GUI components on to the sign-in panel
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


    // Add the sign-in and title panels to the background label
    backgroundLabel.add(signInPanel, BorderLayout.CENTER);
    backgroundLabel.add(titlePanel, BorderLayout.NORTH);

    // Set the background label as the content pane of the main frame
    mainFrame.setContentPane(backgroundLabel);
    mainFrame.setVisible(true);
}

private static void createLibraryScreen() {
    // Remove all components from the content pane of the main frame and reset it
    mainFrame.getContentPane().removeAll();
    mainFrame.setSize(400, 300);

    // Set the background image of the frmae
    String imagePath = "C:\\Users\\Gamer\\Documents\\Picture1.jpeg";
    ImageIcon imageIcon = new ImageIcon(imagePath);
    JLabel backgroundLabel = new JLabel(imageIcon);
    backgroundLabel.setLayout(new BorderLayout());

    // Create the library panel
    JPanel libraryPanel = new JPanel();
    libraryPanel.setLayout(new GridBagLayout());
    libraryPanel.setOpaque(false);

    // Create the title panel
    JPanel titlePanelU = new JPanel(new GridBagLayout());
    titlePanelU.setOpaque(false);

    // Create the title label for the user screen
    JLabel titleLabelU = new JLabel("User Screen");
    titleLabelU.setFont(new Font("Arial", Font.BOLD, 24));
    titlePanelU.add(titleLabelU);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);

    // Create the "View Books" button
    JButton btnViewBook = new JButton("View Books");
    btnViewBook.setBackground(new Color(255, 165, 0)); // Orange background
    btnViewBook.setForeground(Color.BLACK);
    btnViewBook.setFocusPainted(false); // Remove the focus border

    // Create the "Return Book" button
    JButton btnReturnBook = new JButton("Return Book");
    btnReturnBook.setBackground(new Color(144, 238, 144));
    btnReturnBook.setForeground(Color.BLACK);
    btnReturnBook.setFocusPainted(false);

    // Create the "Exit" button
    JButton btnExitU = new JButton("Exit");
    btnExitU.setBackground(new Color(220, 20, 60)); // Crimson background
    btnExitU.setForeground(Color.WHITE);
    btnExitU.setFocusPainted(false);

    // Customize the font for the buttons
    Font buttonFont = new Font("Arial", Font.BOLD, 14);
    btnViewBook.setFont(buttonFont);
    btnReturnBook.setFont(buttonFont);
    btnExitU.setFont(buttonFont);

    // Customize the button size
    Dimension buttonSize = new Dimension(120, 40);
    btnViewBook.setPreferredSize(buttonSize);
    btnReturnBook.setPreferredSize(buttonSize);
    btnExitU.setPreferredSize(buttonSize);

    // Place the GUI components on to the library panel
    gbc.gridx = 0;
    gbc.gridy = 0;
    libraryPanel.add(btnViewBook, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    libraryPanel.add(btnReturnBook, gbc);

    gbc.gridx = 2;
    gbc.gridy = 0;
    libraryPanel.add(btnExitU, gbc);

    // Define action listeners for the buttons

    // Action listener for the "View Books" button that will call the viewBooks method
    btnViewBook.addActionListener(e -> Books.viewBooks());

    // Action listener for the "Return Book" button that will call the ReturnBook Method
    btnReturnBook.addActionListener(e -> {
        String bookToReturn = JOptionPane.showInputDialog(mainFrame, "Enter the book you want to return:");
        if (bookToReturn != null && !bookToReturn.isEmpty()) {
            Books.returnBook(bookToReturn);
        }
    });

    // Action listener for the "Exit" button that will close the program
    btnExitU.addActionListener(e -> {
        System.exit(0);
    });

    // Add the library panel and title panel to the background label
    backgroundLabel.add(libraryPanel, BorderLayout.CENTER);
    backgroundLabel.add(titlePanelU, BorderLayout.NORTH);

    // Set the background label as the content pane of the main frame
    mainFrame.setContentPane(backgroundLabel);
    mainFrame.setVisible(true);
}

private static void createAdminScreen() {
    mainFrame.getContentPane().removeAll();
    mainFrame.setSize(400, 300);

    // Set the path of the image
    String imagePath = "C:\\Users\\Gamer\\Documents\\Picture1.jpeg";
    ImageIcon imageIcon = new ImageIcon(imagePath);

    JLabel backgroundLabel = new JLabel(imageIcon);
    backgroundLabel.setLayout(new BorderLayout());

    // Create the Admin Panel
    JPanel adminPanel = new JPanel();
    adminPanel.setLayout(new GridBagLayout());
    adminPanel.setOpaque(false);

    // Create the title Panel
    JPanel titlePanelA = new JPanel(new GridBagLayout());
    titlePanelA.setOpaque(false);

    // Create and customize the title label
    JLabel titleLabelA = new JLabel("Admin Panel");
    titleLabelA.setFont(new Font("Arial", Font.BOLD, 24));
    titlePanelA.add(titleLabelA);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);

    // Create and customize the buttons
    JButton btnViewUsers = new JButton("View Users");
    JButton btnAddUser = new JButton("Add User");
    JButton btnRemoveUser = new JButton("Remove User");
    JButton btnAddBooks = new JButton("Add Books");
    JButton btnRemoveBooks = new JButton("Remove Book");
    JButton btnViewBooks = new JButton("View Books");
    JButton btnExitA = new JButton("Exit");

    // Set background and foreground colors for each button
    btnViewUsers.setBackground(new Color(255, 165, 0)); // Orange background
    btnViewUsers.setForeground(Color.BLACK); // White text

    btnAddUser.setBackground(new Color(176, 196, 222)); // Light steel blue background
    btnAddUser.setForeground(Color.BLACK); // Black text

    btnRemoveUser.setBackground(new Color(218, 112, 214)); // Orchid background
    btnRemoveUser.setForeground(Color.BLACK); // White text

    btnAddBooks.setBackground(new Color(60, 179, 113)); // Medium sea green background
    btnAddBooks.setForeground(Color.WHITE); // White text

    btnRemoveBooks.setBackground(new Color(255, 0, 0)); // Red background
    btnRemoveBooks.setForeground(Color.WHITE); // White text

    btnViewBooks.setBackground(new Color(70, 130, 180)); // Steel blue background
    btnViewBooks.setForeground(Color.WHITE); // White text

    btnExitA.setBackground(new Color(176, 196, 222)); // Light steel blue background
    btnExitA.setForeground(new Color(255, 0, 0)); // Red text

    // Set a different font for the buttons
    Font buttonFont = new Font("Times New Roman", Font.BOLD, 14);
    btnViewUsers.setFont(buttonFont);
    btnAddUser.setFont(buttonFont);
    btnRemoveUser.setFont(buttonFont);
    btnAddBooks.setFont(buttonFont);
    btnRemoveBooks.setFont(buttonFont);
    btnViewBooks.setFont(buttonFont);
    btnExitA.setFont(buttonFont);

    // PLace the buttons on to the admin panel 
    gbc.gridx = 0;
    gbc.gridy = 0;
    adminPanel.add(btnViewUsers, gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    adminPanel.add(btnAddUser, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    adminPanel.add(btnRemoveUser, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    adminPanel.add(btnAddBooks, gbc);

    gbc.gridx = 1;
    gbc.gridy = 2;
    adminPanel.add(btnRemoveBooks, gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    adminPanel.add(btnViewBooks, gbc);

    gbc.gridx = 1;
    gbc.gridy = 3;
    adminPanel.add(btnExitA, gbc);

    // Add admin panel and title panel to the background label
    backgroundLabel.add(adminPanel, BorderLayout.CENTER);
    backgroundLabel.add(titlePanelA, BorderLayout.NORTH);
    mainFrame.setContentPane(backgroundLabel);
    mainFrame.setVisible(true);

    // Create an User object from the User class
    User user = new User();

    // Action listener for the "View Users" button- calling the createUsersScreen method
    btnViewUsers.addActionListener(e -> user.createUsersScreen());

    // Action listener for the "Add User" button- callng the addUser method- with appropriate error handling
    btnAddUser.addActionListener(e -> {
        String username = JOptionPane.showInputDialog(mainFrame, "Enter username:");
        if (username == null) {
            mainFrame.setVisible(true);
            return;
        }

        String password = JOptionPane.showInputDialog(mainFrame, "Enter password:");
        if (password == null) {
            mainFrame.setVisible(true);
            return;
        }

        user.addUser(username, password);
    });

    // Action listener for the "Remove User" button- callng the RemoveUser method- with appropriate error handling
    btnRemoveUser.addActionListener(e -> {
        String username = JOptionPane.showInputDialog(mainFrame, "Enter username to remove:");
        if (username == null) {
            mainFrame.setVisible(true);
            return;
        }

        user.removeUser(username);
    });

    // Action listener for the "Add Books" button- callng the addBooks method- with appropriate error handling
    btnAddBooks.addActionListener(e -> {
        String title = JOptionPane.showInputDialog(mainFrame, "Enter the book title:");
        if (title == null) {
            mainFrame.setVisible(true);
            return;
        }

        String author = JOptionPane.showInputDialog(mainFrame, "Enter the book author:");
        if (author == null) {
            mainFrame.setVisible(true);
            return;
        }

        String pages = JOptionPane.showInputDialog(mainFrame, "Enter the number of pages:");
        if (pages == null) {
            mainFrame.setVisible(true);
            return;
        }

        Books.addBook(title, author, pages);
    });

    // Action listener for the "Remove Books" button- callng the removeBooks method- with appropriate error handling
    btnRemoveBooks.addActionListener(e -> {
        String title = JOptionPane.showInputDialog(mainFrame, "Enter the book title:");
        if (title == null) {
            mainFrame.setVisible(true);
            return;
        }

        Books.removeBook(title);
    });

    // Action listener for the "View Books" button- Calling the viewBooks method in the Books class
    btnViewBooks.addActionListener(e -> Books.viewBooks());

    // Action listener for the "Exit" button- Exiting the program
    btnExitA.addActionListener(e -> {
        System.exit(0);
    });
}


// Validates the user credentials by checking if the provided username, password, and user type match the stored values
private static boolean validateUser(String username, String password, String userType) {
    try {
        // Read all lines from the "accounts.txt" file
        List<String> accounts = Files.readAllLines(Paths.get("accounts.txt"));
        for (String account : accounts) {
            // Split each line into parts using comma as the delimiter
            String[] parts = account.split(",");
            if (parts.length >= 3) {
                // Extract the stored username, password, and user type
                String storedUsername = parts[0].trim();
                String storedPassword = parts[1].trim();
                String storedUserType = parts[2].trim();
                // Check if the stored username/user type/password match the provided values,
                if (storedUsername.equals(username) && storedUserType.equals(userType)) {
                    return storedPassword.equals(password);
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return false; 

}

// Registers a new account by appending the username, password, and user type to the "accounts.txt" file
private static void registerAccount(String username, String password, String userType) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("accounts.txt", true))) {
        // Create an account string in the format: "username,password,userType"
        String account = username + "," + password + "," + userType + "\n";
        // Append the account to the file
        writer.append(account);
        JOptionPane.showMessageDialog(mainFrame, "Account registered successfully!");
        clearFields(); 
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(mainFrame, "Error registering account.");
        clearFields(); 
    }
}

// Sign in the user by retrieving the entered username, password, and user type, and validating them 

private static void signIn() {
    String username = usernameField.getText();
    String password = new String(passwordField.getPassword());
    String userType = getUserType();

    // Check if any of the required fields is empty
    if (username.isEmpty() || password.isEmpty() || userType == null) {
        JOptionPane.showMessageDialog(mainFrame, "Please enter username, password, and user type.");
        clearFields(); 
        return;
    }

    // Validate the user credentials
    if (validateUser(username, password, userType)) {
        if (userType.equals("User")) {
            createLibraryScreen(); // User signed in successfully, create the library screen
        } else if (userType.equals("Admin")) {
            createAdminScreen(); // Admin signed in successfully, create the admin screen
        }
    } else {
        JOptionPane.showMessageDialog(mainFrame, "Invalid username, password, or user type.");
        clearFields();
    }
}

// Register a new user by retrieving the entered username, password, and user type, and checking if the username already exists before calling registerAccount method
private static void register() {
    String username = usernameField.getText();
    String password = new String(passwordField.getPassword());
    String userType = getUserType();

    // Check if any of the required fields is empty
    if (username.isEmpty() || password.isEmpty() || userType == null) {
        JOptionPane.showMessageDialog(mainFrame, "Please enter username, password, and user type.");
        clearFields(); 
        return;
    }

    // Check if the username already exists
    if (isUserExists(username)) {
        JOptionPane.showMessageDialog(mainFrame, "Username already exists. Please choose a different username.");
        clearFields();
        return;
    }

    // Register the new account
    registerAccount(username, password, userType); 
}

// Check if a user with the given username already exists in the "accounts.txt" file
private static boolean isUserExists(String username) {
    try {
        List<String> accounts = Files.readAllLines(Paths.get("accounts.txt"));
        for (String account : accounts) {
            String[] parts = account.split(",");
            if (parts.length >= 1 && parts[0].trim().equalsIgnoreCase(username)) {
                return true; // User exists
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return false; 
}

// Get the User text from the usernamefield
public static String getUser() {
    return usernameField.getText();
}

// Get the Password text from the password field
public static String getPassword() {
    return new String(passwordField.getPassword());
}

// Get the selected user type from the radio buttons
public static String getUserType() {
    if (radioUser.isSelected()) {
        return "User";
    } else if (radioAdmin.isSelected()) {
        return "Admin";
    } else {
        return null; 
    }
}

// Clear the input fields (username, password) and the selected user type radio button
private static void clearFields() {
    usernameField.setText("");
    passwordField.setText("");
    userTypeGroup.clearSelection();
}


}


