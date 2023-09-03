package com.balash.banking.config;

import lombok.Getter;
import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.Map;

public class AppConfig {

    private static final String CONFIG_FILE = "application.yml";
    private static final AppConfig INSTANCE = new AppConfig();

    @Getter
    private String appUrl;
    private static final String APP_URL_PARAM_NAME = "appUrl";

    @Getter
    private Integer interestCheckPeriod;
    private static final String INTERST_PERIOD_PARAM_NAME = "interestCheckPeriod";

    @Getter
    private String jdbcUrl;
    private static final String JDBC_URL_PARAM_NAME = "jdbcUrl";

    @Getter
    private String dbUsername;
    private static final String DB_USERNAME_PARAM_NAME = "dbUsername";

    @Getter
    private String dbPassword;
    private static final String DB_PASSWORD_PARAM_NAME = "dbPassword";

    public static AppConfig getInstance(){
        return INSTANCE;
    }
    private AppConfig() {
        loadConfig();
    }
    private void loadConfig() {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                Map<String, Object> yamlData = yaml.load(inputStream);
                appUrl = (String) yamlData.get(APP_URL_PARAM_NAME);
                interestCheckPeriod = (Integer) yamlData.get(INTERST_PERIOD_PARAM_NAME);
                dbUsername = (String) yamlData.get(DB_USERNAME_PARAM_NAME);
                dbPassword = yamlData.get(DB_PASSWORD_PARAM_NAME).toString();
                jdbcUrl = (String) yamlData.get(JDBC_URL_PARAM_NAME);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
