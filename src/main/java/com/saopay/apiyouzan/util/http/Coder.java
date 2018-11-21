package com.saopay.apiyouzan.util.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.apache.commons.lang3.StringUtils;

/**
 * @author huangding
 * @description
 * @date 2018/11/12 16:53
 */
public class Coder {

    public static String encoder(String str, String enc) {
        try {
            String encode = URLEncoder.encode(str, enc);
            return encode;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }

    public static String encoder(String str) {
        return encoder(str, "UTF-8");
    }

    public static String decode(String str, String enc) {
        try {
            String encode = URLDecoder.decode(str, enc);
            return encode;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }

    public static String decode(String str) {
        return decode(str, "UTF-8");
    }

    public static void main(String[] args) {
        System.out.println(encoder("韩国进口"));
    }


}
