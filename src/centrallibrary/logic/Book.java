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

    /**
     * Update book information about order
     * @param issuedTo abonent name
     * @return true if success, otherwise false
     */
    public Boolean makeOrder(String issuedTo) {
        if ((issued.equals("")) && (this.issuedTo.equals(""))) {
            issued = new SimpleDateFormat("yyyy.M.d").format(new Date());
            this.issuedTo = issuedTo;
            return true;
        }
        return false;
    }

    /**
     * Update book information about order
     * @return true if success, otherwise false
     */
    public boolean returnBook() {
        if ((!issued.equals("")) && (!issuedTo.equals(""))) {
            issued = "";
            issuedTo = "";
            return true;
        }
        return false;
    }

    /**
     * Return if book can be ordered
     * @return true if success, otherwise false
     */
    public boolean canOrder() {
        return (issued.equals(""));
    }
}
