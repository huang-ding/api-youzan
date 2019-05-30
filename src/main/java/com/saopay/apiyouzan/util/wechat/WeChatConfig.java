package com.saopay.apiyouzan.util.wechat;

/**
 * @author huangding
 * @description
 * @date 2018/11/23 21:10
 */
public class WeChatConfig {

    /**
     * 临时二维码
     */
    public static final String QR_SCENE = "QR_SCENE"; //-1
    /**
     * 永久二维码
     */
    public static final String QR_LIMIT_SCENE = "QR_LIMIT_SCENE";//0
    /**
     * 永久二维码(字符串)
     */
    public static final String QR_LIMIT_STR_SCENE = "QR_LIMIT_STR_SCENE";//1
    /**
     * 创建二维码
     */
    public static final String CREATE_TICKET_PATH = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
    /**
     * 通过ticket换取二维码
     */
    public static final String SHOW_QRCODE_PATH = "https://mp.weixin.qq.com/cgi-bin/showqrcode";
    /**
     * 长链接转成短链接
     */
    public static final String WECHAT_SHORT_QRCODE_URL = "https://api.weixin.qq.com/cgi-bin/shorturl";

    public static final String LONG2SHORT = "long2short";
}
