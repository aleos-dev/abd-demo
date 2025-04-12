package org.aleos.transaction;

import lombok.RequiredArgsConstructor;
import org.aleos.db.ConnectionFactory;
import org.aleos.exception.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
public class TransactionManager {

    private final ConnectionFactory connectionFactory;

    public void beginTransaction() {
        try {
            Connection connection = connectionFactory.getConnection();
            connection.setAutoCommit(false);
            TransactionContext.setConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void commitTransaction() {
        Connection connection = null;
        try {
            connection = TransactionContext.getConnection();
            connection.commit();
        } catch (SQLException e) {
            throw new TransactionException("Unable to commit transaction", e);
        } finally {
            clear();
            close(connection);
        }
    }

    public void rollbackTransaction() {
        Connection connection = null;
        try {
            connection = TransactionContext.getConnection();
            connection.rollback();
        } catch (SQLException e) {
            throw new TransactionException("Unable to rollback transaction", e);
        } finally {
            clear();
            close(connection);
        }
    }

    private void close(Connection connection) {
        try {
            if (connection != null || !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new TransactionException("Unable to close connection", e);
        }
    }

    private void clear() {
        TransactionContext.cleareContext();
    }
}
