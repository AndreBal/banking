package com.balash.banking.dao;

import com.balash.banking.dao.connection.ConnectionProvider;
import com.balash.banking.model.Account;
import com.balash.banking.model.Transaction;
import com.balash.banking.model.constant.TransactionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionDAO {

    private Connection connection;
    private AccountDAO accountDAO;

    private static String SQL_INSERT = "INSERT INTO \"transaction\" (amount, transaction_type, date, donor_acc_id, recipient_acc_id) VALUES (?, ?, ?, ?, ?)";
    private static String SQL_SELECT_ALL = "SELECT * FROM \"transaction\"";
    private static String SQL_SELECT_BY_ID = SQL_SELECT_ALL+" WHERE id = ?";
    private static String SQL_UPDATE = "UPDATE \"transaction\" SET amount=?, transaction_type=?, date=?, donor_acc_id=?, recipient_acc_id=? WHERE id = ?";
    private static String SQL_DELETE = "DELETE FROM \"transaction\" WHERE id = ?";

    public TransactionDAO() throws SQLException {
        this.connection = ConnectionProvider.getConnection();
        this.accountDAO = new AccountDAO();
    }


    public void createTransaction(Transaction transaction) throws SQLException {
        String sql = SQL_INSERT;
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setDouble(1, transaction.getAmount());
            statement.setString(2, transaction.getTransactionType().name());
            statement.setDate(3, new java.sql.Date(transaction.getTransactionDate().getTime()));
            statement.setLong(4, transaction.getDonorAccount().getId());
            statement.setLong(5, transaction.getRecipientAccount().getId());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long transactionId = generatedKeys.getLong(1);
                    transaction.setId(transactionId);
                } else {
                    throw new SQLException("Transaction insertion failed, no generated keys obtained.");
                }
            }
        }
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = SQL_SELECT_ALL;
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                transactions.add(createTransactionFromResultSet(resultSet));
            }
        }
        return transactions;
    }

    public Transaction getTransactionById(long transactionId) throws SQLException {
        String sql = SQL_SELECT_BY_ID;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, transactionId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createTransactionFromResultSet(resultSet);
                }
            }
        }
        return null;
    }

    private Transaction createTransactionFromResultSet(ResultSet resultSet) throws SQLException {
        long transactionId = resultSet.getLong("id");
        long amount = resultSet.getLong("amount");
        Date date = resultSet.getDate("date");
        TransactionType transactionType = TransactionType.valueOf(resultSet.getString("transaction_type"));
        long donorAccountId = resultSet.getLong("donor_acc_id");
        long recipientAccountId = resultSet.getLong("recipient_acc_id");
        Account donorAccount = accountDAO.getAccountById(donorAccountId);
        Account recipientAccount = accountDAO.getAccountById(recipientAccountId);
        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setAmount(amount);
        transaction.setTransactionDate(date);
        transaction.setTransactionType(transactionType);
        transaction.setDonorAccount(donorAccount);
        transaction.setRecipientAccount(recipientAccount);
        return transaction;

    }

    public void deleteTransaction(long transactionId) throws SQLException {
        String sql = SQL_DELETE;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, transactionId);
            statement.executeUpdate();
        }
    }
}
