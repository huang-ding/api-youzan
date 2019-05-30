package com.saopay.apiyouzan.util.wechat.msg;

import com.saopay.apiyouzan.util.DateUtil;
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
 * @date 2018/11/28 14:17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class WeChatSendTemplateMsgTest {


    @Autowired
    private WeChatSendTemplateMsg weChatSendTemplateMsg;

    @Test
    public void testSendMsg() {
        /**
         * {{first.DATA}} 好友昵称：{{keynote1.DATA}} 日期：{{keynote3.DATA}} {{remark.DATA}}
         */
        Map<String, String> map = new HashMap<>();
        map.put("first", "nmd");
        map.put("keynote1", "wd");
        map.put("keynote3", DateUtil.nowDateTimeString());
        map.put("remark", "wc");
        String templateId = "d4Di9doKr80Y9dmDIO6lklfcKE8CuWP0p5Brn3Axf9g";
        String openid = "oWa0S1a3gWvhoDOXYZ6lbVJxiG_A";
        weChatSendTemplateMsg.sendMsg(map, templateId, openid, null);
    }
}
