// Necessary Imports

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;

public class Books {
    // Path to the books file
    private static final String BOOKS_FILE_PATH = "books.txt";
    // Path to the accounts file
    private static final String ACCOUNTS_FILE_PATH = "accounts.txt";
    private static JFrame frame;
    // Currently logged-in user
    private static String loggedInUser = LibraryGUI.getUser();
    // Type of the currently logged-in user
    private static String loggedInUserType = LibraryGUI.getUserType();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            viewBooks();
        });
    }


     // Displays the book list.

    public static void viewBooks() {
        try {
            Path filePath = Paths.get(BOOKS_FILE_PATH);
            if (!Files.exists(filePath)) {
                System.err.println("Books file does not exist.");
                return;
            }

            // Read all books from the file
            List<String> books = Files.readAllLines(filePath);

            if (books.isEmpty()) {
                System.out.println("No books found.");
                return;
            }

            // Create the table data structure
            String[] columnNames = {"Title", "Author", "Pages"};
            Object[][] data = new Object[books.size()][3];

            // Populate the table data from the books list
            for (int i = 0; i < books.size(); i++) {
                String[] parts = books.get(i).split(",");

                if (parts.length >= 3) {
                    data[i][0] = parts[0].trim();
                    data[i][1] = parts[1].trim();
                    data[i][2] = parts[2].trim();
                }
            }

            // Create and configure the table
            JTable table = new JTable(data, columnNames);
            table.setEnabled(false);

            JScrollPane scrollPane = new JScrollPane(table);

            frame = new JFrame("Book List");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(500, 300);
            frame.setLocationRelativeTo(null);
            frame.add(scrollPane);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));

            // Customize the button panel based on the logged-in user type
            if (loggedInUserType.equals("User")) {
                // Add the "Borrow Book" button
                JButton btnBorrowBook = new JButton("Borrow Book");
                // Add action Listener to the Borrow Book Button
                btnBorrowBook.addActionListener(e -> {
                    String bookToBorrow = JOptionPane.showInputDialog(frame, "Enter the book you want to borrow:");
                    if (bookToBorrow != null && !bookToBorrow.isEmpty()) {
                        borrowBook(bookToBorrow);
                    }
                });
                buttonPanel.removeAll();
                buttonPanel.add(btnBorrowBook);
            } else if (loggedInUserType.equals("Admin")) {
                // Add the "Loan Book" button
                JButton btnLoanBook = new JButton("Loan Book");
                // Add action Listener to the Loan Book Button
                btnLoanBook.addActionListener(e -> {
                    String bookToLoan = JOptionPane.showInputDialog(frame, "Enter the book you want to loan:");
                    String userToLoan = JOptionPane.showInputDialog(frame, "Enter the user to loan the book to:");
                    if (bookToLoan != null && !bookToLoan.isEmpty() && userToLoan != null && !userToLoan.isEmpty()) {
                        loanBook(bookToLoan, userToLoan);
                    }
                });
                buttonPanel.removeAll(); // Remove any existing buttons
                buttonPanel.add(btnLoanBook);
            }

            frame.add(buttonPanel, BorderLayout.PAGE_END);
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    // Borrows a book for the logged-in user.
  
    public static void borrowBook(String bookTitle) {
        try {
            // Read all books from the file
            List<String> books = Files.readAllLines(Paths.get(BOOKS_FILE_PATH));

            // Check if the book is available
            int bookIndex = findBookIndex(books, bookTitle);
            if (bookIndex != -1) {
                // Read all accounts from the file
                List<String> accounts = Files.readAllLines(Paths.get(ACCOUNTS_FILE_PATH));

                // Find the user's account
                int userIndex = findUserIndex(accounts, loggedInUser);
                if (userIndex != -1) {
                    String[] bookDetails = books.get(bookIndex).split(",");
                    String bookAuthor = bookDetails[1].trim();
                    String bookPages = bookDetails[2].trim();

                    // Update the books file by removing the borrowed book
                    books.remove(bookIndex);
                    writeLinesToFile(books, BOOKS_FILE_PATH);

                    // Update the user's account by adding the borrowed book details
                    String userAccount = accounts.get(userIndex);
                    userAccount += "," + bookTitle + "," + bookAuthor + "," + bookPages;
                    accounts.set(userIndex, userAccount);
                    writeLinesToFile(accounts, ACCOUNTS_FILE_PATH);

                    JOptionPane.showMessageDialog(frame, "Book borrowed successfully!");
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "User account not found!");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Book not found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    // Finds the index of a book in the list based on its title.
     
    private static int findBookIndex(List<String> books, String bookTitle) {
        for (int i = 0; i < books.size(); i++) {
            String[] parts = books.get(i).split(",");
            if (parts[0].trim().equals(bookTitle.trim())) {
                return i;
            }
        }
        System.out.println("Book not found: " + bookTitle);
        return -1;
    }

    
     // Finds the index of a user's account in the list based on the username.
    
    private static int findUserIndex(List<String> accounts, String username) {
        for (int i = 0; i < accounts.size(); i++) {
            String[] accountParts = accounts.get(i).split(",");
            if (accountParts[0].trim().equalsIgnoreCase(username)) {
                return i;
            }
        }
        return -1;
    }

    
     // Writes a list of lines to a file.
   
    private static void writeLinesToFile(List<String> lines, String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for (String line : lines) {
            writer.write(line);
            writer.newLine();
        }
        writer.close();
    }


// Method to loan book to user
public static void loanBook(String bookToLoan, String userToLoan) {
    try {
        // Read all books from the file
        List<String> books = Files.readAllLines(Paths.get(BOOKS_FILE_PATH));
        
        // Read all accounts from the file
        List<String> accounts = Files.readAllLines(Paths.get(ACCOUNTS_FILE_PATH));

        // Find the index of the book to loan
        int bookIndex = findBookIndex(books, bookToLoan);
        if (bookIndex == -1) {
            JOptionPane.showMessageDialog(null, "Book does not exist.");
            return;
        }

        // Find the index of the user to loan the book to
        int userIndex = findUserIndex(accounts, userToLoan);
        if (userIndex == -1) {
            JOptionPane.showMessageDialog(null, "User does not exist.");
            return;
        }

        // Check if the user already has the book
        String[] accountParts = accounts.get(userIndex).split(",");
        if (bookAvailability(accountParts, bookToLoan)) {
            JOptionPane.showMessageDialog(null, "User already has the book.");
            return;
        }

        // Get the details of the book
        String[] bookDetails = books.get(bookIndex).split(",");
        String bookAuthor = bookDetails[1].trim();
        String bookPages = bookDetails[2].trim();

        // Update the user's account by adding the loaned book details
        accounts.set(userIndex, accounts.get(userIndex) + "," + bookToLoan + "," + bookAuthor + "," + bookPages);
        
        // Remove the loaned book from the list of books
        books.remove(bookIndex);

        // Write the updated accounts and books lists to the respective files
        writeLinesToFile(accounts, ACCOUNTS_FILE_PATH);
        writeLinesToFile(books, BOOKS_FILE_PATH);

        JOptionPane.showMessageDialog(null, "Book loaned successfully.");
        frame.dispose();
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "An error occurred while loaning the book.");
    }
}


 // Checks if a book is already in the user's account.

private static boolean bookAvailability(String[] accountParts, String bookToLoan) {
    for (int i = 3; i < accountParts.length; i++) {
        if (accountParts[i].equalsIgnoreCase(bookToLoan)) {
            return true;
        }
    }
    return false;
}

public static void returnBook(String bookTitle) {
    try {
        // Read all books from the file
        List<String> books = Files.readAllLines(Paths.get(BOOKS_FILE_PATH));
        
        // Read all accounts from the file
        List<String> accounts = Files.readAllLines(Paths.get(ACCOUNTS_FILE_PATH));

        // Find the index of the user's account
        int userIndex = findUserIndex(accounts, loggedInUser);
        if (userIndex == -1) {
            JOptionPane.showMessageDialog(null, "User account not found.");
            return;
        }

        boolean bookReturned = false;
        String[] accountParts = accounts.get(userIndex).split(",");
        String[] booksInAccount = Arrays.copyOfRange(accountParts, 3, accountParts.length);
        List<String> updatedBooks = new ArrayList<>(books);
        StringBuilder updatedAccount = new StringBuilder();

        // Iterate through the books in the user's account
        for (int i = 0; i < booksInAccount.length; i += 3) {
            // If the book to return is found, remove it from the account and add it back to the list of books
            if (!booksInAccount[i].trim().equalsIgnoreCase(bookTitle)) {
                updatedAccount.append(booksInAccount[i]).append(",").append(booksInAccount[i + 1]).append(",")
                        .append(booksInAccount[i + 2]).append(",");
            } else {
                bookReturned = true;
                updatedBooks.add(booksInAccount[i] + "," + booksInAccount[i + 1] + "," + booksInAccount[i + 2]);
            }
        }

        // If the book was not found in the user's account, show an error message
        if (!bookReturned) {
            JOptionPane.showMessageDialog(null, "You don't have this book in your account.");
            return;
        }

        // Update the user's account with the remaining books
        accounts.set(userIndex, String.join(",", Arrays.copyOfRange(accountParts, 0, 3))
                + (updatedAccount.length() > 0 ? updatedAccount.substring(0, updatedAccount.length() - 1) : ""));

        // Write the updated books and accounts lists to the respective files
        Files.write(Paths.get(BOOKS_FILE_PATH), String.join("\n", updatedBooks).getBytes());
        Files.write(Paths.get(ACCOUNTS_FILE_PATH), String.join("\n", accounts).getBytes());

        JOptionPane.showMessageDialog(null, "Book returned successfully.");
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public static void addBook(String title, String author, String pages) {
    try {
        // Validate the input book details
        validateBookInputs(title, author, pages);

        // Check if a book with the same title already exists
        if (isBookTitleExists(title)) {
            throw new IllegalArgumentException("Book with the same title already exists.");
        }

        // Create a new book entry
        String newBookEntry = title + "," + author + "," + pages;

        // Append the new book entry to the file
        BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE_PATH, true));
        writer.newLine(); // Add a new line before appending the new book
        writer.write(newBookEntry);
        writer.close();

        JOptionPane.showMessageDialog(null, "Book added successfully.");
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "An error occurred while adding the book.");
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());

        // Reprompt the user to enter book details if they mistype
        addBookPrompt();
    }
}


 // Validates the input book details and handles any excpetions
private static void validateBookInputs(String title, String author, String pages) {
    if (title.isEmpty()) {
        throw new IllegalArgumentException("Title cannot be empty.");
    }

    if (author.isEmpty()) {
        throw new IllegalArgumentException("Author cannot be empty.");
    }

    if (isNumeric(author)) {
        throw new IllegalArgumentException("Author cannot be a number.");
    }

    if (pages.isEmpty()) {
        throw new IllegalArgumentException("Number of pages cannot be empty.");
    }

    int numPages;
    try {
        numPages = Integer.parseInt(pages);
        if (numPages <= 0) {
            throw new IllegalArgumentException("Number of pages must be a positive integer.");
        }
    } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Number of pages must be a valid integer.");
    }
}


 // Checks if a book with the given title already exists in the books file.
private static boolean isBookTitleExists(String title) throws IOException {
    List<String> books = Files.readAllLines(Paths.get(BOOKS_FILE_PATH));
    for (String book : books) {
        String[] parts = book.split(",");
        String existingTitle = parts[0].trim();
        if (existingTitle.equalsIgnoreCase(title)) {
            return true;
        }
    }
    return false;
}


 // Checks if a string is numeric(for the purpose of exception handling)

private static boolean isNumeric(String str) {
    return str.matches("-?\\d+(\\.\\d+)?");
}


 // Prompts the user to re-enter the book details if they mistype once and calls the addBook method.

private static void addBookPrompt() {
    String title = JOptionPane.showInputDialog(null, "Enter the book title:");
    String author = JOptionPane.showInputDialog(null, "Enter the book author:");
    String pages = JOptionPane.showInputDialog(null, "Enter the number of pages:");

    addBook(title, author, pages);
}

public static void removeBook(String bookTitle) {
    try {
        List<String> books = Files.readAllLines(Paths.get(BOOKS_FILE_PATH));

        // Find the index of the book to remove
        int bookIndex = findBookIndex(books, bookTitle);
        if (bookIndex != -1) {
            // Remove the book from the list
            books.remove(bookIndex);
            // Write the updated list of books back to the file
            Files.write(Paths.get(BOOKS_FILE_PATH), books);
            JOptionPane.showMessageDialog(null, "Book removed successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "Book not found. Cannot remove the book.");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}


}

