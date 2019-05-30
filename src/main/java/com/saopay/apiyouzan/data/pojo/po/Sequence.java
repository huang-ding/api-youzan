package com.saopay.apiyouzan.data.pojo.po;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

/**
 * @author huangding
 * @description
 * @date 2018/11/23 16:31
 */
@Entity(name = "sequence")
@Data
public class Sequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer type;
    private String alias;
    private Integer value;
    private String prefix;
    private String remark;
}
