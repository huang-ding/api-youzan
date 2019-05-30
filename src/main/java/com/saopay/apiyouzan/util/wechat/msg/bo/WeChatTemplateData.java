package com.saopay.apiyouzan.util.wechat.msg.bo;

import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huangding
 * @description
 * @date 2018/11/28 14:10
 */
@NoArgsConstructor
@Data
public class WeChatTemplateData {

    /**
     * 接收者openid
     */
    private String touser;
    /**
     * 模板ID
     */
    private String template_id;
    /**
     * 模板跳转链接（海外帐号没有跳转能力）
     */
    private String url;
    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据
     */
    private MiniprogramBean miniprogram;
    /**
     * 模板数据
     */
    private Map<String, DataBean> data;

    @NoArgsConstructor
    @Data
    public static class MiniprogramBean {

        /**
         * 所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系，暂不支持小游戏）
         */
        private String appid;
        /**
         * 所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar），暂不支持小游戏
         */
        private String pagepath;
    }

    @NoArgsConstructor
    @Data
    public static class DataBean {

        /**
         * value : 恭喜你购买成功！ color : #173177
         */

        private String value;
        private String color;
    }
}
