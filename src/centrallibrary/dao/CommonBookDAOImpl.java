package centrallibrary.dao;

import centrallibrary.logic.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Павел
 * Date: 23.07.13
 * Time: 18:33
 */
public class CommonBookDAOImpl implements BookDAO {
    private List<BookDAO> contexts = new ArrayList<BookDAO>();

    public CommonBookDAOImpl() {
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

    public Book findBookByIndex(int index) {
        Book book = null;
        for (BookDAO context:contexts) {
            book = context.findBookByIndex(index);
            if (book != null) return book;
        }
        return book;
    }
}
