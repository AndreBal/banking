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

    private static final AccountDAO INSTANCE = new AccountDAO();

    private BankDAO bankDAO;
    private UserDAO userDAO;
    private static String SQL_INSERT = "INSERT INTO \"account\" (bank_id, amount, user_id, currency, start_date, end_date, is_interest_active)VALUES ( ?, ?, ?, ?, ?, ?, ?);";
    private static String SQL_SELECT_ALL = "SELECT * FROM \"account\"";
    private static String SQL_SELECT_WITH_INTEREST = SQL_SELECT_ALL + " WHERE is_interest_active = true;";
    private static String SQL_SELECT_BY_ID = SQL_SELECT_ALL + " WHERE id = ?";
    private static String SQL_UPDATE = "UPDATE \"account\" SET bank_id=?, amount=?, user_id=?, currency=?, start_date=?, end_date=?, is_interest_active=? WHERE id = ?";
    private static String SQL_DELETE = "DELETE FROM \"account\" WHERE id = ?";

    public static AccountDAO getInstance() {
        return INSTANCE;
    }

    private AccountDAO() {
        this.bankDAO = BankDAO.getInstance();
        this.userDAO = UserDAO.getInstance();
    }


    public void insertAccount(Account account) throws SQLException {
        String sql = SQL_INSERT;
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (account.getBank() != null) {
                statement.setLong(1, account.getBank().getId());
            }else{
                statement.setNull(1,Types.INTEGER);
            }
            if (account.getAmount() != null) {
                statement.setLong(2, account.getAmount());
            }else{
                statement.setNull(2,Types.INTEGER);
            }
            if (account.getUser() != null) {
                statement.setLong(3, account.getUser().getId());
            }else{
                statement.setNull(3,Types.INTEGER);
            }
            if (account.getCurrency() != null) {
                statement.setString(4, account.getCurrency().name());
            }else{
                statement.setNull(4,Types.VARCHAR);
            }
            if (account.getStartDate() != null) {
                statement.setDate(5, new java.sql.Date(account.getStartDate().getTime()));
            }else{
                statement.setNull(5,Types.TIMESTAMP);
            }
            if (account.getEndDate() != null) {
                statement.setDate(6, new java.sql.Date(account.getEndDate().getTime()));
            }else{
                statement.setNull(6,Types.TIMESTAMP);
            }
            if (account.getIsInterestActive() != null) {
                statement.setBoolean(7, account.getIsInterestActive());
            }else{
                statement.setNull(7,Types.BOOLEAN);
            }
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
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                accounts.add(createAccountFromResultSet(resultSet));
            }
        }
        return accounts;
    }

    public List<Account> getAllAccountsWithInterest() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = SQL_SELECT_WITH_INTEREST;
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                accounts.add(createAccountFromResultSet(resultSet));
            }
        }
        return accounts;
    }

    public Account getAccountById(long accountId) throws SQLException {
        String sql = SQL_SELECT_BY_ID;
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
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
        Long id = resultSet.getLong("id");
        Long bankId = resultSet.getLong("bank_id");
        Long amount = resultSet.getLong("amount");
        Long userId = resultSet.getLong("user_id");
        String currencyStr = resultSet.getString("currency");
        Date startDate = resultSet.getDate("start_date");
        Date endDate = resultSet.getDate("end_date");
        Boolean isInterestActive = resultSet.getBoolean("is_interest_active");
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
        account.setIsInterestActive(isInterestActive);
        return account;
    }

    public void updateAccount(Account account, Connection... connections) throws SQLException {
        String sql = SQL_UPDATE;
        boolean connectionIsExternal = connections.length > 0;
        Connection connection = (connectionIsExternal) ? connections[0] : ConnectionProvider.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if (account.getBank() != null) {
                statement.setLong(1, account.getBank().getId());
            }else{
                statement.setNull(1,Types.INTEGER);
            }
            if (account.getAmount() != null) {
                statement.setLong(2, account.getAmount());
            }else{
                statement.setNull(2,Types.INTEGER);
            }
            if (account.getUser() != null) {
                statement.setLong(3, account.getUser().getId());
            }else{
                statement.setNull(3,Types.INTEGER);
            }
            if (account.getCurrency() != null) {
                statement.setString(4, account.getCurrency().name());
            }else{
                statement.setNull(4,Types.VARCHAR);
            }
            if (account.getStartDate() != null) {
                statement.setDate(5, new java.sql.Date(account.getStartDate().getTime()));
            }else{
                statement.setNull(5,Types.TIMESTAMP);
            }
            if (account.getEndDate() != null) {
                statement.setDate(6, new java.sql.Date(account.getEndDate().getTime()));
            }else{
                statement.setNull(6,Types.TIMESTAMP);
            }
            if (account.getIsInterestActive() != null) {
                statement.setBoolean(7, account.getIsInterestActive());
            }else{
                System.out.println("AGA!");
                statement.setNull(7,Types.BOOLEAN);
            }
            statement.setLong(8, account.getId());
            statement.executeUpdate();
            if (!connectionIsExternal) {
                connection.close();
            }
        }
    }

    public void deleteAccount(long accountId) throws SQLException {
        String sql = SQL_DELETE;
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, accountId);
            statement.executeUpdate();
        }
    }

}
