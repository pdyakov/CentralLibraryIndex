package centrallibrary.ui;

import centrallibrary.logic.Book;
import centrallibrary.dao.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static BookDAO library = new CommonBookDAOImpl();

    private static final String SYNTAX_ERROR = "SYNTAXERROR";

    /* Enum for input options */
    private enum Option {
        FIND, ORDER, RETURN, EXIT, UNDEFINED;

        public static Option getOption(String option) {
            for (Option opt : Option.values()) {
                if (opt.name().toLowerCase().equals(option.toLowerCase())) {
                    return opt;
                }
            }
            return Option.UNDEFINED;
        }
    }

    /* Enum for input arguments */
    private enum Argument {
        ID, AUTHOR, NAME, ABONENT, UNDEFINED;

        public static Argument getOption(String option) {
            for (Argument arg : Argument.values()) {
                if (arg.name().toLowerCase().equals(option.toLowerCase())) {
                    return arg;
                }
            }
            return Argument.UNDEFINED;
        }
    }

    private static int index;
    private static String author;
    private static String name;
    private static String abonent;
    private static Option inputOption;

    public static void main(String[] args) {
        showMenu();
    }

    /**
     * Run console menu
     */
    public static void showMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            author = "";
            name = "";
            abonent = "";
            index = -1;
            /* Show available options */
            showOptions();
            /* Read input */
            String option = scanner.nextLine();
            /* Parse input, if unsuccessful, then print SYNTAXERROR */
            if (!parseInput(option)) {
                System.out.println(SYNTAX_ERROR);
                continue;
            }
            switch (inputOption) {
                case FIND:
                    findBook(author, name);
                    break;
                case ORDER:
                    if (index != -1 && !abonent.equals("")) {
                        orderBook(index, abonent);
                    } else {
                        System.out.println(SYNTAX_ERROR);
                    }
                    break;
                case RETURN:
                    if (index != -1) {
                        returnBook(index);
                    } else {
                        System.out.println(SYNTAX_ERROR);
                    }
                    break;
                case EXIT:
                    scanner.close();
                    return;
                default:
                    System.out.println(SYNTAX_ERROR);
            }
        }
    }

    /**
     * Run method to find books and print results
     * @param author book author
     * @param name book name
     */
    public static void findBook(String author, String name) {
        List<Book> books = library.findBooks(author, name);
        if (books.size() == 0) System.out.println("NOTFOUND");
        for (Book book : books) {
            if (book.canOrder()) {
                System.out.println("FOUND id=" + book.getIndex() + " lib=" + book.getLibrary());
            } else {
                System.out.println("FOUNDMISSING id=" + book.getIndex() + " lib=" + book.getLibrary()
                        + " issued=" + book.getIssued());
            }
        }
    }

    /**
     * Run methods to order book and print results
     * @param index book index
     * @param abonent abonent name
     */
    public static void orderBook(int index, String abonent) {
        Book book = library.findBookByIndex(index);
        if (book == null) {
            System.out.println("NOTFOUND");
            return;
        }
        if (book.canOrder()) {
            book.makeOrder(abonent);
            library.updateBook(book);
            System.out.println("OK abonent=" + book.getIssuedTo() + " date=" + book.getIssued());
        } else {
            System.out.println("RESERVED abonent=" + book.getIssuedTo() + " date=" + book.getIssued());
        }

    }

    /**
     * Run methods to return book and print results
     * @param index book index
     */
    public static void returnBook(int index) {
        Book book = library.findBookByIndex(index);
        if (book == null) {
            System.out.println("NOTFOUND");
            return;
        }
        if (!book.canOrder()) {
            String abonent = book.getIssuedTo();
            book.returnBook();
            library.updateBook(book);
            System.out.println("OK abonent=" + abonent);
        } else {
            System.out.println("ALREADYRETURNED");
        }
    }

    /**
     * Parse input string
     * @param input input string
     * @return true if success, otherwise false
     */
    public static boolean parseInput(String input) {
        String[] elements = input.split(" ");
        if (elements.length == 0) return false;
        inputOption = Option.getOption(elements[0]);
        if (elements.length > 1) {
            if (!parseArgument(elements[1])) return false;
        }
        if (elements.length > 2) {
            if (!parseArgument(elements[2])) return false;
        }
        return true;
    }

    /**
     * Parse a string of argument
     * @param argument argument string
     * @return true if success, otherwise false
     */
    public static boolean parseArgument(String argument) {
        String[] args = argument.split("=");
        if (args.length == 0) return false;
        Argument argName = Argument.getOption(args[0]);
        switch (argName) {
            case ID:
                if (args.length < 2) return false;
                try {
                    index = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return false;
                }
                break;
            case AUTHOR:
                author = (args.length > 1) ? args[1] : "";
                break;
            case NAME:
                name = (args.length > 1) ? args[1] : "";
                break;
            case ABONENT:
                if (args.length < 2) return false;
                abonent = args[1];
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Show available options
     */
    public static void showOptions() {
        System.out.println("==============================================");
        System.out.println("|              AVAILABLE OPTIONS:            |");
        System.out.println("==============================================");
        System.out.println("| FIND [author=<author>] [name=<bookname>]   |");
        System.out.println("| ORDER [id=<index> abonent=<name>]          |");
        System.out.println("| RETURN [id=<index>]                        |");
        System.out.println("| EXIT                                       |");
        System.out.println("==============================================");
        System.out.print("Enter an option: ");
    }
}
