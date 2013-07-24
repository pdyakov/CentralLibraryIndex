package centrallibrary.dao;

import java.util.List;
import centrallibrary.logic.Book;

/**
 * Created with IntelliJ IDEA.
 * User: Павел
 * Date: 22.07.13
 * Time: 15:54
 */
public interface BookDAO {

    /**
     * Search for books in all libraries by searching arguments.
     *
     * @param author author name
     * @param name   book title
     * @return list of matching books if found, otherwise empty list
     */
    List<Book> findBooks(String author, String name);

    /**
     * Search for book in all libraries by book index.
     *
     * @param index book index
     * @return matching book if found, otherwise null
     */
    Book findBookByIndex(int index);

    /**
     * Update information about book in the library.
     *
     * @param book book to update
     * @return true if success, otherwise false
     */
    boolean updateBook(Book book);
}
