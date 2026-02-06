package io.propenxixi.nasa_library_be.common.constant;

public class ApiMessage {

    // General
    public static final String SUCCESS = "Success";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";

    // Auth
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String LOGIN_FAILED = "Invalid username or password";
    public static final String UNAUTHORIZED = "Unauthorized access";

    // Book
    public static final String BOOK_CREATED = "Book created successfully";
    public static final String BOOK_UPDATED = "Book updated successfully";
    public static final String BOOK_DELETED = "Book deleted successfully";
    public static final String BOOK_NOT_FOUND = "Book not found";

    // Loan
    public static final String LOAN_CREATED = "Book loan created";
    public static final String LOAN_RETURNED = "Book returned";
    public static final String LOAN_OVERDUE = "Loan is overdue";

    private ApiMessage() {}
}
