package com.balash.banking.dao;

import com.balash.banking.dao.connection.ConnectionProvider;
import com.balash.banking.model.Account;
import com.balash.banking.model.Transaction;
import com.balash.banking.model.constant.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransactionDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionDAO.class);
    private final Lock lock = new ReentrantLock();
    private static final TransactionDAO INSTANCE = new TransactionDAO();

    private AccountDAO accountDAO;

    private static String SQL_INSERT = "INSERT INTO \"transaction\" (amount, transaction_type, date, donor_acc_id, recipient_acc_id) VALUES (?, ?, ?, ?, ?)";
    private static String SQL_SELECT_ALL = "SELECT * FROM \"transaction\"";
    private static String SQL_SELECT_BY_ID = SQL_SELECT_ALL + " WHERE id = ?";
    private static String SQL_UPDATE = "UPDATE \"transaction\" SET amount=?, transaction_type=?, date=?, donor_acc_id=?, recipient_acc_id=? WHERE id = ?";
    private static String SQL_DELETE = "DELETE FROM \"transaction\" WHERE id = ?";

    public static TransactionDAO getInstance() {
        return INSTANCE;
    }

    private TransactionDAO() {
        this.accountDAO = AccountDAO.getInstance();
    }

    public void insertTransaction(Transaction transaction) throws SQLException {
        String sql = SQL_INSERT;
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setDouble(1, transaction.getAmount());
            statement.setString(2, transaction.getTransactionType().name());
            statement.setDate(3, new java.sql.Date(transaction.getTransactionDate().getTime()));
            Account donorAcc = transaction.getDonorAccount();
            if (donorAcc == null) {
                statement.setNull(4, Types.INTEGER);
            } else {
                statement.setLong(4, donorAcc.getId());
            }
            Account recipientAcc = transaction.getRecipientAccount();
            if (recipientAcc == null) {
                statement.setNull(5, Types.INTEGER);
            } else {
                statement.setLong(5, recipientAcc.getId());
            }
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
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                transactions.add(createTransactionFromResultSet(resultSet));
            }
        }
        return transactions;
    }

    public Transaction getTransactionById(long transactionId) throws SQLException {
        String sql = SQL_SELECT_BY_ID;
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
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
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, transactionId);
            statement.executeUpdate();
        }
    }

    public Transaction transferMoney(Account donorAccount, Account recipientAccount, long amount) throws SQLException {
        try (Connection connection = ConnectionProvider.getConnection()) {
            lock.lock();
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                donorAccount.setAmount(donorAccount.getAmount() - amount);
                accountDAO.updateAccount(donorAccount);
                recipientAccount.setAmount(recipientAccount.getAmount() + amount);
                accountDAO.updateAccount(recipientAccount);
                connection.commit();
                Transaction transaction = new Transaction();
                transaction.setAmount(amount);
                transaction.setTransactionDate(new Date());
                transaction.setTransactionType(TransactionType.TRANSFER);
                transaction.setDonorAccount(donorAccount);
                transaction.setRecipientAccount(recipientAccount);
                insertTransaction(transaction);
                return transaction;
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(), e);
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    LOGGER.error("rollbackException", rollbackException);
                }
            } finally {
                connection.setAutoCommit(true);
                lock.unlock();
            }
        }
        return null;
    }

    public synchronized Transaction depositMoney(Account recipientAccount, long amount) throws SQLException {
        recipientAccount.setAmount(recipientAccount.getAmount() + amount);
        accountDAO.updateAccount(recipientAccount);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionDate(new Date());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setRecipientAccount(recipientAccount);
        insertTransaction(transaction);
        return transaction;
    }

    public synchronized Transaction withdrawMoney(Account donorAccount, long amount) throws SQLException {
        donorAccount.setAmount(donorAccount.getAmount() - amount);
        accountDAO.updateAccount(donorAccount);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTransactionDate(new Date());
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setDonorAccount(donorAccount);
        insertTransaction(transaction);
        return transaction;
    }
}
