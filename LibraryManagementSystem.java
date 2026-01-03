import java.io.*;
import java.util.*;

class Book {
    int id;
    String title;
    String author;
    boolean issued;

    Book(int id, String title, String author, boolean issued) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.issued = issued;
    }

    @Override
    public String toString() {
        return id + "," + title + "," + author + "," + issued;
    }
}

public class LibraryManagementSystem {

    static final String FILE_NAME = "books.txt";
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Book");
            System.out.println("2. View All Books");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addBook();
                case 2 -> viewBooks();
                case 3 -> issueBook();
                case 4 -> returnBook();
                case 5 -> {
                    System.out.println("Exiting Library System...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    static void addBook() {
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
            System.out.print("Enter Book ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Title: ");
            String title = sc.nextLine();

            System.out.print("Enter Author: ");
            String author = sc.nextLine();

            Book book = new Book(id, title, author, false);
            fw.write(book + "\n");

            System.out.println("Book added successfully!");
        } catch (IOException e) {
            System.out.println("Error adding book.");
        }
    }

    static void viewBooks() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            System.out.println("\nID | Title | Author | Status");
            System.out.println("--------------------------------");
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                System.out.println(
                        data[0] + " | " + data[1] + " | " + data[2] + " | " +
                        (Boolean.parseBoolean(data[3]) ? "Issued" : "Available")
                );
            }
        } catch (IOException e) {
            System.out.println("No books found.");
        }
    }

    static void issueBook() {
        updateBookStatus(true, "issued");
    }

    static void returnBook() {
        updateBookStatus(false, "returned");
    }

    static void updateBookStatus(boolean issue, String action) {
        File inputFile = new File(FILE_NAME);
        File tempFile = new File("temp.txt");

        System.out.print("Enter Book ID: ");
        int bookId = sc.nextInt();

        boolean found = false;

        try (
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile))
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (Integer.parseInt(data[0]) == bookId) {
                    data[3] = String.valueOf(issue);
                    found = true;
                    pw.println(String.join(",", data));
                } else {
                    pw.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error updating book.");
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);

        if (found)
            System.out.println("Book successfully " + action + "!");
        else
            System.out.println("Book not found!");
    }
}
