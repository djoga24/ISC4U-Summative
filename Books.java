import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;

public class Books {
    private static final String BOOKS_FILE_PATH = "books.txt";
    private static final String ACCOUNTS_FILE_PATH = "accounts.txt";
    private static JFrame frame;
    private static String loggedInUser = LibraryGUI.getUser();
    private static String loggedInUserType = LibraryGUI.getUserType();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Example usage: View Books
            viewBooks();
        });
    }

public static void viewBooks() {
    try {
        Path filePath = Paths.get(BOOKS_FILE_PATH);
        if (!Files.exists(filePath)) {
            System.err.println("Books file does not exist.");
            return;
        }

        List<String> books = Files.readAllLines(filePath);

        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        String[] columnNames = {"Title", "Author", "Pages"};
        Object[][] data = new Object[books.size()][3];

        for (int i = 0; i < books.size(); i++) {
            String[] parts = books.get(i).split(",");

            if (parts.length >= 3) {
                data[i][0] = parts[0].trim();
                data[i][1] = parts[1].trim();
                data[i][2] = parts[2].trim();
            }
        }

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

        if (loggedInUserType.equals("User")) {
            JButton btnBorrowBook = new JButton("Borrow Book");
            btnBorrowBook.addActionListener(e -> {
                String bookToBorrow = JOptionPane.showInputDialog(frame, "Enter the book you want to borrow:");
                if (bookToBorrow != null && !bookToBorrow.isEmpty()) {
                    borrowBook(bookToBorrow);
                }
            });
            buttonPanel.removeAll();
            buttonPanel.add(btnBorrowBook);
        } else if (loggedInUserType.equals("Admin")) {
            JButton btnLoanBook = new JButton("Loan Book");
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


public static void borrowBook(String bookTitle) {
    try {
        List<String> books = Files.readAllLines(Paths.get(BOOKS_FILE_PATH));

        // Check if the book is available
        int bookIndex = findBookIndex(books, bookTitle);
        if (bookIndex != -1) {
            List<String> accounts = Files.readAllLines(Paths.get(ACCOUNTS_FILE_PATH));

            // Find the user's account
            int userIndex = findUserIndex(accounts, loggedInUser);
            if (userIndex != -1) {
                String[] bookDetails = books.get(bookIndex).split(",");
                String bookAuthor = bookDetails[1].trim();
                String bookPages = bookDetails[2].trim();

                // Update the books file
                books.remove(bookIndex);
                writeLinesToFile(books, BOOKS_FILE_PATH);

                // Update the accounts file
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


private static int findUserIndex(List<String> accounts, String username) {
    for (int i = 0; i < accounts.size(); i++) {
        String[] accountParts = accounts.get(i).split(",");
        if (accountParts[0].trim().equalsIgnoreCase(username)) {
            return i;
        }
    }
    return -1;
}

private static void writeLinesToFile(List<String> lines, String filePath) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
    for (String line : lines) {
        writer.write(line);
        writer.newLine();
    }
    writer.close();
}


public static void loanBook(String bookToLoan, String userToLoan) {
    try {
        List<String> books = Files.readAllLines(Paths.get(BOOKS_FILE_PATH));
        List<String> accounts = Files.readAllLines(Paths.get(ACCOUNTS_FILE_PATH));

        int bookIndex = findBookIndex(books, bookToLoan);
        if (bookIndex == -1) {
            JOptionPane.showMessageDialog(null, "Book does not exist.");
            return;
        }

        int userIndex = findUserIndex(accounts, userToLoan);
        if (userIndex == -1) {
            JOptionPane.showMessageDialog(null, "User does not exist.");
            return;
        }

        String[] accountParts = accounts.get(userIndex).split(",");
        if (isBookAlreadyBorrowed(accountParts, bookToLoan)) {
            JOptionPane.showMessageDialog(null, "User already has the book.");
            return;
        }

        String[] bookDetails = books.get(bookIndex).split(",");
        String bookAuthor = bookDetails[1].trim();
        String bookPages = bookDetails[2].trim();

        accounts.set(userIndex, accounts.get(userIndex) + "," + bookToLoan + "," + bookAuthor + "," + bookPages);
        books.remove(bookIndex);

        writeLinesToFile(accounts, ACCOUNTS_FILE_PATH);
        writeLinesToFile(books, BOOKS_FILE_PATH);

        JOptionPane.showMessageDialog(null, "Book loaned successfully.");
        frame.dispose();
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "An error occurred while loaning the book.");
    }
}

private static boolean isBookAlreadyBorrowed(String[] accountParts, String bookToLoan) {
    for (int i = 3; i < accountParts.length; i++) {
        if (accountParts[i].equalsIgnoreCase(bookToLoan)) {
            return true;
        }
    }
    return false;
}


public static void returnBook(String bookTitle) {
    try {
        List<String> books = Files.readAllLines(Paths.get(BOOKS_FILE_PATH));
        List<String> accounts = Files.readAllLines(Paths.get(ACCOUNTS_FILE_PATH));

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

        for (int i = 0; i < booksInAccount.length; i += 3) {
            if (!booksInAccount[i].trim().equalsIgnoreCase(bookTitle)) {
                updatedAccount.append(booksInAccount[i]).append(",").append(booksInAccount[i + 1]).append(",")
                        .append(booksInAccount[i + 2]).append(",");
            } else {
                bookReturned = true;
                updatedBooks.add(booksInAccount[i] + "," + booksInAccount[i + 1] + "," + booksInAccount[i + 2]);
            }
        }

        if (!bookReturned) {
            JOptionPane.showMessageDialog(null, "You don't have this book in your account.");
            return;
        }

        accounts.set(userIndex, String.join(",", Arrays.copyOfRange(accountParts, 0, 3))
                + (updatedAccount.length() > 0 ? updatedAccount.substring(0, updatedAccount.length() - 1) : ""));

        Files.write(Paths.get(BOOKS_FILE_PATH), String.join("\n", updatedBooks).getBytes());
        Files.write(Paths.get(ACCOUNTS_FILE_PATH), String.join("\n", accounts).getBytes());

        JOptionPane.showMessageDialog(null, "Book returned successfully.");
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public static void addBook(String title, String author, String pages) {
    try {
        validateBookInputs(title, author, pages);

        if (isBookTitleExists(title)) {
            throw new IllegalArgumentException("Book with the same title already exists.");
        }

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

        // Reprompt user to enter book details
        addBookPrompt();
    }
}

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


private static boolean isNumeric(String str) {
    return str.matches("-?\\d+(\\.\\d+)?");
}


private static void addBookPrompt() {
    String title = JOptionPane.showInputDialog(null, "Enter the book title:");
    String author = JOptionPane.showInputDialog(null, "Enter the book author:");
    String pages = JOptionPane.showInputDialog(null, "Enter the number of pages:");

    addBook(title, author, pages);
}

public static void removeBook(String bookTitle) {
    try {
        List<String> books = Files.readAllLines(Paths.get(BOOKS_FILE_PATH));

        int bookIndex = findBookIndex(books, bookTitle);
        if (bookIndex != -1) {
            books.remove(bookIndex);
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


