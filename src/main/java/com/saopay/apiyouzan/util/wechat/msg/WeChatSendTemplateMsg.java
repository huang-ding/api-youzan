package com.saopay.apiyouzan.util.wechat.msg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.saopay.apiyouzan.util.http.JsonResultErrorEnum;
import com.saopay.apiyouzan.util.http.OkHttpUtil;
import com.saopay.apiyouzan.util.wechat.WeChatBaseUtil;
import com.saopay.apiyouzan.util.wechat.msg.bo.WeChatTemplateData;
import com.saopay.apiyouzan.util.wechat.msg.bo.WeChatTemplateData.DataBean;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author huangding
 * @description 发送微信模板消息
 * @date 2018/11/28 14:06
 */
@Component
@Slf4j
public class WeChatSendTemplateMsg {

    @Autowired
    private WeChatBaseUtil weChatBaseUtil;

    private static String baseUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    @Async("geAsynctExecutor")
    public void sendMsg(Map<String, String> map, String templateId, String openId, String resUrl) {

        Map<String, DataBean> data = new HashMap<>();

        map.forEach((key, val) -> {
            DataBean dataBean = new DataBean();
            dataBean.setColor("#173177");
            dataBean.setValue(val);
            data.put(key, dataBean);
        });

        WeChatTemplateData weChatTemplateData = new WeChatTemplateData();
        weChatTemplateData.setTemplate_id(templateId);
        weChatTemplateData.setTouser(openId);
        weChatTemplateData.setUrl(resUrl);
        weChatTemplateData.setData(data);

        String url = baseUrl.replace("ACCESS_TOKEN", weChatBaseUtil.getAccessToken());

        String json = JSON.toJSONString(weChatTemplateData);
        String requestJson = OkHttpUtil.postJsonParams(url, json);
        JSONObject jsonObject = JSONObject.parseObject(requestJson);
        String errcode = jsonObject.getString("errcode");
        log.info("模板id:{}", templateId);
        if (!JsonResultErrorEnum.SUCCESS.getCode().equals(errcode)) {
            log.error("模板消息发送失败：{}", requestJson);
        }
    }

}
