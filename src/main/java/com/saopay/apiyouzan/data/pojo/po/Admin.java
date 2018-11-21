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
 * @description
 * @date 2018/11/20 10:19
 */
@Entity
@Data
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "open_id")
    private String openId;
    private String tags;
    @Column(name = "creator_time")
    private LocalDateTime creatorTime;
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
