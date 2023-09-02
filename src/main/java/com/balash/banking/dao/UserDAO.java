package com.balash.banking.dao;

import com.balash.banking.dao.connection.ConnectionProvider;
import com.balash.banking.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);


    private static String SQL_INSERT = "INSERT INTO \"user\" (username, email) VALUES (?, ?)";
    private static String SQL_SELECT_ALL = "SELECT * FROM \"user\"";
    private static String SQL_SELECT_BY_ID = SQL_SELECT_ALL+" WHERE id = ?";
    private static String SQL_UPDATE = "UPDATE \"user\" SET username = ?, email = ? WHERE id = ?";
    private static String SQL_DELETE = "DELETE FROM \"user\" WHERE id = ?";

    private Connection connection;

    public UserDAO() throws SQLException {
        this.connection = ConnectionProvider.getConnection();
    }

    public void createUser(User user) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long transactionId = generatedKeys.getLong(1);
                    user.setId(transactionId);
                } else {
                    throw new SQLException("User insertion failed, no generated keys obtained.");
                }
            }
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                LOGGER.info("1");
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                LOGGER.info(user.toString());
                users.add(user);
            }
        }
        return users;
    }

    public User getUserById(long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    return user;
                } else {
                    return null;
                }
            }
        }
    }

    public void updateUser(User user) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setLong(3, user.getId());
            statement.executeUpdate();
        }
    }

    public void deleteUser(long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }
}