package com.saopay.apiyouzan.controller;

import com.alibaba.fastjson.JSON;
import com.saopay.apiyouzan.util.wechat.WeChatBaseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangding
 * @description
 * @date 2018/11/19 15:06
 */
@RestController
@RequestMapping("we-chat")
@Api("微信相关")
@Slf4j
public class WeChatController {

    @Value("${wechat.public.app_id}")
    private String appId;
    @Autowired
    private WeChatBaseUtil weChatBaseUtil;

    @GetMapping("config")
    @ApiOperation("微信配置")
    public String getConfig(String url) {
        Map<String, String> map = new HashMap<>();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        UUID uuid = UUID.randomUUID();
        String nonceStr = uuid.toString().replaceAll("-", "");
        map.put("jsapi_ticket", weChatBaseUtil.getJsapiTicket());
        map.put("url", url+"&state=123");
        map.put("timestamp", timestamp);
        //生成签名的随机串
        map.put("noncestr", nonceStr);
        String sign = weChatBaseUtil.getSign(map);
        //签名
        log.info("签名：{}", sign);
        map.put("appId", appId);
        map.put("signature", sign);
        return JSON.toJSONString(map);
    }
}
