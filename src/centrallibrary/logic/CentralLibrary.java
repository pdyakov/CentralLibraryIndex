package centrallibrary.logic;

import java.util.ArrayList;
import java.util.List;
import centrallibrary.dao.*;

/**
 * Created with IntelliJ IDEA.
 * User: Павел
 * Date: 23.07.13
 * Time: 14:59
 */
public class CentralLibrary {
    private List<BookDAO> contexts = new ArrayList<BookDAO>();

    public CentralLibrary() {
        contexts.add(new CSVBookDAOImpl());
        contexts.add(new TextBookDAOImpl());
    }

    public List<Book> findBooks(String author, String name) {
        List<Book> books = new ArrayList<Book>();
        for(BookDAO context:contexts) {
            books.addAll(context.findBooks(author, name));
        }
        return books;
    }
}
