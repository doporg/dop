package com.clsaa.dop.server.pipeline.config;

import com.clsaa.dop.server.pipeline.util.JenkinsUtils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;


@Configuration
public class RestTemplateConfig {
    private JenkinsUtils jenkinsUtils = new JenkinsUtils();


    public class AgentInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            HttpHeaders headers = request.getHeaders();
            String decode = jenkinsUtils.getUsername() + ":" + jenkinsUtils.getPassword();
            byte[] decodeByte = decode.getBytes(StandardCharsets.UTF_8);
            String encoded = Base64.encodeBase64String(decodeByte);
            headers.add(HttpHeaders.AUTHORIZATION, "Basic " + encoded);
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.add(HttpHeaders.USER_AGENT, "");
            return execution.execute(request, body);
        }
    }

    @Bean
    public RestTemplate restTemplate(){
        HttpComponentsClientHttpRequestFactory httpRequestFactory  = new HttpComponentsClientHttpRequestFactory ();
        httpRequestFactory .setConnectTimeout(1000);
        httpRequestFactory .setReadTimeout(1000);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        restTemplate.setInterceptors(Collections.singletonList(new AgentInterceptor()));
        return restTemplate;
    }
}
