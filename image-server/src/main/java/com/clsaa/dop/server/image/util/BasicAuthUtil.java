package com.clsaa.dop.server.image.util;

import java.util.Base64;

/**
 * <p>
 *    Basic Auth工具类
 * </p>
 * @author xzt
 * @since 2019-3-28
 */
public class BasicAuthUtil {
    /**
     * 通过username和password产生basic auth
     * @param username 用户名
     * @param password 用户密码
     * @return {@link String} basic auth
     */
    public static String createAuth(String username,String password){
        String userNamePassword = username +":"+password;
        String auth = "Basic "+Base64.getEncoder().encodeToString(userNamePassword.getBytes());
        return auth;
    }
}
