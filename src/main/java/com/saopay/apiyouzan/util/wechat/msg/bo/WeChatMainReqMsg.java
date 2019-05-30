package com.saopay.apiyouzan.util.wechat.msg.bo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * @author huangding
 * @description 微信推送过来的信息
 * @date 2018/11/23 9:54
 */
@Data
@XStreamAlias("xml")
public class WeChatMainReqMsg extends WeChatNewsRespMsg {

    private String Content;

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

    private String MediaId;

    private String MsgId;

}
