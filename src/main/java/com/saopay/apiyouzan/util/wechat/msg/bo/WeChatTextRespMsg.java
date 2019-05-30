package com.saopay.apiyouzan.util.wechat.msg.bo;

import lombok.Data;

/**
 * @author huangding
 * @description 回复文本消息
 * <xml> <ToUserName>< ![CDATA[toUser] ]></ToUserName> <FromUserName>< ![CDATA[fromUser]
 * ]></FromUserName> <CreateTime>12345678</CreateTime> <MsgType>< ![CDATA[text] ]></MsgType>
 * <Content>< ![CDATA[你好] ]></Content> </xml>
 * @date 2018/11/22 16:08
 */
@Data
public class WeChatTextRespMsg extends WeChatBaseRespMsg {

    /**
     * 回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）
     */
    private String Content;

    public WeChatTextRespMsg() {
    }

    public WeChatTextRespMsg(String content, String fromUserName, String toUserName,
        int createTime) {
        this.FromUserName = fromUserName;
        this.ToUserName = toUserName;
        this.CreateTime = createTime;
        this.Content = content;
    }
}
