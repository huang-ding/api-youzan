package com.saopay.apiyouzan.util.wechat;

import com.alibaba.fastjson.JSONObject;
import com.saopay.apiyouzan.enums.RedisKeyEnum;
import com.saopay.apiyouzan.exception.TokenException;
import com.saopay.apiyouzan.redis.RedisUtil;
import com.saopay.apiyouzan.util.http.OkHttpUtil;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author huangding
 * @description
 * @date 2018/11/19 10:06
 */
@Component
@Slf4j
public class WeChatBaseUtil {

    @Autowired
    private RedisUtil redisUtil;

    private static String tokenUrl;

    private static String userTokenUrl;

    private static String jsapiTicketUrl;


    private String getToken() {

        String json = OkHttpUtil.get(tokenUrl, null);
        JSONObject jsonObject = JSONObject.parseObject(json);
        String access_token = jsonObject.getString("access_token");
        if (StringUtils.isBlank(access_token)) {
            throw new TokenException("获取微信token失败:" + json);
        }
        return access_token;
    }

    public String getOpenId(String code) {
        String url = StringUtils.replace(userTokenUrl, "$CODE", code);
        String json = OkHttpUtil.get(url, null);
        JSONObject jsonObject = JSONObject.parseObject(json);
        String openid = jsonObject.getString("openid");
        if (StringUtils.isBlank(openid)) {
            throw new TokenException("获取微信openId失败:" + json);
        }
        return openid;
    }


    /**
     * 获取微信AccessToken 有效期2小时
     */
    public String getAccessToken() {
        return redisUtil
            .get(RedisKeyEnum.WXP_ACCESS_TOKEN, () -> getToken(), 7200 - 200, String.class);
    }

    /**
     * 获取微信jsapi_ticket有效期2小时
     */
    public String getJsapiTicket() {
        return redisUtil
            .get(RedisKeyEnum.WXP_JSAPI_TICKET, () -> getTicket(), 7200 - 200, String.class);
    }

    private String getTicket() {
        String url = StringUtils.replace(jsapiTicketUrl, "$ACCESS_TOKEN", getAccessToken());
        String json = OkHttpUtil.get(url, null);
        JSONObject jsonObject = JSONObject.parseObject(json);
        String ticket = jsonObject.getString("ticket");
        if (StringUtils.isBlank(ticket)) {
            throw new TokenException("获取微信ticket失败:" + json);
        }
        return ticket;
    }

    /**
     * JS-SDK使用权限签名
     */
    public String getSign(Map<String, String> map) {
        String text = createLinkString(map);
        log.info("加密字符串：{}", text);
        return getSha1(text);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String a="e1d481c6c6c6242157bfe2e0c55310cfcb48e27f";
        String b="e1d481c6c6c6242157bfe2e0c55310cfcb48e27f";
        System.out.println(a.equals(b));
    }

    public static String getSha1(String str){
        if(str == null || str.length()==0){
            return null;
        }
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j*2];
            int k = 0;
            for(int i=0;i<j;i++){
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

    public static String createLinkString(Map<String, String> params) {
        if (params != null && params.size() != 0) {
            List<String> keys = new ArrayList(params.keySet());
            Collections.sort(keys);
            StringBuilder link = new StringBuilder();

            for (int i = 0; i < keys.size(); ++i) {
                String key = (String) keys.get(i);
                String value = StringUtils.isEmpty((CharSequence) params.get(key)) ? ""
                    : (String) params.get(key);
                link.append("&").append(key).append("=").append(value);
            }

            return link.substring(1);
        } else {
            return "";
        }
    }

    @Value("${wechat.public.token_url}")
    public void setTokenUrl(String tokenUrl) {
        WeChatBaseUtil.tokenUrl = tokenUrl;
    }

    @Value("${wechat.public.user_token_url}")
    public void setUserTokenUrl(String userTokenUrl) {
        WeChatBaseUtil.userTokenUrl = userTokenUrl;
    }

    @Value("${wechat.public.jsapi_ticket_url}")
    public void setJsapiTicketUrl(String jsapiTicketUrl) {
        WeChatBaseUtil.jsapiTicketUrl = jsapiTicketUrl;
    }
}
