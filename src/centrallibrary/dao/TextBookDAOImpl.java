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
 * Purpose: This class implements interface BookDAO to work with Text libraries.
 */
public class TextBookDAOImpl implements BookDAO {
    private static final String LIBRARY_DIRECTORY_PATH = "C:/Libraries";
    private static final String LIBRARY_TYPE_NAME = "Text";
    private Integer lastBookIndex = -1;
    private String lastBookPath = "";

    /**
     * Search for books in all text libraries by searching arguments.
     *
     * @param author author name
     * @param name   book title
     * @return list of matching books if found, otherwise empty list
     */
    public List<Book> findBooks(String author, String name) {
        List<Book> books = new ArrayList<Book>();

        File directory = new File(LIBRARY_DIRECTORY_PATH);
        if (!directory.exists() || !directory.isDirectory()) return books;
        File[] libraries = directory.listFiles();
        if (libraries == null) return books;

        String libraryType;
        for (File library : libraries) {
            if (!library.isDirectory()) continue;
            libraryType = library.getName().split("_")[0];
            /* If it is a text library, then find books in it */
            if (libraryType.equals(LIBRARY_TYPE_NAME)) {
                books.addAll(findBooksInLibrary(library, author, name));
            }
        }

        return books;
    }

    /**
     * Search for book in all text libraries by book index.
     *
     * @param index book index
     * @return matching book if found, otherwise null
     */
    public Book findBookByIndex(int index) {
        Book book = null;

        File directory = new File(LIBRARY_DIRECTORY_PATH);
        if (!directory.exists() || !directory.isDirectory()) return book;
        File[] libraries = directory.listFiles();
        if (libraries == null) return book;

        String libraryType;
        for (File library : libraries) {
            if (!library.isDirectory()) continue;
            libraryType = library.getName().split("_")[0];
            /* If it is a text library, then find book in it */
            if (libraryType.equals(LIBRARY_TYPE_NAME)) {
                book = findBookInLibrary(library, index);
                if (book != null) return book;
            }
        }

        return book;
    }

    /**
     * Update information about book in the text library.
     *
     * @param book book to update
     * @return true if success, otherwise false
     */
    public boolean updateBook(Book book) {
        /* Check if book already found, then update information about it */
        if (lastBookIndex.equals(book.getIndex())) {
            File file = new File(lastBookPath);
            if (file.exists() && file.isFile()) {
                return writeBookToFile(file, book);
            }
        }

        File directory = new File(LIBRARY_DIRECTORY_PATH);
        if (!directory.exists() || !directory.isDirectory()) return false;
        File[] libraries = directory.listFiles();
        if (libraries == null) return false;

        String libraryName;
        String libraryType;
        for (File library : libraries) {
            if (!library.isDirectory()) continue;
            libraryType = library.getName().split("_")[0];
            libraryName = library.getName().split("_")[1];
            /* If it is a text library and book is from this library, then update information about book */
            if (libraryType.equals(LIBRARY_TYPE_NAME) && libraryName.equals(book.getLibrary())) {
                return updateBookInLibrary(library, book);
            }
        }
        return false;
    }

    /**
     * Search for books in text library by searching arguments.
     *
     * @param library library directory
     * @param author  author name
     * @param name    book title
     * @return list of matching books if found, otherwise empty list
     */
    private List<Book> findBooksInLibrary(File library, String author, String name) {
        List<Book> books = new ArrayList<Book>();

        File[] files = library.listFiles();
        if (files == null) return books;

        String libraryName = library.getName().split("_")[1];
        Integer bookIndex;
        String bookAuthor, bookName, bookIssued, bookIssuedTo;
        String[] lines = new String[5];

        BufferedReader br = null;
        for (File file : files) {
            if (!file.isFile() || !file.canRead()) continue;
            try {
                br = new BufferedReader(new FileReader(file));

                /* Read 5 lines of file */
                for (int i = 0; i < 5; i++) {
                    lines[i] = br.readLine();
                }
                bookAuthor = lines[1].split("=")[1];
                bookName = lines[2].split("=")[1];

                /* If book match searching arguments, then pars it and add to results */
                if (bookAuthor.toLowerCase().contains(author.toLowerCase()) &&
                        bookName.toLowerCase().contains(name.toLowerCase())) {
                    bookIndex = Integer.parseInt(lines[0].split("=")[1]);
                    String[] temp = lines[3].split("=");
                    bookIssued = (temp.length == 2) ? temp[1] : "";
                    temp = lines[4].split("=");
                    bookIssuedTo = (temp.length == 2) ? temp[1] : "";
                    books.add(new Book(bookIndex, libraryName, bookAuthor, bookName, bookIssued, bookIssuedTo));
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
        return books;
    }

    /**
     * Search for book in text library by book index.
     *
     * @param library library directory
     * @param index   book index
     * @return matching book if found, otherwise null
     */
    private Book findBookInLibrary(File library, int index) {
        File[] files = library.listFiles();
        if (files == null) return null;
        Integer bookIndex;
        String libraryName = library.getName().split("_")[1];
        BufferedReader br = null;
        String[] lines = new String[5];
        for (File file : files) {
            if (!file.isFile() || !file.canRead()) continue;
            try {
                br = new BufferedReader(new FileReader(file));
                for (int i = 0; i < 5; i++) {
                    lines[i] = br.readLine();
                }
                bookIndex = Integer.parseInt(lines[0].split("=")[1]);
                /* If book match index, then parse it and return */
                if (bookIndex.equals(index)) {
                    /* Save book's index and filePath. If the book will be modified,
                     using this information we can update it faster */
                    lastBookIndex = index;
                    lastBookPath = file.getAbsolutePath();
                    String bookAuthor = lines[1].split("=")[1];
                    String bookName = lines[2].split("=")[1];
                    String[] temp = lines[3].split("=");
                    String bookIssued = (temp.length == 2) ? temp[1] : "";
                    temp = lines[4].split("=");
                    String bookIssuedTo = (temp.length == 2) ? temp[1] : "";
                    return new Book(bookIndex, libraryName, bookAuthor, bookName, bookIssued, bookIssuedTo);
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

        return null;
    }

    /**
     * Update information about book in text library
     *
     * @param library library directory
     * @param book    book to update
     * @return true if success, otherwise false
     */
    private boolean updateBookInLibrary(File library, Book book) {
        File[] files = library.listFiles();
        if (files == null) return false;
        int index;
        BufferedReader br = null;
        for (File file : files) {
            if (!file.isFile() || !file.canRead()) continue;
            try {
                br = new BufferedReader(new FileReader(file));
                index = Integer.parseInt(br.readLine().split("=")[1]);
                if (index == book.getIndex()) {
                    br.close();
                    return writeBookToFile(file, book);
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
        return false;
    }

    /**
     * Write information about book to the text file.
     * @param file file to write
     * @param book book to write
     * @return true if success, otherwise false
     */
    private boolean writeBookToFile(File file, Book book) {
        if (!file.canWrite()) return false;
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            bw.write("Index=" + book.getIndex());
            bw.newLine();
            bw.write("Author=" + book.getAuthor());
            bw.newLine();
            bw.write("Name=" + book.getName());
            bw.newLine();
            bw.write("Issued=" + book.getIssued());
            bw.newLine();
            bw.write("IssuedTo=" + book.getIssuedTo());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
