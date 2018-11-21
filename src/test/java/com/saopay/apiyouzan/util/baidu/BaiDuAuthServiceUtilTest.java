package com.saopay.apiyouzan.util.baidu;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author huangding
 * @description
 * @date 2018/11/14 9:57
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BaiDuAuthServiceUtilTest {

    @Test
    public void testGetAuth(){
        log.info(BaiDuAuthServiceUtil.getAuth());
    }

}
