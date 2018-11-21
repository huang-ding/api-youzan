package com.saopay.apiyouzan.util.http;

/**
 * @author huangding
 * @description 错误码
 * @date 2018/9/28 15:24
 */
public enum JsonResultErrorEnum {

    /**
     * 成功
     */
    SUCCESS("success", "0"),

    /**
     * mysql
     */
    MYSQL_DATA_GRAMMAR("mysql语法错误", "6000"),

    /**
     * 有赞
     */
    YZ_TAG_NULL("无该标签", "70001"),
    YZ_USER_NULL("无该用户", "7002"),
    YZ_VIRTUAL_CODE_ERROR("核销出错", "5000"),
    YZ_VIRTUAL_CODE_NULL("该码无法使用此功能核销", "5001"),

    ;

    private String msg;
    private String code;

    JsonResultErrorEnum(String msg, String code) {
        this.msg = msg;
        this.code = code;
    }

    public static JsonResultErrorEnum N(String code) {
        for (JsonResultErrorEnum jsonResultErrorEnum : values()) {
            if (jsonResultErrorEnum.getCode().equals(code)) {
                return jsonResultErrorEnum;
            }
        }
        return null;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
