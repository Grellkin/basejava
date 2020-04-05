package ru.javawebinar.basejava.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {

    private final ConnectionFactory factory;
    private static final Logger LOGGER = LogManager.getLogger("sqlLogger");

    public SqlHelper(ConnectionFactory factory) {
        this.factory = factory;
    }


    public interface SqlExecutor<T> {
        T execute(PreparedStatement statement) throws SQLException;
    }

    public <T> T doSQL(String sql, SqlExecutor<T> executor) {
        try (Connection con = factory.getConnection()) {
            PreparedStatement statement = con.prepareStatement(sql);
            return executor.execute(statement);
        } catch (SQLException e) {
            LOGGER.warn("SQL query " + sql + " fall with exception");
            throw new StorageException("Sorry, some problem with DB.", e);
        }
    }

}
