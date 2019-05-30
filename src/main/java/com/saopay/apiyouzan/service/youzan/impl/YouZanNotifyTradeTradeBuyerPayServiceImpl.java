package com.saopay.apiyouzan.service.youzan.impl;

import com.saopay.apiyouzan.data.pojo.dto.MsgPushEntity;
import com.saopay.apiyouzan.service.youzan.YouZanNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author huangding
 * @description 买家付款
 *  买家付款且主订单状态为「等待商家发货」时触发 /对于周期购订单，
 *  买家付款且主订单状态为「周期购待发货」/对于多人拼团订单，需要拼团成功，
 *  主订单状态为「等待商家发货」时触发
 * @date 2018/11/29 13:46
 */
@Service("trade_TradeBuyerPay")
@Slf4j
public class YouZanNotifyTradeTradeBuyerPayServiceImpl implements YouZanNotifyService {

    @Override
    @Async("geAsynctExecutor")
    public void youZanManageNotify(MsgPushEntity msgPushEntity) {
        log.info("====买家付款通知");
    }
}
