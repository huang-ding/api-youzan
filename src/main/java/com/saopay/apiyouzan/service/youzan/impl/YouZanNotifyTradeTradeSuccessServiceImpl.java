package com.saopay.apiyouzan.service.youzan.impl;

import com.saopay.apiyouzan.data.pojo.dto.MsgPushEntity;
import com.saopay.apiyouzan.service.youzan.YouZanNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author huangding
 * @description 交易成功 买家确认收货或系统自动确认收货且主订单状态变为「交易成功」时触发
 * @date 2018/11/29 10:32
 */

@Service("trade_TradeSuccess")
@Slf4j
public class YouZanNotifyTradeTradeSuccessServiceImpl implements YouZanNotifyService {


    @Override
    @Async("geAsynctExecutor")
    public void youZanManageNotify(MsgPushEntity msgPushEntity) {
        log.info("SCRM_CUSTOMER_EVENT");
    }
}
