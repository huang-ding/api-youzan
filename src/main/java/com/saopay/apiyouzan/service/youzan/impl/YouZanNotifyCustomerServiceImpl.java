package com.saopay.apiyouzan.service.youzan.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.saopay.apiyouzan.data.dao.jpa.YouZanCustomerJpaDao;
import com.saopay.apiyouzan.data.pojo.dto.MsgPushEntity;
import com.saopay.apiyouzan.data.pojo.po.YouZanCustomer;
import com.saopay.apiyouzan.service.youzan.YouZanNotifyService;
import com.saopay.apiyouzan.service.youzan.YouZanUserService;
import com.saopay.apiyouzan.util.http.JsonResult;
import com.youzan.open.sdk.exception.KDTException;
import com.youzan.open.sdk.gen.v3_0_0.model.YouzanUserWeixinOpenidGetResult;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author huangding
 * @description 客户消息事件 SCRM_CUSTOMER_EVENT
 * @date 2018/11/29 10:32
 */

@Service("SCRM_CUSTOMER_EVENT")
@Slf4j
public class YouZanNotifyCustomerServiceImpl implements YouZanNotifyService {

    @Autowired
    private YouZanCustomerJpaDao youZanCustomerJpaDao;
    @Autowired
    private YouZanUserService youZanUserService;


    @Override
    @Async("geAsynctExecutor")
    public void youZanManageNotify(MsgPushEntity msgPushEntity) {
        String msg = msgPushEntity.getMsg();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        log.info("======客户消息：{}", JSON.toJSONString(jsonObject));
//        帐号ID
        String accountId = jsonObject.getString("account_id");
        YouZanCustomer youZanCustomer = youZanCustomerJpaDao.findByAccountId(accountId);
        LocalDateTime now = LocalDateTime.now();

        if (youZanCustomer == null) {
            youZanCustomer = new YouZanCustomer();
            youZanCustomer.setCreatorTime(now);
        }
        String mobile = jsonObject.getString("mobile");
        youZanCustomer.setAccountId(accountId);
        youZanCustomer.setMobile(mobile);
        youZanCustomer.setAccountType(jsonObject.getString("account_type"));
        youZanCustomer.setUpdateTime(now);

        try {
            YouzanUserWeixinOpenidGetResult youzanUserWeixinOpenidGetResult = youZanUserService
                .getYouZanOpenId(mobile + "1231");
            if (null != youzanUserWeixinOpenidGetResult) {
                youZanCustomer.setOpenId(youzanUserWeixinOpenidGetResult.getOpenId());
                youZanCustomer.setUnionId(youzanUserWeixinOpenidGetResult.getUnionId());
            }
        } catch (KDTException e) {
            log.error(e.getMessage());
        }
        youZanCustomerJpaDao.save(youZanCustomer);
    }
}
