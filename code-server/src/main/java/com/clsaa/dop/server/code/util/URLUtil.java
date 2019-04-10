package com.clsaa.dop.server.code.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author wsy
 */
public class URLUtil {


    public static String encode(String s) {

        try {
            s = URLEncoder.encode(s, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return s;

    }

    public static void main(String[] args) {
        System.out.println(encode("kk/jj"));
    }
}
