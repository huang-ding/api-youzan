package com.saopay.apiyouzan.service.youzan;

import com.saopay.apiyouzan.util.youzan.YouZanNotifyType;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author huangding
 * @description 消息推送工厂类
 * @date 2018/11/29 10:45
 */
@Component
@Slf4j
public class YouZanNotifyServiceFactory implements ApplicationContextAware {

    private String[] order_types = {"trade_TradeCreate", "trade_TradePaid", "trade_TradeBuyerPay",
        "trade_TradePartlySellerShip", "trade_TradeSellerShip", "trade_TradeMemoModified",
        "trade_TradeSuccess", "trade_TradeClose"};

    private static Map<String, YouZanNotifyService> beansOfType;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        beansOfType = applicationContext
            .getBeansOfType(YouZanNotifyService.class);
    }

    /**
     * 根据推送类型获取对应实现类
     */
    public static <T extends YouZanNotifyService> T getYouZanNotifyService(String type) {
        return (T) beansOfType.get(type);
    }


}
