package com.saopay.apiyouzan.util.http;

import java.util.Iterator;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @author huangding
 * @description
 * @date 2018/11/3 15:15
 */
@Slf4j
public class OkHttpUtil {

    /**
     * get
     *
     * @param url 请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     */
    public static String get(String url, Map<String, String> queries) {
        String responseBody = "";
        StringBuffer sb = new StringBuffer(url);
        getQuer(queries, sb);
        Request request = new Request.Builder()
            .url(sb.toString())
            .build();
        Response response = null;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.error("okhttp3 put error >> ex = {}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return responseBody;
    }


    private static void getQuer(Map<String, String> queries, StringBuffer sb) {
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            Iterator iterator = queries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry<String, String>) iterator.next();
                if (firstFlag) {
                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
    }

    /**
     * post
     *
     * @param url 请求的url
     * @param params post form 提交的参数
     */
    public static String post(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Request request = new Request.Builder()
            .url(url)
            .post(builder.build())
            .build();
        return responseHandle(request);
    }

    /**
     * get
     *
     * @param url 请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     */
    public static String getForHeader(String url, Map<String, String> queries, Map<String, String> header) {
        StringBuffer sb = new StringBuffer(url);
        getQuer(queries, sb);
        Request request = new Request.Builder()
            .headers(Headers.of(header))
            .url(sb.toString())
            .build();
        return responseHandle(request);
    }

    /**
     * Post请求发送JSON数据....{"name":"zhangsan","pwd":"123456"} 参数一：请求Url 参数二：请求的JSON
     */
    public static String postJsonParams(String url, String jsonParams) {
        RequestBody requestBody = RequestBody
            .create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
        Request request = new Request.Builder()
            .url(url)
            .post(requestBody)
            .build();
        return responseHandle(request);
    }

    /**
     * Post请求发送JSON数据....{"name":"zhangsan","pwd":"123456"} 参数一：请求Url 参数二：请求的JSON 参数三：header
     */
    public static String postJsonParams(String url, String jsonParams, Map<String, String> header) {

        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType, jsonParams);
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .headers(Headers.of(header))
            .build();
        return responseHandle(request);
    }



    /**
     * Post请求发送xml数据.... 参数一：请求Url 参数二：请求的xmlString 参数三：请求回调
     */
    public static String postXmlParams(String url, String xml) {
        RequestBody requestBody = RequestBody
            .create(MediaType.parse("application/xml; charset=utf-8"), xml);
        Request request = new Request.Builder()
            .url(url)
            .post(requestBody)
            .build();
        return responseHandle(request);

    }


    private static String responseHandle(Request request) {
        Response response = null;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.error("okhttp3 post error >> ex = {}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return StringUtils.EMPTY;
    }


}
