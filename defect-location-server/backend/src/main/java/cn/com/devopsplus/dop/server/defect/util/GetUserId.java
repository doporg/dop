package cn.com.devopsplus.dop.server.defect.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GetUserId {
    public Integer getUserId(){
        Integer userId=0;
        String url = "http://172.29.7.157:85/user/id";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        try {
            CloseableHttpResponse response = httpClient.execute(get);
            int statusCode =response.getStatusLine().getStatusCode();
            if(statusCode == 200) {
                HttpEntity entity =response.getEntity();
                System.out.println("success");
                userId = Integer.valueOf(EntityUtils.toString(entity,"utf-8"));
            }
            response.close();
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userId;
    }
}
