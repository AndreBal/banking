package com.balash.banking;

import com.balash.banking.server.BankingServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankingServer.class);

    public static void main(String[] args) {
        URI baseUri = UriBuilder.fromUri("http://0.0.0.0/").port(8080).build();
        ResourceConfig config = new ResourceConfig(BankingServer.class);
        GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
        LOGGER.debug("Server started successfully at " + baseUri);
    }

}
