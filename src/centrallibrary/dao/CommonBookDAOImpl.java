package centrallibrary.dao;

import centrallibrary.logic.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Павел
 * Date: 23.07.13
 * Time: 18:33
 * Purpose: This class implements interface BookDAO to work with all types
 * of libraries, that are listed in its constructor.
 */
public class CommonBookDAOImpl implements BookDAO {
    /* List of BookDAO implementations. Each implementation works
     * with it's type of library. */
    private List<BookDAO> contexts = new ArrayList<BookDAO>();

    public CommonBookDAOImpl() {
        /* Here we can add some more implementations, such as xml or database */
        contexts.add(new CSVBookDAOImpl());
        contexts.add(new TextBookDAOImpl());
    }

    /**
     * Search for books in all libraries by searching arguments.
     *
     * @param author author name
     * @param name   book title
     * @return list of matching books if found, otherwise empty list
     */
    public List<Book> findBooks(String author, String name) {
        List<Book> books = new ArrayList<Book>();
        /* Search for matching books in all types of libraries */
        for(BookDAO context:contexts) {
            books.addAll(context.findBooks(author, name));
        }
        return books;
    }

    /**
     * Search for book in all libraries by book index.
     *
     * @param index book index
     * @return matching book if found, otherwise null
     */
    public Book findBookByIndex(int index) {
        Book book = null;
        /* Search for matching book in all types of libraries */
        for (BookDAO context:contexts) {
            book = context.findBookByIndex(index);
            /* If book found, stop searching and return book */
            if (book != null) return book;
        }
        return book;
    }

    /**
     * Update information about book in the library.
     *
     * @param book book to update
     * @return true if success, otherwise false
     */
    public boolean updateBook(Book book) {
        /* Update book in all types of libraries */
        for (BookDAO context:contexts) {
            /* If book is successfully updated, return true */
            if (context.updateBook(book)) {
                return true;
            }
        }
        return false;
    }
}
