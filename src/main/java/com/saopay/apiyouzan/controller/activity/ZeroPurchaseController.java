package com.saopay.apiyouzan.controller.activity;

import com.saopay.apiyouzan.controller.YouZanBaseController;
import com.saopay.apiyouzan.service.activity.ActivityService;
import com.saopay.apiyouzan.util.BaseUtil;
import com.saopay.apiyouzan.util.wechat.WeChatBaseUtil;
import com.saopay.apiyouzan.util.wechat.msg.WeChatMsgInterface;
import com.saopay.apiyouzan.util.wechat.msg.WeChatMsgUtil;
import io.swagger.annotations.Api;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author huangding
 * @description
 * @date 2018/11/23 9:05
 */
@Controller
@RequestMapping("activity/zero")
@Api("零元购活动")
@Slf4j
public class ZeroPurchaseController extends YouZanBaseController {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private WeChatBaseUtil weChatBaseUtil;
    @Autowired
    private WeChatMsgUtil weChatMsgUtil;

//    @Resource(name = "activityZeroMsgImpl")
    @Autowired
//    @Qualifier("activityZeroMsgImpl")
    private WeChatMsgInterface activityZeroMsgImpl;

//    /**
//     * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx09ee5c62e5b2a134&redirect_uri=http://n188p76826.iok.la/activity/zero/code&response_type=code&scope=snsapi_userinfo&state=123&connect_redirect=1#wechat_redirect
//     */
//    @GetMapping("code/{no}")
//    @ResponseBody
//    public String getCode(@PathVariable String no, String code) {
//        String openId = weChatBaseUtil.getOpenId(code);
//        activityService.saveActivityUser(no, openId);
//        return "ok";
//    }

//
//    @RequestMapping("no")
//    public String getActivityNo(String code, ModelMap modelMap) {
//
//        String openId = weChatBaseUtil.getOpenId(code);
//        String activityCode = activityService.getActivityCode(openId);
//        String tempTicket = WeChatQRCodeUtil
//            .getWeChatQRCode(weChatBaseUtil.getAccessToken(), null,
//                Integer.parseInt(activityCode));
//        String url = WeChatQRCodeUtil.showQrcode(tempTicket);
//
//        modelMap.addAttribute("url", url);
//        return "activity/zero";
//    }

    @RequestMapping(value = "notify", method = RequestMethod.GET, produces = {
        "application/json;charset=utf-8"})
    @ResponseBody
    public void notifyWeChat(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        //微信公众号管理界面配置参数
        String token = "hd520156";
        //获取请求的四个参数
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        //检验四个参数是否有效
        if (!BaseUtil.hasBlank(signature, timestamp, nonce, echostr)) {
            String[] list = {token, timestamp, nonce};
            //字典排序
            Arrays.sort(list);
            //拼接字符串
            StringBuilder builder = new StringBuilder();
            for (String str : list) {
                builder.append(str);
            }
            //sha1加密
            String hashcode = WeChatBaseUtil.getSha1(builder.toString());
            //不区分大小写差异情况下比较是否相同
            if (hashcode.equalsIgnoreCase(signature)) {
                //响应输出
                response.getWriter().println(echostr);
            }
        }

    }

    @RequestMapping(value = "notify", method = RequestMethod.POST, produces = {
        "application/xml; charset=UTF-8"})
    public void notifyMsg(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        weChatMsgUtil.processRequest(request, response, activityZeroMsgImpl);
    }

    @RequestMapping(value = "templateMsgNotify")
    public void templateMsgNotify(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        Enumeration<String> parameterNames = request.getParameterNames();
        Enumeration<String> attributeNames = request.getAttributeNames();


    }



}
