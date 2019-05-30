package com.saopay.apiyouzan.enums;

/**
 * @author huangding
 * @description
 * @date 2018/11/23 16:06
 */
public enum ActivityType {
    /**
     * 活動類型
     */
    ZERO(1, "零元购"),
    ;
    private Integer code;
    private String val;

    ActivityType(Integer code, String val) {
        this.code = code;
        this.val = val;
    }

    public Integer getCode() {
        return code;
    }

    public String getVal() {
        return val;
    }
}
