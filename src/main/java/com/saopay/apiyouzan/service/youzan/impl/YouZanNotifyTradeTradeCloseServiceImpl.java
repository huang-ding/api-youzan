package com.saopay.apiyouzan.service.youzan.impl;

import com.saopay.apiyouzan.data.pojo.dto.MsgPushEntity;
import com.saopay.apiyouzan.service.youzan.YouZanNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author huangding
 * @description 交易关闭 买家未付款， 买家或卖家取消订单，且交易状态变为「交易关闭」时触发/ 超时未付款， 系统取消订单，且交易状态变为「交易关闭」时触发/订单全额退款，
 * 且交易状态变为「交易关闭」时触发
 * @date 2018/11/29 10:32
 */

@Service("trade_TradeClose")
@Slf4j
public class YouZanNotifyTradeTradeCloseServiceImpl implements YouZanNotifyService {


    @Override
    @Async("geAsynctExecutor")
    public void youZanManageNotify(MsgPushEntity msgPushEntity) {
        log.info("SCRM_CUSTOMER_EVENT");
    }
}
