package org.aleos.transaction;

import org.aleos.exception.TransactionException;

import javax.naming.OperationNotSupportedException;
import java.sql.Connection;

public final class TransactionContext {

    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    private TransactionContext() throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Utility class");
    }

    public static Connection getConnection() {
        if (connectionThreadLocal.get() == null) {
            throw new TransactionException("No connection found in the context");
        }
        return connectionThreadLocal.get();
    }

    public static void setConnection(Connection connection) {
        if (connectionThreadLocal.get() != null) {
            throw new TransactionException("Connection already set in the context");
        }
        connectionThreadLocal.set(connection);
    }

    public static void cleareContext() {
        connectionThreadLocal.remove();
    }
}
