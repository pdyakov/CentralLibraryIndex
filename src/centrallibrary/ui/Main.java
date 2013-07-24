package centrallibrary.ui;

import centrallibrary.logic.Book;
import centrallibrary.dao.*;

public class Main {
    private static BookDAO library = new CommonBookDAOImpl();

    public static void main(String[] args) {
        Book book3 = library.findBookByIndex(1000);
        book3.returnBook();
        library.updateBook(book3);
    }
}
