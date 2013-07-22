package centrallibrary.logic;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Павел
 * Date: 22.07.13
 * Time: 14:12
 * To change this template use File | Settings | File Templates.
 */
public class Book {
    private String author;
    private String name;
    private Date issued;
    private String issuedTo;

    public Book(String author, String name, Date issued, String issuedTo) {
        this.author = author;
        this.name = name;
        this.issued = issued;
        this.issuedTo = issuedTo;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public Date getIssued() {
        return issued;
    }

    public String getIssuedTo() {
        return issuedTo;
    }

    public Boolean makeOrder(String issuedTo) {
        Boolean result = false;
        if ((this.issued == null) && (this.issuedTo == null)) {
            this.issued = new Date();
            this.issuedTo = issuedTo;
            result = true;
        }
        return result;
    }

    public String returnBook() {
        String result = null;
        if ((this.issued != null) && (this.issuedTo != null)) {
            result = this.issuedTo;
            this.issued = null;
            this.issuedTo = null;
        }
        return result;
    }
}
