package com.clsaa.dop.server.code.util;

import com.alibaba.fastjson.JSON;
import com.clsaa.dop.server.code.model.bo.ProjectBo;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 发送请求到gitlab的工具类
 *
 * @author wsy
 */
public class RequestUtil {

    //api地址
    private static final String api = "http://gitlab.dop.clsaa.com/api/v4";
    //管理员的token
    private static final String privateToken = "y5MJTK9yisBKfNF1t-gd";

    public static void main(String[] args){

        ProjectBo projectBo=RequestUtil.get("/projects/1",ProjectBo.class);

        System.out.println(projectBo);



    }

    /**
     * get请求获得一个List
     */
    public static <T> List<T> getList(String path,Class<T> clazz){

        return JSON.parseArray(httpGet(path),clazz);

    }

    public static <T> T get(String path,Class<T> clazz){

        return JSON.parseObject(httpGet(path),clazz);

    }



    /**
     * 发送get请求到gitlab
     * @param path 路径
     * @return json字符串
     */
    private static String httpGet(String path) {

        String url = api + path;
        url += url.indexOf('?') == -1 ? "?" : "&";
        url += "private_token=" + privateToken;

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


    public static String httpPost(String url, List<NameValuePair> formparams) throws Exception {
        CloseableHttpClient httpclients = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);

        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpclients.execute(httpPost);
        HttpEntity entity1 = response.getEntity();

        try {
            HttpEntity entity5 = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
            }
        } finally {
            response.close();
            httpclients.close();
        }

        return EntityUtils.toString(entity1);

    }


}
