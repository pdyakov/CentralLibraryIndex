package centrallibrary.dao;

import java.util.List;
import centrallibrary.logic.Book;

/**
 * Created with IntelliJ IDEA.
 * User: Павел
 * Date: 22.07.13
 * Time: 15:54
 * To change this template use File | Settings | File Templates.
 */
public interface BookDAO {
    List<Book> Find(String author, String name);
}
