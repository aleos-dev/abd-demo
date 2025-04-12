package org.aleos.exception;

public class DaoException extends RuntimeException {

    public DaoException(String s) {
        super(s);
    }

    public DaoException(String s, Exception e) {
        super(s, e);
    }
}
