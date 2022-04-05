package com.clsaa.dop.server.code.util;

import com.alibaba.fastjson.JSON;
import com.clsaa.dop.server.code.controller.UserController;
import com.clsaa.dop.server.code.feign.UserCredentialType;
import com.clsaa.dop.server.code.feign.UserCredentialV1;
import com.clsaa.dop.server.code.feign.UserFeign;
import com.clsaa.dop.server.code.model.bo.project.ProjectBo;
import com.clsaa.dop.server.code.model.bo.user.UserIdBo;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
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


//    private static UserService userService;
//
//    @Autowired
//    public void setUserService(UserService userService) {
//        RequestUtil.userService = userService;
//    }

    private static final Logger logger = LoggerFactory.getLogger(RequestUtil.class);

    private static UserFeign userFeign;

    @Autowired
    public void setUserFeign(UserFeign userFeign){RequestUtil.userFeign=userFeign;}

    //api地址
    private static final String api = "http://172.29.7.157:8090/api/v4";

    //    管理员的token
    private static final String rootPrivateToken = "bgFns8gqgd_ZSEqe3JvT";
    private static final String rootAccessToken = "bgFns8gqgd_ZSEqe3JvT";


    /**
     * 发送get请求到gitlab，返回一个对象列表
     *
     * @param path     请求路径
     * @param userId   用户id
     * @param clazz    类
     * @param <T>      结果转化的类型
     * @return 列表
     */
    public static <T> List<T> getList(String path, Long userId, Class<T> clazz) {
        logger.info("fetch access token by user id: " + "userID=" + userId);
        UserCredentialV1 credential = userFeign.getUserCredentialV1ByUserId(userId,UserCredentialType.DOP_INNER_GITLAB_TOKEN);
        logger.info("user credential fetched: " + credential);
        String access_token = credential.getCredential();

        String url = api + path;
        url += url.indexOf('?') == -1 ? "?" : "&";
        url += "access_token=" + access_token;

        List<T> list=new ArrayList<>();
        int page=1;
        while(true){
            String temp_url=url+"&per_page=50&page="+page++;
            logger.info("GitLab get list URL: " + temp_url);
            List<T> temp_list= JSON.parseArray(httpGet(temp_url), clazz);
            logger.info("response: " + temp_list);

            if(temp_list.size()==0){
                break;
            }else {
                list.addAll(temp_list);
            }
        }

        return list;
    }

    /**
     * 没有用户名参数，默认使用root的private_token
     */
    public static <T> List<T> getList(String path, Class<T> clazz) {

        String url = api + path;
        url += url.indexOf('?') == -1 ? "?" : "&";
        url += "private_token=" + rootPrivateToken;

        List<T> list=new ArrayList<>();
        int page=1;
        while(true){
            String temp_url=url+"&per_page=50&page="+page++;
            List<T> temp_list= JSON.parseArray(httpGet(temp_url), clazz);
            if(temp_list.size()==0){
                break;
            }else {
                list.addAll(temp_list);
            }
        }

        return JSON.parseArray(httpGet(url), clazz);

    }

    /**
     * 发送get请求到gitlab，返回一个对象，其他同getList方式
     */
    public static <T> T get(String path, Long userId, Class<T> clazz) {
        String access_token=userFeign.getUserCredentialV1ByUserId(userId,UserCredentialType.DOP_INNER_GITLAB_TOKEN).getCredential();
        String url = api + path;
        url += url.indexOf('?') == -1 ? "?" : "&";
        url += "access_token=" + access_token;

        return JSON.parseObject(httpGet(url), clazz);

    }

    /**
     * 发送get请求到gitlab，返回字符串类型
     */
    public static String getString(String path,Long userId){
        String access_token=userFeign.getUserCredentialV1ByUserId(userId,UserCredentialType.DOP_INNER_GITLAB_TOKEN).getCredential();
        String url = api + path;
        url += url.indexOf('?') == -1 ? "?" : "&";
        url += "access_token=" + access_token;

        return httpGet(url);
    }

    public static void main(String[] args) {
        String id= URLUtil.encodeURIComponent("Admin_0/hhhh");
        String url = api + "/projects/"+id;
        url+="?statistics=true";
        url += url.indexOf('?') == -1 ? "?" : "&";
        String access_token="92d328d1e0602a5cbefff89508ccc27a9a10bc85fcb01f3261d7d9fc50dc772e";
        url += "access_token=" + access_token;
        System.out.println(httpGet(url));
        System.out.println(JSON.parseObject(httpGet(url), ProjectBo.class));
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
     * @param userId   用户id
     * @param params   参数键值对
     * @return 返回状态码
     */
    public static int post(String path, Long userId, List<NameValuePair> params) {

        String access_token=userFeign.getUserCredentialV1ByUserId(userId,UserCredentialType.DOP_INNER_GITLAB_TOKEN).getCredential();
        String url = api + path;
        NameValuePair p = new BasicNameValuePair("access_token", access_token);
        params.add(p);
        return httpPost(url, params);
    }

    /**
     * 发送post请求到gitlab
     * root身份
     *
     * @param path     路径
     * @param params   参数键值对
     * @return 返回状态码
     */
    public static int post(String path, List<NameValuePair> params) {

        String url = api + path;
        NameValuePair p = new BasicNameValuePair("access_token", rootAccessToken);
        params.add(p);
        return httpPost(url, params);
    }


    /**
     * 发送put请求到gitlab
     *
     * @param path     路径
     * @param userId   用户id
     * @param params   参数键值对
     * @return 返回状态码
     */
    public static int put(String path,Long userId, List<NameValuePair> params){

        String access_token=userFeign.getUserCredentialV1ByUserId(userId,UserCredentialType.DOP_INNER_GITLAB_TOKEN).getCredential();
        String url = api + path;
        NameValuePair p = new BasicNameValuePair("access_token", access_token);
        params.add(p);
        return httpPut(url,params);

    }


    /**
     * 发送put请求到gitlab
     *
     * @param path     路径
     * @param params   参数键值对
     * @return 返回状态码
     */
    public static int put(String path,List<NameValuePair> params){

        String url = api + path;
        NameValuePair p = new BasicNameValuePair("access_token", rootAccessToken);
        params.add(p);
        return httpPut(url,params);

    }

    /**
     * 发送delete请求到gitlab
     */
    public static int delete(String path,Long userId){

        String access_token=userFeign.getUserCredentialV1ByUserId(userId,UserCredentialType.DOP_INNER_GITLAB_TOKEN).getCredential();
        String url = api + path;
        url += url.indexOf('?') == -1 ? "?" : "&";
        url += "access_token=" + access_token;

        return httpDelete(url);
    }

    /**
     * 发送delete请求到gitlab，管理员身份
     */
    public static int delete(String path){

        String url = api + path;
        url += url.indexOf('?') == -1 ? "?" : "&";
        url += "private_token=" + rootPrivateToken;

        return httpDelete(url);
    }





    /**
     * 发送http get请求
     *
     * @param url 路径
     * @return json字符串
     */
    private static String httpGet(String url) {

        //首先对url的request param进行url encode
        url=URLUtil.encodeURI(url);

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

        //首先对url的request param进行url encode
        url=URLUtil.encodeURI(url);

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
