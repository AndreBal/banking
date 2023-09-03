package com.balash.banking.dao.connection;

import com.balash.banking.config.AppConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.SQLException;

public class ConnectionProvider {

    private static final DataSource dataSource;

    static {
        AppConfig appConfig = AppConfig.getInstance();
        String jdbcUrl = appConfig.getJdbcUrl();
        String dbUsername = appConfig.getDbUsername();
        String dbPassword = appConfig.getDbPassword();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
