package com.saopay.apiyouzan.service.activity;

import static com.saopay.apiyouzan.util.http.JsonResultErrorEnum.ACTIVITY_ALREADY_INVOLVED;

import com.saopay.apiyouzan.data.dao.jpa.ActivityInfoJpaDao;
import com.saopay.apiyouzan.data.dao.jpa.ActivityNoJpaDao;
import com.saopay.apiyouzan.data.pojo.po.ActivityInfo;
import com.saopay.apiyouzan.data.pojo.po.ActivityNo;
import com.saopay.apiyouzan.enums.ActivityType;
import com.saopay.apiyouzan.exception.SequenceException;
import com.saopay.apiyouzan.service.SequenceService;
import com.saopay.apiyouzan.service.UserService;
import com.saopay.apiyouzan.util.http.JsonResult;
import com.saopay.apiyouzan.util.http.JsonResultErrorEnum;
import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author huangding
 * @description
 * @date 2018/11/23 15:52
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ActivityService {

    @Autowired
    private ActivityInfoJpaDao activityInfoJpaDao;
    @Autowired
    private ActivityNoJpaDao activityNoJpaDao;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private UserService userService;

    public String getActivityCode(String openId) {
        ActivityNo oldActivityNo = activityNoJpaDao
            .findByTypeAndOpenId(ActivityType.ZERO.getCode(), openId);
        if (null == oldActivityNo) {
            String code = sequenceService.getSequenceNextActivityCode();
            ActivityNo activityNo = new ActivityNo();
            activityNo.setType(ActivityType.ZERO.getCode());
            activityNo.setOpenId(openId);
            activityNo.setCreatorTime(LocalDateTime.now());
            activityNo.setCode(code);
            ActivityNo save = activityNoJpaDao.save(activityNo);
            if (save.getId() > 0) {
                return code;
            }
            throw new SequenceException("推广码获取失败");
        }
        return oldActivityNo.getCode();
    }


    /**
     * 添加活动用户 根据推荐码查询对应推荐人openId
     *
     * @param isRepeat 是否已存在的用户可以重复参与
     * @return activityType 活动类型 目前使用enum 后期可使用数据库配置，或者根据活动后台配置
     */
    public JsonResult saveActivityUser(String no, String openId, Integer activityType,
        boolean isRepeat) {

        boolean isOpneId = userService.isOldOpenIdAndSaveOpenId(openId);
        //已存在用户不可重复参加
        if (isOpneId && !isRepeat) {
            return JsonResult.error(JsonResultErrorEnum.USER_CONSIST);
        }
        String parentOpenId = null;
        if (StringUtils.isNotBlank(no)) {
            ActivityNo activityNo = activityNoJpaDao
                .findByTypeAndCode(activityType, no);
            if (null != activityNo) {
                parentOpenId = activityNo.getOpenId();
            }
        }
        if (parentOpenId != null && parentOpenId.equals(openId)) {
            parentOpenId = StringUtils.EMPTY;
        }
        //已存在用户可重复参加
        //是否已经参与本次活动
        ActivityInfo oldActivityInfo = activityInfoJpaDao.findByTypeAndOpenId(activityType, openId);
        if (oldActivityInfo == null) {
            ActivityInfo activityInfo = new ActivityInfo();
            if (StringUtils.isNotBlank(parentOpenId)) {
                activityInfo.setParentOpenId(parentOpenId);
            }
            activityInfo.setOpenId(openId);
            activityInfo.setType(activityType);
            activityInfo.setCreatorTime(LocalDateTime.now());
            activityInfoJpaDao.save(activityInfo);
            return JsonResult.SUCCESS().setData(activityInfo);
        }
        return JsonResult.error(ACTIVITY_ALREADY_INVOLVED);
    }

}
