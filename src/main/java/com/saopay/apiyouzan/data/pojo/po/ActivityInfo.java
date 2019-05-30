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
 * @date 2018/11/23 15:47
 */
@Entity(name = "activity_info")
@Data
public class ActivityInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "open_id")
    private String openId;

    @Column(name = "parent_open_id")
    private String parentOpenId;

    private Integer type;

    @Column(name = "creator_time")
    private LocalDateTime creatorTime;

}
