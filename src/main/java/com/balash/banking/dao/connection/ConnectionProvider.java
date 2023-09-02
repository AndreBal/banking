package com.balash.banking.dao.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.SQLException;

public class ConnectionProvider {

    private static final DataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/banking");
        config.setUsername("postgres");
        config.setPassword("1234");

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
