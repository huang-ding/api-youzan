package com.saopay.apiyouzan.controller.youzan;

import com.alibaba.fastjson.JSONObject;
import com.saopay.apiyouzan.controller.YouZanBaseController;
import com.saopay.apiyouzan.data.pojo.dto.MsgPushEntity;
import com.saopay.apiyouzan.service.youzan.YouZanNotifyService;
import com.saopay.apiyouzan.service.youzan.YouZanNotifyServiceFactory;
import com.saopay.apiyouzan.util.http.Coder;
import com.saopay.apiyouzan.util.youzan.YouZanMD5;
import com.saopay.apiyouzan.util.youzan.YouZanNotifyType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangding
 * @description
 * @date 2018/11/29 9:23
 */
@RestController
@Api("有赞回调处理")
@RequestMapping("youzan/notify")
public class YouZanNotifyController extends YouZanBaseController {

    /**
     * 服务商
     */
    private static final int MODE = 1;


    @PostMapping("msg")
    @ApiOperation("有赞消息推送")
    public JSONObject youZanNotifyMsg(@RequestBody  MsgPushEntity msgPushEntity) {
        JSONObject res = new JSONObject();
        res.put("code", 0);
        res.put("msg", "success");
        /**
         *  判断是否为心跳检查消息，1.是则直接返回
         */
        if (msgPushEntity.isTest()) {
            return res;
        }
        /**
         * 解析消息推送的模式  这步判断可以省略
         * 0-商家自由消息推送 1-服务商消息推送
         * 以服务商举例,判断是否为服务商类型的消息,否则直接返回
         */
        if (msgPushEntity.getMode() != MODE) {
            return res;
        }
        /**
         * 判断消息是否合法
         */
        String sign = YouZanMD5.MD5(
            (youZanBaseUtil.getClientId() + msgPushEntity.getMsg() + youZanBaseUtil
                .getClientSecret()));
        if (!sign.equals(msgPushEntity.getSign())) {
            return res;
        }

        /**
         * 对于msg 先进行URI解码
         */
        String msg = Coder.decode(msgPushEntity.getMsg(), "utf-8");
        msgPushEntity.setMsg(msg);

        //处理逻辑
        String type = msgPushEntity.getType();
        YouZanNotifyService youZanNotifyService = YouZanNotifyServiceFactory
            .getYouZanNotifyService(type);
        if (null == youZanNotifyService) {
            return res;
        }
        youZanNotifyService.youZanManageNotify(msgPushEntity);
        return res;
    }
}
