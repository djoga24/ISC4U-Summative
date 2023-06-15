import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
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

}