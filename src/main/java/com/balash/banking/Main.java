package com.balash.banking;

import com.balash.banking.config.AppConfig;
import com.balash.banking.server.BankingServer;
import com.balash.banking.service.interest.InterestCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        AppConfig appConfig = AppConfig.getInstance();
        String appUrl = appConfig.getAppUrl();
        URI baseUri = UriBuilder.fromUri(appUrl).build();
        ResourceConfig config = new ResourceConfig(BankingServer.class);

        GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
        LOGGER.debug("Server started successfully at " + baseUri);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        try {
            InterestCounter interestCounter = new InterestCounter();
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    interestCounter.count();
                } catch (SQLException e) {
                    LOGGER.error("Interest Counter encountered error during scheduled runtime with message "+e.getMessage(),e);
                }
            }, 0, appConfig.getInterestCheckPeriod(), TimeUnit.SECONDS);
            LOGGER.debug("Interest Counter scheduled successfully");
        }catch (SQLException e){
            LOGGER.error("Interest Counter error during startup with message "+e.getMessage(),e);
        }

    }

}
