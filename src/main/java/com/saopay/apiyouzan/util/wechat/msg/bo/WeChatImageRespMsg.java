package com.saopay.apiyouzan.util.wechat.msg.bo;

import lombok.Data;

/**
 * @author huangding
 * @description 回复图片消息
 * @date 2018/11/22 16:09
 */
@Data
public class WeChatImageRespMsg extends WeChatBaseRespMsg {

    /**
     * 通过素材管理中的接口上传多媒体文件，得到的id。
     */
    private String MediaId;

    public WeChatImageRespMsg() {
    }

    public WeChatImageRespMsg(String fromUserName, String toUserName,
        int createTime, String mediaId) {
        this.FromUserName = fromUserName;
        this.ToUserName = toUserName;
        this.CreateTime = createTime;
        MediaId = mediaId;
    }


}
