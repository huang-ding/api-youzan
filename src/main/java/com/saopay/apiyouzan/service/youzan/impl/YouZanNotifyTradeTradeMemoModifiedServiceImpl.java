package com.saopay.apiyouzan.service.youzan.impl;

import com.saopay.apiyouzan.data.pojo.dto.MsgPushEntity;
import com.saopay.apiyouzan.service.youzan.YouZanNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author huangding
 * @description 卖家修改交易备注 卖家修改交易备注时触发
 * @date 2018/11/29 13:45
 */
@Service("trade_TradeMemoModified")
@Slf4j
public class YouZanNotifyTradeTradeMemoModifiedServiceImpl implements YouZanNotifyService {

    @Override
    @Async("geAsynctExecutor")
    public void youZanManageNotify(MsgPushEntity msgPushEntity) {

    }
}
