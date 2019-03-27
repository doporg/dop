package com.clsaa.dop.server.code.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author wsy
 */
public class test {

    /**
     * 发送http get请求
     *
     * @param url 路径
     * @return json字符串
     */
    private static String httpGet(String url) {


        CloseableHttpClient httpclients = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;

        try {
            response = httpclients.execute(httpGet);

        } catch (IOException e) {
            e.printStackTrace();
        }

        String result = null;
        try {
            //4.处理结果，这里将结果返回为字符串
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;


    }

    public static void main(String[] args) {

        String api = "http://gitlab.dop.clsaa.com/api/v4";

        String tree = api + "/projects/30/repository/tree?recursive=true";

        String blob= api+"/projects/30/repository/blobs/5648544d13c6e45add67f95d767724f887900875";

        String file=api+"/projects/30/repository/files/README.md/raw?ref=master";
        String archive=api+"/projects/30/repository/archive.zip";


         String commit =api+"/projects/3/repository/commits?path=kk";

        String jsonStr = httpGet(commit);
        FormatUtil.printJson(jsonStr);

    }
}
