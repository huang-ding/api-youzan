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
 * @description 用户基本数据
 * @date 2018-11-28 10:21:50
 */
@Data
@Entity(name = "user")
public class User {


    /**
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     *
     */
    @Column(name = "name")
    private String name;

    /**
     *
     */
    @Column(name = "phone")
    private String phone;

    /**
     *
     */
    @Column(name = "sex")
    private Integer sex;

    /**
     *
     */
    @Column(name = "open_id")
    private String openId;

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
