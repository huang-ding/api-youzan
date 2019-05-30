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
 * @description 活动推广码
 * @date 2018/11/23 15:57
 */
@Entity(name = "activity_no")
@Data
public class ActivityNo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "open_id")
    private String openId;

    private Integer type;

    private String code;

    @Column(name = "creator_time")
    private LocalDateTime creatorTime;

    /**
     * 有效期
     */
    @Column(name = "period_of_validity")
    private LocalDateTime periodOfValidity;
}
