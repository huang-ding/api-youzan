package com.saopay.apiyouzan.util.youzan;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author huangding
 * @description
 * @date 2018/11/15 10:37
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class YouZanBaseUtilTest {

    @Autowired
    private YouZanBaseUtil youZanBaseUtil;

    @Test
    public void testGetAccessToken() {
        log.info("有赞token:{}", youZanBaseUtil.getAccessToken());
    }

}
