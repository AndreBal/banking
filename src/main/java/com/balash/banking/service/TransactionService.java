package com.balash.banking.service;

import com.balash.banking.dao.AccountDAO;
import com.balash.banking.dao.TransactionDAO;
import com.balash.banking.model.Account;
import com.balash.banking.model.Transaction;
import com.balash.banking.service.util.TextToFileSaver;
import com.balash.banking.service.util.ReceiptTextFormatter;
import com.balash.banking.service.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class TransactionService {

    private static final String INCORRECT_ACCOUNT_FORMAT_MESSAGE = "Incorrect account number format";
    private static final String INCORRECT_AMOUNT_FORMAT_MESSAGE = "Incorrect amount format";
    private static final String SQLEXCEPTION_ERROR_MESSAGE = "An error occurred while trying to connect to the database";
    private static final String TRANSFER_ERROR_MESSAGE = "An error occurred while trying to transfer money. The operation was not completed";
    private static final String DEPOSIT_ERROR_MESSAGE = "An error occurred while trying to deposit money. The operation was not completed";
    private static final String INSUFFICIENT_FUNDS_MESSAGE = "Insufficient funds to perform this operation";
    private static final String RECEIPT = "receipt ";
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    private Utils utils;
    private ReceiptTextFormatter receiptTextFormatter;
    private TextToFileSaver textToFileSaver;
    private TransactionDAO transactionDAO;
    private AccountDAO accountDAO;

    public TransactionService(){
        this.transactionDAO = TransactionDAO.getInstance();
        this.accountDAO = AccountDAO.getInstance();
        this.utils = Utils.getInstance();
        this.receiptTextFormatter = ReceiptTextFormatter.getInstance();
        this.textToFileSaver = TextToFileSaver.getInstance();
    }

    public String transferMoney(String donorAccountIdStr, String recipientAccountIdStr, String amountString){
        long donorAccountId ;
        long recipientAccountId;
        try {
            donorAccountId = Long.parseLong(donorAccountIdStr);
            recipientAccountId = Long.parseLong(recipientAccountIdStr);
        }catch (Exception e){
            LOGGER.error(e.getMessage()+" for donorAccountId "+donorAccountIdStr+" and recipientAccountId "+recipientAccountIdStr);
            return INCORRECT_ACCOUNT_FORMAT_MESSAGE;
        }
        long amount;
        try {
            amount  = utils.convertToCents(amountString);
        }catch (Exception e){
            LOGGER.error(e.getMessage()+" for amountString "+amountString);
            return INCORRECT_AMOUNT_FORMAT_MESSAGE;
        }

        Account donorAccount;
        Account recipientAccount;
        try {
            donorAccount = accountDAO.getAccountById(donorAccountId);
            recipientAccount = accountDAO.getAccountById(recipientAccountId);
        } catch (SQLException e) {
            LOGGER.error("An sqlexception occurred when attempting to extract accounts with id "+donorAccountId+
                    " and "+recipientAccountId,e);
            return SQLEXCEPTION_ERROR_MESSAGE;
        }
        if(donorAccount.getAmount() < amount){
            return INSUFFICIENT_FUNDS_MESSAGE;
        }
        Transaction transaction = null;
        try {
            transaction = transactionDAO.transferMoney(donorAccount,recipientAccount,amount);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(),e);
            return TRANSFER_ERROR_MESSAGE;
        }
        if(transaction != null){
            String receipt = receiptTextFormatter.TransactionReceipt(transaction);
            textToFileSaver.saveToTXTFile(RECEIPT+transaction.getId(),receipt);
            return receipt;
        }
        return TRANSFER_ERROR_MESSAGE;
    }

    public String depositMoney(String recipientAccountIdStr, String amountString){
        long recipientAccountId;
        try {
            recipientAccountId = Long.parseLong(recipientAccountIdStr);
        }catch (Exception e){
            LOGGER.error(e.getMessage()+" and recipientAccountId "+recipientAccountIdStr);
            return INCORRECT_ACCOUNT_FORMAT_MESSAGE;
        }
        long amount;
        try {
            amount  = utils.convertToCents(amountString);
        }catch (Exception e){
            LOGGER.error(e.getMessage()+" for amountString "+amountString);
            return INCORRECT_AMOUNT_FORMAT_MESSAGE;
        }

        Account recipientAccount;
        try {
            recipientAccount = accountDAO.getAccountById(recipientAccountId);
        } catch (SQLException e) {
            LOGGER.error("An sqlexception occurred when attempting to extract account with id "+recipientAccountId,e);
            return SQLEXCEPTION_ERROR_MESSAGE;
        }
        Transaction transaction = null;
        try {
            transaction = transactionDAO.depositMoney(recipientAccount, amount);
        }catch (SQLException e){
            LOGGER.error(e.getMessage(),e);
            return DEPOSIT_ERROR_MESSAGE;
        }
        if(transaction != null){
            String receipt = receiptTextFormatter.TransactionReceipt(transaction);
            textToFileSaver.saveToTXTFile(RECEIPT+transaction.getId(),receipt);
            return receipt;
        }
        return DEPOSIT_ERROR_MESSAGE;
    }

    public String withdrawMoney(String donorAccountIdStr, String amountString){
        long donorAccountId ;
        try {
            donorAccountId = Long.parseLong(donorAccountIdStr);
        }catch (Exception e){
            LOGGER.error(e.getMessage()+" for donorAccountId "+donorAccountIdStr);
            return INCORRECT_ACCOUNT_FORMAT_MESSAGE;
        }
        long amount;
        try {
            amount  = utils.convertToCents(amountString);
        }catch (Exception e){
            LOGGER.error(e.getMessage()+" for amountString "+amountString);
            return INCORRECT_AMOUNT_FORMAT_MESSAGE;
        }
        Account donorAccount;
        try {
            donorAccount = accountDAO.getAccountById(donorAccountId);
        } catch (SQLException e) {
            LOGGER.error("An sqlexception occurred when attempting to extract account with id "+donorAccountId,e);
            return SQLEXCEPTION_ERROR_MESSAGE;
        }
        if(donorAccount.getAmount() < amount){
            return INSUFFICIENT_FUNDS_MESSAGE;
        }
        Transaction transaction = null;
        try {
            transaction = transactionDAO.withdrawMoney(donorAccount,amount);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(),e);
            return TRANSFER_ERROR_MESSAGE;
        }
        if(transaction != null){
            String receipt = receiptTextFormatter.TransactionReceipt(transaction);
            textToFileSaver.saveToTXTFile(RECEIPT+transaction.getId(),receipt);
            return receipt;
        }
        return TRANSFER_ERROR_MESSAGE;
    }
}
