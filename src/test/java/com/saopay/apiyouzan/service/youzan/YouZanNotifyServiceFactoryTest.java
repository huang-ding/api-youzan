package com.saopay.apiyouzan.service.youzan;

import com.saopay.apiyouzan.data.pojo.dto.MsgPushEntity;
import com.saopay.apiyouzan.service.youzan.impl.YouZanNotifyCustomerServiceImpl;
import com.saopay.apiyouzan.util.youzan.YouZanNotifyType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author huangding
 * @description
 * @date 2018/11/29 10:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class YouZanNotifyServiceFactoryTest {

    @Test
    public void testYouZanNotifyServiceFactory() {
        YouZanNotifyCustomerServiceImpl youZanNotifyService = YouZanNotifyServiceFactory
            .getYouZanNotifyService(YouZanNotifyType.SCRM_CUSTOMER_EVENT.getType());
        youZanNotifyService.youZanManageNotify(new MsgPushEntity());
    }
}
