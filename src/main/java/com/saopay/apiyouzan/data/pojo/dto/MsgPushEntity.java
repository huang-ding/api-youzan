package com.saopay.apiyouzan.data.pojo.dto;

import lombok.Data;

/**
 * @author huangding
 * @description 有赞消息通知数据
 * @date 2018/11/29 10:11
 */
@Data
public class MsgPushEntity {

    private String msg;
    private int sendCount;
    //  默认0 : appid  1 :client
    private int mode;
    private String app_id;
    private String client_id;
    private Long version;
    private String type;
    private String id;
    private String sign;
    private Integer kdt_id;
    private boolean test = false;
    private String status;
    private String kdt_name;



}
