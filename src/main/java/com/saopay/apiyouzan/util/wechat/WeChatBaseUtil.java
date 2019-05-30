package com.saopay.apiyouzan.util.wechat;

import com.alibaba.fastjson.JSONObject;
import com.saopay.apiyouzan.data.pojo.dto.WeChatBaseInfo;
import com.saopay.apiyouzan.enums.RedisKeyEnum;
import com.saopay.apiyouzan.exception.TokenException;
import com.saopay.apiyouzan.exception.WeChatException;
import com.saopay.apiyouzan.redis.RedisUtil;
import com.saopay.apiyouzan.util.http.OkHttpUtil;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

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

    private static String appId;

    private static String tokenUrl;

    private static String userTokenUrl;

    private static String jsapiTicketUrl;

    private static String token;

    private static String encodingAESKey;


    public String getToken() {

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

    public String getTicket() {
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

    public WeChatBaseInfo getWeChatBaseInfo(String openId) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        url = url.replace("ACCESS_TOKEN", getAccessToken()).replace("OPENID", openId);
        String json = OkHttpUtil.get(url, null);
        JSONObject jsonObject = JSONObject.parseObject(json);
        //{"errcode":48001,"errmsg":"api unauthorized hint: [y6y40235vr44!]"}
        String errcode = jsonObject.getString("errcode");
        if (errcode.equals("48001")) {
            throw new WeChatException("微信公众号未认证:" + json);
        }

        log.info(json);
        WeChatBaseInfo weChatBaseInfo = JSONObject.parseObject(json, WeChatBaseInfo.class);
        return weChatBaseInfo;

    }


    public static void main(String[] args) throws UnsupportedEncodingException {
        String a = "e1d481c6c6c6242157bfe2e0c55310cfcb48e27f";
        String b = "e1d481c6c6c6242157bfe2e0c55310cfcb48e27f";
        System.out.println(a.equals(b));
    }

    public static String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
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


    public static WXBizMsgCrypt getWXBizMsgCrypt() throws AesException {
        return new WXBizMsgCrypt(token, encodingAESKey, appId);
    }


    public static String getEncodingAESKeyData() throws Exception {
//
        // 第三方回复公众平台
        //

        // 需要加密的明文
        String timestamp = "1409304348";
        String nonce = "xxxxxx";
        String replyMsg = " 中文<xml><ToUserName><![CDATA[oia2TjjewbmiOUlr6X-1crbLOvLw]]></ToUserName><FromUserName><![CDATA[gh_7f083739789a]]></FromUserName><CreateTime>1407743423</CreateTime><MsgType><![CDATA[video]]></MsgType><Video><MediaId><![CDATA[eYJ1MbwPRJtOvIEabaxHs7TX2D-HV71s79GUxqdUkjm6Gs2Ed1KF3ulAOA9H1xG0]]></MediaId><Title><![CDATA[testCallBackReplyVideo]]></Title><Description><![CDATA[testCallBackReplyVideo]]></Description></Video></xml>";

        WXBizMsgCrypt pc = getWXBizMsgCrypt();
        String mingwen = pc.encryptMsg(replyMsg, timestamp, nonce);
        System.out.println("加密后: " + mingwen);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        dbf.setXIncludeAware(false);
        dbf.setExpandEntityReferences(false);

        DocumentBuilder db = dbf.newDocumentBuilder();
        StringReader sr = new StringReader(mingwen);
        InputSource is = new InputSource(sr);
        Document document = db.parse(is);

        Element root = document.getDocumentElement();
        NodeList nodelist1 = root.getElementsByTagName("Encrypt");
        NodeList nodelist2 = root.getElementsByTagName("MsgSignature");

        String encrypt = nodelist1.item(0).getTextContent();
        String msgSignature = nodelist2.item(0).getTextContent();

        String format = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
        String fromXML = String.format(format, encrypt);

        //
        // 公众平台发送消息给第三方，第三方处理
        //

        // 第三方收到公众号平台发送的消息
        String result2 = pc.decryptMsg(msgSignature, timestamp, nonce, fromXML);
        System.out.println("解密后明文: " + result2);
        return null;
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

    @Value("${wechat.public.token}")
    public void setToken(String token) {
        WeChatBaseUtil.token = token;
    }

    @Value("${wechat.public.encoding_AES_key}")
    public void setEncodingAESKey(String encodingAESKey) {
        WeChatBaseUtil.encodingAESKey = encodingAESKey;
    }

    @Value("${wechat.public.app_id}")
    public void setAppId(String appId) {
        WeChatBaseUtil.appId = appId;
    }
}
