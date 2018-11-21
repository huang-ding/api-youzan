package com.saopay.apiyouzan.util.youzan;

import com.fasterxml.jackson.core.type.TypeReference;
import com.saopay.apiyouzan.enums.RedisKeyEnum;
import com.saopay.apiyouzan.redis.RedisUtil;
import com.youzan.open.sdk.client.auth.Token;
import com.youzan.open.sdk.client.core.DefaultYZClient;
import com.youzan.open.sdk.client.core.YZClient;
import com.youzan.open.sdk.client.oauth.model.OAuthToken;
import com.youzan.open.sdk.util.http.DefaultHttpClient;
import com.youzan.open.sdk.util.http.HttpClient;
import com.youzan.open.sdk.util.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author huangding
 * @description
 * @date 2018/11/13 17:08
 */
@Component
@Slf4j
public class YouZanBaseUtil {


    @Autowired
    private RedisUtil redisUtil;

    private static String clientId;
    private static String clientSecret;
    private static String kdtId;

    public static OAuthToken getToken() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpClient.Params params = HttpClient.Params.custom()
            .add("client_id", clientId)
            .add("client_secret", clientSecret)
            .add("grant_type", "silent")
            .add("kdt_id", kdtId)
            .setContentType(ContentType.APPLICATION_FORM_URLENCODED).build();
        String resp = httpClient.post("https://open.youzan.com/oauth/token", params);
        log.info(resp);
        if (StringUtils.isBlank(resp) || !resp.contains("access_token")) {
            throw new RuntimeException(resp);
        }
        return JsonUtils.toBean(resp, new TypeReference<OAuthToken>() {
        });
    }

    /**
     * 好像没有看到有赞有返回refresh_token 次方法暂时放弃
     */
    @Deprecated
    public static OAuthToken getToken(String refresh_token) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpClient.Params params = HttpClient.Params.custom()
            .add("client_id", clientId)
            .add("client_secret", clientSecret)
            .add("grant_type", "refresh_token")
            .add("kdt_id", kdtId)
            .add("refresh_token", refresh_token)
            .setContentType(ContentType.APPLICATION_FORM_URLENCODED).build();
        String resp = httpClient.post("https://open.youzan.com/oauth/token", params);
        System.out.println(resp);
        if (StringUtils.isBlank(resp) || !resp.contains("access_token")) {
            throw new RuntimeException(resp);
        }
        return JsonUtils.toBean(resp, new TypeReference<OAuthToken>() {
        });
    }


    public YZClient getClient() {
        YZClient client = new DefaultYZClient(new Token(getAccessToken()));
        return client;
    }

    /**
     * 获取有赞AccessToken 有效期7天
     */
    public String getAccessToken() {
        return redisUtil
            .get(RedisKeyEnum.YZ_ACCESS_TOKEN, () -> {
///                    String refreshToken = redisUtil.get(RedisKeyEnum.REFRESH_TOKEN);
                OAuthToken authToken = getToken();
///                    if (StringUtils.isBlank(refreshToken)) {
///                        authToken = getToken();
///                    } else {
///                        authToken = getToken(refreshToken);
///                    }
///                    redisUtil.set(RedisKeyEnum.REFRESH_TOKEN, authToken.getRefreshToken(),28 * 24 * 3600 - 200);
                //刷新令牌 28天
                return authToken.getAccessToken();
            }, 8 * 24 * 3600 - 200, String.class);
    }

    @Value("${youzan.client_id}")
    public void setClientId(String clientId) {
        YouZanBaseUtil.clientId = clientId;
    }

    @Value("${youzan.client_secret}")
    public void setClientSecret(String clientSecret) {
        YouZanBaseUtil.clientSecret = clientSecret;
    }

    @Value("${youzan.kdt_id}")
    public void setKdtId(String kdtId) {
        YouZanBaseUtil.kdtId = kdtId;
    }
}
