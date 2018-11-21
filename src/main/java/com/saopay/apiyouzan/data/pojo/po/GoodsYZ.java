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
 * @date 2018/11/16 9:33
 */
@Data
@Entity(name = "goods_yz")
public class GoodsYZ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "item_id")
    private Long itemId;
    @Column(name = "sku_id")
    private Long skuId;
    private String title;
    @Column(name = "sku_properties_name")
    private String skuPropertiesName;
    @Column(name = "outer_item_id")
    private String outerItemId;
    @Column(name = "goods_type")
    private String goodsType;
    @Column(name = "goods_grouping")
    private String goodsGrouping;
    @Column(name = "cost_of_price")
    private Integer costOfPrice;
    @Column(name = "creator_time")
    private LocalDateTime creatorTime;
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
