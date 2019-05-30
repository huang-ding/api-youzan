package com.saopay.apiyouzan.util.wechat.msg.bo;

import lombok.Data;

/**
 * @author huangding
 * @description 回复视频消息
 * @date 2018/11/22 16:10
 */
@Data
public class WeChatVideoRespMsg extends WeChatImageRespMsg {


    /**
     * 视频消息的标题
     */
    private String Title;

    /**
     * 视频消息的描述
     */
    private String Description;
}
