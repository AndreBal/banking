package com.balash.banking.service.interest;

import com.balash.banking.dao.AccountDAO;
import com.balash.banking.dao.TransactionDAO;
import com.balash.banking.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class InterestCounter {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterestCounter.class);
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private static final int INTEREST_RATE = 1;

    public InterestCounter() throws SQLException {
        this.transactionDAO = new TransactionDAO();
        this.accountDAO = transactionDAO.getAccountDAO();
    }

    public void count() throws SQLException {
        List<Account> interestAccounts = accountDAO.getAllAccountsWithInterest();
        interestAccounts.forEach(account -> {
            try {
                transactionDAO.depositMoney(account,account.getAmount()*INTEREST_RATE/100);
                LOGGER.info("Money deposited");
            } catch (SQLException e) {
                LOGGER.error("Error while depositing interest for account "+account.getId()+" with message "+e.getMessage(),e);
            }
        });
    }

}
