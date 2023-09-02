package com.balash.banking.server;

import com.balash.banking.dao.AccountDAO;
import com.balash.banking.dao.BankDAO;
import com.balash.banking.dao.TransactionDAO;
import com.balash.banking.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;

@Path("/banking")
public class BankingServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankingServer.class);


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getData() {
        try {
            UserDAO userDao = new UserDAO();
            AccountDAO accountDAO = new AccountDAO();
            BankDAO bankDAO = new BankDAO();
            TransactionDAO transactionDAO = new TransactionDAO();
            String users = Arrays.toString(userDao.getAllUsers().toArray());
            String accounts = Arrays.toString(accountDAO.getAllAccounts().toArray());
            String banks = Arrays.toString(bankDAO.getAllBanks().toArray());
            String transactions = Arrays.toString(transactionDAO.getAllTransactions().toArray());
            String data = users + accounts + banks + transactions;
            LOGGER.debug(data);
            return data;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "Error";
    }

    @Path("/users")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getUsers() {
        try {
            UserDAO userDao = new UserDAO();
            String users = Arrays.toString(userDao.getAllUsers().toArray());
            LOGGER.error(users);
            return users;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "Error";
    }

}