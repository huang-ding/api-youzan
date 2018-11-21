package com.saopay.apiyouzan.service;

import com.saopay.apiyouzan.util.youzan.YouZanBaseUtil;
import com.youzan.open.sdk.client.auth.Token;
import com.youzan.open.sdk.client.core.DefaultYZClient;
import com.youzan.open.sdk.client.core.YZClient;
import com.youzan.open.sdk.gen.v3_0_1.api.YouzanUsersWeixinFollowerGet;
import com.youzan.open.sdk.gen.v3_0_1.model.YouzanUsersWeixinFollowerGetParams;
import com.youzan.open.sdk.gen.v3_0_1.model.YouzanUsersWeixinFollowerGetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author huangding
 * @description
 * @date 2018/11/19 12:05
 */
@Service
public class UserService {

    @Autowired
    private YouZanBaseUtil youZanBaseUtil;


    public YouzanUsersWeixinFollowerGetResult getUserInfoByOpenId(String openId) {
        YZClient client = new DefaultYZClient(new Token(youZanBaseUtil.getAccessToken()));
        YouzanUsersWeixinFollowerGetParams youzanUsersWeixinFollowerGetParams = new YouzanUsersWeixinFollowerGetParams();
        youzanUsersWeixinFollowerGetParams.setWeixinOpenid(openId);
        YouzanUsersWeixinFollowerGet youzanUsersWeixinFollowerGet = new YouzanUsersWeixinFollowerGet();
        youzanUsersWeixinFollowerGet.setAPIParams(youzanUsersWeixinFollowerGetParams);
        YouzanUsersWeixinFollowerGetResult result = client.invoke(youzanUsersWeixinFollowerGet);
        return result;
    }

}
