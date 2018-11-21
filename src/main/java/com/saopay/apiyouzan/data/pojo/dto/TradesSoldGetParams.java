package com.saopay.apiyouzan.data.pojo.dto;

import com.youzan.open.sdk.gen.v4_0_0.model.YouzanTradesSoldGetParams;
import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @author huangding
 * @description 处理时间字符串
 * @date 2018/11/15 13:34
 */
@Data
public class TradesSoldGetParams extends YouzanTradesSoldGetParams {

    @ApiParam(value = "订单状态，一次只能查询一种状态 待付款：WAIT_BUYER_PAY 待发货：WAIT_SELLER_SEND_GOODS 等待买家确认：WAIT_BUYER_CONFIRM_GOODS 订单完成：TRADE_SUCCESS 订单关闭：TRADE_CLOSED 退款中：TRADE_REFUND")
    private String status;

    @ApiParam(value = "订单类型 NORMAL：普通订单 PEERPAY：代付 GIFT：我要送人 FX_CAIGOUDAN：分销采购单 PRESENT：赠品 WISH：心愿单 QRCODE：二维码订单 QRCODE_3RD：线下收银台订单 FX_MERGED：合并付货款 VERIFIED：1分钱实名认证 PINJIAN：品鉴 REBATE：返利 FX_QUANYUANDIAN：全员开店 FX_DEPOSIT：保证金 PF：批发 GROUP：拼团 HOTEL：酒店 TAKE_AWAY：外卖 CATERING_OFFLINE：堂食点餐 CATERING_QRCODE：外卖买单 BEAUTY_APPOINTMENT：美业预约单 BEAUTY_SERVICE：美业服务单 KNOWLEDGE_PAY：知识付费 GIFT_CARD：礼品卡")
    private String type;
    @ApiParam(value = "物流类型搜索 同城送订单：LOCAL_DELIVERY 自提订单：SELF_FETCH 快递配送：EXPRESS")
    private String expressType;

    @ApiParam(value = "结束时间(2018-11-11 00:00:00)", format = "2018-11-11 00:00:00")
    private String newEndCreated;
    @ApiParam(value = "开始时间(2018-10-01 00:00:00)", format = "2018-10-01 00:00:00")
    private String newStartCreated;


}
