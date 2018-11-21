package com.saopay.apiyouzan.enums;

/**
 * @author huangding
 * @description
 * @date 2018/11/15 11:02
 */
public enum RedisKeyEnum {

    /**
     * 有赞 accessToken
     */
    YZ_ACCESS_TOKEN("yz-access-token"),
    /**
     * 有赞 refreshToken
     */
    YZ_REFRESH_TOKEN("yz-refresh-token"),

    /**
     * 微信公众号 accessToken
     */
    WXP_ACCESS_TOKEN("wx-access-token"),
    /**
     * 微信公众号 jsapi_ticket
     */
    WXP_JSAPI_TICKET("wx-jsapi-ticket"),

    /**
     * 有赞核销员
     */
    YZ_VERIFICATION_TAG("yz-verification-tag"),
    ;

    private String code;

    RedisKeyEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
