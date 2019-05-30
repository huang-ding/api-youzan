package com.saopay.apiyouzan.service.youzan.impl;

import com.saopay.apiyouzan.data.pojo.dto.MsgPushEntity;
import com.saopay.apiyouzan.service.youzan.YouZanNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author huangding
 * @description 交易创建 当一笔订单创建时会通知该消息
 * @date 2018/11/29 13:38
 */
@Service("trade_TradeCreate")
@Slf4j
public class YouZanNotifyTradeTradeCreateServiceImpl implements YouZanNotifyService {

    @Override
    @Async("geAsynctExecutor")
    public void youZanManageNotify(MsgPushEntity msgPushEntity) {
        log.info("====创建订单通知");
    }
}
