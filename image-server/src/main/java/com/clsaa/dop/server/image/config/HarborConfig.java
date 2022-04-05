package com.clsaa.dop.server.image.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HarborConfig {
    public static String adminUsername;
    public static String adminPassword;

    @Value("${harbor-auth.username}")
    private void setAdminUsername(String username) {
        HarborConfig.adminUsername = username;
    }

    @Value("${harbor-auth.password}")
    private void setAdminPassword(String password) {
        HarborConfig.adminPassword = password;
    }
}
