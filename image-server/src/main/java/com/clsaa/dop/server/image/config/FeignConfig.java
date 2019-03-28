package com.clsaa.dop.server.image.config;

import feign.Retryer;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Feign配置类
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018/8/26
 */
@Configuration
public class FeignConfig {
    @Value("${auth.user}")
    private String user;
    @Value("${auth.password}")
    private String password;
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(100, TimeUnit.SECONDS.toMillis(1), 5);
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        //TODO need to add the value dynamically
        return new BasicAuthRequestInterceptor(user, password);
    }
}