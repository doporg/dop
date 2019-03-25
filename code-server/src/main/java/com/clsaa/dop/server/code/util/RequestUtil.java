package com.clsaa.dop.server.code.util;

import com.alibaba.fastjson.JSON;
import com.clsaa.dop.server.code.model.bo.ProjectBo;
import com.clsaa.dop.server.code.model.po.User;
import com.clsaa.dop.server.code.service.UserService;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 发送请求到gitlab的工具类
 *
 * @author wsy
 */

@Component
public class RequestUtil {


    private static UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        RequestUtil.userService = userService;
    }

    //api地址
    private static final String api = "http://gitlab.dop.clsaa.com/api/v4";

    //    管理员的token
    private static final String rootPrivateToken = "y5MJTK9yisBKfNF1t-gd";

    public static void main(String[] args) throws UnsupportedEncodingException {

//        String url=api+"/projects/3/repository/files/kk%2f1.txt/raw?ref=master";
//        System.out.println(httpGet(url));

//        String path=api+"/projects/3/repository/files/"+URLEncoder.encode("kk/1.txt","GBK");
//        NameValuePair p1=new  BasicNameValuePair("branch","master");
//        NameValuePair p2=new  BasicNameValuePair("content","123\n456\n哈哈哈\n");
//        NameValuePair p3=new  BasicNameValuePair("commit_message","update 1.txt!!!!!!");
//        NameValuePair p4=new BasicNameValuePair("access_token","1756641a28e5fa6133647c8833a2559df420ee053ac8762c40b823f814761e02");
//
//        List<NameValuePair> list=new ArrayList<>();
//        list.add(p1);
//        list.add(p2);
//        list.add(p3);
//        list.add(p4);

        String url=api+"/projects/3/repository/files/"+URLEncoder.encode("kk/test.txt","GBK")+"?branch=master&commit_message=删除test.txt"+
                "&access_token=1756641a28e5fa6133647c8833a2559df420ee053ac8762c40b823f814761e02";

        System.out.println(httpDelete(url));

    }

    /**
     * 发送get请求到gitlab，返回一个对象列表
     *
     * @param path     请求路径
     * @param username 用户名
     * @param clazz    类
     * @param <T>      结果转化的类型
     * @return 列表
     */
    public static <T> List<T> getList(String path, String username, Class<T> clazz) {

        String access_token = userService.findUserAccessToken(username);

        String url = api + path;
        url += url.indexOf('?') == -1 ? "?" : "&";
        url += "access_token=" + access_token;

        return JSON.parseArray(httpGet(url), clazz);

    }

    /**
     * 没有用户名参数，默认使用root的private_token
     */
    public static <T> List<T> getList(String path, Class<T> clazz) {

        String url = api + path;
        url += url.indexOf('?') == -1 ? "?" : "&";
        url += "private_token=" + rootPrivateToken;

        return JSON.parseArray(httpGet(url), clazz);

    }

    /**
     * 发送get请求到gitlab，返回一个对象，其他同getList方式
     */
    public static <T> T get(String path, String username, Class<T> clazz) {

        String access_token = userService.findUserAccessToken(username);

        String url = api + path;
        url += url.indexOf('?') == -1 ? "?" : "&";
        url += "access_token=" + access_token;

        return JSON.parseObject(httpGet(url), clazz);

    }

    /**
     * 发送get请求到gitlab，返回字符串类型
     */
    public static String getString(String path,String username){

        String access_token = userService.findUserAccessToken(username);

        String url = api + path;
        url += url.indexOf('?') == -1 ? "?" : "&";
        url += "access_token=" + access_token;

        return httpGet(url);
    }

    /**
     * 没有用户名参数，默认使用root的private_token
     */
    public static <T> T get(String path, Class<T> clazz) {

        String url = api + path;
        url += url.indexOf('?') == -1 ? "?" : "&";
        url += "private_token=" + rootPrivateToken;

        return JSON.parseObject(httpGet(url), clazz);

    }


    /**
     * 发送post请求到gitlab
     *
     * @param path     路径
     * @param username 用户名
     * @param params   参数键值对
     * @return 返回状态码
     */
    public static int post(String path, String username, List<NameValuePair> params) {
        String url = api + path;
        NameValuePair p = new BasicNameValuePair("access_token", userService.findUserAccessToken(username));
        params.add(p);
        return httpPost(url, params);
    }


    /**
     * 发送put请求到gitlab
     *
     * @param path     路径
     * @param username 用户名
     * @param params   参数键值对
     * @return 返回状态码
     */
    public static int put(String path,String username, List<NameValuePair> params){
        String url = api + path;
        NameValuePair p = new BasicNameValuePair("access_token", userService.findUserAccessToken(username));
        params.add(p);
        return httpPut(url,params);

    }

    /**
     * 发送delete请求到gitlab
     */
    public static int delete(String path,String username){

        String access_token = userService.findUserAccessToken(username);

        String url = api + path;
        url += url.indexOf('?') == -1 ? "?" : "&";
        url += "access_token=" + access_token;

        return httpDelete(url);
    }





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
                result = EntityUtils.toString(entity,"utf-8");
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


    /**
     * 发送http post请求
     *
     * @param url        路径
     * @param formparams 参数键值对
     * @return 状态码
     */
    private static int httpPost(String url, List<NameValuePair> formparams) {


        CloseableHttpClient httpclients = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);

        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclients.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int result = response.getStatusLine().getStatusCode();

        try {
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * 发送http put请求
     *
     * @param url 路径
     * @param formparams 参数键值对
     * @return 状态码
     */
    private static int httpPut(String url, List<NameValuePair> formparams) {


        CloseableHttpClient httpclients = HttpClients.createDefault();

        HttpPut httpPut = new HttpPut(url);

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);

        httpPut.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclients.execute(httpPut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int result = response.getStatusLine().getStatusCode();

        try {
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * 发送http delete请求
     *
     * @param url 路径
     * @return 状态码
     */
    private static int httpDelete(String url) {


        CloseableHttpClient httpclients = HttpClients.createDefault();

        HttpDelete httpDelete = new HttpDelete(url);

        CloseableHttpResponse response = null;
        try {
            response = httpclients.execute(httpDelete);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int result = response.getStatusLine().getStatusCode();

        try {
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }







    /**
     * 获得用户access_token不是rest api的形式，创建一个新的post方法
     * @param url 地址
     * @param formparams 参数
     * @return json字符串
     */
    public static String httpPost1(String url, List<NameValuePair> formparams) {


        CloseableHttpClient httpclients = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);

        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclients.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result = null;

        try {
            HttpEntity entity1 = response.getEntity();
            if (entity1 != null) {
                result = EntityUtils.toString(entity1);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;

    }


}
