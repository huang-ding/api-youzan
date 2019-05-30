package com.saopay.apiyouzan.util.wechat.msg.impl;

import com.saopay.apiyouzan.data.dao.jpa.ActivityInfoJpaDao;
import com.saopay.apiyouzan.data.dao.jpa.WeChatQRCodeInfoJpaDao;
import com.saopay.apiyouzan.data.pojo.dto.WeChatBaseInfo;
import com.saopay.apiyouzan.data.pojo.po.ActivityInfo;
import com.saopay.apiyouzan.data.pojo.po.WeChatQRCodeInfo;
import com.saopay.apiyouzan.enums.ActivityType;
import com.saopay.apiyouzan.service.activity.ActivityService;
import com.saopay.apiyouzan.util.BaseUtil;
import com.saopay.apiyouzan.util.DateUtil;
import com.saopay.apiyouzan.util.file.PictureMerge;
import com.saopay.apiyouzan.util.http.JsonResult;
import com.saopay.apiyouzan.util.http.JsonResultErrorEnum;
import com.saopay.apiyouzan.util.wechat.WeChatBaseUtil;
import com.saopay.apiyouzan.util.wechat.WeChatQRCodeUtil;
import com.saopay.apiyouzan.util.wechat.WechatUploadMeida;
import com.saopay.apiyouzan.util.wechat.msg.WeChatMsgInterface;
import com.saopay.apiyouzan.util.wechat.msg.WeChatMsgUtil;
import com.saopay.apiyouzan.util.wechat.msg.WeChatSendTemplateMsg;
import com.saopay.apiyouzan.util.wechat.msg.bo.WeChatBaseRespMsg;
import com.saopay.apiyouzan.util.wechat.msg.bo.WeChatImageRespMsg;
import com.saopay.apiyouzan.util.wechat.msg.bo.WeChatTextRespMsg;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author huangding
 * @description
 * @date 2018/11/28 11:20
 */
@Slf4j
@Component
public class ActivityZeroMsgImpl implements WeChatMsgInterface {


    @Autowired
    private WeChatBaseUtil weChatBaseUtil;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private WeChatQRCodeInfoJpaDao weChatQRCodeInfoJpaDao;
    @Autowired
    private WeChatSendTemplateMsg weChatSendTemplateMsg;
    @Value("${wechat.template_id.activity_id}")
    private String weChatTemplateIdActivityId;

    @Autowired
    private ActivityInfoJpaDao activityInfoJpaDao;


    /**
     * 发送微信邀请码图片
     */
    @Override
    public void sendInvitationCode(WeChatBaseRespMsg weChatBaseRespMsg,
        HttpServletResponse response)
        throws IOException {
        //获取基本信息
        WeChatBaseInfo weChatBaseInfo = weChatBaseUtil
            .getWeChatBaseInfo(weChatBaseRespMsg.getToUserName());

        //获取邀请码
        String activityCode = activityService.getActivityCode(weChatBaseRespMsg.getToUserName());

        //获取二维码
        String codeUrl;
        WeChatQRCodeInfo weChatQRCodeInfo = weChatQRCodeInfoJpaDao
            .findByTypeAndOpenId(ActivityType.ZERO.getCode(), weChatBaseRespMsg.getToUserName());
        LocalDateTime now = LocalDateTime.now();
        if (null != weChatQRCodeInfo) {
            if (weChatQRCodeInfo.getExpireSeconds() > 0) {
                LocalDateTime localDateTime = weChatQRCodeInfo.getUpdateTime()
                    .plusSeconds(weChatQRCodeInfo.getExpireSeconds());
                if (DateUtil.compareTo(now, localDateTime)) {
                    codeUrl = weChatQRCodeInfo.getUrl();
                } else {
                    codeUrl = WeChatQRCodeUtil
                        .getUrl(weChatBaseUtil.getAccessToken(), activityCode);
                    weChatQRCodeInfo.setUrl(codeUrl);
                    weChatQRCodeInfo.setUpdateTime(now);
                    weChatQRCodeInfoJpaDao.save(weChatQRCodeInfo);
                }

            } else {
                codeUrl = weChatQRCodeInfo.getUrl();
            }
        } else {
            codeUrl = WeChatQRCodeUtil
                .getUrl(weChatBaseUtil.getAccessToken(), activityCode);
            weChatQRCodeInfo = new WeChatQRCodeInfo();
            weChatQRCodeInfo.setUpdateTime(now);
            weChatQRCodeInfo.setCreatorTime(now);
            weChatQRCodeInfo.setUrl(codeUrl);
            weChatQRCodeInfo.setExpireSeconds(2592000 - 2000);
            weChatQRCodeInfo.setEventKey(Integer.valueOf(activityCode));
            weChatQRCodeInfo.setOpenId(weChatBaseRespMsg.getToUserName());
            weChatQRCodeInfo.setType(ActivityType.ZERO.getCode());
            weChatQRCodeInfoJpaDao.save(weChatQRCodeInfo);
        }

        //获取模板
        File templateImageFile = BaseUtil
            .getTemplateImageFile("invitation_code.jpg");

        //拼装模板图片
        String portraitUrl = weChatBaseInfo.getHeadimgurl();
        String userName = weChatBaseInfo.getNickname();
        InputStream inputStream = PictureMerge
            .generateCode(templateImageFile, codeUrl, portraitUrl, userName);

        //上传微信临时素材
        String mediaId = WechatUploadMeida
            .upload(inputStream, LocalDateTime.now().toString() + ".png",
                weChatBaseUtil.getAccessToken(), "image");

        //发送图片信息
        WeChatImageRespMsg weChatTextRespMsg = new WeChatImageRespMsg(
            weChatBaseRespMsg.getFromUserName(),
            weChatBaseRespMsg.getToUserName(), weChatBaseRespMsg.getCreateTime(), mediaId);
        WeChatMsgUtil.imageMessageToXml(weChatTextRespMsg, response);

        //默认添加活动记录
        activityService.saveActivityUser(activityCode, weChatBaseRespMsg.getToUserName(),
            ActivityType.ZERO.getCode(), true);
    }

    @Override
    public void sendEventScan(WeChatBaseRespMsg weChatBaseRespMsg, String eventKey,
        HttpServletResponse response) throws IOException {
        JsonResult jsonResult = activityService
            .saveActivityUser(eventKey, weChatBaseRespMsg.getToUserName(),
                ActivityType.ZERO.getCode(),
                true);

        String eventContent = "谢谢您的关注！！！";

        switch (JsonResultErrorEnum.N(jsonResult.getCode())) {
            case SUCCESS:
                ActivityInfo activityInfo = (ActivityInfo) jsonResult.getData();
                String parentOpenId = activityInfo.getParentOpenId();
                if (StringUtils.isNotBlank(parentOpenId)) {
                    //发送消息通知上级
                    String url = "http://n188p76826.iok.la/activity/zero/templateMsgNotify";
                    sendParentMsg(parentOpenId, activityInfo.getOpenId(), url);
                }
                eventContent = "谢谢参与活动！！！";
                break;
            case USER_CONSIST:
                eventContent = "此活动新用户才可参与！！！";
                break;
            case ACTIVITY_ALREADY_INVOLVED:
                eventContent = "您已参与该活动！！！";
                break;
            default:
                break;
        }

        WeChatTextRespMsg weChatTextScanRespMsg = new WeChatTextRespMsg(
            eventContent,
            weChatBaseRespMsg.getFromUserName(),
            weChatBaseRespMsg.getToUserName(), weChatBaseRespMsg.getCreateTime());
        WeChatMsgUtil.textMessageToXml(weChatTextScanRespMsg, response);
    }

    @Async("geAsynctExecutor")
    public void sendParentMsg(String parentOpenId, String childOpenId, String url) {
        WeChatBaseInfo weChatBaseInfo = weChatBaseUtil.getWeChatBaseInfo(childOpenId);
        int persons = activityInfoJpaDao
            .countByTypeAndParentOpenId(ActivityType.ZERO.getCode(), parentOpenId);
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("first", "恭喜你，离奖励更进一步");
        dataMap.put("keynote1", weChatBaseInfo.getNickname());
        dataMap.put("keynote3", DateUtil.nowDateTimeString());
        dataMap.put("remark", "已邀请人数：" + persons);
        weChatSendTemplateMsg.sendMsg(dataMap, weChatTemplateIdActivityId, parentOpenId, url);
    }
}
