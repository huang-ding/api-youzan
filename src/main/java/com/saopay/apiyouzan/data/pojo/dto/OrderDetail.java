package com.saopay.apiyouzan.data.pojo.dto;

import lombok.Data;

/**
 * @author huangding
 * @description 有赞订单详情
 * @date 2018/11/15 15:49
 */
@Data
public class OrderDetail {

    /**
     * 订单号
     */
    private String tid;
    /**
     * 订单类型
     */
    private Long type;
    /**
     * 物流类型
     */
    private Long expressType;

    /**
     * 买家id
     */
    private Long buyerId;

    /**
     * 买家手机号
     */
    private String buyerPhone;

    /**
     * 订单买家留言
     */
    private String buyerMessage;

    /**
     * 订单商家备注
     */
    private String tradeMemo;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人手机号
     */
    private String receiverTel;

    /**
     * 收货人省
     */
    private String deliveryProvince;
    /**
     * 收货人市
     */
    private String deliveryCity;

    /**
     * 收货人区
     */
    private String deliveryDistrict;
    /**
     * 收货人详细地址
     */
    private String deliveryAddress;
    /**
     * 到店自提信息 json格式
     */
    private String selfFetchInfo;
    /**
     * 订单明细id
     */
    private String oid;
    /**
     * 商品名称
     */
    private String title;
    /**
     * 商品数量
     */
    private Long num;
    /**
     * 商家编码
     */
    private String outerSkuId;
    /**
     * 商品原价
     */
    private Float price;
    /**
     * 商品优惠后总价
     */
    private Float totalFee;
    /**
     * 订单实付款，也即最终支付价格（payment=orders.payment的总和+邮费 post_fee）
     */
    private Float payment;
    /**
     * 商品id
     */
    private Long itemId;
    /**
     * 规格Id
     */
    private Long skuId;
    /**
     * 规格信息
     */
    private String skuPropertiesName;
    /**
     * 商品编码
     */
    private String outerItemId;


}
