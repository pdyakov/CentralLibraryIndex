package centrallibrary.dao;

import centrallibrary.logic.Book;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Павел
 * Date: 23.07.13
 * Time: 13:30
 */
public class TextBookDAOImpl implements BookDAO {
    private static final String LIBRARY_DIRECTORY_PATH = "C:/Libraries";
    private static final String LIBRARY_TYPE_NAME = "Text";

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
                bookIssued = "";
                bookIssuedTo = "";
                BufferedReader br = null;
                String[] lines = new String[5];
                try {
                    br = new BufferedReader(new FileReader(file));
                    for (int i = 0; i < 5; i++) {
                        lines[i] = br.readLine();
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
                if (lines[1].split("=")[1].toLowerCase().contains(author.toLowerCase()) &&
                        lines[2].split("=")[1].toLowerCase().contains(name.toLowerCase())) {
                    bookIndex = Integer.parseInt(lines[0].split("=")[1]);
                    bookAuthor = lines[1].split("=")[1];
                    bookName = lines[2].split("=")[1];
                    String[] temp = lines[3].split("=");
                    if (temp.length == 2) bookIssued = temp[1];
                    temp = lines[4].split("=");
                    if (temp.length == 2) bookIssuedTo = temp[1];
                    books.add(new Book(bookIndex, libraryName, bookAuthor, bookName, bookIssued, bookIssuedTo));
                }
            }
        }
        return books;
    }
}
