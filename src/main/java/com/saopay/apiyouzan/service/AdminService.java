package com.saopay.apiyouzan.service;

import com.saopay.apiyouzan.data.dao.jpa.AdminJpaDao;
import com.saopay.apiyouzan.data.pojo.po.Admin;
import com.saopay.apiyouzan.enums.RedisKeyEnum;
import com.saopay.apiyouzan.redis.RedisUtil;
import com.saopay.apiyouzan.service.youzan.YouZanUserService;
import com.saopay.apiyouzan.util.http.JsonResult;
import com.saopay.apiyouzan.util.http.JsonResultErrorEnum;
import com.saopay.apiyouzan.util.wechat.WeChatBaseUtil;
import com.youzan.open.sdk.gen.v3_0_1.model.YouzanUsersWeixinFollowerGetResult;
import com.youzan.open.sdk.gen.v3_0_1.model.YouzanUsersWeixinFollowerGetResult.CrmUserTag;
import com.youzan.open.sdk.gen.v3_0_1.model.YouzanUsersWeixinFollowerGetResult.CrmWeixinFans;
import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author huangding
 * @description
 * @date 2018/11/20 10:23
 */
@Service
public class AdminService {

    @Autowired
    private WeChatBaseUtil weChatBaseUtil;

    @Autowired
    private YouZanUserService youZanUserService;

    @Autowired
    private AdminJpaDao adminJpaDao;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * @param code 微信code
     * @description 获取openId 从redis查询是否已经存在，不存在则通过openId查询有赞数据，持久化到数据库，然后判断是否存在核销员tag，存在则保存到缓存
     */
    public JsonResult addYouZanVerificationAdmin(String code) {
        String openId = weChatBaseUtil.getOpenId(code);
        String tagName = redisUtil.hget(RedisKeyEnum.YZ_VERIFICATION_TAG, openId);
        if (tagName == null) {
            YouzanUsersWeixinFollowerGetResult youzanUsersWeixinFollowerGetResult = youZanUserService
                .getUserInfoByOpenId(openId);
            CrmWeixinFans user = youzanUsersWeixinFollowerGetResult.getUser();
            if (user != null) {
                boolean flag = false;
                String tagVerification = "核销员";
                Admin admin = new Admin();
                CrmUserTag[] tags = youzanUsersWeixinFollowerGetResult.getUser().getTags();
                if (tags.length > 0) {
                    admin.setOpenId(openId);
                    admin.setUpdateTime(LocalDateTime.now());
                    StringBuilder tagStr = new StringBuilder();
                    for (CrmUserTag tag : tags) {
                        String name = tag.getName();
                        if (tagVerification.equals(name)) {
                            tagStr.append(",").append(name);
                            flag = true;
                        }
                    }
                    if (StringUtils.isBlank(tagStr)) {
                        return JsonResult.error(JsonResultErrorEnum.YZ_TAG_NULL);
                    }
                    admin.setTags(tagStr.substring(1));
                    Admin oldAdmin = adminJpaDao.findByOpenId(openId);
                    if (oldAdmin == null) {
                        admin.setCreatorTime(LocalDateTime.now());
                        adminJpaDao.save(admin);
                    } else if (!admin.getTags().equals(oldAdmin.getTags())) {
                        oldAdmin.setTags(oldAdmin.getTags());
                        oldAdmin.setUpdateTime(LocalDateTime.now());
                        adminJpaDao.save(oldAdmin);
                    }
                    if (flag) {
                        redisUtil
                            .hset(RedisKeyEnum.YZ_VERIFICATION_TAG, openId, tagVerification, 14400);
                    }
                    if (!flag) {
                        return JsonResult.error(JsonResultErrorEnum.YZ_TAG_NULL);
                    }
                    return JsonResult.DATA(openId);
                } else {
                    return JsonResult.error(JsonResultErrorEnum.YZ_TAG_NULL);
                }

            } else {
                return JsonResult.error(JsonResultErrorEnum.YZ_USER_NULL);
            }
        } else {
            return JsonResult.DATA(openId);
        }
    }

}
