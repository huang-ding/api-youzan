package com.saopay.apiyouzan.scheduled;

import com.saopay.apiyouzan.enums.RedisKeyEnum;
import com.saopay.apiyouzan.redis.RedisUtil;
import com.saopay.apiyouzan.util.wechat.WeChatBaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author huangding
 * @description
 * @date 2018/11/23 20:50
 */
@Component
@Slf4j
public class WeChatScheduled {

    /**
     * 微信token超时时间
     */
    public final static long ONE_Minute = 2 * 60 * 60 * 1000 - 2000;


    @Autowired
    private WeChatBaseUtil weChatBaseUtil;
    @Autowired
    private RedisUtil redisUtil;

    @Scheduled(fixedDelay = ONE_Minute)
    public void weChatTokenTask() {
        log.info("微信token刷新开始");
        redisUtil.set(RedisKeyEnum.WXP_ACCESS_TOKEN, weChatBaseUtil.getToken(), 7200 - 200);
        log.info("微信token刷新完成");
    }

    @Scheduled(fixedDelay = ONE_Minute)
    public void weChatTicketTask() {
        log.info("微信ticket刷新开始");
        redisUtil.set(RedisKeyEnum.WXP_JSAPI_TICKET, weChatBaseUtil.getTicket(), 7200 - 200);
        log.info("微信ticket刷新完成");
    }

}
