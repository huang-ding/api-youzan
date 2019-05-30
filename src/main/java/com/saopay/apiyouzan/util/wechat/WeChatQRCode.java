package com.saopay.apiyouzan.util.wechat;

import lombok.Data;

/**
 * @author huangding
 * @description
 * @date 2018/11/23 21:08
 */
@Data
public class WeChatQRCode {

    /**
     * 获取的二维码
     */
    private String ticket;
    /**
     * 二维码的有效时间,单位为秒,最大不超过2592000（即30天）
     */
    private int expire_seconds;
    /**
     * 二维码图片解析后的地址
     */
    private String url;

}
