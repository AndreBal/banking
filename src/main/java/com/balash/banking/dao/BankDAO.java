package com.balash.banking.dao;

import com.balash.banking.dao.connection.ConnectionProvider;
import com.balash.banking.model.Bank;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankDAO {

    private Connection connection;

    private static String SQL_INSERT = "INSERT INTO \"bank\" (name) VALUES (?)";
    private static String SQL_SELECT_ALL = "SELECT * FROM \"bank\"";
    private static String SQL_SELECT_BY_ID = "SELECT name FROM \"bank\" WHERE id = ?";
    private static String SQL_UPDATE = "UPDATE \"bank\"  SET name = ? WHERE id = ?";
    private static String SQL_DELETE = "DELETE FROM \"bank\" WHERE id = ?";

    public BankDAO() throws SQLException {
        this.connection = ConnectionProvider.getConnection();
    }

    public void createBank(Bank bank) throws SQLException {
        String sql = SQL_INSERT;
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, bank.getName());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long bankId = generatedKeys.getLong(1);
                    bank.setId(bankId);
                } else {
                    throw new SQLException("Bank insertion failed, no generated keys obtained.");
                }
            }
        }
    }

    public List<Bank> getAllBanks() throws SQLException {
        List<Bank> banks = new ArrayList<>();
        String sql = SQL_SELECT_ALL;
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                Bank bank = new Bank();
                bank.setId(id);
                bank.setName(name);
                banks.add(bank);
            }
        }
        return banks;
    }

    public Bank getBankById(long bankId) throws SQLException {
        String sql = SQL_SELECT_BY_ID;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, bankId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    Bank bank = new Bank();
                    bank.setId(bankId);
                    bank.setName(name);
                    return bank;
                }
            }
        }
        return null; // Bank not found
    }

    public void updateBank(Bank bank) throws SQLException {
        String sql = SQL_UPDATE;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, bank.getName());
            statement.setLong(2, bank.getId());
            statement.executeUpdate();
        }
    }

    public void deleteBank(long bankId) throws SQLException {
        String sql = SQL_DELETE;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, bankId);
            statement.executeUpdate();
        }
    }

}
