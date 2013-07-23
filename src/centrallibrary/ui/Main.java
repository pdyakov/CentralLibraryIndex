package centrallibrary.ui;

import centrallibrary.logic.Book;
import centrallibrary.logic.CentralLibrary;

import java.util.List;

public class Main {
    private static CentralLibrary library = new CentralLibrary();

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
    }
}
