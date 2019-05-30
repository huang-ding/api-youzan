package com.saopay.apiyouzan.service.youzan.impl;

import com.saopay.apiyouzan.data.pojo.dto.MsgPushEntity;
import com.saopay.apiyouzan.service.youzan.YouZanNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author huangding
 * @description 卖家部分发货 对于一个订单多个商品的情况，卖家对某一个商品发货，且子订单状态为「商家已发货」时触发
 * @date 2018/11/29 13:45
 */
@Service("trade_TradePartlySellerShip")
@Slf4j
public class YouZanNotifyTradeTradePartlySellerShipServiceImpl implements YouZanNotifyService {

    @Override
    @Async("geAsynctExecutor")
    public void youZanManageNotify(MsgPushEntity msgPushEntity) {

    }
}
