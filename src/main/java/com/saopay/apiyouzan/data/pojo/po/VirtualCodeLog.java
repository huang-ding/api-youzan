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
 * @description 核销记录表
 * @date 2018/11/20 13:28
 */
@Entity(name = "virtual_code_log")
@Data
public class VirtualCodeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "open_id")
    private String openId;
    private String code;
    @Column(name = "creator_time")
    private LocalDateTime creatorTime;
}
