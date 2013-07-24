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
 * Purpose: This class implements interface BookDAO to work with CSV libraries.
 */
public class CSVBookDAOImpl implements BookDAO {
    private static final String LIBRARY_DIRECTORY_PATH = "C:/Libraries";
    private static final String LIBRARY_TYPE_NAME = "CSV";
    private Integer lastBookIndex = -1;
    private String lastBookPath = "";
    private long lastBookLine = 0;

    /**
     * Search for books in all csv libraries by searching arguments.
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
            libraryType = library.getName().split("_")[0];
            /* If it is a csv library, then find books in it */
            if (libraryType.equals(LIBRARY_TYPE_NAME)) {
                books.addAll(findBooksInLibrary(library, author, name));
            }
        }

        return books;
    }

    /**
     * Search for book in all csv libraries by book index.
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

        String libraryName;
        for (File library : libraries) {
            libraryName = library.getName().split("_")[0];
            /* If it is a csv library, then find book in it */
            if (libraryName.equals(LIBRARY_TYPE_NAME)) {
                book = findBookInLibrary(library, index);
                if (book != null) return book;
            }
        }

        return book;
    }

    /**
     * Update information about book in the csv library.
     *
     * @param book book to update
     * @return true if success, otherwise false
     */
    public boolean updateBook(Book book) {
        /* Check if book already found, then update information about it */
        if (lastBookIndex.equals(book.getIndex())) {
            File file = new File(lastBookPath);
            if (file.exists() && file.isFile()) {
                return writeBookToFile(file, book, lastBookLine);
            }
        }
        File directory = new File(LIBRARY_DIRECTORY_PATH);
        if (!directory.exists() || !directory.isDirectory()) return false;
        File[] libraries = directory.listFiles();
        if (libraries == null) return false;

        String libraryType;
        String libraryName;
        for (File library : libraries) {
            libraryType = library.getName().split("_")[0];
            libraryName = library.getName().split("_")[1];
            /* If it is a csv library and book is from this library, then update information about book */
            if (libraryType.equals(LIBRARY_TYPE_NAME) && libraryName.equals(book.getLibrary())) {
                return updateBookInLibrary(library, book);
            }
        }

        return false;
    }

    /**
     * Search for books in csv library by searching arguments.
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

        Integer bookIndex;
        String libraryName = library.getName().split("_")[1];
        String bookAuthor, bookName, bookIssued, bookIssuedTo, line;
        BufferedReader br = null;
        String[] values;

        for (File file : files) {
            if (!file.isFile() || !file.canRead()) continue;

            try {
                br = new BufferedReader(new FileReader(file));

                while ((line = br.readLine()) != null) {
                    values = line.split(",");
                    bookAuthor = values[1];
                    bookName = values[2];
                    /* If book match searching arguments, then pars it and add to results */
                    if (bookAuthor.toLowerCase().contains(author.toLowerCase()) &&
                            bookName.toLowerCase().contains(name.toLowerCase())) {
                        bookIndex = Integer.parseInt(values[0]);
                        bookIssued = (values.length > 3) ? values[3] : "";
                        bookIssuedTo = (values.length > 4) ? values[4] : "";
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
        return books;
    }

    /**
     * Search for book in csv library by book index.
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
        String bookAuthor, bookName, bookIssued, bookIssuedTo, line;
        BufferedReader br = null;
        String[] values;
        long currentLine = 0;

        for (File file : files) {
            if (!file.isFile() || !file.canRead()) continue;
            try {
                br = new BufferedReader(new FileReader(file));
                while ((line = br.readLine()) != null) {
                    values = line.split(",");
                    bookIndex = Integer.parseInt(values[0]);
                    if (bookIndex.equals(index)) {
                        /* Save book's index, line and filePath. If the book will be modified,
                        using this information we can update it faster */
                        lastBookIndex = index;
                        lastBookPath = file.getAbsolutePath();
                        lastBookLine = currentLine;
                        bookAuthor = values[1];
                        bookName = values[2];
                        bookIssued = (values.length > 3) ? values[3] : "";
                        bookIssuedTo = (values.length > 4) ? values[4] : "";
                        /* If book match index, then parse it and return */
                        return new Book(bookIndex, libraryName, bookAuthor, bookName, bookIssued, bookIssuedTo);
                    }
                    currentLine++;
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
     * Update information about book in csv library
     *
     * @param library library directory
     * @param book    book to update
     * @return true if success, otherwise false
     */
    private boolean updateBookInLibrary(File library, Book book) {
        File[] files = library.listFiles();
        if (files == null) return false;

        Integer bookIndex;
        String line;
        BufferedReader br = null;
        String[] values;
        long currentLine = 0;

        for (File file : files) {
            if (!file.isFile() || !file.canRead()) continue;
            try {
                br = new BufferedReader(new FileReader(file));
                while ((line = br.readLine()) != null) {
                    values = line.split(",");
                    bookIndex = Integer.parseInt(values[0]);
                    if (bookIndex.equals(book.getIndex())) {
                        br.close();
                        return writeBookToFile(file, book, currentLine);
                    }
                    currentLine++;
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
     * Write information about book to the csv file.
     *
     * @param file file to write
     * @param book book to write
     * @return true if success, otherwise false
     */
    private boolean writeBookToFile(File file, Book book, long position) {
        if (!file.canWrite()) return false;
        BufferedReader br = null;
        BufferedWriter bw = null;
        File tempFile = new File(file.getAbsolutePath().replace(file.getName(), "temp"));
        String line;
        long currentLine = 0;
        try {
            br = new BufferedReader(new FileReader(file));
            bw = new BufferedWriter(new FileWriter(tempFile));
            while ((line = br.readLine()) != null) {
                if (currentLine != position) {
                    bw.write(line);
                } else {
                    bw.write(book.getIndex() + "," + book.getAuthor() + "," + book.getName() + ","
                            + book.getIssued() + "," + book.getIssuedTo());
                }
                bw.newLine();
                currentLine++;
            }
            bw.close();
            br.close();
            if (!file.delete()) return false;
            if (tempFile.renameTo(file)) return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
                if (bw != null) bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
