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
 * @description 有赞消息
 * @date 2018-11-29 16:59:27
 */
@Data
@Entity(name = "you_zan_customer")
public class YouZanCustomer {


    /**
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 有赞帐号ID
     */
    @Column(name = "account_id")
    private String accountId;

    /**
     * FansID：自有粉丝ID， Mobile：手机号， YouZanAccount：有赞账号
     */
    @Column(name = "account_type")
    private String accountType;

    /**
     *
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     *
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 对于拥有多个账号的企业来说，unionid可以帮助识别不同公众账号下的用户是否是同一个人
     */
    @Column(name = "union_id")
    private String unionId;

    /**
     *
     */
    @Column(name = "creator_time")
    private LocalDateTime creatorTime;

    /**
     *
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;


}