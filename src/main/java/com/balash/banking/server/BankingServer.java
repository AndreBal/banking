package com.balash.banking.server;

import com.balash.banking.dao.AccountDAO;
import com.balash.banking.dao.BankDAO;
import com.balash.banking.dao.TransactionDAO;
import com.balash.banking.dao.UserDAO;
import com.balash.banking.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Arrays;



@Path("/banking")
public class BankingServer{

    private static final Logger LOGGER = LoggerFactory.getLogger(BankingServer.class);
    private static final String UNKNOWN_ERROR = "Unnknown error";

    private TransactionService transactionService = null;


    private TransactionService getTransactionService() throws SQLException {
        if(transactionService == null){
            transactionService = new TransactionService();
        }
        return transactionService;
    }


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

    @Path("/transfer/{donor}/{recipient}/{amount}")
    @PUT
    @Produces(MediaType.TEXT_PLAIN+"; charset=utf-8")
    public Response transfer(@PathParam("donor") String donor,@PathParam("recipient") String recipient,@PathParam("amount") String amount) {
        String result = UNKNOWN_ERROR;
        try {
            TransactionService transactionService = getTransactionService();
            result = transactionService.transferMoney(donor, recipient, amount);
            //byte[] utf8Bytes = result.getBytes(StandardCharsets.UTF_8);
            //String utf8String = new String(utf8Bytes);
            Response rs = Response.ok().encoding("utf-8")
                    .entity(result).build();
            //System.out.println(utf8String);
            return rs;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result).build();
    }

    @Path("/deposit/{recipient}/{amount}")
    @PUT
    @Produces(MediaType.TEXT_PLAIN+"; charset=utf-8")
    public Response deposit(@PathParam("recipient") String recipient,@PathParam("amount") String amount) {
        String result = UNKNOWN_ERROR;
        try {
            TransactionService transactionService = getTransactionService();
            result = transactionService.depositMoney(recipient, amount);
            byte[] utf8Bytes = result.getBytes(StandardCharsets.UTF_8);
            String utf8String = new String(utf8Bytes);
            System.out.println(utf8String);
            return Response.ok().encoding("utf-8").entity(result).build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result).build();
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