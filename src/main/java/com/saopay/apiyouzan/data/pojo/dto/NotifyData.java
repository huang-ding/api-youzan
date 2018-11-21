package com.saopay.apiyouzan.data.pojo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huangding
 * @description
 * @date 2018/11/13 16:23
 */
@NoArgsConstructor
@Data
public class NotifyData {

    /**
     * msg : %7B%22fans_id%22%3A0%2C%22fans_type%22%3A0%2C%22mobile%22%3A%2218768423400%22%2C%22card_alias%22%3A%222omk5g63jgmvpa%22%2C%22card_no%22%3A%22231820909057462930%22%2C%22event_time%22%3A%222017-06-30+16%3A08%3A29%22%7D
     * kdt_name : 墨鱼小课堂 VerificationCodeController : false sign : 1be2bc5f1d5ca000736850de10b3ce0d sendCount : 3 type :
     * SCRM_CUSTOMER_CARD version : 1498810109 client_id : 6cd25b3f99727975b5 mode : 1 kdt_id :
     * 164932 id : 231820909057462930 status : CUSTOMER_CARD_TAKEN
     */

    /**
     * 经过UrlEncode（UTF-8）编码的消息对象，具体参数请看本页中各业务消息结构文档
     */
    private String msg;
    /**
     * 店铺名称
     */
    private String kdt_name;
    /**
     * false-非测试消息，true- 测试消息 ；PUSH服务会定期通过发送测试消息检查开发者服务是否正常
     */
    private boolean test;
    private String sign;
    /**
     * 重发的次数
     */
    private int sendCount;
    /**
     * 消息业务类型：TRADE_ORDER_STATE-订单状态事件，TRADE_ORDER_REFUND-退款事件，TRADE_ORDER_EXPRESS-物流事件，ITEM_STATE-商品状态事件，ITEM_INFO-商品基础信息事件，POINTS-积分，SCRM_CARD-会员卡（商家侧），SCRM_CUSTOMER_CARD-会员卡（用户侧），TRADE-交易V1，ITEM-商品V1
     */
    private String type;
    /**
     * 消息版本号，为了解决顺序性的问题 ，高版本覆盖低版本
     */
    private int version;
    /**
     * 对应开发者后台的client_id
     */
    private String client_id;
    /**
     * 1-自用型/工具型/平台型消息；0-签名模式消息
     */
    private int mode;
    /**
     * 店铺ID
     */
    private int kdt_id;
    /**
     * 业务消息的标识: 如 订单消息为订单编号,会员卡消息为会员卡id标识
     */
    private String id;
    /**
     * 消息状态，对应消息业务类型。如TRADE_ORDER_STATE-订单状态事件，对应有等待买家付款（WAIT_BUYER_PAY）、等待卖家发货（WAIT_SELLER_SEND_GOODS）等多种状态，详细可参考
     * 消息结构里的描述
     */
    private String status;
}
