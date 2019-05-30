package com.saopay.apiyouzan.service.youzan.impl;

import com.saopay.apiyouzan.data.pojo.dto.MsgPushEntity;
import com.saopay.apiyouzan.service.youzan.YouZanNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author huangding
 * @description 交易支付 买家付款时触发消息，订单瞬时状态，可以只关注买家付款状态
 * @date 2018/11/29 13:45
 */
@Service("trade_TradePaid")
@Slf4j
public class YouZanNotifyTradeTradePaidServiceImpl implements YouZanNotifyService {

    @Override
    @Async("geAsynctExecutor")
    public void youZanManageNotify(MsgPushEntity msgPushEntity) {
        log.info("====交易支付通知");

    }
}
