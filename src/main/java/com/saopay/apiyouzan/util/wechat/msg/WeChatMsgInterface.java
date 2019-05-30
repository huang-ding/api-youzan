package com.saopay.apiyouzan.util.wechat.msg;

import com.saopay.apiyouzan.util.wechat.msg.bo.WeChatBaseRespMsg;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huangding
 * @description
 * @date 2018/11/28 11:17
 */
public interface WeChatMsgInterface {

    /**
     * 发送邀请码
     */
    void sendInvitationCode(WeChatBaseRespMsg weChatBaseRespMsg, HttpServletResponse response)
        throws IOException;

    /**
     * 扫码反馈
     *
     * @param eventKey 微信二维码场景id
     */
    void sendEventScan(WeChatBaseRespMsg weChatBaseRespMsg,
        String eventKey, HttpServletResponse response) throws IOException;

}
