package com.saopay.apiyouzan.util.wechat.msg;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.saopay.apiyouzan.util.wechat.WXBizMsgCrypt;
import com.saopay.apiyouzan.util.wechat.WeChatBaseUtil;
import com.saopay.apiyouzan.util.wechat.msg.bo.WeChatBaseRespMsg;
import com.saopay.apiyouzan.util.wechat.msg.bo.WeChatImageRespMsg;
import com.saopay.apiyouzan.util.wechat.msg.bo.WeChatMusicRespMsg;
import com.saopay.apiyouzan.util.wechat.msg.bo.WeChatNewsRespMsg;
import com.saopay.apiyouzan.util.wechat.msg.bo.WeChatTextRespMsg;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

/**
 * @author huangding
 * @description
 * @date 2018/11/22 15:29
 */
@Slf4j
@Component
public class WeChatMsgUtil implements Serializable {

    private static final String INVITATION_CODE = "邀请码";

    private static final long serialVersionUID = 8216580104270594538L;


    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<>();
        // 从request中取得输入流
        try (InputStream inputStream = request.getInputStream()) {
            // 读取输入流
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            // 得到xml根元素
            Element root = document.getRootElement();
            // 得到根元素的所有子节点
            List<Element> elementList = root.elements();

            // 遍历所有子节点
            for (Element e : elementList) {
                map.put(e.getName(), e.getText());
            }
        }
        return map;
    }

    public static Map<String, String> parseXml(String xml) throws DocumentException {
        Map<String, String> stringMap = Maps.newHashMap();
        Document document = DocumentHelper.parseText(xml);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList) {
            stringMap.put(e.getName(), e.getText());
        }
        return stringMap;
    }


    /**
     * 微信消息统一处理
     *
     * @param weChatMsgInterface 微信消息发送处理接口
     */
    public void processRequest(HttpServletRequest request, HttpServletResponse response,
        WeChatMsgInterface weChatMsgInterface)
        throws Exception {
        // xml格式的消息数据
        // 默认返回的文本消息内容
        // 调用parseXml方法解析请求消息
        Map<String, String> requestMap = parseXml(request);

        // 开发者微信号
        String fromUserName = requestMap.get("ToUserName");

        //判断文本是否加密
        String encrypt = requestMap.get("Encrypt");
        if (StringUtils.isNotBlank(encrypt)) {
            String fromXML = "<xml><ToUserName><![CDATA[%1$s]]></ToUserName>"
                + "<Encrypt><![CDATA[%2$s]]></Encrypt></xml>";
            String postData = String.format(fromXML, fromUserName, encrypt);

            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String msgSignature = request.getParameter("msg_signature");
            WXBizMsgCrypt wxBizMsgCrypt = WeChatBaseUtil.getWXBizMsgCrypt();
            String data = wxBizMsgCrypt.decryptMsg(msgSignature, timestamp, nonce, postData);

            requestMap.putAll(parseXml(data));
            log.info(data);
        }

        // 发送方帐号
        String toUserName = requestMap.get("FromUserName");
        // 消息类型
        String msgType = requestMap.get("MsgType");
        Integer createTime = Integer.parseInt(requestMap.get("CreateTime"));
        String content = requestMap.get("Content");

        WeChatBaseRespMsg weChatBaseRespMsg = new WeChatBaseRespMsg();
        weChatBaseRespMsg.setToUserName(toUserName);
        weChatBaseRespMsg.setFromUserName(fromUserName);
        weChatBaseRespMsg.setCreateTime(createTime);

        try {
            switch (MsgType.getMagType(msgType)) {
                case TEXT:
                    if (INVITATION_CODE.equals(content)) {
                        weChatMsgInterface.sendInvitationCode(weChatBaseRespMsg, response);
                        return;
                    }
                    WeChatTextRespMsg weChatTextRespMsg = new WeChatTextRespMsg(content,
                        fromUserName,
                        toUserName, createTime);
                    WeChatMsgUtil.textMessageToXml(weChatTextRespMsg, response);
                    return;
                case IMAGE:
                case VIDEO:
                case NEWS:
                case MUSIC:
                case VOICE:
                    return;
                case EVENT:
                    String event = requestMap.get("Event");
                    switch (MsgType.getMagType(event)) {
                        case EVENT_SUBSCRIBE:
                            WeChatTextRespMsg weChatTextSubscribeRespMsg = new WeChatTextRespMsg(
                                "谢谢您的关注！！！",
                                fromUserName,
                                toUserName, createTime);
                            WeChatMsgUtil.textMessageToXml(weChatTextSubscribeRespMsg, response);
                            return;
                        case EVENT_UNSUBSCRIBE:
                            return;
                        case EVENT_SCAN:
                            String eventKey = requestMap.get("EventKey");
                            weChatMsgInterface.sendEventScan(weChatBaseRespMsg, eventKey, response);
                            return;
                        case EVENT_LOCATION:
                        case EVENT_CLICK:
                        default:

                    }
                    break;
                default:
            }
        } catch (NullPointerException e) {
            log.error("未知的消息类型：{}", JSON.toJSONString(requestMap));
            WeChatTextRespMsg weChatTextSubscribeRespMsg = new WeChatTextRespMsg(
                "谢谢您的关注！",
                fromUserName,
                toUserName, createTime);
            WeChatMsgUtil.textMessageToXml(weChatTextSubscribeRespMsg, response);
        }
    }


    /**
     * 文本消息对象转换成xml
     *
     * @param textMessage 文本消息对象
     * @return xml
     */
    public static void textMessageToXml(WeChatTextRespMsg textMessage,
        HttpServletResponse response) throws IOException {
        String xml = "<xml>"
            + "<ToUserName><![CDATA[%s]]></ToUserName>"
            + "<FromUserName><![CDATA[%s]]></FromUserName>"
            + "<CreateTime>%s</CreateTime>"
            + "<MsgType><![CDATA[text]]></MsgType>"
            + "<Content><![CDATA[%s]]></Content>"
            + "</xml>";

        String format = String
            .format(xml, textMessage.getToUserName(), textMessage.getFromUserName(),
                textMessage.getCreateTime(), textMessage.getContent());
        log.info(format);
        response.setContentType("application/xml; charset=utf-8");
        response.getWriter().print(format);
    }

    public static void imageMessageToXml(WeChatImageRespMsg imageRespMsg,
        HttpServletResponse response)
        throws IOException {

        String xml = "<xml>"
            + "<ToUserName><![CDATA[%s]]></ToUserName>"
            + "<FromUserName><![CDATA[%s]]></FromUserName>"
            + "<CreateTime>%d</CreateTime>"
            + "<MsgType><![CDATA[image]]></MsgType>"
            + "<Image>"
            + " <MediaId><![CDATA[%s]]></MediaId>"
            + "</Image>"
            + "</xml>";
        String format = String
            .format(xml, imageRespMsg.getToUserName(), imageRespMsg.getFromUserName(),
                imageRespMsg.getCreateTime(),
                imageRespMsg.getMediaId());

        log.info("发送的图片消息：{}", format);
        response.setContentType("application/xml; charset=utf-8");
        response.getWriter().print(format);
    }

    /**
     * 音乐消息对象转换成xml
     *
     * @param musicMessage 音乐消息对象
     * @return xml
     */
    public static void musicMessageToXml(WeChatMusicRespMsg musicMessage,
        HttpServletResponse response) {
    }

    /**
     * 图文消息对象转换成xml
     *
     * @param newsMessage 图文消息对象
     * @return xml
     */
    public static void newsMessageToXml(WeChatNewsRespMsg newsMessage,
        HttpServletResponse response) throws IOException {

    }
}
