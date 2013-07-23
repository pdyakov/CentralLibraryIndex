package centrallibrary.ui;

import centrallibrary.logic.Book;
import centrallibrary.dao.*;
import java.util.List;

public class Main {
    private static BookDAO bookDAO = new TextBookDAOImpl();

    public static void main(String[] args) {
        List<Book> books = bookDAO.Find("", "");
        for (Book book:books) {
            System.out.println(book.getIndex());
            System.out.println(book.getLibrary());
            System.out.println(book.getAuthor());
            System.out.println(book.getName());
            System.out.println(book.getIssued());
            System.out.println(book.getIssuedTo());
            System.out.println("----------");
        }
    }
}
