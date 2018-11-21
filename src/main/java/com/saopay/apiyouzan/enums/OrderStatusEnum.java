package com.saopay.apiyouzan.enums;

/**
 * @author huangding
 * @description 有赞订单状态
 * @date 2018/11/15 13:21
 */
public enum OrderStatusEnum {
    /**
     * 订单状态，一次只能查询一种状态 待付款：WAIT_BUYER_PAY 待发货：WAIT_SELLER_SEND_GOODS 等待买家确认：WAIT_BUYER_CONFIRM_GOODS
     * 订单完成：TRADE_SUCCESS 订单关闭：TRADE_CLOSED 退款中：TRADE_REFUND
     */
    WAIT_BUYER_PAY("WAIT_BUYER_PAY", "待付款"),
    WAIT_SELLER_SEND_GOODS("WAIT_SELLER_SEND_GOODS", "待发货"),
    WAIT_BUYER_CONFIRM_GOODS("WAIT_BUYER_CONFIRM_GOODS", "等待买家确认"),
    TRADE_SUCCESS("TRADE_SUCCESS", "订单完成"),
    TRADE_CLOSED("TRADE_CLOSED", "订单关闭"),
    TRADE_REFUND("TRADE_REFUND", "退款中"),
    ;

    private String code;

    private String name;

    OrderStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
