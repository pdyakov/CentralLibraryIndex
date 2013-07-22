package centrallibrary.ui;

import centrallibrary.logic.Book;

public class Main {

    public static void main(String[] args) {
        Book book1 = new Book("Dyakov", "Some name", null, null);
        System.out.println(book1.getAuthor());
        System.out.println(book1.getName());
        System.out.println(book1.getIssued());
        System.out.println(book1.getIssuedTo());

        if (book1.makeOrder("Pavel")) {
            System.out.println(book1.getAuthor());
            System.out.println(book1.getName());
            System.out.println(book1.getIssued());
            System.out.println(book1.getIssuedTo());
        }
        String returnResult = book1.returnBook();
        if (returnResult != null) {
            System.out.println("Book is successfully returned for " + returnResult);
            System.out.println(book1.getAuthor());
            System.out.println(book1.getName());
            System.out.println(book1.getIssued());
            System.out.println(book1.getIssuedTo());
        } else {
            System.out.println("Book is already returned");
        }
        returnResult = book1.returnBook();
        if (returnResult != null) {
            System.out.println("Book is successfully returned");
            System.out.println(book1.getAuthor());
            System.out.println(book1.getName());
            System.out.println(book1.getIssued());
            System.out.println(book1.getIssuedTo());
        } else {
            System.out.println("Book is already returned");
        }
    }
}
