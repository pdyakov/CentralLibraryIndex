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
    List<Book> findBooks(String author, String name);
    Book findBookByIndex(int index);
}
