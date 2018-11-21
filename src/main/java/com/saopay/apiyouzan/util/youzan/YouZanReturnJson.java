package com.saopay.apiyouzan.util.youzan;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * @author huangding
 * @description
 * @date 2018/11/13 16:58
 */
@Data
public class YouZanReturnJson {

    private int code;
    private String msg;

    public YouZanReturnJson(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String toJsonString() {
        return JSON.toJSONString(new YouZanReturnJson(0, "success"));
    }

    public static void main(String[] args) {
        System.out.println(toJsonString());
    }
}
