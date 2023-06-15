import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class Books {
    private static final String BOOKS_FILE_PATH = "books.txt";
    private static JFrame frame;
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
            buttonPanel.removeAll();
            buttonPanel.add(btnBorrowBook);
        } else if (loggedInUserType.equals("Admin")) {
            JButton btnLoanBook = new JButton("Loan Book");
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

}