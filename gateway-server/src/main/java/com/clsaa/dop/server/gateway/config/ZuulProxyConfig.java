package com.clsaa.dop.server.gateway.config;

import com.clsaa.dop.server.gateway.zuul.filter.pre.AccessTokenZuulFilter;
import org.springframework.cloud.netflix.zuul.ZuulProxyAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Zuul网关扩展配置
 *
 * @author 任贵杰
 */
@Configuration
public class ZuulProxyConfig extends ZuulProxyAutoConfiguration {
    /**
     * Access-token 头部校验 优先级必须最高（高于所有其他preFilter），
     */
    @Bean
    public AccessTokenZuulFilter accessTokenZuulFilter() {
        return new AccessTokenZuulFilter();
    }

}
