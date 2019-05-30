package com.saopay.apiyouzan.util.youzan;

import com.saopay.apiyouzan.util.http.JsonResultErrorEnum;

/**
 * 有赞回调类型
 *
 * @author huangding
 * @description:
 * @time: 10:52 2018/11/29
 */

public enum YouZanNotifyType {

    /**
     * 订单类型
     */
    TRADE_TRADE_CREATE("trade_TradeCreate", "交易创建"),
    TRADE_TRADE_PAID("trade_TradePaid", "交易支付"),
    TRADE_TRADE_BUYER_PAY("trade_TradeBuyerPay", "买家付款"),
    TRADE_TRADE_PARTLY_SELLER_SHIP("trade_TradePartlySellerShip", "卖家部分发货"),
    TRADE_TRADE_SELLER_SHIP("trade_TradeSellerShip", "卖家发货"),
    TRADE_TRADE_MEMO_MODIFIED("trade_TradeMemoModified", "卖家修改交易备注"),
    TRADE_TRADE_SUCCESS("trade_TradeSuccess", "交易成功"),
    TRADE_TRADE_CLOSE("trade_TradeClose", "交易关闭"),

    /**
     * 客户类型
     */
    SCRM_CUSTOMER_EVENT("SCRM_CUSTOMER_EVENT", "客户消息事件"),
    CUSTOMER_CREATED("CUSTOMER_CREATED", "创建客户"),
    CUSTOMER_UPDATED("CUSTOMER_UPDATED", "更新客户"),

    ;

    YouZanNotifyType(String type, String remarks) {
        this.type = type;
        this.remarks = remarks;
    }

    public static YouZanNotifyType getYouZanNotifyType(String type) {
        for (YouZanNotifyType youZanNotifyType : values()) {
            if (youZanNotifyType.getType().equals(type)) {
                return youZanNotifyType;
            }
        }
        return null;
    }

    private String type;
    private String remarks;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
