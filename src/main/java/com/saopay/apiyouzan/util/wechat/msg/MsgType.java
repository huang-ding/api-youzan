package com.saopay.apiyouzan.util.wechat.msg;

/**
 * @author huangding
 * @description
 * @date 2018/11/23 10:07
 */
public enum MsgType {
    /**
     * 微信消息类型
     */
    TEXT("text"),
    IMAGE("image"),
    VOICE("voice"),
    VIDEO("video"),
    MUSIC("music"),
    NEWS("news"),
    /**
     * 事件
     */
    EVENT("event"),
    //关注
    EVENT_SUBSCRIBE("SUBSCRIBE"),
    //取消关注
    EVENT_UNSUBSCRIBE("unsubscribe"),
    //扫描带参数二维码
    EVENT_SCAN("SCAN"),
    //上报地理位置
    EVENT_LOCATION("LOCATION"),
    //自定义菜单
    EVENT_CLICK("CLICK"),

    ;


    private String type;


    public static MsgType getMagType(String type) {
        for (MsgType msgType : values()) {
            if (msgType.getType().equals(type)) {
                return msgType;
            }
        }
        return null;
    }


    MsgType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
