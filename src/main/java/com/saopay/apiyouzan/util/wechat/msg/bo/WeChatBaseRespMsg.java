package com.saopay.apiyouzan.util.wechat.msg.bo;

import lombok.Data;

/**
 * @author huangding
 * @description
 * @date 2018/11/22 16:00
 */
@Data
public class WeChatBaseRespMsg {


    /**
     * 开发者微信号
     */
    protected String ToUserName;
    /**
     * 发送方帐号（一个OpenID）
     */
    protected String FromUserName;
    /**
     * 消息创建时间 （整型）
     */
    protected int CreateTime;
    /**
     * 消息类型（text/image/location/link）
     */
    protected String MsgType;

}
