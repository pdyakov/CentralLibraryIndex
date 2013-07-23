package centrallibrary.ui;

import centrallibrary.logic.Book;
import centrallibrary.dao.*;

import java.util.List;

public class Main {
    private static BookDAO library = new CommonBookDAOImpl();

    public static void main(String[] args) {
        List<Book> books = library.findBooks("", "");
        for (Book book:books) {
            System.out.println(book.getIndex());
            System.out.println(book.getLibrary());
            System.out.println(book.getAuthor());
            System.out.println(book.getName());
            System.out.println(book.getIssued());
            System.out.println(book.getIssuedTo());
            System.out.println("----------");
        }
        Book book = library.findBookByIndex(1006);
        if (book != null) {
            System.out.println(book.getIndex());
            System.out.println(book.getLibrary());
            System.out.println(book.getAuthor());
            System.out.println(book.getName());
            System.out.println(book.getIssued());
            System.out.println(book.getIssuedTo());
            System.out.println("*****");
        }
    }
}
