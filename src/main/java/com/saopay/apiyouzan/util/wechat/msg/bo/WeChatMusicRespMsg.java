package com.saopay.apiyouzan.util.wechat.msg.bo;

import lombok.Data;

/**
 * @author huangding
 * @description 回复音乐消息
 * <xml><ToUserName>< ![CDATA[toUser] ]></ToUserName><FromUserName>< ![CDATA[fromUser]
 * ]></FromUserName><CreateTime>12345678</CreateTime><MsgType>< ![CDATA[music]
 * ]></MsgType><Music><Title>< ![CDATA[TITLE] ]></Title><Description>< ![CDATA[DESCRIPTION]
 * ]></Description><MusicUrl>< ![CDATA[MUSIC_Url] ]></MusicUrl><HQMusicUrl>< ![CDATA[HQ_MUSIC_Url]
 * ]></HQMusicUrl><ThumbMediaId>< ![CDATA[media_id] ]></ThumbMediaId></Music></xml>
 * @date 2018/11/22 16:10
 */
@Data
public class WeChatMusicRespMsg extends WeChatVideoRespMsg {

    /**
     * 音乐链接
     */
    private String MusicURL;
    /**
     * 高质量音乐链接，WIFI环境优先使用该链接播放音乐
     */
    private String HQMusicUrl;
    /**
     * 缩略图的媒体id，通过素材管理中的接口上传多媒体文件，得到的id
     */
    private String ThumbMediaId;

}
