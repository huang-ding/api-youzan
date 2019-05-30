package com.saopay.apiyouzan.util.wechat;

import com.alibaba.fastjson.JSON;
import com.saopay.apiyouzan.exception.QRCodeException;
import com.saopay.apiyouzan.util.http.Coder;
import com.saopay.apiyouzan.util.http.OkHttpUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import lombok.extern.slf4j.Slf4j;

/**
 * @author huangding
 * @description 微信二维码工具
 * @date 2018/11/23 21:07
 */
@Slf4j
public class WeChatQRCodeUtil {

    /**
     * 创建临时带参数二维码
     *
     * @param sceneId 场景Id
     * @expireSeconds 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
     */
    public static WeChatQRCode getWeChatQRCode(String accessToken, String expireSeconds,
        int sceneId) {
        TreeMap<String, String> params = new TreeMap<>();
        params.put("access_token", accessToken);
        Map<String, Integer> intMap = new HashMap<>();
        intMap.put("scene_id", sceneId);
        Map<String, Map<String, Integer>> mapMap = new HashMap<>();
        mapMap.put("scene", intMap);
        Map<String, Object> paramsMap = new HashMap<>();
        if (null == expireSeconds) {
            expireSeconds = "2592000";
        }
        paramsMap.put("expire_seconds", expireSeconds);
        paramsMap.put("action_name", WeChatConfig.QR_SCENE);
        paramsMap.put("action_info", mapMap);

        String data = JSON.toJSONString(paramsMap);
        StringBuffer url = new StringBuffer(WeChatConfig.CREATE_TICKET_PATH);
        OkHttpUtil.getQuer(params, url);
        String json = OkHttpUtil.postJsonParams(url.toString(), data);
        log.info("json:{}", json);
        try {
            WeChatQRCode wechatQRCode = JSON.parseObject(json, WeChatQRCode.class);
            return wechatQRCode;
        } catch (Exception e) {
            throw new QRCodeException("微信二维码获取失败:" + json, e);
        }
    }

    /**
     * 获取二维码ticket后，通过ticket换取二维码图片展示
     */
    public static String showQrcode(String ticket) {
        Map<String, String> params = new TreeMap<String, String>();
        params.put("ticket", Coder.encoder(ticket));
        StringBuffer url = new StringBuffer(WeChatConfig.SHOW_QRCODE_PATH);
        OkHttpUtil.getQuer(params, url);
        return url.toString();
    }

    public static String getUrl(String accessToken, String activityCode) {
        WeChatQRCode weChatQRCode = WeChatQRCodeUtil
            .getWeChatQRCode(accessToken, null,
                Integer.parseInt(activityCode));
        return WeChatQRCodeUtil.showQrcode(weChatQRCode.getTicket());
    }

}
