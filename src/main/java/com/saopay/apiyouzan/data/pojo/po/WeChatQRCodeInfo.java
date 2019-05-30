package com.saopay.apiyouzan.data.pojo.po;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

/**
 * @author huangding
 * @description 微信二维码信息
 * @date 2018/11/27 9:53
 */
@Entity(name = "we_chat_qr_code_info")
@Data
public class WeChatQRCodeInfo {

    /**
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     *
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 活动二维码
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 微信二维码场景Id
     */
    @Column(name = "event_key")
    private Integer eventKey;

    /**
     * 二维码的有效时间，单位为秒，空代表永久
     */
    @Column(name = "expire_seconds")
    private Integer expireSeconds;

    /**
     * 二维码地址
     */
    @Column(name = "url")
    private String url;

    /**
     *
     */
    @Column(name = "creator_time")
    private LocalDateTime creatorTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
