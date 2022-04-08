package cn.com.devopsplus.dop.server.pipeline.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageRepositoryConfig {
    public static String repositoryHostPort;

    @Value("${harbor.hostAndPort}")
    private void setRepositoryHostPort(String repositoryHostPort) {
        ImageRepositoryConfig.repositoryHostPort = repositoryHostPort;
    }
}
