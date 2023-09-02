package com.balash.banking.dao;

import com.balash.banking.dao.connection.ConnectionProvider;
import com.balash.banking.model.Account;
import com.balash.banking.model.Bank;
import com.balash.banking.model.User;
import com.balash.banking.model.constant.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDAO.class);

    private BankDAO bankDAO;
    private UserDAO userDAO;
    private static String SQL_INSERT = "INSERT INTO \"account\" (bank_id, amount, user_id, currency, start_date, end_date)VALUES ( ?, ?, ?, ?, ?, ?);";
    private static String SQL_SELECT_ALL = "SELECT * FROM \"account\"";
    private static String SQL_SELECT_BY_ID = SQL_SELECT_ALL+" WHERE id = ?";
    private static String SQL_UPDATE = "UPDATE \"account\" SET bank_id=?, amount=?, user_id=?, currency=? WHERE id = ?";
    private static String SQL_DELETE = "DELETE FROM \"account\" WHERE id = ?";

    private Connection connection;

    public AccountDAO() throws SQLException {
        this.connection = ConnectionProvider.getConnection();
        this.bankDAO = new BankDAO();
        this.userDAO = new UserDAO();
    }

    public void createAccount(Account account) throws SQLException {
        String sql = SQL_INSERT;
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, account.getBank().getId());
            statement.setLong(2, account.getAmount());
            statement.setLong(3, account.getUser().getId());
            statement.setString(4, account.getCurrency().name());
            statement.setDate(5, new java.sql.Date(account.getStartDate().getTime()));
            statement.setDate(6, new java.sql.Date(account.getEndDate().getTime()));
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long accountId = generatedKeys.getLong(1);
                    account.setId(accountId);
                } else {
                    throw new SQLException("Account insertion failed, no generated keys obtained.");
                }
            }
        }
    }

    public List<Account> getAllAccounts() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = SQL_SELECT_ALL;
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                accounts.add(createAccountFromResultSet(resultSet));
            }
        }
        return accounts;
    }

    public Account getAccountById(long accountId) throws SQLException {
        String sql = SQL_SELECT_BY_ID;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, accountId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createAccountFromResultSet(resultSet);
                }
            }
        }
        return null; // Account not found
    }

    private Account createAccountFromResultSet(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        long bankId = resultSet.getLong("bank_id");
        long amount = resultSet.getLong("amount");
        long userId = resultSet.getLong("user_id");
        String currencyStr = resultSet.getString("currency");
        Date startDate = resultSet.getDate("start_date");
        Date endDate = resultSet.getDate("end_date");
        Currency currency = Currency.valueOf(currencyStr);
        Bank bank = bankDAO.getBankById(bankId);
        User user = userDAO.getUserById(userId);
        Account account = new Account();
        account.setId(id);
        account.setBank(bank);
        account.setAmount(amount);
        account.setUser(user);
        account.setCurrency(currency);
        account.setStartDate(startDate);
        account.setEndDate(endDate);
        return account;
    }

    public void updateAccount(Account account) throws SQLException {
        String sql = SQL_UPDATE;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, account.getBank().getId());
            statement.setLong(2, account.getAmount());
            statement.setLong(3, account.getUser().getId());
            statement.setString(4, account.getCurrency().name());
            statement.setLong(5, account.getId());
            statement.setDate(5, new java.sql.Date(account.getStartDate().getTime()));
            statement.setDate(6, new java.sql.Date(account.getEndDate().getTime()));
            statement.executeUpdate();
        }
    }

    public void deleteAccount(long accountId) throws SQLException {
        String sql = SQL_DELETE;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, accountId);
            statement.executeUpdate();
        }
    }
}
