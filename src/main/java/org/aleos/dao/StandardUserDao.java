package org.aleos.dao;

import org.aleos.exception.DaoException;
import org.aleos.model.User;
import org.aleos.transaction.TransactionContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class StandardUserDao implements UserDao {

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_MERITS_QUERY = "UPDATE users SET merits = ? WHERE id = ?";

    private static final int USER_ID_COLUMN = 1;
    private static final int USER_NAME_COLUMN = 2;
    private static final int USER_MERITS_COLUMN = 3;

    @Override
    public Optional<User> findById(long id) {
        try (var statement = getConnection().prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();

            return resultSet.next()
                    ? Optional.of(mapToUser(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Error finding user by id");
        }
    }

    @Override
    public void updateMerits(long userId, long merits) {
        try (var statement = getConnection().prepareStatement(UPDATE_MERITS_QUERY)) {
            prepareUpdateMeritsStatement(statement, merits, userId);
            if (statement.executeUpdate() != 1) {
                throw new DaoException("No rows updated");
            }
        } catch (SQLException e) {
            throw new DaoException("Error updating user merits", e);
        }
    }

    private User mapToUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong(USER_ID_COLUMN),
                resultSet.getString(USER_NAME_COLUMN),
                resultSet.getLong(USER_MERITS_COLUMN)
        );
    }

    private void prepareUpdateMeritsStatement(PreparedStatement statement, long merits, long userId) throws SQLException {
        statement.setLong(1, merits);
        statement.setLong(2, userId);
    }

    private Connection getConnection() throws SQLException {
        return TransactionContext.getConnection();
    }
}
