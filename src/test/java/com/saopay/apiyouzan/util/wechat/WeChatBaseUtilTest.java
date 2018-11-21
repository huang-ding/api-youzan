package com.saopay.apiyouzan.util.wechat;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author huangding
 * @description
 * @date 2018/11/19 10:15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class WeChatBaseUtilTest {

    @Autowired
    private WeChatBaseUtil weChatBaseUtil;

    @Test
    public void testGetAccessToken() {
        String accessToken = weChatBaseUtil.getAccessToken();
        log.info("微信token：{}", accessToken);
    }

    @Test
    public void testGetSign() {
        Map<String, String> map = new HashMap<>();
        map.put("noncestr", "Wm3WZYTPz0wzccnW");
        map.put("jsapi_ticket",
            "sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg");
        map.put("timestamp", "1414587457");
        map.put("url", "http://mp.weixin.qq.com?params=value");
        String sign = weChatBaseUtil.getSign(map);
        log.info("微信sdk-sign:{}", sign);

    }
}
