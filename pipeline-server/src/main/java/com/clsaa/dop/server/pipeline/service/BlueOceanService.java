package com.clsaa.dop.server.pipeline.service;


import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 调用blueocean接口业务实现类
 *
 * @author 张富利
 * @since 2019-03-09
 */
@Service
public class BlueOceanService {
    private RestTemplate restTemplate = new RestTemplate();

    // 获得authorization
    public String getAuthorization() {
        String uri = "http://jenkins.dop.clsaa.com:8088/jwt-auth/token";
        HttpHeaders headers = restTemplate.getForEntity(uri, String.class).getHeaders();
        return headers.get("X-BLUEOCEAN-JWT").get(0);
    }
}
