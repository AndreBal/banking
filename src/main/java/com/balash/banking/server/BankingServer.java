package com.balash.banking.server;

import com.balash.banking.dao.UserDAO;
import com.balash.banking.dao.connection.ConnectionProvider;
import com.balash.banking.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;


@Path("/banking")
public class BankingServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankingServer.class);
    private static final String UNKNOWN_ERROR = "Unnknown error";

    private TransactionService transactionService = null;

    private TransactionService getTransactionService(){
        if (transactionService == null) {
            transactionService = new TransactionService();
        }
        return transactionService;
    }

    @Path("/transfer/{donor}/{recipient}/{amount}")
    @PUT
    @Produces(MediaType.TEXT_PLAIN + "; charset=utf-8")
    public Response transfer(@PathParam("donor") String donor, @PathParam("recipient") String recipient, @PathParam("amount") String amount) {
        String result = UNKNOWN_ERROR;
        try {
            TransactionService transactionService = getTransactionService();
            result = transactionService.transferMoney(donor, recipient, amount);
            return Response.ok().encoding("utf-8").entity(result).build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result).build();
    }

    @Path("/deposit/{recipient}/{amount}")
    @PUT
    @Produces(MediaType.TEXT_PLAIN + "; charset=utf-8")
    public Response deposit(@PathParam("recipient") String recipient, @PathParam("amount") String amount) {
        String result = UNKNOWN_ERROR;
        try {
            TransactionService transactionService = getTransactionService();
            result = transactionService.depositMoney(recipient, amount);
            return Response.ok().encoding("utf-8").entity(result).build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result).build();
    }

    @Path("/withdraw/{donor}/{amount}")
    @PUT
    @Produces(MediaType.TEXT_PLAIN + "; charset=utf-8")
    public Response withdrawal(@PathParam("donor") String donor, @PathParam("amount") String amount) {
        String result = UNKNOWN_ERROR;
        try {
            TransactionService transactionService = getTransactionService();
            result = transactionService.withdrawMoney(donor, amount);
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
            UserDAO userDao = UserDAO.getInstance();
            String users = Arrays.toString(userDao.getAllUsers().toArray());
            LOGGER.error(users);
            return users;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return "Error";
    }

}