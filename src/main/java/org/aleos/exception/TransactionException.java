package org.aleos.exception;

import java.sql.SQLException;

public class TransactionException extends RuntimeException {
    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, SQLException e) {
        super(message, e);
    }
}
