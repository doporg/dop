package com.clsaa.dop.server.pipeline.util;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

public class JenkinsUtils {
    private static String uri = "http://jenkins.dop.clsaa.com";
    private static String username = "dop";
    private static String password = "Dop123..";

    public JenkinsUtils() {
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
