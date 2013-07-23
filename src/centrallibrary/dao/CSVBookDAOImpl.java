package centrallibrary.dao;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

import centrallibrary.logic.Book;

/**
 * Created with IntelliJ IDEA.
 * User: Павел
 * Date: 22.07.13
 * Time: 16:36
 */
public class CSVBookDAOImpl implements BookDAO {
    private static final String LIBRARY_DIRECTORY_PATH = "C:/Libraries";
    private static final String LIBRARY_TYPE_NAME = "CSV";

    public List<Book> findBooks(String author, String name) {
        List<Book> books = new ArrayList<Book>();

        File[] libraries;
        File directory = new File(LIBRARY_DIRECTORY_PATH);

        if (directory.isDirectory()) {
            libraries = directory.listFiles();

            if (libraries != null) {
                for (File library : libraries) {
                    if (library.getName().split("_")[0].equals(LIBRARY_TYPE_NAME)) {
                        books.addAll(findBooksInLibrary(library, author, name));
                    }
                }
            }
        }

        return books;
    }

    public Book findBookByIndex(int index) {
        Book book = null;

        File[] libraries;
        File directory = new File(LIBRARY_DIRECTORY_PATH);

        if (directory.isDirectory()) {
            libraries = directory.listFiles();

            if (libraries != null) {
                for (File library : libraries) {
                    if (library.getName().split("_")[0].equals(LIBRARY_TYPE_NAME)) {
                        book = findBookInLibrary(library, index);
                        if (book != null) return book;
                    }
                }
            }
        }

        return book;
    }

    private List<Book> findBooksInLibrary(File library, String author, String name) {
        List<Book> books = new ArrayList<Book>();

        File[] files = library.listFiles();

        Integer bookIndex;
        String libraryName = library.getName().split("_")[1];
        String bookAuthor;
        String bookName;
        String bookIssued;
        String bookIssuedTo;

        for (File file : files) {
            if (file.isFile() && file.canRead()) {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = br.readLine()) != null) {
                        bookIssued = "";
                        bookIssuedTo = "";
                        String[] params = line.split(",");
                        bookAuthor = params[1];
                        bookName = params[2];
                        if (bookAuthor.toLowerCase().contains(author.toLowerCase()) &&
                                bookName.toLowerCase().contains(name.toLowerCase())) {
                            bookIndex = Integer.parseInt(params[0]);
                            if (params.length > 3) {
                                bookIssued = params[3];
                                bookIssuedTo = params[4];
                            }
                            books.add(
                                    new Book(bookIndex, libraryName, bookAuthor, bookName, bookIssued, bookIssuedTo)
                            );
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (br != null) br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return books;
    }

    private Book findBookInLibrary(File library, int index) {
        File[] files = library.listFiles();

        Integer bookIndex;
        String libraryName = library.getName().split("_")[1];
        String bookAuthor;
        String bookName;
        String bookIssued;
        String bookIssuedTo;

        for (File file : files) {
            if (file.isFile() && file.canRead()) {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = br.readLine()) != null) {
                        bookIssued = "";
                        bookIssuedTo = "";
                        String[] params = line.split(",");
                        bookIndex = Integer.parseInt(params[0]);
                        if (bookIndex.equals(index)) {
                            bookAuthor = params[1];
                            bookName = params[2];
                            if (params.length > 3) {
                                bookIssued = params[3];
                                bookIssuedTo = params[4];
                            }
                            return new Book(bookIndex, libraryName, bookAuthor, bookName, bookIssued, bookIssuedTo);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (br != null) br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
