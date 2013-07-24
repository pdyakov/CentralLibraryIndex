package centrallibrary.logic;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Павел
 * Date: 22.07.13
 * Time: 14:12
 */
public class Book {
    private Integer index;
    private String library;
    private String author;
    private String name;
    private String issued;
    private String issuedTo;

    public Book(Integer index, String library, String author, String name, String issued, String issuedTo) {
        this.index = index;
        this.library = library;
        this.author = author;
        this.name = name;
        this.issued = issued;
        this.issuedTo = issuedTo;
    }

    public Integer getIndex() {
        return index;
    }

    public String getLibrary() {
        return library;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public String getIssued() {
        return issued;
    }

    public String getIssuedTo() {
        return issuedTo;
    }

    public Boolean makeOrder(String issuedTo) {
        Boolean result = false;
        if ((issued == "") && (this.issuedTo == "")) {
            issued = new SimpleDateFormat("yyyy.M.d").format(new Date());
            this.issuedTo = issuedTo;
            result = true;
        }
        return result;
    }

    public String returnBook() {
        String result = null;
        if ((issued != "") && (issuedTo != "")) {
            result = issuedTo;
            issued = "";
            issuedTo = "";
        }
        return result;
    }
}
