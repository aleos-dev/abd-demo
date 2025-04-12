package org.aleos.db;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ConnectionFactory {
    public abstract Connection getConnection() throws SQLException;
}
