package com.saopay.apiyouzan.util.wechat.msg.bo;

import lombok.Data;

/**
 * @author huangding
 * @description 回复图文消息
 * <xml><ToUserName>< ![CDATA[toUser] ]></ToUserName><FromUserName>< ![CDATA[fromUser]
 * ]></FromUserName><CreateTime>12345678</CreateTime><MsgType>< ![CDATA[news]
 * ]></MsgType><ArticleCount>1</ArticleCount><Articles><item><Title>< ![CDATA[title1] ]></Title>
 * <Description>< ![CDATA[description1] ]></Description><PicUrl>< ![CDATA[picurl] ]></PicUrl><Url><
 * ![CDATA[url] ]></Url></item></Articles></xml>
 * @date 2018/11/22 16:14
 */
@Data
public class WeChatNewsRespMsg extends WeChatBaseRespMsg {

    /**
     * 图文消息个数；当用户发送文本、图片、视频、图文、地理位置这五种消息时，开发者只能回复1条图文消息；其余场景最多可回复8条图文消息
     */
    private String ArticleCount;

    /**
     * 图文消息信息，注意，如果图文数超过限制，则将只发限制内的条数
     */
    private String Articles;

    /**
     * 图文消息标题
     */
    private String Title;

    /**
     * 图文消息描述
     */
    private String Description;

    /**
     * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
     */
    private String PicUrl;

    /**
     * 点击图文消息跳转链接
     */
    private String Url;
}
