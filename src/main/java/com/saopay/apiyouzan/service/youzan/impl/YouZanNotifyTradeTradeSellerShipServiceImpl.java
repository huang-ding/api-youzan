package com.saopay.apiyouzan.service.youzan.impl;

import com.saopay.apiyouzan.data.pojo.dto.MsgPushEntity;
import com.saopay.apiyouzan.service.youzan.YouZanNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author huangding
 * @description 卖家发货 卖家对所有商品发货完成，且主订单状态为「商家已发货」时触发
 * @date 2018/11/29 13:45
 */
@Service("trade_TradeSellerShip")
@Slf4j
public class YouZanNotifyTradeTradeSellerShipServiceImpl implements YouZanNotifyService {

    @Override
    @Async("geAsynctExecutor")
    public void youZanManageNotify(MsgPushEntity msgPushEntity) {

    }
}
